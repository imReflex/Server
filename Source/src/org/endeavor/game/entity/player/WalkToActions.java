package org.endeavor.game.entity.player;

import org.endeavor.GameSettings;
import org.endeavor.engine.cache.map.Ladder;
import org.endeavor.engine.cache.map.RSObject;
import org.endeavor.engine.cache.map.Region;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.task.impl.FollowToEntityTask;
import org.endeavor.engine.task.impl.HarvestTask;
import org.endeavor.engine.task.impl.PullLeverTask;
import org.endeavor.engine.task.impl.TeleOtherTask;
import org.endeavor.engine.task.impl.WalkToTask;
import org.endeavor.engine.task.impl.forcewalk.HopDitchTask;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.CrystalChest;
import org.endeavor.game.content.Doors;
import org.endeavor.game.content.ItemCreation;
import org.endeavor.game.content.clans.Clan;
import org.endeavor.game.content.clans.Clans;
import org.endeavor.game.content.clans.clanwars.ClanWar;
import org.endeavor.game.content.clans.clanwars.ClanWars;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.content.dialogue.Emotion;
import org.endeavor.game.content.dialogue.OneLineDialogue;
import org.endeavor.game.content.dialogue.impl.ChangeBookDialogue;
import org.endeavor.game.content.dialogue.impl.MandrithDialogue;
import org.endeavor.game.content.dialogue.impl.MeleeInstructor;
import org.endeavor.game.content.dialogue.impl.Pikkupstix;
import org.endeavor.game.content.dialogue.impl.Scavvo;
import org.endeavor.game.content.dialogue.impl.Shantay;
import org.endeavor.game.content.dialogue.impl.SkillMaster;
import org.endeavor.game.content.dialogue.impl.SkillcapeShop;
import org.endeavor.game.content.dialogue.impl.UseBankDialogue;
import org.endeavor.game.content.dialogue.impl.Vannaka;
import org.endeavor.game.content.dialogue.impl.teleport.SpiritTree;
import org.endeavor.game.content.dialogue.impl.teleport.WildernessTeleport;
import org.endeavor.game.content.dialogue.impl.teleport.YanilleMinigamePortal;
import org.endeavor.game.content.dwarfcannon.DwarfMultiCannon;
import org.endeavor.game.content.lottery.LotteryDialogue;
import org.endeavor.game.content.minigames.armsrace.ArmsRaceLobby;
import org.endeavor.game.content.minigames.barrows.Barrows;
import org.endeavor.game.content.minigames.bountyhunter.BountyHunter;
import org.endeavor.game.content.minigames.dungeoneering.DungExiterDialogue;
import org.endeavor.game.content.minigames.dungeoneering.DungLobby;
import org.endeavor.game.content.minigames.dungeoneering.DungTutor;
import org.endeavor.game.content.minigames.fightcave.SelectGameDialogue;
import org.endeavor.game.content.minigames.fightcave.TzharrGame;
import org.endeavor.game.content.minigames.fightpits.FightPits;
import org.endeavor.game.content.minigames.godwars.GodWars;
import org.endeavor.game.content.minigames.pestcontrol.PestControl;
import org.endeavor.game.content.minigames.warriorsguild.ArmorAnimator;
import org.endeavor.game.content.minigames.warriorsguild.CyclopsRoom;
import org.endeavor.game.content.minigames.warriorsguild.WarriorsGuild;
import org.endeavor.game.content.pets.PetDialogue;
import org.endeavor.game.content.pets.Pets;
import org.endeavor.game.content.quest.QuestConstants;
import org.endeavor.game.content.quest.impl.hftd.JossikDialogue;
import org.endeavor.game.content.quest.impl.rfd.GypsyDialogue;
import org.endeavor.game.content.quest.impl.runemysteries.MageOfZamorak;
import org.endeavor.game.content.quest.impl.runemysteries.Traiborn;
import org.endeavor.game.content.randoms.RandomDialogue;
import org.endeavor.game.content.shopping.impl.PestShop;
import org.endeavor.game.content.skill.SkillConstants;
import org.endeavor.game.content.skill.agility.AgilityShortcuts;
import org.endeavor.game.content.skill.agility.GnomeAgilityObjects;
import org.endeavor.game.content.skill.cooking.CookingTask;
import org.endeavor.game.content.skill.crafting.CraftingType;
import org.endeavor.game.content.skill.crafting.HideTanning;
import org.endeavor.game.content.skill.crafting.JewelryCreationTask;
import org.endeavor.game.content.skill.crafting.Spinnable;
import org.endeavor.game.content.skill.herblore.PotionDecanting;
import org.endeavor.game.content.skill.hunter.Hunter;
import org.endeavor.game.content.skill.magic.MagicSkill;
import org.endeavor.game.content.skill.magic.MagicSkill.SpellBookTypes;
import org.endeavor.game.content.skill.mining.MiningTask;
import org.endeavor.game.content.skill.prayer.BoneBurying;
import org.endeavor.game.content.skill.prayer.PrayerBook;
import org.endeavor.game.content.skill.prayer.PrayerBook.PrayerBookType;
import org.endeavor.game.content.skill.runecrafting.AbyssObjects;
import org.endeavor.game.content.skill.runecrafting.RunecraftingTask;
import org.endeavor.game.content.skill.smithing.SmithingConstants;
import org.endeavor.game.content.skill.thieving.ThievingNpcTask;
import org.endeavor.game.content.skill.thieving.ThievingStallTask;
import org.endeavor.game.content.skill.woodcutting.WoodcuttingTask;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.object.GameObject;
import org.endeavor.game.entity.object.ObjectConstants;
import org.endeavor.game.entity.object.ObjectManager;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.out.impl.SendChatInterface;
import org.endeavor.game.entity.player.net.out.impl.SendEnterString;
import org.endeavor.game.entity.player.net.out.impl.SendInterface;
import org.endeavor.game.entity.player.net.out.impl.SendItemOnInterface;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendSidebarInterface;
import org.endeavor.game.entity.player.net.out.impl.SendSound;
import org.endeavor.game.entity.player.net.out.impl.SendString;

public class WalkToActions {
	public static final int[][] SHOP_NPC_DATA = { 
		{ 563, 0 }, 
		{ 520, 0 }, 
		{ 549, 7 }, 
		{ 550, 8 }, 
		{ 553, 9 }, 
		{ 571, 10 },
		{ 541, 11 }, 
		{ 519, 12 }, 
		{ 587, 13 }, //herbs
		{ 540, 14 }, 
		{ 575, 15 },
		{ 2160, 16 },//pickaxes
		{ 563, 17 }, //fishing
		{ 8725, 99 },
		{ 6750, 18 },//pets
		{ 2620, 96 }, //tokkul shop
		{ 659, 95 }, 
		{ 1785, 23 },//team capes
		{ 2266, 92 }, 
		{ 576, 17 },
		{ 2305, 24 },//farm
		{ 2259, 25 } ,
		{558, 17},
		{583, 13},//herb shop port sarim
		{521, 0},
		{2538, 26}, // giles
		{577, 7},//cassie in catherb
		{586, 11},//gauis in summ train area
		{526, 0},//gen
		{527, 0},//gen
		{523, 0},//gen
		{522, 0},//gen
		{ 2572, 24 },//farm
		{542, 7},//lewie legs in al kharid
		{540, 14},//gem trader in al kharid
		{ 1040, 29 }, // fidelio
		{ 933, 28 }, // skilling clothin shop
		
	};

	public static void itemOnObject(final Player player, final int itemId, final int objectId, final int x, final int y) {
		if (GameSettings.DEV_MODE) {
			player.getClient().queueOutgoingPacket(new SendMessage("Using item " + itemId + " on object " + objectId));
		}
		
		if (player.getMagic().isTeleporting()) {
			return;
		}

		RSObject object = Region.getObject(x, y, player.getLocation().getZ());

		int z = player.getLocation().getZ();

		if ((object == null) && (!PlayerConstants.isOverrideObjectExistance(player, objectId, x, y, z))) {
			return;
		}

		final int[] length = ObjectConstants.getObjectLength(objectId, object != null ? object.getFace() : 0);

		TaskQueue.queue(new WalkToTask(player, x, y, length[0], length[1]) {
			@Override
			public void onDestination() {
				player.getUpdateFlags().sendFaceToDirection(length[0] >= 2 ? x + length[0] / 2 : x,
						length[1] >= 2 ? y + length[1] / 2 : y);

				if (SmithingConstants.useBarOnAnvil(player, objectId, itemId)) {
					return;
				}

				if (player.getQuesting().isUseItemOnQuestObject(itemId, objectId)) {
					return;
				}

				if (GodWars.useItemOnObject(player, itemId, objectId)) {
					return;
				}
				
				if (objectId == 3044 || objectId == 45310) {
					SmithingConstants.sendSmeltSelectionInterface(player);
					return;
				}

				if (ItemCreation.fillVials(player, itemId, objectId)) {
					return;
				}

				if (BoneBurying.useBonesOnAltar(player, itemId, objectId)) {
					return;
				}

				if (player.getFarming().plant(itemId, objectId, x, y)) {
					return;
				}

				if (player.getFarming().useItemOnPlant(itemId, x, y)) {
					return;
				}
				
				//newest here
				//document new objects
				
				if (objectId == 48802 && itemId == 954) {//to kalphite lair
					player.teleport(new Location(3484, 9510, 2));
					return;
				}
				
				if (objectId == 48803 && itemId == 954) {//to kalphite boss, from kalphite lair
					player.teleport(new Location(3507, 9494));
					return;
				}
				
				//end newest

				if (objectId == 3044 || objectId == 45310) {
					SmithingConstants.sendSmeltSelectionInterface(player);
					return;
				}

				if (objectId == 4309) {
					if (Spinnable.forId(itemId) != null) {
						Spinnable spinnable = Spinnable.forId(itemId);
						player.getAttributes().set("craftingType", CraftingType.WHEEL_SPINNING);
						player.getAttributes().set("spinnable", spinnable);
						player.getClient().queueOutgoingPacket(
								new SendString("\\n \\n \\n \\n" + spinnable.getOutcome().getDefinition().getName(),
										2799));
						player.getClient().queueOutgoingPacket(new SendChatInterface(4429));
						player.getClient().queueOutgoingPacket(
								new SendItemOnInterface(1746, 170, spinnable.getOutcome().getId()));
					} else {
						player.getClient().queueOutgoingPacket(new SendMessage("You cannot spin this!"));
					}

					return;
				}

				GameObject object = new GameObject(objectId, x, y, player.getLocation().getZ(), 10, 0);

				if (CookingTask.isCookableObject(object)) {
					CookingTask.showInterface(player, object, new Item(itemId, 1));
				}

				ArmorAnimator.armorOnAnimator(player, itemId, object, x, y);

				if ((DwarfMultiCannon.hasCannon(player)) && (!DwarfMultiCannon.getCannon(player).add(itemId)))
					DwarfMultiCannon.getCannon(player).load(player, itemId, objectId);
			}
		});
	}

	public static void clickObject(final Player player, final int option, final int id, final int x, final int y) {
		if (player.getMagic().isTeleporting()) {
			return;
		}

		int z = player.getLocation().getZ();

		RSObject object = Region.getObject(x, y, z);

		if ((object == null) && (!PlayerConstants.isOverrideObjectExistance(player, id, x, y, z))) {
			return;
		}

		final int[] length = ObjectConstants.getObjectLength(id, object == null ? 0 : object.getFace());

		if ((id == 5960) || (id == 5959)) {
			TaskQueue.queue(new PullLeverTask(player, x, y, length[0], length[1]) {
				@Override
				public void onDestination() {
					player.getUpdateFlags().sendFaceToDirection(length[0] >= 2 ? x + length[0] / 2 : x,
							length[1] >= 2 ? y + length[1] / 2 : y);
					player.getMagic().teleportNoWildernessRequirement(id == 5960 ? 3090 : 2539,
							id == 5960 ? 3956 : 4712, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				}
			});
			return;
		}

		TaskQueue.queue(new WalkToTask(player, x, y, length[0], length[1]) {
			@Override
			public void onDestination() {
				player.getUpdateFlags().sendFaceToDirection(length[0] >= 2 ? x + length[0] / 2 : x,
						length[1] >= 2 ? y + length[1] / 2 : y);

				WalkToActions.finishObjectClick(player, id, option, x, y);
			}
		});
	}

	public static void useItemOnNpc(final Player player, final int item, int slot) {
		if (player.getMagic().isTeleporting()) {
			return;
		}

		if ((slot > World.getNpcs().length) || (slot < 0)) {
			return;
		}

		final Mob mob = World.getNpcs()[slot];

		if (mob == null) {
			player.getMovementHandler().reset();
			return;
		}

		TaskQueue.queue(new FollowToEntityTask(player, mob) {
			@Override
			public void onDestination() {
				if (mob.face()) {
					mob.face(player);
				}

				player.face(mob);

				if (mob.getSize() > 1) {
					player.getMovementHandler().reset();
				}

				WalkToActions.finishItemOnNpc(player, item, mob);
			}
		});
	}

	public static void clickNpc(final Player player, final int option, int slot) {
		if (player.getMagic().isTeleporting()) {
			return;
		}

		if ((slot > World.getNpcs().length) || (slot < 0)) {
			return;
		}

		player.getMovementHandler().reset();

		final Mob mob = World.getNpcs()[slot];

		if (mob == null) {
			player.getMovementHandler().reset();
			return;
		}

		if (GameSettings.DEV_MODE) {
			player.getClient().queueOutgoingPacket(new SendMessage("option: " + option));
		}

		TaskQueue.queue(new FollowToEntityTask(player, mob) {
			@Override
			public void onDestination() {
				if (mob.face()) {
					mob.face(player);
				}

				player.face(mob);

				if (mob.getSize() > 1) {
					player.getMovementHandler().reset();
				}

				WalkToActions.finishClickNpc(player, option, mob);
			}
		});
	}

	public static final void finishItemOnNpc(Player player, int item, Mob mob) {
		switch (mob.getId()) {
		case 519:
			player.start(new Scavvo(player, item));
		}
	}

	public static final void finishClickNpc(Player player, int option, Mob mob) {
		int id = mob.getId();

		if (DungLobby.clickNpc(player, mob, id, option)) {
			return;
		}

		if (player.getSummoning().interact(mob, option)) {
			return;
		}
		
		if (id == 836) {
			player.start(new Shantay(player));
			return;
		}

		switch (option) {
		case 1:
			if (player.getFishing().clickNpc(mob, id, 1)) {
				return;
			}

			if (Pets.isMobPet(mob.getId())) {
				if ((player.getPets().hasPet()) && (mob.getOwner().equals(player)))
					player.getPets().remove();
				else {
					player.getClient().queueOutgoingPacket(new SendMessage("This is not your pet."));
				}

				return;
			}

			switch (id) {
			case 933:
				player.getShopping().open(28);
				break;
			case 1040:
				player.getShopping().open(29);
				break;
			case 219:
				player.getShopping().open(17);
				break;
			
			case 388://seer in seers' village for HFTD
				if (player.getQuesting().getQuestStage(QuestConstants.HORROR_FROM_THE_DEEP) == 1) {
					player.getInventory().addOrCreateGroundItem(983, 1, true);
					DialogueManager.sendStatement(player, "The Seer hands you a key.");
				} else {
					DialogueManager.sendNpcChat(player, 388, Emotion.CALM, "I'm sorry, I cannot talk now.");
				}
				break;

			case 3789:
			case 3788:
				player.getShopping().open(PestShop.SHOP_ID);
				break;
			case 6970:
				player.getClient().queueOutgoingPacket(new SendInterface(39700));
				break;
			case 519:
				player.start(new Scavvo(player));
				break;
			case 2271:
				player.start(new UseBankDialogue(player));
				break;
			case 1001:
				player.start(new ChangeBookDialogue(player));
				break;
			case 3001:
				player.start(new LotteryDialogue(player, mob));
				break;
			case 528:
				player.setEnterXInterfaceId(55777);
				player.getClient().queueOutgoingPacket(new SendEnterString());
				break;
			case 881:
				player.start(new Traiborn(player));
				break;
			case 2259:
				player.start(new MageOfZamorak(player));
				break;
			case 8082:
				player.getShopping().open(91);
				break;
			case 2999:
				player.getShopping().open(92);
				break;
			case 659:
				player.getShopping().open(95);
				break;
			case 805:
				player.start(new SkillMaster(player));
				break;
			case 9713:
				player.getShopping().open(97);
				break;
			case 9712:
				player.start(new DungTutor(player));
				break;
			case 2536: // Niles
				player.getShopping().open(27);
				break;
			case 2538: // Giles
				player.getShopping().open(26);
				break;
			case 650:
				player.start(new DungExiterDialogue(player));
				break;
			case 1334:
				player.start(new JossikDialogue(player));
				break;
			case 3385:
				player.start(new GypsyDialogue(player));
				break;
			case 494:
			case 902:
			case 6538:
				player.start(new UseBankDialogue(player));
				break;
				// case 8032:
				// player.start(new LocationTeleporter(player));
				// break;
			case 8725:
				player.start(new MandrithDialogue(player));
				break;
			case 1597:
				player.start(new Vannaka(player, false));
				break;
			case 3022:
			case 3118:
			case 4375:
				player.start(new RandomDialogue(player, mob));
				break;
			case 705:
				player.start(new MeleeInstructor(player));
				break;
			case 373:
				player.resetCombatStats();
				if (player.isPoisoned()) {
					player.curePoison(0);
				}
				DialogueManager.sendNpcChat(player, 373, Emotion.HAPPY_TALK,
						new String[] { "Your skills have been replenished, and you've been cured of poisons." });
				break;
			case 2676:
				player.getClient().queueOutgoingPacket(new SendInterface(3559));
				break;
			default:
				if (player.getUsername().equalsIgnoreCase(PlayerConstants.OWNER_USERNAME[0])) {
					player.getClient().queueOutgoingPacket(new SendMessage("Mob id: " + mob.getId()));
				}
				
				if (OneLineDialogue.doOneLineChat(player, id)) {
					return;
				}
				break;
			}
			break;
		case 2:
			for (int i = 0; i < SHOP_NPC_DATA.length; i++) {
				if (SHOP_NPC_DATA[i][0] == id) {
					player.getShopping().open(SHOP_NPC_DATA[i][1]);
					return;
				}
			}

			if (ThievingNpcTask.attemptThieving(player, mob)) {
				return;
			}

			if (player.getFishing().clickNpc(mob, id, 2)) {
				return;
			}
			switch (id) {
			case 3789:
			case 3788:
				player.getShopping().open(PestShop.SHOP_ID);
				break;
			case 6524:
				DialogueManager.sendNpcChat(player, 6524, Emotion.CALM, "I can decant your potions for 300 gp each.");
				break;
			case 6970:
				player.start(new Pikkupstix(player));
				break;
			case 519:
				player.start(new Scavvo(player));
				break;
			case 2271:
				player.getBank().openBank();
				break;
			/*case 528:
				player.getShopping().open(player);
				break;*/
			case 1597:
				player.start(new Vannaka(player, true));
				break;
			case 494:
			case 902:
			case 6538:
				player.getBank().openBank();
				break;
			case 534:
				player.start(new SkillcapeShop(player));
				break;
			case 804:
				HideTanning.sendTanningInterface(player);
				break;
			case 462://yanille rc mine wizard
				if (player.getQuesting().isQuestCompleted(QuestConstants.RUNE_MYSTERIES)) {
					TaskQueue.queue(new TeleOtherTask(mob, player, new Location(2923, 4819)));
				} else {
					DialogueManager.sendStatement(player,
							new String[] { "You must complete Rune Mysteries to do this." });
				}
				break;
			default:
				if (Pets.isMobPet(mob.getId())) {
					player.start(new PetDialogue(id));
					return;
				}

				if (GameSettings.DEV_MODE) {
					player.getClient().queueOutgoingPacket(new SendMessage("Mob id: " + mob.getId()));
				}
				break;
			}
			break;
		case 3:

			switch (id) {
			case 553://aubury teleport rc mine
				if (player.getQuesting().isQuestCompleted(QuestConstants.RUNE_MYSTERIES)) {
					TaskQueue.queue(new TeleOtherTask(mob, player, new Location(2923, 4819)));
				} else {
					DialogueManager.sendStatement(player,
							new String[] { "You must complete Rune Mysteries to do this." });
				}
				break;
			
			case 6524://potion decanting
				PotionDecanting.decantAll(player);
				break;
			case 1597:
				player.getShopping().open(93);
				break;
			case 2259://mage of zammy
				if (player.getQuesting().isQuestCompleted(QuestConstants.RUNE_MYSTERIES)) {
					TaskQueue.queue(new TeleOtherTask(mob, player, new Location(3039, 4834)));
				} else {
					DialogueManager.sendStatement(player,
							new String[] { "You must complete Rune Mysteries to do this." });
				}
				break;

			case 528:
				/*int c = 0;

				player.getClient().queueOutgoingPacket(new SendString("Players with shops", 8144));

				while (c <= 50) {
					player.getClient().queueOutgoingPacket(new SendString("", 8147 + c));
					c++;
				}

				c = 0;

				while (c <= 49) {
					player.getClient().queueOutgoingPacket(new SendString("", 12174 + c));
					c++;
				}

				c = 0;

				for (Player p : World.getPlayers()) {
					if ((p != null) && (p.isActive()) && (p.getPlayerShop().hasAnyItems())) {
						if (c <= 50)
							player.getClient().queueOutgoingPacket(new SendString(p.getUsername(), 8147 + c));
						else {
							player.getClient().queueOutgoingPacket(new SendString(p.getUsername(), 12174 + c - 50));
						}

						c++;
					}
				}

				player.getClient().queueOutgoingPacket(new SendInterface(8134));

				player.setEnterXInterfaceId(55777);
				player.getClient().queueOutgoingPacket(new SendEnterString());
					*/
				break;
			}
			break;
		case 4:
			if (id == 1597) {
				player.getShopping().open(93);
			} else if (id == 528) {
				player.setEnterXInterfaceId(55778);
				player.getClient().queueOutgoingPacket(new SendEnterString());
			}
			break;
		}
	}

	public static void finishObjectClick(Player player, int id, int option, int x, int y) {
		int z = player.getLocation().getZ();
		if ((GameSettings.DEV_MODE) && (PlayerConstants.isOwner(player))) {
			RSObject o = Region.getObject(x, y, player.getLocation().getZ());
			if (o != null) {
				player.getClient().queueOutgoingPacket(
						new SendMessage("Object option: " + option + " id: " + id + " x: " + x + " y: " + y + " face: " + o.getFace()
								+ " type: " + o.getType()));
			} else {
				player.getClient().queueOutgoingPacket(
						new SendMessage("clicking id with no reference: " + id));
			}
		}

		GameObject object = ObjectManager.getGameObject(x, y, z);
		
		if ((object != null) && ((object instanceof GraveStone))) {
			((GraveStone) object).readGraveStone(player);
			return;
		}
		
		//System.out.println("test1");
		if(Hunter.clickObject(player, id, x, y)) {
			return;
		}
		//System.out.println("test2");
		
		if(BountyHunter.getSingleton().handleObjectClick(player, id))
			return;
		
		if (id == 2552) {//pick-lock for tormented demons
			if (player.getY() <= 3958) {
				player.teleport(new Location(3106, 3959));
			} else {
				if (player.getMaxLevels()[SkillConstants.THIEVING] < 77) {
					player.getClient().queueOutgoingPacket(new SendMessage("You need a Thieving level of 77 to pick this lock."));
				} else {
					player.teleport(new Location(3106, 3958));
				}
			}
		}

		if (SmithingConstants.clickAnvil(player, id)) {
			return;
		}

		if(id == 28213) {
			Clan clan = Clans.getClanForPlayer(player);
			if(clan != null) {
				ClanWar war = ClanWars.getWarForClan(clan);	
				if(war != null) {
					war.addMember(player);
				} else
					player.send(new SendMessage("Your clan is not at war."));
			} else
				player.send(new SendMessage("You are not in a clan."));
		}
		
		if (id == 4577) {//door into HFTD
			if (player.getInventory().hasItemId(983)) {
				if (player.getY() <= 3635) {
					player.teleport(new Location(2509, 3636));
				} else {
					player.teleport(new Location(2509, 3635));
				}
			} else {
				player.getClient().queueOutgoingPacket(new SendMessage("You must get the key from The Seer in Seers' Village to open this door!"));
			}

			return;
		}

		if (Doors.isDoorJammed(player, x, y, z)) {
			return;
		}

		if (player.getQuesting().isClickQuestObject(option, id)) {
			return;
		}

		if (QuestConstants.clickObject(player, id)) {
			return;
		}

		if (DungLobby.clickObject(player, id, option, x, y, z)) {
			return;
		}

		if (player.getFarming().click(player, x, y, option)) {
			return;
		}

		if ((id == 1742) && (x == 2445) && (y == 3434)) {
			player.teleport(new Location(2445, 3433, 1));
			return;
		}
		if ((id == 1742) && (x == 2444) && (y == 3414)) {
			player.teleport(new Location(2445, 3416, 1));
			return;
		}
		if ((id == 1744) && (x == 2445) && (y == 3434)) {
			player.teleport(new Location(2446, 3436));
			return;
		}
		if ((id == 1744) && (x == 2445) && (y == 3415)) {
			player.teleport(new Location(2444, 3413));
			return;
		}

		if ((id == 15638) && (x == 2840) && (y == 3538)) {
			player.teleport(new Location(2854, 3537));
			return;
		}
		if ((id == 1738) && (x == 2853) && (y == 3535)) {
			player.teleport(new Location(2840, 3539, 2));
			return;
		}

		/*
		 * if (player.getController().equals(ControllerManager.HG_CONTROLLER)) {
		 * HungerGamesLobby.getCurrent().getMap().clickObject(player, id, x, y,
		 * player.getLocation().getZ(), option); return; }
		 */

		if (((id == 15644) || (id == 15641)) && (x == 2847) && (z == 2) && ((y == 3541) || (y == 3540))) {
			CyclopsRoom.enterCyclopsRoom(player);
			return;
		}

		if (id == 9357) {
			TzharrGame.finish(player, false);
			return;
		}

		if (PestControl.clickObject(player, id)) {
			return;
		}

		switch (option) {
		case 1:
			/*
			 * if (HungerGamesLobby.clickObject(player, id)) { return; }
			 */

			if (player.getDueling().clickForfeitTrapDoor(id)) {
				return;
			}

			if (GodWars.clickObject(player, id)) {
				return;
			}

			if (FightPits.clickObject(player, id)) {
				return;
			}

			if (Barrows.clickObject(player, id, x, y, z)) {
				return;
			}

			if (PestControl.clickObject(player, id)) {
				return;
			}

			if ((id >= 7) && (id <= 9)) {
				if (DwarfMultiCannon.hasCannon(player)) {
					DwarfMultiCannon.getCannon(player).pickup(player, x, y);
				}
				return;
			}

			if (id == 882) {
				if (x == 3105 && y == 3955) {//to tormented demons
					if (player.getMaxLevels()[SkillConstants.THIEVING] < 77) {
						player.getClient().queueOutgoingPacket(new SendMessage("Nice try, you need a Theiving level of 77 to access this dungeon!"));
						return;
					}
					
					player.teleport(new Location(2525, 5810));
					return;
				}
				
				if (x == 2488 && y == 10150) {//to dag kings
					if (player.getMaxLevels()[SkillConstants.AGILITY] < 45) {
						player.getClient().queueOutgoingPacket(new SendMessage("You need a Agility level of 45 to climb down this manhole!"));
						return;
					}
					
					player.teleport(new Location(2899, 4449));
					return;
				}
				
				if (x == 3331 && y == 3653) {//to strykeworms in wilderness
					if (player.getMaxLevels()[SkillConstants.SLAYER] < 80) {
						player.getClient().queueOutgoingPacket(new SendMessage("You need a Slayer level of 80 to climb down this manhole!"));
						return;
					}
					
					player.teleport(new Location(1990, 5189));
					return;
				}
				
				if (x == 3404 && y == 3089) {//to corp
					if (player.getSkill().getTotalLevel() < 800) {
						player.getClient().queueOutgoingPacket(new SendMessage("You need a total level of 800 to climb down this manhole!"));
						return;
					}
					
					player.teleport(new Location(2885, 4372));
					return;
				}
				
				if (x == 2600 && y == 3096) {//to basic training
					player.teleport(new Location(3161, 4237));
					return;
				}
			}

			if ((id == 5264) && (x == 3161) && (y == 4236)) {
				player.teleport(new Location(3085, 3493));
				return;
			}

			if (Doors.clickDoor(player, x, y, player.getLocation().getZ())) {
				return;
			}

			if (GnomeAgilityObjects.clickObject(player, id)) {
				return;
			}

			if (AgilityShortcuts.click(player, id)) {
				return;
			}

			if (ArmsRaceLobby.clickObject(player, id)) {
				return;
			}

			if (AbyssObjects.clickObject(player, id)) {
				return;
			}

			if (ObjectConstants.isSummoningObelisk(id)) {
				if (player.getLevels()[21] == player.getMaxLevels()[21]) {
					player.getClient().queueOutgoingPacket(new SendMessage("You already have full Summoning points."));
				} else {
					player.getClient().queueOutgoingPacket(new SendSound(442, 1, 0));
					player.getClient().queueOutgoingPacket(
							new SendMessage("You recharge your Summoning points at the obelisk."));
					player.getUpdateFlags().sendAnimation(8502, 0);
					player.getUpdateFlags().sendGraphic(new Graphic(1308, 0, false));
					player.getLevels()[21] = player.getMaxLevels()[21];
					player.getSkill().update(21);
				}
				return;
			}
			
			ThievingStallTask.attemptStealFromStall(player, id, new Location(x, y, z));
			RunecraftingTask.attemptRunecrafting(player, new GameObject(id, x, y, z, 10, 0));
			MiningTask.attemptMining(player, id, x, y, z);
			WoodcuttingTask.attemptWoodcutting(player, id, x, y);

			switch (id) {
			//newest
			
			//document names of new objects.
			
			case 37928://from corp
				player.teleport(new Location(3404, 3090));
				break;
			
			case 1752://from strykeworms to wilderness
				player.teleport(new Location(3331, 3654));
				break;
			
			case 10229://from dag kings to dag cave
				player.teleport(new Location(2488, 10151));
				break;
			
			case 8929://to dags
				player.teleport(new Location(2442, 10146));
				break;
			case 8966://from dags to waterbirth
				player.teleport(new Location(2523, 3739));
				break;
			
			case 47120://curses altar
				/*if (!PlayerConstants.isOwner(player) && player.getSkill().getTotalLevel() < 500 
						|| !PlayerConstants.isOwner(player) && player.getQuesting().getQuestPoints() < 5) {
					player.getClient().queueOutgoingPacket(new SendMessage("You do not meet the requirements for Curses!"));
					player.getClient().queueOutgoingPacket(new SendMessage("You need at least 500 total level, and at least 5 quest points."));
				} else {
					if (player.getPrayer().getPrayerBookType() == PrayerBookType.DEFAULT) {
						player.getClient().queueOutgoingPacket(new SendMessage("You switch your prayer book to Ancient Curses."));
						PrayerBook.setBook(player, PrayerBookType.CURSES);
					} else {
						player.getClient().queueOutgoingPacket(new SendMessage("You switch your prayer book to default."));
						PrayerBook.setBook(player, PrayerBookType.DEFAULT);
					}
					
					player.getUpdateFlags().sendAnimation(new Animation(645));
				}*/
				break;
			
			case 41077://from tormented demons
				player.teleport(new Location(3106, 3955));
				break;
			
			case 3832://kq hive to lair
				player.teleport(new Location(3509, 9499, 2));
				break;
			case 3829://kq lair to desert
				player.teleport(new Location(3228, 3110));
				break;
			
			case 6552://ancient altar
				player.send(new SendMessage("You have switched to the " + (player.getMagic().getSpellBookType() == SpellBookTypes.MODERN ? "ancient" : "modern") + " spellbook."));
				player.getMagic().setSpellBookType(player.getMagic().getSpellBookType() == SpellBookTypes.MODERN ? SpellBookTypes.ANCIENT : SpellBookTypes.MODERN);
				player.getMagic().setMagicBook(player.getMagic().getSpellBookType() == SpellBookTypes.MODERN ? 1151 : 12855);
				player.getUpdateFlags().sendAnimation(new Animation(645));
				break;
			case 410://lunar altar
				player.getMagic().setSpellBookType(player.getMagic().getSpellBookType() == SpellBookTypes.MODERN ? SpellBookTypes.LUNAR : SpellBookTypes.MODERN);
				player.getMagic().setMagicBook(player.getMagic().getSpellBookType() == SpellBookTypes.MODERN ? 1151 : 29999);
				player.getUpdateFlags().sendAnimation(new Animation(645));
				break;
			case 409:
				//regular altar
				if (player.getSkill().getLevels()[SkillConstants.PRAYER] < player.getMaxLevels()[SkillConstants.PRAYER]) {
					player.getSkill().setLevel(SkillConstants.PRAYER, player.getMaxLevels()[SkillConstants.PRAYER]);
					player.getClient().queueOutgoingPacket(new SendMessage("You recharge your prayer points."));
					player.getUpdateFlags().sendAnimation(new Animation(645));
				} else {
					player.getClient().queueOutgoingPacket(new SendMessage("Your prayer is already full."));
				}
				break;

			case 8987://warriors guild portal
				if (player.getX() > 2861) {
					if (player.getSkill().getCombatLevel() >= 45) {
						player.teleport(new Location(2858, 3543));
					} else {
						player.getClient().queueOutgoingPacket(new SendMessage("You need a combat level of at least 45 to enter!"));
					}
				} else {
					player.teleport(new Location(2879, 3546));
				}
				break;

			case 5947://to lumbridge swamp
				player.teleport(new Location(3168, 9572));
				break;
			case 5946://from lumbridge swamp XXX DO THIS ID
				player.teleport(new Location(3169, 3173));
				break;

			case 2113://to fally mine
				player.teleport(new Location(3021, 9739));
				break;
			case 30941://from fally mine
				player.teleport(new Location(3019, 3337));
				break;

			case 7257://to rogue den
				player.teleport(new Location(3061, 4985, 1));
				break;
			case 7258://from rogue den
				player.teleport(new Location(2906, 3537));
				break;

			case 20987://to taverly dung
				player.teleport(new Location(2884, 9798));
				break;
			case 32015://from taverly dung
				player.teleport(new Location(2884, 3398));
				break;

			case 5167://grave to experiments
				player.teleport(new Location(3577, 9927));
				break;
			case 1757://from experiments to grave || from mages' guild basement to mages' guild
				if (Misc.getManhattanDistance(player.getLocation(), new Location(2594, 9486)) <= 15) {//mages' guild basement
					player.teleport(new Location(2594, 3086));
				} else {
					player.teleport(new Location(3574, 3525));
				}
				break;
				
			case 1754://mages' guild to basement
				player.teleport(new Location(2594, 9486));
				break;
				
			case 5008://to rellekka slayer dungeon
				player.teleport(new Location(3206, 9379, 0));
				break;
			case 4499://to rellekka slayer dungeon alt
				player.teleport(new Location(3206, 9379, 0));
				player.getClient().queueOutgoingPacket(
						new SendMessage("This is an alternate entrance, use the rope to find the shorter cave entrance."));
				break;
			case 6439://from rellekka slayer dungeon
				player.teleport(new Location(2730, 3713, 0));
				break;
				
			case 26342://to godwars
				player.teleport(new Location(2882, 5311, 2));
				break;
			case 26293://from godwars
				player.teleport(new Location(2919, 3749));
				break;
				
			case 1765://to kbd
				player.teleport(new Location(2273, 4680));
				break;
			case 1817://from kbd
				player.teleport(new Location(3017, 3848));
				break;
				
			case 13628://Yanille Minigame Portal
				player.start(new YanilleMinigamePortal(player));
				break;
				
			case 38698://to clan wars safe pk area
				player.setController(ControllerManager.CW_SAFE_PK_CONTROLLER);
				player.teleport(new Location(2815, 5511));
				break;
			case 38700://from clan wars safe pk area
				player.teleport(new Location(3087, 3490));
				
				player.curePoison(0);
				player.resetLevels();
				
				player.setController(ControllerManager.DEFAULT_CONTROLLER);
				break;
				
			//end newest

			case 36687:
				player.teleport(new Location(3210, 9616, 0));
				break;
			case 29355:
				player.teleport(new Location(3210, 3216, 0));
				break;
			case 2492:
				player.getMagic().teleport(2809, 3436, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				break;
			case 13627:
				player.start(new WildernessTeleport(player));
				break;
			case 2191:
				CrystalChest.click(player);
				break;
			case 9319:
				player.changeZ(1);
				break;
			case 9320:
				player.changeZ(0);
				break;
			case 2213:
			case 6084:
			case 11758:
			case 12309:
			case 25808:
			case 26972:
			case 27663:
			case 49018:
			case 14369:
			case 24914:
			case 2693:
			case 35647:
				player.getBank().openBank();
				break;
			case 11601:
				JewelryCreationTask.sendInterface(player);
				break;
			case 3044:
			case 11666:
				SmithingConstants.sendSmeltSelectionInterface(player);
				break;
			case 2806:
				player.teleport(new Location(2885, 4372));
				break;
			case 37929:
				if (player.getLocation().getX() < 2926) {
					if (player.getLocation().getX() <= 2918)
						player.teleport(new Location(2921, 4384));
					else {
						player.teleport(new Location(2917, 4384));
					}
				} else if (player.getLocation().getX() <= 2971)
					player.teleport(new Location(2974, 3484));
				else {
					player.teleport(new Location(2970, 4384));
				}

				break;
			case 1293:
				player.start(new SpiritTree(player));
				break;
			case 2640:
			case 4859:
			case 27661:
				if (player.getLevels()[5] == player.getMaxLevels()[5]) {
					player.getClient().queueOutgoingPacket(new SendMessage("You already have full Prayer."));
				} else {
					player.getClient().queueOutgoingPacket(new SendSound(442, 1, 0));
					player.getClient().queueOutgoingPacket(
							new SendMessage("You recharge your Prayer points at the altar."));
					player.getUpdateFlags().sendAnimation(645, 5);
					player.getLevels()[5] = player.getMaxLevels()[5];
					player.getSkill().update(5);
				}
				break;
			case 23271:
				TaskQueue.queue(new HopDitchTask(player));
				break;
			case 9356://to fight caves or fight kiln
				player.start(new SelectGameDialogue(player));
				break;
			case 15653:
				WarriorsGuild.canEnter(player);
				break;
			case 4493:
				player.teleport(new Location(3433, 3537, 1));
				break;
			case 4494:
				player.teleport(new Location(3438, 3538));
				break;
			case 3556:
			case 10529:
				if (player.getLocation().getY() >= 3556)
					player.teleport(new Location(3427, 3555, 1));
				else {
					player.teleport(new Location(3427, 3556, 1));
				}
				break;
			case 4495:
				player.teleport(new Location(3417, 3541, 2));
				break;
			case 4496:
				player.teleport(new Location(3412, 3541, 1));
				break;
			case 5126:
				if (player.getLocation().getY() <= 3554)
					player.teleport(new Location(3445, 3555, 2));
				else {
					player.teleport(new Location(3445, 3554, 2));
				}
				break;
			case 1596:
			case 1597:
				if (player.getLocation().getY() <= 9917)
					player.teleport(new Location(3131, 9918, 0));
				else {
					player.teleport(new Location(3131, 9917, 0));
				}
				break;
			case 1557:
			case 1558:
				if (player.getLocation().getY() == 9944)
					player.teleport(new Location(3105, 9945, 0));
				else if (player.getLocation().getY() == 9945)
					player.teleport(new Location(3105, 9944, 0));
				else if (player.getLocation().getX() == 3146)
					player.teleport(new Location(3145, 9871, 0));
				else if (player.getLocation().getX() == 3145) {
					player.teleport(new Location(3146, 9871, 0));
				}
				break;
			case 2623:
				if (player.getLocation().getX() >= 2924)
					player.teleport(new Location(2923, 9803, 0));
				else {
					player.teleport(new Location(2924, 9803, 0));
				}
				break;
			case 8960:
				if (player.getLocation().getX() <= 2490)
					player.teleport(new Location(2491, 10131, 0));
				else {
					player.teleport(new Location(2490, 10131, 0));
				}
				break;
			case 8959:
				if (player.getLocation().getX() <= 2490)
					player.teleport(new Location(2491, 10146, 0));
				else {
					player.teleport(new Location(2490, 10146, 0));
				}
				break;
			case 8958:
				if (player.getLocation().getX() <= 2490)
					player.teleport(new Location(2491, 10163, 0));
				else {
					player.teleport(new Location(2490, 10163, 0));
				}
				break;
			case 5103:
				if (player.getLocation().getX() >= 2691)
					player.teleport(new Location(2689, 9564, 0));
				else {
					player.teleport(new Location(2691, 9564, 0));
				}
				break;
			case 5104:
				if (player.getLocation().getY() <= 9568)
					player.teleport(new Location(2683, 9570, 0));
				else {
					player.teleport(new Location(2683, 9568, 0));
				}
				break;
			case 5110:
				player.teleport(new Location(2647, 9557, 0));
				break;
			case 5111:
				player.teleport(new Location(2649, 9562, 0));
				break;
			case 5106:
				if (player.getLocation().getX() <= 2674)
					player.teleport(new Location(2676, 9479, 0));
				else {
					player.teleport(new Location(2674, 9479, 0));
				}
				break;
			case 5107:
				if (player.getLocation().getX() <= 2693)
					player.teleport(new Location(2695, 9482, 0));
				else {
					player.teleport(new Location(2693, 9482, 0));
				}
				break;
			case 5105:
				if (player.getLocation().getX() <= 2672)
					player.teleport(new Location(2674, 9499, 0));
				else {
					player.teleport(new Location(2672, 9499, 0));
				}
				break;
			case 5088:
				player.teleport(new Location(2687, 9506, 0));
				break;
			case 5090:
				player.teleport(new Location(2682, 9506, 0));
				break;
			case 5097:
				player.teleport(new Location(2636, 9510, 2));
				break;
			case 5098:
				player.teleport(new Location(2636, 9517, 0));
				break;
			case 5096:
				player.teleport(new Location(2649, 9591, 0));
				break;
			case 5094:
				player.teleport(new Location(2643, 9594, 2));
				break;
			default:
				Ladder l = Region.getLadder(x, y, z);

				if (l != null) {
					l.click(player, option);
				}
				break;
			}

			break;
		case 2:
			if (id == 12309) {
				player.getShopping().open(10);
				return;
			}

			Location location = new Location(x, y, z);
			ThievingStallTask.attemptStealFromStall(player, id, location);

			if (DwarfMultiCannon.hasCannon(player)) {
				DwarfMultiCannon.getCannon(player).pickup(player, x, y);
				return;
			}

			switch (id) {
			//newest
			
			case 35543://shantay pass
				if (player.getY() >= 3117) {//enter
					if (player.getInventory().hasItemAmount(Shantay.getShantayPass())) {
						player.getClient().queueOutgoingPacket(new SendMessage("You use your Shantay pass to enter the desert.."));
						player.teleport(new Location(3304, 3117 - 2));
						player.getInventory().remove(Shantay.getShantayPass());
					} else {
						player.start(new Shantay(player));
						//DialogueManager.sendStatement(player, "You do not have a Shantay pass.");
					}
				} else {
					player.teleport(new Location(3304, 3117));
				}
				break;
				
			//end newest
			
			case 4309:
				for (Item i : player.getInventory().getItems()) {
					if (i != null && Spinnable.forId(i.getId()) != null) {
						Spinnable spinnable = Spinnable.forId(i.getId());
						player.getAttributes().set("craftingType", CraftingType.WHEEL_SPINNING);
						player.getAttributes().set("spinnable", spinnable);
						player.getClient().queueOutgoingPacket(
								new SendString("\\n \\n \\n \\n" + spinnable.getOutcome().getDefinition().getName(),
										2799));
						player.getClient().queueOutgoingPacket(new SendChatInterface(4429));
						player.getClient().queueOutgoingPacket(
								new SendItemOnInterface(1746, 170, spinnable.getOutcome().getId()));
						return;
					}
				}

				player.getClient().queueOutgoingPacket(new SendMessage("You do not have anything to spin!"));
				break;
				
			case 3044:
			case 11666:
			case 45310:
				SmithingConstants.sendSmeltSelectionInterface(player);
				break;
			case 2646:
				TaskQueue.queue(new HarvestTask(player, id, 1779, x, y, z));
				break;
			case 1293:
				player.teleport(new Location(2461, 3434, 0));
				break;
			case 2213:
			case 6084:
			case 11758:
			case 12309:
			case 25808:
			case 26972:
			case 27663:
			case 49018:
			case 14369:
			case 24914:
			case 35647:
				player.getBank().openBank();
				break;

			default:
				Ladder l = Region.getLadder(x, y, z);

				if (l != null) {
					l.click(player, option);
				}
				break;
			}

			break;
		case 3:
			switch (id) {
			/*case 2213:
			case 6084:
			case 11758:
			case 25808:
			case 26972:
			case 27663:
			case 49018:
			case 14369:
			case 24914:
			case 35647:
				player.start(new UsePlayerShopDialogue(player));
				break;*/

			case 12309:
				player.getShopping().open(98);
				break;

			default:
				return;
			}
			break;
		case 4:
			break;
		}
	}
}
