package org.endeavor.game.content.minigames.bountyhunter;

/**
 * 
 * @author Allen K.
 *
 */
public enum Craters {

	LOW("Low", 3, 55, 28119),
	MEDIUM("Medium", 50, 100, 28120),
	HIGH("High", 95, 138, 28121);
	
	private String name;
	private int lowRange;
	private int highRange;
	private int objectId;
	
	Craters(String name, int lowRange, int highRange, int objectId) {
		this.name = name;
		this.lowRange = lowRange;
		this.highRange = highRange;
		this.objectId = objectId;
	}
	
	public String getName() {
		return name;
	}
	
	public int getLowRange() {
		return lowRange;
	}
	
	public int getHighRange() {
		return highRange;
	}
	
	public int getObjectID() {
		return objectId;
	}
	
	public static Craters getCraterForId(int objectID) {
		for(Craters crater : Craters.values())
			if(crater.getObjectID() == objectID)
				return crater;
		return null;
	}
	
}
