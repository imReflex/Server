package org.endeavor.game.entity.player.controllers;

import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;

public abstract class GenericWaitingRoomController extends GenericMinigameController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3600160383994383963L;

	@Override
	public void onDeath(Player p) {
	}

	@Override
	public Location getRespawnLocation(Player player) {
		return null;
	}

	@Override
	public boolean canAttackNPC() {
		return false;
	}

	@Override
	public boolean canSave() {
		return false;
	}

	@Override
	public abstract void onControllerInit(Player paramPlayer);

	@Override
	public abstract void onDisconnect(Player paramPlayer);

	@Override
	public boolean isSafe() {
		return true;
	}

	@Override
	public boolean canAttackPlayer(Player p, Player p2) {
		return false;
	}

	@Override
	public boolean allowMultiSpells() {
		return false;
	}

	@Override
	public boolean allowPvPCombat() {
		return false;
	}

	@Override
	public boolean canUseCombatType(Player p, CombatTypes type) {
		return false;
	}

	@Override
	public boolean canEquip(Player p, int id, int slot) {
		return true;
	}

	@Override
	public boolean canUsePrayer(Player p) {
		return false;
	}

	@Override
	public boolean canEat(Player p) {
		return true;
	}

	@Override
	public boolean canDrink(Player p) {
		return true;
	}

	@Override
	public boolean canUseSpecialAttack(Player p) {
		return false;
	}

	@Override
	public abstract String toString();
}
