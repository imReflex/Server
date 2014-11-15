package org.endeavor.engine.definitions;

import java.util.Arrays;

import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.Projectile;
import org.endeavor.game.entity.item.Item;

public class CombatSpellDefinition {

	private String name;
	private int id;
	private byte baseMaxHit;
	private double baseExperience;
	private Animation animation;
	private Graphic start;
	private Projectile projectile;
	private Graphic end;
	private byte level;
	private int[] weapon;
	private Item[] runes;

	@Override
	public String toString() {
		return "CombatSpellDefinition [name=" + name + ", id=" + id + ", level=" + level + ", runes="
				+ Arrays.toString(runes) + "]";
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public int getBaseMaxHit() {
		return baseMaxHit;
	}

	public double getBaseExperience() {
		return baseExperience;
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

	public int getLevel() {
		return level;
	}

	public int[] getWeapons() {
		return weapon;
	}

	public Item[] getRunes() {
		return runes;
	}
}
