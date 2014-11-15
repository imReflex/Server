package org.endeavor.game.content.skill.summoning;

import java.util.HashMap;
import java.util.Map;

import org.endeavor.engine.definitions.ItemDefinition;
import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.game.content.skill.summoning.specials.Minotaur;
import org.endeavor.game.content.skill.summoning.specials.SpiritJelly;
import org.endeavor.game.content.skill.summoning.specials.SpiritSpider;
import org.endeavor.game.content.skill.summoning.specials.SpiritTerrorBird;
import org.endeavor.game.content.skill.summoning.specials.SpiritWolf;
import org.endeavor.game.content.skill.summoning.specials.UnicornStallion;
import org.endeavor.game.entity.item.Item;

public class SummoningConstants {
	private static final Map<Integer, Familiar> itemsForFamiliar = new HashMap<Integer, Familiar>();

	private static final Map<Familiar, Integer> scrollsForFamiliar = new HashMap<Familiar, Integer>();

	private static final Map<Familiar, FamiliarSpecial> specials = new HashMap<Familiar, FamiliarSpecial>();
	public static final int SHARD_ID = 18016;
	public static final int POUCH_ID = 12525;
	public static final String SUMMONING_FAMILIAR_LOGIN_KEY = "summoningfamsave";
	public static final String SUMMONING_BOB_INVENTORY_KEY = "summoningbobinventory";
	public static final String FAMILIAR_NEXT_MAX_HIT = "summonfammax";

	public static Familiar getFamiliarForPouch(int id) {
		return itemsForFamiliar.get(Integer.valueOf(id));
	}

	public static boolean isAllowed(Familiar f) {
		if (f.attack <= 0) {
			return false;
		}

		return true;
	}

	public static int getScrollForFamiliar(FamiliarMob mob) {
		return !scrollsForFamiliar.containsKey(mob.getData()) ? -1 : scrollsForFamiliar.get(mob.getData()).intValue();
	}

	public static FamiliarSpecial getSpecial(Familiar f) {
		return specials.get(f);
	}

	public static void declare() {
		specials.put(Familiar.a, new SpiritWolf());
		specials.put(Familiar.cc, new SpiritTerrorBird());
		specials.put(Familiar.c, new SpiritSpider());
		specials.put(Familiar.ee, new SpiritJelly());
		specials.put(Familiar.u, new Minotaur());
		specials.put(Familiar.y, new Minotaur());
		specials.put(Familiar.u, new Minotaur());
		specials.put(Familiar.ff, new Minotaur());
		specials.put(Familiar.pp, new Minotaur());
		specials.put(Familiar.aaa, new Minotaur());
		specials.put(Familiar.lll, new Minotaur());
		specials.put(Familiar.mmm, new UnicornStallion());

		GameDefinitionLoader.setValue(18016, 40, 25);
		GameDefinitionLoader.setValue(12525, 750, 700);

		for (Pouch i : Pouch.values()) {
			ItemDefinition def = Item.getDefinition(i.pouchId);

			if (def != null) {
				def.setUntradable();
			}

			GameDefinitionLoader.setValue(i.secondIngredientId, i.ordinal() * 250, i.ordinal() * 25);
		}

		for (Scroll i : Scroll.values()) {
			ItemDefinition def = Item.getDefinition(i.itemId);

			if (def != null) {
				def.setUntradable();
			}
		}

		main: for (Familiar i : Familiar.values()) {
			int pouch = i.pouch;

			if (pouch >= 0) {
				for (Scroll k : Scroll.values()) {
					if ((k.pouch != null) && (k.pouch.getId() == pouch)) {
						scrollsForFamiliar.put(i, Integer.valueOf(k.itemId));
						continue main;
					}
				}

				System.out.println("no scroll for: " + i.name);
			}
		}
		for (Familiar f : Familiar.values())
			itemsForFamiliar.put(Integer.valueOf(f.pouch), f);
	}
}
