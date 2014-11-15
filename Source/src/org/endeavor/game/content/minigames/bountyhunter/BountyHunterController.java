package org.endeavor.game.content.minigames.bountyhunter;

import org.endeavor.engine.TasksExecutor;
import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.Controller;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendPlayerOption;

public class BountyHunterController extends Controller {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8393865315234872684L;

	@Override
	public void tick(final Player player) {
		if(player.getTargetMatch() != null) {
			Player target = player.getTargetMatch().getTargetForPlayer(player);
			if(target == null || target.getTargetMatch() != player.getTargetMatch()) {
				player.setTargetMatch(null);
				BountyHunter.getSingleton().sentBountyInterface(player);
				BountyHunter.getSingleton().scheduleTargetSearch(player, false);
			}
		} else {
			BountyHunter.getSingleton().sentBountyInterface(player);
			BountyHunter.getSingleton().scheduleTargetSearch(player, false);
		}
	}

	@Override
	public boolean onClick(Player player, int buttonID) {
		return false;
	}

	@Override
	public boolean onObject(Player player, int objectID) {
		return false;
	}

	@Override
	public boolean onNpc(Player player, int npcID) {
		return false;
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
	public boolean canAttackPlayer(Player paramPlayer1, Player paramPlayer2) {
		return true;
	}

	@Override
	public boolean allowMultiSpells() {
		return false;
	}

	@Override
	public boolean allowPvPCombat() {
		return true;
	}

	@Override
	public void onDeath(Player player) {
		BountyHunter.getSingleton().onDeath(player);
		Player killer = player.getLastKiller();
		if(killer != null) {
			Player target = killer.getTargetMatch().getTargetForPlayer(killer);
			if(target == player) {
				killer.increaseHunterKills();
				killer.send(new SendMessage("You successfully killed your target. You now have " + killer.getHunterKills() + " target kills."));
				player.setTargetMatch(null);
				BountyHunter.getSingleton().scheduleTargetSearch(player, false);
			} else {
				BountyHunter.getSingleton().addPickupPenalty(player);
				killer.increaseRogueKills();
				killer.send(new SendMessage("You successfully killed a player. You now have " + killer.getRogueKills() + " rogue kills."));
			}
		} else {
			player.send(new SendMessage("Your killer could not be found."));
		}
		if(player.getPenaltyTimer() != null) {
			player.getPenaltyTimer().setEnterTime(30);
		} else {
			player.setPenaltyTimer(new PenatlyTimer(player, 0, 0, 30));
			TasksExecutor.fastExecutor.scheduleAtFixedRate(player.getPenaltyTimer(), 0, 1000);
		}
		player.send(new SendMessage("You may not enter Bounty Hunter again for 30 seconds."));
	}

	@Override
	public Location getRespawnLocation(Player player) {
		return new Location(3166, 3678);
	}

	@Override
	public boolean canLogOut() {
		return false;
	}

	@Override
	public void onDisconnect(Player player) {
		BountyHunter.getSingleton().forceExitCrater(player);
	}

	@Override
	public void onControllerInit(Player player) {
		player.send(new SendPlayerOption("Attack", 3));
		player.send(new SendPlayerOption("null", 4));
	}

	@Override
	public boolean canUseCombatType(Player paramPlayer,
			CombatTypes paramCombatTypes) {
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
		return null;
	}

}
