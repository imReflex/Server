package org.endeavor.game.content.clans;

import java.io.Serializable;

/**
 * 
 * @author allen_000
 *
 */
public class ClanSettings implements Serializable {
	
	private static final long serialVersionUID = -5701337679916749849L;
	private ClanRanks canJoin;
	private ClanRanks canTalk;
	private ClanRanks canKick;
	private String title;
	
	public ClanSettings(String title, ClanRanks canJoin, ClanRanks canTalk, ClanRanks canKick) {
		this.title = title;
		this.canJoin = canJoin;
		this.canTalk = canTalk;
		this.canKick = canKick;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setCanJoin(ClanRanks rank) {
		this.canJoin = rank;
	}
	
	public void setCanTalk(ClanRanks rank) {
		this.canTalk = rank;
	}
	
	public void setCanKick(ClanRanks rank) {
		this.canKick = rank;
	}
	
	public ClanRanks getJoinRank() {
		return canJoin;
	}
	
	public ClanRanks getCanTalk() {
		return canTalk;
	}
	
	public ClanRanks getCanKick() {
		return canKick;
	}
	
	public boolean canJoin(ClanRanks rank) {
		return rank.getID() >= canJoin.getID();
	}
	
	public boolean canTalk(ClanRanks rank) {
		return rank.getID() >= canTalk.getID();
	}
	
	public boolean canKick(ClanRanks rank) {
		return rank.getID() >= canKick.getID();
	}
	
}
