package org.endeavor.game.content.quest.impl.rfd;

import java.io.Serializable;

import org.endeavor.engine.task.TaskQueue;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.content.dialogue.Emotion;
import org.endeavor.game.content.quest.Quest;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.mob.impl.GelatinnothMother;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.ControllerManager;

public class RecipeForDisaster implements Quest, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8309714260232506088L;
	public static final RFDController RFD_CONTROLLER = new RFDController();

	@Override
	public void init(Player p) {
		p.teleport(new Location(1909, 5356, p.getIndex() * 4 + 2));
		p.setController(RFD_CONTROLLER);
		p.getQuesting().setQuestActive(this, true);
		TaskQueue.queue(new RFDWaveTask(p));
	}

	@Override
	public void doAction(Player p, byte stage) {
		p.getQuesting().incrQuestStage(this);
		
		if (p.getQuesting().isQuestCompleted(this)) {
			reward(p);
			return;
		}

		Location l = new Location(RFDConstants.SPAWN);
		l.setZ(p.getIndex() * 4 + 2);

		Mob m = null;

		if (stage != 4)
			m = new Mob(RFDConstants.WAVES[stage], false, l, p, false, false, null);
		else {
			m = new GelatinnothMother(l, p);
		}

		m.getFollowing().setIgnoreDistance(true);
		p.getAttributes().set("currentRFDMob", m);
	}

	@Override
	public void reward(Player p) {
		p.teleport(RFD_CONTROLLER.getRespawnLocation(p));
		p.getQuesting().setCompleted(this);
		p.setController(ControllerManager.DEFAULT_CONTROLLER);
		//DialogueManager.sendNpcChat(p, 3385, Emotion.CALM, new String[] { "You have defeated them!", "Thank you!" });
	}

	@Override
	public String getName() {
		return "Recipe for Disaster";
	}

	@Override
	public byte getFinalStage() {
		return 7;
	}

	@Override
	public boolean clickObject(Player p, int option, int id) {
		if (id == 12356) {
			p.teleport(RFD_CONTROLLER.getRespawnLocation(p));
			p.setController(ControllerManager.DEFAULT_CONTROLLER);
			DialogueManager.sendNpcChat(p, 3385, Emotion.SAD, new String[] { "You have failed!",
					"You must defeat them!" });
		}

		return false;
	}

	@Override
	public boolean clickButton(Player p, int id) {
		return false;
	}

	@Override
	public short getId() {
		return 0;
	}

	@Override
	public boolean useItemOnObject(Player p, int item, int object) {
		return false;
	}

	@Override
	public byte getPoints() {
		return 1;
	}

	@Override
	public String[] getLinesForStage(byte stage) {
		if (stage == 0)
			return new String[] { "You can start this quest by talking to the Gypsy.", "She is located in the Lumbridge castle basement."};
		if (stage < 5) {
			return new String[] { "You must defeat all the monsters to complete this quest." };
		}
		return new String[] { "You have saved this world from monsers.", "You can now purchase all metal gloves!" };
	}
}
