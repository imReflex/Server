package org.endeavor.game.entity.item.impl;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import org.endeavor.GameSettings;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.task.impl.WalkToTask;
import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.game.content.dialogue.impl.Scavvo;
import org.endeavor.game.content.minigames.bountyhunter.BountyHunter;
import org.endeavor.game.content.skill.hunter.Hunter;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.pathfinding.StraightPathFinder;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerConstants;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.out.impl.SendGroundItem;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveGroundItem;
import org.endeavor.game.entity.player.net.out.impl.SendSound;

public class LocalGroundItems implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8900273653421053654L;
	private final Player player;
	private Deque<GroundItem> loaded = new ArrayDeque<GroundItem>();
	private Deque<GroundItem> adding = new ArrayDeque<GroundItem>();
	private Deque<GroundItem> removing = new ArrayDeque<GroundItem>();
	private boolean hasLoaded = true;

	public LocalGroundItems(Player player) {
		this.player = player;
	}

	public void process() {
		if (!hasLoaded) {
			load();
		}

		GroundItem g = null;

		synchronized (adding) {
			while ((g = adding.poll()) != null) {
				player.getClient().queueOutgoingPacket(new SendGroundItem(player, g));
				loaded.add(g);
			}
		}

		synchronized (removing) {
			while ((g = removing.poll()) != null)
				player.getClient().queueOutgoingPacket(new SendRemoveGroundItem(player, g));
		}
	}

	public void pickup(final int x, final int y, final int id) {
		GroundItem g = GroundItemHandler.getGroundItem(id, x, y, player.getLocation().getZ(), player.getUsername(), false);
		if ((g == null) || (!GroundItemHandler.exists(g))) {
			player.getMovementHandler().reset();
			return;
		}
		
		TaskQueue.queue(new WalkToTask(player, g) {
			@Override
			public void onDestination() {
				GroundItem g = GroundItemHandler.getGroundItem(id, x, y, player.getLocation().getZ(), player.getUsername(), false);

				if ((g == null) || (!g.exists())) {
					player.getMovementHandler().reset();
					stop();
					return;
				}

				if (!StraightPathFinder.isInteractionPathClear(player.getLocation(), g.getLocation())) {
					player.getClient().queueOutgoingPacket(new SendMessage("I can't reach that!"));
					stop();
					return;
				}
				if ((player.getInventory().hasSpaceFor(g.getItem())) && (GroundItemHandler.remove(g))) {
					player.getClient().queueOutgoingPacket(new SendSound(358, 1, 0));
					player.getInventory().add(g.getItem());
					if(BountyHunter.getSingleton().isInBountyHunter(player)) {
						BountyHunter.getSingleton().addLeavePenalty(player);
					}
				} else {
					player.getClient().queueOutgoingPacket(new SendMessage("You do not have enough inventory space to pick that up."));
				}
			}
		});
	}
	
	public void handleSecondAction(final int x, final int y, final int id) {
		GroundItem g = GroundItemHandler.getGroundItem(id, x, y, player.getLocation().getZ(), player.getUsername(), false);
		if ((g == null) || (!GroundItemHandler.exists(g))) {
			player.getMovementHandler().reset();
			return;
		}
		
		TaskQueue.queue(new WalkToTask(player, g) {
			@Override
			public void onDestination() {
				GroundItem g = GroundItemHandler.getGroundItem(id, x, y, player.getLocation().getZ(), player.getUsername(), false);

				if ((g == null) || (!g.exists())) {
					player.getMovementHandler().reset();
					stop();
					return;
				}

				if (!StraightPathFinder.isInteractionPathClear(player.getLocation(), g.getLocation())) {
					player.getClient().queueOutgoingPacket(new SendMessage("I can't reach that!"));
					stop();
					return;
				}
				/*if ((player.getInventory().hasSpaceFor(g.getItem())) && (GroundItemHandler.remove(g))) {
					player.getClient().queueOutgoingPacket(new SendSound(358, 1, 0));
					player.getInventory().add(g.getItem());
					if(BountyHunter.getSingleton().isInBountyHunter(player)) {
						BountyHunter.getSingleton().addLeavePenalty(player);
					}
				} else {
					player.getClient().queueOutgoingPacket(new SendMessage("You do not have enough inventory space to pick that up."));
				}*/
				/** Handle item actions. **/
				player.getAttributes().set("relay_trap", true);
				if(Hunter.operateItem(player, id, new Location(x,y,player.getLocation().getZ()))) {
					return;
				}
				if(player.getAttributes().get("relay_trap") != null)
					player.getAttributes().remove("relay_trap");
			}
		});
	}

	public boolean stack(GroundItem g) {
		if (!g.getItem().getDefinition().isStackable()) {
			return false;
		}

		GroundItem onGround = GroundItemHandler.getNonGlobalGroundItem(g.getItem().getId(), g.getLocation().getX(), g
				.getLocation().getY(), g.getLocation().getZ(), g.getLongOwnerName());

		if (onGround == null) {
			return false;
		}

		if (onGround.isGlobal()) {
			return false;
		}
		player.getClient().queueOutgoingPacket(new SendRemoveGroundItem(player, onGround));
		onGround.getItem().add(g.getItem().getAmount());
		player.getClient().queueOutgoingPacket(new SendGroundItem(player, onGround));
		return true;
	}

	public void drop(int id, int slot) {
		if ((player.getController().equals(ControllerManager.WILDERNESS_CONTROLLER)) && (player.getCombat().inCombat())) {
			if (GameDefinitionLoader.getHighAlchemyValue(id) > 1000) {
				player.getClient().queueOutgoingPacket(
						new SendMessage("You cannot drop this item while in combat here."));
			}
		} else if ((player.getController().equals(ControllerManager.DUELING_CONTROLLER))
				|| (player.getController().equals(ControllerManager.ROLLING_DICE_CONTROLLER))) {
			return;
		}

		if ((PlayerConstants.isOwner(player)) && (!GameSettings.DEV_MODE)) {
			player.getInventory().clear(slot);
			return;
		}

		Item drop = new Item(player.getInventory().get(slot));

		Scavvo.doItemBreaking(drop);

		GroundItemHandler.add(drop, new Location(player.getLocation()), player);

		player.getInventory().clear(slot);
	}

	private void load() {
		synchronized (GroundItemHandler.getActive()) {
			for (Iterator<?> g = GroundItemHandler.getActive().iterator(); g.hasNext();) {
				GroundItem i = (GroundItem) g.next();

				if (GroundItemHandler.visible(player, i)) {
					synchronized (adding) {
						adding.add(i);
					}
				}
			}
		}

		hasLoaded = true;
	}

	public boolean drop(Item item, Location location) {
		return GroundItemHandler.add(item, location, player);
	}

	public void add(GroundItem groundItem) {
		synchronized (adding) {
			adding.add(groundItem);
		}
	}

	public void remove(GroundItem groundItem) {
		synchronized (removing) {
			removing.add(groundItem);
		}
	}

	public void onRegionChange() {
		hasLoaded = false;

		synchronized (adding) {
			adding.clear();
		}

		synchronized (loaded) {
			GroundItem g;
			while ((g = loaded.poll()) != null) {
				player.getClient().queueOutgoingPacket(new SendRemoveGroundItem(player, g));
			}
		}
	}
}
