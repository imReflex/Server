package org.endeavor.engine;

import org.endeavor.game.entity.World;
import org.endeavor.game.entity.player.Player;

public class ShutdownHook extends Thread {

	@Override
	public void run() {
		for(Player player : World.getPlayers()) {
			if(player == null)
				continue;
			player.save();
		}
		System.out.println("Saved all players!");
	}

}
