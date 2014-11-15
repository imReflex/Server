package org.endeavor.game.content.skill.agility.gnome;

import org.endeavor.engine.task.RunOnceTask;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;

public class ClimbOverTask extends RunOnceTask {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4737269441793944348L;
	private final Player p;
	private final Location l;
	private final double exp;

	public ClimbOverTask(Player p, int delay, Location l, int animation, double exp) {
		super(p, delay);
		this.p = p;
		this.l = l;
		this.exp = exp;
		p.getUpdateFlags().sendAnimation(animation, 0);
	}

	@Override
	public void onStop() {
		p.teleport(new Location(l));
		p.getSkill().addExperience(16, exp);
	}
}
