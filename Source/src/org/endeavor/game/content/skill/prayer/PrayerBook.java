package org.endeavor.game.content.skill.prayer;

import java.io.Serializable;

import org.endeavor.game.content.combat.Hit;
import org.endeavor.game.content.skill.prayer.impl.CursesPrayerBook;
import org.endeavor.game.content.skill.prayer.impl.DefaultPrayerBook;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.player.EquipmentConstants;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendConfig;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendSidebarInterface;

public abstract class PrayerBook implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8924625331928787306L;

	protected byte[] active = null;

	protected byte[] drain = null;

	protected byte headIcon = -1;

	protected Player player = null;

	protected boolean flag = false;

	public abstract void toggle(int paramInt);

	public abstract void onDisable(int paramInt);

	public abstract boolean clickButton(int paramInt);

	public abstract double getDrainRate(int paramInt);

	public abstract void disableProtection(int paramInt);

	public abstract int getDamage(Hit paramHit);

	public abstract boolean isProtectionActive();

	public abstract void process();

	public abstract void doEffectOnHit(Entity paramEntity, Hit paramHit);

	public void disable() {
		for (int i = 0; i < active.length; i++) {
			active[i] = 0;
			onDisable(i);
		}

		headIcon = -1;
		player.setAppearanceUpdateRequired(true);
	}

	public void disable(int i) {
		active[i] = 0;
		onDisable(i);
	}

	public void drain() {
		int amount = 0;
		for (int i = 0; i < active.length; i++) {
			if (active[i] == 1) {
				if (++drain[i] >= getAffectedDrainRate(i) / 0.6) {
					amount++;
					drain[i] = 0;
				}
			}
		}

		if (amount > 0) {
			drain(amount);
		}
	}

	public double getAffectedDrainRate(int id) {
		return getDrainRate(id) * (1 + (0.035 * player.getBonuses()[EquipmentConstants.PRAYER]));
	}

	public void drain(int drain) {
		int prayer = player.getSkill().getLevels()[5];
		if (drain >= prayer) {
			for (int i = 0; i < this.drain.length; i++) {
				this.drain[i] = 0;
			}
			
			disable();
			player.getSkill().setLevel(5, 0);
			player.getClient().queueOutgoingPacket(
					new SendMessage("You have run out of prayer points; you must recharge at an altar."));
		} else {
			player.getSkill().deductFromLevel(5, drain < 1 ? 1 : (int) Math.ceil(drain));

			if (player.getSkill().getLevels()[5] <= 0) {
				disable();
				player.getClient().queueOutgoingPacket(
						new SendMessage("You have run out of prayer points; you must recharge at an altar."));
			}
		}
	}

	public boolean flag() {
		return flag;
	}

	public boolean active(int id) {
		return active[id] == 1;
	}

	public int getHeadicon() {
		return headIcon;
	}

	public byte getHeadIcon() {
		return headIcon;
	}

	public void setHeadIcon(byte headIcon) {
		this.headIcon = headIcon;
	}

	public PrayerBookType getPrayerBookType() {
		if (player.getPrayerInterface() == 21356) {
			return PrayerBookType.CURSES;
		}

		return PrayerBookType.DEFAULT;
	}

	public static void setBook(Player player, PrayerBookType type) {
		player.getPrayer().disable();
		
		if (type == PrayerBookType.DEFAULT) {
			player.setPrayerInterface(5608);
			player.setPrayer(new DefaultPrayerBook(player));
			player.getClient().queueOutgoingPacket(new SendSidebarInterface(6, 5608));
		} else if (type == PrayerBookType.CURSES) {
			player.setPrayerInterface(21356);
			player.setPrayer(new CursesPrayerBook(player));
			player.getClient().queueOutgoingPacket(new SendSidebarInterface(6, 21356));
		}
	}

	public static enum PrayerBookType {
		DEFAULT, CURSES;
	}
}
