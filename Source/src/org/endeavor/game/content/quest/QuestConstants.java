package org.endeavor.game.content.quest;

import java.util.HashMap;
import java.util.Map;

import org.endeavor.game.content.quest.impl.hftd.HorrorFromTheDeep;
import org.endeavor.game.content.quest.impl.rfd.RecipeForDisaster;
import org.endeavor.game.content.quest.impl.runemysteries.RuneMysteries;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class QuestConstants {
	public static final Quest RECIPE_FOR_DISASTER = new RecipeForDisaster();
	public static final Quest HORROR_FROM_THE_DEEP = new HorrorFromTheDeep();
	public static final Quest RUNE_MYSTERIES = new RuneMysteries();

	public static final Quest[] QUESTS_BY_ID = { RECIPE_FOR_DISASTER, HORROR_FROM_THE_DEEP, RUNE_MYSTERIES };

	public static final int[] QUEST_BUTTONS = { 49228, 2161, 28184 };

	public static final int[] QUEST_LINE_IDS = { 12772, 673, 7352 };

	public static final short TOTAL_QUESTS = (short) QUESTS_BY_ID.length;

	private static final Map<String, Quest> questsByName = new HashMap<String, Quest>();
	
	public static boolean hasCompletedAllQuests(Player player) {
		for (Quest i : QUESTS_BY_ID) {
			if (!player.getQuesting().isQuestCompleted(i)) {
				return false;
			}
		}
		
		return true;
	}

	public static void declare() {
		for (Quest i : QUESTS_BY_ID)
			questsByName.put(i.getName(), i);
	}

	public static Quest getQuestForName(String name) {
		return questsByName.get(name);
	}

	public static boolean clickObject(Player p, int id) {
		switch (id) {
		case 4383://hftd lighthouse ladder downstairs
			p.teleport(new Location(2519, 4619, 1));
			break;
		
		case 4412:
			p.teleport(new Location(3085, 3500, 0));
			break;
		case 4543:
		case 4544:
		case 4545:
		case 4546:
			if (p.getLocation().getY() < 4627)
				p.teleport(new Location(2515, 4628, 1));
			else {
				p.teleport(new Location(2515, 4626, 1));
			}
			break;
		case 4413:
			p.teleport(new Location(2515, 4629, 1));
			p.setController(ControllerManager.DEFAULT_CONTROLLER);
			break;
		case 13405:
			if (p.getQuesting().isQuestCompleted(RECIPE_FOR_DISASTER)) {
				p.getClient().queueOutgoingPacket(new SendMessage("You have already defeated the monsters."));
			} else {
				RECIPE_FOR_DISASTER.init(p);
			}
			return true;
		}

		return false;
	}
}
