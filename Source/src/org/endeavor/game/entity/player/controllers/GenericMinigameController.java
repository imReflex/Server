package org.endeavor.game.entity.player.controllers;

import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;

public abstract class GenericMinigameController extends Controller {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3517832538644779664L;

	@Override
	public abstract void tick(Player paramPlayer);

	@Override
	public boolean canTalk() {
		return true;
	}

	@Override
	public boolean canMove(Player p) {
		return true;
	}

	@Override
	public boolean canSave() {
		return false;
	}

	@Override
	public boolean canClick() {
		return true;
	}

	@Override
	public abstract boolean isSafe();

	@Override
	public abstract boolean canAttackNPC();

	@Override
	public abstract boolean canAttackPlayer(Player paramPlayer1, Player paramPlayer2);

	@Override
	public abstract boolean allowMultiSpells();

	@Override
	public abstract boolean allowPvPCombat();

	@Override
	public abstract void onDeath(Player paramPlayer);

	@Override
	public abstract Location getRespawnLocation(Player player);

	@Override
	public boolean canLogOut() {
		return false;
	}

	@Override
	public abstract void onDisconnect(Player paramPlayer);

	@Override
	public abstract void onControllerInit(Player paramPlayer);

	@Override
	public abstract boolean canUseCombatType(Player paramPlayer, CombatTypes paramCombatTypes);

	@Override
	public boolean canTeleport() {
		return false;
	}

	@Override
	public void onTeleport(Player p) {
	}

	@Override
	public boolean canTrade() {
		return false;
	}

	@Override
	public abstract boolean canEquip(Player paramPlayer, int paramInt1, int paramInt2);

	@Override
	public abstract boolean canUsePrayer(Player paramPlayer);

	@Override
	public abstract boolean canEat(Player paramPlayer);

	@Override
	public abstract boolean canDrink(Player paramPlayer);

	@Override
	public abstract boolean canUseSpecialAttack(Player paramPlayer);

	@Override
	public boolean transitionOnWalk(Player p) {
		return false;
	}

	@Override
	public abstract String toString();
}
