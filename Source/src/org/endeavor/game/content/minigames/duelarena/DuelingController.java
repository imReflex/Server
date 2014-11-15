package org.endeavor.game.content.minigames.duelarena;

import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.Controller;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendPlayerOption;

public class DuelingController extends Controller {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8242129027795289011L;

	@Override
	public void tick(Player player) {
	}

	@Override
	public boolean canTalk() {
		return true;
	}

	@Override
	public boolean canSave() {
		return false;
	}

	@Override
	public boolean canMove(Player p) {
		if (p.getDueling().getRuleToggle() == null) {
			return true;
		}
		if (p.getDueling().getRuleToggle()[1]) {
			p.getClient().queueOutgoingPacket(new SendMessage("You cannot move during this duel."));
			return false;
		}
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
		if (p.getDueling().getInteracting() == null) {
			return false;
		}
		if (!p.getDueling().getInteracting().equals(p2)) {
			p.getClient().queueOutgoingPacket(new SendMessage("You are not dueling this player!"));
			return false;
		}
		if (!p.getDueling().canAttack()) {
			p.getClient().queueOutgoingPacket(new SendMessage("The duel hasn't started yet!"));
			return false;
		}

		return p.getDueling().canUseWeapon();
	}

	@Override
	public void onDeath(Player p) {
		p.getDueling().onDuelEnd(false, false);
	}

	@Override
	public Location getRespawnLocation(Player player) {
		Location p = DuelingConstants.RESPAWN_LOCATIONS[org.endeavor.engine.utility.Misc
				.randomNumber(DuelingConstants.RESPAWN_LOCATIONS.length)];
		int[] dir = org.endeavor.game.GameConstants.DIR[org.endeavor.engine.utility.Misc
				.randomNumber(org.endeavor.game.GameConstants.DIR.length)];
		return new Location(p.getX() + dir[0], p.getY() + dir[1]);
	}

	@Override
	public boolean canLogOut() {
		return false;
	}

	@Override
	public void onDisconnect(Player p) {
	}

	@Override
	public void onControllerInit(Player player) {
		player.getClient().queueOutgoingPacket(new SendPlayerOption("Attack", 3));
		player.getClient().queueOutgoingPacket(new SendPlayerOption("null", 4));
	}

	@Override
	public String toString() {
		return null;
	}

	@Override
	public boolean canUseCombatType(Player p, CombatTypes type) {
		if (p.getDueling().getRuleToggle() == null) {
			return true;
		}
		switch (type) {
		case MAGIC:
			if (p.getDueling().getRuleToggle()[4]) {
				p.getClient().queueOutgoingPacket(new SendMessage("You can't use Magic during this duel!"));
				return false;
			}
			break;
		case MELEE:
			if (p.getDueling().getRuleToggle()[3]) {
				p.getClient().queueOutgoingPacket(new SendMessage("You can't use Melee during this duel!"));
				return false;
			}
			break;
		case RANGED:
			if (p.getDueling().getRuleToggle()[2]) {
				p.getClient().queueOutgoingPacket(new SendMessage("You can't use Ranged during this duel!"));
				return false;
			}

			break;
		}

		return true;
	}

	@Override
	public boolean canTeleport() {
		return false;
	}

	@Override
	public boolean canTrade() {
		return false;
	}

	@Override
	public boolean canEquip(Player p, int id, int slot) {
		if (p.getDueling().getToRemove() == null) {
			return true;
		}
		if ((p.getDueling().getToRemove()[slot])
				|| ((slot == 3) && (p.getDueling().getToRemove()[5]) && (Item.getWeaponDefinition(id).isTwoHanded()))) {
			p.getClient().queueOutgoingPacket(new SendMessage("You can't wear this during the duel!"));
			return false;
		}
		return true;
	}

	@Override
	public boolean canUsePrayer(Player p) {
		if (p.getDueling().getRuleToggle() == null) {
			return true;
		}
		if (p.getDueling().getRuleToggle()[7]) {
			p.getClient().queueOutgoingPacket(new SendMessage("You cannot use prayer during this duel!"));
			return false;
		}
		return true;
	}

	@Override
	public boolean canEat(Player p) {
		if (p.getDueling().getRuleToggle() == null) {
			return true;
		}
		if (p.getDueling().getRuleToggle()[6]) {
			p.getClient().queueOutgoingPacket(new SendMessage("You cannot use food during this duel!"));
			return false;
		}
		return true;
	}

	@Override
	public boolean canDrink(Player p) {
		if (p.getDueling().getRuleToggle() == null) {
			return true;
		}
		if (p.getDueling().getRuleToggle()[5]) {
			p.getClient().queueOutgoingPacket(new SendMessage("You cannot use potions during this duel!"));
			return false;
		}
		return true;
	}

	@Override
	public boolean canUseSpecialAttack(Player p) {
		if (p.getDueling().getRuleToggle() == null) {
			return true;
		}
		if (p.getDueling().getRuleToggle()[10]) {
			p.getClient().queueOutgoingPacket(new SendMessage("You cannot use special attacks during this duel!"));
			return false;
		}
		return true;
	}

	@Override
	public boolean transitionOnWalk(Player p) {
		return false;
	}

	@Override
	public void onTeleport(Player p) {
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
