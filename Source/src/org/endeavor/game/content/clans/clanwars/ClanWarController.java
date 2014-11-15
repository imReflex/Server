package org.endeavor.game.content.clans.clanwars;

import org.endeavor.game.content.clans.Clan;
import org.endeavor.game.content.clans.Clans;
import org.endeavor.game.content.clans.clanwars.ClanWarRules.CombatWarElements;
import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.Controller;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendPlayerOption;

/**
 * 
 * @author allen_000
 *
 */
public class ClanWarController extends Controller {

	private static final long serialVersionUID = 6549889613579625577L;
	private transient ClanWar war;
	private transient Clan clan;
	
	public ClanWarController(ClanWar war) {
		super();
		this.war = war;
	}
	
	@Override
	public void tick(Player player) {
		
	}

	@Override
	public boolean canTalk() {
		return true;
	}

	@Override
	public boolean canMove(Player player) {
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
		return war.getRules().isKeepItems();
	}

	@Override
	public boolean canAttackNPC() {
		return false;
	}

	@Override
	public boolean canAttackPlayer(Player pOne, Player pTwo) {
		if(!war.getTimer().started()) {
			pOne.send(new SendMessage("Please wait for the war to start!"));
			return false;
		}
		if(!war.getAccepterPlayers().contains(pOne) && !war.getRequesterPlayers().contains(pOne)) {
			pOne.send(new SendMessage("You are not currently able to attack."));
			return false;
		}
		if(!war.getRequesterPlayers().contains(pTwo) && !war.getAccepterPlayers().contains(pTwo)) {
			pOne.send(new SendMessage("You cannot attack this player!"));
			return false;
		}
		Clan one = Clans.getClanForPlayer(pOne);
		Clan two = Clans.getClanForPlayer(pTwo);
		if(one == two)
			return false;
		else
			return true;
	}

	@Override
	public boolean allowMultiSpells() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean allowPvPCombat() {
		return war.getTimer().started();
	}

	@Override
	public void onDeath(Player player) {
		war.onDeath(player);
	}

	@Override
	public Location getRespawnLocation(Player player) {
		Clan clan = Clans.getClanForPlayer(player);
		if(clan == war.getRequesterClan()) {
			return new Location(war.getRules().getArena().getRequesterViewingX(), war.getRules().getArena().getRequesterViewingY(), war.getHeight());
		} else if(clan == war.getAccepterClan()) {
			return new Location(war.getRules().getArena().getAccepterViewingX(), war.getRules().getArena().getAccepterViewingY(), war.getHeight());
		}
		return null;
	}

	@Override
	public boolean canLogOut() {
		return false;
	}

	@Override
	public void onDisconnect(Player player) {
		if(war == null) {
			player.setController(ControllerManager.DEFAULT_CONTROLLER);
			player.getClient().queueOutgoingPacket(new SendPlayerOption("Attack", 3));
			player.getClient().queueOutgoingPacket(new SendPlayerOption("null", 4));
			player.teleport(new Location(3271, 3687, 0));
			return;
		}
		this.war.removeMember(player);
	}

	@Override
	public void onControllerInit(Player player) {
		Clan toClan = Clans.getClanForPlayer(player);
		if(toClan != null)
			this.clan = toClan;
		if(war == null) {
			player.setController(ControllerManager.DEFAULT_CONTROLLER);
			player.getClient().queueOutgoingPacket(new SendPlayerOption("Attack", 3));
			player.getClient().queueOutgoingPacket(new SendPlayerOption("null", 4));
			player.teleport(new Location(3271, 3687, 0));
			return;
		}
		player.getClient().queueOutgoingPacket(new SendPlayerOption("Attack", 3));
		player.getClient().queueOutgoingPacket(new SendPlayerOption("null", 4));
		this.war.sendGameInterface(player);
	}

	@Override
	public boolean canUseCombatType(Player player, CombatTypes type) {
		if(type == CombatTypes.MELEE)
			return this.war.getRules().getCombatElements().contains(CombatWarElements.MELEE);
		if(type == CombatTypes.MAGIC)
			return this.war.getRules().getCombatElements().contains(CombatWarElements.MAGIC);
		if(type == CombatTypes.RANGED)
			return this.war.getRules().getCombatElements().contains(CombatWarElements.RANGED);
		return false;
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
		return this.war.getRules().getCombatElements().contains(CombatWarElements.PRAYER);
	}

	@Override
	public boolean canEat(Player paramPlayer) {
		return this.war.getRules().getCombatElements().contains(CombatWarElements.FOOD);
	}

	@Override
	public boolean canDrink(Player paramPlayer) {
		return this.war.getRules().getCombatElements().contains(CombatWarElements.POTIONS);
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

	@Override
	public boolean onClick(Player player, int buttonID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onObject(Player player, int objectID) {
		switch(objectID) {
		case 38697:
		case 28214:
			war.removeMember(player);
			break;
		}
		return false;
	}

	@Override
	public boolean onNpc(Player player, int npcID) {
		// TODO Auto-generated method stub
		return false;
	}

}
