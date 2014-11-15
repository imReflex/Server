package org.endeavor.game.content;

import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.content.dialogue.impl.ConfirmDialogue;
import org.endeavor.game.content.minigames.dungeoneering.DungConstants;
import org.endeavor.game.content.skill.SkillConstants;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.mob.MobDrops;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendInterface;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendSound;
import org.endeavor.game.entity.player.net.out.impl.SendString;

public class ItemInteraction {
	
	public static final int[][] EXP_LAMP_INTERFACE_BUTTONS = {
		{10252, SkillConstants.ATTACK},
		{10253, SkillConstants.STRENGTH},
		{10254, SkillConstants.RANGED},
		{10255, SkillConstants.MAGIC},
		{11000, SkillConstants.DEFENCE},
		{11001, SkillConstants.HITPOINTS},
		{11002, SkillConstants.PRAYER},
		{11003, SkillConstants.AGILITY},
		{11004, SkillConstants.HERBLORE},
		{11005, SkillConstants.THIEVING},
		{11006, SkillConstants.CRAFTING},
		{11007, SkillConstants.RUNECRAFTING},
		{47002, SkillConstants.SLAYER},
		{54090, SkillConstants.FARMING},
		{11008, SkillConstants.MINING},
		{11009, SkillConstants.SMITHING},
		{11010, SkillConstants.FISHING},
		{11011, SkillConstants.COOKING},
		{11012, SkillConstants.FIREMAKING},
		{11013, SkillConstants.WOODCUTTING},
		{11014, SkillConstants.FLETCHING},
	};
	
	public static final int[] MYSTERY_BOX_ITEMS = {
		14484, 11702, 11704, 11706, 11708, 14479, 11724, 11728, 13887, 13890, 13893, 13896, 
		13899, 13902, 13884, 13876, 13873, 13870, 13864, 13861, 13858
	};
	
	public static boolean useExperienceLamp(final Player p, int button) {//XXX:do
		if (!p.getInventory().hasItemId(2528) || p.getController().equals(DungConstants.DUNG_CONTROLLER)) {
			return false;
		}
		
		double exp = 2500;
		int level = -1;
		
		for (int[] i : EXP_LAMP_INTERFACE_BUTTONS) {
			if (i[0] == button) {
				level = i[1];
				break;
			}
		}
		
		if (exp > 0 && level > -1) {
			final int level2 = level;
			final double exp2 = exp;
			
			p.start(new ConfirmDialogue(p, new String[] {(int) (exp2 * SkillConstants.EXPERIENCE_RATES[level2]) + " experience in " + SkillConstants.SKILL_NAMES[level2] + "?"}) {

				@Override
				public void onConfirm() {
					if (!p.getInventory().hasItemId(2528) || p.getController().equals(DungConstants.DUNG_CONTROLLER)) {
						return;
					}
					
					double gained = p.getSkill().addExperience(level2, exp2);
					DialogueManager.sendStatement(p, "You gain " + ((int) gained) + " experience in " + SkillConstants.SKILL_NAMES [level2]+ ".");
					p.getInventory().remove(2528);
				}
				
			});
			return true;
		}
		
		return false;
	}
	
	public static boolean useItem(final Player p, final int id) {
		if (id == 15467) {
			p.start(new ConfirmDialogue(p) {

				@Override
				public void onConfirm() {
					if (!p.getInventory().hasItemId(id)) {
						return;
					}
					
					if (!p.getSlayer().hasTask()) {
						DialogueManager.sendStatement(p, "You do not have a Slayer task.");
						return;
					}
					
					p.getInventory().remove(new Item(id));
					p.getSlayer().reset();
				}
				
			});
		}
		
		if (id == 11858) {//third age melee
			if (p.getInventory().getFreeSlots() < 3) {
				p.send(new SendMessage("You need more inventory space to do this."));
				return true;
			}
			
			p.getInventory().remove(11858, 1, false);
			
			p.getInventory().add(10346, 1, false);
			p.getInventory().add(10348, 1, false);
			p.getInventory().add(10350, 1, false);
			p.getInventory().add(10352, 1, true);
			
			return true;
		}
		
		if (id == 11860) {//third age ranger
			if (p.getInventory().getFreeSlots() < 3) {
				p.send(new SendMessage("You need more inventory space to do this."));
				return true;
			}
			
			p.getInventory().remove(11860, 1, false);
			
			p.getInventory().add(10330, 1, false);
			p.getInventory().add(10332, 1, false);
			p.getInventory().add(10334, 1, false);
			p.getInventory().add(10336, 1, true);
			
			return true;
		}
		
		if (id == 11862) {//third age magic
			if (p.getInventory().getFreeSlots() < 3) {
				p.send(new SendMessage("You need more inventory space to do this."));
				return true;
			}
			
			p.getInventory().remove(11862, 1, false);
			
			p.getInventory().add(10338, 1, false);
			p.getInventory().add(10340, 1, false);
			p.getInventory().add(10342, 1, false);
			p.getInventory().add(10344, 1, true);
			
			return true;
		}
		
		if (id == 15501) {
			p.getInventory().remove(id, 1, false);
			
			int r = Misc.randomNumber(4);
			
			Item i = null;
			
			if (r == 0) {
				i = new Item(MobDrops.CHARMS[0], 3 + Misc.randomNumber(7));
			} else if (r == 1) {
				i = new Item(MobDrops.CHARMS[1], 3 + Misc.randomNumber(5));
			} else if (r == 2) {
				i = new Item(MobDrops.CHARMS[2], 4 + Misc.randomNumber(3));
			} else {
				i = new Item(MobDrops.CHARMS[3], 3 + Misc.randomNumber(2));
			}
			
			p.getInventory().addOrCreateGroundItem(i.getId(), i.getAmount(), true);
			p.getInventory().addOrCreateGroundItem(18016, 1 + (Misc.randomNumber(2500)), true);
			p.getInventory().addOrCreateGroundItem(12525, 1 + (Misc.randomNumber(75)), true);
			return true;
		}
		
		if (id == 6199 && !p.getController().equals(DungConstants.DUNG_CONTROLLER)) {
			p.getInventory().remove(id, 1, false);
			Item i = new Item(MYSTERY_BOX_ITEMS[Misc.randomNumber(MYSTERY_BOX_ITEMS.length)]);
			p.getClient().queueOutgoingPacket(new SendMessage("You receive " + Misc.getAOrAn(i.getDefinition().getName()) + " " + i.getDefinition().getName() + " from the Mystery box!"));
			p.getInventory().add(i);
			return true;
		}
		
		if (id == 2528) {
			p.getClient().queueOutgoingPacket(new SendString("", 2831));
			p.getClient().queueOutgoingPacket(new SendInterface(2808));
			return true;
		}
		
		if (id == 5020 && !p.getController().equals(DungConstants.DUNG_CONTROLLER)) {
			p.getInventory().remove(5020, 1, true);
			p.setDungPoints(p.getDungPoints() + 150);
			QuestTab.updateDungPoints(p);
			p.getClient().queueOutgoingPacket(new SendMessage("You receive 150 Dungeoneering points."));
			return true;
		}

		if (id == 4447 && !p.getController().equals(DungConstants.DUNG_CONTROLLER)) {
			p.getSkill().addExperience(16, 12500.0D);
			p.getInventory().remove(id, 1, true);
			p.getClient().queueOutgoingPacket(new SendMessage("You receive some agility experience."));
		}

		if (id == 18336) {
			DialogueManager.sendStatement(p, new String[] { "Have this in your inventory while harvesting",
					"your farm patch for a chance at receiving", "seeds back." });
		}

		if (id == 13664 && !p.getController().equals(DungConstants.DUNG_CONTROLLER)) {
			p.start(new ConfirmDialogue(p, new String[] { "This ticket is tradeable.",
					"You will get a Donator rank on your account.",
					"You cannot get your ticket back after doing this.", "Are you sure?" }) {
				
				@Override
				public void onConfirm() {
					if (p.getRights() > 0) {
						DialogueManager.sendStatement(p, "You already have a Donator rank or above!");
						return;
					}
					
					if (!p.getInventory().hasItemId(id)) {
						return;
					}
					
					World.sendGlobalMessage(p.getUsername() + " has redeemed a Donator rank!", true);
					p.getInventory().remove(new Item(id));
					p.getClient().queueOutgoingPacket(new SendMessage("You have redeemed the donator rank. Re-log to show effects in public chat."));
					p.setRights(1);
				}

			});
		}
		
		/*if ((id == 13663)  && !p.getController().equals(DungConstants.DUNG_CONTROLLER)) {
			if (p.hasPremium()) {
				DialogueManager
						.sendStatement(p, new String[] { "You must wait until your premium days have expired." });
				return true;
			}

			p.start(new ConfirmDialogue(p, new String[] { "This ticket is tradeable.",
					"You will get " + 30 + " days of premium for redeeming this.",
					"You cannot get your ticket back after doing this.", "Are you sure?" }) {
				@Override
				public void onConfirm() {
					if (!p.getInventory().hasItemId(id)) {
						return;
					}
					
					World.sendGlobalMessage(p.getUsername() + " has redeemed Premium membership!", true);
					p.getInventory().remove(new Item(id));
					p.getClient().queueOutgoingPacket(new SendMessage("You have redeemed " + 30 + " days of premium!"));
					player.givePremium(false);
					QuestTab.updatePremium(player);
				}

			});
		}*/

		return false;
	}

	public static boolean clickPouch(Player p, int id, int option) {
		if ((id >= 5509) && (id <= 5512)) {
			if (option == 1) {
				int index = id - 5509;
				int ess = (index + 1) * (p.isMember() ? 4 : 3);

				if (p.getPouches()[index] == ess) {
					p.getClient().queueOutgoingPacket(new SendMessage("Your pouch is already full."));
					return true;
				}

				if ((index > 0) && (p.getMaxLevels()[20] < index * 25)) {
					p.getClient().queueOutgoingPacket(
							new SendMessage("You need a Runecraft level of " + index * 25 + " to use this pouch."));
					return true;
				}

				int amount = ess - p.getPouches()[index];
				int invAmount = p.getInventory().getItemAmount(7936);

				if (invAmount == 0) {
					p.getClient().queueOutgoingPacket(
							new SendMessage("You do not have any essence to fill your pouch with."));
					return true;
				}

				if (amount > invAmount) {
					amount = invAmount;
				}

				p.getInventory().remove(7936, amount, true);
				p.getPouches()[index] = ((byte) (p.getPouches()[index] + amount));

				if (p.getPouches()[index] == ess)
					p.getClient().queueOutgoingPacket(new SendMessage("You fill your pouch."));
			} else if (option == 2) {
				int index = id - 5509;

				if (p.getPouches()[index] == 0) {
					p.getClient().queueOutgoingPacket(new SendMessage("Your pouch is empty."));
					return true;
				}

				int add = p.getInventory().add(7936, p.getPouches()[index]);
				p.getPouches()[index] = ((byte) (p.getPouches()[index] - add));

				if (p.getPouches()[index] == 0)
					p.getClient().queueOutgoingPacket(new SendMessage("You empty your pouch."));
			} else if (option == 3) {
				int index = id - 5509;

				if (p.getPouches()[index] == 0) {
					p.getClient().queueOutgoingPacket(new SendMessage("Your pouch is empty."));
					return true;
				}

				p.getClient().queueOutgoingPacket(
						new SendMessage("There is " + p.getPouches()[index] + " essence is your pouch."));
			}

			return true;
		}

		return false;
	}

	public static boolean openVoidSet(Player p, int id) {
		if (id == 11872) {
			if (!p.getInventory().hasSpaceFor(getVoidSet())) {
				p.getClient().queueOutgoingPacket(new SendMessage("You must clear some inventory space to do this."));
				return true;
			}

			p.getInventory().remove(11872, 1, false);
			p.getInventory().add(getVoidSet(), true);
			p.getClient().queueOutgoingPacket(new SendMessage("You receive a Void knight set."));
			return true;
		}

		if (id == 11848) {
			if (!p.getInventory().hasSpaceFor(getDharokSet())) {
				p.getClient().queueOutgoingPacket(new SendMessage("You must clear some inventory space to do this."));
				return true;
			}

			p.getInventory().remove(11848, 1, false);
			p.getInventory().add(getDharokSet(), true);
			p.getClient().queueOutgoingPacket(new SendMessage("You receive a Dharok set."));
			return true;
		}

		return false;
	}

	public static Item[] getVoidSet() {
		return new Item[] { new Item(8839), new Item(8840), new Item(8842), new Item(11674), new Item(11675),
				new Item(11676) };
	}

	public static Item[] getDharokSet() {
		return new Item[] { new Item(4716), new Item(4718), new Item(4720), new Item(4722) };
	}

	public static boolean openCasket(Player p, int id) {
		if (id == 2714) {
			p.getClient().queueOutgoingPacket(new SendSound(1641, 10, 0));
			addGoldForItem(p, 2714, p.isMember() ? 75000 : 50000);
			return true;
		}

		if (id == 2724) {
			p.getClient().queueOutgoingPacket(new SendSound(1641, 10, 0));
			addGoldForItem(p, 2724, p.isMember() ? 200000 : 100000);
			return true;
		}

		return false;
	}

	public static boolean addGoldForItem(Player p, int id, int am) {
		if ((p.getInventory().hasSpaceFor(new Item(995, am)))
				|| (p.getInventory().hasSpaceOnRemove(new Item(id, 1), new Item(995, am)))) {
			p.getInventory().remove(id, 1, false);
			p.getInventory().add(995, am);
			p.getClient().queueOutgoingPacket(new SendMessage("You recieve " + am + " coins from the casket."));
		} else {
			p.getClient().queueOutgoingPacket(new SendMessage("You do not have enough inventory space to open this."));
		}

		return false;
	}
}
