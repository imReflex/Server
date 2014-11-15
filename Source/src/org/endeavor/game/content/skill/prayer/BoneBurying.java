package org.endeavor.game.content.skill.prayer;

import java.util.HashMap;
import java.util.Map;

import org.endeavor.game.content.skill.prayer.BoneBurying.Bones;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendEnterXInterface;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendSound;

public class BoneBurying {
	public static final String USING_ON_ALTAR_KEY = "boneonaltarkey";
	public static final int BURYING_ANIMATION = 827;

	public static boolean useBonesOnAltar(Player p, int item, int object) {
		if (object == 2640 || object == 409) {
			Bones bones = Bones.forId(item);

			if (bones == null) {
				return false;
			}

			if (p.getSkill().locked()) {
				return true;
			}

			if (p.isMember()) {
				p.getAttributes().set("boneonaltarkey", Integer.valueOf(item));
				p.setEnterXInterfaceId(55678);
				p.getClient().queueOutgoingPacket(new SendEnterXInterface());
				return true;
			}

			p.getSkill().lock(2);

			p.getClient().queueOutgoingPacket(new SendSound(442, 1, 0));
			p.getClient().queueOutgoingPacket(
					new SendMessage("You sacrifice the " + Item.getDefinition(bones.id).getName() + " at the altar."));

			p.getUpdateFlags().sendAnimation(645, 5);
			p.getInventory().remove(item);
			p.getSkill().addExperience(5, bones.experience * 2.0);

			p.getAchievements().incr(p, "Bury 500 bones");
			p.getAchievements().incr(p, "Bury 5,000 bones");
			return true;
		}

		return false;
	}

	public static void finishOnAltar(Player p, int amount) {
		if (p.getAttributes().get("boneonaltarkey") == null) {
			return;
		}

		int item = p.getAttributes().getInt("boneonaltarkey");

		Bones bones = Bones.forId(item);

		if (bones == null) {
			return;
		}

		int invAmount = p.getInventory().getItemAmount(item);

		if (invAmount == 0)
			return;
		if (invAmount < amount) {
			amount = invAmount;
		}

		p.getSkill().lock(2);

		p.getClient().queueOutgoingPacket(new SendSound(442, 1, 0));
		p.getClient().queueOutgoingPacket(
				new SendMessage("You sacrifice the " + Item.getDefinition(bones.id).getName() + " at the altar."));

		p.getUpdateFlags().sendAnimation(645, 5);
		p.getInventory().remove(new Item(item, amount));
		p.getSkill().addExperience(5, (bones.experience * 2.0D) * amount);

		p.getAchievements().incr(p, "Bury 500 bones", amount);
		p.getAchievements().incr(p, "Bury 5,000 bones", amount);
	}

	public static boolean bury(Player player, int id, int slot) {
		Bones bones = Bones.forId(id);
		if (bones == null) {
			return false;
		}

		if (player.getSkill().locked()) {
			return true;
		}

		player.getSkill().lock(4);
		player.getClient().queueOutgoingPacket(new SendSound(380, 1, 10));
		player.getUpdateFlags().sendAnimation(827, 0);
		player.getClient().queueOutgoingPacket(
				new SendMessage("You bury the " + Item.getDefinition(bones.id).getName() + "."));
		player.getInventory().clear(slot);
		player.getSkill().addExperience(5, bones.experience);

		player.getAchievements().incr(player, "Bury 500 bones");
		player.getAchievements().incr(player, "Bury 5,000 bones");
		return true;
	}

	public static enum Bones {
		NORMAL_BONES(526, 4.5D), WOLF_BONES(2859, 4.5D), BAT_BONES(530, 4.5D), BIG_BONES(532, 18.0D), BABYDRAGON_BONES(
				534, 30.0D), DRAGON_BONES(536, 72.0D), DAGG_BONES(6729, 125.0D), OURG_BONES(4834, 140.0D), LONG_BONE(
				10976, 250.0D);

		private int id;
		private double experience;
		private static Map<Integer, Bones> bones = new HashMap<Integer, Bones>();

		public static final void declare() {
			for (Bones b : values())
				bones.put(Integer.valueOf(b.getId()), b);
		}

		private Bones(int id, double experience) {
			this.id = id;
			this.experience = experience;
		}

		public int getId() {
			return id;
		}

		public static Bones forId(int id) {
			return bones.get(Integer.valueOf(id));
		}
	}
}
