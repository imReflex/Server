package org.endeavor.game.content.skill.crafting;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.impl.ProductionTask;
import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;

public class ArmourCreation extends ProductionTask {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3487888641085551917L;
	short creationAmount;
	Craftable craftable;

	public ArmourCreation(Player entity, short creationAmount, Craftable craft) {
		super(entity, 0, false, Task.StackType.NEVER_STACK, Task.BreakType.ON_MOVE, 0);
		this.creationAmount = creationAmount;
		craftable = craft;
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
		return new Animation(1249);
	}

	@Override
	public double getExperience() {
		return craftable.getExperience() * (getConsumedItems()[0].getId() == 1743 ? 2 : 1);
	}

	@Override
	public Item[] getConsumedItems() {
		if (craftable.getItemId() == 1741) {
			if (player.getInventory().hasItemId(1743)) {
				return new Item[] { new Item(1734, 1), new Item(1743, 1) };
			}
			return new Item[] { new Item(1734, 1), new Item(craftable.getItemId(), 1) };
		}

		return new Item[] { new Item(1734, 1), new Item(craftable.getItemId(), 1) };
	}

	@Override
	public String getInsufficentLevelMessage() {
		return "You need a " + org.endeavor.game.content.skill.SkillConstants.SKILL_NAMES[getSkill()] + " level of "
				+ getRequiredLevel() + " to create "
				+ GameDefinitionLoader.getItemDef(getRewards()[0].getId()).getName() + ".";
	}

	@Override
	public int getProductionCount() {
		return creationAmount;
	}

	@Override
	public int getRequiredLevel() {
		return craftable.getRequiredLevel();
	}

	@Override
	public Item[] getRewards() {
		return new Item[] { new Item(craftable.getOutcome(), 1) };
	}

	@Override
	public int getSkill() {
		return 12;
	}

	@Override
	public String getSuccessfulProductionMessage() {
		String prefix = "a";
		String itemName = GameDefinitionLoader.getItemDef(getRewards()[0].getId()).getName().toLowerCase();
		if ((itemName.contains("glove")) || (itemName.contains("boot")) || (itemName.contains("vamb"))
				|| (itemName.contains("chap")))
			prefix = "a pair of";
		else if (itemName.endsWith("s"))
			prefix = "some";
		else if (Misc.startsWithVowel(itemName)) {
			prefix = "an";
		}
		return "You make " + prefix + " " + itemName + ".";
	}

	@Override
	public void onStop() {
	}

	@Override
	public String noIngredients(Item item) {
		return "You don't have enough " + GameDefinitionLoader.getItemDef(item.getId()).getName()
				+ " to craft this item.";
	}
}
