package org.endeavor.game.content.minigames.godwars;

import java.util.HashMap;
import java.util.Map;

public class GodWarsNpcData {
	public static enum GodWarsNpcs {
		//arm
		SPIRITUAL_RANGER_ARMADYL(6230, 1), 
		SPIRITUAL_MAGE_ARMADYL(6231, 1), 
		SPIRITUAL_WARRIOR_ARMADYL(6229, 1), 
		
		//bandos
		SPIRITUAL_RANGER_BANDOS(6276, 0), 
		SPIRITUAL_MAGE_BANDOS(6278, 0), 
		SPIRITUAL_WARRIOR_BANDOS(6277, 0), 
		HOB_GOBLIN_BANDOS(6275, 0), 
		CYCLOPS_BANDOS(6269, 0),

		//zammy
		SPIRITUAL_RANGER_ZAMORAK(6220, 2), 
		SPIRITUAL_MAGE_ZAMORAK(6221, 2), 
		SPIRITUAL_WARRIOR_ZAMORAK(6219, 2), 
		WEREWOLF_ZAMORAK(6213, 2), 
		
		//sara
		SPIRITUAL_RANGER_SARADOMIN(6256, 3), 
		SPIRITUAL_MAGE_SARADOMIN(6257, 3), 
		SPIRITUAL_WARRIOR_SARADOMIN(6255, 3), 
		KNIGHT_OF_SARADOMIN(6258, 3), 
		SARADOMIN_PRIEST(6254, 3),
		
		//bandos bosses
		MOB_1(6260, 0), 
		MOB_2(6261, 0),
		MOB_3(6263, 0), 
		MOB_4(6265, 0),

		//zammy bosses
		MOB_5(6203, 2), 
		MOB_6(6208, 2), 
		MOB_7(6206, 2), 
		MOB_8(6204, 2),

		//arm bosses
		MOB_9(6227, 1), 
		MOB_10(6225, 1), 
		MOB_11(6222, 1), 
		MOB_12(6223, 1),

		//sara bosses
		MOB_13(6250, 3), 
		MOB_14(6252, 3), 
		MOB_15(6247, 3), 
		MOB_16(6248, 3),

		;

		private int id;
		private int kcId;
		private static Map<Integer, GodWarsNpcs> godWarsNpcs = new HashMap<Integer, GodWarsNpcs>();

		private GodWarsNpcs(int id, int kcId) {
			this.id = id;
			this.kcId = kcId;
		}

		public int getId() {
			return id;
		}

		public int getKcId() {
			return kcId;
		}

		public static final void declare() {
			for (GodWarsNpcs npc : values())
				godWarsNpcs.put(Integer.valueOf(npc.getId()), npc);
		}

		public static int forId(int id) {
			if (!godWarsNpcs.containsKey(Integer.valueOf(id))) {
				return -1;
			}

			return godWarsNpcs.get(Integer.valueOf(id)).kcId;
		}
	}
}
