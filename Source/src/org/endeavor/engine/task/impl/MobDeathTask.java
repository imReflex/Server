package org.endeavor.engine.task.impl;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.Task.BreakType;
import org.endeavor.engine.task.Task.StackType;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.game.content.combat.Combat;
import org.endeavor.game.content.combat.impl.DamageMap;
import org.endeavor.game.content.sounds.MobSounds;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.UpdateFlags;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.mob.MobConstants;
import org.endeavor.game.entity.mob.MobConstants.MobDissapearDelay;
import org.endeavor.game.entity.mob.MobDrops;
import org.endeavor.game.entity.mob.Walking;
import org.endeavor.game.entity.mob.impl.KalphiteQueen;

public class MobDeathTask extends Task
{
	public static final int DEATH_DELAY = 2;
	public static final int DEFAULT_DISSAPEAR_DELAY = 5;
	private final Mob mob;

	public MobDeathTask(final Mob mob)
	{
		super(mob, mob.getRespawnTime() + 1 + MobConstants.MobDissapearDelay.getDelay(mob.getId()), false, Task.StackType.STACK, Task.BreakType.NEVER, 0);
		this.mob = mob;

		mob.setDead(true);
		mob.getUpdateFlags().faceEntity(65535);
		mob.getCombat().reset();

		Task death = new Task(mob, 2, false, Task.StackType.STACK, Task.BreakType.NEVER, 0)
		{
			public void execute() {
				Entity killer = mob.getCombat().getDamageTracker().getKiller();

				if ((killer != null) && (!killer.isNpc())) {
					MobSounds.sendDeathSound(org.endeavor.game.entity.World.getPlayers()[killer.getIndex()], mob.getId());
				}

				mob.getUpdateFlags().sendAnimation(mob.getDeathAnimation());
				stop();
			}

			public void onStop()
			{
			}
		};
		Task dissapear = new Task(mob, MobConstants.MobDissapearDelay.getDelay(mob.getId()), false, Task.StackType.STACK, Task.BreakType.NEVER, 0)
		{
			public void execute() {
				mob.onDeath();
				MobDrops.dropItems(mob.getCombat().getDamageTracker().getKiller(), mob);
				mob.setVisible(false);
				Walking.setNpcOnTile(mob, false);
				mob.getUpdateFlags().setUpdateRequired(true);
				mob.curePoison(0);
				mob.unTransform();
				stop();
			}

			public void onStop()
			{
			}
		};
		TaskQueue.queue(death);
		TaskQueue.queue(dissapear);
	}

	public void execute()
	{
		if (mob.shouldRespawn()) {
			mob.setVisible(true);
			mob.getLocation().setAs(mob.getNextSpawnLocation());
			mob.getUpdateFlags().setUpdateRequired(true);
			mob.getCombat().forRespawn();
			Walking.setNpcOnTile(mob, true);
			mob.resetLevels();
			mob.unfreeze();
			mob.getCombat().getDamageTracker().clear();

			if ((mob instanceof KalphiteQueen))
				((KalphiteQueen)mob).transform();
		}
		else {
			mob.remove();
		}
		stop();
	}

	public void onStop()
	{
	}
}