package org.endeavor.game.entity.player.net.in.command.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.in.command.Command;

public class WriteMobSpawnCommand implements Command {
	@Override
	public void handleCommand(Player player, String command) {
		String[] args = command.split(" ");
		try {
			int npcId = Integer.parseInt(args[1]);
			World.register(new Mob(npcId, false, new Location(player.getLocation())));
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("./data/spawn command dump.txt"), true));
			bw.newLine();
			bw.write("\t<NpcSpawnDefinition>", 0, "\t<NpcSpawnDefinition>".length());
			bw.newLine();
			bw.write("\t\t<id>" + npcId + "</id>", 0, ("\t\t<id>" + npcId + "</id>").length());
			bw.newLine();
			bw.write("\t\t<location>", 0, "\t\t<location>".length());
			bw.newLine();
			bw.write("\t\t\t<x>" + player.getLocation().getX() + "</x>", 0,
					("\t\t\t<x>" + player.getLocation().getX() + "</x>").length());
			bw.newLine();
			bw.write("\t\t\t<y>" + player.getLocation().getY() + "</y>", 0,
					("\t\t\t<y>" + player.getLocation().getY() + "</y>").length());
			bw.newLine();
			bw.write("\t\t\t<z>" + player.getLocation().getZ() + "</z>", 0,
					("\t\t\t<z>" + player.getLocation().getZ() + "</z>").length());
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

	@Override
	public int rightsRequired() {
		return 0;
	}

	@Override
	public boolean meetsRequirements(Player player) {
		return (player.getUsername()
				.equalsIgnoreCase(org.endeavor.game.entity.player.PlayerConstants.OWNER_USERNAME[0]))
				|| (player.getUsername()
						.equalsIgnoreCase(org.endeavor.game.entity.player.PlayerConstants.OWNER_USERNAME[1]));
	}
}
