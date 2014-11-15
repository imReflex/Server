package org.endeavor.game.entity.mob.impl;

import org.endeavor.engine.definitions.NpcCombatDefinition;
import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.content.combat.Hit;
import org.endeavor.game.content.skill.magic.MagicConstants;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerConstants;

public class GelatinnothMother extends Mob {
	public static final int[] STAGES = { 3497, 3498, 3499, 3500, 3501, 3502 };

	private byte stage = 0;

	public GelatinnothMother(Location location, Player owner) {
		super(3497, false, location, owner, false, false, null);

		TaskQueue.queue(new Task(this, 20) {
			@Override
			public void execute() {
				GelatinnothMother tmp4_1 = GelatinnothMother.this;
				tmp4_1.stage = ((byte) (tmp4_1.stage + 1));

				if (stage == GelatinnothMother.STAGES.length) {
					stage = 0;
				}

				transform(GelatinnothMother.STAGES[stage]);
			}

			@Override
			public void onStop() {
			}
		});
	}

	@Override
	public NpcCombatDefinition getCombatDefinition() {
		return GameDefinitionLoader.getNpcCombatDefinition(STAGES[0]);
	}

	@Override
	public int getAffectedDamage(Hit hit) {
		if (hit.getAttacker() != null && !hit.getAttacker().isNpc()) {
			Player p = World.getPlayers()[hit.getAttacker().getIndex()];
			
			if (p != null && PlayerConstants.isOwner(p)) {
				return hit.getDamage();
			}
		}
		
		if ((hit.getAttacker() != null) && (!hit.getAttacker().isNpc())) {
			Player p = org.endeavor.game.entity.World.getPlayers()[hit.getAttacker().getIndex()];
			if (p != null) {
				if (getId() == STAGES[0]) {
					if ((p.getCombat().getCombatType() == CombatTypes.MAGIC)
							&& (MagicConstants.getSpellTypeForId(p.getMagic().getSpellCasting().getCurrentSpellId()) == MagicConstants.SpellType.WIND)) {
						return hit.getDamage();
					}

					return 0;
				}
				if (getId() == STAGES[1]) {
					if (p.getCombat().getCombatType() == CombatTypes.MELEE) {
						return hit.getDamage();
					}

					return 0;
				}
				if (getId() == STAGES[2]) {
					if ((p.getCombat().getCombatType() == CombatTypes.MAGIC)
							&& (MagicConstants.getSpellTypeForId(p.getMagic().getSpellCasting().getCurrentSpellId()) == MagicConstants.SpellType.WATER)) {
						return hit.getDamage();
					}

					return 0;
				}
				if (getId() == STAGES[3]) {
					if ((p.getCombat().getCombatType() == CombatTypes.MAGIC)
							&& (MagicConstants.getSpellTypeForId(p.getMagic().getSpellCasting().getCurrentSpellId()) == MagicConstants.SpellType.FIRE)) {
						return hit.getDamage();
					}

					return 0;
				}
				if (getId() == STAGES[4]) {
					if (p.getCombat().getCombatType() == CombatTypes.RANGED) {
						return hit.getDamage();
					}

					return 0;
				}
				if (getId() == STAGES[5]) {
					if ((p.getCombat().getCombatType() == CombatTypes.MAGIC)
							&& (MagicConstants.getSpellTypeForId(p.getMagic().getSpellCasting().getCurrentSpellId()) == MagicConstants.SpellType.EARTH)) {
						return hit.getDamage();
					}

					return 0;
				}
			}
		}

		return hit.getDamage();
	}
}
