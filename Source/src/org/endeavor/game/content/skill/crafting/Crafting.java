package org.endeavor.game.content.skill.crafting;

import org.endeavor.engine.task.TaskQueue;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendEnterXInterface;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;

public class Crafting {
	private static int[][] LEATHER_ARMOR_IDS = { { 33187, 1, 1129 }, { 33186, 5, 1129 }, { 33185, 10, 1129 },
			{ 33190, 1, 1059 }, { 33189, 5, 1059 }, { 33188, 10, 1059 } };

	public static boolean handleCraftingByButtons(Player player, int buttonId) {
		switch (buttonId) {
		case 6211:
			if (player.getAttributes().get("craftingType") != null) {
				switch (((CraftingType) player.getAttributes().get("craftingType"))) {
				case GEM_CRAFTING:
					if (player.getAttributes().get("craftingGem") != null) {
						TaskQueue.queue(new GemCutting(player, (short) player.getInventory().getItemAmount(
								((Gem) player.getAttributes().get("craftingGem")).getUncutGem()), (Gem) player
								.getAttributes().get("craftingGem")));
						player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
						player.getAttributes().remove("craftingGem");
						player.getAttributes().remove("craftingType");
					}
					return true;
				case WHEEL_SPINNING:
					if (player.getAttributes().get("spinnable") != null) {
						TaskQueue.queue(new WheelSpinning(player, (short) 28, (Spinnable) player.getAttributes().get(
								"spinnable")));
						player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
						player.getAttributes().remove("spinnable");
						player.getAttributes().remove("craftingType");
					}
					break;
				default:
					return true;
				}

				player.getAttributes().remove("craftingType");
			}
			break;
		case 10238:
			if (player.getAttributes().get("craftingType") != null) {
				switch (((CraftingType) player.getAttributes().get("craftingType"))) {
				case GEM_CRAFTING:
					TaskQueue.queue(new GemCutting(player, (short) 5, (Gem) player.getAttributes().get("craftingGem")));
					player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
					player.getAttributes().remove("craftingGem");
					return true;
				case WHEEL_SPINNING:
					TaskQueue.queue(new WheelSpinning(player, (short) 5, (Spinnable) player.getAttributes().get(
							"spinnable")));
					player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
					player.getAttributes().remove("spinnable");
					break;
				default:
					return true;
				}

				player.getAttributes().remove("craftingType");
			}
			break;
		case 10239:
			if (player.getAttributes().get("craftingType") != null) {
				switch (((CraftingType) player.getAttributes().get("craftingType"))) {
				case GEM_CRAFTING:
					TaskQueue.queue(new GemCutting(player, (short) 1, (Gem) player.getAttributes().get("craftingGem")));
					player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
					player.getAttributes().remove("craftingGem");
					player.getAttributes().remove("craftingType");
					return true;
				case WHEEL_SPINNING:
					TaskQueue.queue(new WheelSpinning(player, (short) 1, (Spinnable) player.getAttributes().get(
							"spinnable")));
					player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
					player.getAttributes().remove("spinnable");
					player.getAttributes().remove("craftingType");
					return true;
				default:
					player.getAttributes().remove("craftingType");
					return true;
				}
			} else {
				return false;
			}
		case 6212:
			player.getClient().queueOutgoingPacket(new SendEnterXInterface(8886, 0));
			break;
		case 34183:
			if (player.getAttributes().get("craftingType") != null) {
				switch (((CraftingType) player.getAttributes().get("craftingType"))) {
				case ARMOUR_CREATION:
					if (player.getAttributes().get("craftingHide") == null) {
						return false;
					}
					player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
					TaskQueue.queue(new ArmourCreation(player, (short) 10, Craftable.forReward(((Hide) player
							.getAttributes().get("craftingHide")).getCraftableOutcomes()[0])));
					player.getAttributes().remove("craftingHide");
					player.getAttributes().remove("craftingType");
					return true;
				}
				return true;
			}

			break;
		case 34185:
			if (player.getAttributes().get("craftingType") != null) {
				switch (((CraftingType) player.getAttributes().get("craftingType"))) {
				case ARMOUR_CREATION:
					if (player.getAttributes().get("craftingHide") == null) {
						return false;
					}
					player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
					TaskQueue.queue(new ArmourCreation(player, (short) 1, Craftable.forReward(((Hide) player
							.getAttributes().get("craftingHide")).getCraftableOutcomes()[0])));
					player.getAttributes().remove("craftingHide");
					player.getAttributes().remove("craftingType");
					return true;
				}
				return true;
			}

			break;
		case 34184:
			if (player.getAttributes().get("craftingType") != null) {
				switch (((CraftingType) player.getAttributes().get("craftingType"))) {
				case ARMOUR_CREATION:
					if (player.getAttributes().get("craftingHide") == null) {
						return false;
					}
					player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
					TaskQueue.queue(new ArmourCreation(player, (short) 5, Craftable.forReward(((Hide) player
							.getAttributes().get("craftingHide")).getCraftableOutcomes()[0])));
					player.getAttributes().remove("craftingHide");
					player.getAttributes().remove("craftingType");
					return true;
				}
				return true;
			}

			break;
		case 34186:
			player.getClient().queueOutgoingPacket(new SendEnterXInterface(8890, 0));
			break;
		case 34187:
			if (player.getAttributes().get("craftingType") != null) {
				switch (((CraftingType) player.getAttributes().get("craftingType"))) {
				case ARMOUR_CREATION:
					if (player.getAttributes().get("craftingHide") == null) {
						return false;
					}
					player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
					TaskQueue.queue(new ArmourCreation(player, (short) 10, Craftable.forReward(((Hide) player
							.getAttributes().get("craftingHide")).getCraftableOutcomes()[1])));
					player.getAttributes().remove("craftingHide");
					player.getAttributes().remove("craftingType");
					return true;
				}
				return true;
			}

			break;
		case 34188:
			if (player.getAttributes().get("craftingType") != null) {
				switch (((CraftingType) player.getAttributes().get("craftingType"))) {
				case ARMOUR_CREATION:
					if (player.getAttributes().get("craftingHide") == null) {
						return false;
					}
					player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
					TaskQueue.queue(new ArmourCreation(player, (short) 5, Craftable.forReward(((Hide) player
							.getAttributes().get("craftingHide")).getCraftableOutcomes()[1])));
					player.getAttributes().remove("craftingHide");
					player.getAttributes().remove("craftingType");
					return true;
				}
				return true;
			}

			break;
		case 34189:
			if (player.getAttributes().get("craftingType") != null) {
				switch (((CraftingType) player.getAttributes().get("craftingType"))) {
				case ARMOUR_CREATION:
					if (player.getAttributes().get("craftingHide") == null) {
						return false;
					}
					player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
					TaskQueue.queue(new ArmourCreation(player, (short) 1, Craftable.forReward(((Hide) player
							.getAttributes().get("craftingHide")).getCraftableOutcomes()[1])));
					player.getAttributes().remove("craftingHide");
					player.getAttributes().remove("craftingType");
					return true;
				}
				return true;
			}

			break;
		case 34190:
			player.getClient().queueOutgoingPacket(new SendEnterXInterface(8894, 0));
			return true;
		case 34191:
			if (player.getAttributes().get("craftingType") != null) {
				switch (((CraftingType) player.getAttributes().get("craftingType"))) {
				case ARMOUR_CREATION:
					if (player.getAttributes().get("craftingHide") == null) {
						return false;
					}
					player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
					TaskQueue.queue(new ArmourCreation(player, (short) 10, Craftable.forReward(((Hide) player
							.getAttributes().get("craftingHide")).getCraftableOutcomes()[2])));
					player.getAttributes().remove("craftingHide");
					player.getAttributes().remove("craftingType");
					return true;
				}
				return true;
			}

			break;
		case 34192:
			if (player.getAttributes().get("craftingType") != null) {
				switch (((CraftingType) player.getAttributes().get("craftingType"))) {
				case ARMOUR_CREATION:
					if (player.getAttributes().get("craftingHide") == null) {
						return false;
					}
					player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
					TaskQueue.queue(new ArmourCreation(player, (short) 5, Craftable.forReward(((Hide) player
							.getAttributes().get("craftingHide")).getCraftableOutcomes()[2])));
					player.getAttributes().remove("craftingHide");
					player.getAttributes().remove("craftingType");
					return true;
				}
				return true;
			}

			break;
		case 34193:
			if (player.getAttributes().get("craftingType") != null) {
				switch (((CraftingType) player.getAttributes().get("craftingType"))) {
				case ARMOUR_CREATION:
					if (player.getAttributes().get("craftingHide") == null) {
						return false;
					}
					player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
					TaskQueue.queue(new ArmourCreation(player, (short) 1, Craftable.forReward(((Hide) player
							.getAttributes().get("craftingHide")).getCraftableOutcomes()[2])));
					player.getAttributes().remove("craftingHide");
					player.getAttributes().remove("craftingType");
					return true;
				}
				return true;
			}
			break;
		}

		for (int[] i : LEATHER_ARMOR_IDS) {
			if (i[0] == buttonId) {
				player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
				TaskQueue.queue(new ArmourCreation(player, (short) i[1], Craftable.forReward(i[2])));
				player.getAttributes().remove("craftingHide");
				player.getAttributes().remove("craftingType");
				return true;
			}
		}
		return false;
	}
}
