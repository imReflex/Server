package org.endeavor.game.content.minigames.pestcontrol.impl;

import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.combat.Hit;
import org.endeavor.game.content.minigames.pestcontrol.Pest;
import org.endeavor.game.content.minigames.pestcontrol.PestControlConstants;
import org.endeavor.game.content.minigames.pestcontrol.PestControlGame;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;

public class Splatter extends Pest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7057084365525547966L;

	public Splatter(Location location, PestControlGame game) {
		super(game, PestControlConstants.SPLATTERS[Misc.randomNumber(PestControlConstants.SPLATTERS.length)], location);
	}
	
	@Override
	public void onDeath() {
		if (Misc.getManhattanDistance(getGame().getVoidKnight().getLocation(), getLocation()) <= 2) {
			getGame().getVoidKnight().hit(new Hit(1 + Misc.randomNumber(5)));
		}
		
		for (Player k : getGame().getPlayers()) {
			if (Misc.getManhattanDistance(k.getLocation(), getLocation()) <= 2) {
				k.hit(new Hit(1 + Misc.randomNumber(5)));
			}
		}
	}

	@Override
	public void tick() {
		if (Misc.getManhattanDistance(getGame().getVoidKnight().getLocation(), getLocation()) <= 2) {
			getLevels()[3] = 0;
			checkForDeath();
		}
	}

}
