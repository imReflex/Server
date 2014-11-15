package org.endeavor.game.entity.player.net;

import static org.endeavor.game.entity.player.EquipmentConstants.*;

import java.util.Iterator;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.player.EquipmentConstants;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerUpdateFlags;

public final class PlayerUpdating {
	public static void update(Player player, PlayerUpdateFlags[] pFlags) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(12228);
		StreamBuffer.OutBuffer block = StreamBuffer.newOutBuffer(8192);

		out.writeVariableShortPacketHeader(player.getClient().getEncryptor(), 81);
		out.setAccessType(StreamBuffer.AccessType.BIT_ACCESS);

		PlayerUpdateFlags local = pFlags[player.getIndex()];

		updateLocalPlayerMovement(local, out);
		if (local.isUpdateRequired()) {
			updateState(local, block, false, true);
		}

		out.writeBits(8, player.getPlayers().size());
		for (Iterator<Player> i = player.getPlayers().iterator(); i.hasNext();) {
			PlayerUpdateFlags flags = pFlags[((Player) i.next()).getIndex()];

			if ((flags != null) && (flags.getLocation().isViewableFrom(local.getLocation())) && (flags.isActive())
					&& (!flags.isPlacement()) && (flags.isVisible())) {
				updateOtherPlayerMovement(flags, out);
				if (flags.isUpdateRequired())
					updateState(flags, block, false, player.getPrivateMessaging().ignored(flags.getUsername()));
			} else {
				out.writeBits(1, 1);
				out.writeBits(2, 3);
				i.remove();
			}
		}

		int added = 0;

		for (int i = 0; i < World.getPlayers().length; i++) {
			if ((player.getPlayers().size() >= 255) || (added >= 15)) {
				break;
			}
			
			PlayerUpdateFlags flags = pFlags[i];
			
			if (flags != null && i != player.getIndex() && !flags.getUsername().equals(local.getUsername()) && flags.isActive()) {

				if (flags != null) {
					if (!doesLocalListContainPlayer(player, flags.getUsernameToLong())
							&& flags.getLocation().isViewableFrom(player.getLocation())) {
						player.getPlayers().add(World.getPlayers()[i]);
						addPlayer(out, local, flags, i);
						updateState(flags, block, true, player.getPrivateMessaging().ignored(flags.getUsername()));
						added++;
					}
				}
			}
		}
		if (block.getBuffer().writerIndex() > 0) {
			out.writeBits(11, 2047);
			out.setAccessType(StreamBuffer.AccessType.BYTE_ACCESS);
			out.writeBytes(block.getBuffer());
		} else {
			out.setAccessType(StreamBuffer.AccessType.BYTE_ACCESS);
		}

		out.finishVariableShortPacketHeader();
		player.getClient().send(out.getBuffer());
	}
	
	public static boolean doesLocalListContainPlayer(Player local, long username) {
		for (Player p : local.getPlayers()) {
			if (p != null && p.getUsernameToLong() == username) {
				return true;
			}
		}
		
		return false;
	}

	public static void appendChat(PlayerUpdateFlags flags, StreamBuffer.OutBuffer out) {
		out.writeShort(((flags.getChatColor() & 0xFF) << 8) + (flags.getChatEffects() & 0xFF), StreamBuffer.ByteOrder.LITTLE);
		out.writeByte(flags.getCrownId());
		out.writeByte(0); // glow
		out.writeByte(flags.getChatText().length, StreamBuffer.ValueType.C);
		out.writeBytesReverse(flags.getChatText());
	}

	public static void appendAppearance(PlayerUpdateFlags flags, StreamBuffer.OutBuffer out) {
		StreamBuffer.OutBuffer block = StreamBuffer.newOutBuffer(128);

		block.writeByte(flags.getGender());
		block.writeByte(flags.getHeadicon());
		block.writeByte(flags.getSkullIcon());

		//block.writeByte(flags.getRights());

		short[] e = flags.getEquipment();

		if (flags.getNpcAppearanceId() == -1) {
			if (e[HELM_SLOT] != 0)
				block.writeShort(512 + e[HELM_SLOT]);
			else {
				block.writeByte(0);
			}

			if (e[CAPE_SLOT] != 0)
				block.writeShort(512 + e[CAPE_SLOT]);
			else {
				block.writeByte(0);
			}

			if (e[NECKLACE_SLOT] != 0)
				block.writeShort(512 + e[NECKLACE_SLOT]);
			else {
				block.writeByte(0);
			}

			if (e[WEAPON_SLOT] != 0)
				block.writeShort(512 + e[WEAPON_SLOT]);
			else {
				block.writeByte(0);
			}

			if (e[TORSO_SLOT] != 0)
				block.writeShort(512 + e[TORSO_SLOT]);
			else {
				block.writeShort(256 + flags.getAppearance()[1]);
			}

			if (e[SHIELD_SLOT] != 0)
				block.writeShort(512 + e[SHIELD_SLOT]);
			else {
				block.writeByte(0);
			}

			if ((e[TORSO_SLOT] != 0) && (EquipmentConstants.isFullBody(e[TORSO_SLOT])))
				block.writeByte(0);
			else {
				block.writeShort(256 + flags.getAppearance()[2]);
			}

			if (e[LEGS_SLOT] != 0)
				block.writeShort(512 + e[LEGS_SLOT]);
			else {
				block.writeShort(256 + flags.getAppearance()[4]);
			}

			if (((e[HELM_SLOT] != 0) 
					&& (EquipmentConstants.isFullHelm(e[HELM_SLOT]))) 
					|| e[HELM_SLOT] != 0 
				&& (EquipmentConstants.isFullMask(e[HELM_SLOT]))) {
				block.writeByte(0);
			} else if ((e[HELM_SLOT] != 0) && (EquipmentConstants.isForceNewHair(e[HELM_SLOT])))
				block.writeShort(259);
			else {
				block.writeShort(256 + flags.getAppearance()[0]);
			}

			if (e[GLOVES_SLOT] != 0)
				block.writeShort(512 + e[GLOVES_SLOT]);
			else {
				block.writeShort(256 + flags.getAppearance()[3]);
			}

			if (e[BOOTS_SLOT] != 0)
				block.writeShort(512 + e[BOOTS_SLOT]);
			else {
				block.writeShort(256 + flags.getAppearance()[5]);
			}

			if (flags.getGender() == 1) {
				block.writeByte(0);
			} else if ((e[0] != 0) && (EquipmentConstants.isFullMask(e[HELM_SLOT])))
				block.writeByte(0);
			else
				block.writeShort(256 + flags.getAppearance()[6]);
		} else {
			block.writeShort(-1);
			block.writeShort(flags.getNpcAppearanceId());
		}

		block.writeByte(flags.getColors()[0]);
		block.writeByte(flags.getColors()[1]);
		block.writeByte(flags.getColors()[2]);
		block.writeByte(flags.getColors()[3]);
		block.writeByte(flags.getColors()[4]);

		block.writeShort(flags.getStandEmote());
		block.writeShort(flags.getStandTurnEmote());
		block.writeShort(flags.getWalkEmote());
		block.writeShort(flags.getTurn180Emote());
		block.writeShort(flags.getTurn90CWEmote());
		block.writeShort(flags.getTurn90CCWEmote());
		block.writeShort(flags.getRunEmote());
		
		block.writeShort(0); // constitution
		block.writeShort(0); // max life points

		block.writeLong(flags.getUsernameToLong());
		block.writeLong(flags.getTitle());
		block.writeInt(flags.getTitleColor());
		block.writeByte(flags.getCombatLevel());
		//block.writeByte(flags.getCombatLevelAddon());
		block.writeShort(0);
		block.writeShort(/*flags.getTitle()*/flags.getCrownId());
		
		out.writeByte(block.getBuffer().writerIndex(), StreamBuffer.ValueType.C);
		out.writeBytes(block.getBuffer());
	}

	public static void addPlayer(StreamBuffer.OutBuffer out, PlayerUpdateFlags local, PlayerUpdateFlags other, int index) {
		out.writeBits(11, index);
		//out.writeBit(true);
		//out.writeBit(true);
		out.writeBits(1, 1);
		out.writeBits(1, 1);

		Location delta = Misc.delta(local.getLocation(), other.getLocation());
		out.writeBits(5, delta.getY());
		out.writeBits(5, delta.getX());
	}

	public static void updateLocalPlayerMovement(PlayerUpdateFlags flags, StreamBuffer.OutBuffer out) {
		boolean updateRequired = flags.isUpdateRequired();
		if (flags.isPlacement()) {
			out.writeBit(true);
			int posX = flags.getLocation().getLocalX(flags.getRegion());
			int posY = flags.getLocation().getLocalY(flags.getRegion());
			appendPlacement(out, posX, posY, flags.getLocation().getZ(), flags.isResetMovementQueue(), updateRequired);
		} else {
			int pDir = flags.getPrimaryDirection();
			int sDir = flags.getSecondaryDirection();
			if (pDir != -1) {
				out.writeBit(true);
				if (sDir != -1)
					appendRun(out, pDir, sDir, updateRequired);
				else {
					appendWalk(out, pDir, updateRequired);
				}
			} else if (updateRequired) {
				out.writeBit(true);
				appendStand(out);
			} else {
				out.writeBit(false);
			}
		}
	}

	public static void updateOtherPlayerMovement(PlayerUpdateFlags flags, StreamBuffer.OutBuffer out) {
		boolean updateRequired = flags.isUpdateRequired();
		int pDir = flags.getPrimaryDirection();
		int sDir = flags.getSecondaryDirection();
		if (pDir != -1) {
			out.writeBit(true);
			if (sDir != -1)
				appendRun(out, pDir, sDir, updateRequired);
			else {
				appendWalk(out, pDir, updateRequired);
			}
		} else if (updateRequired) {
			out.writeBit(true);
			appendStand(out);
		} else {
			out.writeBit(false);
		}
	}

	public static void updateState(PlayerUpdateFlags flags, StreamBuffer.OutBuffer block, boolean forceAppearance,
			boolean noChat) {
		int mask = 0;
		if (flags.isGraphicsUpdateRequired()) {
			mask |= 0x100;
		}
		if (flags.isAnimationUpdateRequired()) {
			mask |= 0x8;
		}
		if (flags.isForceChatUpdate()) {
			mask |= 0x4;
		}
		if ((flags.isChatUpdateRequired()) && (!noChat)) {
			mask |= 0x80;
		}
		if (flags.isEntityFaceUpdate()) {
			mask |= 0x1;
		}
		if ((flags.isAppearanceUpdateRequired()) || (forceAppearance)) {
			mask |= 0x10;
		}
		if (flags.isFaceToDirection()) {
			mask |= 0x2;
		}
		if (flags.isHitUpdate()) {
			mask |= 0x20;
		}
		if (flags.isHitUpdate2()) {
			mask |= 0x200;
		}
		if (mask >= 0x100) {
			mask |= 0x40;
			block.writeShort(mask, StreamBuffer.ByteOrder.LITTLE);
			/*block.writeByte(mask * 0xFF);
			block.writeByte(mask >> 8);*/
		} else {
			block.writeByte(mask);
		}
		if (flags.isGraphicsUpdateRequired()) {
			block.writeShort(flags.getGraphicId(), StreamBuffer.ByteOrder.LITTLE);
			block.writeInt(flags.getGraphicDelay() | flags.getGraphicHeight() << 16);
		}
		if (flags.isAnimationUpdateRequired()) {
			block.writeShort(flags.getAnimationId(), StreamBuffer.ByteOrder.LITTLE);
			block.writeByte(flags.getAnimationDelay(), StreamBuffer.ValueType.C);
		}
		if (flags.isForceChatUpdate()) {
			block.writeString(flags.getForceChatMessage());
		}
		if ((flags.isChatUpdateRequired()) && (!noChat)) {
			appendChat(flags, block);
		}
		if (flags.isEntityFaceUpdate()) {
			block.writeShort(flags.getEntityFaceIndex(), StreamBuffer.ByteOrder.LITTLE);
		}
		if ((flags.isAppearanceUpdateRequired()) || (forceAppearance)) {
			appendAppearance(flags, block);
		}
		if (flags.isFaceToDirection()) {
			block.writeShort(flags.getFaceX() * 2 + 1, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
			block.writeShort(flags.getFaceY() * 2 + 1, StreamBuffer.ByteOrder.LITTLE);
		}
		if (flags.isHitUpdate()) {
			block.writeByte(flags.getDamage());
			block.writeByte(flags.getHitType(), StreamBuffer.ValueType.A);
			block.writeByte(flags.getHitUpdateCombatType());
			
			block.writeByte(0); // Absorption
			block.writeByte(flags.getHp(), StreamBuffer.ValueType.C);
			block.writeByte(flags.getMaxHP());
			/*System.out.println("Damage: " + flags.getDamage() + " Type: " + flags.getHitType() + " Icon: " + flags.getHitUpdateCombatType() + " Absorption: 0" +
					" HP: " + flags.getHp() + " Max HP: " + flags.getMaxHP());*/
		}
		if (flags.isHitUpdate2()) { 
			block.writeByte(flags.getDamage2());
			block.writeByte(flags.getHitType2(), StreamBuffer.ValueType.S);
			block.writeByte(flags.getHitUpdateCombatType2());                                                                                                                                                                                                                                                                                                                                                                                                           
			
			block.writeByte(0); // Absorption
			block.writeByte(flags.getHp());
			block.writeByte(flags.getMaxHP(), StreamBuffer.ValueType.C);
			/*System.out.println("Damage2: " + flags.getDamage() + " Type: " + flags.getHitType() + " Icon: " + flags.getHitUpdateCombatType() + " Absorption: 0" +
					" HP: " + flags.getHp() + " Max HP: " + flags.getMaxHP());*/
		}
	}

	public static void appendStand(StreamBuffer.OutBuffer out) {
		out.writeBits(2, 0);
	}

	public static void appendWalk(StreamBuffer.OutBuffer out, int direction, boolean attributesUpdate) {
		out.writeBits(2, 1);

		out.writeBits(3, direction);
		out.writeBit(attributesUpdate);
	}

	public static void appendRun(StreamBuffer.OutBuffer out, int direction, int direction2, boolean attributesUpdate) {
		out.writeBits(2, 2);

		out.writeBits(3, direction);
		out.writeBits(3, direction2);
		out.writeBit(attributesUpdate);
	}

	public static void appendPlacement(StreamBuffer.OutBuffer out, int localX, int localY, int z,
			boolean discardMovementQueue, boolean attributesUpdate) {
		out.writeBits(2, 3);

		out.writeBits(2, z);
		out.writeBit(discardMovementQueue);
		out.writeBit(attributesUpdate);
		out.writeBits(7, localY);
		out.writeBits(7, localX);
	}
}
