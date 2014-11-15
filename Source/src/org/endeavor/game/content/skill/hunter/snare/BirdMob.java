package org.endeavor.game.content.skill.hunter.snare;

import java.util.ArrayList;

import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.skill.hunter.Hunter;
import org.endeavor.game.content.skill.hunter.Trap;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.pathfinding.SimplePathWalker;

public class BirdMob extends Mob {

	/**
	 * 
	 */
	private static final long serialVersionUID = -546214495974255495L;
	private Birds snare;
	private boolean caught = false;
	private Trap trap = null;
	
	public BirdMob(Birds snare, int npcId, boolean walks, Location location) {
		super(npcId, walks, location);
		this.snare = snare;
	}
	
	@Override
	public void onDeath() {
		
	}
	
	@Override
	public void processMovement() {
		if(Misc.randomNumber(10) == 0 && !caught) {
			ArrayList<Trap> traps = Hunter.getSurroundingMobTraps(this.getLocation(), snare.getLevel());
			if(traps.size() > 0) {
				Trap trap = traps.get(Misc.randomNumber(traps.size()));
				if(trap != null && trap.getGameObject() != null) {
					System.out.println("Caught!");
					caught = true;
					SimplePathWalker.walkToNextTile(this, trap.getLocation());
					this.trap = trap;
				}
			}
		}
		if(caught && trap != null && this.getLocation().equals(trap.getLocation())) {
			System.out.println("caught2");
			trap.setTrapCaught(snare.getSnaredCaught());
			this.setDead(true);
		}
	}
	
}
