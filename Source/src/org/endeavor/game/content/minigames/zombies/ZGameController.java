package org.endeavor.game.content.minigames.zombies;

import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.GenericMinigameController;
import org.endeavor.game.entity.player.net.out.impl.SendString;
import org.endeavor.game.entity.player.net.out.impl.SendWalkableInterface;

public class ZGameController extends GenericMinigameController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8578220875297623220L;

	@Override
	public void tick(Player paramPlayer) {
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
	public boolean canAttackPlayer(Player paramPlayer1, Player paramPlayer2) {
		return false;
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
	public void onDeath(Player player) {
	}

	@Override
	public Location getRespawnLocation(Player player) {
		return new Location(3484, 3238);
	}

	@Override
	public void onDisconnect(Player player) {
		player.teleport(new Location(3485, 3246));
	}

	@Override
	public void onControllerInit(Player player) {
		player.getClient().queueOutgoingPacket(new SendWalkableInterface(17600));
		player.getClient().queueOutgoingPacket(new SendString("", 17601));
		player.getClient().queueOutgoingPacket(new SendString("", 17602));
		player.getClient().queueOutgoingPacket(new SendString("", 17603));
		player.getClient().queueOutgoingPacket(new SendString("", 17604));
		player.getClient().queueOutgoingPacket(new SendString("", 17605));
	}

	@Override
	public boolean canUseCombatType(Player paramPlayer,
			CombatTypes paramCombatTypes) {
		return true;
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
	public String toString() {
		
		return "Zombies game";
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
