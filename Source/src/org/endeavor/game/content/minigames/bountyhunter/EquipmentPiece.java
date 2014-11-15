package org.endeavor.game.content.minigames.bountyhunter;

import java.util.ArrayList;

/**
 * 
 * @author Allen K.
 *
 * @param <E> Integer
 * @param <Z> CombatType
 */
public class EquipmentPiece<E,Z> {

	ArrayList<E> equipmentSlots = new ArrayList<E>();
	ArrayList<Z> combatTypes = new ArrayList<Z>();
	
	public boolean containsBoth(E slot, Z type) {
		return equipmentSlots.contains(slot) && combatTypes.contains(type);
	}
	
	public void add(E slot, Z type) {
		equipmentSlots.add(slot);
		combatTypes.add(type);
	}
	
}
