package org.endeavor.game.content.minigames.armsrace;

import org.endeavor.game.content.minigames.fightpits.FightPitsConstants;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.GenericWaitingRoomController;
import org.endeavor.game.entity.player.net.out.impl.SendString;
import org.endeavor.game.entity.player.net.out.impl.SendWalkableInterface;

public class ARWaitingController extends GenericWaitingRoomController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2901667849000347142L;

	@Override
	public void onControllerInit(Player player) {
		for (int i = 0; i < FightPitsConstants.FIGHT_PITS_INTERFACE_STRINGS.length; i++) {
			player.getClient().queueOutgoingPacket(
					new SendString("", FightPitsConstants.FIGHT_PITS_INTERFACE_STRINGS[i]));
		}

		player.getClient().queueOutgoingPacket(
				new SendString("Waiting: " + ArmsRaceLobby.getPlayersWaiting(),
						FightPitsConstants.FIGHT_PITS_INTERFACE_STRINGS[0]));

		player.getClient().queueOutgoingPacket(
				new SendString("Time until next game: " + ArmsRaceLobby.getMinutesUntilNextGame(),
						FightPitsConstants.FIGHT_PITS_INTERFACE_STRINGS[1]));

		player.getClient().queueOutgoingPacket(new SendWalkableInterface(17600));
	}

	@Override
	public void onDisconnect(Player p) {
		ArmsRaceLobby.remove(p);
	}

	@Override
	public String toString() {
		return "AR Waiting Room";
	}

	@Override
	public void tick(Player p) {
	}

	@Override
	public boolean canSave() {
		return false;
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
