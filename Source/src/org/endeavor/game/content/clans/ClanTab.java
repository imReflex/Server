package org.endeavor.game.content.clans;

import org.endeavor.engine.utility.NameUtil;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendString;

/**
 * 
 * @author Allen K.
 *
 */
public final class ClanTab {

	private final static int INTERFACE_ID = 0;
	private final static int LEAVE_JOIN_BUTTON = 0;
	private final static int MANAGE_BUTTON = 0;
	private final static int START_INDEX = 18144;
	private final static int CLAN_TITLE = 18139;
	private final static int CLAN_OWNER = 18140;
	
	public static void updateClanTab(ClanChannel channel, ClanMember member) {
		Player player = World.getPlayerByName(member.getPlayerUsername());
		if(player == null)
			return;
		player.getClient().queueOutgoingPacket(new SendString("Talking in: @yel@" + 
				NameUtil.uppercaseFirstLetter(channel.getClan().getSettings().getTitle()), CLAN_TITLE));
		player.getClient().queueOutgoingPacket(new SendString("Owner: " + channel.getClan().getOwner(), CLAN_OWNER));
		int index = 0;
		for(ClanMember clanMember : channel.getChannelPlayers().values()) {
			Player clanPlayer = World.getPlayerByName(clanMember.getPlayerUsername());
			if(clanPlayer == null) {
				System.out.println(clanMember.getPlayerUsername() + " is null.");
				continue;
			}
			//System.out.println(clanMember.getRank() + " - " + clanMember.getRank().getPrefix());
			player.getClient().queueOutgoingPacket(new SendString(clanMember.getRank().getPrefix() + ""
					+  clanPlayer.getUsername(), START_INDEX + index));
			index += 1;
		}
		if(index > (START_INDEX + 98))
			return;
		int iIndex = START_INDEX + index;
		for(int i = iIndex; i <= (START_INDEX + 98); i++)
			player.send(new SendString("[REG]", i));
	}
	
	public static void clearClanTab(Player player) {
		player.send(new SendString("Talking in: Not in clan", CLAN_TITLE));
		player.send(new SendString("Owner: None", CLAN_OWNER));
		player.send(new SendString("Join Chat", 18135));
		for(int i = START_INDEX; i <= START_INDEX + 99; i++)
			player.send(new SendString("[REG]", i));
	}
	
}
