package org.endeavor.game.content.minigames.pestcontrol;

import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.GenericMinigameController;

public class PestControlController extends GenericMinigameController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4963508647667904114L;

	@Override
	public boolean canLogOut() {
		return true;
	}
	
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
		return false;
	}

	@Override
	public boolean allowPvPCombat() {
		return false;
	}

	@Override
	public void onDeath(Player paramPlayer) {
	}

	@Override
	public Location getRespawnLocation(Player p) {
		if (p.getAttributes().get(PestControlGame.PEST_GAME_KEY) != null) {
			if (((PestControlGame) p.getAttributes().get(PestControlGame.PEST_GAME_KEY)).hasEnded()) {
				return new Location(2657, 2639);
			}
		}
		
		return PestControlConstants.getRandomBoatLocation(p.getZ());
	}

	@Override
	public void onDisconnect(Player p) {
		p.teleport(new Location(2657, 2639));
		((PestControlGame) p.getAttributes().get(PestControlGame.PEST_GAME_KEY)).remove(p);
	}

	@Override
	public void onControllerInit(Player paramPlayer) {
	}

	@Override
	public boolean canUseCombatType(Player paramPlayer, CombatTypes paramCombatTypes) {
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
		return "Pest Control";
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
