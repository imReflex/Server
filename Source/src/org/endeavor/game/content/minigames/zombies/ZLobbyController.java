package org.endeavor.game.content.minigames.zombies;

import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.GenericWaitingRoomController;
import org.endeavor.game.entity.player.net.out.impl.SendString;
import org.endeavor.game.entity.player.net.out.impl.SendWalkableInterface;

public class ZLobbyController extends GenericWaitingRoomController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1424872030040117490L;

	@Override
	public void onControllerInit(Player player) {
		player.getClient().queueOutgoingPacket(new SendWalkableInterface(17600));
		player.getClient().queueOutgoingPacket(new SendString("", 17603));
		player.getClient().queueOutgoingPacket(new SendString("", 17604));
		player.getClient().queueOutgoingPacket(new SendString("", 17605));
	}

	@Override
	public void onDisconnect(Player player) {
		player.teleport(new Location(3486, 3246));
		ZombiesLobby.remove(player);
	}

	@Override
	public String toString() {
		return "Zombies Lobby";
	}

	@Override
	public void tick(Player player) {
		
	}

	@Override
	public boolean onClick(Player player, int buttonID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onObject(Player player, int objectID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onNpc(Player player, int npcID) {
		// TODO Auto-generated method stub
		return false;
	}

}
