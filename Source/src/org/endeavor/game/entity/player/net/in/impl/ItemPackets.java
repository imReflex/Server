package org.endeavor.game.entity.player.net.in.impl;

import org.endeavor.GameSettings;
import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.engine.network.StreamBuffer.ByteOrder;
import org.endeavor.engine.network.StreamBuffer.ValueType;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.task.impl.DigTask;
import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.game.content.BarrowsSet;
import org.endeavor.game.content.ItemCreation;
import org.endeavor.game.content.ItemInteraction;
import org.endeavor.game.content.consumables.ConsumableType;
import org.endeavor.game.content.dwarfcannon.DwarfMultiCannon;
import org.endeavor.game.content.pets.Pets;
import org.endeavor.game.content.randoms.RandomEvent;
import org.endeavor.game.content.randoms.RandomExecutor;
import org.endeavor.game.content.skill.crafting.CraftingType;
import org.endeavor.game.content.skill.crafting.Gem;
import org.endeavor.game.content.skill.crafting.GemCutting;
import org.endeavor.game.content.skill.crafting.Hide;
import org.endeavor.game.content.skill.crafting.JewelryCreationTask;
import org.endeavor.game.content.skill.firemaking.Firemaking;
import org.endeavor.game.content.skill.fletching.Fletching;
import org.endeavor.game.content.skill.herblore.CleanHerbTask;
import org.endeavor.game.content.skill.herblore.HerbloreFinishedPotionTask;
import org.endeavor.game.content.skill.herblore.HerbloreGrindingTask;
import org.endeavor.game.content.skill.herblore.HerbloreUnfinishedPotionTask;
import org.endeavor.game.content.skill.herblore.PotionDecanting;
import org.endeavor.game.content.skill.hunter.Hunter;
import org.endeavor.game.content.skill.prayer.BoneBurying;
import org.endeavor.game.content.skill.smithing.SmithingTask;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.in.IncomingPacket;
import org.endeavor.game.entity.player.net.out.impl.SendChatInterface;
import org.endeavor.game.entity.player.net.out.impl.SendInterface;
import org.endeavor.game.entity.player.net.out.impl.SendItemOnInterface;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendSound;
import org.endeavor.game.entity.player.net.out.impl.SendString;

public class ItemPackets extends IncomingPacket {
	public static final int ITEM_OPERATE = 75;
	public static final int DROP_ITEM = 87;
	public static final int PICKUP_ITEM = 236;
	public static final int HANDLE_OPTIONS = 214;
	public static final int PACKET_145 = 145;
	public static final int PACKET_117 = 117;
	public static final int PACKET_43 = 43;
	public static final int PACKET_129 = 129;
	public static final int EQUIP_ITEM = 41;
	public static final int USE_ITEM_ON_ITEM = 53;
	public static final int FIRST_CLICK_ITEM = 122;
	public static final int SECOND_CLICK_ITEM = 16;

	@Override
	public int getMaxDuplicates() {
		return 40;
	}

	@Override
	public void handle(Player player, StreamBuffer.InBuffer in, int opcode, int length) {
		if ((player.isDead()) || (!player.getController().canClick()))
			return;
		int x;
		int magicId;
		int z;
		switch (opcode) {
		case 145:
			int interfaceId = in.readShort(StreamBuffer.ValueType.A);
			int slot = in.readShort(StreamBuffer.ValueType.A);
			int itemId = in.readShort(StreamBuffer.ValueType.A);

			if (GameSettings.DEV_MODE) {
				player.send(new SendMessage("Item packet 145"));
			}
			
			if ((interfaceId != 1688) && (!player.getInterfaceManager().verify(interfaceId))) {
				return;
			}
			switch (interfaceId) {
			case 2700:
				if (player.getSummoning().isFamilarBOB()) {
					player.getSummoning().getContainer().withdraw(slot, 1);
				}
				break;
			case 1119:
			case 1120:
			case 1121:
			case 1122:
			case 1123:
				SmithingTask.start(player, itemId, 1, interfaceId, slot);
				break;
			case 1688:
				if (!player.getEquipment().slotHasItem(slot)) {
					return;
				}

				player.getEquipment().unequip(slot);
				break;
			case 4233:
			case 4239:
			case 4245:
				JewelryCreationTask.start(player, itemId, 1);
				break;
			case 5064:
				if (!player.getInventory().slotContainsItem(slot, itemId)) {
					return;
				}

				if (player.getInterfaceManager().hasBankOpen())
					bankItem(player, slot, itemId, 1);
				else if (player.getSummoning().isFamilarBOB()) {
					player.getSummoning().getContainer().store(itemId, 1, slot);
				}

				break;
			case 5382:
				withdrawBankItem(player, slot, itemId, 1);
				break;
			case 3322:
				if (player.getTrade().trading())
					handleTradeOffer(player, slot, itemId, 1);
				else if (player.getDueling().isStaking()) {
					player.getDueling().getContainer().offer(itemId, 1, slot);
				}
				break;
			case 3415:
				if (player.getTrade().trading()) {
					handleTradeRemove(player, slot, itemId, 1);
				}
				break;
			case 6669:
				if (player.getDueling().isStaking()) {
					player.getDueling().getContainer().withdraw(slot, 1);
				}
				break;
			case 3900:
				player.getShopping().sendSellPrice(itemId);
				break;
			case 3823:
				player.getShopping().sendBuyPrice(itemId);
			}

			break;
		case 117:
			interfaceId = in.readShort(true, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
			itemId = in.readShort(true, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
			slot = in.readShort(true, StreamBuffer.ByteOrder.LITTLE);

			if (GameSettings.DEV_MODE) {
				player.send(new SendMessage("Item packet 117"));
			}
			
			if ((interfaceId != 1688) && (!player.getInterfaceManager().verify(interfaceId)))
				return;
			switch (interfaceId) {
			case 2700:
				if (player.getSummoning().isFamilarBOB()) {
					player.getSummoning().getContainer().withdraw(slot, 5);
				}
				break;
			case 1688:
				if (itemId == 11283) {
					player.getMagic().onOperateDragonFireShield();
					return;
				}

				if (itemId == 10499) {
					player.getRanged().getFromAvasAccumulator();
					return;
				}
				break;
			case 1119:
			case 1120:
			case 1121:
			case 1122:
			case 1123:
				SmithingTask.start(player, itemId, 5, interfaceId, slot);
				break;
			case 4233:
			case 4239:
			case 4245:
				JewelryCreationTask.start(player, itemId, 5);
				break;
			case 5064:
				if (!player.getInventory().slotContainsItem(slot, itemId)) {
					return;
				}

				if (player.getInterfaceManager().hasBankOpen())
					bankItem(player, slot, itemId, 5);
				else if (player.getSummoning().isFamilarBOB()) {
					player.getSummoning().getContainer().store(itemId, 5, slot);
				}

				break;
			case 5382:
				withdrawBankItem(player, slot, itemId, 5);
				break;
			case 3322:
				if (player.getTrade().trading())
					handleTradeOffer(player, slot, itemId, 5);
				else if (player.getDueling().isStaking()) {
					player.getDueling().getContainer().offer(itemId, 5, slot);
				}
				break;
			case 6669:
				if (player.getDueling().isStaking()) {
					player.getDueling().getContainer().withdraw(slot, 5);
				}
				break;
			case 3415:
				if (player.getTrade().trading()) {
					handleTradeRemove(player, slot, itemId, 5);
				}
				break;
			case 3900:
				player.getShopping().buy(itemId, 1, slot);
				break;
			case 3823:
				player.getShopping().sell(itemId, 1, slot);
			}

			break;
		case 43:
			
			if (GameSettings.DEV_MODE) {
				player.send(new SendMessage("Item packet 43"));
			}
			interfaceId = in.readShort(StreamBuffer.ByteOrder.LITTLE);
			itemId = in.readShort(StreamBuffer.ValueType.A);
			slot = in.readShort(StreamBuffer.ValueType.A);

			if (!player.getInterfaceManager().verify(interfaceId)) {
				return;
			}
			switch (interfaceId) {
			case 2700:
				if (player.getSummoning().isFamilarBOB()) {
					player.getSummoning().getContainer().withdraw(slot, 10);
				}
				break;
			case 1119:
			case 1120:
			case 1121:
			case 1122:
			case 1123:
				SmithingTask.start(player, itemId, 10, interfaceId, slot);
				break;
			case 4233:
			case 4239:
			case 4245:
				JewelryCreationTask.start(player, itemId, 10);
				break;
			case 5064:
				if (!player.getInventory().slotContainsItem(slot, itemId)) {
					return;
				}

				if (player.getInterfaceManager().hasBankOpen())
					bankItem(player, slot, itemId, 10);
				else if (player.getSummoning().isFamilarBOB()) {
					player.getSummoning().getContainer().store(itemId, 10, slot);
				}

				break;
			case 5382:
				withdrawBankItem(player, slot, itemId, 10);
				break;
			case 3322:
				if (player.getTrade().trading())
					handleTradeOffer(player, slot, itemId, 10);
				else if (player.getDueling().isStaking()) {
					player.getDueling().getContainer().offer(itemId, 10, slot);
				}
				break;
			case 6669:
				if (player.getDueling().isStaking()) {
					player.getDueling().getContainer().withdraw(slot, 10);
				}
				break;
			case 3415:
				if (player.getTrade().trading()) {
					handleTradeRemove(player, slot, itemId, 10);
				}
				break;
			case 3900:
				player.getShopping().buy(itemId, 5, slot);
				break;
			case 3823:
				player.getShopping().sell(itemId, 5, slot);
			}

			break;
		case 129:
			slot = in.readShort(StreamBuffer.ValueType.A);
			interfaceId = in.readShort();
			itemId = in.readShort(StreamBuffer.ValueType.A);

			if (GameSettings.DEV_MODE) {
				player.send(new SendMessage("Item packet 129"));
			}
			
			if (!player.getInterfaceManager().verify(interfaceId)) {
				return;
			}
			switch (interfaceId) {
			case 2700:
				if (player.getSummoning().isFamilarBOB()) {
					player.getSummoning().getContainer().withdraw(slot, 2147483647);
				}
				break;
			case 5064:
				if (!player.getInventory().slotContainsItem(slot, itemId)) {
					return;
				}

				if (player.getInterfaceManager().hasBankOpen())
					bankItem(player, slot, itemId, 2147483647);
				else if (player.getSummoning().isFamilarBOB()) {
					player.getSummoning().getContainer().store(itemId, 2147483647, slot);
				}
				break;
			case 5382:
				withdrawBankItem(player, slot, itemId, 2147483647);
				break;
			case 3322:
				if (player.getTrade().trading())
					handleTradeOffer(player, slot, itemId, 2147483647);
				else if (player.getDueling().isStaking()) {
					player.getDueling().getContainer().offer(itemId, 2147483647, slot);
				}
				break;
			case 6669:
				if (player.getDueling().isStaking()) {
					player.getDueling().getContainer().withdraw(slot, 2147483647);
				}
				break;
			case 3415:
				if (player.getTrade().trading()) {
					handleTradeRemove(player, slot, itemId, 2147483647);
				}
				break;
			case 3900:
				player.getShopping().buy(itemId, 10, slot);
				break;
			case 3823:
				player.getShopping().sell(itemId, 10, slot);
			}

			break;
		case 41:
			itemId = in.readShort();
			slot = in.readShort(StreamBuffer.ValueType.A);
			in.readShort();
			
			if (GameSettings.DEV_MODE) {
				player.send(new SendMessage("Item packet 41"));
			}

			if (!player.getInventory().slotContainsItem(slot, itemId)) {
				return;
			}

			if (itemId == 4079) {
				player.getUpdateFlags().sendAnimation(1458, 0);
				return;
			}

			if (ItemInteraction.clickPouch(player, itemId, 2)) {
				return;
			}

			player.getEquipment().equip(player.getInventory().get(slot), slot);
			break;
		case 214:
			interfaceId = in.readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
			in.readByte(StreamBuffer.ValueType.C);
			int fromSlot = in.readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
			int toSlot = in.readShort(StreamBuffer.ByteOrder.LITTLE);

			if (GameSettings.DEV_MODE) {
				player.send(new SendMessage("Item packet 214"));
			}
			
			switch (interfaceId) {
			case 5:
				player.getBank().setTab(0);
				break;
			case 13:
				player.getBank().setTab(1);
				break;
			case 26:
				player.getBank().setTab(2);
				break;
			case 39:
				player.getBank().setTab(3);
				break;
			case 52:
				player.getBank().setTab(4);
				break;
			case 65:
				player.getBank().setTab(5);
				break;
			case 78:
				player.getBank().setTab(6);
				break;
			case 91:
				player.getBank().setTab(7);
				break;
			case 104:
				player.getBank().setTab(8);
				break;
			case 5382:
				player.getBank().swap(toSlot, fromSlot);
				break;
			case 3214:
			case 5064:
				player.getInventory().swap(toSlot, fromSlot);
				break;
			}

			break;
		case 87:
			
			if (GameSettings.DEV_MODE) {
				player.send(new SendMessage("Item packet 87"));
			}
			itemId = in.readShort(StreamBuffer.ValueType.A);
			in.readShort();
			slot = in.readShort(StreamBuffer.ValueType.A);

			if (!player.getInventory().slotContainsItem(slot, itemId)) {
				return;
			}

			if (player.getMinigames().getBetManager().betting()) {
				player.getClient().queueOutgoingPacket(new SendMessage("You cannot drop items while dicing another player."));
				return;
			}
			if (Pets.isItemPet(itemId)) {
				player.getPets().init(itemId);
				return;
			}

			player.getClient().queueOutgoingPacket(new SendSound(376, 1, 0));
			player.send(new SendMessage("The item magically disappears as it hits the ground!"));
			player.getInventory().clear(slot);
			//player.getGroundItems().drop(itemId, slot);
			break;
		case 236:
			int y = in.readShort(StreamBuffer.ByteOrder.LITTLE);
			itemId = in.readShort();
			x = in.readShort(StreamBuffer.ByteOrder.LITTLE);

			player.getCombat().reset();

			player.getGroundItems().pickup(x, y, itemId);
			break;
		case 53:
			int firstSlot = in.readShort();
			int secondSlot = in.readShort(StreamBuffer.ValueType.A);
			int itemUsedId = in.readShort(true, ValueType.A, ByteOrder.BIG);
			int lastItemSelectedInterface = in.readShort();
			int selectedItemId = in.readShort(ByteOrder.BIG);
			int interfaceId2 = in.readShort();

			RandomExecutor.tryRandom(player);

			if ((!player.getInventory().slotHasItem(firstSlot)) || (!player.getInventory().slotHasItem(secondSlot))) {
				return;
			}

			Item usedWith = player.getInventory().get(firstSlot);
			Item itemUsed = player.getInventory().get(secondSlot);

			if ((usedWith == null) || (itemUsed == null)) {
				return;
			}

			if (ItemCreation.createSomething(player, usedWith.getId(), itemUsed.getId())) {
				return;
			}

			if (ItemCreation.useDieOnWhip(player, usedWith.getId(), itemUsed.getId())) {
				return;
			}

			if (ItemCreation.immbueRing(player, usedWith.getId(), itemUsed.getId())) {
				return;
			}

			if (ItemCreation.createBolts(player, usedWith.getId(), itemUsed.getId())) {
				return;
			}

			if (ItemCreation.createCrystalKey(player, usedWith.getId(), itemUsed.getId())) {
				return;
			}

			if (ItemCreation.createDragonPlatebody(player, usedWith.getId(), itemUsed.getId())) {
				return;
			}

			if (ItemCreation.useStringOnAmulet(player, usedWith.getId(), itemUsed.getId())) {
				return;
			}

			if (ItemCreation.createBlade(player, usedWith.getId(), itemUsed.getId())) {
				return;
			}

			if (ItemCreation.createGodsword(player, usedWith.getId(), itemUsed.getId())) {
				return;
			}

			if (ItemCreation.createDragonfireShield(player, usedWith.getId(), itemUsed.getId())) {
				return;
			}

			if (ItemCreation.createSpiritShield(player, usedWith.getId(), itemUsed.getId())) {
				return;
			}

			if (Fletching.useItemOnItem(player, usedWith.getId(), itemUsed.getId())) {
				return;
			}

			if ((usedWith.getId() == 227) || (itemUsed.getId() == 227)) {
				HerbloreUnfinishedPotionTask.displayInterface(player, itemUsed, usedWith);
			} else if (!HerbloreFinishedPotionTask.displayInterface(player, itemUsed, usedWith)) {
				if ((usedWith.getId() == 233) || (itemUsed.getId() == 233)) {
					HerbloreGrindingTask.handleGrindingIngredients(player, itemUsed, usedWith);
				} else if (!Firemaking.attemptFiremaking(player, itemUsed, usedWith)) {
					if ((usedWith.getId() == 1785) || (itemUsed.getId() == 1785)) {
						if ((usedWith.getId() == 1785) && (itemUsed.getId() == 1775))
							player.getClient().queueOutgoingPacket(new SendInterface(11462));
						else if ((itemUsed.getId() == 1785) && (usedWith.getId() == 1775)) {
							player.getClient().queueOutgoingPacket(new SendInterface(11462));
						}

					}

					if ((usedWith.getId() == 1733) || (itemUsed.getId() == 1733)) {
						Hide hide;
						if (usedWith.getId() == 1733)
							hide = Hide.forReward((short) itemUsed.getId());
						else {
							hide = Hide.forReward((short) usedWith.getId());
						}
						if (hide != null) {
							player.getAttributes().set("craftingType", CraftingType.ARMOUR_CREATION);
							player.getAttributes().set("craftingHide", hide);
							if (hide.getItemId() == 1739) {
								player.getClient().queueOutgoingPacket(new SendInterface(2311));
							} else {
								String prefix = GameDefinitionLoader.getItemDef(hide.getOutcome()).getName().split(" ")[0];
								player.getClient().queueOutgoingPacket(new SendChatInterface(8880));
								player.getClient().queueOutgoingPacket(
										new SendItemOnInterface(8883, 250, hide.getCraftableOutcomes()[0]));
								player.getClient().queueOutgoingPacket(
										new SendItemOnInterface(8884, 250, hide.getCraftableOutcomes()[1]));
								player.getClient().queueOutgoingPacket(
										new SendItemOnInterface(8885, 250, hide.getCraftableOutcomes()[2]));

								player.getClient().queueOutgoingPacket(
										new SendString("\\n \\n \\n \\n".concat(prefix + " body"), 8889));
								player.getClient().queueOutgoingPacket(
										new SendString("\\n \\n \\n \\n".concat(prefix + " vambraces"), 8893));
								player.getClient().queueOutgoingPacket(
										new SendString("\\n \\n \\n \\n".concat(prefix + " chaps"), 8897));
							}
						}
					}

					if ((usedWith.getId() == 1755) || (itemUsed.getId() == 1755)) {
						Gem gem;
						if (usedWith.getId() == 1755)
							gem = Gem.forId(itemUsed.getId());
						else {
							gem = Gem.forId(usedWith.getId());
						}
						if (gem != null) {
							if (player.getInventory().getItemAmount(gem.getUncutGem()) > 1) {
								player.getClient().queueOutgoingPacket(
										new SendString("\\n \\n \\n \\n"
												+ GameDefinitionLoader.getItemDef(gem.getCutGem()).getName(), 2799));
								player.getClient().queueOutgoingPacket(
										new SendItemOnInterface(1746, 170, gem.getCutGem()));
								player.getClient().queueOutgoingPacket(new SendChatInterface(4429));
								player.getAttributes().set("craftingType", CraftingType.GEM_CRAFTING);
								player.getAttributes().set("craftingGem", gem);
							} else {
								TaskQueue.queue(new GemCutting(player, (short) 1, gem));
							}
						}
					}

					if (PotionDecanting.decant(player, firstSlot, secondSlot))
						;
				}
			}

			break;
		case 25:
			in.readShort();
			
			int itemInInven = in.readShort(StreamBuffer.ValueType.A);

			int groundItem = in.readShort();
			y = in.readShort(StreamBuffer.ValueType.A);
			z = player.getLocation().getZ();
			in.readShort();
			x = in.readShort();
			break;
		case 237:
			slot = in.readShort();
			itemId = in.readShort(StreamBuffer.ValueType.A);
			interfaceId = in.readShort();
			magicId = in.readShort(StreamBuffer.ValueType.A);

			RandomExecutor.tryRandom(player);

			if (!player.getInterfaceManager().verify(interfaceId)) {
				return;
			}

			if (!player.getInventory().slotContainsItem(slot, itemId)) {
				return;
			}

			player.getAttributes().set("magicitem", Integer.valueOf(itemId));
			player.getMagic().useMagicOnItem(magicId);
			break;
		case 181:
			y = in.readShort(StreamBuffer.ByteOrder.LITTLE);
			itemId = in.readShort();
			x = in.readShort(StreamBuffer.ByteOrder.LITTLE);
			magicId = in.readShort(StreamBuffer.ValueType.A);
			break;
		case 253://second click ground item
			x = in.readShort(StreamBuffer.ByteOrder.LITTLE);
			y = in.readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
			itemId = in.readShort(StreamBuffer.ValueType.A);
			z = player.getLocation().getZ();
			
			player.getCombat().reset();

			player.getGroundItems().handleSecondAction(x, y, itemId);
			break;
		case 122:
			if (GameSettings.DEV_MODE) {
				player.send(new SendMessage("Item packet 122"));
			}
			
			interfaceId = in.readShort(/*StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE*/);
			slot = in.readShort(/*StreamBuffer.ValueType.A*/);
			itemId = in.readShort(/*StreamBuffer.ByteOrder.LITTLE*/);

			if (!player.getInventory().slotContainsItem(slot, itemId)) {
				System.out.println("Incorrect slot values for " + itemId + " slot: " + slot);
				return;
			}
			
			if(!player.getInventory().hasItemId(itemId)) {
				System.out.println("Player does not have item; tried to click!");
				return;
			}

			if(Hunter.operateItem(player, itemId, new Location(player.getLocation()))) {
				return;
			}
			
			if (ItemInteraction.clickPouch(player, itemId, 1)) {
				return;
			}

			if(BarrowsSet.openBarrowsSet(player, itemId)){
				return;
			}
			
			if (itemId == 4079) {
				player.getUpdateFlags().sendAnimation(1457, 0);
				return;
			}

			if (ItemInteraction.openCasket(player, itemId)) {
				return;
			}

			if (ItemInteraction.openVoidSet(player, itemId)) {
				return;
			}

			if (ItemInteraction.useItem(player, itemId)) {
				System.out.println("Item Interaction: " + itemId);
				return;
			}

			if (itemId == 6183) {
				RandomEvent.openGift(player);
				return;
			}

			if (DwarfMultiCannon.setCannonBase(player, itemId)) {
				return;
			}

			if (BoneBurying.bury(player, itemId, slot))
				return;
			if ((player.getConsumables().consume(itemId, slot, ConsumableType.FOOD))
					|| (player.getConsumables().consume(itemId, slot, ConsumableType.POTION)))
				return;
			if (player.getMagic().clickMagicItems(itemId))
				return;
			switch (itemId) {
			case 952:
				TaskQueue.queue(new DigTask(player));
				return;
			}

			CleanHerbTask.attemptHerbCleaning(player, slot);
			RandomExecutor.tryRandom(player);
			break;
		case 16:
			if (GameSettings.DEV_MODE) {
				player.send(new SendMessage("Item packet 16"));
			}
			
			itemId = in.readShort(StreamBuffer.ValueType.A);
			slot = in.readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
			interfaceId = in.readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);

			if (!player.getInventory().slotContainsItem(slot, itemId)) {
				return;
			}

			if (ItemInteraction.clickPouch(player, itemId, 3)) {
				return;
			}

			if (itemId == 4079) {
				player.getUpdateFlags().sendAnimation(1459, 0);
				return;
			}

			if (itemId == 11283) {
				player.getClient().queueOutgoingPacket(
						new SendMessage("Your shield has " + player.getMagic().getDragonFireShieldCharges()
								+ " charges."));
				return;
			}
			

			if (ItemCreation.disassemble(player, itemId))
				;
			break;
		case 75:
			if (GameSettings.DEV_MODE) {
				player.send(new SendMessage("Item packet 75"));
			}
			
			interfaceId = in.readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
			slot = in.readShort(StreamBuffer.ByteOrder.LITTLE);
			itemId = in.readShort(true, StreamBuffer.ValueType.A);

			if (!player.getInventory().slotContainsItem(slot, itemId)) {
				return;
			}

			if (itemId == 4079) {
				player.getUpdateFlags().sendAnimation(1460, 0);
				return;
			}

			if (player.getSummoning().summon(itemId))
				;
			break;
		}
	}

	public void handleTradeOffer(Player player, int slot, int itemId, int amount) {
		player.getTrade().getContainer().offer(itemId, amount, slot);
	}

	public void handleTradeRemove(Player player, int slot, int itemId, int amount) {
		player.getTrade().getContainer().withdraw(slot, amount);
	}

	public void bankItem(Player player, int slot, int itemId, int amount) {
		player.getBank().deposit(itemId, amount, slot);
	}

	public void withdrawBankItem(Player player, int slot, int itemId, int amount) {
		player.getBank().withdraw(slot, amount);
	}
}
