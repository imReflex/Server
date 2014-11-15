package org.endeavor.engine.task.impl;

import org.endeavor.engine.task.Task;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

/**
 * Handles ticking down the antifire potion effect
 * 
 * @author Arithium
 * 
 */
public class AntifireTask extends Task {

	/**
	 * The cycles before it ends
	 */
	private int cycles;

	/**
	 * The player who drank the antifire potion
	 */
	private final Player player;
	
	/**
	 * The potion is a super potion
	 */
	private final boolean isSuper;

	/**
	 * Constructs a new AntiFireTask for the player
	 * 
	 * @param player
	 *            The player who drank the antifire potion
	 * @param isSuper
	 *            If the potion is a super potion or not
	 */
	public AntifireTask(Player player, boolean isSuper) {
		super(player, 1, false, StackType.STACK, BreakType.NEVER, TaskConstants.NONE);
		this.cycles = 600;
		this.player = player;
		this.isSuper = isSuper;
		/**
		 * To cancel the previous task
		 */
		player.getAttributes().set("fire_resist", Boolean.FALSE);
		player.getAttributes().set("super_fire_resist", Boolean.FALSE);
		
		player.getAttributes().set(isSuper ? "super_fire_resist" : "fire_resist", Boolean.TRUE);
	}

	@Override
	public void execute() {
		if (player.isDead()) {
			this.stop();
			return;
		}
		
		if ((!isSuper && !(Boolean) player.getAttributes().get("fire_resist")) || (isSuper && !(Boolean) player.getAttributes().get("super_fire_resist"))) {
			//cancels this task when starting another one
			this.stop();
			return;
		}
		if (cycles > 0) {
			cycles--;
			
			if (cycles == 100) {
				player.getClient().queueOutgoingPacket(new SendMessage("@red@Your resistance to dragonfire is about to run out."));
			}
			
			if (cycles == 0) {
				player.getClient().queueOutgoingPacket(new SendMessage("@red@Your resistance to dragonfire has run out."));
				this.stop();
				return;
			}
		}
	}

	@Override
	public void onStop() {
		player.getAttributes().set(isSuper ? "super_fire_resist" : "fire_resist", Boolean.FALSE);
	}

}
