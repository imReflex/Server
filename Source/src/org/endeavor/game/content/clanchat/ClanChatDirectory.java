package org.endeavor.game.content.clanchat;

import java.util.LinkedList;
import java.util.List;

import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class ClanChatDirectory {
	
	private static final List<ClanChatChannel> openChannels = new LinkedList<ClanChatChannel>();
	
	public static ClanChatChannel enterChat(Player player, String clanName) {
		ClanChatChannel clanChannel = null;

		if (openChannels.size() > 0) {
			for (ClanChatChannel clan : openChannels) {
				if (clan != null) {
					if (clan.getName().equalsIgnoreCase(clanName)) {
						clanChannel = clan;
						break;
					}
				}
			}
		}
		
		if (clanChannel == null) {
			clanChannel = new ClanChatChannel(clanName, player);
		}

		if (!clanChannel.add(player)) {
			player.getClient().queueOutgoingPacket(new SendMessage("This chat is full!"));
			return null;
		}

		return clanChannel;
	}

	public static List<ClanChatChannel> getOpenChannels() {
		return openChannels;
	}
	
	

}
