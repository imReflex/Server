package org.endeavor.game.entity.player;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.endeavor.GameSettings;
import org.endeavor.engine.SerializedTask;
import org.endeavor.engine.TasksExecutor;
import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.engine.network.mysql.SQLConstants;
import org.endeavor.engine.network.mysql.statement.impl.login.LoginPlayer;
import org.endeavor.engine.network.mysql.statement.impl.login.LoginSalt;
import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.utility.Misc;
import org.endeavor.engine.utility.NameUtil;
import org.endeavor.engine.utility.SerializeableFileManager;
import org.endeavor.engine.utility.TimeStamp;
import org.endeavor.game.GameConstants;
import org.endeavor.game.content.Bank;
import org.endeavor.game.content.Emotes;
import org.endeavor.game.content.Inventory;
import org.endeavor.game.content.PrivateMessaging;
import org.endeavor.game.content.QuestTab;
import org.endeavor.game.content.RunEnergy;
import org.endeavor.game.content.clans.Clan;
import org.endeavor.game.content.clans.ClanChannel;
import org.endeavor.game.content.clans.Clans;
import org.endeavor.game.content.clans.clanwars.ClanWarController;
import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.content.combat.CombatInterface;
import org.endeavor.game.content.combat.Hit;
import org.endeavor.game.content.combat.PlayerCombatInterface;
import org.endeavor.game.content.combat.impl.EarningPotential;
import org.endeavor.game.content.combat.impl.Skulling;
import org.endeavor.game.content.combat.impl.SpecialAttack;
import org.endeavor.game.content.combat.spawning.Loadout;
import org.endeavor.game.content.consumables.Consumables;
import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.impl.Tutorial;
import org.endeavor.game.content.dialogue.impl.StarterDialogue;
import org.endeavor.game.content.dwarfcannon.DwarfMultiCannon;
import org.endeavor.game.content.io.PlayerSaveUtil;
import org.endeavor.game.content.minigames.PlayerMinigames;
import org.endeavor.game.content.minigames.bountyhunter.PenatlyTimer;
import org.endeavor.game.content.minigames.bountyhunter.TargetMatch;
import org.endeavor.game.content.minigames.duelarena.Dueling;
import org.endeavor.game.content.minigames.fightcave.TzharrDetails;
import org.endeavor.game.content.minigames.fightcave.TzharrGame;
import org.endeavor.game.content.pets.Pets;
import org.endeavor.game.content.shopping.Shopping;
import org.endeavor.game.content.skill.Skill;
import org.endeavor.game.content.skill.farming.Farming;
import org.endeavor.game.content.skill.fishing.Fishing;
import org.endeavor.game.content.skill.hunter.Hunter;
import org.endeavor.game.content.skill.magic.Autocast;
import org.endeavor.game.content.skill.magic.MagicSkill;
import org.endeavor.game.content.skill.melee.Melee;
import org.endeavor.game.content.skill.prayer.PrayerBook;
import org.endeavor.game.content.skill.prayer.PrayerConstants;
import org.endeavor.game.content.skill.prayer.impl.CursesPrayerBook;
import org.endeavor.game.content.skill.prayer.impl.DefaultPrayerBook;
import org.endeavor.game.content.skill.ranged.RangedSkill;
import org.endeavor.game.content.skill.slayer.Slayer;
import org.endeavor.game.content.skill.summoning.Summoning;
import org.endeavor.game.content.trading.Trade;
import org.endeavor.game.entity.Attributes;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.following.Following;
import org.endeavor.game.entity.following.impl.PlayerFollowing;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.item.impl.LocalGroundItems;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.mob.MobConstants;
import org.endeavor.game.entity.movement.MovementHandler;
import org.endeavor.game.entity.movement.impl.PlayerMovementHandler;
import org.endeavor.game.entity.object.LocalObjects;
import org.endeavor.game.entity.player.controllers.Controller;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.in.command.impl.PunishmentCommand;
import org.endeavor.game.entity.player.net.in.impl.ChangeAppearancePacket;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;
import org.endeavor.game.entity.player.net.out.impl.SendConfig;
import org.endeavor.game.entity.player.net.out.impl.SendInterface;
import org.endeavor.game.entity.player.net.out.impl.SendLoginResponse;
import org.endeavor.game.entity.player.net.out.impl.SendLogout;
import org.endeavor.game.entity.player.net.out.impl.SendMapRegion;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendPlayerOption;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;
import org.endeavor.game.entity.player.net.out.impl.SendSidebarInterface;
import org.endeavor.game.entity.player.net.out.impl.SendString;

public class Player extends Entity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3076868916326777120L;
	private transient Client client;
	private Location currentRegion = new Location(0, 0, 0);

	private transient List<Player> players = new LinkedList<Player>();

	private transient PlayerAnimations playerAnimations = new PlayerAnimations();
	private RunEnergy runEnergy = new RunEnergy(this);
	private transient MovementHandler movementHandler = new PlayerMovementHandler(this);
	
	private transient CombatInterface combatInterface = new PlayerCombatInterface(this);
	private transient Following following = new PlayerFollowing(this);

	private PrivateMessaging privateMessaging = new PrivateMessaging(this);
	private transient Clan clan;
	private transient ClanChannel channel;
	private String clanTitle = "";
	private boolean clanOwner;
	private HashMap<String,Long> clanKickMap = new HashMap<String,Long>();
	
	/**
	 * Bounty
	 */
	private PenatlyTimer penaltyTimer = null;
	private transient TargetMatch targetMatch = null;
	private int rogueKills = 0;
	private int hunterKills = 0;
	private int rxpBounty = 0;
	
	private transient Player lastKiller = null;

	private Inventory inventory = new Inventory(this);
	private Bank bank = new Bank(this);
	private Equipment equipment = new Equipment(this);
	private transient Trade trade = new Trade(this);
	private transient Shopping shopping = new Shopping(this);
	private transient SpecialAttack specialAttack = new SpecialAttack(this);
	private transient Consumables consumables = new Consumables(this);
	private transient LocalGroundItems groundItems = new LocalGroundItems(this);

	private transient ItemDegrading degrading = new ItemDegrading();

	private Skill skill = new Skill(this);
	private MagicSkill magic = new MagicSkill(this);
	private transient RangedSkill ranged = new RangedSkill(this);
	private transient Melee melee = new Melee();
	private transient Fishing fishing = new Fishing(this);
	private transient Hunter hunter = new Hunter(this);
	private PrayerBook prayer;
	private Slayer slayer = new Slayer(this);
	private Farming farming = new Farming(this);
	private final Summoning summoning = new Summoning(this);
	
	//rare mob drop EP
	private transient RareDropEP rareDropEP = new RareDropEP();

	private Pets pets = new Pets(this);

	private Questing questing = new Questing(this);

	private final Achievements achievements = new Achievements();//achievements
	private PlayerTitles titles = new PlayerTitles(this);//titles

	private transient Dialogue dialogue = null;
	private Skulling skulling = new Skulling();
	private transient LocalObjects objects = new LocalObjects(this);
	private Controller controller = ControllerManager.DEFAULT_CONTROLLER;
	private transient InterfaceManager interfaceManager = new InterfaceManager();

	private transient PlayerMinigames minigames = new PlayerMinigames(this);
	private transient Dueling dueling = new Dueling(this);

	private transient TzharrDetails jadDetails = new TzharrDetails();

	private transient EarningPotential earningPotential = new EarningPotential(this);
	
	private byte[] quickPrayersDefault = new byte[PrayerConstants.DEFAULT_NAMES.length];
	private byte[] quickPrayersCurses = new byte[CursesPrayerBook.CURSE_NAMES.length];

	private boolean starter = false;
	private String username;
	private String displayName;
	private String password;
	private long usernameToLong;
	private int rights = 0;
	private int crownId = -1;
	private boolean visible = true;

	private int currentSongId = -1;
	private int chatColor;
	private int chatEffects;
	private byte[] chatText;
	private byte gender = 0;
	private int[] appearance = new int[7];
	private byte[] colors = new byte[5];
	private short npcAppearanceId = -1;

	private boolean appearanceUpdateRequired = false;
	private boolean chatUpdateRequired = false;
	private boolean needsPlacement = false;
	private boolean resetMovementQueue = false;

	private byte screenBrightness = 3;
	private byte multipleMouseButtons = 0;
	private byte chatEffectsEnabled = 0;
	private byte splitPrivateChat = 0;
	private byte acceptAid = 0;
	private long currentStunDelay;
	private long setStunDelay;
	private long lastAction = System.currentTimeMillis();

	private int enterXSlot = -1;
	private int enterXInterfaceId = -1;
	private int enterXItemId = 1;

	private boolean banned = false;
	private int banLength = 0;
	private int banDay = 0;
	private int banYear = 0;

	private boolean muted = false;
	private int muteLength = 0;
	private int muteDay = 0;
	private int muteYear = 0;
	private int remainingMute = 0;

	private boolean yellMuted = false;

	public long timeout = 0L;

	public long aggressionDelay = System.currentTimeMillis();

	private short kills = 0;
	private short deaths = 0;
	private int prayerInterface;
	private int dungPoints = 0;

	private int yearCreated = 0;
	private int dayCreated = 0;

	private int lastLoginDay = 0;
	private int lastLoginYear = 0;

	private byte musicVolume = 3;
	private byte soundVolume = 3;

	private TimeStamp voteTime = null;

	private int totalVotes = 0;
	private int votePoints = 0;
	private int donationPoints = 0;
	private int slayerPoints = 0;
	private int skillPoints = 0;
	private int pestPoints = 0;
	private int rxp = 0;

	private int memberRank = 0;

	private transient boolean canLogin = false;
	
	private boolean betaTester = false;

	private int blackMarks = 0;

	private byte[] pouches = new byte[4];
	
	private Loadout[] customSpawnLoadouts = new Loadout[6];

	private String title = "";
	private int titleColor = 0x9f5913;

	public Player(PlayerDetails pd) {
		this.username = pd.getUsername();
		this.password = pd.getPassword();
		this.client = pd.getClient();
		this.client.setPlayer(this);
		this.client.getPacketHandler().setPlayer(this);
		getLocation().setAs(new Location(PlayerConstants.HOME));
		setNpc(false);
	}

	public Player() {
		ChangeAppearancePacket.setToDefault(this);
		client = new Client(null);
		usernameToLong = 0L;
	}
	
	public void send(OutgoingPacket o) {
		client.queueOutgoingPacket(o);
	}

	@Override
	public boolean equals(Object o) {
		if ((o instanceof Player)) {
			return ((Player) o).getUsernameToLong() == getUsernameToLong();
		}

		return false;
	}

	public void giveBlackmark(int amount) {
		blackMarks += amount;

		client.queueOutgoingPacket(new SendMessage("<img=1> <col=0c32f6>You have received " + amount + " blackmark(s) for breaking rules.</col>"));
		client.queueOutgoingPacket(new SendMessage("<img=1> <col=0c32f6>You now have " + blackMarks + " blackmarks.</col> Use ::rules for an updated list of regulations."));

		client.queueOutgoingPacket(new SendMessage("<img=1> <col=0c32f6>3</col>-<col=0c32f6>4</col> = 3 day mute, <col=0c32f6>5</col>-<col=0c32f6>7</col> = 6 day mute, "
				+ "<col=0c32f6>8</col>-<col=0c32f6>12</col> = 3 day ban, "));
		client.queueOutgoingPacket(new SendMessage("<img=1> <col=0c32f6>13</col>-<col=0c32f6>14</col> = 1 week ban, <col=0c32f6>15+</col> permanent ban."));
		//<col=0c32f6>

		if (blackMarks >= 15) {
			PunishmentCommand.ban(this, Integer.MAX_VALUE);
		} else if (blackMarks >= 13) {
			PunishmentCommand.ban(this, 7);
		} else if (blackMarks >= 8) {
			PunishmentCommand.ban(this, 3);
		} else if (blackMarks >= 5) {
			PunishmentCommand.mute(this, 6);
			client.queueOutgoingPacket(new SendMessage("<img=1> <col=0c32f6>You have been muted for 6 days.</col>"));
			client.queueOutgoingPacket(new SendMessage("<img=1> <col=0c32f6>You will be banned in " + (8 - blackMarks) + " more blackmarks."));
		} else if (blackMarks >= 3) {
			PunishmentCommand.mute(this, 3);
			client.queueOutgoingPacket(new SendMessage("<img=1> <col=0c32f6>You have been muted for 3 days.</col>"));
			client.queueOutgoingPacket(new SendMessage("<img=1> <col=0c32f6>You will be banned in " + (8 - blackMarks) + " more blackmarks."));
		} else {
			client.queueOutgoingPacket(new SendMessage("<img=1> <col=0c32f6>You did not receive any punishment this time.</col>"));
			client.queueOutgoingPacket(new SendMessage("<img=1> <col=0c32f6>You will be muted in " + (3 - blackMarks) + " more blackmarks."));
		}
	}

	public boolean isBusy() {
		return (interfaceManager.hasBankOpen()) || (trade.trading()) || (dueling.isStaking())
				|| (controller.equals(ControllerManager.ROLLING_DICE_CONTROLLER));
	}

	/*public boolean hasPremium() {
		if ((premiumDays == 0) && (premiumYear > 0)) {
			premiumDays = 30;
		}

		return (premiumYear > 0) && (Misc.getElapsed(premiumDay, premiumYear) <= premiumDays);
	}

	public void givePremium(boolean year) {
		premiumDays = (year ? 365 : 30);

		premiumDay = Misc.getDayOfYear();
		premiumYear = Misc.getYear();
	}*/

	public void doFadeTeleport(final Location l, final boolean setController) {
		client.queueOutgoingPacket(new SendInterface(18460));

		setController(Tutorial.TUTORIAL_CONTROLLER);

		final Player player = this;

		TaskQueue.queue(new Task(this, 1) {
			private static final long serialVersionUID = 2934405657365651353L;
			byte pos = 0;

			@Override
			public void execute() {
				if ((this.pos = (byte) (pos + 1)) >= 3)
					if (pos == 3) {
						teleport(l);
						client.queueOutgoingPacket(new SendInterface(18452));
					} else if (pos == 5) {
						client.queueOutgoingPacket(new SendRemoveInterfaces());
						if (setController) {
							setController(ControllerManager.DEFAULT_CONTROLLER);
							ControllerManager.setControllerOnWalk(player);
						}
						stop();
					}
			}

			@Override
			public void onStop() {
			}
		});
	}

	@Override
	public void process() throws Exception {
		if (Math.abs(World.getCycles() - client.getLastPacketTime()) >= 9) {
			if (getCombat().inCombat() && !getCombat().getLastAttackedBy().isNpc()) {
				if (timeout == 0) {
					timeout = System.currentTimeMillis() + 180000;
				} else if (timeout <= System.currentTimeMillis() || !getCombat().inCombat()) {
					logout(false);
					System.out.println("Player timed out: " + getUsername());
				}
			} else {
				System.out.println("Player timed out: " + getUsername());
				logout(false);
			}
		}

		/*if (System.currentTimeMillis() - lastAction >= 180000L) {
			getUpdateFlags().sendGraphic(new Graphic(277, 0, false));
		}*/

		if (controller != null) {
			controller.tick(this);
		}

		shopping.update();
		prayer.drain();

		following.process();

		getCombat().process();

		doAgressionCheck();
	}

	public void resetAggression() {
		aggressionDelay = System.currentTimeMillis();
	}

	public void doAgressionCheck() {
		if (PlayerConstants.isOwner(this)) {
			return;
		}

		if (!controller.canAttackNPC()) {
			return;
		}

		short[] override = new short[3];

		if ((getCombat().inCombat()) && (!inMultiArea()))
			return;
		if ((getCombat().inCombat()) && (getCombat().getLastAttackedBy().isNpc())) {
			Mob m = World.getNpcs()[getCombat().getLastAttackedBy().getIndex()];

			if (m != null) {
				if (m.getId() == 6260) {
					override[0] = 6261;
					override[1] = 6263;
					override[2] = 6265;
				} else if (m.getId() == 6203) {
					override[0] = 6204;
					override[1] = 6206;
					override[2] = 6208;
				} else if (m.getId() == 6222) {
					override[0] = 6227;
					override[1] = 6225;
					override[2] = 6223;
				} else if (m.getId() == 6247) {
					override[0] = 6250;
					override[1] = 6248;
					override[2] = 6252;
				}
			}

		}

		boolean owner = false;

		for (Mob i : getClient().getNpcs())
			if ((i.getCombat().getAttacking() == null) && (i.getCombatDefinition() != null)) {
				boolean overr = false;

				for (short j : override) {
					if ((short) i.getId() == j) {
						overr = true;
						break;
					}
				}

				if (!overr) {
					if (System.currentTimeMillis() - aggressionDelay >= 60000 * 8) {
						continue;
					}
				}

				if ((i.getLocation().getZ() == getLocation().getZ()) && (!i.isWalkToHome())) {
					if ((MobConstants.isAggressive(i.getId())) && ((!i.getCombat().inCombat()) || (i.inMultiArea()))) {
						if ((MobConstants.isAgressiveFor(i, this)) && (!owner)) {
							if ((overr)
									|| (Math.abs(getLocation().getX() - i.getLocation().getX())
											+ Math.abs(getLocation().getY() - i.getLocation().getY()) <= i.getSize() * 2))
								i.getCombat().setAttack(this);
						}
					}
				}
			}
	}

	public void checkForRegionChange() {
		int deltaX = getLocation().getX() - getCurrentRegion().getRegionX() * 8;
		int deltaY = getLocation().getY() - getCurrentRegion().getRegionY() * 8;

		if ((deltaX < 16) || (deltaX >= 88) || (deltaY < 16) || (deltaY > 88))
			getClient().queueOutgoingPacket(new SendMapRegion(this));
	}

	public void onControllerFinish() {
		controller = ControllerManager.DEFAULT_CONTROLLER;
	}

	public void start(Dialogue dialogue) {
		this.dialogue = dialogue;
		if (dialogue != null) {
			dialogue.setNext(0);
			dialogue.setPlayer(this);
			dialogue.execute();
		} else if (getAttributes().get("pauserandom") != null) {
			getAttributes().remove("pauserandom");
		}
	}

	public void incrKills() {
		kills = ((short) (kills + 1));
		QuestTab.updateKDRDisplay(this);
	}

	public void incrDeaths() {
		deaths = ((short) (deaths + 1));
		QuestTab.updateKDRDisplay(this);
	}

	public boolean login(boolean starter, PlayerDetails pd) throws Exception {
		this.initEntity();
		this.client = pd.getClient();
		if(this.client != null) {
			this.client.setPlayer(this);
			this.client.getPacketHandler().setPlayer(this);
		}
		this.starter = starter;
		username = NameUtil.uppercaseFirstLetter(username);

		usernameToLong = Misc.nameToLong(username.toLowerCase());
		
		int response = 2;

		if ((password.length() == 0) || (username.length() == 0) || (username.length() > 12))
			response = 3;
		else if ((banned) || (PlayerSaveUtil.isIPBanned(this)))
			response = 4;
		else if (/*(password != null) && (!password.equals(client.getEnteredPassword()))*/!this.canLogin)
			response = 3;
		else if (World.isUpdating())
			response = 14;
		else if (World.getPlayerByName(username) != null)
			response = 5;
		
		if (response != 2) {
			StreamBuffer.OutBuffer resp = StreamBuffer.newOutBuffer(4);
			resp.writeByte(response);
			resp.writeByte(rights);
			resp.writeByte(this.getCrownId());
			resp.writeByte(0);
			client.send(resp.getBuffer());
			return false;
		}
		
		/**
		 * Instantiate transient instances
		 */
		players = new LinkedList<Player>();
		playerAnimations = new PlayerAnimations();
		movementHandler = new PlayerMovementHandler(this);
		combatInterface = new PlayerCombatInterface(this);
		following = new PlayerFollowing(this);
		trade = new Trade(this);
		shopping = new Shopping(this);
		specialAttack = new SpecialAttack(this);
		consumables = new Consumables(this);
		groundItems = new LocalGroundItems(this);
		degrading = new ItemDegrading();
		magic = new MagicSkill(this);
		ranged = new RangedSkill(this);
		melee = new Melee();
		fishing = new Fishing(this);
		hunter = new Hunter(this);
		rareDropEP = new RareDropEP();
		dialogue = null;
		objects = new LocalObjects(this);
		interfaceManager = new InterfaceManager();
		minigames = new PlayerMinigames(this);
		dueling = new Dueling(this);
		jadDetails = new TzharrDetails();
		earningPotential = new EarningPotential(this);
		//penaltyTimer = null;
		targetMatch = null;
		
		/**
		 * Handle serialized objects.
		 */
		if(privateMessaging == null)
			privateMessaging = new PrivateMessaging(this);
		if(inventory == null)
			inventory = new Inventory(this);
		if(equipment == null)
			equipment = new Equipment(this);
		if(bank == null)
			bank = new Bank(this);
		if(titles == null)
			titles = new PlayerTitles(this);
		if(slayer == null)
			slayer = new Slayer(this);
		if(pets == null)
			pets = new Pets(this);
		if(farming == null)
			farming = new Farming(this);
		if(runEnergy == null)
			runEnergy = new RunEnergy(this);
		if(magic == null)
			magic = new MagicSkill(this);
		if(skill == null)
			skill = new Skill(this);
		privateMessaging.setPlayer(this);
		inventory.setPlayer(this);
		equipment.setPlayer(this);
		bank.setPlayer(this);
		slayer.setPlayer(this);
		pets.setPlayer(this);
		farming.setPlayer(this);
		runEnergy.setPlayer(this);
		magic.initPlayer(this);
		skill.setPlayer(this);
		titles.setPlayer(this);
		
		/**
		 * Check & see if the player can be registered.
		 */
		if (World.register(this) == -1) {
			StreamBuffer.OutBuffer resp = StreamBuffer.newOutBuffer(4);
			resp.writeByte(7);
			resp.writeByte(rights);
			resp.writeByte(this.getCrownId());
			resp.writeByte(0);
			client.send(resp.getBuffer());
			return false;
		}

		PlayerConstants.setOwner(this);

		//System.out.println("Sending Login Response...");
		new SendLoginResponse(response, (PlayerConstants.isOwner(this)) && (!GameSettings.DEV_MODE) ? 4 : rights, this.getCrownId()).execute(client);

		if (PlayerSaveUtil.isIPMuted(this)) {
			setMuted(true);
			setRemainingMute(99999);
		}
		
		ControllerManager.setControllerOnWalk(this);
		
		/**
		 * Clan stuff
		 */
		if(clanKickMap == null)
			clanKickMap = new HashMap<String,Long>();
		
		Clan oclan = Clans.getClanForOwner(this);
		if(oclan != null) {
			oclan.updateOwner(this);
			Clans.saveClan(oclan);
			this.clanOwner = true;
			this.clanTitle = oclan.getSettings().getTitle();
		}
		
		if(clanTitle != "") {
			Clan clan = Clans.getClanForName(clanTitle);
			if(clan != null) {
				if(clan.isMember(this)) {
					this.clan = clan;
					this.channel = clan.getChannel();
					channel.joinChannel(this);
				}
			} else {
				clanTitle = "";
			}
		}
		

		//if (!ChangeAppearancePacket.validate(appearance, colors, gender)) {
		//ChangeAppearancePacket.setToDefault(this);
		//}

		/*if (Region.getRegion(getLocation().getX(), getLocation().getY()) == null) {
			teleport(new Location(3222, 3218, 0));
			client.queueOutgoingPacket(new SendMessage("You have been retrieved from an unknown location."));
		}*/

		movementHandler.getLastLocation().setAs(
				new Location(getLocation().getX(), getLocation().getY() + 1, getLocation().getZ()));

		for (int i = 0; i < PlayerConstants.SIDEBAR_INTERFACE_IDS.length; i++) {
			//if (i != PlayerConstants.PRAYER_TAB && i != PlayerConstants.MAGIC_TAB) {
				client.queueOutgoingPacket(new SendSidebarInterface(i, PlayerConstants.SIDEBAR_INTERFACE_IDS[i]));
			//}
		}

		if (magic.getMagicBook() == 0) {
			magic.setMagicBook(1151);
		}

		if (prayerInterface == 0) {
			prayerInterface = 5608;
			prayer = new DefaultPrayerBook(this);
		} else if (prayerInterface == 21356) {
			prayer = new CursesPrayerBook(this);
		}

		client.queueOutgoingPacket(new SendSidebarInterface(PlayerConstants.PRAYER_TAB, prayerInterface));

		if (starter) {
			ChangeAppearancePacket.setToDefault(this);
			//send(new SendFlashSidebarIcon(135));
			
			PlayerConstants.setAppearance(this);

			if (lastLoginYear == 0) {
				yearCreated = Misc.getYear();
				dayCreated = Misc.getDayOfYear();
			}
		}
		
		/*if (!ChangeAppearancePacket.validate(this)) {
			ChangeAppearancePacket.setToDefault(this);
		}*/

		equipment.onLogin();
		skill.onLogin();
		magic.onLogin();

		privateMessaging.connect();
		runEnergy.update();
		prayer.disable();

		Autocast.resetAutoCastInterface(this);

		if (title == null) {
			title = rights >= 3 ? "Staff" : "Noob";
		}

		

		achievements.onLogin(this);

		Emotes.onLogin(this);

		QuestTab.onLogin(this);

		inventory.update();

		client.queueOutgoingPacket(new SendPlayerOption("Follow", 1));
		client.queueOutgoingPacket(new SendPlayerOption("Trade with", 2));
		
		client.queueOutgoingPacket(new SendConfig(166, screenBrightness));
		client.queueOutgoingPacket(new SendConfig(171, multipleMouseButtons));
		client.queueOutgoingPacket(new SendConfig(172, chatEffectsEnabled));
		client.queueOutgoingPacket(new SendConfig(287, splitPrivateChat));
		client.queueOutgoingPacket(new SendConfig(427, acceptAid));
		client.queueOutgoingPacket(new SendConfig(172, isRetaliate() ? 1 : 0));
		client.queueOutgoingPacket(new SendConfig(173, runEnergy.isRunning() ? 1 : 0));
		client.queueOutgoingPacket(new SendConfig(168, musicVolume));
		client.queueOutgoingPacket(new SendConfig(169, soundVolume));
		client.queueOutgoingPacket(new SendConfig(876, 0));

		client.queueOutgoingPacket(new SendConfig(77, 0));

		getUpdateFlags().setUpdateRequired(true);
		appearanceUpdateRequired = true;
		needsPlacement = true;

		//client.queueOutgoingPacket(new SendMessage("<img=1> Welcome to Endeavor! <img=1>"));
		//client.queueOutgoingPacket(new SendMessage("<shad=59678><col=34567>Use ::rules for the official server rules (updated 3/1/14)."));
		//client.queueOutgoingPacket(new SendMessage("<img=3> Join the official Teamspeak at <col=0c32f6>endeavor.ts.nfoservers.com</col>! <img=3>"));
		client.queueOutgoingPacket(new SendMessage("<shad=57457><col=24247>Welcome " + (starter ? "" : "back ") + "to <col=0c32f6>RevolutionX!"));
		//client.queueOutgoingPacket(new SendMessage("<col=0c32f6>Use the Lodestone Network (World Map) to access the game teleports."));
		
		if (GameConstants.IS_DOUBLE_EXP_WEEKEND) {
			client.queueOutgoingPacket(new SendMessage("<shad=57457><col=0c32f6>Double experience is enabled!"));
		}

		if(this.clanTitle == "") {
			this.send(new SendMessage("If you wish to create a clan type '::create NAME'. NOTE: You cannot currently delete your own clan."));
			this.send(new SendMessage("The official clan for RX is 'revolutionx'."));
		} 
		
		titles.checkForNewAchievmentTitles();
		titles.checkForNewPKAchievments();

		if (inSafePK()) {
			controller = ControllerManager.CW_SAFE_PK_CONTROLLER;
		}
		
		questing.onLogin();
		if(controller instanceof ClanWarController) {
			controller = ControllerManager.DEFAULT_CONTROLLER;
			teleport(new Location(3271, 3687, 0));
		}
		controller.onControllerInit(this);
		
		PlayerConstants.sendLastUpdates(this);
		
		if(this.clanOwner && Clans.getClanForOwner(this) != null) {
			client.queueOutgoingPacket(new SendPlayerOption("Invite", 6));
			if(this.inArea(new Location(3264, 3672, 0), new Location(3279, 3695, 0), false)) {
				client.queueOutgoingPacket(new SendPlayerOption("null", 3));
				client.queueOutgoingPacket(new SendPlayerOption("Challenge", 4));
			}
		}
		
		//if (starter) {
			//send(new SendMessage("Use the <img=19> icon on the Lodestone to fast-tavel."));
			//send(new SendMessage("You can also click the icons to teleport for one Law rune."));
		//}
		
		if (prayer instanceof DefaultPrayerBook) {
			for (int i = 0; i < getQuickPrayersDefault().length; i++) {
				send(new SendConfig(630 + i, getQuickPrayersDefault()[i]));
			}
		} else {
			for (int i = 0; i < getQuickPrayersCurses().length; i++) {
				send(new SendConfig(630 + i, getQuickPrayersCurses()[i]));
			}
		}
		setActive(true);
		start();
		return true;
	}

	public void rules() {
		Misc.openBlankQuestDialogue(this); 

		client.queueOutgoingPacket(new SendString("Bug abuse (i.e. safespots, dupes) is not allowed.", 8147)); 
		client.queueOutgoingPacket(new SendString("Do not flame other players.", 8149)); 
		client.queueOutgoingPacket(new SendString("Do not disrespect staff.", 8151));
		client.queueOutgoingPacket(new SendString("Do not evade punishents (serious punishment).", 8153));
		client.queueOutgoingPacket(new SendString("Do not scam items from other players.", 8155));
		client.queueOutgoingPacket(new SendString("Do not use autos (except: auto talking, at 5 sec).", 8157));
		client.queueOutgoingPacket(new SendString("Do not argue/flame in the official clan chat.", 8159));
		client.queueOutgoingPacket(new SendString("You will be muted for at least 7 days.", 8160));
		
	}

	public void start() {
		//System.out.println("Starting");
		runEnergy.tick();
		startRegeneration();
		specialAttack.tick();
		farming.init();

		summoning.onLogin();

		skulling.tick(this);

		if (jadDetails.getStage() != 0) {
			TzharrGame.loadGame(this);
		}

		//clanChat.enterChat("endeavor");

		/*if ((hasPremium()) && (premiumDays - Misc.getElapsed(premiumDay, premiumYear) <= 5)) {
			client.queueOutgoingPacket(new SendMessage("<col=255>Your premium will run out soon.</col>"));
		}*/

		final Player player = this;
		TaskQueue.queue(new Task(player, 500) {
			private static final long serialVersionUID = 5761570752681173011L;

			@Override
			public void execute() {
				if (canSave()) {
					//PlayerSave.save(player);
					SerializeableFileManager.savePlayer(player);
				}
			}

			@Override
			public void onStop() {
			}
		});
		if ((voteTime == null) || (voteTime.getHoursElapsed() >= 12))
			TaskQueue.queue(new Task(player, 2500) {
				/**
				 * 
				 */
				private static final long serialVersionUID = 7611515734837691504L;

				@Override
				public void execute() {
					client.queueOutgoingPacket(new SendMessage(
							"<col=255>Remember to vote every 12 hours, use ::vote now for great rewards!</col>"));
				}

				@Override
				public void onStop() {
				}
			});

		if (getTeleblockTime() > 0) {
			tickTeleblock();
		}

		if (blackMarks >= 8) {
			client.queueOutgoingPacket(new SendMessage("<img=1> <col=0c32f6>You have 7 or more blackmarks.</col>"));
			client.queueOutgoingPacket(new SendMessage("<img=1> <col=0c32f6>You will be automatically banned on your next blackmark.</col>"));
		}
	}

	public boolean canSave() {
		return controller.canSave();
	}

	public void logout(boolean force) {
		if (isActive()) {
			if ((pets.hasPet()) && (!pets.remove()) && (!force)) {
				return;
			}

			if (force)
				ControllerManager.onForceLogout(this);
			else if ((controller != null) && (!controller.canLogOut())) {
				return;
			}

			World.remove(client.getNpcs());

			if (controller != null) {
				controller.onDisconnect(this);
			}
			
			Clan clan = Clans.getClanForOwner(this);
			if(clan != null) {
				if(clan.getWarSetup() != null)
					clan.getWarSetup().cancel();
			}

			//MySQLThread.queue(this);

			//leave clan channel
			if(this.getClanChannel() != null)
				this.getClanChannel().leaveChannel(this);

			if (trade.trading()) {
				trade.end(false);
			}

			if (dueling.isStaking()) {
				dueling.decline();
			}

			if (minigames.getBetManager().betting()) {
				minigames.getBetManager().end(false);
			}

			if (summoning.hasFamiliar()) {
				summoning.removeForLogout();
			}

			if (DwarfMultiCannon.hasCannon(this)) {
				DwarfMultiCannon.getCannon(this).onLogout();
			}

			//PlayerSave.save(this);
			SerializeableFileManager.savePlayer(this);
		}

		World.unregister(this);
		client.setStage(Client.Stages.LOGGED_OUT);
		setActive(false);

		new SendLogout().execute(client);
		client.disconnect();
	}

	public void save() {
		final Player player = this;
		TasksExecutor.slowExecutor.submit(new Runnable() {
			@Override
			public void run() {
				SerializeableFileManager.savePlayer(player);
			}
		});
	}
	
	public boolean withinRegion(Location other) {
		int deltaX = other.getX() - currentRegion.getRegionX() * 8;
		int deltaY = other.getY() - currentRegion.getRegionY() * 8;

		if ((deltaX < 2) || (deltaX > 110) || (deltaY < 2) || (deltaY > 110)) {
			return false;
		}

		return true;
	}

	@Override
	public void poison(int start) {
		if (isPoisoned()) {
			return;
		}

		super.poison(start);

		if (isActive())
			client.queueOutgoingPacket(new SendMessage("You have been poisoned!"));
	}

	@Override
	public void teleblock(int i) {
		if (isTeleblocked()) {
			client.queueOutgoingPacket(new SendMessage("This player has already been affected by this spell."));
			return;
		}

		achievements.incr(this, "Get tele-blocked");
		client.queueOutgoingPacket(new SendMessage("You have been Tele-blocked!"));
		super.teleblock(i);
	}
	
	@Override
	public void retaliate(Entity attacked) {
		if (attacked != null) {
			if (isRetaliate() && getCombat().getAttacking() == null 
					&& !getMovementHandler().moving()) {//still retaliate when moving?
				getCombat().setAttack(attacked);
			}
		}
	}

	@Override
	public void hit(Hit hit) {
		combatInterface.hit(hit);
	}

	@Override
	public void checkForDeath() {
		combatInterface.checkForDeath();
	}

	@Override
	public void onHit(Entity e, Hit hit) {
		combatInterface.onHit(e, hit);
		
		if (e.isNpc()) {
			Mob m = World.getNpcs()[e.getIndex()];
			
			if (m != null) {
				rareDropEP.forHitOnMob(this, m, hit);
			}
		}
	}

	@Override
	public boolean canAttack() {
		return combatInterface.canAttack();
	}

	@Override
	public void updateCombatType() {
		/**
		 * This shit was gone?
		 */
		CombatTypes type;
		if (magic.getSpellCasting().isCastingSpell())
			type = CombatTypes.MAGIC;
		else {
			type = EquipmentConstants.getCombatTypeForWeapon(this);
		}
		
		if (type != CombatTypes.MAGIC || !magic.getSpellCasting().isAutocasting()) {
			send(new SendConfig(333, 0));
		}

		getCombat().setCombatType(type);

		switch (type) {
		case MELEE:
			equipment.updateMeleeDataForCombat();
			break;
		case RANGED:
			equipment.updateRangedDataForCombat();
			break;
		default:
			break;
		}
	}

	@Override
	public void afterCombatProcess(Entity attack) {
		combatInterface.afterCombatProcess(attack);
	}

	@Override
	public void onCombatProcess(Entity attack) {
		combatInterface.onCombatProcess(attack);
	}

	@Override
	public void onAttack(Entity attack, int hit, CombatTypes type, boolean success) {
		combatInterface.onAttack(attack, hit, type, success);
	}

	@Override
	public int getAccuracy(Entity attack, CombatTypes type) {
		return combatInterface.getAccuracy(attack, type);
	}

	@Override
	public int getMaxHit(CombatTypes type) {
		return combatInterface.getMaxHit(type);
	}

	@Override
	public int getCorrectedDamage(int damage) {
		return combatInterface.getCorrectedDamage(damage);
	}

	@Override
	public boolean isIgnoreHitSuccess() {
		return combatInterface.isIgnoreHitSuccess();
	}

	@Override
	public void reset() {
		following.updateWaypoint();
		appearanceUpdateRequired = false;
		chatUpdateRequired = false;
		resetMovementQueue = false;
		needsPlacement = false;
		getMovementHandler().resetMoveDirections();
		getUpdateFlags().setUpdateRequired(false);
		getUpdateFlags().reset();
	}

	public void teleport(Location location) {
		boolean zChange = location.getZ() != getLocation().getZ();
		getLocation().setAs(location);
		setResetMovementQueue(true);
		setNeedsPlacement(true);
		movementHandler.getLastLocation().setAs(new Location(getLocation().getX(), getLocation().getY() + 1));
		getAttributes().remove("combatsongdelay");

		if (!inHomeBank() && !controller.equals(ControllerManager.DUEL_STAKE_CONTROLLER) && !this.inArea(new Location(3264, 3672, 0), new Location(3279, 3695, 0), false)) {
			client.queueOutgoingPacket(new SendPlayerOption("null", 4));
		}

		ControllerManager.setControllerOnWalk(this);

		//if (!controller.equals(ARConstants.AR_CONTROLLER)) {
		//setHitImmunityDelay(5);
		//}

		TaskQueue.cancelHitsOnEntity(this);

		movementHandler.reset();

		if (zChange) {
			client.queueOutgoingPacket(new SendMapRegion(this));
		} else {
			checkForRegionChange();
		}

		if (trade.trading()) {
			trade.end(false);
		} else if (dueling.isStaking()) {
			dueling.decline();
		}
		
		TaskQueue.onMovement(this);
	}

	public void changeZ(int z) {
		getLocation().setZ(z);
		needsPlacement = true;

		objects.onRegionChange();
		groundItems.onRegionChange();

		getMovementHandler().reset();

		//setHitImmunityDelay(5);

		client.queueOutgoingPacket(new SendMapRegion(this));
	}

	@Override
	public MovementHandler getMovementHandler() {
		return movementHandler;
	}

	@Override
	public Following getFollowing() {
		return following;
	}

	@Override
	public String toString() {
		return "Player(" + getUsername() + ":" + getPassword() + " - " + client.getHost() + ")";
	}

	public void setCurrentRegion(Location currentRegion) {
		this.currentRegion = currentRegion;
	}

	public Location getCurrentRegion() {
		return currentRegion;
	}

	public void setNeedsPlacement(boolean needsPlacement) {
		this.needsPlacement = needsPlacement;
	}

	public boolean needsPlacement() {
		return needsPlacement;
	}

	public Client getClient() {
		return client;
	}
	
	public void setClient(Client client) {
		this.client = client;
	}

	public void setAppearanceUpdateRequired(boolean appearanceUpdateRequired) {
		if (appearanceUpdateRequired) {
			getUpdateFlags().setUpdateRequired(true);
		}
		this.appearanceUpdateRequired = appearanceUpdateRequired;
	}

	public boolean isAppearanceUpdateRequired() {
		return appearanceUpdateRequired;
	}

	public void setRights(int rights) {
		this.rights = rights;
	}

	public int getRights() {
		return rights;
	}
	
	public int getCrownId() {
		if(rights == 0 && memberRank == 0)
			return 128;
		if(rights == 1)
			return 0;
		if(rights == 2)
			return 1;
		if(rights == 3)
			return 2;
		if(rights == 0 && memberRank == 1)
			return 5;
		if(rights == 0 && memberRank == 2)
			return 3;
		if(rights == 0 && memberRank == 3)
			return 4;
		return 128;
	}
	
	public int getRightsForIPB(int right) {
		switch(right) {
		case 31:
		case 26:
		case 4:
			return 2;
		case 6:
		case 27:
			return 1;
		}
		return 0;
	}
	
	public int getRankForIPB(int rank) {
		switch(rank) {
		case 7:
			return 1;
		case 8:
			return 2;
		case 9:
		case 10:
			return 3;
		}
		return 0;
	}

	public void setResetMovementQueue(boolean resetMovementQueue) {
		this.resetMovementQueue = resetMovementQueue;
	}

	public boolean isResetMovementQueue() {
		return resetMovementQueue;
	}

	public void setChatColor(int chatColor) {
		this.chatColor = chatColor;
	}

	public int getChatColor() {
		return chatColor;
	}

	public void setChatEffects(int chatEffects) {
		this.chatEffects = chatEffects;
	}

	public int getChatEffects() {
		return chatEffects;
	}

	public void setChatText(byte[] chatText) {
		this.chatText = chatText;
	}

	public byte[] getChatText() {
		return chatText;
	}

	public void setChatUpdateRequired(boolean chatUpdateRequired) {
		if (chatUpdateRequired) {
			getUpdateFlags().setUpdateRequired(true);
		}
		this.chatUpdateRequired = chatUpdateRequired;
	}

	public boolean isChatUpdateRequired() {
		return chatUpdateRequired;
	}

	public int[] getAppearance() {
		return appearance;
	}

	public byte[] getColors() {
		return colors;
	}

	public void setGender(byte gender) {
		this.gender = gender;
	}

	public byte getGender() {
		return gender;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}
	
	public void setDisplayName(String username) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public byte getScreenBrightness() {
		return screenBrightness;
	}

	public void setScreenBrightness(byte screenBrightness) {
		this.screenBrightness = screenBrightness;
	}

	public byte getMultipleMouseButtons() {
		return multipleMouseButtons;
	}

	public void setMultipleMouseButtons(byte multipleMouseButtons) {
		this.multipleMouseButtons = multipleMouseButtons;
	}

	public byte getChatEffectsEnabled() {
		return chatEffectsEnabled;
	}

	public void setChatEffectsEnabled(byte chatEffectsEnabled) {
		this.chatEffectsEnabled = chatEffectsEnabled;
	}

	public byte getSplitPrivateChat() {
		return splitPrivateChat;
	}

	public void setSplitPrivateChat(byte splitPrivateChat) {
		this.splitPrivateChat = splitPrivateChat;
	}

	public byte getAcceptAid() {
		return acceptAid;
	}

	public void setAcceptAid(byte acceptAid) {
		this.acceptAid = acceptAid;
	}

	public int getEnterXSlot() {
		return enterXSlot;
	}

	public void setEnterXSlot(int enterXSlot) {
		this.enterXSlot = enterXSlot;
	}

	public int getEnterXInterfaceId() {
		return enterXInterfaceId;
	}

	public void setEnterXInterfaceId(int enterXInterfaceId) {
		this.enterXInterfaceId = enterXInterfaceId;
	}

	public int getEnterXItemId() {
		return enterXItemId;
	}

	public void setEnterXItemId(int enterXItemId) {
		this.enterXItemId = enterXItemId;
	}

	public void setNpcAppearanceId(short npcAppearanceId) {
		this.npcAppearanceId = npcAppearanceId;
	}

	public int getNpcAppearanceId() {
		return npcAppearanceId;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isVisible() {
		return visible;
	}

	public Skill getSkill() {
		return skill;
	}

	public MagicSkill getMagic() {
		return magic;
	}

	public RangedSkill getRanged() {
		return ranged;
	}

	public Melee getMelee() {
		return melee;
	}

	public PrayerBook getPrayer() {
		return prayer;
	}

	public void setPrayer(PrayerBook prayer) {
		this.prayer = prayer;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public Bank getBank() {
		return bank;
	}

	public Trade getTrade() {
		return trade;
	}

	public Shopping getShopping() {
		return shopping;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public SpecialAttack getSpecialAttack() {
		return specialAttack;
	}

	public Consumables getConsumables() {
		return consumables;
	}

	public LocalGroundItems getGroundItems() {
		return groundItems;
	}

	public RunEnergy getRunEnergy() {
		return runEnergy;
	}

	public PlayerAnimations getAnimations() {
		return playerAnimations;
	}

	public PrivateMessaging getPrivateMessaging() {
		return privateMessaging;
	}

	public Clan getClan() {
		return this.clan;
	}
	
	public void setClan(Clan clan) {
		this.clan = clan;
	}
	
	public ClanChannel getClanChannel() {
		return this.channel;
	}
	
	public void setClanChannel(ClanChannel channel) {
		this.channel = channel;
	}
	
	public void setClanTitle(String title) {
		this.clanTitle = title;
	}
	
	public String getClanTitle() {
		return clanTitle;
	}
	
	public void setClanOwner(boolean owner) {
		this.clanOwner = owner;
	}
	
	public long getClanKickTime(String name) {
		return clanKickMap.containsKey(name) ? clanKickMap.get(name) : 0;
	}
	
	public void addClanKickTime(String name) {
		this.clanKickMap.put(name, System.currentTimeMillis() + (1000 * 60 * 5));
	}
	
	public Skulling getSkulling() {
		return skulling;
	}

	public boolean isMuted() {
		return muted;
	}

	public void setMuted(boolean muted) {
		this.muted = muted;
	}

	public boolean isBanned() {
		return banned;
	}

	public void setBanned(boolean banned) {
		this.banned = banned;
	}

	public int getRemainingMute() {
		return remainingMute;
	}

	public void setRemainingMute(int remainingMute) {
		this.remainingMute = remainingMute;
	}

	public Dialogue getDialogue() {
		return dialogue;
	}

	public Fishing getFishing() {
		return fishing;
	}
	
	public Hunter getHunter() {
		return hunter;
	}

	public LocalObjects getObjects() {
		return objects;
	}

	public boolean setControllerNoInit(Controller controller) {
		this.controller = controller;
		return true;
	}

	public boolean setController(Controller controller) {
		this.controller = controller;
		controller.onControllerInit(this);
		return true;
	}

	public Controller getController() {
		if (controller == null) {
			setController(ControllerManager.DEFAULT_CONTROLLER);
		}

		return controller;
	}

	public PlayerMinigames getMinigames() {
		return minigames;
	}

	public long getCurrentStunDelay() {
		return currentStunDelay;
	}

	public void setCurrentStunDelay(long delay) {
		currentStunDelay = delay;
	}

	public long getSetStunDelay() {
		return setStunDelay;
	}

	public void setSetStunDelay(long delay) {
		setStunDelay = delay;
	}

	public int getCurrentSongId() {
		return currentSongId;
	}

	public void setCurrentSongId(int currentSongId) {
		this.currentSongId = currentSongId;
	}

	public Dueling getDueling() {
		return dueling;
	}

	public InterfaceManager getInterfaceManager() {
		return interfaceManager;
	}

	public TzharrDetails getJadDetails() {
		return jadDetails;
	}

	public void setAppearance(int[] appearance) {
		this.appearance = appearance;
	}

	public void setColors(byte[] colors) {
		this.colors = colors;
	}

	public void setDialogue(Dialogue d) {
		dialogue = d;
	}

	public Pets getPets() {
		return pets;
	}

	public int getBanDay() {
		return banDay;
	}

	public void setBanDay(int banDay) {
		this.banDay = banDay;
	}

	public int getBanYear() {
		return banYear;
	}

	public void setBanYear(int banYear) {
		this.banYear = banYear;
	}

	public int getMuteDay() {
		return muteDay;
	}

	public void setMuteDay(int muteDay) {
		this.muteDay = muteDay;
	}

	public int getMuteYear() {
		return muteYear;
	}

	public void setMuteYear(int muteYear) {
		this.muteYear = muteYear;
	}

	public int getBanLength() {
		return banLength;
	}

	public void setBanLength(int banLength) {
		this.banLength = banLength;
	}

	public int getMuteLength() {
		return muteLength;
	}

	public void setMuteLength(int muteLength) {
		this.muteLength = muteLength;
	}

	public boolean isStarter() {
		return starter;
	}

	public void setStarter(boolean starter) {
		this.starter = starter;
	}

	public int getPrayerInterface() {
		return prayerInterface;
	}

	public void setPrayerInterface(int prayerInterface) {
		this.prayerInterface = prayerInterface;
	}

	public short getKills() {
		return kills;
	}

	public short getDeaths() {
		return deaths;
	}

	public void setKills(short kills) {
		this.kills = kills;
	}

	public void setDeaths(short deaths) {
		this.deaths = deaths;
	}

	public Slayer getSlayer() {
		return slayer;
	}

	public EarningPotential getEarningPotential() {
		return earningPotential;
	}

	public int getDungPoints() {
		return dungPoints;
	}

	public void setDungPoints(int dungPoints) {
		this.dungPoints = dungPoints;
	}

	public void addDungPoints(int add) {
		dungPoints += add;
	}

	public int getYearCreated() {
		return yearCreated;
	}

	public void setYearCreated(int yearCreated) {
		this.yearCreated = yearCreated;
	}

	public int getDayCreated() {
		return dayCreated;
	}

	public void setDayCreated(int dayCreated) {
		this.dayCreated = dayCreated;
	}

	public boolean isYellMuted() {
		return yellMuted;
	}

	public void setYellMuted(boolean yellMuted) {
		this.yellMuted = yellMuted;
	}

	public int getLastLoginDay() {
		return lastLoginDay;
	}

	public void setLastLoginDay(int lastLoginDay) {
		this.lastLoginDay = lastLoginDay;
	}

	public int getLastLoginYear() {
		return lastLoginYear;
	}

	public void setLastLoginYear(int lastLoginYear) {
		this.lastLoginYear = lastLoginYear;
	}

	public void addToVotePoints(int incr) {
		votePoints += !GameConstants.IS_DOUBLE_EXP_WEEKEND ? ((memberRank > 0) && (Misc.isWeekend()) ? incr *2 :incr) : incr * 2;
		totalVotes++;

		if (totalVotes >= 100 && totalVotes % 100 == 0) {
			if (bank.hasSpaceFor(new Item(6199))) {
				getBank().add(new Item(6199));
				client.queueOutgoingPacket(new SendMessage("A mystery box has been added to your bank, go open it!"));
			} else if (inventory.hasSpaceFor(new Item(6199))) {
				getInventory().add(new Item(6199));
				client.queueOutgoingPacket(new SendMessage("A mystery box has been added to your inventory, open it!"));
			} else {
				inventory.addOrCreateGroundItem(6199, 1, true);
				client.queueOutgoingPacket(new SendMessage("Your bank and inventory are full, your mystery box is on the ground!"));
			}

			World.sendGlobalMessage(username + " has just reached " + totalVotes + " total lifetime votes!", true);
		} else {
			client.queueOutgoingPacket(new SendMessage((totalVotes <= 100 ? 100 - totalVotes : 100 - (totalVotes % 100)) + " votes left until random reward!"));
		}
	}

	public int getTotalVotes() {
		return totalVotes;
	}

	public void setTotalVotes(int totalVotes) {
		this.totalVotes = totalVotes;
	}

	public int getVotePoints() {
		return votePoints;
	}

	public void setVotePoints(int votePoints) {
		this.votePoints = votePoints;
	}

	public ItemDegrading getItemDegrading() {
		return degrading;
	}

	public long getUsernameToLong() {
		return usernameToLong;
	}

	public int getDonationPoints() {
		return donationPoints;
	}

	public void incrDonationPoints(int add) {
		donationPoints += add;
		QuestTab.updateDonatorPoints(this);
	}

	public void setDonationPoints(int donationPoints) {
		this.donationPoints = donationPoints;
	}

	public int getSlayerPoints() {
		return slayerPoints;
	}

	public void setSlayerPoints(int slayerPoints) {
		this.slayerPoints = slayerPoints;
	}

	public void addSlayerPoints(int amount) {
		slayerPoints += amount;
	}

	public byte getMusicVolume() {
		return musicVolume;
	}

	public void setMusicVolume(byte musicVolume) {
		this.musicVolume = musicVolume;
	}

	public byte getSoundVolume() {
		return soundVolume;
	}

	public void setSoundVolume(byte soundVolume) {
		this.soundVolume = soundVolume;
	}

	public boolean isBetaTester() {
		return betaTester;
	}

	public void setBetaTester(boolean betaTester) {
		this.betaTester = betaTester;
	}

	public Farming getFarming() {
		return farming;
	}

	public Questing getQuesting() {
		return questing;
	}

	public TimeStamp getVoteTime() {
		return voteTime;
	}

	public void setVoteTime(TimeStamp voteTime) {
		this.voteTime = voteTime;
	}

	public byte[] getPouches() {
		return pouches;
	}

	public void setPouches(byte[] pouches) {
		this.pouches = pouches;
	}

	public int getSkillPoints() {
		return skillPoints;
	}

	public void setSkillPoints(int skillPoints) {
		this.skillPoints = skillPoints;
	}
	
	public TargetMatch getTargetMatch() {
		return targetMatch;
	}
	
	public void setTargetMatch(TargetMatch targetMatch) {
		this.targetMatch = targetMatch;
	}
	
	public PenatlyTimer getPenaltyTimer() {
		return penaltyTimer;
	}
	
	public boolean hasBountyPenalty() {
		return penaltyTimer != null;
	}
	
	public void increaseHunterKills() {
		hunterKills++;
	}
	
	public void increaseRogueKills() {
		rogueKills++;
	}
	
	public int getHunterKills() {
		return hunterKills;
	}
	
	public int getRogueKills() {
		return rogueKills;
	}
	
	public void setPenaltyTimer(PenatlyTimer timer) {
		if(timer != null && timer.getPlayer() != this)
			return;
		this.penaltyTimer = timer;
	}
	
	public Player getLastKiller() {
		return lastKiller;
	}
	
	public void setLastKiller(Player killer) {
		this.lastKiller = killer;
	}
	
	public int getRXP() {
		return rxp;
	}
	
	public void addRxp(int rxp) {
		this.rxp += rxp;
	}
	
	public void subtractRxp(int rxp) {
		this.rxp -= rxp;
	}
	
	public void setCanLogin(boolean login) {
		this.canLogin = login;
	}
	
	public void setMemberRank(int rank) {
		this.memberRank = rank;
	}
	
	public int getMemberRank() {
		return memberRank;
	}
	
	public boolean isMember() {
		return memberRank > 0 && memberRank <= 3;
	}
	
	public boolean isSuperMember() {
		return memberRank >= 2 && memberRank <= 3;
	}
	
	public boolean isRespectedMember() {
		return memberRank == 3;
	}

	public String getRankName() {
		if(isRespectedMember())
			return "Respected Member";
		if(isSuperMember())
			return "Super Member";
		if(isMember())
			return "Member";
		return "None";
	}
	
	public Achievements getAchievements() {
		return achievements;
	}

	public Summoning getSummoning() {
		return summoning;
	}

	public long getLastAction() {
		return lastAction;
	}

	public void setLastAction(long lastAction) {
		this.lastAction = lastAction;
	}

	public int getPestPoints() {
		return pestPoints;
	}

	public void setPestPoints(int pestPoints) {
		this.pestPoints = pestPoints;
	}

	public int getBlackMarks() {
		return blackMarks;
	}

	public void setBlackMarks(int blackMarks) {
		this.blackMarks = blackMarks;
	}

	public PlayerTitles getTitles() {
		return titles;
	}

	public String getTitle() {
		return title;
	}
	
	public int getTitleColor() {
		return titleColor;
	}
	
	public void setTitleColor(int titleColor) {
		this.titleColor = titleColor;
		setAppearanceUpdateRequired(true);
	}

	public void setTitle(String title) {
		this.title = title;
		setAppearanceUpdateRequired(true);
		QuestTab.updateTitle(this);
	}
	
	public ItemDegrading getDegrading() {
		return degrading;
	}
	
	public RareDropEP getRareDropEP() {
		return rareDropEP;
	}
	
	public byte[] getQuickPrayersDefault() {
		return quickPrayersDefault;
	}

	public byte[] getQuickPrayersCurses() {
		return quickPrayersCurses;
	}
	
	public void setQuickPrayersDefault(byte[] quickPrayersDefault) {
		this.quickPrayersDefault = quickPrayersDefault;
	}

	public void setQuickPrayersCurses(byte[] quickPrayersCurses) {
		this.quickPrayersCurses = quickPrayersCurses;
	}
	
	public Loadout[] getCustomSpawnLoadouts() {
		return customSpawnLoadouts;
	}

	public void setCustomSpawnLoadouts(Loadout[] customSpawnLoadouts) {
		this.customSpawnLoadouts = customSpawnLoadouts;
	}

	public static enum ExperienceType {
		REGULAR, EASY;
	}
}
