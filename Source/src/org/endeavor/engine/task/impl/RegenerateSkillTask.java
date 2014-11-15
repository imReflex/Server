package org.endeavor.engine.task.impl;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.game.content.skill.Skill;
import org.endeavor.game.content.skill.SkillConstants;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.EquipmentConstants;
import org.endeavor.game.entity.player.Player;

public class RegenerateSkillTask extends Task {

	private final Entity entity;
	private Skill skill = null;

	public static final String EXRTA_HP_REGEN_TASK = "extrahpregentask";

	public RegenerateSkillTask(Entity entity, int delay) {
		super(entity, delay, false, StackType.NEVER_STACK, BreakType.NEVER, TaskConstants.SKILL_RESTORE);

		if (entity == null) {
			stop();
		}

		if (!entity.isNpc()) {
			Player p = World.getPlayers()[entity.getIndex()];
			if (p != null) {
				skill = p.getSkill();
			}
		}

		this.entity = entity;
	}

	@Override
	public void execute() {
		if (entity == null) {
			stop();
			return;
		}

		/**
		 * Check for the Regen bracelet
		 */
		if (!entity.isNpc()) {
			final Player p = World.getPlayers()[entity.getIndex()];
			if (p != null) {
				if (p.getAttributes().get(EXRTA_HP_REGEN_TASK) == null) {
					Item gl = p.getEquipment().getItems()[EquipmentConstants.GLOVES_SLOT];

					if (gl != null && gl.getId() == 11133) {
						p.getAttributes().set(EXRTA_HP_REGEN_TASK, (byte) 0);
						Task t = new Task(p, 25) {

							@Override
							public void execute() {
								Item gl = p.getEquipment().getItems()[EquipmentConstants.GLOVES_SLOT];

								if (gl == null || gl != null && gl.getId() != 11133) {
									p.getAttributes().remove(EXRTA_HP_REGEN_TASK);
									stop();
									return;
								}

								if (p.getLevels()[3] < p.getMaxLevels()[3]) {
									p.getLevels()[3] += 1;
									p.getSkill().update(3);
								}
							}

							@Override
							public void onStop() {
							}

						};

						TaskQueue.queue(t);
					}
				}
			}
		}

		for (int i = 0; i < (!entity.isNpc() ? SkillConstants.SKILL_COUNT : 7); i++) {
			if (i > 7 && entity.isNpc()) {
				break;
			}

			if (i == SkillConstants.PRAYER || i == SkillConstants.HITPOINTS 
					&& entity.getLevels()[SkillConstants.HITPOINTS] > entity.getMaxLevels()[SkillConstants.HITPOINTS]) {
				continue;
			}

			int lvl = entity.getLevels()[i];
			int max = entity.getMaxLevels()[i];

			if (lvl != max) {
				entity.getLevels()[i] += (lvl < max ? 1 : -1);
				if (skill != null) {
					skill.update(i);
				}
			}
		}
	}

	@Override
	public void onStop() {
	}
}
