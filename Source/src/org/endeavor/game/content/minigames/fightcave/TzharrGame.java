package org.endeavor.game.content.minigames.fightcave;

import java.util.ArrayList;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.GameConstants;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.mob.VirtualMobRegion;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;

public final class TzharrGame {
	public static final TzharrController CONTROLLER = new TzharrController();

	public static final Location LEAVE = new Location(2438, 5168, 0);
	public static final String FIGHT_CAVE_NPCS_KEY = "fightcavesnpcs";
	public static final Location[] SPAWN_LOCATIONS = { new Location(2411, 5109), new Location(2413, 5105),
			new Location(2385, 5106), new Location(2380, 5102), new Location(2380, 5073), new Location(2387, 5071),
			new Location(2420, 5082), new Location(2416, 5107), new Location(2412, 5111), new Location(2382, 5108),
			new Location(2378, 5103) };

	public static final void checkForFightCave(Player p, Mob mob) {
		if (p.getController().equals(CONTROLLER)) {
			if (!p.getJadDetails().removeNpc(mob)) {
				p.getClient().queueOutgoingPacket(
						new SendMessage("Mob could not be removed error for Fight Caves, please report this error."));
			}

			if (!p.getJadDetails().isKiln() 
					|| mob.getId() == TokHaarData.NPCS_DETAILS.AVATAR_OF_DESTRUCTION && p.getJadDetails().isKiln()) {
				finish(p, true);
				return;
			}

			if (p.getJadDetails().getKillAmount() == 0) {
				p.getJadDetails().increaseStage();
				startNextWave(p);
			}
		}
	}

	public static void startNextWave(final Player p) {
		p.getClient().queueOutgoingPacket(new SendMessage("The next wave will start in 10 seconds."));

		if (p.getJadDetails().getZ() == 0) {
			p.getJadDetails().setZ(p);
			p.changeZ(p.getJadDetails().getZ());
		}

		TaskQueue.queue(new Task(p, 20) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -2790519201198685002L;

			@Override
			public void execute() {
				final ArrayList<Location> randomizedSpawns = new ArrayList<Location>();
				
				for (Location i : SPAWN_LOCATIONS) {
					randomizedSpawns.add(i);
				}
				
				int c;

				VirtualMobRegion r = new VirtualMobRegion(2411, 5109, 400);

				if (!p.getJadDetails().isKiln()) {
					for (short i : TzharrData.values()[p.getJadDetails().getStage()].getNpcs()) {
						c = Misc.randomNumber(randomizedSpawns.size());
						
						Location l = new Location(randomizedSpawns.get(c));
						randomizedSpawns.remove(c);
						
						l.setZ(p.getJadDetails().getZ());
	
						Mob mob = new Mob(i, false, l, p, false, false, r);
						mob.getFollowing().setIgnoreDistance(true);
	
						mob.getCombat().setAttack(p);
	
						p.getJadDetails().addNpc(mob);
					}
				} else {
					for (short i : TokHaarData.values()[p.getJadDetails().getStage()].getNpcs()) {
						c = Misc.randomNumber(randomizedSpawns.size());
						
						Location l = new Location(randomizedSpawns.get(c));
						randomizedSpawns.remove(c);
						
						l.setZ(p.getJadDetails().getZ());
	
						Mob mob = new Mob(i, false, l, p, false, false, r);
						mob.getFollowing().setIgnoreDistance(true);
	
						mob.getCombat().setAttack(p);
	
						p.getJadDetails().addNpc(mob);
					}
				}

				stop();
			}

			@Override
			public void onStop() {
			}
		});
	}

	public static void init(Player p, boolean kiln) {
		p.send(new SendRemoveInterfaces());
		p.setController(CONTROLLER);
		p.getJadDetails().setKiln(kiln);
		
		if (kiln) {
			if (p.getJadDetails().getZ() == 0) {
				p.getJadDetails().setZ(p);
			}
			
			if (p.getJadDetails().getStage() == 0) {
				p.getJadDetails().setStage(1);
			}
			
			p.teleport(new Location(2413, 5117, p.getJadDetails().getZ()));
			startNextWave(p);
		} else {
			if (!GameConstants.IS_DOUBLE_EXP_WEEKEND) {
				p.getJadDetails().setStage(p.isSuperMember() ? 45 : 32);
			} else {
				p.getJadDetails().setStage(55);
			}

			if (p.getJadDetails().getZ() == 0) {
				p.getJadDetails().setZ(p);
			}

			p.teleport(new Location(2413, 5117, p.getJadDetails().getZ()));
			startNextWave(p);
		}
	}

	public static void loadGame(Player player) {
		player.setController(CONTROLLER);

		if (player.getJadDetails().getStage() != 0)
			startNextWave(player);
	}

	public static final void onLeaveGame(Player player) {
		for (Mob i : player.getJadDetails().getMobs()) {
			if (i != null) {
				i.remove();
			}
		}

		player.getJadDetails().getMobs().clear();

		player.setController(ControllerManager.DEFAULT_CONTROLLER);
	}

	@SuppressWarnings("unused")
	public static void finish(Player player, boolean reward) {
		int round = player.getJadDetails().getStage();
		
		if (player.getJadDetails().isKiln()) {
			if (reward) {
				player.getInventory().addOrCreateGroundItem(19111, 1, false);
				player.getInventory().addOrCreateGroundItem(6529, 16064, true);
				player.getClient().queueOutgoingPacket(new SendMessage("You have braved the Fight Kiln, and survived!"));
			} else {
				player.getClient().queueOutgoingPacket(new SendMessage("You have failed the Fight Kiln."));
			}
		} else {
			if (reward) {
				player.getInventory().addOrCreateGroundItem(6570, 1, false);
				player.getInventory().addOrCreateGroundItem(6529, 16064, true);
				player.getClient().queueOutgoingPacket(new SendMessage("Congratulations, you have completed The Fight Cave!"));
			} else if (GameConstants.IS_DOUBLE_EXP_WEEKEND && round > 61 || round > 50 && !GameConstants.IS_DOUBLE_EXP_WEEKEND) {
				player.getInventory().addOrCreateGroundItem(6529, (round - 50 << 7) + (round - 50) * 10, true);
			} else {
				player.getClient().queueOutgoingPacket(new SendMessage("You must get past wave 50 to get a reward."));
			}
		}
		
		player.teleport(LEAVE);
		onLeaveGame(player);
		player.getJadDetails().reset();
	}
}
