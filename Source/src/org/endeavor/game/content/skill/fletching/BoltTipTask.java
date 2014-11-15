package org.endeavor.game.content.skill.fletching;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.impl.ProductionTask;
import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;
import org.endeavor.game.entity.player.net.out.impl.SendSound;

public class BoltTipTask extends ProductionTask {
	private int productionCount;
	private BoltTipData gem;

	public BoltTipTask(Player entity, short productionCount, BoltTipData gem) {
		super(entity, 0, false, Task.StackType.NEVER_STACK, Task.BreakType.ON_MOVE, 0);
		this.productionCount = productionCount;
		this.gem = gem;

		player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
	}

	@Override
	public void execute() {
		player.getClient().queueOutgoingPacket(new SendSound(464, 0, 0));
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
			for (Item item : getConsumedItems())
				if (player.getInventory().getItemAmount(item.getId()) < item.getAmount()) {
					player.getUpdateFlags().sendAnimation(new Animation(-1));
					stop();
					return;
				}
		}
	}

	@Override
	public boolean canProduce() {
		return true;
	}

	@Override
	public int getCycleCount() {
		return 4;
	}

	@Override
	public Graphic getGraphic() {
		return null;
	}

	@Override
	public Animation getAnimation() {
		return new Animation(BoltTipData.getAnimForData(gem));
	}

	@Override
	public double getExperience() {
		return gem.exp;
	}

	@Override
	public Item[] getConsumedItems() {
		return new Item[] { new Item(gem.req[0], 1) };
	}

	@Override
	public String getInsufficentLevelMessage() {
		return "You need a " + org.endeavor.game.content.skill.SkillConstants.SKILL_NAMES[getSkill()] + " level of "
				+ getRequiredLevel() + " to cut that gem into bolt tips.";
	}

	@Override
	public int getProductionCount() {
		return productionCount;
	}

	@Override
	public int getRequiredLevel() {
		if (gem == null) {
			return 1;
		}

		return gem.level;
	}

	@Override
	public Item[] getRewards() {
		return new Item[] { new Item(gem.reward) };
	}

	@Override
	public int getSkill() {
		return 9;
	}

	@Override
	public String getSuccessfulProductionMessage() {
		return "You cut the " + GameDefinitionLoader.getItemDef(gem.req[0]).getName().toLowerCase()
				+ " into bolt tips.";
	}

	@Override
	public void onStop() {
	}

	@Override
	public String noIngredients(Item item) {
		return null;
	}
}
