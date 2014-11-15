package org.endeavor.game.entity;

import java.io.Serializable;

public class UpdateFlags implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5442861817181957603L;
	protected boolean isUpdateRequired;
	protected boolean isForceChatUpdate;
	protected String forceChatMessage;
	protected boolean graphicsUpdateRequired;
	protected boolean animationUpdateRequired;
	protected int animationId;
	protected int animationDelay;
	protected boolean entityFaceUpdate;
	protected int entityFaceIndex = -1;
	protected boolean faceToDirection;
	protected Location face;
	protected boolean hitUpdate;
	protected boolean hitUpdate2;
	protected int hitUpdateCombatType = 1;
	protected int hitUpdateCombatType2 = 1;
	protected int damage;
	protected int damage2;
	protected int hitType;
	protected int hitType2;
	protected Graphic graphic;

	public void sendAnimation(int animationId, int animationDelay) {
		this.animationId = animationId;
		this.animationDelay = animationDelay;
		animationUpdateRequired = true;
		isUpdateRequired = true;
	}

	public void sendAnimation(Animation animation) {
		animationId = animation.getId();
		animationDelay = animation.getDelay();
		animationUpdateRequired = true;
		isUpdateRequired = true;
	}

	public void faceEntity(int entityFaceIndex) {
		this.entityFaceIndex = entityFaceIndex;
		entityFaceUpdate = true;
		isUpdateRequired = true;
	}

	public void sendFaceToDirection(Location face) {
		this.face = face;
		faceToDirection = true;
		isUpdateRequired = true;
	}

	public void sendFaceToDirection(int x, int y) {
		face = new Location(x, y);
		faceToDirection = true;
		isUpdateRequired = true;
	}

	public void sendHit(int damage, int hitType, int hitUpdateCombatType) {
		this.damage = damage;
		this.hitType = hitType;
		hitUpdate = true;
		isUpdateRequired = true;
		this.hitUpdateCombatType = hitUpdateCombatType;
	}

	public void sendHit2(int damage, int hitType, int hitUpdateCombatType2) {
		damage2 = damage;
		hitType2 = hitType;
		hitUpdate2 = true;
		isUpdateRequired = true;
		this.hitUpdateCombatType2 = hitUpdateCombatType2;
	}

	public void sendForceMessage(String forceChatMessage) {
		this.forceChatMessage = forceChatMessage;
		isForceChatUpdate = true;
		isUpdateRequired = true;
	}

	public void reset() {
		if (graphicsUpdateRequired)
			graphicsUpdateRequired = false;
		if (animationUpdateRequired)
			animationUpdateRequired = false;
		if (faceToDirection)
			faceToDirection = false;
		if (hitUpdate)
			hitUpdate = false;
		if (hitUpdate2)
			hitUpdate2 = false;
		if (graphic != null)
			graphic = null;
		if (isForceChatUpdate)
			isForceChatUpdate = false;
		if (entityFaceIndex != 65535)
			faceEntity(65535);
	}

	public void setUpdateRequired(boolean isUpdateRequired) {
		this.isUpdateRequired = isUpdateRequired;
	}

	public boolean isUpdateRequired() {
		return isUpdateRequired;
	}

	public void setForceChatUpdate(boolean isForceChatUpdate) {
		this.isForceChatUpdate = isForceChatUpdate;
	}

	public boolean isForceChatUpdate() {
		return isForceChatUpdate;
	}

	public void setForceChatMessage(String forceChatMessage) {
		this.forceChatMessage = forceChatMessage;
	}

	public String getForceChatMessage() {
		return forceChatMessage;
	}

	public void setGraphicsUpdateRequired(boolean graphicsUpdateRequired) {
		this.graphicsUpdateRequired = graphicsUpdateRequired;
	}

	public boolean isGraphicsUpdateRequired() {
		return graphicsUpdateRequired;
	}

	public void setAnimationUpdateRequired(boolean animationUpdateRequired) {
		this.animationUpdateRequired = animationUpdateRequired;
	}

	public boolean isAnimationUpdateRequired() {
		return animationUpdateRequired;
	}

	public void setAnimationId(int animationId) {
		this.animationId = animationId;
	}

	public int getAnimationId() {
		return animationId;
	}

	public void setAnimationDelay(int animationDelay) {
		this.animationDelay = animationDelay;
	}

	public int getAnimationDelay() {
		return animationDelay;
	}

	public void setEntityFaceUpdate(boolean entityFaceUpdate) {
		this.entityFaceUpdate = entityFaceUpdate;
	}

	public boolean isEntityFaceUpdate() {
		return entityFaceUpdate;
	}

	public void setEntityFaceIndex(int entityFaceIndex) {
		this.entityFaceIndex = entityFaceIndex;
	}

	public int getEntityFaceIndex() {
		return entityFaceIndex;
	}

	public void setFaceToDirection(boolean faceToDirection) {
		this.faceToDirection = faceToDirection;
	}

	public boolean isFaceToDirection() {
		return faceToDirection;
	}

	public void setFace(Location face) {
		this.face = face;
	}

	public Location getFace() {
		return face;
	}

	public void setHitUpdate(boolean hitUpdate) {
		this.hitUpdate = hitUpdate;
	}

	public boolean isHitUpdate() {
		return hitUpdate;
	}

	public void setHitUpdate2(boolean hitUpdate2) {
		this.hitUpdate2 = hitUpdate2;
	}

	public boolean isHitUpdate2() {
		return hitUpdate2;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage2(int damage2) {
		this.damage2 = damage2;
	}

	public int getDamage2() {
		return damage2;
	}

	public void setHitType(int hitType) {
		this.hitType = hitType;
	}

	public int getHitType() {
		return hitType;
	}

	public void setHitType2(int hitType2) {
		this.hitType2 = hitType2;
	}

	public int getHitType2() {
		return hitType2;
	}

	public int getHitUpdateCombatType() {
		return hitUpdateCombatType;
	}

	public void setHitUpdateCombatType(int hitUpdateCombatType) {
		this.hitUpdateCombatType = hitUpdateCombatType;
	}

	public int getHitUpdateCombatType2() {
		return hitUpdateCombatType2;
	}

	public void setHitUpdateCombatType2(int hitUpdateCombatType2) {
		this.hitUpdateCombatType2 = hitUpdateCombatType2;
	}

	public Graphic getGraphic() {
		return graphic;
	}

	public void sendGraphic(Graphic graphic) {
		this.graphic = graphic;
		graphicsUpdateRequired = true;
		isUpdateRequired = true;
	}
}
