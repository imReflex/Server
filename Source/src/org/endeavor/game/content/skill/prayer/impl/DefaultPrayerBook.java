package org.endeavor.game.content.skill.prayer.impl;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.game.content.combat.Hit;
import org.endeavor.game.content.skill.prayer.PrayerBook;
import org.endeavor.game.content.skill.prayer.PrayerConstants;
import org.endeavor.game.content.skill.prayer.PrayerSounds;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.mob.MobConstants;
import org.endeavor.game.entity.player.ItemCheck;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerConstants;
import org.endeavor.game.entity.player.net.out.impl.SendConfig;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendOpenTab;
import org.endeavor.game.entity.player.net.out.impl.SendSidebarInterface;
import org.endeavor.game.entity.player.net.out.impl.SendSound;

public class DefaultPrayerBook extends PrayerBook {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7751065349155354356L;
	private boolean cooldown = true;

	public DefaultPrayerBook(Player player) {
		this.player = player;

		active = new byte[PrayerConstants.DEFAULT_NAMES.length];
		drain = new byte[PrayerConstants.DEFAULT_NAMES.length];
		
		if (player.isActive()) {
			for (int i = 0; i < player.getQuickPrayersDefault().length; i++) {
				player.send(new SendConfig(630 + i, player.getQuickPrayersDefault()[i]));
			}
		}
	}

	@Override
	public void process() {
	}

	@Override
	public void doEffectOnHit(Entity attacked, Hit hit) {
		if ((active(23)) && (attacked.getLevels()[5] > 0)) {
			int newLvl = attacked.getLevels()[5] - (int) (hit.getDamage() * 0.25D);
			attacked.getLevels()[5] = ((byte) newLvl);
			if (!attacked.isNpc()) {
				Player p = org.endeavor.game.entity.World.getPlayers()[attacked.getIndex()];

				if (p != null)
					p.getSkill().update(5);
			}
		}
	}
	
	public boolean hasPrayerRequirements(int id) {
		String name = PrayerConstants.DEFAULT_NAMES[id];
		int requiredLevel = PrayerConstants.DEFAULT_REQUIREMENTS[id];
		boolean canUse = true;
		
		if (player.getMaxLevels()[5] < requiredLevel) {
			player.getClient().queueOutgoingPacket(new SendMessage("You need a Prayer level of " + requiredLevel + " to use " + name + "."));
			canUse = false;
		} else if ((id == 24) && (player.getMaxLevels()[1] < 65)) {
			player.getClient().queueOutgoingPacket(new SendMessage("You need a Defence level of 65 to use Chivalry."));
			canUse = false;
		} else if ((id == 25) && (player.getMaxLevels()[1] < 70)) {
			player.getClient().queueOutgoingPacket(new SendMessage("You need a Defence level of 70 to use Piety."));
			canUse = false;
		}
		
		return canUse;
	}

	@Override
	public void toggle(int id) {
		String name = PrayerConstants.DEFAULT_NAMES[id];
		int requiredLevel = PrayerConstants.DEFAULT_REQUIREMENTS[id];
		int configId = PrayerConstants.DEFAULT_CONFIG_IDS[id];
		boolean canUse = true;

		if (player.isDead()) {
			canUse = false;
		} else if (!player.getController().canUsePrayer(player)) {
			canUse = false;
		} else if (player.getMaxLevels()[5] < requiredLevel) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("You need a Prayer level of " + requiredLevel + " to use " + name + "."));
			canUse = false;
		} else if ((id == 24) && (player.getMaxLevels()[1] < 65)) {
			player.getClient().queueOutgoingPacket(new SendMessage("You need a Defence level of 65 to use Chivalry."));
			canUse = false;
		} else if ((id == 25) && (player.getMaxLevels()[1] < 70)) {
			player.getClient().queueOutgoingPacket(new SendMessage("You need a Defence level of 70 to use Piety."));
			canUse = false;
		} else if (!cooldown) {
			if ((id == 18) || (id == 16) || (id == 17)) {
				player.getClient().queueOutgoingPacket(
						new SendMessage("A magical force prevents you from activating your prayer."));
				canUse = false;
			}
		} else if (player.getSkill().getLevels()[5] == 0) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("You have run out of prayer points; you must recharge at an altar."));
			canUse = false;
		}

		if (!canUse) {
			player.getClient().queueOutgoingPacket(new SendConfig(configId, 0));
			return;
		}

		active[id] = ((byte) (active[id] == 1 ? 0 : 1));
		player.getClient().queueOutgoingPacket(new SendConfig(configId, active[id]));
		if (active[id] == 1) {
			if (PrayerSounds.getSoundId(id) != -1)
				player.getClient().queueOutgoingPacket(new SendSound(PrayerSounds.getSoundId(id), 1, 0));
		} else
			player.getClient().queueOutgoingPacket(new SendSound(435, 1, 0));

		if (active[id] == 1) {
			int[] disabled = PrayerConstants.DEFAULT_DISABLED_PRAYERS[id];
			for (int i = 0; i < disabled.length; i++) {
				if (disabled[i] != id) {
					player.getClient().queueOutgoingPacket(
							new SendConfig(PrayerConstants.DEFAULT_CONFIG_IDS[disabled[i]], 0));
					active[disabled[i]] = 0;
				}
			}
			switch (id) {
			case 16:
				headIcon = 2;
				break;
			case 17:
				headIcon = 1;
				break;
			case 18:
				headIcon = 0;
				break;
			case 21:
				headIcon = 3;
				break;
			case 22:
				headIcon = 5;
				break;
			case 23:
				headIcon = 4;
			case 19:
			case 20:
			}
			if (headIcon != -1)
				player.setAppearanceUpdateRequired(true);
		} else {
			switch (id) {
			case 16:
			case 17:
			case 18:
			case 21:
			case 22:
			case 23:
				headIcon = -1;
			case 19:
			case 20:
			}
			player.setAppearanceUpdateRequired(true);
		}
	}

	@Override
	public void onDisable(int id) {
		player.getClient().queueOutgoingPacket(new SendConfig(PrayerConstants.DEFAULT_CONFIG_IDS[id], 0));

		int headicon = -1;

		switch (id) {
		case 16:
			headicon = 2;
			break;
		case 17:
			headicon = 1;
			break;
		case 18:
			headicon = 0;
			break;
		case 21:
			headicon = 3;
			break;
		case 22:
			headicon = 5;
			break;
		case 23:
			headicon = 4;
		case 19:
		case 20:
		}
		if (headicon != -1) {
			headIcon = -1;
			player.setAppearanceUpdateRequired(true);
		}
	}

	@Override
	public boolean clickButton(int id) {
		if (id >= 67050 && id <= 67075) {
			int prayerId = id - 67050;
			
			if (player.getQuickPrayersDefault()[prayerId] == 0 && !hasPrayerRequirements(prayerId)) {
				return true;
			} else {
				if (player.getQuickPrayersDefault()[prayerId] == 0) {
					int[] disabled = PrayerConstants.DEFAULT_DISABLED_PRAYERS[prayerId];
					for (int i = 0; i < disabled.length; i++) {
						player.getQuickPrayersDefault()[disabled[i]] = 0;
					}
				}
				
				player.getQuickPrayersDefault()[prayerId] = (player.getQuickPrayersDefault()[prayerId] == 1 ? (byte) 0 : (byte) 1);
				
				for (int i = 0; i < player.getQuickPrayersDefault().length; i++) {
					player.send(new SendConfig(630 + i, player.getQuickPrayersDefault()[i]));
				}
			}
			
			return true;
		}
		
		switch (id) {
		case 67089:
			player.send(new SendSidebarInterface(PlayerConstants.PRAYER_TAB, 5608));
			return true;
		case 87082:
			player.send(new SendSidebarInterface(PlayerConstants.PRAYER_TAB, 25789));
			player.send(new SendOpenTab(PlayerConstants.PRAYER_TAB));
			return true;
		case 87087:
			int quicks = 0;
			boolean on = false;
			
			for (int i = 0; i < player.getQuickPrayersDefault().length; i++) {
				if (player.getQuickPrayersDefault()[i] == 1) {
					if (active[i] == 0) {
						on = true;
					}
					
					quicks++;
				}
			}
			
			if (quicks == 0) {
				player.send(new SendMessage("You have not set any quick prayers."));
				return true;
			}
			
			for (int i = 0; i < player.getQuickPrayersDefault().length; i++) {
				if (player.getQuickPrayersDefault()[i] == 1) {
					if (on) {
						if (active[i] == 0) {
							toggle(i);
						}
					} else {
						if (active[i] == 1) {
							toggle(i);
						}
					}
				}
			}
			
			return true;
		
		
		case 97168:
			toggle(0);
			return true;
		case 97170:
			toggle(1);
			return true;
		case 97172:
			toggle(2);
			return true;
		case 97174:
			toggle(3);
			return true;
		case 97176:
			toggle(4);
			return true;
		case 97178:
			toggle(5);
			return true;
		case 97180:
			toggle(6);
			return true;
		case 97182:
			toggle(7);
			return true;
		case 97184:
			toggle(8);
			return true;
		case 97186:
			toggle(9);
			return true;
		case 97188:
			toggle(10);
			return true;
		case 97190:
			toggle(11);
			return true;
		case 97192:
			toggle(12);
			return true;
		case 97194:
			toggle(13);
			return true;
		case 97196:
			toggle(14);
			return true;
		case 97198:
			toggle(15);
			return true;
		case 97200:
			toggle(16);
			return true;
		case 97202:
			toggle(17);
			return true;
		case 97204:
			toggle(18);
			return true;
		case 97206:
			toggle(19);
			return true;
		case 97208:
			toggle(20);
			return true;
		case 97210:
			toggle(21);
			return true;
		case 97212:
			toggle(22);
			return true;
		case 97214:
			toggle(23);
			return true;
		case 97216:
			toggle(24);
			return true;
		case 97218:
			toggle(25);
			return true;
		}

		return false;
	}

	@Override
	public double getDrainRate(int id) {
		return PrayerConstants.DEFAULT_DRAIN_RATES[id];
	}

	@Override
	public void disableProtection(int delay) {
		if (!flag) {
			return;
		}
		flag = false;

		for (int i = 0; i < PrayerConstants.DEFAULT_PROTECTION_PRAYERS.length; i++) {
			if (active[PrayerConstants.DEFAULT_PROTECTION_PRAYERS[i]] == 1) {
				headIcon = -1;
				player.getClient().queueOutgoingPacket(
						new SendConfig(
								PrayerConstants.DEFAULT_CONFIG_IDS[PrayerConstants.DEFAULT_PROTECTION_PRAYERS[i]], 0));
			}
		}

		player.setAppearanceUpdateRequired(true);

		TaskQueue.queue(new Task(player, 9, false, Task.StackType.NEVER_STACK, Task.BreakType.NEVER, 5) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 6192567121754405351L;

			@Override
			public void execute() {
				flag = true;
				stop();
			}

			@Override
			public void onStop() {
			}
		});
	}

	@Override
	public int getDamage(Hit hit) {
		switch (hit.getType()) {
		case MELEE:
			if (active(18)) {
				Entity e = hit.getAttacker();
				if ((e != null) && (!e.isNpc())) {
					Player p2 = org.endeavor.game.entity.World.getPlayers()[e.getIndex()];

					if ((p2 == null) || (!p2.getMelee().isVeracEffectActive())) {
						return hit.getDamage() / 2;
					}
				} else if ((e != null) && (e.isNpc())) {
					Mob mob = org.endeavor.game.entity.World.getNpcs()[e.getIndex()];

					if (mob == null) {
						return hit.getDamage() / 2;
					}

					int id = mob.getId();

					if (id == 10057) {
						return hit.getDamage() / 2;
					}

					if (id == 8596) {
						return hit.getDamage();
					}

					if ((id != 2030) && (id != 8133))
						return 0;
				} else {
					return hit.getDamage() / 2;
				}
			}
			break;
		case MAGIC:
			if (active(16)) {
				Entity e = hit.getAttacker();
				if ((e != null) && (e.isNpc())) {
					Mob mob = org.endeavor.game.entity.World.getNpcs()[e.getIndex()];

					if (mob == null) {
						return hit.getDamage() / 2;
					}

					int id = mob.getId();

					if (id == 12826) {
						return hit.getDamage();
					}

					if (id == 8133) {
						return (int) (hit.getDamage() * 0.8D);
					}
					if (MobConstants.isDragon(mob) && id != 1591 && id != 1592 && !ItemCheck.hasDFireShield(player)) {
						player.getClient().queueOutgoingPacket(new SendMessage("Your prayers resist some of the dragonfire."));
						return (int) (hit.getDamage() / 3);
					}
					return 0;
				}

				return hit.getDamage() / 2;
			}

			break;
		case RANGED:
			if (active(17)) {
				Entity e = hit.getAttacker();
				if ((e != null) && (e.isNpc())) {
					Mob mob = org.endeavor.game.entity.World.getNpcs()[e.getIndex()];

					if (mob == null) {
						return hit.getDamage() / 2;
					}

					int id = mob.getId();

					if (id == 8133) {
						return (int) (hit.getDamage() * 0.8D);
					}

					return 0;
				}

				return hit.getDamage() / 2;
			}

			break;
		}

		return hit.getDamage();
	}

	@Override
	public boolean isProtectionActive() {
		return (active(18)) || (active(16)) || (active(17));
	}
}
