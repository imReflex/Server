package org.endeavor.engine.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;

import org.endeavor.engine.cache.map.Region;
import org.endeavor.engine.cache.map.Tile;
import org.endeavor.engine.definitions.CombatSpellDefinition;
import org.endeavor.engine.definitions.EquipmentDefinition;
import org.endeavor.engine.definitions.FoodDefinition;
import org.endeavor.engine.definitions.ItemBonusDefinition;
import org.endeavor.engine.definitions.ItemDefinition;
import org.endeavor.engine.definitions.ItemDropDefinition;
import org.endeavor.engine.definitions.ItemDropDefinition.ItemDrop;
import org.endeavor.engine.definitions.NpcCombatDefinition;
import org.endeavor.engine.definitions.NpcDefinition;
import org.endeavor.engine.definitions.NpcSpawnDefinition;
import org.endeavor.engine.definitions.PotionDefinition;
import org.endeavor.engine.definitions.RangedStrengthDefinition;
import org.endeavor.engine.definitions.RangedWeaponDefinition;
import org.endeavor.engine.definitions.ShopDefinition;
import org.endeavor.engine.definitions.SpecialAttackDefinition;
import org.endeavor.engine.definitions.WeaponDefinition;
import org.endeavor.game.GameConstants;
import org.endeavor.game.content.combat.impl.PlayerDrops;
import org.endeavor.game.content.combat.spawning.Spawn;
import org.endeavor.game.content.shopping.Shop;
import org.endeavor.game.content.skill.SkillConstants;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.mob.Walking;
import org.endeavor.game.entity.pathfinding.RS317PathFinder;
import org.endeavor.game.entity.player.PlayerConstants;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider;

public class GameDefinitionLoader {

	private static XStream xStream = new XStream(new Sun14ReflectionProvider());

	private static int[][] alternates = new int[53000][1];

	private static int[][] itemValues = new int[PlayerConstants.MAX_ITEM_COUNT][];
	private static Map<Integer, Byte> rareDropChances = new HashMap<Integer, Byte>();

	private static Map<Integer, byte[][]> itemRequirements = new HashMap<Integer, byte[][]>();
	private static Map<Integer, ItemDefinition> itemDefinitions = new HashMap<Integer, ItemDefinition>();
	private static Map<Integer, NpcDefinition> npcDefinitions = new HashMap<Integer, NpcDefinition>();

	private static Map<Integer, SpecialAttackDefinition> specialAttackDefinitions = new HashMap<Integer, SpecialAttackDefinition>();
	private static Map<Integer, RangedWeaponDefinition> rangedWeaponDefinitions = new HashMap<Integer, RangedWeaponDefinition>();
	private static Map<Integer, WeaponDefinition> weaponDefinitions = new HashMap<Integer, WeaponDefinition>();
	private static Map<Integer, FoodDefinition> foodDefinitions = new HashMap<Integer, FoodDefinition>();
	private static Map<Integer, PotionDefinition> potionDefinitions = new HashMap<Integer, PotionDefinition>();
	private static Map<Integer, EquipmentDefinition> equipmentDefinitions = new HashMap<Integer, EquipmentDefinition>();
	private static Map<Integer, ItemBonusDefinition> itemBonusDefinitions = new HashMap<Integer, ItemBonusDefinition>();
	private static Map<Integer, CombatSpellDefinition> combatSpellDefinitions = new HashMap<Integer, CombatSpellDefinition>();
	private static Map<Integer, NpcCombatDefinition> npcCombatDefinitions = new HashMap<Integer, NpcCombatDefinition>();
	private static Map<Integer, RangedStrengthDefinition> rangedStrengthDefinitions = new HashMap<Integer, RangedStrengthDefinition>();

	private static Map<Integer, ItemDropDefinition> mobDropDefinitions = new HashMap<Integer, ItemDropDefinition>();

	private GameDefinitionLoader() {
	}

	public static final void declare() {
		// mob drop defs
		xStream.alias("ItemDropDefinition", org.endeavor.engine.definitions.ItemDropDefinition.class);
		xStream.alias("constant", org.endeavor.engine.definitions.ItemDropDefinition.ItemDropTable.class);
		xStream.alias("common", org.endeavor.engine.definitions.ItemDropDefinition.ItemDropTable.class);
		xStream.alias("uncommon", org.endeavor.engine.definitions.ItemDropDefinition.ItemDropTable.class);
		xStream.alias("itemDrop", org.endeavor.engine.definitions.ItemDropDefinition.ItemDrop.class);
		xStream.alias("scroll", org.endeavor.engine.definitions.ItemDropDefinition.ItemDropTable.ScrollTypes.class);

		xStream.alias("location", org.endeavor.game.entity.Location.class);
		xStream.alias("item", org.endeavor.game.entity.item.Item.class);
		xStream.alias("projectile", org.endeavor.game.entity.Projectile.class);
		xStream.alias("graphic", org.endeavor.game.entity.Graphic.class);
		xStream.alias("animation", org.endeavor.game.entity.Animation.class);

		xStream.alias("NpcCombatDefinition", org.endeavor.engine.definitions.NpcCombatDefinition.class);
		xStream.alias("skill", org.endeavor.engine.definitions.NpcCombatDefinition.Skill.class);
		xStream.alias("melee", org.endeavor.engine.definitions.NpcCombatDefinition.Melee.class);
		xStream.alias("magic", org.endeavor.engine.definitions.NpcCombatDefinition.Magic.class);
		xStream.alias("ranged", org.endeavor.engine.definitions.NpcCombatDefinition.Ranged.class);

		xStream.alias("ItemDefinition", org.endeavor.engine.definitions.ItemDefinition.class);
		xStream.alias("ShopDefinition", org.endeavor.engine.definitions.ShopDefinition.class);
		xStream.alias("WeaponDefinition", org.endeavor.engine.definitions.WeaponDefinition.class);
		xStream.alias("SpecialAttackDefinition", org.endeavor.engine.definitions.SpecialAttackDefinition.class);
		xStream.alias("RangedWeaponDefinition", org.endeavor.engine.definitions.RangedWeaponDefinition.class);
		xStream.alias("RangedStrengthDefinition", org.endeavor.engine.definitions.RangedStrengthDefinition.class);
		xStream.alias("FoodDefinition", org.endeavor.engine.definitions.FoodDefinition.class);
		xStream.alias("PotionDefinition", org.endeavor.engine.definitions.PotionDefinition.class);
		xStream.alias("skillData", org.endeavor.engine.definitions.PotionDefinition.SkillData.class);
		xStream.alias("ItemBonusDefinition", org.endeavor.engine.definitions.ItemBonusDefinition.class);
		xStream.alias("CombatSpellDefinition", org.endeavor.engine.definitions.CombatSpellDefinition.class);
		xStream.alias("NpcDefinition", org.endeavor.engine.definitions.NpcDefinition.class);
		xStream.alias("NpcSpawnDefinition", org.endeavor.engine.definitions.NpcSpawnDefinition.class);
		//xStream.alias("EquipmentDefinition", org.endeavor.engine.definitions.EquipmentDefinition.class);
	}

	public static final void loadRareDropChances() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("./data/def/npcs/DropChances.txt"));

			String line = null;

			while ((line = reader.readLine()) != null) {
				if (line.length() == 0 || line.startsWith("//")) {
					continue;
				}
				
				try {
					int id = Integer.parseInt(line.substring(0, line.indexOf(":")));
					line = line.substring(line.indexOf(":") + 1);
					int value = Integer.parseInt(line.substring(0, line.indexOf("/")));
					
					rareDropChances.put(id, (byte) value);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			reader.close();
			
			//remove me
			/*new File("./data/def/npcs/DropChances.txt").delete();
			BufferedWriter writer = new BufferedWriter(new FileWriter("./data/def/npcs/DropChances.txt"));
			for (Entry<Integer, Byte> i : rareDropChances.entrySet()) {
				writer.write(i.getKey() + ":" + i.getValue() + "//" + itemDefinitions.get(i.getKey()).getName());
				writer.newLine();
			}
			writer.close();*/
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Loaded rare drop chances.");
	}

	public static final void loadAlternateIds() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("./data/def/ObjectAlternates.txt"));

			String line = null;

			while ((line = reader.readLine()) != null) {
				int id = Integer.parseInt(line.substring(0, line.indexOf(":")));
				line = line.substring(line.indexOf(":") + 1);
				int alt = Integer.parseInt(line.substring(0, line.length()));

				alternates[id][0] = alt;
			}

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Alternate door ids loaded.");
	}

	public static final void clearAlternates() {
		alternates = null;
	}

	public static final void loadItemValues() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("./data/def/items/ItemValues.txt"));

			String line = null;

			while ((line = reader.readLine()) != null) {
				int id = Integer.parseInt(line.substring(0, line.indexOf(":")));
				line = line.substring(line.indexOf(":") + 1);
				int a = Integer.parseInt(line.substring(0, line.indexOf(":")));
				line = line.substring(line.indexOf(":") + 1);
				int b = Integer.parseInt(line.substring(0, line.indexOf(":")));
				line = line.substring(line.indexOf(":") + 1);
				int c = Integer.parseInt(line.substring(0, line.indexOf(":")));
				line = line.substring(line.indexOf(":") + 1);
				int d = Integer.parseInt(line.substring(0, line.length()));

				if (a > 0 || b > 0 || c > 0 || d > 0) {
					itemValues[id] = new int[] { a, b, c, d };
				}
			}

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void writeDropPreference() {
		try {
			Queue<Item> items = new PriorityQueue<Item>(42, PlayerDrops.ITEM_VALUE_COMPARATOR);
			
			for (ItemDefinition i : itemDefinitions.values()) {
				if (!i.isTradable() || i.getNoteId() != -1 && items.contains(new Item(i.getNoteId()))) {
					continue;
				}
				
				items.add(new Item(i.getId()));
			}
			
			BufferedWriter writer = new BufferedWriter(new FileWriter("./DropSettings.txt"));
			
			Item item = null;
			
			while ((item = items.poll()) != null) {
				writer.write(item.getId() + ":" + item.getDefinition().getName());
				writer.newLine();
			}
			
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static void loadItemDefinitions() throws FileNotFoundException, IOException {
		List<ItemDefinition> list = (List<ItemDefinition>) xStream.fromXML(new FileInputStream(
				"./data/def/items/ItemDefinitions.xml"));
		for (ItemDefinition definition : list) {
			itemDefinitions.put(definition.getId(), definition);
		}

		System.out.println("Loaded " + list.size() + " item definitions.");
	}

	@SuppressWarnings("unchecked")
	public static void loadWeaponDefinitions() throws FileNotFoundException, IOException {
		List<WeaponDefinition> list = (List<WeaponDefinition>) xStream.fromXML(new FileInputStream(
				"./data/def/items/WeaponDefinitions.xml"));
		for (WeaponDefinition definition : list) {
			weaponDefinitions.put(definition.getId(), definition);
		}
		System.out.println("Loaded " + list.size() + " weapon definitions.");
	}

	@SuppressWarnings("unchecked")
	public static void loadSpecialAttackDefinitions() throws FileNotFoundException, IOException {
		List<SpecialAttackDefinition> list = (List<SpecialAttackDefinition>) xStream.fromXML(new FileInputStream(
				"./data/def/items/SpecialAttackDefinitions.xml"));
		for (SpecialAttackDefinition definition : list) {
			specialAttackDefinitions.put(definition.getId(), definition);
		}
		System.out.println("Loaded " + list.size() + " special attack definitions.");
	}

	@SuppressWarnings("unchecked")
	public static void loadRangedWeaponDefinitions() throws FileNotFoundException, IOException {
		List<RangedWeaponDefinition> list = (List<RangedWeaponDefinition>) xStream.fromXML(new FileInputStream(
				"./data/def/items/RangedWeaponDefinitions.xml"));
		for (RangedWeaponDefinition definition : list) {
			rangedWeaponDefinitions.put(definition.getId(), definition);
		}
		System.out.println("Loaded " + list.size() + " ranged weapon definitions.");
	}

	@SuppressWarnings("unchecked")
	public static void loadFoodDefinitions() throws FileNotFoundException, IOException {
		List<FoodDefinition> list = (List<FoodDefinition>) xStream.fromXML(new FileInputStream(
				"./data/def/items/FoodDefinitions.xml"));
		for (FoodDefinition definition : list) {
			foodDefinitions.put(definition.getId(), definition);
		}
		System.out.println("Loaded " + list.size() + " food definitions.");
	}

	@SuppressWarnings("unchecked")
	public static void loadPotionDefinitions() throws FileNotFoundException, IOException {
		List<PotionDefinition> list = (List<PotionDefinition>) xStream.fromXML(new FileInputStream(
				"./data/def/items/PotionDefinitions.xml"));
		for (PotionDefinition definition : list) {
			if (definition.getName() == null) {
				definition.setName(itemDefinitions.get(definition.getId()).getName());
			}
			potionDefinitions.put(definition.getId(), definition);
		}
		System.out.println("Loaded " + list.size() + " potion definitions.");
	}

	@SuppressWarnings("unchecked")
	public static void loadCombatSpellDefinitions() throws FileNotFoundException, IOException {
		List<CombatSpellDefinition> list = (List<CombatSpellDefinition>) xStream.fromXML(new FileInputStream(
				"./data/def/magic/CombatSpellDefinitions.xml"));
		for (CombatSpellDefinition definition : list) {
			combatSpellDefinitions.put(definition.getId(), definition);
		}
		System.out.println("Loaded " + list.size() + " combat spell definitions.");
	}

	@SuppressWarnings("unchecked")
	public static void loadItemBonusDefinitions() throws FileNotFoundException, IOException {
		List<ItemBonusDefinition> list = (List<ItemBonusDefinition>) xStream.fromXML(new FileInputStream(
				"./data/def/items/ItemBonusDefinitions.xml"));
		for (ItemBonusDefinition definition : list) {
			itemBonusDefinitions.put(definition.getId(), definition);
		}
		System.out.println("Loaded " + list.size() + " item bonus definitions.");
	}

	@SuppressWarnings("unchecked")
	public static void loadRangedStrengthDefinitions() throws FileNotFoundException, IOException {
		List<RangedStrengthDefinition> list = (List<RangedStrengthDefinition>) xStream.fromXML(new FileInputStream(
				"./data/def/items/RangedStrengthDefinitions.xml"));
		for (RangedStrengthDefinition definition : list) {
			rangedStrengthDefinitions.put(definition.getId(), definition);
		}
		System.out.println("Loaded " + list.size() + " ranged strength bonus definitions.");
	}

	@SuppressWarnings("unchecked")
	public static void loadNpcDefinitions() throws FileNotFoundException, IOException {
		List<NpcDefinition> list = (List<NpcDefinition>) xStream.fromXML(new FileInputStream(
				"./data/def/npcs/NpcDefinitions.xml"));
		for (NpcDefinition definition : list) {
			npcDefinitions.put(definition.getId(), definition);
		}
		System.out.println("Loaded " + list.size() + " npc definitions.");
		// dumpSizes();
	}

	public static void dumpSizes() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("./NPCSizes.txt"));

			for (NpcDefinition i : npcDefinitions.values()) {
				if (i != null) {
					writer.write(i.getId() + ":" + i.getSize());
					writer.newLine();
				}
			}

			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Sizes dumped.");
	}

	@SuppressWarnings("unchecked")
	public static void loadNpcCombatDefinitions() throws FileNotFoundException, IOException {
		List<NpcCombatDefinition> list = (List<NpcCombatDefinition>) xStream.fromXML(new FileInputStream(
				"./data/def/npcs/NpcCombatDefinitions.xml"));
		for (NpcCombatDefinition definition : list) {
			npcCombatDefinitions.put(definition.getId(), definition);
		}

		System.out.println("Loaded " + list.size() + " npc combat definitions.");
	}

	@SuppressWarnings("unchecked")
	public static void loadNpcDropDefinitions() throws FileNotFoundException, IOException {
		List<ItemDropDefinition> list = (List<ItemDropDefinition>) xStream.fromXML(new FileInputStream(
				"./data/def/npcs/ItemDropDefinitions.xml"));
		for (ItemDropDefinition def : list) {
			mobDropDefinitions.put(def.getId(), def);
			
			if (def.getCommon() != null && def.getCommon().getDrops() != null) {
				for (ItemDrop i : def.getCommon().getDrops()) {
					if (i.getId() != 995) {
						if (i.getMax() >= 600 || i.getMin() >= 600) {
							i.setMin((short) 100);
							i.setMax((short) 600);
						}
					}
				}
			}
		}

		for (ItemDropDefinition i : mobDropDefinitions.values()) {
			if (npcCombatDefinitions.get(i.getId()) == null) {
				mobDropDefinitions.remove(i);
			}
		}

		System.out.println("Loaded " + list.size() + " npc drops.");
	}

	@SuppressWarnings("unchecked")
	public static void loadNpcSpawns() throws FileNotFoundException, IOException {
		List<NpcSpawnDefinition> list = (List<NpcSpawnDefinition>) xStream.fromXML(new FileInputStream(
				"./data/def/npcs/NpcSpawnDefinitions.xml"));
		for (NpcSpawnDefinition def : list) {
			if (Region.getRegion(def.getLocation().getX(), def.getLocation().getY()) == null) {
				continue;
			}

			if (npcDefinitions.get(def.getId()).isAttackable() && npcCombatDefinitions.get(def.getId()) == null) {
				continue;
			}
			Mob m = new Mob(def.getId(), npcDefinitions.get(def.getId()).getName().equals("Fishing spot") ? false
					: def.isWalk(), def.getLocation());
			
			if (def.getFace() > 0) {
				m.setFaceDir(def.getFace());
			} else {
				m.setFaceDir(-1);
			}
		}
		System.out.println("Loaded " + list.size() + " npc spawns.");
	}

	@SuppressWarnings("unchecked")
	public static void loadShopDefinitions() throws FileNotFoundException, IOException {
		List<ShopDefinition> list = (List<ShopDefinition>) xStream.fromXML(new FileInputStream(
				"./data/def/items/ShopDefinitions.xml"));
		for (ShopDefinition def : list) {
			Shop.getShops()[def.getId()] = new Shop(def.getId(), def.getItems(), def.isGeneral(), def.getName());
		}
		System.out.println("Loaded " + list.size() + " shops.");
	}

	@SuppressWarnings("unchecked")
	public static void loadEquipmentDefinitions() throws FileNotFoundException, IOException {
		/*List<EquipmentDefinition> list = (List<EquipmentDefinition>) xStream.fromXML(new FileInputStream("./data/def/items/EquipmentDefinitions.xml"));
		for (EquipmentDefinition definition : list) {
			equipmentDefinitions.put(definition.getId(), definition);
		}
		System.out.println("Loaded " + list.size() + " equipment definitions.");*/
		DataInputStream in = new DataInputStream(new FileInputStream(new File("./data/def/items/items.dat")));
		int amount = in.readInt();
		for(int i = 0; i < amount; i++) {
			short id = (short)in.readInt();
			byte equipSlot = (byte)in.readInt();
			byte equipType = (byte)in.readInt();
			short[] bonuses = new short[18];
			for(int e = 0; e <= 17; e++) {
				bonuses[e] = (short)in.readInt();
			}
			short[] correctBonuses = new short[18];
			for(int in1 = 0; in1 <= 9; in1++)
				correctBonuses[in1] = bonuses[in1];
			correctBonuses[13] = bonuses[10];
			for(int in2 = 10; in2 <= 12; in2++)
				correctBonuses[in2] = bonuses[in2++];
			for(int in3 = 14; in3 <= 17; in3++)
				correctBonuses[in3] = bonuses[in3];
			
			byte[] requirements = new byte[25];
			for(int s = 0; s <= 24; s++) {
				requirements[s] = (byte)in.readInt();
			}
			EquipmentDefinition definition = new EquipmentDefinition(id, equipSlot, equipType, correctBonuses, requirements);
			equipmentDefinitions.put(definition.getId(), definition);
		}
	}

	public static void setRequirements() {
		for (Object def : equipmentDefinitions.values().toArray()) {
			EquipmentDefinition definition = (EquipmentDefinition) def;

			if (definition == null || definition.getRequirements() == null) {
				continue;
			}

			byte[][] requirements = new byte[SkillConstants.SKILL_COUNT][2];
			int count = 0;

			for (int i = 0; i < definition.getRequirements().length; i++) {
				if (definition.getRequirements()[i] == 1) {
					continue;
				} else {
					requirements[count][0] = (byte) i;
					requirements[count][1] = definition.getRequirements()[i];
					count++;
				}
			}

			byte[][] set = new byte[count][2];

			for (int i = 0; i < count; i++) {
				set[i][0] = requirements[i][0];
				set[i][1] = requirements[i][1];
			}

			itemRequirements.put(((EquipmentDefinition) def).getId(), set);

			((EquipmentDefinition) def).setRequirements(null);
		}
	}

	public static XStream getxStream() {
		return xStream;
	}

	public static byte[][] getItemRequirements(int id) {
		return itemRequirements.get(id);
	}

	public static ItemDefinition getItemDef(int i) {
		return itemDefinitions.get(i);
	}

	public static SpecialAttackDefinition getSpecialDefinition(int id) {
		return specialAttackDefinitions.get(id);
	}

	public static RangedWeaponDefinition getRangedWeaponDefinition(int id) {
		return rangedWeaponDefinitions.get(id);
	}

	public static WeaponDefinition getWeaponDefinition(int id) {
		return weaponDefinitions.get(id);
	}

	public static FoodDefinition getFoodDefinition(int id) {
		return foodDefinitions.get(id);
	}

	public static PotionDefinition getPotionDefinition(int id) {
		return potionDefinitions.get(id);
	}

	public static EquipmentDefinition getEquipmentDefinition(int id) {
		return equipmentDefinitions.get(id);
	}

	public static CombatSpellDefinition getCombatSpellDefinition(int id) {
		return combatSpellDefinitions.get(id);
	}

	public static NpcDefinition getNpcDefinition(int id) {
		return npcDefinitions.get(id);
	}

	public static NpcCombatDefinition getNpcCombatDefinition(int id) {
		return npcCombatDefinitions.get(id);
	}

	public static RangedStrengthDefinition getRangedStrengthDefinition(int id) {
		return rangedStrengthDefinitions.get(id);
	}

	public static ItemBonusDefinition getItemBonusDefinition(int i) {
		return itemBonusDefinitions.get(i);
	}

	public static ItemDropDefinition getItemDropDefinition(int id) {
		return mobDropDefinitions.get(id);
	}

	public static int getHighAlchemyValue(int id) {
		if (itemValues[id] == null || itemValues[id][1] == 0) {
			if (Spawn.spawnable(id)) {
				return 0;
			}
			
			return itemDefinitions.get(id).getValue() / 8;
		}
		
		if (itemValues[id][1] == -1 
				|| itemDefinitions.get(id).isNote() && itemValues[itemDefinitions.get(id).getNoteId()][1] == -1) {
			return 0;
		}

		return itemValues[id][1];
	}

	public static int getLowAlchemyValue(int id) {
		
		if (itemValues[id] == null || itemValues[id][0] == 0) {
			if (Spawn.spawnable(id)) {
				return 0;
			}
			
			return itemDefinitions.get(id).getValue() / 3;
		}
		
		if (itemValues[id][0] == -1 
				|| itemDefinitions.get(id).isNote() && itemValues[itemDefinitions.get(id).getNoteId()][0] == -1) {
			return 0;
		}

		return itemValues[id][0];
	}

	public static int getStoreBuyFromValue(int id) {
		
		if (itemValues[id] == null || itemValues[id][2] == 0) {
			
			if (Spawn.spawnable(id)) {
				return 0;
			}
			
			if (getHighAlchemyValue(id) > itemDefinitions.get(id).getValue() * 2) {
				return (int)(getHighAlchemyValue(id) * 0.75D);
			}

			return itemDefinitions.get(id).getValue() * 2;
		}
		
		if (itemValues[id][2] == -1 
				|| itemDefinitions.get(id).isNote() && itemValues[itemDefinitions.get(id).getNoteId()][2] == -1) {
			return 0;
		}
		

		if (itemValues[id][2] < itemValues[id][3]) {
			if ((itemValues[id] == null) || (itemValues[id][3] == 0)) {
				return itemDefinitions.get(id).getValue() * 1;
			}

			return itemValues[id][3];
		}

		if (itemDefinitions.get(id).isNote()) {
			if (itemValues[id] == null || itemValues[id][2] == 0) {
				return itemDefinitions.get(itemDefinitions.get(id).getNoteId()).getValue() * 4;
			}

			if (getHighAlchemyValue(id) > itemValues[itemDefinitions.get(id).getNoteId()][2]) {
				return (int)(getHighAlchemyValue(id) * 0.75D);
			}

			return itemValues[itemDefinitions.get(id).getNoteId()][2];
		}

		if (getHighAlchemyValue(id) > itemValues[id][2]) {
			return (int)(getHighAlchemyValue(id) * 1.02D);
		}

		return itemValues[id][2];
	}

	public static int getStoreSellToValue(int id) {
		
			
		if ((itemValues[id] == null) || (itemValues[id][3] == 0)) {
			if (Spawn.spawnable(id)) {
				return 0;
			}
			
			return itemDefinitions.get(id).getValue() * 1;
		}
		
		if (itemValues[id][3] == -1 
				|| itemDefinitions.get(id).isNote() && itemValues[itemDefinitions.get(id).getNoteId()][3] == -1) {
			return 0;
		}
		

		if (itemValues[id][3] > itemValues[id][2]) {
			if ((itemValues[id] == null) || (itemValues[id][2] == 0)) {
				return itemDefinitions.get(id).getValue() * 2;
			}

			return itemValues[id][2];
		}

		if (itemDefinitions.get(id).isNote()) {
			if (itemValues[itemDefinitions.get(id).getNoteId()][3] == 0) {
				return itemDefinitions.get(id).getValue() * 5;
			}

			return itemValues[itemDefinitions.get(id).getNoteId()][3];
		}

		return itemValues[id][3];
	}

	public static void setValue(int id, int storeBuyFromValue, int storeSellToValue) {
		if (itemValues[id] == null) {
			itemValues[id] = new int[4];
		}

		itemValues[id][2] = storeBuyFromValue;
		itemValues[id][3] = storeSellToValue;
	}
	
	public static void setAlchValue(int id, int low, int high) {
		if (itemValues[id] == null) {
			itemValues[id] = new int[4];
		}

		itemValues[id][0] = low;
		itemValues[id][1] = high;
	}
	
	public static void setValueToZero(int id) {
		itemValues[id] = new int[] {-1, -1, -1, -1};
	}

	public static void setNotTradable(int id) {
		itemDefinitions.get(id).setUntradable();
	}

	public static int getAlternate(int id) {
		return alternates[id][0];
	}

	public static byte getRareDropChance(int id) {
		if (!rareDropChances.containsKey(id)) {
			return 50;
		}

		return rareDropChances.get(id).byteValue();
	}
}
