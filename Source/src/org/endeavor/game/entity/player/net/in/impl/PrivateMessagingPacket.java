package org.endeavor.game.entity.player.net.in.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.engine.utility.NameUtil;
import org.endeavor.game.content.clans.Clan;
import org.endeavor.game.content.clans.Clans;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.in.IncomingPacket;

public class PrivateMessagingPacket extends IncomingPacket {
	public static final int ADD_FRIEND = 188;
	public static final int REMOVE_FRIEND = 215;
	public static final int ADD_IGNORE = 133;
	public static final int REMOVE_IGNORE = 74;
	public static final int SEND_PM = 126;
	public static final int ENTER_CLAN_CHAT = 76;

	@Override
	public int getMaxDuplicates() {
		return 1;
	}

	@Override
	public void handle(Player player, StreamBuffer.InBuffer in, int opcode, int length) {
		switch (opcode) {
		case 188:
			long name = in.readLong();
			player.getPrivateMessaging().addFriend(name);
			break;
		case 215:
			name = in.readLong();
			player.getPrivateMessaging().removeFriend(name);
			break;
		case 133:
			name = in.readLong();
			player.getPrivateMessaging().addIgnore(name);
			break;
		case 74:
			name = in.readLong();
			player.getPrivateMessaging().removeIgnore(name);
			break;
		case 126:
			name = in.readLong();
			int size = length - 8;
			byte[] message = in.readBytes(size);
			player.getPrivateMessaging().sendPrivateMessage(name, size, message);
			break;
		case 76:
			name = in.readLong();
			Clans.joinChannelForName(player, NameUtil.longToName(name));
		}
	}
}
