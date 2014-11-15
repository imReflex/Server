package org.endeavor.game.content.quest.impl.runemysteries;

import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.content.quest.QuestConstants;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerConstants;
import org.endeavor.game.entity.player.controllers.Controller;

public abstract class CaveController extends Controller {
	/**
	 * 
	 */
	private static final long serialVersionUID = 627433521654307488L;

	@Override
	public void tick(Player p) {
	}

	@Override
	public boolean canTalk() {
		return true;
	}

	@Override
	public abstract boolean canMove(Player paramPlayer);

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
	public boolean allowMultiSpells() {
		return false;
	}

	@Override
	public boolean allowPvPCombat() {
		return false;
	}

	@Override
	public void onDeath(Player p) {
	}

	@Override
	public Location getRespawnLocation(Player player) {
		return PlayerConstants.EDGEVILLE;
	}

	@Override
	public boolean canLogOut() {
		return true;
	}

	@Override
	public void onDisconnect(Player p) {
		Mob mob = (Mob) p.getAttributes().get("magecavenpc");

		if (mob != null) {
			mob.remove();
		}

		p.getAttributes().remove("magecavenpc");
	}

	@Override
	public void onControllerInit(Player p) {
		Mob mob = new Mob(p, 881, false, false, true, p.getLocation());

		mob.getFollowing().setIgnoreDistance(true);
		mob.getFollowing().setFollow(p);
		p.getAttributes().set("magecavenpc", mob);

		if (p.getQuesting().getQuestStage(QuestConstants.RUNE_MYSTERIES) == 4)
			p.start(new Traiborn(p));
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
		return false;
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
		return false;
	}

	@Override
	public boolean transitionOnWalk(Player p) {
		return false;
	}

	@Override
	public String toString() {
		return "RM cave";
	}
}
