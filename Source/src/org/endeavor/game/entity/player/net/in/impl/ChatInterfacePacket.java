package org.endeavor.game.entity.player.net.in.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.game.content.skill.cooking.CookingTask;
import org.endeavor.game.content.skill.crafting.ArmourCreation;
import org.endeavor.game.content.skill.crafting.Craftable;
import org.endeavor.game.content.skill.crafting.CraftingType;
import org.endeavor.game.content.skill.crafting.Gem;
import org.endeavor.game.content.skill.crafting.GemCutting;
import org.endeavor.game.content.skill.herblore.HerbloreFinishedPotionTask;
import org.endeavor.game.content.skill.herblore.HerbloreUnfinishedPotionTask;
import org.endeavor.game.content.skill.prayer.BoneBurying;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.in.IncomingPacket;
import org.endeavor.game.entity.player.net.out.impl.SendEnterXInterface;
import org.endeavor.game.entity.player.net.out.impl.SendMoveComponent;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;

public class ChatInterfacePacket extends IncomingPacket {
	@Override
	public void handle(Player player, StreamBuffer.InBuffer in, int opcode, int length) {
		switch (opcode) {
		case 40:
			handleDialogue(player);
			break;
		case 135:
			showEnterX(player, in);
			break;
		case 208:
			handleEnterX(player, in);
		}
	}

	@Override
	public int getMaxDuplicates() {
		return 10;
	}

	public void handleDialogue(Player player) {
		if ((player.getDialogue() == null) || (player.getDialogue().getNext() == -1))
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
		else if (player.getDialogue().getNext() > -1)
			player.getDialogue().execute();
	}

	public void showEnterX(Player player, StreamBuffer.InBuffer in) {
		player.setEnterXSlot(in.readShort(StreamBuffer.ByteOrder.LITTLE));
		player.setEnterXInterfaceId(in.readShort(StreamBuffer.ValueType.A));
		player.setEnterXItemId(in.readShort(StreamBuffer.ByteOrder.LITTLE));
		player.getClient().queueOutgoingPacket(new SendEnterXInterface());
	}

	public void handleEnterX(Player player, StreamBuffer.InBuffer in) {
		int amount = in.readInt();

		int slot = player.getEnterXSlot();
		int id = player.getEnterXItemId();

		if (amount < 1) {
			return;
		}

		switch (player.getEnterXInterfaceId()) {
		case 2700:
			if (player.getSummoning().isFamilarBOB()) {
				player.getSummoning().getContainer().withdraw(slot, amount);
			}
			break;
		case 55678:
			BoneBurying.finishOnAltar(player, amount);
			break;
		case 3823:
			player.getShopping().sell(id, amount, slot);
			break;
		case 3900:
			player.getShopping().buy(id, amount, slot);
			break;
		/*case 15460:
			player.getPlayerShop().onSetPrice(player, amount);
			break;*/
		case 1748:
			if (player.getAttributes().get("craftingType") != null) {
				switch (((CraftingType) player.getAttributes().get("craftingType"))) {
				case GEM_CRAFTING:
					TaskQueue.queue(new GemCutting(player, (short) amount, (Gem) player.getAttributes().get(
							"craftingGem")));
					player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
					player.getAttributes().remove("craftingType");
					player.getAttributes().remove("craftingGem");
					player.getClient().queueOutgoingPacket(new SendMoveComponent(0, 0, 1746));
				}

			}

			break;
		case 8886:
			if (player.getAttributes().get("craftingType") != null) {
				switch (((CraftingType) player.getAttributes().get("craftingType"))) {
				case ARMOUR_CREATION:
					if (player.getAttributes().get("craftingHide") == null) {
						return;
					}
					player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
					TaskQueue.queue(new ArmourCreation(player, (short) amount, Craftable
							.forReward(((org.endeavor.game.content.skill.crafting.Hide) player.getAttributes().get(
									"craftingHide")).getCraftableOutcomes()[0])));
					player.getAttributes().remove("craftingHide");
					player.getAttributes().remove("craftingType");
				}

			}

			break;
		case 8890:
			if (player.getAttributes().get("craftingType") != null) {
				switch (((org.endeavor.game.content.skill.crafting.CraftingType) player.getAttributes().get(
						"craftingType"))) {
				case ARMOUR_CREATION:
					if (player.getAttributes().get("craftingHide") == null) {
						return;
					}
					player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
					TaskQueue.queue(new ArmourCreation(player, (short) amount, Craftable
							.forReward(((org.endeavor.game.content.skill.crafting.Hide) player.getAttributes().get(
									"craftingHide")).getCraftableOutcomes()[1])));
					player.getAttributes().remove("craftingHide");
					player.getAttributes().remove("craftingType");
				}

			}

			break;
		case 8894:
			if (player.getAttributes().get("craftingType") != null) {
				switch (((org.endeavor.game.content.skill.crafting.CraftingType) player.getAttributes().get(
						"craftingType"))) {
				case ARMOUR_CREATION:
					if (player.getAttributes().get("craftingHide") == null) {
						return;
					}
					player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
					TaskQueue.queue(new ArmourCreation(player, (short) amount, Craftable
							.forReward(((org.endeavor.game.content.skill.crafting.Hide) player.getAttributes().get(
									"craftingHide")).getCraftableOutcomes()[2])));
					player.getAttributes().remove("craftingHide");
					player.getAttributes().remove("craftingType");
				}

			}

			break;
		case 1743:
			CookingTask.attemptCooking(player, player.getAttributes().getInt("cookingitem"), player.getAttributes()
					.getInt("cookingobject"), amount);
			break;
		case 4429:
			HerbloreUnfinishedPotionTask.attemptToCreateUnfinishedPotion(player, amount, (Item) player.getAttributes()
					.get("herbloreitem1"), (Item) player.getAttributes().get("herbloreitem2"));
			break;
		case 4430:
			HerbloreFinishedPotionTask.attemptPotionMaking(player, amount);
			break;
		case 5064:
			if (!player.getInventory().slotContainsItem(slot, id)) {
				return;
			}

			if (player.getInterfaceManager().hasBankOpen())
				player.getBank().deposit(id, amount, slot);
			else if (player.getSummoning().isFamilarBOB()) {
				player.getSummoning().getContainer().store(id, amount, slot);
			}

			break;
		case 5382:
			if (amount == -1) {
				amount = player.getBank().get(slot).getAmount();
			}

			player.getBank().withdraw(slot, amount);
			break;
		case 6669:
			if (player.getDueling().isStaking()) {
				player.getDueling().getContainer().withdraw(slot, amount);
			}
			break;
		case 3322:
			if (player.getTrade().trading())
				player.getTrade().getContainer().offer(id, amount, slot);
			else if (player.getDueling().isStaking()) {
				player.getDueling().getContainer().offer(id, amount, slot);
			}
			break;
		case 3415:
			if (player.getTrade().trading()) {
				player.getTrade().getContainer().withdraw(slot, amount);
			}
			break;
		case 15500:
			boolean incr = player.getMinigames().getBetManager().increase();
			if (amount > player.getInventory().getItemAmount(995)) {
				amount = player.getInventory().getItemAmount(995);
			}
			player.getMinigames().getBetManager().changeOffer(incr ? amount : -amount);
		}
	}
}
