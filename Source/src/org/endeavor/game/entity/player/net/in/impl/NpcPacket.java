package org.endeavor.game.entity.player.net.in.impl;

import org.endeavor.GameSettings;
import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.game.content.randoms.RandomExecutor;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.following.Following;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.WalkToActions;
import org.endeavor.game.entity.player.net.in.IncomingPacket;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;

public class NpcPacket extends IncomingPacket {
	public static final int FIRST_CLICK = 155;
	public static final int SECOND_CLICK = 17;
	public static final int THIRD_CLICK = 21;
	public static final int FOURTH_CLICK = 230;
	public static final int ATTACK = 72;
	public static final int MAGIC_ON_NPC = 131;
	public static final int ITEM_ON_NPC = 57;

	@Override
	public int getMaxDuplicates() {
		return 1;
	}

	@Override
	public void handle(Player player, StreamBuffer.InBuffer in, int opcode, int length) {
		if ((player.isDead()) || (!player.getController().canClick())) {
			return;
		}

		RandomExecutor.tryRandom(player);

		player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());

		player.getCombat().reset();

		if (!player.getMagic().isDFireShieldEffect()) {
			player.getMagic().getSpellCasting().resetOnAttack();
		}

		switch (opcode) {
		case 155:
			int slot = in.readShort(true, StreamBuffer.ByteOrder.LITTLE);

			if ((!World.isMobWithinRange(slot)) || (World.getNpcs()[slot] == null)) {
				return;
			}

			WalkToActions.clickNpc(player, 1, slot);
			break;
		case 17:
			slot = in.readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE) & 0xFFFF;

			if ((!World.isMobWithinRange(slot)) || (World.getNpcs()[slot] == null)) {
				return;
			}

			WalkToActions.clickNpc(player, 2, slot);
			break;
		case 21:
			slot = in.readShort(true);

			if ((!World.isMobWithinRange(slot)) || (World.getNpcs()[slot] == null)) {
				return;
			}

			WalkToActions.clickNpc(player, 3, slot);
			break;
		case 230:
			slot = in.readShort(true, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
			if(GameSettings.DEV_MODE)
				System.out.println("NPC 4 Click: " + slot);
			if ((!World.isMobWithinRange(slot)) || (World.getNpcs()[slot] == null)) {
				return;
			}

			WalkToActions.clickNpc(player, 4, slot);

			break;
		case 72:
			slot = in.readShort(StreamBuffer.ValueType.A);

			Mob mob = World.getNpcs()[slot];

			if ((!World.isMobWithinRange(slot)) || (World.getNpcs()[slot] == null)) {
				return;
			}

			if (mob.getId() == 528) {
				WalkToActions.clickNpc(player, 4, slot);
				return;
			}

			player.getMovementHandler().reset();

			player.getCombat().setAttacking(mob);
			player.getFollowing().setFollow(mob, Following.FollowType.COMBAT);
			if (GameSettings.DEV_MODE) {
				player.getClient().queueOutgoingPacket(new SendMessage("npc id " + mob.getId()));
			}
			break;
		case 131:
			slot = in.readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
			int magicId = in.readShort(StreamBuffer.ValueType.A);

			player.getMovementHandler().reset();
			mob = World.getNpcs()[slot];

			if ((!World.isMobWithinRange(slot)) || (World.getNpcs()[slot] == null)) {
				return;
			}

			if (GameSettings.DEV_MODE) {
				player.getClient().queueOutgoingPacket(new SendMessage("Magic id: " + magicId));
			}

			player.getMagic().getSpellCasting().castCombatSpell(magicId, mob);
			break;
		case 57:
			int itemId = in.readShort(StreamBuffer.ValueType.A);
			slot = in.readShort(StreamBuffer.ValueType.A);
			int itemSlot = in.readShort(StreamBuffer.ByteOrder.LITTLE);

			if ((!World.isMobWithinRange(slot)) || (World.getNpcs()[slot] == null)) {
				return;
			}

			if (!player.getInventory().slotContainsItem(itemSlot, itemId)) {
				return;
			}

			WalkToActions.useItemOnNpc(player, itemId, slot);
		}
	}
}
