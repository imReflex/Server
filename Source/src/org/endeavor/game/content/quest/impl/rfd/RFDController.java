package org.endeavor.game.content.quest.impl.rfd;

import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.content.quest.QuestConstants;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.controllers.GenericMinigameController;

public class RFDController extends GenericMinigameController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7226105593099559303L;

	@Override
	public void onDeath(Player p) {
		p.setController(ControllerManager.DEFAULT_CONTROLLER);
	}

	@Override
	public Location getRespawnLocation(Player player) {
		return new Location(3210, 9621);
	}

	@Override
	public void onDisconnect(Player p) {
		p.getQuesting().setQuestActive(QuestConstants.RECIPE_FOR_DISASTER, false);
		p.teleport(getRespawnLocation(p));
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
		return false;
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
		return "Recipe For Disaster";
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
