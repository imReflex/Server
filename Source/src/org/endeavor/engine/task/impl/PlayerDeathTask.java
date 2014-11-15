package org.endeavor.engine.task.impl;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.game.content.combat.impl.PlayerDrops;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerConstants;
import org.endeavor.game.entity.player.controllers.Controller;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class PlayerDeathTask extends Task
{
	private final Player player;
	private final Controller c;

	public PlayerDeathTask(final Player player)
	{
		super(player, 5, false, Task.StackType.NEVER_STACK, Task.BreakType.NEVER, 0);

		this.player = player;
		c = player.getController();

		if (player.isDead()) {
			stop();
			return;
		}

		player.getUpdateFlags().faceEntity(65535);
		player.setDead(true);
		player.getMovementHandler().reset();

		TaskQueue.queue(new Task(player, 2, false, Task.StackType.STACK, Task.BreakType.NEVER, 5)
		{
			public void execute() {
				player.getUpdateFlags().sendAnimation(PlayerConstants.getDeathAnimation(), 0);
				stop();
			}

			public void onStop()
			{
			}
		});
	}

	public void execute()
	{
		if (!c.isSafe()) {
			PlayerDrops.dropItemsOnDeath(player);
		}
		
		Entity killer = player.getCombat().getDamageTracker().getKiller();
		if(killer != null && killer instanceof Player)
			player.setLastKiller((Player)killer);

		player.teleport(c.getRespawnLocation(player));

		if (player.isPoisoned()) {
			player.curePoison(0);
		}

		if (player.getSkulling().isSkulled()) {
			player.getSkulling().unskull(player);
		}

		if (player.getMagic().isVengeanceActive()) {
			player.getMagic().setVengeanceActive(false);
		}

		player.getSpecialAttack().setSpecialAmount(100);
		player.getSpecialAttack().update();

		player.getPrayer().disable();

		player.getRunEnergy().setEnergy(100);

		player.getEquipment().onLogin();

		player.setAppearanceUpdateRequired(true);
		player.getClient().queueOutgoingPacket(new SendMessage("Oh dear, you have died!"));

		player.getCombat().forRespawn();
		c.onDeath(player);
		stop();
	}

	public void onStop()
	{
	}
}