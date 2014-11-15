package org.endeavor.engine.cache.map;

import static org.endeavor.game.GameConstants.DIR;

import java.util.ArrayList;

import org.endeavor.engine.utility.Misc;
import org.endeavor.engine.utility.NameUtil;
import org.endeavor.game.GameConstants;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.pathfinding.RS317PathFinder;
import org.endeavor.game.entity.player.Player;

public class Ladder extends RSObject {
	
	private final Location location;
	
	private final long[] options;
	
	private final Location[] toLocations;
	
	private final LadderType ladderType;
	
	public enum LadderType {
		UP, DOWN, EITHER
	}
	
	public static final long CLIMB_LONG = NameUtil.nameToLong("Climb");
	public static final long CLIMB_UP_LONG = NameUtil.nameToLong("Climb-up");
	public static final long CLIMB_DOWN_LONG = NameUtil.nameToLong("Climb-down");
	
	public Ladder(int id, int x, int y, int z, int type, int direction) {
		super(x, y, z, id, type, direction);
		this.location = new Location(x, y, z);
		
		ObjectDef d = ObjectDef.getObjectDef(id);
		
		int optionAmount = 0;
		
		for (String i : d.actions) {
			if (i != null) {
				optionAmount++;
			}
		}
		
		options = new long[optionAmount];
		toLocations = new Location[optionAmount];
		
		int index = 0;
		
		for (String i : d.actions) {
			if (i != null) {
				options[index] = NameUtil.nameToLong(i.toLowerCase());
				index++;
			}
		}
		
		this.ladderType = options.length == 3 ? LadderType.EITHER : options[0] == CLIMB_UP_LONG ? LadderType.UP : options[0] == CLIMB_DOWN_LONG ? LadderType.DOWN : null;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof RSObject) {
			RSObject l = (RSObject) o;
			
			return l.getX() == getX() && l.getY() == getY() && l.getZ() == getZ();
		}
		
		return false;
	}
	
	public void click(Player player, int option) {
		if (option - 1 >= options.length) {
			return;
		}
		
		System.out.println("click");
		
		System.out.println(ladderType + "");
		
		if (options[option - 1] == CLIMB_LONG) {
			//send climb dialogue
		} else {
			if (toLocations[option - 1] != null) {
				player.teleport(toLocations[option - 1]);
			}
		}
	}
	
	public void assign(long id, Location location) {
		for (int i = 0; i < options.length; i++) {
			if (options[i] == id) {
				toLocations[i] = location;
				return;
			}
		}
	}
	
	public void create() {
		if (ladderType == LadderType.EITHER) {
			if (options[1] == CLIMB_UP_LONG) {
				toLocations[0] = null;
				
				if (location.getZ() < 3) {
					toLocations[1] = getClearLocation(id, location.getX(), location.getY(), location.getZ() + 1);
				}
				
				if (location.getZ() > 0) {
					toLocations[2] = getClearLocation(id, location.getX(), location.getY(), location.getZ() - 1);
				}
			} else {
				toLocations[0] = null;
				
				if (location.getZ() > 0) {
					toLocations[1] = getClearLocation(id, location.getX(), location.getY(), location.getZ() - 1);
				}
				
				if (location.getZ() < 3) {
					toLocations[2] = getClearLocation(id, location.getX(), location.getY(), location.getZ() + 1);
				}
				
			}
		} else if (ladderType == LadderType.UP) {
			if (location.getZ() < 3) {
				toLocations[0] = getClearLocation(id, location.getX(), location.getY(), location.getZ() + 1);
			}
		} else if (ladderType == LadderType.DOWN && location.getZ() > 0) {
			if (location.getZ() > 0) {
				toLocations[0] = getClearLocation(id, location.getX(), location.getY(), location.getZ() - 1);
			}
		} else {
			toLocations[0] = getClearLocation(id, location.getX(), location.getY() + 6400, location.getZ());
		}
	}
	
	public RSObject getNearestClimbObject(int x, int y, int z) {
		int lowd = 9997899;
		RSObject closest = null;
		
		if (Region.getRegion(x, y) == null) {
			return null;
		}
		
		if (Region.getRegion(x, y).getObjects()[z] == null) {
			return null;
		}
		
		for (RSObject[] i : Region.getRegion(x, y).getObjects()[z]) {
			for (RSObject object : i) {
				if (object != null && isLadder(ObjectDef.getObjectDef(object.getId()))) {
					if (lowd < Misc.getManhattanDistance(x, y, object.getX(), object.getY()) || closest == null) {
						lowd = Misc.getManhattanDistance(x, y, object.getX(), object.getY());
						closest = object;
					}
				}
			}
		}
		
		/**
		 * +x
		 */
		for (int j = 0; j < 7; j++) {
			if (!Region.getRegion(x, y).withinRegion(x, y + j)) {
				if (Region.getRegion(x, y + j) == null) {
					continue;
				}
				
				if (Region.getRegion(x, y + j).getObjects()[z] == null) {
					continue;
				}
				
				for (RSObject[] i : Region.getRegion(x, y + j).getObjects()[z]) {
					for (RSObject object : i) {
						if (object != null && isLadder(ObjectDef.getObjectDef(object.getId()))) {
							if (lowd < Misc.getManhattanDistance(x, y, object.getX(), object.getY()) || closest == null) {
								lowd = Misc.getManhattanDistance(x, y, object.getX(), object.getY());
								closest = object;
							}
						}
					}
				}
			}
		}
		
		/**
		 * +y
		 */
		for (int j = 0; j < 7; j++) {
			if (!Region.getRegion(x, y).withinRegion(x + j, y)) {
				if (Region.getRegion(x + j, y) == null) {
					continue;
				}
				
				if (Region.getRegion(x + j, y).getObjects()[z] == null) {
					continue;
				}
				
				for (RSObject[] i : Region.getRegion(x + j, y).getObjects()[z]) {
					for (RSObject object : i) {
						if (object != null && isLadder(ObjectDef.getObjectDef(object.getId()))) {
							if (lowd < Misc.getManhattanDistance(x, y, object.getX(), object.getY()) || closest == null) {
								lowd = Misc.getManhattanDistance(x, y, object.getX(), object.getY());
								closest = object;
							}
						}
					}
				}
			}
		}
		
		/**
		 * -y
		 */
		for (int j = 0; j < 7; j++) {
			if (!Region.getRegion(x, y).withinRegion(x, y - j)) {
				if (Region.getRegion(x, y - j) == null) {
					continue;
				}
				
				if (Region.getRegion(x, y - j).getObjects()[z] == null) {
					continue;
				}
				
				for (RSObject[] i : Region.getRegion(x, y - j).getObjects()[z]) {
					for (RSObject object : i) {
						if (Region.getRegion(x, y - j) == null) {
							continue;
						}
						
						if (object != null && isLadder(ObjectDef.getObjectDef(object.getId()))) {
							if (lowd < Misc.getManhattanDistance(x, y, object.getX(), object.getY()) || closest == null) {
								lowd = Misc.getManhattanDistance(x, y, object.getX(), object.getY());
								closest = object;
							}
						}
					}
				}
			}
		}
		
		/**
		 * -x
		 */
		for (int j = 0; j < 7; j++) {
			if (!Region.getRegion(x, y).withinRegion(x - j, y)) {
				if (Region.getRegion(x - j, y) == null) {
					continue;
				}
				
				if (Region.getRegion(x - j, y).getObjects()[z] == null) {
					continue;
				}
				
				for (RSObject[] i : Region.getRegion(x - j, y).getObjects()[z]) {
					for (RSObject object : i) {
						if (object != null && isLadder(ObjectDef.getObjectDef(object.getId()))) {
							if (lowd < Misc.getManhattanDistance(x, y, object.getX(), object.getY()) || closest == null) {
								lowd = Misc.getManhattanDistance(x, y, object.getX(), object.getY());
								closest = object;
							}
						}
					}
				}
			}
		}
		
		
		
		return closest;
	}
	
	public Location getClearLocation(int id, int x, int y, int z) {
		int xLength;
		int yLength;
		
		int face = getFace();
		
		if (face != 1 && face != 3) {
			xLength = ObjectDef.getObjectDef(id).xLength();
			yLength = ObjectDef.getObjectDef(id).yLength();
		} else {
			xLength = ObjectDef.getObjectDef(id).yLength();
			yLength = ObjectDef.getObjectDef(id).xLength();
		}
		
		final ArrayList<Location> locs = new ArrayList<Location>();
		final ArrayList<Location> locsAlt = new ArrayList<Location>();
		
		//if (Region.getUnsortedRegion(x, y) != null) {
		
		RSObject closest = getNearestClimbObject(x, y, z);
		
		if (closest == null) {
			return null;
		}
		
		final int face2 = getFace();
		
		int xLength2;
		int yLength2;
		
		if (face2 != 1 && face2 != 3) {
			xLength2 = ObjectDef.getObjectDef(id).xLength();
			yLength2 = ObjectDef.getObjectDef(id).yLength();
		} else {
			xLength2 = ObjectDef.getObjectDef(id).yLength();
			yLength2 = ObjectDef.getObjectDef(id).xLength();
		}
		
		if (xLength == 1 && yLength == 1) {
			if (Region.getUnsortedRegion(x, y) != null) {
				for (int k = 0; k < DIR.length; k++) {
					if (Region.getUnsortedRegion(x, y).canMove(x, y, z, k)) {
						boolean clear = false;
						
						for (int j = 0; j < DIR.length; j++) {
							if (Region.getUnsortedRegion(x + DIR[k][0], y + DIR[k][1]).canMove(x + DIR[k][0], y + DIR[k][1], z, j)) {
								clear = true;
							}
						}
						
						if (!clear) {
							continue;
						}
						
						for (Location l : GameConstants.getBorder(closest.getX(), closest.getY(), xLength2, yLength2)) {
							if (l == null) {
								continue;
							}
							for (int t = 0; t < 8; t++) {
								if (RS317PathFinder.accessable(x + DIR[k][0], y + DIR[k][1], z, l.getX() + DIR[t][0], l.getY() + DIR[t][1])) {
									if (DIR[k][0] != 0 && DIR[k][1] != 0) {
										locsAlt.add(new Location(x + DIR[k][0], y + DIR[k][1], z));
									} else {
										locs.add(new Location(x + DIR[k][0], y + DIR[k][1], z));
									}
									
									break;
								}
							}
						}
					}
				}
			}
		} else {
			while (xLength > 0 || yLength > 0) {
			
				for (int i = 0; i < xLength - 1; i++) {
					x++;
					
					if (Region.getUnsortedRegion(x, y) == null) {
						continue;
					}
					
					for (int k = 0; k < DIR.length; k++) {
						if (Region.getUnsortedRegion(x, y).canMove(x, y, z, k)) {
							boolean clear = false;
							
							for (int j = 0; j < DIR.length; j++) {
								if (Region.getUnsortedRegion(x + DIR[k][0], y + DIR[k][1]).canMove(x + DIR[k][0], y + DIR[k][1], z, j)) {
									clear = true;
								}
							}
							
							if (!clear) {
								continue;
							}
							
							for (Location l : GameConstants.getBorder(closest.getX(), closest.getY(), xLength2, yLength2)) {
								if (l == null) {
									continue;
								}
								for (int t = 0; t < 8; t++) {
									if (RS317PathFinder.accessable(x + DIR[k][0], y + DIR[k][1], z, l.getX() + DIR[t][0], l.getY() + DIR[t][1])) {
										if (DIR[k][0] != 0 && DIR[k][1] != 0) {
											locsAlt.add(new Location(x + DIR[k][0], y + DIR[k][1], z));
										} else {
											locs.add(new Location(x + DIR[k][0], y + DIR[k][1], z));
										}
										
										break;
									}
								}
							}
						}
					}
				}
				
				for (int i = 0; i < yLength - 1; i++) {
					y++;
					
					if (Region.getUnsortedRegion(x, y) == null) {
						continue;
					}
					
					for (int k = 0; k < DIR.length; k++) {
						
						if (Region.getUnsortedRegion(x, y).canMove(x, y, z, k)) {
							boolean clear = false;
							
							for (int j = 0; j < DIR.length; j++) {
								if (Region.getUnsortedRegion(x + DIR[k][0], y + DIR[k][1]).canMove(x + DIR[k][0], y + DIR[k][1], z, j)) {
									clear = true;
								}
							}
							
							if (!clear) {
								continue;
							}
							
							for (Location l : GameConstants.getBorder(closest.getX(), closest.getY(), xLength2, yLength2)) {
								if (l == null) {
									continue;
								}
								
								for (int t = 0; t < 8; t++) {
									if (RS317PathFinder.accessable(x + DIR[k][0], y + DIR[k][1], z, l.getX() + DIR[t][0], l.getY() + DIR[t][1])) {
										if (DIR[k][0] != 0 && DIR[k][1] != 0) {
											locsAlt.add(new Location(x + DIR[k][0], y + DIR[k][1], z));
										} else {
											locs.add(new Location(x + DIR[k][0], y + DIR[k][1], z));
										}
										
										break;
									}
								}
							}
						}
					}
				}
				
				for (int i = 0; i < xLength - 1; i++) {
					x--;
					
					if (Region.getUnsortedRegion(x, y) == null) {
						continue;
					}
					
					for (int k = 0; k < DIR.length; k++) {
						
						if (Region.getUnsortedRegion(x, y).canMove(x, y, z, k)) {
							boolean clear = false;
							
							for (int j = 0; j < DIR.length; j++) {
								if (Region.getUnsortedRegion(x + DIR[k][0], y + DIR[k][1]).canMove(x + DIR[k][0], y + DIR[k][1], z, j)) {
									clear = true;
								}
							}
							
							if (!clear) {
								continue;
							}
							
							for (Location l : GameConstants.getBorder(closest.getX(), closest.getY(), xLength2, yLength2)) {
								if (l == null) {
									continue;
								}
								for (int t = 0; t < 8; t++) {
									if (RS317PathFinder.accessable(x + DIR[k][0], y + DIR[k][1], z, l.getX() + DIR[t][0], l.getY() + DIR[t][1])) {
										if (DIR[k][0] != 0 && DIR[k][1] != 0) {
											locsAlt.add(new Location(x + DIR[k][0], y + DIR[k][1], z));
										} else {
											locs.add(new Location(x + DIR[k][0], y + DIR[k][1], z));
										}
										
										break;
									}
								}
							}
						}
					}
				}
				
				for (int i = 0; i < yLength - 1; i++) {
					y--;
					
					if (Region.getUnsortedRegion(x, y) == null) {
						continue;
					}
					for (int k = 0; k < DIR.length; k++) {
						
						if (Region.getUnsortedRegion(x, y).canMove(x, y, z, k)) {
							boolean clear = false;
							
							for (int j = 0; j < DIR.length; j++) {
								if (Region.getUnsortedRegion(x + DIR[k][0], y + DIR[k][1]).canMove(x + DIR[k][0], y + DIR[k][1], z, j)) {
									clear = true;
								}
							}
							
							if (!clear) {
								continue;
							}
							
							for (Location l : GameConstants.getBorder(closest.getX(), closest.getY(), xLength2, yLength2)) {
								if (l == null) {
									continue;
								}
								for (int t = 0; t < 8; t++) {
									if (RS317PathFinder.accessable(x + DIR[k][0], y + DIR[k][1], z, l.getX() + DIR[t][0], l.getY() + DIR[t][1])) {
										if (DIR[k][0] != 0 && DIR[k][1] != 0) {
											locsAlt.add(new Location(x + DIR[k][0], y + DIR[k][1], z));
										} else {
											locs.add(new Location(x + DIR[k][0], y + DIR[k][1], z));
										}
										
										break;
									}
								}
							}
						}
					}
				}
				
				xLength--;
				yLength--;
			}
		}
		
		if (locs.size() == 0 && locsAlt.size() == 0) {
			return null;
		} else if (locs.size() == 1) {
			return locs.get(0);
		} else if (locsAlt.size() == 1 && locs.size() == 0) {
			return locsAlt.get(0);
		} else {
			if (locs.size() == 0) {
				return locsAlt.get(locsAlt.size() / 2);
			}
			
			return locs.get(locs.size() / 2);//aim for the middle
		}
	}
	
	public static boolean isLadder(ObjectDef def) {
		if (def.actions == null)
			return false;
		
		for (String i : def.actions) {
			if (i != null) {
				if (i.equals("Climb-down") || i.equals("Climb-up") || i.equals("Climb")) {
					return true;
				}
			}
		}
		
		return false;
	}
	
}
