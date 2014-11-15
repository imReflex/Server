package org.endeavor.engine.definitions;

public class PotionDefinition {

	private short id;
	private String name;
	private short replaceId;
	private PotionTypes potionType;
	private SkillData[] skillData;

	public int getId() {
		return id;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getReplaceId() {
		return replaceId;
	}

	public SkillData[] getSkillData() {
		return skillData;
	}

	public PotionTypes getPotionType() {
		return potionType;
	}

	public enum PotionTypes {

		NORMAL, RESTORE, ANTIFIRE, SUPER_ANTIFIRE
	}

	public class SkillData {

		private byte skillId;
		private byte add;
		private double modifier;

		public int getSkillId() {
			return skillId;
		}

		public int getAdd() {
			return add;
		}

		public double getModifier() {
			return modifier;
		}
	}
}
