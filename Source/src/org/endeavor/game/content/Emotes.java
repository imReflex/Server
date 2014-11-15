package org.endeavor.game.content;

import java.util.HashMap;
import java.util.Map;

import org.endeavor.engine.definitions.ItemDefinition;
import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendConfig;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class Emotes {
	private static final Map<Integer, SkillCapeEmote> capeEmotes = new HashMap<Integer, SkillCapeEmote>();

	public static void onLogin(Player p) {
		for (int i = 744; i <= 760; i++)
			p.getClient().queueOutgoingPacket(new SendConfig(i, 1));
	}

	public static final void declare() {
		for (int i = 0; i < 20145; i++) {
			ItemDefinition def = GameDefinitionLoader.getItemDef(i);
			if ((def != null) && (def.getName() != null)) {
				String name = def.getName();

				if (name.contains("Attack cape"))
					capeEmotes.put(Integer.valueOf(i), new SkillCapeEmote(4959, 823));
				else if (name.contains("Defence cape"))
					capeEmotes.put(Integer.valueOf(i), new SkillCapeEmote(4961, 824));
				else if (name.contains("Strength cape"))
					capeEmotes.put(Integer.valueOf(i), new SkillCapeEmote(4981, 824));
				else if (name.contains("Constitution cape"))
					capeEmotes.put(Integer.valueOf(i), new SkillCapeEmote(4971, 833));
				else if (name.contains("Ranging cape"))
					capeEmotes.put(Integer.valueOf(i), new SkillCapeEmote(4973, 832));
				else if (name.contains("Magic cape"))
					capeEmotes.put(Integer.valueOf(i), new SkillCapeEmote(4939, 813));
				else if (name.contains("Prayer cape"))
					capeEmotes.put(Integer.valueOf(i), new SkillCapeEmote(4979, 829));
				else if (name.contains("Cooking cape"))
					capeEmotes.put(Integer.valueOf(i), new SkillCapeEmote(4955, 821));
				else if ((name.contains("Woodcut. cape")) || (name.contains("Woodcutting cape")))
					capeEmotes.put(Integer.valueOf(i), new SkillCapeEmote(4957, 822));
				else if (name.contains("Fletching cape"))
					capeEmotes.put(Integer.valueOf(i), new SkillCapeEmote(4937, 812));
				else if (name.contains("Fishing cape"))
					capeEmotes.put(Integer.valueOf(i), new SkillCapeEmote(4951, 819));
				else if (name.contains("Firemaking cape"))
					capeEmotes.put(Integer.valueOf(i), new SkillCapeEmote(4975, 831));
				else if (name.contains("Crafting cape"))
					capeEmotes.put(Integer.valueOf(i), new SkillCapeEmote(4949, 818));
				else if (name.contains("Smithing cape"))
					capeEmotes.put(Integer.valueOf(i), new SkillCapeEmote(4943, 815));
				else if (name.contains("Mining cape"))
					capeEmotes.put(Integer.valueOf(i), new SkillCapeEmote(4941, 814));
				else if (name.contains("Herblore cape"))
					capeEmotes.put(Integer.valueOf(i), new SkillCapeEmote(4969, 835));
				else if (name.contains("Agility cape"))
					capeEmotes.put(Integer.valueOf(i), new SkillCapeEmote(4977, 830));
				else if (name.contains("Thieving cape"))
					capeEmotes.put(Integer.valueOf(i), new SkillCapeEmote(4965, 826));
				else if (name.contains("Slayer cape"))
					capeEmotes.put(Integer.valueOf(i), new SkillCapeEmote(4967, 1656));
				else if (name.contains("Farming cape"))
					capeEmotes.put(Integer.valueOf(i), new SkillCapeEmote(4963, 825));
				else if (name.contains("Runecraft cape"))
					capeEmotes.put(Integer.valueOf(i), new SkillCapeEmote(4947, 817));
				else if (name.contains("Construction cape"))
					capeEmotes.put(Integer.valueOf(i), new SkillCapeEmote(4953, 820));
				else if (name.contains("Summoning cape"))
					capeEmotes.put(Integer.valueOf(i), new SkillCapeEmote(8525, 1515));
				else if (name.contains("Quest point cape"))
					capeEmotes.put(Integer.valueOf(i), new SkillCapeEmote(4945, 816));
			}
		}
	}

	public static void handleSkillCapeEmote(Player player) {
		Item cape = player.getEquipment().getItems()[1];

		if (cape == null) {
			player.getClient().queueOutgoingPacket(new SendMessage("You need to be wearing a skillcape to do this."));
			return;
		}

		SkillCapeEmote emote = capeEmotes.get(Integer.valueOf(cape.getId()));

		if (emote != null) {
			player.getUpdateFlags().sendAnimation(emote.getAnim(), 0);
			player.getUpdateFlags().sendGraphic(Graphic.lowGraphic(emote.getGfx(), 0));
		} else {
			player.getClient().queueOutgoingPacket(new SendMessage("You must be wearing a skillcape to do this."));
			return;
		}
	}

	public static boolean clickButton(Player player, int id) {
		if (id == 74108) {
			handleSkillCapeEmote(player);
			return true;
		}

		for (Emote i : Emote.values()) {
			if (i.buttonID == id) {
				if (i.animID != 1)
					player.getUpdateFlags().sendAnimation(new Animation(i.animID));
				if (i.gfxID != 1)
					player.getUpdateFlags().sendGraphic(Graphic.lowGraphic(i.gfxID, 0));
				return true;
			}
		}

		return false;
	}

	public static enum Emote {
		Yes(168, 855, -1), No(169, 856, -1), Bow(164, 858, -1), Angry(167, 864, -1), Think(162, 857, -1), Wave(163,
				863, -1), Shrug(52058, 2113, -1), Cheer(171, 862, -1), Beckon(165, 859, -1), Laugh(170, 861, -1), Jump_For_Joy(
				52054, 2109, -1), Yawn(52056, 2111, -1), Dance(166, 866, -1), Jig(52051, 2106, -1), Twirl(52052, 2107,
				-1), Headbang(52053, 2108, -1), Cry(161, 860, -1), Blow_Kiss(43092, 1368, 574), Panic(52050, 2105, -1), Rasberry(
				52055, 2110, -1), Clap(172, 865, -1), Salute(52057, 2112, -1), Goblin_Bow(52071, 2127, -1), Goblin_Salute(
				52072, 2128, -1), Glass_Box(2155, 1131, -1), Climb_Rope(25103, 1130, -1), Lean(25106, 1129, -1), Glass_Wall(
				2154, 1128, -1), Idea(88060, 4276, 712), Stomp(88061, 4278, -1), Flap(88062, 4280, -1), Slap_Head(
				88063, 4275, -1), Zombie_Walk(72032, 3544, -1), Zombie_Dance(72033, 3543, -1), Zombie_Hand(88065, 7272,
				1244), Scared(59062, 2836, -1), Bunny_Hop(72254, 6111, -1), Skillcape(154, 1, 1), AIR_GUITAR(88059,
				2414, 1537);

		public final int gfxID;
		public final int animID;
		public final int buttonID;

		private Emote(int buttonId, int animId, int gfxId) {
			buttonID = buttonId;
			animID = animId;
			gfxID = gfxId;
		}
	}

	private static class SkillCapeEmote {
		private final int anim;
		private final int gfx;

		public SkillCapeEmote(int anim, int gfx) {
			this.anim = anim;
			this.gfx = gfx;
		}

		public int getAnim() {
			return anim;
		}

		public int getGfx() {
			return gfx;
		}
	}
}
