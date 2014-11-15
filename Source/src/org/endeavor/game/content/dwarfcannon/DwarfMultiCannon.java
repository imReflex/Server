package org.endeavor.game.content.dwarfcannon;

import org.endeavor.engine.cache.map.Region;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.task.impl.forcewalk.ForceMovementTask;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class DwarfMultiCannon {
	public static final int CANNONBALL_ITEM_ID = 2;
	public static final int BASE_ITEM_ID = 6;
	public static final int STAND_ITEM_ID = 8;
	public static final int BARRELS_ITEM_ID = 10;
	public static final int FURNACE_ITEM_ID = 12;
	public static final int DWARF_MULTI_CANNON_OBJECT_ID = 6;
	public static final int CANNON_BASE_OBJECT_ID = 7;
	public static final int CANNON_STAND_OBJECT_ID = 8;
	public static final int CANNON_BARRELS_OBJECT_ID = 9;
	public static final String CANNON_ATTRIBUTE_KEY = "dwarfmulticannon";

	public static final boolean setCannonBase(Player player, int id) {
		if (id == 6) {
			if (isCannonSetupClear(player)) {
				final Location l = new Location(player.getLocation());
				TaskQueue.queue(new ForceMovementTask(player, new Location(l.getX() - 2, l.getY()),
						ControllerManager.DEFAULT_CONTROLLER) {
					/**
							 * 
							 */
							private static final long serialVersionUID = -3786933598736695914L;

					@Override
					public void onDestination() {
						if (player.getInventory().hasItemId(6)) {
							player.getInventory().remove(6);
							player.getAttributes().set("dwarfmulticannon",
									new Cannon(player, l.getX(), l.getY(), l.getZ()));
						}
					}
				});
			} else {
				player.getClient().queueOutgoingPacket(new SendMessage("You cannot setup your cannon here."));
			}

			return true;
		}

		return false;
	}

	public static final boolean isCannonSetupClear(Player player) {
		if (!player.getController().equals(ControllerManager.DEFAULT_CONTROLLER)) {
			return false;
		}

		int x = player.getLocation().getX();
		int y = player.getLocation().getY();
		int z = player.getLocation().getZ();

		Region r = Region.getRegion(x, y);

		for (int i = 0; i < 8; i++) {
			if (!r.canMove(x, y, z, i)) {
				return false;
			}
		}

		int x2 = x + org.endeavor.game.GameConstants.DIR[3][0];
		int y2 = y + org.endeavor.game.GameConstants.DIR[3][1];

		if (!Region.getRegion(x2, y2).canMove(x2, y2, z, 3)) {
			return false;
		}

		return true;
	}

	public static boolean hasCannon(Player player) {
		return player.getAttributes().get("dwarfmulticannon") != null;
	}

	public static Cannon getCannon(Player player) {
		return (Cannon) player.getAttributes().get("dwarfmulticannon");
	}
}
