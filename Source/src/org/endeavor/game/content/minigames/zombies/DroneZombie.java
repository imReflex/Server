package org.endeavor.game.content.minigames.zombies;

import org.endeavor.engine.utility.Misc;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.mob.VirtualMobRegion;

public class DroneZombie extends Mob {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7093958636798955761L;
	private final int z;
	private final ZombiesGame game;
	
	private static final int[][] MOB_IDS = {
		{},//difficulty 1
		{},//difficulty 2
		{},//difficulty 3
		{},//difficulty 4
	};
	
	private static final Location[] SPAWN_INDEXES = {
		
	};
	
	public DroneZombie(ZombiesGame game, VirtualMobRegion region, int z, int difficulty, int spawnIndex) {
		super(MOB_IDS[difficulty][Misc.randomNumber(MOB_IDS[difficulty].length)], true, new Location(SPAWN_INDEXES[spawnIndex]).setZ(z), null, false, false, region);
		this.z = z;
		this.game = game;
	}
	
	@Override
	public void doAliveMobProcessing() {
		
	}
	
	@Override 
	public void onDeath() {
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
