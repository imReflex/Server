package org.endeavor.game.entity.object;

import java.util.HashMap;
import java.util.Map;

import org.endeavor.engine.cache.map.ObjectDef;

public class ObjectConstants {
	public static final int BANK_DECORATIVE_ROPE = 4655;
	public static final int BDR_TYPE = 0;
	public static final int BANK_GROUND_D_EDGE = 27018;
	public static final int BANK_GROUND_D_CONNECTOR = 27019;
	public static final int BANK_GROUND_D_TILE = 27020;
	private static final byte[][] sizes = new byte[ObjectDef.getObjects() - 1][2];
	
	private static final Map<Integer, Byte> isSummoningObeliskForId = new HashMap<Integer, Byte>();

	public static boolean isSummoningObelisk(int id) {
		return isSummoningObeliskForId.containsKey(id);
	}
	
	public static void declare() {
		for (int i = 0; i < ObjectDef.getObjects() - 1; i++) {
			ObjectDef def = ObjectDef.getObjectDef(i);

			if (def != null) {
				if (def.name != null) {
					if (def.name.toLowerCase().contains("obelisk") && def.name.length() > "obelisk".length()) {
						isSummoningObeliskForId.put(i, (byte) 0);
					}
				}
				
				sizes[i][0] = ((byte) def.xLength());
				sizes[i][1] = ((byte) def.yLength());

				if (i == 26421) {
					int tmp51_50 = 0;
					byte[] tmp51_49 = sizes[i];
					tmp51_49[tmp51_50] = ((byte) (tmp51_49[tmp51_50] + 3));
					int tmp63_62 = 1;
					byte[] tmp63_61 = sizes[i];
					tmp63_61[tmp63_62] = ((byte) (tmp63_61[tmp63_62] + 3));
				}
			}
		}
	}

	public static int[] getObjectLength(int id, int face) {
		if (id >= sizes.length) {
			return new int[] {3, 3};
		}
		
		byte xLength = sizes[id][0];
		byte yLength = sizes[id][1];

		if ((face != 1) && (face != 3)) {
			return new int[] { xLength, yLength };
		}
		return new int[] { yLength, xLength };
	}
}
