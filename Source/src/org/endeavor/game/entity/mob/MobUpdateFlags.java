package org.endeavor.game.entity.mob;

import java.util.BitSet;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.UpdateFlags;

public final class MobUpdateFlags {
	private final BitSet set = new BitSet(12);
	private final short transformId;
	private final byte primaryDirection;
	private final short hp;
	private final short maxHP;
	private final short x;
	private final short y;
	private final short z;
	private final short id;
	private final byte faceDir;
	private final String forceChatMessage;
	private final short animationId;
	private final byte animationDelay;
	private final int entityFaceIndex;
	private final short faceX;
	private final short faceY;
	private final byte hitUpdateCombatType;
	private final byte hitUpdateCombatType2;
	private final byte damage;
	private final byte damage2;
	private final byte hitType;
	private final byte hitType2;
	private final short graphicId;
	private final byte graphicHeight;
	private final byte graphicDelay;

	public MobUpdateFlags(Mob mob) {
		UpdateFlags u = mob.getUpdateFlags();

		set.set(0, mob.isVisible());
		set.set(1, mob.isTransformUpdate());
		set.set(2, mob.isPlacement());
		set.set(3, u.isUpdateRequired());
		set.set(4, u.isForceChatUpdate());
		set.set(5, u.isGraphicsUpdateRequired());
		set.set(6, u.isAnimationUpdateRequired());
		set.set(7, u.isHitUpdate());
		set.set(8, u.isHitUpdate2());
		set.set(9, u.isEntityFaceUpdate());
		set.set(10, u.isFaceToDirection());

		if (set.get(6)) {
			animationId = ((short) u.getAnimationId());
			animationDelay = ((byte) u.getAnimationDelay());
		} else {
			animationId = 0;
			animationDelay = 0;
		}

		if (set.get(5)) {
			graphicId = ((short) u.getGraphic().getId());
			graphicHeight = ((byte) u.getGraphic().getHeight());
			graphicDelay = ((byte) u.getGraphic().getDelay());
		} else {
			graphicId = 0;
			graphicHeight = 0;
			graphicDelay = 0;
		}

		if (set.get(1))
			transformId = ((short) mob.getTransformId());
		else {
			transformId = 0;
		}

		if (set.get(10)) {
			faceX = ((short) u.getFace().getX());
			faceY = ((short) u.getFace().getY());
		} else {
			faceX = 0;
			faceY = 0;
		}

		if ((set.get(7)) || (set.get(8))) {
			hp = mob.getLevels()[3];
			maxHP = mob.getMaxLevels()[3];
			hitUpdateCombatType = ((byte) u.getHitUpdateCombatType());
			hitUpdateCombatType2 = ((byte) u.getHitUpdateCombatType2());
			damage = ((byte) u.getDamage());
			damage2 = ((byte) u.getDamage2());
			hitType = ((byte) u.getHitType());
			hitType2 = ((byte) u.getHitType2());
		} else {
			hp = 0;
			maxHP = 0;
			hitUpdateCombatType = 0;
			hitUpdateCombatType2 = 0;
			damage = 0;
			damage2 = 0;
			hitType = 0;
			hitType2 = 0;
		}

		if (set.get(4))
			forceChatMessage = u.getForceChatMessage();
		else {
			forceChatMessage = null;
		}

		primaryDirection = ((byte) mob.getMovementHandler().getPrimaryDirection());
		x = ((short) mob.getLocation().getX());
		y = ((short) mob.getLocation().getY());
		z = ((short) mob.getLocation().getZ());
		id = ((short) mob.getId());
		faceDir = ((byte) mob.getFaceDirection());
		entityFaceIndex = u.getEntityFaceIndex();
	}

	public boolean isUpdateRequired() {
		return true;
	}

	public boolean isHitUpdate() {
		return set.get(7);
	}

	public boolean isAnimationUpdateRequired() {
		return set.get(6);
	}

	public boolean isGraphicsUpdateRequired() {
		return set.get(5);
	}

	public boolean isEntityFaceUpdate() {
		return set.get(9);
	}

	public boolean isForceChatUpdate() {
		return set.get(4);
	}

	public boolean isHitUpdate2() {
		return set.get(8);
	}

	public boolean isFaceToDirection() {
		return set.get(10);
	}

	public boolean isVisible() {
		return set.get(0);
	}

	public boolean isTransformUpdate() {
		return set.get(1);
	}

	public int getTransformId() {
		return transformId;
	}

	public int getPrimaryDirection() {
		return primaryDirection;
	}

	public Location getLocation() {
		return new Location(x, y, z);
	}

	public short getHp() {
		return hp;
	}

	public short getMaxHP() {
		return maxHP;
	}

	public int getId() {
		return id;
	}

	public boolean isPlacement() {
		return set.get(2);
	}

	public int getFaceDirection() {
		return faceDir;
	}

	public short getX() {
		return x;
	}

	public short getY() {
		return y;
	}

	public short getZ() {
		return z;
	}

	public String getForceChatMessage() {
		return forceChatMessage;
	}

	public short getAnimationId() {
		return animationId;
	}

	public byte getAnimationDelay() {
		return animationDelay;
	}

	public int getEntityFaceIndex() {
		return entityFaceIndex;
	}

	public short getFaceX() {
		return faceX;
	}

	public short getFaceY() {
		return faceY;
	}

	public Location getFaceLocation() {
		return new Location(faceX, faceY);
	}

	public byte getHitUpdateCombatType() {
		return hitUpdateCombatType;
	}

	public byte getHitUpdateCombatType2() {
		return hitUpdateCombatType2;
	}

	public byte getDamage() {
		return damage;
	}

	public byte getDamage2() {
		return damage2;
	}

	public byte getHitType() {
		return hitType;
	}

	public byte getHitType2() {
		return hitType2;
	}

	public short getGraphicId() {
		return graphicId;
	}

	public byte getGraphicHeight() {
		return graphicHeight;
	}

	public byte getGraphicDelay() {
		return graphicDelay;
	}
}
