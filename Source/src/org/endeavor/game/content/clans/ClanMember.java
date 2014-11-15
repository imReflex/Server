package org.endeavor.game.content.clans;

import java.io.Serializable;

import org.endeavor.game.entity.World;
import org.endeavor.game.entity.player.Player;

/**
 * 
 * @author Allen Kinzalow
 *
 */
public class ClanMember implements Serializable {

	private static final long serialVersionUID = -7683145006521751188L;
	
	private String username;
	private transient Player player;
	private ClanRanks rank;
	
	private int clanKills;
	private int clanDeaths;
	
	public ClanMember(Player player, ClanRanks rank) {
		this.username = player.getUsername();
		this.rank = rank;
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		if(player.getUsername().equalsIgnoreCase(username))
			this.player = player;
	}
	
	public String getPlayerUsername() {
		return this.username;
	}
	
	public ClanRanks getRank() {
		return rank;
	}
	
	public void setRank(ClanRanks rank) {
		this.rank = rank;
	}

}
