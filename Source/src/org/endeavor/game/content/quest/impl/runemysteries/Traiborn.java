package org.endeavor.game.content.quest.impl.runemysteries;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.content.dialogue.Emotion;
import org.endeavor.game.content.dialogue.impl.Tutorial;
import org.endeavor.game.content.quest.QuestConstants;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendResetCamera;
import org.endeavor.game.entity.player.net.out.impl.SendShakeScreen;

public class Traiborn extends Dialogue {
	public static final int ID = 881;

	public Traiborn(Player player) {
		this.player = player;
	}

	@Override
	public void execute() {
		if (player.getQuesting().isQuestActive(QuestConstants.RUNE_MYSTERIES)) {
			if (player.getQuesting().getQuestStage(QuestConstants.RUNE_MYSTERIES) == 1) {
				switch (next) {
				case 0:
					DialogueManager.sendNpcChat(player, 881, Emotion.CALM, new String[] { "How can I help you?" });
					next += 1;
					break;
				case 1:
					if (player.getInventory().hasItemId(1438)) {
						next += 1;
						DialogueManager.sendStatement(player, new String[] { "You show Traiborn the Talisman." });
					} else {
						DialogueManager.sendPlayerChat(player, Emotion.CONFUSED, new String[] {
								"I seem to have forgot the Talisman!", "I'll be right back!" });
						end();
					}
					break;
				case 2:
					DialogueManager.sendNpcChat(player, 881, Emotion.NOWAY, new String[] { "Wow!",
							"That is extraordinary!" });
					next += 1;
					break;
				case 3:
					DialogueManager.sendPlayerChat(player, Emotion.CALM, new String[] { "What is it?" });
					next += 1;
					break;
				case 4:
					DialogueManager.sendNpcChat(player, 881, Emotion.CALM,
							new String[] { "This is one of the talismans left over from the creation." });
					next += 1;
					break;
				case 5:
					DialogueManager.sendNpcChat(player, 881, Emotion.CALM,
							new String[] { "It's said that only the chosen one can use it." });
					next += 1;
					break;
				case 6:
					DialogueManager.sendNpcChat(player, 881, Emotion.CONFUSED,
							new String[] { "Wait..", "What's this?" });
					next += 1;
					break;
				case 7:
					DialogueManager.sendNpcChat(player, 881, Emotion.CONFUSED,
							new String[] { "There is some writing on the back.." });
					next += 1;
					break;
				case 8:
					DialogueManager.sendNpcChat(player, 881, Emotion.CALM, new String[] { "En enigma von blur.." });
					next += 1;
					break;
				case 9:
					DialogueManager.sendPlayerChat(player, Emotion.NOWAY, new String[] { "What does it mean?" });
					next += 1;
					break;
				case 10:
					DialogueManager.sendNpcChat(player, 881, Emotion.CALM, new String[] { "I can't be sure.",
							"But it looks like some kind of spell." });
					next += 1;
					break;
				case 11:
					DialogueManager.sendPlayerChat(player, Emotion.NOWAY, new String[] { "What does it do?" });
					next += 1;
					break;
				case 12:
					DialogueManager.sendNpcChat(player, 881, Emotion.CALM,
							new String[] { "I guess we will have to find out.." });
					next += 1;
					break;
				case 13:
					DialogueManager.sendNpcChat(player, 881, Emotion.CALM, new String[] {
							"I'm going to need 2 Dragon bones,", "13 Regular bones, and a Rope." });
					next += 1;
					break;
				case 14:
					DialogueManager.sendPlayerChat(player, Emotion.PRIDEFULL, new String[] { "I'm on it!" });
					player.getQuesting().incrQuestStage(QuestConstants.RUNE_MYSTERIES);
					end();
				default:
					break;
				}
			} else if (player.getQuesting().getQuestStage(QuestConstants.RUNE_MYSTERIES) == 2) {
				switch (next) {
				case 0:
					DialogueManager.sendNpcChat(player, 881, Emotion.CALM,
							new String[] { "Do you have the items I asked for?" });
					next += 1;
					break;
				case 1:
					if ((player.getInventory().hasItemAmount(526, 13)) && (player.getInventory().hasItemAmount(536, 2))
							&& (player.getInventory().hasItemAmount(954, 1))) {
						DialogueManager.sendPlayerChat(player, Emotion.CALM, new String[] { "Yes, here you go!" });
						next += 1;
					} else {
						DialogueManager.sendPlayerChat(player, Emotion.CALM, new String[] { "No, not yet." });
						end();
					}
					break;
				case 2:
					DialogueManager.sendStatement(player, new String[] { "You hand over the items." });
					player.getInventory().remove(526, 13, false);
					player.getInventory().remove(536, 2, false);
					player.getInventory().remove(954, 1, true);
					player.getQuesting().incrQuestStage(QuestConstants.RUNE_MYSTERIES);
					next = 0;
				default:
					break;
				}
			} else if (player.getQuesting().getQuestStage(QuestConstants.RUNE_MYSTERIES) == 3) {
				switch (next) {
				case 0:
					DialogueManager.sendNpcChat(player, 881, Emotion.CALM, new String[] { "Alright.. here we go." });
					next += 1;
					break;
				case 1:
					player.setController(Tutorial.TUTORIAL_CONTROLLER);

					TaskQueue.queue(new Task(player, 6, true) {
						/**
						 * 
						 */
						private static final long serialVersionUID = 9083019922507482010L;
						private byte pos = 0;

						@Override
						public void execute() {
							if (pos == 0) {
								DialogueManager.sendTimedNpcChat(player, 881, Emotion.SCARED, new String[] {
										"Well, here goes nothing..", "" });
							} else if (pos == 1) {
								DialogueManager.sendTimedNpcChat(player, 881, Emotion.CRAZY, new String[] {
										"En enigma..", "" });
							} else if (pos == 2) {
								DialogueManager.sendTimedNpcChat(player, 881, Emotion.CRAZY, new String[] {
										"VON BLUR!", "" });
								setTaskDelay(2);
							} else if (pos == 3) {
								player.getClient().queueOutgoingPacket(new SendShakeScreen(true));
								setTaskDelay(6);
							} else if (pos == 4) {
								DialogueManager.sendTimedPlayerChat(player, Emotion.SCARED,
										new String[] { "What's happening?!" });
							} else if (pos == 5) {
								DialogueManager.sendTimedNpcChat(player, 881, Emotion.SCARED, new String[] {
										"I do not know!", "In the name of Saradomin, please stop!" });
							} else if (pos == 6) {
								DialogueManager.sendTimedPlayerChat(player, Emotion.SCARED, new String[] { "AHHHH!" });
							} else if (pos == 7) {
								player.doFadeTeleport(new Location(2384, 4721), false);
								setTaskDelay(4);
								player.getQuesting().incrQuestStage(QuestConstants.RUNE_MYSTERIES);
							} else if (pos == 8) {
								player.getClient().queueOutgoingPacket(new SendResetCamera());
								player.setController(new CaveController() {
									/**
									 * 
									 */
									private static final long serialVersionUID = 9095248654046595236L;

									@Override
									public boolean canMove(Player p) {
										return false;
									}

									@Override
									public boolean onClick(Player player,
											int buttonID) {
										// TODO Auto-generated method stub
										return false;
									}

									@Override
									public boolean onObject(Player player,
											int objectID) {
										// TODO Auto-generated method stub
										return false;
									}

									@Override
									public boolean onNpc(Player player,
											int npcID) {
										// TODO Auto-generated method stub
										return false;
									}
								});
							} else if (pos == 9) {
								player.start(new Traiborn(player));
								stop();
							}

							pos = ((byte) (pos + 1));
						}

						@Override
						public void onStop() {
						}
					});
				default:
					break;
				}
			} else if (player.getQuesting().getQuestStage(QuestConstants.RUNE_MYSTERIES) == 4) {
				switch (next) {
				case 0:
					DialogueManager.sendNpcChat(player, 881, Emotion.SCARED, new String[] { "Finally, you are awake.",
							"I feared the worst for you!" });
					next += 1;
					break;
				case 1:
					DialogueManager.sendPlayerChat(player, Emotion.SCARED, new String[] { "Where are we!?" });
					next += 1;
					break;
				case 2:
					DialogueManager.sendNpcChat(player, 881, Emotion.SCARED, new String[] {
							"I'm not sure, but we must get out!", "I sense impending doom!" });
					next += 1;
					break;
				case 3:
					DialogueManager.sendPlayerChat(player, Emotion.SCARED,
							new String[] { "LET'S GET THE HELL OUT OF HERE!" });
					player.getQuesting().incrQuestStage(QuestConstants.RUNE_MYSTERIES);
					end();

					player.setControllerNoInit(new CaveController() {
						/**
						 * 
						 */
						private static final long serialVersionUID = -7207183821747181719L;

						@Override
						public boolean canMove(Player p) {
							return true;
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
				default:
					break;
				}
			} else if (player.getQuesting().getQuestStage(QuestConstants.RUNE_MYSTERIES) == 5) {
				DialogueManager.sendNpcChat(player, 881, Emotion.SCARED,
						new String[] { "Hurry, we must leave this dreadful place!" });
				end();
			} else if (player.getQuesting().getQuestStage(QuestConstants.RUNE_MYSTERIES) == 6) {
				switch (next) {
				case 0:
					DialogueManager.sendNpcChat(player, 881, Emotion.HAPPY_TALK,
							new String[] { "Wow, I can't believe we made it out alive." });
					next += 1;
					break;
				case 1:
					DialogueManager.sendPlayerChat(player, Emotion.CALM, new String[] { "It appears we did." });
					next += 1;
					break;
				case 2:
					DialogueManager.sendNpcChat(player, 881, Emotion.CALM, new String[] { "Maybe we did.." });
					next += 1;
					break;
				case 3:
					DialogueManager.sendNpcChat(player, 881, Emotion.CALM, new String[] {
							"But I fear the worst is yet to come.", "So long old friend." });
					next += 1;
					break;
				case 4:
					end();
					player.getQuesting().incrQuestStage(QuestConstants.RUNE_MYSTERIES);
				default:
					break;
				}
			}
		} else if (player.getQuesting().isQuestCompleted(QuestConstants.RUNE_MYSTERIES)) {
			DialogueManager.sendNpcChat(player, 881, Emotion.CALM, new String[] { "I've taught you all I can." });
			end();
		}
	}

	@Override
	public boolean clickButton(int id) {
		return false;
	}
}
