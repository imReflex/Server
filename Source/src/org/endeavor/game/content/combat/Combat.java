package org.endeavor.game.content.combat;

import java.io.Serializable;

import java.security.SecureRandom;

import org.endeavor.engine.utility.Misc;
import org.endeavor.game.GameConstants;
import org.endeavor.game.content.combat.impl.DamageMap;
import org.endeavor.game.content.combat.impl.Magic;
import org.endeavor.game.content.combat.impl.Melee;
import org.endeavor.game.content.combat.impl.Ranged;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.following.Following;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.mob.impl.Revenant;
import org.endeavor.game.entity.pathfinding.StraightPathFinder;

public class Combat implements Serializable {
	private static final long serialVersionUID = -2134539881387544616L;
	private static final SecureRandom random = new SecureRandom();
	private final Entity entity;
	private Entity attacking = null;
	private Entity lastAttackedBy = null;

	private Animation blockAnimation = null;
	private final Melee melee;
	private final Ranged ranged;
	private final Magic magic;
	private final DamageMap damageMap;

	private CombatTypes combatType = CombatTypes.MELEE;

	private long combatTimer = 0L;
	private int attackTimer = 0;

	public Combat(Entity entity) {
		this.entity = entity;
		melee = new Melee(entity);
		ranged = new Ranged(entity);
		magic = new Magic(entity);
		damageMap = new DamageMap(entity);
	}

	public void process() {
		if (attackTimer > 0) {
			attackTimer -= 1;
		}

		if ((attacking != null) && (attackTimer == 0)) {
			attack();
		}

		if ((!entity.isDead()) && (!inCombat()) && (damageMap.isClearHistory()))
			damageMap.clear();
	}

	public void attack() {
		entity.face(attacking);

		if ((!attacking.isActive()) || (attacking.isDead()) || (entity.isDead())
				|| (attacking.getLocation().getZ() != entity.getLocation().getZ())) {
			reset();
			return;
		}

		if (!withinDistanceForAttack(combatType, false)) {
			return;
		}
		
		if (!entity.canAttack()) {
			entity.getFollowing().reset();
			reset();
			return;
		}

		entity.onCombatProcess(attacking);
		switch (combatType) {
		case MELEE:
			melee.execute(attacking);
			melee.setNextDamage(-1);
			melee.setDamageBoost(1.0D);
			break;
		case MAGIC:
			magic.execute(attacking);
			magic.setMulti(false);
			magic.setpDelay((byte) 0);
			break;
		case RANGED:
			ranged.execute(attacking);
			break;
		}

		entity.afterCombatProcess(attacking);
	}

	public boolean withinDistanceForAttack(CombatTypes type, boolean noMovement) {
		if (attacking == null) {
			return false;
		}

		if (type == null) {
			type = combatType;
		}

		int dist = CombatConstants.getDistanceForCombatType(type);

		boolean ignoreClipping = false;

		if (entity.isNpc()) {
			Mob m = World.getNpcs()[entity.getIndex()];
			if (m != null) {
				if (Revenant.isRevenant(m)) {
					if (type != CombatTypes.MELEE) {
						dist = 16;
					}
				} else if (m.getId() == 8596) {
					dist = 18;
					ignoreClipping = true;
				} else if (m.getId() == 3847) {
					if (type == CombatTypes.MELEE) {
						dist = 2;
						ignoreClipping = true;
					}
				}
			}
		}
		
		if (!entity.isNpc()) {
			if (type == CombatTypes.MELEE) {
				if (attacking.isNpc()) {
					Mob m = World.getNpcs()[attacking.getIndex()];
					if (m != null) {
						if (m.getId() == 3847) {
							dist = 2;
							ignoreClipping = true;
						}
					}
				}
			}
		}
		
		if (!noMovement && !entity.isNpc() && !attacking.isNpc() && entity.getMovementHandler().moving()) {
			dist += 3;
		}
		if (!isWithinDistance(dist)) {
			return false;
		}
		if (entity.isNpc()) {
			Mob m = World.getNpcs()[entity.getIndex()];

			if ((m != null) && (Revenant.isRevenant(m))) {
				return true;
			}
		}

		if (!ignoreClipping) {
			if ((type == CombatTypes.MAGIC) || (combatType == CombatTypes.RANGED)) {
				boolean b = false;
				
				for (Location i : GameConstants.getEdges(entity.getLocation().getX(), entity.getLocation().getY(),
						entity.getSize())) {
					if (StraightPathFinder.isProjectilePathClear(i, attacking.getLocation())
							|| StraightPathFinder.isProjectilePathClear(attacking.getLocation(), i)) {
						b = true;
						break;
					}
				}

				if (!b) {
					return false;
				}
			} else if (type == CombatTypes.MELEE) {
				boolean b = false;
				
				for (Location i : GameConstants.getEdges(entity.getLocation().getX(), entity.getLocation().getY(), entity.getSize())) {
					if (StraightPathFinder.isInteractionPathClear(i, attacking.getLocation()) || StraightPathFinder.isInteractionPathClear(attacking.getLocation(), i)) {
						b = true;
						break;
					}
				}

				if (!b) {
					return false;
				}
			}
		}
		return true;
	}

	public void setAttack(Entity e) {
		attacking = e;
		entity.getFollowing().setFollow(e, Following.FollowType.COMBAT);
	}

	public boolean isWithinDistance(int req) {
		if (!entity.isNpc() && !attacking.isNpc()
				&& Misc.getManhattanDistance(attacking.getLocation(), entity.getLocation()) == 0) {
			return false;
		}

		int x = entity.getLocation().getX();
		int y = entity.getLocation().getY();
		int x2 = attacking.getLocation().getX();
		int y2 = attacking.getLocation().getY();

		if (GameConstants.withinBlock(x, y, entity.getSize(), x2, y2)) {
			return true;
		}

		if (Misc.getManhattanDistance(x, y, x2, y2) <= req) {
			return true;
		}

		Location[] a = GameConstants.getBorder(x, y, entity.getSize());
		Location[] b = GameConstants.getBorder(x2, y2, attacking.getSize());

		for (Location i : a) {
			for (Location k : b) {
				if (Misc.getManhattanDistance(i, k) <= req) {
					return true;
				}
			}
		}
		return false;
	}

	public double getDistanceFromTarget() {
		if (attacking == null) {
			return -1.0D;
		}
		return Math.abs(entity.getLocation().getX() - attacking.getLocation().getX())
				+ Math.abs(entity.getLocation().getY() - attacking.getLocation().getY());
	}

	public void updateTimers(int delay) {
		attackTimer = delay;

		if (entity.getAttributes().get("attacktimerpowerup") != null) {
			attackTimer /= 2;
		}
	}

	public void setInCombat(Entity attackedBy) {
		lastAttackedBy = attackedBy;
		
		combatTimer = World.getCycles() 
				+ (attackedBy != null && attackedBy.isNpc() ? 12 : 8);
	}

	public void reset() {
		attacking = null;
		entity.getFollowing().reset();
	}

	public void forRespawn() {
		combatTimer = 0L;
		damageMap.clear();
		attackTimer = 0;
		lastAttackedBy = null;
		entity.setDead(false);
		entity.resetLevels();
	}

	public int getAttackCooldown() {
		switch (combatType) {
		case MAGIC:
			return magic.getAttack().getAttackDelay();
		case MELEE:
			return melee.getAttack().getAttackDelay();
		case RANGED:
			if (ranged == null) {
				return 4;
			}
			return ranged.getAttack().getAttackDelay();
		}
		return 4;
	}

	public void resetCombatTimer() {
		combatTimer = 0L;
	}

	public boolean inCombat() {
		return combatTimer > World.getCycles();
	}

	public void setAttackTimer(int attackTimer) {
		this.attackTimer = attackTimer;
	}

	public Animation getBlockAnimation() {
		return blockAnimation;
	}

	public void setBlockAnimation(Animation blockAnimation) {
		this.blockAnimation = blockAnimation;
	}

	public void setAttacking(Entity attacking) {
		this.attacking = attacking;
	}

	public Entity getLastAttackedBy() {
		return lastAttackedBy;
	}

	public CombatTypes getCombatType() {
		return combatType;
	}

	public void setCombatType(CombatTypes combatType) {
		this.combatType = combatType;
	}

	public Entity getAttacking() {
		return attacking;
	}

	public void increaseAttackTimer(int amount) {
		attackTimer += amount;
	}

	public int getAttackTimer() {
		return attackTimer;
	}

	public Melee getMelee() {
		return melee;
	}

	public Ranged getRanged() {
		return ranged;
	}

	public Magic getMagic() {
		return magic;
	}

	public DamageMap getDamageTracker() {
		return damageMap;
	}

	public static int next(int length) {
		return random.nextInt(length);
	}

	public static enum CombatTypes {
		MELEE, RANGED, MAGIC;
	}
}
