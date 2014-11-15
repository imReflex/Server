package org.endeavor.engine.task.impl;

import org.endeavor.engine.task.Task;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;

/**
 * Used to handle npc interactivity such as pickpocketing.
 * 
 * 
 */
public abstract class EntityInteractionTask extends Task {

	public EntityInteractionTask(Player entity, int ticks) {
		super(entity, ticks);
	}

	/**
	 * The message when you do not have the level required to interact with the
	 * entity.
	 * 
	 * @return the message to display.
	 */
	public abstract String getInsufficentLevelMessage();

	/**
	 * The message when you begin to interact with the entity.
	 * 
	 * @return the message to display.
	 */
	public abstract String getInteractionMessage();

	/**
	 * The message when you succeed to interact as planned with the entity.
	 * 
	 * @return the message to display.
	 */
	public abstract String getSuccessfulInteractionMessage();

	/**
	 * The message when you fail to interact as planned with the entity.
	 * 
	 * @return the message to display.
	 */
	public abstract String getUnsuccessfulInteractionMessage();

	/**
	 * 
	 * @return
	 */
	public abstract Mob getInteractingMob();

	/**
	 * 
	 * @return
	 */
	public abstract Item[] getConsumedItems();

	/**
	 * 
	 * @return
	 */
	public abstract Item[] getRewards();

	/**
	 * 
	 * @return
	 */
	public abstract short getRequiredLevel();

}
