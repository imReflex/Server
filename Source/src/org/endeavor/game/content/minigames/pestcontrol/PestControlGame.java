package org.endeavor.game.content.minigames.pestcontrol;

import java.util.List;

import org.endeavor.engine.utility.Misc;
import org.endeavor.game.GameConstants;
import org.endeavor.game.content.QuestTab;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.content.dialogue.Emotion;
import org.endeavor.game.content.skill.SkillConstants;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.mob.VirtualMobRegion;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.Controller;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.out.impl.SendString;
import org.endeavor.game.entity.player.net.out.impl.SendWalkableInterface;

public class PestControlGame {
	public static final Controller PEST_CONTROLLER = new PestControlController();
	
	private final List<Player> players;
	private Mob voidKnight;
	private final int z;
	private final VirtualMobRegion region;
	
	private final Portal[] portals;
	///XXX:remove doors
	public static final String PEST_DAMAGE_KEY = "pestdamagekey";
	public static final String PEST_GAME_KEY = "pestgamekey";
	
	private int time = 500;
	
	private boolean ended = false;

	public PestControlGame(List<Player> players, int count) {
		this.players = players;
		z = (count << 2);
		region = new VirtualMobRegion();
		
		portals = new Portal[] {
				new Portal(this, PestControlConstants.PORTAL_IDS[0], PestControlConstants.PORTAL_SPAWN_LOCATIONS[0], z),
				new Portal(this, PestControlConstants.PORTAL_IDS[1], PestControlConstants.PORTAL_SPAWN_LOCATIONS[1], z),
				new Portal(this, PestControlConstants.PORTAL_IDS[2], PestControlConstants.PORTAL_SPAWN_LOCATIONS[2], z),
				new Portal(this, PestControlConstants.PORTAL_IDS[3], PestControlConstants.PORTAL_SPAWN_LOCATIONS[3], z),
			};
		
		init();
	}

	public void init() {
		for (Player p : players) {
			p.teleport(new Location(2656 + Misc.randomNumber(4), 2609 + Misc.randomNumber(6), z));
			p.getClient().queueOutgoingPacket(new SendWalkableInterface(20000));
			p.getAttributes().set(PEST_DAMAGE_KEY, (int) 0);
			p.getAttributes().set(PEST_GAME_KEY, this);
			p.setController(PEST_CONTROLLER);
			
			p.getSpecialAttack().setSpecialAmount(100);
			
			DialogueManager.sendNpcChat(p, 7203, Emotion.CALM, 
					"Go, Go, Go, GO!",
					"Defend the void knight and destroy the portals!",
					"You are the only hope!");
		}

		voidKnight = new Mob(region, 7203, false, false, new Location(PestControlConstants.VOID_KNIGHT_SPAWN, z));
		
		voidKnight.getLevels()[SkillConstants.HITPOINTS] = 200;
		voidKnight.getMaxLevels()[SkillConstants.HITPOINTS] = 200;
		voidKnight.getLevels()[SkillConstants.DEFENCE] = 300;
		
		voidKnight.setRespawnable(false);
		
		voidKnight.getAttributes().set(PEST_GAME_KEY, this);
	}

	public void process() {
		time--;
		
		if (time <= 0) {
			end(false);
			return;
		}
		
		if (voidKnight.isDead()) {
			end(false);
			return;
		}
		
		if (!portals[0].isActive() && !portals[1].isActive() 
				&& !portals[2].isActive() && !portals[3].isActive()) {
			end(true);
		}
		
		for (Player p : players) {
			p.getClient().queueOutgoingPacket(new SendString(((time / 100) + 1) + " minutes", 20018));
			p.getClient().queueOutgoingPacket(new SendString("" + voidKnight.getLevels()[SkillConstants.HITPOINTS], 20015));
			
			for (int i = 0; i < 4; i++) {
				boolean dead = portals[i].isDead();
				p.getClient().queueOutgoingPacket(new SendString((dead ? "@red@" : "") + portals[i].getLevels()[SkillConstants.HITPOINTS], 20011 + i));
			}
			
			if (p.getAttributes().get(PEST_DAMAGE_KEY) != null) {
				int damage = p.getAttributes().getInt(PEST_DAMAGE_KEY);
				p.getClient().queueOutgoingPacket(new SendString((damage >= 80 ? "" : "@red@") + p.getAttributes().getInt(PEST_DAMAGE_KEY), 20016));
			}
		}
	}
	
	public void remove(Player p) {
		players.remove(p);
		
		if (players.size() == 0) {
			for (Portal i : portals) {
				i.cleanup();
			}
			
			voidKnight.remove();
			
			PestControl.onGameEnd(this);
		}
	}
	
	public int getAttackers(Player p) {
		int i = 0;
		
		for (Portal k : portals) {
			for (Mob j : k.getPests()) {
				if (j.getCombat().getAttacking() != null && j.getCombat().getAttacking().equals(p)) {
					i++;
				}
			}
		}
		
		return i;
	}

	public void end(boolean success) {
		ended = true;
		
		for (Portal i : portals) {
			i.remove();
		}
		
		voidKnight.remove();
		
		for (Player p : players) {
			p.teleport(new Location(2657, 2639));
			
			p.setController(ControllerManager.DEFAULT_CONTROLLER);
			
			p.getCombat().reset();
			p.getMagic().setVengeanceActive(false);
			p.resetLevels();
			p.curePoison(0);
			
			if (success) {
				if (p.getAttributes().get(PEST_DAMAGE_KEY) != null
						&& p.getAttributes().getInt(PEST_DAMAGE_KEY) >= 80) {
					DialogueManager.sendNpcChat(p, 3789, Emotion.HAPPY_TALK, 
							"You have managed to destroy all the portals!",
							"We've awarded you four Void Knight Commendation",
							"points. Please also accept these coins as a reward.");
					p.getInventory().addOrCreateGroundItem(995, p.getAttributes().getInt(PEST_DAMAGE_KEY) * 6, true);
					p.setPestPoints(p.getPestPoints() + (4 * (GameConstants.IS_DOUBLE_EXP_WEEKEND ? 2 : 1)));
					QuestTab.updatePestPoints(p);
				} else {
					DialogueManager.sendNpcChat(p, 3789, Emotion.CALM, 
							"You were successful but did not contribute enough",
							"to gain points. Try harder next time!");
				}
			} else {
				DialogueManager.sendNpcChat(p, 3789, Emotion.SAD, 
						"You void knight has fallen!",
						"You must defeat them next time!");
			}
			
			p.getAttributes().remove(PEST_DAMAGE_KEY);
			p.getAttributes().remove(PEST_GAME_KEY);
		}
		
		for (Portal i : portals) {
			i.cleanup();
		}
		
		voidKnight.remove();
		
		PestControl.onGameEnd(this);
	}
	
	public Mob getVoidKnight() {
		return voidKnight;
	}

	public VirtualMobRegion getVirtualRegion() {
		return region;
	}
	
	public int getZ() {
		return z;
	}
	
	public List<Player> getPlayers() {
		return players;
	}
	
	public boolean hasEnded() {
		return ended;
	}
}
