package org.endeavor;

import org.endeavor.engine.cache.inter.RSInterface;
import org.endeavor.engine.cache.map.MapLoading;
import org.endeavor.engine.cache.map.ObjectDef;
import org.endeavor.engine.cache.map.Region;
import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.game.content.Emotes;
import org.endeavor.game.content.clans.Clans;
import org.endeavor.game.content.combat.impl.EarningPotential;
import org.endeavor.game.content.combat.impl.PoisonWeapons;
import org.endeavor.game.content.combat.spawning.Spawn;
import org.endeavor.game.content.combat.special.SpecialAttackHandler;
import org.endeavor.game.content.dialogue.OneLineDialogue;
import org.endeavor.game.content.io.BetaBank;
import org.endeavor.game.content.lottery.Lottery;
import org.endeavor.game.content.minigames.duelarena.DuelingConstants;
import org.endeavor.game.content.minigames.godwars.GodWarsNpcData;
import org.endeavor.game.content.pets.PetData;
import org.endeavor.game.content.quest.QuestConstants;
import org.endeavor.game.content.randoms.RandomEvent;
import org.endeavor.game.content.shopping.Shop;
import org.endeavor.game.content.skill.Skill;
import org.endeavor.game.content.skill.SkillConstants;
import org.endeavor.game.content.skill.SkillingGeneralPrices;
import org.endeavor.game.content.skill.cooking.CookingData;
import org.endeavor.game.content.skill.crafting.Craftable;
import org.endeavor.game.content.skill.crafting.Gem;
import org.endeavor.game.content.skill.crafting.Glass;
import org.endeavor.game.content.skill.crafting.Hide;
import org.endeavor.game.content.skill.crafting.Jewelry;
import org.endeavor.game.content.skill.crafting.Spinnable;
import org.endeavor.game.content.skill.firemaking.LogData;
import org.endeavor.game.content.skill.fishing.FishableData;
import org.endeavor.game.content.skill.fishing.Fishing;
import org.endeavor.game.content.skill.fishing.ToolData;
import org.endeavor.game.content.skill.fletching.BoltTipData;
import org.endeavor.game.content.skill.fletching.FinishedArrowData;
import org.endeavor.game.content.skill.fletching.FinishedBoltData;
import org.endeavor.game.content.skill.fletching.FletchLogData;
import org.endeavor.game.content.skill.fletching.StringBowData;
import org.endeavor.game.content.skill.herblore.FinishedPotionData;
import org.endeavor.game.content.skill.herblore.GrimyHerbData;
import org.endeavor.game.content.skill.herblore.GrindingData;
import org.endeavor.game.content.skill.herblore.UnfinishedPotionData;
import org.endeavor.game.content.skill.hunter.Hunter;
import org.endeavor.game.content.skill.magic.MagicConstants;
import org.endeavor.game.content.skill.magic.MagicEffects;
import org.endeavor.game.content.skill.mining.MiningPickAxeData;
import org.endeavor.game.content.skill.mining.MiningRockData;
import org.endeavor.game.content.skill.prayer.BoneBurying;
import org.endeavor.game.content.skill.prayer.PrayerConstants;
import org.endeavor.game.content.skill.prayer.impl.CursesPrayerBook;
import org.endeavor.game.content.skill.ranged.AmmoData;
import org.endeavor.game.content.skill.runecrafting.RunecraftingData;
import org.endeavor.game.content.skill.slayer.SlayerMonsters;
import org.endeavor.game.content.skill.smithing.SmeltingData;
import org.endeavor.game.content.skill.summoning.SummoningConstants;
import org.endeavor.game.content.skill.thieving.ThievingNpcData;
import org.endeavor.game.content.skill.thieving.ThievingStallData;
import org.endeavor.game.content.skill.thieving.ThievingStallTask;
import org.endeavor.game.content.skill.woodcutting.WoodcuttingAxeData;
import org.endeavor.game.content.skill.woodcutting.WoodcuttingTreeData;
import org.endeavor.game.content.sounds.MobSounds;
import org.endeavor.game.content.sounds.PlayerSounds;
import org.endeavor.game.entity.item.impl.GlobalItemHandler;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.mob.MobAbilities;
import org.endeavor.game.entity.mob.MobConstants;
import org.endeavor.game.entity.object.ObjectConstants;
import org.endeavor.game.entity.object.ObjectManager;
import org.endeavor.game.entity.player.Achievements;
import org.endeavor.game.entity.player.EquipmentConstants;
import org.endeavor.game.entity.player.ItemDegrading;
import org.endeavor.game.entity.player.net.in.PacketHandler;
import org.endeavor.game.entity.player.net.in.command.CommandManager;

/**
 * Loads all of the neccessary game data
 * 
 * @author Michael Sasse
 * 
 */
public class GameDataLoader {

	/**
	 * The stage of the game server
	 */
	private static int stage = 0;

	/**
	 * Gets if the server has been successfully loaded
	 * 
	 * @return
	 */
	public static boolean loaded() {
		return stage == 2;
	}

	/**
	 * Loads all of the game data
	 */
	public static void load() {
		Achievements.declare();
		try {
			GameDefinitionLoader.declare();

			new Thread() {
				@Override
				public void run() {
					try {
						ObjectDef.loadConfig();
						ObjectConstants.declare();

						MapLoading.load();
						Region.sort();
						GameDefinitionLoader.loadAlternateIds();
						MapLoading.processDoors();
						GameDefinitionLoader.clearAlternates();
						ObjectManager.declare();

						Hunter.spawnHunterMobs();
						Mob.spawnBosses();
						GameDefinitionLoader.loadNpcSpawns();
						Lottery.init();
						GlobalItemHandler.spawnGroundItems();
					} catch (Exception e) {
						e.printStackTrace();
					}

					GameDataLoader.stage += 1;
				}
			}.start();

			GameDefinitionLoader.loadItemValues();

			RSInterface.unpack();
			RSInterface.plotSongList();

			GameDefinitionLoader.loadNpcDefinitions();

			GameDefinitionLoader.loadItemDefinitions();
			
			GameDefinitionLoader.loadRareDropChances();
			
			PetData.setItemValues();
			ItemDegrading.declare();
			ThievingStallTask.declare();

			GameDefinitionLoader.loadEquipmentDefinitions();
			GameDefinitionLoader.loadShopDefinitions();
			GameDefinitionLoader.setRequirements();

			GameDefinitionLoader.loadWeaponDefinitions();
			GameDefinitionLoader.loadSpecialAttackDefinitions();
			GameDefinitionLoader.loadRangedStrengthDefinitions();
			GameDefinitionLoader.loadSpecialAttackDefinitions();
			GameDefinitionLoader.loadCombatSpellDefinitions();
			GameDefinitionLoader.loadFoodDefinitions();
			GameDefinitionLoader.loadPotionDefinitions();
			GameDefinitionLoader.loadRangedWeaponDefinitions();

			GameDefinitionLoader.loadNpcCombatDefinitions();
			GameDefinitionLoader.loadNpcDropDefinitions();
			GameDefinitionLoader.loadItemBonusDefinitions();

			//GameDefinitionLoader.writeDropPreference();
			
			FletchLogData.declare();
			StringBowData.declare();
			FinishedArrowData.declare();
			FinishedBoltData.declare();

			SummoningConstants.declare();
			Shop.declare();
			BoltTipData.declare();
			RandomEvent.declare();
			MagicConstants.declare();
			QuestConstants.declare();
			SlayerMonsters.declare();
			MobSounds.declare();
			PlayerSounds.declare();
			BetaBank.declare();
			DuelingConstants.declare();
			MobConstants.declare();
			Skill.declare();
			Emotes.declare();
			PetData.declare();
			PoisonWeapons.declare();
			SpecialAttackHandler.declare();
			GodWarsNpcData.GodWarsNpcs.declare();
			CookingData.declare();
			Gem.declare();
			Glass.declare();
			Hide.declare();
			Jewelry.declare();
			Spinnable.declare();
			LogData.declare();
			FishableData.Fishable.declare();
			Fishing.FishingSpots.declare();
			ToolData.Tools.declare();
			FinishedPotionData.declare();
			GrimyHerbData.declare();
			GrindingData.declare();
			UnfinishedPotionData.declare();
			MagicEffects.declare();
			MiningPickAxeData.declare();
			MiningRockData.declare();
			BoneBurying.Bones.declare();
			PrayerConstants.declare();
			CursesPrayerBook.declare();
			AmmoData.Ammo.declare();
			RunecraftingData.declare();
			SkillConstants.declare();
			ThievingNpcData.declare();
			ThievingStallData.declare();
			WoodcuttingAxeData.declare();
			WoodcuttingTreeData.declare();
			PlayerSounds.declare();
			EquipmentConstants.declare();
			CommandManager.declare();
			PacketHandler.declare();
			Craftable.declare();
			MobConstants.MobDissapearDelay.declare();
			MobAbilities.declare();
			SmeltingData.declare();
			EarningPotential.StatueData.declare();
			OneLineDialogue.declare();
			
			SkillingGeneralPrices.declare();
			
			Clans.loadClans();
			
			Spawn.declare();

			stage += 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
