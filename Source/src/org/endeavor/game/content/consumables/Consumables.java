package org.endeavor.game.content.consumables;

import java.io.Serializable;

import org.endeavor.engine.definitions.FoodDefinition;
import org.endeavor.engine.definitions.PotionDefinition;
import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.task.impl.AntifireTask;
import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.game.content.combat.Hit;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendSound;

public final class Consumables implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2936989876848214721L;
	private final Player player;
	public static final short EATING_ANIMATION = 829;
	public static final byte HITPOINTS = 3;
	public static final short EMPTY_VIAL_ID = 229;
	private boolean canEat = true;
	private boolean canDrink = true;

	public Consumables(Player player) {
		this.player = player;
	}

	public static boolean isPotion(Item i) {
		return i != null && GameDefinitionLoader.getPotionDefinition(i.getId()) != null;
	}

	public final boolean consume(int id, int slot, ConsumableType type) {
		Item consumable = player.getInventory().get(slot);

		if (consumable == null) {
			return false;
		}

		player.getAchievements().incr(player, "Use 1,000 consumables");
		player.getAchievements().incr(player, "Use 10,000 consumables");

		PotionDefinition potions = Item.getPotionDefinition(id);
		FoodDefinition food = Item.getFoodDefinition(id);
		switch (type) {
		case FOOD:
			SpecialConsumables.specialFood(player, consumable);

			if (food == null) {
				return false;
			}

			if ((!canEat) || (!player.getController().canEat(player))) {
				return true;
			}

			int foodHealth = food.getHeal();

			if (id == 15272) {
				foodHealth = (int) Math.round(player.getMaxLevels()[3] * 0.23D);
			}

			int heal = player.getSkill().getLevels()[3] + foodHealth;

			if (heal > player.getMaxLevels()[3]) {
				if (id != 15272)
					heal = player.getMaxLevels()[3];
				else {
					heal = player.getMaxLevels()[3] + 10;
				}
			}
			if ((food.getReplaceId() == -1) && (consumable.getAmount() <= 1)) {
				player.getInventory().clear(slot);
			} else if ((food.getReplaceId() == -1) && (consumable.getAmount() > 1)) {
				consumable.remove(1);
				player.getInventory().update();
			} else {
				player.getInventory().setId(slot, food.getReplaceId());
			}
			
			Hit hit = new Hit(foodHealth, Hit.HitTypes.EAT);
			if (!player.getUpdateFlags().isHitUpdate()) {
				player.getUpdateFlags().sendHit(hit.getDamage(), hit.getHitType(), hit.getCombatHitType());
			} else {
				player.getUpdateFlags().sendHit2(hit.getDamage(), hit.getHitType(), hit.getCombatHitType());
			}
			
			player.getClient().queueOutgoingPacket(new SendSound(317, 1, 2));
			player.getUpdateFlags().sendAnimation(829, 0);

			if (player.getSkill().getLevels()[3] < heal) {
				player.getSkill().setLevel(3, heal);
			}

			player.getClient().queueOutgoingPacket(new SendMessage(food.getMessage()));

			player.getCombat().reset();

			if (player.getCombat().getAttackTimer() > 0) {
				player.getCombat().increaseAttackTimer(food.getDelay());
			}

			canEat = false;

			TaskQueue.queue(new Task(player, food.getDelay(), false, Task.StackType.STACK, Task.BreakType.NEVER, 4) {
				/**
				 * 
				 */
				private static final long serialVersionUID = -598261393453838008L;

				@Override
				public void execute() {
					canEat = true;
					stop();
				}

				@Override
				public void onStop() {
				}
			});
			break;
		case POTION:
			if (potions == null) {
				return false;
			}

			if ((!player.getController().canDrink(player)) || (!canDrink)) {
				return true;
			}

			canDrink = false;
			PotionDefinition.SkillData[] skillData = potions.getSkillData();
			String name = potions.getName();
			player.getUpdateFlags().sendAnimation(829, 0);
			player.getClient().queueOutgoingPacket(new SendSound(334, 1, 0));
			String message = "You drink a dose of your " + name + ".";
			player.getClient().queueOutgoingPacket(new SendMessage(message));
			player.getInventory().setId(slot, potions.getReplaceId() == 0 ? 229 : potions.getReplaceId());
			player.getCombat().reset();

			useSpecialCasePotion(id);

			if ((skillData != null) && (skillData.length > 0)) {
				for (int i = 0; i < skillData.length; i++) {
					int skillId = skillData[i].getSkillId();
					int add = skillData[i].getAdd();
					double modifier = skillData[i].getModifier();

					int level = player.getSkill().getLevels()[skillId];
					int levelForExp = player.getMaxLevels()[skillId];

					if (modifier < 0.0D) {
						int affectedLevel = level + (int) (levelForExp * modifier) + add;

						if (affectedLevel < 1) {
							affectedLevel = 1;
						}

						player.getSkill().setLevel(skillId, affectedLevel);
					} else {
						int maxLvl = potions.getPotionType() == PotionDefinition.PotionTypes.RESTORE ? levelForExp
								: levelForExp + (int) (levelForExp * modifier) + add;
						int affectedLevel = level + (int) (levelForExp * modifier) + add;

						if ((skillId == 3) && (potions.getName().contains("Saradomin brew"))) {
							maxLvl = 111;
						}

						if ((skillId == 5) && (potions.getName().contains("Zamorak brew"))) {
							maxLvl = 99;
						}

						if (maxLvl > level) {
							if (affectedLevel > maxLvl) {
								affectedLevel = maxLvl;
							}
							player.getSkill().setLevel(skillId, affectedLevel);
						}
					}
				}
			}

			TaskQueue.queue(new Task(player, 3, false, Task.StackType.STACK, Task.BreakType.NEVER, 3) {
				@Override
				public void execute() {
					canDrink = true;
					stop();
				}

				@Override
				public void onStop() {
				}
			});
			break;
		default:
			System.out.print("[ERROR] - @org.endeavor.game.content.consumables");
		}

		return true;
	}

	public boolean useSpecialCasePotion(int id) {
		switch (id) {
		case 2452:
		case 2454:
		case 2456:
		case 2488:
			TaskQueue.queue(new AntifireTask(player, false));
			break;
		case 15304:
		case 15305:
		case 15306:
		case 15307:
			//player.doFireImmunity(60 * 6);
			TaskQueue.queue(new AntifireTask(player, true));
			break;
		
		case 3008:
		case 3010:
		case 3012:
		case 3014:
			player.getRunEnergy().add((int)(player.getRunEnergy().getEnergy() * 0.20));
			return true;
		case 3016:
		case 3018:
		case 3020:
		case 3022:
			player.getRunEnergy().add(20);
			return true;
		case 175:
		case 177:
		case 179:
		case 2446:
			player.curePoison(100);
			return true;
		}
		return false;
	}
}
