package org.endeavor.game.content.skill.fletching;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.impl.ProductionTask;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;
import org.endeavor.game.entity.player.net.out.impl.SendSound;

public class StringBowTask extends ProductionTask {
	private int productionCount;
	private StringBowData data;

	public StringBowTask(Player entity, short productionCount, StringBowData data) {
		super(entity, 0, false, Task.StackType.NEVER_STACK, Task.BreakType.ON_MOVE, 0);
		this.productionCount = productionCount;
		this.data = data;
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
			if (cycleCount == 2) {
				player.getClient().queueOutgoingPacket(new SendSound(1311, 0, 0));
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
		switch (data.getItem()) {
		case 48:
			return new Animation(6684);
		case 50:
			return new Animation(6678);
		case 54:
			return new Animation(6679);
		case 56:
			return new Animation(6685);
		case 58:
			return new Animation(6686);
		case 60:
			return new Animation(6680);
		case 62:
			return new Animation(6687);
		case 64:
			return new Animation(6681);
		case 66:
			return new Animation(6688);
		case 68:
			return new Animation(6682);
		case 70:
			return new Animation(6689);
		case 72:
			return new Animation(6683);
		case 49:
		case 51:
		case 52:
		case 53:
		case 55:
		case 57:
		case 59:
		case 61:
		case 63:
		case 65:
		case 67:
		case 69:
		case 71:
		}
		return new Animation(-1);
	}

	@Override
	public double getExperience() {
		return data.getExperience();
	}

	@Override
	public Item[] getConsumedItems() {
		return new Item[] { new Item(data.getItem(), 1), new Item(1777, 1) };
	}

	@Override
	public String getInsufficentLevelMessage() {
		return "You need a " + org.endeavor.game.content.skill.SkillConstants.SKILL_NAMES[getSkill()] + " level of "
				+ getRequiredLevel() + " to string this bow.";
	}

	@Override
	public int getProductionCount() {
		return productionCount;
	}

	@Override
	public int getRequiredLevel() {
		return data.getLevelRequired();
	}

	@Override
	public Item[] getRewards() {
		return new Item[] { new Item(data.getProduct(), 1) };
	}

	@Override
	public int getSkill() {
		return 9;
	}

	@Override
	public String getSuccessfulProductionMessage() {
		return "You attach a string to the bow.";
	}

	@Override
	public void onStop() {
	}

	@Override
	public String noIngredients(Item item) {
		return null;
	}
}
