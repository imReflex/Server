package org.endeavor.game.content.minigames.duelarena;

import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerConstants;
import org.endeavor.game.entity.player.controllers.Controller;
import org.endeavor.game.entity.player.net.out.impl.SendPlayerOption;
import org.endeavor.game.entity.player.net.out.impl.SendWalkableInterface;

public class DuelArenaController extends Controller {
	@Override
	public void tick(Player player) {
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
	public boolean canSave() {
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
		return true;
	}

	@Override
	public boolean canAttackPlayer(Player p, Player p2) {
		return false;
	}

	@Override
	public void onDeath(Player p) {
	}

	@Override
	public Location getRespawnLocation(Player player) {
		return new Location(PlayerConstants.EDGEVILLE);
	}

	@Override
	public boolean canLogOut() {
		return true;
	}

	@Override
	public void onDisconnect(Player p) {
	}

	@Override
	public void onControllerInit(Player player) {
		player.getClient().queueOutgoingPacket(new SendPlayerOption("null", 3));
		player.getClient().queueOutgoingPacket(new SendPlayerOption("Challenge", 4));
		player.getClient().queueOutgoingPacket(new SendWalkableInterface(201));
	}

	@Override
	public String toString() {
		return "DUEL ARENA";
	}

	@Override
	public boolean canUseCombatType(Player p, CombatTypes type) {
		return true;
	}

	@Override
	public boolean canTeleport() {
		return true;
	}

	@Override
	public boolean canTrade() {
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
	public boolean transitionOnWalk(Player p) {
		return true;
	}

	@Override
	public void onTeleport(Player p) {
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
