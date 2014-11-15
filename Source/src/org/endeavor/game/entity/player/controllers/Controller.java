package org.endeavor.game.entity.player.controllers;

import java.io.Serializable;

import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;

public abstract class Controller implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1713446971582217179L;

	public abstract void tick(Player paramPlayer);

	public abstract boolean onClick(Player player, int buttonID);
	
	public abstract boolean onObject(Player player, int objectID);
	
	public abstract boolean onNpc(Player player, int npcID);
	
	public abstract boolean canTalk();

	public abstract boolean canMove(Player paramPlayer);

	public abstract boolean canSave();

	public abstract boolean canClick();

	public abstract boolean isSafe();

	public abstract boolean canAttackNPC();

	public abstract boolean canAttackPlayer(Player paramPlayer1, Player paramPlayer2);

	public abstract boolean allowMultiSpells();

	public abstract boolean allowPvPCombat();

	public abstract void onDeath(Player paramPlayer);

	public abstract Location getRespawnLocation(Player player);

	public abstract boolean canLogOut();

	public abstract void onDisconnect(Player paramPlayer);

	public abstract void onControllerInit(Player paramPlayer);

	public abstract boolean canUseCombatType(Player paramPlayer, CombatTypes paramCombatTypes);

	public abstract boolean canTeleport();

	public abstract void onTeleport(Player paramPlayer);

	public abstract boolean canTrade();

	public abstract boolean canEquip(Player paramPlayer, int paramInt1, int paramInt2);

	public abstract boolean canUsePrayer(Player paramPlayer);

	public abstract boolean canEat(Player paramPlayer);

	public abstract boolean canDrink(Player paramPlayer);

	public abstract boolean canUseSpecialAttack(Player paramPlayer);

	public abstract boolean transitionOnWalk(Player paramPlayer);

	@Override
	public abstract String toString();

	public void throwException(Player player, String action) {
		System.out.println("||||||||||||||||||||||||||");
		System.out.println("UNABLE TO " + action + " FOR PLAYER " + player.getUsername() + "!");
		System.out.println("CONTROLLER: " + player.getController().toString());
		System.out.println("||||||||||||||||||||||||||");
	}
}
