package org.endeavor.game.content.skill.runecrafting;

import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;

public class AbyssObjects {
	public static boolean clickObject(Player player, int id) {
		switch (id) {
		case 7134:
			player.teleport(new Location(2281, 4837));
			return true;
		case 7135:
			player.teleport(new Location(2464, 4818));
			return true;
		case 7136:
			player.teleport(new Location(2208, 4830));
			return true;
		case 7137:
			player.teleport(new Location(3494, 4832));
			return true;
		case 7139:
			player.teleport(new Location(2841, 4829));
			return true;
		case 7140:
			player.teleport(new Location(2793, 4828));
			return true;
		case 7130:
			player.teleport(new Location(2655, 4830));
			return true;
		case 7129:
			player.teleport(new Location(2577, 4846));
			return true;
		case 7141:
			player.teleport(new Location(2468, 4889, 1));
			return true;
		case 7132:
			player.teleport(new Location(2162, 4833));
			return true;
		case 7133:
			player.teleport(new Location(2400, 4835));
			return true;
		case 7131:
			player.teleport(new Location(2521, 4834));
			return true;
		case 7138:
		}
		return false;
	}
}
