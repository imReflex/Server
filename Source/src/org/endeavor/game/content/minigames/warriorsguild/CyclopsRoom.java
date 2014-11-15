package org.endeavor.game.content.minigames.warriorsguild;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.item.impl.GroundItemHandler;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.controllers.GenericMinigameController;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public final class CyclopsRoom extends AnimatorConstants {
	public static final TokenRoomController TOKEN_ROOM_CONTROLLER = new TokenRoomController();

	public static int getDefender(Player player) {
		int currentDefender = -1;

		Item shield = player.getEquipment().getItems()[5];

		for (int i = 0; i < DEFENDERS.length; i++) {
			if ((player.getInventory().hasItemId(DEFENDERS[i]))
					|| ((shield != null) && (shield.getId() == DEFENDERS[i]))) {
				currentDefender = i;
			}
		}

		if ((currentDefender >= -1) && (currentDefender != 7)) {
			currentDefender++;
		}

		return DEFENDERS[currentDefender];
	}

	public static void dropDefender(Player player, Mob mob) {
		if (Misc.randomNumber(7) != 0) {
			return;
		}

		if (player.getAttributes().get("cyclopsdefenderdrop") == null) {
			return;
		}

		int defender = ((Integer) player.getAttributes().get("cyclopsdefenderdrop")).intValue();

		GroundItemHandler.add(new Item(defender), mob.getLocation(), player);

		if (defender != 18653) {
			player.getAttributes().set("cyclopsdefenderdrop", Integer.valueOf(defender + 1));
			player.getClient().queueOutgoingPacket(
					new SendMessage("Cyclops is now dropping: " + Item.getDefinition(defender + 1).getName() + "."));
		}
	}

	public static boolean enterCyclopsRoom(Player player) {
		if (player.getLocation().getX() <= 2846) {
			if (player.getInventory().getItemAmount(8851) >= 100) {
				player.teleport(new Location(2847, 3541, 2));
				executeTimer(player);

				int defender = getDefender(player);

				player.getAttributes().set("cyclopsdefenderdrop", Integer.valueOf(defender));
				player.getClient().queueOutgoingPacket(
						new SendMessage("Cyclops is now dropping: " + Item.getDefinition(defender).getName() + "."));

				player.setController(TOKEN_ROOM_CONTROLLER);
				return false;
			}
			player.getClient().queueOutgoingPacket(new SendMessage("You need at least 100 tokens to enter!"));
			return true;
		}

		if (player.getAttributes().get("warrguildtokentask") != null) {
			((Task) player.getAttributes().get("warrguildtokentask")).stop();
		}

		player.getAttributes().remove("cyclopsdefenderdrop");
		player.getAttributes().remove("warrguildtokentask");

		player.setController(ControllerManager.DEFAULT_CONTROLLER);

		player.teleport(new Location(2846, 3540, 2));
		return true;
	}

	public static void executeTimer(Player player) {
		Task task = new TokenTask(player, (byte) 100);
		player.getAttributes().set("warrguildtokentask", task);
		TaskQueue.queue(task);
	}

	public static class TokenRoomController extends GenericMinigameController {
		/**
		 * 
		 */
		private static final long serialVersionUID = -5577110674799221745L;

		@Override
		public void tick(Player p) {
		}

		@Override
		public boolean isSafe() {
			return true;
		}

		@Override
		public boolean canAttackNPC() {
			return true;
		}

		@Override
		public boolean canAttackPlayer(Player p, Player p2) {
			return false;
		}

		@Override
		public boolean allowMultiSpells() {
			return false;
		}

		@Override
		public boolean allowPvPCombat() {
			return false;
		}

		@Override
		public void onDeath(Player p) {
		}

		@Override
		public Location getRespawnLocation(Player player) {
			return new Location(2846, 3540, 2);
		}

		@Override
		public void onDisconnect(Player p) {
			p.teleport(new Location(2846, 3540, 2));
		}

		@Override
		public void onControllerInit(Player p) {
		}

		@Override
		public boolean canUseCombatType(Player p, CombatTypes type) {
			return true;
		}

		@Override
		public boolean canEquip(Player p, int id, int slot) {
			return true;
		}

		@Override
		public boolean canUsePrayer(Player p) {
			return true;
		}

		@Override
		public boolean canEat(Player p) {
			return true;
		}

		@Override
		public boolean canDrink(Player p) {
			return true;
		}

		@Override
		public boolean canUseSpecialAttack(Player p) {
			return true;
		}

		@Override
		public String toString() {
			return "Cyclops WG Room";
		}

		@Override
		public boolean onClick(Player player, int buttonID) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onObject(Player player, int objectID) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onNpc(Player player, int npcID) {
			// TODO Auto-generated method stub
			return false;
		}
	}
}
