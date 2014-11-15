package org.endeavor.game.entity.pathfinding;

import java.util.ArrayList;
import java.util.List;

import org.endeavor.engine.cache.map.Region;
import org.endeavor.engine.utility.Benchmarker;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

/**
 * The 317 client's path finding.
 * 
 * @author Jagex
 * 
 */
public class RS317PathFinder {

	/**
	 * The region for the current pathfinding attempt (Only one pathfinding
	 * attempt should be running at any single moment)
	 */
	private static Region r = null;

	private static final int DEFAULT_PATH_LENGTH = 4000;
	
	public static int getClip(int x, int y, int z) {
		if (r == null || !r.withinRegion(x, y)) {
			r = Region.getRegion(x, y);
			
			if (r == null) {
				return 0;
			}
		}

		return r.getClip(x, y, z);
	}

	public static void findRoute(Player c, int destX, int destY, boolean moveNear, int xLength, int yLength) {
		Benchmarker.start();
		if (destX == c.getLocation().getLocalX() && destY == c.getLocation().getLocalY() && !moveNear) {
			c.getClient().queueOutgoingPacket(new SendMessage("ERROR WITH PATHFINDING"));
			return;
		}

		destX = destX - (c.getLocation().getRegionX() << 3);
		destY = destY - (c.getLocation().getRegionY() << 3);

		int[][] via = new int[104][104];
		int[][] cost = new int[104][104];

		ArrayList<Integer> tileQueueX = new ArrayList<Integer>(9000);
		ArrayList<Integer> tileQueueY = new ArrayList<Integer>(9000);

		int curX = c.getLocation().getLocalX();
		int curY = c.getLocation().getLocalY();
		via[curX][curY] = 99;
		cost[curX][curY] = 1;
		int tail = 0;
		tileQueueX.add(curX);
		tileQueueY.add(curY);
		
		final int regionX = c.getLocation().getRegionX() << 3;
		final int regionY = c.getLocation().getRegionY() << 3;

		boolean foundPath = false;
		int pathLength = 4000;

		while (tail != tileQueueX.size() && tileQueueX.size() < pathLength) {

			curX = tileQueueX.get(tail);
			curY = tileQueueY.get(tail);

			int curAbsX = (regionX) + curX;
			int curAbsY = (regionY) + curY;

			if (curX == destX && curY == destY) {
				foundPath = true;
				break;
			}

			tail = (tail + 1) % pathLength;

			int thisCost = cost[curX][curY] + 1 + 1;

			if (curY > 0 && via[curX][curY - 1] == 0 && (getClip(curAbsX, curAbsY - 1, c.getLocation().getZ()) & 0x1280102) == 0) {
				tileQueueX.add(curX);
				tileQueueY.add(curY - 1);
				via[curX][curY - 1] = 1;
				cost[curX][curY - 1] = thisCost;
			}

			if (curX > 0 && via[curX - 1][curY] == 0 && (getClip(curAbsX - 1, curAbsY, c.getLocation().getZ()) & 0x1280108) == 0) {
				tileQueueX.add(curX - 1);
				tileQueueY.add(curY);
				via[curX - 1][curY] = 2;
				cost[curX - 1][curY] = thisCost;
			}

			if (curY < 104 - 1 && via[curX][curY + 1] == 0 && (getClip(curAbsX, curAbsY + 1, c.getLocation().getZ()) & 0x1280120) == 0) {
				tileQueueX.add(curX);
				tileQueueY.add(curY + 1);
				via[curX][curY + 1] = 4;
				cost[curX][curY + 1] = thisCost;
			}

			if (curX < 104 - 1 && via[curX + 1][curY] == 0 && (getClip(curAbsX + 1, curAbsY, c.getLocation().getZ()) & 0x1280180) == 0) {
				tileQueueX.add(curX + 1);
				tileQueueY.add(curY);
				via[curX + 1][curY] = 8;
				cost[curX + 1][curY] = thisCost;
			}

			if (curX > 0 && curY > 0 && via[curX - 1][curY - 1] == 0 && (getClip(curAbsX - 1, curAbsY - 1, c.getLocation().getZ()) & 0x128010e) == 0
					&& (getClip(curAbsX - 1, curAbsY, c.getLocation().getZ()) & 0x1280108) == 0
					&& (getClip(curAbsX, curAbsY - 1, c.getLocation().getZ()) & 0x1280102) == 0) {
				tileQueueX.add(curX - 1);
				tileQueueY.add(curY - 1);
				via[curX - 1][curY - 1] = 3;
				cost[curX - 1][curY - 1] = thisCost;
			}

			if (curX > 0 && curY < 104 - 1 && via[curX - 1][curY + 1] == 0 && (getClip(curAbsX - 1, curAbsY + 1, c.getLocation().getZ()) & 0x1280138) == 0
					&& (getClip(curAbsX - 1, curAbsY, c.getLocation().getZ()) & 0x1280108) == 0
					&& (getClip(curAbsX, curAbsY + 1, c.getLocation().getZ()) & 0x1280120) == 0) {
				tileQueueX.add(curX - 1);
				tileQueueY.add(curY + 1);
				via[curX - 1][curY + 1] = 6;
				cost[curX - 1][curY + 1] = thisCost;
			}

			if (curX < 104 - 1 && curY > 0 && via[curX + 1][curY - 1] == 0 && (getClip(curAbsX + 1, curAbsY - 1, c.getLocation().getZ()) & 0x1280183) == 0
					&& (getClip(curAbsX + 1, curAbsY, c.getLocation().getZ()) & 0x1280180) == 0
					&& (getClip(curAbsX, curAbsY - 1, c.getLocation().getZ()) & 0x1280102) == 0) {
				tileQueueX.add(curX + 1);
				tileQueueY.add(curY - 1);
				via[curX + 1][curY - 1] = 9;
				cost[curX + 1][curY - 1] = thisCost;
			}

			if (curX < 104 - 1 && curY < 104 - 1 && via[curX + 1][curY + 1] == 0 && (getClip(curAbsX + 1, curAbsY + 1, c.getLocation().getZ()) & 0x12801e0) == 0
					&& (getClip(curAbsX + 1, curAbsY, c.getLocation().getZ()) & 0x1280180) == 0
					&& (getClip(curAbsX, curAbsY + 1, c.getLocation().getZ()) & 0x1280120) == 0) {
				tileQueueX.add(curX + 1);
				tileQueueY.add(curY + 1);
				via[curX + 1][curY + 1] = 12;
				cost[curX + 1][curY + 1] = thisCost;
			}
		}

		if (!foundPath) {
			if (moveNear) {

				int i_223_ = 1000;
				int thisCost = 100 + 1;
				int i_225_ = 10;

				for (int x = destX - i_225_; x <= destX + i_225_; x++) {
					for (int y = destY - i_225_; y <= destY + i_225_; y++) {
						if (x >= 0 && y >= 0 && x < 104 && y < 104 && cost[x][y] < 100 && cost[x][y] != 0) {
							int i_228_ = 0;
							if (x < destX) {
								i_228_ = destX - x;
							} else if (x > destX + xLength - 1) {
								i_228_ = x - (destX + xLength - 1);
							}
							int i_229_ = 0;
							if (y < destY) {
								i_229_ = destY - y;
							} else if (y > destY + yLength - 1) {
								i_229_ = y - (destY + yLength - 1);
							}
							int i_230_ = i_228_ * i_228_ + i_229_ * i_229_;
							if (i_230_ < i_223_ || i_230_ == i_223_ && cost[x][y] < thisCost && cost[x][y] != 0) {
								i_223_ = i_230_;
								thisCost = cost[x][y];
								curX = x;
								curY = y;
							}
						}
					}
				}

				if (i_223_ == 1000) {
					return;
				}

			} else {
				return;
			}
		}

		tail = 0;
		tileQueueX.set(tail, curX);
		tileQueueY.set(tail++, curY);
		int l5;

		for (int j5 = l5 = via[curX][curY]; curX != c.getLocation().getLocalX() || curY != c.getLocation().getLocalY(); j5 = via[curX][curY]) {
			if (j5 != l5) {
				l5 = j5;
				tileQueueX.set(tail, curX);
				tileQueueY.set(tail++, curY);
			}
			if ((j5 & 2) != 0) {
				curX++;
			} else if ((j5 & 8) != 0) {
				curX--;
			}
			if ((j5 & 1) != 0) {
				curY++;
			} else if ((j5 & 4) != 0) {
				curY--;
			}
		}

		c.getMovementHandler().reset();

		int size = tail--;
		int pathX = (regionX) + tileQueueX.get(tail);
		int pathY = (regionY) + tileQueueY.get(tail);

		c.getMovementHandler().addToPath(new Location(pathX, pathY));

		for (int i = 1; i < size; i++) {
			tail--;
			pathX = (regionX) + tileQueueX.get(tail);
			pathY = (regionY) + tileQueueY.get(tail);
			c.getMovementHandler().addToPath(new Location(pathX, pathY));
		}

		c.getMovementHandler().finish();
		
		Benchmarker.stop(0);
	}

	public static boolean accessable(int x, int y, int z, int destX, int destY) {
		Location p = new Location(x, y, z);
		if (destX == p.getLocalX() && destY == p.getLocalY()) {
			return false;
		}

		int[][] via = new int[104][104];
		int[][] cost = new int[104][104];

		List<Integer> tileQueueX = new ArrayList<Integer>(10000);
		List<Integer> tileQueueY = new ArrayList<Integer>(10000);

		int curX = p.getLocalX();
		int curY = p.getLocalY();
		via[curX][curY] = 99;
		cost[curX][curY] = 1;
		int tail = 0;
		tileQueueX.add(curX);
		tileQueueY.add(curY);
		
		final int regionX = p.getRegionX() << 3;
		final int regionY = p.getRegionY() << 3;
		
		destX = destX - regionX;
		destY = destY - regionY;

		while (tail != tileQueueX.size() && tileQueueX.size() < DEFAULT_PATH_LENGTH) {

			curX = tileQueueX.get(tail);
			curY = tileQueueY.get(tail);

			int curAbsX = regionX + curX;
			int curAbsY = regionY + curY;

			if (curX == destX && curY == destY) {
				return true;
			}

			tail = (tail + 1) % DEFAULT_PATH_LENGTH;

			int thisCost = cost[curX][curY] + 1 + 1;

			if (curY > 0 && via[curX][curY - 1] == 0 && (getClip(curAbsX, curAbsY - 1, z) & 0x1280102) == 0) {
				tileQueueX.add(curX);
				tileQueueY.add(curY - 1);
				via[curX][curY - 1] = 1;
				cost[curX][curY - 1] = thisCost;
			}

			if (curX > 0 && via[curX - 1][curY] == 0 && (getClip(curAbsX - 1, curAbsY, z) & 0x1280108) == 0) {
				tileQueueX.add(curX - 1);
				tileQueueY.add(curY);
				via[curX - 1][curY] = 2;
				cost[curX - 1][curY] = thisCost;
			}

			if (curY < 104 - 1 && via[curX][curY + 1] == 0 && (getClip(curAbsX, curAbsY + 1, z) & 0x1280120) == 0) {
				tileQueueX.add(curX);
				tileQueueY.add(curY + 1);
				via[curX][curY + 1] = 4;
				cost[curX][curY + 1] = thisCost;
			}

			if (curX < 104 - 1 && via[curX + 1][curY] == 0 && (getClip(curAbsX + 1, curAbsY, z) & 0x1280180) == 0) {
				tileQueueX.add(curX + 1);
				tileQueueY.add(curY);
				via[curX + 1][curY] = 8;
				cost[curX + 1][curY] = thisCost;
			}

			if (curX > 0 && curY > 0 && via[curX - 1][curY - 1] == 0 && (getClip(curAbsX - 1, curAbsY - 1, z) & 0x128010e) == 0
					&& (getClip(curAbsX - 1, curAbsY, z) & 0x1280108) == 0
					&& (getClip(curAbsX, curAbsY - 1, z) & 0x1280102) == 0) {
				tileQueueX.add(curX - 1);
				tileQueueY.add(curY - 1);
				via[curX - 1][curY - 1] = 3;
				cost[curX - 1][curY - 1] = thisCost;
			}

			if (curX > 0 && curY < 104 - 1 && via[curX - 1][curY + 1] == 0 && (getClip(curAbsX - 1, curAbsY + 1, z) & 0x1280138) == 0
					&& (getClip(curAbsX - 1, curAbsY, z) & 0x1280108) == 0
					&& (getClip(curAbsX, curAbsY + 1, z) & 0x1280120) == 0) {
				tileQueueX.add(curX - 1);
				tileQueueY.add(curY + 1);
				via[curX - 1][curY + 1] = 6;
				cost[curX - 1][curY + 1] = thisCost;
			}

			if (curX < 104 - 1 && curY > 0 && via[curX + 1][curY - 1] == 0 && (getClip(curAbsX + 1, curAbsY - 1, z) & 0x1280183) == 0
					&& (getClip(curAbsX + 1, curAbsY, z) & 0x1280180) == 0
					&& (getClip(curAbsX, curAbsY - 1, z) & 0x1280102) == 0) {
				tileQueueX.add(curX + 1);
				tileQueueY.add(curY - 1);
				via[curX + 1][curY - 1] = 9;
				cost[curX + 1][curY - 1] = thisCost;
			}

			if (curX < 104 - 1 && curY < 104 - 1 && via[curX + 1][curY + 1] == 0 && (getClip(curAbsX + 1, curAbsY + 1, z) & 0x12801e0) == 0
					&& (getClip(curAbsX + 1, curAbsY, z) & 0x1280180) == 0
					&& (getClip(curAbsX, curAbsY + 1, z) & 0x1280120) == 0) {
				tileQueueX.add(curX + 1);
				tileQueueY.add(curY + 1);
				via[curX + 1][curY + 1] = 12;
				cost[curX + 1][curY + 1] = thisCost;
			}
		}

		return false;
	}

	public int localize(int x, int mapRegion) {
		return x - (mapRegion << 3);
	}
}
