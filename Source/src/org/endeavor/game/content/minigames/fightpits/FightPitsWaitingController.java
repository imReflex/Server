package org.endeavor.game.content.minigames.fightpits;

import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerConstants;
import org.endeavor.game.entity.player.controllers.Controller;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.out.impl.SendPlayerOption;
import org.endeavor.game.entity.player.net.out.impl.SendString;
import org.endeavor.game.entity.player.net.out.impl.SendWalkableInterface;

public class FightPitsWaitingController extends Controller {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4803421932841650109L;

	@Override
	public void tick(Player p) {
		FightPits.updateInterface(p);
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
		return true;
	}

	@Override
	public boolean canAttackPlayer(Player p, Player p2) {
		return false;
	}

	@Override
	public void onDeath(Player p) {
		p.setController(ControllerManager.DEFAULT_CONTROLLER);
		FightPits.removeFromWaitingRoom(p);
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
		FightPits.removeFromWaitingRoom(p);
	}

	@Override
	public void onControllerInit(Player p) {
		FightPits.updateInterface(p);
		p.getClient().queueOutgoingPacket(new SendPlayerOption("null", 3));
		p.getClient().queueOutgoingPacket(new SendPlayerOption("null", 4));
		for (int i = 2; i < FightPitsConstants.FIGHT_PITS_INTERFACE_STRINGS.length; i++) {
			p.getClient().queueOutgoingPacket(new SendString("", FightPitsConstants.FIGHT_PITS_INTERFACE_STRINGS[i]));
		}
		p.getClient().queueOutgoingPacket(new SendWalkableInterface(17600));
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
	public String toString() {
		return "FIGHT PITS WAITING ROOM";
	}

	@Override
	public boolean transitionOnWalk(Player p) {
		return false;
	}

	@Override
	public void onTeleport(Player p) {
		FightPits.removeFromWaitingRoom(p);
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
