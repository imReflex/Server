package org.endeavor.game.entity.player.net.in.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.task.impl.FollowToEntityTask;
import org.endeavor.game.content.clans.Clan;
import org.endeavor.game.content.clans.Clans;
import org.endeavor.game.content.minigames.armsrace.ARConstants;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.following.Following;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.in.IncomingPacket;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;

public class PlayerOptionPacket extends IncomingPacket {
	public static final int TRADE = 153;
	public static final int FOLLOW = 128;
	public static final int ATTACK = 73;
	public static final int OPTION_4 = 139;
	public static final int MAGIC_ON_PLAYER = 249;
	public static final int USE_ITEM_ON_PLAYER = 14;
	public static final int TRADE_ANSWER2 = 39;

	@Override
	public int getMaxDuplicates() {
		return 2;
	}

	@Override
	public void handle(final Player player, StreamBuffer.InBuffer in, int opcode, int length) {
		if ((player.isDead()) || (!player.getController().canClick())) {
			return;
		}

		int playerSlot = -1;

		int itemSlot = -1;
		TaskQueue.onMovement(player);

		Player other = null;

		player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
		player.getMagic().getSpellCasting().resetOnAttack();

		switch (opcode) {
		case 139:
			final int slot = in.readShort(true, StreamBuffer.ByteOrder.LITTLE);

			if ((!World.isPlayerWithinRange(slot)) || (World.getPlayers()[slot] == null) || (slot == player.getIndex())) {
				return;
			}

			TaskQueue.queue(new FollowToEntityTask(player, World.getPlayers()[slot]) {
				@Override
				public void onDestination() {
					Player other = World.getPlayers()[slot];

					if (other == null) {
						player.getMovementHandler().reset();
						return;
					}

					if (player.getController().equals(ControllerManager.DUEL_ARENA_CONTROLLER)) {
						player.face(other);
						player.getDueling().request(other);
					}/* else if (player.inHomeBank()) {
						player.face(other);
						player.getShopping().open(other);
					}*/ else if(player.inArea(new Location(3264, 3672, 0), new Location(3279, 3695, 0), false) && 
							other.inArea(new Location(3264, 3672, 0), new Location(3279, 3695, 0), false)) {
						player.face(other);
						Clan clan = Clans.getClanForOwner(player);
						Clan otherClan = Clans.getClanForOwner(other);
						if(clan == otherClan) {
							player.send(new SendMessage("This player is in your clan!"));
							return;
						}
						if(clan != null) {
							if(otherClan != null) {
								if(otherClan.challengeQueueContains(player))
									otherClan.acceptChallenge(player);
								else
									clan.sendChallengeRequest(player, other);
							} else {
								player.send(new SendMessage("This player does not own a clan."));
							}
						} else {
							player.send(new SendMessage("You are not the owner of a clan."));
						}
					} else {
						player.send(new SendMessage("You are not in the right area to use this option."));
						player.send(new SendMessage("If you are challenging a clan owner, please be in the safe area."));
					}
				}
			});
			break;
		case 153:
			final int tradeSlot = in.readShort(true, StreamBuffer.ByteOrder.LITTLE);

			if ((!World.isPlayerWithinRange(tradeSlot)) || (World.getPlayers()[tradeSlot] == null)
					|| (tradeSlot == player.getIndex())) {
				return;
			}

			TaskQueue.queue(new FollowToEntityTask(player, World.getPlayers()[tradeSlot]) {
				@Override
				public void onDestination() {
					Player other = World.getPlayers()[tradeSlot];

					if (other == null) {
						stop();
						player.getMovementHandler().reset();
						return;
					}

					player.face(other);

					player.getTrade().request(other);
					stop();
				}
			});
			break;
		case 128:
			player.getMovementHandler().reset();
			playerSlot = in.readShort();

			if ((!World.isPlayerWithinRange(playerSlot)) || (playerSlot == player.getIndex())) {
				return;
			}

			other = World.getPlayers()[playerSlot];

			if (other == null) {
				return;
			}

			player.getFollowing().setFollow(other);
			break;
		case 73:
			playerSlot = in.readShort(true, StreamBuffer.ByteOrder.LITTLE);
			player.getMovementHandler().reset();

			if ((playerSlot == player.getIndex()) || (!World.isPlayerWithinRange(playerSlot))) {
				return;
			}

			other = World.getPlayers()[playerSlot];

			if (other == null) {
				return;
			}

			if (player.getController().canMove(player)) {
				player.getFollowing().setFollow(other, Following.FollowType.COMBAT);
			}
			player.getCombat().setAttacking(other);

			player.getMagic().getSpellCasting().disableClickCast();

			break;
		case 249:
			playerSlot = in.readShort(true, StreamBuffer.ValueType.A);
			int magicId = in.readShort(true, StreamBuffer.ByteOrder.LITTLE);

			if (player.getController().equals(ARConstants.AR_CONTROLLER)) {
				return;
			}

			player.getMovementHandler().reset();

			if ((!World.isPlayerWithinRange(playerSlot)) || (World.getPlayers()[playerSlot] == null)
					|| (playerSlot == player.getIndex())) {
				return;
			}

			other = World.getPlayers()[playerSlot];

			player.getMagic().getSpellCasting().castCombatSpell(magicId, other);
			break;
		case 14:
			playerSlot = in.readShort();
			itemSlot = in.readShort(StreamBuffer.ByteOrder.LITTLE);

			if ((!World.isPlayerWithinRange(playerSlot)) || (playerSlot == player.getIndex()))
				;
			break;
		}
	}
}
