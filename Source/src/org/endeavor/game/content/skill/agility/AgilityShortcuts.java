package org.endeavor.game.content.skill.agility;

import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class AgilityShortcuts {
	public static boolean click(Player p, int id) {
		switch (id) {
		case 9293:
			if (hasAgilityLevel(p, 50)) {
				if (p.getLocation().getX() <= 2886)
					p.teleport(new Location(2892, 9799));
				else
					p.teleport(new Location(2886, 9799));
			}
			return true;
		case 9294:
			if (hasAgilityLevel(p, 70)) {
				if (p.getLocation().getX() <= 2879)
					p.teleport(new Location(2880, 9813));
				else
					p.teleport(new Location(2878, 9813));
			}
			return true;
		case 4616://to HFTD
			if (hasAgilityLevel(p, 35)) {
				p.teleport(new Location(2596, 3608));
			}
			return true;
		case 4615://from HFTD
			if (hasAgilityLevel(p, 35)) {
				p.teleport(new Location(2598, 3608));
			}
			return true;
			
		case 9327://up first to god wars entrance
			p.getClient().queueOutgoingPacket(new SendMessage("Trying to get to the GWD? It's a little more north.."));
			break;
		
		case 26327://up to god wars entrance
			if (hasAgilityLevel(p, 55)) {
				p.teleport(new Location(2942, 3768));
			}
			break;
		case 26328://down from god wars entrance
			if (hasAgilityLevel(p, 55)) {
				p.teleport(new Location(2950, 3767));
			}
			break;
			
		case 6245://to near godwars
			if (hasAgilityLevel(p, 55)) {
				p.teleport(new Location(2928, 3757));
			}
			break;
			
		case 26323://from near godwars
			if (hasAgilityLevel(p, 55)) {
				if (p.getY() >= 3759) {
					p.teleport(new Location(2928, 3757));
				} else {
					p.teleport(new Location(2928, 3762));
				}
			}
			break;
		}

		return false;
	}

	public static boolean hasAgilityLevel(Player p, int level) {
		if (p.getMaxLevels()[16] >= level) {
			return true;
		}
		p.getClient().queueOutgoingPacket(
				new SendMessage("You need an Agility level of " + level + " to use this shortcut."));
		return false;
	}
}
