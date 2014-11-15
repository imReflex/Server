package org.endeavor.game.content.clans;

import java.io.Serializable;

/**
 * An enumeration of Clan Rankings.
 * @author Allen
 *
 */
public enum ClanRanks implements Serializable {

	GUEST("Guest", "[REG]", 0),
	MEMBER("Member", "[FRI]", 1),
	RECRUIT("Recruit", "[REC]", 2),
	CORPORAL("Corporal", "[COR]", 3),
	SERGEANT("Sergeant", "[SER]", 4),
	LIEUTENANT("Lieutenant", "[LIE]", 5),
	CAPTAIN("Captain", "[CAP]", 6),
	GENERAL("General", "[GEN]", 7),
	OWNER("Owner", "[OWN]", 8),
	ADMIN("Admin", "[MOD]", 9);
	
	
	String rankName;
	String prefix;
	int identifier;
	
	ClanRanks(String rankName, String prefix, int identifier) {
		this.rankName = rankName;
		this.prefix = prefix;
		this.identifier = identifier;
	}
	
	public String getRankName() {
		return this.rankName;
	}
	
	public String getPrefix() {
		return this.prefix;
	}
	
	public int getID() {
		return this.identifier;
	}
	
	@Override
	public String toString() {
		return this.rankName;
	}
	
	/**
	 * Retrieve a ClanRanks for a given prefix.
	 * @param prefix
	 * @return
	 */
	public static ClanRanks getForPrefix(String prefix) {
		for(ClanRanks ranks : ClanRanks.values())
			if(ranks.getPrefix().equalsIgnoreCase(prefix))
				return ranks;
		return null;
	}
	
}
