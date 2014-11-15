package org.endeavor.game.content.io;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.endeavor.engine.utility.Misc;
import org.endeavor.engine.utility.NameUtil;
import org.endeavor.engine.utility.TimeStamp;
import org.endeavor.game.content.combat.spawning.Loadout;
import org.endeavor.game.content.skill.farming.GrassyPatch;
import org.endeavor.game.content.skill.farming.Plant;
import org.endeavor.game.content.skill.prayer.impl.CursesPrayerBook;
import org.endeavor.game.content.skill.prayer.impl.DefaultPrayerBook;
import org.endeavor.game.content.skill.slayer.Slayer;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Equipment;
import org.endeavor.game.entity.player.ItemDegrading;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerConstants;

public final class PlayerSave {
	public static final Gson GSON = new Gson();

	public static synchronized boolean load(Player p) throws Exception {
		if (!PlayerDetails.loadDetails(p)) {
			return false;
		}

		if (!PlayerContainer.loadDetails(p)) {
			return false;
		}

		return true;
	}

	public static synchronized final void save(Player p) {
		try {
			new PlayerDetails(p).parseDetails();
			new PlayerContainer(p).parseDetails(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final class PlayerContainer {
		private final Item[] bank;
		private final Item[] inventory;
		private final Item[] equipment;
		private final Item[] bobInventory;
		private final int donationPoints;
		/*private final int premiumDay;
		private final int premiumYear;
		private final int premiumDays;*/
		
		/*private final Item[] shopItems;
		private final int[] shopPrices;*/
		
		

		public PlayerContainer(Player player) {
			bank = player.getBank().getItems();
			inventory = player.getInventory().getItems();
			equipment = player.getEquipment().getItems();
			bobInventory = (player.getSummoning().isFamilarBOB() ? player.getSummoning().getContainer().getItems()
					: null);
			
			
			donationPoints = player.getDonationPoints();
			
			/*premiumDays = player.getPremiumDays();
			premiumDay = player.getPremiumDay();
			premiumYear = player.getPremiumYear();*/
			
			/*shopItems = player.getPlayerShop().getItems();
			shopPrices = player.getPlayerShop().getPrices();*/
		}

		public void parseDetails(Player player) throws IOException {
			BufferedWriter writer = new BufferedWriter(new FileWriter("./data/characters/containers/"
					+ player.getUsername() + ".json", false));
			try {
				writer.write(PlayerSave.GSON.toJson(this));
				writer.flush();
			} finally {
				writer.close();
			}
		}

		public static boolean loadDetails(Player player) throws Exception {
			File file = new File("./data/characters/containers/" + player.getUsername() + ".json");

			if (!file.exists()) {
				return false;
			}

			BufferedReader reader = new BufferedReader(new FileReader(file));
			try {
				PlayerContainer details = PlayerSave.GSON.fromJson(reader, PlayerContainer.class);

				/*player.setPremiumDays(details.premiumDays);
				player.setPremiumDay(details.premiumDay);
				player.setPremiumYear(details.premiumYear);*/
				
				/*if (details.shopItems != null) {
					player.getPlayerShop().setItems(details.shopItems);
				}

				if (details.shopPrices != null) {
					player.getPlayerShop().setPrices(details.shopPrices);
				}*/
				
				if (details.bank != null) {
					for (int i = 0; i < 1080; i++) {
						if (i >= 1080) {
							break;
						}
						if (i >= details.bank.length) {
							break;
						}
						player.getBank().getItems()[i] = ItemCheck.check(player, details.bank[i]);
					}
				}

				if (details.equipment != null) {
					for (int i = 0; i < details.equipment.length; i++) {
						player.getEquipment().getItems()[i] = ItemCheck.check(player, details.equipment[i]);
					}
				}

				if (details.inventory != null) {
					for (int i = 0; i < details.inventory.length; i++) {
						player.getInventory().getItems()[i] = ItemCheck.check(player, details.inventory[i]);
					}
				}

				player.getBank().shiftAll();

				if (details.bobInventory != null) {
					player.getAttributes().set("summoningbobinventory", details.bobInventory);
				}

				player.setLastLoginDay(Misc.getDayOfYear());
				player.setLastLoginYear(Misc.getYear());
				
				player.setDonationPoints(details.donationPoints);
			} finally {
				if (reader != null) {
					reader.close();
				}
			}

			return true;
		}
	}

	public static final class PlayerDetails {
		private final String username;
		private final String password;
		private final int x;
		private final int y;
		private final int z;
		private final byte rights;
		private final boolean banned;
		private final int banLength;
		private final int banDay;
		private final int banYear;
		private final boolean muted;
		private final int muteLength;
		private final int muteDay;
		private final int muteYear;
		private final int fightCavesWave;
		private final int magicBook;
		private final int prayerBook;
		private final boolean retaliate;
		private final boolean expLock;
		private final boolean[] barrowsKilled;
		private final int ep;
		private final int damage;
		private final int pkp;
		private final short[] gwkc;
		private final boolean poisoned;
		private final int poisonDmg;
		private final String slayerTask;
		private final byte slayerAmount;
		private final Slayer.SlayerDifficulty slayerDifficulty;
		private final boolean petTut;
		private final short[] skillsLevel;
		private final double[] experience;
		private final byte gender;
		private final int[] appearance;
		private final byte[] colours;
		private final long left;
		private final int skullIcon;
		private final byte bright;
		private final byte multipleMouse;
		private final byte chatEffects;
		private final byte splitPrivate;
		private final byte acceptAid;
		private final String[] friends;
		private final String[] ignores;
		private final byte[] questStages;
		private final byte[] activeQuests;
		private final int dungPoints;
		private final int yearCreated;
		private final int dayCreated;
		private final int recoilStage;
		private final int spec;
		private final Equipment.AttackStyles attackStyle;
		private final double energy;
		private final int lastLoginDay;
		private final int lastLoginYear;
		private final String host;
		private final int votePoints;
		private final ItemDegrading.DegradedItem[] degrading;
		private final byte dragonFireShieldCharges;
		private final int slayerPoints;
		private final int musicVolume;
		private final int soundVolume;
		private final TimeStamp lastVote;
		private final Item[] savedArrows;
		private final int kills;
		private final int deaths;
		private final boolean betaTester;
		private final Plant[] plants;
		private final GrassyPatch[] patches;
		private final int skillPoints;
		private final boolean running;
		private final boolean logPackets;
		private final String[][] achievements;
		private final int summoningTime;
		private final int summoningSpecialAmount;
		private final int summoningFamiliar;
		private final boolean summoningAttack;
		private final int pestPoints;
		private final int totalVotes;
		private final int teleblockTime;
		private final int blackMarks;
		private final long[] killOrKilledBy;
		private final String[] titles;
		private final String title;
		private final double rareDropEP;
		private final byte[] quickPrayersDefault;
		private final byte[] quickPrayersCurses;
		private final int rareDropsReceived;
		private final boolean inKiln;
		private final Loadout[] customSpawnLodeouts;
		/**
		 * XXX: check for nulls if you have to
		 */
		
		public PlayerDetails(Player player) {
			username = player.getUsername();
			password = player.getPassword();
			x = player.getLocation().getX();
			y = player.getLocation().getY();
			z = player.getLocation().getZ();
			rights = ((byte) player.getRights());
			banned = player.isBanned();
			banLength = player.getBanLength();
			banDay = player.getBanDay();
			banYear = player.getBanYear();
			muted = player.isMuted();
			muteLength = player.getMuteLength();
			muteDay = player.getMuteDay();
			muteYear = player.getMuteYear();
			fightCavesWave = player.getJadDetails().getStage();
			magicBook = player.getMagic().getMagicBook();
			prayerBook = player.getPrayerInterface();
			retaliate = player.isRetaliate();
			expLock = player.getSkill().isExpLocked();
			barrowsKilled = player.getMinigames().getBarrowsKilled();
			ep = player.getEarningPotential().getEP();
			damage = player.getEarningPotential().getDamage();
			pkp = player.getEarningPotential().getPkp();
			gwkc = player.getMinigames().getGWKC();
			
			customSpawnLodeouts = player.getCustomSpawnLoadouts();
			
			quickPrayersCurses = player.getQuickPrayersCurses();
			quickPrayersDefault = player.getQuickPrayersDefault();
			
			rareDropEP = player.getRareDropEP().getEp();
			
			rareDropsReceived = player.getRareDropEP().getReceived();
			
			blackMarks = player.getBlackMarks();
			
			killOrKilledBy = player.getEarningPotential().getKillOrKilledBy();
			
			title = player.getTitle();
			
			inKiln = player.getJadDetails().isKiln();

			poisoned = player.isPoisoned();
			poisonDmg = player.getPoisonDamage();
			slayerTask = player.getSlayer().getTask();
			slayerAmount = player.getSlayer().getAmount();
			petTut = player.getPets().hadPetTut();
			experience = player.getSkill().getExperience();
			skillsLevel = player.getSkill().getLevels();
			gender = player.getGender();
			appearance = (player.getAppearance().clone());
			colours = (player.getColors().clone());
			left = player.getSkulling().getLeft();
			skullIcon = player.getSkulling().getSkullIcon();
			spec = player.getSpecialAttack().getAmount();
			attackStyle = player.getEquipment().getAttackStyle();
			energy = player.getRunEnergy().getEnergy();
			votePoints = player.getVotePoints();
			
			titles = player.getTitles().getTitles();
			
			totalVotes = player.getTotalVotes();
			
			teleblockTime = player.getTeleblockTime();

			summoningAttack = player.getSummoning().isAttack();
			summoningTime = player.getSummoning().getTime();
			summoningSpecialAmount = player.getSummoning().getSpecialAmount();
			summoningFamiliar = (player.getSummoning().getFamiliarData() != null ? player.getSummoning()
					.getFamiliarData().mob : -1);

			logPackets = player.getClient().isLogPlayer();

			running = player.getRunEnergy().isRunning();

			achievements = player.getAchievements().getData();

			pestPoints = player.getPestPoints();
			
			skillPoints = player.getSkillPoints();

			soundVolume = player.getSoundVolume();

			patches = player.getFarming().getPatches();

			betaTester = player.isBetaTester();

			plants = player.getFarming().getPlants();

			lastVote = player.getVoteTime();

			kills = player.getKills();
			deaths = player.getDeaths();

			musicVolume = player.getMusicVolume();

			dragonFireShieldCharges = player.getMagic().getDragonFireShieldCharges();

			host = player.getClient().getHost();

			degrading = player.getItemDegrading().getDegrading();

			lastLoginDay = player.getLastLoginDay();
			lastLoginYear = player.getLastLoginYear();

			yearCreated = player.getYearCreated();
			dayCreated = player.getDayCreated();

			slayerPoints = player.getSlayerPoints();
			slayerDifficulty = player.getSlayer().getCurrent();

			if (player.getAttributes().get("recoilhits") != null)
				recoilStage = player.getAttributes().getInt("recoilhits");
			else {
				recoilStage = -1;
			}

			bright = player.getScreenBrightness();
			multipleMouse = player.getMultipleMouseButtons();
			chatEffects = player.getChatEffectsEnabled();
			splitPrivate = player.getSplitPrivateChat();
			acceptAid = player.getAcceptAid();
			questStages = player.getQuesting().getQuestStages();
			dungPoints = player.getDungPoints();

			savedArrows = player.getRanged().getSavedArrows().getItems();

			if (player.getQuesting().getActiveQuests().size() > 0) {
				activeQuests = new byte[player.getQuesting().getActiveQuests().size()];

				for (int i = 0; i < player.getQuesting().getActiveQuests().size(); i++)
					activeQuests[i] = ((byte) player.getQuesting().getActiveQuests().get(i).getId());
			} else {
				activeQuests = null;
			}

			int k = 0;
			friends = new String[player.getPrivateMessaging().getFriends().size()];
			for (String i : player.getPrivateMessaging().getFriends()) {
				friends[k] = i;
				k++;
			}

			k = 0;
			ignores = new String[player.getPrivateMessaging().getIgnores().size()];
			for (String i : player.getPrivateMessaging().getIgnores()) {
				ignores[k] = i;
				k++;
			}
		}

		public void parseDetails() throws Exception {
			BufferedWriter writer = null;
			try {
				writer = new BufferedWriter(new FileWriter("./data/characters/details/" + username + ".json", false));
				writer.write(PlayerSave.GSON.toJson(this));
				writer.flush();
			} finally {
				if (writer != null)
					writer.close();
			}
		}

		public static boolean loadDetails(Player player) throws Exception {
			BufferedReader reader = null;
			try {
				File file = new File("./data/characters/details/" + NameUtil.uppercaseFirstLetter(player.getUsername()) + ".json");

				if (!file.exists()) {
					return false;
				}

				reader = new BufferedReader(new FileReader(file));

				PlayerDetails details = PlayerSave.GSON.fromJson(reader, PlayerDetails.class);
				player.setUsername(details.username);
				player.setPassword(details.password);
				player.getLocation().setAs(new Location(details.x, details.y, details.z));
				player.setRights(details.rights);
				player.setRetaliate(details.retaliate);
				player.getSkill().setExpLock(details.expLock);
				player.getEarningPotential().setEP(details.ep);
				player.getEarningPotential().setDamage(details.damage);
				player.getEarningPotential().setPkp(details.pkp);
				player.getSlayer().setAmount(details.slayerAmount);
				player.getSlayer().setTask(details.slayerTask);
				player.getPets().setPetTut(details.petTut);
				player.setPoisonDamage(details.poisonDmg);
				player.getSpecialAttack().setSpecialAmount(details.spec);
				player.getRunEnergy().setEnergy(details.energy);
				
				if (details.customSpawnLodeouts != null) {
					player.setCustomSpawnLoadouts(details.customSpawnLodeouts);
				}
				
				player.setTotalVotes(details.totalVotes);
				
				player.setTitle(details.title);

				player.getSummoning().setAttack(details.summoningAttack);
				player.getSummoning().setSpecial(details.summoningSpecialAmount);
				player.getSummoning().setTime(details.summoningTime);
				
				player.setPestPoints(details.pestPoints);
				
				if (details.titles != null) {
					player.getTitles().addTitlesFromArray(details.titles);
				}
				
				if (details.killOrKilledBy != null) {
					player.getEarningPotential().setKillOrKilledBy(details.killOrKilledBy);
				}
				
				if (details.summoningFamiliar != -1) {
					player.getAttributes().set("summoningfamsave", Integer.valueOf(details.summoningFamiliar));
				}

				player.getClient().setLogPlayer(details.logPackets);

				player.setBetaTester(details.betaTester);

				player.getRunEnergy().setRunning(details.running);

				player.setSkillPoints(details.skillPoints);
				
				player.setTeleblockTime(details.teleblockTime);
				
				player.getRareDropEP().setEp(details.rareDropEP);
				player.getRareDropEP().setReceived(details.rareDropsReceived);

				if (details.achievements != null) {
					player.getAchievements().setData(details.achievements);
				}

				if (details.lastVote != null) {
					player.setVoteTime(details.lastVote);
				}

				player.setKills((short) details.kills);
				player.setDeaths((short) details.deaths);

				player.setMusicVolume((byte) details.musicVolume);
				player.setSoundVolume((byte) details.soundVolume);

				player.setSlayerPoints(details.slayerPoints);
				
				player.setBlackMarks(details.blackMarks);

				player.getMagic().setDragonFireShieldCharges(details.dragonFireShieldCharges);

				if (details.degrading != null) {
					player.getItemDegrading().setDegrading(details.degrading);
				}

				if (details.savedArrows != null) {
					player.getRanged().getSavedArrows().setItems(details.savedArrows);
				}

				player.setVotePoints(details.votePoints);

				if (details.attackStyle != null) {
					player.getEquipment().setAttackStyle(details.attackStyle);
				}
				if (details.recoilStage != -1) {
					player.getAttributes().set("recoilhits", Integer.valueOf(details.recoilStage));
				}

				if (details.plants != null) {
					player.getFarming().setPlants(details.plants);
				}

				if (details.patches != null) {
					player.getFarming().setPatches(details.patches);
				}
				
				player.getSkulling().setLeft(details.left);

				if (player.getSkulling().isSkulled()) {
					player.getSkulling().setSkullIcon(player, details.skullIcon);
				}

				if (details.host != null) {
					player.getClient().setHost(details.host);
				}

				if (details.slayerDifficulty != null) {
					player.getSlayer().setCurrent(details.slayerDifficulty);
				}

				player.setYearCreated(details.yearCreated);
				player.setDayCreated(details.dayCreated);

				player.setLastLoginDay(details.lastLoginDay);
				player.setLastLoginYear(details.lastLoginYear);

				player.setScreenBrightness(details.bright);
				player.setMultipleMouseButtons(details.multipleMouse);
				player.setChatEffects(details.chatEffects);
				player.setSplitPrivateChat(details.splitPrivate);
				player.setAcceptAid(details.acceptAid);

				player.getJadDetails().setStage(details.fightCavesWave);
				player.getJadDetails().setKiln(details.inKiln);

				player.setDungPoints(details.dungPoints);

				if (details.questStages != null) {
					for (int i = 0; i < details.questStages.length; i++) {
						player.getQuesting().getQuestStages()[i] = details.questStages[i];
					}
				}

				if (details.activeQuests != null) {
					for (byte i : details.activeQuests) {
						player.getQuesting().setQuestActive(
								org.endeavor.game.content.quest.QuestConstants.QUESTS_BY_ID[i], true);
					}

				}

				if (details.friends != null) {
					for (String i : details.friends) {
						player.getPrivateMessaging().getFriends().add(i);
					}
				}

				if (details.ignores != null) {
					for (String i : details.ignores) {
						player.getPrivateMessaging().getIgnores().add(i);
					}
				}

				if ((details.poisonDmg > 0) && (details.poisoned)) {
					player.poison(details.poisonDmg);
				}

				player.setGender(details.gender);

				if (details.appearance != null) {
					for (int i = 0; i < details.appearance.length; i++)
						player.getAppearance()[i] = details.appearance[i];
				}
				if (details.colours != null) {
					for (int i = 0; i < details.colours.length; i++)
						player.getColors()[i] = details.colours[i];
				}
				if (details.experience != null) {
					for (int i = 0; i < details.experience.length; i++) {
						player.getSkill().getExperience()[i] = details.experience[i];
					}
				}
				if (details.skillsLevel != null) {
					for (int i = 0; i < details.skillsLevel.length; i++) {
						player.getLevels()[i] = details.skillsLevel[i];
					}
				}
				if (details.experience != null) {
					for (int i = 0; i < details.experience.length; i++) {
						player.getMaxLevels()[i] = player.getSkill().getLevelForExperience(i, details.experience[i]);
					}
				}

				if (details.gwkc != null) {
					player.getMinigames().setGWKC(details.gwkc);
				}

				boolean banned = details.banned;
				boolean muted = details.muted;

				if ((banned) && (!Misc.isExpired(details.banDay, details.banYear, details.banLength))) {
					player.setBanned(true);
				}

				if ((muted) && (!Misc.isExpired(details.muteDay, details.muteYear, details.muteLength))) {
					player.setMuted(true);
					player.setMuteDay(details.muteDay);
					player.setMuteYear(details.muteYear);
					player.setMuteLength(details.muteLength);
					player.setRemainingMute(details.muteLength - Misc.getElapsed(details.muteDay, details.muteYear));
				}

				if (details.magicBook > 0) {
					player.getMagic().setMagicBook(details.magicBook);
				}

				if (details.prayerBook > 0) {
					player.setPrayerInterface(details.prayerBook);
				}

				if (details.barrowsKilled != null) {
					for (int i = 0; i < details.barrowsKilled.length; i++) {
						if (details.barrowsKilled[i]) {
							player.getMinigames().setBarrowKilled(i);
						}
					}
				}
				
				//set prayer book
				
				if (details.prayerBook == 0) {
					player.setPrayerInterface(5608);
					player.setPrayer(new DefaultPrayerBook(player));
				} else if (details.prayerBook == 5608) {
					player.setPrayer(new DefaultPrayerBook(player));
				} else if (details.prayerBook == 21356) {
					player.setPrayer(new CursesPrayerBook(player));
				}
				
				if (details.quickPrayersDefault != null) {
					player.setQuickPrayersDefault(details.quickPrayersDefault);
				}
				
				if (details.quickPrayersCurses != null) {
					player.setQuickPrayersCurses(details.quickPrayersCurses);
				}

				return true;
			} finally {
				if (reader != null)
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
	}
}
