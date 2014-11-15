package org.endeavor.game.content.skill.hunter;

import java.util.ArrayList;
import java.util.HashMap;

import org.endeavor.engine.cache.map.Region;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.skill.SkillConstants;
import org.endeavor.game.content.skill.hunter.snare.BirdMob;
import org.endeavor.game.content.skill.hunter.snare.BirdSnare;
import org.endeavor.game.content.skill.hunter.snare.Birds;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

/**
 * 
 * @author Allen K.
 *
 */
public class Hunter {

	private Player player;
	
	public Hunter(Player player) {
		this.player = player;
	}
	
	private transient static ArrayList<Trap> globalTraps = new ArrayList<Trap>();
	
	public static ArrayList<Trap> getSurroundingMobTraps(Location location, int levelRequired) {
		ArrayList<Trap> availableTraps = new ArrayList<Trap>();
		for(Trap trap : globalTraps) {
			if(Misc.getExactDistance(trap.getLocation(), location) <= 6 && !trap.isCaught() && !trap.isBroke()) {
				if(trap.getOwner().getSkill().getLevels()[SkillConstants.HUNTER] >= levelRequired) {
					availableTraps.add(trap);
				}
			}
		}
		return availableTraps;
	}
	
	/**
	 * Check to see if the global traps list contains a trap for 
	 * 	the given location.
	 * @param location
	 * @return
	 */
	public static boolean containsTrap(Location location) {
		for(Trap trap : globalTraps)
			if(trap.getLocation().equals(location)) {
				System.out.println("Same Location: " + trap.getLocation().getX() + "," + trap.getLocation().getY() + " -- " + location.getX() + "," + location.getY());
				return true;
			}
		return false;
	}
	
	/**
	 * Gets the Trap for a given Location
	 * @param location
	 * @return
	 */
	public static Trap getTrapForLocation(Location location) {
		for(Trap trap : globalTraps)
			if(trap.getLocation().equals(location))
				return trap;
		return null;
	}
	
	/**
	 * Handles hunter objects.
	 * @param player
	 * @param objectId
	 * @param x
	 * @param y
	 * @return
	 */
	public static boolean clickObject(Player player, int objectId, int x, int y) {
		switch(objectId) {
		case 19175:
		case 19174:
			Trap trap = Hunter.getTrapForLocation(new Location(x,y,0));
			if(trap != null) {
				player.getHunter().removeTrap(trap);
			} else {
				player.send(new SendMessage("No trap found."));
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Handles hunter items.
	 * @param player
	 * @param id
	 * @return
	 */
	public static boolean operateItem(Player player, int id, Location location) {
		switch(id) {
		case 10006:
			System.out.println("Laying snare!");
			player.getHunter().addTrap(new BirdSnare(player, location), location);
			return true;
		}
		return false;
	}
	
	/**
	 * Initialize the hunter mobs.
	 */
	public static void spawnHunterMobs() {
		for(Birds bird : Birds.values()) {
			for(Location location : bird.getSpawnPoints())
				new BirdMob(bird, bird.getNpcId(), true, location);
		}
	}
	
	/**
	 * Get the max trap amount for a given level.
	 * @param level
	 * @return
	 */
	public static int getTrapsForLevel(int level) {
		if(level < 20)
			return 1;
		if(level < 40)
			return 2;
		if(level < 60)
			return 3;
		if(level < 80)
			return 4;
		return 5;
	}
	
	public int getTrapCount() {
		int count = 0;
		for(Trap trap : globalTraps)
			if(trap.getOwner() == player)
				count++;
		return count;
	}
	
	/**
	 * Add a trap at a given location.
	 * @param trap
	 * @param location
	 */
	public void addTrap(Trap trap, Location location) {
		if(trap.canLayTrap()) {
			System.out.println("Trap added!");
			trap.layTrap();
			globalTraps.add(trap);
		}
	}
	
	/**
	 * Remove a trap.
	 * @param trap
	 */
	public void removeTrap(Trap trap) {
		if(trap.getOwner() == player) {
			trap.pickupTrap();
		} else {
			player.send(new SendMessage("This is not your trap."));
		}
	}
	
	public boolean isTrapRemoved(Trap trap) {
		return globalTraps.remove(trap);
	}
	
	/**
	 * Safely remove all traps.
	 */
	public void removeAllTraps() {
		/**
		 * Must do it this way for thread safety.
		 * 	Or else the arraylist will mess up.
		 */
		ArrayList<Integer> indices = new ArrayList<Integer>();
		for(int index = 0; index < globalTraps.size(); index++) {
			Trap trap = globalTraps.get(index);
			if(trap.getOwner() == player)
				indices.add(index);
		}
		if(indices.size() > 0) {
			for(int index : indices)
				globalTraps.remove(index);
		}
	}
	
	public void walk() {
		if (!Region.getRegion(player.getLocation().getX(), player.getLocation().getY()).blockedWest(
				player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())) {
			player.getMovementHandler().walkTo(-1, 0);
		} else if (!Region.getRegion(player.getLocation().getX(), player.getLocation().getY()).blockedEast(
				player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()))
			player.getMovementHandler().walkTo(1, 0);
	}
	
	public boolean isSuccess() {
		
		return true;
	}
	
}
