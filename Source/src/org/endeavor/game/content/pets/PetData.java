package org.endeavor.game.content.pets;

import java.util.HashMap;
import java.util.Map;

import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.game.content.pets.PetData.PetStage;

public enum PetData {
	BLACK_AND_GREY_CAT(new PetStage[] { new PetStage(1555, 761, PetConstants.KITTEN_CHAT, 0, PetTypeData.CAT,
			new PetStage(1561, 768, PetConstants.CAT_CHAT, 1, PetTypeData.CAT, new PetStage(1567, 774,
					PetConstants.CAT_CHAT, 2, PetTypeData.CAT, null))) }),

	GREY_CAT(new PetStage[] { new PetStage(1556, 762, PetConstants.KITTEN_CHAT, 0, PetTypeData.CAT, new PetStage(1562,
			769, PetConstants.CAT_CHAT, 1, PetTypeData.CAT, new PetStage(1568, 775, PetConstants.CAT_CHAT, 2,
					PetTypeData.CAT, null))) }),

	BROWN_AND_LIGHT_BROWN_CAT(new PetStage[] { new PetStage(1557, 763, PetConstants.KITTEN_CHAT, 0, PetTypeData.CAT,
			new PetStage(1563, 770, PetConstants.CAT_CHAT, 1, PetTypeData.CAT, new PetStage(1569, 776,
					PetConstants.CAT_CHAT, 2, PetTypeData.CAT, null))) }),

	BLACK_CAT(new PetStage[] { new PetStage(1558, 764, PetConstants.KITTEN_CHAT, 0, PetTypeData.CAT, new PetStage(1564,
			771, PetConstants.CAT_CHAT, 1, PetTypeData.CAT, new PetStage(1570, 777, PetConstants.CAT_CHAT, 2,
					PetTypeData.CAT, null))) }),

	BROWN_AND_GREY_CAT(new PetStage[] { new PetStage(1559, 765, PetConstants.KITTEN_CHAT, 0, PetTypeData.CAT,
			new PetStage(1565, 772, PetConstants.CAT_CHAT, 1, PetTypeData.CAT, new PetStage(1571, 778,
					PetConstants.CAT_CHAT, 2, PetTypeData.CAT, null))) }),

	BLUE_AND_GREY_CAT(new PetStage[] { new PetStage(1560, 766, PetConstants.KITTEN_CHAT, 0, PetTypeData.CAT,
			new PetStage(1566, 773, PetConstants.CAT_CHAT, 1, PetTypeData.CAT, new PetStage(1572, 779,
					PetConstants.CAT_CHAT, 2, PetTypeData.CAT, null))) }),

	TERRIER_LIGHT_BROWN(new PetStage[] { new PetStage(12512, 6958, PetConstants.DOG_CHAT, 0, PetTypeData.DOG,
			new PetStage(12513, 6959, PetConstants.DOG_CHAT, 0, PetTypeData.DOG, null)) }),

	TERRIER_WHITE(new PetStage[] { new PetStage(12700, 7237, PetConstants.DOG_CHAT, 0, PetTypeData.DOG, new PetStage(
			12701, 7238, PetConstants.DOG_CHAT, 0, PetTypeData.DOG, null)) }),

	TERRIER_GREY(new PetStage[] { new PetStage(12702, 7239, PetConstants.DOG_CHAT, 0, PetTypeData.DOG, new PetStage(
			12703, 7240, PetConstants.DOG_CHAT, 0, PetTypeData.DOG, null)) }),

	GREYHOUND_BROWN(new PetStage[] { new PetStage(12514, 6960, PetConstants.DOG_CHAT, 0, PetTypeData.DOG, new PetStage(
			12515, 6961, PetConstants.DOG_CHAT, 0, PetTypeData.DOG, null)) }),

	GREYHOUND_TAN(new PetStage[] { new PetStage(12704, 7241, PetConstants.DOG_CHAT, 0, PetTypeData.DOG, new PetStage(
			12705, 7242, PetConstants.DOG_CHAT, 0, PetTypeData.DOG, null)) }),

	GREYHOUND_DARK_BROWN(new PetStage[] { new PetStage(12706, 7243, PetConstants.DOG_CHAT, 0, PetTypeData.DOG,
			new PetStage(12707, 7244, PetConstants.DOG_CHAT, 0, PetTypeData.DOG, null)) }),

	LAB_WHITE(new PetStage[] { new PetStage(12516, 6962, PetConstants.DOG_CHAT, 0, PetTypeData.DOG, new PetStage(12517,
			6963, PetConstants.DOG_CHAT, 0, PetTypeData.DOG, null)) }),

	LAB_LIGHT_BLUE(new PetStage[] { new PetStage(12708, 7245, PetConstants.DOG_CHAT, 0, PetTypeData.DOG, new PetStage(
			12709, 7246, PetConstants.DOG_CHAT, 0, PetTypeData.DOG, null)) }),

	LAB_LIGHT_BROWN(new PetStage[] { new PetStage(12710, 7247, PetConstants.DOG_CHAT, 0, PetTypeData.DOG, new PetStage(
			12711, 7248, PetConstants.DOG_CHAT, 0, PetTypeData.DOG, null)) }),

	DALMATIAN_A(new PetStage[] { new PetStage(12518, 6964, PetConstants.DOG_CHAT, 0, PetTypeData.DOG, new PetStage(
			12519, 6965, PetConstants.DOG_CHAT, 0, PetTypeData.DOG, null)) }),

	DALMATIAN_B(new PetStage[] { new PetStage(12712, 7249, PetConstants.DOG_CHAT, 0, PetTypeData.DOG, new PetStage(
			12713, 7250, PetConstants.DOG_CHAT, 0, PetTypeData.DOG, null)) }),

	DALMATIAN_C(new PetStage[] { new PetStage(12714, 7251, PetConstants.DOG_CHAT, 0, PetTypeData.DOG, new PetStage(
			12715, 7252, PetConstants.DOG_CHAT, 0, PetTypeData.DOG, null)) }),

	SHEEPDOG_WHITE(new PetStage[] { new PetStage(12520, 6966, PetConstants.DOG_CHAT, 0, PetTypeData.DOG, new PetStage(
			12521, 6967, PetConstants.DOG_CHAT, 0, PetTypeData.DOG, null)) }),

	SHEEPDOG_TAN(new PetStage[] { new PetStage(12718, 7255, PetConstants.DOG_CHAT, 0, PetTypeData.DOG, new PetStage(
			12719, 7256, PetConstants.DOG_CHAT, 0, PetTypeData.DOG, null)) }),

	BULLDOG_WHITE(new PetStage[] { new PetStage(12522, 6968, PetConstants.DOG_CHAT, 0, PetTypeData.DOG, new PetStage(
			12523, 6969, PetConstants.DOG_CHAT, 0, PetTypeData.DOG, null)) }),

	BULLDOG_BLUE(new PetStage[] { new PetStage(12720, 7257, PetConstants.DOG_CHAT, 0, PetTypeData.DOG, new PetStage(
			12721, 7258, PetConstants.DOG_CHAT, 0, PetTypeData.DOG, null)) }),

	RAVEN(new PetStage[] { new PetStage(12726, 7263, PetConstants.BIRD_CHAT, 0, PetTypeData.RAVEN, new PetStage(12727,
			7264, PetConstants.BIRD_CHAT, 0, PetTypeData.RAVEN, null)) }),

	RACCOON(new PetStage[] { new PetStage(12734, 7271, null, 0, PetTypeData.RACOON, new PetStage(12735, 7272, null, 0,
			PetTypeData.RACOON, null)) }),

	GECKO(new PetStage[] { new PetStage(12738, 7277, null, 0, PetTypeData.GECKO, new PetStage(12742, 7281, null, 0,
			PetTypeData.GECKO, null)) }),

	GIANT_CRAB(new PetStage[] { new PetStage(12198, 6947, null, 0, PetTypeData.CRAB, new PetStage(12501, 6948, null, 0,
			PetTypeData.CRAB, null)) }),

	SQUIRREL(new PetStage[] { new PetStage(12754, 7301, null, 0, PetTypeData.SQUIRREL, new PetStage(12755, 7302, null,
			0, PetTypeData.SQUIRREL, null)) }),

	PENGUIN(new PetStage[] { new PetStage(12762, 7313, null, 0, PetTypeData.PENGUIN, new PetStage(12763, 7314, null, 0,
			PetTypeData.PENGUIN, null)) }),

	VULTURE(new PetStage[] { new PetStage(12766, 7319, PetConstants.BIRD_CHAT, 0, PetTypeData.VULTURE, new PetStage(
			12767, 7320, PetConstants.BIRD_CHAT, 0, PetTypeData.VULTURE, null)) }),

	BABY_DRAGON_RED(new PetStage[] { new PetStage(12469, 6900, PetConstants.DRAGON_CHAT, 0, PetTypeData.BABY_DRAGON,
			new PetStage(12470, 6901, PetConstants.DRAGON_CHAT, 0, PetTypeData.BABY_DRAGON, null)) }),

	BABY_DRAGON_BLUE(new PetStage[] { new PetStage(12471, 6902, PetConstants.DRAGON_CHAT, 0, PetTypeData.BABY_DRAGON,
			new PetStage(12472, 6903, PetConstants.DRAGON_CHAT, 0, PetTypeData.BABY_DRAGON, null)) }),

	BABY_DRAGON_GREEN(new PetStage[] { new PetStage(12473, 6904, PetConstants.DRAGON_CHAT, 0, PetTypeData.BABY_DRAGON,
			new PetStage(12474, 6905, PetConstants.DRAGON_CHAT, 0, PetTypeData.BABY_DRAGON, null)) }),

	BABY_DRAGON_BLACK(new PetStage[] { new PetStage(12475, 6906, PetConstants.DRAGON_CHAT, 0, PetTypeData.BABY_DRAGON,
			new PetStage(12476, 6907, PetConstants.DRAGON_CHAT, 0, PetTypeData.BABY_DRAGON, null)) }),

	MONKEY(new PetStage[] { new PetStage(12496, 6942, null, 0, PetTypeData.MONKEY, new PetStage(12497, 6943, null, 0,
			PetTypeData.MONKEY, null)) }),

	PLATYPUS(new PetStage[] { new PetStage(12551, 7018, null, 0, PetTypeData.PLATYPUS, new PetStage(12547, 7015, null,
			0, PetTypeData.PLATYPUS, null)) }),

	GNOME(new PetStage[] { new PetStage(3257, 2371, null, 0, PetTypeData.PLATYPUS, null) });

	private static final Map<Integer, PetStage> petsByNpcId = new HashMap<Integer, PetStage>();
	private static final Map<Integer, PetStage> petsByItemId = new HashMap<Integer, PetStage>();
	public final PetStage[] stages;

	private PetData(PetStage[] stages) {
		this.stages = stages;
	}
	
	public static boolean isSamePet(int id1, int id2) {
		for (PetData i : PetData.values()) {
			boolean a = false;
			boolean b = false;
			
			PetStage k = i.stages[0];
			
			do {
				if (k.itemId == id1 || k.itemId == id2) {
					if (a) {
						b = true;
					} else {
						a = true;
					}
					continue;
				}
			} while ((k = k.next) != null);
			
			if (a && b) {
				return true;
			}
		}
		
		return false;
	}

	public static PetStage getPetDataForMob(int id) {
		return petsByNpcId.get(Integer.valueOf(id));
	}

	public static PetStage getPetDataForItem(int id) {
		return petsByItemId.get(Integer.valueOf(id));
	}

	public static final void declare() {
		for (PetData i : values()) {
			for (PetStage k : i.stages) {
				petsByNpcId.put(Integer.valueOf(k.npcId), k);
				petsByItemId.put(Integer.valueOf(k.itemId), k);
				while ((k = k.next) != null) {
					petsByNpcId.put(Integer.valueOf(k.npcId), k);
					petsByItemId.put(Integer.valueOf(k.itemId), k);
				}
			}
		}
	}

	public static void setItemValues() {
		for (PetData i : values())
			for (PetStage k : i.stages) {
				int value = 1000000;

				GameDefinitionLoader.setValue(k.itemId, value, value / 2);
				GameDefinitionLoader.setNotTradable(k.itemId);
			}
	}

	public static class PetStage {
		public final int itemId;
		protected final int npcId;
		protected final String[] messages;
		protected final byte stage;
		protected final PetTypeData petType;
		protected final PetStage next;

		public PetStage(int itemId, int npcId, String[] messages, int stage, PetTypeData petType, PetStage next) {
			this.itemId = itemId;
			this.npcId = npcId;
			this.messages = messages;
			this.stage = (byte) stage;
			this.petType = petType;
			this.next = next;
		}
	}
}
