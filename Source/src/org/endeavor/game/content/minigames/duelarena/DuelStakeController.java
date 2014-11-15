package org.endeavor.game.content.minigames.duelarena;

import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerConstants;
import org.endeavor.game.entity.player.controllers.Controller;

public class DuelStakeController extends Controller {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1957391373265885864L;

	@Override
	public void tick(Player p) {
	}

	@Override
	public boolean canTalk() {
		return true;
	}

	@Override
	public boolean canSave() {
		return false;
	}

	@Override
	public boolean canMove(Player p) {
		return true;
	}

	@Override
	public boolean canClick() {
		return true;
	}

	@Override
	public boolean isSafe() {
		return false;
	}

	@Override
	public boolean canAttackNPC() {
		return false;
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
	public void onDeath(Player p) {
	}

	@Override
	public Location getRespawnLocation(Player player) {
		return PlayerConstants.HOME;
	}

	@Override
	public boolean canLogOut() {
		return true;
	}

	@Override
	public void onDisconnect(Player p) {
	}

	@Override
	public void onControllerInit(Player p) {
	}

	@Override
	public boolean canUseCombatType(Player p, CombatTypes type) {
		return false;
	}

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
	public boolean canEquip(Player p, int id, int slot) {
		return false;
	}

	@Override
	public boolean canUsePrayer(Player p) {
		return false;
	}

	@Override
	public boolean canEat(Player p) {
		return false;
	}

	@Override
	public boolean canDrink(Player p) {
		return false;
	}

	@Override
	public boolean canUseSpecialAttack(Player p) {
		return false;
	}

	@Override
	public boolean transitionOnWalk(Player p) {
		return false;
	}

	@Override
	public String toString() {
		return "Duel Stake";
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
