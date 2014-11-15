package org.endeavor.game.content.clans.clanwars;

import org.endeavor.engine.utility.NameUtil;
import org.endeavor.game.content.clans.Clan;
import org.endeavor.game.content.clans.Clans;
import org.endeavor.game.content.clans.clanwars.ClanWarRules.CombatWarElements;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendConfig;
import org.endeavor.game.entity.player.net.out.impl.SendInterface;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;
import org.endeavor.game.entity.player.net.out.impl.SendString;

/**
 * A class to handle clan war's rules setup.
 * @author Allen K.
 *
 */
public class ClanWarSetup {

	private final static int CLAN_WAR_SETUP = 28560;
	private final static int WAR_SETUP_TITLE = 28562;
	
	private Clan requester;
	private Clan accepter;
	private ClanWarRules rules;
	
	private boolean requesterAccept;
	private boolean acceptorAccept;
	
	public ClanWarSetup(Clan requester, Clan accepter) {
		this.requester = requester;
		this.accepter = accepter;
		this.rules = new ClanWarRules();
	}
	
	public void openChallengeInterface(Player requestPlayer, Player acceptPlayer) {
		Clan requester = Clans.getClanForOwner(requestPlayer);
		Clan accepter = Clans.getClanForOwner(acceptPlayer);
		if(requester == null) {
			requestPlayer.send(new SendMessage("You do not own a clan."));
			acceptPlayer.send(new SendMessage(requestPlayer.getUsername() + " does not own a clan."));
			return;
		}
		if(accepter == null) {
			acceptPlayer.send(new SendMessage("You do not own a clan."));
			requestPlayer.send(new SendMessage(acceptPlayer.getUsername() + " does not own a clan."));
			return;
		}
		sendDefaultConfigs(requestPlayer, acceptPlayer);
		requestPlayer.send(new SendString("Clan Wars Options: Challenging " + NameUtil.uppercaseFirstLetter(accepter.getSettings().getTitle()), WAR_SETUP_TITLE));
		acceptPlayer.send(new SendString("Clan Wars Options: Challenging " + NameUtil.uppercaseFirstLetter(requester.getSettings().getTitle()), WAR_SETUP_TITLE));
		requestPlayer.send(new SendInterface(CLAN_WAR_SETUP));
		acceptPlayer.send(new SendInterface(CLAN_WAR_SETUP));
	}
	
	public void sendDefaultConfigs(Player player, Player other) {
		for(int i = 150; i <= 162; i++) {
			if(i == 154)
				continue;
			sendSetupConfig(player, other, i);
		}
	}
	
	public void sendSetupConfig(Player player, Player other, int configID) {
		int value = getConfigValue(configID);
		System.out.println("Sending Setup Config: " + configID + ", " + value);
		if(value != -1) {
			player.send(new SendConfig(configID, value));
			other.send(new SendConfig(configID, value));
		}
	}
	
	public int getConfigValue(int configID) {
		int value = -1;
		switch(configID) {
			case 150:
				if(rules.getVictoryKills() ==  1)
					value = 1;
				if(rules.getVictoryKills() == 2)
					value = 2;
				if(rules.getVictoryKills() == 25)
					value = 3;
				if(rules.getVictoryKills() == 50)
					value = 4;
				if(rules.getVictoryKills() == 100)
					value = 5;
				if(rules.getVictoryKills() == 200)
					value = 6;
				if(rules.getVictoryKills() == 500)
					value = 7;
				if(rules.getVictoryKills() == 1000)
					value = 8;
				if(rules.getVictoryKills() == 2000)
					value = 9;
				if(rules.getVictoryKills() == 5000)
					value = 10;
				break;
			case 151:
				value = (rules.getStragglers()) ? 2 : 1; 
				break;
			case 152:
				if(rules.getTimeLimit() == 5)
					value = 1;
				if(rules.getTimeLimit() == 10)
					value = 2;
				if(rules.getTimeLimit() == 15)
					value = 3;
				if(rules.getTimeLimit() == 30)
					value = 4;
				if(rules.getTimeLimit() == 60)
					value = 5;
				if(rules.getTimeLimit() == 120)
					value = 6;
				break;
			case 153:
				if(rules.getArena() == ClanArena.CLASSIC)
					value = 1;
				if(rules.getArena() == ClanArena.PLATEAU)
					value = 2;
				if(rules.getArena() == ClanArena.BLASTED_FORREST)
					value = 3;
				if(rules.getArena() == ClanArena.TURRETS)
					value = 4;
				if(rules.getArena() == ClanArena.FORSAKEN_QUARRY)
					value = 5;
				break;
			case 155:
				value = (rules.containsCombatElement(CombatWarElements.MELEE)) ? 1 : 0;
				break;
			case 156:
				value = (rules.containsCombatElement(CombatWarElements.MAGIC)) ? 1 : 0;
				break;
			case 157:
				value = (rules.containsCombatElement(CombatWarElements.RANGED)) ? 1 : 0;
				break;
			case 158:
				value = (rules.containsCombatElement(CombatWarElements.PRAYER)) ? 1 : 0;
				break;
			case 159:
				value = (rules.containsCombatElement(CombatWarElements.SUMMONING)) ? 1 : 0;
				break;
			case 160:
				value = (rules.containsCombatElement(CombatWarElements.FOOD)) ? 1 : 0;
				break;
			case 161:
				value = (rules.containsCombatElement(CombatWarElements.POTIONS)) ? 1 : 0;
				break;
			case 162:
				value = (!rules.isKeepItems()) ? 1 : 0;
				break;
		}
		return value;
	}
	
	public boolean handleWarSetupButtons(Player player, int buttonID) {
		Clan clan = Clans.getClanForOwner(player);
		if(clan != requester && clan != accepter) {
			return false;
		}
		Clan oppClan = (clan == requester) ? accepter : requester;
		Player other = World.getPlayerByName(oppClan.getOwner());
		if(other == null)
			return false;
		switch(buttonID) {
		case 111147:
			this.cancel();
			return true;
		case 111150:
			this.accept(clan);
			return true;
		case 111154:
			rules.setVictoryKills(1);
			sendSetupConfig(player, other, 150);
			return true;
		case 111156:
			rules.setVictoryKills(2);
			sendSetupConfig(player, other, 150);
			return true;
		case 111159:
			rules.setVictoryKills(25);
			sendSetupConfig(player, other, 150);
			return true;
		case 111160:
			rules.setVictoryKills(50);
			sendSetupConfig(player, other, 150);
			return true;
		case 111161:
			rules.setVictoryKills(100);
			sendSetupConfig(player, other, 150);
			return true;
		case 111162:
			rules.setVictoryKills(200);
			sendSetupConfig(player, other, 150);
			return true;
		case 111163:
			rules.setVictoryKills(500);
			sendSetupConfig(player, other, 150);
			return true;
		case 111164:
			rules.setVictoryKills(1000);
			sendSetupConfig(player, other, 150);
			return true;
		case 111165:
			rules.setVictoryKills(2000);
			sendSetupConfig(player, other, 150);
			return true;
		case 111166:
			rules.setVictoryKills(5000);
			sendSetupConfig(player, other, 150);
			return true;
		case 111180:
			rules.setTimeLimit(5);
			player.send(new SendMessage("<col=255>The time limit is now 5 minutes.</col>"));
			sendSetupConfig(player, other, 152);
			return true;
		case 111181:
			rules.setTimeLimit(10);
			player.send(new SendMessage("<col=255>The time limit is now 10 minutes.</col>"));
			sendSetupConfig(player, other, 152);
			return true;
		case 111182:
			rules.setTimeLimit(15);
			player.send(new SendMessage("<col=255>The time limit is now 15 minutes.</col>"));
			sendSetupConfig(player, other, 152);
			return true;
		case 111183:
			rules.setTimeLimit(30);
			player.send(new SendMessage("<col=255>The time limit is now 30 minutes.</col>"));
			sendSetupConfig(player, other, 152);
			return true;
		case 111184:
			rules.setTimeLimit(60);
			player.send(new SendMessage("<col=255>The time limit is now 1 hour.</col>"));
			sendSetupConfig(player, other, 152);
			return true;
		case 111185:
			rules.setTimeLimit(120);
			player.send(new SendMessage("<col=255>The time limit is now 2 hours.</col>"));
			sendSetupConfig(player, other, 152);
			return true;
		case 111176:
			player.send(new SendMessage("<col=255>Stragglers must be killed!</col>"));
			rules.setStragglers(false);
			sendSetupConfig(player, other, 151);
			return true;
		case 111177:
			player.send(new SendMessage("<col=255>5 stragglers will be ignored!</col>"));
			rules.setStragglers(true);
			sendSetupConfig(player, other, 151);
			return true;
		case 111206:
			rules.switchCombatElement(CombatWarElements.MELEE);
			sendSetupConfig(player, other, 155);
			return true;
		case 111208:
			rules.switchCombatElement(CombatWarElements.MAGIC);
			sendSetupConfig(player, other, 156);
			return true;
		case 111210:
			rules.switchCombatElement(CombatWarElements.RANGED);
			sendSetupConfig(player, other, 157);
			return true;
		case 111212:
			rules.switchCombatElement(CombatWarElements.PRAYER);
			sendSetupConfig(player, other, 158);
			return true;
		case 111214:
			rules.switchCombatElement(CombatWarElements.SUMMONING);
			sendSetupConfig(player, other, 159);
			return true;
		case 111216:
			rules.switchCombatElement(CombatWarElements.FOOD);
			sendSetupConfig(player, other, 160);
			return true;
		case 111218:
			rules.switchCombatElement(CombatWarElements.POTIONS);
			sendSetupConfig(player, other, 161);
			return true;
		case 111219:
			rules.setKeepItems(!rules.isKeepItems());
			player.send(new SendString("... You " + (rules.isKeepItems() ? "keep" : "lose") + "\nyour items.", 28611));
			sendSetupConfig(player, other, 162);
			return true;
		case 111197:
			rules.setArena(ClanArena.CLASSIC);
			sendSetupConfig(player, other, 153);
			return true;
		case 111198:
			rules.setArena(ClanArena.PLATEAU);
			sendSetupConfig(player, other, 153);
			return true;
		case 111199:
			rules.setArena(ClanArena.BLASTED_FORREST);
			sendSetupConfig(player, other, 153);
			return true;
		case 111200:
			rules.setArena(ClanArena.TURRETS);
			sendSetupConfig(player, other, 153);
			return true;
		case 111221:
			rules.setArena(ClanArena.FORSAKEN_QUARRY);
			sendSetupConfig(player, other, 153);
			return true;
		}
		return false;
	}
	
	public void cancel() {
		Player requesterPlayer = World.getPlayerByName(requester.getOwner());
		Player accepterPlayer = World.getPlayerByName(accepter.getOwner());
		if(requesterPlayer != null)
			requesterPlayer.send(new SendRemoveInterfaces());
		if(accepterPlayer != null)
			accepterPlayer.send(new SendRemoveInterfaces());
		requester.setWarSetup(null);
		accepter.setWarSetup(null);
	}
	
	public void accept(Clan clan) {
		if(requester == clan)
			requesterAccept = true;
		else if(accepter == clan)
			acceptorAccept = true;
		if(acceptorAccept == true && requesterAccept == true) {
			ClanWars.initClanWar(requester, accepter, rules);
		} else if(acceptorAccept == true && !requesterAccept) {
			Player requesterPlayer = World.getPlayerByName(requester.getOwner());
			requesterPlayer.send(new SendMessage("<col=255>The opponent has accepted.</col>"));
		} else if(requesterAccept == true && !acceptorAccept) {
			Player accepterPlayer = World.getPlayerByName(accepter.getOwner());
			accepterPlayer.send(new SendMessage("<col=255>The opponent has accepted.</col>"));
		}
	}
	
	public ClanWarRules getRules() {
		return rules;
	}
	
	public Clan getRequester() {
		return requester;
	}
	
	public Clan getAccepter() {
		return accepter;
	}
	
}
