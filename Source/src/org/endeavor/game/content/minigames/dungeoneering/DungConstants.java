package org.endeavor.game.content.minigames.dungeoneering;

import java.util.ArrayList;

import org.endeavor.engine.utility.Misc;
import org.endeavor.game.GameConstants;
import org.endeavor.game.content.QuestTab;
import org.endeavor.game.content.minigames.dungeoneering.maps.DungBossA;
import org.endeavor.game.content.minigames.dungeoneering.maps.DungBossB;
import org.endeavor.game.content.minigames.dungeoneering.maps.DungBossC;
import org.endeavor.game.content.minigames.dungeoneering.maps.DungBossD;
import org.endeavor.game.content.minigames.dungeoneering.maps.DungBossE;
import org.endeavor.game.content.minigames.dungeoneering.maps.DungMapA;
import org.endeavor.game.content.minigames.dungeoneering.maps.DungMapB;
import org.endeavor.game.content.minigames.dungeoneering.maps.DungMapC;
import org.endeavor.game.content.minigames.dungeoneering.maps.DungMapD;
import org.endeavor.game.content.minigames.dungeoneering.maps.DungMapE;
import org.endeavor.game.content.minigames.dungeoneering.maps.DungMapF;
import org.endeavor.game.content.minigames.dungeoneering.maps.DungMapG;
import org.endeavor.game.content.minigames.dungeoneering.maps.DungMapH;
import org.endeavor.game.content.minigames.dungeoneering.maps.DungMapI;
import org.endeavor.game.content.minigames.dungeoneering.maps.DungMapJ;
import org.endeavor.game.content.minigames.dungeoneering.maps.DungMapK;
import org.endeavor.game.content.minigames.dungeoneering.maps.DungMapL;
import org.endeavor.game.content.minigames.dungeoneering.maps.DungMapM;
import org.endeavor.game.content.minigames.dungeoneering.maps.DungMapN;
import org.endeavor.game.content.minigames.dungeoneering.maps.DungMapO;
import org.endeavor.game.content.minigames.dungeoneering.maps.DungMapP;
import org.endeavor.game.content.minigames.dungeoneering.maps.DungMapQ;
import org.endeavor.game.content.minigames.dungeoneering.maps.DungMapR;
import org.endeavor.game.content.minigames.dungeoneering.maps.DungMapS;
import org.endeavor.game.content.minigames.dungeoneering.maps.DungMapT;
import org.endeavor.game.content.skill.fishing.Fishing;
import org.endeavor.game.content.skill.herblore.FinishedPotionData;
import org.endeavor.game.content.skill.prayer.BoneBurying;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.item.impl.GroundItem;
import org.endeavor.game.entity.item.impl.GroundItemHandler;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.mob.VirtualMobRegion;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.Controller;
import org.endeavor.game.entity.player.net.out.impl.SendString;
import org.endeavor.game.entity.player.net.out.impl.SendWalkableInterface;

public class DungConstants {
	public static final Controller DUNG_CONTROLLER = new DungController();
	public static final String DAMAGE_KEY = "dungeoneeringdamagekey";
	public static final String DUNG_ITEM_RETURN_KEY = "dungitemreturnkey";
	public static final String DUNG_FISH_EXPIRE_KEY = "dungfishexpirekey";
	public static final String DUNG_EXPIRE_TASK_KEY = "dungexpiretaskkey";
	public static final String LAST_MAP_KEY = "dunglastmap";
	public static final int MAX_PLAYERS = 6;
	public static final int FIRE_OBJECT_ID = 2732;
	public static final int ROCKTAIL_SPOT_ID = Fishing.FishingSpots.ROCKTAIL_SPOT.getId();
	public static final String CURRENT_DAMAGE = "Damage: ";
	public static final String CURRENT_WAVE = "Wave: ";
	public static final String MOBS_REMAINING = "Remaining: ";
	public static final String NEXT_GAME = "Next game: ";
	public static final String PLAYERS_WAITING = "Players ready: ";
	public static final String WAVE_NOT_FINISHED = "You must complete the wave before you move on.";
	public static final int PORTAL = 2465;
	public static final int CHEST = 2566;
	public static final int WAVE_BONUS_MODIFIER = 2;
	public static final int NEXT_GAME_TIMER_MAX = 50;
	public static final int WAVE_LEVEL_MODIFIER = 2;
	public static final Location END_GAME_LOCATION = new Location(1834, 5215);

	public static final FinishedPotionData[] POTION_DROPS = { FinishedPotionData.SUPER_ATTACK,
			FinishedPotionData.SUPER_DEFENCE, FinishedPotionData.SUPER_STRENGTH, FinishedPotionData.SUPER_RESTORE,
			FinishedPotionData.ANTIPOISON };

	public static final DungMap[] MAPS = { new DungMapA(), new DungMapB(), new DungMapC(), new DungMapD(),
			new DungMapE(), new DungMapF(), new DungMapG(), new DungMapH(), new DungMapI(), new DungMapJ(),
			new DungMapK(), new DungMapL(), new DungMapM(), new DungMapN(), new DungMapO(), new DungMapP(),
			new DungMapQ(), new DungMapR(), new DungMapS(), new DungMapT() };

	public static final DungMap[] BOSS_MAPS = { new DungBossA(), new DungBossB(), new DungBossC(), new DungBossD(),
			new DungBossE() };

	public static final int[] DUNG_MOBS = { 117, 82, 8125, 1615, 6275, 6213, 6368, 49, 73, 1643, 1459 };

	public static final int[][] CHEST_LOOT_TABLE = { { 385, 391, 15272, 149, 161, 167, 3030 },
			{ 16425, 16955, 17039, 17149, 17361, 17259, 16711, 16689, 16359, 16293, 16909, 16403, 17273 } };

	public static void sendInterface(Player p) {
		p.getClient().queueOutgoingPacket(new SendString("Damage: 0", 17601));
		p.getClient().queueOutgoingPacket(new SendString("Wave: 1", 17602));
		p.getClient().queueOutgoingPacket(new SendString("Remaining: 0", 17603));
		p.getClient().queueOutgoingPacket(new SendString("", 17604));
		p.getClient().queueOutgoingPacket(new SendString("", 17605));

		p.getClient().queueOutgoingPacket(new SendWalkableInterface(17600));
	}

	public static void spawn(DungGame game) {
		DungMap map = game.getCurrentMap();
		Location base = map.getPlayerSpawn();

		VirtualMobRegion r = new VirtualMobRegion(base.getX(), base.getY(), 1000);

		Location[] locs = map.getMobSpawnsForWave();

		if (locs == null) {
			return;
		}

		for (int i = 0; i < locs.length; i++) {
			Location l = new Location(locs[i]);
			l.setZ(game.getZ());

			game.add(new DungMob(DUNG_MOBS[Misc.randomNumber(DUNG_MOBS.length)], l, r, game));
		}
	}

	public static ArrayList<DungMap> getMaps() {
		ArrayList<DungMap> maps = new ArrayList<DungMap>();

		for (DungMap i : MAPS) {
			maps.add(i);
		}

		return maps;
	}

	public static void doDungPoints(Player p, int wave) {
		if (p.getAttributes().get("dungeoneeringdamagekey") != null) {
			int d = p.getAttributes().getInt("dungeoneeringdamagekey");
			int points = d / 350 + (wave != 0 ? wave / 2 : 0);
			p.addDungPoints(points * (GameConstants.IS_DOUBLE_EXP_WEEKEND ? 2 : 1));
			p.getAchievements().incr(p, "Achieve 1,000 Dung Points");
			p.getSkill().addExperience(24, d + wave * 500);
			QuestTab.updateDungPoints(p);

			p.getAttributes().remove("dungeoneeringdamagekey");
		}
	}

	public static void dropItemsForDungMob(Player p, Mob mob, boolean drop) {
		mob.getDungGame().remove(mob);

		if (p != null) {
			GroundItemHandler.add(new Item(BoneBurying.Bones.BIG_BONES.getId()), mob.getLocation(), p);

			if (drop)
				if (Misc.randomNumber(6) != 0) {
					GroundItem g = new GroundItem(new Item(
							CHEST_LOOT_TABLE[0][Misc.randomNumber(CHEST_LOOT_TABLE[0].length)]), mob.getLocation(), 80,
							p.getUsername());

					GroundItemHandler.add(g);
				} else {
					FinishedPotionData pDrop = POTION_DROPS[Misc.randomNumber(POTION_DROPS.length)];

					GroundItem groundItem1 = new GroundItem(new Item(pDrop.getItemNeeded()), mob.getLocation(), 80,
							p.getUsername());
					GroundItem groundItem2 = new GroundItem(new Item(pDrop.getUnfinishedPotion()), mob.getLocation(),
							80, p.getUsername());

					GroundItemHandler.add(groundItem1);
					GroundItemHandler.add(groundItem2);
				}
		}
	}

	public static int getRandomChestLoot(Player p) {
		int r = Misc.randomNumber(CHEST_LOOT_TABLE.length + 2);

		if (r >= CHEST_LOOT_TABLE.length) {
			int c = 0;
			int[] rewards = new int[CHEST_LOOT_TABLE[1].length];

			for (int i : CHEST_LOOT_TABLE[1]) {
				if ((!p.getEquipment().isWearingItem(i)) && (!p.getInventory().hasItemId(i))) {
					rewards[c] = i;
					c++;
				}
			}

			if (c == 0) {
				return CHEST_LOOT_TABLE[0][Misc.randomNumber(CHEST_LOOT_TABLE[0].length)];
			}

			return rewards[Misc.randomNumber(c)];
		}
		return CHEST_LOOT_TABLE[0][Misc.randomNumber(CHEST_LOOT_TABLE[0].length)];
	}
}
