package org.endeavor.game.entity.player;

import static org.endeavor.game.entity.player.EquipmentConstants.*;

import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.skill.SkillConstants;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.net.out.impl.SendInterface;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public final class PlayerConstants {
	public static final String[] LATEST_UPDATES = { "Clan Wars & Bounty Hunter!"};
	public static final String OFFICIAL_CLAN_CHAT = "revolutionx";
	public static final String[] OWNER_USERNAME = { "allen", "beatz", "allen v2" };
	public static final String IS_OWNER_KEY = "ownerkey";
	public static final int PLAYER_CREATION_INTERFACE = 3559;
	public static final int PLAYER_TIMEOUT = 180000;
	public static final int MAX_ITEM_COUNT = 21411;
	public static Location HOME = new Location(3086, 3490, 0);

	public static Location EDGEVILLE = new Location(3086, 3489, 0);
	public static final byte GENDER_MALE = 0;
	public static final byte GENDER_FEMALE = 1;
	public static final byte APPEARANCE_SLOT_CHEST = 0;
	public static final byte APPEARANCE_SLOT_ARMS = 1;
	public static final byte APPEARANCE_SLOT_LEGS = 2;
	public static final byte APPEARANCE_SLOT_HEAD = 3;
	public static final byte APPEARANCE_SLOT_HANDS = 4;
	public static final byte APPEARANCE_SLOT_FEET = 5;
	public static final byte APPEARANCE_SLOT_BEARD = 6;
	public static final short DEFAULT_DEATH_ANIMATION = 9055;
	public static final short DEFAULT_STAND_EMOTE = 808;
	public static final short DEFAULT_TURN_EMOTE = 823;
	public static final short DEFAULT_WALK_EMOTE = 819;
	public static final short DEFAULT_TURN_180_EMOTE = 820;
	public static final short DEFAULT_TURN_90CW_EMOTE = 821;
	public static final short DEFAULT_TURN_90CCW_EMOTE = 822;
	public static final short DEFAULT_RUN_EMOTE = 824;
	public static final int RIGHTS_REGULAR = 0;
	public static final int RIGHTS_DONATOR = 1;
	public static final int RIGHTS_DONATOR_2 = 2;
	public static final int RIGHTS_MODERATOR = 3;
	public static final int RIGHTS_ADMINISTRATOR = 4;
	public static final String LOGIN_MESSAGE_1 = "Welcome to RevolutionX.";
	public static final String OTHER_PLAYER_IS_BUSY = "The other player is busy at the moment.";
	public static final String ABSORB_HEALTH_MESSAGE = "You absorb some of your opponent's hitpoints.";
	public static final String FROZEN_MESSAGE = "A magical force stops you from moving.";
	public static final String NOT_ENOUGH_INV_SPACE_MESSAGE = "You do not have enough inventory space to carry that.";
	public static final byte COMBAT_TAB = 0;
	public static final byte SKILLS_TAB = 2;
	public static final byte QUEST_TAB = 14;
	public static final byte ACHIEVEMENT_TAB = 1;
	public static final byte INVENTORY_TAB = 3;
	public static final byte EQUIPMENT_TAB = 4;
	public static final byte PRAYER_TAB = 5;
	public static final byte MAGIC_TAB = 6;
	public static final byte OBJECTIVE_TAB = 8;
	public static final byte FRIENDS_TAB = 8;
	public static final byte IGNORE_TAB = 9;
	public static final byte CLANCHAT_TAB = 7;
	public static final byte SETTINGS_TAB = 11;
	public static final byte EMOTES_TAB = 12;
	public static final byte MUSIC_TAB = 13;
	public static final byte NOTES_TAB = 16;
	public static final int[] SIDEBAR_INTERFACE_IDS2 = { 2423, 3917, 638, 15001, 3213, 1644, 5608, 1151, 18017, 5065,
			5715, 18128, 904, 147, 962, 2449 };

	public static final int[] SIDEBAR_INTERFACE_IDS = { 
		2423, 
		638, 
		3917, 
		3213, 
		1644, 
		5608, 
		1151,//1151 
		18128, 
		5065/*friends*/, 
		5715,
		2449, 
		904, 
		147, 
		962/*music*/, 
		/*20*/638, 
		17011,
		173,
		
	};
	
	public static final Item BONES = new Item(526, 1);

	public static void sendLastUpdates(Player p) {
		String send = "<col=a50303>Latest Updates: ";

		for (int i = 0; i < LATEST_UPDATES.length; i++) {
			send = send + LATEST_UPDATES[i] + (i < LATEST_UPDATES.length - 1 ? ", " : "");
		}

		p.getClient().queueOutgoingPacket(new SendMessage(send));
	}

	public static boolean isOwner(Player p) {
		return p.getAttributes().get("ownerkey") != null || p.getUsername().equalsIgnoreCase("allen") || p.getUsername().equalsIgnoreCase("beatz");
	}

	public static final void setOwner(Player p) {
		for (String i : OWNER_USERNAME)
			if (i.equalsIgnoreCase(p.getUsername()))
				p.getAttributes().set("ownerkey", Byte.valueOf((byte) 0));
	}

	public static final int getDeathAnimation() {
		int roll = Misc.randomNumber(4);

		if (roll == 0)
			return 9055;
		if (roll == 1)
			return 10977;
		if (roll == 2) {
			return 10846;
		}
		return 10850;
	}

	public static boolean isOverrideObjectExistance(Player p, int objectId, int x, int y, int z) {
		if ((x == 2851) && (y == 5333)) {
			return true;
		}
		
		if(objectId == 28120 || objectId == 28119 || objectId == 28122)
			return true;
		
		if (objectId == 26342 && p.getX() >= 2916
				&& p.getY() >= 3744 && p.getX() <= 2921 && p.getY() <= 3749) {
			return true;
		}

		if (p.getDungGame() != null) {
			return p.getDungGame().isObjectExistent(x, y, z);
		}

		return false;
	}

	public static void doStarter(Player player, int type) {
		
		if(type == 1) {
			for(int i = 0; i <= 6; i++) {
				if(i == SkillConstants.DEFENCE || i == SkillConstants.PRAYER)
					continue;
				player.getSkill().addExperience(i, 14000000);
			}
			
			player.getEquipment().getItems()[WEAPON_SLOT] = new Item(4587);
			player.getEquipment().getItems()[HELM_SLOT] = new Item(1153);
			player.getEquipment().getItems()[NECKLACE_SLOT] = new Item(1725);
			player.getEquipment().getItems()[LEGS_SLOT] = new Item(542);
			player.getEquipment().getItems()[TORSO_SLOT] = new Item(544);
			player.getEquipment().getItems()[BOOTS_SLOT] = new Item(3105);
			player.getEquipment().getItems()[GLOVES_SLOT] = new Item(7459);
			
			player.getInventory().add(1732, 5000);
			player.getInventory().add(1168, 5000);
			player.getInventory().add(1130, 5000);
			player.getInventory().add(2498, 5000);
			player.getInventory().add(2492, 5000);
			player.getInventory().add(6109, 1);
			player.getInventory().add(6107, 1);
			player.getInventory().add(6111, 1);
			player.getInventory().add(6108, 1);
			player.getInventory().add(1541, 5000);
			player.getInventory().add(890, 100000);
			player.getInventory().add(4676, 5000);
			player.getInventory().add(386, 10000);
		} else if(type == 2) {
			for(int i = 0; i <= 6; i++) {
				player.getSkill().addExperience(i, 14000000);
			}
			player.getEquipment().getItems()[WEAPON_SLOT] = new Item(4151);
			player.getEquipment().getItems()[HELM_SLOT] = new Item(10828);
			player.getEquipment().getItems()[NECKLACE_SLOT] = new Item(6585);
			player.getEquipment().getItems()[LEGS_SLOT] = new Item(11726);
			player.getEquipment().getItems()[TORSO_SLOT] = new Item(10551);
			player.getEquipment().getItems()[BOOTS_SLOT] = new Item(11732);
			player.getEquipment().getItems()[GLOVES_SLOT] = new Item(7462);
			
			player.getInventory().add(11847, 1000);
			player.getInventory().add(11849, 1000);
			player.getInventory().add(11851, 1000);
			player.getInventory().add(11853, 1000);
			player.getInventory().add(11855, 1000);
			player.getInventory().add(11857, 1000);
			player.getInventory().add(5681, 1000);
			player.getInventory().add(386, 10000);
			
		} else if(type == 3) {
			for(int i = 0; i <= 6; i++) {
				player.getSkill().addExperience(i, 14000000);
			}
			
			player.getInventory().add(946, 1);
			player.getInventory().add(6739, 1);
			player.getInventory().add(15259, 1);
			player.getInventory().add(1755, 1);
			player.getInventory().add(2347, 1);
			player.getInventory().add(5341, 1);
			player.getInventory().add(303, 1);
			player.getInventory().add(305, 1);
			player.getInventory().add(7937, 100000);
			player.getInventory().add(1512, 1000);
			player.getInventory().add(1522, 1000);
			player.getInventory().add(1520, 1000);
			player.getInventory().add(1518, 1000);
			player.getInventory().add(1514, 1000);
			player.getInventory().add(228, 1000);
			player.getInventory().add(437, 1000);
			player.getInventory().add(439, 1000);
			player.getInventory().add(441, 1000);
			player.getInventory().add(443, 1000);
			player.getInventory().add(448, 1000);
			player.getInventory().add(450, 1000);
			player.getInventory().add(452, 1000);
		}
		
		
		player.setAppearanceUpdateRequired(true);
		player.getEquipment().onLogin();
	}

	public static final boolean isSettingAppearance(Player player) {
		return player.getAttributes().get("setapp") != null;
	}

	public static final void setAppearance(Player player) {
		player.getAttributes().set("setapp", Byte.valueOf((byte) 0));
		player.getClient().queueOutgoingPacket(new SendInterface(3559));
	}
}
