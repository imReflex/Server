package org.endeavor.game.entity.player.controllers;

import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendPlayerOption;

public class CWSafePKController extends Controller {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2506586315196362819L;

	@Override
	public void tick(Player paramPlayer) {
	}

	@Override
	public boolean canTalk() {
		return true;
	}

	@Override
	public boolean canMove(Player paramPlayer) {
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
	public boolean isSafe() {
		return true;
	}

	@Override
	public boolean canAttackNPC() {
		return false;
	}

	@Override
	public boolean canAttackPlayer(Player p1, Player p2) {
		if (p1 == null || p2 == null) {
			return false;
		}
		
		if (!p1.getController().equals(this) || p1.getY() <= 5511) {
			p1.getClient().queueOutgoingPacket(new SendMessage("You are outside the combat zone."));
			return false;
		}
		
		if (!p2.getController().equals(this) || p2.getY() <= 5511) {
			p1.getClient().queueOutgoingPacket(new SendMessage("This player is outside the combat zone."));
			return false;
		}
		
		return true;
	}

	@Override
	public boolean allowMultiSpells() {
		return true;
	}

	@Override
	public boolean allowPvPCombat() {
		return true;
	}

	@Override
	public void onDeath(Player paramPlayer) {
	}

	@Override
	public Location getRespawnLocation(Player player) {
		return new Location(2814, 5511);
	}

	@Override
	public boolean canLogOut() {
		return true;
	}

	@Override
	public void onDisconnect(Player paramPlayer) {
	}

	@Override
	public void onControllerInit(Player player) {
		player.getClient().queueOutgoingPacket(new SendPlayerOption("Attack", 3));
		player.getClient().queueOutgoingPacket(new SendPlayerOption("null", 4));
	}

	@Override
	public boolean canUseCombatType(Player paramPlayer, CombatTypes paramCombatTypes) {
		return true;
	}

	@Override
	public boolean canTeleport() {
		return false;
	}

	@Override
	public void onTeleport(Player paramPlayer) {
	}

	@Override
	public boolean canTrade() {
		return false;
	}

	@Override
	public boolean canEquip(Player paramPlayer, int paramInt1, int paramInt2) {
		return true;
	}

	@Override
	public boolean canUsePrayer(Player paramPlayer) {
		return true;
	}

	@Override
	public boolean canEat(Player paramPlayer) {
		return true;
	}

	@Override
	public boolean canDrink(Player paramPlayer) {
		return true;
	}

	@Override
	public boolean canUseSpecialAttack(Player paramPlayer) {
		return true;
	}

	@Override
	public boolean transitionOnWalk(Player paramPlayer) {
		return false;
	}

	@Override
	public String toString() {
		return "Clan Wars Safe PK";
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
