package org.endeavor.game.entity.following.impl;

import org.endeavor.engine.definitions.NpcCombatDefinition;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.GameConstants;
import org.endeavor.game.content.combat.Combat;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.following.Following;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.pathfinding.SimplePathWalker;

/**
 * Handles mob following
 * @author Michael Sasse
 *
 */
public class MobFollowing extends Following {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3231455377939443518L;
	public static final String NEXT_DIR_KEY = "nextfollowdir";
	private final Mob mob;

	public MobFollowing(Mob mob) {
		super(mob);
		this.mob = mob;
	}

	@Override
	public void process() {
		if (following != null)
			if (mob.isNoFollow())
				reset();
			else
				follow();
	}

	@Override
	public boolean pause() {
		if (GameConstants.withinBlock(following.getX(), following.getY(), following.getSize(), mob.getX(), mob.getY(),
				mob.getSize())) {
			return false;
		}

		if (this.type == Following.FollowType.COMBAT) {
			if (mob.getCombatDefinition() != null) {
				NpcCombatDefinition.CombatTypes type = mob.getCombatDefinition().getCombatType();

				if (type == NpcCombatDefinition.CombatTypes.MELEE)
					return mob.getCombat().withinDistanceForAttack(Combat.CombatTypes.MELEE, false);
				if (type == NpcCombatDefinition.CombatTypes.RANGED) {
					return mob.getCombat().withinDistanceForAttack(Combat.CombatTypes.RANGED, false);
				}
				
				return mob.getCombat().withinDistanceForAttack(Combat.CombatTypes.MAGIC, false);
			} else {
				return mob.getCombat().withinDistanceForAttack(mob.getCombat().getCombatType(), false);
			}
		}

		int x = entity.getLocation().getX();
		int y = entity.getLocation().getY();
		int x2 = following.getLocation().getX();
		int y2 = following.getLocation().getY();

		Location[] a = GameConstants.getBorder(x, y, entity.getSize());
		Location[] b = GameConstants.getBorder(x2, y2, following.getSize());

		for (Location i : a) {
			for (Location k : b) {
				if ((Math.abs(i.getX() - k.getX()) < 2) && (Math.abs(i.getY() - k.getY()) < 2)) {
					return true;
				}
			}

		}

		return false;
	}

	@Override
	public boolean outOfRange() {
		if (ignoreDistance) {
			return false;
		}

		if ((!mob.getCombat().inCombat()) && (mob.isWalkToHome())) {
			return true;
		}

		return !isWithinDistance();
	}

	@Override
	public void findPath(Location location) {
		if ((mob.isLockFollow()) && (Misc.getManhattanDistance(mob.getLocation(), mob.getOwner().getLocation()) > 10)) {
			onCannotReach();
		} else if (type == FollowType.DEFAULT && GameConstants.withinBlock(following.getX(), following.getY(), following.getSize(), mob.getX(), mob.getY(), mob.getSize())) {
				if (mob.getAttributes().get("nextfollowdir") == null) {
					Location l = GameConstants.getClearAdjacentLocation(following.getLocation(), mob.getSize());
	
					if (l != null) {
						SimplePathWalker.walkToNextTile(mob, l);
						if (mob.getMovementHandler().getPrimaryDirection() != -1)
							mob.getAttributes().set("nextfollowdir",
									Integer.valueOf(mob.getMovementHandler().getPrimaryDirection()));
					}
				} else {
					int dir = mob.getAttributes().getInt("nextfollowdir");
					SimplePathWalker.walkToNextTile(mob, new Location(mob.getX() + GameConstants.DIR[dir][0], mob.getY()
							+ GameConstants.DIR[dir][1], mob.getZ()));
				}
		} else {
			if (mob.getAttributes().get("nextfollowdir") != null) {
				mob.getAttributes().remove("nextfollowdir");
			}

			SimplePathWalker.walkToNextTile(mob, location);
		}
	}

	@Override
	public void onCannotReach() {
		if (mob.isLockFollow()) {
			Location l = GameConstants.getClearAdjacentLocation(mob.getOwner().getLocation(), mob.getSize());

			if (l != null) {
				mob.teleport(l);
			}

			setFollow(mob.getOwner(), Following.FollowType.DEFAULT);
		} else if (mob.getOwner() != null) {
			mob.remove();
		} else {
			reset();
			if (type == Following.FollowType.COMBAT)
				mob.getCombat().reset();
		}
	}
}
