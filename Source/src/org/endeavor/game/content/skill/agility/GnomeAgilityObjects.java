package org.endeavor.game.content.skill.agility;

import org.endeavor.engine.task.TaskQueue;
import org.endeavor.game.content.skill.agility.gnome.BalanceOverObject;
import org.endeavor.game.content.skill.agility.gnome.ClimbOverTask;
import org.endeavor.game.content.skill.agility.gnome.ClimbThroughPipe;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class GnomeAgilityObjects {
	public static final int TICKET = 2996;
	public static final String[] TICKET_KEYS = { "gnometicketkeya", "gnometicketkeyb", "gnometicketkeyc",
			"gnometicketkeyd", "gnometicketkeye", "gnometicketkeyf", "gnometicketkeyg" };

	public static void setCompleted(Player p, int i) {
		p.getAttributes().set(TICKET_KEYS[i], Byte.valueOf((byte) 0));
	}

	public static boolean hasCompleted(Player p) {
		for (String i : TICKET_KEYS) {
			if (p.getAttributes().get(i) == null) {
				return false;
			}
		}

		return true;
	}

	public static void onFinishCourse(Player p) {
		if (hasCompleted(p)) {
			p.getInventory().addOrCreateGroundItem(2996, p.isSuperMember() ? 2 : 1, true);
			p.getSkill().addExperience(16, 30.0D);
			p.getClient().queueOutgoingPacket(new SendMessage("You have completed the course and earned a ticket."));

			p.getAchievements().incr(p, "Finish Gnome agility course");
		} else {
			p.getClient().queueOutgoingPacket(
					new SendMessage("You did not cross every obstacle, so you do not get a ticket."));
		}

		for (String i : TICKET_KEYS)
			p.getAttributes().remove(i);
	}

	public static boolean clickObject(Player p, int id) {
		switch (id) {
		case 2295:
			if (p.getSkill().locked()) {
				return true;
			}
			p.getSkill().lock(4);

			TaskQueue.queue(new BalanceOverObject(p, new Location(2474, 3429), 7.5D));
			setCompleted(p, 0);
			return true;
		case 2285:
			if (p.getSkill().locked()) {
				return true;
			}
			p.getSkill().lock(4);

			TaskQueue.queue(new ClimbOverTask(p, 1, new Location(2473, 3424, 1), 828, 5.0D));
			setCompleted(p, 1);
			return true;
		case 35970:
			if (p.getSkill().locked()) {
				return true;
			}
			p.getSkill().lock(4);

			TaskQueue.queue(new ClimbOverTask(p, 1, new Location(2473, 3420, 2), 828, 5.0D));
			setCompleted(p, 2);
			return true;
		case 2312:
			if (p.getSkill().locked()) {
				return true;
			}
			p.getSkill().lock(4);

			TaskQueue.queue(new BalanceOverObject(p, new Location(2483, 3420, 2), 7.5D));
			setCompleted(p, 3);
			return true;
		case 2314:
			if (p.getSkill().locked()) {
				return true;
			}
			p.getSkill().lock(4);

			TaskQueue.queue(new ClimbOverTask(p, 1, new Location(2486, 3422, 0), 828, 5.0D));
			setCompleted(p, 4);
			return true;
		case 2286:
			if ((p.getSkill().locked()) || (p.getLocation().getY() >= 3426)) {
				return true;
			}
			p.getSkill().lock(4);

			TaskQueue.queue(new ClimbOverTask(p, 1,
					new Location(p.getLocation().getX(), p.getLocation().getY() + 2, 0), 828, 2.0D));
			setCompleted(p, 5);
			return true;
		case 43544:
			if (p.getLocation().getY() > 3432) {
				return true;
			}

			if (p.getSkill().locked()) {
				return true;
			}
			p.getSkill().lock(4);

			TaskQueue.queue(new ClimbThroughPipe(p, new Location(2483, 3435), new Location(2483, 3432), 7.5D));
			setCompleted(p, 6);
			return true;
		case 43543:
			if (p.getLocation().getY() > 3432) {
				return true;
			}

			if (p.getSkill().locked()) {
				return true;
			}
			p.getSkill().lock(4);

			TaskQueue.queue(new ClimbThroughPipe(p, new Location(2487, 3435), new Location(2487, 3432), 7.5D));
			setCompleted(p, 6);
			return true;
		}

		return false;
	}
}
