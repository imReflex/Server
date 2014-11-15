package org.endeavor.game.content;

import org.endeavor.GameSettings;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerConstants;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class Yelling {
	public static final String YELL_COOLDOWN_KEY = "yellcooldown";

	public static void yell(Player p, String message) {
		int rights = (p.getRights() == 4) || (PlayerConstants.isOwner(p)) ? 2 : p.getRights() == 3 ? 1 : 0;

		String send = "";
		
		if(p.getRights() == 0) {
			if(p.getMemberRank() > 0) {
				if(p.getMemberRank() == 1) {
					send = "[Member]<img=" + p.getCrownId() + ">" + p.getUsername() + ": " + message;
				} else if(p.getMemberRank() == 2) {
					send = "[<col=ff0000>Super Member</col>]<img=" + p.getCrownId() + ">" + p.getUsername() + ": <col=ff0000>" + message + "</col>";
				} else if(p.getMemberRank() == 3) {
					send = "[<col=00ffff><shad=000000>Respected Member</shad></col>]<img=" + p.getCrownId() + ">" + p.getUsername() + ": <col=00ffff><shad=000000>" + message + "</shad></col>";
				}
			}
		} else if(p.getRights() == 1) {
			send = "[<col=000099>Moderator</col>]<img=" + p.getCrownId() + ">" + p.getUsername() + ": <col=000099>" + message + "</col>";
		} else if(p.getRights() == 2) {
			send = "[<col=ff0000>Administrator</col>]<img=" + p.getCrownId() + ">" + p.getUsername() + ": <col=ff0000>" + message + "</col>";
		} else if(p.getRights() == 3) {
			send = "[<col=ff0000><shad=000000>Owner</shad></col>]<img=" + p.getCrownId() + ">" + p.getUsername() + ": <col=ff0000><shad=000000>" + message + "</shad></col>";
		}
		
		if (p.isMuted()) {
			p.getClient().queueOutgoingPacket(new SendMessage("You are muted."));
			return;
		}

		if (p.isYellMuted()) {
			p.getClient().queueOutgoingPacket(new SendMessage("You are not allowed to yell."));
			return;
		}

		if ((p.getRights() == 0) && (!p.isMember()) && !PlayerConstants.isOwner(p) && !GameSettings.DEV_MODE) {
			p.getClient().queueOutgoingPacket(new SendMessage("You must be a member to yell."));
			return;
		}
		
		if (message.contains("<")) {
			p.getClient().queueOutgoingPacket(new SendMessage("You cannot use text arguments when yelling."));
			return;
		}

		if (p.getRights() < 3) {
			if (p.getAttributes().get("yellcooldown") == null) {
				p.getAttributes().set("yellcooldown", Long.valueOf(System.currentTimeMillis()));
			} else if (System.currentTimeMillis() - ((Long) p.getAttributes().get("yellcooldown")).longValue() < 5000L) {
				p.getClient().queueOutgoingPacket(new SendMessage("You must wait 5 seconds in between yells."));
				return;
			}

			p.getAttributes().set("yellcooldown", Long.valueOf(System.currentTimeMillis()));
		}

		for (Player i : World.getPlayers())
			if (i != null)
				i.getClient().queueOutgoingPacket(new SendMessage(send));
	}
}
