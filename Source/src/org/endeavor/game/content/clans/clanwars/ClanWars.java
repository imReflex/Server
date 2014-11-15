package org.endeavor.game.content.clans.clanwars;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.endeavor.engine.TasksExecutor;
import org.endeavor.game.content.clans.Clan;

/**
 * 
 * @author Allen K.
 * 
 * Process all wars.
 *
 */
public class ClanWars {

	public transient static ArrayList<ClanWar> wars = new ArrayList<ClanWar>();
	
	public static void init() {
		TasksExecutor.slowExecutor.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				cycle();
			}
		}, 0, 600, TimeUnit.MILLISECONDS);
	}
	
	public static void cycle() {
		for(ClanWar war : wars) {
			if(war.removeWar()) {
				System.out.println("Removing War: " + war.getRequesterClan().getSettings().getTitle() + " vs. " + war.getAccepterClan().getSettings().getTitle());
				wars.remove(war);
			} else
				war.tick();
		}
	}
	
	public static void initClanWar(Clan requester, Clan accepter, ClanWarRules rules) {
		requester.getWarSetup().cancel();
		ClanWar war = new ClanWar(requester, accepter, rules);
		war.init();
		wars.add(war);
	}
	
	public static boolean isClanInWar(Clan clan) {
		for(ClanWar war : wars)
			if(war.getAccepterClan() == clan || war.getRequesterClan() == clan)
				return true;
		return false;
	}
	
	public static ClanWar getWarForClan(Clan clan) {
		for(ClanWar war : wars)
			if(war.getAccepterClan() == clan || war.getRequesterClan() == clan)
				return war;
		return null;
	}
	
}
