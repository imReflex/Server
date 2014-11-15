package org.endeavor.game.content.quest.impl.hftd;

import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.GenericMinigameController;

public class HFTDController extends GenericMinigameController {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2742871742751070603L;

	@Override
	public void onDeath(Player p) {
	}

	@Override
	public Location getRespawnLocation(Player player) {
		return new Location(3085, 3500, 0);
	}

	@Override
	public void onDisconnect(Player p) {
	}

	@Override
	public void tick(Player p) {
	}

	@Override
	public boolean isSafe() {
		return true;
	}

	@Override
	public boolean canAttackNPC() {
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
	public void onControllerInit(Player p) {
	}

	@Override
	public boolean canUseCombatType(Player p, CombatTypes type) {
		return true;
	}

	@Override
	public boolean canEquip(Player p, int id, int slot) {
		return true;
	}

	@Override
	public boolean canUsePrayer(Player p) {
		return true;
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
		return true;
	}

	@Override
	public String toString() {
		return "Horror From The Deep - boss";
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
}
