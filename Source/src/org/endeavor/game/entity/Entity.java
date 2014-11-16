package org.endeavor.game.entity;

import java.io.Serializable;
import java.util.LinkedList;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.task.impl.RegenerateSkillTask;
import org.endeavor.game.content.combat.Combat;
import org.endeavor.game.content.combat.CombatInterface;
import org.endeavor.game.content.combat.Hit;
import org.endeavor.game.content.minigames.armsrace.ARConstants;
import org.endeavor.game.content.minigames.dungeoneering.DungGame;
import org.endeavor.game.content.minigames.pestcontrol.PestControlGame;
//import org.endeavor.game.content.skill.construction.MapInstance;
import org.endeavor.game.entity.following.Following;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.mob.impl.Revenant;
import org.endeavor.game.entity.movement.MovementHandler;
import org.endeavor.game.entity.player.Player;

/**
 * Represents a single combatable entity
 * 
 * @author Michael Sasse
 * 
 */
public abstract class Entity implements CombatInterface, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5362055320741938506L;

	/**
	 * The player index mod
	 */
	public static final int PLAYER_INDEX_MOD = 32768;

	/**
	 * The index of the entity
	 */
	private short index = -1;

	/**
	 * The size of the entity
	 */
	private byte size = 1;

	/**
	 * The last damage dealt by the entity
	 */
	private byte lastDamageDealt = 0;

	/**
	 * The entities max levels
	 */
	private short[] maxLevels = new short[25];

	/**
	 * The entities current levels
	 */
	private short[] levels = new short[25];

	/**
	 * The entities current bonuses
	 */
	private short[] bonuses = new short[18];

	/**
	 * the last attacked entity
	 */
	private Entity lastAttacked;

	/**
	 * Dunno what this is for
	 */
	private int consecutiveAttacks;
	private transient DungGame dungGame = null;
	private double rangedWeaknessMod;
	private double magicWeaknessMod;
	private double meleeWeaknessMod;

	/**
	 * A list of tasks
	 */
	private final LinkedList<Task> tasks = new LinkedList<Task>();

	/**
	 * The entity is an npc
	 */
	private boolean npc = true;

	/**
	 * The entity is active
	 */
	private boolean active = false;

	/**
	 * The entity is dead
	 */
	private boolean dead = false;

	/**
	 * The entity can take damage
	 */
	private boolean takeDamage = true;

	/**
	 * The entity can retaliate
	 */
	private boolean retaliate = true;

	/**
	 * The last hit success
	 */
	private boolean lastHitSuccess = false;

	/**
	 * The entity is poisoned
	 */
	private boolean poisoned = false;

	/**
	 * The current poison damage
	 */
	private int poisonDamage = 0;

	/**
	 * The teleblock time
	 */
	private int teleblockTime;

	/**
	 * The freeze delay
	 */
	private long freeze;

	/**
	 * The immunity from being frozen
	 */
	private long iceImmunity;

	/**
	 * The immunity from being poisoned again
	 */
	private long poisonImmunity;

	/**
	 * The immunity from being hit
	 */
	private long hitImmunity = 0L;

	/**
	 * The immunity from fire
	 */
	private long fireImmunity = 0;

	/**
	 * the location of the entity
	 */
	private final Location location = new Location(0, 0, 0);

	/**
	 * Constructs an update flags instance
	 */
	private transient UpdateFlags updateFlags = new UpdateFlags();

	/**
	 * Constructs a new combat instance
	 */
	private transient Combat combat = new Combat(this);

	/**
	 * Constructs a new attributes instance
	 */
	private transient Attributes attributes = new Attributes();
	
	/**
	 * Construction map instance
	 */
	//private transient MapInstance mapInstance = null;
	
	
	public final void initEntity() {
		updateFlags = new UpdateFlags();
		combat = new Combat(this);
		attributes = new Attributes();
	//	mapInstance = null;
	}

	/**
	 * The entities individual process
	 * 
	 * @throws Exception
	 */
	public abstract void process() throws Exception;
	
	public abstract void retaliate(Entity attacked);

	/**
	 * Get the current movement handler instance
	 * 
	 * @return
	 */
	public abstract MovementHandler getMovementHandler();

	/**
	 * Gets the current following instance
	 * 
	 * @return
	 */
	public abstract Following getFollowing();

	/**
	 * Resets the entitys updates
	 */
	public abstract void reset();
	
	public Player getPlayer() {
		return npc ? null : World.getPlayers()[index];
	}
	
	public Mob getMob() {
		return !npc ? null : World.getNpcs()[index];
	}

	/**
	 * Gets the entities x coordinate
	 * 
	 * @return
	 */
	public int getX() {
		return location.getX();
	}

	/**
	 * Gets the entities y coordinate
	 * 
	 * @return
	 */
	public int getY() {
		return location.getY();
	}

	/**
	 * Gets the entities z coordinate
	 * 
	 * @return
	 */
	public int getZ() {
		return location.getZ();
	}

	/**
	 * Performs a consecutive attack
	 * 
	 * @param attacking
	 */
	public void doConsecutiveAttacks(Entity attacking) {
		if (lastAttacked == null) {
			consecutiveAttacks = 1;
			lastAttacked = attacking;
			return;
		}

		if (lastAttacked.equals(attacking)) {
			consecutiveAttacks += 1;
		} else {
			consecutiveAttacks = 0;
			lastAttacked = attacking;
		}
	}

	/**
	 * Provides fire immunity for a set amount of seconds
	 * 
	 * @param delayInSeconds
	 *            The delay of the immunity
	 */
	public void doFireImmunity(int delayInSeconds) {
		fireImmunity = System.currentTimeMillis() + (delayInSeconds * 1000);
	}

	/**
	 * The entity has fire immunity
	 * 
	 * @return
	 */
	public boolean hasFireImmunity() {
		return fireImmunity > System.currentTimeMillis() || (getAttributes().get("fire_resist") != null && (Boolean) getAttributes().get("fire_resist")) || (getAttributes().get("super_fire_resist") != null && (Boolean) getAttributes().get("super_fire_resist"));
	}

	/**
	 * The entity has super fire immunity
	 * 
	 * @return
	 */
	public boolean hasSuperFireImmunity() {
		return (getAttributes().get("super_fire_resist") != null && (Boolean) getAttributes()
				.get("super_fire_resist"));
	}

	/**
	 * The entity has attacked consecutively
	 * 
	 * @param check
	 * @param req
	 * @return
	 */
	public boolean hasAttackedConsecutively(Entity check, int req) {
		return (lastAttacked != null) && (lastAttacked.equals(check))
				&& (consecutiveAttacks >= req);
	}

	@Override
	public boolean equals(Object other) {
		if ((other instanceof Entity)) {
			Entity e = (Entity) other;
			return (e.getIndex() == index) && (e.isNpc() == npc);
		}

		return false;
	}

	/**
	 * Sets the hit immunity in seconds
	 * 
	 * @param delay
	 *            The seconds the entity is immune to hits
	 */
	public void setHitImmunityDelay(int delay) {
		hitImmunity = (System.currentTimeMillis() + delay * 1000);
	}

	/**
	 * Gets if the entity is immune to a hit
	 * @return
	 */
	public boolean isImmuneToHit() {
		return System.currentTimeMillis() < hitImmunity;
	}

	/**
	 * Starts a regeneration task
	 */
	public void startRegeneration() {
		TaskQueue.queue(new RegenerateSkillTask(this, 75));
	}

	/**
	 * Resets the entities levels
	 */
	public void resetLevels() {
		for (int i = 0; i < 25; i++) {
			levels[i] = maxLevels[i];
		}

		if (!npc) {
			Player p = World.getPlayers()[index];

			if (p != null) {
				p.getSkill().update();
			}
		}
	}

	/**
	 * Resets the entities combat stats
	 */
	public void resetCombatStats() {
		Player p = null;
		if (!npc) {
			p = World.getPlayers()[index];
		}

		for (int i = 0; i <= 7; i++) {
			levels[i] = maxLevels[i];
			if (!npc)
				p.getSkill().update(i);
		}
	}

	/**
	 * Gets if the entity is immune to ice
	 * @return
	 */
	public boolean isImmuneToIce() {
		return iceImmunity > System.currentTimeMillis();
	}

	/**
	 * The entity is immune
	 * @return
	 */
	public boolean isFrozen() {
		return freeze > System.currentTimeMillis();
	}

	/**
	 * Freezes an entity for a certain time
	 * 
	 * @param time
	 *            The time to freeze the entity
	 * @param immunity
	 *            The time the entity is immune to being frozen again
	 */
	public void freeze(double time, int immunity) {
		if ((isFrozen()) || (isImmuneToIce())) {
			return;
		}

		freeze = (long) (System.currentTimeMillis() + (time * 1000));
		iceImmunity = (freeze + 5000);
	}

	/**
	 * Unfreezes an entity
	 */
	public void unfreeze() {
		freeze = 0L;
	}

	/**
	 * Teleblocks an entity for a certain amount of time
	 * @param time
	 * The time to teleblock the entity
	 */
	public void teleblock(int time) {
		if (isTeleblocked()) {
			return;
		}
		
		teleblockTime = time;
		
		tickTeleblock();
	}
	
	/**
	 * Ticks the teleblock
	 */
	public void tickTeleblock() {
		TaskQueue.queue(new Task(this, 1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 8856682907667807991L;

			@Override
			public void execute() {
				if (--teleblockTime <= 0) {
					teleblockTime = 0;
					stop();
				}
			}

			@Override
			public void onStop() {}
			
		});
	}

	/**
	 * poisons an entity
	 * @param start
	 */
	public void poison(int start) {
		if ((poisoned) || (World.getCycles() < poisonImmunity)) {
			return;
		}

		poisoned = true;
		poisonDamage = start;

		TaskQueue.queue(new Task(this, 30) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 9195629421148844933L;
			int count = 0;

			@Override
			public void execute() {
				if ((!poisoned) || (poisonDamage <= 0)) {
					stop();
					return;
				}

				hit(new Hit(poisonDamage, Hit.HitTypes.POISON));

				if (++count == 4) {
					poisonDamage -= 1;
					count = 0;
					if (poisonDamage == 0)
						stop();
				}
			}

			@Override
			public void onStop() {
			}
		});
	}

	/**
	 * Cures poison
	 * @param immunity
	 */
	public void curePoison(int immunity) {
		poisoned = false;
		poisonDamage = 0;
		if (immunity > 0)
			poisonImmunity = (World.getCycles() + immunity);
	}

	/**
	 * Forces an entity to face a direction
	 * 
	 * @param entity
	 */
	public void face(Entity entity) {
		if (entity == null) {
			return;
		}

		if (!entity.isNpc())
			updateFlags.faceEntity(entity.getIndex() + 32768);
		else
			updateFlags.faceEntity(entity.getIndex());
	}

	/**
	 * Gets if an entity is in an area
	 * 
	 * @param bottomLeft
	 *            The bottom left corner
	 * @param topRight
	 *            The top right corner
	 * @param heightSupport
	 *            Should the height be checked
	 * @return
	 */
	public boolean inArea(Location bottomLeft, Location topRight, boolean heightSupport) {
		if ((heightSupport) && (location.getZ() != bottomLeft.getZ()))
			return false;
		return (location.getX() >= bottomLeft.getX()) && (location.getX() <= topRight.getX())
				&& (location.getY() >= bottomLeft.getY()) && (location.getY() <= topRight.getY());
	}
	
	public boolean inSafePK() {
		return getX() >= 2754 && getY() >= 5504 && getX() <= 2880 && getY() <= 5583;
	}
	
	public boolean isOverSafePKBoundary(int y) {
		return inSafePK() && y >= 5575;
	}

	/**
	 * The entity is in a multi area
	 * @return
	 */
	public boolean inMultiArea() {
		if (dungGame != null) {
			return true;
		}
		
		if (attributes.get(PestControlGame.PEST_GAME_KEY) != null) {
			return true;
		}

		if (inGodwars()) {
			return true;
		}

		int x = location.getX();
		int y = location.getY();
		int z = location.getZ();
		if (inArea(new Location(2686, 2690, 0), new Location(2825, 2816, 0), false)) {
			return true;
		}

		if (Revenant.withinRevenant(this)) {
			return true;
		}

		if (!npc) {
			Player p = World.getPlayers()[index];
			if ((p != null) && (p.getController().equals(ARConstants.AR_CONTROLLER))) {
				return true;
			}
		}

		return  x >= 2333 && y >= 3687 && x <= 2362 && y <= 3717
				|| x >= 3306 && y >= 9364 && x <= 3332 && y <= 9392
				|| ((x >= 2956) && (y >= 3714) && (x <= 3006) && (y <= 3750))
				|| ((x <= 2049) && (y <= 5251) && (x >= 1980) && (y >= 5178))
				|| ((x >= 2893) && (y >= 4430) && (x <= 2934) && (y <= 4471))
				|| ((x >= 1761) && (y >= 5337) && (x <= 1780) && (y <= 5370))
				|| ((x >= 3029) && (x <= 3374) && (y >= 3759) && (y <= 3903))
				|| ((x >= 2250) && (x <= 2280) && (y >= 4670) && (y <= 4720))
				|| ((x >= 3198) && (x <= 3380) && (y >= 3904) && (y <= 3970))
				|| ((x >= 3191) && (x <= 3326) && (y >= 3510) && (y <= 3759))
				|| ((x >= 2987) && (x <= 3006) && (y >= 3912) && (y <= 3937))
				|| ((x >= 2245) && (x <= 2295) && (y >= 4675) && (y <= 4720))
				//|| ((x >= 2450) && (x <= 3520) && (y >= 9450) && (y <= 9550))
				|| ((x >= 3006) && (x <= 3071) && (y >= 3602) && (y <= 3710))
				|| ((x >= 3134) && (x <= 3192) && (y >= 3519) && (y <= 3646))
				|| ((z > 0) && (x >= 3460) && (x <= 3545) && (y >= 3150) && (y <= 3267))
				|| ((x >= 2369) && (x <= 2425) && (y >= 5058) && (y <= 5122))
				|| ((x >= 3241) && (y >= 9353) && (x <= 3256) && (y <= 9378))
				|| ((x >= 2914) && (y >= 4359) && (x <= 2972) && (y <= 4412));
	}

	/**
	 * The entity is in the home bank
	 * 
	 * @return
	 */
	public boolean inHomeBank() {
		int x = getLocation().getX();
		int y = getLocation().getY();

		return (x >= 3090) && (y >= 3488) && (x <= 3098) && (y <= 3500);
	}

	/**
	 * The entity is in godwars
	 * 
	 * @return
	 */
	public boolean inGodwars() {
		return inArea(new Location(2816, 5243, 2), new Location(2960, 5400, 2), false);
	}

	/**
	 * The entity is in the wilderness
	 * 
	 * @return
	 */
	public boolean inWilderness() {
		int x = location.getX();
		int y = location.getY();
	//	if (getLocation().inKingBlackDragonArea()) {
			/*
			 * May disable, sets kbd area in wild
			 */
			//return true;
		//}
		if ((x <= 3279) && (y <= 3695) && (x >= 3264) && (y >= 3672)) {
			return false;
		}

		return ((x >= 2944) && (x < 3392) && (y >= 3525) && (y < 4026))
				|| ((y > 9919) && (y < 10009) && (x > 3065) && (x < 3151));
	}
	
	
	public static boolean inWilderness(int x, int y) {
	//	if (getLocation().inKingBlackDragonArea()) {
			/*
			 * May disable, sets kbd area in wild
			 */
			//return true;
		//}
		if ((x <= 3279) && (y <= 3695) && (x >= 3264) && (y >= 3672)) {
			return false;
		}

		return ((x >= 2944) && (x < 3392) && (y >= 3525) && (y < 4026))
				|| ((y > 9919) && (y < 10009) && (x > 3065) && (x < 3151));
	}

	/**
	 * The entity is in duel arena
	 * 
	 * @return
	 */
	public boolean inDuelArena() {
		return (location.getX() >= 3325) && (location.getX() <= 3396) && (location.getY() >= 3199)
				&& (location.getY() <= 3289);
	}

	/**
	 * Gets the wilderness level the entity is in
	 * 
	 * @return
	 */
	public int getWildernessLevel() {
		/**
		 * Sets the wilderness level for kbd area to 42
		 */
		//if (getLocation().inKingBlackDragonArea()) {
		//	return 42;
		//}
		int level = -1;
		if ((location.getY() > 9928) && (location.getY() < 10009) && (location.getX() > 3065) && (location.getX() < 3151))
			level = (location.getY() - 9928) / 8 + 1;
		else {
			level = (location.getY() - 3525) / 8 + 1;
		}
		return level;
	}

	/**
	 * Adds a bonus to the bonuses
	 * 
	 * @param id
	 *            The bonus id
	 * @param add
	 *            The amount to add to the bonus
	 */
	public void addBonus(int id, int add) {
		int tmp5_4 = id;
		short[] tmp5_1 = bonuses;
		tmp5_1[tmp5_4] = ((short) (tmp5_1[tmp5_4] + add));
	}

	/**
	 * Gets the size of the entity
	 * 
	 * @return
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Sets the size of the entity
	 * 
	 * @param size
	 */
	public void setSize(int size) {
		this.size = ((byte) size);
	}

	/**
	 * Gets the entities index
	 * 
	 * @return
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Sets the entities index
	 * 
	 * @param index
	 *            The index to place the npc
	 */
	public void setIndex(int index) {
		this.index = ((short) index);
	}

	/**
	 * Gets if the entity is an npc
	 * 
	 * @return
	 */
	public boolean isNpc() {
		return npc;
	}

	/**
	 * Sets if the entity is an npc
	 * 
	 * @param npc
	 */
	public void setNpc(boolean npc) {
		this.npc = npc;
	}

	/**
	 * Gets if the entity is dead
	 * 
	 * @return
	 */
	public boolean isDead() {
		return dead;
	}

	/**
	 * Sets the entity dead
	 * 
	 * @param dead
	 */
	public void setDead(boolean dead) {
		this.dead = dead;
	}

	/**
	 * Gets if the entity can take damage
	 * 
	 * @return
	 */
	public boolean canTakeDamage() {
		return takeDamage;
	}

	/**
	 * Sets if the entity can take damage
	 * 
	 * @param takeDamage
	 */
	public void setTakeDamage(boolean takeDamage) {
		this.takeDamage = takeDamage;
	}

	/**
	 * Gets if the entity is active
	 * 
	 * @return
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets if the entity is active
	 * 
	 * @param active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Gets if the entity can retaliate
	 * 
	 * @return
	 */
	public boolean isRetaliate() {
		return retaliate;
	}

	/**
	 * Sets if the entity can retalite
	 * 
	 * @param retaliate
	 */
	public void setRetaliate(boolean retaliate) {
		this.retaliate = retaliate;
	}

	/**
	 * Gets the entities location
	 * 
	 * @return
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Gets the entities levels
	 * 
	 * @return
	 */
	public short[] getLevels() {
		return levels;
	}

	/**
	 * Sets the entities levels
	 * 
	 * @param levels
	 */
	public void setLevels(int[] levels) {
		for (int i = 0; i < levels.length; i++)
			this.levels[i] = ((short) levels[i]);
	}

	/**
	 * Gets the entities max levels
	 * 
	 * @return
	 */
	public short[] getMaxLevels() {
		return maxLevels;
	}

	/**
	 * Sets the entities max levels
	 * 
	 * @param maxLevels
	 */
	public void setMaxLevels(int[] maxLevels) {
		for (int i = 0; i < maxLevels.length; i++)
			this.maxLevels[i] = ((short) maxLevels[i]);
	}

	/**
	 * Gets the entities update flags
	 * 
	 * @return
	 */
	public UpdateFlags getUpdateFlags() {
		return updateFlags;
	}

	/**
	 * Gets the combat instance
	 * 
	 * @return
	 */
	public Combat getCombat() {
		return combat;
	}

	/**
	 * The entity is teleblocked
	 * 
	 * @return
	 */
	public boolean isTeleblocked() {
		return teleblockTime > 0;
	}

	/**
	 * Gets the bonuses for the entity
	 * 
	 * @return
	 */
	public short[] getBonuses() {
		return bonuses;
	}

	/**
	 * Sets the bonuses for the entity
	 * 
	 * @param bonuses
	 */
	public void setBonuses(int[] bonuses) {
		if(this.bonuses.length < 18)
			this.bonuses = new short[18];
		for (int i = 0; i < bonuses.length; i++)
			this.bonuses[i] = ((short) bonuses[i]);
	}

	/**
	 * Gets the last damage dealt
	 * 
	 * @return
	 */
	public int getLastDamageDealt() {
		return lastDamageDealt;
	}

	/**
	 * Sets the last damage dealt
	 * 
	 * @param lastDamageDealt
	 */
	public void setLastDamageDealt(int lastDamageDealt) {
		this.lastDamageDealt = ((byte) lastDamageDealt);
	}

	/**
	 * The entity is poisoned
	 * 
	 * @return
	 */
	public boolean isPoisoned() {
		return poisoned;
	}

	/**
	 * Gets the entities attributes
	 * 
	 * @return
	 */
	public Attributes getAttributes() {
		return attributes;
	}

	/**
	 * Gets the poison damage
	 * 
	 * @return
	 */
	public int getPoisonDamage() {
		return poisonDamage;
	}

	/**
	 * Sets the poison damage
	 * 
	 * @param poisonDamage
	 */
	public void setPoisonDamage(int poisonDamage) {
		this.poisonDamage = poisonDamage;
	}

	/**
	 * Gets a list of tasks
	 * 
	 * @return
	 */
	public LinkedList<Task> getTasks() {
		return tasks;
	}

	/**
	 * Gets the dung game
	 * 
	 * @return
	 */
	public DungGame getDungGame() {
		return dungGame;
	}

	/**
	 * Sets the dung game
	 * 
	 * @param dungGame
	 */
	public void setDungGame(DungGame dungGame) {
		this.dungGame = dungGame;
	}

	/**
	 * Gets if the last hit was successful
	 * 
	 * @return
	 */
	public boolean wasLastHitSuccess() {
		return lastHitSuccess;
	}

	/**
	 * Sets the last hit successful
	 * 
	 * @param lastHitSuccess
	 */
	public void setLastHitSuccess(boolean lastHitSuccess) {
		this.lastHitSuccess = lastHitSuccess;
	}

	public double getRangedWeaknessMod() {
		return rangedWeaknessMod;
	}

	public void setRangedWeaknessMod(double rangedWeaknessMod) {
		this.rangedWeaknessMod = rangedWeaknessMod;
	}

	public double getMagicWeaknessMod() {
		return magicWeaknessMod;
	}

	public void setMagicWeaknessMod(double magicWeaknessMod) {
		this.magicWeaknessMod = magicWeaknessMod;
	}

	public double getMeleeWeaknessMod() {
		return meleeWeaknessMod;
	}

	public void setMeleeWeaknessMod(double meleeWeaknessMod) {
		this.meleeWeaknessMod = meleeWeaknessMod;
	}

	/**
	 * Gets the current teleblock time
	 * 
	 * @return
	 */
	public int getTeleblockTime() {
		return teleblockTime;
	}

	/**
	 * Sets the teleblock time
	 * 
	 * @param teleblockTime
	 *            The time to teleblock the entity
	 */
	public void setTeleblockTime(int teleblockTime) {
		this.teleblockTime = teleblockTime;
	}

	/*public MapInstance getMapInstance() {
		return mapInstance;
	}*/

	/*public void setMapInstance(MapInstance mapInstance) {
		this.mapInstance = mapInstance;
	}*/
	
}
