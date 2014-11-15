package org.endeavor.game.entity.player;

import java.io.Serializable;

import org.endeavor.GameSettings;
import org.endeavor.game.content.combat.Hit;
import org.endeavor.game.content.skill.SkillConstants;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class RareDropEP implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7972917694333626504L;
	private double ep = 0;
	private byte received = 0;
	
	public RareDropEP() {}
	
	public void forHitOnMob(Player player, Mob mob, Hit hit) {
		if (hit.getDamage() > 0) {
			if (player.getController().equals(ControllerManager.DEFAULT_CONTROLLER) 
					|| player.getController().equals(ControllerManager.GOD_WARS_CONTROLLER) || player.getController().equals(ControllerManager.WILDERNESS_CONTROLLER)) {
				
				ep += (((double) hit.getDamage()) / 5000.0) + (((double) mob.getLevels()[SkillConstants.DEFENCE]) / 4000.0);
				
				if (GameSettings.DEV_MODE) {
					player.send(new SendMessage("Rare drop EP: " + ep));
				}
			}
		}
	}
	
	public double getEp() {
		return ep;
	}
	
	public int getEpAddon() {
		return (int) ep;
	}
	
	public void reset() {
		ep = 0;
	}
	
	public void setEp(double ep) {
		this.ep = ep;
	}

	public int getReceived() {
		return received;
	}

	public void setReceived(int received) {
		this.received = (byte) received;
	}
	
	public void addReceived() {
		received++;
	}
	
}
