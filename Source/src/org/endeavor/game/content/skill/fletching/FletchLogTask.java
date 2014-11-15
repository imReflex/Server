package org.endeavor.game.content.skill.fletching;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.impl.ProductionTask;
import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;
import org.endeavor.game.entity.player.net.out.impl.SendSound;

public class FletchLogTask extends ProductionTask {
	private int productionCount;
	private FletchLogData data;
	private int index;

	public FletchLogTask(Player entity, short productionCount, FletchLogData data, int index) {
		super(entity, 0, false, Task.StackType.NEVER_STACK, Task.BreakType.ON_MOVE, 0);
		this.productionCount = productionCount;
		this.data = data;
		this.index = index;
		entity.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
	}

	@Override
	public void execute() {
		if (player.getSkill().getLevels()[getSkill()] < getRequiredLevel()) {
			DialogueManager.sendStatement(player, new String[] { getInsufficentLevelMessage() });
			player.getUpdateFlags().sendAnimation(new Animation(-1));
			stop();
			return;
		}
		for (Item productionItem : getConsumedItems()) {
			if ((productionItem != null)
					&& (player.getInventory().getItemAmount(productionItem.getId()) < productionItem.getAmount())) {
				if (noIngredients(productionItem) != null)
					player.getClient().queueOutgoingPacket(new SendMessage(noIngredients(productionItem)));
				player.getUpdateFlags().sendAnimation(new Animation(-1));
				stop();
				return;
			}
		}

		if (!canProduce()) {
			stop();
			return;
		}
		if (!started) {
			started = true;
			if (getAnimation() != null) {
				player.getUpdateFlags().sendAnimation(getAnimation());
			}
			if (getGraphic() != null) {
				player.getUpdateFlags().sendGraphic(getGraphic());
			}

			productionCount = getProductionCount();
			cycleCount = getCycleCount();
			return;
		}

		if (cycleCount > 1) {
			if (cycleCount == 4) {
				player.getClient().queueOutgoingPacket(new SendSound(1631, 0, 0));
			}

			cycleCount -= 1;
		} else {
			if ((getAnimation() != null) && (getAnimation().getId() > 0)) {
				player.getUpdateFlags().sendAnimation(getAnimation());
			}
			if ((getGraphic() != null) && (getGraphic().getId() > 0)) {
				player.getUpdateFlags().sendGraphic(getGraphic());
			}

			cycleCount = getCycleCount();

			productionCount -= 1;

			for (Item item : getConsumedItems()) {
				player.getInventory().remove(item);
			}
			for (Item item : getRewards()) {
				player.getInventory().add(item);
			}
			player.getClient().queueOutgoingPacket(new SendMessage(getSuccessfulProductionMessage()));
			player.getSkill().addExperience(getSkill(), getExperience());

			if (productionCount < 1) {
				player.getUpdateFlags().sendAnimation(new Animation(-1));
				stop();
				return;
			}
			for (Item item : getConsumedItems()) {
				if (player.getInventory().getItemAmount(item.getId()) < item.getAmount()) {
					player.getUpdateFlags().sendAnimation(new Animation(-1));
					stop();
					return;
				}
			}

			player.getAchievements().incr(player, "Fletch 1,000 logs");
		}
	}

	@Override
	public boolean canProduce() {
		return true;
	}

	@Override
	public int getCycleCount() {
		return 2;
	}

	@Override
	public Graphic getGraphic() {
		return null;
	}

	@Override
	public Animation getAnimation() {
		return new Animation(1248);
	}

	@Override
	public double getExperience() {
		return data.getExperience()[index];
	}

	@Override
	public Item[] getConsumedItems() {
		return new Item[] { new Item(data.getLogId(), 1) };
	}

	@Override
	public String getInsufficentLevelMessage() {
		return "You need a " + org.endeavor.game.content.skill.SkillConstants.SKILL_NAMES[getSkill()] + " level of "
				+ getRequiredLevel() + " to fletch that log.";
	}

	@Override
	public int getProductionCount() {
		return productionCount;
	}

	@Override
	public int getRequiredLevel() {
		return data.getRequiredLevel()[index];
	}

	@Override
	public Item[] getRewards() {
		if ((data == FletchLogData.NORMAL) && (index == 0)) {
			return new Item[] { new Item(data.getRewards()[index], 15) };
		}

		return new Item[] { new Item(data.getRewards()[index], 1) };
	}

	@Override
	public int getSkill() {
		return 9;
	}

	@Override
	public String getSuccessfulProductionMessage() {
		if ((data == FletchLogData.NORMAL) && (index == 0)) {
			return "You fletch 15 Arrow shafts.";
		}

		String name = GameDefinitionLoader.getItemDef(data.getRewards()[index]).getName();
		return "You fletch " + Misc.getAOrAn(name) + " " + name + ".";
	}

	@Override
	public void onStop() {
	}

	@Override
	public String noIngredients(Item item) {
		return null;
	}
}
