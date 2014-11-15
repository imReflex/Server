package org.endeavor.game.content.dialogue;

import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendChatInterface;
import org.endeavor.game.entity.player.net.out.impl.SendModelAnimation;
import org.endeavor.game.entity.player.net.out.impl.SendNPCDialogueHead;
import org.endeavor.game.entity.player.net.out.impl.SendPlayerDialogueHead;
import org.endeavor.game.entity.player.net.out.impl.SendString;

public class DialogueManager {
	public static void sendInformationBox(Player player, String title, String line1, String line2, String line3,
			String line4) {
		player.getClient().queueOutgoingPacket(new SendString(title, 6180));
		player.getClient().queueOutgoingPacket(new SendString(line1, 6181));
		player.getClient().queueOutgoingPacket(new SendString(line2, 6182));
		player.getClient().queueOutgoingPacket(new SendString(line3, 6183));
		player.getClient().queueOutgoingPacket(new SendString(line4, 6184));
		player.getClient().queueOutgoingPacket(new SendChatInterface(6179));
	}

	public static void sendOption(Player player, String... options) {
		if (options.length < 2) {
			return;
		}
		switch (options.length) {
		case 1:
			throw new IllegalArgumentException("1 option is not possible! (DialogueManager.java)");
		case 2:
			player.getClient().queueOutgoingPacket(new SendString(options[0], 2461));
			player.getClient().queueOutgoingPacket(new SendString(options[1], 2462));
			player.getClient().queueOutgoingPacket(new SendChatInterface(2459));
			break;
		case 3:
			player.getClient().queueOutgoingPacket(new SendString(options[0], 2471));
			player.getClient().queueOutgoingPacket(new SendString(options[1], 2472));
			player.getClient().queueOutgoingPacket(new SendString(options[2], 2473));
			player.getClient().queueOutgoingPacket(new SendChatInterface(2469));
			break;
		case 4:
			player.getClient().queueOutgoingPacket(new SendString(options[0], 2482));
			player.getClient().queueOutgoingPacket(new SendString(options[1], 2483));
			player.getClient().queueOutgoingPacket(new SendString(options[2], 2484));
			player.getClient().queueOutgoingPacket(new SendString(options[3], 2485));
			player.getClient().queueOutgoingPacket(new SendChatInterface(2480));
			break;
		case 5:
			player.getClient().queueOutgoingPacket(new SendString(options[0], 2494));
			player.getClient().queueOutgoingPacket(new SendString(options[1], 2495));
			player.getClient().queueOutgoingPacket(new SendString(options[2], 2496));
			player.getClient().queueOutgoingPacket(new SendString(options[3], 2497));
			player.getClient().queueOutgoingPacket(new SendString(options[4], 2498));
			player.getClient().queueOutgoingPacket(new SendChatInterface(2492));
		}
	}

	public static void sendStatement(Player player, String... lines) {
		switch (lines.length) {
		case 1:
			player.getClient().queueOutgoingPacket(new SendString(lines[0], 357));
			player.getClient().queueOutgoingPacket(new SendChatInterface(356));
			break;
		case 2:
			player.getClient().queueOutgoingPacket(new SendString(lines[0], 360));
			player.getClient().queueOutgoingPacket(new SendString(lines[1], 361));
			player.getClient().queueOutgoingPacket(new SendChatInterface(359));
			break;
		case 3:
			player.getClient().queueOutgoingPacket(new SendString(lines[0], 364));
			player.getClient().queueOutgoingPacket(new SendString(lines[1], 365));
			player.getClient().queueOutgoingPacket(new SendString(lines[2], 366));
			player.getClient().queueOutgoingPacket(new SendChatInterface(363));
			break;
		case 4:
			player.getClient().queueOutgoingPacket(new SendString(lines[0], 369));
			player.getClient().queueOutgoingPacket(new SendString(lines[1], 370));
			player.getClient().queueOutgoingPacket(new SendString(lines[2], 371));
			player.getClient().queueOutgoingPacket(new SendString(lines[3], 372));
			player.getClient().queueOutgoingPacket(new SendChatInterface(368));
			break;
		case 5:
			player.getClient().queueOutgoingPacket(new SendString(lines[0], 375));
			player.getClient().queueOutgoingPacket(new SendString(lines[1], 376));
			player.getClient().queueOutgoingPacket(new SendString(lines[2], 377));
			player.getClient().queueOutgoingPacket(new SendString(lines[3], 378));
			player.getClient().queueOutgoingPacket(new SendString(lines[4], 379));
			player.getClient().queueOutgoingPacket(new SendChatInterface(374));
		}
	}

	public static void sendTimedStatement(Player player, String... lines) {
		switch (lines.length) {
		case 1:
			player.getClient().queueOutgoingPacket(new SendString(lines[0], 12789));
			player.getClient().queueOutgoingPacket(new SendChatInterface(12788));
			break;
		case 2:
			player.getClient().queueOutgoingPacket(new SendString(lines[0], 12791));
			player.getClient().queueOutgoingPacket(new SendString(lines[1], 12792));
			player.getClient().queueOutgoingPacket(new SendChatInterface(12790));
			break;
		case 3:
			player.getClient().queueOutgoingPacket(new SendString(lines[0], 12794));
			player.getClient().queueOutgoingPacket(new SendString(lines[1], 12795));
			player.getClient().queueOutgoingPacket(new SendString(lines[2], 12796));
			player.getClient().queueOutgoingPacket(new SendChatInterface(12793));
			break;
		case 4:
			player.getClient().queueOutgoingPacket(new SendString(lines[0], 12798));
			player.getClient().queueOutgoingPacket(new SendString(lines[1], 12799));
			player.getClient().queueOutgoingPacket(new SendString(lines[2], 12800));
			player.getClient().queueOutgoingPacket(new SendString(lines[3], 12801));
			player.getClient().queueOutgoingPacket(new SendChatInterface(12797));
			break;
		case 5:
			player.getClient().queueOutgoingPacket(new SendString(lines[0], 12803));
			player.getClient().queueOutgoingPacket(new SendString(lines[1], 12804));
			player.getClient().queueOutgoingPacket(new SendString(lines[2], 12805));
			player.getClient().queueOutgoingPacket(new SendString(lines[3], 12806));
			player.getClient().queueOutgoingPacket(new SendString(lines[4], 12807));
			player.getClient().queueOutgoingPacket(new SendChatInterface(12802));
		}
	}

	public static void sendNpcChat(Player player, int npcId, Emotion emotion, String... lines) {
		String npcName = GameDefinitionLoader.getNpcDefinition(npcId).getName();
		switch (lines.length) {
		case 1:
			player.getClient().queueOutgoingPacket(new SendModelAnimation(4883, emotion.getEmoteId()));
			player.getClient().queueOutgoingPacket(new SendString(npcName, 4884));
			player.getClient().queueOutgoingPacket(new SendString(lines[0], 4885));
			player.getClient().queueOutgoingPacket(new SendNPCDialogueHead(npcId, 4883));
			player.getClient().queueOutgoingPacket(new SendChatInterface(4882));
			break;
		case 2:
			player.getClient().queueOutgoingPacket(new SendModelAnimation(4888, emotion.getEmoteId()));
			player.getClient().queueOutgoingPacket(new SendString(npcName, 4889));
			player.getClient().queueOutgoingPacket(new SendString(lines[0], 4890));
			player.getClient().queueOutgoingPacket(new SendString(lines[1], 4891));
			player.getClient().queueOutgoingPacket(new SendNPCDialogueHead(npcId, 4888));
			player.getClient().queueOutgoingPacket(new SendChatInterface(4887));
			break;
		case 3:
			player.getClient().queueOutgoingPacket(new SendModelAnimation(4894, emotion.getEmoteId()));
			player.getClient().queueOutgoingPacket(new SendString(npcName, 4895));
			player.getClient().queueOutgoingPacket(new SendString(lines[0], 4896));
			player.getClient().queueOutgoingPacket(new SendString(lines[1], 4897));
			player.getClient().queueOutgoingPacket(new SendString(lines[2], 4898));
			player.getClient().queueOutgoingPacket(new SendNPCDialogueHead(npcId, 4894));
			player.getClient().queueOutgoingPacket(new SendChatInterface(4893));
			break;
		case 4:
			player.getClient().queueOutgoingPacket(new SendModelAnimation(4901, emotion.getEmoteId()));
			player.getClient().queueOutgoingPacket(new SendString(npcName, 4902));
			player.getClient().queueOutgoingPacket(new SendString(lines[0], 4903));
			player.getClient().queueOutgoingPacket(new SendString(lines[1], 4904));
			player.getClient().queueOutgoingPacket(new SendString(lines[2], 4905));
			player.getClient().queueOutgoingPacket(new SendString(lines[3], 4906));
			player.getClient().queueOutgoingPacket(new SendNPCDialogueHead(npcId, 4901));
			player.getClient().queueOutgoingPacket(new SendChatInterface(4900));
		}
	}

	public static void sendTimedNpcChat(Player player, int npcId, Emotion emotion, String... lines) {
		String npcName = GameDefinitionLoader.getNpcDefinition(npcId).getName();
		switch (lines.length) {
		case 2:
			player.getClient().queueOutgoingPacket(new SendModelAnimation(12379, emotion.getEmoteId()));
			player.getClient().queueOutgoingPacket(new SendString(npcName, 12380));
			player.getClient().queueOutgoingPacket(new SendString(lines[0], 12381));
			player.getClient().queueOutgoingPacket(new SendString(lines[1], 12382));
			player.getClient().queueOutgoingPacket(new SendNPCDialogueHead(npcId, 12379));
			player.getClient().queueOutgoingPacket(new SendChatInterface(12378));
			break;
		case 3:
			player.getClient().queueOutgoingPacket(new SendModelAnimation(12384, emotion.getEmoteId()));
			player.getClient().queueOutgoingPacket(new SendString(npcName, 12385));
			player.getClient().queueOutgoingPacket(new SendString(lines[0], 12386));
			player.getClient().queueOutgoingPacket(new SendString(lines[1], 12387));
			player.getClient().queueOutgoingPacket(new SendString(lines[2], 12388));
			player.getClient().queueOutgoingPacket(new SendNPCDialogueHead(npcId, 12384));
			player.getClient().queueOutgoingPacket(new SendChatInterface(12383));
			break;
		case 4:
			player.getClient().queueOutgoingPacket(new SendModelAnimation(11892, emotion.getEmoteId()));
			player.getClient().queueOutgoingPacket(new SendString(npcName, 11893));
			player.getClient().queueOutgoingPacket(new SendString(lines[0], 11894));
			player.getClient().queueOutgoingPacket(new SendString(lines[1], 11895));
			player.getClient().queueOutgoingPacket(new SendString(lines[2], 11896));
			player.getClient().queueOutgoingPacket(new SendString(lines[3], 11897));
			player.getClient().queueOutgoingPacket(new SendNPCDialogueHead(npcId, 11892));
			player.getClient().queueOutgoingPacket(new SendChatInterface(11891));
		}
	}

	public static void sendPlayerChat(Player player, Emotion emotion, String... lines) {
		switch (lines.length) {
		case 1:
			player.getClient().queueOutgoingPacket(new SendModelAnimation(969, emotion.getEmoteId()));
			player.getClient().queueOutgoingPacket(new SendString(player.getUsername(), 970));
			player.getClient().queueOutgoingPacket(new SendString(lines[0], 971));
			player.getClient().queueOutgoingPacket(new SendPlayerDialogueHead(969));
			player.getClient().queueOutgoingPacket(new SendChatInterface(968));
			break;
		case 2:
			player.getClient().queueOutgoingPacket(new SendModelAnimation(974, emotion.getEmoteId()));
			player.getClient().queueOutgoingPacket(new SendString(player.getUsername(), 975));
			player.getClient().queueOutgoingPacket(new SendString(lines[0], 976));
			player.getClient().queueOutgoingPacket(new SendString(lines[1], 977));
			player.getClient().queueOutgoingPacket(new SendPlayerDialogueHead(974));
			player.getClient().queueOutgoingPacket(new SendChatInterface(973));
			break;
		case 3:
			player.getClient().queueOutgoingPacket(new SendModelAnimation(980, emotion.getEmoteId()));
			player.getClient().queueOutgoingPacket(new SendString(player.getUsername(), 981));
			player.getClient().queueOutgoingPacket(new SendString(lines[0], 982));
			player.getClient().queueOutgoingPacket(new SendString(lines[1], 983));
			player.getClient().queueOutgoingPacket(new SendString(lines[2], 984));
			player.getClient().queueOutgoingPacket(new SendPlayerDialogueHead(980));
			player.getClient().queueOutgoingPacket(new SendChatInterface(979));
			break;
		case 4:
			player.getClient().queueOutgoingPacket(new SendModelAnimation(987, emotion.getEmoteId()));
			player.getClient().queueOutgoingPacket(new SendString(player.getUsername(), 988));
			player.getClient().queueOutgoingPacket(new SendString(lines[0], 989));
			player.getClient().queueOutgoingPacket(new SendString(lines[1], 990));
			player.getClient().queueOutgoingPacket(new SendString(lines[2], 991));
			player.getClient().queueOutgoingPacket(new SendString(lines[3], 992));
			player.getClient().queueOutgoingPacket(new SendPlayerDialogueHead(987));
			player.getClient().queueOutgoingPacket(new SendChatInterface(986));
		}
	}

	public static void sendTimedPlayerChat(Player player, Emotion emotion, String... lines) {
		switch (lines.length) {
		case 1:
			player.getClient().queueOutgoingPacket(new SendModelAnimation(12774, emotion.getEmoteId()));
			player.getClient().queueOutgoingPacket(new SendString(player.getUsername(), 12775));
			player.getClient().queueOutgoingPacket(new SendString(lines[0], 12776));
			player.getClient().queueOutgoingPacket(new SendPlayerDialogueHead(12774));
			player.getClient().queueOutgoingPacket(new SendChatInterface(12773));
			break;
		case 2:
			player.getClient().queueOutgoingPacket(new SendModelAnimation(12778, emotion.getEmoteId()));
			player.getClient().queueOutgoingPacket(new SendString(player.getUsername(), 12779));
			player.getClient().queueOutgoingPacket(new SendString(lines[0], 12780));
			player.getClient().queueOutgoingPacket(new SendString(lines[1], 12781));
			player.getClient().queueOutgoingPacket(new SendPlayerDialogueHead(12778));
			player.getClient().queueOutgoingPacket(new SendChatInterface(12777));
			break;
		case 3:
			player.getClient().queueOutgoingPacket(new SendModelAnimation(12783, emotion.getEmoteId()));
			player.getClient().queueOutgoingPacket(new SendString(player.getUsername(), 12784));
			player.getClient().queueOutgoingPacket(new SendString(lines[0], 12785));
			player.getClient().queueOutgoingPacket(new SendString(lines[1], 12786));
			player.getClient().queueOutgoingPacket(new SendString(lines[2], 12787));
			player.getClient().queueOutgoingPacket(new SendPlayerDialogueHead(12783));
			player.getClient().queueOutgoingPacket(new SendChatInterface(12782));
			break;
		case 4:
			player.getClient().queueOutgoingPacket(new SendModelAnimation(11885, emotion.getEmoteId()));
			player.getClient().queueOutgoingPacket(new SendString(player.getUsername(), 11886));
			player.getClient().queueOutgoingPacket(new SendString(lines[0], 11887));
			player.getClient().queueOutgoingPacket(new SendString(lines[1], 11888));
			player.getClient().queueOutgoingPacket(new SendString(lines[2], 11889));
			player.getClient().queueOutgoingPacket(new SendString(lines[3], 11890));
			player.getClient().queueOutgoingPacket(new SendPlayerDialogueHead(11885));
			player.getClient().queueOutgoingPacket(new SendChatInterface(11884));
		}
	}
}
