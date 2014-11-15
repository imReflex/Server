package org.endeavor.game.entity.item;

import java.io.Serializable;

import org.endeavor.engine.definitions.EquipmentDefinition;
import org.endeavor.engine.definitions.FoodDefinition;
import org.endeavor.engine.definitions.ItemBonusDefinition;
import org.endeavor.engine.definitions.ItemDefinition;
import org.endeavor.engine.definitions.PotionDefinition;
import org.endeavor.engine.definitions.RangedStrengthDefinition;
import org.endeavor.engine.definitions.RangedWeaponDefinition;
import org.endeavor.engine.definitions.SpecialAttackDefinition;
import org.endeavor.engine.definitions.WeaponDefinition;
import org.endeavor.engine.utility.GameDefinitionLoader;

public class Item implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6080395320330232626L;
	private short id;
	private int amount;

	public Item() {
	}

	public Item(int id, int amount) {
		this.id = ((short) id);
		this.amount = amount;
	}

	public Item(Item item) {
		id = ((short) item.getId());
		amount = item.getAmount();
	}

	public Item(int id) {
		this.id = ((short) id);
		amount = 1;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = ((short) id);
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void add(int amount) {
		this.amount += amount;
	}

	public void remove(int amount) {
		this.amount -= amount;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", amount=" + amount + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj instanceof Item)) {
			Item item = (Item) obj;
			return item.getId() == id;
		}
		return false;
	}

	public void note() {
		int noteId = getDefinition().getNoteId();

		if (noteId == -1) {
			return;
		}

		id = ((short) noteId);
	}

	public void unNote() {
		int noteId = getDefinition().getNoteId();

		if (noteId == -1) {
			return;
		}

		id = ((short) noteId);
	}

	public ItemDefinition getDefinition() {
		return GameDefinitionLoader.getItemDef(id);
	}

	public static ItemDefinition getDefinition(int id) {
		return GameDefinitionLoader.getItemDef(id);
	}

	public WeaponDefinition getWeaponDefinition() {
		return GameDefinitionLoader.getWeaponDefinition(id);
	}

	public static WeaponDefinition getWeaponDefinition(int id) {
		if ((id < 0) || (id > 20144)) {
			return null;
		}

		return GameDefinitionLoader.getWeaponDefinition(id);
	}

	public FoodDefinition getFoodDefinition() {
		return GameDefinitionLoader.getFoodDefinition(id);
	}

	public static FoodDefinition getFoodDefinition(int id) {
		if ((id < 0) || (id > 20144)) {
			return null;
		}

		return GameDefinitionLoader.getFoodDefinition(id);
	}

	public PotionDefinition getPotionDefinition() {
		return GameDefinitionLoader.getPotionDefinition(id);
	}

	public static PotionDefinition getPotionDefinition(int id) {
		if ((id < 0) || (id > 20144)) {
			return null;
		}

		return GameDefinitionLoader.getPotionDefinition(id);
	}

	public EquipmentDefinition getEquipmentDefinition() {
		return GameDefinitionLoader.getEquipmentDefinition(id);
	}

	public static EquipmentDefinition getEquipmentDefinition(int id) {
		return GameDefinitionLoader.getEquipmentDefinition(id);
	}

	public byte[][] getItemRequirements() {
		return GameDefinitionLoader.getItemRequirements(id);
	}

	public short[] getItemBonuses() {
		//ItemBonusDefinition def = GameDefinitionLoader.getItemBonusDefinition(id);
		EquipmentDefinition def = GameDefinitionLoader.getEquipmentDefinition(id);
		if (def != null) {
			return def.getBonuses();
		}
		return null;
	}

	public static short[] getItemBonuses(int id) {
		//ItemBonusDefinition def = GameDefinitionLoader.getItemBonusDefinition(id);
		EquipmentDefinition def = GameDefinitionLoader.getEquipmentDefinition(id);
		if (def != null) {
			return def.getBonuses();
		}
		return null;
	}

	public RangedWeaponDefinition getRangedDefinition() {
		return GameDefinitionLoader.getRangedWeaponDefinition(id);
	}

	public static RangedWeaponDefinition getRangedDefinition(int id) {
		return GameDefinitionLoader.getRangedWeaponDefinition(id);
	}

	public SpecialAttackDefinition getSpecialDefinition() {
		return GameDefinitionLoader.getSpecialDefinition(id);
	}

	public static SpecialAttackDefinition getSpecialDefinition(int id) {
		return GameDefinitionLoader.getSpecialDefinition(id);
	}

	public int getRangedStrengthBonus() {
		RangedStrengthDefinition def = GameDefinitionLoader.getRangedStrengthDefinition(id);
		return def == null ? 0 : def.getBonus();
	}

	public static int getRangedStrengthBonus(int id) {
		RangedStrengthDefinition def = GameDefinitionLoader.getRangedStrengthDefinition(id);
		return def == null ? 0 : def.getBonus();
	}
}
