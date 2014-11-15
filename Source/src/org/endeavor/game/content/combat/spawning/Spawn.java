package org.endeavor.game.content.combat.spawning;

import static org.endeavor.game.content.QuestTab.*;

import java.util.ArrayList;

import org.endeavor.GameSettings;
import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.game.content.quest.QuestConstants;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.EquipmentConstants;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class Spawn {
	private static final ArrayList<Integer> spawnable = new ArrayList<Integer>();
	
	private static void a(int id) {
		if (!spawnable.contains(id)) {
			spawnable.add(id);
			GameDefinitionLoader.setValueToZero(id);
			GameDefinitionLoader.setNotTradable(id);
		}
	}
	
	public static boolean spawnable(int id) {
		return /*spawnable.contains(id)*/true;
	}
	
	public static void declare() {
		a(1725);
		a(19111);
		a(1052);
		a(542);
		a(544);
		a(3105);
		a(1153);
		a(4587);
		a(1215);
		a(7459);//addy gloves
		a(3842);//unholy book
		a(1731);
		a(3105);//climbing boots
		a(861);
		a(890);
		a(2497);
		a(1129);
		a(1167);
		a(2491);
		a(19111);
		a(7459);//addy gloves
		a(3105);//climbing boots
		a(6107);
		a(6108);
		a(6109);
		a(6110);
		a(4675);
		a(19111);
		a(1731);
		a(7459);//addy gloves
		a(3842);//unholy book
		a(3105);//climbing boots
		a(6107);
		a(2497);
		a(6109);
		a(6110);
		a(4675);
		a(19111);
		a(1731);
		a(4587);
		a(1215);
		a(890);
		a(861);
		a(7459);//addy gloves
		a(3842);//unholy book
		a(3105);//climbing boots
		a(4587);
		a(1215);
		a(5574);
		a(5575);
		a(5576);
		a(1725);
		a(19111);
		a(7459);//addy gloves
		a(3842);//unholy book
		a(3105);//climbing boots
		a(7459);//addy gloves
		a(3842);//unholy book
		a(4089);
		a(4091);
		a(4093);
		a(4675);
		a(1731);
		a(3105);//climbing boots
		a(7459);//addy gloves
		a(3842);//unholy book
		a(4089);
		a(4091);
		a(4093);
		a(4587);
		a(1731);
		a(1215);
		a(3105);//climbing boots
		a(7459);//addy gloves
		a(4089);
		a(4091);
		a(4093);
		a(861);
		a(1731);
		a(890);
		a(1153);
		a(1115);
		a(1067);
		a(1191);
		a(5574);
		a(5575);
		a(5576);
		a(1161);
		a(1123);
		a(1073);
		a(1199);
		a(4089);
		a(4091);
		a(4093);
		a(4095);
		a(4097);
		a(2503);
		a(2497);
		a(2491);
		a(6106);
		a(6107);
		a(6108);
		a(6109);
		a(6110);
		a(6111);
		a(4587);
		a(1215);
		a(4153);
		a(1333);
		a(1323);
		a(6528);
		a(861);
		a(810);//addy darts
		a(867);//addy knifes
		a(9185);
		a(9142);
		a(6528);
		a(4675);
		a(1387);
		a(1385);
		a(1383);
		a(1381);
		a(9142);
		a(890);
		a(810);//addy darts
		a(867);//addy knifes
		a(4153);
		a(6528);
		a(1540);
		a(19111);
		a(6570);
		
		System.out.println("Set spawnable item values.");
	}
	
	public static boolean clickButton(Player p, int button) {
		switch (button) {
		
		case BUTTON_1://clear equipment
			boolean error = false;
			int index = 0;
			
			for (Item i : p.getEquipment().getItems()) {
				if (i != null) {
					if (GameSettings.DEV_MODE || spawnable.contains(i.getId())) {
						if (i.getId() != 6570 && i.getId() != 19111 
								|| i.getId() == 6570 && p.getBank().hasItemId(6570) || i.getId() == 19111 && p.getBank().hasItemId(19111)) {
							p.getEquipment().getItems()[index] = null;
						}
					} else {
						error = true;
					}
				}
				
				index++;
			}
			
			if (error) {
				p.send(new SendMessage("Your Equipment contains an unspawnable item which cannot be deleted."));
			}
			
			p.setAppearanceUpdateRequired(true);
			p.getEquipment().onLogin();
			p.updateCombatType();
			return true;
		case BUTTON_2://clear inventory
			boolean error1 = false;
			int index1 = 0;
			
			for (Item i : p.getInventory().getItems()) {
				if (i != null) {
					if (GameSettings.DEV_MODE || spawnable.contains(i.getId())) {
						if (i.getId() != 6570 && i.getId() != 19111 
								|| i.getId() == 6570 && p.getBank().hasItemId(6570) || i.getId() == 19111 && p.getBank().hasItemId(19111)) {
							p.getInventory().getItems()[index1] = null;
						}
					} else {
						error1 = true;
					}
				}
				
				index1++;
			}
			
			if (error1) {
				p.send(new SendMessage("Your Inventory contains an unspawnable item which cannot be deleted."));
			}
			
			p.setAppearanceUpdateRequired(true);
			p.getInventory().update();
			return true;
		case BUTTON_3://custom loadout 1
			p.start(new UseLoadoutDialogue(p, 0));
			return true;
		case BUTTON_4://custom loadout 2
			p.start(new UseLoadoutDialogue(p, 1));
			return true;
		case BUTTON_5://custom loadout 3
			p.start(new UseLoadoutDialogue(p, 2));
			return true;
		case BUTTON_6://custom loadout 4
			p.start(new UseLoadoutDialogue(p, 3));
			return true;
		case BUTTON_7://custom loadout 5
			p.start(new UseLoadoutDialogue(p, 4));
			return true;
		case BUTTON_8://custom loadout 6
			p.start(new UseLoadoutDialogue(p, 5));
			return true;
		case BUTTON_9://empty
			//do stuff
			return true;
		case BUTTON_10://melee pure equipment
			spawn(p, 
					new Item(1725),
					new Item(19111),
					new Item(542),
					new Item(544),
					new Item(3105),
					new Item(1153),
					new Item(4587),
					new Item(1215),
					
					new Item(7459),//addy gloves
					new Item(3842)//unholy book
					);
			return true;
		case BUTTON_11://ranged pure equipment
			spawn(p, 
					new Item(1731),
					new Item(3105),//climbing boots
					new Item(861),
					new Item(890, 400),
					new Item(2497),
					new Item(1129),
					new Item(1167),
					new Item(2491),
					new Item(19111),
					new Item(7459)//addy gloves
					);
			return true;
		case BUTTON_12://magic pure equipment
			spawn(p, 
					new Item(3105),//climbing boots
					new Item(6107),
					new Item(6108),
					new Item(6109),
					new Item(6110),
					new Item(4675),
					new Item(19111),
					new Item(1731),
					new Item(7459),//addy gloves
					new Item(3842)//unholy book
					);
			return true;
		case BUTTON_13://hybrid pure equipment
			spawn(p, 
					new Item(3105),//climbing boots
					new Item(6107),
					new Item(2497),
					new Item(6109),
					new Item(6110),
					new Item(4675),
					new Item(19111),
					new Item(1731),
					new Item(4587),
					new Item(1215),
					new Item(890, 400),
					new Item(861),
					new Item(7459),//addy gloves
					new Item(3842)//unholy book
					);
			return true;
		case BUTTON_14://initiate pure equipment
			spawn(p, 
					new Item(3105),//climbing boots
					new Item(4587),
					new Item(1215),
					new Item(5574),
					new Item(5575),
					new Item(5576),
					new Item(1725),
					new Item(19111),
					new Item(7459),//addy gloves
					new Item(3842)//unholy book
					);
			return true;
			
		case BUTTON_15://mystic magic equipment
			spawn(p, 
					new Item(3105),//climbing boots
					new Item(7459),//addy gloves
					new Item(3842),//unholy book
					new Item(4089),
					new Item(4091),
					new Item(4093),
					new Item(4675),
					new Item(19111),
					new Item(1731)
					);
			return true;
		case BUTTON_16://mystic melee equipment
			spawn(p, 
					new Item(3105),//climbing boots
					new Item(7459),//addy gloves
					new Item(3842),//unholy book
					new Item(4089),
					new Item(4091),
					new Item(4093),
					new Item(4587),
					new Item(19111),
					new Item(1731),
					new Item(1215)
					);
			return true;
		case BUTTON_17://mystic ranged equipment
			spawn(p, 
					new Item(3105),//climbing boots
					new Item(7459),//addy gloves
					new Item(4089),
					new Item(4091),
					new Item(4093),
					new Item(861),
					new Item(19111),
					new Item(1731),
					new Item(890, 400)
					);
			return true;
			
		case BUTTON_19://iron armour set
			spawn(p, 
					new Item(1153),
					new Item(1115),
					new Item(1067),
					new Item(1191)
					);
			return true;
		case BUTTON_20://initiate armour set
			spawn(p, 
					new Item(5574),
					new Item(5575),
					new Item(5576)
					);
			return true;
		case BUTTON_21://adamant armour set
			spawn(p, 
					new Item(1161),
					new Item(1123),
					new Item(1073),
					new Item(1199)
					);
			return true;
		case BUTTON_22://mystic armour set
			spawn(p, 
					new Item(4089),
					new Item(4091),
					new Item(4093),
					new Item(4095),
					new Item(4097)
					);
			return true;
		case BUTTON_23://black d'hide set
			spawn(p, 
					new Item(2503),
					new Item(2497),
					new Item(2491)
					);
			return true;
		case BUTTON_24://ghostly robes set
			spawn(p, 
					new Item(6106),
					new Item(6107),
					new Item(6108),
					new Item(6109),
					new Item(6110),
					new Item(6111)
					);
			return true;
		case BUTTON_26://melee weapons
			spawn(p, 
					new Item(4587),
					new Item(1215),
					new Item(4153),
					new Item(1333),
					new Item(1323),
					new Item(6528)
					);
			return true;
		case BUTTON_27://ranged weapons
			spawn(p, 
					new Item(861),
					new Item(810, 800),//addy darts
					new Item(867, 800),//addy knifes
					new Item(9185),
					new Item(9142, 800),
					new Item(6528)
					);
			return true;
		case BUTTON_28://magic weapons
			spawn(p, 
					new Item(4675),
					new Item(1387),
					new Item(1385),
					new Item(1383),
					new Item(1381)
					);
			return true;
		case BUTTON_29://ammo
			spawn(p, 
					new Item(9142, 2000),
					new Item(890, 2000),
					new Item(810, 2000),//addy darts
					new Item(867, 2000)//addy knifes
					);
			return true;
		case BUTTON_30://drag dagger
			spawn(p, 
					new Item(1215)
					);
			return true;
		case BUTTON_31://magic shortbow
			spawn(p, 
					new Item(861)
					);
			return true;
		case BUTTON_32://amulets
			spawn(p, 
					new Item(1725),
					new Item(1731)
					);
			return true;
		case BUTTON_33://Climbing boots
			spawn(p, 
					new Item(3105)
					);
			return true;
		case BUTTON_34://adamant darts
			spawn(p, 
					new Item(810, 2000)//addy darts
					);
			return true;
		case BUTTON_35://rune crossbow
			spawn(p, 
					new Item(9185)
					);
			return true;
		case BUTTON_36://ancient staff
			spawn(p, 
					new Item(4675)
					);
			return true;
		case BUTTON_37://granite maul
			spawn(p, 
					new Item(4153)
					);
			return true;
		case BUTTON_38://obby maul
			spawn(p, 
					new Item(6528)
					);
			return true;
		case BUTTON_39://adamant gloves
			spawn(p, 
					new Item(7459)
					);
			return true;
		case BUTTON_40://unholy book
			spawn(p, 
					new Item(3842)
					);
			return true;
		}
		
		return false;
	}
	
	public static void spawn(Player player, Item... items) {
		boolean error = false;
		
		for (Item i : items) {
			if (i == null) {
				continue;
			}
			
			/*if (i.getId() == 3842) {
				if (!player.getQuesting().isQuestCompleted(QuestConstants.HORROR_FROM_THE_DEEP)) {
					player.send(new SendMessage("You must complete Horror from the deep to spawn the Unholy Book."));
					i.setId(1540);
				}
			}
			
			if (i.getId() == 7459) {
				if (!player.getQuesting().isQuestCompleted(QuestConstants.RECIPE_FOR_DISASTER)) {
					player.send(new SendMessage("You must complete Recipe for Disaster to spawn Adamant Gloves."));
					continue;
				}
			}*/
			
			if (i.getId() == 19111) {
				if (!player.getInventory().hasItemId(19111) && !player.getBank().hasItemId(19111)) {
					player.send(new SendMessage("You must have already completed the Fight Kiln to use TokHaar-Kal."));
					
					if (player.getInventory().hasItemId(6570) || player.getBank().hasItemId(6570)) {
						i.setId(6570);
					} else {
						i.setId(1052);
					}
				}
			}
			
			if (spawnable.contains(i.getId())) {
				Item[] e = player.getEquipment().getItems();
				
				if (i.getEquipmentDefinition() != null
						&& e[i.getEquipmentDefinition().getSlot()] == null
						&& player.getEquipment().canEquip(i, i.getEquipmentDefinition().getSlot())
						&& (i.getWeaponDefinition() == null 
							|| !i.getWeaponDefinition().isTwoHanded() || e[EquipmentConstants.SHIELD_SLOT] == null)
						&& (i.getEquipmentDefinition().getSlot() != EquipmentConstants.SHIELD_SLOT 
							|| e[EquipmentConstants.WEAPON_SLOT] == null 
							|| !e[EquipmentConstants.WEAPON_SLOT].getWeaponDefinition().isTwoHanded())) {
					
					player.getEquipment().getItems()[i.getEquipmentDefinition().getSlot()] = new Item(i);
				} else {
					player.getInventory().add(i.getId(), i.getAmount(), false);
				}
			} else {
				error = true;
			}
		}
		
		if (error) {
			player.send(new SendMessage("One or more of those items could not be spawned."));
		}
		
		player.getInventory().update();
		player.getEquipment().onLogin();
		player.updateCombatType();
		player.setAppearanceUpdateRequired(true);
	}

}
