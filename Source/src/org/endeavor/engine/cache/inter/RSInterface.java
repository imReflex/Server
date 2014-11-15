package org.endeavor.engine.cache.inter;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.endeavor.engine.cache.ByteStreamExt;

public final class RSInterface {

	private static final Map<Integer, Short> songs = new HashMap<Integer, Short>();

	public static int getSongId(int id) {
		return songs.get(id) != null ? songs.get(id) : -1;
	}
	
	private static String[] musicNames = {
		"7th Realm", "A Familiar Feeling", "A New Menace", "A Pirate's Life for Me", "Adorno I", "Adorno II", "Adorno III", "Adorno IV", "Adorno IX", "Adorno V", "Adorno VI", "Adorno VII", "Adorno VIII", "Adorno X", "Adventure", "Al Kharid", "All's Fairy in Love and War", "Alone", "Altar Ego", "Alternative Root", "Ambient Jungle", "An Easter United", "Ancestral Wisdom", "Animal Apogee", "Anywhere", "Arabian", "Arabian 2", "Arabian 3", "Arabique",
		"Ardougne Ago", "Arma Gonna Get You", "Armadyl Alliance", "Armageddon", "Army of Darkness", "Arrival", "Artistry", "Assault and Battery", "Attack 1", "Attack 2", "Attack 3", "Attack 4", "Attack 5", "Attack 6", "Attention", "Autumn Voyage", "Aye Car Rum Ba", "Aztec", "Back to Life", "Background", "Bal'lak the Pummeller", "Ballad of Enchantment", "Bandit Camp", "Bandos Battalion", "Bane of Summer", "Barb Wire", "Barbarianism", "Barking Mad", "Baroque", "Battle of Souls",
		"Beetle Juice", "Beyond", "Big Chords", "Bish Bash Bosh", "Bittersweet Bunny", "Black of Knight", "Blistering Barnacles", "Bloodbath", "Body Parts", "Bolrie's Diary", "Bone Dance", "Bone Dry", "Book of Spells", "Borderland", "Born to Do This", "Bounty Hunter Level 1", "Bounty Hunter Level 2", "Bounty Hunter Level 3", "Brain Battle", "Breeze", "Brew Hoo Hoo", "Brimstail's Scales", "Bubble and Squeak", "Bulwark Beast", "But We Can Fight", "Cabin Fever", "Camelot",
		"CastleWars", "Catacombs and Tombs", "Catch Me If You Can", "Cave Background", "Cave of Beasts", "Cave of the Goblins", "Cavern", "Cavernous Mythology", "Cellar Song", "Chain of Command", "Chamber", "Charmin' Farmin'", "Chef Surprize", "Chickened Out", "Chompy Hunt", "Circus", "City of the Dead", "Clan Wars", "Claustrophobia", "Close Quarters", "Command Centre", "Competition", "Complication", "Conspiracy: Part 1", "Conspiracy: Part 2", "Contest", "Cool for Ali Cats",
		"Copris Lunaris", "Corporal Punishment", "Corridors of Power", "Courage", "Creature Cruelty", "Creepy", "Crystal Castle", "Crystal Cave", "Crystal Sword", "Cursed", "Dagannoth Dawn", "Dance of Death", "Dance of the Undead", "Dangerous", "Dangerous Logic", "Dangerous Road", "Dangerous Way", "Dark", "Davy Jones's Locker", "Dead and Buried", "Dead Can Dance", "Dead Quiet", "Deadlands", "Deep Down", "Deep Wildy", "Demise of the Dorgeshuun", "Desert Heat", "Desert Island Bear",
		"Desert Smoke", "Desert Voyage", "Desolate Ruins", "Desolo I", "Desolo II", "Desolo III", "Desolo IV", "Desolo IX", "Desolo V", "Desolo VI", "Desolo VII", "Desolo VIII", "Desolo X", "Diango's Little Helpers", "Dillo-gence is Key", "Dimension X", "Distant Land", "Distillery Hilarity", "Divine Skinweaver", "Dogs of War", "Don't Panic Zanik", "Doorways", "Dorgeshuun City", "Dorgeshuun Deep", "Down and Out", "Down Below", "Down to Earth", "Dragontooth Island", "Dream",
		"Dream Theatre", "Dreamstate", "Duel Arena", "Dunjun", "Dusk in Yu'biusk", "Dwarf Theme", "Dynasty", "Eagle Peak", "Easter Jig", "Egypt", "Elven Mist", "Elven Seed", "Emotion", "Emperor", "Eruption", "Escape", "Espionage", "Etcetera", "Everlasting", "Everlasting Fire", "Everywhere", "Evil Bob's Island", "Exam Conditions", "Exhibit 'A'", "Expanse", "Expecting", "Expedition", "Exposed", "Face Off", "Faerie", "Faithless", "Fanfare", "Fanfare 2", "Fanfare 3", "Fangs for the Memory",
		"Far Away", "Fe Fi Fo Fum", "Fear and Loathing", "Fenkenstrain's Refrain", "Fight of the Dwarves", "Fight or Flight", "Final Destination", "Find My Way", "Fire and Brimstone", "Fishing", "Floating Free", "Flute Salad", "Food for Thought", "Forbidden", "Forest", "Forever", "Forgettable Melody", "Forgotten", "Freshwater", "Frogland", "Frost Fight", "Frostbite", "Fruits de Mer", "Funny Bunnies", "Gaol", "Garden", "Garden of Autumn", "Garden of Spring", "Garden of Summer", "Garden of Winter",
		"Ghost of Christmas Presents", "Glacialis IV", "Glacialis VI", "Glacialis VIII", "Glorious Recallation...", "Glorious Recallation...", "Glorious Recallation...", "Gnome King", "Gnome Village", "Gnome Village 2", "Gnomeball", "Goblin Game", "Goblin Village", "Godslayer", "Golden Touch", "Greatness", "Grimly Fiendish", "Grip of the Talon", "Grotto", "Ground Scape", "Grumpy", "Guthix's Hunter", "H.A.M. Fisted", "Ham and Seek", "Ham Attack", "Har'Lakk the Riftsplitter", "Hare-brained Bitches", 
		"Harmony", "Harmony 2", "Haunted Mine", "Have a Blast", "Have an Ice Day", "Head to Head", "Healin' Feelin'", "Heart and Mind", "Hell's Bells", "Hermit", "High Seas", "High Spirits", "Historic Memories", "Hobgoblin Geomancer", "Home Sweet Home", "Homescape", "Honkytonky Harmony", "Honkytonky Medieval", "Honkytonky Newbie Melody", "Honkytonky Parade", "Honkytonky Sea Shanty", "Horizon", "Hot 'n' Bothered", "Hypnotized", "I'm Counting on You", "Iban", "Ice Day for Penguins", "Ice Melody",
		"Icy a Worried Gnome", "Icy Trouble Ahead", "Illusive", "Impetuous", "In Between", "In the Brine", "In the Clink", "In the Manor", "In the Pits", "Inadequacy", "Incantation", "Incarceration", "Insect Queen", "Inspiration", "Into the Abyss", "Intrepid", "Island Life", "Island of the Trolls", "Isle of Everywhere", "Itsy Bitsy...", "Jailbird", "Jaws of the Dagannoth", "Jester Minute", "Jolly-R", "Joy of the Hunt", "Jungle Bells", "Jungle Community", "Jungle Hunt", "Jungle Island", "Jungle Island XMAS",
		"Jungle Troubles", "Jungly 1", "Jungly 2", "Jungly 3", "Karamja Jam", "Kharidian Nights", "Kingdom", "Knightly", "Knightmare", "La Mort", "Labyrinth", "Lair", "Lair of Kang Admi", "Lament", "Lament of Meiyerditch", "Lamistard's Labyrinth", "Land Down Under", "Land of Snow", "Land of the Dwarves", "Landlubber", "Last Stand", "Lasting", "Lazy Wabbit", "Legend", "Legion", "Lexicus Runewright", "Life's a Beach!", "Lighthouse", "Lightness", "Lightwalk", "Little Cave of Horrors", "Living Rock", "Lonesome",
		"Long Ago", "Long Way Home", "Looking Back", "Lore and Order", "Lost Soul", "Lullaby", "Mad Eadgar", "Mage Arena","Magic and Mystery", "Magic Dance", "Magic Magic Magic", "Magical Journey", "Maiasaura", "Major Miner", "Making Waves", "Malady", "March", "Marooned", "Marzipan", "Masquerade", "Mastermindless", "Mausoleum", "Meddling Kids", "Medieval", "Mellow", "Melodrama", "Melzar's Maze", "Meridian", "Method of Madness", "Miles Away", "Mind Over Matter", "Miracle Dance", "Mirage", "Miscellania",
		"Mobilising Armies", "Monarch Waltz", "Monkey Madness", "Monster Melee", "Moody", "Morytania", "Mouse Trap", "Mudskipper Melody", "Mutant Medley", "My Arm's Journey", "Narnode's Theme", "Natural", "Neverland", "Newbie Melody", "Nial's Widow", "Night of the Vampyre", "Night-gazer Khighorahk", "Nightfall", "No Way Out", "Nomad", "Norse Code", "Null and Void", "Ogre the Top", "On the Up", "On the Wing", "Oriental", "Out of the Deep", "Over To Nardah", "Overpass", "Overture", "Parade", "Path of Peril",
		"Pathways", "Penguin Possible", "Pest Control", "Pharaoh's Tomb", "Phasmatys", "Pheasant Peasant", "Pinball Wizard", "Pirates of Penance", "Pirates of Peril", "Poison Dreams", "Poles Apart", "Prime Time", "Principality", "Quest", "Rammernaut", "Rat a Tat Tat", "Rat Hunt", "Ready for Battle", "Regal", "Reggae", "Reggae 2", "Rellekka", "Rest for the Weary", "Right on Track", "Righteousness", "Rising Damp", "Riverside", "Roc and Roll", "Roll the Bones", "Romancing the Crone", "Romper Chomper",
		"Roots and Flutes", "Royale", "Rune Essence", "Sad Meadow", "Safety in Numbers", "Saga", "Sagittare","Saltwater", "Sarah's Lullaby", "Sarcophagus", "Sarim's Vermin", "Scape Cave", "Scape Hunter", "Scape Main", "Scape Original", "Scape Sad", "Scape Santa", "Scape Scared", "Scape Soft", "Scape Summon", "Scape Theme", "Scape Wild", "Scarab", "Scarabaeoidea", "School's Out", "Sea Shanty", "Sea Shanty 2", "Sea Shanty XMAS", "Second Vision", "Serenade", "Serene", "Settlement", "Shadow-forger Ihlakhizan",
		"Shadowland", "Shaping Up", "Shine", "Shining", "Shining Spirit", "Shipwrecked", "Showdown", "Sigmund's Showdown", "Silent Knight", "Slain to Waste", "Slice of Silent Movie", "Slice of Station", "Slither and Thither", "Slug a Bug Ball", "Smorgasbord", "Sojourn", "Something Fishy", "Soundscape", "Spa Bizarre", "Sphinx", "Spirit", "Spirits of Elid", "Splendour", "Spooky", "Spooky 2", "Spooky Jungle", "Stagnant", "Starlight", "Start", "Stealing Creation", "Still Night", "Stillness", "Stillwater", "Stomp",
		"Storeroom Shuffle", "Storm Brew", "Stranded", "Strange Place", "Stratosphere", "Strength of Saradomin", "Subterranea", "Sunburn", "Superstition", "Surok's Theme", "Suspicious", "Tale of Keldagrim", "Talking Forest", "Tears of Guthix", "Technology", "Temple", "Temple Desecrated", "Temple of Light", "Temple of Tribes", "Terrorbird Tussle", "The Adventurer", "The Adventurers Re-United!", "The Art of Hocus-Pocus", "The Cellar Dwellers", "The Chosen", "The Chosen Commander", "The Columbarium", "The Dance of the Snow Queen",
		"The Depths", "The Desert", "The Desolate Isle", "The Duke", "The Enchanter", "The Evil Within", "The Fallen Hero", "The Far Side", "The Galleon", "The Genie", "The Golem", "The Heist", "The Horn of Chill", "The Last Shanty", "The Longramble Scramble", "The Lost Melody", "The Lost Tribe", "The Lunar Isle", "The Mad Mole", "The Mentor", "The Mollusc Menace", "The Monsters Below", "The Muspah's Tomb", "The Navigator", "The Noble Rodent", "The Other Side", "The Pact", "The Pengmersible", "The Phoenixb",
		"The Plundered Tomb", "The Power of Tears", "The Quiz Master", "The Rogues' Den", "The Route of All Evil", "The Route of the Problem", "The Ruins of Camdozaal", "The Shadow", "The Slayer", "The Sound of Guthix", "The Terrible Caverns", "The Terrible Tower", "The Terrible Tunnels", "The Throne of Bandos", "The Tower", "The Trade Parade", "The Vacant Abyss", "The Wrong Path", "Theme", "These Stones", "Throne of the Demon", "Time Out", "Time to Mine", "Tiptoe", "Title Fight", "TokTz-Ket-Ek-Mack", "Tomb Raider",
		"Tomorrow", "Too Many Cooks...", "Tournament!", "Trawler", "Trawler Minor", "Tree Spirits", "Trees Aren't Your Friends", "Tremble", "Tribal", "Tribal 2", "Tribal Background", "Trick or Treat?", "Trinity", "Trouble Brewing", "Troubled", "Troubled Spirit", "Tune from the Dune", "Twilight", "TzHaar!", "Undead Army", "Undead Dungeon", "Under the Sand", "Undercurrent", "Underground", "Underground Pass", "Understanding", "Unholy Cursebearer", "Unknown Land", "Untouchable", "Upcoming", "Venomous", "Venture", "Venture 2",
		"Victory is Mine", "Village", "Vision", "Volcanic Vikings", "Voodoo Cult", "Voyage", "Waiting for Battle", "Waiting for the Hunt", "Waking Dream", "Wander", "Warrior", "Warriors' Guild", "Waste Defaced", "Waterfall", "Waterlogged", "Way of the Enchanter", "Wayward", "We Are the Fairies", "Well of Voyage", "Where Eagles Lair", "Wild Isle", "Wild Side", "Wilderness", "Wilderness 2", "Wilderness 3", "Wildwood", "Winter Funfare", "Witching", "Woe of the Wyvern", "Wonder", "Wonderous", "Woodland", "Work Work Work",
		"Workshop", "Wrath and Ruin", "Xenophobe", "Yesteryear", "Zamorak Zoo", "Zanik's Theme", "Zaros Stirs", "Zealot", "Zogre Dance"
	};

	public static final void plotSongList() {
		int plotted = 0;

		/**
		 * Song data, name -> id for name
		 */
		Map<String, Integer> loaded = new HashMap<String, Integer>();

		/**
		 * Load song data
		 */
		try {
			BufferedReader reader = new BufferedReader(new FileReader("./data/def/music.txt"));

			String line = null;

			while ((line = reader.readLine()) != null) {
				String name = null;
				int id = -1;

				line = line.toLowerCase();

				if (line.indexOf("e: ") == -1 || line.indexOf("id: ") == -1 || line.indexOf(" n") == -1) {
					continue;
				}

				try {
					name = line.substring(line.indexOf("e: ") + 2, line.length()).trim();
					id = Integer.parseInt(line.substring(line.indexOf("id: ") + 3, line.indexOf(" n")).trim());

					for (int i = 0; i <= 9; i++) {
						if (name.contains(" " + i)) {
							break;
						}

						if (name.contains("" + i) && name.indexOf("" + i) == name.length() - 1) {
							name = name.substring(0, name.length() - 1) + " " + i;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

				if (name == null || id == -1) {
					continue;
				}

				loaded.put(name, id);
			}

			reader.close();
		} catch (Exception e) {
			System.out.println("Music data could not be loaded.");
			e.printStackTrace();
			return;
		}

		System.out.println("Loaded music data.");

		/**
		 * Music selection interface
		 */
		RSInterface musicInterface = interfaceCache[962];

		for (Integer i : musicInterface.children) {
			RSInterface child = interfaceCache[i];

			if (child != null && child.children != null) {
				for (Integer j : child.children) {
					if (interfaceCache[j].message == null || interfaceCache[j].message.equals("null")) {
						continue;
					}

					if (loaded.containsKey(interfaceCache[j].message.toLowerCase())) {
						songs.put(j, loaded.get(interfaceCache[j].message.toLowerCase()).shortValue());
						loaded.remove(interfaceCache[j].message.toLowerCase());
						plotted++;
					}
				}
			}
		}

		System.out.println("Could not plot " + loaded.size() + " songs.");
		System.out.println("Plotted " + plotted + " songs.");
	}

	public static final int getParent(int id) {
		if (interfaceCache[id] == null || interfaceCache[id].parentID == -1 || interfaceCache[id].parentID == id)
			return id;
		else
			return getParent(interfaceCache[id].parentID);
	}

	public static void unpack() {
		ByteStreamExt byteVector = null;
		try {
			File f = new File("./data/def/interface");
			byte[] buffer = new byte[(int) f.length()];
			DataInputStream dis = new DataInputStream(new FileInputStream(f));
			dis.readFully(buffer);
			dis.close();
			byteVector = new ByteStreamExt(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int i = -1;
		byteVector.readUnsignedWord();
		interfaceCache = new RSInterface[60000];
		do {
			if (byteVector.currentOffset >= byteVector.buffer.length)
				break;
			int k = byteVector.readUnsignedWord();
			if (k == 65535) {
				i = byteVector.readUnsignedWord();
				k = byteVector.readUnsignedWord();
			}
			RSInterface rsInterface = interfaceCache[k] = new RSInterface();
			rsInterface.id = k;
			rsInterface.parentID = i;
			rsInterface.type = byteVector.readUnsignedByte();
			rsInterface.atActionType = byteVector.readUnsignedByte();
			rsInterface.contentType = byteVector.readUnsignedWord();
			rsInterface.width = byteVector.readUnsignedWord();
			rsInterface.height = byteVector.readUnsignedWord();
			rsInterface.opacity = (byte) byteVector.readUnsignedByte();
			rsInterface.mouseOverPopupInterface = byteVector.readUnsignedByte();
			if (rsInterface.mouseOverPopupInterface != 0)
				rsInterface.mouseOverPopupInterface = (rsInterface.mouseOverPopupInterface - 1 << 8)
						+ byteVector.readUnsignedByte();
			else
				rsInterface.mouseOverPopupInterface = -1;
			int i1 = byteVector.readUnsignedByte();
			if (i1 > 0) {
				rsInterface.valueCompareType = new int[i1];
				rsInterface.requiredValues = new int[i1];
				for (int j1 = 0; j1 < i1; j1++) {
					rsInterface.valueCompareType[j1] = byteVector.readUnsignedByte();
					rsInterface.requiredValues[j1] = byteVector.readUnsignedWord();
				}

			}
			int k1 = byteVector.readUnsignedByte();
			if (k1 > 0) {
				rsInterface.valueIndexArray = new int[k1][];
				for (int l1 = 0; l1 < k1; l1++) {
					int i3 = byteVector.readUnsignedWord();
					rsInterface.valueIndexArray[l1] = new int[i3];
					for (int l4 = 0; l4 < i3; l4++)
						rsInterface.valueIndexArray[l1][l4] = byteVector.readUnsignedWord();

				}

			}
			if (rsInterface.type == 0) {
				rsInterface.drawsTransparent = false;
				rsInterface.scrollMax = byteVector.readUnsignedWord();
				rsInterface.isMouseoverTriggered = byteVector.readUnsignedByte() == 1;
				int i2 = byteVector.readUnsignedWord();
				rsInterface.children = new int[i2];
				rsInterface.childX = new int[i2];
				rsInterface.childY = new int[i2];
				for (int j3 = 0; j3 < i2; j3++) {
					rsInterface.children[j3] = byteVector.readUnsignedWord();
					rsInterface.childX[j3] = byteVector.readSignedWord();
					rsInterface.childY[j3] = byteVector.readSignedWord();
				}

			}
			if (rsInterface.type == 1) {
				byteVector.readUnsignedWord();
				byteVector.readUnsignedByte();
			}
			if (rsInterface.type == 2) {
				rsInterface.inv = new int[rsInterface.width * rsInterface.height];
				rsInterface.invStackSizes = new int[rsInterface.width * rsInterface.height];
				rsInterface.aBoolean259 = byteVector.readUnsignedByte() == 1;
				rsInterface.isInventoryInterface = byteVector.readUnsignedByte() == 1;
				rsInterface.usableItemInterface = byteVector.readUnsignedByte() == 1;
				rsInterface.aBoolean235 = byteVector.readUnsignedByte() == 1;
				rsInterface.invSpritePadX = byteVector.readUnsignedByte();
				rsInterface.invSpritePadY = byteVector.readUnsignedByte();
				rsInterface.spritesX = new int[20];
				rsInterface.spritesY = new int[20];
				for (int j2 = 0; j2 < 20; j2++) {
					int k3 = byteVector.readUnsignedByte();
					if (k3 != 1)
						continue;
					rsInterface.spritesX[j2] = byteVector.readSignedWord();
					rsInterface.spritesY[j2] = byteVector.readSignedWord();
					byteVector.readString();
				}

				rsInterface.itemActions = new String[6];
				for (int l3 = 0; l3 < 5; l3++) {
					rsInterface.itemActions[l3] = byteVector.readString();
					if (rsInterface.parentID == 3824)
						rsInterface.itemActions[4] = "Buy X";
					if (rsInterface.itemActions[l3].length() == 0)
						rsInterface.itemActions[l3] = null;
					if (rsInterface.parentID == 1644)
						rsInterface.itemActions[2] = "Operate";
				}

			}
			if (rsInterface.type == 3)
				rsInterface.aBoolean227 = byteVector.readUnsignedByte() == 1;
			if (rsInterface.type == 4 || rsInterface.type == 1) {
				rsInterface.centerText = byteVector.readUnsignedByte() == 1;
				byteVector.readUnsignedByte();
				rsInterface.textShadow = byteVector.readUnsignedByte() == 1;
			}
			if (rsInterface.type == 4) {
				rsInterface.message = byteVector.readString();
				rsInterface.disabledText = byteVector.readString();
			}
			if (rsInterface.type == 1 || rsInterface.type == 3 || rsInterface.type == 4)
				rsInterface.textColor = byteVector.readDWord();
			if (rsInterface.type == 3 || rsInterface.type == 4) {
				rsInterface.anInt219 = byteVector.readDWord();
				rsInterface.textHoverColour = byteVector.readDWord();
				rsInterface.anInt239 = byteVector.readDWord();
			}
			if (rsInterface.type == 5) {
				rsInterface.drawsTransparent = false;
				byteVector.readString();
				byteVector.readString();
			}
			if (rsInterface.type == 6) {
				int l = byteVector.readUnsignedByte();
				if (l != 0) {
					rsInterface.anInt233 = 1;
					rsInterface.mediaID = (l - 1 << 8) + byteVector.readUnsignedByte();
				}
				l = byteVector.readUnsignedByte();
				if (l != 0) {
					rsInterface.anInt255 = 1;
					rsInterface.anInt256 = (l - 1 << 8) + byteVector.readUnsignedByte();
				}
				l = byteVector.readUnsignedByte();
				if (l != 0)
					rsInterface.anInt257 = (l - 1 << 8) + byteVector.readUnsignedByte();
				else
					rsInterface.anInt257 = -1;
				l = byteVector.readUnsignedByte();
				if (l != 0)
					rsInterface.anInt258 = (l - 1 << 8) + byteVector.readUnsignedByte();
				else
					rsInterface.anInt258 = -1;
				rsInterface.modelZoom = byteVector.readUnsignedWord();
				rsInterface.modelRotation1 = byteVector.readUnsignedWord();
				rsInterface.modelRotation2 = byteVector.readUnsignedWord();
			}
			if (rsInterface.type == 7) {
				rsInterface.inv = new int[rsInterface.width * rsInterface.height];
				rsInterface.invStackSizes = new int[rsInterface.width * rsInterface.height];
				rsInterface.centerText = byteVector.readUnsignedByte() == 1;
				byteVector.readUnsignedByte();
				rsInterface.textShadow = byteVector.readUnsignedByte() == 1;
				rsInterface.textColor = byteVector.readDWord();
				rsInterface.invSpritePadX = byteVector.readSignedWord();
				rsInterface.invSpritePadY = byteVector.readSignedWord();
				rsInterface.isInventoryInterface = byteVector.readUnsignedByte() == 1;
				rsInterface.itemActions = new String[6];
				for (int k4 = 0; k4 < 5; k4++) {
					rsInterface.itemActions[k4] = byteVector.readString();
					if (rsInterface.itemActions[k4].length() == 0)
						rsInterface.itemActions[k4] = null;
				}

			}
			if (rsInterface.atActionType == 2 || rsInterface.type == 2) {
				rsInterface.selectedActionName = byteVector.readString();
				rsInterface.spellName = byteVector.readString();
				rsInterface.spellUsableOn = byteVector.readUnsignedWord();
			}
			if (rsInterface.type == 8)
				rsInterface.message = byteVector.readString();
			if (rsInterface.atActionType == 1 || rsInterface.atActionType == 4 || rsInterface.atActionType == 5
					|| rsInterface.atActionType == 6) {
				rsInterface.tooltip = byteVector.readString();
				if (rsInterface.tooltip.length() == 0) {
					if (rsInterface.atActionType == 1)
						rsInterface.tooltip = "Ok";
					if (rsInterface.atActionType == 4)
						rsInterface.tooltip = "Select";
					if (rsInterface.atActionType == 5)
						rsInterface.tooltip = "Select";
					if (rsInterface.atActionType == 6)
						rsInterface.tooltip = "Continue";
				}
			}
		} while (true);

		unpackCustom();
		

		System.out.println("Loaded interfaces.");
	}

	public static void unpackCustom() {
		RSInterface tab = addTabInterface(962);
	    addSprite(27800, 2, "Music/img");
	    addSprite(27801, 3, "Music/img");
	    addButton(27802, 0, "Music/img", 25, 25, "Loop", 1);
	    tab.totalChildren(24);
	    for (int i = 0; i < 6; i++) {
	      tab.child(2 + i, 27801, 32 * i, 59);
	      tab.child(8 + i, 27801, 32 * i, 240);
	      tab.child(14 + i, 27801, 32 * i, 35);
	    }
	    tab.child(0, 27800, 0, 62);
	    tab.child(1, 963, 0, 62);
	    tab.child(20, 27802, 75, 5);
	    tab.child(21, 27803, 122, 5);
	    tab.child(22, 27804, 121, 16);
	    tab.child(23, 27805, 62, 40);
	    RSInterface list = addTabInterface(963);
	    list.totalChildren(676);
	    for (int i = 27000; i < 27676; i++) {
	      addClickableText(i, musicNames[(i - 27000)], musicNames[(i - 27000)], 0, 65280, 16777215, 162, 11);
	    }
	    int id = 27000; for (int i = 0; (id < 27676) && (i < 676); i++) {
	      list.children[i] = id; list.childX[i] = 12;
	      int id2 = 27000; for (int i2 = 1; (id2 < 27676) && (i2 < 676); i2++) {
	        list.childY[0] = 2;
	        list.childY[i2] = (list.childY[(i2 - 1)] + 15);

	        id2++;
	      }
	      id++;
	    }

	    list.height = 178; list.width = 174;
	    list.scrollMax = 10143;
	}
	
	public static void addClickableText(int id, String text, String tooltip, int idx, int color, int hoverColor, int width, int height)
	  {
	    RSInterface Tab = addTab2(id);
	    Tab.parentID = id;
	    Tab.id = id;
	    Tab.type = 4;
	    Tab.atActionType = 1;
	    Tab.width = width;
	    Tab.height = height;
	    Tab.contentType = 0;
	    Tab.opacity = 0;
	    Tab.mouseOverPopupInterface = -1;
	    Tab.centerText = false;
	    Tab.textShadow = true;
	    Tab.message = text;
	    Tab.tooltip = tooltip;
	    Tab.disabledText = "";
	    Tab.textColor = color;
	    Tab.anInt219 = 0;
	    Tab.textHoverColour = hoverColor;
	    Tab.anInt239 = 0;
	  }
	
	public static RSInterface addTab2(int id)
	  {
		interfaceCache[id] = new RSInterface();
	    RSInterface Tab = interfaceCache[id];
	    Tab.id = id;
	    Tab.parentID = id;
	    Tab.type = 0;
	    Tab.atActionType = 0;
	    Tab.contentType = 0;
	    Tab.width = 512;
	    Tab.height = 334;
	    Tab.opacity = 0;
	    Tab.mouseOverPopupInterface = 0;
	    return Tab;
	  }

	public static void drawCustomInterface() {
		RSInterface rsinterface = addTabInterface(17600);
		// addText(17601, "Next round starts in: ", 1, 0xff9040, true, true);
		// addText(17602, "Current round: ", 1, 0xff9040, true, true);
		// addText(17603, "Zombies Left: ", 1, 0xff9040, true, true);
		// addText(17604, "Players alive: ", 1, 0xff9040, true, true);
		// addText(17605, "LINE5", 1, 0xff9040, true, true);
		rsinterface.children = new int[5];
		rsinterface.childX = new int[5];
		rsinterface.childY = new int[5];
		rsinterface.children[0] = 17601;
		rsinterface.childX[0] = 266;
		rsinterface.childY[0] = 10;
		rsinterface.children[1] = 17602;
		rsinterface.childX[1] = 266;
		rsinterface.childY[1] = rsinterface.childY[0] + 17;
		rsinterface.children[2] = 17603;
		rsinterface.childX[2] = 266;
		rsinterface.childY[2] = rsinterface.childY[1] + 17;
		rsinterface.children[3] = 17604;
		rsinterface.childX[3] = 266;
		rsinterface.childY[3] = rsinterface.childY[2] + 17;
		rsinterface.children[4] = 17605;
		rsinterface.childX[4] = 266;
		rsinterface.childY[4] = rsinterface.childY[3] + 17;
	}

	public static void addSprite(int a, int id, int spriteId, String spriteName, boolean l) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.opacity = (byte) 0;
		tab.mouseOverPopupInterface = 52;
		if (a == 1) {
		}
		tab.width = 512;
		tab.height = 334;
	}

	public static void addToItemGroup(RSInterface rsi, int w, int h, int x, int y, boolean actions, String action1,
			String action2, String action3) {
		rsi.width = w;
		rsi.height = h;
		rsi.inv = new int[w * h];
		rsi.invStackSizes = new int[w * h];
		rsi.usableItemInterface = false;
		rsi.isInventoryInterface = false;
		rsi.invSpritePadX = x;
		rsi.invSpritePadY = y;
		rsi.spritesX = new int[20];
		rsi.spritesY = new int[20];
		if (actions) {
		}
		rsi.type = 2;
	}

	public static void Sell() {
		RSInterface rsinterface = addTabInterface(24700);
		int x = 9;
		addSprite(24701, 0, "Interfaces/GrandExchange/sell");
		addHoverButton(24702, "Interfaces/GrandExchange/close", 1, 16, 16, "Close", 0, 24703, 1);
		addHoveredButton(24703, "Interfaces/GrandExchange/close", 2, 16, 16, 24704);
		addHoverButton(24706, "Interfaces/GrandExchange/sprite", 1, 13, 13, "Decrease Quantity", 0, 24707, 1);
		addHoveredButton(24707, "Interfaces/GrandExchange/sprite", 3, 13, 13, 24708);
		addHoverButton(24710, "Interfaces/GrandExchange/sprite", 2, 13, 13, "Increase Quantity", 0, 24711, 1);
		addHoveredButton(24711, "Interfaces/GrandExchange/sprite", 4, 13, 13, 24712);
		addHoverButton(24714, "Interfaces/GrandExchange/sprite", 5, 35, 25, "Sell 1", 0, 24715, 1);
		addHoveredButton(24715, "Interfaces/GrandExchange/sprite", 6, 35, 25, 24716);
		addHoverButton(24718, "Interfaces/GrandExchange/sprite", 7, 35, 25, "Sell 10", 0, 24719, 1);
		addHoveredButton(24719, "Interfaces/GrandExchange/sprite", 8, 35, 25, 24720);
		addHoverButton(24722, "Interfaces/GrandExchange/sprite", 9, 35, 25, "Sell 100", 0, 24723, 1);
		addHoveredButton(24723, "Interfaces/GrandExchange/sprite", 10, 35, 25, 24724);
		addHoverButton(24726, "Interfaces/GrandExchange/sprite", 29, 35, 25, "Sell All", 0, 24727, 1);
		addHoveredButton(24727, "Interfaces/GrandExchange/sprite", 30, 35, 25, 24728);
		addHoverButton(24730, "Interfaces/GrandExchange/sprite", 13, 35, 25, "Edit Quantity", 7713, 24731, 1);
		addHoveredButton(24731, "Interfaces/GrandExchange/sprite", 14, 35, 25, 24732);
		addHoverButton(24734, "Interfaces/GrandExchange/sprite", 15, 35, 25, "Decrease Price", 0, 24735, 1);
		addHoveredButton(24735, "Interfaces/GrandExchange/sprite", 16, 35, 25, 24736);
		addHoverButton(24738, "Interfaces/GrandExchange/sprite", 17, 35, 25, "Offer Guild Price", 0, 24739, 1);
		addHoveredButton(24739, "Interfaces/GrandExchange/sprite", 18, 35, 25, 24740);
		addHoverButton(24742, "Interfaces/GrandExchange/sprite", 13, 35, 25, "Edit Price", 7715, 24743, 1);
		addHoveredButton(24743, "Interfaces/GrandExchange/sprite", 14, 35, 25, 24744);
		addHoverButton(24746, "Interfaces/GrandExchange/sprite", 19, 35, 25, "Increase Price", 0, 24747, 1);
		addHoveredButton(24747, "Interfaces/GrandExchange/sprite", 20, 35, 25, 24748);
		addHoverButton(24750, "Interfaces/GrandExchange/sprite", 21, 120, 43, "Confirm Offer", 0, 24751, 1);
		addHoveredButton(24751, "Interfaces/GrandExchange/sprite", 22, 120, 43, 24752);
		addHoverButton(24758, "Interfaces/GrandExchange/sprite", 25, 29, 23, "Back", 0, 24759, 1);
		addHoveredButton(24759, "Interfaces/GrandExchange/sprite", 26, 29, 23, 24760);
		addHoverButton(24762, "Interfaces/GrandExchange/sprite", 1, 13, 13, "Decrease Price", 0, 24763, 1);
		addHoveredButton(24763, "Interfaces/GrandExchange/sprite", 3, 13, 13, 24764);
		addHoverButton(24765, "Interfaces/GrandExchange/sprite", 2, 13, 13, "Increase Price", 0, 24766, 1);
		addHoveredButton(24766, "Interfaces/GrandExchange/sprite", 4, 13, 13, 24767);
		// addText(24769, "Choose an item to exchange", 0, 0x96731A, false,
		// true);
		// addText(24770, "Select an item from your invertory to sell.", 0,
		// 0x958E60, false, true);
		// addText(24771, "0", 0, 0xB58338, true, true);
		// addText(24772, "1 gp", 0, 0xB58338, true, true);
		// addText(24773, "0 gp", 0, 0xB58338, true, true);
		RSInterface add = addInterface(24780);
		addToItemGroup(add, 1, 1, 24, 24, true, "[GE]", "[GE]", "[GE]");
		// addText(24782, "N/A", 0, 0xB58338, false, true);
		rsinterface.totalChildren(40);
		rsinterface.child(0, 24701, 4 + x, 23);
		rsinterface.child(1, 24702, 464 + x, 33);
		rsinterface.child(2, 24703, 464 + x, 33);
		rsinterface.child(3, 24706, 46 + x, 184);
		rsinterface.child(4, 24707, 46 + x, 184);
		rsinterface.child(5, 24710, 226 + x, 184);
		rsinterface.child(6, 24711, 226 + x, 184);
		rsinterface.child(7, 24714, 43 + x, 208);
		rsinterface.child(8, 24715, 43 + x, 208);
		rsinterface.child(9, 24718, 84 + x, 208);
		rsinterface.child(10, 24719, 84 + x, 208);
		rsinterface.child(11, 24722, 125 + x, 208);
		rsinterface.child(12, 24723, 125 + x, 208);
		rsinterface.child(13, 24726, 166 + x, 208);
		rsinterface.child(14, 24727, 166 + x, 208);
		rsinterface.child(15, 24730, 207 + x, 208);
		rsinterface.child(16, 24731, 207 + x, 208);
		rsinterface.child(17, 24734, 260 + x, 208);
		rsinterface.child(18, 24735, 260 + x, 208);
		rsinterface.child(19, 24738, 316 + x, 208);
		rsinterface.child(20, 24739, 316 + x, 208);
		rsinterface.child(21, 24742, 357 + x, 208);
		rsinterface.child(22, 24743, 357 + x, 208);
		rsinterface.child(23, 24746, 413 + x, 208);
		rsinterface.child(24, 24747, 413 + x, 208);
		rsinterface.child(25, 24750, 191 + x, 273);
		rsinterface.child(26, 24751, 191 + x, 273);
		rsinterface.child(27, 24758, 19 + x, 284);
		rsinterface.child(28, 24759, 19 + x, 284);
		rsinterface.child(29, 24762, 260 + x, 184);
		rsinterface.child(30, 24763, 260 + x, 184);
		rsinterface.child(31, 24765, 435 + x, 184);
		rsinterface.child(32, 24766, 435 + x, 184);
		rsinterface.child(33, 24769, 202 + x, 71);
		rsinterface.child(34, 24770, 202 + x, 98);
		rsinterface.child(35, 24771, 142 + x, 185);
		rsinterface.child(36, 24772, 354 + x, 185);
		rsinterface.child(37, 24773, 252 + x, 246);
		rsinterface.child(38, 24780, 97 + x, 97);
		rsinterface.child(39, 24782, 121, 136);
	}

	public static void BuyandSell() {
		RSInterface Interface = addTabInterface(24500 + 150);
		setChildren(51, Interface);
		addHoverButton(24541 + 150, "b", 3, 138, 108, "Abort offer", 0, 24542 + 150, 1);
		addHoverButton(24543 + 150, "b", 3, 138, 108, "View offer", 0, 24544 + 150, 1);
		addHoverButton(24545 + 150, "b", 3, 138, 108, "Abort offer", 0, 24546 + 150, 1);
		addHoverButton(24547 + 150, "b", 3, 138, 108, "View offer", 0, 24548 + 150, 1);
		addHoverButton(24549 + 150, "b", 3, 138, 108, "Abort offer", 0, 24550 + 150, 1);
		addHoverButton(24551 + 150, "b", 3, 138, 108, "View offer", 0, 24552 + 150, 1);
		addHoverButton(24553 + 150, "b", 3, 138, 108, "Abort offer", 0, 24554 + 150, 1);
		addHoverButton(24555 + 150, "b", 3, 138, 108, "View offer", 0, 24556 + 150, 1);
		addHoverButton(24557 + 150, "b", 3, 138, 108, "Abort offer", 0, 24558 + 150, 1);
		addHoverButton(24559 + 150, "b", 3, 138, 108, "View offer", 0, 24560 + 150, 1);
		addHoverButton(24561 + 150, "b", 3, 138, 108, "Abort offer", 0, 24562 + 150, 1);
		addHoverButton(24563 + 150, "b", 3, 138, 108, "View offer", 0, 24564 + 150, 1);
		addSprite(1, 24579 + 150, 1, "b", false);
		addSprite(1, 24580 + 150, 1, "b", false);
		addSprite(1, 24581 + 150, 1, "b", false);
		addSprite(1, 24582 + 150, 1, "b", false);
		addSprite(1, 24583 + 150, 1, "b", false);
		addSprite(1, 24584 + 150, 1, "b", false);
		addSprite(24501 + 150, 1, "Interfaces/GE/NEW");
		addHoverButton(24502 + 150, "Interfaces/GE/SPRITE", 1, 21, 21, "Close", 250, 24503 + 150, 3);
		addHoveredButton(24503 + 150, "Interfaces/GE/SPRITE", 3, 21, 21, 24504);
		addHoverButton(24505 + 150, "Interfaces/GE/NEW", 2, 50, 50, "Buy", 0, 24506 + 150, 1);
		addHoveredButton(24506 + 150, "Interfaces/GE/NEW", 4, 50, 50, 24507);
		addHoverButton(24508 + 150, "Interfaces/GE/NEW", 2, 50, 50, "Buy", 0, 24509 + 150, 1);
		addHoveredButton(24509 + 150, "Interfaces/GE/NEW", 4, 50, 50, 24510);
		addHoverButton(24514 + 150, "Interfaces/GE/NEW", 2, 50, 50, "Buy", 0, 24515 + 150, 1);
		addHoveredButton(24515 + 150, "Interfaces/GE/NEW", 4, 50, 50, 24516);
		addHoverButton(24517 + 150, "Interfaces/GE/NEW", 2, 50, 50, "Buy", 0, 24518 + 150, 1);
		addHoveredButton(24518 + 150, "Interfaces/GE/NEW", 4, 50, 50, 24519);
		addHoverButton(24520 + 150, "Interfaces/GE/NEW", 2, 50, 50, "Buy", 0, 24521 + 150, 1);
		addHoveredButton(24521 + 150, "Interfaces/GE/NEW", 4, 50, 50, 24522);
		addHoverButton(24523 + 150, "Interfaces/GE/NEW", 2, 50, 50, "Buy", 0, 24524 + 150, 1);
		addHoveredButton(24524 + 150, "Interfaces/GE/NEW", 4, 50, 50, 24525);
		addHoverButton(24511 + 150, "Interfaces/GE/NEW", 3, 50, 50, "Sell", 0, 24512 + 150, 1);
		addHoveredButton(24512 + 150, "Interfaces/GE/NEW", 5, 50, 50, 24513);
		addHoverButton(24526 + 150, "Interfaces/GE/NEW", 3, 50, 50, "Sell", 0, 24527 + 150, 1);
		addHoveredButton(24527 + 150, "Interfaces/GE/NEW", 5, 50, 50, 24528);
		addHoverButton(24529 + 150, "Interfaces/GE/NEW", 3, 50, 50, "Sell", 0, 24530 + 150, 1);
		addHoveredButton(24530 + 150, "Interfaces/GE/NEW", 5, 50, 50, 24531);
		addHoverButton(24532 + 150, "Interfaces/GE/NEW", 3, 50, 50, "Sell", 0, 24533 + 150, 1);
		addHoveredButton(24533 + 150, "Interfaces/GE/NEW", 5, 50, 50, 24534);
		addHoverButton(24535 + 150, "Interfaces/GE/NEW", 3, 50, 50, "Sell", 0, 24536 + 150, 1);
		addHoveredButton(24536 + 150, "Interfaces/GE/NEW", 5, 50, 50, 24537);
		addHoverButton(24538 + 150, "Interfaces/GE/NEW", 3, 50, 50, "Sell", 0, 24539 + 150, 1);
		addHoveredButton(24539 + 150, "Interfaces/GE/NEW", 5, 50, 50, 24540 + 150);

		RSInterface add = addInterface(24567 + 150);
		addToItemGroup(add, 1, 1, 24, 24, true, "[GE]", "[GE]", "[GE]");
		add = addInterface(24569 + 150);
		addToItemGroup(add, 1, 1, 24, 24, true, "[GE]", "[GE]", "[GE]");
		add = addInterface(24571 + 150);
		addToItemGroup(add, 1, 1, 24, 24, true, "[GE]", "[GE]", "[GE]");
		add = addInterface(24573 + 150);
		addToItemGroup(add, 1, 1, 24, 24, true, "[GE]", "[GE]", "[GE]");
		add = addInterface(24575 + 150);
		addToItemGroup(add, 1, 1, 24, 24, true, "[GE]", "[GE]", "[GE]");
		add = addInterface(24577 + 150);
		addToItemGroup(add, 1, 1, 24, 24, true, "[GE]", "[GE]", "[GE]");

		setBounds(24541 + 150, 30, 74, 0, Interface);
		setBounds(24543 + 150, 30, 74, 1, Interface);
		setBounds(24545 + 150, 186, 74, 2, Interface);
		setBounds(24547 + 150, 186, 74, 3, Interface);
		setBounds(24549 + 150, 342, 74, 4, Interface);
		setBounds(24551 + 150, 342, 74, 5, Interface);
		setBounds(24553 + 150, 30, 194, 6, Interface);
		setBounds(24555 + 150, 30, 194, 7, Interface);
		setBounds(2455 + 150, 186, 194, 8, Interface);
		setBounds(24559 + 150, 186, 194, 9, Interface);
		setBounds(24561 + 150, 339, 194, 10, Interface);
		setBounds(24563 + 150, 339, 194, 11, Interface);
		setBounds(24501 + 150, 4, 23, 12, Interface);
		setBounds(24579 + 150, 30 + 6, 74 + 30, 13, Interface);
		setBounds(24580 + 150, 186 + 6, 74 + 30, 14, Interface);
		setBounds(24581 + 150, 342 + 6, 74 + 30, 15, Interface);
		setBounds(24582 + 150, 30 + 6, 194 + 30, 16, Interface);
		setBounds(24583 + 150, 186 + 6, 194 + 30, 17, Interface);
		setBounds(24584 + 150, 342 + 6, 194 + 30, 18, Interface);
		setBounds(24502 + 150, 480, 32, 19, Interface);
		setBounds(24503 + 150, 480, 32, 20, Interface);
		setBounds(24505 + 150, 45, 115, 21, Interface);
		setBounds(24506 + 150, 45, 106, 22, Interface);
		setBounds(24508 + 150, 45, 234, 23, Interface);
		setBounds(24509 + 150, 45, 225, 24, Interface);
		setBounds(24511 + 150, 105, 115, 25, Interface);
		setBounds(24512 + 150, 105, 106, 26, Interface);
		setBounds(24514 + 150, 358, 115, 27, Interface);
		setBounds(24515 + 150, 358, 106, 28, Interface);
		setBounds(24517 + 150, 202, 234, 29, Interface);
		setBounds(24518 + 150, 202, 225, 30, Interface);
		setBounds(24520 + 150, 358, 234, 31, Interface);
		setBounds(24521 + 150, 358, 225, 32, Interface);
		setBounds(24523 + 150, 202, 115, 33, Interface);
		setBounds(24524 + 150, 202, 106, 34, Interface);
		setBounds(24526 + 150, 261, 115, 35, Interface);
		setBounds(24527 + 150, 261, 106, 36, Interface);
		setBounds(24529 + 150, 417, 115, 37, Interface);
		setBounds(24530 + 150, 417, 106, 38, Interface);
		setBounds(24532 + 150, 105, 234, 39, Interface);
		setBounds(24533 + 150, 105, 225, 40, Interface);
		setBounds(24535 + 150, 261, 234, 41, Interface);
		setBounds(24536 + 150, 261, 225, 42, Interface);
		setBounds(24538 + 150, 417, 234, 43, Interface);
		setBounds(24539 + 150, 417, 225, 44, Interface);

		setBounds(24567 + 150, 39, 106, 45, Interface);
		setBounds(24569 + 150, 46 + 156 - 7, 114 - 7, 46, Interface);
		setBounds(24571 + 150, 46 + 156 + 156 - 7, 114 - 7, 47, Interface);
		setBounds(24573 + 150, 39, 234 - 7, 48, Interface);
		setBounds(24575 + 150, 46 + 156 - 7, 234 - 7, 49, Interface);
		setBounds(24577 + 150, 46 + 156 + 156 - 7, 234 - 7, 50, Interface);

	}

	public static void settingsInterface() {
		RSInterface rsinterface = addTabInterface(26000);
		int x = 168;
		int y = 49;
		addSprite(26001, 0, "Interfaces/settings/base");
		// addText(26002, "More Options", 0xe4a146, true, true, 52, 2);
		addInAreaHover(26003, "Interfaces/summoning/creation/close", 0, 1, 16, 16, "Close", 250, 3);
		addButton(26007, 4, -1, 2, 3, "Interfaces/settings/click", 15, 15, "Toggle function keys", 650, 1);
		addButton(26008, 4, -1, 2, 3, "Interfaces/settings/click", 15, 15, "Toggle halth bars", 651, 1);
		addButton(26010, 4, -1, 2, 3, "Interfaces/settings/click", 15, 15, "Toggle x10 damage", 652, 1);
		addButton(26014, 4, -1, 2, 3, "Interfaces/settings/click", 15, 15, "Toggle day time", 654, 1);
		/**
		 * Fixed buttons
		 */
		addHoverButton(26016, "Interfaces/settings/full", 0, 50, 39, "Fixed Mode", -1, 26017, 1);
		addHoveredButton(26017, "Interfaces/settings/full", 1, 50, 39, 26018);
		/**
		 * Resizable buttons
		 */
		addHoverButton(26019, "Interfaces/settings/full", 2, 50, 39, "Resizable Mode", -1, 26020, 1);
		addHoveredButton(26020, "Interfaces/settings/full", 3, 50, 39, 26021);
		/**
		 * Fullscreen buttons
		 */
		addHoverButton(26022, "Interfaces/settings/full", 4, 50, 39, "Fullscreen Mode", -1, 26023, 1);
		addHoveredButton(26023, "Interfaces/settings/full", 5, 50, 39, 26024);
		setChildren(26, rsinterface);
		int i = 0;
		setBounds(26001, x + 0, y + 0, i, rsinterface);
		i++;
		setBounds(26002, x + 89, y + 3, i, rsinterface);
		i++;
		setBounds(26003, x + 151, y + 3, i, rsinterface);
		i++;
		setBounds(26004, x + 9, y + 25, i, rsinterface);
		i++;
		setBounds(26006, x + 9, y + 57, i, rsinterface);
		i++;
		setBounds(26007, x + 154, y + 29, i, rsinterface);
		i++;
		setBounds(26014, x + 154, y + 125, i, rsinterface);
		i++;
		setBounds(26008, x + 154, y + 61, i, rsinterface);
		i++;
		setBounds(26009, x + 9, y + 89, i, rsinterface);
		i++;
		setBounds(26015, x + 9, y + 121, i, rsinterface);
		i++;
		setBounds(26005, x + 7, y + 115, i, rsinterface);
		i++;
		setBounds(26010, x + 154, y + 93, i, rsinterface);
		i++;
		setBounds(26005, x + 7, y + 51, i, rsinterface);
		i++;
		setBounds(26005, x + 7, y + 83, i, rsinterface);
		i++;
		setBounds(26005, x + 7, y + 115, i, rsinterface);
		i++;
		setBounds(26005, x + 7, y + 147, i, rsinterface);
		i++;
		setBounds(26011, x + 151, y + 22, i, rsinterface);
		i++;
		setBounds(26011, x + 151, y + 53, i, rsinterface);
		i++;
		setBounds(26011, x + 151, y + 85, i, rsinterface);
		i++;
		setBounds(26011, x + 151, y + 117, i, rsinterface);
		i++;
		/**
		 * Fixed
		 */
		setBounds(26016, x + 10, y + 155, i, rsinterface);
		i++;
		setBounds(26017, x + 10, y + 155, i, rsinterface);
		i++;
		/**
		 * Resizable
		 */
		setBounds(26019, x + 64, y + 155, i, rsinterface);
		i++;
		setBounds(26020, x + 64, y + 155, i, rsinterface);
		i++;
		/**
		 * Fullscreen
		 */
		setBounds(26022, x + 118, y + 155, i, rsinterface);
		i++;
		setBounds(26023, x + 118, y + 155, i, rsinterface);
		i++;
	}

	public static final void pestPointExchange() {
		RSInterface qz = addTabInterface(14244);
		qz.children = new int[7];
		qz.childX = new int[7];
		qz.childY = new int[7];
		qz.children[0] = 14245;
		qz.childX[0] = 21;
		qz.childY[0] = 3;
		qz.children[1] = 14246;
		qz.childX[1] = 28;
		qz.childY[1] = 40;
		qz.children[2] = 14247;
		qz.childX[2] = 463;
		qz.childY[2] = 13;
		qz.children[3] = 14248;
		qz.childX[3] = 150;
		qz.childY[3] = 16;
		qz.children[4] = 14306;
		qz.childX[4] = 174;
		qz.childY[4] = 271;
		qz.children[5] = 14307;
		qz.childX[5] = 224;
		qz.childY[5] = 297;
		qz.children[6] = 14308;
		qz.childX[6] = 225;
		qz.childY[6] = 283;
		qz = addTabInterface(14246);
		qz.parentID = 13200;
		qz.id = 14246;
		qz.type = 0;
		qz.atActionType = 0;
		qz.contentType = 221;
		qz.width = 440;
		qz.height = 221;
		qz.opacity = 0;
		qz.mouseOverPopupInterface = -1;
		qz.scrollMax = 340;
		qz.children = new int[60];
		qz.childX = new int[60];
		qz.childY = new int[60];
		qz.children[0] = 14249;
		qz.childX[0] = 3;
		qz.childY[0] = 7;
		qz.children[1] = 14250;
		qz.childX[1] = 225;
		qz.childY[1] = 6;
		qz.children[2] = 14251;
		qz.childX[2] = 6;
		qz.childY[2] = 42;
		qz.children[3] = 14252;
		qz.childX[3] = 222;
		qz.childY[3] = 41;
		qz.children[4] = 14253;
		qz.childX[4] = 4;
		qz.childY[4] = 81;
		qz.children[5] = 14254;
		qz.childX[5] = 223;
		qz.childY[5] = 79;
		qz.children[6] = 14255;
		qz.childX[6] = 4;
		qz.childY[6] = 115;
		qz.children[7] = 14256;
		qz.childX[7] = 45;
		qz.childY[7] = 8;
		qz.children[8] = 14257;
		qz.childX[8] = 264;
		qz.childY[8] = 7;
		qz.children[9] = 14258;
		qz.childX[9] = 45;
		qz.childY[9] = 45;
		qz.children[10] = 14259;
		qz.childX[10] = 264;
		qz.childY[10] = 45;
		qz.children[11] = 14260;
		qz.childX[11] = 46;
		qz.childY[11] = 82;
		qz.children[12] = 14261;
		qz.childX[12] = 264;
		qz.childY[12] = 82;
		qz.children[13] = 14262;
		qz.childX[13] = 46;
		qz.childY[13] = 121;
		qz.children[14] = 14263;
		qz.childX[14] = 46;
		qz.childY[14] = 22;
		qz.children[15] = 14264;
		qz.childX[15] = 92;
		qz.childY[15] = 22;
		qz.children[16] = 14265;
		qz.childX[16] = 152;
		qz.childY[16] = 22;
		qz.children[17] = 14266;
		qz.childX[17] = 265;
		qz.childY[17] = 22;
		qz.children[18] = 14267;
		qz.childX[18] = 311;
		qz.childY[18] = 22;
		qz.children[19] = 14268;
		qz.childX[19] = 371;
		qz.childY[19] = 22;
		qz.children[20] = 14269;
		qz.childX[20] = 46;
		qz.childY[20] = 59;
		qz.children[21] = 14270;
		qz.childX[21] = 92;
		qz.childY[21] = 59;
		qz.children[22] = 14271;
		qz.childX[22] = 152;
		qz.childY[22] = 59;
		qz.children[23] = 14272;
		qz.childX[23] = 264;
		qz.childY[23] = 59;
		qz.children[24] = 14273;
		qz.childX[24] = 311;
		qz.childY[24] = 59;
		qz.children[25] = 14274;
		qz.childX[25] = 371;
		qz.childY[25] = 59;
		qz.children[26] = 14275;
		qz.childX[26] = 46;
		qz.childY[26] = 98;
		qz.children[27] = 14276;
		qz.childX[27] = 92;
		qz.childY[27] = 98;
		qz.children[28] = 14277;
		qz.childX[28] = 152;
		qz.childY[28] = 98;
		qz.children[29] = 14278;
		qz.childX[29] = 264;
		qz.childY[29] = 98;
		qz.children[30] = 14279;
		qz.childX[30] = 311;
		qz.childY[30] = 98;
		qz.children[31] = 14280;
		qz.childX[31] = 371;
		qz.childY[31] = 98;
		qz.children[32] = 14281;
		qz.childX[32] = 46;
		qz.childY[32] = 136;
		qz.children[33] = 14282;
		qz.childX[33] = 92;
		qz.childY[33] = 136;
		qz.children[34] = 14283;
		qz.childX[34] = 152;
		qz.childY[34] = 136;
		qz.children[35] = 14284;
		qz.childX[35] = 51;
		qz.childY[35] = 161;
		qz.children[36] = 14285;
		qz.childX[36] = 8;
		qz.childY[36] = 179;
		qz.children[37] = 14286;
		qz.childX[37] = 235;
		qz.childY[37] = 181;
		qz.children[38] = 14287;
		qz.childX[38] = 12;
		qz.childY[38] = 220;
		qz.children[39] = 14288;
		qz.childX[39] = 230;
		qz.childY[39] = 221;
		qz.children[40] = 14289;
		qz.childX[40] = 8;
		qz.childY[40] = 259;
		qz.children[41] = 14290;
		qz.childX[41] = 233;
		qz.childY[41] = 259;
		qz.children[42] = 14291;
		qz.childX[42] = 12;
		qz.childY[42] = 296;
		qz.children[43] = 14292;
		qz.childX[43] = 46;
		qz.childY[43] = 181;
		qz.children[44] = 14293;
		qz.childX[44] = 264;
		qz.childY[44] = 181;
		qz.children[45] = 14294;
		qz.childX[45] = 46;
		qz.childY[45] = 220;
		qz.children[46] = 14295;
		qz.childX[46] = 264;
		qz.childY[46] = 220;
		qz.children[47] = 14296;
		qz.childX[47] = 46;
		qz.childY[47] = 258;
		qz.children[48] = 14297;
		qz.childX[48] = 264;
		qz.childY[48] = 258;
		qz.children[49] = 14298;
		qz.childX[49] = 46;
		qz.childY[49] = 296;
		qz.children[50] = 14299;
		qz.childX[50] = 46;
		qz.childY[50] = 196;
		qz.children[51] = 14300;
		qz.childX[51] = 264;
		qz.childY[51] = 196;
		qz.children[52] = 14301;
		qz.childX[52] = 46;
		qz.childY[52] = 235;
		qz.children[53] = 14302;
		qz.childX[53] = 264;
		qz.childY[53] = 235;
		qz.children[54] = 14303;
		qz.childX[54] = 46;
		qz.childY[54] = 273;
		qz.children[55] = 14304;
		qz.childX[55] = 264;
		qz.childY[55] = 273;
		qz.children[56] = 14305;
		qz.childX[56] = 46;
		qz.childY[56] = 311;
		qz.children[57] = 14310;
		qz.childX[57] = 264;
		qz.childY[57] = 311;
		qz.children[58] = 14309;
		qz.childX[58] = 264;
		qz.childY[58] = 296;
		qz.children[59] = 14311;
		qz.childX[59] = 233;
		qz.childY[59] = 296;
		C(14247, 501, 501, 16, 16, "Close Window");
		C(14306, 217, 217, 157, 49, "Confirm");
		// addText(14248, "Void Knight's Reward Options", 2, 0xff9040);
		Z(14245, 200, 200, "");
		Z(14284, 216, 216, "");
		// addText(14256, "Attack - 0 xp", 1, 0xff9b00);
		// addText(14257, "Strength - 0 xp", 1, 0xff9b00);
		// addText(14258, "Defence - 0 xp", 1, 0xff9b00);
		// addText(14259, "Ranged - 0 xp", 1, 0xff9b00);
		// addText(14260, "Magic - 0 xp", 1, 0xff9b00);
		// addText(14261, "Hitpoints - 0 xp", 1, 0xff9b00);
		// addText(14262, "Prayer - 0 xp", 1, 0xff9b00);
		// addText(14292, "Void knight mace", 1, 0xff9b00);
		// addText(14293, "Void knight top", 1, 0xff9b00);
		// addText(14294, "Void knight robe", 1, 0xff9b00);
		// addText(14295, "Void knight gloves", 1, 0xff9b00);
		// addText(14296, "Void mage helm", 1, 0xff9b00);
		// addText(14297, "Void ranger helm", 1, 0xff9b00);
		// addText(14298, "Void melee helm", 1, 0xff9b00);
		// addText(14309, "Fighter torso", 1, 0xff9b00);
		addPActionButton(14263, 218, 218, 44, 9, "Use 1 Point");
		addPActionButton(14264, 219, 219, 44, 9, "Use 10 Points");
		addPActionButton(14265, 220, 220, 44, 9, "Use 100 Points");
		addPActionButton(14266, 218, 218, 44, 9, "Use 1 Point");
		addPActionButton(14267, 219, 219, 44, 9, "Use 10 Points");
		addPActionButton(14268, 220, 220, 44, 9, "Use 100 Points");
		addPActionButton(14269, 218, 218, 44, 9, "Use 1 Point");
		addPActionButton(14270, 219, 219, 44, 9, "Use 10 Points");
		addPActionButton(14271, 220, 220, 44, 9, "Use 100 Points");
		addPActionButton(14272, 218, 218, 44, 9, "Use 1 Point");
		addPActionButton(14273, 219, 219, 44, 9, "Use 10 Points");
		addPActionButton(14274, 220, 220, 44, 9, "Use 100 Points");
		addPActionButton(14275, 218, 218, 44, 9, "Use 1 Point");
		addPActionButton(14276, 219, 219, 44, 9, "Use 10 Points");
		addPActionButton(14277, 220, 220, 44, 9, "Use 100 Points");
		addPActionButton(14278, 218, 218, 44, 9, "Use 1 Point");
		addPActionButton(14279, 219, 219, 44, 9, "Use 10 Points");
		addPActionButton(14280, 220, 220, 44, 9, "Use 100 Points");
		addPActionButton(14281, 218, 218, 44, 9, "Use 1 Point");
		addPActionButton(14282, 219, 219, 44, 9, "Use 10 Points");
		addPActionButton(14283, 220, 220, 44, 9, "Use 100 Points");
		addPActionButton(14299, 225, 225, 35, 9, "Use 50 Points");
		addPActionButton(14300, 220, 220, 44, 9, "Use 100 Points");
		addPActionButton(14301, 220, 220, 44, 9, "Use 100 Points");
		addPActionButton(14302, 225, 225, 35, 9, "Use 50 Points");
		addPActionButton(14303, 225, 225, 35, 9, "Use 50 Points");
		addPActionButton(14304, 225, 225, 35, 9, "Use 50 Points");
		addPActionButton(14305, 225, 225, 35, 9, "Use 50 Points");
		addPActionButton(14310, 220, 220, 44, 9, "Use 100 Points");
		// addText(14307, "Points: 0", 0, 0xff9040);
		// addText(14308, "Confirm: 0", 0, 0x969696);
		Z(14249, 201, 201, "");
		Z(14250, 205, 205, "");
		Z(14251, 202, 202, "");
		Z(14252, 206, 206, "");
		Z(14253, 203, 203, "");
		Z(14254, 207, 207, "");
		Z(14255, 204, 204, "");
		Z(14285, 208, 208, "");
		Z(14286, 212, 212, "");
		Z(14287, 209, 209, "");
		Z(14288, 213, 213, "");
		Z(14289, 210, 210, "");
		Z(14290, 214, 214, "");
		Z(14291, 211, 211, "");
		Z(14311, 224, 224, "");
	}

	public static final void Z(int i, int j, int k, String s) {
		RSInterface qz = interfaceCache[i] = new RSInterface();
		qz.id = i;
		qz.parentID = i;
		qz.type = 5;
		qz.atActionType = 1;
		qz.contentType = 0;
		qz.width = 1;
		qz.height = 1;
		qz.opacity = 0;
		qz.mouseOverPopupInterface = 52;
		qz.tooltip = s;
	}

	public static void addPActionButton(int i, int j, int k, int width, int height, String s) {
		RSInterface class9 = interfaceCache[i] = new RSInterface();
		class9.id = i;
		class9.parentID = i;
		class9.type = 5;
		class9.atActionType = 1;
		class9.contentType = 0;
		class9.width = width;
		class9.height = height;
		class9.opacity = 0;
		class9.mouseOverPopupInterface = 52;
		class9.tooltip = s;
	}

	public static final void C(int i, int j, int k, int l, int i1, String s) {
		RSInterface qz = interfaceCache[i] = new RSInterface();
		qz.id = i;
		qz.parentID = i;
		qz.type = 5;
		qz.atActionType = 1;
		qz.contentType = 0;
		qz.width = l;
		qz.height = i1;
		qz.opacity = 0;
		qz.mouseOverPopupInterface = 52;
		qz.tooltip = s;
	}

	public static void drawWelcomeInterface() {
		RSInterface Interface = addTabInterface(1136);
		Interface.totalChildren(5);
		Interface.child(0, 21951, 7, 4);
		Interface.child(1, 21952, 250, 220);
		Interface.child(2, 21953, 250, 220);
		Interface.child(3, 21955, 45, 220);
		Interface.child(4, 21956, 45, 220);
	}

	public static void bountyHunter() {
		RSInterface tab = addInterface(25347);
		tab.totalChildren(5);

		// addText(25349, "Target:", 0xffcccc, false, true, -1, 1);
		// addText(25350, "Zezimablud12", 0xff6666, true, true, -1, 2);
		// addText(25351, "Pickup penalty:", 0xff3333, false, true, -1, 1);
		// addText(25352, "180 Sec", 0xff6666, true, true, -1, 2);

		addTransparentSprite(25348, 0, "Interfaces/others/bh", 145);

		tab.child(0, 25348, 332, 3);
		tab.child(1, 25349, 340, 13);
		tab.child(2, 25350, 452, 13);
		tab.child(3, 25351, 340, 34);
		tab.child(4, 25352, 470, 34);
	}

	private static void addFamiliarHead(int interfaceID, int width, int height, int zoom) {
		RSInterface rsi = addTabInterface(interfaceID);
		rsi.type = 6;
		rsi.anInt233 = 2;
		rsi.mediaID = 4000;
		rsi.modelZoom = zoom;
		rsi.modelRotation1 = 40;
		rsi.modelRotation2 = 1800;
		rsi.height = height;
		rsi.width = width;
	}

	public static void SummonTab() {
		RSInterface localRSInterface = addTabInterface(17011);
		addSprite(17012, 6, "Interfaces/SummonTab/SUMMON");
		addButton(17013, 7, "Interfaces/SummonTab/SUMMON", "Click");
		addFamiliarHead(17027, 75, 55, 900);
		addSprite(17014, 6, "Interfaces/SummonTab/SUMMON");
		addConfigButton(17015, 17032, 14, 8, "Interfaces/SummonTab/SUMMON", 20, 30, "Firmiliar Special", 1, 5, 300);
		addHoverButton(17018, "Interfaces/SummonTab/SUMMON", 2, 38, 36, "Beast of burden Inventory", -1, 17028, 1);
		addHoveredButton(17028, "Interfaces/SummonTab/SUMMON", 12, 38, 36, 17029);
		addHoverButton(17022, "Interfaces/SummonTab/SUMMON", 1, 38, 36, "Call Familiar", -1, 17030, 1);
		addHoveredButton(17030, "Interfaces/SummonTab/SUMMON", 13, 38, 36, 17031);
		addHoverButton(17023, "Interfaces/SummonTab/SUMMON", 3, 38, 36, "Dismiss Familiar", -1, 17033, 1);
		addHoveredButton(17033, "Interfaces/SummonTab/SUMMON", 15, 38, 36, 17034);
		addSprite(17016, 5, "Interfaces/SummonTab/SUMMON");
		// addText(17017, "", 2, 14329120, false, true);
		addSprite(17019, 9, "Interfaces/SummonTab/SUMMON");
		// addText(17021, "", 0, 16753920, false, true);
		addSprite(17020, 10, "Interfaces/SummonTab/SUMMON");
		addSprite(17024, 11, "Interfaces/SummonTab/SUMMON");
		// addText(17025, "", 0, 16753920, false, true);
		// addText(17026, "", 0, 16753920, false, true);
		localRSInterface.totalChildren(19);
		localRSInterface.child(0, 17012, 10, 25);
		localRSInterface.child(1, 17013, 24, 7);
		localRSInterface.child(2, 17014, 10, 25);
		localRSInterface.child(3, 17015, 11, 25);
		localRSInterface.child(4, 17016, 15, 140);
		localRSInterface.child(5, 17017, 45, 143);
		localRSInterface.child(6, 17018, 20, 170);
		localRSInterface.child(7, 17019, 115, 167);
		localRSInterface.child(8, 17020, 143, 170);
		localRSInterface.child(9, 17021, 135, 197);
		localRSInterface.child(10, 17022, 20, 213);
		localRSInterface.child(11, 17023, 67, 193);
		localRSInterface.child(12, 17024, 135, 214);
		localRSInterface.child(13, 17025, 135, 240);
		localRSInterface.child(14, 17026, 21, 59);
		localRSInterface.child(15, 17027, 75, 55);
		localRSInterface.child(16, 17028, 20, 170);
		localRSInterface.child(17, 17030, 20, 213);
		localRSInterface.child(18, 17033, 67, 193);
	}

	public static void RingColourChange() {
		RSInterface Interface = addTabInterface(29812);
		setChildren(26, Interface);
		addSprite(29813, 0, "Interfaces/RingofKinship/BACKGROUND");
		addHover(29814, 3, 0, 29815, 0, "Interfaces/RingofKinship/CLOSE", 17, 16, "Exit");
		addHovered(29815, 1, "Interfaces/RingofKinship/CLOSE", 17, 16, 29816);
		addHoverButton(29817, "Interfaces/RingofKinship/EXAMINE", 0, 72, 64, "Examine @or1@Ring of Kinship", -1, 29818,
				1);
		addHoveredButton(29818, "Interfaces/RingofKinship/EXAMINE", 0, 72, 64, 29819);
		addHoverButton(29820, "Interfaces/RingofKinship/EXAMINE", 0, 72, 64, "Examine @or1@Ring of Kinship", -1, 29821,
				1);
		addHoveredButton(29821, "Interfaces/RingofKinship/EXAMINE", 0, 72, 64, 29822);
		addHoverButton(29823, "Interfaces/RingofKinship/EXAMINE", 0, 72, 64, "Examine @or1@Ring of Kinship", -1, 29824,
				1);
		addHoveredButton(29824, "Interfaces/RingofKinship/EXAMINE", 0, 72, 64, 29825);
		addHoverButton(29826, "Interfaces/RingofKinship/EXAMINE", 0, 72, 64, "Examine @or1@Ring of Kinship", -1, 29827,
				1);
		addHoveredButton(29827, "Interfaces/RingofKinship/EXAMINE", 0, 72, 64, 29828);
		addHoverButton(29830, "Interfaces/RingofKinship/BUTTON", 2, 75, 23, "Buy", -1, 29831, 1);
		addHoveredButton(29831, "Interfaces/RingofKinship/BUTTON", 3, 75, 23, 29832);
		addHoverButton(29833, "Interfaces/RingofKinship/BUTTON", 2, 75, 23, "Buy", -1, 29834, 1);
		addHoveredButton(29834, "Interfaces/RingofKinship/BUTTON", 3, 75, 23, 29835);
		addHoverButton(29836, "Interfaces/RingofKinship/BUTTON", 2, 75, 23, "Buy", -1, 29837, 1);
		addHoveredButton(29837, "Interfaces/RingofKinship/BUTTON", 3, 75, 23, 29838);
		addHoverButton(29839, "Interfaces/RingofKinship/BUTTON", 2, 75, 23, "Buy", -1, 29840, 1);
		addHoveredButton(29840, "Interfaces/RingofKinship/BUTTON", 3, 75, 23, 29841);
		// addText(29842, "Buy", 1, 0xFFFFFF, false, true);
		// addText(29843, "Buy", 1, 0xFFFFFF, false, true);
		// addText(29844, "Buy", 1, 0xFFFFFF, false, true);
		// addText(29845, "Buy", 1, 0xFFFFFF, false, true);
		// addText(29846,
		setBounds(29813, 60, 71, 0, Interface);
		setBounds(29814, 433, 80, 1, Interface);
		setBounds(29815, 433, 80, 2, Interface);
		setBounds(29817, 95, 111, 3, Interface);
		setBounds(29818, 95, 111, 4, Interface);
		setBounds(29820, 180, 111, 5, Interface);
		setBounds(29821, 180, 111, 6, Interface);
		setBounds(29823, 265, 111, 7, Interface);
		setBounds(29824, 265, 111, 8, Interface);
		setBounds(29826, 350, 111, 9, Interface);
		setBounds(29827, 350, 111, 10, Interface);
		setBounds(29829, 163, 78, 11, Interface);
		setBounds(29830, 92, 191, 12, Interface);
		setBounds(29831, 92, 191, 13, Interface);
		setBounds(29833, 178, 191, 14, Interface);
		setBounds(29834, 178, 191, 15, Interface);
		setBounds(29836, 264, 191, 16, Interface);
		setBounds(29837, 264, 191, 17, Interface);
		setBounds(29839, 350, 191, 18, Interface);
		setBounds(29840, 350, 191, 19, Interface);
		setBounds(29842, 118, 193, 20, Interface);
		setBounds(29843, 204, 193, 21, Interface);
		setBounds(29844, 290, 193, 22, Interface);
		setBounds(29845, 376, 193, 23, Interface);
		setBounds(29846, 80, 223, 24, Interface);
		setBounds(29847, 190, 239, 25, Interface);
	}

	public static void questTab() {
		RSInterface localRSInterface = addInterface(639);
		setChildren(4, localRSInterface);
		// addText(39155, "Quests", 16750623, false, true, 52, 2);
		addButton(39156, 1, "Interfaces/QuestTab/QUEST", 18, 18, "Swap to Quest Diary", 1);
		addSprite(39157, 0, "Interfaces/QuestTab/QUEST");
		setBounds(39155, 10, 5, 0, localRSInterface);
		setBounds(39156, 165, 5, 1, localRSInterface);
		setBounds(39157, 3, 24, 2, localRSInterface);
		setBounds(39160, 5, 29, 3, localRSInterface);
		localRSInterface = addInterface(39160);
		localRSInterface.height = 214;
		localRSInterface.width = 165;
		localRSInterface.scrollMax = 1700;
		setChildren(105, localRSInterface);
		// addText(39161, "GameCharacter Information", 16750623, false, true,
		// 52, 2);
		// addText(663, "", 16750623, false, true, 52, 2);
		// tab.disabledSprite(39162, "", "", 0, 16711680, false, true, 150);
		setBounds(39161, 8, 0, 0, localRSInterface);
		setBounds(39162, 8, 18, 1, localRSInterface);
		setBounds(39163, 8, 33, 2, localRSInterface);
		setBounds(39164, 8, 48, 3, localRSInterface);
		setBounds(663, 4, 63, 4, localRSInterface);
		int i = 63;
		int j = 5;
		for (int k = 39165; k <= 39264; k++) {
			// tab.disabledSprite(k, "", "View Quest", 0, 16711680, false, true,
			setBounds(k, 8, i, j, localRSInterface);
			j++;
			i += 15;
		}
		localRSInterface = addInterface(39265);
		try {
			setChildren(4, localRSInterface);
			// addText(39266, " ", 16750623, false, true, -1, 2);
			addButton(39267, 2, "Interfaces/QuestTab/QUEST", 18, 18, "Swap to Quest Diary", 1);
			addSprite(39269, 0, "Interfaces/QuestTab/QUEST");
			setBounds(39266, 10, 5, 0, localRSInterface);
			setBounds(39267, 165, 5, 1, localRSInterface);
			setBounds(39269, 3, 24, 2, localRSInterface);
			setBounds(39268, 5, 29, 3, localRSInterface);
			localRSInterface = addInterface(39268);
			localRSInterface.height = 214;
			localRSInterface.width = 165;
			localRSInterface.scrollMax = 1700;
			setChildren(20, localRSInterface);
			setBounds(39295, 8, 4, 0, localRSInterface);
			setBounds(39296, 8, 16, 1, localRSInterface);
			setBounds(39297, 8, 29, 2, localRSInterface);
			setBounds(39298, 8, 42, 3, localRSInterface);
			setBounds(39299, 8, 54, 4, localRSInterface);
			setBounds(39300, 8, 66, 5, localRSInterface);
			setBounds(39301, 8, 78, 6, localRSInterface);
			setBounds(39302, 8, 90, 7, localRSInterface);
			setBounds(39303, 8, 102, 8, localRSInterface);
			setBounds(39304, 8, 114, 9, localRSInterface);
			setBounds(39305, 8, 126, 10, localRSInterface);
			setBounds(39306, 8, 138, 11, localRSInterface);
			setBounds(39307, 8, 150, 12, localRSInterface);
			setBounds(39308, 8, 162, 13, localRSInterface);
			setBounds(39309, 8, 174, 14, localRSInterface);
			setBounds(39310, 8, 186, 15, localRSInterface);
			setBounds(39311, 8, 198, 16, localRSInterface);
			setBounds(39312, 8, 210, 17, localRSInterface);
			setBounds(39313, 8, 222, 18, localRSInterface);
			setBounds(39314, 8, 234, 19, localRSInterface);
			// tab.disabledSprite(39295, "", "", 0, 16750623, false, true, 150);
			// tab.disabledSprite(39296, "", " ", 0, 16711680, false, true,
			// 150);
			// tab.disabledSprite(39297, "", " ", 0, 16711680, false, true,
			// 150);
			// tab.disabledSprite(39298, "", " ", 0, 16711680, false, true,
			// 150);
			// tab.disabledSprite(39299, "", " ", 0, 16711680, false, true,
			// 150);
			// tab.disabledSprite(39300, "", "", 0, 16711680, false, true, 150);
			// tab.disabledSprite(39301, "", "", 0, 16750623, false, true, 150);
			// tab.disabledSprite(39302, "", " ", 0, 16711680, false, true,
			// 150);
			// tab.disabledSprite(39303, "", " ", 0, 16711680, false, true,
			// 150);
			// tab.disabledSprite(39304, "", " ", 0, 16711680, false, true,
			// 150);
			// tab.disabledSprite(39305, "", "", 0, 16711680, false, true, 150);
			// tab.disabledSprite(39306, "", "", 0, 16750623, false, true, 150);
			// tab.disabledSprite(39307, "", " ", 0, 16711680, false, true,
			// 150);
			// tab.disabledSprite(39308, "", " ", 0, 16711680, false, true,
			// 150);
			// tab.disabledSprite(39309, "", " ", 0, 16711680, false, true,
			// 150);
			// tab.disabledSprite(39310, "", " ", 0, 16711680, false, true,
			// 150);
			// tab.disabledSprite(39311, "", " ", 0, 16711680, false, true,
			// 150);
			// tab.disabledSprite(39312, " ", " ", 0, 16711680, false, true,
			// 150);
			// tab.disabledSprite(39313, "", "", 0, 16711680, false, true, 150);
			// tab.disabledSprite(39314, "", "", 0, 16711680, false, true, 150);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	public static void achievementTab() {
		RSInterface Interface = addInterface(638);
		setChildren(4, Interface);
		// addText(29155, "Quests", 0xFF981F, false, true, 52, 2);
		addButton(29156, 2, "Interfaces/QuestTab/QUEST", 18, 18, "Swap to Quest Tab", 1);
		addSprite(29157, 0, "Interfaces/QuestTab/QUEST");
		setBounds(29155, 10, 5, 0, Interface);
		setBounds(29156, 165, 5, 1, Interface);
		setBounds(29157, 3, 24, 2, Interface);
		setBounds(29160, 5, 29, 3, Interface);
		Interface = addInterface(29160);
		Interface.height = 214;
		Interface.width = 165;
		Interface.scrollMax = 1700;
		setChildren(105, Interface);
		// addText(29161, "TBD", 0xFF981F, false, true, 52, 2);
		// tab.disabledSprite(29162, "", "Spawn", 0, 0xff0000, false, true,
		// 150);
		// tab.disabledSprite(29163, "", "Spawn", 0, 0xff0000, false, true,
		// 150);
		// tab.disabledSprite(29164, "", "Spawn", 0, 0xff0000, false, true,
		// 150);

		setBounds(29161, 8, 5, 0, Interface);
		setBounds(29162, 8, 20, 1, Interface);
		setBounds(29163, 8, 35, 2, Interface);
		setBounds(29164, 8, 50, 3, Interface);
		setBounds(663, 4, 65, 4, Interface);
		int Ypos = 65;
		int frameID = 5;

		for (int id = 29165; id <= 29264; id++) {
			// tab.disabledSprite(id, "" + id + "", "tbd", 0, 0xff0000, false,
			setBounds(id, 8, Ypos, frameID, Interface);
			frameID++;
			Ypos += 15;
			Ypos++;
		}

		// addText(29173, "", 0xFF981F, false, true, 58, 2);

		Interface = addInterface(29265);
		try {
			setChildren(4, Interface);
			// addText(29266, "Coming soon!", 0xFF981F, false, true, -1, 2);
			addButton(29267, 1, "Interfaces/QuestTab/QUEST", 18, 18, "Swap to GameCharacter Info", 1);
			addSprite(29269, 0, "Interfaces/QuestTab/QUEST");
			setBounds(29266, 10, 5, 0, Interface);
			setBounds(29267, 165, 5, 1, Interface);
			setBounds(29269, 3, 24, 2, Interface);
			setBounds(29268, 5, 29, 3, Interface);
			Interface = addInterface(29268);
			Interface.height = 214;
			Interface.width = 165;
			Interface.scrollMax = 1700;
			setChildren(20, Interface);
			setBounds(29295, 8, 4, 0, Interface);
			setBounds(29296, 8, 16, 1, Interface);
			setBounds(29297, 8, 29, 2, Interface);
			setBounds(29298, 8, 42, 3, Interface);
			setBounds(29299, 8, 54, 4, Interface);
			setBounds(29300, 8, 66, 5, Interface);
			setBounds(29301, 8, 78, 6, Interface);
			setBounds(29302, 8, 90, 7, Interface);
			setBounds(29303, 8, 102, 8, Interface);
			setBounds(29304, 8, 114, 9, Interface);
			setBounds(29305, 8, 126, 10, Interface);
			setBounds(29306, 8, 138, 11, Interface);
			setBounds(29307, 8, 150, 12, Interface);
			setBounds(29308, 8, 162, 13, Interface);
			setBounds(29309, 8, 174, 14, Interface);
			setBounds(29310, 8, 186, 15, Interface);
			setBounds(29311, 8, 198, 16, Interface);
			setBounds(29312, 8, 210, 17, Interface);
			setBounds(29313, 8, 222, 18, Interface);
			setBounds(29314, 8, 234, 19, Interface);
			// tab.disabledSprite(29295, "Please register at",
			// "Please Register",
			// tab.disabledSprite(29296, "www.divinefury.org", "", 0, 0xff0000,
			// tab.disabledSprite(29297, "And advertise/vote daily!", "", 0,
			// tab.disabledSprite(29298, "::vote for more players!", "", 0,
			// tab.disabledSprite(29299, "More players=More updates!", "", 0,
			// tab.disabledSprite(29300, "", "", 0, 0xff0000, false, true, 150);
			// tab.disabledSprite(29301, "", "", 1, 0xFF981F, false, true, 150);
			// tab.disabledSprite(29302, "", "", 0, 0xff0000, false, true, 150);
			// tab.disabledSprite(29303, "", "", 0, 0xff0000, false, true, 150);
			// tab.disabledSprite(29304, "", "", 0, 0xff0000, false, true, 150);
			// tab.disabledSprite(29305, "", "", 0, 0xff0000, false, true, 150);
			// tab.disabledSprite(29306, "", "", 1, 0xFF981F, false, true, 150);
			// tab.disabledSprite(29307, "", "", 0, 0xff0000, false, true, 150);
			// tab.disabledSprite(29308, "", "", 0, 0xff0000, false, true, 150);
			// tab.disabledSprite(29309, "", "", 0, 0xff0000, false, true, 150);
			// tab.disabledSprite(29310, "", "", 0, 0xff0000, false, true, 150);
			// tab.disabledSprite(29311, "", "", 0, 0xff0000, false, true, 150);
			// tab.disabledSprite(29312, "", "", 0, 0xff0000, false, true, 150);
			// tab.disabledSprite(29313, "", "", 0, 0xff0000, false, true, 150);
			// tab.disabledSprite(29314, "", "", 0, 0xff0000, false, true, 150);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void customStrings() {
		RSInterface rsinterface = addTabInterface(17050); // interface id
		// addText(17051, "Custom Text", 0xff9040, false, true, 52, 0); // text
		// shown
		rsinterface.scrollMax = 0; // leave this
		rsinterface.children = new int[1]; // how many children there are ( only
											// added 1
		rsinterface.childX = new int[1]; // children x ( same as above ) needs
											// to be as many strings as there
											// are
		rsinterface.childY = new int[1]; // children y (same as above)
		rsinterface.children[0] = 17051; // first child starting the children
											// each will be + 1 of this one
		rsinterface.childX[0] = 40; // x coord
		rsinterface.childY[0] = 40; // y coord
	}

	public static void GodWars() {
		RSInterface rsinterface = addTabInterface(16220);
		// addText(16211, "Mob killcount", 0xff9040, false, true, 52, 0);
		// addText(16212, "Armadyl kills", 0xff9040, false, true, 52, 0);
		// addText(16213, "Bandos kills", 0xff9040, false, true, 52, 0);
		// addText(16214, "Saradomin kills", 0xff9040, false, true, 52, 0);
		// addText(16215, "Zamorak kills", 0xff9040, false, true, 52, 0);
		// addText(16216, "0", 0x66FFFF, false, true, 52, 0);// armadyl
		// addText(16217, "0", 0x66FFFF, false, true, 52, 0);// bandos
		// addText(16218, "0", 0x66FFFF, false, true, 52, 0);// saradomin
		// addText(16219, "0", 0x66FFFF, false, true, 52, 0);// zamorak
		rsinterface.scrollMax = 0;
		rsinterface.children = new int[9];
		rsinterface.childX = new int[9];
		rsinterface.childY = new int[9];
		rsinterface.children[0] = 16211;
		rsinterface.childX[0] = -52 + 375 + 30;
		rsinterface.childY[0] = 7;
		rsinterface.children[1] = 16212;
		rsinterface.childX[1] = -52 + 375 + 30;
		rsinterface.childY[1] = 30;
		rsinterface.children[2] = 16213;
		rsinterface.childX[2] = -52 + 375 + 30;
		rsinterface.childY[2] = 44;
		rsinterface.children[3] = 16214;
		rsinterface.childX[3] = -52 + 375 + 30;
		rsinterface.childY[3] = 58;
		rsinterface.children[4] = 16215;
		rsinterface.childX[4] = -52 + 375 + 30;
		rsinterface.childY[4] = 73;

		rsinterface.children[5] = 16216;
		rsinterface.childX[5] = -52 + 460 + 60;
		rsinterface.childY[5] = 31;
		rsinterface.children[6] = 16217;
		rsinterface.childX[6] = -52 + 460 + 60;
		rsinterface.childY[6] = 45;
		rsinterface.children[7] = 16218;
		rsinterface.childX[7] = -52 + 460 + 60;
		rsinterface.childY[7] = 59;
		rsinterface.children[8] = 16219;
		rsinterface.childX[8] = -52 + 460 + 60;
		rsinterface.childY[8] = 74;
	}

	public static final void minigame() {
		RSInterface rsinterface = addTabInterface(45200);
		// addText(45201, "Minigames Teleport", 0xff9b00, false, true, 52, 2);
		addHoverButton(45202, "Interfaces/Minigame/Hover", 0, 172, 24, "Duel Arena", -1, 45203, 1);
		addHoveredButton(45203, "Interfaces/Minigame/Hover", 4, 172, 24, 45204);
		addHoverButton(45218, "Interfaces/Minigame/Hover", 0, 172, 24, "Barrows", -1, 45219, 1);
		addHoveredButton(45219, "Interfaces/Minigame/Hover", 4, 172, 24, 45220);
		addHoverButton(45221, "Interfaces/Minigame/Hover", 0, 172, 24, "Pest Control", -1, 45222, 1);
		addHoveredButton(45222, "Interfaces/Minigame/Hover", 4, 172, 24, 45223);
		addHoverButton(45224, "Interfaces/Minigame/Hover", 0, 172, 24, "Tzhaar", -1, 45225, 1);
		addHoveredButton(45225, "Interfaces/Minigame/Hover", 4, 172, 24, 45226);
		addHoverButton(45227, "Interfaces/Minigame/Hover", 0, 172, 24, "Warriors Guild", -1, 45228, 1);
		addHoveredButton(45228, "Interfaces/Minigame/Hover", 4, 172, 24, 45229);
		addHoverButton(45233, "Interfaces/Minigame/Back", 0, 16, 16, "Back", -1, 45234, 1);
		addHoveredButton(45234, "Interfaces/Minigame/Back", 1, 16, 16, 45235);
		addSprite(45205, 1, "Interfaces/Minigame/DuelArena");
		addSprite(45206, 1, "Interfaces/Minigame/Barrows");
		addSprite(45207, 1, "Interfaces/Minigame/PestControl");
		addSprite(45208, 1, "Interfaces/Minigame/Tzhaar");
		addSprite(45209, 1, "Interfaces/Minigame/Warriors");
		addSprite(45211, 1, "Interfaces/Minigame/Background");
		// addText(45212, "Duel Arena", 0xd67b29, true, true, 52, 2);
		// addText(45213, "Barrows", 0xd67b29, true, true, 52, 2);
		// addText(45214, "Pest Control", 0xd67b29, true, true, 52, 2);
		// addText(45215, "Tzhaar", 0xd67b29, true, true, 52, 2);
		// addText(45216, "Warriors Guild", 0xd67b29, true, true, 52, 2);
		byte childAmount = 24;
		int indexChild = 0;
		setChildren(childAmount, rsinterface);
		setBounds(45211, 0, 26, indexChild, rsinterface);
		indexChild++;
		setBounds(45201, 33, 7, indexChild, rsinterface);
		indexChild++;
		setBounds(45202, 8, 35, indexChild, rsinterface);
		indexChild++;
		setBounds(45203, 8, 35, indexChild, rsinterface);
		indexChild++;
		setBounds(45212, 80, 39, indexChild, rsinterface);
		indexChild++;
		setBounds(45218, 8, 72, indexChild, rsinterface);
		indexChild++;
		setBounds(45219, 8, 72, indexChild, rsinterface);
		indexChild++;
		setBounds(45213, 80, 76, indexChild, rsinterface);
		indexChild++;
		setBounds(45221, 8, 109, indexChild, rsinterface);
		indexChild++;
		setBounds(45222, 8, 109, indexChild, rsinterface);
		indexChild++;
		setBounds(45214, 80, 113, indexChild, rsinterface);
		indexChild++;
		setBounds(45224, 8, 146, indexChild, rsinterface);
		indexChild++;
		setBounds(45225, 8, 146, indexChild, rsinterface);
		indexChild++;
		setBounds(45215, 80, 150, indexChild, rsinterface);
		indexChild++;
		setBounds(45227, 8, 183, indexChild, rsinterface);
		indexChild++;
		setBounds(45228, 8, 183, indexChild, rsinterface);
		indexChild++;
		setBounds(45216, 80, 187, indexChild, rsinterface);
		indexChild++;
		setBounds(45205, 148, 33, indexChild, rsinterface);
		indexChild++;
		setBounds(45206, 148, 70, indexChild, rsinterface);
		indexChild++;
		setBounds(45207, 148, 104, indexChild, rsinterface);
		indexChild++;
		setBounds(45208, 148, 140, indexChild, rsinterface);
		indexChild++;
		setBounds(45209, 148, 179, indexChild, rsinterface);
		indexChild++;
		setBounds(45233, 10, 6, indexChild, rsinterface);
		indexChild++;
		setBounds(45234, 10, 6, indexChild, rsinterface);
		indexChild++;
	}

	public static final void wilderness() {

		RSInterface rsinterface = addTabInterface(45600);
		// addText(45601, "PKing Teleport", 0xff9b00, false, true, 52, 2);
		addHoverButton(45602, "Interfaces/Minigame/Hover", 0, 172, 24, "Mage Bank", -1, 45603, 1);
		addHoveredButton(45603, "Interfaces/Minigame/Hover", 2, 172, 24, 45604);
		addHoverButton(45618, "Interfaces/Minigame/Hover", 0, 172, 24, "Varrock PK (Multi)", -1, 45619, 1);
		addHoveredButton(45619, "Interfaces/Minigame/Hover", 2, 172, 24, 45620);
		addHoverButton(45621, "Interfaces/Minigame/Hover", 0, 172, 24, "GraveYard (Lvl 19)", -1, 45622, 1);
		addHoveredButton(45622, "Interfaces/Minigame/Hover", 2, 172, 24, 45623);
		addHoverButton(45624, "Interfaces/Minigame/Hover", 0, 172, 24, "Edgeville", -1, 45625, 1);
		addHoveredButton(45625, "Interfaces/Minigame/Hover", 2, 172, 24, 45626);
		addHoverButton(45627, "Interfaces/Minigame/Hover", 0, 172, 24, "Green Dragons", -1, 45628, 1);
		addHoveredButton(45628, "Interfaces/Minigame/Hover", 2, 172, 24, 45629);
		addHoverButton(45633, "Interfaces/Minigame/Back", 0, 16, 16, "Back", -1, 45634, 1);
		addHoveredButton(45634, "Interfaces/Minigame/Back", 1, 16, 16, 45635);
		addSprite(45605, 1, "Interfaces/Minigame/Pk");
		addSprite(45606, 1, "Interfaces/Minigame/Pk");
		addSprite(45607, 1, "Interfaces/Minigame/Pk");
		addSprite(45608, 1, "Interfaces/Minigame/Pk");
		addSprite(45609, 1, "Interfaces/Minigame/Pk");
		addSprite(45611, 1, "Interfaces/Minigame/Background");
		// addText(45612, "Mage Bank", 0xd67b29, true, true, 52, 2);
		// addText(45613, "Varrock PK (Multi)", 0xd67b29, true, true, 52, 2);
		// addText(45614, "GraveYard", 0xd67b29, true, true, 52, 2);
		// addText(45615, "Edgeville", 0xd67b29, true, true, 52, 2);
		// addText(45616, "Green Dragons", 0xd67b29, true, true, 52, 2);
		byte childAmount = 24;
		int indexChild = 0;
		setChildren(childAmount, rsinterface);
		setBounds(45611, -1, 26, indexChild, rsinterface);
		indexChild++;
		setBounds(45601, 33, 7, indexChild, rsinterface);
		indexChild++;
		setBounds(45602, 8, 35, indexChild, rsinterface);
		indexChild++;
		setBounds(45603, 8, 35, indexChild, rsinterface);
		indexChild++;
		setBounds(45612, 80, 39, indexChild, rsinterface);
		indexChild++;
		setBounds(45618, 8, 72, indexChild, rsinterface);
		indexChild++;
		setBounds(45619, 8, 72, indexChild, rsinterface);
		indexChild++;
		setBounds(45613, 80, 76, indexChild, rsinterface);
		indexChild++;
		setBounds(45621, 8, 109, indexChild, rsinterface);
		indexChild++;
		setBounds(45622, 8, 109, indexChild, rsinterface);
		indexChild++;
		setBounds(45614, 80, 113, indexChild, rsinterface);
		indexChild++;
		setBounds(45624, 8, 146, indexChild, rsinterface);
		indexChild++;
		setBounds(45625, 8, 146, indexChild, rsinterface);
		indexChild++;
		setBounds(45615, 80, 150, indexChild, rsinterface);
		indexChild++;
		setBounds(45627, 8, 183, indexChild, rsinterface);
		indexChild++;
		setBounds(45628, 8, 183, indexChild, rsinterface);
		indexChild++;
		setBounds(45616, 80, 187, indexChild, rsinterface);
		indexChild++;
		setBounds(45605, 148, 34, indexChild, rsinterface);
		indexChild++;
		setBounds(45606, 148, 71, indexChild, rsinterface);
		indexChild++;
		setBounds(45607, 148, 108, indexChild, rsinterface);
		indexChild++;
		setBounds(45608, 148, 146, indexChild, rsinterface);
		indexChild++;
		setBounds(45609, 148, 183, indexChild, rsinterface);
		indexChild++;
		setBounds(45633, 10, 6, indexChild, rsinterface);
		indexChild++;
		setBounds(45634, 10, 6, indexChild, rsinterface);
		indexChild++;
	}

	public static final void boss() {
		RSInterface rsinterface = addTabInterface(45500);
		// addText(45501, "Boss Teleport", 0xff9b00, false, true, 52, 2);
		addHoverButton(45502, "Interfaces/Minigame/Hover", 0, 172, 24, "Nex", -1, 45503, 1);
		addHoveredButton(45503, "Interfaces/Minigame/Hover", 3, 172, 24, 45504);
		addHoverButton(45518, "Interfaces/Minigame/Hover", 0, 172, 24, "King Black Dragon", -1, 45519, 1);
		addHoveredButton(45519, "Interfaces/Minigame/Hover", 3, 172, 24, 45520);
		addHoverButton(45521, "Interfaces/Minigame/Hover", 0, 172, 24, "Dagannoth Kings", -1, 45522, 1);
		addHoveredButton(45522, "Interfaces/Minigame/Hover", 3, 172, 24, 45523);
		addHoverButton(45524, "Interfaces/Minigame/Hover", 0, 172, 24, "Tormented Demons", -1, 45525, 1);
		addHoveredButton(45525, "Interfaces/Minigame/Hover", 3, 172, 24, 45526);
		addHoverButton(45527, "Interfaces/Minigame/Hover", 0, 172, 24, "Corporal Beast", -1, 45528, 1);
		addHoveredButton(45528, "Interfaces/Minigame/Hover", 3, 172, 24, 45529);
		addHoverButton(45533, "Interfaces/Minigame/Back", 0, 16, 16, "Back", -1, 45534, 1);
		addHoveredButton(45534, "Interfaces/Minigame/Back", 1, 16, 16, 45535);
		addSprite(45505, 1, "Interfaces/Minigame/Godwars");
		addSprite(45506, 1, "Interfaces/Minigame/Kbd");
		addSprite(45507, 1, "Interfaces/Minigame/Dagganoths");
		addSprite(45508, 1, "Interfaces/Minigame/Chaos");
		addSprite(45509, 1, "Interfaces/Minigame/Corporeal");
		addSprite(45511, 1, "Interfaces/Minigame/Background");
		// addText(45512, "Nex", 0xd67b29, true, true, 52, 2);
		// addText(45513, "King Black Dragon", 0xd67b29, true, true, 52, 2);
		// addText(45514, "Dagannoth Kings", 0xd67b29, true, true, 52, 2);
		// addText(45515, "Tormented Demons", 0xd67b29, true, true, 52, 2);
		// addText(45516, "Corporal Beast", 0xd67b29, true, true, 52, 2);
		byte childAmount = 24;
		int indexChild = 0;
		setChildren(childAmount, rsinterface);
		setBounds(45511, -1, 26, indexChild, rsinterface);
		indexChild++;
		setBounds(45501, 33, 7, indexChild, rsinterface);
		indexChild++;
		setBounds(45502, 8, 35, indexChild, rsinterface);
		indexChild++;
		setBounds(45503, 8, 35, indexChild, rsinterface);
		indexChild++;
		setBounds(45512, 80, 39, indexChild, rsinterface);
		indexChild++;
		setBounds(45518, 8, 72, indexChild, rsinterface);
		indexChild++;
		setBounds(45519, 8, 72, indexChild, rsinterface);
		indexChild++;
		setBounds(45513, 80, 76, indexChild, rsinterface);
		indexChild++;
		setBounds(45521, 8, 109, indexChild, rsinterface);
		indexChild++;
		setBounds(45522, 8, 109, indexChild, rsinterface);
		indexChild++;
		setBounds(45514, 80, 113, indexChild, rsinterface);
		indexChild++;
		setBounds(45524, 8, 146, indexChild, rsinterface);
		indexChild++;
		setBounds(45525, 8, 146, indexChild, rsinterface);
		indexChild++;
		setBounds(45515, 80, 150, indexChild, rsinterface);
		indexChild++;
		setBounds(45527, 8, 183, indexChild, rsinterface);
		indexChild++;
		setBounds(45528, 8, 183, indexChild, rsinterface);
		indexChild++;
		setBounds(45516, 80, 187, indexChild, rsinterface);
		indexChild++;
		setBounds(45505, 148, 33, indexChild, rsinterface);
		indexChild++;
		setBounds(45506, 148, 70, indexChild, rsinterface);
		indexChild++;
		setBounds(45507, 148, 104, indexChild, rsinterface);
		indexChild++;
		setBounds(45508, 148, 144, indexChild, rsinterface);
		indexChild++;
		setBounds(45509, 148, 179, indexChild, rsinterface);
		indexChild++;
		setBounds(45533, 10, 6, indexChild, rsinterface);
		indexChild++;
		setBounds(45534, 10, 6, indexChild, rsinterface);
		indexChild++;
	}

	public static void teleport() {
		RSInterface localRSInterface = addInterface(11650);
		addSprite(11651, 10, "CLICK");
		addHoverButton(11652, "CLICK", 2, 200, 30, "Which Zone?", -1, 11653, 1);
		addHoveredButton(11653, "CLICK", 2, 200, 30, 11654);
		addHoverButton(11655, "CLICK", 3, 200, 30, "Which Zone?", -1, 11656, 1);
		addHoveredButton(11656, "CLICK", 3, 200, 30, 11657);
		addHoverButton(11658, "CLICK", 3, 200, 30, "Which Zone?", -1, 11659, 1);
		addHoveredButton(11659, "CLICK", 3, 200, 30, 11660);
		addHoverButton(11661, "CLICK", 3, 200, 30, "Which Zone?", -1, 11662, 1);
		addHoveredButton(11662, "CLICK", 3, 200, 30, 11663);
		addHoverButton(11664, "CLICK", 3, 200, 30, "Which Zone?", -1, 11665, 1);
		addHoveredButton(11665, "CLICK", 3, 200, 30, 11666);
		addHoverButton(11667, "CLICK", 3, 200, 30, "Which Zone?", -1, 11668, 1);
		addHoveredButton(11668, "CLICK", 3, 200, 30, 11669);
		addHoverButton(11670, "CLICK", 3, 200, 30, "Which Zone?", -1, 11671, 1);
		addHoveredButton(11671, "CLICK", 3, 200, 30, 11672);
		addHoverButton(11673, "CLICK", 1, 200, 30, "Stop Viewing", -1, 11674, 1);
		addHoveredButton(11674, "CLICK", 1, 200, 30, 11675);
		// addText(11204, "Yak's/Rock Crabs", 0, 16750623, false, true);
		// addText(11208, "Taverly Dungeon", 0, 16750623, false, true);
		// addText(11212, "Slayer Tower", 0, 16750623, false, true);
		// addText(11216, "Brimhaven Dungeon", 0, 16750623, false, true);
		// addText(11220, "Hill Giants", 0, 16750623, false, true);
		// //addText(11220, "Hill Giants", false, true, -1, 2);
		// addText(11224, "Dark Beasts", 0, 16750623, false, true);
		// addText(11228, "Strykeworms", 0, 16750623, false, true);
		localRSInterface.totalChildren(24);
		localRSInterface.child(0, 11651, 0, 0);
		localRSInterface.child(1, 11652, 12, 40);
		localRSInterface.child(2, 11653, 11, 40);
		localRSInterface.child(3, 11655, 12, 65);
		localRSInterface.child(4, 11656, 11, 65);
		localRSInterface.child(5, 11658, 12, 90);
		localRSInterface.child(6, 11659, 11, 90);
		localRSInterface.child(7, 11661, 12, 115);
		localRSInterface.child(8, 11662, 11, 115);
		localRSInterface.child(9, 11664, 12, 143);
		localRSInterface.child(10, 11665, 11, 143);
		localRSInterface.child(11, 11667, 12, 168);
		localRSInterface.child(12, 11668, 11, 168);
		localRSInterface.child(13, 11670, 12, 193);
		localRSInterface.child(14, 11671, 11, 193);
		localRSInterface.child(15, 11673, 38, 236);
		localRSInterface.child(16, 11674, 38, 236);
		localRSInterface.child(17, 11204, 38, 45);
		localRSInterface.child(18, 11208, 38, 70);
		localRSInterface.child(19, 11212, 38, 95);
		localRSInterface.child(20, 11216, 38, 120);
		localRSInterface.child(21, 11220, 38, 147);
		localRSInterface.child(22, 11224, 38, 174);
		localRSInterface.child(23, 11228, 38, 201);
		localRSInterface = addTabInterface(14000);
		localRSInterface.width = 474;
		localRSInterface.height = 213;
		localRSInterface.scrollMax = 305;
		for (int i = 14001; i <= 14030; ++i) {
			// addText(i, "", 1, 16777215, false, true);
		}
		localRSInterface.totalChildren(30);
		int i = 0;
		int j = 5;
		for (int k = 14001; k <= 14030; ++k) {
			localRSInterface.child(i, k, 248, j);
			++i;
			j += 13;
		}
	}

	public static void pouchCreation() {
		int totalScrolls = pouchItems.length;
		int xPadding = 53;
		int yPadding = 57;
		int xPos = 13;
		int yPos = 20;
		RSInterface rsinterface = addTabInterface(23471);
		setChildren(7, rsinterface);
		addSprite(23472, 1, "Interfaces/summoning/creation/summoning");
		addButton(23475, 0, "Interfaces/summoning/creation/tab", "Transform Scolls");
		addSprite(23474, 1, "Interfaces/summoning/creation/pouch");
		addSprite(23473, 1, "Interfaces/summoning/creation/tab");
		addSprite(23476, 0, "Interfaces/summoning/creation/scroll");
		addInAreaHover(23477, "Interfaces/summoning/creation/close", 0, 1, 16, 16, "Close", 250, 3);
		RSInterface scroll = addTabInterface(23478);
		setChildren(3 * totalScrolls, scroll);
		for (int i = 0; i < totalScrolls; i++) {
			addInAreaHover(23479 + i * 8, "Interfaces/summoning/creation/box", 0, 1, 48, 52, "nothing", -1, 0);
			int req[] = { 1, 2, 3 };
			addPouch(23480 + i * 8, req, 1, pouchItems[i], summoningLevelRequirements[i], pouchNames[i], i, 5);
			addSprite(23485 + i * 8, pouchItems[i], null, 50, 50);
			setBounds(23479 + i * 8, 36 + (i % 8) * xPadding, 0 + (i / 8) * yPadding, 0 + i * 2, scroll);
			setBounds(23480 + i * 8, 43 + (i % 8) * xPadding, 2 + (i / 8) * yPadding, 1 + i * 2, scroll);
		}

		for (int i = 0; i < totalScrolls; i++) {
			int drawX = 5 + (i % 8) * xPadding;
			if (drawX > 292)
				drawX -= 90;
			int drawY = 55 + (i / 8) * yPadding;
			if (drawY > 160)
				drawY -= 80;
			setBounds(23481 + i * 8, drawX, drawY, 2 + (totalScrolls - 1) * 2 + i, scroll);
		}

		scroll.parentID = 23478;
		scroll.id = 23478;
		scroll.atActionType = 0;
		scroll.contentType = 0;
		scroll.width = 474;
		scroll.height = 257;
		scroll.scrollMax = 570;
		setBounds(23472, xPos, yPos, 0, rsinterface);
		setBounds(23473, xPos + 9, yPos + 9, 1, rsinterface);
		setBounds(23474, xPos + 29, yPos + 10, 2, rsinterface);
		setBounds(23475, xPos + 79, yPos + 9, 3, rsinterface);
		setBounds(23476, xPos + 106, yPos + 10, 4, rsinterface);
		setBounds(23477, xPos + 461, yPos + 10, 5, rsinterface);
		setBounds(23478, 0, yPos + 39, 6, rsinterface);
	}

	public static void scrollCreation() {
		int totalScrolls = pouchItems.length;
		int xPadding = 53;
		int yPadding = 57;
		int xPos = 13;
		int yPos = 20;
		RSInterface rsinterface = addTabInterface(22760);
		setChildren(7, rsinterface);
		addSprite(22761, 0, "Interfaces/summoning/creation/summoning");
		addButton(22762, 0, "Interfaces/summoning/creation/tab", "Infuse Pouches");
		addSprite(22763, 0, "Interfaces/summoning/creation/pouch");
		addSprite(22764, 1, "Interfaces/summoning/creation/tab");
		addSprite(22765, 1, "Interfaces/summoning/creation/scroll");
		addInAreaHover(22766, "Interfaces/summoning/creation/close", 0, 1, 16, 16, "Close", 250, 3);
		RSInterface scroll = addTabInterface(22767);
		setChildren(4 * totalScrolls, scroll);
		for (int i = 0; i < totalScrolls; i++) {
			addInAreaHover(22768 + i * 9, "Interfaces/summoning/creation/box", 0, 1, 48, 52, "nothing", -1, 0);
			addScroll(22769 + i * 9, pouchItems[i], 1, scrollItems[i], summoningLevelRequirements[i], scrollNames[i],
					i, 5);
			addSprite(22776 + i * 9, pouchItems[i], null, 50, 50);
			setBounds(22768 + i * 9, 36 + (i % 8) * xPadding, 0 + (i / 8) * yPadding, 0 + i * 3, scroll);
			setBounds(22769 + i * 9, 43 + (i % 8) * xPadding, 2 + (i / 8) * yPadding, 1 + i * 3, scroll);
			setBounds(22776 + i * 9, 28 + (i % 8) * xPadding, 28 + (i / 8) * yPadding, 2 + i * 3, scroll);
		}

		for (int i = 0; i < totalScrolls; i++) {
			int drawX = 5 + (i % 8) * xPadding;
			if (drawX > 292)
				drawX -= 90;
			int drawY = 55 + (i / 8) * yPadding;
			if (drawY > 160)
				drawY -= 80;
			setBounds(22770 + i * 9, drawX, drawY, 3 + (totalScrolls - 1) * 3 + i, scroll);
		}

		scroll.parentID = 22767;
		scroll.id = 22767;
		scroll.atActionType = 0;
		scroll.contentType = 0;
		scroll.width = 474;
		scroll.height = 257;
		scroll.scrollMax = 570;
		setBounds(22761, xPos, yPos, 0, rsinterface);
		setBounds(22762, xPos + 9, yPos + 9, 1, rsinterface);
		setBounds(22763, xPos + 29, yPos + 10, 2, rsinterface);
		setBounds(22764, xPos + 79, yPos + 9, 3, rsinterface);
		setBounds(22765, xPos + 106, yPos + 10, 4, rsinterface);
		setBounds(22766, xPos + 461, yPos + 10, 5, rsinterface);
		setBounds(22767, 0, yPos + 39, 6, rsinterface);
	}

	public static void addScroll(int ID, int r1, int ra1, int r2, int lvl, String name, int imageID, int type) {
		RSInterface rsInterface = addTabInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 5;
		rsInterface.atActionType = type;
		rsInterface.contentType = 0;
		rsInterface.mouseOverPopupInterface = ID + 1;
		rsInterface.width = 32;
		rsInterface.height = 32;
		rsInterface.tooltip = (new StringBuilder()).append("Transform @or1@").append(name).toString();
		rsInterface.spellName = name;
		rsInterface.valueCompareType = new int[2];
		rsInterface.requiredValues = new int[2];
		rsInterface.valueCompareType[0] = 3;
		rsInterface.requiredValues[0] = ra1;
		rsInterface.valueCompareType[1] = 3;
		rsInterface.requiredValues[1] = lvl - 1;
		rsInterface.valueIndexArray = new int[3][];
		rsInterface.valueIndexArray[0] = new int[4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = r1;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.valueIndexArray[1] = new int[3];
		rsInterface.valueIndexArray[1][0] = 1;
		rsInterface.valueIndexArray[1][1] = 6;
		rsInterface.valueIndexArray[1][2] = 0;
		rsInterface.itemSpriteId1 = r2;
		rsInterface.itemSpriteId2 = r2;
		rsInterface.itemSpriteIndex = imageID;
		rsInterface.greyScale = true;
		RSInterface hover = addTabInterface(ID + 1);
		hover.mouseOverPopupInterface = -1;
		hover.isMouseoverTriggered = true;
		setChildren(5, hover);
		addSprite(ID + 2, 0, "Interfaces/Lunar/BOX");
		// addText(ID + 3, (new StringBuilder()).append("Level ").append(lvl)
		// addText(ID + 4, "This item requires", 0xaf6a1a, true, true, 52, 0);
		addRuneText(ID + 5, ra1, r1);
		addSprite(ID + 6, r1, null);
		setBounds(ID + 2, 0, 0, 0, hover);
		setBounds(ID + 3, 90, 4, 1, hover);
		setBounds(ID + 4, 90, 19, 2, hover);
		setBounds(ID + 5, 87, 66, 3, hover);
		setBounds(ID + 6, 72, 33, 4, hover);
	}

	public static void addPouch(int ID, int r1[], int ra1, int r2, int lvl, String name, int imageID, int type) {
		RSInterface rsInterface = addTabInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 5;
		rsInterface.atActionType = type;
		rsInterface.contentType = 0;
		rsInterface.mouseOverPopupInterface = ID + 1;
		rsInterface.width = 32;
		rsInterface.height = 32;
		rsInterface.tooltip = (new StringBuilder()).append("Infuse @or1@").append(name).toString();
		rsInterface.spellName = name;
		rsInterface.valueCompareType = new int[2];
		rsInterface.requiredValues = new int[2];
		rsInterface.valueCompareType[0] = 3;
		rsInterface.requiredValues[0] = ra1;
		rsInterface.valueCompareType[1] = 3;
		rsInterface.requiredValues[1] = lvl - 1;
		rsInterface.valueIndexArray = new int[2 + r1.length][];
		for (int i = 0; i < r1.length; i++) {
			rsInterface.valueIndexArray[i] = new int[4];
			rsInterface.valueIndexArray[i][0] = 4;
			rsInterface.valueIndexArray[i][1] = 3214;
			rsInterface.valueIndexArray[i][2] = r1[i];
			rsInterface.valueIndexArray[i][3] = 0;
		}

		rsInterface.valueIndexArray[1] = new int[3];
		rsInterface.valueIndexArray[1][0] = 1;
		rsInterface.valueIndexArray[1][1] = 6;
		rsInterface.valueIndexArray[1][2] = 0;
		rsInterface.itemSpriteId1 = r2;
		rsInterface.itemSpriteId2 = r2;
		rsInterface.itemSpriteIndex = imageID;
		rsInterface.greyScale = true;
		RSInterface hover = addTabInterface(ID + 1);
		hover.mouseOverPopupInterface = -1;
		hover.isMouseoverTriggered = true;
		setChildren(5, hover);
		addSprite(ID + 2, 0, "Interfaces/Lunar/BOX");
		// addText(ID + 3, (new StringBuilder()).append("Level ").append(lvl)
		// addText(ID + 4, "This item requires", 0xaf6a1a, true, true, 52, 0);
		addRuneText(ID + 5, ra1, r1[0]);
		addSprite(ID + 6, r1[0], null);
		addSprite(ID + 7, r1[1], null);
		addSprite(ID + 8, r1[2], null);
		setBounds(ID + 2, 0, 0, 0, hover);
		setBounds(ID + 3, 90, 4, 1, hover);
		setBounds(ID + 4, 90, 19, 2, hover);
		setBounds(ID + 5, 87, 66, 3, hover);
		setBounds(ID + 6, 14, 33, 4, hover);
	}

	public static void skillInterface() {
		RSInterface rsinterface = addTabInterface(3917);
		skillInterface(19746, 255);
		skillInterface(19749, 52);
		// addText(29801, "", 0, 0xFFEE33); // Summoning
		// addText(29800, "", 0, 0xFFEE33); // Hunter

		addButton(19747, 51, 27700, "Skill/Skill", 62, 32, "View @lre@Hunter @whi@Guide", 1);
		addButton(19748, 50, 27701, "Skill/Skill", 62, 32, "@lre@Dismiss Summoning Familiar@whi@", 1);

		// addText(13984, "Total", 0, 0xFFEE33);
		// addText(3985, "", 0, 0xFFEE33);
		// addText(13983, "", 0, 0xFFEE33, true, true);
		for (int k = 0; k < boxIds.length; k++) {
			skillInterface(boxIds[k], 256);
		}
		// RSInterface rsinterface = addTabInterface(3917);
		rsinterface.children = new int[63];
		rsinterface.childX = new int[63];
		rsinterface.childY = new int[63];
		rsinterface.children[0] = 3918;
		rsinterface.childX[0] = 0;
		rsinterface.childY[0] = 0;
		rsinterface.children[1] = 3925;
		rsinterface.childX[1] = 0;
		rsinterface.childY[1] = 31;
		rsinterface.children[2] = 3932;
		rsinterface.childX[2] = 0;
		rsinterface.childY[2] = 62;
		rsinterface.children[3] = 3939;
		rsinterface.childX[3] = 0;
		rsinterface.childY[3] = 93;
		rsinterface.children[4] = 3946;
		rsinterface.childX[4] = 0;
		rsinterface.childY[4] = 124;
		rsinterface.children[5] = 3953;
		rsinterface.childX[5] = 0;
		rsinterface.childY[5] = 155;
		rsinterface.children[6] = 4148;
		rsinterface.childX[6] = 0;
		rsinterface.childY[6] = 186;
		rsinterface.children[7] = 19746;
		rsinterface.childX[7] = 70;
		rsinterface.childY[7] = 69;
		rsinterface.children[8] = 19748;
		rsinterface.childX[8] = 1;
		rsinterface.childY[8] = 219;
		rsinterface.children[9] = 19747;
		rsinterface.childX[9] = 64;
		rsinterface.childY[9] = 219;
		rsinterface.children[10] = 14000;
		rsinterface.childX[10] = 10;
		rsinterface.childY[10] = 219;
		rsinterface.children[11] = 19749;
		rsinterface.childX[11] = 128;
		rsinterface.childY[11] = 220;
		rsinterface.children[12] = 13983;
		rsinterface.childX[12] = 158;
		rsinterface.childY[12] = 238;
		rsinterface.children[13] = 3984;
		rsinterface.childX[13] = 300;
		rsinterface.childY[13] = 225;
		rsinterface.children[14] = 3985;
		rsinterface.childX[14] = 130;
		rsinterface.childY[14] = 238;
		rsinterface.children[15] = 29800;
		rsinterface.childX[15] = 98;
		rsinterface.childY[15] = 220;
		rsinterface.children[16] = 29800;
		rsinterface.childX[16] = 107;
		rsinterface.childY[16] = 235;
		rsinterface.children[17] = 29801;
		rsinterface.childX[17] = 36;
		rsinterface.childY[17] = 220;
		rsinterface.children[18] = 29801;
		rsinterface.childX[18] = 45;
		rsinterface.childY[18] = 235;
		rsinterface.children[19] = 4040;
		rsinterface.childX[19] = 5;
		rsinterface.childY[19] = 20;
		rsinterface.children[20] = 8654;
		rsinterface.childX[20] = 0;
		rsinterface.childY[20] = 2;
		rsinterface.children[21] = 8655;
		rsinterface.childX[21] = 64;
		rsinterface.childY[21] = 2;
		rsinterface.children[22] = 4076;
		rsinterface.childX[22] = 20;
		rsinterface.childY[22] = 20;
		rsinterface.children[23] = 8656;
		rsinterface.childX[23] = 128;
		rsinterface.childY[23] = 2;
		rsinterface.children[24] = 4112;
		rsinterface.childX[24] = 20;
		rsinterface.childY[24] = 20;
		rsinterface.children[25] = 8657;
		rsinterface.childX[25] = 0;
		rsinterface.childY[25] = 33;
		rsinterface.children[26] = 4046;
		rsinterface.childX[26] = 20;
		rsinterface.childY[26] = 50;
		rsinterface.children[27] = 8658;
		rsinterface.childX[27] = 64;
		rsinterface.childY[27] = 33;
		rsinterface.children[28] = 4082;
		rsinterface.childX[28] = 20;
		rsinterface.childY[28] = 50;
		rsinterface.children[29] = 8659;
		rsinterface.childX[29] = 128;
		rsinterface.childY[29] = 33;
		rsinterface.children[30] = 4118;
		rsinterface.childX[30] = 20;
		rsinterface.childY[30] = 50;
		rsinterface.children[31] = 8660;
		rsinterface.childX[31] = 0;
		rsinterface.childY[31] = 60 + 10;
		rsinterface.children[32] = 4052;
		rsinterface.childX[32] = 20;
		rsinterface.childY[32] = 83;
		rsinterface.children[33] = 8661;
		rsinterface.childX[33] = 65;
		rsinterface.childY[33] = 60 + 10;
		rsinterface.children[34] = 4088;
		rsinterface.childX[34] = 20;
		rsinterface.childY[34] = 83;
		rsinterface.children[35] = 8662;
		rsinterface.childX[35] = 130;
		rsinterface.childY[35] = 60 + 10;
		rsinterface.children[36] = 4124;
		rsinterface.childX[36] = 20;
		rsinterface.childY[36] = 83;
		rsinterface.children[37] = 8663;
		rsinterface.childX[37] = 0;
		rsinterface.childY[37] = 90 + 10;
		rsinterface.children[38] = 4058;
		rsinterface.childX[38] = 20;
		rsinterface.childY[38] = 120;
		rsinterface.children[39] = 8664;
		rsinterface.childX[39] = 65;
		rsinterface.childY[39] = 90 + 10;
		rsinterface.children[40] = 4094;
		rsinterface.childX[40] = 20;
		rsinterface.childY[40] = 120;
		rsinterface.children[41] = 8665;
		rsinterface.childX[41] = 130;
		rsinterface.childY[41] = 90 + 10;
		rsinterface.children[42] = 4130;
		rsinterface.childX[42] = 20;
		rsinterface.childY[42] = 120;
		rsinterface.children[43] = 8666;
		rsinterface.childX[43] = 0;
		rsinterface.childY[43] = 130;
		rsinterface.children[44] = 4064;
		rsinterface.childX[44] = 20;
		rsinterface.childY[44] = 150;
		rsinterface.children[45] = 8667;
		rsinterface.childX[45] = 65;
		rsinterface.childY[45] = 130;
		rsinterface.children[46] = 4100;
		rsinterface.childX[46] = 20;
		rsinterface.childY[46] = 150;
		rsinterface.children[47] = 8668;
		rsinterface.childX[47] = 130;
		rsinterface.childY[47] = 130;
		rsinterface.children[48] = 4136;
		rsinterface.childX[48] = 20;
		rsinterface.childY[48] = 150;
		rsinterface.children[49] = 8669;
		rsinterface.childX[49] = 0;
		rsinterface.childY[49] = 160;
		rsinterface.children[50] = 4070;
		rsinterface.childX[50] = 20;
		rsinterface.childY[50] = 180;
		rsinterface.children[51] = 8670;
		rsinterface.childX[51] = 65;
		rsinterface.childY[51] = 160;
		rsinterface.children[52] = 4106;
		rsinterface.childX[52] = 20;
		rsinterface.childY[52] = 180;
		rsinterface.children[53] = 8671;
		rsinterface.childX[53] = 130;
		rsinterface.childY[53] = 160;
		rsinterface.children[54] = 4142;
		rsinterface.childX[54] = 20;
		rsinterface.childY[54] = 180;
		rsinterface.children[55] = 8672;
		rsinterface.childX[55] = 0;
		rsinterface.childY[55] = 190;
		rsinterface.children[56] = 4160;
		rsinterface.childX[56] = 20;
		rsinterface.childY[56] = 150;
		rsinterface.children[57] = 4160;
		rsinterface.childX[57] = 20;
		rsinterface.childY[57] = 150;
		rsinterface.children[58] = 12162;
		rsinterface.childX[58] = 65;
		rsinterface.childY[58] = 190;
		rsinterface.children[59] = 2832;
		rsinterface.childX[59] = 20;
		rsinterface.childY[59] = 150;
		rsinterface.children[60] = 13928;
		rsinterface.childX[60] = 130;
		rsinterface.childY[60] = 190;
		rsinterface.children[61] = 13917;
		rsinterface.childX[61] = 20;
		rsinterface.childY[61] = 150;
		rsinterface.children[62] = 13984;
		rsinterface.childX[62] = 145;
		rsinterface.childY[62] = 225;
	}

	public static void skillInterface(int i, int j) {
		RSInterface rsinterface = interfaceCache[i] = new RSInterface();
		rsinterface.id = i;
		rsinterface.parentID = i;
		rsinterface.type = 5;
		rsinterface.atActionType = 0;
		rsinterface.contentType = 0;
		rsinterface.width = 26;
		rsinterface.height = 34;
		rsinterface.opacity = 0;
		rsinterface.mouseOverPopupInterface = 0;
		// class9.disabledSprite = imageLoader(j, "Skill/Skill");
		// rsinterface.enabledSprite = imageLoader(j, "Skill/Skill");
	}

	public static void addButton(int i, int j, int hoverId, String name, int W, int H, String S, int AT) {
		RSInterface RSInterface = addInterface(i);
		RSInterface.id = i;
		RSInterface.parentID = i;
		RSInterface.type = 5;
		RSInterface.atActionType = AT;
		RSInterface.opacity = 0;
		RSInterface.mouseOverPopupInterface = hoverId;
		// class9.disabledSprite = imageLoader(j, name);
		// rsinterface.enabledSprite = imageLoader(j, name);
		RSInterface.width = W;
		RSInterface.height = H;
		RSInterface.tooltip = S;
	}

	public static void addSprite(int i, int j, int k) {
		RSInterface rsinterface = interfaceCache[i] = new RSInterface();
		rsinterface.id = i;
		rsinterface.parentID = i;
		rsinterface.type = 5;
		rsinterface.atActionType = 1;
		rsinterface.contentType = 0;
		rsinterface.width = 20;
		rsinterface.height = 20;
		rsinterface.opacity = 0;
		rsinterface.mouseOverPopupInterface = 52;
	}

	public static void itemsOnDeath() {
		RSInterface rsinterface = addInterface(17100);
		addSprite(17101, 2, 2);
		addHover(17102, 3, 0, 10601, 1, "Interfaces/Equipment/SPRITE", 17, 17, "Close Window");
		addHovered(10601, 3, "Interfaces/Equipment/SPRITE", 17, 17, 10602);
		// addText(17103, "Items Kept On Death", 2, 0xff981f);
		// addText(17106, "Information", 1, 0xff981f);
		// addText(17107, "Max items kept on death:", 1, 0xffcc33);
		// addText(17108, "~ 3 ~", 1, 0xffcc33);
		rsinterface.scrollMax = 0;
		rsinterface.isMouseoverTriggered = false;
		rsinterface.children = new int[12];
		rsinterface.childX = new int[12];
		rsinterface.childY = new int[12];

		rsinterface.children[0] = 17101;
		rsinterface.childX[0] = 7;
		rsinterface.childY[0] = 8;
		rsinterface.children[1] = 17102;
		rsinterface.childX[1] = 480;
		rsinterface.childY[1] = 17;
		rsinterface.children[2] = 17103;
		rsinterface.childX[2] = 185;
		rsinterface.childY[2] = 18;
		rsinterface.children[3] = 17104;
		rsinterface.childX[3] = 22;
		rsinterface.childY[3] = 50;
		rsinterface.children[4] = 17105;
		rsinterface.childX[4] = 22;
		rsinterface.childY[4] = 110;
		rsinterface.children[5] = 17106;
		rsinterface.childX[5] = 347;
		rsinterface.childY[5] = 47;
		rsinterface.children[6] = 17107;
		rsinterface.childX[6] = 349;
		rsinterface.childY[6] = 270;
		rsinterface.children[7] = 17108;
		rsinterface.childX[7] = 398;
		rsinterface.childY[7] = 298;
		rsinterface.children[8] = 17115;
		rsinterface.childX[8] = 348;
		rsinterface.childY[8] = 64;
		rsinterface.children[9] = 10494;
		rsinterface.childX[9] = 26;
		rsinterface.childY[9] = 74;
		rsinterface.children[10] = 10600;
		rsinterface.childX[10] = 26;
		rsinterface.childY[10] = 133;
		rsinterface.children[11] = 10601;
		rsinterface.childX[11] = 480;
		rsinterface.childY[11] = 17;
	}

	public static void itemsOnDeathDATA() {
		RSInterface rsinterface = addInterface(17115);
		// addText(17109, "", 0, 0xff981f);
		// addText(17110, "The normal amount of", 0, 0xff981f);
		// addText(17111, "items kept is three.", 0, 0xff981f);
		// addText(17112, "", 0, 0xff981f);
		// addText(17113, "If you are skulled,", 0, 0xff981f);
		// addText(17114, "you will lose all your", 0, 0xff981f);
		// addText(17117, "items, unless an item", 0, 0xff981f);
		// addText(17118, "protecting prayer is", 0, 0xff981f);
		// addText(17119, "used.", 0, 0xff981f);
		// addText(17120, "", 0, 0xff981f);
		// addText(17121, "Item protecting prayers", 0, 0xff981f);
		// addText(17122, "will allow you to keep", 0, 0xff981f);
		// addText(17123, "one extra item.", 0, 0xff981f);
		// addText(17124, "", 0, 0xff981f);
		// addText(17125, "The items kept are", 0, 0xff981f);
		// addText(17126, "selected by the server", 0, 0xff981f);
		// addText(17127, "and include the most", 0, 0xff981f);
		// addText(17128, "expensive items you're", 0, 0xff981f);
		// addText(17129, "carrying.", 0, 0xff981f);
		// addText(17130, "", 0, 0xff981f);
		rsinterface.parentID = 17115;
		rsinterface.id = 17115;
		rsinterface.type = 0;
		rsinterface.atActionType = 0;
		rsinterface.contentType = 0;
		rsinterface.width = 130;
		rsinterface.height = 197;
		rsinterface.opacity = 0;
		rsinterface.mouseOverPopupInterface = -1;
		rsinterface.scrollMax = 280;
		rsinterface.children = new int[20];
		rsinterface.childX = new int[20];
		rsinterface.childY = new int[20];
		rsinterface.children[0] = 17109;
		rsinterface.childX[0] = 0;
		rsinterface.childY[0] = 0;
		rsinterface.children[1] = 17110;
		rsinterface.childX[1] = 0;
		rsinterface.childY[1] = 12;
		rsinterface.children[2] = 17111;
		rsinterface.childX[2] = 0;
		rsinterface.childY[2] = 24;
		rsinterface.children[3] = 17112;
		rsinterface.childX[3] = 0;
		rsinterface.childY[3] = 36;
		rsinterface.children[4] = 17113;
		rsinterface.childX[4] = 0;
		rsinterface.childY[4] = 48;
		rsinterface.children[5] = 17114;
		rsinterface.childX[5] = 0;
		rsinterface.childY[5] = 60;
		rsinterface.children[6] = 17117;
		rsinterface.childX[6] = 0;
		rsinterface.childY[6] = 72;
		rsinterface.children[7] = 17118;
		rsinterface.childX[7] = 0;
		rsinterface.childY[7] = 84;
		rsinterface.children[8] = 17119;
		rsinterface.childX[8] = 0;
		rsinterface.childY[8] = 96;
		rsinterface.children[9] = 17120;
		rsinterface.childX[9] = 0;
		rsinterface.childY[9] = 108;
		rsinterface.children[10] = 17121;
		rsinterface.childX[10] = 0;
		rsinterface.childY[10] = 120;
		rsinterface.children[11] = 17122;
		rsinterface.childX[11] = 0;
		rsinterface.childY[11] = 132;
		rsinterface.children[12] = 17123;
		rsinterface.childX[12] = 0;
		rsinterface.childY[12] = 144;
		rsinterface.children[13] = 17124;
		rsinterface.childX[13] = 0;
		rsinterface.childY[13] = 156;
		rsinterface.children[14] = 17125;
		rsinterface.childX[14] = 0;
		rsinterface.childY[14] = 168;
		rsinterface.children[15] = 17126;
		rsinterface.childX[15] = 0;
		rsinterface.childY[15] = 180;
		rsinterface.children[16] = 17127;
		rsinterface.childX[16] = 0;
		rsinterface.childY[16] = 192;
		rsinterface.children[17] = 17128;
		rsinterface.childX[17] = 0;
		rsinterface.childY[17] = 204;
		rsinterface.children[18] = 17129;
		rsinterface.childX[18] = 0;
		rsinterface.childY[18] = 216;
		rsinterface.children[19] = 17130;
		rsinterface.childX[19] = 0;
		rsinterface.childY[19] = 228;
	}

	public void swapInventoryItems(int i, int j) {
		int k = inv[i];
		inv[i] = inv[j];
		inv[j] = k;
		k = invStackSizes[i];
		invStackSizes[i] = invStackSizes[j];
		invStackSizes[j] = k;
	}

	public static void Construction() {
		RSInterface Interface = addInterface(31250);
		setChildren(53, Interface);
		addHoverButton(29561, "Interfaces/Construction/BUTTON", 0, 16, 16, "Close", 0, 29562, 1);// CLOSE
		addHoveredButton(29562, "Interfaces/Construction/BUTTON", 1, 16, 16, 29563);// CLOSE
																					// HOVER
		addSprite(31249, 0, "Interfaces/Construction/CONSTRUCTION");// BACKGROUND

		addButton(31251, 0, "Interfaces/Construction/CONS", "Build @or1@Fern");
		addTooltip(31252, "Fern (lvl 1):\n1x Guam, 1x Logs");

		addButton(31254, 1, "Interfaces/Construction/CONS", "Build @or1@Tree");
		addTooltip(31255, "Tree (lvl 5):\n3x Logs");

		addButton(31257, 2, "Interfaces/Construction/CONS", "Build @or1@Chair");
		addTooltip(31258, "Chair (lvl 19):\n10x Nails, 2x Oak plank");

		addButton(31260, 3, "Interfaces/Construction/CONS", "Build @or1@Bookcase");
		addTooltip(31261, "Bookcase (lvl 29):\n15x Nails, 3x Oak plank");

		addButton(31263, 4, "Interfaces/Construction/CONS", "Build @or1@Greenman's ale");
		addTooltip(31264, "Greenamn's ale (lvl 26):\n15x Nails, 2x Oak plank");

		addButton(31266, 5, "Interfaces/Construction/CONS", "Build @or1@Small oven");
		addTooltip(31267, "Small oven (lvl 24):\n2x Iron bar");

		addButton(31269, 6, "Interfaces/Construction/CONS", "Build @or1@Carved oak bench");
		addTooltip(31270, "Carved oak bench (lvl 31):\n15x Nails, 3x Oak plank");

		addButton(31272, 7, "Interfaces/Construction/CONS", "Build @or1@Painting stand");
		addTooltip(31273, "Painting stand (lvl 41):\n20x Nails, 2x Oak plank");

		addButton(31275, 8, "Interfaces/Construction/CONS", "Build @or1@Bed");
		addTooltip(31276, "Bed (lvl 40):\n20x Nails, 3x Oak plank");

		addButton(31278, 9, "Interfaces/Construction/CONS", "Build @or1@Teak drawers");
		addTooltip(31279, "Teak drawers (lvl 51):\n20x Nails, 2x Teak plank");

		addButton(31281, 10, "Interfaces/Construction/CONS", "Build @or1@Mithril armour");
		addTooltip(31282, "Mithril armour (lvl 28):\n1x Mithril full helm, platebody, platelegs");

		addButton(31284, 11, "Interfaces/Construction/CONS", "Build @or1@Adamant armour");
		addTooltip(31285, "Adamant armour (lvl 28):\n1x Adamant full helm, platebody, platelegs");

		addButton(31287, 12, "Interfaces/Construction/CONS", "Build @or1@Rune armour");
		addTooltip(31288, "Rune armour (lvl 28):\n1x Rune full helm, platebody, platelegs");

		addButton(31290, 13, "Interfaces/Construction/CONS", "Build @or1@Rune display case");
		addTooltip(31291, "Rune display case (lvl 41):\n100x Law rune, 100x Nature rune, 1x Teak plank");

		addButton(31293, 14, "Interfaces/Construction/CONS", "Build @or1@Archery target");
		addTooltip(31294, "Archery target (lvl 81):\n25x Nails, 3x Teak plank");

		addButton(31296, 15, "Interfaces/Construction/CONS", "Build @or1@Combat stone");
		addTooltip(31297, "Combat stone (lvl 59):\n4x Iron bar");

		addButton(31299, 16, "Interfaces/Construction/CONS", "Build @or1@Elemental balance");
		addTooltip(31300, "Elemental balance (lvl 77):\n4x Iron bar");

		addButton(31302, 17, "Interfaces/Construction/CONS", "Build @or1@Mahogany prize chest");
		addTooltip(31303, "Mahogany prize chest (lvl 54):\n20x Nails, 2x Mahogany plank");

		addButton(31305, 18, "Interfaces/Construction/CONS", "Build @or1@Lectern");
		addTooltip(31306, "Lectern (lvl 67):\n40x Nails, 2x Mahogany plank");

		addButton(31308, 19, "Interfaces/Construction/CONS", "Build @or1@Crystal of power");
		addTooltip(31309, "Crystal of power (lvl 66):\n15x Nails, 2x Mahogany plank, 1x Iron bar");

		addButton(31311, 20, "Interfaces/Construction/CONS", "Build @or1@Altar");
		addTooltip(31312, "Altar (lvl 64):\n15x Nails, 2x Mahogany plank, 1x Iron bar");

		addButton(31314, 21, "Interfaces/Construction/CONS", "Build @or1@Intense burners");
		addTooltip(31315, "Intense burners (lvl 61):\n10x Nails, 2x Mahogany plank, 1x Kwuarm");

		addButton(31317, 22, "Interfaces/Construction/CONS", "Build @or1@Hedge");
		addTooltip(31318, "Hedge (lvl 80):\n2x Logs, 2x Kwuarm");

		addButton(31320, 23, "Interfaces/Construction/CONS", "Build @or1@Rocnar");
		addTooltip(31321, "Rocnar (lvl 83):\n2x Adamant bar, 2x Kwuarm");

		addButton(31323, 24, "Interfaces/Construction/CONS", "Build @or1@Bank chest");
		addTooltip(31324, "Bank chest (lvl 92):\n40x Nails, 2x Mahogany plank, 1x Iron bar");

		setBounds(29561, 413, 9, 1, Interface);// CLOSE
		setBounds(29562, 413, 9, 2, Interface);// CLOSE HOVER
		setBounds(31249, 69, 3, 0, Interface);// BACKOGRUND X Y

		setBounds(31251, 109, 28, 3, Interface);// Build item
		setBounds(31252, 76, 285, 4, Interface);// Requirements

		setBounds(31254, 172, 28, 5, Interface);// Build item
		setBounds(31255, 76, 285, 6, Interface);// Requirements

		setBounds(31257, 236, 28, 7, Interface);// Build item
		setBounds(31258, 76, 285, 8, Interface);// Requirements

		setBounds(31260, 300, 28, 9, Interface);// Build item
		setBounds(31261, 76, 285, 10, Interface);// Requirements

		setBounds(31263, 364, 28, 11, Interface);// Build item
		setBounds(31264, 76, 285, 12, Interface);// Requirements

		setBounds(31266, 109, 76, 13, Interface);// Build item
		setBounds(31267, 76, 285, 14, Interface);// Requirements

		setBounds(31269, 172, 76, 15, Interface);// Build item
		setBounds(31270, 76, 285, 16, Interface);// Requirements

		setBounds(31272, 236, 76, 17, Interface);// Build item
		setBounds(31273, 76, 285, 18, Interface);// Requirements

		setBounds(31275, 300, 76, 19, Interface);// Build item
		setBounds(31276, 76, 285, 20, Interface);// Requirements

		setBounds(31278, 364, 76, 21, Interface);// Build item
		setBounds(31279, 76, 285, 22, Interface);// Requirements

		setBounds(31281, 109, 124, 23, Interface);// Build item
		setBounds(31282, 76, 285, 24, Interface);// Requirements

		setBounds(31284, 172, 124, 25, Interface);// Build item
		setBounds(31285, 76, 285, 26, Interface);// Requirements

		setBounds(31287, 236, 124, 27, Interface);// Build item
		setBounds(31288, 76, 285, 28, Interface);// Requirements

		setBounds(31290, 300, 124, 29, Interface);// Build item
		setBounds(31291, 76, 285, 30, Interface);// Requirements

		setBounds(31293, 364, 124, 31, Interface);// Build item
		setBounds(31294, 76, 285, 32, Interface);// Requirements

		setBounds(31296, 109, 172, 33, Interface);// Build item
		setBounds(31297, 76, 285, 34, Interface);// Requirements

		setBounds(31299, 172, 172, 35, Interface);// Build item
		setBounds(31300, 76, 285, 36, Interface);// Requirements

		setBounds(31302, 236, 172, 37, Interface);// Build item
		setBounds(31303, 76, 285, 38, Interface);// Requirements

		setBounds(31305, 300, 172, 39, Interface);// Build item
		setBounds(31306, 76, 285, 40, Interface);// Requirements

		setBounds(31308, 364, 172, 41, Interface);// Build item
		setBounds(31309, 76, 285, 42, Interface);// Requirements

		setBounds(31311, 109, 220, 43, Interface);// Build item
		setBounds(31312, 76, 285, 44, Interface);// Requirements

		setBounds(31314, 172, 220, 45, Interface);// Build item
		setBounds(31315, 76, 285, 46, Interface);// Requirements

		setBounds(31317, 236, 220, 47, Interface);// Build item
		setBounds(31318, 76, 285, 48, Interface);// Requirements

		setBounds(31320, 300, 220, 49, Interface);// Build item
		setBounds(31321, 76, 285, 50, Interface);// Requirements

		setBounds(31323, 364, 220, 51, Interface);// Build item
		setBounds(31324, 76, 285, 52, Interface);// Requirements

		Interface = addInterface(31330);
		addSprite(31329, 1, "Interfaces/Construction/CONSTRUCTION");// Back

		addHoverButton(31331, "Interfaces/Construction/BUTTON", 2, 90, 44, "Choose", 0, 31332, 1);
		addHoveredButton(31332, "Interfaces/Construction/BUTTON", 4, 90, 44, 31333);

		addHoverButton(31334, "Interfaces/Construction/BUTTON", 2, 90, 44, "Choose", 0, 31335, 1);
		addHoveredButton(31335, "Interfaces/Construction/BUTTON", 4, 90, 44, 31336);

		// addText(31337, "Public", 0xFFEE33, false, true, 52, 2);
		// addText(31338, "Private", 0xFFEE33, false, true, 52, 2);

		addHoverButton(29561, "Interfaces/Construction/BUTTON", 0, 16, 16, "Close", 0, 29562, 1);// CLOSE
		addHoveredButton(29562, "Interfaces/Construction/BUTTON", 1, 16, 16, 29563);// CLOSE
																					// HOVER

		setChildren(9, Interface);
		setBounds(31329, 169, 79, 0, Interface);// Back

		setBounds(31331, 195, 95, 1, Interface);// Button 1
		setBounds(31332, 195, 95, 2, Interface);// Button 1

		setBounds(31334, 195, 157, 3, Interface);// Button 2
		setBounds(31335, 195, 157, 4, Interface);// Button 2

		setBounds(31337, 210, 108, 5, Interface);// Text 1
		setBounds(31338, 210, 170, 6, Interface);// Text 2

		setBounds(29561, 289, 85, 7, Interface);// CLOSE
		setBounds(29562, 289, 85, 8, Interface);// CLOSE HOVER
	}

	public static void summoningLevelUp() {
		RSInterface Interface = addTabInterface(22602);
		setChildren(2, Interface);
		addSprite(22603, 0, "Interfaces/summoning/cons2/levelup");
		setBounds(6206, 0, 5, 0, Interface);
		setBounds(22603, 22, 5, 1, Interface);
	}

	public static void passwordChanger() {
		int xPos = 71;
		int yPos = 92;
		RSInterface Interface = addTabInterface(22595);
		setChildren(6, Interface);
		addSprite(22596, 0, "Interfaces/password/backing");
		addButton(22597, 0, "Interfaces/password/button", 53, 21, "Change", 1);
		addButton(22598, 1, "Interfaces/password/button", 53, 21, "Close", 3);
		// addText(22599, "Password Changer", 0xff981f, false, true, 52, 2);
		// addText(22600, "Choose your new password.", 0xffffff, false, true,
		// 52,
		// addText(22601, "Password Strength: None.", 0xf3ede1, false, true, 52,
		setBounds(22596, xPos, yPos, 0, Interface);
		setBounds(22597, xPos + 120, yPos + 114, 1, Interface);
		setBounds(22598, xPos + 215, yPos + 114, 2, Interface);
		setBounds(22599, xPos + 125, yPos + 7, 3, Interface);
		setBounds(22600, xPos + 153, yPos + 30, 4, Interface);
		setBounds(22601, xPos + 10, yPos + 96, 5, Interface);
	}

	public static void newTrade() {
		RSInterface Interface = addInterface(3323);
		setChildren(19, Interface);
		addSprite(3324, 6, "Interfaces/TradeTab/TRADE");
		addHover(3442, 3, 0, 3325, 1, "Interfaces/Bank/BANK", 17, 17, "Close Window");
		addHovered(3325, 2, "Interfaces/Bank/BANK", 17, 17, 3326);
		// addText(3417, "Trading With:", 0xFF981F, true, true, 52, 2);
		// addText(3418, "Trader's Offer", 0xFF981F, false, true, 52, 1);
		// addText(3419, "Your Offer", 0xFF981F, false, true, 52, 1);
		// addText(3421, "Accept", 0x00C000, true, true, 52, 1);
		// addText(3423, "Decline", 0xC00000, true, true, 52, 1);

		addHover(3420, 1, 0, 3327, 5, "Interfaces/TradeTab/TRADE", 65, 32, "Accept");
		addHovered(3327, 2, "Interfaces/TradeTab/TRADE", 65, 32, 3328);
		addHover(3422, 3, 0, 3329, 5, "Interfaces//TradeTab/TRADE", 65, 32, "Decline");
		addHovered(3329, 2, "Interfaces/TradeTab/TRADE", 65, 32, 3330);
		setBounds(3324, 0, 16, 0, Interface);
		setBounds(3442, 485, 24, 1, Interface);
		setBounds(3325, 485, 24, 2, Interface);
		setBounds(3417, 258, 25, 3, Interface);
		setBounds(3418, 355, 51, 4, Interface);
		setBounds(3419, 68, 51, 5, Interface);
		setBounds(3420, 223, 120, 6, Interface);
		setBounds(3327, 223, 120, 7, Interface);
		setBounds(3422, 223, 160, 8, Interface);
		setBounds(3329, 223, 160, 9, Interface);
		setBounds(3421, 256, 127, 10, Interface);
		setBounds(3423, 256, 167, 11, Interface);
		setBounds(3431, 256, 272, 12, Interface);
		setBounds(3415, 12, 64, 13, Interface);
		setBounds(3416, 321, 67, 14, Interface);
		setBounds(24505, 256, 67, 16, Interface);
		setBounds(24504, 255, 310, 15, Interface);
		setBounds(24506, 20, 310, 17, Interface);
		setBounds(24507, 380, 310, 18, Interface);
		Interface = addInterface(3443);
		setChildren(15, Interface);
		addSprite(3444, 3, "Interfaces/TradeTab/TRADE");
		AddInterfaceButton(3546, 2, "Interfaces/ShopTab/SHOP", 63, 24, "Accept", 1);
		AddInterfaceButton(3548, 2, "Interfaces/ShopTab/SHOP", 63, 24, "Decline", 3);
		// addText(3547, "Accept", 0x00C000, true, true, 52, 1);
		// addText(3549, "Decline", 0xC00000, true, true, 52, 1);
		// addText(3450, "Trading With:", 0x00FFFF, true, true, 52, 2);
		// addText(3451, "Yourself", 0x00FFFF, true, true, 52, 2);
		setBounds(3444, 12, 20, 0, Interface);
		setBounds(3442, 470, 32, 1, Interface);
		setBounds(3325, 470, 32, 2, Interface);
		setBounds(3535, 130, 28, 3, Interface);
		setBounds(3536, 105, 47, 4, Interface);
		setBounds(3546, 189, 295, 5, Interface);
		setBounds(3548, 258, 295, 6, Interface);
		setBounds(3547, 220, 299, 7, Interface);
		setBounds(3549, 288, 299, 8, Interface);
		setBounds(3557, 71, 87, 9, Interface);
		setBounds(3558, 315, 87, 10, Interface);
		setBounds(3533, 64, 70, 11, Interface);
		setBounds(3534, 297, 70, 12, Interface);
		setBounds(3450, 95, 289, 13, Interface);
		setBounds(3451, 95, 304, 14, Interface);
	}

	public static void Shop() {
		RSInterface rsinterface = addTabInterface(3824);
		setChildren(8, rsinterface);
		addSprite(3825, 0, "Interfaces/Shop/SHOP");
		addHover(3902, 3, 0, 3826, 1, "Interfaces/Shop/CLOSE", 17, 17, "Close Window");
		addHovered(3826, 2, "Interfaces/Shop/CLOSE", 17, 17, 3827);
		// addText(19679, "", 0xff981f, false, true, 52, 1);
		// addText(19680, "", 0xbf751d, false, true, 52, 1);
		addButton(19681, 2, "Interfaces/Shop/SHOP", 0, 0, "", 1);
		addSprite(19687, 1, "Interfaces/Shop/ITEMBG");
		setBounds(3825, 6, 8, 0, rsinterface);
		setBounds(3902, 478, 10, 1, rsinterface);
		setBounds(3826, 478, 10, 2, rsinterface);
		setBounds(3900, 26, 44, 3, rsinterface);
		setBounds(3901, 240, 11, 4, rsinterface);
		setBounds(19679, 42, 54, 5, rsinterface);
		setBounds(19680, 150, 54, 6, rsinterface);
		setBounds(19681, 129, 50, 7, rsinterface);
		rsinterface = interfaceCache[3900];
		setChildren(1, rsinterface);
		setBounds(19687, 6, 15, 0, rsinterface);
		rsinterface.invSpritePadX = 15;
		rsinterface.width = 10;
		rsinterface.height = 4;
		rsinterface.invSpritePadY = 25;
		rsinterface = addTabInterface(19682);
		addSprite(19683, 1, "Interfaces/Shop/SHOP");
		// addText(19684, "Main Stock", 0xbf751d, false, true, 52, 1);
		// addText(19685, "Store Info", 0xff981f, false, true, 52, 1);
		addButton(19686, 2, "Interfaces/Shop/SHOP", 95, 19, "Main Stock", 1);
		setChildren(7, rsinterface);
		setBounds(19683, 12, 12, 0, rsinterface);
		setBounds(3901, 240, 21, 1, rsinterface);
		setBounds(19684, 42, 54, 2, rsinterface);
		setBounds(19685, 150, 54, 3, rsinterface);
		setBounds(19686, 23, 50, 4, rsinterface);
		setBounds(3902, 471, 22, 5, rsinterface);
		setBounds(3826, 60, 85, 6, rsinterface);
	}

	public static void Bank() {
		RSInterface Interface = addTabInterface(5292);
		setChildren(20, Interface);
		addSprite(5293, 0, "Interfaces/Bank/BANK");
		setBounds(5293, 13, 13, 0, Interface);
		addHover(5384, 3, 0, 5380, 1, "Interfaces/Bank/BANK", 17, 17, "Close Window");
		addHovered(5380, 2, "Interfaces/Bank/BANK", 17, 17, 5379);
		setBounds(5384, 476, 16, 3, Interface);
		setBounds(5380, 476, 16, 4, Interface);
		addHover(5294, 4, 0, 5295, 4, "Interfaces/Bank/BANK", 114, 25, "");
		addHovered(5295, 4, "Interfaces/Bank/BANK", 114, 25, 5296);
		setBounds(5294, 444, 38, 5, Interface);
		setBounds(5295, 444, 38, 6, Interface);
		addBankHover(21000, 4, 21001, 7, 8, "Interfaces/Bank/BANK", 35, 25, 304, 1, "Swap Withdraw Mode", 21002, 7, 8,
				"Interfaces/Bank/BANK", 21003, "Switch to insert items \nmode", "Switch to swap items \nmode.", 12, 20);
		setBounds(21000, 25, 285, 7, Interface);
		setBounds(21001, 10, 225, 8, Interface);
		addBankHover(21004, 4, 21005, 13, 15, "Interfaces/Bank/BANK", 35, 25, 0, 1, "Search", 21006, 14, 16,
				"Interfaces/Bank/BANK", 21007, "Click here to search your \nbank", "Click here to search your \nbank",
				12, 20);
		setBounds(21004, 65, 285, 9, Interface);
		setBounds(21005, 50, 225, 10, Interface);
		addBankHover(21008, 4, 21009, 9, 11, "Interfaces/Bank/BANK", 35, 25, 115, 1, "Search", 21010, 10, 12,
				"Interfaces/Bank/BANK", 21011, "Switch to note withdrawal \nmode", "Switch to item withdrawal \nmode",
				12, 20);
		setBounds(21008, 240, 285, 11, Interface);
		setBounds(21009, 225, 225, 12, Interface);
		addBankHover1(21012, 5, 21013, 17, "Interfaces/Bank/BANK", 35, 25, "Deposit carried tems", 21014, 18,
				"Interfaces/Bank/BANK", 21015, "Empty your backpack into\nyour bank", 0, 20);
		setBounds(21012, 375, 285, 13, Interface);
		setBounds(21013, 360, 225, 14, Interface);
		addBankHover1(21016, 5, 21017, 19, "Interfaces/Bank/BANK", 35, 25, "Deposit worn items", 21018, 20,
				"Interfaces/Bank/BANK", 21019, "Empty the items your are\nwearing into your bank", 0, 20);
		setBounds(21016, 415, 285, 15, Interface);
		setBounds(21017, 400, 225, 16, Interface);
		addBankHover1(21020, 5, 21021, 21, "Interfaces/Bank/BANK", 35, 25, "Deposit beast of burden inventory.", 21022,
				22, "Interfaces/Bank/BANK", 21023, "Empty your BoB's inventory\ninto your bank", 0, 20);
		setBounds(21020, 455, 285, 17, Interface);
		setBounds(21021, 440, 225, 18, Interface);
		// addText(21022, "352", 0xF2A71B, false, true, 52, 0);
		setBounds(21022, 455, 42, 19, Interface);
		setBounds(5383, 170, 15, 1, Interface);
		setBounds(5385, -4, 74, 2, Interface);
		Interface = interfaceCache[5385];
		Interface.height = 206;
		Interface.width = 480;
		Interface = interfaceCache[5382];
		Interface.width = 10;
		Interface.invSpritePadX = 12;
		Interface.height = 35;
	}

	public static void addBankHover(int interfaceID, int actionType, int hoverid, int spriteId, int spriteId2,
			String NAME, int Width, int Height, int configFrame, int configId, String Tooltip, int hoverId2,
			int hoverSpriteId, int hoverSpriteId2, String hoverSpriteName, int hoverId3, String hoverDisabledText,
			String hoverEnabledText, int X, int Y) {
		RSInterface hover = addTabInterface(interfaceID);
		hover.id = interfaceID;
		hover.parentID = interfaceID;
		hover.type = 5;
		hover.atActionType = actionType;
		hover.contentType = 0;
		hover.opacity = 0;
		hover.mouseOverPopupInterface = hoverid;
		// Tab.enabledSprite = imageLoader(spriteId, NAME);
		hover.width = Width;
		hover.tooltip = Tooltip;
		hover.height = Height;
		hover.valueCompareType = new int[1];
		hover.requiredValues = new int[1];
		hover.valueCompareType[0] = 1;
		hover.requiredValues[0] = configId;
		hover.valueIndexArray = new int[1][3];
		hover.valueIndexArray[0][0] = 5;
		hover.valueIndexArray[0][1] = configFrame;
		hover.valueIndexArray[0][2] = 0;
		hover = addTabInterface(hoverid);
		hover.parentID = hoverid;
		hover.id = hoverid;
		hover.type = 0;
		hover.atActionType = 0;
		hover.width = 550;
		hover.height = 334;
		hover.isMouseoverTriggered = true;
		hover.mouseOverPopupInterface = -1;
		addSprite(hoverId2, hoverSpriteId, hoverSpriteId2, hoverSpriteName, configId, configFrame);
		addHoverBox(hoverId3, interfaceID, hoverDisabledText, hoverEnabledText, configId, configFrame);
		setChildren(2, hover);
		setBounds(hoverId2, 15, 60, 0, hover);
		setBounds(hoverId3, X, Y, 1, hover);
	}

	public static void addBankHover1(int interfaceID, int actionType, int hoverid, int spriteId, String NAME,
			int Width, int Height, String Tooltip, int hoverId2, int hoverSpriteId, String hoverSpriteName,
			int hoverId3, String hoverDisabledText, int X, int Y) {
		RSInterface hover = addTabInterface(interfaceID);
		hover.id = interfaceID;
		hover.parentID = interfaceID;
		hover.type = 5;
		hover.atActionType = actionType;
		hover.contentType = 0;
		hover.opacity = 0;
		hover.mouseOverPopupInterface = hoverid;
		// Tab.enabledSprite = imageLoader(spriteId, NAME);
		hover.width = Width;
		hover.tooltip = Tooltip;
		hover.height = Height;
		hover = addTabInterface(hoverid);
		hover.parentID = hoverid;
		hover.id = hoverid;
		hover.type = 0;
		hover.atActionType = 0;
		hover.width = 550;
		hover.height = 334;
		hover.isMouseoverTriggered = true;
		hover.mouseOverPopupInterface = -1;
		addSprite(hoverId2, hoverSpriteId, hoverSpriteId, hoverSpriteName, 0, 0);
		addHoverBox(hoverId3, interfaceID, hoverDisabledText, hoverDisabledText, 0, 0);
		setChildren(2, hover);
		setBounds(hoverId2, 15, 60, 0, hover);
		setBounds(hoverId3, X, Y, 1, hover);
	}

	public static void addHoverBox(int id, int ParentID, String text, String text2, int configId, int configFrame) {
		RSInterface rsi = addTabInterface(id);
		rsi.id = id;
		rsi.parentID = ParentID;
		rsi.type = 8;
		rsi.disabledText = text;
		rsi.message = text2;
		rsi.valueCompareType = new int[1];
		rsi.requiredValues = new int[1];
		rsi.valueCompareType[0] = 1;
		rsi.requiredValues[0] = configId;
		rsi.valueIndexArray = new int[1][3];
		rsi.valueIndexArray[0][0] = 5;
		rsi.valueIndexArray[0][1] = configFrame;
		rsi.valueIndexArray[0][2] = 0;
	}

	public static void addSprite(int ID, int i, int i2, String name, int configId, int configFrame) {
		RSInterface Tab = addTabInterface(ID);
		Tab.id = ID;
		Tab.parentID = ID;
		Tab.type = 5;
		Tab.atActionType = 0;
		Tab.contentType = 0;
		Tab.width = 512;
		Tab.height = 334;
		Tab.opacity = 0;
		Tab.mouseOverPopupInterface = -1;
		Tab.valueCompareType = new int[1];
		Tab.requiredValues = new int[1];
		Tab.valueCompareType[0] = 1;
		Tab.requiredValues[0] = configId;
		Tab.valueIndexArray = new int[1][3];
		Tab.valueIndexArray[0][0] = 5;
		Tab.valueIndexArray[0][1] = configFrame;
		Tab.valueIndexArray[0][2] = 0;
		if (name == null) {
			Tab.itemSpriteZoom1 = -1;
			Tab.itemSpriteId1 = i;
			Tab.itemSpriteZoom2 = 70;
			Tab.itemSpriteId2 = i2;
		} else {
		}
	}

	public static void addTransparentSprite(int id, int spriteId, String spriteName, int op) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 10;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.mouseOverPopupInterface = 52;
		tab.width = 512;
		tab.height = 334;
		tab.opacity = (byte) op;
		tab.drawsTransparent = true;
	}

	public static void addPrayerWithTooltip(int i, int configId, int configFrame, int requiredValues,
			int prayerSpriteID, int Hover, String tooltip) {
		RSInterface Interface = addTabInterface(i);
		Interface.id = i;
		Interface.parentID = 5608;
		Interface.type = 5;
		Interface.atActionType = 4;
		Interface.contentType = 0;
		Interface.opacity = 0;
		Interface.mouseOverPopupInterface = Hover;
		Interface.width = 34;
		Interface.height = 34;
		Interface.valueCompareType = new int[1];
		Interface.requiredValues = new int[1];
		Interface.valueCompareType[0] = 1;
		Interface.requiredValues[0] = configId;
		Interface.valueIndexArray = new int[1][3];
		Interface.valueIndexArray[0][0] = 5;
		Interface.valueIndexArray[0][1] = configFrame;
		Interface.valueIndexArray[0][2] = 0;
		Interface.tooltip = tooltip;
		Interface = addTabInterface(i + 1);
		Interface.id = i + 1;
		Interface.parentID = 5608;
		Interface.type = 5;
		Interface.atActionType = 0;
		Interface.contentType = 0;
		Interface.opacity = 0;
		Interface.width = 34;
		Interface.height = 34;
		Interface.valueCompareType = new int[1];
		Interface.requiredValues = new int[1];
		Interface.valueCompareType[0] = 2;
		Interface.requiredValues[0] = requiredValues + 1;
		Interface.valueIndexArray = new int[1][3];
		Interface.valueIndexArray[0][0] = 2;
		Interface.valueIndexArray[0][1] = 5;
		Interface.valueIndexArray[0][2] = 0;
	}

	public static void addPrayer(int i, int configId, int configFrame, int requiredValues, int prayerSpriteID,
			String PrayerName, int Hover) {
		RSInterface Interface = addTabInterface(i);
		Interface.id = i;
		Interface.parentID = 22500;
		Interface.type = 5;
		Interface.atActionType = 4;
		Interface.contentType = 0;
		Interface.opacity = 0;
		Interface.mouseOverPopupInterface = Hover;
		Interface.width = 34;
		Interface.height = 34;
		Interface.valueCompareType = new int[1];
		Interface.requiredValues = new int[1];
		Interface.valueCompareType[0] = 1;
		Interface.requiredValues[0] = configId;
		Interface.valueIndexArray = new int[1][3];
		Interface.valueIndexArray[0][0] = 5;
		Interface.valueIndexArray[0][1] = configFrame;
		Interface.valueIndexArray[0][2] = 0;
		Interface.tooltip = "Activate@or1@ " + PrayerName;
		Interface = addTabInterface(i + 1);
		Interface.id = i + 1;
		Interface.parentID = 22500;
		Interface.type = 5;
		Interface.atActionType = 0;
		Interface.contentType = 0;
		Interface.opacity = 0;
		Interface.width = 34;
		Interface.height = 34;
		Interface.valueCompareType = new int[1];
		Interface.requiredValues = new int[1];
		Interface.valueCompareType[0] = 2;
		Interface.requiredValues[0] = requiredValues + 1;
		Interface.valueIndexArray = new int[1][3];
		Interface.valueIndexArray[0][0] = 2;
		Interface.valueIndexArray[0][1] = 5;
		Interface.valueIndexArray[0][2] = 0;
	}

	public static void addSpriteWithHover(int id, int spriteId, String spriteName, int hover) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.opacity = (byte) 0;
		tab.type = hover;
		tab.width = 190;
		tab.height = 47;
	}

	public static void Curses() {
		RSInterface Interface = addTabInterface(22500);
		int index = 0;
		// addText(687, "99/99", 0xFF981F, false, false, -1, 1);
		addSprite(22502, 0, "Interfaces/CurseTab/ICON");
		addPrayer(22503, 0, 610, 49, 7, "Protect Item", 22582);// 1
		addPrayer(22505, 0, 611, 49, 4, "Sap Warrior", 22544);// 2
		addPrayer(22507, 0, 612, 51, 5, "Sap Ranger", 22546);// 3
		addPrayer(22509, 0, 613, 53, 3, "Sap Mage", 22548);// 4
		addPrayer(22511, 0, 614, 55, 2, "Sap Spirit", 22550);// 5
		addPrayer(22513, 0, 615, 58, 18, "Berserker", 22552);// 6
		addPrayer(22515, 0, 616, 61, 15, "Deflect Summoning", 22554);// /7
		addPrayer(22517, 0, 617, 64, 17, "Deflect Magic", 22556);// /8
		addPrayer(22519, 0, 618, 67, 16, "Deflect Missiles", 22558);// /9
		addPrayer(22521, 0, 619, 70, 6, "Deflect Melee", 22560);// /10
		addPrayer(22523, 0, 620, 73, 9, "Leech Attack", 22562);// 11
		addPrayer(22525, 0, 621, 75, 10, "Leech Ranged", 22564);// 12
		addPrayer(22527, 0, 622, 77, 11, "Leech Magic", 22566);// 13
		addPrayer(22529, 0, 623, 79, 12, "Leech Defence", 22568);// 14
		addPrayer(22531, 0, 624, 81, 13, "Leech Strength", 22570);// 15
		addPrayer(22533, 0, 625, 83, 14, "Leech Energy", 22572);// 16
		addPrayer(22535, 0, 626, 85, 19, "Leech Special Attack", 22574);// 17
		addPrayer(22537, 0, 627, 88, 1, "Wrath", 22576);// /18
		addPrayer(22539, 0, 628, 91, 8, "Soul Split", 22578);// /19
		addPrayer(22541, 0, 629, 94, 20, "Turmoil", 22580);// 20
		addTooltip(22582, "Level 50\nProtect Item\nKeep 1 extra item if you die");
		addTooltip(22544,
				"Level 50\nSap Warrior\nDrains 10% of enemy Attack,\nStrength and Defence,\nincreasing to 20% over time");
		addTooltip(22546, "Level 52\nSap Ranger\nDrains 10% of enemy Ranged\nand Defence, increasing to 20%\nover time");
		addTooltip(22548, "Level 54\nSap Mage\nDrains 10% of enemy Magic\nand Defence, increasing to 20%\nover time");
		addTooltip(22550, "Level 56\nSap Spirit\nDrains enenmy special attack\nenergy");
		addTooltip(22552, "Level 59\nBerserker\nBoosted stats last 15% longer");
		addTooltip(
				22554,
				"Level 62\nDeflect Summoning\nReduces damage dealt from\nSummoning scrolls, prevents the\nuse of a familiar's special\nattack, and can deflect some of\ndamage back to the attacker");
		addTooltip(22556,
				"Level 65\nDeflect Magic\nProtects against magical attacks\nand can deflect some of the\ndamage back to the attacker");
		addTooltip(22558,
				"Level 68\nDeflect Missiles\nProtects against ranged attacks\nand can deflect some of the\ndamage back to the attacker");
		addTooltip(22560,
				"Level 71\nDeflect Melee\nProtects against melee attacks\nand can deflect some of the\ndamage back to the attacker");
		addTooltip(
				22562,
				"Level 74\nLeech Attack\nBoosts Attack by 5%, increasing\nto 10% over time, while draining\nenemy Attack by 10%, increasing\nto 25% over time");
		addTooltip(
				22564,
				"Level 76\nLeech Ranged\nBoosts Ranged by 5%, increasing\nto 10% over time, while draining\nenemy Ranged by 10%,\nincreasing to 25% over\ntime");
		addTooltip(
				22566,
				"Level 78\nLeech Magic\nBoosts Magic by 5%, increasing\nto 10% over time, while draining\nenemy Magic by 10%, increasing\nto 25% over time");
		addTooltip(
				22568,
				"Level 80\nLeech Defence\nBoosts Defence by 5%, increasing\nto 10% over time, while draining\n enemy Defence by10%,\nincreasing to 25% over\ntime");
		addTooltip(
				22570,
				"Level 82\nLeech Strength\nBoosts Strength by 5%, increasing\nto 10% over time, while draining\nenemy Strength by 10%, increasing\n to 25% over time");
		addTooltip(22572, "Level 84\nLeech Energy\nDrains enemy run energy, while\nincreasing your own");
		addTooltip(22574,
				"Level 86\nLeech Special Attack\nDrains enemy special attack\nenergy, while increasing your\nown");
		addTooltip(22576, "Level 89\nWrath\nInflicts damage to nearby\ntargets if you die");
		addTooltip(22578,
				"Level 92\nSoul Split\n1/4 of damage dealt is also removed\nfrom opponent's Prayer and\nadded to your Hitpoints");
		addTooltip(
				22580,
				"Level 95\nTurmoil\nIncreases Attack and Defence\nby 15%, plus 15% of enemy's\nlevel, and Strength by 23% plus\n10% of enemy's level");
		setChildren(62, Interface);

		setBounds(687, 85, 241, index, Interface);
		index++;
		setBounds(22502, 65, 241, index, Interface);
		index++;
		setBounds(22503, 2, 5, index, Interface);
		index++;
		setBounds(22504, 8, 8, index, Interface);
		index++;
		setBounds(22505, 40, 5, index, Interface);
		index++;
		setBounds(22506, 47, 12, index, Interface);
		index++;
		setBounds(22507, 76, 5, index, Interface);
		index++;
		setBounds(22508, 82, 11, index, Interface);
		index++;
		setBounds(22509, 113, 5, index, Interface);
		index++;
		setBounds(22510, 116, 8, index, Interface);
		index++;
		setBounds(22511, 150, 5, index, Interface);
		index++;
		setBounds(22512, 155, 10, index, Interface);
		index++;
		setBounds(22513, 2, 45, index, Interface);
		index++;
		setBounds(22514, 9, 48, index, Interface);
		index++;
		setBounds(22515, 39, 45, index, Interface);
		index++;
		setBounds(22516, 42, 47, index, Interface);
		index++;
		setBounds(22517, 76, 45, index, Interface);
		index++;
		setBounds(22518, 79, 48, index, Interface);
		index++;
		setBounds(22519, 113, 45, index, Interface);
		index++;
		setBounds(22520, 116, 48, index, Interface);
		index++;
		setBounds(22521, 151, 45, index, Interface);
		index++;
		setBounds(22522, 154, 48, index, Interface);
		index++;
		setBounds(22523, 2, 82, index, Interface);
		index++;
		setBounds(22524, 6, 86, index, Interface);
		index++;
		setBounds(22525, 40, 82, index, Interface);
		index++;
		setBounds(22526, 42, 86, index, Interface);
		index++;
		setBounds(22527, 77, 82, index, Interface);
		index++;
		setBounds(22528, 79, 86, index, Interface);
		index++;
		setBounds(22529, 114, 83, index, Interface);
		index++;
		setBounds(22530, 119, 87, index, Interface);
		index++;
		setBounds(22531, 153, 83, index, Interface);
		index++;
		setBounds(22532, 156, 86, index, Interface);
		index++;
		setBounds(22533, 2, 120, index, Interface);
		index++;
		setBounds(22534, 7, 125, index, Interface);
		index++;
		setBounds(22535, 40, 120, index, Interface);
		index++;
		setBounds(22536, 45, 124, index, Interface);
		index++;
		setBounds(22537, 78, 120, index, Interface);
		index++;
		setBounds(22538, 86, 124, index, Interface);
		index++;
		setBounds(22539, 114, 120, index, Interface);
		index++;
		setBounds(22540, 120, 125, index, Interface);
		index++;
		setBounds(22541, 151, 120, index, Interface);
		index++;
		setBounds(22542, 153, 127, index, Interface);
		index++;
		setBounds(22582, 10, 40, index, Interface);
		index++;
		setBounds(22544, 20, 40, index, Interface);
		index++;
		setBounds(22546, 20, 40, index, Interface);
		index++;
		setBounds(22548, 20, 40, index, Interface);
		index++;
		setBounds(22550, 20, 40, index, Interface);
		index++;
		setBounds(22552, 10, 80, index, Interface);
		index++;
		setBounds(22554, 10, 80, index, Interface);
		index++;
		setBounds(22556, 10, 80, index, Interface);
		index++;
		setBounds(22558, 10, 80, index, Interface);
		index++;
		setBounds(22560, 10, 80, index, Interface);
		index++;
		setBounds(22562, 10, 120, index, Interface);
		index++;
		setBounds(22564, 10, 120, index, Interface);
		index++;
		setBounds(22566, 10, 120, index, Interface);
		index++;
		setBounds(22568, 5, 120, index, Interface);
		index++;
		setBounds(22570, 5, 120, index, Interface);
		index++;
		setBounds(22572, 10, 160, index, Interface);
		index++;
		setBounds(22574, 10, 160, index, Interface);
		index++;
		setBounds(22576, 10, 160, index, Interface);
		index++;
		setBounds(22578, 10, 160, index, Interface);
		index++;
		setBounds(22580, 10, 160, index, Interface);
		index++;

	}

	public static void collectSell() {
		RSInterface rsinterface = addTabInterface(54700);
		int x = 9;
		addSprite(54701, 1, "Interfaces/GE/sellCollect");

		addHoverButton(54702, "Interfaces/GrandExchange/close", 1, 16, 16, "Close", 0, 54703, 1);
		addHoveredButton(54703, "Interfaces/GrandExchange/close", 2, 16, 16, 54704);
		addHoverButton(54758, "Interfaces/GrandExchange/sprite", 25, 29, 23, "Back", 0, 54759, 1);
		addHoveredButton(54759, "Interfaces/GrandExchange/sprite", 26, 29, 23, 54760);
		// addText(54769, "Choose an item to exchange", 0, 0x96731A, false,
		// true);
		// addText(54770, "Select an item from your invertory to sell.", 0,
		// 0x958E60, false, true);
		// addText(54771, "0", 0, 0xB58338, true, true);
		// addText(54772, "1 gp", 0, 0xB58338, true, true);
		// addText(54773, "0 gp", 0, 0xB58338, true, true);
		addHoverButton(54793, "Interfaces/GE/collectNoHover", 1, 40, 36, "[GE]", 0, 54794, 1);
		addHoveredButton(54794, "Interfaces/GE/collectHover", 1, 40, 36, 54795);
		addHoverButton(54796, "Interfaces/GE/collectNoHover", 1, 40, 36, "[GE]", 0, 54797, 1);
		addHoveredButton(54797, "Interfaces/GE/collectHover", 1, 40, 36, 54798);
		RSInterface add = addInterface(54780);
		addToItemGroup(add, 1, 1, 24, 24, true, "[GE]", "[GE]", "[GE]");
		add = addInterface(54781);
		addToItemGroup(add, 1, 1, 24, 24, true, "[COINS]Collect", "[GE]", "[GE]");
		add = addInterface(54782);
		addToItemGroup(add, 1, 1, 24, 24, true, "[ITEM]Collect", "[GE]", "[GE]");
		// addText(54784, "", 0, 0xFFFF00, false, true);
		// addText(54785, "", 0, 0xFFFF00, false, true);
		// addText(54787, "N/A", 0, 0xB58338, false, true);
		// addText(54788, "", 0, 0xFFFF00, true, true);
		// addText(54789, "", 0, 0xFFFF00, true, true);
		addHoverButton(54800, "Interfaces/GE/clickAbort", 1, 20, 20, "Abort offer", 0, 54801, 1);
		addHoveredButton(54801, "Interfaces/GE/clickAbort", 2, 20, 20, 54802);
		rsinterface.totalChildren(24);
		rsinterface.child(0, 54701, 4 + x, 23);// 385, 260
		rsinterface.child(1, 54702, 464 + x, 33);// 435, 260
		rsinterface.child(2, 54703, 464 + x, 33);
		rsinterface.child(3, 54758, 19 + x, 284);
		rsinterface.child(4, 54759, 19 + x, 284);
		rsinterface.child(5, 54769, 202 + x, 71);
		rsinterface.child(6, 54770, 202 + x, 98);
		rsinterface.child(7, 54771, 142 + x, 185);
		rsinterface.child(8, 54772, 354 + x, 185);
		rsinterface.child(9, 54773, 252 + x, 246);
		rsinterface.child(10, 54793, 386 + x, 256 + 23);
		rsinterface.child(11, 54794, 386 + x, 256 + 23);
		rsinterface.child(12, 54796, 435 + x, 256 + 23);
		rsinterface.child(13, 54797, 435 + x, 256 + 23);
		rsinterface.child(14, 54780, 97 + x, 97);
		rsinterface.child(15, 54781, 385 + 4 + x, 260 + 23);
		rsinterface.child(16, 54782, 435 + 4 + x, 260 + 23);
		rsinterface.child(17, 54784, 385 + 4 + x, 260 + 23);
		rsinterface.child(18, 54785, 435 + 4 + x, 260 + 23);
		rsinterface.child(19, 54787, 108, 136);
		rsinterface.child(20, 54788, 214 + x, 249 + 23);
		rsinterface.child(21, 54789, 214 + x, 263 + 23);
		rsinterface.child(22, 54800, 345 + x, 250 + 23);
		rsinterface.child(23, 54801, 345 + x, 250 + 23);
	}

	public static void collectBuy() {
		RSInterface rsinterface = addTabInterface(53700);
		int x = 9;
		addSprite(53701, 1, "Interfaces/GE/buyCollect");
		addHoverButton(53702, "Interfaces/GrandExchange/close", 1, 16, 16, "Close", 0, 53703, 1);
		addHoveredButton(53703, "Interfaces/GrandExchange/close", 2, 16, 16, 53704);
		addHoverButton(53758, "Interfaces/GrandExchange/sprite", 25, 29, 23, "Back", 0, 53759, 1);
		addHoveredButton(53759, "Interfaces/GrandExchange/sprite", 26, 29, 23, 53760);
		// addText(53769, "Choose an item to exchange", 0, 0x96731A, false,
		// true);
		// addText(53770, "Select an item from your invertory to sell.", 0,
		// 0x958E60, false, true);
		// addText(53771, "0", 0, 0xB58338, true, true);
		// addText(53772, "1 gp", 0, 0xB58338, true, true);
		// addText(53773, "0 gp", 0, 0xB58338, true, true);
		addHoverButton(53793, "Interfaces/GE/collectNoHover", 1, 40, 36, "[GE]", 0, 53794, 1);
		addHoveredButton(53794, "Interfaces/GE/collectHover", 1, 40, 36, 53795);
		addHoverButton(53796, "Interfaces/GE/collectNoHover", 1, 40, 36, "[GE]", 0, 53797, 1);
		addHoveredButton(53797, "Interfaces/GE/collectHover", 1, 40, 36, 53798);
		RSInterface add = addInterface(53780);
		addToItemGroup(add, 1, 1, 24, 24, true, "[GE]", "[GE]", "[GE]");
		add = addInterface(53781);
		addToItemGroup(add, 1, 1, 24, 24, true, "[ITEM]Collect", "[GE]", "[GE]");
		add = addInterface(53782);
		addToItemGroup(add, 1, 1, 24, 24, true, "[COINS]Collect", "[GE]", "[GE]");
		// addText(53784, "", 0, 0xFFFF00, false, true);
		// addText(53785, "", 0, 0xFFFF00, false, true);
		// addText(53787, "N/A", 0, 0xB58338, false, true);
		// addText(53788, "", 0, 0xFFFF00, true, true);
		// addText(53789, "", 0, 0xFFFF00, true, true);
		addHoverButton(53800, "Interfaces/GE/clickAbort", 1, 20, 20, "Abort offer", 0, 53801, 1);
		addHoveredButton(53801, "Interfaces/GE/clickAbort", 2, 20, 20, 53802);
		rsinterface.totalChildren(24);
		rsinterface.child(0, 53701, 4 + x, 23);// 385, 260
		rsinterface.child(1, 53702, 464 + x, 33);// 435, 260
		rsinterface.child(2, 53703, 464 + x, 33);
		rsinterface.child(3, 53758, 19 + x, 284);
		rsinterface.child(4, 53759, 19 + x, 284);
		rsinterface.child(5, 53769, 202 + x, 71);
		rsinterface.child(6, 53770, 202 + x, 98);
		rsinterface.child(7, 53771, 142 + x, 185);
		rsinterface.child(8, 53772, 354 + x, 185);
		rsinterface.child(9, 53773, 252 + x, 246);
		rsinterface.child(10, 53793, 386 + x, 256 + 23);
		rsinterface.child(11, 53794, 386 + x, 256 + 23);
		rsinterface.child(12, 53796, 435 + x, 256 + 23);
		rsinterface.child(13, 53797, 435 + x, 256 + 23);
		rsinterface.child(14, 53780, 97 + x, 97);
		rsinterface.child(15, 53781, 385 + 4 + x, 260 + 23);
		rsinterface.child(16, 53782, 435 + 4 + x, 260 + 23);
		rsinterface.child(17, 53784, 385 + 4 + x, 260 + 23);
		rsinterface.child(18, 53785, 435 + 4 + x, 260 + 23);
		rsinterface.child(19, 53787, 108, 136);
		rsinterface.child(20, 53788, 214 + x, 249 + 23);
		rsinterface.child(21, 53789, 214 + x, 263 + 23);
		rsinterface.child(22, 53800, 345 + x, 250 + 23);
		rsinterface.child(23, 53801, 345 + x, 250 + 23);
	}

	public static void addHoverButton(int a, int i, String imageName, int j, int width, int height, String text,
			int contentType, int hoverOver, int aT) {// hoverable button
		RSInterface tab = addTabInterface(i);
		tab.id = i;
		tab.parentID = i;
		tab.type = 5;
		tab.atActionType = aT;
		tab.contentType = contentType;
		tab.opacity = 0;
		tab.mouseOverPopupInterface = hoverOver;
		// tab.disabledSprite = imageLoader(j, imageName);

		if (a == 1) {
		}
		tab.width = width;
		tab.height = height;
		tab.tooltip = text;
	}

	public static void Buy() {
		RSInterface rsinterface = addTabInterface(24600);
		int x = 9;
		addSprite(24601, 0, "Interfaces/GrandExchange/buy");
		addHoverButton(24602, "Interfaces/GrandExchange/close", 1, 16, 16, "Close", 0, 24603, 1);
		addHoveredButton(24603, "Interfaces/GrandExchange/close", 2, 16, 16, 24604);
		addHoverButton(24606, "Interfaces/GrandExchange/sprite", 1, 13, 13, "Decrease Quantity", 0, 24607, 1);
		addHoveredButton(24607, "Interfaces/GrandExchange/sprite", 3, 13, 13, 24608);
		addHoverButton(24610, "Interfaces/GrandExchange/sprite", 2, 13, 13, "Increase Quantity", 0, 24611, 1);
		addHoveredButton(24611, "Interfaces/GrandExchange/sprite", 4, 13, 13, 24612);
		addHoverButton(24614, "Interfaces/GrandExchange/sprite", 5, 35, 25, "Add 1", 0, 24615, 1);
		addHoveredButton(24615, "Interfaces/GrandExchange/sprite", 6, 35, 25, 24616);
		addHoverButton(24618, "Interfaces/GrandExchange/sprite", 7, 35, 25, "Add 10", 0, 24619, 1);
		addHoveredButton(24619, "Interfaces/GrandExchange/sprite", 8, 35, 25, 24620);
		addHoverButton(24622, "Interfaces/GrandExchange/sprite", 9, 35, 25, "Add 100", 0, 24623, 1);
		addHoveredButton(24623, "Interfaces/GrandExchange/sprite", 10, 35, 25, 24624);
		addHoverButton(24626, "Interfaces/GrandExchange/sprite", 11, 35, 25, "Add 1000", 0, 24627, 1);
		addHoveredButton(24627, "Interfaces/GrandExchange/sprite", 12, 35, 25, 24628);
		addHoverButton(24630, "Interfaces/GrandExchange/sprite", 13, 35, 25, "Edit Quantity", 7712, 24631, 1);
		addHoveredButton(24631, "Interfaces/GrandExchange/sprite", 14, 35, 25, 24632);
		addHoverButton(24634, "Interfaces/GrandExchange/sprite", 15, 35, 25, "Decrease Price", 0, 24635, 1);
		addHoveredButton(24635, "Interfaces/GrandExchange/sprite", 16, 35, 25, 24636);
		addHoverButton(24638, "Interfaces/GrandExchange/sprite", 17, 35, 25, "Offer Guild Price", 0, 24639, 1);
		addHoveredButton(24639, "Interfaces/GrandExchange/sprite", 18, 35, 25, 24640);
		addHoverButton(24642, "Interfaces/GrandExchange/sprite", 13, 35, 25, "Edit Price", 7714, 24643, 1);
		addHoveredButton(24643, "Interfaces/GrandExchange/sprite", 14, 35, 25, 24644);
		addHoverButton(24646, "Interfaces/GrandExchange/sprite", 19, 35, 25, "Increase Price", 0, 24647, 1);
		addHoveredButton(24647, "Interfaces/GrandExchange/sprite", 20, 35, 25, 24648);
		addHoverButton(24650, "Interfaces/GrandExchange/sprite", 21, 120, 43, "Confirm Offer", 0, 24651, 1);
		addHoveredButton(24651, "Interfaces/GrandExchange/sprite", 22, 120, 43, 24652);
		addHoverButton(1, 24654, "Interfaces/GrandExchange/sprite", 23, 40, 36, "Choose Item", 0, 24655, 1);
		addHoveredButton(24655, "Interfaces/GrandExchange/sprite", 24, 40, 36, 24656);
		addHoverButton(24658, "Interfaces/GrandExchange/sprite", 25, 29, 23, "Back", 0, 24659, 1);
		addHoveredButton(24659, "Interfaces/GrandExchange/sprite", 26, 29, 23, 24660);
		addHoverButton(24662, "Interfaces/GrandExchange/sprite", 1, 13, 13, "Decrease Price", 0, 24663, 1);
		addHoveredButton(24663, "Interfaces/GrandExchange/sprite", 3, 13, 13, 24664);
		addHoverButton(24665, "Interfaces/GrandExchange/sprite", 2, 13, 13, "Increase Price", 0, 24666, 1);
		addHoveredButton(24666, "Interfaces/GrandExchange/sprite", 4, 13, 13, 24667);
		// addText(24669, "Choose an item to exchange", 0, 0x96731A, false,
		// true);
		// addText(24670, "Click the icon to the left to search for items.", 0,
		// 0x958E60, false, true);
		// addText(24671, "0", 0, 0xB58338, true, true);
		// addText(24672, "1 gp", 0, 0xB58338, true, true);
		// addText(24673, "0 gp", 0, 0xB58338, true, true);
		RSInterface add = addInterface(24680);
		addToItemGroup(add, 1, 1, 24, 24, true, "[GE]", "[GE]", "[GE]");
		// addText(24682, "N/A", 0, 0xB58338, false, true);
		rsinterface.totalChildren(42);
		rsinterface.child(0, 24601, 4 + x, 23);
		rsinterface.child(1, 24602, 464 + x, 33);
		rsinterface.child(2, 24603, 464 + x, 33);
		rsinterface.child(3, 24606, 46 + x, 184);
		rsinterface.child(4, 24607, 46 + x, 184);
		rsinterface.child(5, 24610, 226 + x, 184);
		rsinterface.child(6, 24611, 226 + x, 184);
		rsinterface.child(7, 24614, 43 + x, 208);
		rsinterface.child(8, 24615, 43 + x, 208);
		rsinterface.child(9, 24618, 84 + x, 208);
		rsinterface.child(10, 24619, 84 + x, 208);
		rsinterface.child(11, 24622, 125 + x, 208);
		rsinterface.child(12, 24623, 125 + x, 208);
		rsinterface.child(13, 24626, 166 + x, 208);
		rsinterface.child(14, 24627, 166 + x, 208);
		rsinterface.child(15, 24630, 207 + x, 208);
		rsinterface.child(16, 24631, 207 + x, 208);
		rsinterface.child(17, 24634, 260 + x, 208);
		rsinterface.child(18, 24635, 260 + x, 208);
		rsinterface.child(19, 24638, 316 + x, 208);
		rsinterface.child(20, 24639, 316 + x, 208);
		rsinterface.child(21, 24642, 357 + x, 208);
		rsinterface.child(22, 24643, 357 + x, 208);
		rsinterface.child(23, 24646, 413 + x, 208);
		rsinterface.child(24, 24647, 413 + x, 208);
		rsinterface.child(25, 24650, 191 + x, 273);
		rsinterface.child(26, 24651, 191 + x, 273);
		rsinterface.child(27, 24654, 93 + x, 95);
		rsinterface.child(28, 24655, 93 + x, 95);
		rsinterface.child(29, 24658, 19 + x, 284);
		rsinterface.child(30, 24659, 19 + x, 284);
		rsinterface.child(31, 24662, 260 + x, 184);
		rsinterface.child(32, 24663, 260 + x, 184);
		rsinterface.child(33, 24665, 435 + x, 184);
		rsinterface.child(34, 24666, 435 + x, 184);
		rsinterface.child(35, 24669, 202 + x, 71);
		rsinterface.child(36, 24670, 202 + x, 98);
		rsinterface.child(37, 24671, 142 + x, 185);
		rsinterface.child(38, 24672, 354 + x, 185);
		rsinterface.child(39, 24673, 252 + x, 246);
		rsinterface.child(40, 24680, 97 + x, 97);
		rsinterface.child(41, 24682, 121, 136);
	}

	public static void quickCurses() {
		int frame = 0;
		RSInterface tab = addTabInterface(17234);
		// addText(17235, "Select your quick curses:", 0, 0xff981f, false,
		int i = 17202;
		for (int j = 630; i <= 17222 || j <= 656; j++) {
			addConfigButton(i, 17200, 2, 1, "/Interfaces/QuickPrayer/Sprite", 14, 15, "Select", 0, 1, j);
			i++;
		}

		addHoverButton(17231, "/Interfaces/QuickPrayer/Sprite", 4, 190, 24, "Confirm Selection", -1, 17232, 1);
		addHoveredButton(17232, "/Interfaces/QuickPrayer/Sprite", 5, 190, 24, 17233);
		setChildren(46, tab);
		setBounds(22504, 5, 28, frame++, tab);
		setBounds(22506, 44, 28, frame++, tab);
		setBounds(22508, 79, 31, frame++, tab);
		setBounds(22510, 116, 30, frame++, tab);
		setBounds(22512, 153, 29, frame++, tab);
		setBounds(22514, 5, 68, frame++, tab);
		setBounds(22516, 44, 67, frame++, tab);
		setBounds(22518, 79, 69, frame++, tab);
		setBounds(22520, 116, 70, frame++, tab);
		setBounds(22522, 154, 70, frame++, tab);
		setBounds(22524, 4, 104, frame++, tab);
		setBounds(22526, 44, 107, frame++, tab);
		setBounds(22528, 81, 105, frame++, tab);
		setBounds(22530, 117, 105, frame++, tab);
		setBounds(22532, 156, 107, frame++, tab);
		setBounds(22534, 5, 145, frame++, tab);
		setBounds(22536, 43, 144, frame++, tab);
		setBounds(22538, 83, 144, frame++, tab);
		setBounds(22540, 115, 141, frame++, tab);
		setBounds(22542, 154, 144, frame++, tab);
		setBounds(17229, 0, 25, frame++, tab);
		setBounds(17201, 0, 22, frame++, tab);
		setBounds(17201, 0, 237, frame++, tab);
		setBounds(17202, 2, 25, frame++, tab);
		setBounds(17203, 41, 25, frame++, tab);
		setBounds(17204, 76, 25, frame++, tab);
		setBounds(17205, 113, 25, frame++, tab);
		setBounds(17206, 150, 25, frame++, tab);
		setBounds(17207, 2, 65, frame++, tab);
		setBounds(17208, 41, 65, frame++, tab);
		setBounds(17209, 76, 65, frame++, tab);
		setBounds(17210, 113, 65, frame++, tab);
		setBounds(17211, 150, 65, frame++, tab);
		setBounds(17212, 2, 102, frame++, tab);
		setBounds(17213, 41, 102, frame++, tab);
		setBounds(17214, 76, 102, frame++, tab);
		setBounds(17215, 113, 102, frame++, tab);
		setBounds(17216, 150, 102, frame++, tab);
		setBounds(17217, 2, 141, frame++, tab);
		setBounds(17218, 41, 141, frame++, tab);
		setBounds(17219, 76, 141, frame++, tab);
		setBounds(17220, 113, 141, frame++, tab);
		setBounds(17221, 150, 141, frame++, tab);
		setBounds(17235, 5, 5, frame++, tab);
		setBounds(17231, 0, 237, frame++, tab);
		setBounds(17232, 0, 237, frame++, tab);
	}

	public static void quickPrayers() {
		int frame = 0;
		RSInterface tab = addTabInterface(17200);
		addSprite(17201, 3, "/Interfaces/QuickPrayer/Sprite");
		// addText(17230, "Select your quick prayers:", 0, 0xff981f, false,
		addTransparentSprite(17229, 0, "/Interfaces/QuickPrayer/Sprite", 50);
		int i = 17202;
		for (int j = 630; i <= 17228 || j <= 656; j++) {
			addConfigButton(i, 17200, 2, 1, "/Interfaces/QuickPrayer/Sprite", 14, 15, "Select", 0, 1, j);
			i++;
		}

		addHoverButton(17231, "/Interfaces/QuickPrayer/Sprite", 4, 190, 24, "Confirm Selection", -1, 17232, 1);
		addHoveredButton(17232, "/Interfaces/QuickPrayer/Sprite", 5, 190, 24, 17233);
		setChildren(58, tab);
		setBounds(25001, 5, 28, frame++, tab);
		setBounds(25003, 44, 28, frame++, tab);
		setBounds(25005, 79, 31, frame++, tab);
		setBounds(25007, 116, 30, frame++, tab);
		setBounds(25009, 153, 29, frame++, tab);
		setBounds(25011, 5, 68, frame++, tab);
		setBounds(25013, 44, 67, frame++, tab);
		setBounds(25015, 79, 69, frame++, tab);
		setBounds(25017, 116, 70, frame++, tab);
		setBounds(25019, 154, 70, frame++, tab);
		setBounds(25021, 4, 104, frame++, tab);
		setBounds(25023, 44, 107, frame++, tab);
		setBounds(25025, 81, 105, frame++, tab);
		setBounds(25027, 117, 105, frame++, tab);
		setBounds(25029, 156, 107, frame++, tab);
		setBounds(25031, 5, 145, frame++, tab);
		setBounds(25033, 43, 144, frame++, tab);
		setBounds(25035, 83, 144, frame++, tab);
		setBounds(25037, 115, 141, frame++, tab);
		setBounds(25039, 154, 144, frame++, tab);
		setBounds(25041, 5, 180, frame++, tab);
		setBounds(25043, 41, 178, frame++, tab);
		setBounds(25045, 79, 183, frame++, tab);
		setBounds(25047, 116, 178, frame++, tab);
		setBounds(25049, 161, 180, frame++, tab);
		setBounds(25051, 4, 219, frame++, tab);
		setBounds(17229, 0, 25, frame++, tab);
		setBounds(17201, 0, 22, frame++, tab);
		setBounds(17201, 0, 237, frame++, tab);
		setBounds(17202, 2, 25, frame++, tab);
		setBounds(17203, 41, 25, frame++, tab);
		setBounds(17204, 76, 25, frame++, tab);
		setBounds(17205, 113, 25, frame++, tab);
		setBounds(17206, 150, 25, frame++, tab);
		setBounds(17207, 2, 65, frame++, tab);
		setBounds(17208, 41, 65, frame++, tab);
		setBounds(17209, 76, 65, frame++, tab);
		setBounds(17210, 113, 65, frame++, tab);
		setBounds(17211, 150, 65, frame++, tab);
		setBounds(17212, 2, 102, frame++, tab);
		setBounds(17213, 41, 102, frame++, tab);
		setBounds(17214, 76, 102, frame++, tab);
		setBounds(17215, 113, 102, frame++, tab);
		setBounds(17216, 150, 102, frame++, tab);
		setBounds(17217, 2, 141, frame++, tab);
		setBounds(17218, 41, 141, frame++, tab);
		setBounds(17219, 76, 141, frame++, tab);
		setBounds(17220, 113, 141, frame++, tab);
		setBounds(17221, 150, 141, frame++, tab);
		setBounds(17222, 2, 177, frame++, tab);
		setBounds(17223, 41, 177, frame++, tab);
		setBounds(17224, 76, 177, frame++, tab);
		setBounds(17225, 113, 177, frame++, tab);
		setBounds(17226, 150, 177, frame++, tab);
		setBounds(17227, 1, 211, frame++, tab);
		setBounds(17230, 5, 5, frame++, tab);
		setBounds(17231, 0, 237, frame++, tab);
		setBounds(17232, 0, 237, frame++, tab);
	}

	public static void prayerMenu() {
		RSInterface prayerMenu = addInterface(5608);
		int index = 0;
		int prayIndex = 0;
		int firstRowXPos = 10;
		int firstRowYPos = 50;
		int secondRowXPos = 10;
		int secondRowYPos = 86;
		int thirdRowXPos = 10;
		int thirdRowYPos = 122;
		int fourthRowXPos = 10;
		int fourthRowYPos = 159;
		int fifthRowXPos = 10;
		int fifthRowYPos = 86;
		int sixthRowXPos = 1;
		int sixthRowYPos = 52;
		// addText(687, "", 0xff981f, false, true, -1, 1);
		addSprite(25105, 0, "Interfaces/PrayerTab/PRAYERICON");
		addPrayerWithTooltip(25000, 0, 83, 0, prayIndex, 25052, "Activate @lre@Thick Skin");
		prayIndex++;
		addPrayerWithTooltip(25002, 0, 84, 3, prayIndex, 25054, "Activate @lre@Burst of Strength");
		prayIndex++;
		addPrayerWithTooltip(25004, 0, 85, 6, prayIndex, 25056, "Activate @lre@Clarity of Thought");
		prayIndex++;
		addPrayerWithTooltip(25006, 0, 601, 7, prayIndex, 25058, "Activate @lre@Sharp Eye");
		prayIndex++;
		addPrayerWithTooltip(25008, 0, 602, 8, prayIndex, 25060, "Activate @lre@Mystic Will");
		prayIndex++;
		addPrayerWithTooltip(25010, 0, 86, 9, prayIndex, 25062, "Activate @lre@Rock Skin");
		prayIndex++;
		addPrayerWithTooltip(25012, 0, 87, 12, prayIndex, 25064, "Activate @lre@Superhuman Strength");
		prayIndex++;
		addPrayerWithTooltip(25014, 0, 88, 15, prayIndex, 25066, "Activate @lre@Improved Reflexes");
		prayIndex++;
		addPrayerWithTooltip(25016, 0, 89, 18, prayIndex, 25068, "Activate @lre@Rapid Restore");
		prayIndex++;
		addPrayerWithTooltip(25018, 0, 90, 21, prayIndex, 25070, "Activate @lre@Rapid Heal");
		prayIndex++;
		addPrayerWithTooltip(25020, 0, 91, 24, prayIndex, 25072, "Activate @lre@Protect Item");
		prayIndex++;
		addPrayerWithTooltip(25022, 0, 603, 25, prayIndex, 25074, "Activate @lre@Hawk Eye");
		prayIndex++;
		addPrayerWithTooltip(25024, 0, 604, 26, prayIndex, 25076, "Activate @lre@Mystic Lore");
		prayIndex++;
		addPrayerWithTooltip(25026, 0, 92, 27, prayIndex, 25078, "Activate @lre@Steel Skin");
		prayIndex++;
		addPrayerWithTooltip(25028, 0, 93, 30, prayIndex, 25080, "Activate @lre@Ultimate Strength");
		prayIndex++;
		addPrayerWithTooltip(25030, 0, 94, 33, prayIndex, 25082, "Activate @lre@Incredible Reflexes");
		prayIndex++;
		addPrayerWithTooltip(25032, 0, 95, 36, prayIndex, 25084, "Activate @lre@Protect from Magic");
		prayIndex++;
		addPrayerWithTooltip(25034, 0, 96, 39, prayIndex, 25086, "Activate @lre@Protect from Missles");
		prayIndex++;
		addPrayerWithTooltip(25036, 0, 97, 42, prayIndex, 25088, "Activate @lre@Protect from Melee");
		prayIndex++;
		addPrayerWithTooltip(25038, 0, 605, 43, prayIndex, 25090, "Activate @lre@Eagle Eye");
		prayIndex++;
		addPrayerWithTooltip(25040, 0, 606, 44, prayIndex, 25092, "Activate @lre@Mystic Might");
		prayIndex++;
		addPrayerWithTooltip(25042, 0, 98, 45, prayIndex, 25094, "Activate @lre@Retribution");
		prayIndex++;
		addPrayerWithTooltip(25044, 0, 99, 48, prayIndex, 25096, "Activate @lre@Redemption");
		prayIndex++;
		addPrayerWithTooltip(25046, 0, 100, 51, prayIndex, 25098, "Activate @lre@Smite");
		prayIndex++;
		addPrayerWithTooltip(25048, 0, 607, 59, prayIndex, 25100, "Activate @lre@Chivalry");
		prayIndex++;
		addPrayerWithTooltip(25050, 0, 608, 69, prayIndex, 25102, "Activate @lre@Piety");
		prayIndex++;
		addTooltip(25052, "Level 01\nThick Skin\nIncreases your Defence by 5%");
		addTooltip(25054, "Level 04\nBurst of Strength\nIncreases your Strength by 5%");
		addTooltip(25056, "Level 07\nClarity of Thought\nIncreases your Attack by 5%");
		addTooltip(25058, "Level 08\nSharp Eye\nIncreases your Ranged by 5%");
		addTooltip(25060, "Level 09\nMystic Will\nIncreases your Magic by 5%");
		addTooltip(25062, "Level 10\nRock Skin\nIncreases your Defence by 10%");
		addTooltip(25064, "Level 13\nSuperhuman Strength\nIncreases your Strength by 10%");
		addTooltip(25066, "Level 16\nImproved Reflexes\nIncreases your Attack by 10%");
		addTooltip(25068,
				"Level 19\nRapid Restore\n2x restore rate for all stats\nexcept Hitpoints, Summoning\nand Prayer");
		addTooltip(25070, "Level 22\nRapid Heal\n2x restore rate for the\nHitpoints stat");
		addTooltip(25072, "Level 25\nProtect Item\nKeep 1 extra item if you die");
		addTooltip(25074, "Level 26\nHawk Eye\nIncreases your Ranged by 10%");
		addTooltip(25076, "Level 27\nMystic Lore\nIncreases your Magic by 10%");
		addTooltip(25078, "Level 28\nSteel Skin\nIncreases your Defence by 15%");
		addTooltip(25080, "Level 31\nUltimate Strength\nIncreases your Strength by 15%");
		addTooltip(25082, "Level 34\nIncredible Reflexes\nIncreases your Attack by 15%");
		addTooltip(25084, "Level 37\nProtect from Magic\nProtection from magical attacks");
		addTooltip(25086, "Level 40\nProtect from Missles\nProtection from ranged attacks");
		addTooltip(25088, "Level 43\nProtect from Melee\nProtection from melee attacks");
		addTooltip(25090, "Level 44\nEagle Eye\nIncreases your Ranged by 15%");
		addTooltip(25092, "Level 45\nMystic Might\nIncreases your Magic by 15%");
		addTooltip(25094, "Level 46\nRetribution\nInflicts damage to nearby\ntargets if you die");
		addTooltip(25096, "Level 49\nRedemption\nHeals you when damaged\nand Hitpoints falls\nbelow 10%");
		addTooltip(25098, "Level 52\nSmite\n1/4 of damage dealt is\nalso removed from\nopponent's Prayer");
		addTooltip(25100, "Level 60\nChivalry\nIncreases your Defence by 20%,\nStrength by 18%, and Attack by\n15%");
		addTooltip(25102, "Level 70\nPiety\nIncreases your Defence by 25%,\nStrength by 23%, and Attack by\n20%");
		setChildren(80, prayerMenu);
		setBounds(687, 85, 241, index, prayerMenu);
		index++;
		setBounds(25105, 65, 241, index, prayerMenu);
		index++;
		setBounds(25000, 2, 5, index, prayerMenu);
		index++;
		setBounds(25001, 5, 8, index, prayerMenu);
		index++;
		setBounds(25002, 40, 5, index, prayerMenu);
		index++;
		setBounds(25003, 44, 8, index, prayerMenu);
		index++;
		setBounds(25004, 76, 5, index, prayerMenu);
		index++;
		setBounds(25005, 79, 11, index, prayerMenu);
		index++;
		setBounds(25006, 113, 5, index, prayerMenu);
		index++;
		setBounds(25007, 116, 10, index, prayerMenu);
		index++;
		setBounds(25008, 150, 5, index, prayerMenu);
		index++;
		setBounds(25009, 153, 9, index, prayerMenu);
		index++;
		setBounds(25010, 2, 45, index, prayerMenu);
		index++;
		setBounds(25011, 5, 48, index, prayerMenu);
		index++;
		setBounds(25012, 39, 45, index, prayerMenu);
		index++;
		setBounds(25013, 44, 47, index, prayerMenu);
		index++;
		setBounds(25014, 76, 45, index, prayerMenu);
		index++;
		setBounds(25015, 79, 49, index, prayerMenu);
		index++;
		setBounds(25016, 113, 45, index, prayerMenu);
		index++;
		setBounds(25017, 116, 50, index, prayerMenu);
		index++;
		setBounds(25018, 151, 45, index, prayerMenu);
		index++;
		setBounds(25019, 154, 50, index, prayerMenu);
		index++;
		setBounds(25020, 2, 82, index, prayerMenu);
		index++;
		setBounds(25021, 4, 84, index, prayerMenu);
		index++;
		setBounds(25022, 40, 82, index, prayerMenu);
		index++;
		setBounds(25023, 44, 87, index, prayerMenu);
		index++;
		setBounds(25024, 77, 82, index, prayerMenu);
		index++;
		setBounds(25025, 81, 85, index, prayerMenu);
		index++;
		setBounds(25026, 114, 83, index, prayerMenu);
		index++;
		setBounds(25027, 117, 85, index, prayerMenu);
		index++;
		setBounds(25028, 153, 83, index, prayerMenu);
		index++;
		setBounds(25029, 156, 87, index, prayerMenu);
		index++;
		setBounds(25030, 2, 120, index, prayerMenu);
		index++;
		setBounds(25031, 5, 125, index, prayerMenu);
		index++;
		setBounds(25032, 40, 120, index, prayerMenu);
		index++;
		setBounds(25033, 43, 124, index, prayerMenu);
		index++;
		setBounds(25034, 78, 120, index, prayerMenu);
		index++;
		setBounds(25035, 83, 124, index, prayerMenu);
		index++;
		setBounds(25036, 114, 120, index, prayerMenu);
		index++;
		setBounds(25037, 115, 121, index, prayerMenu);
		index++;
		setBounds(25038, 151, 120, index, prayerMenu);
		index++;
		setBounds(25039, 154, 124, index, prayerMenu);
		index++;
		setBounds(25040, 2, 158, index, prayerMenu);
		index++;
		setBounds(25041, 5, 160, index, prayerMenu);
		index++;
		setBounds(25042, 39, 158, index, prayerMenu);
		index++;
		setBounds(25043, 41, 158, index, prayerMenu);
		index++;
		setBounds(25044, 76, 158, index, prayerMenu);
		index++;
		setBounds(25045, 79, 163, index, prayerMenu);
		index++;
		setBounds(25046, 114, 158, index, prayerMenu);
		index++;
		setBounds(25047, 116, 158, index, prayerMenu);
		index++;
		setBounds(25048, 153, 158, index, prayerMenu);
		index++;
		setBounds(25049, 161, 160, index, prayerMenu);
		index++;
		setBounds(25050, 2, 196, index, prayerMenu);
		index++;
		setBounds(25051, 4, 207, index, prayerMenu);
		setBoundry(++index, 25052, firstRowXPos - 2, firstRowYPos, prayerMenu);
		setBoundry(++index, 25054, firstRowXPos - 5, firstRowYPos, prayerMenu);
		setBoundry(++index, 25056, firstRowXPos, firstRowYPos, prayerMenu);
		setBoundry(++index, 25058, firstRowXPos, firstRowYPos, prayerMenu);
		setBoundry(++index, 25060, firstRowXPos, firstRowYPos, prayerMenu);
		setBoundry(++index, 25062, secondRowXPos - 9, secondRowYPos, prayerMenu);
		setBoundry(++index, 25064, secondRowXPos - 11, secondRowYPos, prayerMenu);
		setBoundry(++index, 25066, secondRowXPos, secondRowYPos, prayerMenu);
		setBoundry(++index, 25068, secondRowXPos, secondRowYPos, prayerMenu);
		setBoundry(++index, 25070, secondRowXPos + 25, secondRowYPos, prayerMenu);
		setBoundry(++index, 25072, thirdRowXPos, thirdRowYPos, prayerMenu);
		setBoundry(++index, 25074, thirdRowXPos - 2, thirdRowYPos, prayerMenu);
		setBoundry(++index, 25076, thirdRowXPos, thirdRowYPos, prayerMenu);
		setBoundry(++index, 25078, thirdRowXPos - 7, thirdRowYPos, prayerMenu);
		setBoundry(++index, 25080, thirdRowXPos - 10, thirdRowYPos, prayerMenu);
		setBoundry(++index, 25082, fourthRowXPos, fourthRowYPos, prayerMenu);
		setBoundry(++index, 25084, fourthRowXPos - 8, fourthRowYPos, prayerMenu);
		setBoundry(++index, 25086, fourthRowXPos - 7, fourthRowYPos, prayerMenu);
		setBoundry(++index, 25088, fourthRowXPos - 2, fourthRowYPos, prayerMenu);
		setBoundry(++index, 25090, fourthRowXPos - 2, fourthRowYPos, prayerMenu);
		setBoundry(++index, 25092, fifthRowXPos, fifthRowYPos, prayerMenu);
		setBoundry(++index, 25094, fifthRowXPos, fifthRowYPos - 20, prayerMenu);
		setBoundry(++index, 25096, fifthRowXPos, fifthRowYPos - 25, prayerMenu);
		setBoundry(++index, 25098, fifthRowXPos + 15, fifthRowYPos - 25, prayerMenu);
		setBoundry(++index, 25100, fifthRowXPos - 12, fifthRowYPos - 20, prayerMenu);
		setBoundry(++index, 25102, sixthRowXPos - 2, sixthRowYPos, prayerMenu);
		index++;
	}

	public static void setBoundry(int frame, int ID, int X, int Y, RSInterface RSInterface) {
		RSInterface.children[frame] = ID;
		RSInterface.childX[frame] = X;
		RSInterface.childY[frame] = Y;
	}

	public static void addTooltipBox(int id, String text) {
		RSInterface rsi = addInterface(id);
		rsi.id = id;
		rsi.parentID = id;
		rsi.type = 9;
		rsi.message = text;
	}

	public static void addTooltip(int id, String text, int H, int W) {
		RSInterface rsi = addTabInterface(id);
		rsi.id = id;
		rsi.type = 0;
		rsi.isMouseoverTriggered = true;
		rsi.mouseOverPopupInterface = -1;
		addTooltipBox(id + 1, text);
		rsi.totalChildren(1);
		rsi.child(0, id + 1, 0, 0);
		rsi.height = H;
		rsi.width = W;
	}

	public static void setChildren(int total, RSInterface i) {
		i.children = new int[total];
		i.childX = new int[total];
		i.childY = new int[total];
	}

	public static void addTooltip(int id, String text) {
		RSInterface rsinterface = addTabInterface(id);
		rsinterface.parentID = id;
		rsinterface.type = 0;
		rsinterface.isMouseoverTriggered = true;
		rsinterface.mouseOverPopupInterface = -1;
		addTooltipBox(id + 1, text);
		rsinterface.totalChildren(1);
		rsinterface.child(0, id + 1, 0, 0);
	}

	public static void magicTab() {
		RSInterface tab = addTabInterface(1151);
		RSInterface homeHover = addTabInterface(1196);
		RSInterface spellButtons = interfaceCache[12424];
		spellButtons.scrollMax = 0;
		spellButtons.height = 260;
		spellButtons.width = 190;
		int spellButton[] = { 1196, 1199, 1206, 1215, 1224, 1231, 1240, 1249, 1258, 1267, 1274, 1283, 1573, 1290, 1299,
				1308, 1315, 1324, 1333, 1340, 1349, 1358, 1367, 1374, 1381, 1388, 1397, 1404, 1583, 12038, 1414, 1421,
				1430, 1437, 1446, 1453, 1460, 1469, 15878, 1602, 1613, 1624, 7456, 1478, 1485, 1494, 1503, 1512, 1521,
				1530, 1544, 1553, 1563, 1593, 1635, 12426, 12436, 12446, 12456, 6004, 18471 };
		tab.totalChildren(63);
		tab.child(0, 12424, 13, 24);
		for (int i1 = 0; i1 < spellButton.length; i1++) {
			int yPos = i1 <= 34 ? 183 : 8;
			tab.child(1, 1195, 13, 24);
			tab.child(i1 + 2, spellButton[i1], 5, yPos);
			addButton(1195, 1, "Interfaces/Magic/Home", "Cast @gre@Home Teleport");
			RSInterface homeButton = interfaceCache[1195];
			homeButton.mouseOverPopupInterface = 1196;
		}

		for (int i2 = 0; i2 < spellButton.length; i2++) {
			if (i2 < 60)
				spellButtons.childX[i2] = spellButtons.childX[i2] + 24;
			if (i2 == 6 || i2 == 12 || i2 == 19 || i2 == 35 || i2 == 41 || i2 == 44 || i2 == 49 || i2 == 51)
				spellButtons.childX[i2] = 0;
			spellButtons.childY[6] = 24;
			spellButtons.childY[12] = 48;
			spellButtons.childY[19] = 72;
			spellButtons.childY[49] = 96;
			spellButtons.childY[44] = 120;
			spellButtons.childY[51] = 144;
			spellButtons.childY[35] = 170;
			spellButtons.childY[41] = 192;
		}

		homeHover.isMouseoverTriggered = true;
		// addText(1197, "Level 0: Home Teleport", 1, 0xfe981f, true, true);
		RSInterface homeLevel = interfaceCache[1197];
		homeLevel.width = 174;
		homeLevel.height = 68;
		// addText(1198, "A teleport which requires no", 0, 0xaf6a1a, true,
		// addText(18998, "runes and no required level that", 0, 0xaf6a1a,
		homeHover.totalChildren(4);
		homeHover.child(0, 1197, 3, 4);
		homeHover.child(1, 1198, 91, 23);
		homeHover.child(2, 18998, 91, 34);
		homeHover.child(3, 18999, 91, 45);
	}

	public static void addClickableText(int id, String text, String tooltip, int idx, int color, boolean center,
			boolean shadow, int width) {
		RSInterface tab = addTabInterface(id);
		tab.parentID = id;
		tab.id = id;
		tab.type = 4;
		tab.atActionType = 1;
		tab.width = width;
		tab.height = 11;
		tab.contentType = 0;
		tab.opacity = 0;
		tab.mouseOverPopupInterface = -1;
		tab.centerText = center;
		tab.textShadow = shadow;
		tab.message = text;
		tab.disabledText = "";
		tab.textColor = color;
		tab.anInt219 = 0;
		tab.textHoverColour = 0xffffff;
		tab.anInt239 = 0;
		tab.tooltip = tooltip;
	}

	public static void ancientMagicTab() {
		RSInterface tab = addInterface(12855);
		addButton(12856, 1, "Interfaces/Magic/Home", "Cast @gre@Home Teleport");
		RSInterface homeButton = interfaceCache[12856];
		homeButton.mouseOverPopupInterface = 1196;
		int itfChildren[] = { 12856, 12939, 12987, 13035, 12901, 12861, 13045, 12963, 13011, 13053, 12919, 12881,
				13061, 12951, 12999, 13069, 12911, 12871, 13079, 12975, 13023, 13087, 12929, 12891, 13095, 1196, 12940,
				12988, 13036, 12902, 12862, 13046, 12964, 13012, 13054, 12920, 12882, 13062, 12952, 13000, 13070,
				12912, 12872, 13080, 12976, 13024, 13088, 12930, 12892, 13096 };
		tab.totalChildren(itfChildren.length);
		int i1 = 0;
		int xPos = 18;
		int yPos = 8;
		while (i1 < itfChildren.length) {
			if (xPos > 175) {
				xPos = 18;
				yPos += 28;
			}
			if (i1 < 25)
				tab.child(i1, itfChildren[i1], xPos, yPos);
			if (i1 > 24) {
				yPos = i1 >= 41 ? 1 : 181;
				tab.child(i1, itfChildren[i1], 4, yPos);
			}
			i1++;
			xPos += 45;
		}
	}

	public static void drawBlackBox(int xPos, int yPos) {
	}

	public static void addButton(int id, int sid, String spriteName, String tooltip) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = 1;
		tab.contentType = 0;
		tab.opacity = 0;
		tab.mouseOverPopupInterface = 52;
		// tab.disabledSprite = imageLoader(sid, spriteName);
		// Tab.enabledSprite = imageLoader(sid, spriteName);
		// tab.width = //tab.disabledSprite.myWidth;
		// tab.height = //Tab.enabledSprite.myHeight;
		tab.tooltip = tooltip;
	}

	public static void AddInterfaceButton(int i, int j, String name, int W, int H, String S, int AT) {
		RSInterface RSInterface = addInterface(i);
		RSInterface.id = i;
		RSInterface.parentID = i;
		RSInterface.type = 5;
		RSInterface.atActionType = AT;
		RSInterface.contentType = 0;
		RSInterface.opacity = 0;
		RSInterface.mouseOverPopupInterface = 52;
		// class9.disabledSprite = imageLoader(j, name);
		// rsinterface.enabledSprite = imageLoader(j, name);
		RSInterface.width = W;
		RSInterface.height = H;
		RSInterface.tooltip = S;
	}

	public static void friendsTab() {
		RSInterface tab = addTabInterface(5065);
		tab.width = 192;
		tab.height = 263;
		// addText(5067, "Friends List", kam, 1, 0xff9933, true, true);
		// addText(5068, "- Endeavor -", kam, 1, 0xff9933, true, true);
		addSprite(5069, 0, "Interfaces/Kamiel/SPRITE");
		addSprite(5070, 1, "Interfaces/Kamiel/SPRITE");
		addSprite(5071, 1, "Interfaces/Kamiel/SPRITE");
		addSprite(5072, 2, "Interfaces/Kamiel/SPRITE");
		addHoverButton(5073, "Interfaces/Kamiel/SPRITE", 3, 17, 17, "Add friend", 201, 5074, 1);
		addHoveredButton(5074, "Interfaces/Kamiel/SPRITE", 4, 17, 17, 5075);
		addHoverButton(5076, "Interfaces/Kamiel/SPRITE", 5, 17, 17, "Delete friend", 202, 5077, 1);
		addHoveredButton(5077, "Interfaces/Kamiel/SPRITE", 6, 17, 17, 5078);
		// addText(5079, "0 / 200", kam, 0, 0xF5F5DC, false, true, 901, 0);
		addButton(5080, 10, "Interfaces/Kamiel/SPRITE", "Show ignores tab", 17, 17);
		addSprite(5081, 7, "Interfaces/Kamiel/SPRITE");

		RSInterface friendsList = interfaceCache[5066];
		friendsList.height = 197;
		friendsList.width = 174;
		for (int id = 5092; id <= 5191; id++) {
			int i = id - 5092;
			friendsList.children[i] = id;
			friendsList.childX[i] = 3;
			friendsList.childY[i] = friendsList.childY[i] - 3;
		}
		for (int id = 5192; id <= 5291; id++) {
			int i = id - 5092;
			friendsList.children[i] = id;
			friendsList.childX[i] = 113;
			friendsList.childY[i] = friendsList.childY[i] - 3;
		}

		tab.totalChildren(14);
		tab.child(0, 5067, 92, 2);
		tab.child(1, 5068, 92, 17);
		tab.child(2, 5069, 0, 39);
		tab.child(3, 5070, 0, 36);
		tab.child(4, 5071, 0, 237);
		tab.child(5, 5072, 107, 39);
		tab.child(6, 5073, 5, 242);
		tab.child(7, 5074, 5, 242);
		tab.child(8, 5076, 23, 242);
		tab.child(9, 5077, 23, 242);
		tab.child(10, 5079, 46, 245);
		tab.child(11, 5080, 170, 242);
		tab.child(12, 5081, 151, 242);
		tab.child(13, 5066, 0, 39);

	}

	public static void ignoreTab() {
		RSInterface tab = addTabInterface(5715);
		tab.width = 192;
		tab.height = 263;
		// addText(5717, "Ignore List", kam, 1, 0xff9933, true, true);
		addSprite(5718, 0, "Interfaces/Kamiel/SPRITE");
		addSprite(5719, 1, "Interfaces/Kamiel/SPRITE");
		addSprite(5720, 1, "Interfaces/Kamiel/SPRITE");
		addHoverButton(5721, "Interfaces/Kamiel/SPRITE", 11, 17, 17, "Add name", 501, 5722, 1);
		addHoveredButton(5722, "Interfaces/Kamiel/SPRITE", 12, 17, 17, 5723);
		addHoverButton(5724, "Interfaces/Kamiel/SPRITE", 13, 17, 17, "Delete name", 502, 5725, 1);
		addHoveredButton(5725, "Interfaces/Kamiel/SPRITE", 14, 17, 17, 5726);
		// addText(5727, "0 / 100", kam, 0, 0xF5F5DC, false, true, 902, 0);
		addSprite(5728, 8, "Interfaces/Kamiel/SPRITE");
		addButton(5729, 9, "Interfaces/Kamiel/SPRITE", "Show friends tab", 17, 17);

		RSInterface ignoresList = interfaceCache[5716];
		ignoresList.height = 197;
		ignoresList.width = 174;
		for (int id = 5742; id <= 5841; id++) {
			int i = id - 5742;
			ignoresList.children[i] = id;
			ignoresList.childX[i] = 3;
			ignoresList.childY[i] = ignoresList.childY[i] - 3;
		}

		tab.totalChildren(12);
		tab.child(0, 5717, 95, 9);
		tab.child(1, 5718, 0, 39);
		tab.child(2, 5719, 0, 36);
		tab.child(3, 5720, 0, 237);
		tab.child(4, 5721, 5, 242);
		tab.child(5, 5722, 5, 242);
		tab.child(6, 5724, 23, 242);
		tab.child(7, 5725, 23, 242);
		tab.child(8, 5727, 46, 245);
		tab.child(9, 5728, 170, 242);
		tab.child(10, 5729, 151, 242);
		tab.child(11, 5716, 0, 39);

	}

	public static RSInterface addInterface(int id) {
		RSInterface rsi = interfaceCache[id] = new RSInterface();
		rsi.id = id;
		rsi.parentID = id;
		rsi.width = 512;
		rsi.height = 334;
		return rsi;
	}

	public static void textColor(int id, int color) {
		RSInterface rsi = interfaceCache[id];
		rsi.textColor = color;
	}

	public static void textSize(int id, int idx) {
	}

	public static void addCacheSprite(int id, int sprite1, int sprite2, String sprites) {
		RSInterface rsi = interfaceCache[id] = new RSInterface();
		rsi.parentID = id;
		rsi.id = id;
		rsi.type = 5;
	}

	public static void sprite1(int id, int sprite) {
		// class9.disabledSprite = CustomSpriteLoader(sprite, "");
	}

	public static void addActionButton(int id, int sprite, int sprite2, int width, int height, String s) {
		RSInterface rsi = interfaceCache[id] = new RSInterface();
		// rsi.disabledSprite = CustomSpriteLoader(sprite, "");
		// if (sprite2 == sprite)
		// rsi.enabledSprite = CustomSpriteLoader(sprite, "a");
		// else
		// rsi.enabledSprite = CustomSpriteLoader(sprite2, "");
		rsi.tooltip = s;
		rsi.contentType = 0;
		rsi.atActionType = 1;
		rsi.width = width;
		rsi.mouseOverPopupInterface = 52;
		rsi.parentID = id;
		rsi.id = id;
		rsi.type = 5;
		rsi.height = height;
	}

	public static void addToggleButton(int id, int sprite, int setconfig, int width, int height, String s) {
		RSInterface rsi = addInterface(id);
		// rsi.disabledSprite = CustomSpriteLoader(sprite, "");
		// rsi.enabledSprite = CustomSpriteLoader(sprite, "a");
		rsi.requiredValues = new int[1];
		rsi.requiredValues[0] = 1;
		rsi.valueCompareType = new int[1];
		rsi.valueCompareType[0] = 1;
		rsi.valueIndexArray = new int[1][3];
		rsi.valueIndexArray[0][0] = 5;
		rsi.valueIndexArray[0][1] = setconfig;
		rsi.valueIndexArray[0][2] = 0;
		rsi.atActionType = 4;
		rsi.width = width;
		rsi.mouseOverPopupInterface = -1;
		rsi.parentID = id;
		rsi.id = id;
		rsi.type = 5;
		rsi.height = height;
		rsi.tooltip = s;
	}

	public void totalChildren(int id, int x, int y) {
		children = new int[id];
		childX = new int[x];
		childY = new int[y];
	}

	public static void removeSomething(int id) {
		@SuppressWarnings("unused")
		RSInterface rsi = interfaceCache[id] = new RSInterface();
	}

	public void specialBar(int id) {
		addActionButton(id - 12, 7587, -1, 150, 26, "Use @gre@Special Attack");
		for (int i = id - 11; i < id; i++)
			removeSomething(i);

		RSInterface rsi = interfaceCache[id - 12];
		rsi.width = 150;
		rsi.height = 26;
		rsi = interfaceCache[id];
		rsi.width = 150;
		rsi.height = 26;
		rsi.child(0, id - 12, 0, 0);
		rsi.child(12, id + 1, 3, 7);
		rsi.child(23, id + 12, 16, 8);
		for (int i = 13; i < 23; i++)
			rsi.childY[i]--;

		rsi = interfaceCache[id + 1];
		rsi.type = 5;
		// rsi.disabledSprite = CustomSpriteLoader(7600, "");
		for (int i = id + 2; i < id + 12; i++) {
			rsi = interfaceCache[i];
			rsi.type = 5;
		}

		sprite1(id + 2, 7601);
		sprite1(id + 3, 7602);
		sprite1(id + 4, 7603);
		sprite1(id + 5, 7604);
		sprite1(id + 6, 7605);
		sprite1(id + 7, 7606);
		sprite1(id + 8, 7607);
		sprite1(id + 9, 7608);
		sprite1(id + 10, 7609);
		sprite1(id + 11, 7610);
	}

	public static void Sidebar0() {
		Sidebar0a(1698, 1701, 7499, "Chop", "Hack", "Smash", "Block", 42, 75, 127, 75, 39, 128, 125, 128, 122, 103, 40,
				50, 122, 50, 40, 103);
		Sidebar0a(2276, 2279, 7574, "Stab", "Lunge", "Slash", "Block", 43, 75, 124, 75, 41, 128, 125, 128, 122, 103,
				40, 50, 122, 50, 40, 103);
		Sidebar0a(2423, 2426, 7599, "Chop", "Slash", "Lunge", "Block", 42, 75, 125, 75, 40, 128, 125, 128, 122, 103,
				40, 50, 122, 50, 40, 103);
		Sidebar0a(3796, 3799, 7624, "Pound", "Pummel", "Spike", "Block", 39, 75, 121, 75, 41, 128, 125, 128, 122, 103,
				40, 50, 122, 50, 40, 103);
		Sidebar0a(4679, 4682, 7674, "Lunge", "Swipe", "Pound", "Block", 40, 75, 124, 75, 39, 128, 125, 128, 122, 103,
				40, 50, 122, 50, 40, 103);
		Sidebar0a(4705, 4708, 7699, "Chop", "Slash", "Smash", "Block", 42, 75, 125, 75, 39, 128, 125, 128, 122, 103,
				40, 50, 122, 50, 40, 103);
		Sidebar0a(5570, 5573, 7724, "Spike", "Impale", "Smash", "Block", 41, 75, 123, 75, 39, 128, 125, 128, 122, 103,
				40, 50, 122, 50, 40, 103);
		Sidebar0a(7762, 7765, 7800, "Chop", "Slash", "Lunge", "Block", 42, 75, 125, 75, 40, 128, 125, 128, 122, 103,
				40, 50, 122, 50, 40, 103);
		Sidebar0b(776, 779, "Reap", "Chop", "Jab", "Block", 42, 75, 126, 75, 46, 128, 125, 128, 122, 103, 122, 50, 40,
				103, 40, 50);
		Sidebar0c(425, 428, 7474, "Pound", "Pummel", "Block", 39, 75, 121, 75, 42, 128, 40, 103, 40, 50, 122, 50);
		Sidebar0c(1749, 1752, 7524, "Accurate", "Rapid", "Longrange", 33, 75, 125, 75, 29, 128, 40, 103, 40, 50, 122,
				50);
		Sidebar0c(1764, 1767, 7549, "Accurate", "Rapid", "Longrange", 33, 75, 125, 75, 29, 128, 40, 103, 40, 50, 122,
				50);
		Sidebar0c(4446, 4449, 7649, "Accurate", "Rapid", "Longrange", 33, 75, 125, 75, 29, 128, 40, 103, 40, 50, 122,
				50);
		Sidebar0c(5855, 5857, 7749, "Punch", "Kick", "Block", 40, 75, 129, 75, 42, 128, 40, 50, 122, 50, 40, 103);
		Sidebar0c(6103, 6132, 6117, "Bash", "Pound", "Block", 43, 75, 124, 75, 42, 128, 40, 103, 40, 50, 122, 50);
		Sidebar0c(8460, 8463, 8493, "Jab", "Swipe", "Fend", 46, 75, 124, 75, 43, 128, 40, 103, 40, 50, 122, 50);
		Sidebar0c(12290, 12293, 12323, "Flick", "Lash", "Deflect", 44, 75, 127, 75, 36, 128, 40, 50, 40, 103, 122, 50);
		Sidebar0d(328, 331, "Bash", "Pound", "Focus", 42, 66, 39, 101, 41, 136, 40, 120, 40, 50, 40, 85);
		RSInterface rsi = addInterface(19300);
		textSize(3983, 0);
		addToggleButton(150, 150, 172, 150, 44, "Auto Retaliate");
		rsi.totalChildren(2, 2, 2);
		rsi.child(0, 3983, 52, 25);
		rsi.child(1, 150, 21, 153);
		rsi = interfaceCache[3983];
		rsi.centerText = true;
		rsi.textColor = 0xff981f;
	}

	public static void addAttackStyleButton2(int id, int sprite, int setconfig, int width, int height, String s,
			int hoverID, int hW, int hH, String hoverText) {
		RSInterface rsi = addInterface(id);
		// rsi.disabledSprite = CustomSpriteLoader(sprite, "");
		// rsi.enabledSprite = CustomSpriteLoader(sprite, "a");
		rsi.valueCompareType = new int[1];
		rsi.valueCompareType[0] = 1;
		rsi.requiredValues = new int[1];
		rsi.requiredValues[0] = 1;
		rsi.valueIndexArray = new int[1][3];
		rsi.valueIndexArray[0][0] = 5;
		rsi.valueIndexArray[0][1] = setconfig;
		rsi.valueIndexArray[0][2] = 0;
		rsi.atActionType = 4;
		rsi.width = width;
		rsi.mouseOverPopupInterface = hoverID;
		rsi.parentID = id;
		rsi.id = id;
		rsi.type = 5;
		rsi.height = height;
		rsi.tooltip = s;
		rsi.isMouseoverTriggered = true;
		rsi.type = 0;
		rsi.atActionType = 0;
		rsi.mouseOverPopupInterface = -1;
		rsi.parentID = hoverID;
		rsi.id = hoverID;
	}

	public static void addBox(int id, int byte1, boolean filled, int color, String text) {
		RSInterface Interface = addInterface(id);
		Interface.id = id;
		Interface.parentID = id;
		Interface.type = 9;
		Interface.opacity = (byte) byte1;
		Interface.aBoolean227 = filled;
		Interface.mouseOverPopupInterface = -1;
		Interface.atActionType = 0;
		Interface.contentType = 0;
		Interface.textColor = color;
		Interface.message = text;
	}

	public static void Sidebar0a(int id, int id2, int id3, String text1, String text2, String text3, String text4,
			int str1x, int str1y, int str2x, int str2y, int str3x, int str3y, int str4x, int str4y, int img1x,
			int img1y, int img2x, int img2y, int img3x, int img3y, int img4x, int img4y) {
		RSInterface rsi = addInterface(id);
		// addText(id2, "-2", 3, 0xff981f, true);
		// addText(id2 + 11, text1, 0, 0xff981f, false);
		// addText(id2 + 12, text2, 0, 0xff981f, false);
		// addText(id2 + 13, text3, 0, 0xff981f, false);
		// addText(id2 + 14, text4, 0, 0xff981f, false);
		rsi.specialBar(id3);
		rsi.width = 190;
		rsi.height = 261;
		int last = 15;
		int frame = 0;
		rsi.totalChildren(last, last, last);
		rsi.child(frame, id2 + 3, 21, 46);
		frame++;
		rsi.child(frame, id2 + 4, 104, 99);
		frame++;
		rsi.child(frame, id2 + 5, 21, 99);
		frame++;
		rsi.child(frame, id2 + 6, 105, 46);
		frame++;
		rsi.child(frame, id2 + 7, img1x, img1y);
		frame++;
		rsi.child(frame, id2 + 8, img2x, img2y);
		frame++;
		rsi.child(frame, id2 + 9, img3x, img3y);
		frame++;
		rsi.child(frame, id2 + 10, img4x, img4y);
		frame++;
		rsi.child(frame, id2 + 11, str1x, str1y);
		frame++;
		rsi.child(frame, id2 + 12, str2x, str2y);
		frame++;
		rsi.child(frame, id2 + 13, str3x, str3y);
		frame++;
		rsi.child(frame, id2 + 14, str4x, str4y);
		frame++;
		rsi.child(frame, 19300, 0, 0);
		frame++;
		rsi.child(frame, id2, 94, 4);
		frame++;
		rsi.child(frame, id3, 21, 205);
		frame++;
		for (int i = id2 + 3; i < id2 + 7; i++) {
			rsi = interfaceCache[i];
			// rsi.disabledSprite = CustomSpriteLoader(19301, "");
			// rsi.enabledSprite = CustomSpriteLoader(19301, "a");
			rsi.width = 68;
			rsi.height = 44;
		}

	}

	public static void Sidebar0b(int id, int id2, String text1, String text2, String text3, String text4, int str1x,
			int str1y, int str2x, int str2y, int str3x, int str3y, int str4x, int str4y, int img1x, int img1y,
			int img2x, int img2y, int img3x, int img3y, int img4x, int img4y) {
		RSInterface rsi = addInterface(id);
		// addText(id2, "-2", 3, 0xff981f, true);
		// addText(id2 + 11, text1, 0, 0xff981f, false);
		// addText(id2 + 12, text2, 0, 0xff981f, false);
		// addText(id2 + 13, text3, 0, 0xff981f, false);
		// addText(id2 + 14, text4, 0, 0xff981f, false);
		rsi.width = 190;
		rsi.height = 261;
		int last = 14;
		int frame = 0;
		rsi.totalChildren(last, last, last);
		rsi.child(frame, id2 + 3, 21, 46);
		frame++;
		rsi.child(frame, id2 + 4, 104, 99);
		frame++;
		rsi.child(frame, id2 + 5, 21, 99);
		frame++;
		rsi.child(frame, id2 + 6, 105, 46);
		frame++;
		rsi.child(frame, id2 + 7, img1x, img1y);
		frame++;
		rsi.child(frame, id2 + 8, img2x, img2y);
		frame++;
		rsi.child(frame, id2 + 9, img3x, img3y);
		frame++;
		rsi.child(frame, id2 + 10, img4x, img4y);
		frame++;
		rsi.child(frame, id2 + 11, str1x, str1y);
		frame++;
		rsi.child(frame, id2 + 12, str2x, str2y);
		frame++;
		rsi.child(frame, id2 + 13, str3x, str3y);
		frame++;
		rsi.child(frame, id2 + 14, str4x, str4y);
		frame++;
		rsi.child(frame, 19300, 0, 0);
		frame++;
		rsi.child(frame, id2, 94, 4);
		frame++;
		for (int i = id2 + 3; i < id2 + 7; i++) {
			rsi = interfaceCache[i];
			// rsi.disabledSprite = CustomSpriteLoader(19301, "");
			// rsi.enabledSprite = CustomSpriteLoader(19301, "a");
			rsi.width = 68;
			rsi.height = 44;
		}

	}

	public static void Sidebar0c(int id, int id2, int id3, String text1, String text2, String text3, int str1x,
			int str1y, int str2x, int str2y, int str3x, int str3y, int img1x, int img1y, int img2x, int img2y,
			int img3x, int img3y) {
		RSInterface rsi = addInterface(id);
		// addText(id2, "-2", 3, 0xff981f, true);
		// addText(id2 + 9, text1, 0, 0xff981f, false);
		// addText(id2 + 10, text2, 0, 0xff981f, false);
		// addText(id2 + 11, text3, 0, 0xff981f, false);
		rsi.specialBar(id3);
		rsi.width = 190;
		rsi.height = 261;
		int last = 12;
		int frame = 0;
		rsi.totalChildren(last, last, last);
		rsi.child(frame, id2 + 3, 21, 99);
		frame++;
		rsi.child(frame, id2 + 4, 105, 46);
		frame++;
		rsi.child(frame, id2 + 5, 21, 46);
		frame++;
		rsi.child(frame, id2 + 6, img1x, img1y);
		frame++;
		rsi.child(frame, id2 + 7, img2x, img2y);
		frame++;
		rsi.child(frame, id2 + 8, img3x, img3y);
		frame++;
		rsi.child(frame, id2 + 9, str1x, str1y);
		frame++;
		rsi.child(frame, id2 + 10, str2x, str2y);
		frame++;
		rsi.child(frame, id2 + 11, str3x, str3y);
		frame++;
		rsi.child(frame, 19300, 0, 0);
		frame++;
		rsi.child(frame, id2, 94, 4);
		frame++;
		rsi.child(frame, id3, 21, 205);
		frame++;
		for (int i = id2 + 3; i < id2 + 6; i++) {
			rsi = interfaceCache[i];
			// rsi.disabledSprite = CustomSpriteLoader(19301, "");
			// rsi.enabledSprite = CustomSpriteLoader(19301, "a");
			rsi.width = 68;
			rsi.height = 44;
		}

	}

	public static void Sidebar0d(int id, int id2, String text1, String text2, String text3, int str1x, int str1y,
			int str2x, int str2y, int str3x, int str3y, int img1x, int img1y, int img2x, int img2y, int img3x, int img3y) {
		RSInterface rsi = addInterface(id);
		// addText(id2, "-2", 3, 0xff981f, true);
		// addText(id2 + 9, text1, 0, 0xff981f, false);
		// addText(id2 + 10, text2, 0, 0xff981f, false);
		// addText(id2 + 11, text3, 0, 0xff981f, false);
		removeSomething(353);
		// addText(354, "Spell", 0, 0xff981f, false);
		addCacheSprite(337, 19, 0, "combaticons");
		addCacheSprite(338, 13, 0, "combaticons2");
		addCacheSprite(339, 14, 0, "combaticons2");
		removeSomething(349);
		addToggleButton(350, 350, 108, 68, 44, "Select");
		rsi.width = 190;
		rsi.height = 261;
		int last = 15;
		int frame = 0;
		rsi.totalChildren(last, last, last);
		rsi.child(frame, id2 + 3, 20, 115);
		frame++;
		rsi.child(frame, id2 + 4, 20, 80);
		frame++;
		rsi.child(frame, id2 + 5, 20, 45);
		frame++;
		rsi.child(frame, id2 + 6, img1x, img1y);
		frame++;
		rsi.child(frame, id2 + 7, img2x, img2y);
		frame++;
		rsi.child(frame, id2 + 8, img3x, img3y);
		frame++;
		rsi.child(frame, id2 + 9, str1x, str1y);
		frame++;
		rsi.child(frame, id2 + 10, str2x, str2y);
		frame++;
		rsi.child(frame, id2 + 11, str3x, str3y);
		frame++;
		rsi.child(frame, 349, 105, 46);
		frame++;
		rsi.child(frame, 350, 104, 106);
		frame++;
		rsi.child(frame, 353, 125, 74);
		frame++;
		rsi.child(frame, 354, 125, 134);
		frame++;
		rsi.child(frame, 19300, 0, 0);
		frame++;
		rsi.child(frame, id2, 94, 4);
		frame++;
	}

	public static void emoteTab() {
		RSInterface tab = addTabInterface(147);
		RSInterface scroll = addTabInterface(148);
		tab.totalChildren(1);
		tab.child(0, 148, 0, 1);
		int ButtonIDs[] = { 168, 169, 164, 165, 162, 163, 13370, 171, 167, 170, 13366, 13368, 166, 13363, 13364, 13365,
				161, 11100, 13362, 13367, 172, 13369, 13383, 13384, 667, 6503, 6506, 666, 18464, 18465, 15166, 18686,
				18689, 18688, 18691, 18692, 18687, 154, 22586, 22587, 22588, 22589, 22590, 22591, 22592, 22593, 22594 };
		int EmoteX[] = { 10, 54, 98, 137, 9, 48, 95, 132, 7, 51, 95, 139, 6, 50, 90, 135, 8, 51, 99, 137, 10, 53, 85,
				138, 3, 52, 96, 141, 5, 53, 88, 142, 10, 51, 139, 88, 12, 49, 97, 135, 10, 50, 90, 130, 10, 60, 90 };
		int EmoteY[] = { 6, 6, 8, 6, 55, 55, 55, 55, 104, 104, 103, 104, 153, 153, 153, 153, 203, 202, 203, 202, 250,
				250, 252, 249, 300, 299, 299, 299, 349, 350, 352, 350, 401, 402, 406, 402, 452, 450, 452, 452, 505,
				505, 505, 505, 560, 560, 560 };
		int EmoteIDs[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24,
				25, 26, 27, 33, 34, 35, 36, 28, 29, 30, 37, 31, 32, 38, 39, 40, 41, 42, 43, 44, 45, 46 };
		String EmoteTooltip[] = { "Yes", "No", "Bow", "Angry", "Think", "Wave", "Shrug", "Cheer", "Beckon", "Laugh",
				"Jump for Joy", "Yawn", "Dance", "Jig", "Spin", "Headbang", "Cry", "Blow Kiss", "Panic", "Raspberry",
				"Clap", "Salute", "Goblin Bow", "Goblin Salute", "Glass Box", "Climb Rope", "Lean on Air",
				"Glass Wall", "Idea", "Stomp", "Flap", "Slap Head", "Zombie Walk", "Zombie Dance", "Scared",
				"Zombie Hand", "Bunny Hop", "Skill Cape", "Snowman Dance", "Air Guitar", "Safety First", "Explore",
				"Trick", "Freeze & Melt", "Give Thanks", "Around The World In Eggty Days", "Dramatic Point" };
		scroll.totalChildren(ButtonIDs.length);
		for (int index = 0; index < ButtonIDs.length; index++)
			addButton(ButtonIDs[index], EmoteIDs[index], "Interfaces/Emotes/EMOTE", EmoteTooltip[index]);

		for (int index = 0; index < ButtonIDs.length; index++)
			scroll.child(index, ButtonIDs[index], EmoteX[index], EmoteY[index]);

		scroll.width = 173;
		scroll.height = 260;
		scroll.scrollMax = 610;
	}

	public static void optionTab() {
		RSInterface Interface = addTabInterface(904);
		setChildren(46, Interface);
		addSprite(25801, 0, "Interfaces/OptionTab/OPTION");
		addSprite(25802, 1, "Interfaces/OptionTab/OPTION");
		addSprite(25803, 1, "Interfaces/OptionTab/OPTION");
		addSprite(25804, 1, "Interfaces/OptionTab/OPTION");
		setBounds(25801, 49, 17, 0, Interface);
		setBounds(25802, 49, 54, 1, Interface);
		setBounds(25803, 49, 90, 2, Interface);
		setBounds(25804, 49, 127, 3, Interface);
		addButton(25805, 5, -1, 2, 2, "Interfaces/OptionTab/OPTION", 32, 32, "Adjust Brightness", 166, 1);
		setBounds(25805, 9, 8, 4, Interface);
		addButton(25806, 5, -1, -1, 18, "Interfaces/OptionTab/OPTION", 16, 16, "Select", 166, 1);
		addButton(25807, 5, -1, -1, 18, "Interfaces/OptionTab/OPTION", 16, 16, "Select", 166, 2);
		addButton(25808, 5, -1, -1, 18, "Interfaces/OptionTab/OPTION", 16, 16, "Select", 166, 3);
		addButton(25809, 5, -1, -1, 18, "Interfaces/OptionTab/OPTION", 16, 16, "Select", 166, 4);
		setBounds(25806, 57, 16, 5, Interface);
		setBounds(25807, 88, 16, 6, Interface);
		setBounds(25808, 119, 16, 7, Interface);
		setBounds(25809, 153, 16, 8, Interface);
		addButton(25810, 5, -1, 3, 4, "Interfaces/OptionTab/OPTION", 32, 32, "Adjust Music Level", 168, 4);
		setBounds(25810, 9, 45, 9, Interface);
		addButton(25811, 5, -1, -1, 18, "Interfaces/OptionTab/OPTION", 16, 16, "Select", 168, 4);
		addButton(25812, 5, -1, -1, 18, "Interfaces/OptionTab/OPTION", 16, 16, "Select", 168, 3);
		addButton(25813, 5, -1, -1, 18, "Interfaces/OptionTab/OPTION", 16, 16, "Select", 168, 2);
		addButton(25814, 5, -1, -1, 18, "Interfaces/OptionTab/OPTION", 16, 16, "Select", 168, 1);
		addButton(25815, 5, -1, -1, 18, "Interfaces/OptionTab/OPTION", 16, 16, "Select", 168, 0);
		setBounds(25811, 54, 53, 10, Interface);
		setBounds(25812, 78, 53, 11, Interface);
		setBounds(25813, 105, 53, 12, Interface);
		setBounds(25814, 131, 53, 13, Interface);
		setBounds(25815, 156, 53, 14, Interface);
		addButton(25816, 5, -1, 5, 6, "Interfaces/OptionTab/OPTION", 32, 32, "Adjust Sounds", 169, 4);
		setBounds(25816, 9, 81, 15, Interface);
		addButton(25817, 5, -1, -1, 18, "Interfaces/OptionTab/OPTION", 16, 16, "Select", 169, 4);
		addButton(25818, 5, -1, -1, 18, "Interfaces/OptionTab/OPTION", 16, 16, "Select", 169, 3);
		addButton(25819, 5, -1, -1, 18, "Interfaces/OptionTab/OPTION", 16, 16, "Select", 169, 2);
		addButton(25820, 5, -1, -1, 18, "Interfaces/OptionTab/OPTION", 16, 16, "Select", 169, 1);
		addButton(25821, 5, -1, -1, 18, "Interfaces/OptionTab/OPTION", 16, 16, "Select", 169, 0);
		setBounds(25817, 54, 89, 16, Interface);
		setBounds(25818, 78, 89, 17, Interface);
		setBounds(25819, 105, 89, 18, Interface);
		setBounds(25820, 131, 89, 19, Interface);
		setBounds(25821, 156, 89, 20, Interface);
		addButton(25822, 5, -1, 7, 8, "Interfaces/OptionTab/OPTION", 32, 32, "Adjust Sound Effects", 400, 0);
		setBounds(25822, 10, 119, 21, Interface);
		addButton(25823, 5, -1, -1, 18, "Interfaces/OptionTab/OPTION", 16, 16, "Select", 400, 0);
		addButton(25824, 5, -1, -1, 18, "Interfaces/OptionTab/OPTION", 16, 16, "Select", 400, 1);
		addButton(25825, 5, -1, -1, 18, "Interfaces/OptionTab/OPTION", 16, 16, "Select", 400, 2);
		addButton(25826, 5, -1, -1, 18, "Interfaces/OptionTab/OPTION", 16, 16, "Select", 400, 3);
		addButton(25827, 5, -1, -1, 18, "Interfaces/OptionTab/OPTION", 16, 16, "Select", 400, 4);
		setBounds(25823, 54, 126, 22, Interface);
		setBounds(25824, 78, 126, 23, Interface);
		setBounds(25825, 105, 126, 24, Interface);
		setBounds(25826, 131, 126, 25, Interface);
		setBounds(25827, 156, 126, 26, Interface);
		addButton(25828, 4, 25829, 9, 10, "Interfaces/OptionTab/OPTION", 40, 40, "Toggle Mouse Buttons", 170, 0);
		addTooltip(25829, "Toggle Mouse Buttons");
		addButton(25831, 4, 25832, 9, 10, "Interfaces/OptionTab/OPTION", 40, 40, "Toggle Chat Effects", 171, 0);
		addTooltip(25832, "Toggle Chat Effects");
		addButton(25834, 4, 25835, 9, 10, "Interfaces/OptionTab/OPTION", 40, 40, "Toggle Split-Level Chat", 287, 0);
		addTooltip(25835, "Toggle Split-Level Chat");
		addButton(25837, 4, 25838, 9, 10, "Interfaces/OptionTab/OPTION", 40, 40, "Toggle Accept Aid", 427, 0);
		addTooltip(25838, "Toggle Accept Aid");
		addButton(152, 4, 25841, 9, 10, "Interfaces/OptionTab/OPTION", 40, 40, "Toggle Run Mode", 173, 1);
		addTooltip(25841, "Toggle Run-Mode");
		addButton(25842, 4, 25843, 9, 10, "Interfaces/OptionTab/OPTION", 40, 40, "More Options", 175, 1);
		addTooltip(25843, "More client options,\\nincluding fullscreen");
		setBounds(25828, 19, 152, 27, Interface);
		setBounds(25831, 75, 152, 28, Interface);
		setBounds(25834, 131, 152, 29, Interface);
		setBounds(25837, 19, 206, 30, Interface);
		setBounds(152, 75, 206, 31, Interface);
		setBounds(25857, 78, 159, 32, Interface);
		setBounds(25858, 136, 158, 33, Interface);
		setBounds(25859, 23, 212, 34, Interface);
		setBounds(25860, 86, 210, 35, Interface);
		setBounds(25856, 23, 159, 36, Interface);
		setBounds(25829, 19, 130, 37, Interface);
		setBounds(25832, 78, 130, 38, Interface);
		setBounds(25835, 71, 130, 39, Interface);
		setBounds(25838, 19, 185, 40, Interface);
		setBounds(25841, 78, 185, 41, Interface);
		setBounds(25842, 131, 206, 42, Interface);
		setBounds(25861, 139, 217, 43, Interface);
		setBounds(25843, 137, 185, 44, Interface);
		addSprite(25856, 11, "Interfaces/OptionTab/OPTION");
		addSprite(25857, 12, "Interfaces/OptionTab/OPTION");
		addSprite(25858, 13, "Interfaces/OptionTab/OPTION");
		addSprite(25859, 14, "Interfaces/OptionTab/OPTION");
		addSprite(25860, 15, "Interfaces/OptionTab/OPTION");
		addSprite(25861, 12, "sideicon/sideicons");
		// addText(149, "100%", 0xff9800, true, true, 52, 1);
		setBounds(149, 94, 230, 45, Interface);
	}

	private static void addButton(int ID, int type, int hoverID, int dS, int eS, String NAME, int W, int H,
			String text, int configFrame, int configId) {
		RSInterface rsinterface = addInterface(ID);
		rsinterface.id = ID;
		rsinterface.parentID = ID;
		rsinterface.type = 5;
		rsinterface.atActionType = type;
		rsinterface.opacity = 0;
		rsinterface.mouseOverPopupInterface = hoverID;
		// class9.disabledSprite = imageLoader(dS, NAME);
		// rsinterface.enabledSprite = imageLoader(eS, NAME);
		rsinterface.width = W;
		rsinterface.height = H;
		rsinterface.tooltip = text;
		rsinterface.isMouseoverTriggered = true;
		rsinterface.valueCompareType = new int[1];
		rsinterface.requiredValues = new int[1];
		rsinterface.valueCompareType[0] = 1;
		rsinterface.requiredValues[0] = configId;
		rsinterface.valueIndexArray = new int[1][3];
		rsinterface.valueIndexArray[0][0] = 5;
		rsinterface.valueIndexArray[0][1] = configFrame;
		rsinterface.valueIndexArray[0][2] = 0;
	}

	public static void clanChatTab() {
		RSInterface tab = addTabInterface(18128);
		addHoverButton(18129, "Interfaces//Clan Chat/SPRITE", 6, 72, 32, "Join Chat", 550, 18130, 1);
		addHoveredButton(18130, "Interfaces/Clan Chat/SPRITE", 7, 72, 32, 18131);
		addHoverButton(18132, "Interfaces/Clan Chat/SPRITE", 6, 72, 32, "Leave Chat", -1, 18133, 5);
		addHoveredButton(18133, "Interfaces/Clan Chat/SPRITE", 7, 72, 32, 18134);
		addButton(18250, 0, "Interfaces/Clan Chat/Lootshare", "Toggle lootshare");
		// addText(18135, "Join Chat", 0, 0xff9b00, true, true);
		// addText(18136, "Clan Setup", 0, 0xff9b00, true, true);
		addSprite(18137, 37, "Interfaces/Clan Chat/SPRITE");
		// addText(18138, "Clan Chat", 1, 0xff9b00, true, true);
		// addText(18139, "Talking in: Not in chat", 0, 0xff9b00, false, true);
		// addText(18140, "Owner: None", 0, 0xff9b00, false, true);
		tab.totalChildren(14);
		tab.child(0, 16126, 0, 221);
		tab.child(1, 16126, 0, 59);
		tab.child(2, 18137, 0, 62);
		tab.child(3, 18143, 0, 62);
		tab.child(4, 18129, 15, 226);
		tab.child(5, 18130, 15, 226);
		tab.child(6, 18132, 103, 226);
		tab.child(7, 18133, 103, 226);
		tab.child(8, 18135, 51, 237);
		tab.child(9, 18136, 139, 237);
		tab.child(10, 18138, 95, 1);
		tab.child(11, 18139, 10, 23);
		tab.child(12, 18140, 25, 38);
		tab.child(13, 18250, 145, 15);
		RSInterface list = addTabInterface(18143);
		list.totalChildren(100);

		int id = 18144;
		for (int i = 0; id <= 18243 && i <= 99; i++) {
			list.children[i] = id;
			list.childX[i] = 5;
			int id2 = 18144;
			for (int i2 = 1; id2 <= 18243 && i2 <= 99; i2++) {
				list.childY[0] = 2;
				list.childY[i2] = list.childY[i2 - 1] + 14;
				id2++;
			}

			id++;
		}

		list.height = 158;
		list.width = 174;
		list.scrollMax = 1405;
	}

	public static void editClan() {
		RSInterface tab = addTabInterface(40172);
		addSprite(47251, 1, "Interfaces/Clan Chat/CLAN");
		addHoverButton(47252, "Interfaces/Clan Chat/BUTTON", 1, 150, 35, "Set name", 22222, 47253, 1);
		addHoveredButton(47253, "Interfaces/Clan Chat/BUTTON", 2, 150, 35, 47254);
		addHoverButton(47255, "Interfaces/Clan Chat/BUTTON", 1, 150, 35, "Anyone", -1, 47256, 1);
		addHoveredButton(47256, "Interfaces/Clan Chat/BUTTON", 2, 150, 35, 47257);

		addHoverButton(48000, "b", 1, 150, 35, "Only me", -1, 47999, 1);
		addHoverButton(48001, "b", 1, 150, 35, "General+", -1, 47999, 1);
		addHoverButton(48002, "b", 1, 150, 35, "Captain+", -1, 47999, 1);
		addHoverButton(48003, "b", 1, 150, 35, "Lieutenant+", -1, 47999, 1);
		addHoverButton(48004, "b", 1, 150, 35, "Sergeant+", -1, 47999, 1);
		addHoverButton(48005, "b", 1, 150, 35, "Corporal+", -1, 47999, 1);
		addHoverButton(48006, "b", 1, 150, 35, "Recruit+", -1, 47999, 1);
		addHoverButton(48007, "b", 1, 150, 35, "Any friends", -1, 47999, 1);

		addHoverButton(47258, "Interfaces/Clan Chat/BUTTON", 1, 150, 35, "Anyone", -1, 47259, 1);
		addHoveredButton(47259, "Interfaces/Clan Chat/BUTTON", 2, 150, 35, 17260);

		addHoverButton(48010, "b", 1, 150, 35, "Only me", -1, 47999, 1);
		addHoverButton(48011, "b", 1, 150, 35, "General+", -1, 47999, 1);
		addHoverButton(48012, "b", 1, 150, 35, "Captain+", -1, 47999, 1);
		addHoverButton(48013, "b", 1, 150, 35, "Lieutenant+", -1, 47999, 1);
		addHoverButton(48014, "b", 1, 150, 35, "Sergeant+", -1, 47999, 1);
		addHoverButton(48015, "b", 1, 150, 35, "Corporal+", -1, 47999, 1);
		addHoverButton(48016, "b", 1, 150, 35, "Recruit+", -1, 47999, 1);
		addHoverButton(48017, "b", 1, 150, 35, "Any friends", -1, 47999, 1);

		addHoverButton(47261, "Interfaces/Clan Chat/BUTTON", 1, 150, 35, "Only me", -1, 47262, 1);
		addHoveredButton(47262, "Interfaces/Clan Chat/BUTTON", 2, 150, 35, 47263);

		// addHoverButton(48020, "b", 1, 150, 35, "Only me", -1, 47999, 1);
		addHoverButton(48021, "b", 1, 150, 35, "General+", -1, 47999, 1);
		addHoverButton(48022, "b", 1, 150, 35, "Captain+", -1, 47999, 1);
		addHoverButton(48023, "b", 1, 150, 35, "Lieutenant+", -1, 47999, 1);
		addHoverButton(48024, "b", 1, 150, 35, "Sergeant+", -1, 47999, 1);
		addHoverButton(48025, "b", 1, 150, 35, "Corporal+", -1, 47999, 1);
		addHoverButton(48026, "b", 1, 150, 35, "Recruit+", -1, 47999, 1);

		addHoverButton(47267, "Interfaces/Clan Chat/CLOSE", 3, 16, 16, "Close", -1, 47268, 1);
		addHoveredButton(47268, "Interfaces/Clan Chat/CLOSE", 4, 16, 16, 47269);

		// addText(47800, "Clan name:", 0, 0xff981f, false, true);
		// addText(47801, "Who can enter chat?", 0, 0xff981f, false, true);
		// addText(47812, "Who can talk on chat?", 0, 0xff981f, false, true);
		// addText(47813, "Who can kick on chat?", 0, 0xff981f, false, true);
		// addText(47814, "Alex", 0, 0xffffff, true, true);
		// addText(47815, "Anyone", 0, 0xffffff, true, true);
		// addText(47816, "Anyone", 0, 0xffffff, true, true);
		// addText(47817, "Only me", 0, 0xffffff, true, true);
		tab.totalChildren(42);
		tab.child(0, 47251, 15, 15);
		tab.child(1, 47252, 25, 47 + 20);
		tab.child(2, 47253, 25, 47 + 20);
		tab.child(3, 47267, 476, 23);
		tab.child(4, 47268, 476, 23);
		tab.child(5, 48000, 25, 87 + 25);
		tab.child(6, 48001, 25, 87 + 25);
		tab.child(7, 48002, 25, 87 + 25);
		tab.child(8, 48003, 25, 87 + 25);
		tab.child(9, 48004, 25, 87 + 25);
		tab.child(10, 48005, 25, 87 + 25);
		tab.child(11, 48006, 25, 87 + 25);
		tab.child(12, 48007, 25, 87 + 25);
		tab.child(13, 47255, 25, 87 + 25);
		tab.child(14, 47256, 25, 87 + 25);
		tab.child(15, 48010, 25, 128 + 30);
		tab.child(16, 48011, 25, 128 + 30);
		tab.child(17, 48012, 25, 128 + 30);
		tab.child(18, 48013, 25, 128 + 30);
		tab.child(19, 48014, 25, 128 + 30);
		tab.child(20, 48015, 25, 128 + 30);
		tab.child(21, 48016, 25, 128 + 30);
		tab.child(22, 48017, 25, 128 + 30);
		tab.child(23, 47258, 25, 128 + 30);
		tab.child(24, 47259, 25, 128 + 30);
		// tab.child(25, 48020, 25, 168+35);
		tab.child(25, 48021, 25, 168 + 35);
		tab.child(26, 48022, 25, 168 + 35);
		tab.child(27, 48023, 25, 168 + 35);
		tab.child(28, 48024, 25, 168 + 35);
		tab.child(29, 48025, 25, 168 + 35);
		tab.child(30, 48026, 25, 168 + 35);
		tab.child(31, 47261, 25, 168 + 35);
		tab.child(32, 47262, 25, 168 + 35);
		tab.child(33, 47800, 73, 54 + 20);
		tab.child(34, 47801, 53, 95 + 25);
		tab.child(35, 47812, 53, 136 + 30);
		tab.child(36, 47813, 53, 177 + 35);
		tab.child(37, 47814, 100, 54 + 20 + 12);
		tab.child(38, 47815, 100, 95 + 25 + 12);
		tab.child(39, 47816, 100, 136 + 30 + 12);
		tab.child(40, 47817, 100, 177 + 35 + 12);
		tab.child(41, 44000, 0, 94);

		tab = addTabInterface(44000);
		tab.width = 474;
		tab.height = 213;
		tab.scrollMax = 2030;
		for (int i = 44001; i <= 44200; i++) {
			// addText(i, "", 1, 0xffff64);
		}
		for (int i = 44801; i <= 45000; i++) {
			// //addText(i, "Not in clan", 1, 0xffffff, false, true);
			// //tab.disabledSprite(i, "Not ranked", "[CC]", 1, 0xffffff, false,
			// false, 150);
		}
		tab.totalChildren(400);
		int Child = 0;
		int Y = 3;
		for (int i = 44001; i <= 44200; i++) {
			tab.child(Child, i, 204, Y);
			Child++;
			Y += 13;
		}
		Y = 3;
		for (int i = 44801; i <= 45000; i++) {
			tab.child(Child, i, 343, Y);
			Child++;
			Y += 13;
		}
	}

	public static void Pestpanel() {
		RSInterface RSinterface = addInterface(27119);
		// addText(27120, "What", 0x999999, false, true, 52, 1);
		// addText(27121, "What", 0x33cc00, false, true, 52, 1);
		// addText(27122, "(Need 5 to 25 players)", 0xffcc33, false, true, 52,
		// addText(27123, "Points", 0x33ccff, false, true, 52, 1);
		int last = 4;
		RSinterface.children = new int[last];
		RSinterface.childX = new int[last];
		RSinterface.childY = new int[last];
		setBounds(27120, 15, 12, 0, RSinterface);
		setBounds(27121, 15, 30, 1, RSinterface);
		setBounds(27122, 15, 48, 2, RSinterface);
		setBounds(27123, 15, 66, 3, RSinterface);
	}

	public static void Pestpanel2() {
		RSInterface RSinterface = addInterface(21100);
		addSprite(21101, 0, "Interfaces/Pest Control/PEST1");
		addSprite(21102, 1, "Interfaces/Pest Control/PEST1");
		addSprite(21103, 2, "Interfaces/Pest Control/PEST1");
		addSprite(21104, 3, "Interfaces/Pest Control/PEST1");
		addSprite(21105, 4, "Interfaces/Pest Control/PEST1");
		addSprite(21106, 5, "Interfaces/Pest Control/PEST1");
		// addText(21107, "", 0xcc00cc, false, true, 52, 1);
		// addText(21108, "", 255, false, true, 52, 1);
		// addText(21109, "", 0xffff44, false, true, 52, 1);
		// addText(21110, "", 0xcc0000, false, true, 52, 1);
		// addText(21111, "250", 0x99ff33, false, true, 52, 1);
		// addText(21112, "250", 0x99ff33, false, true, 52, 1);
		// addText(21113, "250", 0x99ff33, false, true, 52, 1);
		// addText(21114, "250", 0x99ff33, false, true, 52, 1);
		// addText(21115, "200", 0x99ff33, false, true, 52, 1);
		// addText(21116, "0", 0x99ff33, false, true, 52, 1);
		// addText(21117, "Time Remaining:", 0xffffff, false, true, 52, 0);
		// addText(21118, "", 0xffffff, false, true, 52, 0);
		int last = 18;
		RSinterface.children = new int[last];
		RSinterface.childX = new int[last];
		RSinterface.childY = new int[last];
		setBounds(21101, 361, 26, 0, RSinterface);
		setBounds(21102, 396, 26, 1, RSinterface);
		setBounds(21103, 436, 26, 2, RSinterface);
		setBounds(21104, 474, 26, 3, RSinterface);
		setBounds(21105, 3, 21, 4, RSinterface);
		setBounds(21106, 3, 50, 5, RSinterface);
		setBounds(21107, 371, 60, 6, RSinterface);
		setBounds(21108, 409, 60, 7, RSinterface);
		setBounds(21109, 443, 60, 8, RSinterface);
		setBounds(21110, 479, 60, 9, RSinterface);
		setBounds(21111, 362, 10, 10, RSinterface);
		setBounds(21112, 398, 10, 11, RSinterface);
		setBounds(21113, 436, 10, 12, RSinterface);
		setBounds(21114, 475, 10, 13, RSinterface);
		setBounds(21115, 32, 32, 14, RSinterface);
		setBounds(21116, 32, 62, 15, RSinterface);
		setBounds(21117, 8, 88, 16, RSinterface);
		setBounds(21118, 87, 88, 17, RSinterface);
	}

	public static void addHoverBox(int id, String text) {
		RSInterface rsi = interfaceCache[id];
		rsi.id = id;
		rsi.parentID = id;
		rsi.isMouseoverTriggered = true;
		rsi.type = 8;
		rsi.hoverText = text;
	}

	public static void addButton(int id, int sid, String spriteName, String tooltip, int w, int h) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = 1;
		tab.contentType = 0;
		tab.opacity = 0;
		tab.mouseOverPopupInterface = 52;
		// tab.disabledSprite = imageLoader(sid, spriteName);
		// Tab.enabledSprite = imageLoader(sid, spriteName);
		tab.width = w;
		tab.height = h;
		tab.tooltip = tooltip;
	}

	public static void addConfigButton(int ID, int pID, int bID, int bID2, String bName, int width, int height,
			String tT, int configID, int aT, int configFrame) {
		RSInterface Tab = addTabInterface(ID);
		Tab.parentID = pID;
		Tab.id = ID;
		Tab.type = 5;
		Tab.atActionType = aT;
		Tab.contentType = 0;
		Tab.width = width;
		Tab.height = height;
		Tab.opacity = 0;
		Tab.mouseOverPopupInterface = -1;
		Tab.valueCompareType = new int[1];
		Tab.requiredValues = new int[1];
		Tab.valueCompareType[0] = 1;
		Tab.requiredValues[0] = configID;
		Tab.valueIndexArray = new int[1][3];
		Tab.valueIndexArray[0][0] = 5;
		Tab.valueIndexArray[0][1] = configFrame;
		Tab.valueIndexArray[0][2] = 0;
		// Tab.enabledSprite = imageLoader(bID2, bName);
		Tab.tooltip = tT;
	}

	public static void addSprite(int id, int spriteId, String spriteName) {
		addSprite(id, spriteId, spriteName, -1, -1);
	}

	public static void addSprite(int id, int spriteId, String spriteName, int zoom1, int zoom2) // summon
																								// pouch
																								// creation
	{
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.opacity = 0;
		tab.mouseOverPopupInterface = 52;
		if (spriteName == null) {
			tab.itemSpriteZoom1 = zoom1;
			tab.itemSpriteId1 = spriteId;
			tab.itemSpriteZoom2 = zoom2;
			tab.itemSpriteId2 = spriteId;
		} else {
		}
		tab.width = 512;
		tab.height = 334;
	}

	public static void addHoverButton(int i, String imageName, int sId, int width, int height, String text,
			int contentType, int hoverOver, int aT) {
		RSInterface tab = addTabInterface(i);
		tab.id = i;
		tab.parentID = i;
		tab.type = 5;
		tab.atActionType = aT;
		tab.contentType = contentType;
		tab.opacity = 0;
		tab.mouseOverPopupInterface = hoverOver;
		tab.width = width;
		tab.height = height;
		tab.tooltip = text;
	}

	public static void addHoveredButton(int i, String imageName, int j, int w, int h, int IMAGEID) {
		RSInterface tab = addTabInterface(i);
		tab.parentID = i;
		tab.id = i;
		tab.type = 0;
		tab.atActionType = 0;
		tab.width = w;
		tab.height = h;
		tab.isMouseoverTriggered = true;
		tab.opacity = 0;
		tab.mouseOverPopupInterface = -1;
		tab.scrollMax = 0;
		addHoverImage(IMAGEID, j, j, imageName);
		tab.totalChildren(1);
		tab.child(0, IMAGEID, 0, 0);
	}

	public static void addInAreaHover(int i, String imageName, int sId, int sId2, int w, int h, String text,
			int contentType, int actionType) {
		RSInterface tab = addTabInterface(i);
		tab.id = i;
		tab.parentID = i;
		tab.type = 5;
		tab.atActionType = actionType;
		tab.contentType = contentType;
		tab.opacity = 0;
		tab.mouseOverPopupInterface = i;
		// Tab.enabledSprite = imageLoader(sId2, imageName);
		tab.width = w;
		tab.height = h;
		tab.tooltip = text;
	}

	public static void addHover(int i, int aT, int contentType, int hoverOver, int sId, String imageName, int width,
			int height, String text) {
		addHoverButton(i, imageName, sId, width, height, text, contentType, hoverOver, aT);
	}

	public static void addHovered(int i, int j, String imageName, int w, int h, int IMAGEID) {
		addHoveredButton(i, imageName, j, w, h, IMAGEID);
	}

	public static void addHoverImage(int i, int j, int k, String name) {
		RSInterface tab = addTabInterface(i);
		tab.id = i;
		tab.parentID = i;
		tab.type = 5;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.width = 512;
		tab.height = 334;
		tab.opacity = 0;
		tab.mouseOverPopupInterface = 52;
		// Tab.enabledSprite = imageLoader(k, name);
	}

	public static RSInterface addScreenInterface(int id) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 0;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.width = 512;
		tab.height = 334;
		tab.opacity = 0;
		tab.mouseOverPopupInterface = -1;
		return tab;
	}

	public static RSInterface addTabInterface(int id) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 0;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.width = 512;
		tab.height = 700;
		tab.opacity = 0;
		tab.mouseOverPopupInterface = -1;
		return tab;
	}

	public void child(int id, int interID, int x, int y) {
		children[id] = interID;
		childX[id] = x;
		childY[id] = y;
	}

	public void totalChildren(int t) {
		children = new int[t];
		childX = new int[t];
		childY = new int[t];
	}

	public static void EquipmentTab() {
		RSInterface Interface = interfaceCache[1644];
		addSprite(15101, 0, "Interfaces/Equipment/bl");// cheap hax
		addSprite(15102, 1, "Interfaces/Equipment/bl");// cheap hax
		addSprite(15109, 2, "Interfaces/Equipment/bl");// cheap hax
		removeSomething(15103);
		removeSomething(15104);
		Interface.children[23] = 15101;
		Interface.childX[23] = 40;
		Interface.childY[23] = 205;
		Interface.children[24] = 15102;
		Interface.childX[24] = 110;
		Interface.childY[24] = 205;
		Interface.children[25] = 15109;
		Interface.childX[25] = 39;
		Interface.childY[25] = 240;
		Interface.children[26] = 27650;
		Interface.childX[26] = 0;
		Interface.childY[26] = 0;
		Interface = addInterface(27650);
		addButton(27651, 6, "Interfaces/Equipment/BOX", "Lock/Unlock XP", 27659, 1, 26, 33);
		addTooltip(27659, "Locks/unlocks your EXP");
		addButton(27653, 1, "Interfaces/Equipment/BOX", "Show Equipment Stats", 27655, 1, 40, 39);
		addTooltip(27655, "Show Equipment Stats");
		addButton(27654, 2, "Interfaces/Equipment/BOX", "Items Kept On Death", 27657, 1, 40, 39);
		addTooltip(27657, "Shows items kept on death.");
		setChildren(6, Interface);
		setBounds(27651, 84, 215, 0, Interface);
		setBounds(27653, 29, 205, 1, Interface);
		setBounds(27654, 124, 205, 2, Interface);
		setBounds(27659, 39, 240, 3, Interface);
		setBounds(27655, 39, 240, 4, Interface);
		setBounds(27657, 39, 240, 5, Interface);
	}

	public static void equipmentScreen() {
		RSInterface Interface = addInterface(19148);
		addSprite(19149, 0, "Interfaces/Equipment/CHAR");
		addHover(19150, 3, 0, 19151, 3, "Interfaces/Equipment/CHAR", 21, 21, "Close");
		addHovered(19151, 2, "Interfaces/Equipment/CHAR", 21, 21, 19152);
		// addText(19154, "Equip Your Character...", 0xFF981F, false, true, 52,
		// addText(1673, "Attack bonus", 0xFF981F, false, true, 52, 2);
		// addText(1674, "Defense bonus", 0xFF981F, false, true, 52, 2);
		// addText(1685, "Other bonuses", 0xFF981F, false, true, 52, 2);
		// addText(1675, "Stab:", 0xFF981F, false, true, 52, 1);
		// addText(1676, "Slash:", 0xFF981F, false, true, 52, 1);
		// addText(1677, "Crush:", 0xFF981F, false, true, 52, 1);
		// addText(1678, "Magic:", 0xFF981F, false, true, 52, 1);
		// addText(1679, "Ranged:", 0xFF981F, false, true, 52, 1);
		// addText(1680, "Stab:", 0xFF981F, false, true, 52, 1);
		// addText(1681, "Slash:", 0xFF981F, false, true, 52, 1);
		// addText(1682, "Crush:", 0xFF981F, false, true, 52, 1);
		// addText(1683, "Magic:", 0xFF981F, false, true, 52, 1);
		// addText(1684, "Ranged:", 0xFF981F, false, true, 52, 1);
		// addText(1686, "Strength:", 0xFF981F, false, true, 52, 1);
		// addText(1687, "Prayer:", 0xFF981F, false, true, 52, 1);
		// addText(19155, "0%", 0xFF981F, false, true, 52, 1);
		addChar(19153);
		int last = 45;
		int frame = 0;
		setChildren(last, Interface);
		setBounds(19149, 12, 20, frame, Interface);
		frame++;
		setBounds(19150, 472, 27, frame, Interface);
		frame++;
		setBounds(19151, 472, 27, frame, Interface);
		frame++;
		setBounds(19153, 193, 190, frame, Interface);
		frame++;
		setBounds(19154, 23, 29, frame, Interface);
		frame++;
		setBounds(1673, 365, 71, frame, Interface);
		frame++;
		setBounds(1674, 365, 163, frame, Interface);
		frame++;
		setBounds(1675, 372, 85, frame, Interface);
		frame++;
		setBounds(1676, 372, 99, frame, Interface);
		frame++;
		setBounds(1677, 372, 113, frame, Interface);
		frame++;
		setBounds(1678, 372, 127, frame, Interface);
		frame++;
		setBounds(1679, 372, 141, frame, Interface);
		frame++;
		setBounds(1680, 372, 177, frame, Interface);
		frame++;
		setBounds(1681, 372, 191, frame, Interface);
		frame++;
		setBounds(1682, 372, 205, frame, Interface);
		frame++;
		setBounds(1683, 372, 219, frame, Interface);
		frame++;
		setBounds(1684, 372, 233, frame, Interface);
		frame++;
		setBounds(1685, 365, 253, frame, Interface);
		frame++;
		setBounds(1686, 372, 267, frame, Interface);
		frame++;
		setBounds(1687, 372, 281, frame, Interface);
		frame++;
		setBounds(19155, 94, 286, frame, Interface);
		frame++;
		setBounds(1645, 83, 106, frame, Interface);
		frame++;
		setBounds(1646, 83, 135, frame, Interface);
		frame++;
		setBounds(1647, 83, 172, frame, Interface);
		frame++;
		setBounds(1648, 83, 213, frame, Interface);
		frame++;
		setBounds(1649, 27, 185, frame, Interface);
		frame++;
		setBounds(1650, 27, 221, frame, Interface);
		frame++;
		setBounds(1651, 139, 185, frame, Interface);
		frame++;
		setBounds(1652, 139, 221, frame, Interface);
		frame++;
		setBounds(1653, 53, 148, frame, Interface);
		frame++;
		setBounds(1654, 112, 148, frame, Interface);
		frame++;
		setBounds(1655, 63, 109, frame, Interface);
		frame++;
		setBounds(1656, 117, 108, frame, Interface);
		frame++;
		setBounds(1657, 83, 71, frame, Interface);
		frame++;
		setBounds(1658, 42, 110, frame, Interface);
		frame++;
		setBounds(1659, 83, 110, frame, Interface);
		frame++;
		setBounds(1660, 124, 110, frame, Interface);
		frame++;
		setBounds(1661, 27, 149, frame, Interface);
		frame++;
		setBounds(1662, 83, 149, frame, Interface);
		frame++;
		setBounds(1663, 139, 149, frame, Interface);
		frame++;
		setBounds(1664, 83, 189, frame, Interface);
		frame++;
		setBounds(1665, 83, 229, frame, Interface);
		frame++;
		setBounds(1666, 27, 229, frame, Interface);
		frame++;
		setBounds(1667, 139, 229, frame, Interface);
		frame++;
		setBounds(1688, 29, 111, frame, Interface);
		frame++;
	}

	public static void addButton(int id, int sid, String spriteName, String tooltip, int mOver, int atAction,
			int width, int height) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = atAction;
		tab.contentType = 0;
		tab.opacity = 0;
		tab.mouseOverPopupInterface = mOver;
		// Tab.enabledSprite = imageLoader(sid, spriteName);
		tab.width = width;
		tab.height = height;
		tab.tooltip = tooltip;
		// tab.inventoryhover = true;
	}

	public static void addChar(int ID) {
		RSInterface t = interfaceCache[ID] = new RSInterface();
		t.id = ID;
		t.parentID = ID;
		t.type = 6;
		t.atActionType = 0;
		t.contentType = 328;
		t.width = 136;
		t.height = 168;
		t.opacity = 0;
		t.mouseOverPopupInterface = 0;
		t.modelZoom = 560;
		t.modelRotation1 = 150;
		t.modelRotation2 = 0;
		t.anInt257 = -1;
		t.anInt258 = -1;
	}

	public static void drawRune(int i, int id, String runeName) {
		RSInterface RSInterface = addTabInterface(i);
		RSInterface.type = 5;
		RSInterface.atActionType = 0;
		RSInterface.contentType = 0;
		RSInterface.opacity = 0;
		RSInterface.mouseOverPopupInterface = 52;
		RSInterface.width = 500;
		RSInterface.height = 500;
	}

	public static void addRuneText(int ID, int runeAmount, int RuneID) {
		RSInterface rsInterface = addTabInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 4;
		rsInterface.atActionType = 0;
		rsInterface.contentType = 0;
		rsInterface.width = 0;
		rsInterface.height = 14;
		rsInterface.mouseOverPopupInterface = -1;
		rsInterface.valueCompareType = new int[1];
		rsInterface.requiredValues = new int[1];
		rsInterface.valueCompareType[0] = 3;
		rsInterface.requiredValues[0] = runeAmount - 1;
		rsInterface.valueIndexArray = new int[1][4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = RuneID;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.centerText = true;
		rsInterface.textShadow = true;
		rsInterface.message = (new StringBuilder()).append("%1/").append(runeAmount).append("").toString();
		rsInterface.disabledText = "";
		rsInterface.textColor = 0xc00000;
		rsInterface.anInt219 = 49152;
	}

	public static void homeTeleport() {
		RSInterface RSInterface = addTabInterface(30000);
		RSInterface.tooltip = "Cast @gre@Lunar Home Teleport";
		RSInterface.id = 30000;
		RSInterface.parentID = 30000;
		RSInterface.type = 5;
		RSInterface.atActionType = 5;
		RSInterface.contentType = 0;
		RSInterface.opacity = 0;
		RSInterface.mouseOverPopupInterface = 30001;
		RSInterface.width = 20;
		RSInterface.height = 20;
		RSInterface hover = addTabInterface(30001);
		hover.mouseOverPopupInterface = -1;
		hover.isMouseoverTriggered = true;
		setChildren(1, hover);
		addSprite(30002, 0, "Interfaces/Lunar/SPRITE");
		setBounds(30002, 0, 0, 0, hover);
	}

	public static void addLunar2RunesSmallBox(int ID, int r1, int r2, int ra1, int ra2, int rune1, int lvl,
			String name, String descr, int sid, int suo, int type) {
		RSInterface rsInterface = addTabInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 5;
		rsInterface.atActionType = type;
		rsInterface.contentType = 0;
		rsInterface.mouseOverPopupInterface = ID + 1;
		rsInterface.spellUsableOn = suo;
		rsInterface.selectedActionName = "Cast On";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = (new StringBuilder()).append("Cast @gre@").append(name).toString();
		rsInterface.spellName = name;
		rsInterface.valueCompareType = new int[3];
		rsInterface.requiredValues = new int[3];
		rsInterface.valueCompareType[0] = 3;
		rsInterface.requiredValues[0] = ra1;
		rsInterface.valueCompareType[1] = 3;
		rsInterface.requiredValues[1] = ra2;
		rsInterface.valueCompareType[2] = 3;
		rsInterface.requiredValues[2] = lvl;
		rsInterface.valueIndexArray = new int[3][];
		rsInterface.valueIndexArray[0] = new int[4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = r1;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.valueIndexArray[1] = new int[4];
		rsInterface.valueIndexArray[1][0] = 4;
		rsInterface.valueIndexArray[1][1] = 3214;
		rsInterface.valueIndexArray[1][2] = r2;
		rsInterface.valueIndexArray[1][3] = 0;
		rsInterface.valueIndexArray[2] = new int[3];
		rsInterface.valueIndexArray[2][0] = 1;
		rsInterface.valueIndexArray[2][1] = 6;
		rsInterface.valueIndexArray[2][2] = 0;
		// rsinterface.enabledSprite = loadSprite(sid,
		// "Interfaces/Lunar/LUNARON");
		// class9.disabledSprite = loadSprite(sid, "Interfaces/Lunar/LUNAROFF");
		RSInterface hover = addTabInterface(ID + 1);
		hover.mouseOverPopupInterface = -1;
		hover.isMouseoverTriggered = true;
		setChildren(7, hover);
		addSprite(ID + 2, 0, "Interfaces/Lunar/BOX");
		setBounds(ID + 2, 0, 0, 0, hover);
		setBounds(ID + 3, 90, 4, 1, hover);
		// addText(ID + 4, descr, 0xaf6a1a, true, true, 52, 0);
		setBounds(ID + 4, 90, 19, 2, hover);
		setBounds(30016, 37, 35, 3, hover);
		setBounds(rune1, 112, 35, 4, hover);
		addRuneText(ID + 5, ra1 + 1, r1);
		setBounds(ID + 5, 50, 66, 5, hover);
		addRuneText(ID + 6, ra2 + 1, r2);
		setBounds(ID + 6, 123, 66, 6, hover);
	}

	public static void addLunar3RunesSmallBox(int ID, int r1, int r2, int r3, int ra1, int ra2, int ra3, int rune1,
			int rune2, int lvl, String name, String descr, int sid, int suo, int type) {
		RSInterface rsInterface = addTabInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 5;
		rsInterface.atActionType = type;
		rsInterface.contentType = 0;
		rsInterface.mouseOverPopupInterface = ID + 1;
		rsInterface.spellUsableOn = suo;
		rsInterface.selectedActionName = "Cast on";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = (new StringBuilder()).append("Cast @gre@").append(name).toString();
		rsInterface.spellName = name;
		rsInterface.valueCompareType = new int[4];
		rsInterface.requiredValues = new int[4];
		rsInterface.valueCompareType[0] = 3;
		rsInterface.requiredValues[0] = ra1;
		rsInterface.valueCompareType[1] = 3;
		rsInterface.requiredValues[1] = ra2;
		rsInterface.valueCompareType[2] = 3;
		rsInterface.requiredValues[2] = ra3;
		rsInterface.valueCompareType[3] = 3;
		rsInterface.requiredValues[3] = lvl;
		rsInterface.valueIndexArray = new int[4][];
		rsInterface.valueIndexArray[0] = new int[4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = r1;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.valueIndexArray[1] = new int[4];
		rsInterface.valueIndexArray[1][0] = 4;
		rsInterface.valueIndexArray[1][1] = 3214;
		rsInterface.valueIndexArray[1][2] = r2;
		rsInterface.valueIndexArray[1][3] = 0;
		rsInterface.valueIndexArray[2] = new int[4];
		rsInterface.valueIndexArray[2][0] = 4;
		rsInterface.valueIndexArray[2][1] = 3214;
		rsInterface.valueIndexArray[2][2] = r3;
		rsInterface.valueIndexArray[2][3] = 0;
		rsInterface.valueIndexArray[3] = new int[3];
		rsInterface.valueIndexArray[3][0] = 1;
		rsInterface.valueIndexArray[3][1] = 6;
		rsInterface.valueIndexArray[3][2] = 0;
		RSInterface hover = addTabInterface(ID + 1);
		hover.mouseOverPopupInterface = -1;
		hover.isMouseoverTriggered = true;
		setChildren(9, hover);
		addSprite(ID + 2, 0, "Interfaces/Lunar/BOX");
		setBounds(ID + 2, 0, 0, 0, hover);
		// addText(ID + 3, (new StringBuilder()).append("Level ").append(lvl +
		// 1)
		setBounds(ID + 3, 90, 4, 1, hover);
		// addText(ID + 4, descr, 0xaf6a1a, true, true, 52, 0);
		setBounds(ID + 4, 90, 19, 2, hover);
		setBounds(30016, 14, 35, 3, hover);
		setBounds(rune1, 74, 35, 4, hover);
		setBounds(rune2, 130, 35, 5, hover);
		addRuneText(ID + 5, ra1 + 1, r1);
		setBounds(ID + 5, 26, 66, 6, hover);
		addRuneText(ID + 6, ra2 + 1, r2);
		setBounds(ID + 6, 87, 66, 7, hover);
		addRuneText(ID + 7, ra3 + 1, r3);
		setBounds(ID + 7, 142, 66, 8, hover);
	}

	public static void addLunar3RunesBigBox(int ID, int r1, int r2, int r3, int ra1, int ra2, int ra3, int rune1,
			int rune2, int lvl, String name, String descr, int sid, int suo, int type) {
		RSInterface rsInterface = addTabInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 5;
		rsInterface.atActionType = type;
		rsInterface.contentType = 0;
		rsInterface.mouseOverPopupInterface = ID + 1;
		rsInterface.spellUsableOn = suo;
		rsInterface.selectedActionName = "Cast on";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = (new StringBuilder()).append("Cast @gre@").append(name).toString();
		rsInterface.spellName = name;
		rsInterface.valueCompareType = new int[4];
		rsInterface.requiredValues = new int[4];
		rsInterface.valueCompareType[0] = 3;
		rsInterface.requiredValues[0] = ra1;
		rsInterface.valueCompareType[1] = 3;
		rsInterface.requiredValues[1] = ra2;
		rsInterface.valueCompareType[2] = 3;
		rsInterface.requiredValues[2] = ra3;
		rsInterface.valueCompareType[3] = 3;
		rsInterface.requiredValues[3] = lvl;
		rsInterface.valueIndexArray = new int[4][];
		rsInterface.valueIndexArray[0] = new int[4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = r1;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.valueIndexArray[1] = new int[4];
		rsInterface.valueIndexArray[1][0] = 4;
		rsInterface.valueIndexArray[1][1] = 3214;
		rsInterface.valueIndexArray[1][2] = r2;
		rsInterface.valueIndexArray[1][3] = 0;
		rsInterface.valueIndexArray[2] = new int[4];
		rsInterface.valueIndexArray[2][0] = 4;
		rsInterface.valueIndexArray[2][1] = 3214;
		rsInterface.valueIndexArray[2][2] = r3;
		rsInterface.valueIndexArray[2][3] = 0;
		rsInterface.valueIndexArray[3] = new int[3];
		rsInterface.valueIndexArray[3][0] = 1;
		rsInterface.valueIndexArray[3][1] = 6;
		rsInterface.valueIndexArray[3][2] = 0;
		// class9.disabledSprite = loadSprite(sid, "Interfaces/Lunar/LUNAROFF");
		RSInterface hover = addTabInterface(ID + 1);
		hover.mouseOverPopupInterface = -1;
		hover.isMouseoverTriggered = true;
		setChildren(9, hover);
		addSprite(ID + 2, 1, "Interfaces/Lunar/BOX");
		setBounds(ID + 2, 0, 0, 0, hover);
		// addText(ID + 3, (new StringBuilder()).append("Level ").append(lvl +
		// 1)
		setBounds(ID + 3, 90, 4, 1, hover);
		// addText(ID + 4, descr, 0xaf6a1a, true, true, 52, 0);
		setBounds(ID + 4, 90, 21, 2, hover);
		setBounds(30016, 14, 48, 3, hover);
		setBounds(rune1, 74, 48, 4, hover);
		setBounds(rune2, 130, 48, 5, hover);
		addRuneText(ID + 5, ra1 + 1, r1);
		setBounds(ID + 5, 26, 79, 6, hover);
		addRuneText(ID + 6, ra2 + 1, r2);
		setBounds(ID + 6, 87, 79, 7, hover);
		addRuneText(ID + 7, ra3 + 1, r3);
		setBounds(ID + 7, 142, 79, 8, hover);
	}

	public static void addLunar3RunesLargeBox(int ID, int r1, int r2, int r3, int ra1, int ra2, int ra3, int rune1,
			int rune2, int lvl, String name, String descr, int sid, int suo, int type) {
		RSInterface rsInterface = addTabInterface(ID);
		rsInterface.id = ID;
		rsInterface.parentID = 1151;
		rsInterface.type = 5;
		rsInterface.atActionType = type;
		rsInterface.contentType = 0;
		rsInterface.mouseOverPopupInterface = ID + 1;
		rsInterface.spellUsableOn = suo;
		rsInterface.selectedActionName = "Cast on";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = (new StringBuilder()).append("Cast @gre@").append(name).toString();
		rsInterface.spellName = name;
		rsInterface.valueCompareType = new int[4];
		rsInterface.requiredValues = new int[4];
		rsInterface.valueCompareType[0] = 3;
		rsInterface.requiredValues[0] = ra1;
		rsInterface.valueCompareType[1] = 3;
		rsInterface.requiredValues[1] = ra2;
		rsInterface.valueCompareType[2] = 3;
		rsInterface.requiredValues[2] = ra3;
		rsInterface.valueCompareType[3] = 3;
		rsInterface.requiredValues[3] = lvl;
		rsInterface.valueIndexArray = new int[4][];
		rsInterface.valueIndexArray[0] = new int[4];
		rsInterface.valueIndexArray[0][0] = 4;
		rsInterface.valueIndexArray[0][1] = 3214;
		rsInterface.valueIndexArray[0][2] = r1;
		rsInterface.valueIndexArray[0][3] = 0;
		rsInterface.valueIndexArray[1] = new int[4];
		rsInterface.valueIndexArray[1][0] = 4;
		rsInterface.valueIndexArray[1][1] = 3214;
		rsInterface.valueIndexArray[1][2] = r2;
		rsInterface.valueIndexArray[1][3] = 0;
		rsInterface.valueIndexArray[2] = new int[4];
		rsInterface.valueIndexArray[2][0] = 4;
		rsInterface.valueIndexArray[2][1] = 3214;
		rsInterface.valueIndexArray[2][2] = r3;
		rsInterface.valueIndexArray[2][3] = 0;
		rsInterface.valueIndexArray[3] = new int[3];
		rsInterface.valueIndexArray[3][0] = 1;
		rsInterface.valueIndexArray[3][1] = 6;
		rsInterface.valueIndexArray[3][2] = 0;
		RSInterface hover = addTabInterface(ID + 1);
		hover.isMouseoverTriggered = true;
		hover.mouseOverPopupInterface = -1;
		setChildren(9, hover);
		addSprite(ID + 2, 2, "Interfaces/Lunar/BOX");
		setBounds(ID + 2, 0, 0, 0, hover);
		// addText(ID + 3, (new StringBuilder()).append("Level ").append(lvl +
		// 1)
		setBounds(ID + 3, 90, 4, 1, hover);
		// addText(ID + 4, descr, 0xaf6a1a, true, true, 52, 0);
		setBounds(ID + 4, 90, 34, 2, hover);
		setBounds(30016, 14, 61, 3, hover);
		setBounds(rune1, 74, 61, 4, hover);
		setBounds(rune2, 130, 61, 5, hover);
		addRuneText(ID + 5, ra1 + 1, r1);
		setBounds(ID + 5, 26, 92, 6, hover);
		addRuneText(ID + 6, ra2 + 1, r2);
		setBounds(ID + 6, 87, 92, 7, hover);
		addRuneText(ID + 7, ra3 + 1, r3);
		setBounds(ID + 7, 142, 92, 8, hover);
	}

	public static void addBobStorage(int index) {
		RSInterface rsi = interfaceCache[index] = new RSInterface();
		rsi.itemActions = new String[5];
		rsi.spritesX = new int[20];
		rsi.invStackSizes = new int[30];
		rsi.inv = new int[30];
		rsi.spritesY = new int[20];

		rsi.children = new int[0];
		rsi.childX = new int[0];
		rsi.childY = new int[0];

		rsi.itemActions[0] = "Take 1";
		rsi.itemActions[1] = "Take 5";
		rsi.itemActions[2] = "Take 10";
		rsi.itemActions[3] = "Take All";
		rsi.centerText = false;
		rsi.aBoolean227 = false;
		rsi.aBoolean235 = false;
		rsi.usableItemInterface = false;
		rsi.isInventoryInterface = false;
		// rsi.aBoolean251 = false;
		rsi.aBoolean259 = true;
		// rsi.interfaceShown = false;
		rsi.textShadow = false;
		rsi.type = -1;
		rsi.invSpritePadX = 24;
		rsi.invSpritePadY = 24;
		rsi.height = 5;
		rsi.width = 6;
		rsi.parentID = 2702;
		rsi.id = 2700;
		rsi.type = 2;
	}

	public static void bobInterface() {
		RSInterface rsi = addTabInterface(2700);
		addSprite(2701, 20, "Interfaces/SummonTab/SUMMON");
		addBobStorage(2702);
		addHoverButton(2703, "Interfaces/Equipment/SPRITE", 1, 21, 21, "Close", 250, 2704, 3);
		addHoveredButton(2704, "Interfaces/Equipment/SPRITE", 3, 21, 21, 2705);
		rsi.totalChildren(4);
		rsi.child(0, 2701, 90, 14);
		rsi.child(1, 2702, 100, 56);
		rsi.child(2, 2703, 431, 23);
		rsi.child(3, 2704, 431, 23);
	}

	public static void configureLunar() {
		homeTeleport();
		drawRune(30003, 1, "Fire");
		drawRune(30004, 2, "Water");
		drawRune(30005, 3, "Air");
		drawRune(30006, 4, "Earth");
		drawRune(30007, 5, "Mind");
		drawRune(30008, 6, "Body");
		drawRune(30009, 7, "Death");
		drawRune(30010, 8, "Nature");
		drawRune(30011, 9, "Chaos");
		drawRune(30012, 10, "Law");
		drawRune(30013, 11, "Cosmic");
		drawRune(30014, 12, "Blood");
		drawRune(30015, 13, "Soul");
		drawRune(30016, 14, "Astral");
		addLunar3RunesSmallBox(30017, 9075, 554, 555, 0, 4, 3, 30003, 30004, 64, "Bake Pie",
				"Bake pies without a stove", 0, 16, 2);
		addLunar2RunesSmallBox(30025, 9075, 557, 0, 7, 30006, 65, "Cure Plant", "Cure disease on farming patch", 1, 4,
				2);
		addLunar3RunesBigBox(30032, 9075, 564, 558, 0, 0, 0, 30013, 30007, 65, "Monster Examine",
				"Detect the combat statistics of a\\nmonster", 2, 2, 2);
		addLunar3RunesSmallBox(30040, 9075, 564, 556, 0, 0, 1, 30013, 30005, 66, "Mob Contact",
				"Speak with varied NPCs", 3, 0, 2);
		addLunar3RunesSmallBox(30048, 9075, 563, 557, 0, 0, 9, 30012, 30006, 67, "Cure Other", "Cure poisoned players",
				4, 8, 2);
		addLunar3RunesSmallBox(30056, 9075, 555, 554, 0, 2, 0, 30004, 30003, 67, "Humidify",
				"fills certain vessels with water", 5, 0, 5);
		addLunar3RunesSmallBox(30064, 9075, 563, 557, -1, -1, -1, 30012, 30006, 68, "Skill Teleport", "Skill Teleport",
				6, 0, 5);
		addLunar3RunesBigBox(30075, 9075, 563, 557, -1, -1, -1, 30012, 30006, 69, "Minigames", "Barrows & Assault", 7,
				0, 5);
		addLunar3RunesSmallBox(30083, 9075, 563, 557, -1, -1, -1, 30012, 30006, 70, "Boss Teleport", "Boss Teleport",
				8, 0, 5);
		addLunar3RunesSmallBox(30091, 9075, 564, 563, 1, 1, 0, 30013, 30012, 70, "Cure Me", "Cures Poison", 9, 0, 5);
		addLunar2RunesSmallBox(30099, 9075, 557, 1, 1, 30006, 70, "Hunter Kit", "Get a kit of hunting gear", 10, 0, 5);
		addLunar3RunesSmallBox(30106, 9075, 563, 555, -1, -1, -1, 30012, 30004, 71, "Pk Teleport",
				"Gives your several of options", 11, 0, 5);
		addLunar3RunesBigBox(30114, 9075, 563, 555, -1, -1, -1, 30012, 30004, 72, "Monster Teleport",
				"Monster Teleport", 12, 0, 5);
		addLunar3RunesSmallBox(30122, 9075, 564, 563, 1, 1, 1, 30013, 30012, 73, "Cure Group",
				"Cures Poison on players", 13, 0, 5);
		addLunar3RunesBigBox(30130, 9075, 564, 559, 1, 1, 4, 30013, 30008, 74, "Stat Spy",
				"Cast on another player to see their\\nskill levels", 14, 8, 2);
		addLunar3RunesBigBox(30138, 9075, 563, 554, -1, -1, -1, 30012, 30003, 74, "Barbarian Teleport",
				"Teleports you to the Barbarian\\noutpost", 15, 0, 5);
		addLunar3RunesBigBox(30146, 9075, 563, 554, 1, 1, 5, 30012, 30003, 75, "Tele Group Barbarian",
				"Teleports players to the Barbarian\\noutpost", 16, 0, 5);
		addLunar3RunesSmallBox(30154, 9075, 554, 556, 1, 5, 9, 30003, 30005, 76, "Superglass Make",
				"Make glass without a furnace", 17, 16, 2);
		addLunar3RunesSmallBox(30162, 9075, 563, 555, 1, 1, 3, 30012, 30004, 77, "Khazard Teleport",
				"Teleports you to Port khazard", 18, 0, 5);
		addLunar3RunesSmallBox(30170, 9075, 563, 555, 1, 1, 7, 30012, 30004, 78, "Tele Group Khazard",
				"Teleports players to Port khazard", 19, 0, 5);
		addLunar3RunesBigBox(30178, 9075, 564, 559, 1, 0, 4, 30013, 30008, 78, "Dream",
				"Take a rest and restore hitpoints 3\\n times faster", 20, 0, 5);
		addLunar3RunesSmallBox(30186, 9075, 557, 555, 1, 9, 4, 30006, 30004, 79, "String Jewellery",
				"String amulets without wool", 21, 0, 5);
		addLunar3RunesLargeBox(30194, 9075, 557, 555, 1, 9, 9, 30006, 30004, 80, "Stat Restore Pot\\nShare",
				"Share a potion with up to 4 nearby\\nplayers", 22, 0, 5);
		addLunar3RunesSmallBox(30202, 9075, 554, 555, 1, 6, 6, 30003, 30004, 81, "Magic Imbue",
				"Combine runes without a talisman", 23, 0, 5);
		addLunar3RunesBigBox(30210, 9075, 561, 557, 2, 1, 14, 30010, 30006, 82, "Fertile Soil",
				"Fertilise a farming patch with super\\ncompost", 24, 4, 2);
		addLunar3RunesBigBox(30218, 9075, 557, 555, 2, 11, 9, 30006, 30004, 83, "Boost Potion Share",
				"Shares a potion with up to 4 nearby\\nplayers", 25, 0, 5);
		addLunar3RunesSmallBox(30226, 9075, 563, 555, 2, 2, 9, 30012, 30004, 84, "Fishing Guild Teleport",
				"Teleports you to the fishing guild", 26, 0, 5);
		addLunar3RunesLargeBox(30234, 9075, 563, 555, 1, 2, 13, 30012, 30004, 85, "Tele Group Fishing\\nGuild",
				"Teleports players to the Fishing\\nGuild", 27, 0, 5);
		addLunar3RunesSmallBox(30242, 9075, 557, 561, 2, 14, 0, 30006, 30010, 85, "Plank Make",
				"Turn Logs into planks", 28, 16, 5);
		addLunar3RunesSmallBox(30250, 9075, 563, 555, 2, 2, 9, 30012, 30004, 86, "Catherby Teleport",
				"Teleports you to Catherby", 29, 0, 5);
		addLunar3RunesSmallBox(30258, 9075, 563, 555, 2, 2, 14, 30012, 30004, 87, "Tele Group Catherby",
				"Teleports players to Catherby", 30, 0, 5);
		addLunar3RunesSmallBox(30266, 9075, 563, 555, 2, 2, 7, 30012, 30004, 88, "Ice Plateau Teleport",
				"Teleports you to Ice Plateau", 31, 0, 5);
		addLunar3RunesBigBox(30274, 9075, 563, 555, 2, 2, 15, 30012, 30004, 89, "Tele Group Ice\\n Plateau",
				"Teleports players to Ice Plateau", 32, 0, 5);
		addLunar3RunesBigBox(30282, 9075, 563, 561, 2, 1, 0, 30012, 30010, 90, "Energy Transfer",
				"Spend hitpoints and SA Energy to\\n give another player hitpoints and run energy", 33, 8, 2);
		addLunar3RunesBigBox(30290, 9075, 563, 565, 2, 2, 0, 30012, 30014, 91, "Heal Other",
				"Transfer up to 75% of hitpoints\\n to another player", 34, 8, 2);
		addLunar3RunesBigBox(30298, 9075, 560, 557, 2, 1, 9, 30009, 30006, 92, "Vengeance Other",
				"Allows another player to rebound\\ndamage to an opponent", 35, 8, 2);
		addLunar3RunesSmallBox(30306, 9075, 560, 557, 3, 1, 9, 30009, 30006, 93, "Vengeance",
				"Rebound damage to an opponent", 36, 0, 5);
		addLunar3RunesBigBox(30314, 9075, 565, 563, 3, 2, 5, 30014, 30012, 94, "Heal Group",
				"Transfer up to 75% of hitpoints to a group", 37, 0, 5);
		addLunar3RunesBigBox(30322, 9075, 564, 563, 2, 1, 0, 30013, 30012, 95, "Spellbook Swap",
				"Change to another spellbook for 1\\nspell cast", 38, 0, 5);
	}

	public static void constructLunar() {
		RSInterface Interface = addTabInterface(29999);
		setChildren(80, Interface);
		setBounds(30000, 11, 10, 0, Interface);
		setBounds(30017, 40, 9, 1, Interface);
		setBounds(30025, 71, 12, 2, Interface);
		setBounds(30032, 103, 10, 3, Interface);
		setBounds(30040, 135, 12, 4, Interface);
		setBounds(30048, 165, 10, 5, Interface);
		setBounds(30056, 8, 38, 6, Interface);
		setBounds(30064, 39, 39, 7, Interface);
		setBounds(30075, 71, 39, 8, Interface);
		setBounds(30083, 103, 39, 9, Interface);
		setBounds(30091, 135, 39, 10, Interface);
		setBounds(30099, 165, 37, 11, Interface);
		setBounds(30106, 12, 68, 12, Interface);
		setBounds(30114, 42, 68, 13, Interface);
		setBounds(30122, 71, 68, 14, Interface);
		setBounds(30130, 103, 68, 15, Interface);
		setBounds(30138, 135, 68, 16, Interface);
		setBounds(30146, 165, 68, 17, Interface);
		setBounds(30154, 14, 97, 18, Interface);
		setBounds(30162, 42, 97, 19, Interface);
		setBounds(30170, 71, 97, 20, Interface);
		setBounds(30178, 101, 97, 21, Interface);
		setBounds(30186, 135, 98, 22, Interface);
		setBounds(30194, 168, 98, 23, Interface);
		setBounds(30202, 11, 125, 24, Interface);
		setBounds(30210, 42, 124, 25, Interface);
		setBounds(30218, 74, 125, 26, Interface);
		setBounds(30226, 103, 125, 27, Interface);
		setBounds(30234, 135, 125, 28, Interface);
		setBounds(30242, 164, 126, 29, Interface);
		setBounds(30250, 10, 155, 30, Interface);
		setBounds(30258, 42, 155, 31, Interface);
		setBounds(30266, 71, 155, 32, Interface);
		setBounds(30274, 103, 155, 33, Interface);
		setBounds(30282, 136, 155, 34, Interface);
		setBounds(30290, 165, 155, 35, Interface);
		setBounds(30298, 13, 185, 36, Interface);
		setBounds(30306, 42, 185, 37, Interface);
		setBounds(30314, 71, 184, 38, Interface);
		setBounds(30322, 104, 184, 39, Interface);
		setBounds(30001, 6, 184, 40, Interface);
		setBounds(30018, 5, 176, 41, Interface);
		setBounds(30026, 5, 176, 42, Interface);
		setBounds(30033, 5, 163, 43, Interface);
		setBounds(30041, 5, 176, 44, Interface);
		setBounds(30049, 5, 176, 45, Interface);
		setBounds(30057, 5, 176, 46, Interface);
		setBounds(30065, 5, 176, 47, Interface);
		setBounds(30076, 5, 163, 48, Interface);
		setBounds(30084, 5, 176, 49, Interface);
		setBounds(30092, 5, 176, 50, Interface);
		setBounds(30100, 5, 176, 51, Interface);
		setBounds(30107, 5, 176, 52, Interface);
		setBounds(30115, 5, 163, 53, Interface);
		setBounds(30123, 5, 176, 54, Interface);
		setBounds(30131, 5, 163, 55, Interface);
		setBounds(30139, 5, 163, 56, Interface);
		setBounds(30147, 5, 163, 57, Interface);
		setBounds(30155, 5, 176, 58, Interface);
		setBounds(30163, 5, 176, 59, Interface);
		setBounds(30171, 5, 176, 60, Interface);
		setBounds(30179, 5, 163, 61, Interface);
		setBounds(30187, 5, 176, 62, Interface);
		setBounds(30195, 5, 149, 63, Interface);
		setBounds(30203, 5, 176, 64, Interface);
		setBounds(30211, 5, 163, 65, Interface);
		setBounds(30219, 5, 163, 66, Interface);
		setBounds(30227, 5, 176, 67, Interface);
		setBounds(30235, 5, 149, 68, Interface);
		setBounds(30243, 5, 176, 69, Interface);
		setBounds(30251, 5, 5, 70, Interface);
		setBounds(30259, 5, 5, 71, Interface);
		setBounds(30267, 5, 5, 72, Interface);
		setBounds(30275, 5, 5, 73, Interface);
		setBounds(30283, 5, 5, 74, Interface);
		setBounds(30291, 5, 5, 75, Interface);
		setBounds(30299, 5, 5, 76, Interface);
		setBounds(30307, 5, 5, 77, Interface);
		setBounds(30323, 5, 5, 78, Interface);
		setBounds(30315, 5, 5, 79, Interface);
	}

	public static void setBounds(int ID, int X, int Y, int frame, RSInterface RSinterface) {
		RSinterface.children[frame] = ID;
		RSinterface.childX[frame] = X;
		RSinterface.childY[frame] = Y;
	}

	public static void addButton(int i, int j, String name, int W, int H, String S, int AT) {
		RSInterface RSInterface = addInterface(i);
		RSInterface.id = i;
		RSInterface.parentID = i;
		RSInterface.type = 5;
		RSInterface.atActionType = AT;
		RSInterface.contentType = 0;
		RSInterface.opacity = 0;
		RSInterface.mouseOverPopupInterface = 52;
		RSInterface.width = W;
		RSInterface.height = H;
		RSInterface.tooltip = S;
	}

	public static void addHDHoverButton(int i, String imageName, int j, int width, int height, String text,
			int contentType, int hoverOver, int aT) {// hoverable button
		RSInterface tab = addTabInterface(i);
		tab.id = i;
		tab.parentID = i;
		tab.type = 12;
		tab.atActionType = aT;
		tab.contentType = contentType;
		tab.opacity = 0;
		tab.mouseOverPopupInterface = hoverOver;

		tab.width = width;
		tab.height = height;
		tab.tooltip = text;
	}

	public static void addHDHoveredButton(int i, String imageName, int j, int w, int h, int IMAGEID) {// hoverable
																										// button
		RSInterface tab = addTabInterface(i);
		tab.parentID = i;
		tab.id = i;
		tab.type = 0;
		tab.atActionType = 0;
		tab.width = w;
		tab.height = h;
		tab.isMouseoverTriggered = true;
		tab.opacity = 0;
		tab.mouseOverPopupInterface = -1;
		tab.scrollMax = 0;
		addHDHoverImage(IMAGEID, j, j, imageName);
		tab.totalChildren(1);
		tab.child(0, IMAGEID, 0, 0);
	}

	public static void addHDHoverImage(int i, int j, int k, String name) {
		RSInterface tab = addTabInterface(i);
		tab.id = i;
		tab.parentID = i;
		tab.type = 12;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.width = 512;
		tab.height = 334;
		tab.opacity = 0;
		tab.mouseOverPopupInterface = 52;
	}

	public static void addHDSprite(int id, String spriteName, String T, int H, int X, int Y) {
		RSInterface rsi = addTabInterface(id);
		rsi.id = id;
		rsi.parentID = id;
		rsi.type = 12;
		rsi.atActionType = 1;
		rsi.contentType = -1;
		rsi.opacity = (byte) 0;
		rsi.mouseOverPopupInterface = 52;
		rsi.tooltip = T;
		rsi.mouseOverPopupInterface = H;
		rsi.width = X;
		rsi.height = Y;
	}

	public RSInterface() {
		itemSpriteId1 = -1;
		itemSpriteId2 = -1;
		itemSpriteZoom1 = -1;
		itemSpriteZoom2 = -1;
		itemSpriteIndex = 0;
	}

	public static int boxIds[] = { 4041, 4077, 4113, 4047, 4083, 4119, 4053, 4089, 4125, 4059, 4095, 4131, 4065, 4101,
			4137, 4071, 4107, 4143, 4154, 12168, 13918 };
	public String popupString;
	public String hoverText;
	public boolean drawsTransparent;
	public int anInt208;
	public static RSInterface interfaceCache[];
	public int requiredValues[];
	public int contentType;
	public int spritesX[];
	public int textHoverColour;
	public int atActionType;
	public String spellName;
	public int anInt219;
	public int width;
	public String tooltip;
	public String selectedActionName;
	public boolean centerText;
	public int scrollLocation;
	public String itemActions[];
	public int valueIndexArray[][];
	public boolean aBoolean227;
	public String disabledText;
	public int mouseOverPopupInterface;
	public int invSpritePadX;
	public int textColor;
	public int anInt233;
	public int mediaID;
	public boolean aBoolean235;
	public int parentID;
	public int spellUsableOn;
	public int anInt239;
	public int children[];
	public int childX[];
	public boolean usableItemInterface;
	public int invSpritePadY;
	public int valueCompareType[];
	public int anInt246;
	public int spritesY[];
	public String message;
	public boolean isInventoryInterface;
	public int id;
	public int invStackSizes[];
	public int inv[];
	public byte opacity;
	public int anInt255;
	public int anInt256;
	public int anInt257;
	public int anInt258;
	public boolean aBoolean259;
	public int scrollMax;
	public int type;
	public int anInt263;
	public int anInt265;
	public boolean isMouseoverTriggered;
	public int height;
	public boolean textShadow;
	public int modelZoom;
	public int modelRotation1;
	public int modelRotation2;
	public int childY[];
	public int itemSpriteId1;
	public int itemSpriteId2;
	public int itemSpriteZoom1;
	public int itemSpriteZoom2;
	public int itemSpriteIndex;
	public boolean greyScale;
	private static int summoningLevelRequirements[] = { 1, 4, 10, 13, 16, 17, 18, 19, 22, 23, 25, 28, 29, 31, 32, 33,
			34, 34, 34, 34, 36, 40, 41, 42, 43, 43, 43, 43, 43, 43, 43, 46, 46, 47, 49, 52, 54, 55, 56, 56, 57, 57, 57,
			58, 61, 62, 63, 64, 66, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 76, 77, 78, 79, 79, 79, 80, 83, 83, 85,
			86, 88, 89, 92, 93, 95, 96, 99 };
	private static int pouchItems[] = { 12047, 12043, 12059, 12019, 12009, 12778, 12049, 12055, 12808, 12067, 12063,
			12091, 12800, 12053, 12065, 12021, 12818, 12780, 12798, 12814, 12073, 12087, 12071, 12051, 12095, 12097,
			12099, 12101, 12103, 12105, 12107, 12075, 12816, 12041, 12061, 12007, 12035, 12027, 12077, 12531, 12810,
			12812, 12784, 12023, 12085, 12037, 12015, 12045, 12079, 12123, 12031, 12029, 12033, 12820, 12057, 14623,
			12792, 12069, 12011, 12081, 12782, 12794, 12013, 12802, 12804, 12806, 12025, 12017, 12788, 12776, 12083,
			12039, 12786, 12089, 12796, 12822, 12093, 12790 };
	private static int scrollItems[] = { 12425, 12445, 12428, 12459, 12533, 12838, 12460, 12432, 12839, 12430, 12446,
			12440, 12834, 12447, 12433, 12429, 12443, 12443, 12443, 12443, 12461, 12431, 12422, 12448, 12458, 12458,
			12458, 12458, 12458, 12458, 12458, 12462, 12829, 12426, 12444, 12441, 12454, 12453, 12463, 12424, 12835,
			12836, 12840, 12455, 12468, 12427, 12436, 12467, 12464, 12452, 12439, 12438, 12423, 12830, 12451, 14622,
			12826, 12449, 12450, 12465, 12841, 12831, 12457, 12824, 12824, 12824, 12442, 12456, 12837, 12832, 12466,
			12434, 12833, 12437, 12827, 12828, 12435, 12825 };
	private static String scrollNames[] = { "Howl", "Dreadfowl Strike", "Egg Spawn", "Slime Spray", "Stony Shell",
			"Pester", "Electric Lash", "Venom Shot", "Fireball Assault", "Cheese Feast", "Sandstorm",
			"Generate Compost", "Explode", "Vampire Touch", "Insane Ferocity", "Multichop", "Call of Arms",
			"Call of Arms", "Call of Arms", "Call of Arms", "Bronze Bull Rush", "Unburden", "Herbcall", "Evil Flames",
			"Petrifying gaze", "Petrifying gaze", "Petrifying gaze", "Petrifying gaze", "Petrifying gaze",
			"Petrifying gaze", "Petrifying gaze", "Iron Bull Rush", "Immense Heat", "Thieving Fingers", "Blood Drain",
			"Tireless Run", "Abyssal Drain", "Dissolve", "Steel Bull Rush", "Fish Rain", "Goad", "Ambush", "Rending",
			"Doomsphere Device", "Dust Cloud", "Abyssal Stealth", "Ophidian Incubation", "Poisonous Blast",
			"Mithril Bull Rush", "Toad Bark", "Testudo", "Swallow Whole", "Fruitfall", "Famine", "Arctic Blast",
			"Rise from the Ashes", "Volcanic Strength", "Crushing Claw", "Mantis Strike", "Adamant Bull Rush",
			"Inferno", "Deadly Claw", "Acorn Missile", "Titan's Consitution", "Titan's Consitution",
			"Titan's Consitution", "Regrowth", "Spike Shot", "Ebon Thunder", "Swamp Plague", "Rune Bull Rush",
			"Healing Aura", "Boil", "Magic Focus", "Essence Shipment", "Iron Within", "Winter Storage",
			"Steel of Legends" };
	private static String pouchNames[] = { "Spirit wolf", "Dreadfowl", "Spirit spider", "Thorny snail", "Granite crab",
			"Spirit mosquito", "Desert wyrm", "Spirit scorpion", "Spirit tz-kih", "Albino rat", "Spirit kalphite",
			"Compost mound", "Giant chinchompa", "Vampire bat", "Honey badger", "Beaver", "Void ravager",
			"Void spinner", "Void torcher", "Void shifter", "Bronze minotaur", "Bull ant", "Macaw", "Evil turnip",
			"Sp. cockatrice", "Sp. guthatrice", "Sp. saratrice", "Sp. zamatrice", "Sp. pengatrice", "Sp. coraxatrice",
			"Sp. vulatrice", "Iron minotaur", "Pyrelord", "Magpie", "Bloated leech", "Spirit terrorbird",
			"Abyssal parasite", "Spirit jelly", "Steel minotaur", "Ibis", "Spirit graahk", "Spirit kyatt",
			"Spirit larupia", "Karam. overlord", "Smoke devil", "Abyssal lurker", "Spirit cobra", "Stranger plant",
			"Mithril minotaur", "Barker toad", "War tortoise", "Bunyip", "Fruit bat", "Ravenous locust", "Arctic bear",
			"Phoenix", "Obsidian golem", "Granite lobster", "Praying mantis", "Adamant minotaur", "Forge regent",
			"Talon beast", "Giant ent", "Fire titan", "Moss titan", "Ice titan", "Hydra", "Spirit dagannoth",
			"Lava titan", "Swamp titan", "Rune minotaur", "Unicorn stallion", "Geyser titan", "Wolpertinger",
			"Abyssal titan", "Iron titan", "Pack yak", "Steel titan" };

}
