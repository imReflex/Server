package org.endeavor.game.content;

import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerConstants;
import org.endeavor.game.entity.player.controllers.Controller;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendSidebarInterface;

public class EasterRing {
	public static final int EASTER_RING_ID = 7927;
	public static final int UNMORPH_INTERFACE_ID = 6014;
	public static final Controller EASTER_RING_CONTROLLER = new EasterRingController();

	public static final int[] EGG_IDS = { 3689, 3690, 3691, 3692, 3693, 3694 };

	public static void init(Player player) {
		player.setNpcAppearanceId((short) EGG_IDS[org.endeavor.engine.utility.Misc.randomNumber(EGG_IDS.length)]);
		player.setController(EASTER_RING_CONTROLLER);
		player.getMovementHandler().reset();

		int[] tabs = player.getInterfaceManager().getTabs().clone();

		player.getAttributes().set("tabs", tabs);

		for (int i = 0; i < tabs.length; i++) {
			player.getClient().queueOutgoingPacket(new SendSidebarInterface(i, -1));
		}
		player.getClient().queueOutgoingPacket(new SendSidebarInterface(PlayerConstants.INVENTORY_TAB, 6014));
		player.getClient().queueOutgoingPacket(new SendMessage("You morph into an Easter egg."));
	}

	public static void cancel(Player player) {
		player.setController(ControllerManager.DEFAULT_CONTROLLER);
		player.setNpcAppearanceId((short) -1);
		player.setAppearanceUpdateRequired(true);

		int[] tabs = (int[]) player.getAttributes().get("tabs");

		for (int i = 0; i < tabs.length; i++) {
			player.getClient().queueOutgoingPacket(new SendSidebarInterface(i, tabs[i]));
		}

		player.getClient().queueOutgoingPacket(new SendMessage("You morph back into a human."));
	}

	public static final boolean canEquip(Player player) {
		if (!player.getController().equals(ControllerManager.DEFAULT_CONTROLLER)) {
			player.getClient().queueOutgoingPacket(new SendMessage("You cannot do this here."));
			return false;
		}

		return true;
	}

	public static class EasterRingController extends Controller {
		/**
		 * 
		 */
		private static final long serialVersionUID = -264530029362575370L;

		@Override
		public void tick(Player p) {
		}

		@Override
		public boolean canSave() {
			return false;
		}

		@Override
		public boolean canTalk() {
			return true;
		}

		@Override
		public boolean canMove(Player p) {
			return false;
		}

		@Override
		public boolean canClick() {
			return false;
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
			return PlayerConstants.HOME;
		}

		@Override
		public boolean canLogOut() {
			return false;
		}

		@Override
		public void onDisconnect(Player p) {
		}

		@Override
		public void onControllerInit(Player p) {
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
			return true;
		}

		@Override
		public String toString() {
			return "Easter ring";
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
}
