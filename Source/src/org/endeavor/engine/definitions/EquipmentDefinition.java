package org.endeavor.engine.definitions;

public class EquipmentDefinition {

	private short id;
	private byte slot;
	private byte type;
	private short[] bonuses;
	private byte[] requirements;

	public EquipmentDefinition(short id, byte slot, byte type, short[] bonuses, byte[] requirements) {
		this.id = id;
		this.slot = slot;
		this.type = type;
		this.bonuses = bonuses;
		this.requirements = requirements;
	}
	
	public int getId() {
		return id;
	}

	public int getSlot() {
		return slot;
	}
	
	public int getType() {
		return type;
	}
	
	public short[] getBonuses() {
		return bonuses;
	}

	public byte[] getRequirements() {
		return requirements;
	}

	public void setRequirements(byte[] requirements) {
		this.requirements = requirements;
	}
}
