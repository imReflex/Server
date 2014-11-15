package org.endeavor.game.entity.mob.abilities;

import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.task.impl.HitTask;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.combat.CombatEffect;
import org.endeavor.game.content.combat.Hit;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.Graphic;

public class IcyBonesAbility implements CombatEffect {
	@Override
	public void execute(Entity e1, Entity e2) {
		if (Misc.randomNumber(5) == 0) {
			e2.getUpdateFlags().sendGraphic(new Graphic(2598, 0, 0));
			TaskQueue.queue(new HitTask(2, false, new Hit(Misc.randomNumber(10)), e2));
		}
	}
}
