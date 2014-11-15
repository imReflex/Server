package org.endeavor.game.content.minigames.armsrace;

import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.content.skill.SkillConstants;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.GenericMinigameController;
import org.endeavor.game.entity.player.net.out.impl.SendPlayerOption;

public class ARController extends GenericMinigameController {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8411014106009181531L;

	@Override
	public void tick(Player p) {
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
		return (p2.getController().equals(this)) && (ArmsRaceLobby.getGame() != null)
				&& (ArmsRaceLobby.getGame().hasStarted()) && (ArmsRaceLobby.getGame().isInGame(p))
				&& (ArmsRaceLobby.getGame().isInGame(p2));
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
	public void onDeath(Player p) {
		p.getSkill().setLevel(SkillConstants.HITPOINTS, 99);
		
		Entity k = p.getCombat().getDamageTracker().getKiller();

		p.getAttributes().remove("killstreakar");
		p.getAttributes().remove("extradamagepowerup");
		p.getAttributes().remove("attacktimerpowerup");

		if ((k != null) && (!k.isNpc()) && (ArmsRaceLobby.getGame() != null))
			ArmsRaceLobby.getGame().onKill(org.endeavor.game.entity.World.getPlayers()[k.getIndex()]);
	}

	@Override
	public Location getRespawnLocation(Player player) {
		return ArmsRaceLobby.getGame() == null ? ARConstants.FROM_WAITING : new Location(ARConstants.START.getX() + ARConstants.getXMod(), 
				ARConstants.START.getY() + ARConstants.getYMod());
	}

	@Override
	public void onDisconnect(Player p) {
		ArmsRaceLobby.getGame().remove(p);
	}

	@Override
	public void onControllerInit(Player p) {
		p.getClient().queueOutgoingPacket(new SendPlayerOption("Attack", 3));
		p.getClient().queueOutgoingPacket(new SendPlayerOption("null", 4));
	}

	@Override
	public boolean canUseCombatType(Player p, CombatTypes type) {
		return true;
	}

	@Override
	public boolean canEquip(Player p, int id, int slot) {
		return false;
	}

	@Override
	public boolean canUsePrayer(Player p) {
		return false;
	}

	@Override
	public boolean canEat(Player p) {
		return false;
	}

	@Override
	public boolean canDrink(Player p) {
		return false;
	}

	@Override
	public boolean canUseSpecialAttack(Player p) {
		return true;
	}

	@Override
	public String toString() {
		return "AR game";
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
