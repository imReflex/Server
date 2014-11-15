package org.endeavor.game.content.quest.impl.runemysteries;

import java.io.Serializable;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.content.dialogue.Emotion;
import org.endeavor.game.content.quest.Quest;
import org.endeavor.game.content.quest.QuestConstants;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendResetCamera;
import org.endeavor.game.entity.player.net.out.impl.SendShakeScreen;

public class RuneMysteries implements Quest, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4894515711979120550L;
	public static final String MAGE_CAVE_NPC = "magecavenpc";

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
		return "Rune Mysteries";
	}

	@Override
	public byte getFinalStage() {
		return 7;
	}

	@Override
	public short getId() {
		return 2;
	}

	@Override
	public boolean useItemOnObject(Player p, int item, int object) {
		return false;
	}

	@Override
	public boolean clickObject(final Player p, int option, int id) {
		if ((id == 2971) && (p.getQuesting().getQuestStage(this) == 5)) {
			p.setControllerNoInit(new CaveController() {
				/**
				 * 
				 */
				private static final long serialVersionUID = -6929472242129415657L;

				@Override
				public boolean canMove(Player p) {
					return false;
				}

				@Override
				public boolean canClick() {
					return false;
				}

				@Override
				public boolean canLogOut() {
					return false;
				}

				@Override
				public boolean onClick(Player player, int buttonID) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public boolean onObject(Player player, int objectID) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public boolean onNpc(Player player, int npcID) {
					// TODO Auto-generated method stub
					return false;
				}
			});
			TaskQueue.queue(new Task(p, 6, true) {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1007841649773403646L;
				private byte pos = 0;

				@Override
				public void execute() {
					if (pos == 0) {
						DialogueManager.sendTimedNpcChat(p, 881, Emotion.SCARED, new String[] {
								"Wait, maybe this will work..", "" });
					} else if (pos == 1) {
						DialogueManager.sendTimedNpcChat(p, 881, Emotion.CRAZY, new String[] { "En enigma..", "" });
					} else if (pos == 2) {
						DialogueManager.sendTimedNpcChat(p, 881, Emotion.CRAZY, new String[] { "VON BLUR!", "" });
						setTaskDelay(2);
					} else if (pos == 3) {
						p.getClient().queueOutgoingPacket(new SendShakeScreen(true));
						setTaskDelay(6);
					} else if (pos == 4) {
						DialogueManager.sendTimedPlayerChat(p, Emotion.SCARED, new String[] { "Oh NO!" });
					} else if (pos == 5) {
						DialogueManager.sendTimedNpcChat(p, 881, Emotion.SCARED, new String[] { "So long old friend!",
								"Perhaps I'll see you in heaven!" });
					} else if (pos == 6) {
						DialogueManager.sendTimedPlayerChat(p, Emotion.SCARED, new String[] { "AHHHH!" });

						Mob mob = (Mob) p.getAttributes().get("magecavenpc");

						if (mob != null) {
							mob.remove();
						}

						p.getAttributes().remove("magecavenpc");
					} else if (pos == 7) {
						p.doFadeTeleport(new Location(2990, 3367), true);
						setTaskDelay(4);
						p.getQuesting().incrQuestStage(QuestConstants.RUNE_MYSTERIES);
					} else if (pos == 8) {
						p.getClient().queueOutgoingPacket(new SendResetCamera());
						stop();
					}

					pos = ((byte) (pos + 1));
				}

				@Override
				public void onStop() {
				}
			});
		}
		return false;
	}

	@Override
	public boolean clickButton(Player p, int id) {
		return false;
	}

	@Override
	public byte getPoints() {
		return 2;
	}

	@Override
	public String[] getLinesForStage(byte stage) {
		if (stage == 0) {
			return new String[] { "You can start this quest by talking to", "the Mage of Zamorak in Edgeville.", "He is located in level five wild near the river."};
		}

		if (stage == 1) {
			return new String[] { "I must take the Talisman to the Mage in Falador.", "He will tell me what to do." };
		}
		if (stage == 2) {
			return new String[] { "I must get some items for Traiborn.", "He needs:", "13 Bones", "2 Dragon bones",
					"A rope" };
		}
		if (stage == 3) {
			return new String[] { "I've given the items to Traiborn", "Now we will see what he wants me to do." };
		}
		if (stage == 4) {
			return new String[] { "Where are we?" };
		}
		if (stage == 5) {
			return new String[] { "We must leave this place!" };
		}
		if (stage == 6) {
			return new String[] { "We got out alive, now I should talk to Traiborn." };
		}
		if (stage == 7) {
			return new String[] { "Traiborn and I braved the unknown", "and got out alive!" };
		}
		
		Exception e = new Exception("Unknown quest line for stage: " + stage);
		e.printStackTrace();

		return null;
	}
}
