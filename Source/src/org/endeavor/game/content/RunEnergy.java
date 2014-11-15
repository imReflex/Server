package org.endeavor.game.content;

import java.io.Serializable;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.game.entity.player.EquipmentConstants;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerConstants;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.out.impl.SendConfig;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendUpdateEnergy;

/**
 * Handles resting and restoring energy
 * @author Michael Sasse
 *
 */
public class RunEnergy implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -35019109878431563L;
	public static final int RESTORE_TIMER = 2;
	public static final int REST_ANIMATION = 11786;
	public static final int STAND_UP_ANIMATION = 11788;
	private double energy = 100.0D;
	private int weight = 0;
	private boolean allowed = true;
	private boolean running = false;
	private boolean resting = false;
	private transient Player player;

	public RunEnergy(Player player) {
		this.player = player;
	}

	/**
	 * Toggles the resting to start the process
	 */
	public void toggleResting() {
		if (energy >= 100) {
			player.getClient().queueOutgoingPacket(new SendMessage("Your energy is already full."));
			return;
		}
		
		resting = (!resting);

		if (!resting) {
			player.getUpdateFlags().sendAnimation(11788, 0);
			player.getEquipment().updatePlayerAnimations();
			player.getClient().queueOutgoingPacket(new SendConfig(778, 0));
		} else {
			player.getUpdateFlags().sendAnimation(11786, 0);
			player.getAnimations().setStandEmote(11786);
			player.getMovementHandler().reset();
			player.getClient().queueOutgoingPacket(new SendConfig(778, 1));
		}

		player.setAppearanceUpdateRequired(true);
	}

	/**
	 * Deducts energy from the players energy
	 * 
	 * @param amount
	 *            The amount to deduct
	 */
	public void deduct(int amount) {
		energy -= amount;
		if (energy < 0.0D) {
			energy = 0.0D;
		}
		update();
	}

	/**
	 * Deducts energy from the player by percentage
	 * 
	 * @param percent
	 *            The percentage to remove
	 */
	public void deduct(double percent) {
		energy -= (int) (energy * percent);
		if (energy < 0.0D) {
			energy = 0.0D;
		}
		update();
	}

	/**
	 * Adds energy to the players current energy
	 * 
	 * @param amount
	 *            The amount to add
	 */
	public void add(int amount) {
		energy += amount;
		if (energy > 100.0D) {
			energy = 100.0D;
		}
		update();
	}

	/**
	 * Restores the players energy
	 */
	public void restoreAll() {
		energy = 100.0D;
		update();
	}

	/**
	 * Updates the players energy client sided
	 */
	public void update() {
		player.getClient().queueOutgoingPacket(new SendUpdateEnergy(energy));
	}

	/**
	 * Handles the energy tick as well as resting
	 */
	public void tick() {
		TaskQueue.queue(new Task(player, 4, false, Task.StackType.STACK, Task.BreakType.NEVER, 4) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -9121136324474321221L;

			@Override
			public void execute() {
				if ((allowed) && (energy < 100.0D)) {
					RunEnergy en = RunEnergy.this;
					en.energy = (en.energy + (resting ? 5.0D : (1.0D + player.getMaxLevels()[16] * 0.011D)
							* (player.isMember() ? 1.5D : 1.0D)));

					if (energy > 100.0D) {
						energy = 100.0D;
					}

					update();
				} else if (!allowed) {
					allowed = true;
				}
				/*
				 * No need to turn off resting as thats not how it works.. or is this because u want to stop the event?
				 */
				if ((resting) && (energy == 100.0D))
					toggleResting();
			}

			@Override
			public void onStop() {
			}
		});
	}

	/**
	 * Resets the player running
	 */
	public void reset() {
		running = false;
		allowed = true;
	}

	/**
	 * Handles what happens when a player is running
	 */
	public void onRun() {
		/*System.out.println("running..");
		if ((PlayerConstants.isOwner(player)) || ((player.getMaxLevels()[16] == 99) && (player.isRespectedMember()) && (!player.getController().equals(ControllerManager.WILDERNESS_CONTROLLER)))) {
			return;
		}*/
		energy -= (1.0D - player.getMaxLevels()[16] * 0.005D) * (player.isMember() ? 0.5D : 1.0D) 
				* (player.getEquipment().getItems()[EquipmentConstants.BOOTS_SLOT] != null && player.getEquipment().getItems()[EquipmentConstants.BOOTS_SLOT].getId() == 88 ? 0.8 : 1.0);
		update();
		allowed = false;

		if (energy == 0.0D) {
			player.getClient().queueOutgoingPacket(new SendConfig(173, 0));
			player.getClient().queueOutgoingPacket(new SendMessage("You have run out of energy."));
			running = false;
		}
	}

	/**
	 * Gets if a player can run
	 * 
	 * @return
	 */
	public boolean canRun() {
		return energy > 0.0D;
	}

	@Override
	public String toString() {
		return "RunEnergy [energy=" + energy + ", weight=" + weight + "]";
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public int getEnergy() {
		return (int) energy;
	}

	public void setEnergy(double energy) {
		this.energy = energy;
	}

	public boolean isAllowed() {
		return allowed;
	}

	public void setAllowed(boolean allowed) {
		this.allowed = allowed;
	}

	public boolean isResting() {
		return resting;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
}
