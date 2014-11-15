package org.endeavor.game.content;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.endeavor.engine.utility.NameUtil;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendFriendUpdate;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendPMServer;
import org.endeavor.game.entity.player.net.out.impl.SendPrivateMessage;

public class PrivateMessaging implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8021966114110139272L;
	private transient Player player;
	private List<String> friends = new LinkedList<String>();
	private List<String> ignores = new LinkedList<String>();
	private int messagesReceived = 0;

	public PrivateMessaging(Player player) {
		this.player = player;
	}

	public void connect() {
		player.getClient().queueOutgoingPacket(new SendPMServer(2));

		for (Iterator<String> i = friends.iterator(); i.hasNext();) {
			String name = (String) i.next();
			player.getClient().queueOutgoingPacket(
					new SendFriendUpdate(NameUtil.nameToLong(name), World.getPlayerByName(name) == null ? 0 : 1));
		}

		player.getAchievements().set(player, "Have 25 friends", friends.size(), false);
	}

	public int getNextMessageId() {
		messagesReceived += 1;
		return messagesReceived;
	}

	public void sendPrivateMessage(long id, int size, byte[] text) {
		String name = NameUtil.longToName(id).replaceAll("_", " ");
		Player sentTo = World.getPlayerByName(name);

		if (sentTo != null) {
			if (sentTo.getPrivateMessaging().ignored(player.getUsername())) {
				return;
			}

			if (player.isMuted()) {
				player.getClient()
						.queueOutgoingPacket(
								new SendMessage("You are muted, you will be unmuted in " + player.getRemainingMute()
										+ " days."));
				return;
			}

			// LoginThread.queuePrivateChatLog(player, sentTo,
			// Misc.textUnpack(text, size));
			sentTo.getClient().queueOutgoingPacket(
					new SendPrivateMessage(NameUtil.nameToLong(player.getUsername()), player.getCrownId(), text, sentTo
							.getPrivateMessaging().getNextMessageId()));
		} else {
			player.getClient().queueOutgoingPacket(new SendMessage("Your private message could not be delivered."));
		}
	}

	public void updateOnlineStatus(Player connectedPlayer, boolean connected) {
		String name = connectedPlayer.getUsername().toLowerCase();

		if (friends.contains(name))
			player.getClient().queueOutgoingPacket(new SendFriendUpdate(NameUtil.nameToLong(name), connected ? 1 : 0));
	}

	public void addFriend(String name) {
		name = name.toLowerCase();

		long id = NameUtil.nameToLong(name);
		friends.add(name);

		player.getAchievements().set(player, "Have 25 friends", friends.size(), false);

		player.getClient().queueOutgoingPacket(new SendFriendUpdate(id, World.getPlayerByName(name) == null ? 0 : 1));
	}

	public void addFriend(long id) {
		String name = NameUtil.longToName(id).toLowerCase().replaceAll("_", " ");

		friends.add(name);

		player.getAchievements().set(player, "Have 25 friends", friends.size(), false);

		player.getClient().queueOutgoingPacket(new SendFriendUpdate(id, World.getPlayerByName(name) == null ? 0 : 1));
	}

	public void removeFriend(long id) {
		friends.remove(NameUtil.longToName(id).toLowerCase().replaceAll("_", " "));
	}

	public void addIgnore(String name) {
		ignores.add(name);
	}

	public void addIgnore(long id) {
		ignores.add(NameUtil.longToName(id).replaceAll("_", " "));
	}

	public void removeIgnore(long id) {
		ignores.remove(NameUtil.longToName(id).replaceAll("_", " "));
	}

	public boolean ignored(String n) {
		return ignores.contains(n.toLowerCase());
	}

	public List<String> getFriends() {
		return friends;
	}

	public List<String> getIgnores() {
		return ignores;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
}
