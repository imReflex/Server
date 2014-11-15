package org.endeavor.game.content.minigames.pestcontrol.impl;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.task.impl.FollowToEntityTask;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.minigames.pestcontrol.Pest;
import org.endeavor.game.content.minigames.pestcontrol.PestControlConstants;
import org.endeavor.game.content.minigames.pestcontrol.PestControlGame;
import org.endeavor.game.content.minigames.pestcontrol.Portal;
import org.endeavor.game.entity.Location;

public class Spinner extends Pest {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6424775419093426555L;
	private final Portal portal;
	private final Task heal = null;

	public Spinner(Location l, PestControlGame game, Portal portal) {
		super(game, PestControlConstants.SPINNERS[Misc.randomNumber(PestControlConstants.SPINNERS.length)], l);
		setRetaliate(false);
		this.portal = portal;
	}

	@Override
	public void tick() {
		if (portal.isDead()) {
			return;
		}
		
		if ((portal.isDamaged()) && (Misc.randomNumber(3) == 0)) {
			heal();
		}
	}

	public void heal() {
		if ((heal == null) || (heal.stopped())) {
			TaskQueue.queue(new FollowToEntityTask(this, portal) {
				/**
				 * 
				 */
				private static final long serialVersionUID = 2873643440358045056L;

				@Override
				public void onDestination() {
					getUpdateFlags().sendAnimation(3911, 0);
					portal.heal(5);
					stop();
				}
			});
		}
	}
}
