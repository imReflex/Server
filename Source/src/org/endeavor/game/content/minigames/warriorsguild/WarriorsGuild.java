package org.endeavor.game.content.minigames.warriorsguild;

import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public final class WarriorsGuild {
	public static boolean canEnter(Player player) {
		byte attackLevel = (byte) player.getSkill().getLevels()[0];
		byte strengthLevel = (byte) player.getSkill().getLevels()[2];
		if ((attackLevel + strengthLevel == 130) || (attackLevel == 99) || (strengthLevel == 99))
			player.getClient().queueOutgoingPacket(new SendMessage("Welcome to the Warriors Guild."));
		else {
			player.getClient().queueOutgoingPacket(new SendMessage("You cannot enter..."));
		}
		return true;
	}
}
