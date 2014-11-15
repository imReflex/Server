package org.endeavor.game.content.minigames.fightcave;

import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.Controller;

public final class TzharrController extends Controller {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1986264173061162794L;
	public static final String MINIGAME = "Tzharr Fight Caves";

	@Override
	public void tick(Player p) {
	}

	@Override
	public boolean canSave() {
		return true;
	}

	@Override
	public boolean canTalk() {
		return true;
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
	public void onDeath(Player p) {
		TzharrGame.finish(p, false);
	}

	@Override
	public Location getRespawnLocation(Player player) {
		return TzharrGame.LEAVE;
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
		return true;
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
	public boolean transitionOnWalk(Player p) {
		return false;
	}

	@Override
	public String toString() {
		return "Tzharr Fight Caves";
	}

	@Override
	public boolean allowMultiSpells() {
		return true;
	}

	@Override
	public boolean allowPvPCombat() {
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
}
