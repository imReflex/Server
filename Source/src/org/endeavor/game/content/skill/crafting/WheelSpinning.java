package org.endeavor.game.content.skill.crafting;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.impl.ProductionTask;
import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;

public class WheelSpinning extends ProductionTask {
	private Spinnable spinnable;
	private short productionCount;

	public WheelSpinning(Player entity, short productionCount, Spinnable spin) {
		super(entity, 0, false, Task.StackType.NEVER_STACK, Task.BreakType.ON_MOVE, 0);
		this.productionCount = productionCount;
		spinnable = spin;
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
		return new Animation(896);
	}

	@Override
	public double getExperience() {
		return spinnable.getExperience();
	}

	@Override
	public Item[] getConsumedItems() {
		return new Item[] { spinnable.getItem() };
	}

	@Override
	public String getInsufficentLevelMessage() {
		return "You need a " + org.endeavor.game.content.skill.SkillConstants.SKILL_NAMES[getSkill()] + " level of "
				+ getRequiredLevel() + " to spin "
				+ GameDefinitionLoader.getItemDef(spinnable.getOutcome().getId()).getName().toLowerCase() + ".";
	}

	@Override
	public int getProductionCount() {
		return productionCount;
	}

	@Override
	public int getRequiredLevel() {
		return spinnable.getRequiredLevel();
	}

	@Override
	public Item[] getRewards() {
		return new Item[] { spinnable.getOutcome() };
	}

	@Override
	public int getSkill() {
		return 12;
	}

	@Override
	public String getSuccessfulProductionMessage() {
		return "You spin the " + GameDefinitionLoader.getItemDef(getConsumedItems()[0].getId()).getName().toLowerCase()
				+ " into a " + GameDefinitionLoader.getItemDef(getRewards()[0].getId()).getName().toLowerCase() + ".";
	}

	@Override
	public void onStop() {
	}

	@Override
	public String noIngredients(Item item) {
		return null;
	}
}
