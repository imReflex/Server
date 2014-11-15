package org.endeavor.game.content.combat.impl;

import java.util.HashMap;
import java.util.Map;

import org.endeavor.engine.definitions.ItemDefinition;
import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;

public class PoisonWeapons {
	private static final Map<Integer, PoisonData> poison = new HashMap<Integer, PoisonData>();

	public static final void declare() {
		for (int i = 0; i < 20145; i++) {
			ItemDefinition def = GameDefinitionLoader.getItemDef(i);

			if ((def != null) && (def.getName() != null)) {
				String name = def.getName();

				if (name.contains("(p)"))
					poison.put(Integer.valueOf(i), new PoisonData(4));
				else if (name.contains("(p+)"))
					poison.put(Integer.valueOf(i), new PoisonData(5));
				else if (name.contains("(p++)"))
					poison.put(Integer.valueOf(i), new PoisonData(6));
			}
		}
	}

	public static void checkForPoison(Player player, Entity attack) {
		if (Misc.randomNumber(3) != 0) {
			return;
		}

		if ((attack != null) && (!attack.isNpc())) {
			Player o = org.endeavor.game.entity.World.getPlayers()[attack.getIndex()];

			if (o != null) {
				Item shield = o.getEquipment().getItems()[5];

				if ((shield != null) && (shield.getId() == 18340)) {
					return;
				}
			}

		}

		Item weapon = player.getEquipment().getItems()[3];
		Item ammo = player.getEquipment().getItems()[13];

		CombatTypes type = player.getCombat().getCombatType();

		if (type == CombatTypes.MELEE) {
			if ((weapon == null) || (poison.get(Integer.valueOf(weapon.getId())) == null)) {
				return;
			}
			attack.poison(poison.get(Integer.valueOf(weapon.getId())).getStart());
		} else if (type == CombatTypes.RANGED) {
			if ((ammo == null) || (poison.get(Integer.valueOf(ammo.getId())) == null)) {
				return;
			}
			attack.poison(poison.get(Integer.valueOf(ammo.getId())).getStart());
		}
	}
}
