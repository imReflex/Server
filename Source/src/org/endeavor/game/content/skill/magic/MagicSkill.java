package org.endeavor.game.content.skill.magic;

import java.io.Serializable;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.combat.impl.Attack;
import org.endeavor.game.content.dialogue.impl.WildernessWarning;
import org.endeavor.game.content.dialogue.impl.teleport.BossesTeleport;
import org.endeavor.game.content.dialogue.impl.teleport.QuestStartTeleport;
import org.endeavor.game.content.dialogue.impl.teleport.SkillingTeleport;
import org.endeavor.game.content.dialogue.impl.teleport.TownTeleport;
import org.endeavor.game.content.dialogue.impl.teleport.TrainingTeleport;
import org.endeavor.game.content.dialogue.impl.teleport.WildernessTeleport;
import org.endeavor.game.content.dialogue.impl.teleport.YanilleMinigamePortal;
import org.endeavor.game.content.minigames.armsrace.ARConstants;
import org.endeavor.game.content.skill.magic.spells.Charge;
import org.endeavor.game.content.skill.magic.spells.HighAlchemy;
import org.endeavor.game.content.skill.magic.spells.LowAlchemy;
import org.endeavor.game.content.skill.magic.spells.Vengeance;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.Projectile;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerConstants;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;
import org.endeavor.game.entity.player.net.out.impl.SendSidebarInterface;
import org.endeavor.game.entity.player.net.out.impl.SendSound;
import org.endeavor.game.entity.player.net.out.impl.SendString;

public class MagicSkill implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5046089926686414198L;
	public static final String MAGIC_ITEM_KEY = "magicitem";
	public static final int[][] TELEPORT_NAME_IDS = { { 19641, 21833, 30067 }, { 19722, 21933, 30078 },
			{ 19803, 22052, 30086 }, { 19960, 22123, 30109 }, { 20195, 22232, 30117 },  {20354, 22307, 30141},
			{20570, 22415, 30165}};

	public static final int[][] DESC_IDS = { { 19642, 21834, 30068 }, { 19723, 21934, 30079 }, { 19804, 22053, 30087 },
			{ 19961, 22124, 30110 }, { 20196, 22233, 30118 },  {20355, 22308, 30142},
			{20571, 22416, 30166}};

	public static final String[][] SPELLBOOK_STRINGS = { { "Training teleport", "Teleports you to Training areas" },
			{ "Mini-game teleport", "Teleports you to Mini-games" }, { "Boss teleport", "Teleports you to bosses" },
			{ "Skilling", "Teleports you to Skilling areas" }, { "Towns", "Teleports you to towns" },
			{ "Quests", "Teleports you to Quests" }, { "Pking", "Teleports you to Pking areas" },
			{ "None", "This teleport cannot be used" } };
	
	private transient Player player;
	private transient SpellCasting spellCasting;
	private boolean teleporting = false;
	private boolean vengeanceActive = false;
	private boolean ahrimEffectActive = false;

	private byte dragonFireShieldCharges = 0;

	private int magicBook = 0;

	private long lastVengeance = 0L;

	private SpellBookTypes spellBookType = SpellBookTypes.MODERN;

	private boolean dFireShieldEffect = false;
	private long dFireShieldTime = 0L;

	public static final int[][] AUTOCAST_BUTTONS = { { 84242, 21746 }, { 50091, 12891 }, { 50129, 12929 },
			{ 50223, 13023 }, { 50175, 12975 }, { 84241, 21745 }, { 50071, 12871 }, { 50111, 12911 }, { 50199, 12999 },
			{ 50151, 12951 }, { 86152, 22168 }, { 50081, 12881 }, { 50119, 12919 }, { 50221, 13011 }, { 50163, 12963 },
			{ 84220, 21744 }, { 50061, 12861 }, { 50101, 12901 }, { 50187, 12987 }, { 50139, 12939 }, { 6056, 1592 },
			{ 4165, 1189 }, { 4164, 1188 }, { 4161, 1185 }, { 4159, 1183 }, { 4168, 1192 }, { 4167, 1191 },
			{ 4166, 1190 }, { 4157, 1181 }, { 4153, 1177 }, { 6046, 1582 }, { 4151, 1175 }, { 4148, 1172 },
			{ 4145, 1169 }, { 4142, 1166 }, { 4139, 1163 }, { 6036, 1572 }, { 4136, 1160 }, { 4134, 1158 },
			{ 4132, 1156 }, { 4130, 1154 }, { 4128, 1152 } };

	public MagicSkill(Player player) {
		this.player = player;
		spellCasting = new SpellCasting(player);
	}

	public void onOperateDragonFireShield() {
		if (!player.getController().equals(ARConstants.AR_CONTROLLER)) {
			if (dragonFireShieldCharges == 0) {
				player.getClient().queueOutgoingPacket(new SendMessage("You do not have any charges on your shield."));
				return;
			}
			if ((!PlayerConstants.isOwner(player)) && (System.currentTimeMillis() - dFireShieldTime < 300000L)) {
				player.getClient().queueOutgoingPacket(new SendMessage("You must let your shield cool down before using it again."));
				return;
			}
		}

		dFireShieldEffect = (!dFireShieldEffect);

		if (dFireShieldEffect) {
			player.getClient().queueOutgoingPacket(new SendMessage("Your Dragonfire shield attack is now active."));

			Projectile p = new Projectile(1166);

			p.setStartHeight(25);
			p.setEndHeight(25);

			p.setDelay(40);
			p.setCurve(0);

			player.getCombat().getMagic().setpDelay((byte) 1);

			if (player.getCombat().getAttacking() == null) {
				player.getUpdateFlags().sendAnimation(6695, 0);
			}

			player.getCombat().getMagic().setAttack(new Attack(5, 5), new Animation(6696), new Graphic(1167, 45, true), new Graphic(1167, 0, true), p);
		}

		player.updateCombatType();
	}

	public void reset() {
		dFireShieldEffect = false;
		dFireShieldTime = System.currentTimeMillis();
	}

	public void onLogin() {
		for (int i = 0; i < TELEPORT_NAME_IDS.length; i++) {
			int[] names = TELEPORT_NAME_IDS[i];
			int[] desc = DESC_IDS[i];

			player.getClient().queueOutgoingPacket(new SendString(SPELLBOOK_STRINGS[i][0], names[0]));
			player.getClient().queueOutgoingPacket(new SendString(SPELLBOOK_STRINGS[i][0], names[1]));
			player.getClient().queueOutgoingPacket(new SendString(SPELLBOOK_STRINGS[i][0], names[2]));

			player.getClient().queueOutgoingPacket(new SendString(SPELLBOOK_STRINGS[i][1], desc[0]));
			player.getClient().queueOutgoingPacket(new SendString(SPELLBOOK_STRINGS[i][1], desc[1]));
			player.getClient().queueOutgoingPacket(new SendString(SPELLBOOK_STRINGS[i][1], desc[2]));
		}

		player.getClient().queueOutgoingPacket(new SendString("Yanille Home Teleport", 19220));
		player.getClient().queueOutgoingPacket(new SendString("Yanille Home Teleport", 21756));
	}

	public void setMagicBook(int magicBook) {
		this.magicBook = magicBook;
		player.getClient().queueOutgoingPacket(new SendSidebarInterface(6, magicBook));
		
		if (player.isActive()) {
			spellCasting.disableAutocast();
			Autocast.resetAutoCastInterface(player);
			player.updateCombatType();
		}

		switch (magicBook) {
		case 1151:
			player.getMagic().setSpellBookType(SpellBookTypes.MODERN);
			break;
		case 12855:
			player.getMagic().setSpellBookType(SpellBookTypes.ANCIENT);
			break;
		case 29999:
			player.getMagic().setSpellBookType(SpellBookTypes.LUNAR);
		}
	}

	public void teleport(MagicConstants.Teleports teleport, TeleportTypes teleportType) {
		teleport(teleport.getLocation().getX(), teleport.getLocation().getY(), teleport.getLocation().getZ(),
				teleportType);
	}

	public boolean canTeleport(TeleportTypes type) {
		if (player.isTeleblocked()) {
			player.getClient().queueOutgoingPacket(new SendMessage("You are tele-blocked."));
			return false;
		} else if (type == TeleportTypes.SPELL_BOOK && (player.getCombat().inCombat())
				&& (player.getController().equals(ControllerManager.WILDERNESS_CONTROLLER))) {
			player.getClient().queueOutgoingPacket(new SendMessage("You cannot cast this while in player combat, use a tele-tab!"));
			return false;
		} else if (teleporting) {
			return false;
		} else if (!player.getController().canTeleport()) {
			player.getClient().queueOutgoingPacket(new SendMessage("You can't teleport right now."));
			return false;
		} else if ((player.getCombat().inCombat()) && (player.getController().equals(ControllerManager.WILDERNESS_CONTROLLER))
				&& (type != TeleportTypes.TABLET)) {
			player.getClient().queueOutgoingPacket(new SendMessage("You cannot cast this right now."));
			return false;
		}

		return true;
	}

	public void useMagicOnItem(int id) {
		switch (id) {
		case 1162:
			spellCasting.cast(new LowAlchemy());
			break;
		case 1178:
			spellCasting.cast(new HighAlchemy());
			break;
		}
	}

	public boolean clickMagicButtons(int buttonId) {
		if (player.getController().equals(ARConstants.AR_CONTROLLER)) {
			return false;
		}
		
		if (Lodestone.clickButton(player, buttonId)) {
			return true;
		}
		
		if (buttonId == 26010) {
			//if (!player.getController().equals(ARConstants.AR_CONTROLLER)) {
				spellCasting.disableAutocast();
				Autocast.resetAutoCastInterface(player);
				player.updateCombatType();
			//}
			return true;
		}

		for (int i = 0; i < AUTOCAST_BUTTONS.length; i++) {
			if (buttonId == AUTOCAST_BUTTONS[i][0]) {
				//if (!player.getController().equals(ARConstants.AR_CONTROLLER)) {
					Autocast.setAutocast(player, AUTOCAST_BUTTONS[i][1]);
				//}s
				return true;
			}
		}

		switch (buttonId) {
		
		case 4169:
			player.getMagic().getSpellCasting().cast(new Charge());
			return true;
		case 118098:
			player.getMagic().getSpellCasting().cast(new Vengeance());
			return true;
		case 4171:
		case 50056:
		case 75010:
		case 84237:
		case 117048:

			player.getMagic().teleport(PlayerConstants.HOME.getX() + Misc.randomNumber(2), PlayerConstants.HOME.getY() + Misc.randomNumber(2), 0, TeleportTypes.SPELL_BOOK);
			return true;
		case 4140:
		case 50235:
		case 117112:
			
			if ((player.getCombat().inCombat())
					&& (player.getController().equals(ControllerManager.WILDERNESS_CONTROLLER))) {
				player.getClient().queueOutgoingPacket(new SendMessage("You cannot cast this right now."));
				return true;
			}

			player.start(new TrainingTeleport(player));
			return true;
		case 4143:
		case 50245:
		case 117123:
			
			if ((player.getCombat().inCombat())
					&& (player.getController().equals(ControllerManager.WILDERNESS_CONTROLLER))) {
				player.getClient().queueOutgoingPacket(new SendMessage("You cannot cast this right now."));
				return true;
			}

			player.start(new YanilleMinigamePortal(player));
			return true;
		case 4146:
		case 50253:
		case 117131:
			
			if ((player.getCombat().inCombat())
					&& (player.getController().equals(ControllerManager.WILDERNESS_CONTROLLER))) {
				player.getClient().queueOutgoingPacket(new SendMessage("You cannot cast this right now."));
				return true;
			}

			player.start(new BossesTeleport(player));
			return true;
		case 4150:
		case 51005:
		case 117154:
			
			player.start(new SkillingTeleport(player));
			return true;
		case 6004:
		case 51013:
		case 117162:
			
			player.start(new TownTeleport(player));
			return true;
			
		case 117186:
		case 51023:
		case 6005:
			player.start(new QuestStartTeleport(player));
			return true;
			
		case 29031://trollheim/pk tele
		case 51031:
		case 117210:
			player.start(new WildernessTeleport(player));
			return true;
			
		}

		return false;
	}

	public boolean clickMagicItems(int id) {
		switch (id) {
		case 8013:
			if (canTeleport(TeleportTypes.TABLET)) {
				teleport(MagicConstants.Teleports.HOME, TeleportTypes.TABLET);
				player.getInventory().remove(new Item(8013, 1));
				return true;
			}
			break;
		}
		return false;
	}

	public int hasRunes(Item[] runes) {
		Item[] items = player.getInventory().getItems();
		int k;
		for (int i = 0; i < runes.length; i++) {
			if (runes[i] != null) {
				if (!needsRune(runes[i].getId())) {
					runes[i] = null;
				} else {
					for (k = 0; k < items.length; k++)
						if (items[k] != null) {
							if (items[k].getId() == runes[i].getId()) {
								if (items[k].getAmount() < runes[i].getAmount()) {
									return items[k].getId();
								}
								runes[i] = null;
								break;
							}
						}
				}
			}
		}
		for (Item item : runes) {
			if (item != null) {
				return item.getId();
			}
		}

		return -1;
	}

	public void removeRunes(Item[] runes) {
		for (int i = 0; i < runes.length; i++)
			if ((runes[i] != null) && (needsRune(runes[i].getId()))) {
				if (runes[i].getId() == 561) {
					Item weapon = player.getEquipment().getItems()[3];

					if ((weapon != null) && (weapon.getId() == 18341) && (Misc.randomNumber(2) != 0));
					else
						player.getInventory().remove(runes[i]);
				} else {
					player.getInventory().remove(runes[i]);
				}
			}
	}

	public boolean needsRune(int runeId) {
		Item weapon = player.getEquipment().getItems()[3];
		Item shield = player.getEquipment().getItems()[3];

		if (weapon == null) {
			return true;
		}

		int wep = weapon.getId();

		switch (runeId) {
		case 556:
			if ((wep == 1381) || (wep == 1397) || (wep == 17293)) {
				return false;
			}
			return true;
		case 554:
			if ((wep == 1387) || (wep == 1393) || (wep == 17293)) {
				return false;
			}
			return true;
		case 555:
			if ((wep == 1383) || (wep == 1395) || (wep == 17293) || ((shield != null) && (shield.getId() == 18346))) {
				return false;
			}
			return true;
		case 557:
			if ((wep == 1385) || (wep == 1399) || (wep == 17293)) {
				return false;
			}
			return true;
		}
		return true;
	}

	public void teleportNoWildernessRequirement(int x, int y, int z, TeleportTypes type) {
		if (teleporting)
			return;
		if (player.isTeleblocked()) {
			player.getClient().queueOutgoingPacket(new SendMessage("You are tele-blocked."));
			return;
		}
		if (!player.getController().canTeleport()) {
			player.getClient().queueOutgoingPacket(new SendMessage("You can't teleport right now."));
			return;
		}

		if (player.isDead()) {
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			return;
		}

		initTeleport(x, y, z, type);
	}

	public void teleport(int x, int y, int z, TeleportTypes type) {
		if (!canTeleport(type) || player.isDead()) {
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			return;
		}

		initTeleport(x, y, z, type);
	}
	
	
	public void doWildernessTeleport(final int x, final int y, final int z, final TeleportTypes type) {
		
		player.start(new WildernessWarning(player) {

			@Override
			public void onConfirm() {
				player.setTakeDamage(false);
				player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
				player.getController().onTeleport(player);
				teleporting = true;
				int delay = 3;
				switch (type) {
				case TABLET:
					player.getUpdateFlags().sendAnimation(MagicConstants.TABLET_BREAK_ANIMATION);
					TaskQueue.queue(new Task(player, 1, false, Task.StackType.STACK, Task.BreakType.NEVER, 5) {
						/**
						 * 
						 */
						private static final long serialVersionUID = -7428792423902512046L;

						@Override
						public void execute() {
							player.getUpdateFlags().sendAnimation(MagicConstants.TABLET_TELEPORT_ANIMATION);
							player.getUpdateFlags().sendGraphic(MagicConstants.TABLET_TELEPORT_GRAPHIC);
							stop();
						}

						@Override
						public void onStop() {
						}
					});
					break;
				case TELE_OTHER:
					player.getUpdateFlags().sendAnimation(1816, 0);
					player.getUpdateFlags().sendGraphic(new Graphic(342, 0, false));
					break;
				default:
					switch (spellBookType) {
					case ANCIENT:
						player.getUpdateFlags().sendAnimation(MagicConstants.ANCIENT_TELEPORT_ANIMATION);
						player.getUpdateFlags().sendGraphic(MagicConstants.ANCIENT_TELEPORT_GRAPHIC);
						delay = 4;
						break;
					case LUNAR:
						player.getUpdateFlags().sendAnimation(9606, 0);
						player.getUpdateFlags().sendGraphic(new Graphic(1685, 0, false));
						delay = 4;
						break;
					default:
						player.getClient().queueOutgoingPacket(new SendSound(202, 1, 0));
						player.getUpdateFlags().sendAnimation(MagicConstants.MODERN_TELEPORT_ANIMATION);
						player.getUpdateFlags().sendGraphic(MagicConstants.MODERN_TELEPORT_GRAPHIC);
						delay = 4;
					}

					break;
				}

				TaskQueue.queue(new Task(player, delay, false, Task.StackType.STACK, Task.BreakType.NEVER, 5) {
					/**
					 * 
					 */
					private static final long serialVersionUID = -5601482144705027744L;

					@Override
					public void execute() {
						if (!player.getController().canTeleport()) {
							player.setTakeDamage(true);
							teleporting = false;
							return;
						}

						TaskQueue.onMovement(player);

						player.teleport(new Location(x, y, z));
						player.setTakeDamage(true);
						teleporting = false;

						switch (type) {
						case SPELL_BOOK:
							player.getUpdateFlags().sendAnimation(MagicConstants.MODERN_TELEPORT_END_ANIMATION);
							switch (spellBookType) {
							case MODERN:
								player.getUpdateFlags().sendGraphic(MagicConstants.MODERN_TELEPORT_END_GRAPHIC);
								break;
							default:
								break;
							}
							break;
						case TABLET:
							player.getUpdateFlags().sendAnimation(MagicConstants.TABLET_TELEPORT_END_ANIMATION);
							break;
						default:
							break;
						}

						stop();
					}

					@Override
					public void onStop() {
					}
				});
			}
			
		});
	}

	private void initTeleport(final int x, final int y, final int z, final TeleportTypes type) {
		if (Entity.inWilderness(x, y)) {
			doWildernessTeleport(x, y, z, type);
			return;
		}
		
		player.setTakeDamage(false);
		player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
		player.getController().onTeleport(player);
		teleporting = true;
		int delay = 3;
		switch (type) {
		case TABLET:
			player.getUpdateFlags().sendAnimation(MagicConstants.TABLET_BREAK_ANIMATION);
			TaskQueue.queue(new Task(player, 1, false, Task.StackType.STACK, Task.BreakType.NEVER, 5) {
				/**
				 * 
				 */
				private static final long serialVersionUID = 4934960396061853911L;

				@Override
				public void execute() {
					player.getUpdateFlags().sendAnimation(MagicConstants.TABLET_TELEPORT_ANIMATION);
					player.getUpdateFlags().sendGraphic(MagicConstants.TABLET_TELEPORT_GRAPHIC);
					stop();
				}

				@Override
				public void onStop() {
				}
			});
			break;
		case TELE_OTHER:
			player.getUpdateFlags().sendAnimation(1816, 0);
			player.getUpdateFlags().sendGraphic(new Graphic(342, 0, false));
			break;
		default:
			switch (spellBookType) {
			case ANCIENT:
				player.getUpdateFlags().sendAnimation(MagicConstants.ANCIENT_TELEPORT_ANIMATION);
				player.getUpdateFlags().sendGraphic(MagicConstants.ANCIENT_TELEPORT_GRAPHIC);
				delay = 4;
				break;
			case LUNAR:
				player.getUpdateFlags().sendAnimation(9606, 0);
				player.getUpdateFlags().sendGraphic(new Graphic(1685, 0, false));
				delay = 4;
				break;
			default:
				player.getClient().queueOutgoingPacket(new SendSound(202, 1, 0));
				player.getUpdateFlags().sendAnimation(MagicConstants.MODERN_TELEPORT_ANIMATION);
				player.getUpdateFlags().sendGraphic(MagicConstants.MODERN_TELEPORT_GRAPHIC);
				delay = 4;
			}

			break;
		}

		TaskQueue.queue(new Task(player, delay, false, Task.StackType.STACK, Task.BreakType.NEVER, 5) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -8253262215031926901L;

			@Override
			public void execute() {
				if (!player.getController().canTeleport()) {
					player.setTakeDamage(true);
					teleporting = false;
					return;
				}

				TaskQueue.onMovement(player);

				player.teleport(new Location(x, y, z));
				player.setTakeDamage(true);
				teleporting = false;

				switch (type) {
				case SPELL_BOOK:
					player.getUpdateFlags().sendAnimation(MagicConstants.MODERN_TELEPORT_END_ANIMATION);
					switch (spellBookType) {
					case MODERN:
						player.getUpdateFlags().sendGraphic(MagicConstants.MODERN_TELEPORT_END_GRAPHIC);
						break;
					default:
						break;
					}
					break;
				case TABLET:
					player.getUpdateFlags().sendAnimation(MagicConstants.TABLET_TELEPORT_END_ANIMATION);
					break;
				default:
					break;
				}

				stop();
			}

			@Override
			public void onStop() {
			}
		});
	}

	public void activateVengeance() {
		vengeanceActive = true;
		lastVengeance = System.currentTimeMillis();
	}

	public boolean isVengeanceActive() {
		return vengeanceActive;
	}

	public void deactivateVengeance() {
		vengeanceActive = false;
	}

	public long getLastVengeance() {
		return lastVengeance;
	}

	public SpellBookTypes getSpellBookType() {
		return spellBookType;
	}

	public void setSpellBookType(SpellBookTypes spellBookType) {
		this.spellBookType = spellBookType;
	}

	public SpellCasting getSpellCasting() {
		return spellCasting;
	}

	public boolean isTeleporting() {
		return teleporting;
	}

	public boolean isAhrimEffectActive() {
		return ahrimEffectActive;
	}

	public void setAhrimEffectActive(boolean ahrimEffectActive) {
		this.ahrimEffectActive = ahrimEffectActive;
	}

	public int getMagicBook() {
		return magicBook;
	}

	public void incrDragonFireShieldCharges() {
		dragonFireShieldCharges = ((byte) (dragonFireShieldCharges + 1));
		if (dragonFireShieldCharges == 50)
			player.getClient().queueOutgoingPacket(new SendMessage("Your Dragonfire shield is now fully charged."));
		else if (dragonFireShieldCharges > 50)
			dragonFireShieldCharges = 50;
	}

	public void decrDragonFireShieldCharges() {
		dragonFireShieldCharges = ((byte) (dragonFireShieldCharges - 1));

		if (dragonFireShieldCharges == 0)
			player.getClient().queueOutgoingPacket(new SendMessage("Your Dragonfire shield is now empty."));
	}

	public byte getDragonFireShieldCharges() {
		return dragonFireShieldCharges;
	}

	public void setDragonFireShieldCharges(int dragonFireShieldCharges) {
		this.dragonFireShieldCharges = ((byte) dragonFireShieldCharges);
	}

	public boolean isDFireShieldEffect() {
		return dFireShieldEffect;
	}

	public void setDFireShieldEffect(boolean dFireShieldEffect) {
		this.dFireShieldEffect = dFireShieldEffect;
	}

	public void setVengeanceActive(boolean vengeanceActive) {
		this.vengeanceActive = vengeanceActive;
	}

	public static enum SpellBookTypes {
		MODERN, ANCIENT, LUNAR;
	}

	public static enum TeleportTypes {
		SPELL_BOOK, TABLET, TELE_OTHER;
	}
	
	public void initPlayer(Player player) {
		this.player = player;
		if(spellCasting == null)
			spellCasting = new SpellCasting(this.player);
	}
}
