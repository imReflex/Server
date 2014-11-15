package org.endeavor.game.content.sounds;

import org.endeavor.engine.cache.inter.RSInterface;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.minigames.dungeoneering.DungConstants;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.Controller;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.out.impl.SendSong;
import org.endeavor.game.entity.player.net.out.impl.SendSound;

public class MusicPlayer {
	public static final String MUSIC_START_KEY = "musicstarttime";
	private static final Music HOME_THEME = new Music(608, 0, 0, 0, 0);

	private static final int[] DEFAULT_SONGS = { 650, 82, 3, 7, 99, 83, 327, 76, 55, 193, 52, 75, 132, 152 };

	private static final int[] COMBAT_SONGS = { 8, 9, 24, 25, 26, 27, 28, 29, 68, 178 };

	private static final int[] MINIGAME_SONGS = { 314 };
	private static final int COMBAT_SONG = -4;
	private static final int DEFAULT_SONG = -3;
	private static final int HOME_SONG = -6;
	private static final int WILDERNESS_MUSIC = -2;
	private static final int MINIGAME_SONG = -5;
	private static final int[] RANDOM_BIRD_SOUNDS = { 1413, 1429, 1418, 1419, 1420, 1429, 2023 };

	private static final Music[] WILDERNESS = { new Music(435, 0, 0, 0, 0), new Music(42, 0, 0, 0, 0),
			new Music(43, 0, 0, 0, 0) };

	private static final Music[] songs = { new Music(76, 3200, 3199, 3273, 3302), new Music(2, 3200, 3303, 3273, 3353),
			new Music(111, 3274, 3328, 3315, 3394), new Music(123, 3274, 3266, 3323, 3327),
			new Music(36, 3274, 3200, 3323, 3265), new Music(50, 3257, 3112, 3333, 3199),
			new Music(47, 3324, 3200, 3408, 3262), new Music(122, 3324, 3263, 3408, 3285),
			new Music(541, 3324, 3286, 3408, 3327), new Music(64, 3136, 3136, 3193, 3199),
			new Music(3, 3066, 3200, 3120, 3272), new Music(327, 3121, 3200, 3199, 3268),
			new Music(163, 3121, 3269, 3199, 3314), new Music(151, 3066, 3273, 3120, 3314),
			new Music(333, 3066, 3315, 3147, 3394), new Music(116, 3148, 3315, 3199, 3394),
			new Music(106, 3200, 3354, 3273, 3394), new Music(157, 3248, 3395, 3328, 3468),
			new Music(125, 3166, 3395, 3247, 3468), new Music(175, 3111, 3395, 3165, 3468),
			new Music(177, 3111, 3469, 3264, 3524), new Music(93, 3265, 3469, 3328, 3524),
			new Music(48, 3329, 3447, 3418, 3524), new Music(35, 2993, 3186, 3065, 3260),
			new Music(107, 2889, 3265, 2940, 3324), new Music(127, 2941, 3261, 3013, 3324),
			new Music(49, 3014, 3261, 3065, 3324), new Music(186, 2880, 3325, 2935, 3394),
			new Music(72, 2936, 3325, 3065, 3394), new Music(54, 2944, 3395, 3008, 3458),
			new Music(54, 2944, 3459, 2987, 3474), new Music(150, 3009, 3395, 3065, 3458),
			new Music(141, 3066, 3395, 3110, 3450), new Music(23, 2937, 3475, 2987, 3524),
			new Music(102, 2988, 3459, 3065, 3524), new Music(98, 3066, 3451, 3110, 3524),
			new Music(34, 2944, 3525, 2991, 3591), new Music(96, 2992, 3525, 3034, 3555),
			new Music(96, 2992, 3556, 3124, 3605), new Music(182, 3035, 3525, 3124, 3555),
			new Music(169, 3125, 3525, 3264, 3579), new Music(121, 3265, 3563, 3392, 3619),
			new Music(113, 2944, 3592, 2991, 3655), new Music(160, 2992, 3606, 3055, 3655),
			new Music(176, 3056, 3606, 3124, 3655), new Music(10, 3125, 3581, 3205, 3655),
			new Music(179, 3206, 3580, 3264, 3655), new Music(183, 2944, 3656, 3003, 3722),
			new Music(66, 3004, 3656, 3064, 3722), new Music(476, 3065, 3656, 3126, 3722),
			new Music(43, 3127, 3656, 3197, 3714), new Music(8, 3198, 3656, 3264, 3702),
			new Music(337, 3265, 3621, 3392, 3716), new Music(435, 3048, 3723, 3126, 3799),
			new Music(68, 3127, 3715, 3197, 3758), new Music(332, 3198, 3704, 3264, 3758),
			new Music(182, 3265, 3717, 3392, 3842), new Music(37, 2944, 3800, 3003, 3903),
			new Music(331, 3212, 3843, 3392, 3903), new Music(52, 2944, 3904, 3009, 3969),
			new Music(13, 3077, 3905, 3139, 3969), new Music(92, 2891, 3133, 2940, 3195),
			new Music(92, 2941, 3133, 2971, 3185), new Music(172, 2817, 3133, 2890, 3208) };

	public static final void select(Player player, int button) {
		int id = RSInterface.getSongId(button);

		if (player.getCurrentSongId() != id) {
			player.getClient().queueOutgoingPacket(new SendSong(RSInterface.getSongId(button)));
			player.setCurrentSongId(id);
			player.getAttributes().set("musicStartTime", Long.valueOf(System.currentTimeMillis()));

			if (player.getAttributes().get("manual") == null)
				player.getAttributes().set("manual", Byte.valueOf((byte) 1));
		}
	}

	public static void play(Player player) {
		Controller c = player.getController();

		if ((c.equals(ControllerManager.DEFAULT_CONTROLLER)) && (player.getCurrentSongId() == -3)
				&& (Misc.randomNumber(250) == 0)) {
			player.getClient().queueOutgoingPacket(
					new SendSound(RANDOM_BIRD_SOUNDS[Misc.randomNumber(RANDOM_BIRD_SOUNDS.length)], 0, 0));
		}

		if (player.getAttributes().get("manual") != null) {
			return;
		}

		if (c.equals(DungConstants.DUNG_CONTROLLER)) {
			startSong(player, MINIGAME_SONGS[Misc.randomNumber(MINIGAME_SONGS.length)], -5);
			return;
		}

		if (player.getCombat().inCombat()) {
			if (!player.isDead()) {
				player.getAttributes().set("combatsongdelay", Long.valueOf(System.currentTimeMillis() + 40000L));
				startSong(player, COMBAT_SONGS[Misc.randomNumber(COMBAT_SONGS.length)], -4);
				return;
			}

		} else if (player.getAttributes().get("combatsongdelay") != null) {
			if (System.currentTimeMillis() > ((Long) player.getAttributes().get("combatsongdelay")).longValue())
				player.getAttributes().remove("combatsongdelay");
			else {
				return;
			}
		}

		if (c.equals(ControllerManager.WILDERNESS_CONTROLLER)) {
			startSong(player, WILDERNESS[Misc.randomNumber(WILDERNESS.length)].music, -2);
			return;
		}

		Music song = getMusicId(player);

		if (song == null) {
			startSong(player, DEFAULT_SONGS[Misc.randomNumber(DEFAULT_SONGS.length)], -3);
			return;
		}

		startSong(player, song.music, song.music);
	}

	public static void startSong(Player p, int songId, int setId) {
		if ((p.getCurrentSongId() != setId) || (restart(p))) {
			p.getClient().queueOutgoingPacket(new SendSong(songId));
			p.setCurrentSongId(setId);

			p.getAttributes().set("musicstarttime", Long.valueOf(System.currentTimeMillis()));
		}
	}

	public static final boolean restart(Player p) {
		return (p.getAttributes().get("musicstarttime") != null)
				&& (System.currentTimeMillis() - ((Long) p.getAttributes().get("musicstarttime")).longValue() > 240000L);
	}

	private static final Music getMusicId(Player player) {
		int x = player.getLocation().getX();
		int y = player.getLocation().getY();
		for (int i = 0; i < songs.length; i++) {
			if ((x >= songs[i].swX) && (x <= songs[i].neX) && (y >= songs[i].swY) && (y <= songs[i].neY)) {
				return songs[i];
			}
		}
		return null;
	}

	public static class Music {
		public int music;
		public int swX;
		public int swY;
		public int neX;
		public int neY;

		public Music(int music, int swX, int swY, int neX, int neY) {
			this.music = music;
			this.swX = swX;
			this.swY = swY;
			this.neX = neX;
			this.neY = neY;
		}
	}
}
