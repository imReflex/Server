package org.endeavor.game.content.minigames.armsrace;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.minigames.fightpits.FightPitsConstants;
import org.endeavor.game.content.skill.SkillConstants;
import org.endeavor.game.content.skill.magic.spells.Vengeance;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;
import org.endeavor.game.entity.player.net.out.impl.SendString;

public class ARGame {
	public static final int GAME_TIME = 420;
	public static final int SWITCH_TIME = GAME_TIME / 6;
	
	private final Player[] players;
	private boolean started = false;
	
	private int time = GAME_TIME;
	private int switchTimer = 0;

	private final byte[] used = new byte[15];

	private String winning = "No one!";
	
	private int switches = 0;

	public ARGame(Player[] players) {
		this.players = players;

		for (int i = 0; i < used.length; i++) {
			used[i] = -1;
		}

		init();
	}

	public void onHit(Player p, int damage) {
		if (p.getAttributes().get("argamedamage") == null) {
			p.getAttributes().set("argamedamage", Integer.valueOf(0));
		}

		int dmg = p.getAttributes().getInt("argamedamage") + damage;

		p.getAttributes().set("argamedamage", Integer.valueOf(dmg));

		setWinner();
	}

	public void setWinner() {
		String name = null;
		int dmg = -1;

		for (Player p : players) {
			if ((p.getAttributes().get("argamedamage") != null) && (p.getAttributes().getInt("argamedamage") > dmg)) {
				name = p.getUsername();
				dmg = p.getAttributes().getInt("argamedamage");
			}
		}

		if ((name != null) && (!winning.equals(name))) {
			winning = name;
			sendPacketToPlayers(new SendString("Winning: " + winning,
					FightPitsConstants.FIGHT_PITS_INTERFACE_STRINGS[3]));
		}
	}

	public void onKill(Player p) {
		p.getMagic().getSpellCasting().cast(new Vengeance());

		int kills = p.getAttributes().get("killstreakar") != null ? p.getAttributes().getInt("killstreakar") : 0;
		p.getAttributes().set("killstreakar", Integer.valueOf(kills + 1));

		String message = null;

		switch (kills + 1) {
		case 2:
			message = "2 KILL STREAK - POWER UP!";
			p.getAttributes().set("extradamagepowerup", Byte.valueOf((byte) 0));
			break;
		case 4:
			message = "4 KILL STREAK - POWER UP!";
			p.getAttributes().set("attacktimerpowerup", Byte.valueOf((byte) 0));
			break;
		case 3:
		default:
			if (kills + 1 > 4) {
				message = kills + 1 + " KILL STREAK - UNSTOPPABLE!";
			}
			break;
		}

		if (message != null)
			p.getUpdateFlags().sendForceMessage(message);
	}
	
	public int getPlayerAmount() {
		int am = 0;
		
		for (Player p : players) {
			if (p != null) {
				am++;
			}
		}
		
		return am;
	}

	public boolean isEmpty() {
		int c = 0;

		for (Player p : players) {
			if (p != null) {
				c++;
			}
		}

		return c == 0;
	}

	public void process() {
		if (!started) {
			return;
		}

		if (getPlayerAmount() < 2) {
			end();
			return;
		}
		
		final int prev = getMinutesTillEnd();
		final int prev2 = time / 2;
		final int prev3 = switchTimer / 2;
		
		time--;
		
		if (getMinutesTillEnd() > 1) {
			if (getMinutesTillEnd() != prev) {
				sendPacketToPlayers(new SendString("Time until game ends: " + getMinutesTillEnd(),
						FightPitsConstants.FIGHT_PITS_INTERFACE_STRINGS[1]));
			}
		} else {
			if (time / 2 != prev2) {
				sendPacketToPlayers(new SendString("Time until game ends: " + time / 2,
						FightPitsConstants.FIGHT_PITS_INTERFACE_STRINGS[1]));
			}
		}
		
		if (switches < 6) {
			switchTimer--;
			
			if (switchTimer <= 0) {
				switchTimer = SWITCH_TIME;
				switches++;
				
				for (Player p : players) {
					p.getUpdateFlags().sendForceMessage("GO!");
				}
				
				next();
				
				if (switches == 6) {
					sendPacketToPlayers(new SendString("Final weapon!",
							FightPitsConstants.FIGHT_PITS_INTERFACE_STRINGS[2]));
				}
			} else {
				if (switchTimer / 2 != prev3) {
					sendPacketToPlayers(new SendString("Next: " + (switchTimer / 2 + 1),
							FightPitsConstants.FIGHT_PITS_INTERFACE_STRINGS[2]));
					if (switchTimer / 2 <= 9) {
						for (Player p : players) {
							p.getUpdateFlags().sendForceMessage("" + ((switchTimer / 2) + 1) + "!");
						}
					}
				}
			}
		}
		
		/*if (time >= 70) {
			int next = time % 70;

			if ((time % 2 != 0) && (next / 2 <= 5)) {
				if (next / 2 == 0) {
					next();
				}

				for (Player p : players) {
					if (p != null) {
						if (next / 2 == 0)
							p.getUpdateFlags().sendForceMessage("GO!");
						else {
							p.getUpdateFlags().sendForceMessage(next / 2 + "!");
						}
					}
				}
			}

			sendPacketToPlayers(new SendString("Next: " + next / 2,
					FightPitsConstants.FIGHT_PITS_INTERFACE_STRINGS[1]));
		} else {
			sendPacketToPlayers(new SendString("Final weapon!",
					FightPitsConstants.FIGHT_PITS_INTERFACE_STRINGS[1]));
		}*/

		if (time == 0) {
			end();
		}
	}

	public boolean hasUsed(int i) {
		for (byte b : used) {
			if (b == -1) {
				return false;
			}

			if (b == (byte) i) {
				return true;
			}
		}

		return false;
	}

	public void setUsed(int ind) {
		for (int i = 0; i < used.length; i++)
			if (used[i] == -1) {
				used[i] = ((byte) ind);
				return;
			}
	}

	public void next() {
		int next = Misc.randomNumber(ARConstants.AR_SWITCHES.length);

		while (hasUsed(next)) {
			next = Misc.randomNumber(ARConstants.AR_SWITCHES.length);
		}

		boolean veng = Misc.randomNumber(3) == 0;

		setUsed(next);
		ARSwitch s = ARConstants.AR_SWITCHES[next];

		for (Player p : players)
			if (p != null) {
				final Entity attacking = p.getCombat().getAttacking();
				
				p.getEquipment().clear();

				s.execute(p);

				p.getSpecialAttack().setSpecialAmount(100);

				p.getEquipment().updatePlayerAnimations();
				p.getEquipment().updateBlockAnimation();
				p.getEquipment().updateSidebar();

				p.getEquipment().calculateBonuses();
				p.updateCombatType();
				p.getEquipment().update();
				p.getSpecialAttack().onEquip();

				if (veng) {
					p.getMagic().getSpellCasting().cast(new Vengeance());
				}

				p.getCombat().setAttack(attacking);
				p.setAppearanceUpdateRequired(true);
			}
	}

	public void end() {
		for (Player player : players) {
			if (player != null) {
				remove(player);
			}
		}

		ArmsRaceLobby.onEndGame();
	}

	public boolean isInGame(Player p) {
		for (Player player : players) {
			if ((player != null) && (p.equals(player))) {
				return true;
			}
		}

		return false;
	}

	public void remove(Player p) {
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null && players[i].equals(p)) {
				players[i] = null;
				break;
			}
		}
		
		p.getAttributes().remove("argamedamage");
		p.getAttributes().remove("killstreakar");
		p.getAttributes().remove("attacktimerpowerup");
		p.getAttributes().remove("extradamagepowerup");
		
		p.teleport(ARConstants.FROM_WAITING);
		p.getInventory().clear();
		p.getEquipment().clear();
		p.resetLevels();
		p.getMagic().setDFireShieldEffect(false);
		p.getMagic().setVengeanceActive(false);
		p.setController(ControllerManager.DEFAULT_CONTROLLER);
		p.curePoison(0);
	}

	public void sendPacketToPlayers(OutgoingPacket o) {
		for (Player p : players)
			if (p != null)
				p.getClient().queueOutgoingPacket(o);
	}

	public void init() {
		for (Player p : players) {
			if (p != null) {
				p.teleport(new Location(ARConstants.START.getX() + ARConstants.getXMod(), ARConstants.START.getY()
						+ ARConstants.getYMod()));
				p.setController(ARConstants.AR_CONTROLLER);
				p.resetLevels();
				p.curePoison(0);
				p.getMagic().setVengeanceActive(false);
				p.getPrayer().disable();
				p.getSkill().setLevel(SkillConstants.HITPOINTS, 99);
			}
		}

		sendPacketToPlayers(new SendString("Competitors: " + players.length,
				FightPitsConstants.FIGHT_PITS_INTERFACE_STRINGS[0]));
		sendPacketToPlayers(new SendString("Time until game ends: " + getMinutesTillEnd(),
				FightPitsConstants.FIGHT_PITS_INTERFACE_STRINGS[1]));

		TaskQueue.queue(new Task(2, false) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 9183512241816387468L;
			byte pos = 10;

			@Override
			public void execute() {
				if (pos-- == 0) {
					stop();
				} else {
					for (Player p : players) {
						p.getUpdateFlags().sendForceMessage((pos + 1) + "!");
					}
				}
			}

			@Override
			public void onStop() {
				started = true;
				next();
			}
		});
	}

	public boolean hasStarted() {
		return started;
	}

	public int getMinutesTillEnd() {
		return time / 100 + 1;
	}
}
