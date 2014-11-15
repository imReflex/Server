package org.endeavor.game.entity.player.net.in.impl;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.endeavor.engine.cache.map.RSObject;
import org.endeavor.engine.cache.map.Region;
import org.endeavor.engine.network.StreamBuffer.InBuffer;
import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.Yelling;
import org.endeavor.game.content.clans.Clan;
import org.endeavor.game.content.clans.Clans;
import org.endeavor.game.content.combat.Hit;
import org.endeavor.game.content.combat.impl.MaxHitFormulas;
import org.endeavor.game.content.dialogue.impl.ConfirmDialogue;
import org.endeavor.game.content.dwarfcannon.DwarfMultiCannon;
import org.endeavor.game.content.io.PlayerSaveUtil;
import org.endeavor.game.content.minigames.dungeoneering.DungConstants;
import org.endeavor.game.content.skill.Skill;
import org.endeavor.game.content.skill.crafting.JewelryCreationTask;
import org.endeavor.game.content.skill.magic.MagicSkill;
import org.endeavor.game.content.skill.smithing.SmithingConstants;
import org.endeavor.game.content.sounds.PlayerSounds;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.item.impl.GroundItemHandler;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.object.GameObject;
import org.endeavor.game.entity.object.ObjectManager;
import org.endeavor.game.entity.player.ItemCheck;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerConstants;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.in.IncomingPacket;
import org.endeavor.game.entity.player.net.out.impl.SendAnimateObject;
import org.endeavor.game.entity.player.net.out.impl.SendConfig;
import org.endeavor.game.entity.player.net.out.impl.SendFlashSidebarIcon;
import org.endeavor.game.entity.player.net.out.impl.SendInterface;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendModelAnimation;
import org.endeavor.game.entity.player.net.out.impl.SendMoveCamera;
import org.endeavor.game.entity.player.net.out.impl.SendResetCamera;
import org.endeavor.game.entity.player.net.out.impl.SendShakeScreen;
import org.endeavor.game.entity.player.net.out.impl.SendStillGraphic;
import org.endeavor.game.entity.player.net.out.impl.SendString;
import org.endeavor.game.entity.player.net.out.impl.SendSystemBan;
import org.endeavor.game.entity.player.net.out.impl.SendTurnCamera;

public class CommandsPacket extends IncomingPacket {

	@Override
	public void handle(Player player, InBuffer in, int opcode, int length) {
		String plainText = in.readString();
		String command = plainText.toLowerCase();
		String[] args = command.split(" ");
		
		if (command.startsWith("/")) {
			if(player.getClanChannel() != null) {
				player.getClanChannel().interpret(player, plainText);
			} else {
				player.send(new SendMessage("You are not in a clan."));
			}
			return;
		}
		
		/**
		 * Member commands
		 */
		if(player.getMemberRank() >= 1) {
			
		}
		
		/**
		 * Super Member commands
		 */
		if(player.getMemberRank() >= 2) {
			
		}
		
		/**
		 * Respected Member commands
		 */
		if(player.getMemberRank() == 3) {
			if(command.startsWith("title")) {
				try {
					String title = command.substring(5);
					if(title.length() > 12) {
						player.send(new SendMessage("Please use a title that is less than 13 characters."));
						return;
					}
					if(title.contains("<") || title.contains(">") || title.contains("@") || title.contains("mod") || title.contains("owner") || title.contains("admin")) {
						player.send(new SendMessage("The title you entered is invalid, please try again."));
						return;
					}
				player.setTitle(title);
				} catch (Exception e) {
					player.send(new SendMessage("Please use '::title TITLE'"));
				}
				return;
			}
			
			if(command.startsWith("tcolor")) {
				try {
					String color = command.substring(6);
					if(color.length() != 6 && color.length() != 8) {
						player.send(new SendMessage("Please use the format ::color 000000 or ::color 0xfffff"));
						return;
					}
					player.setTitleColor(Integer.parseInt(color));
				} catch (Exception e) {
					player.send(new SendMessage("Please use the format ::color 000000 or ::color 0xfffff"));
				}
				return;
			}
		}
		
		/**
		 * Player commands
		 */
		if(player.getRights() >= 0) {
			
			if(command.equals("testcommand")) {
				player.send(new SendMessage("Test command!"));
				return;
			}
			
			if(command.equals("empty")) {
				player.getInventory().clear();
			}
			 
			if(command.startsWith("create")) {
				System.out.println("creating clan..");
				Clans.createClan(player, command.substring(7));
				return;
			}
			
			if(command.startsWith("accept")) {
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
				return;
			}
			
			if(command.startsWith("acceptwar")) {
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
			
			if(command.equals("leave")) {
				Clan clan = Clans.getClanForPlayer(player);
				if(clan != null) {
					clan.leaveClan(player);
				}
			}
			
			if (command.equals("master") && player.getRights() != 2) {
				for (int i = 0; i <= 6; i++) {
					player.getLevels()[i] = 99;
					player.getMaxLevels()[i] = 99;
					player.getSkill().getExperience()[i] = Skill.EXP_FOR_LEVEL[98];
					player.getSkill().update(i);
				}

				player.getSkill().updateCombatLevel();

				player.setAppearanceUpdateRequired(true);
				return;
			}
			
			if (command.equals("setlevel") && player.getRights() != 2) {
				int id = Integer.parseInt(args[1]);
				if(id > 6) {
					player.send(new SendMessage("You can not set this level."));
					return;
				}
				int lvl = Integer.parseInt(args[2]);

				player.getLevels()[id] = ((byte) lvl);
				player.getMaxLevels()[id] = ((byte) lvl);
				player.getSkill().getExperience()[id] = player.getSkill().getXPForLevel(id, lvl);
				player.getSkill().update(id);

				player.getSkill().updateCombatLevel();
				return;
			}
			
			if (command.startsWith("item") && player.getRights() != 2) {
				/** TODO: Add restrictions! **/
				try {
					if (args.length == 2) {
						int item = Integer.parseInt(args[1]);
						if(item >= 20100) {
							player.send(new SendMessage("You can not spawn this item."));
							return;
						}
						player.getInventory().add(item, 1);
					} else if (args.length == 3) {
						int item = Integer.parseInt(args[1]);
						if(item >= 20100) {
							player.send(new SendMessage("You can not spawn this item."));
							return;
						}
						int amount = Integer.parseInt(args[2]);
						player.getInventory().add(item, amount);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			}
			
			if (command.equals("skull")) {
				player.getSkulling().skull(player, null);
				return;
			}
			
			if(command.equalsIgnoreCase("players")) {
				player.getClient().queueOutgoingPacket(new SendMessage("There are currently " + World.getActivePlayers() +" players online."));
				return;
			}
			
			if (command.equals("zerker")) {
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
			
			if (command.equals("pure")) {
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
				return;
			}

			if (command.equals("rules")) {
				player.rules();
				return;
			}
			
			if ((command.startsWith("yell")) && (!command.contains("yellmute"))) {
				try {
					Yelling.yell(player, command.substring(5, command.length()));
				} catch (Exception e) {
					player.getClient().queueOutgoingPacket(new SendMessage("Invalid yell format, syntax: ::yell messsage"));
				}
				return;
			}

			if (command.startsWith("train")) {
				player.getMagic().teleport(3161, 4237, 0, MagicSkill.TeleportTypes.SPELL_BOOK);
				return;
			}
			
		}
		
		/**
		 * Moderator commands
		 */
		if(player.getRights() >= 1) {
			
			if (command.startsWith("xteleto") && !command.contains("xteletome")) {
				String username = command.substring(8);
				for (Player players : World.getPlayers()) {
					if (players != null) {
						if (players.getUsername().equalsIgnoreCase(username)) {
							if (!PlayerConstants.isOwner(player) && players.getController().equals(DungConstants.DUNG_CONTROLLER)) {
								player.send(new SendMessage("You cannot teleport to a player in Dungeoneering, contact an owner."));
								return;
							} else
							player.teleport(players.getLocation());
						}
					}
				}
			}
			
			if (command.startsWith("kick")) {
				String name = args[1].replaceAll("_", " ");

				Player p = World.getPlayerByName(name);
				if (p == null) {
					player.getClient().queueOutgoingPacket(new SendMessage("Player not found."));
				} else {
					p.logout(true);
					player.getClient().queueOutgoingPacket(new SendMessage("Kicked."));
				}

				return;
			}
			
			if(command.startsWith("mute")) {
				if (args.length == 3) {
					final int leng = Integer.parseInt(args[2]);
					if (leng > 0) {
						final String name = args[1].replaceAll("_", " ");
						final Player punish = World.getPlayerByName(name);
						if (punish != null) {
							punish.setMuted(true);
							punish.setRemainingMute(leng);
							punish.setMuteDay(Misc.getDayOfYear());
							punish.setMuteYear(Misc.getYear());
							punish.setMuteLength(leng);
						} else {
							/*if (!PlayerSaveUtil.muteOfflinePlayer(name, leng)) {
								player.getClient().queueOutgoingPacket(
										new SendMessage("Player save for " + name
												+ " does not exist or could not be saved."));
								return;
							}*/
						}
						player.getClient()
								.queueOutgoingPacket(new SendMessage("You have successfully muted " + name + "."));
					} else {
						player.getClient().queueOutgoingPacket(new SendMessage("You must enter a positive mute length."));
					}
				} else {
					player.getClient().queueOutgoingPacket(new SendMessage("Syntax: mute player_name_example days"));
				}
			}
			
			if(command.startsWith("yellmute")) {
				if (args.length == 2) {
					final String name = args[1].replaceAll("_", " ");
					final Player punish = World.getPlayerByName(name);
					if (punish != null) {
						punish.setYellMuted(true);
					} else {
						/*if (!PlayerSaveUtil.yellMuteOfflinePlayer(name)) {
							player.getClient().queueOutgoingPacket(
									new SendMessage("Player save for " + name + " does not exist or could not be saved."));
							return;
						}*/
					}
					player.getClient().queueOutgoingPacket(
							new SendMessage("You have successfully yell muted " + name + "."));
				} else {
					player.getClient().queueOutgoingPacket(new SendMessage("Syntax: yellmute player_name_example"));
				}
			}
			
		}
		
		/**
		 * Administrator rights
		 */
		if(player.getRights() >= 2) {
			
			if (args[0].equalsIgnoreCase("string")) {
				int frameId = Integer.parseInt(command.substring(7));
				player.getClient().queueOutgoingPacket(new SendString("test: " + frameId, frameId));
				player.getClient().queueOutgoingPacket(new SendMessage("Sending new string: " + frameId));
			}
			
			if (command.startsWith("xteletome")) {
				String user = command.substring(10);
				for (Player players : World.getPlayers()) {
					if (players != null) {
						if (players.getUsername().equalsIgnoreCase(user)) {
							if (!PlayerConstants.isOwner(player) && players.getController().equals(DungConstants.DUNG_CONTROLLER)) {
								player.send(new SendMessage("You cannot teleport a player out of Dungeoneering, contact an owner."));
								return;
							} else
							players.teleport(player.getLocation());
						}
					}
				}
				return;
			}
			
			if (command.startsWith("tele")) {
				int x = Integer.parseInt(args[1]);
				int y = Integer.parseInt(args[2]);
				int z = 0;
				
				if (args.length > 3) {
					z = Integer.parseInt(args[3]);
				}
				
				player.teleport(new Location(x, y, z));
				return;
			}
			
			if (command.startsWith("ipban")) {
				String name = args[1].replaceAll("_", " ");

				Player p = World.getPlayerByName(name);

				if (p == null) {
					player.getClient().queueOutgoingPacket(new SendMessage("Player not found."));
				} else {
					player.getClient().queueOutgoingPacket(new SendMessage("Success."));
					new SendSystemBan().execute(p.getClient());
					PlayerSaveUtil.setIPBanned(p);
					p.logout(true);
				}
				return;
			}
			
			if (command.startsWith("ipmute")) {
				String name = args[1].replaceAll("_", " ");

				Player p = World.getPlayerByName(name);

				if (p == null) {
					player.getClient().queueOutgoingPacket(new SendMessage("Player not found."));
				} else {
					PlayerSaveUtil.setIPMuted(p);
					player.getClient().queueOutgoingPacket(new SendMessage("Success."));
					p.setMuted(true);
				}
				return;
			}
			
			if(command.startsWith("unyellmute")) {
				if (args.length == 2) {
					final String name = args[1].replaceAll("_", " ");
					final Player punish = World.getPlayerByName(name);
					if (punish != null) {
						punish.setYellMuted(false);
					} else {
						if (!PlayerSaveUtil.unYellMuteOfflinePlayer(name)) {
							player.getClient().queueOutgoingPacket(
									new SendMessage("Player save for " + name + " does not exist or could not be saved."));
							return;
						}
					}
					player.getClient().queueOutgoingPacket(
							new SendMessage("You have successfully yell muted " + name + "."));
				} else {
					player.getClient().queueOutgoingPacket(new SendMessage("Syntax: yellmute player_name_example"));
				}
			}
		
			if(command.startsWith("unmute")) {
				if (args.length == 2) {
					final String name = args[1].replaceAll("_", " ");
					final Player p2 = World.getPlayerByName(name);
					if (p2 != null) {
						p2.setMuted(false);
					} else {
						/*if (!PlayerSaveUtil.unmuteOfflinePlayer(name)) {
							player.getClient().queueOutgoingPacket(
									new SendMessage("Player save for " + name + " does not exist or could not be saved."));
							return;
						}*/
					}
					player.getClient().queueOutgoingPacket(new SendMessage("You have successfully unmuted " + name + "."));
				} else {
					player.getClient().queueOutgoingPacket(new SendMessage("Syntax: unmute player_name_example"));
				}
			}

			if(command.startsWith("ban")) {
				if (args.length == 3) {
					final int leng = Integer.parseInt(args[2]);
					if (leng > 0) {
						final String name = args[1].replaceAll("_", " ");
						final Player punish = World.getPlayerByName(name);
						if (punish != null) {
							punish.setBanned(true);
							punish.setBanDay(Misc.getDayOfYear());
							punish.setBanYear(Misc.getYear());
							punish.setBanLength(leng);
							punish.logout(true);
						} else {
							/*if (!PlayerSaveUtil.banOfflinePlayer(name, leng)) {
								player.getClient().queueOutgoingPacket(
										new SendMessage("Player save for " + name
												+ " does not exist or could not be saved."));
								return;
							}*/
							player.send(new SendMessage("This player is offline."));
						}
						player.getClient().queueOutgoingPacket(
								new SendMessage("You have successfully banned " + name + "."));
					} else {
						player.getClient().queueOutgoingPacket(new SendMessage("You must enter a positive ban length."));
					}
				} else {
					player.getClient().queueOutgoingPacket(new SendMessage("Syntax: ban player_name_example days"));
				}
			}

			if(command.startsWith("unban")) {
				if (args.length == 2) {
					final String name = args[1].replaceAll("_", " ");
					/*if (!PlayerSaveUtil.unbanOfflinePlayer(name)) {
						player.getClient().queueOutgoingPacket(
								new SendMessage("Player save for " + name + " does not exist or could not be saved."));
						return;
					}*/
					player.getClient().queueOutgoingPacket(new SendMessage("You have successfully unbanned " + name + "."));
				} else {
					player.getClient().queueOutgoingPacket(new SendMessage("Syntax: unban player_name_example"));
				}
			}
			
			if (command.startsWith("npc")) {
				Mob mob = new Mob(Integer.parseInt(args[1]), true, new Location(player.getLocation()));
				player.getClient().queueOutgoingPacket(new SendMessage("Spawned NPC index: " + mob.getIndex()));
				return;
			}
			
			if (command.equals("flash")) {
				player.send(new SendFlashSidebarIcon(135));
			}
			
			if(command.equals("spec")) {
				player.getSpecialAttack().setSpecialAmount(100);
				player.getEquipment().updateSidebar();
			}
			
			if (command.equals("cleargi")) {
				synchronized (GroundItemHandler.getActive()) {
					GroundItemHandler.getActive().clear();
				}
			}
			
			if(command.startsWith("title")) {
				String title = args[1].replaceAll("_", " ");
				player.setTitle(title);
				player.send(new SendMessage("Your title is now: " + title));
				return;
			}
			
			if(command.startsWith("tcolor")) {
				int titleColor = Integer.parseInt(args[1]);
				player.setTitleColor(titleColor);
				player.send(new SendMessage("Your title color is now: " + titleColor));
				return;
			}
			
			if (command.equals("getobjectid")) {
				player.getClient().queueOutgoingPacket(new SendMessage("" + Region.getObject(player.getX(), player.getY(), player.getZ()).getId()));
			}

			if (command.startsWith("interface")) {
				player.getClient().queueOutgoingPacket(new SendInterface(Integer.parseInt(args[1])));
			}

			if (command.equals("shake")) {
				player.getClient().queueOutgoingPacket(new SendShakeScreen(true));
			}

			if (command.equals("stopshake")) {
				player.getClient().queueOutgoingPacket(new SendShakeScreen(false));
			}

			if (command.startsWith("headicon")) {
				player.getPrayer().setHeadIcon((byte) Integer.parseInt(args[1]));
				player.setAppearanceUpdateRequired(true);
			}
			
			if (command.equals("emptybank")) {
				player.getBank().clear();
			}

			if (command.startsWith("flashicon"))
				player.getClient().queueOutgoingPacket(new SendFlashSidebarIcon(Integer.parseInt(args[1])));
			
			if (command.equals("poison")) {
				player.poison(2);
			}
			
			if (command.equals("cure")) {
				player.curePoison(0);
			}

			if (command.equals("logout")) {
				player.logout(true);
				return;
			}
			
			if (command.equals("resetlevels")) {
				for (int i = 0; i < 25; i++) {
					player.getSkill().reset(i);
				}
			}
			
			if (command.equals("update")) {
				World.initUpdate();
				return;
			}

			if (command.startsWith("pnpc")) {
				player.setNpcAppearanceId((short) Integer.parseInt(args[1]));
				player.setAppearanceUpdateRequired(true);
			}

			if (command.equals("ci")) {
				JewelryCreationTask.sendInterface(player);
			}

			if (command.equals("reloados")) {
				ObjectManager.declare();
			}

			if (command.equals("reloadsounds")) {
				PlayerSounds.declare();
			}

			if (command.equals("cannon")) {
				DwarfMultiCannon.setCannonBase(player, 6);
			}

			if (command.equals("smith")) {
				SmithingConstants.sendSmithingInterface(player, 0);
			}
			
			if(command.startsWith("oanim")) {
				player.getClient().queueOutgoingPacket(
						new SendAnimateObject(new RSObject(player.getX(), player.getY(), player.getZ(), Integer.parseInt(args[1]), 10, 0), Integer.parseInt(args[2])));
			}
			
			if (command.startsWith("gfx")) {
				player.getClient().queueOutgoingPacket(
						new SendStillGraphic(Integer.parseInt(args[1]), player.getLocation(), 0));
			}
			
			if (command.equals("resetupdate")) {
				World.resetUpdate();
			}
			
			if (command.startsWith("object")) {
				ObjectManager.register(new GameObject(Integer.parseInt(args[1]), player.getLocation().getX(),
						player.getLocation().getY(), player.getLocation().getZ(), 10, 0));
			}
			
			if (command.startsWith("meleemax")) {
				player.getClient().queueOutgoingPacket(
						new SendMessage("melee max hit: " + MaxHitFormulas.getMeleeMaxHit(player)));
			}
			if (command.startsWith("rangedmax")) {
				player.getClient().queueOutgoingPacket(
						new SendMessage("ranged max hit: " + MaxHitFormulas.getRangedMaxHit(player)));
			}
			if (command.startsWith("magicmax")) {
				player.getClient().queueOutgoingPacket(
						new SendMessage("magic max hit: " + MaxHitFormulas.getMagicMaxHit(player)));
			}
			
			if (command.equals("master")) {
				for (int i = 0; i < 25; i++) {
					player.getLevels()[i] = 99;
					player.getMaxLevels()[i] = 99;
					player.getSkill().getExperience()[i] = Skill.EXP_FOR_LEVEL[98];
					player.getSkill().update(i);
				}

				player.getSkill().updateCombatLevel();

				player.setAppearanceUpdateRequired(true);
			}
			
			if (command.startsWith("setlevel")) {
				if(args.length < 3) {
					player.send(new SendMessage("Use the syntax ::setlevel skillid level"));
					return;
				}
				int id = Integer.parseInt(args[1]);
				int lvl = Integer.parseInt(args[2]);

				player.getLevels()[id] = ((byte) lvl);
				player.getMaxLevels()[id] = ((byte) lvl);
				player.getSkill().getExperience()[id] = player.getSkill().getXPForLevel(id, lvl);
				player.getSkill().update(id);

				player.getSkill().updateCombatLevel();
			}
			
			if (command.equals("reloadmag")) {
				try {
					GameDefinitionLoader.loadCombatSpellDefinitions();
				} catch (Exception e) {
					player.send(new SendMessage("Error reloading magic definitions."));
				}
			}
			
			if (command.startsWith("hit")) {
				player.hit(new Hit(Integer.parseInt(args[1])));
			}
			
			if (command.startsWith("summonspec")) {
				player.getSummoning().setSpecial(60);
			}
			
			if(command.startsWith("alert")) {
				try {
					World.sendGlobalMessage("[Server] " + command.substring(6), true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if (command.startsWith("item")) {
				try {
					if (args.length == 2) {
						int item = Integer.parseInt(args[1]);
						player.getInventory().add(item, 1);
					} else if (args.length == 3) {
						int item = Integer.parseInt(args[1]);
						int amount = Integer.parseInt(args[2]);
						player.getInventory().add(item, amount);
					}
				} catch (Exception e) {
					player.send(new SendMessage("Use ::item id amount"));
				}
			}
			
			if (command.startsWith("anim")) {
				player.getUpdateFlags().sendAnimation(Integer.parseInt(args[1]), 0);
			}
			
			if (command.startsWith("intanim")) {
				player.getClient().queueOutgoingPacket(
						new SendModelAnimation(Integer.parseInt(args[1]), Integer.parseInt(args[2])));
			}
			
			if (command.startsWith("config")) {
				player.getClient().queueOutgoingPacket(
						new SendConfig(Integer.parseInt(args[1]), Integer.parseInt(args[2])));
			}
			
			if (command.equals("mypos")) {
				player.getClient().queueOutgoingPacket(new SendMessage("You are at: " + player.getLocation()));
			}
			
			if (command.equals("bank")) {
				player.getBank().openBank();
			}
			
			if (command.startsWith("movecam")) {
				if(args.length != 6) {
					player.send(new SendMessage("Invalid syntax."));
					return;
				}
				player.getClient().queueOutgoingPacket(
						new SendMoveCamera(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer
								.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5])));
			}

			if (command.startsWith("turncam")) {
				if(args.length != 6) {
					player.send(new SendMessage("Invalid syntax."));
					return;
				}
				player.getClient().queueOutgoingPacket(
						new SendTurnCamera(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer
								.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5])));
			}

			if (command.equals("resetcam")) {
				player.getClient().queueOutgoingPacket(new SendResetCamera());
			}
			
			if (command.equals("die")) {
				player.getLevels()[3] = 1;
				player.hit(new Hit(1));
			}
			
		}
	}

	@Override
	public int getMaxDuplicates() {
		return 1;
	}

}
