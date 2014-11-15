package org.endeavor.game.content.minigames.godwars;

import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.DefaultController;
import org.endeavor.game.entity.player.net.out.impl.SendWalkableInterface;

public class GodWarsController extends DefaultController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7632415381673941815L;

	@Override
	public void onControllerInit(Player p) {
		p.getClient().queueOutgoingPacket(new SendWalkableInterface(21346));

		for (int i = 0; i < 4; i++)
			p.getMinigames().updateGWKC(i);
	}
}
