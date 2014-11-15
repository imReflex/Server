package org.endeavor.game.content.sounds;

import org.endeavor.engine.definitions.NpcDefinition;
import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendSound;

public class MobSounds {
	private static final short[][] sounds = new short[18000][];

	public static void sendAttackSound(Player p, int id, CombatTypes type, boolean hit) {
		if ((p == null) || (p.getClient() == null)) {
			return;
		}
		
		

		if (type == CombatTypes.MELEE) {
			if (sounds[id] == null) {
				return;
			}
			
			p.getClient().queueOutgoingPacket(new SendSound(sounds[id][0], 10, 20));
		} else if (type == CombatTypes.RANGED) {
			if (sounds[id] == null || sounds[id].length < 1) {
				return;
			}
			
			if ((sounds[id] != null) && (sounds[id][1] > 0))
				p.getClient().queueOutgoingPacket(new SendSound(sounds[id][1], 10, 20));
		} else if ((type == CombatTypes.MAGIC) && (sounds[id] != null) && (sounds[id][2] > 0)) {
			if (sounds[id] == null || sounds[id].length < 2) {
				return;
			}
			
			if (!hit)
				p.getClient().queueOutgoingPacket(new SendSound(218, 10, 20));
			else
				p.getClient().queueOutgoingPacket(new SendSound(sounds[id][2], 10, 20));
		}
	}

	public static void sendBlockSound(Player p, int id) {
		if ((p == null) || (p.getClient() == null)) {
			return;
		}
		
		if (sounds[id] == null || sounds[id].length < 3) {
			return;
		}

		if ((sounds[id] != null) && (sounds[id][3] > 0)) {
			p.getClient().queueOutgoingPacket(new SendSound(sounds[id][3], 10, 10));
		}
	}

	public static void sendDeathSound(Player p, int id) {
		if ((p == null) || (p.getClient() == null)) {
			return;
		}
		
		if (sounds[id] == null || sounds[id].length < 4) {
			return;
		}

		if ((sounds[id] != null) && (sounds[id][4] > 0)) {
			p.getClient().queueOutgoingPacket(new SendSound(sounds[id][4], 10, 10));
		}
	}

	public static void declare() {
		for (int i = 0; i < sounds.length; i++) {
			NpcDefinition def = Mob.getDefinition(i);

			if ((def != null) && (def.getName() != null)) {
				String s = def.getName().toLowerCase();

				if ((s.contains("man")) || (s.contains("mugger")) || (s.contains("mage")) || (s.contains("wizard")))
					sounds[i] = new short[] { 417, 417, 417, 72, 70 };
				else if (s.contains("woman"))
					sounds[i] = new short[] { 417, 417, 417, 73, 71 };
				else if (s.contains("cow"))
					sounds[i] = new short[] { 4, 4, 4, 3, 5 };
				else if (s.contains("rat"))
					sounds[i] = new short[] { 16, 16, 16, 17, 15 };
				else if (s.contains("bear"))
					sounds[i] = new short[] { 20, 20, 20, 18, 19 };
				else if (s.contains("chicken"))
					sounds[i] = new short[] { 26, 26, 26, 24, 25 };
				else if ((s.contains("greater demon")) || (s.contains("lesser demon")))
					sounds[i] = new short[] { 47, 47, 47, 46, 45 };
				else if (s.contains("giant"))
					sounds[i] = new short[] { 56, 56, 56, 54, 55 };
				else if ((s.contains("ghost")) || (s.contains("tormented wraith")))
					sounds[i] = new short[] { 61, 61, 61, 0, 60 };
				else if (s.contains("imp"))
					sounds[i] = new short[] { 64, 64, 64, 62, 63 };
				else if (s.contains("giant"))
					sounds[i] = new short[] { 56, 56, 56, 54, 55 };
				else if (s.contains("dog"))
					sounds[i] = new short[] { 36, 36, 36, 34, 35 };
				else if (s.contains("skeleton"))
					sounds[i] = new short[] { 108, 108, 108, 110, 109 };
				else if ((s.contains("baby")) && (s.contains("dragon")))
					sounds[i] = new short[] { 117, 117, 117, 119, 118 };
				else if (s.contains("dragon"))
					sounds[i] = new short[] { 115, 0, 0, 116, 118 };
				else if ((s.contains("hellhound")) || (s.contains("hell hound")) || (s.contains("wolf")))
					sounds[i] = new short[] { 36, 36, 36, 122, 121 };
				else if (s.contains("goblin"))
					sounds[i] = new short[] { 123, 123, 123, 124, 125 };
				else if (s.contains("zombie"))
					sounds[i] = new short[] { 148, 148, 148, 146, 147 };
				else if (s.contains("gnome"))
					sounds[i] = new short[] { 62, 62, 62, 64, 63 };
				else if (s.contains("mummy"))
					sounds[i] = new short[] { 77, 77, 77, 78, 76 };
				else if (s.contains("monkey"))
					sounds[i] = new short[] { 138, 138, 138, 139, 140 };
				else if (s.contains("vampire"))
					sounds[i] = new short[] { 632, 632, 632, 631, 633 };
				else if (s.contains("leech"))
					sounds[i] = new short[] { 642, 642, 642, 641, 640 };
				else if (s.contains("dagannoth"))
					sounds[i] = new short[] { 746, 749, 642, 748, 747 };
				else if (s.contains("dwarf"))
					sounds[i] = new short[] { 903, 903, 903, 904, 905 };
				else if (s.contains("ice troll"))
					sounds[i] = new short[] { 959, 959, 959, 960 };
				else if (s.contains("cave slime"))
					sounds[i] = new short[] { 1036, 1036, 1036, 1035, 1037 };
				else if (s.contains("rock crab"))
					sounds[i] = new short[] { 1707, 1707, 1707, 1709, 1708 };
				else if (s.contains("dark beast"))
					sounds[i] = new short[] { 1730, 1730, 1730, 1729, 1728 };
				else if (s.contains("jelly"))
					sounds[i] = new short[] { 825, 825, 825, 826, 827 };
				else if (s.contains("nechryael"))
					sounds[i] = new short[] { 831, 831, 831, 830, 829 };
				else if (s.contains("pyrefield"))
					sounds[i] = new short[] { 832, 832, 832, 834, 833 };
				else if (s.contains("rockslug"))
					sounds[i] = new short[] { 835, 835, 835, 836, 837 };
				else if (s.contains("crawling hand"))
					sounds[i] = new short[] { 838, 838, 838, 840, 839 };
				else if (s.contains("basilisk"))
					sounds[i] = new short[] { 843, 838, 838, 841, 844 };
				else if (s.contains("banshee"))
					sounds[i] = new short[] { 849, 838, 848, 852, 851 };
				else if (s.contains("bloodveld"))
					sounds[i] = new short[] { 853, 853, 853, 855, 854 };
				else if (s.contains("abyssal demon"))
					sounds[i] = new short[] { 856, 838, 838, 858, 857 };
				else if (s.contains("dust devil"))
					sounds[i] = new short[] { 859, 838, 838, 860, 861 };
				else if (s.contains("cockatrice"))
					sounds[i] = new short[] { 862, 838, 838, 863, 864 };
				else if (s.contains("gargoyle"))
					sounds[i] = new short[] { 865, 838, 838, 866, 867 };
				else if (s.contains("cave crawler"))
					sounds[i] = new short[] { 869, 838, 838, 870, 871 };
				else if (s.contains("giant rock crab"))
					sounds[i] = new short[] { 1668, 838, 838, 1669, 1670 };
				else if (s.contains("tz-kih"))
					sounds[i] = new short[] { 1813, 1813, 1813, 1815, 1814 };
				else if (s.contains("yt-mejkot"))
					sounds[i] = new short[] { 1816, 1816, 1816, 1818, 1817 };
				else if (s.contains("tok-xil"))
					sounds[i] = new short[] { 0, 1819 };
				else if (s.contains("ket-zek"))
					sounds[i] = new short[] { 1820, 1820, 1820, 1821, 1822 };
				else if (s.contains("tz-kek"))
					sounds[i] = new short[] { 1825, 1825, 1825, 1824, 1823 };
			}
		}
	}
}
