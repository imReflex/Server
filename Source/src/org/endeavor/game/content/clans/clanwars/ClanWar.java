package org.endeavor.game.content.clans.clanwars;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.endeavor.engine.TasksExecutor;
import org.endeavor.game.content.clans.Clan;
import org.endeavor.game.content.clans.ClanMember;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.Controller;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.out.impl.SendInterface;
import org.endeavor.game.entity.player.net.out.impl.SendPlayerOption;
import org.endeavor.game.entity.player.net.out.impl.SendString;
import org.endeavor.game.entity.player.net.out.impl.SendWalkableInterface;

/**
 * A Clan War.
 * @author Allen K.
 *
 */
public class ClanWar/* implements Serializable*/ {

	/**
	 * 
	 */
	//private static final long serialVersionUID = -1693090699798843639L;
	private final static int CLAN_GAME_INTER = 28540;
	private final static int MY_CLAN_PLAYERS = 28550;
	private final static int OPP_CLAN_PLAYERS = 28551;
	private final static int COUNTDOWN_TIMER_TITLE = 28552;
	private final static int COUNTDOWN_TIMER = 28553;
	
	private transient Clan requester;
	private transient Clan accepter;
	private transient ClanWarRules rules;
	private int height;
	private transient ArrayList<Player> requesterPlayers = new ArrayList<Player>();
	private transient ArrayList<Player> accepterPlayers = new ArrayList<Player>();
	private transient ArrayList<Player> requesterViewPlayers = new ArrayList<Player>();
	private transient ArrayList<Player> accepterViewPlayers = new ArrayList<Player>();
	private transient int requesterKillCount = 0;
	private transient int accepterKillCount = 0;
	private transient ArrayList<Player> deathPool = new ArrayList<Player>();
	private transient ClanWarController controller;
	private boolean warFinished = false;
	private boolean continueTick = true;
	private transient ClanWarTimer warTimer;
	private transient WallHandler wallHandler;
	
	
	public ClanWar(Clan requester, Clan accepter, ClanWarRules rules) {
		this.requester = requester;
		this.accepter = accepter;
		this.rules = rules;
		Player player = World.getPlayerByName(requester.getOwner());
		this.height = player.getIndex() * 4;
		this.controller = new ClanWarController(this);
		this.warFinished = false;
		this.wallHandler = new WallHandler(this);
		this.warTimer = new ClanWarTimer(this);
		TasksExecutor.fastExecutor.scheduleAtFixedRate(warTimer, 1000, 1000);
	}
	
	public void init() {
		requester.setWarSetup(null);
		accepter.setWarSetup(null);
		continueTick = true;
		requesterPlayers = new ArrayList<Player>();
		accepterPlayers = new ArrayList<Player>();
		Player reqPlayer = World.getPlayerByName(requester.getOwner());
		Player accPlayer = World.getPlayerByName(accepter.getOwner());
		requesterPlayers.add(reqPlayer);
		accepterPlayers.add(accPlayer);
		
		reqPlayer.teleport(new Location(rules.getArena().getRequesterSpawnX(), rules.getArena().getRequesterSpawnY(), height));
		accPlayer.teleport(new Location(rules.getArena().getAccepterSpawnX(), rules.getArena().getAccepterSpawnY(), height));
		
		reqPlayer.setController(this.controller);
		accPlayer.setController(this.controller);
		
		/*requesterPlayers = requester.getAvailableWarMembers();
		accepterPlayers = accepter.getAvailableWarMembers();
		for(Player player : requesterPlayers) {
			player.teleport(new Location(rules.getArena().getRequesterSpawnX(), rules.getArena().getRequesterSpawnY(), height));
			player.setController(controller);
		}
		for(Player player : accepterPlayers) {
			player.teleport(new Location(rules.getArena().getAccepterSpawnX(), rules.getArena().getAccepterSpawnY(), height));
			player.setController(controller);
		}*/
		this.wallHandler.spawnWalls();
	}
	
	public void tick() {
		if(continueTick) {
			if(isEnded()) {
				System.out.println("Ended!");
				continueTick = false;
				finishWar();
			}
		}
	}
	
	public void addMember(Player player) {
		if(rules.getVictoryKills() == 1) {
			if(requester.isMember(player)) {
				if(!deathPool.contains(player)) {
					player.teleport(new Location(rules.getArena().getRequesterSpawnX(), rules.getArena().getRequesterSpawnY(), this.height));
				} else {
					player.teleport(new Location(rules.getArena().getRequesterViewingX(), rules.getArena().getRequesterViewingY(), this.height));
				}
				player.setController(this.controller);
			} else if(accepter.isMember(player)) {
				if(!deathPool.contains(player)) {
					player.teleport(new Location(rules.getArena().getAccepterSpawnX(), rules.getArena().getAccepterSpawnY(), this.height));
				} else {
					player.teleport(new Location(rules.getArena().getAccepterViewingX(), rules.getArena().getAccepterViewingY(), this.height));
				}
				player.setController(this.controller);
			}
		} else {
			if(requester.isMember(player)) {
				player.teleport(new Location(rules.getArena().getRequesterSpawnX(), rules.getArena().getRequesterSpawnY(), this.height));
				player.setController(this.controller);
			} else if(accepter.isMember(player)) {
				player.teleport(new Location(rules.getArena().getAccepterSpawnX(), rules.getArena().getAccepterSpawnY(), this.height));
				player.setController(this.controller);
			}
		}
		if(!this.getTimer().started()) {
			if(this.getTimer().getTimeToStart() >= 5000) {
				this.wallHandler.spawnWalls();
			} else {
				this.wallHandler.dropWall(player);
			}
		}
	}
	
	public void sendGameInterface(Player player) {
		if(requester.isMember(player)) {
			player.getClient().queueOutgoingPacket(new SendWalkableInterface(CLAN_GAME_INTER));
			player.send(new SendString(String.valueOf(requesterPlayers.size()), MY_CLAN_PLAYERS));
			player.send(new SendString(String.valueOf(accepterPlayers.size()), OPP_CLAN_PLAYERS));
			player.send(new SendString(warTimer.getTimeString(), COUNTDOWN_TIMER_TITLE));
			player.send(new SendString(String.valueOf(warTimer.formatTime()), COUNTDOWN_TIMER));
		} else if(accepter.isMember(player)) {
			player.getClient().queueOutgoingPacket(new SendWalkableInterface(CLAN_GAME_INTER));
			player.send(new SendString(String.valueOf(accepterPlayers.size()), MY_CLAN_PLAYERS));
			player.send(new SendString(String.valueOf(requesterPlayers.size()), OPP_CLAN_PLAYERS));
			player.send(new SendString(warTimer.getTimeString(), COUNTDOWN_TIMER_TITLE));
			player.send(new SendString(String.valueOf(warTimer.formatTime()), COUNTDOWN_TIMER));
		}
	}
	
	public void sendGameInterface() {
		for(Player player : requesterPlayers)
			sendGameInterface(player);
		for(Player player : accepterPlayers)
			sendGameInterface(player);
		for(Player player : requesterViewPlayers)
			sendGameInterface(player);
		for(Player player : accepterViewPlayers)
			sendGameInterface(player);
	}
	
	public void onDeath(Player player) {
		if(requesterPlayers.remove(player)) {
			requesterViewPlayers.add(player);
			deathPool.add(player);
			accepterKillCount++;
		} else if(accepterPlayers.remove(player)) {
			accepterViewPlayers.add(player);
			deathPool.add(player);
			requesterKillCount++;
		}
		if(isEnded()) {
			removeMember(player);
		}
	}
	
	public void removeMember(Player player) {
		if(requesterPlayers.remove(player) || accepterPlayers.remove(player) || 
				requesterViewPlayers.remove(player) || accepterViewPlayers.remove(player)) {
			player.teleport(new Location(3271, 3687, 0));
			player.send(new SendPlayerOption("null", 3));
			player.setController(ControllerManager.DEFAULT_CONTROLLER);
		}
	}
	
	public void finishMember(Player player) {
		if(requesterPlayers.contains(player) || accepterPlayers.contains(player) || 
				requesterViewPlayers.contains(player) || accepterViewPlayers.contains(player)) {
			player.teleport(new Location(3271, 3687, 0));
			player.send(new SendPlayerOption("null", 3));
			player.setController(ControllerManager.DEFAULT_CONTROLLER);
		}
	}
	
	public void removeAllMembers() {
		for(Player player : requesterPlayers)
			finishMember(player);
		for(Player player : accepterPlayers)
			finishMember(player);
		for(Player player : requesterViewPlayers)
			finishMember(player);
		for(Player player : accepterViewPlayers)
			finishMember(player);
		requesterPlayers.clear();
		accepterPlayers.clear();
		requesterViewPlayers.clear();
		accepterViewPlayers.clear();
	}
	
	public void finishWar() {
		System.out.println("Finishing War!");
		if(isEnded()) {
			Clan clan = getWinner();
			if(clan == requester) {
				requester.winWar();
				accepter.loseWar();
				System.out.println("before1");
				removeAllMembers();
				System.out.println("Winner: " + accepter.getSettings().getTitle() + " War Wins: " + requester.getWarWins());
				TasksExecutor.slowExecutor.schedule(new Runnable() {
					@Override
					public void run() {
						System.out.println("calling interface");
						showFinishInterfaces(requester);
					}
				}, 1200, TimeUnit.MILLISECONDS);
			} else if(clan == accepter) {
				accepter.winWar();
				requester.loseWar();
				System.out.println("before2");
				removeAllMembers();
				System.out.println("Winner: " + accepter.getSettings().getTitle() + " War Wins: " + accepter.getWarWins());
				TasksExecutor.slowExecutor.schedule(new Runnable() {
					@Override
					public void run() {
						System.out.println("calling interface");
						showFinishInterfaces(accepter);
					}
				}, 1200, TimeUnit.MILLISECONDS);
			}
			finalize();
		}
	}
	
	public void showFinishInterfaces(Clan winner) {
		System.out.println("Showing interfaces.");
		if(winner == requester) {
			System.out.println("Showing interfaces. Winner: " + requester.getSettings().getTitle());
			for(ClanMember member : requester.getMembers().values()) {
				Player player = World.getPlayerByName(member.getPlayerUsername());
				if(player != null && player.inArea(new Location(3264, 3672, 0), new Location(3279, 3695, 0), false)) {
					player.send(new SendInterface(40000));
				}
			}
			for(ClanMember member : accepter.getMembers().values()) {
				Player player = World.getPlayerByName(member.getPlayerUsername());
				if(player != null && player.inArea(new Location(3264, 3672, 0), new Location(3279, 3695, 0), false)) {
					player.send(new SendInterface(40004));
				}
			}
		} else if(winner == accepter) {
			System.out.println("Showing interfaces. Winner: " + accepter.getSettings().getTitle());
			for(ClanMember member : accepter.getMembers().values()) {
				Player player = World.getPlayerByName(member.getPlayerUsername());
				if(player != null && player.inArea(new Location(3264, 3672, 0), new Location(3279, 3695, 0), false)) {
					player.send(new SendInterface(40000));
				}
			}
			for(ClanMember member : requester.getMembers().values()) {
				Player player = World.getPlayerByName(member.getPlayerUsername());
				if(player != null && player.inArea(new Location(3264, 3672, 0), new Location(3279, 3695, 0), false)) {
					player.send(new SendInterface(40004));
				}
			}
		}
	}
	
	public void finalize() {
		this.warFinished = true;
	}
	
	public Clan getWinner() {
		if(isEnded()) {
			if(rules.getVictoryKills() >= 2) {
				if(requesterKillCount > accepterKillCount)
					return requester;
				else
					return accepter;
			}
			if(requesterPlayers.size() > 0 && accepterPlayers.size() > 0) {
				if(requesterPlayers.size() > accepterPlayers.size())
					return requester;
				else 
					return accepter;
			} else {
				if(requesterPlayers.size() == 0 && accepterPlayers.size() > 0)
					return accepter;
				else if(requesterPlayers.size() > 0 && accepterPlayers.size() == 0)
					return requester;
			}
		}
		return null;
	}
	
	public boolean isEnded() {
		if(this.rules.getStragglers()) {
			return (requesterPlayers.size() <= 5 && accepterPlayers.size() > 5) || 
					(requesterPlayers.size() > 5 && accepterPlayers.size() <= 5);
		}
		if(rules.getVictoryKills() > 2 && 
				(requesterKillCount >= rules.getVictoryKills() || accepterKillCount >= rules.getVictoryKills())) {
			return true;
		}
		if(rules.getVictoryKills() == 1 && 
				(requesterPlayers.size() == 0 || accepterPlayers.size() == 0))
			return true;
		return this.warTimer.getTimeLeft() <= 0;
	}
	
	public boolean removeWar() {
		return this.warFinished;
	}
	
	public void endWar() {
		
	}
	
	public ClanArena getClanArena() {
		return rules.getArena();
	}
	
	public ClanWarRules getRules() {
		return rules;
	}
	
	public ArrayList<Player> getRequesterPlayers() {
		return requesterPlayers;
	}
	
	public ArrayList<Player> getAccepterPlayers() {
		return accepterPlayers;
	}
	
	public Clan getRequesterClan() {
		return requester;
	}
	
	public Clan getAccepterClan() {
		return accepter;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public WallHandler getWallHandler() {
		return wallHandler;
	}
	
	public ClanWarTimer getTimer() {
		return warTimer;
	}
	
}
