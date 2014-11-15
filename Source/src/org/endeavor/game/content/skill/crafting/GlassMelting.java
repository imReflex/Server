package org.endeavor.game.content.skill.crafting;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.impl.ProductionTask;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;

public class GlassMelting extends ProductionTask {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2882657847549955764L;
	private short productionCount;
	private Glass glass;

	public GlassMelting(Player entity, short productionCount, Glass glass) {
		super(entity, 0, false, Task.StackType.NEVER_STACK, Task.BreakType.ON_MOVE, 0);
		this.productionCount = productionCount;
		this.glass = glass;
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
		return new Animation(899);
	}

	@Override
	public double getExperience() {
		return glass.getExperience();
	}

	@Override
	public Item[] getConsumedItems() {
		return new Item[] { new Item(1783), new Item(glass.getMaterialId()) };
	}

	@Override
	public String getInsufficentLevelMessage() {
		return "You need a " + org.endeavor.game.content.skill.SkillConstants.SKILL_NAMES[getSkill()] + " level of "
				+ getRequiredLevel() + " to melt glass.";
	}

	@Override
	public int getProductionCount() {
		return productionCount;
	}

	@Override
	public int getRequiredLevel() {
		return glass.getRequiredLevel();
	}

	@Override
	public Item[] getRewards() {
		return new Item[] { new Item(glass.getRewardId()) };
	}

	@Override
	public int getSkill() {
		return 12;
	}

	@Override
	public String getSuccessfulProductionMessage() {
		return "you heat the sand and soda ash in the furnace to make glass.";
	}

	@Override
	public void onStop() {
	}

	@Override
	public String noIngredients(Item item) {
		return null;
	}
}
