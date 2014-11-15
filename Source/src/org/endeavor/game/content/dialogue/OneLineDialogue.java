package org.endeavor.game.content.dialogue;

import java.util.HashMap;

import org.endeavor.game.entity.player.Player;

public class OneLineDialogue {
	
	private static final HashMap<Integer, String> idsForChat = new HashMap<Integer, String>();

	public static void declare() {
		idsForChat.put(462, "Welcome to the Mages' guild!");
		idsForChat.put(553, "Need any magic supplies?");
	}
	
	public static boolean doOneLineChat(Player player, int id) {
		if (idsForChat.containsKey(id)) {
			DialogueManager.sendNpcChat(player, id, Emotion.HAPPY_TALK, idsForChat.get(id));
			return true;
		}
		
		return false;
	}
	
}
