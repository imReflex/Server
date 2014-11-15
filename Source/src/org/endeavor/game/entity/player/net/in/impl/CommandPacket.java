package org.endeavor.game.entity.player.net.in.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import org.endeavor.GameSettings;
import org.endeavor.engine.cache.map.RSObject;
import org.endeavor.engine.cache.map.Region;
import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.engine.network.mysql.MySQL;
import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.game.content.Bank;
import org.endeavor.game.content.MobControl;
import org.endeavor.game.content.Yelling;
import org.endeavor.game.content.clans.Clan;
import org.endeavor.game.content.clans.Clans;
import org.endeavor.game.content.combat.Hit;
import org.endeavor.game.content.combat.impl.MaxHitFormulas;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.content.dialogue.impl.ChangePasswordDialogue;
import org.endeavor.game.content.dialogue.impl.ConfirmDialogue;
import org.endeavor.game.content.dialogue.impl.SelectTitleDialogue;
import org.endeavor.game.content.dwarfcannon.DwarfMultiCannon;
import org.endeavor.game.content.io.PlayerSave;
import org.endeavor.game.content.io.PlayerSaveUtil;
import org.endeavor.game.content.minigames.armsrace.ArmsRaceLobby;
import org.endeavor.game.content.minigames.dungeoneering.DungLobby;
import org.endeavor.game.content.quest.QuestConstants;
import org.endeavor.game.content.skill.Skill;
import org.endeavor.game.content.skill.SkillConstants;
import org.endeavor.game.content.skill.crafting.JewelryCreationTask;
import org.endeavor.game.content.skill.magic.MagicSkill;
import org.endeavor.game.content.skill.smithing.SmithingConstants;
import org.endeavor.game.content.sounds.PlayerSounds;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.Projectile;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.following.Following;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.item.impl.GroundItemHandler;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.mob.MobDrops;
import org.endeavor.game.entity.mob.VirtualMobRegion;
import org.endeavor.game.entity.object.GameObject;
import org.endeavor.game.entity.object.ObjectManager;
import org.endeavor.game.entity.pathfinding.RS317PathFinder;
import org.endeavor.game.entity.pathfinding.StraightPathFinder;
import org.endeavor.game.entity.player.EquipmentConstants;
import org.endeavor.game.entity.player.ItemCheck;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerConstants;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.in.IncomingPacket;
import org.endeavor.game.entity.player.net.in.command.CommandManager;
import org.endeavor.game.entity.player.net.out.impl.SendAltConfig;
import org.endeavor.game.entity.player.net.out.impl.SendAnimateObject;
import org.endeavor.game.entity.player.net.out.impl.SendConfig;
import org.endeavor.game.entity.player.net.out.impl.SendFlashSidebarIcon;
import org.endeavor.game.entity.player.net.out.impl.SendInterface;
import org.endeavor.game.entity.player.net.out.impl.SendInventoryInterface;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendModelAnimation;
import org.endeavor.game.entity.player.net.out.impl.SendMoveCamera;
import org.endeavor.game.entity.player.net.out.impl.SendObject;
import org.endeavor.game.entity.player.net.out.impl.SendQuickSong;
import org.endeavor.game.entity.player.net.out.impl.SendResetCamera;
import org.endeavor.game.entity.player.net.out.impl.SendShakeScreen;
import org.endeavor.game.entity.player.net.out.impl.SendSound;
import org.endeavor.game.entity.player.net.out.impl.SendStillGraphic;
import org.endeavor.game.entity.player.net.out.impl.SendString;
import org.endeavor.game.entity.player.net.out.impl.SendSystemBan;
import org.endeavor.game.entity.player.net.out.impl.SendTurnCamera;
import org.endeavor.game.entity.player.net.out.impl.SendUpdateItems;

public class CommandPacket extends IncomingPacket {
	public static String[] commands = { "yell", "changepass", "train", "vote", "rules", "skull", "pure" };

	@Override
	public void handle(Player player, StreamBuffer.InBuffer in, int opcode, int length) {
		String command = in.readString();
		String[] split = command.split(" ");

		String check = command.toLowerCase();

		if (check.startsWith("/")) {
			if(player.getClanChannel() != null) {
				player.getClanChannel().interpret(player, command);
			} else {
				player.send(new SendMessage("You are not in a clan."));
			}
			return;
		}
		if(check.startsWith("create")) {
			System.out.println("creating clan..");
			Clans.createClan(player, command.substring(7));
		}
		
		/*if(check.startsWith("spawno")) {
			player.getObjects().add(new GameObject(Integer.valueOf(split[1]), player.getLocation(), 10, 0));
			//player.send(new SendObject(player, new GameObject(Integer.valueOf(split[1]), player.getLocation(), 0, 0)));
		}*/
		
		if(check.startsWith("accept")) {
			String name = command.substring(7);
			Player clanOwner = World.getPlayerByName(name);
			if(clanOwner != null) {
				Clan clan = Clans.getClanForOwner(clanOwner);
				if(clan != null) {
					clan.acceptInvite(player);
				}
			} else {
				player.send(new SendMessage("This player is not online."));
			}
		}
		
		if(check.startsWith("acceptwar")) {
			String name = command.substring(10);
			Player clanOwner = World.getPlayerByName(name);
			if(clanOwner != null) {
				Clan clan = Clans.getClanForOwner(player);
				Clan otherClan = Clans.getClanForOwner(clanOwner);
				if(clan == otherClan) {
					player.send(new SendMessage("This player is in your clan!"));
					return;
				}
				if(clan != null) {
					if(otherClan != null) {
						if(otherClan.challengeQueueContains(player))
							otherClan.acceptChallenge(player);
						else
							clan.sendChallengeRequest(player, clanOwner);
					} else {
						player.send(new SendMessage("This player does not own a clan."));
					}
				} else {
					player.send(new SendMessage("You are not the owner of a clan."));
				}
			} else {
				player.send(new SendMessage("This player is not online."));
			}
		}
		
		if(check.equals("leave")) {
			Clan clan = Clans.getClanForPlayer(player);
			if(clan != null) {
				clan.leaveClan(player);
			}
		}

		if (check.equals("setlevel")) {
			int id = Integer.parseInt(split[1]);
			if(id > 6) {
				player.send(new SendMessage("You can not set this level."));
				return;
			}
			int lvl = Integer.parseInt(split[2]);

			player.getLevels()[id] = ((byte) lvl);
			player.getMaxLevels()[id] = ((byte) lvl);
			player.getSkill().getExperience()[id] = player.getSkill().getXPForLevel(id, lvl);
			player.getSkill().update(id);

			player.getSkill().updateCombatLevel();
			return;
		}
		if (check.startsWith("item")) {
			try {
				if (split.length == 2) {
					int item = Integer.parseInt(split[1]);
					if(item >= 20100) {
						player.send(new SendMessage("You can not spawn this item."));
						return;
					}
					player.getInventory().add(item, 1);
				} else if (split.length == 3) {
					int item = Integer.parseInt(split[1]);
					if(item >= 20100) {
						player.send(new SendMessage("You can not spawn this item."));
						return;
					}
					int amount = Integer.parseInt(split[2]);
					player.getInventory().add(item, amount);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		
		if (check.equals("skull")) {
			player.getSkulling().skull(player, null);
		}
		
		if (split[0].equalsIgnoreCase("string")) {
			int frameId = Integer.parseInt(check.substring(7));
			player.getClient().queueOutgoingPacket(new SendString("test: " + frameId, frameId));
			player.getClient().queueOutgoingPacket(new SendMessage("Sending new string: " + frameId));
		}

		if (player.getRights() >= 2) {
		
			if (check.startsWith("players")) {
				/*int c = 0;
	
				player.getClient().queueOutgoingPacket(new SendString("Players online: " + World.getActivePlayers(), 8144));
	
				while (c <= 50) {
					player.getClient().queueOutgoingPacket(new SendString("", 8147 + c));
					c++;
				}
	
				c = 0;
	
				while (c <= 49) {
					player.getClient().queueOutgoingPacket(new SendString("", 12174 + c));
					c++;
				}
	
				c = 0;
	
				for (Player p : World.getPlayers()) {
					if (p != null) {
						if (c <= 50)
							player.getClient().queueOutgoingPacket(new SendString(p.getUsername(), 8147 + c));
						else {
							player.getClient().queueOutgoingPacket(new SendString(p.getUsername(), 12174 + c - 50));
						}
	
						c++;
					}
				}
	
				player.getClient().queueOutgoingPacket(new SendInterface(8134));*/
				
				player.getClient().queueOutgoingPacket(new SendMessage("There are currently " + World.getActivePlayers() +" players online."));
				return;
			}
		}

		if (check.startsWith("-")) {
			try {
				Yelling.yell(player, command.substring(5, command.length()));
			} catch (Exception e) {
				player.getClient().queueOutgoingPacket(new SendMessage("Invalid yell format, syntax: -messsage"));
			}
			return;
		}

		if (check.equals("rules")) {
			player.rules();
			return;
		}
		
		if (check.equals("pure")) {
			player.start(new ConfirmDialogue(player, new String[] {"This will replace your current combat stats.", 
					"Any Prayer or Defence levels will be reset.", "Are you sure you want to do this?"}) {
				public void onConfirm() {
					if (!player.getController().equals(ControllerManager.DEFAULT_CONTROLLER)) {
						player.getClient().queueOutgoingPacket(new SendMessage("You can't use this here."));
						return;
					}
		
					if (!player.getController().equals(ControllerManager.DEFAULT_CONTROLLER)) {
						player.getClient().queueOutgoingPacket(new SendMessage("You cannot do this here."));
						return;
					}
		
					if (ItemCheck.hasEquipmentOn(player)) {
						player.getClient().queueOutgoingPacket(
								new SendMessage("You must remove your equipment to do this command."));
						return;
					}
		
					player.getSkill().getExperience()[1] = 0.0D;
					player.getLevels()[1] = 1;
					player.getMaxLevels()[1] = 1;
					player.getSkill().update(1);
		
					player.getSkill().getExperience()[5] = Skill.EXP_FOR_LEVEL[50];
					player.getLevels()[5] = 52;
					player.getMaxLevels()[5] = 52;
					player.getSkill().update(5);
		
					for (int i = 0; i <= 6; i++) {
						if ((i != 1) && (i != 5)) {
							player.getSkill().getExperience()[i] = Skill.EXP_FOR_LEVEL[99];
							player.getLevels()[i] = 99;
							player.getMaxLevels()[i] = 99;
							player.getSkill().update(i);
						}
					}
					player.getSkill().updateCombatLevel();
				}
			});
		}

		if ((check.startsWith("yell")) && (!check.contains("yellmute"))) {
			try {
				Yelling.yell(player, command.substring(5, command.length()));
			} catch (Exception e) {
				player.getClient().queueOutgoingPacket(new SendMessage("Invalid yell format, syntax: ::yell messsage"));
			}
			return;
		}

		if (check.startsWith("train")) {
			player.getMagic().teleport(3161, 4237, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
			return;
		}

		/*if (check.startsWith("changepass")) {
			try {
				String password = command.substring("changepass".length()).trim();
				if ((password.length() > 4) && (password.length() < 15))
					player.start(new ChangePasswordDialogue(player, password));
				else
					DialogueManager.sendStatement(player,
							new String[] { "Your password be between 4 and 15 characters." });
			} catch (Exception e) {
				player.getClient().queueOutgoingPacket(
						new SendMessage("Invalid password format, syntax: ::changepass password here"));
			}
			return;
		}*/

		if (player.getRights() <= 2 && (!PlayerConstants.isOwner(player)) && (!GameSettings.DEV_MODE)) {
			String s = "The available commands are: ";

			for (String i : commands) {
				s = s + "'" + i + "', ";
			}

			player.getClient().queueOutgoingPacket(new SendMessage(s));
			return;
		}

		

		handleCommand(player, split[0].toLowerCase(), Arrays.copyOfRange(split, 1, split.length));

		CommandManager.handleCommand(player, command);
	}

	@Override
	public int getMaxDuplicates() {
		return 1;
	}

	public void handleCommand(final Player player, String keyword, String[] args) {
		try {
			/**
			 * Player mod+ commands
			 */
			if (player.getRights() >= 3 || PlayerConstants.isOwner(player)) {
				if (keyword.equals("tele")) {
					int x = Integer.parseInt(args[0]);
					int y = Integer.parseInt(args[1]);
					int z = 0;
					
					if (args.length > 2) {
						z = Integer.parseInt(args[2]);
					}
					
					player.teleport(new Location(x, y, z));
					return;
				}
				
				if (keyword.startsWith("packetlog")) {
					String name = args[0];
					Player p = World.getPlayerByName(name.replaceAll("_", " "));
					if (p == null) {
						player.getClient().queueOutgoingPacket(new SendMessage("Player not found."));
					} else {
						p.getClient().setLogPlayer(true);
						player.getClient().queueOutgoingPacket(
								new SendMessage("Now logging incoming packets for: " + p.getUsername() + "."));
					}

					return;
				}

				if (keyword.startsWith("unpacketlog")) {
					String name = args[0].replaceAll("_", " ");

					Player p = World.getPlayerByName(name);
					if (p == null) {
						player.getClient().queueOutgoingPacket(new SendMessage("Player not found."));
					} else {
						p.getClient().setLogPlayer(false);
						player.getClient().queueOutgoingPacket(
								new SendMessage("No longer logging incoming packets for: " + p.getUsername() + "."));
					}

					return;
				}

				if (keyword.startsWith("kick")) {
					String name = args[0].replaceAll("_", " ");

					Player p = World.getPlayerByName(name);
					if (p == null) {
						player.getClient().queueOutgoingPacket(new SendMessage("Player not found."));
					} else {
						p.logout(true);
						player.getClient().queueOutgoingPacket(new SendMessage("Kicked."));
					}

					return;
				}
				
				/*if (keyword.equals("disableplayershops")) {
					PlayerOwnedShop.setDisabled(true);
				}

				if (keyword.equals("enableplayershops")) {
					PlayerOwnedShop.setDisabled(false);
				}*/
				
				if (keyword.equals("ipban")) {
					String name = args[0].replaceAll("_", " ");

					Player p = World.getPlayerByName(name);

					if (p == null) {
						player.getClient().queueOutgoingPacket(new SendMessage("Player not found."));
					} else {
						player.getClient().queueOutgoingPacket(new SendMessage("Success."));
						new SendSystemBan().execute(p.getClient());
						PlayerSaveUtil.setIPBanned(p);
						p.logout(true);
					}
				}
				
				if (keyword.equals("ipmute")) {
					String name = args[0].replaceAll("_", " ");

					Player p = World.getPlayerByName(name);

					if (p == null) {
						player.getClient().queueOutgoingPacket(new SendMessage("Player not found."));
					} else {
						PlayerSaveUtil.setIPMuted(p);
						player.getClient().queueOutgoingPacket(new SendMessage("Success."));
						p.setMuted(true);
					}
				}

			}

			if ((PlayerConstants.isOwner(player)) || (GameSettings.DEV_MODE)) {
				byte offsetY;
				if (GameSettings.DEV_MODE || (player.getUsername().equalsIgnoreCase("mikey") || player.getUsername().equalsIgnoreCase("arithium"))) {
					if (keyword.startsWith("ecosearch")) {
						int id = Integer.parseInt(args[0]);

						long amount = 0L;

						for (Player p : World.getPlayers()) {
							if ((p != null) && (p.isActive())) {
								amount += p.getInventory().getItemAmount(id);
								amount += p.getBank().getItemAmount(id);
							}
						}

						player.getClient().queueOutgoingPacket(
								new SendMessage("There is current " + amount + " of item: "
										+ Item.getDefinition(id).getName()));

						return;
					}
					
					
					
					if (keyword.equals("emptybank")) {
						player.getBank().setItems(new Item[player.getBank().getItems().length]);
					}
					
					if (keyword.equals("banktab")) {
						for (int i = 0; i < Bank.ONE_TAB_SIZE; i++) {
							player.getBank().add(new Item(1050 + i, 1));
						}
							
					}
					
					if (keyword.equals("50votes")) {
						for (int i = 0; i < 50; i++) {
							player.addToVotePoints(5);
						}
					}

					if (keyword.equals("npc")) {
						Mob mob = new Mob(Integer.parseInt(args[0]), true, new Location(player.getLocation()));
						player.getClient().queueOutgoingPacket(new SendMessage("Spawned NPC index: " + mob.getIndex()));
					}
					
					if (keyword.equals("getindexforname")) {
						String name = args[0];
						
						int count = 0;
						
						for (Mob i : World.getNpcs()) {
							if (i != null && i.getDefinition().getName().toLowerCase().contains(name.toLowerCase())) {
								count++;
								player.getClient().queueOutgoingPacket(new SendMessage("Mob found at index: " + i.getIndex()));
							}
						}
						
						if (count == 0) {
							player.getClient().queueOutgoingPacket(new SendMessage("No mob found with name: " + name));
						}
					}
					
					if (keyword.equals("teletomobindex")) {
						int index = Integer.parseInt(args[0]);
						
						if (World.getNpcs()[index] != null) {
							player.teleport(new Location(World.getNpcs()[index].getLocation()));
						} else {
							player.getClient().queueOutgoingPacket(new SendMessage("Mob not found at index: " + index));
						}
					}
					
					if (keyword.equals("flash")) {
						player.send(new SendFlashSidebarIcon(135));
					}

					if (keyword.equals("giveitem")) {
						String name = args[0].replaceAll("_", " ");

						Player p = World.getPlayerByName(name);
						if (p == null) {
							player.getClient().queueOutgoingPacket(new SendMessage("Player not found."));
						} else {
							p.getInventory().add(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
							player.getClient().queueOutgoingPacket(new SendMessage("Item successfully given."));
						}
					}

					if (keyword.equals("p")) {
						Projectile p = new Projectile(Integer.parseInt(args[0]));
						Location l = new Location(player.getLocation().getX() + 3, player.getLocation().getY());

						byte offsetX = (byte) ((player.getLocation().getY() - l.getY()) * -1);
						offsetY = (byte) ((player.getLocation().getX() - l.getX()) * -1);

						World.sendProjectile(p, l, -1, offsetX, offsetY);
					}

					if (keyword.equals("rd")) {
						try {
							int id = Integer.parseInt(args[0]);
							Mob m = new Mob(id, false, player.getLocation());
							MobDrops.dropItems(player, m);
							m.remove();
						} catch (Exception e) {
							e.printStackTrace();
						}

						return;
					}

					if (keyword.equals("logtasks")) {
						try {
							// TaskQueue.log();
							player.getClient().queueOutgoingPacket(new SendMessage("Tasks have been logged."));
						} catch (Exception e) {
							e.printStackTrace();
						}

						return;
					}

					if (keyword.equals("dfirecharges")) {
						for (int i = 0; i < 51; i++) {
							player.getMagic().incrDragonFireShieldCharges();
						}
					}

					if (keyword.equals("disablerares")) {
						MobDrops.setRares(false);
						player.getClient().queueOutgoingPacket(new SendMessage("Rares have been disabled."));
					}

					if (keyword.equals("enablerares")) {
						MobDrops.setRares(true);
						player.getClient().queueOutgoingPacket(new SendMessage("Rares have been enabled."));
					}

					if (keyword.equals("disabledung")) {
						DungLobby.setDisabled(true);
						player.getClient().queueOutgoingPacket(new SendMessage("Dungeoneering has been disabled."));
					}

					if (keyword.equals("resetquest")) {
						player.getQuesting().reset(Integer.parseInt(args[0]));
					}
					
					if (keyword.equals("getqueststage")) {
						player.getClient().queueOutgoingPacket(new SendMessage("Stage: " + player.getQuesting().getQuestStage(QuestConstants.QUESTS_BY_ID[Integer.parseInt(args[0])])));
					}

					if (keyword.equals("setqueststage")) {
						player.getQuesting().setQuestStage(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
					}

					if (keyword.equals("enabledung")) {
						DungLobby.setDisabled(false);
						player.getClient().queueOutgoingPacket(new SendMessage("Dungeoneering has been enabled."));
					}

					if (keyword.equals("disablear")) {
						ArmsRaceLobby.setDisabled(true);
						player.getClient().queueOutgoingPacket(new SendMessage("AR has been disabled."));
					}

					if (keyword.equals("enablear")) {
						ArmsRaceLobby.setDisabled(false);
						player.getClient().queueOutgoingPacket(new SendMessage("AR has been enabled."));
					}

					if (keyword.equals("cleargi")) {
						synchronized (GroundItemHandler.getActive()) {
							GroundItemHandler.getActive().clear();
						}
					}
				}
				
				if (keyword.equals("title")) {
					player.start(new SelectTitleDialogue(player));
				}
				
				if (keyword.equals("ach")) {
					player.getAchievements().setAllCompleted();
				}
				
				if (keyword.equals("teleblock")) {
					player.teleblock(30);
				}
				
				if (keyword.equals("clip")) {
					player.getClient().queueOutgoingPacket(new SendMessage("" + Region.getRegion(player.getX(), player.getY()).getClip(player.getX(), player.getY(), player.getZ())));
				}
				
				if (keyword.equals("getobjectid")) {
					player.getClient().queueOutgoingPacket(new SendMessage("" + Region.getObject(player.getX(), player.getY(), player.getZ()).getId()));
				}

				if (keyword.equals("interface")) {
					player.getClient().queueOutgoingPacket(new SendInterface(Integer.parseInt(args[0])));
				}

				if (keyword.equals("shake")) {
					player.getClient().queueOutgoingPacket(new SendShakeScreen(true));
				}

				if (keyword.equals("stopshake")) {
					player.getClient().queueOutgoingPacket(new SendShakeScreen(false));
				}

				if (keyword.equals("headicon")) {
					player.getPrayer().setHeadIcon((byte) Integer.parseInt(args[0]));
					player.setAppearanceUpdateRequired(true);
				}

				if (keyword.equals("fadetele")) {
					player.doFadeTeleport(new Location(Integer.parseInt(args[0]), Integer.parseInt(args[1])), true);
				}

				if (keyword.equals("emptybank")) {
					player.getBank().clear();
				}

				if (keyword.equals("flashicon"))
					player.getClient().queueOutgoingPacket(new SendFlashSidebarIcon(Integer.parseInt(args[0])));

				if (keyword.equals("donarpoints")) {
					String user = args[0].replaceAll("_", " ");
					int am = Integer.parseInt(args[1]);

					Player p = World.getPlayerByName(user);

					if (p == null) {
						player.getClient().queueOutgoingPacket(
								new SendMessage("User not found, syntax = ::donarpoints this_is_a_username 10"));
						return;
					}

					p.incrDonationPoints(am);
					player.getClient().queueOutgoingPacket(
							new SendMessage(am + " Donator points given to " + user + "."));
					return;
				}

				if (keyword.equals("setbetatester")) {
					String user = args[0].replaceAll("_", " ");

					Player p = World.getPlayerByName(user);

					if (p == null) {
						player.getClient().queueOutgoingPacket(
								new SendMessage("User not found, syntax = ::betatester this_is_a_username"));
						return;
					}

					p.setBetaTester(!p.isBetaTester());
					player.getClient().queueOutgoingPacket(
							new SendMessage(p.getUsername() + " is beta tester? : " + p.isBetaTester()));
					return;
				}

				if (keyword.equals("stringupdate")) {
					player.getClient().queueOutgoingPacket(new SendString(args[0], Integer.parseInt(args[1])));
				}

				if (keyword.equals("dokc")) {
					for (int i = 0; i < 75; i++) {
						player.getMinigames().incrGWKC(Integer.parseInt(args[0]));
					}
				}
				if (keyword.equals("poison")) {
					player.poison(2);
				}

				if (keyword.equals("logout")) {
					player.logout(true);
				}

				if (keyword.equals("rhp")) {
					int tmp1710_1709 = 3;
					short[] tmp1710_1706 = player.getLevels();
					tmp1710_1706[tmp1710_1709] = ((short) (tmp1710_1706[tmp1710_1709] - 10));
					player.getSkill().update(3);
				}

				if (keyword.equals("resetlevels")) {
					for (int i = 0; i < 25; i++) {
						player.getSkill().reset(i);
					}
				}

				if (keyword.equals("hair")) {
					player.getAppearance()[3] = ((byte) Integer.parseInt(args[0]));
					player.setAppearanceUpdateRequired(true);
				}

				if (keyword.equals("curepoison")) {
					player.curePoison(0);
				}

				if (keyword.equals("giverights")) {
					String name = args[0].replaceAll("_", " ");

					Player p = World.getPlayerByName(name);
					if (p == null)
						player.getClient().queueOutgoingPacket(new SendMessage("Player not found."));
					else {
						p.setRights(Integer.parseInt(args[1]));
					}
				}

				if (keyword.equals("giverights")) {
					String name = args[0].replaceAll("_", " ");

					Player p = World.getPlayerByName(name);
					if (p == null) {
						player.getClient().queueOutgoingPacket(new SendMessage("Player not found."));
					} else {
						p.setRights(Integer.parseInt(args[1]));
						player.getClient().queueOutgoingPacket(new SendMessage("Success."));
					}
				}
 
	
				if (keyword.equals("update")) {
					World.initUpdate();
				}

				if (keyword.equals("pnpc")) {
					player.setNpcAppearanceId((short) Integer.parseInt(args[0]));
					player.setAppearanceUpdateRequired(true);
				}

				if (keyword.equals("ci")) {
					JewelryCreationTask.sendInterface(player);
				}

				if (keyword.equals("reloadobjectspawns")) {
					ObjectManager.declare();
				}

				if (keyword.equals("reloadsounds")) {
					PlayerSounds.declare();
				}

				if (keyword.equals("cannon")) {
					DwarfMultiCannon.setCannonBase(player, 6);
				}

				if (keyword.equals("smith")) {
					SmithingConstants.sendSmithingInterface(player, 0);
				}

				if (keyword.equals("canim")) {
					player.getClient().queueOutgoingPacket(
							new SendAnimateObject(DwarfMultiCannon.getCannon(player), Integer.parseInt(args[0])));
				}
				
				if(keyword.equals("oanim")) {
					player.getClient().queueOutgoingPacket(
							new SendAnimateObject(new RSObject(player.getX(), player.getY(), player.getZ(), Integer.parseInt(args[0]), 10, 0), Integer.parseInt(args[1])));
				}

				if (keyword.equals("stillgfx")) {
					player.getClient().queueOutgoingPacket(
							new SendStillGraphic(Integer.parseInt(args[0]), player.getLocation(), 0));
				}

				if (keyword.equals("pkpshop")) {
					player.getShopping().open(99);
				}

				if (keyword.equals("equipcon")) {
					EquipmentConstants.declare();
					player.setAppearanceUpdateRequired(true);
				}

				if (keyword.equals("pkp")) {
					player.getEarningPotential().setPkp(100);
				}

				if (keyword.equals("mobcontrol")) {
					new MobControl(player, Integer.parseInt(args[0]));
				}

				if ((keyword.equals("mobanim")) && (player.getAttributes().get("npccontrol") != null)) {
					((MobControl) player.getAttributes().get("npccontrol")).anim(player, Integer.parseInt(args[0]));
				}

				if (keyword.equals("resetupdate")) {
					World.resetUpdate();
				}
				
				if (keyword.equals("object")) {
					ObjectManager.register(new GameObject(Integer.parseInt(args[0]), player.getLocation().getX(),
							player.getLocation().getY(), player.getLocation().getZ(), 10, 0));
				}

				if (keyword.equals("object2")) {
					ObjectManager.register(new GameObject(Integer.parseInt(args[0]), player.getLocation().getX(),
							player.getLocation().getY(), player.getLocation().getZ(), Integer.parseInt(args[1]), 0));
				}

				if (keyword.equals("stack")) {
					player.getInventory().add(new Item(995, 2147483646));
				}
				if (keyword.startsWith("meleemax")) {
					player.getClient().queueOutgoingPacket(
							new SendMessage("melee max hit: " + MaxHitFormulas.getMeleeMaxHit(player)));
				}
				if (keyword.startsWith("rangedmax")) {
					player.getClient().queueOutgoingPacket(
							new SendMessage("ranged max hit: " + MaxHitFormulas.getRangedMaxHit(player)));
				}
				if (keyword.startsWith("magicmax")) {
					player.getClient().queueOutgoingPacket(
							new SendMessage("magic max hit: " + MaxHitFormulas.getMagicMaxHit(player)));
				}

				if (keyword.equals("master")) {
					for (int i = 0; i < 25; i++) {
						player.getLevels()[i] = 99;
						player.getMaxLevels()[i] = 99;
						player.getSkill().getExperience()[i] = Skill.EXP_FOR_LEVEL[98];
						player.getSkill().update(i);
					}

					player.getSkill().updateCombatLevel();

					player.setAppearanceUpdateRequired(true);
				}

				if (keyword.equals("setlevel")) {
					int id = Integer.parseInt(args[0]);
					int lvl = Integer.parseInt(args[1]);

					player.getLevels()[id] = ((byte) lvl);
					player.getMaxLevels()[id] = ((byte) lvl);
					player.getSkill().getExperience()[id] = player.getSkill().getXPForLevel(id, lvl);
					player.getSkill().update(id);

					player.getSkill().updateCombatLevel();
				}

				if (keyword.equals("exp")) {
					int id = Integer.parseInt(args[0]);
					player.getSkill().addExperience(id, 900000);
				}

				if (keyword.equals("setlevelbyname")) {
					int id = -1;
					int lvl = Integer.parseInt(args[1]);

					for (int i = 0; i < SkillConstants.SKILL_NAMES.length; i++) {
						if (args[0].equalsIgnoreCase(SkillConstants.SKILL_NAMES[i])) {
							id = i;
							break;
						}
					}

					if (id == -1) {
						return;
					}

					player.getLevels()[id] = ((byte) lvl);
					player.getMaxLevels()[id] = ((byte) lvl);
					player.getSkill().getExperience()[id] = Skill.EXP_FOR_LEVEL[lvl];
					player.getSkill().update(id);
				}

				if (keyword.equals("zerker")) {
					if (ItemCheck.hasEquipmentOn(player)) {
						player.getClient().queueOutgoingPacket(
								new SendMessage("You must remove your equipment before using this command."));
						return;
					}

					if (!player.getController().equals(ControllerManager.DEFAULT_CONTROLLER)) {
						player.getClient().queueOutgoingPacket(
								new SendMessage("You must be in another setting to use this command, teleport home."));
						return;
					}

					player.getSkill().getExperience()[1] = Skill.EXP_FOR_LEVEL[45];
					player.getLevels()[1] = 45;
					player.getMaxLevels()[1] = 45;
					player.getSkill().update(1);

					player.getSkill().updateCombatLevel();
				}

				if (keyword.equals("random")) {
					org.endeavor.game.content.randoms.RandomExecutor.RANDOM_EVENTS[org.endeavor.engine.utility.Misc
							.randomNumber(org.endeavor.game.content.randoms.RandomExecutor.RANDOM_EVENTS.length)]
							.init(player);
				}

				if (keyword.equals("reloadshops")) {
					GameDefinitionLoader.loadShopDefinitions();
				}

				if (keyword.equals("donate")) {
					MySQL.checkForDonation(player);
					return;
				}

				if (keyword.equals("button")) {
					System.out.println("verified: "
							+ player.getInterfaceManager().verifyButton(Integer.parseInt(args[0])));
				}

				if (keyword.equals("rights") && PlayerConstants.isOwner(player)) {
					int id = Integer.parseInt(args[0]);
					player.setRights(id);
					return;
				}

				if (keyword.equals("levelto")) {
					int id = Integer.parseInt(args[0]);
					int lvl = Integer.parseInt(args[0]);
					player.getLevels()[id] = ((byte) lvl);
					player.getSkill().update(id);
				}

				if (keyword.equals("vote")) {
					//MySQL.checkForVote(player);
					//return;
				}

				if (keyword.equals("virtualtest")) {
					VirtualMobRegion r = new VirtualMobRegion(3200, 3200, 100);
					new Mob(r, 1, true, false, new Location(3200, 3200, 0));
					new Mob(r, 1, true, false, new Location(3201, 3200, 0));
					new Mob(r, 1, true, false, new Location(3202, 3200, 0));
					new Mob(r, 1, true, false, new Location(3203, 3200, 0));
					new Mob(r, 1, true, false, new Location(3200, 3201, 0));
					new Mob(r, 1, true, false, new Location(3200, 3202, 0));
					new Mob(r, 1, true, false, new Location(3200, 3203, 0));
				}

				if (keyword.equals("reloadmag")) {
					GameDefinitionLoader.loadCombatSpellDefinitions();
				}

				if (keyword.equals("hit")) {
					player.hit(new Hit(Integer.parseInt(args[0])));
				}

				if (keyword.startsWith("gfx")) {
					player.getUpdateFlags().sendGraphic(new Graphic(Integer.parseInt(args[0]), 0, true));
				}

				if (keyword.startsWith("summonspec")) {
					player.getSummoning().setSpecial(60);
				}

				if (keyword.startsWith("ipsearch")) {
					try {
						File[] files = new File("./data/characters/details/").listFiles();
						String host = args[0];

						System.out.println("Now searching accounts for host: " + host);

						for (offsetY = 0; offsetY < files.length; offsetY++) {
							File i = files[offsetY];
							String name = i.getName().replace(".json", "");

							Player p = new Player();

							p.setUsername(name);
							try {
								PlayerSave.load(p);

								if (p.getClient().getHost().equals(host))
									System.out.println(name);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					return;
				}

				if (keyword.equals("bob")) {
					player.getClient().queueOutgoingPacket(
							new SendUpdateItems(2702, new Item[] { new Item(4151, 5), new Item(4151, 10),
									new Item(4151), new Item(4151), new Item(4151), new Item(4151), new Item(4151),
									new Item(4151), new Item(4151), new Item(4151), new Item(4151), new Item(4151),
									new Item(4151), new Item(4151), new Item(4151), new Item(4151) }));
				}

				if (keyword.startsWith("bobint")) {
					player.getClient().queueOutgoingPacket(new SendUpdateItems(5064, player.getInventory().getItems()));
					player.getClient().queueOutgoingPacket(
							new SendUpdateItems(2702, new Item[] { new Item(4151, 5), new Item(4151, 10),
									new Item(4151), new Item(4151), new Item(4151), new Item(4151), new Item(4151),
									new Item(4151), new Item(4151), new Item(4151), new Item(4151), new Item(4151),
									new Item(4151), new Item(4151), new Item(4151), new Item(4151) }));

					player.getClient().queueOutgoingPacket(new SendInventoryInterface(2700, 5063));
				}

				if(keyword.startsWith("alert")) {
					try {
						World.sendGlobalMessage("[Server] " + keyword.substring(6), true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				if (keyword.startsWith("item")) {
					try {
						if (args.length == 1) {
							int item = Integer.parseInt(args[0]);
							player.getInventory().add(item, 1);
						} else if (args.length == 2) {
							int item = Integer.parseInt(args[0]);
							int amount = Integer.parseInt(args[1]);
							player.getInventory().add(item, amount);
						}
					} catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
					}
				}

				if (keyword.startsWith("npccombat")) {
					World.getNpcs()[Integer.parseInt(args[0])].getCombat().setAttacking(
							World.getNpcs()[Integer.parseInt(args[1])]);
					World.getNpcs()[Integer.parseInt(args[0])].getFollowing().setFollow(
							World.getNpcs()[Integer.parseInt(args[1])], Following.FollowType.COMBAT);

					World.getNpcs()[Integer.parseInt(args[1])].getCombat().setAttacking(
							World.getNpcs()[Integer.parseInt(args[0])]);
					World.getNpcs()[Integer.parseInt(args[1])].getFollowing().setFollow(
							World.getNpcs()[Integer.parseInt(args[0])], Following.FollowType.COMBAT);
				}

				if (keyword.equals("anim")) {
					player.getUpdateFlags().sendAnimation(Integer.parseInt(args[0]), 0);
				}

				if (keyword.equals("skull")) {
					player.getSkulling().skull(player, player);
				}

				if (keyword.startsWith("loopanim")) {
					final int anim = Integer.parseInt(args[0]);
					TaskQueue.queue(new Task(1) {
						int count = 0;

						@Override
						public void execute() {
							player.getUpdateFlags().sendAnimation(anim + count, 0);
							player.getClient().queueOutgoingPacket(new SendMessage("id: " + (anim + count)));
							count += 1;
						}

						@Override
						public void onStop() {
						}
					});
				}
				if (keyword.startsWith("loopintanim")) {
					final int inter = Integer.parseInt(args[0]);
					final int start = Integer.parseInt(args[1]);
					TaskQueue.queue(new Task(Integer.parseInt(args[2])) {
						int count = 0;

						@Override
						public void execute() {
							int id = start + count;
							player.getClient().queueOutgoingPacket(new SendModelAnimation(inter, id));
							player.getClient().queueOutgoingPacket(new SendMessage("id: " + id));
							count += 1;
						}

						@Override
						public void onStop() {
						}
					});
				}
				if (keyword.startsWith("loopsound")) {
					final int start = Integer.parseInt(args[0]);
					TaskQueue.queue(new Task(4) {
						int count = 0;

						@Override
						public void execute() {
							int id = start + count;
							player.getClient().queueOutgoingPacket(new SendSound(id, 1, 0));
							player.getClient().queueOutgoingPacket(new SendMessage("id: " + id));
							count += 1;
						}

						@Override
						public void onStop() {
						}
					});
				}
				if (keyword.startsWith("intanim")) {
					player.getClient().queueOutgoingPacket(
							new SendModelAnimation(Integer.parseInt(args[0]), Integer.parseInt(args[1])));
				}

				if (keyword.startsWith("findintmodel")) {
					for (int i = 500; i < 150000; i++) {
						player.getClient().queueOutgoingPacket(
								new SendModelAnimation(Integer.parseInt(args[0]), Integer.parseInt(args[1]) + i));
						System.out.println("id " + i);
						try {
							Thread.sleep(100L);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}

				if (keyword.equals("spawn")) {
					try {
						int npcId = Integer.parseInt(args[0]);
						World.register(new Mob(npcId, false, new Location(player.getLocation())));
						BufferedWriter bw = new BufferedWriter(new FileWriter(
								new File("./data/spawn command dump.txt"), true));
						bw.newLine();
						bw.write("\t<NpcSpawnDefinition>", 0, "\t<NpcSpawnDefinition>".length());
						bw.newLine();
						bw.write("\t<!-->" + Mob.getDefinition(npcId).getName() + "<-->", 0, ("\t<!-->" + Mob.getDefinition(npcId).getName() + "<-->").length());
						bw.newLine();
						bw.write("\t\t<id>" + npcId + "</id>", 0, ("\t\t<id>" + npcId + "</id>").length());
						bw.newLine();
						bw.write("\t\t<location>", 0, "\t\t<location>".length());
						bw.newLine();
						bw.write("\t\t\t<x>" + player.getLocation().getX() + "</x>", 0, ("\t\t\t<x>"
								+ player.getLocation().getX() + "</x>").length());
						bw.newLine();
						bw.write("\t\t\t<y>" + player.getLocation().getY() + "</y>", 0, ("\t\t\t<y>"
								+ player.getLocation().getY() + "</y>").length());
						bw.newLine();
						bw.write("\t\t\t<z>" + player.getLocation().getZ() + "</z>", 0, ("\t\t\t<z>"
								+ player.getLocation().getZ() + "</z>").length());
						bw.newLine();
						bw.write("\t\t</location>", 0, "\t\t</location>".length());
						bw.newLine();
						bw.write("\t\t<walk>true</walk>", 0, "\t\t<walk>true</walk>".length());
						bw.newLine();
						bw.write("\t</NpcSpawnDefinition>", 0, "\t</NpcSpawnDefinition>".length());
						bw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				if (keyword.startsWith("shop")) {
					player.getShopping().open(Integer.parseInt(args[0]));
				}
				if (keyword.startsWith("proj")) {
					System.out.println(StraightPathFinder.isProjectilePathClear(player.getLocation().getX(), player
							.getLocation().getY(), player.getLocation().getZ(), Integer.parseInt(args[0]), Integer
							.parseInt(args[1])));
				}

				if (keyword.startsWith("droptest")) {
					for (int x = player.getLocation().getX(); x < player.getLocation().getX() + 10; x++) {
						for (int y = player.getLocation().getY(); y < player.getLocation().getY() + 10; y++) {
							GroundItemHandler.add(new Item(4151, 1), new Location(x, y), player);
						}
					}
				}

				if (keyword.startsWith("2droptest")) {
					for (int i = 0; i < 28; i++) {
						GroundItemHandler.add(new Item(4151, 1), player.getLocation(), player);
					}
				}

				if (keyword.startsWith("config")) {
					player.getClient().queueOutgoingPacket(
							new SendConfig(Integer.parseInt(args[0]), Integer.parseInt(args[1])));
				}

				if (keyword.equals("fc")) {
					player.getClient().queueOutgoingPacket(new SendConfig(504, 1792));
					return;
				}

				if (keyword.startsWith("fc2")) {
					player.getClient().queueOutgoingPacket(new SendConfig(504, 2313));
				}

				if (keyword.startsWith("dr")) {
					player.getClient()
							.queueOutgoingPacket(
									new SendAltConfig(
											286,
											org.endeavor.game.content.minigames.duelarena.DuelingConstants.DUEL_RULE_IDS[Integer
													.parseInt(args[0])]));
				}

				if (keyword.startsWith("clearplots")) {
					player.getFarming().clear();
				}

				if (keyword.startsWith("testnpc")) {
					for (int i = 0; i < 400; i++) {
						World.register(new Mob(1, true, player.getLocation()));
					}
				}

				if (keyword.equals("mypos")) {
					player.getClient().queueOutgoingPacket(new SendMessage("You are at: " + player.getLocation()));
				}

				if (keyword.equals("addexp")) {
					player.getSkill().addExperience(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
				}

				if (keyword.equals("gp")) {
					player.getInventory().add(new Item(995, Integer.parseInt(args[0])));
				}

				if (keyword.equals("stacks")) {
					for (int i = 0; i < 6; i++) {
						player.getInventory().add(new Item(995, 2147483647));
					}
				}

				if (keyword.equals("removeitem")) {
					player.getInventory().remove(new Item(Integer.parseInt(args[0]), Integer.parseInt(args[1])));
				}

				if (keyword.equals("bank")) {
					player.getBank().openBank();
				}

				if (keyword.equals("bankitems")) {
					player.getBank().openBank();

					for (int i = 0; i <= 1080; i++) {
						player.getBank().add(new Item(i));
					}
				}

				if (keyword.equals("path")) {
					RS317PathFinder.findRoute(player, Integer.parseInt(args[0]), Integer.parseInt(args[1]), true, 0, 0);
				}

				if (keyword.equals("npc2")) {
					new Mob(Integer.parseInt(args[0]), false, new Location(player.getLocation()));
				}
				if (keyword.equals("gi")) {
					GroundItemHandler.add(new Item(Integer.parseInt(args[0]), Integer.parseInt(args[1])), new Location(
							player.getLocation()), player);
				}

				if (keyword.equals("run")) {
					player.getRunEnergy().setRunning(!player.getRunEnergy().isRunning());
				}

				keyword.equals("sound");

				if (keyword.equals("qs")) {
					player.getClient().queueOutgoingPacket(new SendQuickSong(Integer.parseInt(args[0]), 9000));
				}

				if (keyword.equals("movecam")) {
					player.getClient().queueOutgoingPacket(
							new SendMoveCamera(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer
									.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4])));
				}

				if (keyword.equals("turncam")) {
					player.getClient().queueOutgoingPacket(
							new SendTurnCamera(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer
									.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4])));
				}

				if (keyword.equals("resetcam")) {
					player.getClient().queueOutgoingPacket(new SendResetCamera());
				}

				if (keyword.startsWith("loopmovecam")) {
					final int x = Integer.parseInt(args[0]);
					final int y = Integer.parseInt(args[1]);

					TaskQueue.queue(new Task(1) {
						int count = 128;

						@Override
						public void execute() {
							player.getClient().queueOutgoingPacket(new SendMoveCamera(x, y, count, 0, 100));
							player.getClient().queueOutgoingPacket(new SendMessage("Pos: " + count));
							count += 1;
						}

						@Override
						public void onStop() {
						}
					});
				}
				if (keyword.equals("die")) {
					player.getLevels()[3] = 1;
					player.hit(new Hit(1));
				}

				if (keyword.equals("damtest")) {
					player.getCombat().getDamageTracker().addDamage(player, 10);
					System.out.println(player.getCombat().getDamageTracker().getKiller());
				}
			}
		} catch (Exception e) {
			if (GameSettings.DEV_MODE) {
				e.printStackTrace();
			}
			player.getClient().queueOutgoingPacket(new SendMessage("Invalid syntax."));
		}
	}
}
