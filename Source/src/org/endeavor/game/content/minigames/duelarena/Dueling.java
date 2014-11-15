package org.endeavor.game.content.minigames.duelarena;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.ItemCheck;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.out.impl.SendAltConfig;
import org.endeavor.game.entity.player.net.out.impl.SendDuelEquipment;
import org.endeavor.game.entity.player.net.out.impl.SendInterface;
import org.endeavor.game.entity.player.net.out.impl.SendInventoryInterface;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendPlayerHint;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;
import org.endeavor.game.entity.player.net.out.impl.SendString;
import org.endeavor.game.entity.player.net.out.impl.SendUpdateItems;

public class Dueling implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8680830180142865744L;
	private final Player p;
	private Player lastRequest = null;

	private Player interacting = null;
	private int rules;
	private boolean[] ruleToggle = null;

	private boolean[] toRemove = null;
	private StakingContainer container;
	private DuelingStatuses s = DuelingStatuses.WAITING;

	private int arenaId = -1;

	private boolean attack = false;

	public Dueling(Player p) {
		this.p = p;
		container = new StakingContainer(p);
	}

	public void request(Player r) {
		p.getClient().queueOutgoingPacket(new SendMessage("Sending duel offer.."));

		if ((r.isBusy()) || (p.isBusy())) {
			p.getClient().queueOutgoingPacket(new SendMessage("The other player is busy at the moment."));
			return;
		}

		if (r.getDueling().requested(p)) {
			beginStake(p, r);
		} else if (!r.getPrivateMessaging().ignored(p.getUsername())) {
			r.getClient().queueOutgoingPacket(new SendMessage(p.getUsername() + ":duelreq:"));
			lastRequest = r;
		}
	}

	public static void beginStake(Player p1, Player p2) {
		p1.setController(ControllerManager.DUEL_STAKE_CONTROLLER);
		p2.setController(ControllerManager.DUEL_STAKE_CONTROLLER);

		Dueling d1 = p1.getDueling();
		Dueling d2 = p2.getDueling();

		d1.setDuelingWith(p2);
		d2.setDuelingWith(p1);
		d1.resetForStake();
		d2.resetForStake();
		d1.setStatus(DuelingStatuses.SCREEN_1);
		d2.setStatus(DuelingStatuses.SCREEN_1);

		p1.getClient().queueOutgoingPacket(new SendString("No Armour/2h Weps", 669));
		p1.getClient().queueOutgoingPacket(new SendString("Whip + DD only", 6696));

		p2.getClient().queueOutgoingPacket(new SendString("No Armour/2h Weps", 669));
		p2.getClient().queueOutgoingPacket(new SendString("Whip + DD only", 6696));

		sendDuelEquipment(p1);
		sendDuelEquipment(p2);

		p1.getClient().queueOutgoingPacket(new SendAltConfig(286, 0));
		p2.getClient().queueOutgoingPacket(new SendAltConfig(286, 0));

		p1.getClient().queueOutgoingPacket(new SendString("Dueling with: " + p2.getUsername(), 6671));
		p2.getClient().queueOutgoingPacket(new SendString("Dueling with: " + p1.getUsername(), 6671));

		p1.getClient().queueOutgoingPacket(new SendString("", 6684));
		p2.getClient().queueOutgoingPacket(new SendString("", 6684));

		p1.getClient().queueOutgoingPacket(new SendInventoryInterface(6575, 3321));
		p2.getClient().queueOutgoingPacket(new SendInventoryInterface(6575, 3321));

		d1.getContainer().update();
		d2.getContainer().update();
	}

	public static void sendScreen2(Player p1, Player p2) {
		p1.getDueling().setStatus(DuelingStatuses.SCREEN_2);
		p2.getDueling().setStatus(DuelingStatuses.SCREEN_2);

		String[] before = DuelingConstants.getBeforeDuelStringUpdates(p1.getDueling().getRuleToggle());
		String[] during = DuelingConstants.getDuringDuelStringUpdates(p1.getDueling().getRuleToggle());

		String stake1 = DuelingConstants.getStakedItemsToString(p1.getDueling().getContainer().getItems(), p1
				.getDueling().getContainer().getSize()
				- p1.getDueling().getContainer().getFreeSlots());
		String stake2 = DuelingConstants.getStakedItemsToString(p2.getDueling().getContainer().getItems(), p2
				.getDueling().getContainer().getSize()
				- p2.getDueling().getContainer().getFreeSlots());

		for (int i = 0; i < DuelingConstants.BEFORE_THE_DUEL_STRING_IDS.length; i++) {
			String s = before[i] == null ? "" : before[i];
			p1.getClient().queueOutgoingPacket(new SendString(s, DuelingConstants.BEFORE_THE_DUEL_STRING_IDS[i]));
			p2.getClient().queueOutgoingPacket(new SendString(s, DuelingConstants.BEFORE_THE_DUEL_STRING_IDS[i]));
		}

		for (int i = 0; i < DuelingConstants.DURING_THE_DUEL_STRING_IDS.length; i++) {
			String s = during[i] == null ? "" : during[i];
			p1.getClient().queueOutgoingPacket(new SendString(s, DuelingConstants.DURING_THE_DUEL_STRING_IDS[i]));
			p2.getClient().queueOutgoingPacket(new SendString(s, DuelingConstants.DURING_THE_DUEL_STRING_IDS[i]));
		}

		p1.getClient().queueOutgoingPacket(new SendString(stake1, 6516));
		p1.getClient().queueOutgoingPacket(new SendString(stake2, 6517));

		p2.getClient().queueOutgoingPacket(new SendString(stake2, 6516));
		p2.getClient().queueOutgoingPacket(new SendString(stake1, 6517));

		p1.getClient().queueOutgoingPacket(new SendString("", 6571));
		p2.getClient().queueOutgoingPacket(new SendString("", 6571));

		p1.getClient().queueOutgoingPacket(new SendInventoryInterface(6412, 3321));
		p2.getClient().queueOutgoingPacket(new SendInventoryInterface(6412, 3321));
	}

	public static void beginDuel(final Player p1, final Player p2) {
		boolean obstacles = p1.getDueling().getRuleToggle()[8];
		boolean noMovement = p1.getDueling().getRuleToggle()[1];
		int arena = DuelingManager.getDuelArenaId(obstacles);
		p1.getDueling().setArenaId(arena);
		p2.getDueling().setArenaId(arena);

		p1.setController(ControllerManager.DUELING_CONTROLLER);
		p2.setController(ControllerManager.DUELING_CONTROLLER);

		p1.getDueling().setStatus(DuelingStatuses.DUELING);
		p2.getDueling().setStatus(DuelingStatuses.DUELING);

		p1.getDueling().removeEquipmentForDuel();
		p2.getDueling().removeEquipmentForDuel();

		p1.resetCombatStats();
		p2.resetCombatStats();

		p1.getPrayer().disable();
		p2.getPrayer().disable();

		p1.getSpecialAttack().setSpecialAmount(100);
		p2.getSpecialAttack().setSpecialAmount(100);

		p1.getSpecialAttack().update();
		p2.getSpecialAttack().update();

		p1.getRunEnergy().restoreAll();
		p2.getRunEnergy().restoreAll();

		p1.getMagic().setVengeanceActive(false);
		p2.getMagic().setVengeanceActive(false);

		if (p1.getSpecialAttack().isInitialized()) {
			p1.getSpecialAttack().toggleSpecial();
		}

		if (p2.getSpecialAttack().isInitialized()) {
			p2.getSpecialAttack().toggleSpecial();
		}

		if (noMovement) {
			Location p = DuelingManager.getCoordinates(arena, obstacles, false, true);
			p1.teleport(p);
			p.move(1, 0);
			p2.teleport(p);
		} else {
			p1.teleport(DuelingManager.getCoordinates(arena, obstacles, false, false));
			p2.teleport(DuelingManager.getCoordinates(arena, obstacles, true, false));
		}

		p1.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
		p2.getClient().queueOutgoingPacket(new SendRemoveInterfaces());

		p1.getClient().queueOutgoingPacket(new SendPlayerHint(true, p2.getIndex()));
		p2.getClient().queueOutgoingPacket(new SendPlayerHint(true, p1.getIndex()));

		TaskQueue.queue(new Task(2) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1372015630396881057L;
			int time = 5;

			@Override
			public void execute() {
				if ((--time > 0) && (time <= 3)) {
					p1.getUpdateFlags().sendForceMessage("" + time);
					p2.getUpdateFlags().sendForceMessage("" + time);
				} else if (time == 0) {
					p1.getUpdateFlags().sendForceMessage("FIGHT!");
					p2.getUpdateFlags().sendForceMessage("FIGHT!");
					p1.getDueling().setCanAttack(true);
					p2.getDueling().setCanAttack(true);
					stop();
				}
			}

			@Override
			public void onStop() {
			}
		});
	}

	public void removeEquipmentForDuel() {
		Item wep = p.getEquipment().getItems()[3];
		for (int i = 0; i < toRemove.length; i++)
			if (toRemove[i]) {
				if ((i == 5) && toRemove[3] && (wep != null) && (wep.getWeaponDefinition().isTwoHanded())) {
					p.getEquipment().unequip(3);
				}

				if (p.getEquipment().getItems()[i] != null)
					p.getEquipment().unequip(i);
			}
	}

	public void accept() {
		switch (s) {
		case SCREEN_1:
			if ((canAccept()) && (interacting != null)) {
				if (interacting.getDueling().hasAccepted()) {
					sendScreen2(p, interacting);
				} else {
					s = DuelingStatuses.SCREEN_1_ACCEPTED;
					p.getClient().queueOutgoingPacket(new SendString("Waiting for other player..", 6684));
					interacting.getClient().queueOutgoingPacket(new SendString("Other player has accepted.", 6684));
				}
			}

			break;
		case SCREEN_2:
			if (interacting != null) {
				if (interacting.getDueling().hasAccepted()) {
					beginDuel(p, interacting);
				} else {
					s = DuelingStatuses.SCREEN_2_ACCEPTED;
					p.getClient().queueOutgoingPacket(new SendString("Waiting for other player..", 6571));
					interacting.getClient().queueOutgoingPacket(new SendString("Other player has accepted.", 6571));
				}
			}
			break;
		default:
			break;
		}
	}

	public void onForceLogout() {
		onDuelEnd(false, true);
	}

	public void onDuelEnd(boolean forfeit, boolean forceTie) {
		p.getClient().queueOutgoingPacket(new SendPlayerHint(true, -1));

		if (interacting != null) {
			interacting.getClient().queueOutgoingPacket(new SendPlayerHint(true, -1));
		}

		if (interacting != null && interacting.isDead() || interacting != null && forceTie) {
			interacting.teleport(interacting.getController().getRespawnLocation(interacting));

			onDecline(p, interacting);

			p.getClient().queueOutgoingPacket(new SendMessage("The duel was a tie."));
			if (interacting != null) {
				interacting.getClient().queueOutgoingPacket(new SendMessage("The duel was a tie."));
			}
			if (!p.isDead()) {
				p.teleport(p.getController().getRespawnLocation(p));
			}
		} else if (interacting != null) {
			interacting.teleport(interacting.getController().getRespawnLocation(interacting));
			interacting.getDueling().onVictory();

			interacting.getSkill().restore();

			if (interacting.isPoisoned())
				interacting.curePoison(0);
		} else {
			decline();
		}

		if (forfeit) {
			p.teleport(p.getController().getRespawnLocation(p));
			if (p.isPoisoned()) {
				p.curePoison(0);
				p.getClient().queueOutgoingPacket(new SendMessage(":curepoison:"));
			}
			p.getPrayer().disable();
			p.resetCombatStats();
		}

		if (ruleToggle != null) {
			DuelingManager.onFinishDuel(arenaId, ruleToggle[8]);
		}

		if (interacting != null) {
			interacting.getPrayer().disable();
			interacting.resetCombatStats();
		}

		p.setController(ControllerManager.DUEL_ARENA_CONTROLLER);

		if (interacting != null) {
			interacting.setController(ControllerManager.DUEL_ARENA_CONTROLLER);
			interacting.getDueling().reset();
		}

		p.getDueling().reset();
	}

	public void onVictory() {
		p.getAchievements().incr(p, "Win a duel");
		p.getAchievements().incr(p, "Win 100 Duels");

		Item[] staked = null;
		Item[] won = null;

		if ((interacting != null) && (interacting.getDueling().getContainer().getItems() != null)) {
			won = interacting.getDueling().getContainer().getItems().clone();
		}

		if (container.getItems() != null) {
			staked = container.getItems().clone();
		}

		container.clear();
		interacting.getDueling().getContainer().clear();

		if (staked != null) {
			for (Item i : staked) {
				if (i != null) {
					p.getInventory().addOrCreateGroundItem(i.getId(), i.getAmount(), false);
				}
			}
		}

		if (won != null) {
			for (Item i : won) {
				if (i != null) {
					p.getInventory().addOrCreateGroundItem(i.getId(), i.getAmount(), false);
				}
			}
		}

		p.getInventory().update();

		if (won != null)
			p.getClient().queueOutgoingPacket(new SendUpdateItems(6822, won));
		else {
			p.getClient().queueOutgoingPacket(new SendUpdateItems(6822, new Item[0]));
		}
		p.getClient().queueOutgoingPacket(new SendString("" + interacting.getSkill().getCombatLevel(), 6839));
		p.getClient().queueOutgoingPacket(new SendString(interacting.getUsername(), 6840));
		p.getClient().queueOutgoingPacket(new SendInterface(6733));

		p.getInventory().update();
	}

	public void decline() {
		onDecline(p, interacting);

		p.getClient().queueOutgoingPacket(new SendMessage("You decline the duel."));

		if (interacting != null) {
			interacting.getClient().queueOutgoingPacket(new SendMessage("The other player declined the duel."));

			interacting.getClient().queueOutgoingPacket(new SendRemoveInterfaces());

			interacting.getDueling().reset();
		}

		reset();
	}

	public static void onDecline(Player p1, Player p2) {
		if (p1 != null) {
			for (Item i : p1.getDueling().getContainer().getItems()) {
				if (i != null) {
					p1.getInventory().add(i);
				}
			}

			p1.getDueling().getContainer().clear();
			p1.getInventory().update();
		}

		if (p2 != null) {
			for (Item i : p2.getDueling().getContainer().getItems()) {
				if (i != null) {
					p2.getInventory().add(i);
				}
			}

			p2.getDueling().getContainer().clear();
			p2.getInventory().update();
		}

		if (p1 != null) {
			p1.setController(ControllerManager.DUEL_ARENA_CONTROLLER);
		}

		if (p2 != null)
			p2.setController(ControllerManager.DUEL_ARENA_CONTROLLER);
	}

	public void toggleRule(int id) {
		if (((s != DuelingStatuses.SCREEN_1) && (s != DuelingStatuses.SCREEN_1_ACCEPTED)) || (interacting == null)) {
			return;
		}

		toggle(id);
		interacting.getDueling().toggle(id);

		p.getClient().queueOutgoingPacket(new SendAltConfig(286, rules));
		interacting.getClient().queueOutgoingPacket(new SendAltConfig(286, interacting.getDueling().getRules()));

		p.getDueling().setStatus(DuelingStatuses.SCREEN_1);
		interacting.getDueling().setStatus(DuelingStatuses.SCREEN_1);

		p.getClient().queueOutgoingPacket(new SendString("", 6684));
		interacting.getClient().queueOutgoingPacket(new SendString("", 6684));
	}

	public void toggle(int id) {
		ruleToggle[id] = (ruleToggle[id] ? false : true);
		rules += (ruleToggle[id] ? DuelingConstants.DUEL_RULE_IDS[id] : -DuelingConstants.DUEL_RULE_IDS[id]);

		if (id == 9) {
			for (int i = 11; i <= 20; i++) {
				if (i != 14) {
					DuelingConstants.updateToRemove(this, i, ruleToggle[9]);
				}
			}
		}
		if ((id == 0) && (ruleToggle[id])) {
			if (!ruleToggle[4]) {
				toggleRule(4);
			}

			if (!ruleToggle[2]) {
				toggleRule(2);
			}

		}

		if ((id == 4) && (!ruleToggle[4]) && (ruleToggle[0])) {
			toggleRule(0);
		}

		if ((id == 2) && (!ruleToggle[2]) && (ruleToggle[0]))
			toggleRule(0);
	}

	public void onStake() {
		s = DuelingStatuses.SCREEN_1;
		interacting.getDueling().setStatus(DuelingStatuses.SCREEN_1);

		p.getClient().queueOutgoingPacket(new SendString("", 6684));
		interacting.getClient().queueOutgoingPacket(new SendString("", 6684));
	}

	public boolean canAccept() {
		if (interacting == null) {
			return false;
		}
		
		if (p.getSummoning().hasFamiliar() || p.getPets().hasPet()) {
			p.getClient().queueOutgoingPacket(new SendMessage("You must dismiss your familiar to duel."));
			return false;
		}

		if ((ruleToggle[3]) && (ruleToggle[2]) && (ruleToggle[4])) {
			p.getClient().queueOutgoingPacket(new SendMessage("You must enable one combat type to duel!"));
			return false;
		}

		if ((ruleToggle[1]) && (ruleToggle[8])) {
			p.getClient().queueOutgoingPacket(
					new SendMessage("You can only select either obstacles or no movement, not both."));
			return false;
		}

		if (!p.getSkill().locked())
			p.getSkill().lock(1);
		else {
			return false;
		}

		if (!p.getInventory().hasSpaceFor(buildItemArray(p, interacting, toRemove))) {
			p.getClient().queueOutgoingPacket(
					new SendMessage("You do not have enough inventory space to accept this duel."));
			return false;
		}

		return true;
	}

	public static Item[] buildItemArray(Player p1, Player p2, boolean[] toRemove) {
		List<Item> items = new ArrayList<Item>();

		Item[] equip = p1.getEquipment().getItems();
		Item[] stake = p1.getDueling().getContainer().getItems();
		Item[] oStake = p2.getDueling().getContainer().getItems();

		for (int i = 0; i < toRemove.length; i++) {
			if ((toRemove[i]) && (equip[i] != null)) {
				items.add(equip[i]);
			}

		}

		for (int i = 0; i < stake.length; i++) {
			if ((stake[i] == null) && (oStake[i] == null)) {
				break;
			}
			if (stake[i] != null) {
				items.add(stake[i]);
			}

			if (oStake[i] != null) {
				items.add(oStake[i]);
			}
		}

		Item[] ret = new Item[items.size()];

		for (int i = 0; i < ret.length; i++) {
			ret[i] = ((Item) items.get(i));
		}

		return ret;
	}

	public static void sendDuelEquipment(Player p) {
		Item[] equ = p.getEquipment().getItems();
		for (int i = 0; i < equ.length; i++)
			if (equ[i] == null)
				p.getClient().queueOutgoingPacket(new SendDuelEquipment(0, 0, i));
			else
				p.getClient().queueOutgoingPacket(new SendDuelEquipment(equ[i], i));
	}

	public void appendToRemove(int slot, boolean remove) {
		toRemove[slot] = remove;
	}

	public void reset() {
		rules = 0;
		ruleToggle = null;
		s = DuelingStatuses.WAITING;
		lastRequest = null;
		interacting = null;
		container.clear();
		toRemove = null;
		arenaId = -1;
		attack = false;
	}

	public void resetForStake() {
		rules = 0;
		ruleToggle = new boolean[DuelingConstants.DUEL_RULE_IDS.length];
		toRemove = new boolean[14];
		container.clear();
		lastRequest = null;
		arenaId = -1;
		attack = false;
	}

	public boolean canUseWeapon() {
		if (ruleToggle[0]) {
			Item wep = p.getEquipment().getItems()[3];

			if (wep == null) {
				return true;
			}

			if ((wep.getId() != 4151) && (!ItemCheck.isItemDyedWhip(wep)) && (wep.getId() != 5698)
					&& (wep.getId() != 1215)) {
				p.getClient().queueOutgoingPacket(
						new SendMessage("You can only use Abyssal whips and Dragon daggers during this duel."));
				return false;
			}

		}

		return true;
	}

	public boolean clickForfeitTrapDoor(int id) {
		if (id == 3203) {
			if (ruleToggle == null) {
				return true;
			}

			if (ruleToggle[0]) {
				p.getClient().queueOutgoingPacket(new SendMessage("You are not allowed to forfeit this duel."));
				return true;
			}
			p.start(new DuelArenaForfeit(p));
		}

		return false;
	}

	public boolean hasAccepted() {
		return (s == DuelingStatuses.SCREEN_1_ACCEPTED) || (s == DuelingStatuses.SCREEN_2_ACCEPTED);
	}

	public boolean canAppendStake() {
		return (s == DuelingStatuses.SCREEN_1) || (s == DuelingStatuses.SCREEN_1_ACCEPTED);
	}

	public boolean isDueling() {
		return s == DuelingStatuses.DUELING;
	}

	public boolean isStaking() {
		return (s != DuelingStatuses.WAITING) && (s != DuelingStatuses.DUELING);
	}

	public boolean requested(Player p) {
		return (lastRequest != null) && (lastRequest.equals(lastRequest));
	}

	public void setStatus(DuelingStatuses s) {
		this.s = s;
	}

	public void setDuelingWith(Player dueling) {
		interacting = dueling;
	}

	public StakingContainer getContainer() {
		return container;
	}

	public boolean[] getRuleToggle() {
		return ruleToggle;
	}

	public int getRules() {
		return rules;
	}

	public Player getInteracting() {
		return interacting;
	}

	public boolean canAttack() {
		return attack;
	}

	public void setCanAttack(boolean attack) {
		this.attack = attack;
	}

	public void setArenaId(int arenaId) {
		this.arenaId = arenaId;
	}

	public boolean[] getToRemove() {
		return toRemove;
	}

	public static enum DuelingStatuses {
		WAITING,

		SCREEN_1,

		SCREEN_1_ACCEPTED,

		SCREEN_2,

		SCREEN_2_ACCEPTED,

		DUELING;
	}
}
