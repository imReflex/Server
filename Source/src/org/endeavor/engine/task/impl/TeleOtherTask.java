package org.endeavor.engine.task.impl;

import org.endeavor.engine.task.Task;
import org.endeavor.game.content.skill.magic.MagicSkill;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;

public class TeleOtherTask extends Task {
	private final Player player;
	private final Location to;

	public TeleOtherTask(Entity entity, Player player, Location to) {
		super(entity, 2);
		this.to = to;
		this.player = player;

		entity.getUpdateFlags().sendAnimation(1818, 0);
		entity.getUpdateFlags().sendGraphic(new Graphic(343, 15, true));
	}

	@Override
	public void execute() {
		player.getMagic().teleport(to.getX(), to.getY(), to.getZ(), MagicSkill.TeleportTypes.TELE_OTHER);
		stop();
	}

	@Override
	public void onStop() {
	}
}
