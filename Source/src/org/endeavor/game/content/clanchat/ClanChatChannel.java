package org.endeavor.game.content.clanchat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.endeavor.engine.utility.NameUtil;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendClanMessage;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class ClanChatChannel {
	
	private Player[] playersInChat = new Player[100];
	private String name;
	private String owner;
	private long ownerNameToLong;
	private int count = 0;
	
	private boolean lootshare = false;
	private long lootshareEffectDelay = 0;
	
	private final Map<String, Byte> rights = new HashMap<String, Byte>();
	private final List<Long> banned = new ArrayList<Long>();
	
	private JoinPermission joinPermissions = JoinPermission.ALL;
	
	public enum JoinPermission {
		ALL, FRIENDS, RECRUIT, CORPORAL, SERGEANT,
		LIEUTENANT, CAPTAIN, GENERAL, CLAN_ADMIN,
		ORGANISER, OVERSEER, DEPUTY_OWNER
	}

	public ClanChatChannel(String name, Player player) {
		this.name = NameUtil.uppercaseFirstLetter(name);

		if (name.equals("revolutionx")) {
			owner = "Allen";
		} else {
			owner = NameUtil.uppercaseFirstLetter(player.getUsername());
		}
		
		ownerNameToLong = NameUtil.nameToLong(owner);

		ClanChatDirectory.getOpenChannels().add(this);
	}
	
	public ClanChatChannel() {}
	
	public void doChangeRights(Player player, String username, byte rightsForUsername) {
		if (player.getUsernameToLong() != ownerNameToLong) {
			DialogueManager.sendStatement(player, "You do not own this clan.");
			return;
		}
		
		
	}
	
	public boolean isOwnerInChannel() {
		for (Player p : playersInChat) {
			if (p != null && p.getUsernameToLong() == ownerNameToLong) {
				return true;
			}
		}
		
		return false;
	}
	
	public void toggleLootShare() {
		lootshare = !lootshare;
		lootshareEffectDelay = System.currentTimeMillis() + (60000 * 2);
	}
	
	public boolean isLootShareEnabled() {
		if (!isOwnerInChannel()) {
			return false;
		}
		
		return lootshare && System.currentTimeMillis() - lootshareEffectDelay >= 0 || !lootshare && System.currentTimeMillis() - lootshareEffectDelay < 0;
	}

	public void sendClanMessage(Player player, String message) {
		for (Player others : playersInChat)
			if (others != null) {
				others.getClient().queueOutgoingPacket(new SendClanMessage(message, player.getUsername(), owner, player.getRights()));
			}
	}

	public void sendMessage(String message, Player other) {
		for (Player player : playersInChat) {
			if (player != null && !player.getPrivateMessaging().ignored(other.getUsername())) {
				player.getClient().queueOutgoingPacket(new SendMessage(message));
			}
		}
	}

	public boolean add(Player player) {
		if ((player == null) || (player.getUsername() == null)) {
			return false;
		}

		for (int i = 0; i < playersInChat.length; i++) {
			if (playersInChat[i] == null) {
				playersInChat[i] = player;
				for (Player p : playersInChat) {
					if (p != null) {
						p.getClanChat().add(player.getUsername());
					}
				}
				count += 1;
				return true;
			}
		}

		return false;
	}

	public void remove(Player player) {
		int c = 0;

		for (int i = 0; i < playersInChat.length; i++) {
			if (playersInChat[i] != null) {
				if (playersInChat[i].equals(player)) {
					playersInChat[i] = null;
					count -= 1;

					for (Player p : playersInChat) {
						if (p != null) {
							p.getClanChat().updateUserList();
						}
					}

					if (c > 0) {
						break;
					}
					return;
				}

				c++;
			}
		}

		if (c == 0) {
			ClanChatDirectory.getOpenChannels().remove(this);
		}
	}

	public String getName() {
		return name;
	}

	public String getOwner() {
		return owner;
	}

	public Player[] getPlayersInChat() {
		return playersInChat;
	}

	public int getPlayerCount() {
		return count;
	}
	
	public long getOwnerNameToLong() {
		return ownerNameToLong;
	}

	public JoinPermission getJoinPermissions() {
		return joinPermissions;
	}

	public void setJoinPermissions(JoinPermission joinPermissions) {
		this.joinPermissions = joinPermissions;
	}

	public Map<String, Byte> getRights() {
		return rights;
	}

	public List<Long> getBanned() {
		return banned;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public void setOwnerNameToLong(long ownerNameToLong) {
		this.ownerNameToLong = ownerNameToLong;
	}
	
}
