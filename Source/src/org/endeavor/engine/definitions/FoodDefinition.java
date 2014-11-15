package org.endeavor.engine.definitions;

public class FoodDefinition {

	private short id;
	private String name;
	private byte heal;
	private byte delay;
	private short replaceId;
	private String message;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getHeal() {
		return heal;
	}

	public int getDelay() {
		return delay;
	}

	public int getReplaceId() {
		return replaceId;
	}

	public String getMessage() {
		return message;
	}
}
