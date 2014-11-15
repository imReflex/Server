package org.endeavor.game.entity.item.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.endeavor.engine.cache.map.Region;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;

public class GroundItemHandler {
	
	public static final int SHOW_GROUND_ITEM = 100;
	public static final int REMOVE_GROUND_ITEM = 350;
	public static final int MAX_GLOBALIZATION = 10;
	public static final int MAX_REMOVAL = 10;
	private static final List<GroundItem> active = new LinkedList<GroundItem>();
	private static final List<GroundItem> globalizeQueue = new LinkedList<GroundItem>();

	public static void process() {
		synchronized (active) {
			for (Iterator<GroundItem> i = active.iterator(); i.hasNext();) {
				GroundItem groundItem = (GroundItem) i.next();

				groundItem.countdown();

				if (groundItem.globalize()) {
					globalize(groundItem);
				}

				if (groundItem.remove()) {
					groundItem.erase();

					if (!groundItem.isGlobal()) {
						Player owner = groundItem.getOwner();

						if ((owner != null) && (visible(owner, groundItem)))
							owner.getGroundItems().remove(groundItem);
					} else {
						for (int k = 1; k < World.getPlayers().length; k++) {
							Player player = World.getPlayers()[k];

							if (player != null) {
								if (visible(player, groundItem)) {
									player.getGroundItems().remove(groundItem);
								}
							}
						}
					}
					i.remove();
				}
			}
		}

		for (Iterator<GroundItem> i = globalizeQueue.iterator(); i.hasNext();) {
			GroundItem groundItem = (GroundItem) i.next();

			if (!groundItem.exists()) {
				i.remove();
			} else {
				groundItem.setGlobal(true);

				Player owner = groundItem.getOwner();

				for (int k = 1; k < World.getPlayers().length; k++) {
					Player player = World.getPlayers()[k];

					if ((player != null) && ((owner == null) || (!player.equals(owner)))) {
						if (visible(player, groundItem)) {
							player.getGroundItems().add(groundItem);
						}
					}
				}
				i.remove();
			}
		}
	}

	public static boolean add(Item item, Location location, Player player, int time) {
		GroundItem groundItem = new GroundItem(item, new Location(location), player == null ? null : player.getUsername());

		if (time >= 0) {
			groundItem.setTime(time);
		}

		return add(groundItem);
	}

	public static boolean add(Item item, Location location, Player player) {
		GroundItem groundItem = new GroundItem(item, new Location(location), player == null ? null : player.getUsername());
		add(groundItem);
		return true;
	}

	public static boolean add(GroundItem groundItem) {
		Player owner = groundItem.getOwner();

		if ((owner != null) && (owner.getGroundItems().stack(groundItem))) {
			return true;
		}

		if ((owner != null) && (visible(owner, groundItem))) {
			owner.getGroundItems().add(groundItem);
		}

		active.add(groundItem);

		return true;
	}

	public static boolean remove(GroundItem groundItem) {
		if (groundItem.isGlobal) {
			GlobalItemHandler.createRespawnTask(groundItem);
		}
		groundItem.erase();

		if (!groundItem.isGlobal()) {
			Player owner = groundItem.getOwner();

			if ((owner != null) && (visible(owner, groundItem)))
				owner.getGroundItems().remove(groundItem);
		} else {
			for (int k = 1; k < World.getPlayers().length; k++) {
				Player player = World.getPlayers()[k];

				if (player != null) {
					if (visible(player, groundItem)) {
						player.getGroundItems().remove(groundItem);
					}
				}
			}
		}
		active.remove(groundItem);

		return true;
	}

	/**
	 * name = player username your searching for
	 * specific = if this item is owned by this player
	 */
	public static GroundItem getGroundItem(int id, int x, int y, int z, String name, boolean specific) {
		long longAsName = name == null ? -1 : Misc.nameToLong(name);

		Location l = new Location(x, y, z);

		for (Iterator<GroundItem> i = active.iterator(); i.hasNext();) {
			GroundItem g = (GroundItem) i.next();

			if (g.getLocation().equals(l) && g.exists()) {
				if (longAsName != -1 && longAsName == g.getLongOwnerName() && g.getItem().getId() == id 
						|| !specific && g.isGlobal() && g.getItem().getId() == id) {
					return g;
				}
			}
		}

		return null;
	}

	public static GroundItem getNonGlobalGroundItem(int id, int x, int y, int z, long name) {
		Location l = new Location(x, y, z);

		for (Iterator<GroundItem> i = active.iterator(); i.hasNext();) {
			GroundItem g = (GroundItem) i.next();

			if ((g.getLocation().equals(l)) && (!g.isGlobal()) && (g.exists())) {
				if ((g.getLongOwnerName() == name) && (g.getItem().getId() == id)) {
					return g;
				}
			}
		}
		return null;
	}

	public static void globalize(GroundItem groundItem) {
		globalizeQueue.add(groundItem);
	}

	public static boolean visible(Player player, GroundItem groundItem) {
		Player owner = groundItem.getOwner();

		if ((player.withinRegion(groundItem.getLocation()))
				&& (player.getLocation().getZ() == groundItem.getLocation().getZ())
				&& ((groundItem.isGlobal()) || ((owner != null) && (player.equals(owner))))) {
			return true;
		}

		return false;
	}

	public static boolean exists(GroundItem g) {
		return active.contains(g);
	}

	public static Region getRegion(GroundItem groundItem) {
		return Region.getRegion(groundItem.getLocation().getX(), groundItem.getLocation().getY());
	}

	public static List<GroundItem> getActive() {
		return active;
	}
}
