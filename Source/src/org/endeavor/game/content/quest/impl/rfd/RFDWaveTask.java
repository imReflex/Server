package org.endeavor.game.content.quest.impl.rfd;

import org.endeavor.engine.task.Task;
import org.endeavor.game.content.quest.QuestConstants;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;

public class RFDWaveTask extends Task {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7422230659787833473L;
	private final Player player;
	private byte wave = 0;
	private Mob mob = null;

	public RFDWaveTask(Player player) {
		super(player, 3);
		this.player = player;

		player.getPrayer().disable();
	}

	@Override
	public void execute() {
		if ((player.isDead()) || (!player.getController().equals(RecipeForDisaster.RFD_CONTROLLER))) {
			if (mob != null) {
				mob.remove();
			}

			stop();
			return;
		}
		if ((mob == null) || ((mob.isDead()) && (!mob.isVisible()))) {
			if (wave == RFDConstants.WAVES.length) {
				QuestConstants.RECIPE_FOR_DISASTER.doAction(player, wave);
				stop();
				return;
			}

			QuestConstants.RECIPE_FOR_DISASTER.doAction(player, wave);
			mob = ((Mob) player.getAttributes().get("currentRFDMob"));

			mob.getCombat().setAttack(player);

			wave = ((byte) (wave + 1));

			if (wave == RFDConstants.WAVES.length) {
				this.setTaskDelay(1);
				mob.getUpdateFlags().sendForceMessage("Now you will die!");
			}
		}
	}

	@Override
	public void onStop() {
	}
}
