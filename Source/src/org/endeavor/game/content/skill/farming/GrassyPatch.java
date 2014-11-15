package org.endeavor.game.content.skill.farming;

import java.io.Serializable;
import java.util.Calendar;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class GrassyPatch implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2850498559728864028L;
	private byte stage = 0;
	private int minute;
	private int hour;
	private int day;
	private int year;

	public void setTime() {
		minute = Calendar.getInstance().get(12);
		hour = Calendar.getInstance().get(11);
		day = Calendar.getInstance().get(6);
		year = Calendar.getInstance().get(1);
	}

	public boolean isRaked() {
		return stage == 3;
	}

	public void process(Player player, int index) {
		if (stage == 0) {
			return;
		}

		int elapsed = Misc.getMinutesElapsed(minute, hour, day, year);
		int grow = 4;

		if (elapsed >= grow) {
			for (int i = 0; i < elapsed / grow; i++) {
				if (stage == 0) {
					return;
				}

				stage = ((byte) (stage - 1));
			}

			setTime();
		}
	}

	public void click(Player player, int option, int index) {
		if (option == 1)
			rake(player, index);
	}

	public void rake(final Player p, final int index) {
		if (isRaked()) {
			p.getClient().queueOutgoingPacket(new SendMessage("This plot is already fully raked."));
			return;
		}

		if (!p.getInventory().hasItemId(5341)) {
			p.getClient().queueOutgoingPacket(new SendMessage("You need a rake!"));
			return;
		}

		p.getUpdateFlags().sendAnimation(2273, 0);

		TaskQueue.queue(new Task(p, 4, false, Task.StackType.NEVER_STACK, Task.BreakType.ON_MOVE, 0) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 6363269031476877916L;

			@Override
			public void execute() {
				p.getUpdateFlags().sendAnimation(2273, 0);

				setTime();
				GrassyPatch tmp25_22 = GrassyPatch.this;
				tmp25_22.stage = ((byte) (tmp25_22.stage + 1));
				doConfig(p, index);

				p.getSkill().addExperience(19, 1.0D);
				p.getInventory().addOrCreateGroundItem(6055, 1, true);

				if (isRaked()) {
					p.getClient().queueOutgoingPacket(new SendMessage("The plot is now fully raked."));
					p.getUpdateFlags().sendAnimation(65535, 0);
					stop();
					return;
				}
			}

			@Override
			public void onStop() {
			}
		});
	}

	public void doConfig(Player p, int index) {
		p.getFarming().doConfig();
	}

	public int getConfig(int index) {
		return stage * FarmingPatches.values()[index].mod;
	}
}
