package org.endeavor.game.entity.player.net.in.command.impl;

import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.io.PlayerSaveUtil;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.in.command.Command;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class PunishmentCommand implements Command {
	@Override
	public void handleCommand(Player player, String command) {
		String[] args = command.split(" ");
		switch (args[0]) {
		case "kick":
			final String n = args[1].replaceAll("_", " ");
			
			final Player p = World.getPlayerByName(n);
			if (p != null) {
				p.logout(true);
				player.getClient().queueOutgoingPacket(new SendMessage("You have kicked: " + p.getUsername()));
			} else {
				player.getClient().queueOutgoingPacket(new SendMessage("Player not found, syntax: ::kick username_example"));
			}
			break;
		case "blackmark":
		case "bm":
			try {
				if (args.length == 2 || args.length == 3) {
					final String n1 = args[1].replaceAll("_", " ");
					
					final Player p1 = World.getPlayerByName(n1);
					
					int amount = 1;
					
					if (args.length == 3) {
						amount = Integer.parseInt(args[2]);
					}
					
					if (p1 != null) {
						p1.giveBlackmark(amount);
						player.getClient().queueOutgoingPacket(new SendMessage("Player " + n1 + " was blackmarked with " + amount + " marks."));
					} else {
						player.getClient().queueOutgoingPacket(new SendMessage("Player " + n1 + " could not be located."));
					}
				} else {
					player.getClient().queueOutgoingPacket(new SendMessage("Incorrect syntax, syntax: ::blackmark username_example 1 (amount is optional, default is 1)."));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case "mute":
			if (args.length == 3) {
				final int length = Integer.parseInt(args[2]);
				if (length > 0) {
					final String name = args[1].replaceAll("_", " ");
					final Player punish = World.getPlayerByName(name);
					if (punish != null) {
						punish.setMuted(true);
						punish.setRemainingMute(length);
						punish.setMuteDay(Misc.getDayOfYear());
						punish.setMuteYear(Misc.getYear());
						punish.setMuteLength(length);
					} else {
						if (!PlayerSaveUtil.muteOfflinePlayer(name, length)) {
							player.getClient().queueOutgoingPacket(
									new SendMessage("Player save for " + name
											+ " does not exist or could not be saved."));
							return;
						}
					}
					player.getClient()
							.queueOutgoingPacket(new SendMessage("You have successfully muted " + name + "."));
				} else {
					player.getClient().queueOutgoingPacket(new SendMessage("You must enter a positive mute length."));
				}
			} else {
				player.getClient().queueOutgoingPacket(new SendMessage("Syntax: mute player_name_example days"));
			}
			break;
		case "yellmute":
			if (args.length == 2) {
				final String name = args[1].replaceAll("_", " ");
				final Player punish = World.getPlayerByName(name);
				if (punish != null) {
					punish.setYellMuted(true);
				} else {
					if (!PlayerSaveUtil.yellMuteOfflinePlayer(name)) {
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
			break;
		case "unyellmute":
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
			break;
		case "unmute":
			if (args.length == 2) {
				final String name = args[1].replaceAll("_", " ");
				final Player p2 = World.getPlayerByName(name);
				if (p2 != null) {
					p2.setMuted(false);
				} else {
					if (!PlayerSaveUtil.unmuteOfflinePlayer(name)) {
						player.getClient().queueOutgoingPacket(
								new SendMessage("Player save for " + name + " does not exist or could not be saved."));
						return;
					}
				}
				player.getClient().queueOutgoingPacket(new SendMessage("You have successfully unmuted " + name + "."));
			} else {
				player.getClient().queueOutgoingPacket(new SendMessage("Syntax: unmute player_name_example"));
			}
			break;

		case "ban":
			if (args.length == 3) {
				final int length = Integer.parseInt(args[2]);
				if (length > 0) {
					final String name = args[1].replaceAll("_", " ");
					final Player punish = World.getPlayerByName(name);
					if (punish != null) {
						punish.setBanned(true);
						punish.setBanDay(Misc.getDayOfYear());
						punish.setBanYear(Misc.getYear());
						punish.setBanLength(length);
						punish.logout(true);
					} else {
						if (!PlayerSaveUtil.banOfflinePlayer(name, length)) {
							player.getClient().queueOutgoingPacket(
									new SendMessage("Player save for " + name
											+ " does not exist or could not be saved."));
							return;
						}
					}
					player.getClient().queueOutgoingPacket(
							new SendMessage("You have successfully banned " + name + "."));
				} else {
					player.getClient().queueOutgoingPacket(new SendMessage("You must enter a positive ban length."));
				}
			} else {
				player.getClient().queueOutgoingPacket(new SendMessage("Syntax: ban player_name_example days"));
			}
			break;

		case "unban":
			if (args.length == 2) {
				final String name = args[1].replaceAll("_", " ");
				if (!PlayerSaveUtil.unbanOfflinePlayer(name)) {
					player.getClient().queueOutgoingPacket(
							new SendMessage("Player save for " + name + " does not exist or could not be saved."));
					return;
				}
				player.getClient().queueOutgoingPacket(new SendMessage("You have successfully unbanned " + name + "."));
			} else {
				player.getClient().queueOutgoingPacket(new SendMessage("Syntax: unban player_name_example"));
			}
			break;
		}
	}
	
	public static void ban(Player p, int length) {
		p.setBanned(true);
		p.setBanDay(Misc.getDayOfYear());
		p.setBanYear(Misc.getYear());
		p.setBanLength(length);
		p.logout(true);
	}
	
	public static void mute(Player p, int length) {
		p.setMuted(true);
		p.setRemainingMute(length);
		p.setMuteDay(Misc.getDayOfYear());
		p.setMuteYear(Misc.getYear());
		p.setMuteLength(length);
	}

	@Override
	public int rightsRequired() {
		return 3;
	}

	@Override
	public boolean meetsRequirements(Player player) {
		return true;
	}
}
