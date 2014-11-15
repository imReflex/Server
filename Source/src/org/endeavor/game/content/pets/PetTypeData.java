package org.endeavor.game.content.pets;

public enum PetTypeData {
	CAT(new int[] { 1927, 327 }, true), DOG(new int[] { 526, 2132 }, false), RAVEN(new int[] { 313 }, false), RACOON(
			new int[] { 2132, 327 }, false), GECKO(new int[] { 12125 }, false), CRAB(new int[] { 327 }, false), SQUIRREL(
			new int[] { 12130 }, false), PENGUIN(new int[] { 327 }, false), VULTURE(new int[] { 313 }, false), BABY_DRAGON(
			new int[] { 313, 2138, 2132 }, false), MONKEY(new int[] { 1963 }, false), PLATYPUS(new int[] { 327, 313 },
			false);

	public final int[] food;
	public final boolean overgrows;

	private PetTypeData(int[] food, boolean overgrows) {
		this.food = food;
		this.overgrows = overgrows;
	}
}
