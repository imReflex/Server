package org.endeavor.game.content.minigames.bountyhunter;

import java.util.TimerTask;

import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

/**
 * 
 * @author Allen K.
 *
 */
public class PenatlyTimer extends TimerTask {

	private transient Player player;
	private int pickupTime = 0;
	private int exitTime = 0;
	private int enterTime = 0;
	private boolean remove = false;
	
	public PenatlyTimer(Player player, int pickupTime, int exitTime, int enterTime) {
		this.player = player;
		this.pickupTime = pickupTime;
		this.exitTime = exitTime;
		this.enterTime = enterTime;
	}
	
	@Override
	public void run() {
		if(remove) {
			this.cancel();
			return;
		}	
		if((!hasExitTime() && !hasEnterTime() && !hasPickupTime()) || player == null) {
			if(player != null)
				BountyHunter.getSingleton().sentBountyInterface(player);
			this.cancel();
		}
		if(player.getController() instanceof BountyHunterController) {
			if(exitTime > 0) {
				exitTime -= 1;
				BountyHunter.getSingleton().sentBountyInterface(player);
				if(exitTime == 0) {
					player.send(new SendMessage("Your leave penalty has been lifted."));
					player.setPenaltyTimer(null);
					this.cancel();
				}
			} else {
				if(pickupTime > 0) {
					pickupTime -= 1;
					BountyHunter.getSingleton().sentBountyInterface(player);
					if(pickupTime == 0) {
						player.send(new SendMessage("Your pickup penalty has been lifted."));
						player.setPenaltyTimer(null);
						this.cancel();
					}
				}
			}
		} else {
			if(enterTime > 0) {
				enterTime -= 1;
				if(enterTime == 0) {
					player.send(new SendMessage("You may now enter BountyHunter again."));
					player.setPenaltyTimer(null);
					this.cancel();
				}
			}
		}
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public boolean hasEnterTime() {
		return enterTime > 0;
	}
	
	public boolean hasPickupTime() {
		return pickupTime > 0;
	}
	
	public boolean hasExitTime() {
		return exitTime > 0;
	}
	
	public void setEnterTime(int time) {
		enterTime = time;
	}
	
	public void setExitTime(int time) {
		exitTime = time;
	}
	
	public void setPickupTime(int time) {
		pickupTime = time;
	}
	
	public int getPickupTime() {
		return pickupTime;
	}
	
	public int getExitTime() {
		return exitTime;
	}
	
	public int getEnterTime() {
		return enterTime;
	}
	
	public void setRemove(boolean remove) {
		this.remove = remove;
	}

}
