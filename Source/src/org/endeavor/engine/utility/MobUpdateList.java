package org.endeavor.engine.utility;

import java.util.List;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.endeavor.game.entity.mob.Mob;

/**
 * A linked list of (@UpdateableMob)s that contains no duplicates
 * 
 * @author Michael Sasse
 * 
 */
public class MobUpdateList {

	/**
	 * The list
	 */
	private final List<UpdateableMob> list = new Vector<UpdateableMob>();

	/**
	 * The queues to dictate modification
	 */
	private final Queue<UpdateableMob> incr = new ConcurrentLinkedQueue<UpdateableMob>();
	private final Queue<UpdateableMob> decr = new ConcurrentLinkedQueue<UpdateableMob>();
	private final Queue<UpdateableMob> remove = new ConcurrentLinkedQueue<UpdateableMob>();

	/**
	 * Creates a new (@MobUpdateList)
	 */
	public MobUpdateList() {
	}

	/**
	 * Processes modifications to list
	 */
	public final void process() {
		UpdateableMob a = null;

		while ((a = incr.poll()) != null) {
			add(a);
		}

		while ((a = decr.poll()) != null) {
			remove(a);
		}

		while ((a = remove.poll()) != null) {
			definiteRemove(a);
		}
	}

	public void incr(Mob mob) {
		incr.add(new UpdateableMob(mob));
	}

	public void decr(Mob mob) {
		decr.add(new UpdateableMob(mob));
	}

	public void toRemoval(Mob mob) {
		remove.add(new UpdateableMob(mob));
	}

	/**
	 * Adds a mob, or increments the view count
	 */
	private void add(UpdateableMob u) {
		int i = list.indexOf(u);

		if (i > -1) {
			list.get(i).viewed += 1;
		} else {
			list.add(u);
		}
	}

	/**
	 * Removes a mob or decrements the view count
	 */
	private void remove(UpdateableMob u) {
		int i = list.indexOf(u);

		if (i > -1) {
			UpdateableMob l = list.get(i);
			l.viewed--;
			if (l.viewed <= 0) {
				list.remove(u);
			}
		}
	}

	/**
	 * Removes the mob
	 */
	private void definiteRemove(UpdateableMob u) {
		list.remove(u);
	}

	/**
	 * Gets the list of mobs
	 */
	public List<UpdateableMob> getList() {
		return list;
	}
}
