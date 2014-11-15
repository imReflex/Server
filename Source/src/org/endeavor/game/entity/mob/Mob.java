package org.endeavor.game.entity.mob;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.endeavor.engine.definitions.NpcCombatDefinition;
import org.endeavor.engine.definitions.NpcCombatDefinition.Magic;
import org.endeavor.engine.definitions.NpcCombatDefinition.Melee;
import org.endeavor.engine.definitions.NpcCombatDefinition.Ranged;
import org.endeavor.engine.definitions.NpcDefinition;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.task.impl.MobDeathTask;
import org.endeavor.engine.task.impl.forcewalk.MobWalkTask;
import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.content.combat.Hit;
import org.endeavor.game.content.combat.impl.AccuracyFormulas;
import org.endeavor.game.content.minigames.barrows.Barrows;
import org.endeavor.game.content.minigames.dungeoneering.DungGame;
import org.endeavor.game.content.minigames.warriorsguild.AnimatorConstants;
import org.endeavor.game.content.skill.summoning.FamiliarMob;
import org.endeavor.game.content.sounds.MobSounds;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.following.Following;
import org.endeavor.game.entity.following.impl.MobFollowing;
import org.endeavor.game.entity.mob.impl.CorporealBeast;
import org.endeavor.game.entity.mob.impl.DesertStrykeworm;
import org.endeavor.game.entity.mob.impl.KalphiteQueen;
import org.endeavor.game.entity.mob.impl.Kreearra;
import org.endeavor.game.entity.mob.impl.Revenant;
import org.endeavor.game.entity.mob.impl.SeaTrollQueen;
import org.endeavor.game.entity.mob.impl.TormentedDemon;
import org.endeavor.game.entity.movement.MovementHandler;
import org.endeavor.game.entity.movement.impl.MobMovementHandler;
import org.endeavor.game.entity.player.Player;

/**
 * Represents a single Mob in the in-game world
 * 
 * @author Michael Sasse
 * 
 */
public class Mob extends Entity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2340277435805006093L;

	/**
	 * Constructs a new MovementHandler instance
	 */
	private MovementHandler movementHandler = new MobMovementHandler(this);
	
	/**
	 * Constructs a new MobFollowing instance
	 */
	private MobFollowing following = new MobFollowing(this);
	
	/**
	 * The spawn location of the MOB
	 */
	private final Location spawnLocation;
	
	/**
	 * The mob walks around
	 */
	private final boolean walks;
	
	/**
	 * The id of the mob
	 */
	private short npcId;
	
	/**
	 * The original id for transforming mobs
	 */
	private final short originalNpcId;
	
	/**
	 * The id of the mob to transform into
	 */
	private short transformId = -1;

	/**
	 * If the mob is visible or not
	 */
	private boolean visible = true;
	
	/**
	 * A transform update required
	 */
	private boolean transformUpdate = false;
	
	/**
	 * A force walk update required
	 */
	private boolean forceWalking = false;

	/**
	 * The mob needs placement
	 */
	private boolean placement = false;

	private byte combatIndex = 0;
	private final boolean attackable;
	private boolean shouldRespawn = true;
	private final boolean face;
	private final boolean noFollow;
	private byte faceDir;
	private final boolean lockFollow;
	private final Player owner;
	private VirtualMobRegion virtualRegion = null;
	private List<Player> combatants;
	private boolean attacked = false;
	private boolean movedLastCycle = false;

	/**
	 * Spawns the boss mobs
	 */
	public static void spawnBosses() {
		new CorporealBeast();//IS ALWAYS FIRST TO SPAWN!
		//rest don't matter
		
		new SeaTrollQueen();
		new Kreearra();
		TormentedDemon.spawn();
		Revenant.spawn();
		DesertStrykeworm.spawn();
		new KalphiteQueen();
	}

	/**
	 * Constructs a new Mob
	 * 
	 * @param npcId
	 *            The id of the mob
	 * @param walks
	 *            The mob can walk or not
	 * @param location
	 *            The location of the mob
	 * @param owner
	 *            The owner of the mob
	 * @param shouldRespawn
	 *            The mob should respawn after dying
	 * @param lockFollow
	 *            Themob should follow
	 * @param virtualRegion
	 *            The virtual region the mob is in
	 */
	public Mob(int npcId, boolean walks, Location location, Player owner, boolean shouldRespawn, boolean lockFollow,
			VirtualMobRegion virtualRegion) {
		originalNpcId = ((short) npcId);
		this.npcId = ((short) npcId);
		this.walks = walks;
		this.virtualRegion = virtualRegion;
		this.owner = owner;
		this.lockFollow = lockFollow;
		this.shouldRespawn = shouldRespawn;
		face = MobConstants.face(npcId);
		noFollow = MobConstants.noFollow(this);

		getLocation().setAs(location);
		spawnLocation = new Location(location);

		setSize(getDefinition().getSize());

		movementHandler.resetMoveDirections();

		setNpc(true);

		updateCombatType();

		Walking.setNpcOnTile(this, true);
		World.register(this);

		getUpdateFlags().setUpdateRequired(true);

		attackable = getDefinition().isAttackable();

		setActive(true);

		if (attackable) {
			if (getCombatDefinition() != null) {
				setBonuses(getCombatDefinition().getBonuses().clone());
				NpcCombatDefinition.Skill[] skills = getCombatDefinition().getSkills();
				if (skills != null) {
					int[] skill = new int[25];

					for (int i = 0; i < skills.length; i++) {
						skill[skills[i].getId()] = skills[i].getLevel();
					}

					setLevels(skill.clone());
					setMaxLevels(skill.clone());
				}

			}

			if (inMultiArea())
				combatants = new ArrayList<Player>();
			else
				combatants = null;
		} else {
			combatants = null;
		}

		if (npcId == 8725)
			faceDir = 4;
		else if ((npcId == 553) && (location.getX() == 3091) && (location.getY() == 3497))
			faceDir = 4;
		else
			faceDir = -1;
		
		setRetaliate(attackable);
	}

	public Mob(int npcId, boolean walks, Location location) {
		this(npcId, walks, location, null, true, false, null);
	}

	public Mob(int npcId, boolean walks, Location location, VirtualMobRegion r) {
		this(npcId, walks, location, null, true, false, r);
	}

	public Mob(Player owner, int npcId, boolean walks, boolean shouldRespawn, boolean lockFollow, Location location) {
		this(npcId, walks, location, owner, shouldRespawn, lockFollow, null);
	}

	public Mob(Player owner, VirtualMobRegion region, int npcId, boolean walks, boolean shouldRespawn,
			boolean lockFollow, Location location) {
		this(npcId, walks, location, owner, shouldRespawn, lockFollow, region);
	}

	public Mob(VirtualMobRegion virtualRegion, int npcId, boolean walks, boolean shouldRespawn, Location location) {
		this(npcId, walks, location, null, shouldRespawn, false, virtualRegion);
	}
	
	/**
	 * Resets the mobs levels
	 */
	public void resetLevels() {
		if (getCombatDefinition() != null) {
			setBonuses(getCombatDefinition().getBonuses().clone());
			NpcCombatDefinition.Skill[] skills = getCombatDefinition().getSkills();
			if (skills != null) {
				int[] skill = new int[25];

				for (int i = 0; i < skills.length; i++) {
					skill[skills[i].getId()] = skills[i].getLevel();
				}

				setLevels(skill.clone());
				setMaxLevels(skill.clone());
			}

		}
	}

	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}

	/**
	 * Un-transforms a mob
	 */
	public void unTransform() {
		if (originalNpcId != npcId)
			transform(npcId);
	}

	/**
	 * Adds a new combatant to the mobs list
	 * 
	 * @param p
	 *            The player to add
	 */
	public void addCombatant(Player p) {
		if (combatants == null) {
			combatants = new ArrayList<Player>();
		}

		if (!combatants.contains(p))
			combatants.add(p);
	}

	/**
	 * Teleports the mob to a new location
	 * 
	 * @param p
	 *            The location to teleport the mob too
	 */
	public void teleport(Location p) {
		Walking.setNpcOnTile(this, false);
		getMovementHandler().getLastLocation().setAs(new Location(p.getX(), p.getY() + 1));
		getLocation().setAs(p);
		Walking.setNpcOnTile(this, true);
		placement = true;
		getMovementHandler().resetMoveDirections();
	}

	/**
	 * The mob shoul walk to home
	 * 
	 * @return If the mob can walk to home or not
	 */
	public boolean isWalkToHome() {
		if ((npcId == 6260) || (npcId == 6261) || (npcId == 6263) || (npcId == 6265) || (npcId == 6203)
				|| (npcId == 6204) || (npcId == 6206) || (npcId == 6208) || (npcId == 6222) || (npcId == 6227)
				|| (npcId == 6225) || (npcId == 6223) || (npcId == 6247) || (npcId == 6250) || (npcId == 6248)
				|| (npcId == 6252)) {
			return false;
		}

		if ((getFollowing().isIgnoreDistance()) || (owner != null)) {
			return false;
		}

		if (attackable) {
			return Math.abs(getLocation().getX() - spawnLocation.getX()) + Math.abs(getLocation().getY() - spawnLocation.getY()) > getSize() * 2 + 6;
		}
		return Misc.getManhattanDistance(spawnLocation, getLocation()) > 2;
	}

	/**
	 * If the entity is within the mobs walking distance
	 * 
	 * @param e
	 *            The entity the mob is following
	 * @return
	 */
	public boolean withinMobWalkDistance(Entity e) {
		if ((following.isIgnoreDistance()) || (owner != null)) {
			return true;
		}

		return Math.abs(e.getLocation().getX() - spawnLocation.getX()) + Math.abs(e.getLocation().getY() - spawnLocation.getY()) < getSize() * 2 + 6;
	}

	/**
	 * Removes the mob from the in-game world
	 */
	public void remove() {
		if ((Barrows.isBarrowsBrother(this)) && (owner != null)) {
			owner.getAttributes().remove("barrowsActive" + npcId);
			owner.getCombat().resetCombatTimer();
		}

		if ((AnimatorConstants.isAnimatedArmour(npcId)) && (owner != null)) {
			owner.getAttributes().remove("warriorGuildAnimator");
		}

		visible = false;
		setActive(false);
		World.unregister(this);
		Walking.setNpcOnTile(this, false);

		if (virtualRegion != null) {
			virtualRegion = null;
		}
	}

	/**
	 * The mob retreats from combat
	 */
	public void retreat() {
		if (getCombat().getAttacking() != null) {
			forceWalking = true;
			getCombat().reset();
			TaskQueue.queue(new MobWalkTask(this, new Location(getX() + 5, getY() + 5), false));
		}
	}

	@Override
	public void process() throws Exception {
		if ((owner != null) && (!owner.isActive())) {
			remove();
			return;
		}

		if ((attackable) || ((this instanceof FamiliarMob))) {
			if (forceWalking)
				return;
			if (isDead()) {
				getCombat().reset();
				return;
			}

			if (inMultiArea()) {
				if (combatants == null) {
					combatants = new ArrayList<Player>();
				}

				if (combatants.size() > 0) {
					if (getCombat().getAttacking() == null)
						combatants.clear();
					else {
						for (Iterator<Player> i = combatants.iterator(); i.hasNext();) {
							Player p = (Player) i.next();
							if ((!p.getLocation().isViewableFrom(getLocation())) || (!p.getCombat().inCombat())
									|| (p.isDead())) {
								i.remove();
							}
						}
					}
				}
			}

			doAliveMobProcessing();

			if ((getCombat().getAttackTimer() <= 1) && (getCombat().getAttacking() != null)) {
				updateCombatType();
			}

			if (isWalkToHome()) {
				getCombat().reset();
				getFollowing().reset();
				TaskQueue.queue(new MobWalkTask(this, spawnLocation, true));
			} else if ((!isDead()) && (getCombat().getAttacking() == null) && (!getFollowing().isFollowing()) && (walks) && (!forceWalking)) {
				RandomMobChatting.handleRandomMobChatting(this);
				Walking.randomWalk(this);
			}

			if ((!forceWalking) && (!isDead())) {
				following.process();
				getCombat().process();
			}
		} else if ((!isDead()) && (!attackable) && (walks) && (!following.isFollowing()) && (!forceWalking)) {
			Walking.randomWalk(this);
		} else if (!forceWalking) {
			following.process();
		}
	}

	/**
	 * Actions taken on death for custom npcs
	 */
	public void onDeath() {}

	/**
	 * Processes custom npcs while they are alive
	 */
	public void doAliveMobProcessing() {}
	
	/**
	 * processes the movement of custom npcs
	 */
	public void processMovement(){}
	
	@Override
	public void retaliate(Entity attacked) {
		if (!getCombat().inCombat()) {
			getCombat().setAttack(attacked);
		}
	}

	@Override
	public void hit(Hit hit) {
		if (!canTakeDamage()) {
			return;
		}

		if (isDead())
			hit.setDamage(0);
		else {
			hit.setDamage(getAffectedDamage(hit));
		}

		if ((npcId == 2883) && ((hit.getType() == Hit.HitTypes.MELEE) || (hit.getType() == Hit.HitTypes.RANGED))) {
			hit.setDamage(0);
		}

		if ((npcId == 2881) && ((hit.getType() == Hit.HitTypes.MAGIC) || (hit.getType() == Hit.HitTypes.RANGED))) {
			hit.setDamage(0);
		}

		if ((npcId == 2882) && ((hit.getType() == Hit.HitTypes.MAGIC) || (hit.getType() == Hit.HitTypes.MELEE))) {
			hit.setDamage(hit.getDamage() / 10);
		}

		if (hit.getDamage() > getLevels()[3])
			hit.setDamage(getLevels()[3]);
		getLevels()[3] = ((short) (getLevels()[3] - hit.getDamage()));

		if (!getUpdateFlags().isHitUpdate())
			getUpdateFlags().sendHit(hit.getDamage(), hit.getHitType(), hit.getCombatHitType());
		else {
			getUpdateFlags().sendHit2(hit.getDamage(), hit.getHitType(), hit.getCombatHitType());
		}

		if (hit.getAttacker() != null) {
			getCombat().getDamageTracker().addDamage(hit.getAttacker(), hit.getDamage());

			if (getCombat().getAttacking() == null && isRetaliate() || !inMultiArea() && isRetaliate()) {
				getCombat().setAttack(hit.getAttacker());
			}

			if (inMultiArea() && (attackable) && isRetaliate()) {
				if (((attacked) && (hit.getAttacker() != getCombat().getAttacking())) || ((!attacked) && (!movedLastCycle) && (!getCombat().withinDistanceForAttack(getCombat().getCombatType(), true)))) {
					getCombat().setAttack(hit.getAttacker());
					attacked = false;
				}

				if (!hit.getAttacker().isNpc()) {
					Player p = World.getPlayers()[hit.getAttacker().getIndex()];
					if (p != null) {
						MobSounds.sendBlockSound(p, npcId);

						addCombatant(p);

						if (getDungGame() != null)
							DungGame.forHit(p, hit.getDamage());
					}
				}
			} else if ((!isDead()) && (!hit.getAttacker().isNpc())) {
				Player p = World.getPlayers()[hit.getAttacker().getIndex()];
				if (p != null) {
					MobSounds.sendBlockSound(p, npcId);

					if (getDungGame() != null) {
						DungGame.forHit(p, hit.getDamage());
					}
				}
			}

			doPostHitProcessing(hit);
		}

		if (!isDead()) {
			checkForDeath();
		}

		if (hit.getAttacker() != null)
			hit.getAttacker().onHit(this, hit);
	}

	/**
	 * Gets the affected hit damage
	 * 
	 * @param hit
	 *            The hit dealt to the mob
	 * @return
	 */
	public int getAffectedDamage(Hit hit) {
		return hit.getDamage();
	}

	public void doPostHitProcessing(Hit hit) {}

	@Override
	public void onHit(Entity e, Hit hit) {
		if ((e.isDead()) && (inMultiArea()) && (!e.isNpc())) {
			if (combatants == null) {
				combatants = new ArrayList<Player>();
			}

			combatants.remove(World.getPlayers()[e.getIndex()]);
		}
	}

	@Override
	public boolean canAttack() {
		Entity attacking = getCombat().getAttacking();

		if ((!inMultiArea()) || (!attacking.inMultiArea())) {
			if ((getCombat().inCombat()) && (getCombat().getLastAttackedBy() != getCombat().getAttacking())) {
				return false;
			}

			if ((attacking.getCombat().inCombat()) && (attacking.getCombat().getLastAttackedBy() != this)) {
				return false;
			}

		}

		if (!attacking.isNpc()) {
			Player p = World.getPlayers()[attacking.getIndex()];

			if ((p != null) && (!p.getController().canAttackNPC())) {
				return false;
			}

		}

		return true;
	}

	@Override
	public MovementHandler getMovementHandler() {
		return movementHandler;
	}

	@Override
	public Following getFollowing() {
		return following;
	}

	@Override
	public void reset() {
		movedLastCycle = (getMovementHandler().getPrimaryDirection() != -1);
		getMovementHandler().resetMoveDirections();
		getFollowing().updateWaypoint();
		getUpdateFlags().reset();
		placement = false;
	}

	/**
	 * Gets the mobs death animation
	 * @return
	 */
	public Animation getDeathAnimation() {
		return getCombatDefinition() != null ? getCombatDefinition().getDeath() : new Animation(9055, 0);
	}

	@Override
	public void updateCombatType() {
		NpcCombatDefinition def = getCombatDefinition();

		if (def == null) {
			return;
		}

		CombatTypes combatType = CombatTypes.MELEE;

		switch (def.getCombatType()) {
		case MAGIC:
			combatType = CombatTypes.MAGIC;
			break;
		case MELEE_AND_MAGIC:
			if (!getCombat().withinDistanceForAttack(CombatTypes.MELEE, true) || Misc.randomNumber(2) == 1) {
				combatType = CombatTypes.MAGIC;
			} else {
				combatType = CombatTypes.MELEE;
			}
			break;
		case MELEE_AND_RANGED:
			if (!getCombat().withinDistanceForAttack(CombatTypes.MELEE, true) || Misc.randomNumber(2) == 1) {
				combatType = CombatTypes.RANGED;
			} else {
				combatType = CombatTypes.MELEE;
			}
			break;
		case RANGED:
			combatType = CombatTypes.RANGED;
			break;
		case RANGED_AND_MAGIC:
			if (!getCombat().withinDistanceForAttack(CombatTypes.RANGED, true) || Misc.randomNumber(2) == 1) {
				combatType = CombatTypes.MAGIC;
			} else {
				combatType = CombatTypes.RANGED;
			}
			break;
		case ALL:
			if (!getCombat().withinDistanceForAttack(CombatTypes.MELEE, true)) {
				int roll = Misc.randomNumber(2);
				if (getCombat().withinDistanceForAttack(CombatTypes.RANGED, true) && roll == 0)
					combatType = CombatTypes.RANGED;
				else
					combatType = CombatTypes.MAGIC;
				break;
			}

			int roll = Misc.randomNumber(3);

			if (roll == 0) {
				combatType = CombatTypes.MAGIC;
			} else if (roll == 1) {
				combatType = CombatTypes.RANGED;
			} else if (roll == 2) {
				combatType = CombatTypes.MELEE;
			}
			break;
		default:
			break;
		}

		getCombat().setCombatType(combatType);
		getCombat().setBlockAnimation(def.getBlock());

		switch (combatType) {
		case MELEE:
			if (def.getMelee() == null || def.getMelee().length < 1) {
				remove();
				System.out.println("Null combat def error:melee for npc: " + npcId);
				return;
			}

			combatIndex = (byte) Misc.randomNumber(def.getMelee().length);
			Melee melee = def.getMelee()[combatIndex];
			getCombat().getMelee().setAttack(melee.getAttack(), melee.getAnimation());
			break;
		case MAGIC:
			if (def.getMagic() == null || def.getMagic().length < 1) {
				remove();
				System.out.println("Null combat def error:magic for npc: " + npcId);
				return;
			}

			combatIndex = (byte) Misc.randomNumber(def.getMagic().length);
			Magic magic = def.getMagic()[combatIndex];
			getCombat().getMagic().setAttack(magic.getAttack(), magic.getAnimation(), magic.getStart(), magic.getEnd(),
					magic.getProjectile());
			break;
		case RANGED:
			if (def.getRanged() == null || def.getRanged().length < 1) {
				remove();
				System.out.println("Null combat def error:ranged for npc: " + npcId);
				return;
			}

			combatIndex = (byte) Misc.randomNumber(def.getRanged().length);
			Ranged ranged = def.getRanged()[combatIndex];
			getCombat().getRanged().setAttack(ranged.getAttack(), ranged.getAnimation(), ranged.getStart(),
					ranged.getEnd(), ranged.getProjectile());
			break;
		}
	}

	@Override
	public void afterCombatProcess(Entity attack) {
		if (attack.isDead())
			getCombat().reset();
		else
			MobAbilities.executeAbility(npcId, this, attack);
	}

	@Override
	public void onCombatProcess(Entity attack) {
		if ((npcId == 8133) && (getCombat().getCombatType() == CombatTypes.MELEE) && (combatIndex == 0)) {
			getUpdateFlags().sendGraphic(new Graphic(1834, 0, false));
		}

		attacked = true;

		if ((inMultiArea()) && (!attack.isNpc())) {
			Player p = World.getPlayers()[attack.getIndex()];

			if (p != null) {
				MobSounds.sendBlockSound(p, npcId);
				addCombatant(p);
			}
		} else if (!attack.isNpc()) {
			Player p = World.getPlayers()[attack.getIndex()];

			if (p != null)
				MobSounds.sendAttackSound(p, npcId, getCombat().getCombatType(), getLastDamageDealt() > 0);
		}
	}

	@Override
	public void onAttack(Entity attack, int hit, CombatTypes type, boolean success) {
	}

	@Override
	public int getAccuracy(Entity attack, CombatTypes type) {
		return AccuracyFormulas.getAccuracy(this, attack, type);
	}

	@Override
	public int getMaxHit(CombatTypes type) {
		if (getCombatDefinition() == null) {
			return 1;
		}

		switch (type) {
		case MAGIC:
			if (getCombatDefinition().getMagic() != null) {
				return getCombatDefinition().getMagic()[combatIndex].getMax();
			}
			break;
		case MELEE:
			if (getCombatDefinition().getMelee() != null) {
				int max = getCombatDefinition().getMelee()[combatIndex].getMax();
				if (npcId == 2026) {
					return (int) (max * (1.0D + (2.0D - getLevels()[3] / getMaxLevels()[3])));
				}
				return max;
			}

			break;
		case RANGED:
			if (getCombatDefinition().getRanged() != null) {
				return getCombatDefinition().getRanged()[combatIndex].getMax();
			}
			break;
		}
		return 1;
	}

	@Override
	public int getCorrectedDamage(int damage) {
		return damage;
	}

	@Override
	public boolean isIgnoreHitSuccess() {
		return false;
	}

	@Override
	public void checkForDeath() {
		if (getLevels()[3] <= 0) {
			TaskQueue.queue(new MobDeathTask(this));

			if (combatants != null)
				combatants.clear();
		}
	}

	/**
	 * Transforms the mob
	 * 
	 * @param id
	 *            The id to transform the mob too
	 */
	public void transform(int id) {
		transformUpdate = true;
		transformId = ((short) id);
		npcId = ((short) id);
	}

	/**
	 * Gets the mobs id
	 * 
	 * @return The mobs id
	 */
	public int getId() {
		return npcId;
	}

	/**
	 * Sets the mob's visibility status
	 * 
	 * @param isVisible
	 *            The mob is visible or not
	 */
	public void setVisible(boolean isVisible) {
		visible = isVisible;
	}

	/**
	 * Gets if the mob is visible or not
	 * 
	 * @return
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Sets if the mob should respawn after death
	 * 
	 * @param state
	 *            The mob should respawn or not
	 */
	public void setRespawnable(boolean state) {
		shouldRespawn = state;
	}

	/**
	 * Should the mob respawn after death
	 * @return
	 * The mob should respawn
	 */
	public boolean shouldRespawn() {
		return shouldRespawn;
	}
	
	/**
	 * Gets if the mob is requesting a transform update
	 * 
	 * @return the mob is transforming
	 */
	public boolean isTransformUpdate() {
		return transformUpdate;
	}

	/**
	 * Sets the mobs transform status
	 * 
	 * @param transformUpdate
	 */
	public void setTransformUpdate(boolean transformUpdate) {
		this.transformUpdate = transformUpdate;
	}

	/**
	 * Gets the mobs transform id
	 * 
	 * @return
	 */
	public int getTransformId() {
		return transformId;
	}

	public void setTransformId(int transformId) {
		this.transformId = ((short) transformId);
	}

	public Location getSpawnLocation() {
		return spawnLocation;
	}

	public Location getNextSpawnLocation() {
		return spawnLocation;
	}

	public NpcDefinition getDefinition() {
		return GameDefinitionLoader.getNpcDefinition(npcId);
	}

	public static NpcDefinition getDefinition(int id) {
		return GameDefinitionLoader.getNpcDefinition(id);
	}

	public NpcCombatDefinition getCombatDefinition() {
		return GameDefinitionLoader.getNpcCombatDefinition(npcId);
	}

	public void setForceWalking(boolean walkingHome) {
		forceWalking = walkingHome;
	}

	public int getRespawnTime() {
		if (getCombatDefinition() != null) {
			return getCombatDefinition().getRespawnTime();
		}
		return 50;
	}

	public Player getOwner() {
		return owner;
	}

	public boolean isLockFollow() {
		return (owner != null) && (lockFollow);
	}

	public boolean face() {
		return face;
	}

	public boolean isPlacement() {
		return placement;
	}

	public void setPlacement(boolean placement) {
		this.placement = placement;
	}

	public boolean isNoFollow() {
		return noFollow;
	}

	public boolean inVirtualRegion() {
		return virtualRegion != null;
	}

	public VirtualMobRegion getVirtualRegion() {
		return virtualRegion;
	}

	public NpcCombatDefinition getCombatDefinition(int id) {
		return GameDefinitionLoader.getNpcCombatDefinition(id);
	}

	public int getFaceDirection() {
		return faceDir;
	}

	public List<Player> getCombatants() {
		if (combatants == null) {
			combatants = new ArrayList<Player>();
		}

		return combatants;
	}

	public void setFaceDir(int face) {
		faceDir = ((byte) face);
	}

	public int getCombatIndex() {
		return combatIndex;
	}

	public boolean isMovedLastCycle() {
		return movedLastCycle;
	}
	
}
