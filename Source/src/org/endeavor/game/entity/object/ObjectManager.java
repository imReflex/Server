package org.endeavor.game.entity.object;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.endeavor.engine.cache.map.MapLoading;
import org.endeavor.engine.cache.map.RSObject;
import org.endeavor.engine.cache.map.Region;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.player.GraveStone;
import org.endeavor.game.entity.player.Player;

public class ObjectManager {
	public static final int BLANK_OBJECT_ID = 2376;
	
	private static final List<GameObject> active = new LinkedList<GameObject>();
	private static final Deque<GameObject> register = new LinkedList<GameObject>();

	private static final Queue<GameObject> send = new ConcurrentLinkedQueue<GameObject>();

	public static void process() {
		for (Iterator<GameObject> i = register.iterator(); i.hasNext();) {
			GameObject reg = (GameObject) i.next();
			active.remove(reg);
			Region.getRegion(reg.getLocation()).addObject(new RSObject(reg.getLocation().getX(), reg.getLocation().getY(), reg.getLocation().getZ(), 
					reg.getId(), reg.getType(), reg.getFace()));
			active.add(reg);
			send.add(reg);

			i.remove();
		}
	}

	public static GameObject getGameObject(int x, int y, int z) {
		int index = active.indexOf(new GameObject(x, y, z));

		if (index == -1) {
			return null;
		}

		return active.get(index);
	}

	public static GameObject getGraveFor(Player p) {
		for (GameObject o : active) {
			if ((o.getId() == 12717) && ((o instanceof GraveStone))) {
				return o;
			}
		}

		return null;
	}

	private static final void spawn(int id, int x, int y, int z, int type, int face) {
		MapLoading.addObject(false, id, x, y, z, type, face);
	}

	private static final void spawnWithObject(int id, int x, int y, int z, int type, int face) {
		active.add(new GameObject(id, x, y, z, type, face));
		MapLoading.addObject(false, id, x, y, z, type, face);

		send(new GameObject(id, x, y, z, type, face));
	}

	private static final void delete(int x, int y, int z) {
		RSObject object = Region.getObject(x, y, z);

		if (Region.getDoor(x, y, z) != null) {
			Region.removeDoor(x, y, z);
		}

		if (object == null) {
			if (z > 0)
				active.add(new GameObject(2376, x, y, z, 10, 0));
			return;
		}

		MapLoading.removeObject(object.getId(), x, y, z, object.getType(), object.getFace());

		if ((object.getType() != 10) || (z > 0))
			active.add(new GameObject(2376, x, y, z, object.getType(), 0));
	}

	private static final void deleteWithObject(int x, int y, int z) {
		RSObject object = Region.getObject(x, y, z);

		if (Region.getDoor(x, y, z) != null) {
			Region.removeDoor(x, y, z);
		}

		if (object == null) {
			active.add(new GameObject(2376, x, y, z, 10, 0));
			return;
		}

		MapLoading.removeObject(object.getId(), x, y, z, object.getType(), object.getFace());

		active.add(new GameObject(2376, x, y, z, object.getType(), 0));
	}
	
	private static final void deleteWithObject(int x, int y, int z, int type) {
		active.add(new GameObject(2376, x, y, z, type, 0));
	}

	public static void setClipToZero(int x, int y, int z) {
		Region region = Region.getRegion(x, y);

		region.setClipToZero(x, y, z);
	}

	public static void setPClipInfinity(int x, int y, int z) {
		Region region = Region.getRegion(x, y);

		region.setProjecileClipToInfinity(x, y, z);
	}

	private static final void removeWithoutClip(int x, int y, int z, int type) {
	}

	public static void register(GameObject o) {
		register.add(o);
	}

	public static void addClippedObject(GameObject o) {
		register.add(o);
	}

	public static void send(GameObject o) {
		for (Player player : World.getPlayers())
			if ((player != null) && (player.isActive())) {
				if ((player.withinRegion(o.getLocation()))
						&& (player.getLocation().getZ() % 4 == o.getLocation().getZ() % 4))
					player.getObjects().add(o);
			}
	}

	public static boolean objectExists(Location location) {
		for (GameObject object : active) {
			if (location.equals(object.getLocation())) {
				return true;
			}
		}
		return false;
	}

	public static void add(GameObject o) {
		active.add(o);
	}

	public static void remove(GameObject o) {
		removeFromList(o);
		Region.getRegion(o.getLocation()).removeObject(new RSObject(o.getLocation().getX(), o.getLocation().getY(), o.getLocation().getZ(), o.getId(), o.getType(), o.getFace()));
		send.add(getBlankObject(o.getLocation(), o.getType()));
	}

	public static void removeFromList(GameObject o) {
		active.remove(o);
	}

	public static List<GameObject> getActive() {
		return active;
	}

	public static void queueSend(GameObject o) {
		send.add(o);
	}

	public static Queue<GameObject> getSend() {
		return send;
	}

	public static final GameObject getBlankObject(Location p) {
		return new GameObject(2376, p.getX(), p.getY(), p.getZ(), 10, 0, false);
	}

	public static GameObject getBlankObject(Location p, int type) {
		return new GameObject(2376, p.getX(), p.getY(), p.getZ(), type, 0, false);
	}

	public static void declare() {
		for (GameObject i : active) {
			send(getBlankObject(i.getLocation()));
		}

		active.clear();

		for (int x = 3092; x <= 3096; x++) {
			deleteWithObject(x, 3957, 0);
		}

		for (int i = 0; i < 5; i++) {
			removeWithoutClip(2846 - i, 10161, 0, 3);
			removeWithoutClip(2846 - i, 10162, 0, 3);
		}
		for (int i = 0; i < 8; i++) {
			removeWithoutClip(2849 - i, 10148, 0, 3);
			removeWithoutClip(2849 - i, 10149, 0, 3);
		}

		
		// NEW SKILL AREA - CATHERBY
		for (int x = 2813; x <= 2813 + 4; x++) {
			for (int y = 3450; y <= 3450 + 4; y++) {
				delete(x, y, 0);
			}
		}
		
		delete(3084, 3494, 0);//edge flowers

		delete(2806, 3439, 0);

		delete(2812, 3450, 0);
		delete(2812, 3449, 0);

		delete(2817, 3442, 0);
		delete(2817, 3441, 0);
		delete(2817, 3440, 0);

		// EDGEVILLE BANK
		delete(3093, 3488, 0);
		delete(3092, 3488, 0);
		delete(3090, 3494, 0);
		delete(3090, 3495, 0);
		delete(3090, 3496, 0);

		// KELDAGRIM EAST SKILLING AREA NEW BANK
		delete(2886, 10195, 0);
		delete(2887, 10192, 0);
		delete(2888, 10191, 0);
		delete(2889, 10190, 0);
		delete(2890, 10201, 0);
		delete(2889, 10202, 0);
		delete(2890, 10202, 0);
		delete(2889, 10203, 0);
		delete(2890, 10203, 0);
		delete(2891, 10203, 0);
		delete(2894, 10209, 0);
		delete(2886, 10201, 0);
		delete(2894, 10211, 0);
		delete(2885, 10200, 0);
		delete(2885, 10197, 0);
		delete(2885, 10204, 0);
		delete(2895, 10205, 0);
		delete(2895, 10204, 0);
		delete(2890, 10204, 0);
		delete(2896, 10190, 0);
		delete(2894, 10195, 0);

		// varr sewers
		delete(3241, 9911, 0);
		delete(3241, 9910, 0);
		delete(3210, 9899, 0);
		delete(3210, 9898, 0);

		delete(3085, 3468, 0);
		delete(3096, 3470, 0);
		delete(3097, 3468, 0);
		delete(3085, 3480, 0);

		// dung blocked door
		delete(2763, 9316, 0);
		delete(2762, 9316, 0);
		delete(2763, 9315, 0);
		delete(2762, 9315, 0);
		// edge rfd
		delete(3090, 3476, 0);
		delete(3090, 3479, 0);

		for (int i = 0; i < 5; i++) {
			delete(3092, 3493 + i, 0);
			delete(3091, 3493 + i, 0);
		}

		// dung map a
		for (int x = 2959; x <= 2966; x++) {
			for (int y = 9628; y <= 9639; y++) {
				delete(x, y, 0);
			}
		}

		for (int y = 9630; y <= 9645; y++) {
			for (int x = 2979; x <= 2994; x++) {
				delete(x, y, 0);
			}
		}

		// end dung map a
		// dung lobby
		delete(1832, 5215, 0);

		for (int x = 1838; x <= 1846; x++) {
			for (int y = 5218; y <= 5225; y++) {
				delete(x, y, 0);
			}
		}

		for (int x = 1833; x <= 1838; x++) {
			for (int y = 5213; y <= 5216; y++) {
				delete(x, y, 0);
			}
		}

		// end dung lobby

		// RANDOM
		delete(3090, 3476, 0);
		delete(3096, 3476, 0);

		// dung map b
		delete(2575, 9631, 0);
		delete(2576, 9631, 0);

		// HOME
		delete(2840, 10213, 0);
		delete(2841, 10214, 0);
		delete(2837, 10214, 0);
		delete(2838, 10215, 0);
		delete(2834, 10213, 0);
		delete(2835, 10212, 0);
		delete(2886, 10198, 0);
		delete(2897, 10201, 0);
		delete(2894, 10188, 0);
		delete(2871, 10174, 0);
		delete(3095, 3498, 0);
		delete(3095, 3499, 0);
		delete(3248, 9364, 0);
		delete(2921, 10218, 0);

		// slayer tower
		delete(3445, 3554, 2);
		delete(3445, 3555, 2);

		// SKILLING
		delete(2891, 10195, 0);
		delete(2890, 10195, 0);
		delete(2890, 10194, 0);

		delete(3096, 3479, 0);
		delete(3104, 3494, 0);

		// slayer tower door
		delete(3426, 3555, 1);
		delete(3427, 3555, 1);
		delete(3426, 3556, 1);
		delete(3427, 3556, 1);

		// rouges den
		delete(3042, 4974, 1);

		// catherby bank
		delete(2812, 3439, 0);

		// START KELDAGRIM EAST BANK

		// int bx = 3090;
		// int by = 3475;

		/**
		 * Yanille safe pk portal
		 */
		//to client
		deleteWithObject(2601, 3093, 0);
		deleteWithObject(2601, 3094, 0);
		deleteWithObject(2602, 3092, 0);
		deleteWithObject(2600, 3094, 0);
		deleteWithObject(2601, 3096, 0);
		spawnWithObject(38698, 3083, 3492, 0, 10, 1);
		
		//to client
		
		/**
		 * GWD
		 */
		spawnWithObject(26323, 2928, 3760, 0, 22, 0);
		
		/**
		 * Yanille cafe doors
		 */
		deleteWithObject(2604, 3104, 0);
		deleteWithObject(2606, 3104, 0);
		deleteWithObject(2608, 3104, 0);
		
		/**
		 * Manhole to Dag kings
		 */
		spawnWithObject(882, 2488, 10150, 0, 10, 1);
		setClipToZero(2899, 4449, 0);
		
		/**
		 * Manhole to Strykeworms
		 */
		spawnWithObject(882, 3331, 3653, 0, 10, 1);
		spawnWithObject(1752, 1989, 5189, 0, 10, 1);
		
		/**
		 * Manhole to Corp
		 */
		spawnWithObject(882, 3404, 3089, 0, 10, 1);
		
		/**
		 * Manhole to Basic Training
		 */
		spawnWithObject(882, 2600, 3096, 0, 10, 0);
		spawnWithObject(1750, 3161, 4236, 0, 10, 0);
		
		/**
		 * New manhole for lumbridge swamp dungeon
		 */
		spawnWithObject(5947, 3244, 3191, 0, 10, 0);
		
		/**
		 * New Yanille Home
		 */
		//bank
		deleteWithObject(2612, 3097, 0);
		deleteWithObject(2611, 3097, 0);
		deleteWithObject(2611, 3096, 0);
		deleteWithObject(2611, 3095, 0);
		deleteWithObject(2611, 3088, 0);
		
		//webs
		deleteWithObject(3092, 3957, 0, 0);
		deleteWithObject(3095, 3957, 0, 0);
		
		//portal to shit
		spawn(13627, 2605, 3086, 0, 10, 0);
		
		// altars
		spawnWithObject(409, 2611 - 2, 3098, 0, 10, 0);
		spawnWithObject(6552, 3094, 3506, 0, 10, 2);
		spawnWithObject(410, 3103, 3500, 0, 10, 0);
		
		//do not delete this
		/*int bx = 3218;
		int by = 3239;
		for (int x = 0; x < 7; x++) {
			if (x == 0 || x == 6) {
				spawn(ObjectConstants.BANK_DECORATIVE_ROPE, bx - x, by, 0, ObjectConstants.BDR_TYPE, 0);
				spawn(ObjectConstants.BANK_GROUND_D_EDGE, bx - x + (x == 6 ? -1 : 0), by + 2, 0, 22, x == 0 ? 1 : 0);
				
				spawn(ObjectConstants.BANK_GROUND_D_CONNECTOR, bx - x + (x == 6 ? -1 : 0), by, 0, 22, x == 0 ? 1 : 3);
				spawn(ObjectConstants.BANK_GROUND_D_CONNECTOR, bx - x + (x == 6 ? -1 : 0), by + 1, 0, 22, x == 0 ? 1 : 3);
				
				if (x == 6) {
					spawn(ObjectConstants.BANK_GROUND_D_TILE, bx - x, by, 0, 22, 0);
					spawn(ObjectConstants.BANK_GROUND_D_TILE, bx - x, by + 1, 0, 22, 0);
				}
			} else {
				spawn(ObjectConstants.BANK_GROUND_D_TILE, bx - x, by, 0, 22, 0);
				spawn(ObjectConstants.BANK_GROUND_D_TILE, bx - x, by + 1, 0, 22, 0);
			}
			
			if (x != 0) {
				spawn(ObjectConstants.BANK_GROUND_D_CONNECTOR, bx - x, by + 2, 0, 22, 0);
				spawn(11758, bx - x, by + 1, 0, 10, 0);
			}
		}*/
		
		/**
		 * Al kharid
		 */
		spawnWithObject(11601, 3276, 3188, 0, 10, 1);
		
		/**
		 * End New Home
		 */
		/**
		 * Remove guards blocking port sarim from draynor
		 */
		deleteWithObject(3070, 3275, 0);
		deleteWithObject(3070, 3278, 0);
		deleteWithObject(3070, 3277, 0);
		
		/**
		 * Warriors guild portal
		 */
		spawnWithObject(8987, 2878, 3546, 0, 10, 0);
		spawnWithObject(8987, 2858, 3544, 0, 10, 0);
		/**
		 * End Warriors guild portal
		 */
		
		/**
		 * Mini-game portal
		 */
		spawnWithObject(13628, 3084, 3490, 0, 10, 1);
		
		/**
		 * Camelot spinning wheel
		 */
		spawnWithObject(4309, 2713, 3472, 0, 10, 3);
		
		/**
		 * Tormented demons pick-lock doors near Mages' guild
		 */
		deleteWithObject(3106, 3958, 0);
		deleteWithObject(3105, 3958, 0);
		deleteWithObject(3106, 3958, 0);
		spawnWithObject(2552, 3106, 3958, 0, 0, 1);
		spawnWithObject(2552, 3105, 3958, 0, 0, 1);
		spawnWithObject(882, 3105, 3955, 0, 10, 2);
		
		// ground decorations
		/*
		 * for (int i = 0; i < 7; i++) { for (int k = 0; k < 3; k++) { int obj =
		 * ObjectConstants.BANK_GROUND_D_TILE; int face = 0;
		 * 
		 * if (i == 5 && k == 0 || i == 0 && k == 0) {
		 * spawnWithObject(ObjectConstants.BANK_DECORATIVE_ROPE, bx + k, by, 0,
		 * ObjectConstants.BDR_TYPE, 1); }
		 * 
		 * if (i == 0 || i == 6) { if (k == 2) { obj =
		 * ObjectConstants.BANK_GROUND_D_EDGE; if (i == 0) { face = 2; } else {
		 * face = 1; } } else { obj = ObjectConstants.BANK_GROUND_D_CONNECTOR;
		 * if (i == 0) { face = 2; } else { face = 0; } } } else if (k == 1) {
		 * spawnWithObject(11758, bx + k, by, 0, 10, 1); } else if (k == 2) {
		 * obj = ObjectConstants.BANK_GROUND_D_CONNECTOR; face = 1; }
		 * 
		 * spawnWithObject(obj, bx + k, by, 0, 22, face); }
		 * 
		 * by++; }
		 */

		// summon obelisk
		spawn(29954, 3085, 3506, 0, 10, 0);

		// new home

		// END KELDAGRIM EAST BANK

		// crystal chest
		spawn(2191, 2609, 3090, 0, 10, 1);

		// fireplace at fishing guild
		spawn(6096, 2591, 3414, 0, 10, 0);

		// edgeville - new home
		// spawn(13641, 3090, 3495, 0, 10, 0);
		// spawn(882, 3084, 3493, 0, 10, 2);
		spawn(2191, 3091, 3492, 0, 10, 1);

		// catherby bank sink
		spawn(873, 2806, 3438, 0, 10, 2);

		// fally mine rune
		spawn(45070, 3024, 9738, 0, 10, 0);
		spawn(45070, 3025, 9737, 0, 10, 0);
		spawn(45070, 3025, 9741, 0, 10, 0);
		spawn(45070, 3026, 9741, 0, 10, 0);
		spawn(45070, 3027, 9741, 0, 10, 0);
		spawn(45070, 3028, 9741, 0, 10, 0);
		spawn(45070, 3030, 9741, 0, 10, 0);
		// addy
		spawn(31085, 3036, 9743, 0, 10, 0);
		spawn(31085, 3037, 9744, 0, 10, 1);
		spawn(31085, 3037, 9746, 0, 10, 3);
		spawn(31085, 3037, 9747, 0, 10, 2);
		spawn(31085, 3039, 9749, 0, 10, 2);
		spawn(31085, 3040, 9750, 0, 10, 1);
		spawn(31085, 3051, 9750, 0, 10, 1);
		spawn(31085, 3052, 9749, 0, 10, 2);
		spawn(31085, 3053, 9748, 0, 10, 0);
		spawn(31085, 3054, 9747, 0, 10, 3);

		// stalls
		spawn(6163, 3043, 4964, 1, 10, 0);
		spawn(17031, 3041, 4964, 1, 10, 0);
		spawn(6166, 3039, 4965, 1, 10, 1);
		spawn(6165, 3039, 4967, 1, 10, 1);
		spawn(6164, 3040, 4970, 1, 10, 1);
		spawn(6162, 3040, 4972, 1, 10, 1);

		// new home flax
		/*
		 * for (int y = 3488; y <= 3493; y++) { spawn(2646, 3101, y, 0, 10, 0);
		 * } for (int y = 3488; y <= 3493; y++) { spawn(2646, 3102, y, 0, 10,
		 * 0); } for (int y = 3488; y <= 3493; y++) { spawn(2646, 3103, y, 0,
		 * 10, 0); } for (int y = 3488; y <= 3493; y++) { spawn(2646, 3104, y,
		 * 0, 10, 0); }
		 */

		// CATHERBY
		// flax
		for (int y = 3439; y <= 3445; y++) {
			spawn(2646, 2814, y, 0, 10, 0);
		}
		for (int y = 3439; y <= 3445; y++) {
			spawn(2646, 2813, y, 0, 10, 0);
		}
		for (int y = 3447; y <= 3453; y++) {
			spawn(2646, 2802, y, 0, 10, 0);
		}
		for (int y = 3447; y <= 3453; y++) {
			spawn(2646, 2801, y, 0, 10, 0);
		}

		// spinning wheels
		spawn(4309, 2806, 3440, 0, 10, 3);
		spawn(4309, 2806, 3441, 0, 10, 3);

		// spawn(3044, 2813, 3454, 0, 10, 1);
		// spawn(4306, 2812, 3450, 0, 10, 0);
		// spawn(4306, 2812, 3452, 0, 10, 0);

		// spawn(882, 2817, 3454, 0, 10, 3);

		spawn(6096, 2806, 3435, 0, 10, 2);

		spawn(11601, 2818, 3440, 0, 10, 2);

		// spawn(1293, 2789, 3428, 0, 10, 2);

		// fally mine
		spawn(3044, 3054, 9771, 0, 10, 2);
		spawn(4306, 3049, 9771, 0, 10, 0);
		spawn(4306, 3049, 9773, 0, 10, 0);

		// spawnWithObject(11758, 2817, 3451, 0, 10, 1);

		// END CATHERBY

		// mith rocks
		/*
		 * spawn(2102, 2832, 10171, 0, 10, 0); spawn(2102, 2829, 10162, 0, 10,
		 * 0); spawn(2102, 2824, 10166, 0, 10, 0); spawn(2102, 2832, 10171, 0,
		 * 10, 0); spawn(2102, 2820, 10164, 0, 10, 0);
		 */
		// addy
		/*
		 * spawn(2105, 2818, 10166, 0, 10, 0); spawn(2105, 2818, 10167, 0, 10,
		 * 0);
		 */
		// rune
		/*
		 * spawn(45070, 2817, 10168, 0, 10, 0); spawn(45070, 2817, 10169, 0, 10,
		 * 0); spawn(45070, 2817, 10170, 0, 10, 0); spawn(45070, 2818, 10171, 0,
		 * 10, 0); spawn(45070, 2818, 10172, 0, 10, 0);
		 */

		// WARR GUILD
		spawn(1738, 2853, 3535, 0, 10, 0);// stairs
		spawn(2213, 2850, 3544, 0, 10, 1);// bank booth

		// edge rfd
		spawn(13405, 3209, 9622, 0, 10, 0);// portal
		spawn(12309, 3219, 9623, 0, 10, 3);// chest

		// KELDAGRIM EAST SKILLING AREA
		spawn(6164, 2886, 10191, 0, 10, 1);// silver stall new area
		spawn(17031, 2894, 10210, 0, 10, 0);// crossbow stall new area
		spawn(6096, 2890, 10190, 0, 10, 1);// fire pit

		// EDGEVILLE
		spawn(2640, 3091, 3506, 0, 10, 2);
		//spawn(2469, 3091, 3498, 0, 10, 0);
		
		//portal to pvp edgeville
		spawnWithObject(13627, 3084, 3496, 0, 10, 1);

		//zaros altar
		//spawnWithObject(47120, 3104, 3503, 0, 10, 0);
		
		/*
		 * spawn(13641, 2835, 10214, 0, 10, 0); spawn(2640, 2834, 10210, 0, 10,
		 * 1);
		 */

		/*
		 * spawn(2213, 2871, 10174, 0, 10, 1); spawn(2213, 2871, 10175, 0, 10,
		 * 1); spawn(1293, 2843, 10222, 0, 10, 0); spawn(2805, 2820, 10200, 0,
		 * 10, 3); spawn(10090, 2871, 10161, 0, 10, 3); spawn(10090, 2864,
		 * 10166, 0, 10, 2); //spawn(882, 2844, 10205, 0, 10, 2); //spawn(882,
		 * 2840, 10184, 0, 10, 1); spawn(5264, 3161, 4236, 0, 10, 0);
		 * //spawn(882, 2856, 10191, 0, 10, 1); //spawn(882, 2836, 10234, 0, 10,
		 * 1); spawn(882, 2867, 10247, 0, 10, 0); spawn(882, 2839, 10152, 0, 10,
		 * 1); spawn(5264, 3106, 4388, 0, 10, 1); spawn(882, 2852, 10211, 0, 10,
		 * 2); spawn(11601, 2895, 10187, 0, 10, 2); spawn(2642, 2895, 10190, 0,
		 * 10, 0); spawn(882, 2940, 10234, 0, 10, 2); spawn(882, 2937, 10199, 0,
		 * 10, 0); spawn(882, 2937, 10199, 0, 10, 0); spawn(2806, 2933, 10159,
		 * 0, 10, 1);
		 */

		// rocks
		/*
		 * spawn(2090, 2852, 10160, 0, 10, 1); spawn(2090, 2851, 10161, 0, 10,
		 * 3); spawn(2094, 2851, 10168, 0, 10, 0); spawn(2094, 2852, 10169, 0,
		 * 10, 2); spawn(2092, 2855, 10168, 0, 10, 2); spawn(2092, 2856, 10167,
		 * 0, 10, 3); spawn(2092, 2855, 10161, 0, 10, 0); spawn(2092, 2857,
		 * 10161, 0, 10, 0); spawn(2096, 2859, 10163, 0, 10, 0); spawn(2096,
		 * 2861, 10164, 0, 10, 2); spawn(2096, 2860, 10170, 0, 10, 0);
		 * spawn(2098, 2862, 10171, 0, 10, 0); spawn(2098, 2865, 10171, 0, 10,
		 * 0); spawn(2092, 2861, 10164, 0, 10, 0); spawn(2096, 2858, 10168, 0,
		 * 10, 0); spawn(2092, 2837, 10169, 0, 10, 2); spawn(2092, 2835, 10162,
		 * 0, 10, 0); spawn(2092, 2831, 10162, 0, 10, 1); spawn(2096, 2828,
		 * 10171, 0, 10, 1); spawn(2096, 2831, 10171, 0, 10, 1); spawn(2096,
		 * 2834, 10169, 0, 10, 3); spawn(2096, 2841, 10165, 0, 10, 3);
		 * spawn(2098, 2839, 10169, 0, 10, 2); spawn(2098, 2826, 10172, 0, 10,
		 * 2); spawn(2098, 2825, 10163, 0, 10, 1); spawn(2096, 2827, 10165, 0,
		 * 10, 3); spawn(2096, 2827, 10167, 0, 10, 3); spawn(2092, 2829, 10171,
		 * 0, 10, 1); spawn(2092, 2828, 10162, 0, 10, 1);
		 */

		// high-level rocks
		/*
		 * spawn(2102, 2822, 10163, 0, 10, 1); spawn(2105, 2822, 10163, 0, 10,
		 * 0); spawn(2105, 2819, 10173, 0, 10, 2); spawn(2102, 2824, 10167, 0,
		 * 10, 1); spawn(2102, 2826, 10163, 0, 10, 1);
		 */
		// HOME END
		
		setClipToZero(3445, 3554, 2);
		setClipToZero(3445, 3555, 2);
		
		setClipToZero(2851, 5332, 2);
		setClipToZero(2851, 5333, 2);
		setClipToZero(2851, 5334, 2);
		setClipToZero(2850, 5332, 2);
		setClipToZero(2850, 5333, 2);
		setClipToZero(2850, 5334, 2);
		
		setClipToZero(2656, 2592, 0);

		// delete(2763, 9316, 0);
		// delete(2762, 9316, 0);
		// delete(2763, 9315, 0);
		// delete(2762, 9315, 0);

		setClipToZero(2763, 9316, 0);
		setClipToZero(2762, 9316, 0);
		setClipToZero(2763, 9315, 0);
		setClipToZero(2762, 9315, 0);

		setClipToZero(3253, 9236, 0);
		
		setClipToZero(3445, 3554, 2);
		setClipToZero(3445, 3555, 2);

		// delete(2891, 10195, 0);
		// delete(2890, 10195, 0);
		// delete(2890, 10194, 0);

		// delete(3096, 3479, 0);
		// delete(3104, 3494, 0);

		// delete(3426, 3555, 1);
		// delete(3427, 3555, 1);
		// delete(3426, 3556, 1);
		// delete(3427, 3556, 1);
		setClipToZero(3426, 3555, 1);
		setClipToZero(3427, 3555, 1);
		setClipToZero(3426, 3556, 1);
		setClipToZero(3427, 3556, 1);
		
		//to draynor from port sarim
		setClipToZero(3070, 3278, 0);
		setClipToZero(3070, 3277, 0);
		setClipToZero(3070, 3276, 0);
		setClipToZero(3070, 3275, 0);
		setClipToZero(3069, 3278, 0);
		setClipToZero(3069, 3277, 0);
		setClipToZero(3069, 3276, 0);
		setClipToZero(3069, 3275, 0);
		
		//al kharid
		setClipToZero(3280, 3185, 0);
		setClipToZero(3279, 3185, 0);
		setClipToZero(3278, 3191, 0);
		setClipToZero(3277, 3191, 0);
		setClipToZero(3277, 3191, 0);
		
		/**
		 * Fix lumbridge smelt place clipping
		 */
		setClipToZero(3221, 3256, 0);
		setClipToZero(3221, 3255, 0);
		setClipToZero(3221, 3254, 0);
		setClipToZero(3221, 3253, 0);
		setClipToZero(3222, 3256, 0);
		setClipToZero(3222, 3255, 0);
		setClipToZero(3222, 3254, 0);
		setClipToZero(3222, 3253, 0);
		
		/**
		 * Fix slayer tower entrance clipping
		 */
		deleteWithObject(3428, 3536, 0);
		deleteWithObject(3429, 3536, 0);
		setClipToZero(3428, 3535, 0);
		setClipToZero(3429, 3535, 0);
		
		spawnWithObject(4469, 1832, 5215, 0, 0, 0);

		for (GameObject i : active) {
			send(i);
		}

		System.out.println("Object spawns loaded.");
	}
}
