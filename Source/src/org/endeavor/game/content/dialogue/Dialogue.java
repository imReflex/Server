package org.endeavor.game.content.dialogue;

import org.endeavor.game.entity.player.Player;

public abstract class Dialogue {
	protected int next = 0;
	protected int option;
	protected Player player;

	public abstract void execute();

	public abstract boolean clickButton(int id);

	public void end() {
		next = -1;
	}

	public int getNext() {
		return next;
	}

	public void setNext(int next) {
		this.next = next;
	}

	public int getOption() {
		return option;
	}

	public void setOption(int option) {
		this.option = option;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
