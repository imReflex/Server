package org.endeavor.game.content.minigames.barrows;

import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.following.Following;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.item.impl.GroundItemHandler;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class Barrows {
	public static final void spawnBrother(Player p, int id, Location location) {
		Mob brother = new Mob(p, id, false, false, false, location);
		brother.getFollowing().setFollow(p, Following.FollowType.COMBAT);
		brother.getCombat().setAttacking(p);
		p.getAttributes().set("barrowsActive" + id, new Object());
	}

	public static final void onBarrowsDeath(Player p, Mob killed) {
		if (isBarrowsBrother(killed)) {
			int id = killed.getId();
			p.getMinigames().setBarrowKilled(BarrowsConstants.getIndexForId(id));
			p.getAttributes().remove("barrowsActive" + id);

			int negate = p.getInventory().hasItemId(15378) ? 1 : 0;
			
			if (p.getMinigames().getBarrowsKillcount() >= 6 - negate) {
				p.getMinigames().resetBarrows();

				Item[] reward = BarrowsRewardTable.roll();

				for (Item i : reward) {
					GroundItemHandler.add(i, killed.getLocation(), p);
				}
			}
		}
	}

	public static boolean isBarrowsBrother(Mob mob) {
		int id = mob.getId();
		return (id >= 2025) && (id <= 2030);
	}

	public static final boolean clickObject(Player p, int id, int x, int y, int z) {
		for (int i = 0; i < BarrowsConstants.CRYPT_STAIRCASE_IDS.length; i++) {
			if (id == BarrowsConstants.CRYPT_STAIRCASE_IDS[i]) {
				p.teleport(BarrowsConstants.BARROWS_STAIRCASE_DEST[i]);
				return true;
			}
		}

		for (int i = 0; i < BarrowsConstants.CRYPT_COFFIN_IDS.length; i++) {
			if (id == BarrowsConstants.CRYPT_COFFIN_IDS[i]) {
				if ((!p.getMinigames().killedBarrow(i))
						&& (p.getAttributes().get("barrowsActive" + BarrowsConstants.BARROWS_NPC_IDS[i]) == null)) {
					spawnBrother(p, BarrowsConstants.BARROWS_NPC_IDS[i], new Location(x + 3, y + 3, z));
				} else {
					p.getClient().queueOutgoingPacket(new SendMessage("You search the sarcophagus and find nothing."));

					int negate = p.getInventory().hasItemId(15378) ? 1 : 0;
					
					if (p.getMinigames().getBarrowsKillcount() >= 6 - negate) {
						p.getMinigames().resetBarrows();

						Item[] reward = BarrowsRewardTable.roll();

						for (Item k : reward) {
							GroundItemHandler.add(k, p.getLocation(), p);
						}
					}
				}
				return true;
			}
		}

		return false;
	}

	public static final boolean dig(Player p) {
		int x = p.getLocation().getX();
		int y = p.getLocation().getY();
		int z = p.getLocation().getZ();
		int[][] bounds = BarrowsConstants.BARROWS_CRYPT_DIG_BOUNDS;

		if (z != 0) {
			return false;
		}

		for (int i = 0; i < bounds.length; i++) {
			if ((x > bounds[i][0]) && (y > bounds[i][1]) && (x < bounds[i][2]) && (y < bounds[i][3])) {
				p.teleport(BarrowsConstants.BARROWS_CRYPT_DIG_DEST[i]);
				p.getClient().queueOutgoingPacket(new SendMessage("You've broken into a crypt!"));
				return true;
			}
		}

		return false;
	}
}
