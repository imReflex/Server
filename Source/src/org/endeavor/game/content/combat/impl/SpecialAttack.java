package org.endeavor.game.content.combat.impl;

import java.io.Serializable;

import org.endeavor.engine.definitions.SpecialAttackDefinition;
import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.game.content.combat.special.SpecialAttackHandler;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendConfig;
import org.endeavor.game.entity.player.net.out.impl.SendSpecialBar;

public class SpecialAttack implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4459402701302628972L;
	private final Player player;
	private boolean initialized = false;
	private int button = -1;
	private int barId1 = 0;
	private int barId2 = 0;
	private int amount = 100;
	public static final int FULL_SPECIAL = 100;
	public static final int SPECIAL_TIMER_MAX = 50;

	public SpecialAttack(Player player) {
		this.player = player;
	}

	public void tick() {
		TaskQueue.queue(new Task(player, 50, false, Task.StackType.NEVER_STACK, Task.BreakType.NEVER, 3) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 5608541688970506031L;

			@Override
			public void execute() {
				if (amount < 100) {
					amount += 10;
					if (amount > 100) {
						amount = 100;
					}
					if (barId1 > 0) {
						SpecialAttackHandler.updateSpecialBarText(player, barId2, amount, initialized);
						SpecialAttackHandler.updateSpecialAmount(player, barId2, amount);
					}
				}
			}

			@Override
			public void onStop() {
			}
		});
	}

	public void update() {
		SpecialAttackHandler.updateSpecialBarText(player, barId2, amount, initialized);
		SpecialAttackHandler.updateSpecialAmount(player, barId2, amount);
	}

	public void onEquip() {
		Item weapon = player.getEquipment().getItems()[3];
		updateSpecialBar(weapon == null ? 0 : weapon.getId());
		if (initialized)
			toggleSpecial();
	}

	public void updateSpecialBar(int weaponId) {
		SpecialAttackDefinition def = Item.getSpecialDefinition(weaponId);

		player.getClient().queueOutgoingPacket(new SendConfig(78, 0));

		if (def != null) {
			barId1 = def.getBarId1();
			barId2 = def.getBarId2();
			button = def.getButton();

			if (weaponId == 15486)
				player.getClient().queueOutgoingPacket(new SendConfig(78, 1));
			else {
				player.getClient().queueOutgoingPacket(new SendSpecialBar(0, barId1));
			}

			update();
		} else {
			barId1 = 0;
			barId2 = 0;
			button = -1;
			player.getClient().queueOutgoingPacket(new SendSpecialBar(1, 7549));
			player.getClient().queueOutgoingPacket(new SendSpecialBar(1, 7574));
			player.getClient().queueOutgoingPacket(new SendSpecialBar(1, 12323));
			player.getClient().queueOutgoingPacket(new SendSpecialBar(1, 7599));
			player.getClient().queueOutgoingPacket(new SendSpecialBar(1, 7674));
			player.getClient().queueOutgoingPacket(new SendSpecialBar(1, 7474));
			player.getClient().queueOutgoingPacket(new SendSpecialBar(1, 7499));
			player.getClient().queueOutgoingPacket(new SendSpecialBar(1, 8493));
			player.getClient().queueOutgoingPacket(new SendSpecialBar(1, 7574));
			player.getClient().queueOutgoingPacket(new SendSpecialBar(1, 7624));
			player.getClient().queueOutgoingPacket(new SendSpecialBar(1, 7699));
			player.getClient().queueOutgoingPacket(new SendSpecialBar(1, 7800));
			player.getClient().queueOutgoingPacket(new SendSpecialBar(-1, 0));
		}
	}

	public boolean clickSpecialButton(int buttonId) {
		if (buttonId != button) {
			return false;
		}
		toggleSpecial();
		return true;
	}

	public void toggleSpecial() {
		Item weapon = player.getEquipment().getItems()[3];
		if (weapon == null) {
			initialized = false;
			return;
		}

		if ((weapon.getId() == 4153) && (!initialized) && (doInstantGraniteMaulSpecial())) {
			initialized = true;
			player.getCombat().attack();
		} else {
			initialized = (!initialized);

			if (weapon.getId() == 15241) {
				int a = player.getCombat().getAttackTimer();
				if (a > 0) {
					if (initialized)
						player.getCombat().setAttackTimer(a + 2);
					else {
						player.getCombat().setAttackTimer(a > 2 ? a - 2 : 1);
					}
				}
			}

			if (barId2 > -1)
				SpecialAttackHandler.updateSpecialBarText(player, barId2, amount, initialized);
		}
	}

	public void afterSpecial() {
		toggleSpecial();
		SpecialAttackHandler.updateSpecialAmount(player, barId2, amount);
		player.updateCombatType();
	}

	public void deduct(int amount) {
		this.amount -= amount;
	}

	public boolean doInstantGraniteMaulSpecial() {
		return (player.getCombat().getAttacking() != null)
				&& (player.getCombat().withinDistanceForAttack(player.getCombat().getCombatType(), false))
				&& (player.canAttack());
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public int getAmount() {
		return amount;
	}

	public void setSpecialAmount(int amount) {
		this.amount = amount;
	}
}
