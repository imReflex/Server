package org.endeavor.game.entity.item;

public class BasicItemContainer extends ItemContainer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5927422338818805345L;

	public BasicItemContainer(int size) {
		super(size, ItemContainer.ContainerTypes.ALWAYS_STACK, false, false);
	}

	@Override
	public void update() {
	}

	@Override
	public void onFillContainer() {
	}

	@Override
	public void onMaxStack() {
	}

	@Override
	public boolean allowZero(int id) {
		return false;
	}

	@Override
	public void onAdd(Item item) {
	}

	@Override
	public void onRemove(Item item) {
	}
}
