package org.endeavor.game.content.clanchat;

import java.io.Serializable;

import org.endeavor.engine.utility.NameUtil;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerConstants;
import org.endeavor.game.entity.player.net.out.impl.SendConfig;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendString;

public class ClanChat implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4258803232470680138L;
	private final Player player;
	private ClanChatChannel current;
	
	private boolean lootshare = false;
	private long lootshareDelay = 0;//finish lootshare and better clanchat system..

	public ClanChat(Player player) {
		this.player = player;
	}
	
	public void onLogin() {
		player.getClient().queueOutgoingPacket(new SendConfig(812, 0));
	}
	
	public boolean clickButton(int id) {
		switch (id) {
		case 71074:
			lootshare = !lootshare;
			lootshareDelay = System.currentTimeMillis() + (60000 * 2);
			player.getClient().queueOutgoingPacket(new SendConfig(812, lootshare ? 1 : 0));
			
			player.getClient().queueOutgoingPacket(new SendMessage("Lootshare is now " + (lootshare ? "enabled" : "disabled") + ", changes will take effect in two minutes"));
			
			if (current != null) {
				Player owner = World.getPlayerByName(current.getOwner());
				if (owner != null && owner.equals(player)) {
					current.toggleLootShare();
				}
			}
			return true;
		}
		
		return false;
	}

	public void sendMessage(String message) {
		if (current == null) {
			player.getClient().queueOutgoingPacket(new SendMessage("You are not in a clan."));
			return;
		}

		if (player.isMuted()) {
			player.getClient().queueOutgoingPacket(new SendMessage("You are muted."));
			return;
		}
		
		if (message.contains("<")) {
			player.getClient().queueOutgoingPacket(new SendMessage("You cannot use text arguments when using the clanchat."));
			return;
		}

		int rights = player.getRights() == 2 ? 4 : player.getRights() == 1 ? 3 : (player.getRights() == 4)
				|| (PlayerConstants.isOwner(player)) ? 2 : player.getRights() == 3 ? 1 : 0;

		current.sendMessage("[<col=255>" + current.getName() + "</col>] "
				+ (rights > 0 ? "<img=" + (player.getCrownId()) + "></img>" : "") + player.getUsername() + ": "
				+ "</col><col=800000>" + message.replaceAll("_", " ") + "</col>", player);
		//current.sendClanMessage(player, message.replaceAll("_", " "));
	}

	public void enterChat(String name) {
		if (current != null) {
			player.getClient().queueOutgoingPacket(new SendMessage("You already in a clan."));
			return;
		}

		if ((player.getRights() < 3) && (name.equalsIgnoreCase("staff"))) {
			player.getClient().queueOutgoingPacket(new SendMessage("You must be a staff member to use this clan chat."));
			return;
		}

		current = ClanChatDirectory.enterChat(player, name);

		if (current != null)
			onJoin();
	}

	public void leaveChat() {
		if (current == null) {
			player.getClient().queueOutgoingPacket(new SendMessage("You are not in a clan."));
			return;
		}

		current.remove(player);
		current = null;

		clear();
	}

	public void add(String name) {
		if ((name != null) && (current != null))
			player.getClient().queueOutgoingPacket(new SendString(getRank(name) + "" + name, 18144 + current.getPlayerCount()));
	}
	
	public String getRank(String name) {
		String[] ranks = { "[MOD]", "[FRI]", "[REC]", "[COR]", "[SER]",
				"[LIE]", "[BER]", "[VR]" };
		Player player = World.getPlayerByName(name);
		if(player != null) {
			if(player.getUsernameToLong() == current.getOwnerNameToLong())
				return "[OWN]";
			if(player.getRights() == 2 || player.getRights() == 3)
				return ranks[0];
			//TODO: ADD REST OF RANKS!
		}
		return "[REG]";
	}

	public void updateUserList() {
		player.getClient().queueOutgoingPacket(new SendConfig(77, 1));

		Player[] clan = current.getPlayersInChat();

		int size = 0;
		
		for (int i = 0; i < clan.length; i++)
			if (clan[i] != null) {
				player.getClient().queueOutgoingPacket(new SendString(getRank(clan[i].getUsername()) + "" + clan[i].getUsername(), size + 18144));
				size++;
			}
	}

	public void clear() {
		player.getClient().queueOutgoingPacket(new SendConfig(77, 0));
	}

	public void onJoin() {
		Player[] clan = current.getPlayersInChat();

		player.getClient().queueOutgoingPacket(new SendString("Talking in: @yel@" + current.getName(), 18139));
		player.getClient().queueOutgoingPacket(new SendString("Owner: " + current.getOwner(), 18140));

		player.getClient().queueOutgoingPacket(new SendConfig(77, 1));

		int size = 0;

		for (int i = 0; i < 100; i++)
			if (clan[i] != null) {
				player.getClient().queueOutgoingPacket(new SendString(getRank(clan[i].getUsername()) + "" + clan[i].getUsername(), size + 18144));
				size++;
			}
	}

	public boolean inClanChat() {
		return current != null;
	}
}
