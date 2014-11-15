package org.endeavor.game.content.dialogue.impl;

import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueConstants;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.entity.player.Player;

public class SelectTitleDialogue extends Dialogue {
	
	private final String[] titles;
	
	private int page = 0;
	private boolean reverse = false;
	
	public SelectTitleDialogue(Player player) {
		this.player = player;
		this.titles = player.getTitles().getAvailableTitlesForSelection();
	}
	
	@Override
	public void execute() {
		if (titles == null || titles.length <= 1) {
			DialogueManager.sendStatement(player, "Your current title is the only one available for you.",
					"You can earn more titles by being active in the game.");
			end();
			return;
		}
		
		open(0);
	}
	
	public void open(int page) {
		this.page = page;
		int index = page * 4;
		int options = titles.length - index;
		
		if (index >= titles.length) {
			throw new IllegalArgumentException("Index too far (SelectTitleDialogue): " + index);
		}
		
		if (options < 4) {
			if (!reverse) {
				reverse = true;
			}
			
			if (titles.length <= 4) {
				if (options == 3) {
					DialogueManager.sendOption(player, titles[index], titles[index + 1], titles[index + 2]);
				} else if (options == 2) {
					DialogueManager.sendOption(player, titles[index], titles[index + 1]);
				} else if (options == 1) {
					DialogueManager.sendOption(player, titles[index]);
				} else {
					throw new IllegalArgumentException("0 option count (SelectTitleDialogue)??");
				}
			} else {
				if (options == 3) {
					DialogueManager.sendOption(player, titles[index], titles[index + 1], titles[index + 2], reverse ? "Back" : "Next");
				} else if (options == 2) {
					DialogueManager.sendOption(player, titles[index], titles[index + 1], reverse ? "Back" : "Next");
				} else if (options == 1) {
					DialogueManager.sendOption(player, titles[index], reverse ? "Back" : "Next");
				} else {
					throw new IllegalArgumentException("0 option count (SelectTitleDialogue)??");
				}
			}
		} else {
			if (reverse && page == 0) {
				reverse = false;
			} else if (!reverse && index + 4 >= titles.length) {
				reverse = true;
			}
			
			if (titles.length <= 4) {
				DialogueManager.sendOption(player, titles[index], titles[index + 1], titles[index + 2], titles[index + 3]);
			} else {
				DialogueManager.sendOption(player, titles[index], titles[index + 1], titles[index + 2], titles[index + 3], reverse ? "Back" : "Next");
			}
		}
	}
	
	public void onSelectTitle(String title) {
		player.setTitle(title);
		DialogueManager.sendStatement(player, "You've selected " + "'" + title + "'" + " as your title.");
		end();
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case DialogueConstants.OPTIONS_2_2:
			if (titles.length <= 4) {
				onSelectTitle(titles[getIndex() + 1]);
			} else {
				open(reverse ? page - 1 : page + 1);
			}
			return true;
		case DialogueConstants.OPTIONS_3_3:
			if (titles.length <= 4) {
				onSelectTitle(titles[getIndex() + 2]);
			} else {
				open(reverse ? page - 1 : page + 1);
			}
			return true;
		case DialogueConstants.OPTIONS_4_4:
			if (titles.length <= 4) {
				onSelectTitle(titles[getIndex() + 3]);
			} else {
				open(reverse ? page - 1 : page + 1);
			}
			return true;
		case DialogueConstants.OPTIONS_5_5:
			if (titles.length <= 4) {
				onSelectTitle(titles[getIndex() + 4]);
			} else {
				open(reverse ? page - 1 : page + 1);
			}
			return true;
			
		case DialogueConstants.OPTIONS_2_1:
		case DialogueConstants.OPTIONS_3_1:
		case DialogueConstants.OPTIONS_4_1:
		case DialogueConstants.OPTIONS_5_1:
			onSelectTitle(titles[getIndex()]);
			return true;
		case DialogueConstants.OPTIONS_3_2:
		case DialogueConstants.OPTIONS_4_2:
		case DialogueConstants.OPTIONS_5_2:
			onSelectTitle(titles[getIndex() + 1]);
			return true;
			
		case DialogueConstants.OPTIONS_4_3:
		case DialogueConstants.OPTIONS_5_3:
			onSelectTitle(titles[getIndex() + 2]);
			return true;
			
		case DialogueConstants.OPTIONS_5_4:
			onSelectTitle(titles[getIndex() + 3]);
			return true;
		}
		
		return false;
	}
	
	public int getIndex() {
		return page * 4;
	}

}
