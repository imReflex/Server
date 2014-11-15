package org.endeavor.game.content.dwarfcannon;

import org.endeavor.engine.cache.map.RSObject;
import org.endeavor.engine.cache.map.Region;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.task.impl.HitTask;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.content.combat.Hit;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.Projectile;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.object.GameObject;
import org.endeavor.game.entity.object.ObjectManager;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendAnimateObject;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendSound;

public class Cannon extends RSObject {
	public static final String NOT_OWNER = "This is not your cannon!";
	public static final String NOT_ENOUGH_SPACE_FOR_PICKUP = "You do not have enough inventory space to pick up your cannon.";
	public static final String NO_CANNON_BALLS = "You do not have any Cannon balls.";
	public static final String LAST_CANNON_BALLS = "You load the last of your Cannon balls.";
	public static final String YOUR_CANNON_IS_FULL = "Your cannon is full.";
	public static final String YOUR_CANNON_IS_EMPTY = "Your cannon has ran out of Cannon balls.";
	public static int[] ROTATION_DIRECTIONS = { 515, 516, 517, 518, 519, 520, 521, 514 };

	public static int[] DIRECIONS = { 1, 2, 4, 7, 6, 5, 3 };

	public static int[][] ALTERNATE_PATH_MODS = { { 1 }, { 1, -1 }, { 0, -1 }, { -1, -1 }, { -1 }, { -1, 1 }, { 0, 1 },
			{ 1, 1 } };
	private final Player owner;
	private final Location loc;
	private final Location pLoc;
	private int balls = 0;

	private byte stage = 1;

	private boolean notify = true;

	private int dir = 7;

	public Cannon(Player owner, int x, int y, int z) {
		super(x - 1, y - 1, z, 7, 10, 0);
		this.owner = owner;

		Region.getRegion(x, y).addObject(this);
		ObjectManager.register(getGameObject());

		loc = new Location(x - 1, y - 1, z);
		pLoc = new Location(x, y, z);
	}

	public void tick() {
		if (stage != 4) {
			return;
		}

		dir = (dir == 7 ? 0 : dir + 1);

		if (!loc.isViewableFrom(owner.getLocation())) {
			return;
		}

		if (balls == 0) {
			if (!notify) {
				notify = true;
				owner.getClient().queueOutgoingPacket(new SendMessage("Your cannon has ran out of Cannon balls."));
			}
			return;
		}
		if (notify) {
			notify = false;
		}

		Mob[] mobs = getMobsInPath();

		if (mobs != null)
			for (Mob i : mobs)
				if (i != null) {
					int lockon = i.getIndex() + 1;
					byte offsetX = (byte) ((i.getLocation().getY() - i.getLocation().getY()) * -1);
					byte offsetY = (byte) ((i.getLocation().getX() - i.getLocation().getX()) * -1);
					World.sendProjectile(getCannonFire(), pLoc, lockon, offsetX, offsetY);

					Hit hit = getHit();

					TaskQueue.queue(new HitTask(3, false, hit, i));
					owner.getSkill().addCombatExperience(CombatTypes.RANGED, hit.getDamage());

					if (--balls == 0)
						break;
				}
	}

	public void rotate(Player player) {
		player.getClient().queueOutgoingPacket(new SendAnimateObject(this, ROTATION_DIRECTIONS[dir]));
		player.getClient().queueOutgoingPacket(new SendSound(2139, 10, 0));
	}

	public boolean add(int id) {
		if ((stage == 1) && (id == 8)) {
			owner.getInventory().remove(8);

			stage = ((byte) (stage + 1));
			ObjectManager.removeFromList(getGameObject());

			setId(8);
			Region.getRegion(getX(), getY()).addObject(this);

			ObjectManager.register(getGameObject());

			return true;
		}
		if ((stage == 2) && (id == 10)) {
			owner.getInventory().remove(10);

			stage = ((byte) (stage + 1));
			ObjectManager.removeFromList(getGameObject());

			setId(9);
			Region.getRegion(getX(), getY()).addObject(this);

			ObjectManager.register(getGameObject());

			return true;
		}
		if ((stage == 3) && (id == 12)) {
			owner.getInventory().remove(12);

			stage = ((byte) (stage + 1));
			ObjectManager.removeFromList(getGameObject());

			setId(6);
			Region.getRegion(getX(), getY()).addObject(this);

			ObjectManager.register(getGameObject());

			World.addCannon(this);

			return true;
		}

		return false;
	}

	public void onLogout() {
		if (!pickup(owner, getX(), getY())) {
			for (Item i : getItemsForStage()) {
				owner.getBank().add(i);
			}

			if (balls > 0) {
				owner.getBank().add(2, balls);
			}

			if (stage == 4) {
				World.removeCannon(this);
			}

			owner.getAttributes().remove("dwarfmulticannon");

			Region.getRegion(getX(), getY()).removeObject(this);
			ObjectManager.remove(getGameObject());
		}
	}

	public boolean pickup(Player player, int x, int y) {
		if (isOwner(player)) {
			if (player.getInventory().hasSpaceFor(getItemsForStage())) {
				if ((stage == 4) && (balls > 0) && (!player.getInventory().hasSpaceFor(new Item(2, balls)))) {
					player.getClient().queueOutgoingPacket(
							new SendMessage("You do not have enough inventory space to pick up your cannon."));
					return false;
				}

				if (balls > 0) {
					player.getInventory().add(2, balls, false);
				}

				player.getInventory().add(getItemsForStage(), true);
				Region.getRegion(x, y).removeObject(this);
				ObjectManager.remove(getGameObject());

				player.getAttributes().remove("dwarfmulticannon");

				if (stage == 4) {
					World.removeCannon(this);
				}

				return true;
			}
			player.getClient().queueOutgoingPacket(
					new SendMessage("You do not have enough inventory space to pick up your cannon."));
			return false;
		}

		player.getClient().queueOutgoingPacket(new SendMessage("This is not your cannon!"));
		return true;
	}

	public boolean load(Player player, int item, int obj) {
		if ((item == 2) && (obj == 6)) {
			if (isOwner(player)) {
				if (!player.getInventory().hasItemId(2)) {
					player.getClient().queueOutgoingPacket(new SendMessage("You do not have any Cannon balls."));
				} else {
					int needed = 30 - balls;

					if (needed == 0) {
						player.getClient().queueOutgoingPacket(new SendMessage("Your cannon is full."));
						return true;
					}

					int invBalls = player.getInventory().getItemAmount(2);

					if (invBalls <= needed) {
						player.getInventory().remove(2, invBalls);
						player.getClient().queueOutgoingPacket(
								new SendMessage("You load the last of your Cannon balls."));

						balls += invBalls;
					} else {
						player.getInventory().remove(2, needed);
						player.getClient().queueOutgoingPacket(
								new SendMessage("You load " + needed + " balls into the cannon."));

						balls += needed;
					}
				}
			} else
				player.getClient().queueOutgoingPacket(new SendMessage("This is not your cannon!"));

			return true;
		}

		return false;
	}

	public Item[] getItemsForStage() {
		switch (stage) {
		case 1:
			return new Item[] { new Item(6, 1) };
		case 2:
			return new Item[] { new Item(6, 1), new Item(8, 1) };
		case 3:
			return new Item[] { new Item(6, 1), new Item(8, 1), new Item(10) };
		case 4:
			return new Item[] { new Item(6, 1), new Item(8, 1), new Item(10, 1), new Item(12, 1) };
		}

		return null;
	}

	public Mob[] getMobsInPath() {
		/*
		 * int x = pLoc.getX(); int y = pLoc.getY(); int z = getZ();
		 * 
		 * int dir = this.dir == 7 ? 1 : this.dir == 6 ? 0 : this.dir + 2;
		 * 
		 * int xM = org.endeavor.game.GameConstants.DIR[DIRECIONS[dir]][0]; int
		 * yM = org.endeavor.game.GameConstants.DIR[DIRECIONS[dir]][1];
		 * 
		 * ArrayList xGrid = new ArrayList(); ArrayList yGrid = new ArrayList();
		 * 
		 * int xAltM = ALTERNATE_PATH_MODS[dir][0]; int yAlyM =
		 * ALTERNATE_PATH_MODS[dir][1];
		 * 
		 * for (int i = 0; i < 8; i++) { int x2 = x; int y2 = y; int x3 = x; int
		 * y3 = y;
		 * 
		 * for (int k = 0; k < i; k++) { if (k < i / 2) {
		 * xGrid.add(Integer.valueOf(x2 += xAltM)); yGrid.add(Integer.valueOf(y2
		 * += yAlyM)); } else { xGrid.add(Integer.valueOf(x3 -= xAltM));
		 * yGrid.add(Integer.valueOf(y3 -= yAlyM)); } }
		 * 
		 * xGrid.add(Integer.valueOf(x)); yGrid.add(Integer.valueOf(y));
		 * 
		 * x += xM; y += yM; }
		 * 
		 * ArrayList attack = new ArrayList();
		 * 
		 * for (Iterator<Mob> y2 = owner.getClient().getNpcs().iterator();
		 * y2.hasNext();) { Mob mob = (Mob)y2.next(); int mx =
		 * mob.getLocation().getX(); int my = mob.getLocation().getY();
		 * 
		 * if ((mx == ((Integer)xGrid.get(i)).intValue()) && (my ==
		 * ((Integer)yGrid.get(i)).intValue()) && (mob != null) &&
		 * (!mob.isDead()) && (mob.getDefinition().isAttackable())) { if
		 * (((!owner.getCombat().inCombat()) || (owner.inMultiArea()) ||
		 * ((owner.getCombat().inCombat()) &&
		 * (owner.getCombat().getLastAttackedBy().equals(mob)))) &&
		 * (StraightPathFinder.isProjectilePathClear(getX(), getY(), z,
		 * ((Integer)xGrid.get(i)).intValue(),
		 * ((Integer)yGrid.get(i)).intValue()))) attack.add(mob); } }
		 * 
		 * if (attack.size() == 0) { return null; }
		 * 
		 * Mob[] mob = new Mob[attack.size()];
		 * 
		 * for (int i = 0; i < mob.length; i++) { mob[i] = ((Mob)attack.get(i));
		 * }
		 */
		return null;
	}

	public Projectile getCannonFire() {
		Projectile p = new Projectile(53);
		p.setStartHeight(50);
		p.setEndHeight(50);
		p.setCurve(0);
		return p;
	}

	public Hit getHit() {
		return new Hit(owner, Misc.randomNumber(31), Hit.HitTypes.RANGED);
	}

	public GameObject getGameObject() {
		return new GameObject(getId(), getX(), getY(), getZ(), getType(), getFace());
	}

	public boolean isOwner(Player player) {
		return owner.equals(player);
	}

	public Location getLoc() {
		return loc;
	}
}
