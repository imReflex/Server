package org.endeavor.game.content.clans;

import java.io.File;
import java.util.HashMap;

import org.endeavor.engine.TasksExecutor;
import org.endeavor.engine.utility.NameUtil;
import org.endeavor.engine.utility.SerializeableFileManager;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

/**
 * A class to handle and  manage all Clans.
 * @author Allen
 *
 */
public final class Clans {

	public static HashMap<String, Clan> clans = new HashMap<String, Clan>();
	public final static String CLAN_DIRECTORY = "./data/clans/";
	
	
	public void initialize() {
		loadClans();
		
	}
	
	/**
	 * Load all of the clans.
	 */
	public static void loadClans() {
		final File directory = new File(CLAN_DIRECTORY);
		TasksExecutor.slowExecutor.submit(new Runnable() {

			@Override
			public void run() {
				for(File file : directory.listFiles()) {
					Clan clan = SerializeableFileManager.loadClan(file);
					if(clan != null) {
						clan.initializeClan();
						clans.put(clan.getOwner(), clan);
					}
				}
			}
			
		});
	}
	
	/**
	 * Save a clan
	 * @param clan	The clan instance.
	 */
	public static void saveClan(final Clan clan) {
		TasksExecutor.slowExecutor.submit(new Runnable() {
			@Override
			public void run() {
				SerializeableFileManager.saveClan(clan);
			}
		});
	}
	
	/**
	 * Save all loaded clans.
	 */
	public static void saveAllClans() {
		TasksExecutor.slowExecutor.submit(new Runnable() {
			@Override
			public void run() {
				for(Clan clan : clans.values()) {
					SerializeableFileManager.saveClan(clan);
				}
			}
		});
	}
	
	/**
	 * Create a clan.
	 * @param owner
	 * @param title
	 */
	public static void createClan(Player owner, String title) {
		title = title.toLowerCase();
		System.out.println("creating clan: " + title);
		if(owner.getClanTitle() != "" || owner.getClan() != null || Clans.getClanForPlayer(owner) != null || Clans.getClanForOwner(owner) != null) {
			owner.send(new SendMessage("Please leave the clan you are in before attempting to create one."));
			return;
		}
		if(Clans.getClanForName(title) != null) {
			owner.send(new SendMessage("That clan title is taken, please try another."));
			return;
		}
		Clan clan = new Clan(owner, title);
		clan.initializeClan();
		owner.setClanOwner(true);
		owner.setClanTitle(title);
		clans.put(owner.getUsername(), clan);
		Clans.saveClan(clan);
		owner.save();
		owner.send(new SendMessage("You have successfully created the clan <col=255>" + NameUtil.uppercaseFirstLetter(title) + "</col>."));
	}
	
	/**
	 * Delete/Remove a clan.
	 * @param clan
	 * @param owner
	 */
	public static void removeClan(Clan clan, Player owner) {
		if(clan.getOwner().equalsIgnoreCase(owner.getUsername())) {
			for(ClanMember member : clan.getMembers().values()) {
				Player player = member.getPlayer();
				player.setClanChannel(null);
				ClanTab.clearClanTab(player);
				player.setClanTitle("");
				player.setClan(null);
			}
			clans.remove(clan);
			
		}
	}
	
	/**
	 * Get a clan for its title name.
	 * @param name
	 * @return
	 */
	public static Clan getClanForName(String name) {
		for(Clan clan : clans.values())
			if(clan.getSettings().getTitle().equalsIgnoreCase(name))
				return clan;
		return null;
	}
	
	/**
	 * Retrieves a clan for a given player.
	 * @param owner The player instnace
	 * @return Clan instance
	 */
	public static Clan getClanForOwner(Player owner) {
		for(Clan clan : clans.values())
			if(clan.getOwner().equalsIgnoreCase(owner.getUsername()))
				return clan;
		//clans.get(owner.getUsername());
		return null;
	}
	
	/**
	 * Checks to see if a title is taken.
	 * @param title
	 * @return
	 */
	public static boolean isTitleTaken(String title) {
		for(Clan clan : clans.values())
			if(clan.getSettings().getTitle().equalsIgnoreCase(title))
				return true;
		return false;
	}
	
	public static boolean clanContainsPlayer(Player player) {
		for(Clan clan : clans.values())
			if(clan.isMember(player))
				return true;
		return false;
	}
	
	public static Clan getClanForPlayer(Player player) {
		for(Clan clan : clans.values())
			if(clan.isMember(player))
				return clan;
		return null;
	}
	
	public static void joinChannelForName(Player player, String name) {
		name = name.toLowerCase();
		System.out.println("Joining clan: " + name);
		if(player.getClanKickTime(name) > 0 && ((player.getClanKickTime(name) - System.currentTimeMillis()) >= 0)) {
			player.send(new SendMessage("You are temporarily banned from joining this clan chat. Try again in 5 minutes."));
			return;
		}
		Clan clan = Clans.getClanForName(name);
		if(clan != null) {
			if(clan.isBanned(player)) {
				player.send(new SendMessage("You are banned from this clan channel."));
				return;
			}
			clan.getChannel().joinChannel(player);
		} else
			player.send(new SendMessage("The clan you entered does not exists."));
	}
	
}
