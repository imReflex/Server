package org.endeavor.engine.cache.map;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.endeavor.engine.cache.ByteStream;

public class MapLoading {

	private static List<QueuedDoor> doorQueue = new LinkedList<QueuedDoor>();
	
	private static int doors = 0;

	public static void load() {
		try {
			File f = new File("./data/map/map_index");
			byte[] buffer = new byte[(int) f.length()];
			DataInputStream dis = new DataInputStream(new FileInputStream(f));
			dis.readFully(buffer);
			dis.close();
			ByteStream in = new ByteStream(buffer);
			int size = in.length() / 6;
			Region.setRegions(new Region[size]);
			int[] regionIds = new int[size];
			int[] mapGroundFileIds = new int[size];
			int[] mapObjectsFileIds = new int[size];
			for (int i = 0; i < size; i++) {
				regionIds[i] = in.getUShort();
				mapGroundFileIds[i] = in.getUShort();
				mapObjectsFileIds[i] = in.getUShort();
			}
			for (int i = 0; i < size; i++) {
				Region.getRegions()[i] = new Region(regionIds[i]);
			}
			for (int i = 0; i < size; i++) {
				byte[] file1 = getBuffer(new File("./data/map/mapdata/" + mapObjectsFileIds[i] + ".gz"));
				byte[] file2 = getBuffer(new File("./data/map/mapdata/" + mapGroundFileIds[i] + ".gz"));
				if (file1 == null || file2 == null) {
					continue;
				}
				try {
					loadMaps(regionIds[i], new ByteStream(file1), new ByteStream(file2));
				} catch (Exception e) {
					// e.printStackTrace();
					System.out.println("Error loading map region: " + regionIds[i] + ", ids: " + mapObjectsFileIds[i]
							+ " and " + mapGroundFileIds[i]);
				}
			}
			
			System.out.println("Loaded " + size + " Maps.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void processDoors() {
		List<QueuedDoor> ignore = new LinkedList<QueuedDoor>();
		for (Iterator<QueuedDoor> k = doorQueue.iterator(); k.hasNext();) {
			QueuedDoor i = k.next();

			if (ignore.contains(i)) {
				continue;
			}

			RSObject o = MapConstants.getDoubleDoor(i.getId(), i.getX(), i.getY(), i.getZ(), i.getFace());

			if (o != null) {// it's a double door
				if (MapConstants.isOpen(o.getId())) {
					continue;// XXX: for now..
				}

				ignore.add(new QueuedDoor(-1, o.getX(), o.getY(), o.getZ(), -1, -1));
				Region.getRegion(i.getX(), i.getY()).addDoubleDoor(new DoubleDoor(i, o));
			} else {
				Region r = Region.getRegion(i.getX(), i.getY());
				if (r != null) {
					r.addDoor(i.getId(), i.getX(), i.getY(), i.getZ(), i.getType(), i.getFace());
				}
			}
		}

		doorQueue = null;
		System.out.println("Loaded " + doors + " Doors.");
	}

	public static void addObject(boolean beforeLoad, int objectId, int x, int y, int height, int type, int direction) {
		ObjectDef def = ObjectDef.getObjectDef(objectId);
		
		if (def == null) {
			return;
		}

		/*if (def.name != null && def.name.contains("Flax")) {
			def.unwalkable = false;
		}

		if (objectId == 9369 || objectId == 9368) {
			def.unwalkable = true;
		}*/

		int xLength;
		int yLength;
		if (direction != 1 && direction != 3) {
			xLength = def.xLength();
			yLength = def.yLength();
		} else {
			xLength = def.yLength();
			yLength = def.xLength();
		}

		/*if (type == 22) {
			if (def.hasActions() && def.clipped()) {
				addClipping(beforeLoad, x, y, height, 0x200000);

				if (def.unshootable) {
					addProjectileClipping(beforeLoad, x, y, height, 0x200000);
				}
			}
		} else if (type >= 9) {
			if (def.clipped()) {
				addClippingForSolidObject(beforeLoad, x, y, height, xLength, yLength, def.unshootable);
			}
		} else if (type >= 0 && type <= 3) {
			if (def.clipped() || def.hasActions) {
				addClippingForVariableObject(beforeLoad, x, y, height, type, direction, def.unshootable);
			}

			if (def.unshootable) {
				addProjectileClippingForVariableObject(beforeLoad, x, y, height, type, direction, true);
			}
		}*/
		
		if (type == 22) {
			if (def.hasActions() && def.aBoolean779) {
				addClipping(beforeLoad, x, y, height, 0x200000);
			}
		} else if (type >= 9) {
			if (def.aBoolean779) {
				addClippingForSolidObject(beforeLoad, x, y, height, xLength, yLength, def.aBoolean757);
			}
		} else if (type >= 0 && type <= 3) {
			if (def.aBoolean779) {
				addClippingForVariableObject(beforeLoad, x, y, height, type, direction, /*def.isSolid()*/def.aBoolean757);
			}
		}


		if (def.hasActions()) {
			(beforeLoad ? Region.getUnsortedRegion(x, y) : Region.getRegion(x, y)).addObject(new RSObject(x, y, height,
					objectId, type, direction));
			if (beforeLoad) {
				if (def.actions != null && def.name != null) {
					if (objectId == 1586 || objectId == 23156 || objectId == 15516 || def.name.toLowerCase().contains("door") && !def.name.toLowerCase().contains("trapdoor")
							|| def.name.toLowerCase().contains("gate")) {
						doorQueue.add(new QueuedDoor(objectId, x, y, height, type, direction));
						doors++;
					}
				}
			}
		}
	}

	private static void addClipping(boolean before, int x, int y, int height, int shift) {
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = ((regionX / 8) << 8) + (regionY / 8);
		if (before) {
			for (Region r : Region.getRegions()) {
				if (r.id() == regionId) {
					r.addClip(x, y, height, shift);
					break;
				}
			}
		} else {
			Region.getRegion(x, y).addClip(x, y, height, shift);
		}
	}
	
	private static void addClippingAlternate(boolean before, int shift, int x, int y, int height) {
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = ((regionX / 8) << 8) + (regionY / 8);
		if (before) {
			for (Region r : Region.getRegions()) {
				if (r.id() == regionId) {
					r.addClip(x, y, height, shift);
					break;
				}
			}
		} else {
			Region.getRegion(x, y).addClip(x, y, height, shift);
		}
	}

	private static void addProjectileClipping(boolean before, int x, int y, int height, int flag) {
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = ((regionX / 8) << 8) + (regionY / 8);
		if (before) {
			for (Region r : Region.getRegions()) {
				if (r.id() == regionId) {
					r.addShootable(x, y, height, flag);
					break;
				}
			}
		} else {
			Region.getRegion(x, y).addShootable(x, y, height, flag);
		}
	}

	private static void addClippingForSolidObject(boolean before, int x, int y, int height, int xLength, int yLength,
			boolean flag) {
		int clipping = 256;
		for (int i = x; i < x + xLength; i++) {
			for (int i2 = y; i2 < y + yLength; i2++) {
				if (flag) {
					addProjectileClipping(before, i, i2, height, clipping);
				}
				addClipping(before, i, i2, height, clipping);
			}
		}
	}

	public static void removeObject(int objectId, int x, int y, int height, int type, int direction) {
		ObjectDef def = ObjectDef.getObjectDef(objectId);

		if (def == null) {
			System.out.println("null object def: " + objectId);
			return;
		}

		int xLength;
		int yLength;
		if (direction != 1 && direction != 3) {
			xLength = def.xLength();
			yLength = def.yLength();
		} else {
			xLength = def.yLength();
			yLength = def.xLength();
		}
		
		if (type == 22) {
			if (def.hasActions() && def.aBoolean779) {
				addClipping(false, x, y, height, -0x200000);
			}
		} else if (type >= 9) {
			if (def.aBoolean779) {
				removeClippingForSolidObject(x, y, height, xLength, yLength, def.aBoolean757);
			}
		} else if (type >= 0 && type <= 3) {
			if (def.aBoolean779) {
				removeClippingForVariableObject(x, y, height, type, direction, /*def.isSolid()*/def.aBoolean757);
			}
		}
		
		if (def.hasActions()) {
			Region.getRegion(x, y).removeObject(new RSObject(x, y, height, objectId, type, direction));
		}
	}

	public static void removeClippingForVariableObject(int x, int y, int height, int type, int direction, boolean flag) {
		boolean before = false;
		if (type == 0) {
			// Normal walls
			if (direction == 0) {
				// Wall tile-west?
				addClipping(before, x, y, height, -128);
				addClipping(before, x - 1, y, height, -8);
			} else if (direction == 1) {
				// Wall tile-north?
				addClipping(before, x, y, height, -2);
				addClipping(before, x, y + 1, height, -32);
			} else if (direction == 2) {
				// Wall tile-east?
				addClipping(before, x, y, height, -8);
				addClipping(before, x + 1, y, height, -128);
			} else if (direction == 3) {
				// Wall tile-south?
				addClipping(before, x, y, height, -32);
				addClipping(before, x, y - 1, height, -2);
			}
			// Diagonal walls
		} else if (type == 1 || type == 3) {
			if (direction == 0) {
				// Wall north-west???
				addClipping(before, x, y, height, -1);
				addClipping(before, x - 1, y, height, -16);
			} else if (direction == 1) {
				// Wall north-east
				addClipping(before, x, y, height, -4);
				addClipping(before, x + 1, y + 1, height, -64);
			} else if (direction == 2) {
				// Wall south-east
				addClipping(before, x, y, height, -16);
				addClipping(before, x + 1, y - 1, height, -1);
			} else if (direction == 3) {
				// Wall south-west
				addClipping(before, x, y, height, -64);
				addClipping(before, x - 1, y - 1, height, -4);
			}
		} else if (type == 2) {
			if (direction == 0) {
				// Blocked north and west
				addClipping(before, x, y, height, -130);
				addClipping(before, x - 1, y, height, -8);
				addClipping(before, x, y + 1, height, -32);
			} else if (direction == 1) {
				// Blocked north and east
				addClipping(before, x, y, height, -10);
				addClipping(before, x, y + 1, height, -32);
				addClipping(before, x + 1, y, height, -128);
			} else if (direction == 2) {
				// Blocked south and east
				addClipping(before, x, y, height, -40);
				addClipping(before, x + 1, y, height, -128);
				addClipping(before, x, y - 1, height, -2);
			} else if (direction == 3) {
				// Blocked south and west
				addClipping(before, x, y, height, -160);
				addClipping(before, x, y - 1, height, -2);
				addClipping(before, x - 1, y, height, -8);
			}
		}

		/*if (flag) {
			if (type == 0) {
				if (direction == 0) {
					addProjectileClipping(before, x, y, height, -128);
					addProjectileClipping(before, x - 1, y, height, -8);
				}
				if (direction == 1) {
					addProjectileClipping(before, x, y, height, -2);
					addProjectileClipping(before, x, y + 1, height, -32);
				}
				if (direction == 2) {
					addProjectileClipping(before, x, y, height, -8);
					addProjectileClipping(before, x + 1, y, height, -128);
				}
				if (direction == 3) {
					addProjectileClipping(before, x, y, height, -32);
					addProjectileClipping(before, x, y - 1, height, -2);
				}
			}
			if (type == 1 || type == 3) {
				if (direction == 0) {
					addProjectileClipping(before, x, y, height, -1);
					addProjectileClipping(before, x - 1, y + 1, height, -16);
				}
				if (direction == 1) {
					addProjectileClipping(before, x, y, height, -4);
					addProjectileClipping(before, x + 1, y + 1, height, -64);
				}
				if (direction == 2) {
					addProjectileClipping(before, x, y, height, -16);
					addProjectileClipping(before, x + 1, y - 1, height, -1);
				}
				if (direction == 3) {
					addProjectileClipping(before, x, y, height, -64);
					addProjectileClipping(before, x - 1, y - 1, height, -4);
				}
			}
			if (type == 2) {
				if (direction == 0) {
					addProjectileClipping(before, x, y, height, -130);
					addProjectileClipping(before, x - 1, y, height, -8);
					addProjectileClipping(before, x, y + 1, height, -32);
				}
				if (direction == 1) {
					addProjectileClipping(before, x, y, height, -10);
					addProjectileClipping(before, x, y + 1, height, -32);
					addProjectileClipping(before, x + 1, y, height, -128);
				}
				if (direction == 2) {
					addProjectileClipping(before, x, y, height, -40);
					addProjectileClipping(before, x + 1, y, height, -128);
					addProjectileClipping(before, x, y - 1, height, -2);
				}
				if (direction == 3) {
					addProjectileClipping(before, x, y, height, -160);
					addProjectileClipping(before, x, y - 1, height, -2);
					addProjectileClipping(before, x - 1, y, height, -8);
				}
			}
		}*/
	}

	private static void removeClippingForSolidObject(int x, int y, int height, int xLength, int yLength, boolean flag) {
		int clipping = -256;
		for (int i = x; i < x + xLength; i++) {
			for (int i2 = y; i2 < y + yLength; i2++) {
				if (flag) {
					addProjectileClipping(false, i, i2, height, -clipping);
				}
				addClipping(false, i, i2, height, -clipping);
			}
		}
	}

	public static void addClippingForVariableObject(boolean before, int x, int y, int height, int type, int direction,
			boolean flag) {
		
		addProjectileClippingForVariableObject(before, x, y, height, type, direction, flag);
		
		if (type == 0) {
            if (direction == 0) {
                addClippingAlternate(before, 128, x, y, height);
                addClippingAlternate(before, 8, x - 1, y, height);
            }
            if (direction == 1) {
                addClippingAlternate(before, 2, x, y, height);
                addClippingAlternate(before, 32, x, y + 1, height);
            }
            if (direction == 2) {
                addClippingAlternate(before, 8, x, y, height);
                addClippingAlternate(before, 128, x + 1, y, height);
            }
            if (direction == 3) {
                addClippingAlternate(before, 32, x, y, height);
                addClippingAlternate(before, 2, x, y - 1, height);
            }
        }
        if (type == 1 || type == 3) {
            if (direction == 0) {
                addClippingAlternate(before, 1, x, y, height);
                addClippingAlternate(before, 16, x - 1, y + 1, height);
            }
            if (direction == 1) {
                addClippingAlternate(before, 4, x, y, height);
                addClippingAlternate(before, 64, x + 1, y + 1, height);
            }
            if (direction == 2) {
                addClippingAlternate(before, 16, x, y, height);
                addClippingAlternate(before, 1, x + 1, y - 1, height);
            }
            if (direction == 3) {
                addClippingAlternate(before, 64, x, y, height);
                addClippingAlternate(before, 4, x - 1, y - 1, height);
            }
        }
        if (type == 2) {
            if (direction == 0) {
                addClippingAlternate(before, 130, x, y, height);
                addClippingAlternate(before, 8, x - 1, y, height);
                addClippingAlternate(before, 32, x, y + 1, height);
            }
            if (direction == 1) {
                addClippingAlternate(before, 10, x, y, height);
                addClippingAlternate(before, 32, x, y + 1, height);
                addClippingAlternate(before, 128, x + 1, y, height);
            }
            if (direction == 2) {
                addClippingAlternate(before, 40, x, y, height);
                addClippingAlternate(before, 128, x + 1, y, height);
                addClippingAlternate(before, 2, x, y - 1, height);
            }
            if (direction == 3) {
                addClippingAlternate(before, 160, x, y, height);
                addClippingAlternate(before, 2, x, y - 1, height);
                addClippingAlternate(before, 8, x - 1, y, height);
            }
        }
        /*if (flag) {
            if (type == 0) {
                if (direction == 0) {
                    addClippingAlternate(before, 0x10000, x, y, height);
                    addClippingAlternate(before, 4096, x - 1, y, height);
                }
                if (direction == 1) {
                    addClippingAlternate(before, 1024, x, y, height);
                    addClippingAlternate(before, 16384, x, y + 1, height);
                }
                if (direction == 2) {
                    addClippingAlternate(before, 4096, x, y, height);
                    addClippingAlternate(before, 0x10000, x + 1, y, height);
                }
                if (direction == 3) {
                    addClippingAlternate(before, 16384, x, y, height);
                    addClippingAlternate(before, 1024, x, y - 1, height);
                }
            }
            if (type == 1 || type == 3) {
                if (direction == 0) {
                    addClippingAlternate(before, 512, x, y, height);
                    addClippingAlternate(before, 8192, x - 1, y + 1, height);
                }
                if (direction == 1) {
                    addClippingAlternate(before, 2048, x, y, height);
                    addClippingAlternate(before, 32768, x + 1, y + 1, height);
                }
                if (direction == 2) {
                    addClippingAlternate(before, 8192, x, y, height);
                    addClippingAlternate(before, 512, x + 1, y - 1, height);
                }
                if (direction == 3) {
                    addClippingAlternate(before, 32768, x, y, height);
                    addClippingAlternate(before, 2048, x - 1, y - 1, height);
                }
            }
            if (type == 2) {
                if (direction == 0) {
                    addClippingAlternate(before, 0x10400, x, y, height);
                    addClippingAlternate(before, 4096, x - 1, y, height);
                    addClippingAlternate(before, 16384, x, y + 1, height);
                }
                if (direction == 1) {
                    addClippingAlternate(before, 5120, x, y, height);
                    addClippingAlternate(before, 16384, x, y + 1, height);
                    addClippingAlternate(before, 0x10000, x + 1, y, height);
                }
                if (direction == 2) {
                    addClippingAlternate(before, 20480, x, y, height);
                    addClippingAlternate(before, 0x10000, x + 1, y, height);
                    addClippingAlternate(before, 1024, x, y - 1, height);
                }
                if (direction == 3) {
                    addClippingAlternate(before, 0x14000, x, y, height);
                    addClippingAlternate(before, 1024, x, y - 1, height);
                    addClippingAlternate(before, 4096, x - 1, y, height);
                }
            }
        }*/
		
		/*if (type == 0) {
			// Normal walls
			if (direction == 0) {
				// Wall tile-west?
				addClipping(before, x, y, height, 128);
				addClipping(before, x - 1, y, height, 8);
			} else if (direction == 1) {
				// Wall tile-north?
				addClipping(before, x, y, height, 2);
				addClipping(before, x, y + 1, height, 32);
			} else if (direction == 2) {
				// Wall tile-east?
				addClipping(before, x, y, height, 8);
				addClipping(before, x + 1, y, height, 128);
			} else if (direction == 3) {
				// Wall tile-south?
				addClipping(before, x, y, height, 32);
				addClipping(before, x, y - 1, height, 2);
			}
			// Diagonal walls
		} else if (type == 1 || type == 3) {
			if (direction == 0) {
				// Wall north-west???
				addClipping(before, x, y, height, 1);
				addClipping(before, x - 1, y, height, 16);
			} else if (direction == 1) {
				// Wall north-east
				addClipping(before, x, y, height, 4);
				addClipping(before, x + 1, y + 1, height, 64);
			} else if (direction == 2) {
				// Wall south-east
				addClipping(before, x, y, height, 16);
				addClipping(before, x + 1, y - 1, height, 1);
			} else if (direction == 3) {
				// Wall south-west
				addClipping(before, x, y, height, 64);
				addClipping(before, x - 1, y - 1, height, 4);
			}
		} else if (type == 2) {
			if (direction == 0) {
				// Blocked north and west
				addClipping(before, x, y, height, 130);
				addClipping(before, x - 1, y, height, 8);
				addClipping(before, x, y + 1, height, 32);
			} else if (direction == 1) {
				// Blocked north and east
				addClipping(before, x, y, height, 10);
				addClipping(before, x, y + 1, height, 32);
				addClipping(before, x + 1, y, height, 128);
			} else if (direction == 2) {
				// Blocked south and east
				addClipping(before, x, y, height, 40);
				addClipping(before, x + 1, y, height, 128);
				addClipping(before, x, y - 1, height, 2);
			} else if (direction == 3) {
				// Blocked south and west
				addClipping(before, x, y, height, 160);
				addClipping(before, x, y - 1, height, 2);
				addClipping(before, x - 1, y, height, 8);
			}
		}*/
	}

	public static void addProjectileClippingForVariableObject(boolean before, int x, int y, int height, int type,
			int direction, boolean flag) {
		if (flag) {
			if (type == 0) {
				if (direction == 0) {
					addProjectileClipping(before, x, y, height, 128);
					addProjectileClipping(before, x - 1, y, height, 8);
				}
				if (direction == 1) {
					addProjectileClipping(before, x, y, height, 2);
					addProjectileClipping(before, x, y + 1, height, 32);
				}
				if (direction == 2) {
					addProjectileClipping(before, x, y, height, 8);
					addProjectileClipping(before, x + 1, y, height, 128);
				}
				if (direction == 3) {
					addProjectileClipping(before, x, y, height, 32);
					addProjectileClipping(before, x, y - 1, height, 2);
				}
			}
			if (type == 1 || type == 3) {
				if (direction == 0) {
					addProjectileClipping(before, x, y, height, 1);
					addProjectileClipping(before, x - 1, y + 1, height, 16);
				}
				if (direction == 1) {
					addProjectileClipping(before, x, y, height, 4);
					addProjectileClipping(before, x + 1, y + 1, height, 64);
				}
				if (direction == 2) {
					addProjectileClipping(before, x, y, height, 16);
					addProjectileClipping(before, x + 1, y - 1, height, 1);
				}
				if (direction == 3) {
					addProjectileClipping(before, x, y, height, 64);
					addProjectileClipping(before, x - 1, y - 1, height, 4);
				}
			}
			if (type == 2) {
				if (direction == 0) {
					addProjectileClipping(before, x, y, height, 130);
					addProjectileClipping(before, x - 1, y, height, 8);
					addProjectileClipping(before, x, y + 1, height, 32);
				}
				if (direction == 1) {
					addProjectileClipping(before, x, y, height, 10);
					addProjectileClipping(before, x, y + 1, height, 32);
					addProjectileClipping(before, x + 1, y, height, 128);
				}
				if (direction == 2) {
					addProjectileClipping(before, x, y, height, 40);
					addProjectileClipping(before, x + 1, y, height, 128);
					addProjectileClipping(before, x, y - 1, height, 2);
				}
				if (direction == 3) {
					addProjectileClipping(before, x, y, height, 160);
					addProjectileClipping(before, x, y - 1, height, 2);
					addProjectileClipping(before, x - 1, y, height, 8);
				}
			}
		}
	}

	private static void loadMaps(int regionId, ByteStream str1, ByteStream str2) {
		int absX = (regionId >> 8) * 64;
		int absY = (regionId & 0xff) * 64;
		int[][][] someArray = new int[4][64][64];
		for (int i = 0; i < 4; i++) {
			for (int i2 = 0; i2 < 64; i2++) {
				for (int i3 = 0; i3 < 64; i3++) {
					while (true) {
						int v = str2.getUByte();
						if (v == 0) {
							break;
						} else if (v == 1) {
							str2.skip(1);
							break;
						} else if (v <= 49) {
							str2.skip(1);
						} else if (v <= 81) {
							someArray[i][i2][i3] = v - 49;
						}
					}
				}
			}
		}
		for (int i = 0; i < 4; i++) {
			for (int i2 = 0; i2 < 64; i2++) {
				for (int i3 = 0; i3 < 64; i3++) {
					if ((someArray[i][i2][i3] & 1) == 1) {
						int height = i;
						if ((someArray[1][i2][i3] & 2) == 2) {
							height--;
						}
						if (height >= 0 && height <= 3) {
							addClipping(true, absX + i2, absY + i3, height, 0x200000);
						}
					}
				}
			}
		}
		int objectId = -1;
		int incr;
		while ((incr = str1.getUSmart()) != 0) {
			objectId += incr;
			int location = 0;
			int incr2;
			while ((incr2 = str1.getUSmart()) != 0) {
				location += incr2 - 1;
				int localX = (location >> 6 & 0x3f);
				int localY = (location & 0x3f);
				int height = location >> 12;
				int objectData = str1.getUByte();
				int type = objectData >> 2;
				int direction = objectData & 0x3;
				if (localX < 0 || localX >= 64 || localY < 0 || localY >= 64) {
					continue;
				}
				if ((someArray[1][localX][localY] & 2) == 2) {
					height--;
				}
				if (height >= 0 && height <= 3) {
					addObject(true, objectId, absX + localX, absY + localY, height, type, direction);
				}
			}
		}
	}

	public static byte[] getBuffer(File f) throws Exception {
		if (!f.exists()) {
			return null;
		}
		byte[] buffer = new byte[(int) f.length()];
		DataInputStream dis = new DataInputStream(new FileInputStream(f));
		dis.readFully(buffer);
		dis.close();
		byte[] gzipInputBuffer = new byte[999999];
		int bufferlength = 0;
		GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(buffer));
		do {
			if (bufferlength == gzipInputBuffer.length) {
				System.out.println("Error inflating data.\nGZIP buffer overflow.");
				break;
			}
			int readByte = gzip.read(gzipInputBuffer, bufferlength, gzipInputBuffer.length - bufferlength);
			if (readByte == -1) {
				break;
			}
			bufferlength += readByte;
		} while (true);
		byte[] inflated = new byte[bufferlength];
		System.arraycopy(gzipInputBuffer, 0, inflated, 0, bufferlength);
		buffer = inflated;

		gzip.close();

		if (buffer.length < 10) {
			return null;
		}
		return buffer;
	}
}
