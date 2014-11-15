package org.endeavor.game.entity.object;

import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendObject;

public class LocalObjects implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6387393310157878296L;
	private final Player player;
	private Deque<GameObject> adding = new LinkedList<GameObject>();
	private boolean load = false;

	public LocalObjects(Player player) {
		this.player = player;
	}

	public void process() {
		if (load) {
			load();
		}

		synchronized (adding) {
			GameObject g = null;
			while ((g = adding.poll()) != null) {
				player.getClient().queueOutgoingPacket(new SendObject(player, g));
			}
		}
	}

	public void add(GameObject o) {
		synchronized (adding) {
			adding.add(o);
		}
	}

	private void load() {
		if (ObjectManager.getActive() == null) {
			return;
		}

		synchronized (ObjectManager.getActive()) {
			if (ObjectManager.getActive() == null) {
				return;
			}

			for (GameObject o : ObjectManager.getActive()) {
				if (player.withinRegion(o.getLocation())
						&& player.getLocation().getZ() % 4 == o.getLocation().getZ() % 4) {
					player.getClient().queueOutgoingPacket(new SendObject(player, o));
				}
			}
		}

		load = false;
	}

	public void onRegionChange() {
		synchronized (adding) {
			adding.clear();
		}

		load = true;
	}
}
