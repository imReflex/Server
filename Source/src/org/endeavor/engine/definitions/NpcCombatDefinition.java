package org.endeavor.engine.definitions;

import org.endeavor.game.content.combat.impl.Attack;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.Projectile;

public class NpcCombatDefinition {

	public enum CombatTypes {
		MELEE, RANGED, MAGIC, MELEE_AND_RANGED, MELEE_AND_MAGIC, RANGED_AND_MAGIC, ALL
	}

	private short id;
	private CombatTypes combatType;
	private short respawnTime;
	private Animation block;
	private Animation death;
	private Skill[] skills;
	private int[] bonuses;
	private Melee[] melee;
	private Magic[] magic;
	private Ranged[] ranged;

	public int getId() {
		return id;
	}

	public CombatTypes getCombatType() {
		return combatType;
	}

	public Animation getBlock() {
		return block;
	}

	public Animation getDeath() {
		return death;
	}

	public short getRespawnTime() {
		return respawnTime;
	}

	public Skill[] getSkills() {
		return skills;
	}

	public int[] getBonuses() {
		return bonuses;
	}

	public Melee[] getMelee() {
		return melee;
	}

	public Magic[] getMagic() {
		return magic;
	}

	public Ranged[] getRanged() {
		return ranged;
	}

	public class Melee {

		private Attack attack;
		private Animation animation;
		private byte max;

		public Attack getAttack() {
			return attack;
		}

		public Animation getAnimation() {
			return animation;
		}

		public int getMax() {
			return max;
		}
	}

	/**
	 * Holds the values for magic based attacks for npcs
	 * 
	 * @author Michael Sasse
	 * 
	 */
	public class Magic {

		private Attack attack;
		private Animation animation;
		private Graphic start;
		private Projectile projectile;
		private Graphic end;
		private byte max;

		public Attack getAttack() {
			return attack;
		}

		public Animation getAnimation() {
			return animation;
		}

		public Graphic getStart() {
			return start;
		}

		public Projectile getProjectile() {
			return projectile;
		}

		public Graphic getEnd() {
			return end;
		}

		public int getMax() {
			return max;
		}
	}

	public class Ranged {

		private Attack attack;
		private Animation animation;
		private Graphic start;
		private Projectile projectile;
		private Graphic end;
		private byte max;

		public Attack getAttack() {
			return attack;
		}

		public Animation getAnimation() {
			return animation;
		}

		public Graphic getStart() {
			return start;
		}

		public Projectile getProjectile() {
			return projectile;
		}

		public Graphic getEnd() {
			return end;
		}

		public int getMax() {
			return max;
		}
	}

	public class Skill {

		private int id;
		private int level;

		public int getId() {
			return id;
		}

		public int getLevel() {
			return level;
		}
	}
}
