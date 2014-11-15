package org.endeavor.game.content.skill.slayer;

import java.io.Serializable;

import org.endeavor.engine.definitions.NpcDefinition;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.QuestTab;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class Slayer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1384213881909191513L;
	private transient Player p;
	private String task = null;
	private byte amount = 0;
	private SlayerDifficulty current = null;
	private Player partner = null;

	public Slayer(Player p) {
		this.p = p;
	}

	public boolean hasSlayerPartner() {
		return partner != null;
	}

	public void reset() {
		task = null;
		amount = 0;
		current = null;
	}

	public static boolean isSlayerTask(Player p, Mob mob) {
		return (p.getSlayer().getTask() != null)
				&& (mob.getDefinition().getName().toLowerCase().contains(p.getSlayer().getTask().toLowerCase()));
	}

	public static boolean isSlayerTask(Player p, String other) {
		return (p.getSlayer().getTask() != null)
				&& (other.toLowerCase().contains(p.getSlayer().getTask().toLowerCase()));
	}

	public void checkForSlayer(Mob killed) {
		if ((partner != null)
				&& ((task == null) || ((partner.getSlayer().hasTask()) && (!isSlayerTask(partner, task))))
				&& (partner.getLocation().isViewableFrom(p.getLocation())) && (partner.getSlayer().hasTask())
				&& (isSlayerTask(partner, killed.getDefinition().getName()))) {
			partner.getSkill().addExperience(18, killed.getDefinition().getLevel() * 2 / 4);
		}

		if (task == null) {
			return;
		}

		NpcDefinition def = killed.getDefinition();

		if (isSlayerTask(p, killed)) {
			amount = ((byte) (amount - 1));
			double exp = def.getLevel() * 2;

			addSlayerExperience(exp);
			doSocialSlayerExperience(killed, exp);

			if (amount == 0) {
				p.getAchievements().incr(p, "Complete a Slayer task");
				p.getAchievements().incr(p, "Complete 50 Slayer tasks");

				task = null;
				addSlayerExperience(def.getLevel() * 35);
				p.getClient().queueOutgoingPacket(
						new SendMessage("You have completed your Slayer task; return to Vannaka for another."));
				p.addSlayerPoints(amount);
				QuestTab.updateSlayerTask(p);

				if (current != null) {
					p.addSlayerPoints(current == SlayerDifficulty.LOW ? 2 + (p.isSuperMember() ? 1 : 0)
							: current == SlayerDifficulty.MEDIUM ? 5 + (p.isSuperMember() ? 2 : 0)
									: current == SlayerDifficulty.HIGH ? 7 + (p.isSuperMember() ? 2 : 0) : 0);
				}

				QuestTab.updateSlayerPoints(p);
			} else {
				QuestTab.updateSlayerTask(p);
			}
		}
	}

	public void doSocialSlayerExperience(Mob killed, double am) {
		if (partner != null) {
			Player other = partner;

			if ((other.getSlayer().getPartner() == null) || (!other.getSlayer().getPartner().equals(p))) {
				return;
			}

			if (!other.isActive()) {
				p.getClient().queueOutgoingPacket(new SendMessage("Your social slayer partner is not online."));
				partner = null;
				QuestTab.updateSocialSlayer(p);
			} else if ((other.getLocation().isViewableFrom(p.getLocation()))
					&& ((other.getSlayer().hasTask()) || (isSlayerTask(other, task)))) {
				other.getSkill().addExperience(18, am / 2.0D);
			}
		}
	}

	public void setSocialSlayerPartner(String name) {
		if (name.equalsIgnoreCase(p.getUsername())) {
			p.getClient().queueOutgoingPacket(new SendMessage("You cannot set yourself as your Slayer partner!"));
			return;
		}

		Player other = World.getPlayerByName(name);

		if (other == null) {
			p.getClient().queueOutgoingPacket(new SendMessage("Could not locate player: " + name + "."));
		} else {
			p.getClient().queueOutgoingPacket(new SendMessage("Your social slayer partner has been set."));
			partner = other;
			QuestTab.updateSocialSlayer(p);
		}
	}

	public void addSlayerExperience(double am) {
		p.getSkill().addExperience(18, am);
	}

	public void assign(SlayerDifficulty diff) {
		switch (diff) {
		case LOW:
			SlayerConstants.LowLevel[] lval = SlayerConstants.LowLevel.values();

			SlayerConstants.LowLevel set = lval[Misc.randomNumber(lval.length)];

			while (p.getMaxLevels()[18] < set.lvl) {
				set = lval[Misc.randomNumber(lval.length)];
			}

			task = set.name;

			amount = ((byte) (30 + Misc.randomNumber(25)));
			current = SlayerDifficulty.LOW;
			break;
		case MEDIUM:
			SlayerConstants.MediumLevel[] mval = SlayerConstants.MediumLevel.values();

			SlayerConstants.MediumLevel set2 = mval[Misc.randomNumber(mval.length)];

			while (p.getMaxLevels()[18] < set2.lvl) {
				set2 = mval[Misc.randomNumber(mval.length)];
			}

			task = set2.name;

			amount = ((byte) (30 + Misc.randomNumber(25)));
			current = SlayerDifficulty.MEDIUM;
			break;
		case HIGH:
			SlayerConstants.HighLevel[] hval = SlayerConstants.HighLevel.values();

			SlayerConstants.HighLevel set3 = hval[Misc.randomNumber(hval.length)];

			while (p.getMaxLevels()[18] < set3.lvl) {
				set3 = hval[Misc.randomNumber(hval.length)];
			}

			task = set3.name;

			amount = ((byte) (20 + Misc.randomNumber(25)));
			current = SlayerDifficulty.HIGH;
			break;
		default:
			throw new IllegalArgumentException("(Slayer.java) The world is going to end");
		}
	}

	public boolean hasTask() {
		return (amount > 0) && (task != null);
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public byte getAmount() {
		return amount;
	}

	public void setAmount(byte amount) {
		this.amount = amount;
	}

	public Player getPartner() {
		return partner;
	}

	public String getPartnerName() {
		return partner != null ? partner.getUsername() : null;
	}

	public SlayerDifficulty getCurrent() {
		return current;
	}

	public void setCurrent(SlayerDifficulty current) {
		this.current = current;
	}

	public static enum SlayerDifficulty implements Serializable {
		LOW, MEDIUM, HIGH;
	}
	
	public void setPlayer(Player player) {
		this.p = player;
	}
}
