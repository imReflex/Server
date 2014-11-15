package org.endeavor.game.content.combat.impl;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.game.entity.player.Player;

public class Skulling implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3081032696283602043L;

	public static final short SKULL_TIME = 500;
	
	private int icon = -1;
	private short left = 0;

	private List<Player> attacked = new LinkedList<Player>();

	public boolean isSkull(Player player, Player attacking) {
		return (!attacking.isNpc()) && (player.inWilderness()) && (!attacking.getSkulling().hasAttacked(player));
	}

	public void tick(final Player player) {
		TaskQueue.queue(new Task(player, 25) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -9142598010415277206L;

			@Override
			public void execute() {
				if (!isSkulled()) {
					return;
				}
				
				if ((left -= 25) <= 0) {
					unskull(player);
				}
			}

			@Override
			public void onStop() {}
		});
	}

	public boolean hasAttacked(Player p) {
		return attacked.contains(p);
	}

	public void checkForSkulling(Player player, Player attack) {
		if (isSkull(player, attack))
			skull(player, attack);
	}

	public void skull(Player player, Player attacking) {
		if (attacking != null) {
			attacked.add(attacking);
		}

		if (left <= 0) {
			player.getAchievements().incr(player, "Get skulled");
			left = SKULL_TIME;
			player.setAppearanceUpdateRequired(true);
			icon = 0;
		}
	}

	public void unskull(Player player) {
		attacked.clear();
		left = 0;
		icon = -1;
		player.setAppearanceUpdateRequired(true);
	}

	public boolean isSkulled() {
		return left > 0;
	}

	public long getLeft() {
		return left;
	}

	public void setLeft(long left) {
		if (left < 0) {
			return;
		}
		
		if (left > Short.MAX_VALUE) {
			left = SKULL_TIME;
		}
		
		this.left = (short) left;
	}

	public int getSkullIcon() {
		return icon;
	}

	public void setSkullIcon(Player player, int skullIcon) {
		this.icon = skullIcon;
		player.setAppearanceUpdateRequired(true);
	}
}
