package org.endeavor.game.content.skill;

import org.endeavor.engine.utility.GameDefinitionLoader;

public class SkillingGeneralPrices {
	
	public static void declare() {
		//mining
		set(447, 20000);
		set(449, 30000);
		set(451, 50000);
		
		//wc
		set(1513, 14000);
		set(1515, 5000);
		
		//rc
		set(563, 600);
		
		//fishing
		set(15270, 6000);
		set(15272, 9000);
		set(389, 4000);
		set(391, 6000);
		
		//herb
		set(187, 50000);
		
		//crafting
		set(1645, 250000);
		
		//farming
		set(6012, 50000);
		set(14583, 100000);
	}
	
	private static void set(int id, int amount) {
		GameDefinitionLoader.setValue(id, amount + (int) (amount * 0.1), amount);
	}

}