package org.endeavor.game.content.quest.impl.hftd;

import java.io.Serializable;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.game.content.quest.Quest;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.mob.impl.GelatinnothMother;
import org.endeavor.game.entity.player.Player;

public class HorrorFromTheDeep implements Quest,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6778961119944564913L;
	public static final HFTDController HFTD_CONTROLLER = new HFTDController();

	@Override
	public void init(Player p) {
	}

	@Override
	public void doAction(Player p, byte stage) {
	}

	@Override
	public void reward(Player p) {
	}

	@Override
	public String getName() {
		return "Horror From The Deep";
	}

	@Override
	public byte getFinalStage() {
		return 2;
	}

	@Override
	public short getId() {
		return 1;
	}

	@Override
	public boolean clickObject(final Player p, int option, int id) {
		if (id == 4485) {
			p.setController(HFTD_CONTROLLER);
			final Quest quest = this;
			final Mob boss = new GelatinnothMother(new Location(2525, 4652, p.getIndex() * 4), p) {
				/**
				 * 
				 */
				private static final long serialVersionUID = -8041223789212727540L;

				@Override
				public void onDeath() {
					p.getQuesting().incrQuestStage(quest);
				}
			};

			boss.getFollowing().setIgnoreDistance(true);
			boss.getCombat().setAttack(p);

			p.getPrayer().disable();
			
			TaskQueue.queue(new Task(p, 1) {
				/**
				 * 
				 */
				private static final long serialVersionUID = 2360231801629394330L;

				@Override
				public void execute() {
					if (p.getZ() != boss.getZ() && !boss.isDead() || p.getQuesting().isQuestCompleted(quest)) {
						boss.remove();
						stop();
						return;
					}
				}

				@Override
				public void onStop() {
				}
			});
			p.teleport(new Location(2515, 4631, p.getIndex() * 4));
			return true;
		}

		return false;
	}

	@Override
	public boolean clickButton(Player p, int id) {
		return false;
	}

	@Override
	public boolean useItemOnObject(Player p, int item, int object) {
		/*if ((object == 26945) && (item == 954)) {
			p.teleport(new Location(2519, 4619, 1));
			return true;
		}*/
		return false;
	}

	@Override
	public byte getPoints() {
		return 2;
	}

	@Override
	public String[] getLinesForStage(byte stage) {
		if (stage == 0) {
			return new String[] { "I can start this quest by talking to Jossik near Rellekka.", "You need 35 Agility to start this quest." };
		} else if (stage == 1) {
			return new String[] { "I must go to The Seer in Seers' Village to get the key!",
					"Once I have it I can enter the lighthouse ",
					"and find Jossik's wife."};
		}
		
		return new String[] { "I've defeated the beast!", "I should return to Jossik for my reward." };
	}
}
