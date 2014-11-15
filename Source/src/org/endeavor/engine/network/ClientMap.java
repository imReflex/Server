package org.endeavor.engine.network;

import org.endeavor.game.entity.World;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.Client;

public class ClientMap {

	private ClientMap() {
	}

	public static boolean allow(Client client) {
		byte am = 0;

		for (Player p : World.getPlayers()) {
			if (p != null && p.getClient().getHost() != null && p.getClient().getHost().equals(client.getHost())) {
				am++;
			}
		}

		return am < 4;
	}

}
