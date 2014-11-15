package org.endeavor.game.entity.player;

import java.util.BitSet;

import org.endeavor.engine.utility.NameUtil;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.UpdateFlags;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.net.out.impl.SendString;

public final class PlayerUpdateFlags {
	private final BitSet set = new BitSet(30);
	private final byte rights;
	private final byte crownId;
	private final int chatColor;
	private final int chatEffects;
	private final byte[] chatText;
	private final byte gender;
	private final int[] appearance;
	private final byte[] colors;
	private final short npcAppearanceId;
	private final byte primaryDirection;
	private final byte secondaryDirection;
	private final byte hp;
	private final byte maxHP;
	private final short x;
	private final short y;
	private final short z;
	private final short regionX;
	private final short regionY;
	private final short regionZ;
	private final byte headicon;
	private final short[] equipment;
	private final short standEmote;
	private final short standTurnEmote;
	private final short walkEmote;
	private final short turn180Emote;
	private final short turn90CWEmote;
	private final short turn90CCWEmote;
	private final short runEmote;
	private final String username;
	private final byte combatLevel;
	private final byte combatLevelAddon;
	private final byte skullIcon;
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
	private final byte team;
	private final long usernameToLong;
	private final long titleToLong;
	private final int titleColor;

	public static byte getTeam(Player p) {
		Item cape = p.getEquipment().getItems()[1];

		if ((cape != null) && (cape.getId() >= 4315) && (cape.getId() <= 4413)) {
			return (byte) (cape.getId() - 4315 + 1);
		}

		return 0;
	}

	public PlayerUpdateFlags(Player player) {
		UpdateFlags u = player.getUpdateFlags();

		set.set(0, player.isVisible());
		set.set(1, player.isChatUpdateRequired());
		set.set(2, player.isAppearanceUpdateRequired());
		set.set(3, u.isUpdateRequired());
		set.set(4, u.isForceChatUpdate());
		set.set(5, u.isGraphicsUpdateRequired());
		set.set(6, u.isAnimationUpdateRequired());
		set.set(7, u.isHitUpdate());
		set.set(8, u.isHitUpdate2());
		set.set(9, u.isEntityFaceUpdate());
		set.set(10, u.isFaceToDirection());
		set.set(11, player.needsPlacement());
		set.set(12, player.isResetMovementQueue());

		team = getTeam(player);

		x = ((short) player.getLocation().getX());
		y = ((short) player.getLocation().getY());
		z = ((short) player.getLocation().getZ());

		regionX = ((short) player.getCurrentRegion().getX());
		regionY = ((short) player.getCurrentRegion().getY());
		regionZ = ((short) player.getCurrentRegion().getZ());
		
		usernameToLong = player.getUsernameToLong();
		titleToLong = NameUtil.nameToLong(player.getTitle());
		titleColor = player.getTitleColor();

		set.get(0);

		if (set.get(3)) {
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

			if (set.get(10)) {
				faceX = ((short) u.getFace().getX());
				faceY = ((short) u.getFace().getY());
			} else {
				faceX = 0;
				faceY = 0;
			}

			if ((set.get(7)) || (set.get(8))) {
				hp = ((byte) player.getLevels()[3]);
				maxHP = ((byte) player.getMaxLevels()[3]);
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

			if (set.get(1)) {
				chatText = player.getChatText();
				chatColor = player.getChatColor();
				chatEffects = player.getChatEffects();
			} else {
				chatText = null;
				chatColor = 0;
				chatEffects = 0;
			}

			entityFaceIndex = u.getEntityFaceIndex();
		} else {
			animationId = 0;
			animationDelay = 0;
			graphicId = 0;
			graphicHeight = 0;
			graphicDelay = 0;
			faceX = 0;
			faceY = 0;
			hp = 0;
			maxHP = 0;
			hitUpdateCombatType = 0;
			hitUpdateCombatType2 = 0;
			damage = 0;
			damage2 = 0;
			hitType = 0;
			hitType2 = 0;
			forceChatMessage = null;
			entityFaceIndex = 0;
			chatText = null;
			chatColor = 0;
			chatEffects = 0;
		}

		primaryDirection = ((byte) player.getMovementHandler().getPrimaryDirection());
		secondaryDirection = ((byte) player.getMovementHandler().getSecondaryDirection());

		equipment = new short[14];
		for (int i = 0; i < equipment.length; i++) {
			if (player.getEquipment().getItems()[i] != null) {
				equipment[i] = ((short) player.getEquipment().getItems()[i].getId());
			}
		}

		npcAppearanceId = ((short) player.getNpcAppearanceId());

		rights = ((byte) player.getRights());
		crownId = ((byte) player.getCrownId());
		combatLevel = ((byte) player.getSkill().getCombatLevel());
		combatLevelAddon = ((byte) player.getSkill().getSummoningLevelAddon());
		headicon = ((byte) player.getPrayer().getHeadicon());

		standEmote = ((short) player.getAnimations().getStandEmote());
		runEmote = ((short) player.getAnimations().getRunEmote());
		standTurnEmote = ((short) player.getAnimations().getStandTurnEmote());
		walkEmote = ((short) player.getAnimations().getWalkEmote());
		turn180Emote = ((short) player.getAnimations().getTurn180Emote());
		turn90CWEmote = ((short) player.getAnimations().getTurn90CWEmote());
		turn90CCWEmote = ((short) player.getAnimations().getTurn90CCWEmote());

		username = player.getUsername();
		skullIcon = ((byte) player.getSkulling().getSkullIcon());

		gender = player.getGender();

		colors = (player.getColors().clone());
		appearance = (player.getAppearance().clone());
	}

	public boolean isPlacement() {
		return set.get(11);
	}

	public boolean isChatUpdateRequired() {
		return set.get(1);
	}

	public boolean isAppearanceUpdateRequired() {
		return set.get(2);
	}

	public boolean isUpdateRequired() {
		return set.get(3);
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

	public int getRights() {
		return rights;
	}
	
	public int getCrownId() {
		return crownId;
	}

	public boolean isVisible() {
		return set.get(0);
	}

	public int getChatColor() {
		return chatColor;
	}

	public int getChatEffects() {
		return chatEffects;
	}

	public byte[] getChatText() {
		return chatText;
	}

	public int getGender() {
		return gender;
	}

	public int[] getAppearance() {
		return appearance;
	}

	public byte[] getColors() {
		return colors;
	}

	public int getNpcAppearanceId() {
		return npcAppearanceId;
	}

	public boolean isResetMovementQueue() {
		return set.get(12);
	}

	public int getPrimaryDirection() {
		return primaryDirection;
	}

	public int getSecondaryDirection() {
		return secondaryDirection;
	}

	public byte getHp() {
		return hp;
	}

	public byte getMaxHP() {
		return maxHP;
	}

	public Location getLocation() {
		return new Location(x, y, z);
	}

	public boolean isActive() {
		return set.get(0);
	}

	public Location getRegion() {
		return new Location(regionX, regionY, regionZ);
	}

	public int getHeadicon() {
		return headicon;
	}

	public short[] getEquipment() {
		return equipment;
	}

	public int getStandEmote() {
		return standEmote;
	}

	public int getStandTurnEmote() {
		return standTurnEmote;
	}

	public int getWalkEmote() {
		return walkEmote;
	}

	public int getTurn180Emote() {
		return turn180Emote;
	}

	public int getTurn90CWEmote() {
		return turn90CWEmote;
	}

	public int getTurn90CCWEmote() {
		return turn90CCWEmote;
	}

	public int getRunEmote() {
		return runEmote;
	}

	public String getUsername() {
		return username;
	}

	public int getCombatLevel() {
		return combatLevel;
	}
	
	public int getCombatLevelAddon() {
		return combatLevelAddon;
	}

	public int getSkullIcon() {
		return skullIcon;
	}

	public BitSet getSet() {
		return set;
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

	public short getRegionX() {
		return regionX;
	}

	public short getRegionY() {
		return regionY;
	}

	public short getRegionZ() {
		return regionZ;
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

	public byte getTeam() {
		return team;
	}
	
	public long getUsernameToLong() {
		return usernameToLong;
	}
	
	public long getTitle() {
		return titleToLong;
	}
	
	public int getTitleColor() {
		return titleColor;
	}
}
