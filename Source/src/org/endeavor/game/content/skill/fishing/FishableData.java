package org.endeavor.game.content.skill.fishing;

import java.util.HashMap;
import java.util.Map;

import org.endeavor.game.content.skill.fishing.FishableData.Fishable;

public class FishableData {
	public static enum Fishable {
		SHRIMP(317, 303, 1, 10.0D, -1), CRAYFISH(13435, 13431, 1, 10.0D, -1), KARAMBWANJI(3150, 303, 5, 5.0D, -1), SARDINE(
				327, 307, 5, 20.0D, 313), HERRING(345, 307, 10, 30.0D, 313), ANCHOVIES(321, 303, 15, 40.0D, -1), MACKEREL(
				353, 305, 16, 20.0D, -1), TROUT(335, 309, 20, 50.0D, 314), COD(341, 305, 23, 45.0D, -1),

		PIKE(349, 307, 25, 60.0D, 313),

		SLIMY_EEL(3379, 307, 28, 65.0D, 313), SALMON(331, 309, 30, 70.0D, 314), FROG_SPAWN(5004, 303, 33, 75.0D, -1), TUNA(
				359, 311, 35, 80.0D, -1), CAVE_EEL(5001, 307, 38, 80.0D, 313), LOBSTER(377, 301, 40, 90.0D, -1), BASS(
				363, 305, 46, 100.0D, -1), SWORD_FISH(371, 311, 50, 100.0D, -1), LAVA_EEL(2148, 307, 53, 30.0D, 313), MONK_FISH(
				7944, 303, 62, 120.0D, -1), KARAMBWAN(3142, 3157, 65, 100.0D, -1), SHARK(383, 311, 76, 110.0D, -1), SEA_TURTLE(
				395, -1, 79, 38.0D, -1), MANTA_RAY(389, 311, 81, 155.0D, -1),

		ROCKTAIL(15270, 307, 90, 380.0D, -1);

		private short rawFishId;
		private short toolId;
		private short levelRequired;
		private short baitRequired;
		private double experienceGain;
		private static Map<Integer, Fishable> fish = new HashMap<Integer, Fishable>();

		private Fishable(int rawFishId, int toolId, int levelRequired, double experienceGain, int baitRequired) {
			this.rawFishId = ((short) rawFishId);
			this.toolId = ((short) toolId);
			this.levelRequired = ((short) levelRequired);
			this.experienceGain = experienceGain;
			this.baitRequired = ((short) baitRequired);
		}

		public short getRawFishId() {
			return rawFishId;
		}

		public short getToolId() {
			return toolId;
		}

		public short getRequiredLevel() {
			return levelRequired;
		}

		public double getExperience() {
			return experienceGain;
		}

		public short getBaitRequired() {
			return baitRequired;
		}

		public static final void declare() {
			for (Fishable fishes : values())
				fish.put(Integer.valueOf(fishes.getRawFishId()), fishes);
		}

		public static Fishable forId(int rawFishId) {
			return fish.get(Integer.valueOf(rawFishId));
		}
	}
}
