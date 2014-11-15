package org.endeavor.game.content.diversions;

import java.util.ArrayList;
import java.util.List;
import org.endeavor.engine.task.RunOnceTask;
import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.object.GameObject;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class EvilTree extends GameObject {
	private static boolean spawned = false;

	private final List<Player> interacted = new ArrayList<Player>();
	private byte stage = 0;

	private static final int[] TREES = { 11435, 11437, 11441, 11444, 11916, 11919, 11922 };

	private static final Location[] LOCATIONS = { new Location(3105, 3495), new Location(2790, 3430),
			new Location(2818, 3478), new Location(3211, 3205), new Location(2978, 3401), new Location(3198, 3433),
			new Location(3282, 3198), new Location(3095, 3229) };

	public static void start() {
		TaskQueue.queue(new Task(200) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 561820255623877956L;

			@Override
			public void execute() {
				if (EvilTree.spawned) {
					return;
				}

				if (Misc.randomNumber(6) == 0) {
					new EvilTree(EvilTree.LOCATIONS[Misc.randomNumber(EvilTree.LOCATIONS.length)]);
					EvilTree.spawned = true;
					World.sendGlobalMessage("An Evil tree sapling has spawned!", true);
				}
			}

			@Override
			public void onStop() {
			}
		});
	}

	public EvilTree(Location location) {
		super(11391, location.getX(), location.getY(), location.getZ(), 10, 0, true);

		TaskQueue.queue(new RunOnceTask(null, 1000) {
			@Override
			public void onStop() {
				grow();
			}
		});
	}

	public void interact(Player player, int option) {
		if (stage == 0) {
			if (interacted.contains(player)) {
				player.getClient().queueOutgoingPacket(new SendMessage("You've already nurtured this sapling."));
				return;
			}

			interacted.add(player);
			player.getClient().queueOutgoingPacket(new SendMessage("You nurture the sapling."));

			player.getSkill().addExperience(8, 100.0D);
			player.getSkill().addExperience(11, 100.0D);
			player.getSkill().addExperience(19, 100.0D);

			player.getUpdateFlags().sendAnimation(2291, 0);
		} else if ((stage != 1) || (option == 1))
			;
	}

	public void grow() {
		setId(TREES[Misc.randomNumber(TREES.length)]);
		interacted.clear();
		World.sendGlobalMessage("An Evil tree has become fully grown!", true);
		stage = 1;
	}
}
