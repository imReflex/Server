package org.endeavor.game.content.skill.slayer;

public class SlayerConstants {
	public static final short DEFAULT_KILL_AMOUNT = 20;
	public static final short RANDOM_KILL_ADDON = 50;

	public static enum HighLevel {
		BLACK_DRAGON("Black dragon"), 
		BLUE_DRAGON("Blue dragon"), 
		RED_DRAGON("Red dragon"), 
		IRON_DRAGON("Iron Dragon"), 
		BRONZE_DRAGON("Bronze Dragon"), 
		STEEL_DRAGON("Steel Dragon"),
		MITHRIL_DRAGON("Mithril dragon"),

		BLACK_DEMON("Black demon"),

		
		HELL_HOUND("Hellhound"), 
		TORMENTED_WRAITH("Tormented wraith"), 
		SPIRITUAL_WARRIOR("Spiritual warrior"), 
		ABYSSAL_DEMON("Abyssal demon"), 
		DARK_BEAST("Dark beast"), 
		AQUANITE("Aquanite"), 
		WATER_FIEND("Waterfiend"), 
		KURASK("Kurask"),

		CHAOS_DWARF_HAND_CANNONEER2("Chaos dwarf hand cannoneer"), 
		HELL_HOUND2("Hellhound"), 
		TORMENTED_WRAITH2("Tormented wraith"), 
		SPIRITUAL_WARRIOR2("Spiritual warrior"), 
		ABYSSAL_DEMON2("Abyssal demon"), 
		DARK_BEAST2("Dark beast"), 
		AQUANITE2("Aquanite"), 
		WATER_FIEND2("Waterfiend"), 
		KURASK2("Kurask"),
		
		TZHAAR_KET("TzHaar-Ket"),
		TZHAAR_KET2("TzHaar-Ket"),
		
		TZHAAR_XIL("TzHaar-Xil"),
		TZHAAR_XIL2("TzHaar-Xil"),
		
		;

		public final String name;
		public final byte lvl;

		private HighLevel(String name) {
			this.name = name;
			lvl = SlayerMonsters.getLevelForName(name.toLowerCase());
		}
	}

	public static enum LowLevel {
		ROCK_CRAB("Rock crab"), 
		CHAOS_DRUID("Chaos druid"), 
		EXPERIMENT("Experiment"), 
		HILL_GIANT("Hill giant"), 
		MOSS_GIANT("Moss giant"), 
		GIANT_BAT("Giant bat"), 
		CRAWLING_HAND("Crawling hand"), 
		KALPHITE_WORK("Kalphite worker"), 
		BANSHEE("Banshee"),
		
		;
		

		public final String name;
		public final byte lvl;

		private LowLevel(String name) {
			this.name = name;
			lvl = SlayerMonsters.getLevelForName(name.toLowerCase());
		}
	}

	public static enum MediumLevel {
		LESSER_DEMON("Lesser demon"), 
		GREATER_DEMON("Greater demon"), 
		GREEN_DRAGON("Green dragon"), 
		FIRE_GIANT("Fire giant"), 
		INFERNAL_MAGE("Infernal mage"), 
		DUST_DEVIL("Dust devil"), 
		BLOOD_VELD("Bloodveld"), 
		JELLY("Jelly"),
		TERROR_DOG("Terror Dog"),
		KURASK("Kurask"),
		KALPIHTE_SOLD("Kalphite soldier"),
		WALLASALKI("Wallasalki"),
		GIANT_ROCK_CRAB("Giant rock crab"),
		DAGANNOTH("Dagannoth"),
		;

		public final String name;
		public final byte lvl;

		private MediumLevel(String name) {
			this.name = name;
			lvl = SlayerMonsters.getLevelForName(name.toLowerCase());
		}
	}
}
