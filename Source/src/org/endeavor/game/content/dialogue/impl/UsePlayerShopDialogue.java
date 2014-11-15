package org.endeavor.game.content.dialogue.impl;

import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueConstants;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendEnterString;
import org.endeavor.game.entity.player.net.out.impl.SendInterface;
import org.endeavor.game.entity.player.net.out.impl.SendString;

/*public class UsePlayerShopDialogue extends Dialogue {
	
	public UsePlayerShopDialogue(Player player) {
		this.player = player;
	}

	@Override
	public void execute() {
		DialogueManager.sendOption(player, "Search-user", "Search-item", "Edit-shop", "View-users");
	}

	@Override
	public boolean clickButton(int button) {
		switch (button) {
		case DialogueConstants.OPTIONS_4_1:
			player.setEnterXInterfaceId(55777);
			player.getClient().queueOutgoingPacket(new SendEnterString());
			return true;
		case DialogueConstants.OPTIONS_4_2:
			player.setEnterXInterfaceId(55778);
			player.getClient().queueOutgoingPacket(new SendEnterString());
			return true;
		case DialogueConstants.OPTIONS_4_3:
			player.getShopping().open(player);
			
			
			return true;
		case DialogueConstants.OPTIONS_4_4:
			int c = 0;

			player.getClient().queueOutgoingPacket(new SendString("Players with shops", 8144));

			while (c <= 50) {
				player.getClient().queueOutgoingPacket(new SendString("", 8147 + c));
				c++;
			}

			c = 0;

			while (c <= 49) {
				player.getClient().queueOutgoingPacket(new SendString("", 12174 + c));
				c++;
			}

			c = 0;

			for (Player p : World.getPlayers()) {
				if ((p != null) && (p.isActive()) && (p.getPlayerShop().hasAnyItems())) {
					if (c <= 50)
						player.getClient().queueOutgoingPacket(new SendString(p.getUsername(), 8147 + c));
					else {
						player.getClient().queueOutgoingPacket(new SendString(p.getUsername(), 12174 + c - 50));
					}

					c++;
				}
			}

			player.getClient().queueOutgoingPacket(new SendInterface(8134));

			player.setEnterXInterfaceId(55777);
			player.getClient().queueOutgoingPacket(new SendEnterString());
			return true;
		}
		
		return false;
	}

}*/
