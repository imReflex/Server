package org.endeavor.game.content.minigames.dungeoneering;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.endeavor.GameSettings;
import org.endeavor.engine.task.RunOnceTask;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.task.impl.forcewalk.JumpObjectTask;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.content.combat.Hit;
import org.endeavor.game.content.skill.fishing.Fishing;
import org.endeavor.game.content.skill.fishing.FishableData.Fishable;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.object.GameObject;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerConstants;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendString;

public class DungGame {
	private final Player[] team;
	private final Mob[] monsters = new Mob[40];
	private final Mob[] otherMobs = new Mob[4];

	private final GameObject[] objects = new GameObject[15];

	private final Queue<DungMap> maps = new LinkedList<DungMap>();

	private DungMap map = null;

	private short z = 0;
	private byte wave = 0;
	private byte inNextWave = 0;

	private boolean boss = false;
	private CombatTypes weakness;

	public DungGame(List<Player> players) {
		team = new Player[players.size()];

		List<?> m = DungConstants.getMaps();

		while (m.size() > 0) {
			int r = Misc.randomNumber(m.size());

			maps.add((DungMap) m.get(r));
			m.remove(r);
		}

		for (int i = 0; i < players.size(); i++) {
			Player p = players.get(i);

			if (p != null) {
				team[i] = p;
			} else {
				Exception e = new IllegalArgumentException("Null player team adding attempt (DungGame)!");
				e.printStackTrace();
			}

		}

		init();
	}

	public void cleanup() {
		for (int i = 0; i < monsters.length; i++) {
			if (monsters[i] != null) {
				monsters[i].remove();
				monsters[i] = null;
			}
		}

		for (int i = 0; i < otherMobs.length; i++) {
			if (otherMobs[i] != null) {
				otherMobs[i].remove();
				otherMobs[i] = null;
			}
		}

		for (int i = 0; i < objects.length; i++) {
			if (objects[i] != null) {
				objects[i] = null;
			}
		}

		inNextWave = 0;
	}

	public Item[] createItemReturnArrayFrom(Item[] array) {
		Item[] ret = new Item[array.length];

		for (int i = 0; i < ret.length; i++) {
			if (array[i] != null) {
				ret[i] = new Item(array[i]);
			}
		}

		return ret;
	}

	private void init() {
		for (int i = 0; i < team.length; i++)
			if (team[i] != null) {
				team[i].setDungGame(this);
				team[i].setController(DungConstants.DUNG_CONTROLLER);

				team[i].getAttributes().set(
						"dungitemreturnkey",
						new Item[][] { createItemReturnArrayFrom(team[i].getInventory().getItems()),
								createItemReturnArrayFrom(team[i].getEquipment().getItems()) });

				DungConstants.sendInterface(team[i]);

				doNextMapTeleport(team[i]);

				team[i].getAttributes().set("dungeoneeringdamagekey", Integer.valueOf(0));

				team[i].getInventory().addOrCreateGroundItem(385, 10, true);

				team[i].getSkill().restore();

				team[i].getSpecialAttack().setSpecialAmount(100);
				team[i].getSpecialAttack().update();
			}
	}

	private void setNextMap() {
		wave = ((byte) (wave + 1));

		if (maps.size() == 0) {
			if (!boss) {
				map = DungConstants.BOSS_MAPS[(DungConstants.BOSS_MAPS.length - 1)];
				boss = true;
			} else {
				map = null;
			}
		} else if (wave % 5 == 0) {
			map = DungConstants.BOSS_MAPS[(wave / 5 - 1)];
		} else {
			map = (maps.poll());
		}

		if (map != null) {
			z = ((short) (getFirstPlayer().getIndex() * 4 + map.getPlayerSpawn().getZ()));
		}
	}

	public void doNextMapTeleport(Player p) {
		if (map != null) {
			p.getAttributes().set(DungConstants.LAST_MAP_KEY, map);
		}
		
		if (inNextWave == 0) {
			setNextMap();

			weakness = CombatTypes.values()[Misc.randomNumber(CombatTypes.values().length)];

			if (map == null) {
				for (int i = 0; i < team.length; i++) {
					if (team[i] != null) {
						remove(team[i]);
					}
				}

				return;
			}
		}
		
		Location l = new Location(map.getPlayerSpawn());
		l.setZ(z);

		p.teleport(l);
		p.getClient().queueOutgoingPacket(new SendString("Wave: " + wave, 17602));

		p.getClient().queueOutgoingPacket(
				new SendString("Weakness: "
						+ (weakness == CombatTypes.RANGED ? "Ranged" : weakness == CombatTypes.MAGIC ? "Magic"
								: "Melee"), 17604));

		if ((this.inNextWave = (byte) (inNextWave + 1)) == getTeamSize()) {
			cleanup();
			map.init(this);
			DungConstants.spawn(this);

			for (int i = 0; i < team.length; i++) {
				if (team[i] != null) {
					for (GameObject o : objects) {
						if (o != null) {
							team[i].getObjects().add(o);
						}
					}
					team[i].getClient().queueOutgoingPacket(
							new SendString("Remaining: " + getMonstersRemaining(), 17603));
				}
			}
		}

		if (isBossWave()) {
			p.getInventory().addOrCreateGroundItem(385, 5, true);
			p.getRunEnergy().setEnergy(100.0D);

			if (p.isPoisoned()) {
				p.curePoison(0);
			}

			p.getSpecialAttack().setSpecialAmount(100);
		}

		p.getAttributes().remove("dungexpiretaskkey");
		p.getAttributes().remove("dungfishexpirekey");
	}

	public void remove(Player p) {
		if (p == null) {
			Exception e = new IllegalArgumentException("Player is null and cannot be removed (DungGame)!");
			e.printStackTrace();
		}

		for (int i = 0; i < team.length; i++) {
			if ((team[i] != null) && (team[i].equals(p))) {
				if (team[i].getTrade().trading()) {
					team[i].getTrade().end(false);
				}

				Item[][] items = (Item[][]) team[i].getAttributes().get("dungitemreturnkey");
				team[i].getAttributes().remove("dungitemreturnkey");

				if (items != null) {
					team[i].getInventory().setItems(items[0]);
					team[i].getEquipment().setItems(items[1]);
				} else {
					team[i].getInventory().clear();
					team[i].getEquipment().clear();
					Exception e = new IllegalArgumentException("Player does not have an item return key! (DungGame)!");
					e.printStackTrace();
				}

				p.getSkill().restore();
				p.getRunEnergy().setEnergy(100.0D);

				if (p.isPoisoned()) {
					p.curePoison(0);
				}

				team[i].teleport(DungConstants.END_GAME_LOCATION);
				team[i].setController(ControllerManager.DEFAULT_CONTROLLER);

				DungConstants.doDungPoints(team[i], wave);

				team[i].setDungGame(null);

				team[i].getAttributes().remove("dungexpiretaskkey");
				team[i].getAttributes().remove("dungfishexpirekey");
				team[i].getAttributes().remove("dungeoneeringdamagekey");
				team[i].getAttributes().remove(DungConstants.LAST_MAP_KEY);

				if ((wave == 26) || ((boss) && (map == null))) {
					team[i].setDungPoints(team[i].getDungPoints() + 25 + (p.isRespectedMember() ? 15 : 0));
					team[i].getSkill().addExperience(24, 8000.0D);

					p.getClient().queueOutgoingPacket(
							new SendMessage("Congratulations! You have completed Dungeoneering!"));
				}

				team[i].getAchievements().incr(team[i], "Complete 100 Dung waves", wave);

				team[i] = null;

				if (getTeamSize() == 0) {
					cleanup();
				}
				return;
			}
		}

		Exception e = new IllegalArgumentException("Player could not be found (DungGame)!");
		e.printStackTrace();
	}

	public Player getFirstPlayer() {
		for (int i = 0; i < team.length; i++) {
			if (team[i] != null) {
				return team[i];
			}
		}

		Exception e = new IllegalArgumentException("Could retrieve a player (DungGame)!");
		e.printStackTrace();

		return null;
	}

	public byte getTeamSize() {
		byte a = 0;

		for (int i = 0; i < team.length; i++) {
			if (team[i] != null) {
				a = (byte) (a + 1);
			}
		}

		return a;
	}

	public void add(Mob mob) {
		if (mob == null) {
			Exception e = new IllegalArgumentException("Monster to remove is null2 (DungGame)!");
			e.printStackTrace();
		}

		for (int i = 0; i < monsters.length; i++) {
			if (monsters[i] == null) {
				monsters[i] = mob;
				return;
			}
		}

		Exception e = new IllegalArgumentException("Monster could not be added (DungGame)!");
		e.printStackTrace();
	}

	public void addNonCombatMob(Mob mob) {
		if (mob == null) {
			Exception e = new IllegalArgumentException("Non-combat mob is null and cannot be added (DungGame)!");
			e.printStackTrace();
		}

		for (int i = 0; i < otherMobs.length; i++) {
			if (otherMobs[i] == null) {
				otherMobs[i] = mob;
				return;
			}
		}

		Exception e = new IllegalArgumentException("Non-combat mob could not be added (DungGame)!");
		e.printStackTrace();
	}

	public void remove(Mob mob) {
		if (mob == null) {
			Exception e = new IllegalArgumentException("Monster to remove is null (DungGame)!");
			e.printStackTrace();
		}

		for (int i = 0; i < monsters.length; i++) {
			if ((monsters[i] != null) && (monsters[i].equals(mob))) {
				monsters[i] = null;

				for (int k = 0; k < team.length; k++) {
					if (team[k] != null) {
						team[k].getClient().queueOutgoingPacket(
								new SendString("Remaining: " + getMonstersRemaining(), 17603));
					}
				}
				return;
			}
		}

		Exception e = new IllegalArgumentException("Monster could not be found (DungGame)!");
		e.printStackTrace();
	}

	public void spawn(GameObject object) {
		if (object == null) {
			Exception e = new IllegalArgumentException("Object is null and cannot be spawned (DungGame)!");
			e.printStackTrace();
		}

		for (int i = 0; i < objects.length; i++) {
			if (objects[i] == null) {
				objects[i] = object;
				return;
			}
		}

		Exception e = new IllegalArgumentException("Object could not be spawned (DungGame)!");
		e.printStackTrace();
	}

	public void spawnObjects(Player p) {
		for (int i = 0; i < objects.length; i++)
			if (objects[i] != null)
				p.getObjects().add(objects[i]);
	}

	public GameObject getObject(GameObject object) {
		for (int i = 0; i < objects.length; i++) {
			if ((objects[i] != null) && (objects[i].getLocation().equals(object.getLocation()))) {
				return objects[i];
			}
		}

		return null;
	}

	public boolean isObjectExistent(int x, int y, int z) {
		Location l = new Location(x, y, z);

		for (int i = 0; i < objects.length; i++) {
			if ((objects[i] != null) && (objects[i].getLocation().equals(l))) {
				return true;
			}
		}

		return false;
	}

	public static void forHit(Player p, int hit) {
		if (p.getAttributes().get("dungeoneeringdamagekey") == null) {
			p.getAttributes().set("dungeoneeringdamagekey", Integer.valueOf(0));
		}
		int dmg = p.getAttributes().getInt("dungeoneeringdamagekey") + hit;

		p.getAttributes().set("dungeoneeringdamagekey", Integer.valueOf(dmg));
		p.getClient().queueOutgoingPacket(new SendString("Damage: " + dmg, 17601));
	}

	public boolean isWaveFinished() {
		for (Mob i : monsters) {
			if (i != null) {
				return false;
			}
		}

		return true;
	}

	public byte getMonstersRemaining() {
		byte k = 0;

		for (Mob i : monsters) {
			if (i != null) {
				k = (byte) (k + 1);
			}
		}

		return k;
	}

	public boolean isBossWave() {
		return (maps.size() == 0) || (wave % 5 == 0);
	}

	public DungMap getCurrentMap() {
		return map;
	}

	public byte getWave() {
		return wave;
	}

	public int getZ() {
		return z;
	}

	public boolean clickMob(final Player p, Mob mob, int id, int option) {
		if (id == Fishing.FishingSpots.ROCKTAIL_SPOT.getId()) {
			if ((Fishing.canFish(p, Fishable.ROCKTAIL, false)) || (Fishing.canFish(p, Fishable.LOBSTER, false))) {
				p.getClient().queueOutgoingPacket(
						new SendMessage("You either need the required level or the tool to fish here."));
				return true;
			}

			if (p.getAttributes().get("dungfishexpirekey") != null) {
				p.getClient().queueOutgoingPacket(new SendMessage("This fishing spot looks lifeless.."));
			} else {
				p.getFishing().clickNpc(mob, id, option);

				if (p.getAttributes().get("dungexpiretaskkey") == null) {
					RunOnceTask t = new RunOnceTask(p, 30) {
						@Override
						public void onStop() {
							p.getUpdateFlags().sendAnimation(65535, 0);
							p.getClient().queueOutgoingPacket(new SendMessage("Looks like this spot is empty now.."));
							p.getAttributes().set("dungfishexpirekey", Byte.valueOf((byte) 0));
							TaskQueue.onMovement(p);
						}
					};
					p.getAttributes().set("dungexpiretaskkey", t);

					TaskQueue.queue(t);
				}
			}

			return true;
		}

		return false;
	}

	public boolean clickObject(final Player p, int option, int id, int x, int y, int z) {
		switch (id) {
		case 32015:
			DungMap last = p.getAttributes().get(DungConstants.LAST_MAP_KEY) != null ? ((DungMap) p.getAttributes().get(DungConstants.LAST_MAP_KEY)) : null;
			
			if (!map.equals(DungConstants.MAPS[1]) && (last != null ? last.equals(DungConstants.MAPS[1]) : true)) {
				return true;
			}

			if (p.getLocation().getX() <= 2570)
				p.teleport(new Location(2573, 9631, this.z));
			else {
				p.teleport(new Location(2569, 9628, this.z));
			}
			return true;
		case 41006:
			TaskQueue.queue(new JumpObjectTask(p, p.getLocation().getY() <= 9945 ? new Location(3024, 9951, z)
					: new Location(3024, 9944, this.z)));
			return true;
		case 2566:
			GameObject o = getObject(new GameObject(x, y, z));

			if (o == null) {
				return false;
			}

			final DungChest chest = (DungChest) o;

			if (!chest.canSearch(p)) {
				p.getClient().queueOutgoingPacket(new SendMessage("You have already looted this chest!"));
				return true;
			}

			if (option == 1) {
				chest.setSearchedBy(p);
				p.getUpdateFlags().sendAnimation(832, 0);

				TaskQueue.queue(new RunOnceTask(p, 1) {
					@Override
					public void onStop() {
						if (!chest.isTrap()) {
							int r = DungConstants.getRandomChestLoot(p);
							String name = Item.getDefinition(r).getName();

							p.getInventory().addOrCreateGroundItem(r, 1, true);

							p.getClient().queueOutgoingPacket(
									new SendMessage("You receive " + Misc.getAOrAn(name) + " " + name
											+ " from the chest."));
						} else {
							p.getClient().queueOutgoingPacket(new SendMessage("The chest was a trap!"));
							p.hit(new Hit(Misc.randomNumber(8)));
							p.poison(6);
						}
					}
				});
			} else if (p.getMaxLevels()[17] >= 85) {
				if (!chest.canCheck(p)) {
					p.getClient().queueOutgoingPacket(
							new SendMessage("You have already checked this chest for traps. It "
									+ (!chest.isTrap() ? "does not contain" : "contains") + " a trap."));
					return true;
				}

				chest.setCheckedBy(p);
				p.getClient().queueOutgoingPacket(new SendMessage("You search the chest for traps.."));
				p.getUpdateFlags().sendAnimation(832, 0);

				TaskQueue.queue(new RunOnceTask(p, 1) {
					@Override
					public void onStop() {
						if (chest.isTrap()) {
							p.getClient().queueOutgoingPacket(new SendMessage("This chest contains a trap."));
							chest.setSearchedBy(p);
						} else {
							p.getClient().queueOutgoingPacket(new SendMessage("You did not find any traps."));
						}

						p.getSkill().addExperience(17, 150.0D);
					}
				});
			} else {
				p.getClient().queueOutgoingPacket(new SendMessage("You need 85 Thieving to search for traps."));
			}

			return true;
		case 2465:
			if ((isWaveFinished()) || ((PlayerConstants.isOwner(p)) && (GameSettings.DEV_MODE)))
				doNextMapTeleport(p);
			else {
				p.getClient().queueOutgoingPacket(new SendMessage("You must complete the wave before you move on."));
			}

			return true;
		}
		return false;
	}

	public CombatTypes getFloorWeakness() {
		return weakness;
	}
}
