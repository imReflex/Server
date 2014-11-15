package org.endeavor.engine.definitions;

public class ItemDropDefinition {

	private short id;

	private ItemDropTable constant;

	private ItemDropTable common;

	private ItemDropTable uncommon;

	private ItemDropTable rare;

	private boolean useRareTable;

	public ItemDropDefinition() {
	}

	public int getId() {
		return id;
	}

	public ItemDropTable getConstant() {
		return constant;
	}

	public ItemDropTable getCommon() {
		return common;
	}

	public ItemDropTable getUncommon() {
		return uncommon;
	}

	public ItemDropTable getRare() {
		return rare;
	}

	public boolean isRareTable() {
		return useRareTable;
	}

	public static class ItemDropTable {

		private TableTypes type;

		private ScrollTypes[] scrolls;

		private ItemDrop[] charms;

		private ItemDrop[] drops;

		public ItemDropTable() {
		}

		public TableTypes getType() {
			return type;
		}

		public ScrollTypes[] getScrolls() {
			return scrolls;
		}

		public ItemDrop[] getCharms() {
			return charms;
		}

		public ItemDrop[] getDrops() {
			return drops;
		}

		public enum ScrollTypes {
			EASY, MEDIUM, HARD, ELITE
		}

		public enum TableTypes {
			COMMON, UNCOMMON, ALWAYS
		}
	}

	public static class ItemDrop {
		private short id;

		private int min;

		public int max;

		public ItemDrop() {

		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = (short) id;
		}

		public int getMin() {
			return min;
		}

		public int getMax() {
			return max;
		}

		/**
		 * @param id the id to set
		 */
		public void setId(short id) {
			this.id = id;
		}

		/**
		 * @param min the min to set
		 */
		public void setMin(short min) {
			this.min = min;
		}

		/**
		 * @param max the max to set
		 */
		public void setMax(short max) {
			this.max = max;
		}
		
	}
}
