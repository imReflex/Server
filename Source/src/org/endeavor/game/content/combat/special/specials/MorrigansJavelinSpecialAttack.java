package org.endeavor.game.content.combat.special.specials;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.combat.Hit;
import org.endeavor.game.content.combat.special.Special;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.player.Player;

public class MorrigansJavelinSpecialAttack implements Special {
	@Override
	public void handleAttack(Player player) {
		player.getCombat().getRanged().setStart(new Graphic(1836, 0, true));

		Entity e = player.getCombat().getAttacking();

		if ((e != null) && (!e.isNpc())) {
			final Player p = org.endeavor.game.entity.World.getPlayers()[e.getIndex()];

			if (p != null)
				TaskQueue.queue(new Task(p, 2) {
					private byte c = 0;

					@Override
					public void execute() {
						if (p.isDead() || !p.getController().allowPvPCombat()) {
							stop();
							return;
						}

						p.hit(new Hit(Misc.randomNumber(5) + 1));

						if ((this.c = (byte) (c + 1)) == 9)
							stop();
					}

					@Override
					public void onStop() {
					}
				});
		}
	}

	@Override
	public int getSpecialAmountRequired() {
		return 50;
	}

	@Override
	public boolean checkRequirements(Player player) {
		return true;
	}
}
