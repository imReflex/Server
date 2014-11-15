package org.endeavor.game.content.clans.clanwars;

import java.util.TimerTask;

/**
 * 
 * @author allen_000
 *
 */
public class ClanWarTimer extends TimerTask {

	private ClanWar war;
	private boolean started = false;
	private int timeToStart;
	private int timeLeft;
	private boolean wallsDropped = false;
	
	public ClanWarTimer(ClanWar war) {
		this.war = war;
		this.timeToStart = (60 * 1000);
		this.timeLeft = war.getRules().getTimeLimit() * (60 * 1000);
	}
	
	@Override
	public void run() {
		try {
			if(timeToStart > 0) {
				timeToStart -= 1000;
				if(timeToStart <= 4000 && !wallsDropped) {
					war.getWallHandler().dropWalls();
					wallsDropped = true;
				}
				if(timeToStart <= 0) {
					war.getWallHandler().removeWalls();
					started = true;
				}
				war.sendGameInterface();
			} else {
				started = true;
				timeLeft -= 1000;
				war.sendGameInterface();
				if(timeLeft <= 0 || war.isEnded()) {
					this.cancel();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean started() {
		return started;
	}
	
	public int getTimeToStart() {
		return timeToStart;
	}
	
	public int getTimeLeft() {
		return timeLeft;
	}
	
	public int getTime() {
		return started ? (timeLeft) : (timeToStart);
	}
	
	public String formatTime() {
		int time  = this.getTime();
		String display = "";
		display = (time / 60 / 1000) + "m " + ((time / 1000) % 60) + "s";
		return display;
	}
	
	public String getTimeString() {
		return started ? "Countdown to end:" : "Countdown to battle:";
	}

}
