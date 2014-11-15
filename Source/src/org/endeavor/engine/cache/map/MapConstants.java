package org.endeavor.engine.cache.map;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import org.endeavor.game.GameConstants;

public class MapConstants {

	/**
	 * Spiral staircases
	 */
	private static final Map<Short, Byte> spiral = new HashMap<Short, Byte>();

	public static final void addSpiralStaircase(int id, int options) {
		spiral.put((short) id, (byte) options);
	}

	public static final boolean isSpiralStaircase(int id) {
		return spiral.get((short) id) != null;
	}

	public static final int getOptionCount(ObjectDef def) {
		int k = 0;

		for (String i : def.actions) {
			if (i != null)
				k++;
		}

		return k;
	}
	
	public static boolean isReverseOrientation(int id) {
		return id == 14234 || id == 14233 || id == 31841;
	}

	public static final int getZModifier(int id, int option) {
		byte options = spiral.get((short) id);

		if (options == 1) {
			return 1;
		} else if (options == 2) {
			if (option == 2)
				return -1;
			else
				return 1;
		} else if (options == 3) {
			if (option == 3)
				return -1;
			else
				return 1;
		} else if (options == 4) {
			return -1;
		}

		return 0;
	}

	public static final RSObject getDoubleDoor(final int id, final int x, final int y, final int z, final int face) {
		final String name = ObjectDef.getObjectDef(id).name;

		for (int i = 0; i < 8; i++) {
			int x2 = x + GameConstants.DIR[i][0];
			int y2 = y + GameConstants.DIR[i][1];

			RSObject o = Region.getObject(x2, y2, z);
			ObjectDef d = null;

			if (o != null && (d = ObjectDef.getObjectDef(o.getId())) != null && d.name != null && d.name.equals(name)
					&& o.getFace() == face) {
				return o;
			}
		}

		return null;
	}

	public static final boolean isUp(int id) {
		ObjectDef def = ObjectDef.getObjectDef(id);

		if (def.actions != null) {
			for (String i : def.actions) {
				if (i == null)
					continue;
				if (i.equals("Climb-up")) {
					return true;
				}
			}
		}

		return false;
	}

	public static final boolean isOpen(int id) {
		ObjectDef def = ObjectDef.getObjectDef(id);

		if (def.actions != null) {
			for (String i : def.actions) {
				if (i == null)
					continue;
				if (i.equals("Close")) {
					return true;
				}
			}
		}

		return false;
	}

	public static final void writeAlternateIds() {
		int[] alts = new int[60000];

		for (int i = 0; i < alts.length; i++) {
			boolean flag = false;

			if (ObjectDef.getObjectDef(i) != null) {
				flag = !isOpen(i);
			} else {
				continue;
			}

			if (ObjectDef.getObjectDef(i) != null && ObjectDef.getObjectDef(i).name != null) {
				if (ObjectDef.getObjectDef(i).name.toLowerCase().contains("door")
						|| ObjectDef.getObjectDef(i).name.toLowerCase().contains("gate")) {
					alts[i] = getAlternateId(ObjectDef.getObjectDef(i), flag);
				}
			}
		}

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./data/def/ObjectAlternates.txt")));

			for (int i = 0; i < 60000; i++) {
				if (alts[i] > 0) {
					writer.write(i + ":" + alts[i]);
					writer.newLine();
				}
			}

			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Alternates have been written.");
	}

	public static final int getAlternateId(final ObjectDef main, boolean flag) {
		final String name = main.name;
		final int type = main.type;

		int[] a = null;
		int[] b = null;
		int[] c = null;
		int[] d = null;

		if (main.anIntArray773 != null) {
			a = main.anIntArray773.clone();
		}

		if (main.anIntArray776 != null) {
			b = main.anIntArray776.clone();
		}

		if (main.modifiedModelColors != null) {
			c = main.modifiedModelColors.clone();
		}

		if (main.originalModelColors != null) {
			d = main.originalModelColors.clone();
		}

		loop: for (int i = type - 200; i < type + 200; i++) {
			try {
				ObjectDef def = ObjectDef.getObjectDef(i);

				if (!def.name.equalsIgnoreCase(name)) {
					continue;
				}

				if (a != null) {
					for (int k = 0; k < def.anIntArray773.length; k++) {
						if (a.length - 1 < k || def.anIntArray773[k] != a[k])
							continue loop;
					}
				}

				if (b != null) {
					for (int k = 0; k < def.anIntArray776.length; k++) {
						if (b.length - 1 < k || def.anIntArray776[k] != b[k])
							continue loop;
					}
				}

				if (c != null) {
					for (int k = 0; k < def.modifiedModelColors.length; k++) {
						if (c.length - 1 < k || def.modifiedModelColors[k] != c[k])
							continue loop;
					}
				}

				if (d != null) {
					for (int k = 0; k < def.originalModelColors.length; k++) {
						if (d.length - 1 < k || def.originalModelColors[k] != d[k])
							continue;
					}
				}

				for (String k : def.actions) {
					if (k == null)
						continue;
					if (flag && k.equals("Close") || !flag && k.equals("Open")) {
						return i;
					}
				}
			} catch (Exception e) {
				continue;
			}
		}

		return -1;
	}

}
