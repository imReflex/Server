package org.endeavor.game.entity.player.net.in.command;

import java.util.HashMap;
import java.util.Map;

import org.endeavor.GameSettings;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerConstants;
import org.endeavor.game.entity.player.net.in.command.impl.EmptyInventoryCommand;
import org.endeavor.game.entity.player.net.in.command.impl.OwnerCommands;
import org.endeavor.game.entity.player.net.in.command.impl.PlayersOnlineCommand;
import org.endeavor.game.entity.player.net.in.command.impl.PunishmentCommand;
import org.endeavor.game.entity.player.net.in.command.impl.ReloadCommand;
import org.endeavor.game.entity.player.net.in.command.impl.SendAlertCommand;
import org.endeavor.game.entity.player.net.in.command.impl.SendAnimationCommand;
import org.endeavor.game.entity.player.net.in.command.impl.SendGraphicCommand;
import org.endeavor.game.entity.player.net.in.command.impl.SendPacketCommand;
import org.endeavor.game.entity.player.net.in.command.impl.SetPnpcCommand;
import org.endeavor.game.entity.player.net.in.command.impl.SetRightsCommand;
import org.endeavor.game.entity.player.net.in.command.impl.SetSkillCommand;
import org.endeavor.game.entity.player.net.in.command.impl.SetSpecialCommand;
import org.endeavor.game.entity.player.net.in.command.impl.SpawnObjectCommand;
import org.endeavor.game.entity.player.net.in.command.impl.SwitchMagicInterfaceCommand;
import org.endeavor.game.entity.player.net.in.command.impl.TeleportCommand;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class CommandManager {
	private static Map<String, Command> commands = new HashMap<String, Command>();

	public static void reloadCommands() {
		new CommandManager();
	}

	public static void declare() {
		put("switch", new SwitchMagicInterfaceCommand());
		put("players", new PlayersOnlineCommand());
		put("reload", new ReloadCommand());
		put("alert", new SendAlertCommand());
		put("object", new SpawnObjectCommand());
		put("pnpc", new SetPnpcCommand());
		put("setrights", new SetRightsCommand());
		put("spec", new SetSpecialCommand());
		put("empty", new EmptyInventoryCommand());
		put("anim", new SendAnimationCommand());
		put("gfx", new SendGraphicCommand());

		TeleportCommand teleport = new TeleportCommand();
		put("xteleto", teleport);
		put("xteletome", teleport);
		put("tele", teleport);

		SendPacketCommand packet = new SendPacketCommand();
		put("interface", packet);
		put("string", packet);
		put("mapstate", packet);
		put("resetcam", packet);
		put("config", packet);
		put("altconfig", packet);
		put("sound", packet);
		put("location", packet);

		SetSkillCommand skill = new SetSkillCommand();
		put("master", skill);
		put("setlevel", skill);

		PunishmentCommand punish = new PunishmentCommand();
		put("mute", punish);
		put("unmute", punish);
		put("ban", punish);
		put("unban", punish);
		put("yellmute", punish);
		put("unyellmute", punish);
		put("blackmark", punish);
		put("bm,", punish);
		
		Command command = new OwnerCommands();
		put("maxhit", command);
		put("hit", command);
	}

	private static void put(String name, Command command) {
		commands.put(name, command);
	}

	public static boolean handleCommand(Player player, String command) {
		String name = "";
		if (command.indexOf(' ') > -1)
			name = command.substring(0, command.indexOf(' '));
		else {
			name = command;
		}
		name = name.toLowerCase();
		Command comm = commands.get(name);
		if (comm != null) {
			if (GameSettings.DEV_MODE) {
				try {
					comm.handleCommand(player, command);
				} catch (Exception e) {
					player.getClient().queueOutgoingPacket(new SendMessage("Invalid command format."));
				}
				
				return true;
			}
			
			if (!comm.meetsRequirements(player) && !PlayerConstants.isOwner(player))
				return false;
			
			if (player.getRights() >= comm.rightsRequired() || !PlayerConstants.isOwner(player)) {
				try {
					comm.handleCommand(player, command);
				} catch (Exception e) {
					player.getClient().queueOutgoingPacket(new SendMessage("Invalid command format."));
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}
}
