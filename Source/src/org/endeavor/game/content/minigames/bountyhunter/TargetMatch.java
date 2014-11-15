package org.endeavor.game.content.minigames.bountyhunter;

import org.endeavor.game.entity.player.Player;

/**
 * 
 * @author Allen K.
 *
 */
public class TargetMatch {

	private Player targetOne;
	private Player targetTwo;
	
	public TargetMatch(Player targetOne, Player targetTwo) {
		this.targetOne = targetOne;
		this.targetTwo = targetTwo;
	}
	
	public boolean contains(Player player) {
		return targetOne == player || targetTwo == player;
	}
	
	public Player getTargetForPlayer(Player player) {
		if(targetOne == player)
			return targetTwo;
		if(targetTwo == player)
			return targetOne;
		return null;
	}
	
}
