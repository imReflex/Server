package org.endeavor.game.content.minigames.dungeoneering;

import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.GenericWaitingRoomController;
import org.endeavor.game.entity.player.net.out.impl.SendString;
import org.endeavor.game.entity.player.net.out.impl.SendWalkableInterface;

public class DungLobbyController extends GenericWaitingRoomController {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6681054430999368987L;

	@Override
	public void onControllerInit(Player player) {
		player.getClient().queueOutgoingPacket(new SendString("Next game: " + DungLobby.getSecondsToNextGame(), 17601));
		player.getClient().queueOutgoingPacket(new SendString("Players ready: " + DungLobby.getPlayersReady(), 17602));
		player.getClient().queueOutgoingPacket(new SendString("", 17603));
		player.getClient().queueOutgoingPacket(new SendString("", 17604));
		player.getClient().queueOutgoingPacket(new SendString("", 17605));

		player.getClient().queueOutgoingPacket(new SendWalkableInterface(17600));
	}

	@Override
	public void onDisconnect(Player p) {
		p.teleport(new Location(1832, 5215));
	}

	@Override
	public String toString() {
		return "Dungeoneering waiting room";
	}

	@Override
	public void tick(Player p) {
		p.getClient().queueOutgoingPacket(new SendString("Next game: " + DungLobby.getSecondsToNextGame(), 17601));
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
