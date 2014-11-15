package org.endeavor.game.content.skill.firemaking;

import org.endeavor.engine.cache.map.Region;
import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.game.content.skill.SkillConstants;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.item.impl.GroundItem;
import org.endeavor.game.entity.item.impl.GroundItemHandler;
import org.endeavor.game.entity.object.GameObject;
import org.endeavor.game.entity.object.ObjectManager;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendSound;

public class Firemaking extends Task {
	private Player player;
	private LogData logData;
	private Item log;
	private int animationCycle;
	private static GroundItem groundLog;

	public Firemaking(int delay, Player entity, Item log, LogData logData) {
		super(entity, delay, false, Task.StackType.NEVER_STACK, Task.BreakType.ON_MOVE, 0);
		player = entity;
		this.log = log;
		this.logData = logData;

		player.getMovementHandler().reset();
	}

	private static boolean meetsRequirements(Player player, Item log) {
		int skillLevel = player.getSkill().getLevels()[11];
		LogData data = LogData.getLogById(log.getId());

		int x = player.getLocation().getX();
		int y = player.getLocation().getY();

		if ((x >= 3090) && (y >= 3488) && (x <= 3098) && (y <= 3500)) {
			player.getClient().queueOutgoingPacket(new SendMessage("You cannot light fires here."));
			return false;
		}

		if (skillLevel < data.getLevelRequired()) {
			player.getClient()
					.queueOutgoingPacket(
							new SendMessage("You need a firemaking level of " + data.getLevelRequired()
									+ " to light this log."));
			return false;
		}
		
		if (ObjectManager.objectExists(player.getLocation())) {
			player.getClient().queueOutgoingPacket(new SendMessage("You cannot light a fire here."));
			return false;
		}
		
		if (!player.getInventory().hasItemId(590)) {
			player.getClient().queueOutgoingPacket(new SendMessage("You need a tinderbox to light this log."));
			return false;
		}
		
		return true;
	}

	public static boolean attemptFiremaking(Player player, Item used, Item usedWith) {
		Item log = usedWith.getId() != 590 ? usedWith : used.getId() != 590 ? used : null;
		LogData logData = LogData.getLogById(log.getId());
		
		if (logData == null) {
			return false;
		}
		
		if (!meetsRequirements(player, log)) {
			return true;
		}

		player.getUpdateFlags().sendAnimation(new Animation(733));
		groundLog = new GroundItem(new Item(log.getId(), 1), new Location(player.getLocation()), player.getUsername());
		GroundItemHandler.add(groundLog);
		player.getInventory().remove(log);
		player.getClient().queueOutgoingPacket(new SendSound(811, 1, 5));
		TaskQueue.queue(new Firemaking(1, player, log, logData));
		
		return true;
	}
	
	/*public static boolean firemakeGrounditem(Player player, int groundItem) {
		if (GroundItemHandler.getGroundItem(id, x, y, z, name, specific))
		
		LogData logData = LogData.getLogById(groundItem);
		
		if (logData == null) {
			return false;
		}
		
		
	}*/

	public static boolean success(Player player, Item log) {
		return SkillConstants.isSuccess(player, 11, LogData.getLogById(log.getId()).getLevelRequired());
	}

	private void successfullFiremake(Player player) {
		GroundItemHandler.remove(groundLog);

		player.getUpdateFlags().sendAnimation(65535, 0);

		GameObject object = new GameObject(2732, new Location(player.getLocation()), 10, 0);

		TaskQueue.queue(new FireTask(player, 50 + logData.ordinal() * 15, object));

		player.getSkill().addExperience(11, logData.getExperience());
		walk(player);

		player.getAchievements().incr(player, "Burn 1,000 logs");
	}

	private void walk(Player player) {
		if (!Region.getRegion(player.getLocation().getX(), player.getLocation().getY()).blockedWest(
				player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ())) {
			player.getMovementHandler().walkTo(-1, 0);
		} else if (!Region.getRegion(player.getLocation().getX(), player.getLocation().getY()).blockedEast(
				player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()))
			player.getMovementHandler().walkTo(1, 0);
	}

	@Override
	public void execute() {
		if (!meetsRequirements(player, log)) {
			stop();
			return;
		}
		if (success(player, log)) {
			successfullFiremake(player);
			stop();
			return;
		}
		if (animationCycle < 6) {
			animationCycle += 1;
		} else if (animationCycle == 3) {
			player.getClient().queueOutgoingPacket(new SendSound(375, 1, 0));
		} else {
			player.getClient().queueOutgoingPacket(new SendSound(375, 1, 0));
			player.getUpdateFlags().sendAnimation(new Animation(733));
			animationCycle = 0;
		}
	}

	@Override
	public void onStop() {
	}
}
