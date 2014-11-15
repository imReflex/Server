package org.endeavor.game.entity.player.net.in.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.in.IncomingPacket;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class PublicChatPacket extends IncomingPacket {
	@Override
	public int getMaxDuplicates() {
		return 1;
	}

	@Override
	public void handle(Player player, StreamBuffer.InBuffer in, int opcode, int length) {
		int effects = in.readByte(false, StreamBuffer.ValueType.S);
		int color = in.readByte(false, StreamBuffer.ValueType.S);
		int chatLength = length - 2;
		byte[] text = in.readBytesReverse(chatLength, StreamBuffer.ValueType.A);

		if (!player.getController().canTalk()) {
			player.getClient().queueOutgoingPacket(new SendMessage("You cannot talk right now."));
			return;
		}

		String message = Misc.textUnpack(text, chatLength);
		//System.out.println("PublicChat: " + message);
		/*if (message.length() > 60) {
			return;
		}*/
		
		if (isAskingHowToMakeMoney(message)) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("You can earn money by doing many things including: Bosses, Cutting gems, Thieving,"));
			player.getClient().queueOutgoingPacket(
					new SendMessage("Pking, Merchanting, Killing monsters, Voting, Barrows mini-game, and much more!"));
			player.getClient().queueOutgoingPacket(new SendMessage("Ask a player if you need more help."));
		}

		player.setChatEffects(effects);
		player.setChatColor(color);
		player.setChatText(text);

		if (player.isMuted()) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("You are muted, you will be unmuted in " + player.getRemainingMute() + " days."));
		} else {
			// LoginThread.queuePublicChatLog(player, message);
			player.setChatUpdateRequired(true);
		}
	}

	public static boolean isAskingHowToMakeMoney(String text) {
		text = text.toLowerCase();

		if (((text.contains("money")) || (text.contains("gp")) || (text.contains("gold")))
				&& ((text.contains("make")) || (text.contains("earn")))
				&& ((text.contains("where")) || (text.contains("how")))) {
			return true;
		}

		return false;
	}
}
