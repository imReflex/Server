package org.endeavor.game.entity.player.net.in.command.impl;

import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.in.command.Command;

public class SendPacketCommand implements Command {
	@Override
	public void handleCommand(Player player, String command) {
		String[] args = command.split(" ");
		String str;
		switch ((str = args[0]).hashCode()) {
		case -1468048853:
			if (str.equals("altconfig"))
				;
			break;
		case -1354792126:
			if (str.equals("config"))
				;
			break;
		case -891985903:
			if (str.equals("string"))
				;
			break;
		case -350344160:
			if (str.equals("resetcam"))
				;
			break;
		case 3536962:
			if (str.equals("spin"))
				;
			break;
		case 109627663:
			if (str.equals("sound"))
				break;
			break;
		case 109765032:
			if (str.equals("still"))
				;
			break;
		case 197531829:
			if (str.equals("mapstate"))
				;
			break;
		case 502623545:
			if (str.equals("interface"))
				;
			break;
		}
	}

	@Override
	public int rightsRequired() {
		return 7;
	}

	@Override
	public boolean meetsRequirements(Player player) {
		return true;
	}
}
