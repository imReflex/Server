package org.endeavor.game.content.minigames.godwars;

import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendSound;

public class GodWars {
	public static final int GOD_WARS_BANDOS_ID = 0;
	public static final int GOD_WARS_ARMADYL_ID = 1;
	public static final int GOD_WARS_ZAMORAK_ID = 2;
	public static final int GOD_WARS_SARADOMIN_ID = 3;
	public static final String NOT_ENOUGH_KC = "You need more kills to enter this room.";
	public static final String GWD_ALTAR_KEY = "gwdaltarkey";

	public static void onGodwarsKill(Player player, int id) {
		int gwid = GodWarsNpcData.GodWarsNpcs.forId(id);

		if (gwid != -1) {
			player.getMinigames().incrGWKC(gwid);
			player.getAchievements().incr(player, "Achieve 1,000 total GWD KC");
		}
	}

	public static int getPointsToEnter(Player player) {
		return player.isRespectedMember() ? 25 : 40;
	}

	public static boolean clickObject(Player player, int id) {
		switch (id) {
		case 26286:
		case 26287:
		case 26288:
		case 26289:
			if (player.getCombat().inCombat()) {
				player.getClient().queueOutgoingPacket(new SendMessage("You cannot use this while in combat!"));
				return true;
			}

			if ((player.getAttributes().get("gwdaltarkey") == null)
					|| (((Long) player.getAttributes().get("gwdaltarkey")).longValue() < System.currentTimeMillis())) {
				player.getAttributes().set("gwdaltarkey", Long.valueOf(System.currentTimeMillis() + 600000L));
				player.getClient().queueOutgoingPacket(new SendSound(442, 1, 0));
				player.getClient()
						.queueOutgoingPacket(new SendMessage("You recharge your Prayer points at the altar."));
				player.getUpdateFlags().sendAnimation(645, 5);
				player.getLevels()[5] = player.getMaxLevels()[5];
				player.getSkill().update(5);
			} else {
				player.getClient().queueOutgoingPacket(new SendMessage("You cannot use this yet!"));
			}

			return true;
		case 26428:
			if (player.getLocation().getY() <= 5331) {
				player.teleport(new Location(2925, 5332, 2));
			} else if (player.getMinigames().getGWKC()[2] >= getPointsToEnter(player)) {
				player.teleport(new Location(2925, 5331, 2));
				int tmp344_343 = 2;
				short[] tmp344_340 = player.getMinigames().getGWKC();
				tmp344_340[tmp344_343] = ((short) (tmp344_340[tmp344_343] - getPointsToEnter(player)));
				player.getMinigames().updateGWKC(2);
			} else {
				player.getClient().queueOutgoingPacket(new SendMessage("You need more kills to enter this room."));
			}

			return true;
		case 26439:
			if (player.getLocation().getY() <= 5335)
				player.teleport(new Location(2885, 5345, 2));
			else {
				player.teleport(new Location(2885, 5332, 2));
			}
			return true;
		case 26427:
			if (player.getLocation().getX() >= 2908) {
				if (player.getMinigames().getGWKC()[3] >= getPointsToEnter(player)) {
					player.teleport(new Location(2907, 5265, 0));
					int tmp491_490 = 3;
					short[] tmp491_487 = player.getMinigames().getGWKC();
					tmp491_487[tmp491_490] = ((short) (tmp491_487[tmp491_490] - getPointsToEnter(player)));
					player.getMinigames().updateGWKC(3);
				} else {
					player.getClient().queueOutgoingPacket(new SendMessage("You need more kills to enter this room."));
				}
			} else {
				player.teleport(new Location(2908, 5265, 0));
			}

			return true;
		case 26303:
			if (player.getInventory().hasItemAmount(new Item(954, 1))) {
				if (player.getLocation().getY() >= 5279)
					player.teleport(new Location(2872, 5269, 2));
				else
					player.teleport(new Location(2871, 5279, 2));
			} else {
				player.getClient().queueOutgoingPacket(new SendMessage("You need a rope to grapple over this."));
			}
			return true;
		case 26426:
			if (player.getLocation().getY() <= 5295) {
				if (player.getMinigames().getGWKC()[1] >= getPointsToEnter(player)) {
					player.teleport(new Location(2839, 5296, 2));
					int tmp699_698 = 1;
					short[] tmp699_695 = player.getMinigames().getGWKC();
					tmp699_695[tmp699_698] = ((short) (tmp699_695[tmp699_698] - getPointsToEnter(player)));
					player.getMinigames().updateGWKC(1);
				} else {
					player.getClient().queueOutgoingPacket(new SendMessage("You need more kills to enter this room."));
				}
			} else {
				player.teleport(new Location(2839, 5295, 2));
			}

			return true;
		case 26384:
			if (player.getLocation().getX() >= 2852){ 
				player.teleport(new Location(2850, 5333, 2));
			} else {
				player.teleport(new Location(2852, 5333, 2));
			}
			return true;
		case 26425:
			if (player.getLocation().getX() <= 2863) {
				if (player.getMinigames().getGWKC()[0] >= getPointsToEnter(player)) {
					player.teleport(new Location(2864, 5354, 2));
					int tmp867_866 = 0;
					short[] tmp867_863 = player.getMinigames().getGWKC();
					tmp867_863[tmp867_866] = ((short) (tmp867_863[tmp867_866] - getPointsToEnter(player)));
					player.getMinigames().updateGWKC(0);
				} else {
					player.getClient().queueOutgoingPacket(new SendMessage("You need more kills to enter this room."));
				}
			} else {
				player.teleport(new Location(2863, 5354, 2));
			}
			return true;
			
		case 26444:
			player.teleport(new Location(2914, 5300, 1));
			return true;
		case 26445:
			player.teleport(new Location(2920, 5274, 0));
			return true;
		}
		return false;
	}

	public static final boolean useItemOnObject(Player player, int id, int obj) {
		switch (id) {
		case 954:
			switch (obj) {
			case 26420:
				if (player.getLocation().getY() < 5280)
					player.teleport(new Location(2920, 5274, 0));
				else {
					player.teleport(new Location(2914, 5300, 1));
				}
				return true;
			case 26421:
				if (player.getLocation().getY() < 5280)
					player.teleport(new Location(2921, 5276, 1));
				else {
					player.teleport(new Location(2912, 5300, 2));
				}
				return true;
			case 26303:
				if (player.getLocation().getY() >= 5279)
					player.teleport(new Location(2872, 5269, 2));
				else {
					player.teleport(new Location(2871, 5279, 2));
				}
				return true;
			}
			return false;
		}
		return false;
	}
}
