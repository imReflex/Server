package org.endeavor.game.content.skill.herblore;

import java.util.HashMap;
import java.util.Map;

public enum GrimyHerbData {
	GUAM(199, 249, 1, 3), MARRENTILL(201, 251, 5, 4), TARROMIN(203, 253, 11, 5), HARRALANDER(205, 255, 20, 6), RANARR(
			207, 257, 25, 8), TOADFLAX(3049, 2998, 30, 8), SPIRITWEED(12174, 12172, 35, 8), IRIT(209, 259, 40, 9), WERGALI(
			14836, 14854, 30, 8), AVANTOE(211, 261, 48, 10), KWUARM(213, 263, 54, 11), SNAPDRAGON(3051, 3000, 59, 12), CADANTINE(
			215, 265, 65, 13), LANTADYME(2485, 2481, 67, 13), DWARFWEED(217, 267, 70, 14), TORSTOL(219, 269, 75, 15);

	private int grimyHerb;
	private int cleanHerb;
	private int levelReq;
	private int cleaningExp;
	private static Map<Integer, GrimyHerbData> herbs = new HashMap<Integer, GrimyHerbData>();

	private GrimyHerbData(int grimyHerb, int cleanHerb, int levelReq, int cleaningExp) {
		this.grimyHerb = grimyHerb;
		this.cleanHerb = cleanHerb;
		this.levelReq = levelReq;
		this.cleaningExp = cleaningExp;
	}

	public static final void declare() {
		for (GrimyHerbData data : values())
			herbs.put(Integer.valueOf(data.getGrimyHerb()), data);
	}

	public int getGrimyHerb() {
		return grimyHerb;
	}

	public int getCleanHerb() {
		return cleanHerb;
	}

	public int getLevelReq() {
		return levelReq;
	}

	public int getExp() {
		return cleaningExp;
	}

	public static GrimyHerbData forId(int herbId) {
		return herbs.get(Integer.valueOf(herbId));
	}
}
