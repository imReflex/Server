package org.endeavor.game.entity.following.impl;

import org.endeavor.game.GameConstants;
import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.following.Following;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.pathfinding.RS317PathFinder;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class PlayerFollowing extends Following {
	/**
	 * 
	 */
	private static final long serialVersionUID = 890927121544197490L;
	private final Player player;

	public PlayerFollowing(Player player) {
		super(player);
		this.player = player;
	}

	@Override
	public boolean pause() {
		if (type == Following.FollowType.COMBAT) {
			if (GameConstants.withinBlock(following.getLocation().getX(), following.getLocation().getY(),
					following.getSize(), player.getLocation().getX(), player.getLocation().getY())) {
				return false;
			}

			if (following.isNpc()) {
				CombatTypes c = player.getCombat().getCombatType();

				if ((c == CombatTypes.MAGIC) || (c == CombatTypes.RANGED)) {
					Mob mob = org.endeavor.game.entity.World.getNpcs()[following.getIndex()];

					if (!mob.withinMobWalkDistance(player)) {
						return false;
					}
				}
			}

			if ((!player.getLocation().equals(following.getLocation())) && (player.getCombat().withinDistanceForAttack(player.getCombat().getCombatType(), true))) {
				return true;
			}

		}

		return false;
	}

	@Override
	public void findPath(Location location) {
		if (type == Following.FollowType.COMBAT) {
			if (player.getCombat().getCombatType() == CombatTypes.MELEE)
				RS317PathFinder.findRoute(player, location.getX(), location.getY(), false, 0, 0);
			else
				RS317PathFinder.findRoute(player, location.getX(), location.getY(), true, 16, 16);
		} else
			RS317PathFinder.findRoute(player, location.getX(), location.getY(), true, 16, 16);
	}

	@Override
	public void onCannotReach() {
		reset();

		if (type == Following.FollowType.COMBAT) {
			player.getCombat().reset();
		}

		player.getClient().queueOutgoingPacket(new SendMessage("I can't reach that!"));
	}
}
