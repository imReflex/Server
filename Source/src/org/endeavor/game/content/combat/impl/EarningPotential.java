package org.endeavor.game.content.combat.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.Inventory;
import org.endeavor.game.content.QuestTab;
import org.endeavor.game.content.combat.Hit;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.item.impl.GroundItemHandler;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class EarningPotential implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5551010995182760202L;
	public static final int MINIMUM_CARRIED_WEALTH = 75000;
	public static final int EXTRA_DROPS_ROLL = 2;
	public static final int DROP_CHANCE = 5;
	public static final int EXTRA_PKP_CHANCE = 10;
	private final Player player;
	private int ep = 0;

	private int damage = 0;

	private int pkp = 0;

	private long[] killOrKilledBy = new long[5];

	public EarningPotential(Player player) {
		this.player = player;
	}

	public void add(Player player) {
		for (int i = 0; i < killOrKilledBy.length; i++) {
			if (killOrKilledBy[i] == 0L) {
				killOrKilledBy[i] = player.getUsernameToLong();
				return;
			}
		}

		killOrKilledBy = new long[5];
		killOrKilledBy[0] = player.getUsernameToLong();
	}

	public boolean contains(Player player) {
		for (long i : killOrKilledBy) {
			if (i == player.getUsernameToLong()) {
				return true;
			}
		}

		return false;
	}

	public void onKilled(Entity killer) {
		if (killer == null) {
			return;
		}

		if (!killer.isNpc()) {
			Player other = org.endeavor.game.entity.World.getPlayers()[killer.getIndex()];

			if (other == null) {
				return;
			}

			if ((contains(other)) || (other.getEarningPotential().contains(player))) {
				other.getClient().queueOutgoingPacket(
						new SendMessage("You have recently killed or been killed by " + player.getUsername() + "."));
				other.getClient().queueOutgoingPacket(new SendMessage("You gain no PKP for this kill."));
				return;
			}
			add(other);

			if (allow(other, true)) {
				other.getEarningPotential().add(player);
				other.getEarningPotential().addPKP();
				other.getAchievements().incr(player, "Kill 25 players in Wild");
			}
		}
	}

	public void addPKP() {
		int am = 2 + (Misc.randomNumber(player.isRespectedMember() ? 5 : 10) == 0 ? 1 : 0);

		pkp += am;
		QuestTab.updatePKP(player);

		player.getAchievements().incr(player, "Achieve 500 total PKP", am);
	}

	public boolean allow(Player other, boolean message) {
		if (!other.getController().equals(ControllerManager.WILDERNESS_CONTROLLER) || !player.getController().equals(ControllerManager.WILDERNESS_CONTROLLER)) {
			return false;
		}

		if (other.getClient().getHostId() == player.getClient().getHostId()) {
			if (message)
				other.getClient().queueOutgoingPacket(
						new SendMessage("You do not receive PKP for killing a player with the same IP address."));
			return false;
		}

		if (getCarriedWealth(player) < 75000) {
			if (message)
				other.getClient().queueOutgoingPacket(
						new SendMessage("The other player didn't have enough carried wealth for you to gain PKP."));
			return false;
		}

		if (getCarriedWealth(other) < 75000) {
			if (message)
				other.getClient().queueOutgoingPacket(
						new SendMessage("You do not have enough carried wealth for you to gain PKP."));
			return false;
		}

		return true;
	}

	public static int getCarriedWealth(Player p) {
		int w = 0;

		for (Item i : p.getEquipment().getItems()) {
			if (i != null) {
				w += GameDefinitionLoader.getHighAlchemyValue(i.getId()) * i.getAmount();
			}
		}

		for (Item i : p.getInventory().getItems()) {
			if (i != null) {
				w += GameDefinitionLoader.getHighAlchemyValue(i.getId()) * i.getAmount();
			}
		}

		return w < 0 ? -w : w;
	}

	public void drop(Player other) {
		player.incrKills();
		other.incrDeaths();
		
		if (!allow(other, false)) {
			return;
		}

		player.getTitles().checkForNewPKAchievments();

		if ((ep == 100) || (Misc.randomNumber(5) == 0)) {
			for (int i = 0; i < 1 + Misc.randomNumber(2); i++) {
				Item stat = null;

				if ((player.isMember()) && (ep == 100))
					stat = new Item(StatueData.values()[(StatueData.values().length / 2 + Misc.randomNumber(StatueData
							.values().length - StatueData.values().length / 2))].id, 1);
				else {
					stat = new Item(StatueData.values()[Misc.randomNumber(StatueData.values().length)].id, 1);
				}

				GroundItemHandler.add(stat, other.getLocation(), player);
			}

			if (ep == 100) {
				ep = 0;
				QuestTab.updateEP(player);
				QuestTab.updatePKP(player);
			}
		}
	}

	public void addEarningPotentialForHit(Entity other, Hit hit) {
		if (!other.isNpc()) {
			Player otherPlayer = org.endeavor.game.entity.World.getPlayers()[other.getIndex()];

			if (otherPlayer == null) {
				return;
			}

			if (!contains(otherPlayer)) {
				if (!allow(otherPlayer, false)) {
					return;
				}

				damage += hit.getDamage();

				if (((player.isSuperMember()) && (damage >= 25)) || (damage >= 30)) {
					ep += damage / (player.isSuperMember()? 25 : 30);

					if (ep > 100) {
						ep = 100;
					}

					damage = 0;
					QuestTab.updateEP(player);
				}
			}
		}
	}

	public void trade() {
		Inventory inv = player.getInventory();

		int total = 0;

		for (int i = 0; i < 28; i++) {
			Item k = inv.get(i);
			if (k != null) {
				int am = StatueData.forId(k.getId());
				if ((am > 0) && (inv.hasSpaceOnRemove(new Item(k.getId(), 1), new Item(995, am)))) {
					total += am;
					inv.setSlot(null, i);
					inv.add(995, am, false);
				}
			}
		}

		if (total == 0) {
			DialogueManager.sendStatement(player, new String[] { "You do not have any artifacts to trade." });
		} else {
			player.getInventory().update();
			DialogueManager.sendStatement(player, new String[] { "You recieve " + Misc.formatCoins(total)
					+ " for your artifacts." });
		}
	}

	public int getEP() {
		return ep;
	}

	public void setEP(int ep) {
		this.ep = ep;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getPkp() {
		return pkp;
	}

	public void setPkp(int pkp) {
		this.pkp = pkp;
	}

	public void decreasePKP(int amount) {
		pkp -= amount;
	}
	
	public long[] getKillOrKilledBy() {
		return killOrKilledBy;
	}

	public void setKillOrKilledBy(long[] killOrKilledBy) {
		this.killOrKilledBy = killOrKilledBy;
	}

	public static enum StatueData {
		ARTEFACT_RECEIPT(11974, 400000),
		
		ANCIENT(14876, 5000000), SEREN(14877, 2500000), ARMADYL(14878, 1000000), ZAMORAK(14879, 1000000), SARADOMIN(
				14880, 1000000), BANDOS(14881, 750000), RUBY(14882, 600000), GUTHIX(14883, 500000), ZAMORAK_B(14885,
				500000), THIRD_AGE(14891, 350000), STAT_HEADDRESS(14892, 350000), SARADOMIN_B(14886, 250000), BANDOS_B(
				14887, 150000), SARADOMIN_C(14888, 150000), ANCIENT_B(14889, 150000), BRONZED_CLAW(14890, 150000), ARMADYL_B(
				14884, 100000);

		final int id;
		final int coins;
		private static final Map<Integer, Integer> stats = new HashMap<Integer, Integer>();

		private StatueData(int id, int coins) {
			this.id = id;
			this.coins = coins * 2;
		}

		public static final void declare() {
			for (StatueData i : values())
				stats.put(Integer.valueOf(i.id), Integer.valueOf(i.coins));
		}

		public static final int forId(int id) {
			if (stats.containsKey(Integer.valueOf(id))) {
				return stats.get(Integer.valueOf(id)).intValue();
			}

			return 0;
		}
	}
}
