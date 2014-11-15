package org.endeavor.game.content;

import org.endeavor.engine.cache.map.Door;
import org.endeavor.engine.cache.map.DoubleDoor;
import org.endeavor.engine.cache.map.Region;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.task.impl.TickDoorTask;
import org.endeavor.game.entity.object.GameObject;
import org.endeavor.game.entity.object.ObjectManager;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendSound;

public class Doors {
	public static final int[][] JAMMED_DOORS = { { 1852, 5212 }, { 1832, 5224 }, { 1832, 5225 }, { 2854, 3546 },
			{ 2855, 3546 } };

	public static boolean isDoorJammed(Player player, int x, int y, int z) {
		for (int[] i : JAMMED_DOORS) {
			if ((x == i[0]) && (y == i[1])) {
				player.getClient().queueOutgoingPacket(new SendMessage("This door won't open.."));
				return true;
			}
		}

		return false;
	}

	public static boolean clickDoor(Player player, int x, int y, int z) {
		if (clickDoor(x, y, z)) {
			player.getClient().queueOutgoingPacket(new SendSound(326, 0, 0));
			return true;
		}

		return false;
	}

	public static boolean clickDoor(int x, int y, int z) {
		if (Region.isDoor(x, y, z)) {
			Door door = Region.getDoor(x, y, z);

			if (door == null) {
				return false;
			}

			if (z > 3) {
				z %= 4;
			}
			
			boolean original = door.original();
			
			if (original) {
				ObjectManager.register(new GameObject(2376, x, y, z, door.getType(), door.getCurrentFace(), true));
			} else {
				ObjectManager.remove(new GameObject(door.getCurrentId(), x, y, z, door.getType(), door.getCurrentFace(), true));
			}
			
			Region.getRegion(x, y).appendDoor(door.getCurrentId(), x, y, z);

			if (door.original()) {
				ObjectManager.removeFromList(new GameObject(2376, door.getX(), door.getY(), z, door.getType(), door
						.getCurrentFace(), true));
				ObjectManager.queueSend(new GameObject(door.getCurrentId(), door.getX(), door.getY(), z,
						door.getType(), door.getCurrentFace(), true));
			} else {
				ObjectManager.register(new GameObject(door.getCurrentId(), door.getX(), door.getY(), z, door.getType(),
						door.getCurrentFace(), true));

				TaskQueue.queue(new TickDoorTask(door));
			}
			
			return true;
		}

		if (Region.isDoubleDoor(x, y, z)) {
			DoubleDoor door = Region.getDoubleDoor(x, y, z);

			if (door == null) {
				return false;
			}

			if (z > 3) {
				z %= 4;
			}

			ObjectManager.removeFromList(new GameObject(door.getCurrentId1(), door.getX1(), door.getY1(), z, door
					.getType(), door.getCurrentFace1(), true));
			ObjectManager.register(new GameObject(2376, door.getX1(), door.getY1(), z, door.getType(), door
					.getCurrentFace1(), true));

			ObjectManager.removeFromList(new GameObject(door.getCurrentId2(), door.getX2(), door.getY2(), z, door
					.getType(), door.getCurrentFace2(), true));
			ObjectManager.register(new GameObject(2376, door.getX2(), door.getY2(), z, door.getType(), door
					.getCurrentFace2(), true));

			Region.getRegion(x, y).appendDoubleDoor(door.getCurrentId1(), door.getX1(), door.getY1(), z);

			ObjectManager.removeFromList(new GameObject(2376, door.getX1(), door.getY1(), z, door.getType(), door
					.getCurrentFace1(), true));
			ObjectManager.removeFromList(new GameObject(2376, door.getX2(), door.getY2(), z, door.getType(), door
					.getCurrentFace2(), true));

			ObjectManager.register(new GameObject(door.getCurrentId1(), door.getX1(), door.getY1(), z, door.getType(),
					door.getCurrentFace1(), true));
			ObjectManager.register(new GameObject(door.getCurrentId2(), door.getX2(), door.getY2(), z, door.getType(),
					door.getCurrentFace2(), true));
			
			return true;
		}
		
		return false;
	}
}
