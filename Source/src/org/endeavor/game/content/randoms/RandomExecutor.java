package org.endeavor.game.content.randoms;

import org.endeavor.GameSettings;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.minigames.bountyhunter.BountyHunterController;
import org.endeavor.game.content.minigames.dungeoneering.DungConstants;
import org.endeavor.game.content.randoms.impl.Genie;
import org.endeavor.game.content.randoms.impl.PrisonPete;
import org.endeavor.game.content.randoms.impl.SecurityGuard;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.ControllerManager;

public class RandomExecutor {
	public static final int RANDOM_EVENT_CHANCE = 100;
	public static final long RANDOM_EVENT_COOLDOWN = 600L;
	public static final long RANDOM_CHANCE_COOLDOWN = 2000L;
	public static final String RANDOM_EVENT_COOLDOWN_KEY = "randomcooldown";
	public static final String RANDOM_CHANCE_COOLDOWN_KEY = "randomcooldown";
	public static final RandomEvent[] RANDOM_EVENTS = { new Genie(), new PrisonPete(), new SecurityGuard() };

	public static void tryRandom(Player player) {
		if(GameSettings.DEV_MODE)
			return;
		if (player.getController().allowPvPCombat() && !player.getController().equals(ControllerManager.WILDERNESS_CONTROLLER) 
				|| player.getController().equals(ControllerManager.DUEL_ARENA_CONTROLLER) 
				|| player.getController().equals(ControllerManager.DUEL_STAKE_CONTROLLER) 
				|| player.getController().equals(DungConstants.DUNG_CONTROLLER) ||
				player.getController() instanceof BountyHunterController) {
			return;
		}
		
		if (player.getAttributes().get(RANDOM_EVENT_COOLDOWN_KEY) != null) {
			long cool = player.getAttributes().getLong(RANDOM_EVENT_COOLDOWN_KEY);
			
			if (World.getCycles() == cool) {
				return;
			} else {
				player.getAttributes().set(RANDOM_EVENT_COOLDOWN_KEY, World.getCycles());
			}
			
		} else {
			player.getAttributes().set(RANDOM_EVENT_COOLDOWN_KEY, World.getCycles());
		}

		if (Misc.randomNumber(500) <= 2 && player.getAttributes().get("randomevent") == null) {
			RANDOM_EVENTS[Misc.randomNumber(RANDOM_EVENTS.length)].init(player);
		}
	}
}
