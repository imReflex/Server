package org.endeavor.game.content.minigames.zombies;

import java.util.ArrayList;

import org.endeavor.engine.utility.Misc;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;

public class ZombiesGame {
	
	private final ArrayList<Player> team = new ArrayList<Player>();
	
	private final int z;
	
	public ZombiesGame(Player[] players) {
		for (Player p : players) {
			if (p != null) {
				team.add(p);
			}
		}
		
		z = team.get(0).getIndex() + Misc.randomNumber(8493) * 4;
		
		for (Player p : team) {
			p.teleport(new Location(3481 + Misc.randomNumber(5), 3237 + Misc.randomNumber(5)));
		}
		
		//spawn shit
	}
	
	

}
