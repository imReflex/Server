package org.endeavor.game.content;

import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;

public class MobControl {
	public MobControl(Player player, int id) {
		player.getAttributes().set("npccontrol", this);
		player.getAttributes().set("controllingnpc", new Mob(id, false, player.getLocation()));
	}

	public void anim(Player player, int id) {
		((Mob) player.getAttributes().get("controllingnpc")).getUpdateFlags().sendAnimation(id, 0);
	}

	public void remove(Player player) {
		((Mob) player.getAttributes().get("controllingnpc")).remove();
	}
}
