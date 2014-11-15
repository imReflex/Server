package org.endeavor.engine.definitions;

import org.endeavor.game.entity.item.Item;

public class ShopDefinition {

	private short id;
	private String name;
	private boolean general;
	private Item[] items;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean isGeneral() {
		return general;
	}

	public Item[] getItems() {
		return items;
	}
}
