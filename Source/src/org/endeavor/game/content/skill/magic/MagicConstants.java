package org.endeavor.game.content.skill.magic;

import java.util.HashMap;
import java.util.Map;

import org.endeavor.engine.definitions.CombatSpellDefinition;
import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.game.content.skill.magic.MagicConstants.SpellType;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.PlayerConstants;

public class MagicConstants {
	public static final int SET_AUTOCAST_BUTTON_ID = 1093;
	public static final int HOME_TELEPORT_BUTTON = 4171;
	public static final int TELEPORT_VARROCK_BUTTON = 4140;
	public static final int TELEPORT_LUMBRIDGE_BUTTON = 4143;
	public static final int TELEPORT_FALADOR_BUTTON = 4146;
	public static final int TELEPORT_CAMELOT_BUTTON = 4150;
	public static final int VENGEANCE_BUTTON = 118098;
	public static final int VENGEANCE_ANIMATION = 4410;
	public static final int VENGEANCE_GRAPHIC = 726;
	public static final int VENGEANCE_DELAY = 30000;
	public static final double VENGEANCE_HIT = 0.75D;
	public static final double VENGEANCE_EXPERIENCE = 112.0D;
	public static final String VENGEANCE_MESSAGE = "Taste Vengeance!";
	public static final Item[] VENGEANCE_RUNES = { new Item(9075, 4), new Item(557, 10), new Item(560, 2) };
	public static final int STAFF_OF_LIGHT_ID = 15486;
	public static final int TELE_BLOCK_SPELL_ID = 12445;
	public static final long TELE_BLOCK_TIME = 180000L;
	public static final int ICE_SPELL_IMMUNITY = 5;
	public static final int ICE_RUSH_SPELL_ID = 12861;
	public static final int ICE_RUSH_FREEZE_TIME = 8;
	public static final int ICE_BURST_SPELL_ID = 12881;
	public static final int ICE_BURST_FREEZE_TIME = 8;
	public static final int ICE_BLITZ_SPELL_ID = 12871;
	public static final int ICE_BLITZ_FREEZE_TIME = 12;
	public static final int ICE_BARRAGE_SPELL_ID = 12891;
	public static final int ICE_BARRAGE_FREEZE_TIME = 17;
	public static final Animation MODERN_TELEPORT_ANIMATION = new Animation(8939, 0);
	public static final Graphic MODERN_TELEPORT_GRAPHIC = Graphic.lowGraphic(1576, 0);
	public static final Graphic MODERN_TELEPORT_END_GRAPHIC = Graphic.lowGraphic(1577, 0);
	public static final Animation MODERN_TELEPORT_END_ANIMATION = new Animation(8941, 0);

	public static final Animation ANCIENT_TELEPORT_ANIMATION = new Animation(9599, 0);
	public static final Graphic ANCIENT_TELEPORT_GRAPHIC = Graphic.lowGraphic(1681, 0);
	public static final Animation ANCIENT_TELEPORT_END_ANIMATION = new Animation(65535, 0);
	public static final Graphic ANCIENT_TELEPORT_END_GRAPHIC = Graphic.lowGraphic(-1, 0);

	public static final Animation TABLET_BREAK_ANIMATION = new Animation(4069, 0);
	public static final Animation TABLET_TELEPORT_ANIMATION = new Animation(4731, 0);
	public static final Graphic TABLET_TELEPORT_GRAPHIC = Graphic.lowGraphic(678, 0);
	public static final Animation TABLET_TELEPORT_END_ANIMATION = new Animation(65535, 0);

	private static final Map<Integer, SpellType> spellTypes = new HashMap<Integer, SpellType>();

	public static SpellType getSpellTypeForId(int id) {
		SpellType type = spellTypes.get(Integer.valueOf(id));

		if (type != null) {
			return type;
		}
		return SpellType.NONE;
	}

	public static void declare() {
		for (int i = 0; i < 150000; i++) {
			CombatSpellDefinition def = GameDefinitionLoader.getCombatSpellDefinition(i);

			if (def != null) {
				String n = def.getName().toLowerCase();

				if (n.contains("fire"))
					spellTypes.put(Integer.valueOf(def.getId()), SpellType.FIRE);
				else if (n.contains("earth"))
					spellTypes.put(Integer.valueOf(def.getId()), SpellType.EARTH);
				else if ((n.contains("air")) || (n.contains("wind")))
					spellTypes.put(Integer.valueOf(def.getId()), SpellType.WIND);
				else if (n.contains("water"))
					spellTypes.put(Integer.valueOf(def.getId()), SpellType.WATER);
			}
		}
	}

	public static MagicSkill.SpellBookTypes getStaffType(int id) {
		switch (id) {
		case 4675:
			return MagicSkill.SpellBookTypes.ANCIENT;
		}
		return MagicSkill.SpellBookTypes.MODERN;
	}

	public static String spellTypeToString(MagicSkill.SpellBookTypes spellBookType) {
		switch (spellBookType) {
		case ANCIENT:
			return "Modern";
		case LUNAR:
			return "Ancient";
		case MODERN:
			return "Lunar";
		}
		return "Modern";
	}

	public static enum SpellType {
		NONE, EARTH, WATER, WIND, FIRE;
	}

	public static enum Teleports {
		HOME(new Location(PlayerConstants.HOME));

		private final Location location;

		private Teleports(Location location) {
			this.location = location;
		}

		public Location getLocation() {
			return location;
		}
	}
}
