package org.endeavor.game.entity.mob;

import java.security.SecureRandom;

import org.endeavor.GameSettings;
import org.endeavor.engine.definitions.ItemDropDefinition;
import org.endeavor.engine.definitions.ItemDropDefinition.ItemDrop;
import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.minigames.barrows.Barrows;
import org.endeavor.game.content.minigames.dungeoneering.DungConstants;
import org.endeavor.game.content.minigames.fightcave.TzharrGame;
import org.endeavor.game.content.minigames.godwars.GodWars;
import org.endeavor.game.content.minigames.warriorsguild.AnimatorConstants;
import org.endeavor.game.content.minigames.warriorsguild.ArmorAnimator;
import org.endeavor.game.content.minigames.warriorsguild.CyclopsRoom;
import org.endeavor.game.content.skill.farming.Plants;
import org.endeavor.game.content.skill.summoning.FamiliarMob;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.item.impl.GroundItemHandler;
import org.endeavor.game.entity.mob.impl.Revenant;
import org.endeavor.game.entity.mob.impl.SeaTrollQueen;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class MobDrops {
	private static final SecureRandom random = new SecureRandom();
	public static final int UNCOMMON_DROP_TABLE_CHANCE = 10;
	public static final int CLUE_SCROLL_CHANCE = 10;
	public static final int CHARM_CHANCE = 18;
	public static final int EXTRA_DROP_CHANCE = 4;
	public static final int HERB_CHANCE = 3;
	public static final int RARE_SHARED_TABLE_CHANCE = 3;
	public static final int EASY_CASKET_DROP_CHANCE = 3;
	public static final int HARD_CASKET_DROP_CHANCE = 1;
	public static final int EASY_CASKET_ID = 2714;
	public static final int HARD_CASKET_ID = 2724;
	public static final int KEY_HALF_CHANCE = 5;
	public static final int KEY_HALF_1 = 985;
	public static final int KEY_HALF_2 = 987;
	public static final int RARE_DROP_CHANCE = 1000;
	private static boolean rares = true;

	public static final int[] CHARMS = { 12158, 12159, 12160, 12163 };

	public static final int[] HERBS = { 199, 201, 203, 205, 207, 209, 212, 213, 215, 217, 219, 2485, 3049, 3051, 14836,
			12174 };

	public static void dropItems(Entity entity, Mob mob) {
		if (mob == null) {
			return;
		}

		if ((entity instanceof FamiliarMob)) {
			Mob m = World.getNpcs()[entity.getIndex()];

			if (m != null) {
				entity = m.getOwner();
			}
		}
		
		Location dropLocation = mob != null ? mob.getLocation() : null;
		
		if (dropLocation == null) {
			Exception e = new Exception("Mob is null?");
			e.printStackTrace();
			return;
		}
		
		if (mob instanceof SeaTrollQueen) {
			dropLocation = new Location(2344, 3699);
		}

		int level = mob.getDefinition().getLevel();

		ItemDropDefinition drops = GameDefinitionLoader.getItemDropDefinition(mob.getId());

		int ucRoll = 10;

		int ceRoll = 3;
		int chRoll = 1;
		int rtMod = 0;

		boolean drop = true;
		int amount = 1;

		if (mob.getMaxLevels()[3] == 0) {
			drop = false;
		}

		Player p = null;
		if ((entity != null) && (!entity.isNpc()) && ((p = World.getPlayers()[entity.getIndex()]) != null)) {
			if (Revenant.isRevenant(mob)) {
				World.sendGlobalMessage("<col=80000>" + p.getUsername() + " has killed a Revenant at level "
						+ p.getWildernessLevel() + ".", true);
			}

			if (mob != null) {
				p.getAchievements().onKillMob(p, mob);
			}

			if (mob.getDungGame() != null) {
				DungConstants.dropItemsForDungMob(p, mob, drop);
				return;
			}

			if (AnimatorConstants.isAnimatedArmour(mob.getId())) {
				ArmorAnimator.dropForAnimatedArmour(p, mob);
				return;
			}

			if (mob.getId() == 4291) {
				if (drop)
					CyclopsRoom.dropDefender(p, mob);
			} else {
				try {
					p.getSlayer().checkForSlayer(mob);
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					GodWars.onGodwarsKill(p, mob.getId());
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					Barrows.onBarrowsDeath(p, mob);
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					TzharrGame.checkForFightCave(p, mob);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			Item ring = p.getEquipment().getItems()[12];

			if ((ring != null) && (ring.getId() == 2572)) {
				ucRoll += 5;
				rtMod = 2;
				ceRoll += 3;
				chRoll++;
			}

			if (p.getController().equals(ControllerManager.DEFAULT_CONTROLLER)) {
				if (random.nextInt(100) <= 8) {
					if (Misc.randomNumber(100) < 50)
						GroundItemHandler.add(new Item(985, 1), dropLocation, p);
					else {
						GroundItemHandler.add(new Item(987, 1), dropLocation, p);
					}
				}

				if (random.nextInt(100) <= 5) {
					int seed = Plants.values()[Misc.randomNumber(Plants.values().length)].seed;
					GroundItemHandler.add(new Item(seed, Misc.randomNumber(3)), dropLocation, p);
				}

				if ((amount == 1) && (drop)) {
					if (random.nextInt(100) <= ceRoll)
						GroundItemHandler.add(new Item(2714, 1), dropLocation, p);
					else if (random.nextInt(100) <= chRoll) {
						GroundItemHandler.add(new Item(2724, 1), dropLocation, p);
					}
				}
			}
		} else if (AnimatorConstants.isAnimatedArmour(mob.getId())) {
			return;
		}

		if (mob.getDungGame() != null) {
			DungConstants.dropItemsForDungMob(null, mob, drop);
			return;
		}

		if (drops == null) {
			return;
		}

		if ((drops.getConstant() != null) && (drops.getConstant().getDrops() != null)) {
			dropConstants(entity, mob, drops.getConstant(), dropLocation);
		}

		if (!drop) {
			return;
		}

		dropHerbs(entity, mob, dropLocation);

		if ((drops.getCommon() == null) || (drops.getCommon().getDrops() == null)) {
			return;
		}

		boolean ucTable = random.nextInt(100) <= ucRoll;

		boolean hasCommon = (drops.getCommon() != null) && (drops.getCommon().getDrops() != null)
				&& (drops.getCommon().getDrops().length > 0);
		boolean hasUncommon = (drops.getUncommon() != null) && (drops.getUncommon().getDrops() != null)
				&& (drops.getUncommon().getDrops().length > 0);
		boolean hasRare = (drops.getRare() != null) && (drops.getRare().getDrops() != null)
				&& (drops.getRare().getDrops().length > 0);

		//if (random.nextInt(2) == 0) {
			if ((rares) && (hasRare) && (rollRareDrop(entity, mob, drops.getRare(), rtMod, dropLocation))) {
				return;
			}
		//}

		if (entity != null) {
			if (random.nextInt(100) <= 18) {
				int[] charm = new int[level / 20 == 0 ? 1 : level / 20 > 4 ? 4 : level / 20];
	
				for (int i = 0; i < charm.length; i++) {
					if (i >= CHARMS.length) {
						break;
					}
					charm[i] = CHARMS[i];
				}
	
				int charmDrop = charm[random.nextInt(charm.length)];
	
				if (charmDrop > 0) {
					GroundItemHandler.add(new Item(charmDrop, 1), dropLocation, entity.isNpc() ? null : World.getPlayers()[entity.getIndex()]);
				}
			}
		} else {
		}

		for (int i = 0; i < amount; i++) {
			if ((hasUncommon) && (ucTable))
				drop(entity, mob, drops.getUncommon(), dropLocation);
			else if (hasCommon)
				drop(entity, mob, drops.getCommon(), dropLocation);
		}
		
		/*else if ((level >= 50) && (random.nextInt(100) <= 3))
		GroundItemHandler.add(RareDropTable.getDrop(), dropLocation, p);*/
	}

	public static boolean rollRareDrop(Entity e, Mob mob, ItemDropDefinition.ItemDropTable table, int mod, Location dropLocation) {
		ItemDropDefinition.ItemDrop rare = table.getDrops()[random.nextInt(table.getDrops().length)];

		Player p = null;
		if ((e != null) && (!e.isNpc())) {
			p = World.getPlayers()[e.getIndex()];
		}
		
		//for (ItemDrop rare : table.getDrops()) {
			if (rare != null) {
				byte chance = GameDefinitionLoader.getRareDropChance(rare.getId());
				int roll = random.nextInt(1000 - (p != null && chance < 50 ? p.getRareDropEP().getEpAddon() : 0));
				
				if (GameSettings.DEV_MODE && p != null) {
					p.send(new SendMessage("Roll: " + roll + " / rare = " + (chance + mod)));
				}
				
				if (roll >= 500 && roll <= 500 + chance + mod) {
					
					int am = rare.getMin() < rare.getMax() ? rare.getMin() + random.nextInt(rare.getMax() - rare.getMin()) : rare.getMin();
	
					if (p != null) {
						if (chance < 80 || p.getRareDropEP().getReceived() >= 4) {
							p.getRareDropEP().reset();
						}
						
						p.getRareDropEP().addReceived();
						
						if (!p.getController().equals(ControllerManager.WILDERNESS_CONTROLLER) && (chance < 35)) {
							String send = "<col=255>" + p.getUsername() + " has acquired x" + am + " "
									+ Item.getDefinition(rare.getId()).getName() + " from a "
									+ mob.getDefinition().getName() + ".</col>";
							World.sendGlobalMessage(send, false);
						}
					}
	
					Item drop = new Item(rare.getId(), am);
	
					if (p != null) {
						p.getAchievements().incr(p, "Obtain 50 rare drops");
						p.getAchievements().incr(p, "Obtain 250 rare drops");
					}
	
					GroundItemHandler.add(drop, dropLocation, p);
					return true;
				}
			}
		//}

		return false;
	}

	public static int getLevel(Mob mob, int chance) {
		int lvl = mob.getDefinition().getLevel();

		if ((lvl / 6 > chance) || (lvl / 6 == 0)) {
			return chance;
		}

		return lvl / 6;
	}

	public static void drop(Entity entity, Mob mob, ItemDropDefinition.ItemDropTable table, Location dropLocation) {
		// table.getScrolls();

		if ((table != null) && (table.getDrops() != null)) {
			ItemDropDefinition.ItemDrop drop = table.getDrops()[random.nextInt(table.getDrops().length)];

			if (drop == null) {
				return;
			}

			int id = drop.getId();

			for (int i = 0; i < HERBS.length; i++) {
				if (HERBS[i] == id) {
					id = Item.getDefinition(id).getNoteId();
					break;
				}
			}

			Item item = new Item(drop.getId(), calculateAmount(drop));

			GroundItemHandler.add(item, dropLocation,
					(entity == null) || (entity.isNpc()) ? null : World.getPlayers()[entity.getIndex()]);
		}
	}

	public static void dropHerbs(Entity entity, Mob mob, Location dropLocation) {
		if (random.nextInt(3) == 0) {
			int r = HERBS[random.nextInt(HERBS.length)];

			GroundItemHandler.add(new Item(Item.getDefinition(r).getNoteId(), 1), dropLocation, (entity == null)
					|| (entity.isNpc()) ? null : World.getPlayers()[entity.getIndex()]);
		}
	}

	public static void dropConstants(Entity entity, Mob mob, ItemDropDefinition.ItemDropTable constants, Location dropLocation) {
		if ((constants == null) || (constants.getDrops() == null)) {
			return;
		}

		for (ItemDropDefinition.ItemDrop i : constants.getDrops()) {
			Item item = new Item(i.getId(), calculateAmount(i));
			GroundItemHandler.add(item, dropLocation,
					(entity == null) || (entity.isNpc()) ? null : World.getPlayers()[entity.getIndex()]);
		}
	}

	public static int calculateAmount(ItemDropDefinition.ItemDrop drop) {
		if (drop.getMax() <= drop.getMin()) {
			return drop.getMin();
		}

		if (drop.getMin() + random.nextInt(drop.getMax() - drop.getMin()) < 1) {
			return 1;
		}

		return drop.getMin() + random.nextInt(drop.getMax() - drop.getMin());
	}

	public static void setRares(boolean set) {
		rares = set;
	}
}
