package org.endeavor.game.content.clans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.endeavor.engine.utility.NameUtil;
import org.endeavor.game.content.clans.clanwars.ClanWarSetup;
import org.endeavor.game.content.clans.clanwars.ClanWars;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

/**
 * A class representing a Clan.
 * @author Allen K.
 */
public class Clan implements Serializable {
	
	private static final long serialVersionUID = 6993607546112217126L;
	private HashMap<String, ClanMember> members = new HashMap<String, ClanMember>();
	private ArrayList<String> banned = new ArrayList<String>();
	private transient ArrayList<Player> inviteQueue = new ArrayList<Player>();
	private transient ArrayList<Player> challengeQueue = new ArrayList<Player>();
	private String owner;
	private ClanSettings settings;
	private transient ClanChannel channel;
	private transient ClanWarSetup warSetup;
	private int warWins = 0;
	private int warDeaths = 0;
	
	public Clan(Player owner, String title) {
		this.owner = owner.getUsername();
		this.settings = new ClanSettings(title, ClanRanks.GUEST, ClanRanks.GUEST, ClanRanks.SERGEANT);
		members.put(owner.getUsername(), new ClanMember(owner, ClanRanks.OWNER));
	}
	
	public void initializeClan() {
		channel = new ClanChannel(this);
		if(banned == null)
			banned = new ArrayList<String>();
		if(inviteQueue == null)
			inviteQueue = new ArrayList<Player>();
		if(challengeQueue == null)
			challengeQueue = new ArrayList<Player>();
		warSetup = null;
		for(ClanMember member : members.values())
			System.out.println("Clan: " + this.getSettings().getTitle() + " Member: " + member.getPlayerUsername());
	}
	
	/**
	 * Retrieve a list of players available for a clan war.
	 * @return
	 */
	public ArrayList<Player> getAvailableWarMembers() {
		ArrayList<Player> warList = new ArrayList<Player>();
		for(ClanMember member : members.values()) {
			Player player = World.getPlayerByName(member.getPlayerUsername());
			if(player != null) {
				if(player.inArea(new Location(3264, 3672, 0), new Location(3279, 3695, 0), false)) {
					warList.add(player);
				}
			}
		}
		return warList;
	}
	
	/**
	 * Send a clan war challenge request.
	 * @param requester	The clan owner sending the request.
	 * @param player	The clan owner receiving the request.
	 */
	public void sendChallengeRequest(Player requester, Player player) {
		if(!owner.equalsIgnoreCase(requester.getUsername())) {
			requester.send(new SendMessage("You are not the owner of this clan."));
			return;
		}
		Clan clan = Clans.getClanForOwner(player);
		if(clan != null) {
			if(!ClanWars.isClanInWar(clan)) {
				addChallengeQueue(player);
				player.send(new SendMessage(requester.getUsername() + ":warreq:"));
			} else {
				requester.send(new SendMessage("This clan is currently in a war."));
			}
		} else {
			requester.send(new SendMessage("This player is not the owner of a clan."));
		}
	}
	
	/**
	 * Add a player to the queue of challenges.
	 * @param player	The clan owner who received the war request.
	 */
	public void addChallengeQueue(Player player) {
		if(!isMember(player))
			this.challengeQueue.add(player);
	}
	
	/**
	 * Accept a war request sent by {this} clan.
	 * @param player	The clan owner who received the request.
	 */
	public void acceptChallenge(Player player) {
		Player playerOwner = this.getPlayerOwner();
		if(playerOwner == null) {
			player.send(new SendMessage(this.getOwner() + " is not online."));
			return;
		}
		if(challengeQueue.contains(player)) {
			Clan clan = Clans.getClanForOwner(player);
			if(clan != null) {
				challengeQueue.remove(player);
				ClanWarSetup setup = new ClanWarSetup(this, clan);
				clan.setWarSetup(setup);
				this.setWarSetup(setup);
				this.getWarSetup().openChallengeInterface(playerOwner, player);
			}
		}
	}
	
	/**
	 * Returns whether or not a given player is a member of the clan.
	 * @param player
	 * @return
	 */
	public boolean isMember(Player player) {
		return members.containsKey(player.getUsername());
	}
	
	/**
	 * Add a player to this clan.
	 * @param player
	 */
	public void addMember(Player player) {
		Player pOwner = World.getPlayerByName(owner);
		if(!Clans.clanContainsPlayer(player)) {
			ClanMember member = new ClanMember(player, ClanRanks.MEMBER);
			members.put(player.getUsername(), member);
			player.setClanTitle(this.getSettings().getTitle());
			player.send(new SendMessage("You have been added to <col=255>" + NameUtil.uppercaseFirstLetter(this.getSettings().getTitle()) + "</col>'s clan!"));
			Clans.saveClan(this);
			if(pOwner != null)
				pOwner.send(new SendMessage(player.getUsername() + " has been added to your clan."));
		} else {
			if(pOwner != null)
				pOwner.send(new SendMessage(player.getUsername() + " is already in a clan."));
		}
	}
	
	/**
	 * Remove a player from this clan from the owner's action..
	 * @param owner
	 * @param player
	 */
	public void removeMember(Player owner, Player player) {
		if(members.containsKey(player.getUsername())) {
			members.remove(player.getUsername());
			this.getChannel().leaveChannel(player);
			player.setClanTitle("");
			Clans.saveClan(this);
			owner.send(new SendMessage(player.getUsername() + " has been removed from your clan."));
		}
	}
	
	/**
	 * Remove a player from this clan from the player's action.
	 * @param player
	 */
	public void leaveClan(Player player) {
		if(members.containsKey(player.getUsername())) {
			members.remove(player.getUsername());
			this.getChannel().leaveChannel(player);
			player.setClanTitle("");
			Clans.saveClan(this);
			player.send(new SendMessage("You have left your clan."));
		}
	}
	
	/**
	 * Ban player from clan.
	 * @param banner
	 * @param toBan
	 */
	public void banPlayer(Player banner, Player toBan) {
		if(this.isMember(toBan)) {
			banner.send(new SendMessage("This player is a member of this clan. Please remove the member before banning them."));
			return;
		} else {
			banned.add(toBan.getUsername());
			this.getChannel().leaveChannel(toBan);
		}
	}
	
	/**
	 * Check to see if a player is banned from this clan.
	 * @param player
	 * @return
	 */
	public boolean isBanned(Player player) {
		return banned.contains(player.getUsername());
	}
	
	/**
	 * Queue a clan invite.
	 * @param player
	 */
	public void queueInvite(Player player) {
		if(this.isMember(player)) {
			return;
		}
		inviteQueue.add(player);
	}
	
	/**
	 * Accept a clan invite.
	 * @param player
	 */
	public void acceptInvite(Player player) {
		System.out.println("Accepting: " + player.getUsername());
		if(inviteQueue.contains(player)) {
			inviteQueue.remove(player);
			this.addMember(player);
		} else {
			player.send(new SendMessage("You were not invited to this clan."));
		}
	}
	
	/**
	 * Open the clan edit interface.
	 */
	public void openClanEdit(Player player) {
		ClanEdit.openEditInterface(this, player);
	}
	
	/**
	 * Process clan edit buttons.
	 * @param player
	 * @param buttonID
	 * @return
	 */
	public boolean handleEditClick(Player player, int buttonID) {
		return ClanEdit.handleEditClick(this, player, buttonID);
	}
	
	/**
	 * Retrieve the ClanMember for a given Player instance.
	 * @param player
	 * @return
	 */
	public ClanMember getMember(Player player) {
		return members.get(player.getUsername());
	}
	
	public String getOwner() {
		return owner;
	}
	
	public boolean challengeQueueContains(Player player) {
		return challengeQueue.contains(player);
	}

	public Player getPlayerOwner() {
		return World.getPlayerByName(owner);
	}
	
	public void updateOwner(Player player) {
		this.owner = player.getUsername();
	}
	
	public ClanSettings getSettings() {
		return this.settings;
	}
	
	public HashMap<String,ClanMember> getMembers() {
		return this.members;
	}
	
	public ClanChannel getChannel() {
		return this.channel;
	}
	
	public ClanWarSetup getWarSetup() {
		return warSetup;
	}
	
	public void setWarSetup(ClanWarSetup setup) {
		this.warSetup = setup;
	}
	
	public int getWarWins() {
		return warWins;
	}
	
	public int getWarDeaths() {
		return warDeaths;
	}
	
	public void winWar() {
		this.warWins++;
	}
	
	public void loseWar() {
		this.warDeaths++;
	}
	
}
