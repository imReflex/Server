package org.endeavor.game.content.minigames.pestcontrol.impl;

import org.endeavor.engine.utility.Misc;
import org.endeavor.game.GameConstants;
import org.endeavor.game.content.minigames.pestcontrol.Pest;
import org.endeavor.game.content.minigames.pestcontrol.PestControlConstants;
import org.endeavor.game.content.minigames.pestcontrol.PestControlGame;
import org.endeavor.game.entity.Location;

public class Shifter extends Pest {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6336965389101021497L;
	private byte delay = 0;

	public Shifter(Location location, PestControlGame game) {
		super(game, PestControlConstants.SHIFTERS[Misc.randomNumber(PestControlConstants.SHIFTERS.length)], location);
	}

	@Override
	public void tick() {
		if (++delay == 7) {
			if (Misc.getManhattanDistance(getLocation(), getGame().getVoidKnight().getLocation()) > 2) {
				if (!isMovedLastCycle() && getCombat().getAttackTimer() == 0) {
					if (getCombat().getAttacking() != null) {
						if (getCombat().getAttacking().equals(getGame().getVoidKnight())) {
							Location l = GameConstants.getClearAdjacentLocation(getGame().getVoidKnight().getLocation(), getSize(), getGame().getVirtualRegion());
							
							if (l != null) {
								teleport(l);
							}
						} else {
							getCombat().setAttack(getGame().getVoidKnight());
						}
					} else {
						getCombat().setAttack(getGame().getVoidKnight());
					}
				}
			}
			
			delay = 0;
		}
	}
}
