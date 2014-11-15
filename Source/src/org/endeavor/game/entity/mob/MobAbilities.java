package org.endeavor.game.entity.mob;

import java.util.HashMap;
import java.util.Map;

import org.endeavor.engine.definitions.NpcDefinition;
import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.game.content.combat.CombatEffect;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.mob.abilities.BarrelchestAbility;
import org.endeavor.game.entity.mob.abilities.BorkAbility;
import org.endeavor.game.entity.mob.abilities.CorporealBeastAbility;
import org.endeavor.game.entity.mob.abilities.HobgoblinGeomancerAbility;
import org.endeavor.game.entity.mob.abilities.IcyBonesAbility;
import org.endeavor.game.entity.mob.abilities.JadAbility;
import org.endeavor.game.entity.mob.abilities.TormentedDemonAbility;
import org.endeavor.game.entity.mob.abilities.UnholyCurseBearerAbility;

public class MobAbilities {
	private static final Map<Integer, CombatEffect> abilities = new HashMap<Integer, CombatEffect>();

	public static final void declare() {
		abilities.put(5666, new BarrelchestAbility());
		abilities.put(Integer.valueOf(2745), new JadAbility());
		abilities.put(Integer.valueOf(10057), new IcyBonesAbility());
		abilities.put(Integer.valueOf(10127), new UnholyCurseBearerAbility());
		abilities.put(Integer.valueOf(10072), new HobgoblinGeomancerAbility());
		abilities.put(Integer.valueOf(7133), new BorkAbility());
		abilities.put(Integer.valueOf(8133), new CorporealBeastAbility());

		for (int i = 0; i < 18000; i++) {
			NpcDefinition def = GameDefinitionLoader.getNpcDefinition(i);
			if (def != null) {
				String check = def.getName().toLowerCase();

				if (check.contains("tormented demon"))
					abilities.put(Integer.valueOf(i), new TormentedDemonAbility());
			}
		}
	}

	public static final void executeAbility(int id, Mob mob, Entity a) {
		CombatEffect e = abilities.get(Integer.valueOf(id));

		if (e != null)
			e.execute(mob, a);
	}
}
