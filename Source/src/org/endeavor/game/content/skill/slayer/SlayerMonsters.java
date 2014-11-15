package org.endeavor.game.content.skill.slayer;

import java.util.HashMap;
import java.util.Map;

import org.endeavor.engine.definitions.NpcDefinition;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class SlayerMonsters {
	private static final Map<Integer, Byte> slayerRequired = new HashMap<Integer, Byte>();

	public static boolean canAttackMob(Player player, Mob mob) {
		if (player.getDungGame() != null) {
			return true;
		}

		if ((mob.getId() == 10775) && (player.getMemberRank() <= 0)) {
			if (player.getMaxLevels()[24] < 85) {
				player.getClient().queueOutgoingPacket(
						new SendMessage("You need a Dungeoneering level of 85 to attack this mob."));
				return false;
			}

			return true;
		}

		int req = getRequiredLevel(mob.getId());

		if ((req != 0) && (player.getMaxLevels()[18] < req)) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("You need a Slayer level of " + req + " to attack this mob."));
			return false;
		}

		return true;
	}

	public static byte getRequiredLevel(int id) {
		return slayerRequired.containsKey(Integer.valueOf(id)) ? slayerRequired.get(Integer.valueOf(id)).byteValue()
				: 1;
	}

	public static final void declare() {
		for (int i = 0; i < 18000; i++) {
			NpcDefinition def = Mob.getDefinition(i);

			int lvl = 0;

			if ((def != null) && (def.getName() != null)) {
				String check = def.getName().toLowerCase();
				lvl = getLevelForName(check);
			}

			if (lvl > 0)
				slayerRequired.put(Integer.valueOf(i), Byte.valueOf((byte) lvl));
		}
	}

	public static byte getLevelForName(String check) {
		byte lvl = 0;

		switch (check) {
		case "crawling hand":
			lvl = (byte) 5;
			break;
		case "cave bug":
			lvl = (byte) 7;
			break;
		case "cave crawler":
			lvl = (byte) 10;
			break;
		case "banshee":
			lvl = (byte) 15;
			break;
		case "cave slime":
			lvl = (byte) 17;
			break;
		case "rockslug":
			lvl = (byte) 20;
			break;
		case "desert lizard":
			lvl = (byte) 22;
			break;
		case "cockatrice":
			lvl = (byte) 25;
			break;
		case "pyrefiend":
			lvl = (byte) 30;
			break;
		case "mogre":
			lvl = (byte) 32;
			break;
		case "infernal mage":
			lvl = (byte) 45;
			break;
		case "bloodveld":
			lvl = (byte) 50;
			break;
		case "jelly":
			lvl = (byte) 62;
			break;
		case "cave horror":
			lvl = (byte) 58;
			break;
		case "aberrant spectre":
			lvl = (byte) 60;
			break;
		case "dust devil":
			lvl = (byte) 65;
			break;
		case "spiritual ranger":
			lvl = (byte) 63;
			break;
		case "spiritual warrior":
			lvl = (byte) 68;
			break;
		case "gargoyle":
			lvl = (byte) 75;
			break;
		case "nechryael":
			lvl = (byte) 80;
			break;
		case "spiritual mage":
			lvl = (byte) 83;
			break;
		case "abyssal demon":
			lvl = (byte) 85;
			break;
		case "dark beast":
			lvl = (byte) 90;
			break;
		}

		return lvl;
	}
}
