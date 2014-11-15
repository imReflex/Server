package org.endeavor.game.content.minigames.pestcontrol;

import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerConstants;
import org.endeavor.game.entity.player.controllers.Controller;
import org.endeavor.game.entity.player.net.out.impl.SendString;
import org.endeavor.game.entity.player.net.out.impl.SendWalkableInterface;

public class PestWaitingRoomController extends Controller {
	/**
	 * 
	 */
	private static final long serialVersionUID = -487715434067326740L;

	@Override
	public void tick(Player p) {
		p.getClient().queueOutgoingPacket(
				new SendString("Next Departure: " + (PestControl.getMinutesTillDepart() + 1) + " min", 14311));
		p.getClient().queueOutgoingPacket(new SendString("Players Ready: " + PestControl.getPlayersReady(), 14312));
		p.getClient().queueOutgoingPacket(new SendString("Pest points: " + p.getPestPoints(), 14314));
	}

	@Override
	public boolean canSave() {
		return false;
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
		return false;
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
		return false;
	}

	@Override
	public void onDisconnect(Player p) {
	}

	@Override
	public void onControllerInit(Player p) {
		p.getClient().queueOutgoingPacket(new SendWalkableInterface(14310));
		p.getClient().queueOutgoingPacket(new SendString("Pest Points: 0", 14314));
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
		return false;
	}

	@Override
	public boolean transitionOnWalk(Player p) {
		return false;
	}

	@Override
	public String toString() {
		return "PEST CONTROL";
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
