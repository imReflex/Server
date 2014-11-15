package org.endeavor.game.content.randoms.impl;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.game.content.randoms.RandomEvent;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;

public class SecurityGuard extends RandomEvent {
	public static final int SECURITY_GUARD_ID = 4375;
	public static final Location[] FAILURE_LOCATIONS = { new Location(3079, 3493, 0) };

	@Override
	public void init(final Player player) {
		final Mob genie = new Mob(player, 4375, false, false, true, player.getLocation());
		genie.getFollowing().setIgnoreDistance(true);
		genie.getFollowing().setFollow(player);

		player.getAttributes().set("randomevent", this);

		Task timeout = new Task(genie, 1) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -228416261101129769L;
			byte stage = 0;

			@Override
			public void execute() {
				if (player.getAttributes().get("pauserandom") != null) {
					return;
				}

				if (player.getAttributes().get("randomevent") == null) {
					stop();
					return;
				}

				if (!doStage(player, genie, stage)) {
					genie.remove();
					stop();
					return;
				}

				stage = ((byte) (stage + 1));
			}

			@Override
			public void onStop() {
			}
		};
		TaskQueue.queue(timeout);
	}

	@Override
	public void onSuccess(final Mob mob) {
		mob.getUpdateFlags().sendAnimation(863, 0);

		TaskQueue.queue(new Task(mob, 2) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 6015713388880514361L;
			byte stage = 0;

			@Override
			public void execute() {
				if (stage == 0) {
					mob.getUpdateFlags().sendGraphic(new Graphic(86, 0, false));
				} else {
					mob.remove();
					stop();
					return;
				}

				stage = ((byte) (stage + 1));
			}

			@Override
			public void onStop() {
			}
		});
	}

	@Override
	public boolean doStage(Player player, Mob mob, byte stage) {
		switch (stage) {
		case 2:
			mob.getUpdateFlags().sendForceMessage("Greetings, " + player.getUsername() + "!");
			mob.getUpdateFlags().sendAnimation(863, 0);
			return true;
		case 15:
			mob.getUpdateFlags().sendForceMessage(player.getUsername() + ", are you there?!");
			mob.getUpdateFlags().sendAnimation(863, 0);
			return true;
		case 45:
			mob.getUpdateFlags().sendForceMessage(player.getUsername() + ", please respond!");
			return true;
		case 60:
			mob.getUpdateFlags().sendForceMessage("DO NOT IGNORE ME, " + player.getUsername().toUpperCase() + "!!");
			return true;
		case 80:
			mob.getUpdateFlags().sendForceMessage("Last chance, " + player.getUsername() + "!");
			return true;
		case 100:
			mob.getUpdateFlags().sendForceMessage("NO ONE IGNORES ME!");
			return true;
		case 102:
			player.getUpdateFlags().sendGraphic(new Graphic(86, 0, false));
			return true;
		case 104:
			fail(player);
			return false;
		}

		return true;
	}

	@Override
	public void doFailure(Player player) {
		player.teleport(FAILURE_LOCATIONS[org.endeavor.engine.utility.Misc.randomNumber(FAILURE_LOCATIONS.length)]);
	}

	@Override
	public void clickButton(Player player, int id) {
	}
}
