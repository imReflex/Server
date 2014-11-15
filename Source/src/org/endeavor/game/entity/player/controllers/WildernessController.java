package org.endeavor.game.entity.player.controllers;

import org.endeavor.GameSettings;
import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerConstants;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendPlayerOption;
import org.endeavor.game.entity.player.net.out.impl.SendString;
import org.endeavor.game.entity.player.net.out.impl.SendWalkableInterface;

public class WildernessController extends Controller {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6708888018467406507L;
	public static final String LEVEL_ATTRIBUTE = "wildlvlattr";

	@Override
	public void tick(Player player) {
		int lvl = player.getWildernessLevel();

		if ((player.getAttributes().get("wildlvlattr") == null)
				|| (player.getAttributes().getInt("wildlvlattr") != lvl)) {
			player.getAttributes().set("wildlvlattr", Integer.valueOf(lvl));
			player.getClient().queueOutgoingPacket(new SendString("@yel@Level: " + player.getWildernessLevel(), 199));
		}
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
		return false;
	}

	@Override
	public boolean canAttackNPC() {
		return true;
	}

	@Override
	public boolean canAttackPlayer(Player p, Player p2) {
		if ((!GameSettings.DEV_MODE) && (PlayerConstants.isOwner(p))) {
			p.getClient().queueOutgoingPacket(new SendMessage("You cannot attack in the wilderness."));
			return false;
		}
		if ((!GameSettings.DEV_MODE) && (PlayerConstants.isOwner(p2))) {
			p.getClient().queueOutgoingPacket(new SendMessage("You cannot attack this player."));
			return false;
		}

		if (p2.getController().equals(ControllerManager.WILDERNESS_CONTROLLER)) {
			int difference = Math.abs(p.getSkill().getCombatLevel() - p2.getSkill().getCombatLevel());

			if (difference > p.getWildernessLevel()) {
				p.getClient().queueOutgoingPacket(
						new SendMessage("You must move deeper in the Wilderness to attack this player."));
				return false;
			}
			if (difference > p2.getWildernessLevel()) {
				p.getClient().queueOutgoingPacket(
						new SendMessage("This player must move deeper in the Wilderness for you to attack him."));
				return false;
			}
		} else {
			p.getClient().queueOutgoingPacket(new SendMessage("This player is not in the Wilderness."));
			return false;
		}

		return true;
	}

	@Override
	public void onDisconnect(Player p) {
	}

	@Override
	public String toString() {
		return "WILDERNESS";
	}

	@Override
	public boolean canLogOut() {
		return true;
	}

	@Override
	public void onControllerInit(Player player) {
		player.getClient().queueOutgoingPacket(new SendPlayerOption("Attack", 3));
		player.getClient().queueOutgoingPacket(new SendPlayerOption("null", 4));
		player.getClient().queueOutgoingPacket(new SendWalkableInterface(197));
	}

	@Override
	public void onDeath(Player p) {
	}

	@Override
	public Location getRespawnLocation(Player player) {
		return new Location(PlayerConstants.EDGEVILLE);
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
		return true;
	}

	@Override
	public boolean allowPvPCombat() {
		return true;
	}

	@Override
	public boolean canSave() {
		return true;
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
