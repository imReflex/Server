package org.endeavor.game.content.skill.prayer;

import java.util.HashMap;
import java.util.Map;

public enum PrayerSounds {
	THICK_SKIN(0, 446), BURST_OF_STRENGTH(1, 449), CLARITY_OF_THOUGHT(2, 436), ROCK_SKIN(5, 441), SUPERHUMAN_STRENGTH(
			6, 434), IMPROVED_REFLEXES(7, 448), RAPID_RESTORE(8, 452), RAPID_HEAL(9, 443), STEEL_SKIN(13, 439), ULTIMATE_STRENGTH(
			14, 450), INCREDIBLE_REFLEXES(15, 440), PROTECT_FROM_MAGIC(16, 438), PROTECT_FROM_RANGED(17, 444), PROTECT_FROM_MELEE(
			18, 433);

	private int prayerId;
	private int soundId;
	private static Map<Integer, PrayerSounds> sounds;

	static {
		sounds = new HashMap<Integer, PrayerSounds>();

		for (PrayerSounds sound : values())
			sounds.put(Integer.valueOf(sound.getPrayerId()), sound);
	}

	private PrayerSounds(int id, int sound) {
		prayerId = id;
		soundId = sound;
	}

	public int getPrayerId() {
		return prayerId;
	}

	public int getSoundId() {
		return soundId;
	}

	private static PrayerSounds forId(int id) {
		return sounds.get(Integer.valueOf(id));
	}

	public static int getSoundId(int id) {
		PrayerSounds sounds = forId(id);
		return sounds == null ? -1 : sounds.getSoundId();
	}
}
