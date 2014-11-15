package org.endeavor.game.content.minigames;

import java.io.Serializable;

import org.endeavor.game.content.minigames.burthrope.GameBetManager;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendString;

public class PlayerMinigames implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3388017797348859415L;
	private Player player;
	private boolean[] barrowsKilled = new boolean[6];

	private short[] kc = new short[4];

	private int lastDiceRoll = 0;
	public int zombiePoints;
	private int playerZeal;
	private final GameBetManager betManager;

	public PlayerMinigames(Player player) {
		betManager = new GameBetManager(player);
		this.player = player;
	}

	public void incrGWKC(int id) {
		int tmp5_4 = id;
		short[] tmp5_1 = kc;
		tmp5_1[tmp5_4] = ((short) (tmp5_1[tmp5_4] + 1));
		updateGWKC(id);
	}

	public void resetGWKC(int id) {
		int tmp5_4 = id;
		short[] tmp5_1 = kc;
		tmp5_1[tmp5_4] = ((short) (tmp5_1[tmp5_4] - 50));
		updateGWKC(id);
	}

	public void updateGWKC(int id) {
		if (id == 1)
			player.getClient().queueOutgoingPacket(new SendString("" + kc[1], 21354));
		else if (id == 0)
			player.getClient().queueOutgoingPacket(new SendString("" + kc[0], 21353));
		else if (id == 2)
			player.getClient().queueOutgoingPacket(new SendString("" + kc[2], 21355));
		else if (id == 3)
			player.getClient().queueOutgoingPacket(new SendString("" + kc[3], 21352));
	}

	public short[] getGWKC() {
		return kc;
	}

	public void setGWKC(short[] kc) {
		this.kc = kc;
	}

	public int getLastDiceRoll() {
		return lastDiceRoll;
	}

	public void setLastDiceRoll(int lastDiceRoll) {
		this.lastDiceRoll = lastDiceRoll;
	}

	public void setBarrowKilled(int i) {
		barrowsKilled[i] = true;
	}

	public void resetBarrows() {
		barrowsKilled = new boolean[6];

		for (int i = 2025; i <= 2030; i++)
			player.getAttributes().remove("barrowsActive" + i);
	}

	public boolean killedBarrow(int i) {
		return barrowsKilled[i];
	}

	public int getBarrowsKillcount() {
		int i = 0;

		for (boolean k : barrowsKilled) {
			if (k) {
				i++;
			}
		}

		return i;
	}

	public boolean[] getBarrowsKilled() {
		return barrowsKilled;
	}

	public void setBarrowsKilled(boolean[] barrowsKilled) {
		this.barrowsKilled = barrowsKilled;
	}

	public GameBetManager getBetManager() {
		return betManager;
	}

	public int getZombiePoints() {
		return zombiePoints;
	}

	public void setZombiePoints(int points) {
		zombiePoints = points;
	}

	public int getZeal() {
		return playerZeal;
	}

	public void setZeal(int zeal) {
		playerZeal = zeal;
	}
}
