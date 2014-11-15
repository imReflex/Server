package org.endeavor.game.content.clans;

import java.util.HashMap;

import org.endeavor.engine.utility.NameUtil;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendString;

/**
 * 
 * @author Allen K.
 *
 */
public class ClanChannel {

	private Clan clan;
	private HashMap<Player,ClanMember> channelPlayers = new HashMap<Player,ClanMember>();
	
	public ClanChannel(Clan clan) {
		this.clan = clan;
	}
	
	/**
	 * Join this channel.
	 * @param player
	 */
	public void joinChannel(Player player) {
		player.send(new SendMessage("Attempting to join clan channel..."));
		if(clan.isMember(player)) {
			ClanMember member = clan.getMember(player);
			if(clan.getSettings().canJoin(member.getRank())) {
				channelPlayers.put(player, member);
				player.setClanChannel(this);
				updateClanTabs();
				player.send(new SendMessage("You have entered <col=255>" + NameUtil.uppercaseFirstLetter(clan.getSettings().getTitle()) + "</col>'s clan channel."));
				player.send(new SendString("Leave Chat", 18135));
			} else
				player.send(new SendMessage("You do not have a high enough rank to enter this clan chat."));
		} else {
			ClanMember member = new ClanMember(player, ClanRanks.GUEST);
			if(clan.getSettings().canJoin(member.getRank())) {
				channelPlayers.put(player, member);
				player.setClanChannel(this);
				updateClanTabs();
				player.send(new SendMessage("You have entered <col=255>" + NameUtil.uppercaseFirstLetter(clan.getSettings().getTitle()) + "</col>'s clan channel."));
				player.send(new SendString("Leave Chat", 18135));
			} else
				player.send(new SendMessage("You do not have a high enough rank to enter this clan chat."));
		}
	}
	
	/**
	 * Leave this channel.
	 * @param player
	 */
	public void leaveChannel(Player player) {
		if(channelPlayers.containsKey(player)) {
			channelPlayers.remove(player);
			ClanTab.clearClanTab(player);
			updateClanTabs();
			player.send(new SendMessage("You have left the clan chat."));
			player.send(new SendString("Join Chat", 18135));
		}
		player.setClanChannel(null);
	}
	
	public void updateClanTabs() {
		for(ClanMember member : channelPlayers.values())
			ClanTab.updateClanTab(this,member);
	}
	
	public void interpret(Player player, String command) {
		if(!channelPlayers.containsKey(player)) {
			player.setClanChannel(null);
			ClanTab.clearClanTab(player);
			player.send(new SendMessage("You are not in this clan channel."));
		}
		System.out.println("Clan interpret: " + command);
		if(command.startsWith("/kick")) {
			this.kick(player, command.substring(6));
		} else if(ClanRanks.getForPrefix(command.split(" ")[0].replaceAll("/", "")) != null) {
			rankMemberCommand(player, ClanRanks.getForPrefix(command.split(" ")[0].replaceAll("/", "")), command.substring(7));
		} else if(command.startsWith("/name")) {
			this.renameClanCommand(player, command.substring(6));
		} else if(command.startsWith("/invite")) {
			this.invitePlayerCommand(player, command.substring(8));
		} else if(command.startsWith("/ban")) {
			this.ban(player, command.substring(5));
		} else if(command.startsWith("/")) {
			this.sendMessage(player, command.substring(1));
		}
	}
	
	private void invitePlayerCommand(Player player, String name) {
		if(clan.getOwner().equalsIgnoreCase(player.getUsername())) {
			Player toInvite = World.getPlayerByName(name);
			if(clan.isMember(toInvite)) {
				player.send(new SendMessage("This player is already in your clan."));
				return;
			}
			if(Clans.clanContainsPlayer(toInvite)) {
				player.send(new SendMessage("This player is already in another clan."));
				return;
			}
			if(toInvite != null) {
				clan.queueInvite(toInvite);
				toInvite.send(new SendMessage(player.getUsername() + ":clanreq:"));
			} else {
				player.send(new SendMessage("This player is not logged in."));
			}
		} else {
			player.send(new SendMessage("You do not have permission to do this."));
		}
	}
	
	private void renameClanCommand(Player player, String name) {
		if(clan.getOwner().equalsIgnoreCase(player.getUsername())) {
			if(Clans.getClanForName(name) == null) {
				this.clan.getSettings().setTitle(name);
				player.send(new SendString(NameUtil.uppercaseFirstLetter(clan.getSettings().getTitle()), ClanEdit.CLAN_NAME));
				Clans.saveClan(clan);
				updateClanTabs();
			} else {
				player.send(new SendMessage("That clan name is taken, please try another."));
			}
		} else {
			player.send(new SendMessage("You do not have permission to do this."));
		}
	}
	
	private void rankMemberCommand(Player player, ClanRanks rank, String name) {
		if(clan.getOwner().equalsIgnoreCase(player.getUsername())) {
			ClanMember member = clan.getMembers().get(name);
			member.setRank(rank);
			clan.openClanEdit(player);
		}
	}
	
	private void sendMessage(Player player, String message) {
		if(channelPlayers.containsKey(player)) {
			ClanMember member = channelPlayers.get(player);
			if(!clan.getSettings().canTalk(member.getRank())) {
				player.send(new SendMessage("You do not have permission to talk in this channel."));
				return;
			}
			sendChannelMessage("[<col=255>" + NameUtil.uppercaseFirstLetter(clan.getSettings().getTitle()) + "</col>] "
					+ (player.getCrownId() != 128 ? "<img=" + (player.getCrownId()) + "></img>" : "") + player.getUsername() + ": "
					+ "</col><col=800000>" + message.replaceAll("_", " ") + "</col>");
		} else {
			player.send(new SendMessage("You are not in a clan chat."));
			player.setClanChannel(null);
			ClanTab.clearClanTab(player);
		}
	}
	
	private void sendChannelMessage(String message) {
		for(ClanMember member : channelPlayers.values()) {
			Player player = World.getPlayerByName(member.getPlayerUsername());
			if(player == null)
				continue;
			player.send(new SendMessage(message));
		}
	}
	
	private void kick(Player player, String name) {
		if(clan.isMember(player)) {
			ClanMember member = clan.getMember(player);
			if(clan.getSettings().canKick(member.getRank())) {
				Player toKick = World.getPlayerByName(name);
				if(toKick != null) {
					ClanMember toKickMember = channelPlayers.get(toKick);
					if(toKickMember != null) {
						if(toKickMember.getRank().getID() < member.getRank().getID()) {
							toKick.setClanChannel(null);
							channelPlayers.remove(toKick);
							ClanTab.clearClanTab(toKick);
							updateClanTabs();
							toKick.addClanKickTime(clan.getSettings().getTitle());
							toKick.save();
							toKick.send(new SendMessage("You have been kicked from the " + clan.getSettings().getTitle() + " clan chat."));
						} else
							player.send(new SendMessage("You do not have a high enough rank to kick this player."));
					} else
						player.send(new SendMessage("This player is not in this clan."));
				} else
					player.send(new SendMessage("This player does not exists."));
			} else
				player.send(new SendMessage("You do not have a high enough rank to use the kick command."));
		}
	}
	
	private void ban(Player player, String name) {
		
	}
	
	public HashMap<Player,ClanMember> getChannelPlayers() {
		return this.channelPlayers;
	}
	
	public Clan getClan() {
		return clan;
	}
	
}
