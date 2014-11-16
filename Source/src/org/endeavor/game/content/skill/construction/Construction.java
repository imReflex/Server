package org.endeavor.game.content.skill.construction;

import java.util.ArrayList;
import java.util.Iterator;

import org.endeavor.Server;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.dialogue.DialogueManager;
//import org.endeavor.game.content.skill.construction.ConstructionData.Butlers;
//import org.endeavor.game.content.skill.construction.ConstructionData.Furniture;
//import org.endeavor.game.content.skill.construction.ConstructionData.HotSpots;
//import org.endeavor.game.content.skill.construction.ConstructionData.RoomData;
//import org.endeavor.game.content.skill.construction.Palette.PaletteTile;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;

/**
 * 
 * @author Owner Blade
 *
 */
public class Construction {
//construction removed
	/*public static void createPalette(Player p) {
		Palette palette = new Palette();
		for (int z = 0; z < 4; z++) {
			for (int x = 0; x < 13; x++) {
				for (int y = 0; y < 13; y++) {
					if (p.houseRooms[z][x][y] == null)
						continue;
					if (p.houseRooms[z][x][y].getX() == 0)
						continue;
					PaletteTile tile = new PaletteTile(
							p.houseRooms[z][x][y].getX(),
							p.houseRooms[z][x][y].getY(),
							p.houseRooms[z][x][y].getZ(),
							p.houseRooms[z][x][y].getRotation());
					palette.setTile(x, y, z, tile);
				}
			}
		}
		House mapInstance = new House(2544, 3094, true);
		mapInstance.addMember(p);
		mapInstance.setOwner(p);
		mapInstance.setPalette(palette);
		createDungeonPalette(p);
		enterHouse(p, p, p.inBuildingMode);
		placeMobs(p);
	}

	public static void createDungeonPalette(Player p) {
		Palette palette = new Palette();
		for (int x = 0; x < 13; x++) {
			for (int y = 0; y < 13; y++) {
				PaletteTile tile = null;
				if (p.houseRooms[4][x][y] == null) {
					tile = new PaletteTile(
							ConstructionData.RoomData.DUNGEON_EMPTY.getX(),
							ConstructionData.RoomData.DUNGEON_EMPTY.getY(), 0,
							0);
				} else {
					tile = new PaletteTile(p.houseRooms[4][x][y].getX(),
							p.houseRooms[4][x][y].getY(),
							p.houseRooms[4][x][y].getZ(),
							p.houseRooms[4][x][y].getRotation());
				}
				palette.setTile(x, y, 0, tile);
			}
		}

		p.getMapInstance().setSecondaryPalette(palette);
	}

	public static void updatePalette(Player p) {
		Palette palette = p.getMapInstance().getPalette();
		for (int z = 0; z < 4; z++) {
			for (int x = 0; x < 13; x++) {
				for (int y = 0; y < 13; y++) {
					if (p.houseRooms[z][x][y] == null)
						continue;
					if (p.houseRooms[z][x][y].getX() == 0)
						continue;
					PaletteTile tile = new PaletteTile(
							p.houseRooms[z][x][y].getX(),
							p.houseRooms[z][x][y].getY(),
							p.houseRooms[z][x][y].getZ(),
							p.houseRooms[z][x][y].getRotation());
					palette.setTile(x, y, z, tile);
				}
			}
		}
	}

	public static void enterHouse(final Player me, final Player houseOwner,
			final boolean buildingMode) {
		if(me.getFields().mapInstance == null || !(me.getFields().mapInstance instanceof House) && !(me.getFields().mapInstance instanceof HouseDungeon)) {
			createPalette(me);
			return;
		}
		me.getMovementQueue().stopMovement();
		me.getPacketSender().sendInterfaceRemoval();
		me.getPacketSender().sendMapState(2);
		if (buildingMode) {
			me.getPacketSender().sendConfig(8000, 0);
			me.getPacketSender().sendConfig(8001, 0);
		} else {
			me.getPacketSender().sendConfig(8000, 1);
			me.getPacketSender().sendConfig(8001, 1);
		}
		me.moveTo(new Position(ConstructionData.MIDDLE_X, ConstructionData.MIDDLE_Y, 0));
		me.getMovementQueue().setMovementStatus(MovementStatus.CANNOT_MOVE);
		me.getPacketSender().sendInterface(28640);
		Server.getTaskScheduler().schedule(new Task(1, me, true) {
			int ticks = 0;
			@Override
			public void execute() {
				ticks++;
				switch(ticks) {
				case 1:
					me.getPacketSender().constructMapRegionForConstruction(houseOwner.getFields().mapInstance.getPalette());
					break;
				case 2:
					placeAllFurniture(me, 0);
					placeAllFurniture(me, 1);
					if (me.toConsCoords != null) {
						me.moveTo(new Position(me.toConsCoords[0],
								me.toConsCoords[1], 0));
						me.toConsCoords = null;
					} else {
						PlayerFurniture portal = findNearestPortal(me);
						int toX = ConstructionData.BASE_X+((portal.getRoomX()+1)*8);
						int toY = ConstructionData.BASE_Y+((portal.getRoomY()+1)*8);
						me.moveTo(new Position(toX+2, toY+2, 0));
					}
					break;
				case 3:
					me.getPacketSender().sendInterfaceRemoval();
					me.getPacketSender().sendMapState(0);
					try {
						((House)me.getFields().mapInstance).greet(me);
					} catch(Exception e) {
						
					}
					me.getMovementQueue().setMovementStatus(MovementStatus.NONE);
					this.stop();
					break;
				}
			}
		});
	}

	public static void sendServant(final Player p, int amount)
	{
		p.servantCharges--;
		p.getPacketSender().sendInterfaceRemoval();
		final Servant servant = (p.getMapInstance() instanceof House ? (House) p.getMapInstance() : ((HouseDungeon)p.getMapInstance()).getHouse()).getButler();
		servant.setFetching(true);
		servant.getFields().mapInstance = null;
		servant.takeItemsFromBank(p, p.servantItemFetchId, amount);
		GameServer.getTaskScheduler().schedule(new Task((int)Butlers.forId(servant.getId()).getTripSeconds(), p, false) {
			@Override
			public void execute()
			{
				servant.getFields().mapInstance = p.getMapInstance();
				if(servant.freeSlots() == servant.getInventory().length-1)
					////DialogueMethods.sendMobChat(p, p.talkingMob, "Servant", //DialogueMethods.DEPRESSED, "I'm sorry but i could not find this item on thou bank.");
					servant.giveItems(p);
				servant.setFetching(false);
				this.stop();
			}
		});
	}

	public static void rotateRoom(int wise, Player p)
	{
		if(p.getMapInstance().getOwner() != p)
			return;
		int[] myTiles = getMyChunk(p);
		int xOnTile = getXTilesOnTile(myTiles, p);
		int yOnTile = getYTilesOnTile(myTiles, p);
		int direction = 0;
		final int LEFT = 0, DOWN = 1, RIGHT = 2, UP = 3;
		if (xOnTile == 0)
			direction = LEFT;
		if (yOnTile == 0)
			direction = DOWN;
		if (xOnTile == 7)
			direction = RIGHT;
		if (yOnTile == 7)
			direction = UP;
		int xOff = 0, yOff = 0;
		if (direction == LEFT) {
			xOff = -1;
		}
		if (direction == DOWN) {
			yOff = -1;
		}
		if (direction == RIGHT) {
			xOff = 1;
		}
		if (direction == UP) {
			yOff = 1;
		}
		int chunkX = (myTiles[0] - 1) + xOff;
		int chunkY = (myTiles[1] - 1) + yOff;
		Room r = p.getMapInstance().getOwner().houseRooms[p.getFields().inDungeon() ? 4 : p.getLocation().getZ()][chunkX][chunkY];
		RoomData rd = RoomData.forID(r.getType());
		int toRot = (wise == 0 ? RoomData.getNextEligibleRotationClockWise(rd, direction, r.getRotation()) :
			RoomData.getNextEligibleRotationCounterClockWise(rd, direction, r.getRotation()));
		PaletteTile tile = new PaletteTile(rd.getX(), rd.getY(), 0, toRot);
		p.getPacketSender().sendObjectsRemoval(chunkX, chunkY, p.getLocation().getZ());
		House house = p.getMapInstance() instanceof House ? (House) p.getMapInstance() : ((HouseDungeon)p.getMapInstance()).getHouse();
		if (p.getFields().inDungeon()) {
			house.getSecondaryPalette().setTile(chunkX, chunkY, 0, tile);
		} else {
			house.getPalette().setTile(chunkX, chunkY, p.getLocation().getZ(), tile);
		}
		p.getMapInstance().getOwner().houseRooms[p.getFields().inDungeon() ? 4 : p.getLocation().getZ()][chunkX][chunkY].setRotation(toRot);
		p.toConsCoords = new int[] {p.getLocation().getX(), p.getLocation().getY()};
		p.getMapInstance().destroy();
		createPalette(p);
		/*if (p.getFields().inDungeon()) {
			p.getPacketSender().constructMapRegionForConstruction(
					house.getSecondaryPalette());
		} else {
			p.getPacketSender().constructMapRegionForConstruction(house.getPalette());
		}
		placeAllFurniture(p, chunkX, chunkY, p.getFields().inDungeon() ? 4 : p.getLocation().getZ());
		p.getPacketSender().sendInterfaceRemoval();*/
/*//constructionremoved
	}
	public static void placeMobs(Player p)
	{

		if(p.houseServant > 0) {
			PlayerFurniture portal = findNearestPortal(p);
			int toX = ConstructionData.BASE_X+((portal.getRoomX()+1)*8);
			int toY = ConstructionData.BASE_Y+((portal.getRoomY()+1)*8);
			//Servant Mob = MobHandler.spawnMob(null, p.houseServant, toX+3, toY+1, p.getLocation().getZ());
			Servant Mob = (Servant) MobManager.spawnMob(p.houseServant, new Position(toX+3, toY+1, p.getLocation().getZ()), 10, null, false, false);
			((House)p.getMapInstance()).addMob(Mob);
		}
		if(p.inBuildingMode)
		{
			return;
		}
		for(PlayerFurniture pf : p.playerFurniture)
		{
			Furniture f = Furniture.forFurnitureId(pf.getFurnitureId());
			int MobId = ConstructionData.getGuardId(f.getFurnitureId());
			if(pf.getRoomZ() != 4)
				continue;
			if(MobId == -1)
				continue;
			Room room = p.houseRooms[pf.getRoomZ()][pf.getRoomX()][pf.getRoomY()];
			HotSpots hs = HotSpots.forHotSpotIdAndCoords(pf.getHotSpotId(),
					pf.getStandardXOff(), pf.getStandardYOff(), room);
			int actualX = ConstructionData.BASE_X + (pf.getRoomX() + 1) * 8;
			actualX += ConstructionData.getXOffsetForObjectId(
					pf.getFurnitureId(), hs, room.getRotation());
			int actualY = ConstructionData.BASE_Y + (pf.getRoomY() + 1) * 8;
			actualY += ConstructionData.getYOffsetForObjectId(
					pf.getFurnitureId(), hs, room.getRotation());
			Servant Mob = (Servant) MobManager.spawnMob(MobId, new Position( actualX, actualY, 0), 10, null, false, false);
			((House)p.getMapInstance()).getDungeon().addMob(Mob);
		}
	} 	
	public static void placeAllFurniture(Player p, int x, int y, int z) {
		Player owner = p.getMapInstance().getOwner();
		for (PlayerFurniture pf : owner.playerFurniture) {
			if (pf.getRoomZ() != z)
				continue;
			if(pf.getRoomX() != x || pf.getRoomY() != y)
				continue;
			Room room = owner.houseRooms[pf.getRoomZ()][pf.getRoomX()][pf.getRoomY()];
			HotSpots hs = HotSpots.forHotSpotIdAndCoords(pf.getHotSpotId(),
					pf.getStandardXOff(), pf.getStandardYOff(), room);
			if (hs == null)
				return;
			int actualX = ConstructionData.BASE_X + (pf.getRoomX() + 1) * 8;
			actualX += ConstructionData.getXOffsetForObjectId(
					pf.getFurnitureId(), hs, room.getRotation());
			int actualY = ConstructionData.BASE_Y + (pf.getRoomY() + 1) * 8;
			actualY += ConstructionData.getYOffsetForObjectId(
					pf.getFurnitureId(), hs, room.getRotation());
			Furniture f = Furniture.forFurnitureId(pf.getFurnitureId());
			ArrayList<HotSpots> hsses = HotSpots
					.forObjectId_3(f.getHotSpotId());
			doFurniturePlace(hs, f, hsses, getMyChunkFor(actualX, actualY),
					actualX, actualY, room.getRotation(), p, false, z);
		}
	}

	public static void placeAllFurniture(Player p, int heightLevel) {
		Player owner = p.getMapInstance().getOwner();
		for (PlayerFurniture pf : owner.playerFurniture) {
			if (pf.getRoomZ() != heightLevel)
				continue;
			Room room = owner.houseRooms[pf.getRoomZ()][pf.getRoomX()][pf
			                                                           .getRoomY()];
			if (room == null)
				return;
			HotSpots hs = HotSpots.forHotSpotIdAndCoords(pf.getHotSpotId(),
					pf.getStandardXOff(), pf.getStandardYOff(), room);
			if (hs == null)
				return;
			// int rotation = hs.getRotation(room.getRotation());

			int actualX = ConstructionData.BASE_X + (pf.getRoomX() + 1) * 8;
			actualX += ConstructionData.getXOffsetForObjectId(
					pf.getFurnitureId(), hs, room.getRotation());
			int actualY = ConstructionData.BASE_Y + (pf.getRoomY() + 1) * 8;
			actualY += ConstructionData.getYOffsetForObjectId(
					pf.getFurnitureId(), hs, room.getRotation());
			Furniture f = Furniture.forFurnitureId(pf.getFurnitureId());
			ArrayList<HotSpots> hsses = HotSpots
					.forObjectId_3(f.getHotSpotId());
			doFurniturePlace(hs, f, hsses, getMyChunkFor(actualX, actualY),
					actualX, actualY, room.getRotation(), p, false, heightLevel);
		}
	}

	public static void createRoom(int roomType, Player p, int toHeight) {
		RoomData rd = ConstructionData.RoomData.forID(roomType);
		if (rd == null)
			return;
		if (p.getInventory().getAmount(995) < rd.getCost()) {
			p.getPacketSender().sendMessage("You need " + rd.getCost() + " coins to build this");
			return;
		}
		boolean isDungeonRoom = ConstructionData.isDungeonRoom(roomType);
		if (!p.getFields().inDungeon()) {
			if (isDungeonRoom && toHeight != 102 && toHeight != 103) {
				p.getPacketSender().sendMessage("You can only build this room in your dungeon.");
				return;
			}
		} else {
			if (!isDungeonRoom) {
				p.getPacketSender().sendMessage("You can only build this room on the surface");
				return;
			}
		}
		int[] myTiles = getMyChunk(p);
		if(myTiles == null)
			return;
		int xOnTile = getXTilesOnTile(myTiles, p);
		int yOnTile = getYTilesOnTile(myTiles, p);
		int direction = 0;
		final int LEFT = 0, DOWN = 1, RIGHT = 2, UP = 3, SAME = 4;
		if (xOnTile == 0)
			direction = LEFT;
		if (yOnTile == 0)
			direction = DOWN;
		if (xOnTile == 7)
			direction = RIGHT;
		if (yOnTile == 7)
			direction = UP;
		int rotation = ConstructionData.RoomData.getFirstElegibleRotation(rd,
				direction);
		if (toHeight == 100) {
			/** Create room from stair **//*constructionremoved
			direction = SAME;
			toHeight = 1;
			rotation = p.getMapInstance().getOwner().houseRooms[0][myTiles[0] - 1][myTiles[1] - 1]
					.getRotation();
			int stairId = 0;
			for (PlayerFurniture furn : p.playerFurniture) {
				if (furn.getRoomX() == myTiles[0] - 1
						&& furn.getRoomY() == myTiles[1] - 1
						&& furn.getRoomZ() == 0) {
					if (furn.getStandardXOff() == 3
							&& furn.getStandardYOff() == 3) {
						stairId = furn.getFurnitureId() + 1;
					}
				}
			}
			doFurniturePlace(HotSpots.SKILL_HALL_STAIRS_1,
					Furniture.forFurnitureId(stairId), null, myTiles,
					ConstructionData.BASE_X + (myTiles[0] * 8) + 3,
					ConstructionData.BASE_Y + (myTiles[1] * 8) + 3, rotation,
					p, false, 1);
			PlayerFurniture pf = new PlayerFurniture(myTiles[0] - 1,
					myTiles[1] - 1, 1, 37, stairId, 3, 3);
			p.playerFurniture.add(pf);
		}
		if (toHeight == 101) {
			direction = SAME;
			toHeight = 0;
			rotation = p.getMapInstance().getOwner().houseRooms[1][myTiles[0] - 1][myTiles[1] - 1]
					.getRotation();
			int stairId = 0;
			for (PlayerFurniture furn : p.playerFurniture) {
				if (furn.getRoomX() == myTiles[0] - 1
						&& furn.getRoomY() == myTiles[1] - 1
						&& furn.getRoomZ() == 1) {
					if (furn.getStandardXOff() == 3
							&& furn.getStandardYOff() == 3) {
						stairId = furn.getFurnitureId() + 1;
					}
				}
			}
			doFurniturePlace(HotSpots.SKILL_HALL_STAIRS,
					Furniture.forFurnitureId(stairId), null, myTiles,
					ConstructionData.BASE_X + (myTiles[0] * 8) + 3,
					ConstructionData.BASE_Y + (myTiles[1] * 8) + 3, rotation,
					p, false, 1);
			PlayerFurniture pf = new PlayerFurniture(myTiles[0] - 1,
					myTiles[1] - 1, 0, 36, stairId, 3, 3);
			p.playerFurniture.add(pf);
		}

		/**
		 * Create dungeon room from entrance
		 */
	/*//constructionremoved
	if (toHeight == 102 || toHeight == 103) {
			direction = SAME;
			toHeight = 4;
			rotation = p.getMapInstance().getOwner().houseRooms[0][myTiles[0] - 1][myTiles[1] - 1]
					.getRotation();
			PlayerFurniture pf = null;
			if (toHeight == 102) {
				int stairId = 13497;
				pf = new PlayerFurniture(myTiles[0] - 1, myTiles[1] - 1, 4, 36,
						stairId, 3, 3);
			} else {
				pf = new PlayerFurniture(myTiles[0] - 1, myTiles[1] - 1, 4, 88,
						13328, 1, 6);
			}
			p.playerFurniture.add(pf);
		}

		Room room = new Room(rotation, roomType, 0);
		PaletteTile tile = new PaletteTile(room.getX(), room.getY(),
				room.getZ(), room.getRotation());

		int xOff = 0, yOff = 0;
		if (direction == LEFT) {
			xOff = -1;
		}
		if (direction == DOWN) {
			yOff = -1;
		}
		if (direction == RIGHT) {
			xOff = 1;
		}
		if (direction == UP) {
			yOff = 1;
		}

		if (toHeight == 1) {
			Room r = p.getMapInstance().getOwner().houseRooms[0][(myTiles[0] - 1) + xOff][(myTiles[1] - 1)
			                                                                                     + yOff];
			if (r.getType() == ConstructionData.EMPTY
					|| r.getType() == ConstructionData.BUILDABLE
					|| r.getType() == ConstructionData.GARDEN
					|| r.getType() == ConstructionData.FORMAL_GARDEN) {
				p.getPacketSender().sendMessage("You need a foundation to build there");
				return;
			}
		}
		House house = p.getMapInstance() instanceof House ? (House) p.getMapInstance() : ((HouseDungeon)p.getMapInstance()).getHouse();
		if (toHeight == 4 || p.getFields().inDungeon()) {
			house.getSecondaryPalette().setTile(
					(myTiles[0] - 1) + xOff, (myTiles[1] - 1) + yOff, 0, tile);
		} else {
			house.getPalette().setTile((myTiles[0] - 1) + xOff,
					(myTiles[1] - 1) + yOff, toHeight, tile);
		}
		house.getOwner().houseRooms[p.getFields().inDungeon() ? 4 : toHeight][(myTiles[0] - 1) + xOff][(myTiles[1] - 1)
		                                                                                               + yOff] = new Room(rotation, roomType, 0);
		p.toConsCoords = new int[] {p.getLocation().getX(), p.getLocation().getY()};
		p.getMapInstance().destroy();
		createPalette(p);
		/*if (p.getFields().inDungeon()) {
			p.getPacketSender().constructMapRegionForConstruction(
					house.getSecondaryPalette());
		} else {
			p.getPacketSender().constructMapRegionForConstruction(house.getPalette());
		}
		p.getPacketSender().sendInterfaceRemoval();*/
/*//constructionremoved
	}

	public static void deleteRoom(Player p, int toHeight) {

		int[] myTiles = getMyChunk(p);
		int xOnTile = getXTilesOnTile(myTiles, p);
		int yOnTile = getYTilesOnTile(myTiles, p);
		int direction = 0;
		final int LEFT = 0, DOWN = 1, RIGHT = 2, UP = 3;
		if (xOnTile == 0)
			direction = LEFT;
		if (yOnTile == 0)
			direction = DOWN;
		if (xOnTile == 7)
			direction = RIGHT;
		if (yOnTile == 7)
			direction = UP;

		int roomType = p.getFields().inDungeon() ? ConstructionData.DUNGEON_EMPTY
				: ConstructionData.EMPTY;
		Room room = new Room(0, roomType, 0);
		PaletteTile tile = new PaletteTile(room.getX(), room.getY(),
				room.getZ(), room.getRotation());

		int xOff = 0, yOff = 0;
		if (direction == LEFT) {
			xOff = -1;
		}
		if (direction == DOWN) {
			yOff = -1;
		}
		if (direction == RIGHT) {
			xOff = 1;
		}
		if (direction == UP) {
			yOff = 1;
		}
		int chunkX = (myTiles[0] - 1) + xOff;
		int chunkY = (myTiles[1] - 1) + yOff;
		Room r = p.getMapInstance().getOwner().houseRooms[p.getFields().inDungeon() ? 4 : toHeight][chunkX][chunkY];
		if (r.getType() == ConstructionData.GARDEN
				|| r.getType() == ConstructionData.FORMAL_GARDEN) {
			int gardenAmt = 0;
			for (int z = 0; z < p.getMapInstance().getOwner().houseRooms.length; z++) {
				for (int x = 0; x < p.getMapInstance().getOwner().houseRooms[z].length; x++) {
					for (int y = 0; y < p.getMapInstance().getOwner().houseRooms[z][x].length; y++) {
						Room r1 = p.getMapInstance().getOwner().houseRooms[z][x][y];
						if (r1 == null)
							continue;
						if (r1.getType() == ConstructionData.GARDEN
								|| r1.getType() == ConstructionData.FORMAL_GARDEN) {
							gardenAmt++;
						}
					}
				}
			}
			if (gardenAmt < 2) {
				p.getPacketSender().sendMessage("You need atleast 1 garden or formal garden");
				p.getPacketSender().sendInterfaceRemoval();
				return;
			}
		}
		p.getPacketSender().sendObjectsRemoval(chunkX, chunkY, p.getLocation().getZ());
		House house = p.getMapInstance() instanceof House ? (House) p.getMapInstance() : ((HouseDungeon)p.getMapInstance()).getHouse();
		if (p.getLocation().getZ() == 0) {
			if (p.getFields().inDungeon()) {
				house.getSecondaryPalette().setTile(chunkX, chunkY, 0,
						tile);
			} else {
				house.getPalette().setTile(chunkX, chunkY, toHeight,
						tile);
			}
			p.getMapInstance().getOwner().houseRooms[p.getFields().inDungeon() ? 4 : toHeight][chunkX][chunkY] = new Room(
					0, roomType, 0);
		} else {
			if (p.getFields().inDungeon()) {
				house.getSecondaryPalette().setTile(chunkX, chunkY, 0,
						null);
			} else {
				house.getPalette().setTile(chunkX, chunkY, toHeight,
						null);
			}
			p.getMapInstance().getOwner().houseRooms[p.getFields().inDungeon() ? 4 : toHeight][chunkX][chunkY] = null;
		}
		p.toConsCoords = new int[] {p.getLocation().getX(), p.getLocation().getY()};
		p.getMapInstance().destroy();
		createPalette(p);
	/*	if (p.getFields().inDungeon()) {
			p.getPacketSender().constructMapRegionForConstruction(
					house.getSecondaryPalette());
		} else {
			p.getPacketSender().constructMapRegionForConstruction(house.getPalette());
		}
		p.getPacketSender().sendInterfaceRemoval();
*/
		/*constructionremoved
	Iterator<PlayerFurniture> iterator = p.playerFurniture.iterator();
		while (iterator.hasNext()) {
			PlayerFurniture pf = iterator.next();
			if (pf.getRoomX() == chunkX
					&& pf.getRoomY() == chunkY
					&& pf.getRoomZ() == toHeight)
				iterator.remove();
		}
		Iterator<Portal> portals = p.portals.iterator();
		while(portals.hasNext())
		{
			Portal port = portals.next();
			if (port.getRoomX() == chunkX
					&& port.getRoomY() == chunkY
					&& port.getRoomZ() == toHeight)
				iterator.remove();
		}
	}

	public static boolean roomExists(Player p) {
		int[] myTiles = getMyChunk(p);
		int xOnTile = getXTilesOnTile(myTiles, p);
		int yOnTile = getYTilesOnTile(myTiles, p);
		int direction = 0;
		final int LEFT = 0, DOWN = 1, RIGHT = 2, UP = 3;
		if (xOnTile == 0)
			direction = LEFT;
		if (yOnTile == 0)
			direction = DOWN;
		if (xOnTile == 7)
			direction = RIGHT;
		if (yOnTile == 7)
			direction = UP;
		int xOff = 0, yOff = 0;
		if (direction == LEFT) {
			xOff = -1;
		}
		if (direction == DOWN) {
			yOff = -1;
		}
		if (direction == RIGHT) {
			xOff = 1;
		}
		if (direction == UP) {
			yOff = 1;
		}
		Room room = p.getMapInstance().getOwner().houseRooms[p.getFields().inDungeon() ? 4 : p.getLocation().getZ()][myTiles[0]
				- 1 + xOff][myTiles[1] - 1 + yOff];
		if (room == null)
			return false;
		if (room.getType() == ConstructionData.BUILDABLE
				|| room.getType() == ConstructionData.EMPTY
				|| room.getType() == ConstructionData.DUNGEON_EMPTY)
			return false;
		return true;
	}

	public static void handleItemOnItem(int item1, int item2, Player p) {
		if (item1 == 7738) {
			if (item2 == 7702) {
				p.getInventory().delete(7738, 1);
				p.getInventory().delete(7702, 1);
				p.getInventory().add(7700, 1);
			}
			if (item2 == 7714) {
				p.getInventory().delete(7738, 1);
				p.getInventory().delete(7714, 1);
				p.getInventory().add(7712, 1);
			}
			if (item2 == 7726) {
				p.getInventory().delete(7738, 1);
				p.getInventory().delete(7726, 1);
				p.getInventory().add(7724, 1);
			}
		}
		if (item2 == 7738) {
			if (item1 == 7702) {
				p.getInventory().delete(7738, 1);
				p.getInventory().delete(7702, 1);
				p.getInventory().add(7700, 1);
			}
			if (item1 == 7714) {
				p.getInventory().delete(7738, 1);
				p.getInventory().delete(7714, 1);
				p.getInventory().add(7712, 1);
			}
			if (item1 == 7726) {
				p.getInventory().delete(7738, 1);
				p.getInventory().delete(7726, 1);
				p.getInventory().add(7724, 1);
			}
		}
		if (item1 == 7700 || item1 == 7712 || item1 == 7724) {
			if (item2 == 7728 || item2 == 7732 || item2 == 7735) {
				p.getInventory().delete(item1, 1);
				p.getInventory().delete(item2, 1);
				if (item1 == 7700 || item1 == 7712)
					p.getInventory().add(item2 + 1, 1);
				if (item1 == 7700 || item1 == 7724)
					p.getInventory().add(item2 + 2, 1);
			}
		}
	}

	public static void handleItemOnObject(int objectId, int itemId, Player p) {
		switch (objectId) {

		case 13528:
		case 13529:
		case 13531:
		case 13533:
		case 13536:
		case 13539:
		case 13542:
			if (itemId == 7690) {
				p.getInventory().delete(7690, 1);
				p.getInventory().add(7691, 1);
			}
			break;
		case 13568:
		case 13569:
		case 13570:
		case 13571:
		case 13572:
		case 13573:
			if (itemId != 1919)
				return;
			if (p.getInventory().contains(1919)) {
				p.getInventory().delete(1919, 1);
				int beer = 0;
				if (objectId == 13568)
					beer = 1917;
				if (objectId == 13569)
					beer = 5763;
				if (objectId == 13570)
					beer = 1905;
				if (objectId == 13571)
					beer = 1909;
				if (objectId == 13572)
					beer = 1911;
				if (objectId == 13573)
					beer = 5755;
				p.getInventory().add(beer, 1);
			}
			break;
		}

	}

	public static boolean handleButtonClick(int buttonId, Player p) {
		switch (buttonId) {
		case -27213:
		case 28645:
			p.getPacketSender().sendInterfaceRemoval();
			return true;
		case 2471:
			if (p.getFields().getDialogueAction() == 423) {
				p.getInventory().add(7688, 1);
			}
			if (p.getFields().getDialogueAction() == 428) {
				p.getInventory().add(1923, 1);
				return true;
			}
			if(p.getFields().getDialogueAction() == 442)
			{
				
				p.getPacketSender().sendInterfaceRemoval();
				if(p.houseRooms[0][0][0] == null)
				{
					p.getPacketSender().sendMessage("You don't own a house. Talk to the House Agent to buy one.");
					return true;
				}
				p.inBuildingMode = false;
				Construction.createPalette(p);
				return true;
			}
			if(p.getFields().getDialogueAction() == 642)
			{
				/**
				 * Counter room clockwise
				 */
	
	/*//constructionremoved
				rotateRoom(0, p);
				return true;
			}
			if(p.getFields().getDialogueAction() == 645)
			{
				p.portalSelected = 0;
				////DialogueManager.sendDialogues(p, 646, 0);
				return true;
			}
			break;
		case 2472:
			if (p.getFields().getDialogueAction() == 423) {
				p.getInventory().add(7702, 1);
				return true;
			}
			if (p.getFields().getDialogueAction() == 428) {
				p.getInventory().add(1887, 1);
				return true;
			}
			if(p.getFields().getDialogueAction() == 442)
			{
				p.getPacketSender().sendInterfaceRemoval();
				if(p.houseRooms[0][0][0] == null)
				{
					p.getPacketSender().sendMessage("You don't own a house. Talk to the House Agent to buy one.");
					return true;
				}
				p.inBuildingMode = true;
				Construction.createPalette(p);
				return true;
			}
			if(p.getFields().getDialogueAction() == 642)
			{
				/**
				 * Counter room clockwise
				 *//*//constructionremoved
				rotateRoom(1, p);
				return true;
			}
			if(p.getFields().getDialogueAction() == 645)
			{
				p.portalSelected = 1;
				////DialogueManager.sendDialogues(p, 646, 0);
				return true;
			}
			break;
		case 2473:
			if (p.getFields().getDialogueAction() == 423) {
				p.getInventory().add(7728, 1);
				return true;
			}
			if (p.getFields().getDialogueAction() == 428) {
				DialogueManager.start(p, ConstructionDialogues.withdrawSuppliesDialogue(p, 427));
				return true;
			}
			if(p.getFields().getDialogueAction() == 442)
			{
				p.getPacketSender().commandFrame(2);
				return true;
			}
			if(p.getFields().getDialogueAction() == 642)
			{
				/**
				 * Remove room
				 *//*//constructionremoved
				if (p.getLocation().getZ() == 0 && !p.getFields().inDungeon())
					deleteRoom(p, 0);
				if (p.getFields().inDungeon())
					deleteRoom(p, 4);
				if (p.getLocation().getZ() == 1) {
					deleteRoom(p, 1);
				}
				return true;
			}
			if(p.getFields().getDialogueAction() == 645)
			{
				p.portalSelected = 2;
				//DialogueManager.sendDialogues(p, 646, 0);
				return true;
			}
			break;
		case 2482:
			if (p.getFields().getDialogueAction() == 424) {
				p.getInventory().add(7688, 1);
				return true;
			}
			if (p.getFields().getDialogueAction() == 430) {
				p.getInventory().add(1923, 1);
				return true;
			}
			if (p.getFields().getDialogueAction() == 434) {
				p.getInventory().add(7738, 1);
				return true;
			}
			if(p.getFields().getDialogueAction() == 440)
			{
				p.getInventory().add(7671, 1);
				return true;
			}
			if(p.getFields().getDialogueAction() == 644)
			{
				p.getMapInstance().removePlayer(p);
				p.moveTo(new Position(ConstructionConstants.EDGEVILLE_X, ConstructionConstants.EDGEVILLE_Y, 0));
				return true;
			}
			break;
		case 2483:
			if (p.getFields().getDialogueAction() == 424) {
				p.getInventory().add(7702, 1);
				return true;
			}
			if (p.getFields().getDialogueAction() == 430) {
				p.getInventory().add(2313, 1);
				return true;
			}
			if (p.getFields().getDialogueAction() == 434) {
				p.getInventory().add(1927, 1);
				return true;
			}
			if(p.getFields().getDialogueAction() == 440)
			{
				p.getInventory().add(7673, 1);
				return true;
			}
			if(p.getFields().getDialogueAction() == 644)
			{
				p.getMapInstance().removePlayer(p);
				p.moveTo(new Position(ConstructionConstants.KARAMJA_X, ConstructionConstants.KARAMJA_Y, 0));
				return true;
			}
			break;
		case 2484:
			if (p.getFields().getDialogueAction() == 424) {
				p.getInventory().add(7728, 1);
				return true;
			}
			if (p.getFields().getDialogueAction() == 430) {
				p.getInventory().add(1931, 1);
				return true;
			}
			if (p.getFields().getDialogueAction() == 434) {
				p.getInventory().add(1944, 1);
				return true;
			}
			if(p.getFields().getDialogueAction() == 440)
			{
				p.getInventory().add(7675, 1);
				return true;
			}
			if(p.getFields().getDialogueAction() == 644)
			{
				p.getMapInstance().removePlayer(p);
				p.moveTo(new Position(ConstructionConstants.DRAYNOR.getX(), ConstructionConstants.DRAYNOR.getY(), 0));
				return true;
			}
			break;
		case 2485:
			if (p.getFields().getDialogueAction() == 424) {
				p.getInventory().add(1919, 1);
				return true;
			}
			if (p.getFields().getDialogueAction() == 430) {
				DialogueManager.start(p, ConstructionDialogues.withdrawSuppliesDialogue(p, 429));
				return true;
			}
			if (p.getFields().getDialogueAction() == 434) {
				p.getInventory().add(1933, 1);
				return true;
			}
			if(p.getFields().getDialogueAction() == 440)
			{
				p.getInventory().add(7676, 1);
				return true;
			}
			if(p.getFields().getDialogueAction() == 644)
			{
				p.getMapInstance().removePlayer(p);
				p.moveTo(new Position(ConstructionConstants.AL_KHARID_X, ConstructionConstants.AL_KHARID_Y, 0));
				return true;
			}
			break;
		case 2494:
			if(p.getFields().getDialogueAction() == 646)
			{
				String s = Portals.VARROCK.canBuild(p);
				if(s != null)
				{
					p.getPacketSender().sendInterfaceRemoval();
					DialogueManager.start(p, ConstructionDialogues.sendConstructionStatement(s));
				}
				return true;
			}
			if(p.getFields().getDialogueAction() == 647)
			{
				String s = Portals.ARDOUGNE.canBuild(p);
				if(s != null)
				{
					p.getPacketSender().sendInterfaceRemoval();
					DialogueManager.start(p, ConstructionDialogues.sendConstructionStatement(s));
				}
				return true;
			}
			if(p.getFields().getDialogueAction() == 453)
			{
				p.servantItemFetchId = ConstructionData.PLANK;
				p.xInterfaceId = 28643;
				//p.getOutStream().createFrame(27);
				return true;
			}
			if(p.getFields().getDialogueAction() == 454)
			{
				p.servantItemFetchId = ConstructionData.SOFT_CLAY;
				p.xInterfaceId = 28643;
				//p.getOutStream().createFrame(27);
				return true;
			}
			if(p.getFields().getDialogueAction() == 455)
			{
				p.servantItemFetchId = ConstructionData.CLOTH;
				p.xInterfaceId = 28643;
				//p.getOutStream().createFrame(27);
				return true;
			}
			if(p.getFields().getDialogueAction() == 452)
			{
				p.getPacketSender().sendInterfaceRemoval();
				return true;
			}
			if(p.getFields().getDialogueAction() == 441)
			{
				p.getInventory().add(7671, 1);
				return true;
			}
			if (p.getFields().getDialogueAction() == 425 || p.getFields().getDialogueAction() == 426
					|| p.getFields().getDialogueAction() == 427 || p.getFields().getDialogueAction() == 429
					|| p.getFields().getDialogueAction() == 431) {
				p.getInventory().add(7688, 1);
				return true;
			}
			if (p.getFields().getDialogueAction() == 432) {
				p.getInventory().add(1923, 1);
				return true;
			}
			if (p.getFields().getDialogueAction() == 435) {
				p.getInventory().add(7738, 1);
				return true;
			}
			if (p.getFields().getDialogueAction() == 436) {
				p.getInventory().add(1942, 1);
				return true;
			}
			break;
		case 2495:

			if(p.getFields().getDialogueAction() == 646)
			{
				String s = Portals.LUMBRIDGE.canBuild(p);
				if(s != null)
				{
					p.getPacketSender().sendInterfaceRemoval();
					DialogueManager.start(p, ConstructionDialogues.sendConstructionStatement(s));
				}
				return true;
			}
			if(p.getFields().getDialogueAction() == 647)
			{
				String s = Portals.YANILLE.canBuild(p);
				if(s != null)
				{
					p.getPacketSender().sendInterfaceRemoval();
					DialogueManager.start(p, ConstructionDialogues.sendConstructionStatement(s));
				}
				return true;
			}
			if(p.getFields().getDialogueAction() == 453)
			{
				p.servantItemFetchId = ConstructionData.OAK_PLANK;
				p.xInterfaceId = 28643;
				//p.getOutStream().createFrame(27);
				return true;
			}
			if(p.getFields().getDialogueAction() == 454)
			{
				p.servantItemFetchId = ConstructionData.LIMESTONE_BRICK;
				p.xInterfaceId = 28643;
				//p.getOutStream().createFrame(27);
				return true;
			}
			if(p.getFields().getDialogueAction() == 455)
			{
				p.servantItemFetchId = ConstructionData.GOLD_LEAF;
				p.xInterfaceId = 28643;
				//p.getOutStream().createFrame(27);
				return true;
			}
			if(p.getFields().getDialogueAction() == 452)
			{
				if(p.getMapInstance().getOwner() == p)
				{
					DialogueManager.start(p, ConstructionDialogues.servantOptions(p, 453));
				} else
					DialogueManager.start(p, ConstructionDialogues.notPlayersButler(p.interactingMob.getId()));
				return true;
			}
			if(p.getFields().getDialogueAction() == 441)
			{
				p.getInventory().add(7673, 1);
				return true;
			}
			if (p.getFields().getDialogueAction() == 425) {
				p.getInventory().add(7702, 1);
				return true;
			}
			if (p.getFields().getDialogueAction() == 426 || p.getFields().getDialogueAction() == 427) {
				p.getInventory().add(7714, 1);
				return true;
			}
			if (p.getFields().getDialogueAction() == 429 || p.getFields().getDialogueAction() == 431
					|| p.getFields().getDialogueAction() == 429) {
				p.getInventory().add(7726, 1);
				return true;
			}
			if (p.getFields().getDialogueAction() == 432) {
				p.getInventory().add(2313, 1);
				return true;
			}
			if (p.getFields().getDialogueAction() == 435) {
				p.getInventory().add(1927, 1);
				return true;
			}
			if (p.getFields().getDialogueAction() == 436) {
				p.getInventory().add(1550, 1);
				return true;
			}
			break;
		case 2496:
			if(p.getFields().getDialogueAction() == 646)
			{
				String s = Portals.FALADOR.canBuild(p);
				if(s != null)
				{
					p.getPacketSender().sendInterfaceRemoval();
					DialogueManager.start(p, ConstructionDialogues.sendConstructionStatement(s));
				}
				return true;
			}
			if(p.getFields().getDialogueAction() == 647)
			{
				String s = Portals.KHARYLL.canBuild(p);
				if(s != null)
				{
					p.getPacketSender().sendInterfaceRemoval();
					DialogueManager.start(p, ConstructionDialogues.sendConstructionStatement(s));
				}
				return true;
			}
			if(p.getFields().getDialogueAction() == 453)
			{
				p.servantItemFetchId = ConstructionData.TEAK_PLANK;
				p.xInterfaceId = 28643;
				//p.getOutStream().createFrame(27);
				return true;
			}
			if(p.getFields().getDialogueAction() == 454)
			{
				p.servantItemFetchId = ConstructionData.STEEL_BAR;
				p.xInterfaceId = 28643;
				//p.getOutStream().createFrame(27);
				return true;
			}
			if(p.getFields().getDialogueAction() == 455)
			{
				p.servantItemFetchId = ConstructionData.MARBLE_BLOCK;
				p.xInterfaceId = 28643;
				//p.getOutStream().createFrame(27);
				return true;
			}
			if(p.getFields().getDialogueAction() == 452)
			{
				if(p.getMapInstance().getOwner() == p) {
					House house = p.getMapInstance() instanceof House ? (House) p.getMapInstance() : ((HouseDungeon)p.getMapInstance()).getHouse();
					Servant butler = house.getButler();
					//	butler.randomWalk = true;
					butler.spawnedFor = null;
					butler.setGreetVisitors(true);
					p.getPacketSender().sendInterfaceRemoval();
				} else {
					DialogueManager.start(p, ConstructionDialogues.notPlayersButler(p.interactingMob.getId()));
				}
				return true;

			}
			if(p.getFields().getDialogueAction() == 441)
			{
				p.getInventory().add(7675, 1);
				return true;
			}
			if (p.getFields().getDialogueAction() == 425 || p.getFields().getDialogueAction() == 426
					|| p.getFields().getDialogueAction() == 427 || p.getFields().getDialogueAction() == 429
					|| p.getFields().getDialogueAction() == 431) {
				p.getInventory().add(7732, 1);
				return true;
			}
			if (p.getFields().getDialogueAction() == 432) {
				p.getInventory().add(1931, 1);
				return true;
			}
			if (p.getFields().getDialogueAction() == 435) {
				p.getInventory().add(1944, 1);
				return true;
			}
			if (p.getFields().getDialogueAction() == 436) {
				p.getInventory().add(1957, 1);
				return true;
			}
			break;
		case 2497:
			if(p.getFields().getDialogueAction() == 646)
			{
				String s = Portals.CAMELOT.canBuild(p);
				if(s != null)
				{
					p.getPacketSender().sendInterfaceRemoval();
					DialogueManager.start(p, ConstructionDialogues.sendConstructionStatement(s));
				}
				return true;
			}
			if(p.getFields().getDialogueAction() == 647)
			{
				String s = Portals.EMPTY.canBuild(p);
				if(s != null)
				{
					p.getPacketSender().sendInterfaceRemoval();
					DialogueManager.start(p, ConstructionDialogues.sendConstructionStatement(s));
				}
				return true;
			}
			if(p.getFields().getDialogueAction() == 453)
			{
				p.servantItemFetchId = ConstructionData.MAHOGANY_PLANK;
				p.xInterfaceId = 28643;
				//p.getOutStream().createFrame(27);
				return true;
			}
			if(p.getFields().getDialogueAction() == 454)
			{
				DialogueManager.start(p, ConstructionDialogues.servantOptions(p, 453));
				return true;
			}
			if(p.getFields().getDialogueAction() == 455)
			{
				p.servantItemFetchId = ConstructionData.MAGIC_STONE;
				p.xInterfaceId = 28643;
				//p.getOutStream().createFrame(27);
				return true;
			}
			if(p.getFields().getDialogueAction() == 452)
			{
				House house = p.getMapInstance() instanceof House ? (House) p.getMapInstance() : ((HouseDungeon)p.getMapInstance()).getHouse();
				Mob butler = house.getButler();
				if(butler.spawnedFor != p)
					butler.spawnedFor = p;
				else
					butler.spawnedFor = null;
				p.getPacketSender().sendInterfaceRemoval();
				return true;
			}
			if(p.getFields().getDialogueAction() == 441)
			{
				p.getInventory().add(7676, 1);
				return true;
			}
			if (p.getFields().getDialogueAction() == 425 || p.getFields().getDialogueAction() == 426
					|| p.getFields().getDialogueAction() == 427 || p.getFields().getDialogueAction() == 429
					|| p.getFields().getDialogueAction() == 431) {
				p.getInventory().add(1919, 1);
				return true;
			}
			if (p.getFields().getDialogueAction() == 432) {
				p.getInventory().add(1949, 1);
				return true;
			}
			if (p.getFields().getDialogueAction() == 435) {
				p.getInventory().add(1933, 1);
				return true;
			}
			if (p.getFields().getDialogueAction() == 436) {
				p.getInventory().add(1985, 1);
				return true;
			}
			break;
		case 2498:
			if(p.getFields().getDialogueAction() == 453)
			{
				DialogueManager.start(p, ConstructionDialogues.servantOptions(p, 454));
				return true;
			} else
				if(p.getFields().getDialogueAction() == 454)
				{
					DialogueManager.start(p, ConstructionDialogues.servantOptions(p, 455));
					return true;
				} else
					if(p.getFields().getDialogueAction() == 455)
					{
						DialogueManager.start(p, ConstructionDialogues.servantOptions(p, 454));
						return true;
					}
			if(p.getFields().getDialogueAction() == 452)
			{
				House house = p.getMapInstance() instanceof House ? (House) p.getMapInstance() : ((HouseDungeon)p.getMapInstance()).getHouse();
				if(house.getOwner() == p)
				{	
					Mob butler = house.getButler();
					GameServer.getWorld().deregister(butler);
					p.houseServant = 0;
					p.getPacketSender().sendInterfaceRemoval();
				} else {
					p.getPacketSender().sendMessage("You can't fire someone else's servant.");
					p.getPacketSender().sendInterfaceRemoval();
				}
				return true;

			}
			if(p.getFields().getDialogueAction() == 441)
			{
				p.getInventory().add(7679, 1);
				return true;
			}
			if (p.getFields().getDialogueAction() == 425) {
				p.getInventory().add(1887, 1);
				return true;
			}
			if (p.getFields().getDialogueAction() == 426) {
				p.getInventory().add(1923, 1);
				return true;
			}
			if (p.getFields().getDialogueAction() == 427) {
				DialogueManager.start(p, ConstructionDialogues.withdrawSuppliesDialogue(p, 428));
				return true;
			}
			if (p.getFields().getDialogueAction() == 429) {
				DialogueManager.start(p, ConstructionDialogues.withdrawSuppliesDialogue(p, 430));
				return true;
			}
			if (p.getFields().getDialogueAction() == 431) {
				DialogueManager.start(p, ConstructionDialogues.withdrawSuppliesDialogue(p, 432));
				return true;
			} else if (p.getFields().getDialogueAction() == 432) {
				DialogueManager.start(p, ConstructionDialogues.withdrawSuppliesDialogue(p, 431));
				return true;
			}
			if (p.getFields().getDialogueAction() == 435) {
				DialogueManager.start(p, ConstructionDialogues.withdrawSuppliesDialogue(p, 436));
				return true;
			} else if (p.getFields().getDialogueAction() == 436) {
				DialogueManager.start(p, ConstructionDialogues.withdrawSuppliesDialogue(p, 435));
				return true;
			}
			break;
		case 2461:
			if(p.getFields().getDialogueAction() == 654)
			{
				if(p.houseRooms[0][0][0] != null)
					return true;
				for (int x = 0; x < 13; x++)
					for (int y = 0; y < 13; y++)
						p.houseRooms[0][x][y] = new Room(0,
								ConstructionData.EMPTY, 0);
				p.houseRooms[0][7][7] = new Room(0, ConstructionData.GARDEN, 0);
				PlayerFurniture pf = new PlayerFurniture(7, 7, 0, HotSpots.CENTREPIECE.getHotSpotId(), 
						Furniture.EXIT_PORTAL.getFurnitureId(), HotSpots.CENTREPIECE.getXOffset(), 
						HotSpots.CENTREPIECE.getYOffset());
				p.playerFurniture.add(pf);
				DialogueManager.start(p, 354);
				return true;
			}
			if(p.getFields().getDialogueAction() == 457)
			{
				Butlers b = Butlers.forId(p.houseServant);
				if(p.getInventory().getAmount(995) >= b.getLoanCost()) {
					p.servantCharges = 8;
					p.getInventory().delete(995, b.getLoanCost());
				} else {
					p.getPacketSender().sendMessage("You need "+b.getLoanCost()+" coins to do that.");
				}
				p.getPacketSender().sendInterfaceRemoval();
				return true;

			}
			if(p.getFields().getDialogueAction() == 444)
			{
				Butlers b = Butlers.forId(p.interactingMob.getId());
				if(p.getSkillManager().getCurrentLevel(Skill.CONSTRUCTION) < b.getConsLevel())
				{
					DialogueManager.start(p, ConstructionDialogues.hireServantDeclineDialogue(p, b.getMobId(), "lvlreq"));
					return true;
				}
				int roomCount = 0;
				for(int z = 0; z < p.houseRooms.length; z++)
				{
					for(int x = 0; x < p.houseRooms[z].length; x++)
					{
						for(int y = 0; y < p.houseRooms[z][x].length; y++)
						{
							if(p.houseRooms[z][x][y] == null)
							{
								continue;
							}
							if(p.houseRooms[z][x][y].getType() == ConstructionData.BEDROOM)
								roomCount++;
							if(roomCount > 1)
								break;
						}
					}
				}
				if(roomCount < 2)
				{
					DialogueManager.start(p, ConstructionDialogues.hireServantDeclineDialogue(p, b.getMobId(), "room"));
					return true;
				}
				DialogueManager.start(p, ConstructionDialogues.hireServantMakeDealDialogue(p, b.getMobId()));
				return true;
			}
			if(p.getFields().getDialogueAction() == 448)
			{
				p.houseServant = p.interactingMob.getId();
				DialogueManager.start(p, ConstructionDialogues.finalServantDealDialogue(true));
				return true;
			}
			if(p.getFields().getDialogueAction() == 439)
			{
				p.getInventory().add(7671, 1);
				return true;
			}
			if (p.getFields().getDialogueAction() == 433) {
				p.getInventory().add(7738, 1);
				return true;
			}
			if (p.getFields().getDialogueAction() == 422) {
				if (p.getLocation().getZ() == 0 && !p.getFields().inDungeon())
					deleteRoom(p, 0);
				if (p.getFields().inDungeon())
					deleteRoom(p, 4);
				if (p.getLocation().getZ() == 1) {
					deleteRoom(p, 1);
				}
				return true;
			}
			if (p.getFields().getDialogueAction() == 419) {

				int myTiles[] = getMyChunk(p);
				Room room = p.getMapInstance().getOwner().houseRooms[4][myTiles[0] - 1][myTiles[1] - 1];
				if (room != null) {
					p.getPacketSender().sendMessage("Error handling room.");
					p.getPacketSender().sendInterfaceRemoval();
				} else {
					createRoom(ConstructionData.DUNGEON_STAIR_ROOM, p, 102);
					p.getPacketSender().sendInterfaceRemoval();
				}
				return true;
			}
			if (p.getFields().getDialogueAction() == 438) {

				int myTiles[] = getMyChunk(p);
				Room room = p.getMapInstance().getOwner().houseRooms[4][myTiles[0] - 1][myTiles[1] - 1];
				if (room != null) {
					p.getPacketSender().sendMessage("You did something retarded and now there is a under you for some reason.");
					p.getPacketSender().sendInterfaceRemoval();
				} else {
					createRoom(ConstructionData.OUBLIETTE, p, 103);
					p.getPacketSender().sendInterfaceRemoval();
				}
				return true;
			}
			if (p.getFields().getDialogueAction() == 416) {

				int myTiles[] = getMyChunk(p);
				Room room = p.getMapInstance().getOwner().houseRooms[0][myTiles[0] - 1][myTiles[1] - 1];
				Room room_1 = p.getMapInstance().getOwner().houseRooms[1][myTiles[0] - 1][myTiles[1] - 1];
				if (room.getType() != ConstructionData.EMPTY) {
					p.getPacketSender().sendMessage("You did something retarded and now there is a under above you for some reason.");
					p.getPacketSender().sendInterfaceRemoval();
				} else {
					createRoom(
							room_1.getType() == ConstructionData.SKILL_HALL_DOWN ? ConstructionData.SKILL_ROOM
									: ConstructionData.QUEST_ROOM, p, 101);
					p.getPacketSender().sendInterfaceRemoval();
				}
				return true;
			}

			if (p.getFields().getDialogueAction() == 414) {

				int myTiles[] = getMyChunk(p);
				Room room = p.getMapInstance().getOwner().houseRooms[1][myTiles[0] - 1][myTiles[1] - 1];
				Room room_1 = p.getMapInstance().getOwner().houseRooms[0][myTiles[0] - 1][myTiles[1] - 1];
				if (room != null) {
					p.getPacketSender().sendMessage("You did something retarded and now there is a room above you for some reason.");
					p.getPacketSender().sendInterfaceRemoval();
				} else {
					createRoom(
							room_1.getType() == ConstructionData.SKILL_ROOM ? ConstructionData.SKILL_HALL_DOWN
									: ConstructionData.QUEST_HALL_DOWN, p, 100);
					p.getPacketSender().sendInterfaceRemoval();
				}
				return true;
			}
			break;

		case 2462:// no option
			if(p.getFields().getDialogueAction() == 448 || p.getFields().getDialogueAction() == 444)
			{
				DialogueManager.start(p, ConstructionDialogues.finalServantDealDialogue(false));
				return true;
			}
			if(p.getFields().getDialogueAction() == 439)
			{
				p.getInventory().add(7673, 1);
				return true;
			}
			if (p.getFields().getDialogueAction() == 433) {
				p.getInventory().add(1927, 1);
				return true;
			}
			if (p.getFields().getDialogueAction() == 414 || p.getFields().getDialogueAction() == 416
					|| p.getFields().getDialogueAction() == 419 || p.getFields().getDialogueAction() == 457) {
				p.getPacketSender().sendInterfaceRemoval();
				return true;
			}
			break;

		case 28647:
			createRoom(ConstructionData.PARLOUR, p, p.getLocation().getZ());
			return true;
		case 28651:
			createRoom(ConstructionData.GARDEN, p, p.getLocation().getZ());
			return true;
		case 28655:
			createRoom(ConstructionData.KITCHEN, p, p.getLocation().getZ());
			return true;
		case 28659:
			createRoom(ConstructionData.DINING_ROOM, p, p.getLocation().getZ());
			return true;
		case 28663:
			createRoom(ConstructionData.WORKSHOP, p, p.getLocation().getZ());
			return true;
		case 28667:
			createRoom(ConstructionData.BEDROOM, p, p.getLocation().getZ());
			return true;
		case 28671:
			createRoom(ConstructionData.SKILL_ROOM, p, p.getLocation().getZ());
			return true;
		case 28675:
			createRoom(ConstructionData.GAMES_ROOM, p, p.getLocation().getZ());
			return true;
		case 28679:
			createRoom(ConstructionData.COMBAT_ROOM, p, p.getLocation().getZ());
			return true;
		case 28683:
			createRoom(ConstructionData.QUEST_ROOM, p, p.getLocation().getZ());
			return true;
		case 28687:
			createRoom(ConstructionData.MENAGERY, p, p.getLocation().getZ());
			return true;
		case 28691:
			createRoom(ConstructionData.STUDY, p, p.getLocation().getZ());
			return true;
		case 28695:
			createRoom(ConstructionData.COSTUME_ROOM, p, p.getLocation().getZ());
			return true;
		case 28699:
			createRoom(ConstructionData.CHAPEL, p, p.getLocation().getZ());
			return true;
		case 28703:
			createRoom(ConstructionData.PORTAL_ROOM, p, p.getLocation().getZ());
			return true;
		case 28707:
			createRoom(ConstructionData.FORMAL_GARDEN, p, p.getLocation().getZ());
			return true;
		case 28711:
			createRoom(ConstructionData.THRONE_ROOM, p, p.getLocation().getZ());
			return true;
		case 28715:
			createRoom(ConstructionData.OUBLIETTE, p, p.getLocation().getZ());
			return true;
		case 28719:
			createRoom(ConstructionData.CORRIDOR, p, p.getLocation().getZ());
			return true;
		case 28723:
			createRoom(ConstructionData.JUNCTION, p, p.getLocation().getZ());
			return true;
		case 28727:
			createRoom(ConstructionData.DUNGEON_STAIR_ROOM, p, p.getLocation().getZ());
			return true;
		case 28731:
			createRoom(ConstructionData.PIT, p, p.getLocation().getZ());
			return true;
		case 28735:
			createRoom(ConstructionData.TREASURE_ROOM, p, p.getLocation().getZ());
			return true;
		}
		return false;
	}

	public static void handleItemClick(int itemID, Player p) {
		if (p.getMapInstance().getOwner() != p)
			return;
		if (!p.inBuildingMode)
			return;
		Furniture f = ConstructionData.Furniture.forItemId(itemID);
		if (f == null)
			return;

		ArrayList<HotSpots> hsses = ConstructionData.HotSpots
				.forObjectId_2(p.buildFurnitureId);
		if (hsses.isEmpty())
			return;


		int[] myTiles = getMyChunk(p);
		int toHeight = (p.getMapInstance() instanceof HouseDungeon ? 4 : p.getLocation().getZ());
		int roomRot = p.getMapInstance().getOwner().houseRooms[toHeight][myTiles[0] - 1][myTiles[1] - 1]
				.getRotation();
		int myRoomType = p.getMapInstance().getOwner().houseRooms[toHeight][myTiles[0] - 1][myTiles[1] - 1].getType();
		HotSpots s = null;
		if (hsses.size() == 1) {
			s = hsses.get(0);
		} else {
			for (HotSpots find : hsses) {
				int actualX = ConstructionData.BASE_X + (myTiles[0] * 8);
				actualX += ConstructionData.getXOffsetForObjectId(
						find.getObjectId(), find, roomRot);
				int actualY = ConstructionData.BASE_Y + (myTiles[1] * 8);
				actualY += ConstructionData.getYOffsetForObjectId(
						find.getObjectId(), find, roomRot);
				if (p.buildFurnitureX == actualX
						&& p.buildFurnitureY == actualY
						&& myRoomType == find.getRoomType()
						|| find.getCarpetDim() != null && myRoomType == find.getRoomType()) {
					s = find;
					break;
				}
			}
		}
		if (s == null)
			return;

		if(!buildActions(p, f, s))
			return;
		int actualX = ConstructionData.BASE_X + (myTiles[0] * 8);
		actualX += ConstructionData.getXOffsetForObjectId(f.getFurnitureId(),
				s, p.getMapInstance().getOwner().houseRooms[toHeight][myTiles[0] - 1][myTiles[1] - 1]
						.getRotation());
		int actualY = ConstructionData.BASE_Y + (myTiles[1] * 8);
		actualY += ConstructionData.getYOffsetForObjectId(f.getFurnitureId(),
				s, roomRot);
		if(s.getRoomType() != myRoomType && s.getCarpetDim() == null)
		{
			p.getPacketSender().sendMessage("You can't build this furniture in this room.");
			return;
		}
		doFurniturePlace(s, f, hsses, myTiles, actualX, actualY, roomRot, p,
				false, p.getLocation().getZ());
		PlayerFurniture pf = new PlayerFurniture(myTiles[0] - 1,
				myTiles[1] - 1, toHeight, s.getHotSpotId(), f.getFurnitureId(),
				s.getXOffset(), s.getYOffset());
		p.playerFurniture.add(pf);
		p.getPacketSender().sendInterfaceRemoval();
		p.performAnimation(new Animation(3684));
	}

	public static void doFurniturePlace(HotSpots s, Furniture f,
			ArrayList<HotSpots> hsses, int[] myTiles, int actualX, int actualY,
			int roomRot, Player p, boolean placeBack, int height) {
		int portalId = -1;
		if(s.getHotSpotId() == 72)
		{
			if(s.getXOffset() == 0)
			{
				for(Portal portal : p.getMapInstance().getOwner().portals)
				{
					if(portal.getRoomX() == myTiles[0]-1 &&
							portal.getRoomY() == myTiles[1]-1 &&
							portal.getRoomZ() == height && portal.getId() == 0)
					{
						if(Portals.forType(portal.getType()).getObjects() != null)
							portalId = Portals.forType(portal.getType()).getObjects()[f.getFurnitureId()-13636];

					}
				}
			}
			if(s.getXOffset() == 3)
			{
				for(Portal portal : p.getMapInstance().getOwner().portals)
				{
					if(portal.getRoomX() == myTiles[0]-1 &&
							portal.getRoomY() == myTiles[1]-1 &&
							portal.getRoomZ() == height && portal.getId() == 1)
					{
						if(Portals.forType(portal.getType()).getObjects() != null)
							portalId = Portals.forType(portal.getType()).getObjects()[f.getFurnitureId()-13636];

					}
				}

			}
			if(s.getXOffset() == 7)
			{
				for(Portal portal : p.getMapInstance().getOwner().portals)
				{
					if(portal.getRoomX() == myTiles[0]-1 &&
							portal.getRoomY() == myTiles[1]-1 &&
							portal.getRoomZ() == height && portal.getId() == 2)
					{
						if(Portals.forType(portal.getType()).getObjects() != null)
							portalId = Portals.forType(portal.getType()).getObjects()[f.getFurnitureId()-13636];

					}
				}
			}
		}
		if (height == 4)
			height = 0;

		if (s.getHotSpotId() == 92) {
			int offsetX = ConstructionData.BASE_X + (myTiles[0] * 8);
			int offsetY = ConstructionData.BASE_Y + (myTiles[1] * 8);
			//System.out.println(s.getObjectId());
			if (s.getObjectId() == 15329 || s.getObjectId() == 15328) {
				p.getPacketSender().sendObject_cons(
						actualX,
						actualY,
						s.getObjectId() == 15328 ? (placeBack ? 15328 : f
								.getFurnitureId()) : (placeBack ? 15329 : f
										.getFurnitureId() + 1), s.getRotation(roomRot),
										0, height);
				offsetX += ConstructionData.getXOffsetForObjectId(
						f.getFurnitureId(), s.getXOffset()
						+ (s.getObjectId() == 15329 ? 1 : -1),
						s.getYOffset(), roomRot, s.getRotation(0));
				offsetY += ConstructionData.getYOffsetForObjectId(
						f.getFurnitureId(), s.getXOffset()
						+ (s.getObjectId() == 15329 ? 1 : -1),
						s.getYOffset(), roomRot, s.getRotation(0));
				p.getPacketSender().sendObject_cons(
						offsetX,
						offsetY,
						s.getObjectId() == 15329 ? (placeBack ? 15328 : f
								.getFurnitureId()) : (placeBack ? 15329 : f
										.getFurnitureId() + 1), s.getRotation(roomRot),
										0, height);

			}
			if (s.getObjectId() == 15326 || s.getObjectId() == 15327) {
				p.getPacketSender().sendObject_cons(
						actualX,
						actualY,
						s.getObjectId() == 15327 ? (placeBack ? 15327 : f
								.getFurnitureId() + 1) : (placeBack ? 15326 : f
										.getFurnitureId()), s.getRotation(roomRot), 0,
										height);
				offsetX += ConstructionData.getXOffsetForObjectId(
						f.getFurnitureId(), s.getXOffset()
						+ (s.getObjectId() == 15326 ? 1 : -1),
						s.getYOffset(), roomRot, s.getRotation(0));
				offsetY += ConstructionData.getYOffsetForObjectId(
						f.getFurnitureId(), s.getXOffset()
						+ (s.getObjectId() == 15326 ? 1 : -1),
						s.getYOffset(), roomRot, s.getRotation(0));
				p.getPacketSender().sendObject_cons(
						offsetX,
						offsetY,
						s.getObjectId() == 15326 ? (placeBack ? 15327 : f
								.getFurnitureId() + 1) : (placeBack ? 15326 : f
										.getFurnitureId()), s.getRotation(roomRot), 0,
										height);

			}
		} else if (s.getHotSpotId() == 85) {
			actualX = ConstructionData.BASE_X + (myTiles[0] * 8) + 2;
			actualY = ConstructionData.BASE_Y + (myTiles[1] * 8) + 2;
			int type = 22, leftObject = 0, rightObject = 0, upperObject = 0, downObject = 0, middleObject = 0, veryMiddleObject = 0, cornerObject = 0;
			if (f.getFurnitureId() == 13331) {
				leftObject = rightObject = upperObject = downObject = 13332;
				middleObject = 13331;
				cornerObject = 13333;
			}
			if (f.getFurnitureId() == 13334) {
				leftObject = rightObject = upperObject = downObject = 13335;
				middleObject = 13334;
				cornerObject = 13336;
			}
			if (f.getFurnitureId() == 13337) {
				leftObject = rightObject = upperObject = downObject = middleObject = cornerObject = 13337;
				type = 10;
			}
			if (f.getFurnitureId() == 13373) {
				veryMiddleObject = 13373;
				leftObject = rightObject = upperObject = downObject = middleObject = 6951;
			}
			if (placeBack || f.getFurnitureId() == 13337) {
				for (int x = 0; x < 4; x++) {
					for (int y = 0; y < 4; y++) {
						p.getPacketSender().sendObject_cons(actualX + x, actualY + y,
								6951, 0, 10, height);
						p.getPacketSender().sendObject_cons(actualX + x, actualY + y,
								6951, 0, 22, height);
					}
				}

			}
			p.getPacketSender().sendObject_cons(actualX, actualY,
					placeBack ? 15348 : cornerObject, 1, type, height);
			p.getPacketSender().sendObject_cons(actualX, actualY + 1,
					placeBack ? 15348 : leftObject, 1, type, height);
			p.getPacketSender().sendObject_cons(actualX, actualY + 2,
					placeBack ? 15348 : leftObject, 1, type, height);
			p.getPacketSender().sendObject_cons(actualX, actualY + 3,
					placeBack ? 15348 : cornerObject, 2, type, height);
			p.getPacketSender().sendObject_cons(actualX + 1, actualY + 3,
					placeBack ? 15348 : upperObject, 2, type, height);
			p.getPacketSender().sendObject_cons(actualX + 2, actualY + 3,
					placeBack ? 15348 : upperObject, 2, type, height);
			p.getPacketSender().sendObject_cons(actualX + 3, actualY + 3,
					placeBack ? 15348 : cornerObject, 3, type, height);
			p.getPacketSender().sendObject_cons(actualX + 3, actualY + 2,
					placeBack ? 15348 : rightObject, 3, type, height);
			p.getPacketSender().sendObject_cons(actualX + 3, actualY + 1,
					placeBack ? 15348 : rightObject, 3, type, height);
			p.getPacketSender().sendObject_cons(actualX + 3, actualY,
					placeBack ? 15348 : cornerObject, 0, type, height);
			p.getPacketSender().sendObject_cons(actualX + 2, actualY,
					placeBack ? 15348 : downObject, 0, type, height);
			p.getPacketSender().sendObject_cons(actualX + 1, actualY,
					placeBack ? 15348 : downObject, 0, type, height);
			p.getPacketSender().sendObject_cons(actualX + 1, actualY + 1,
					placeBack ? 15348 : middleObject, 0, type, height);
			p.getPacketSender().sendObject_cons(actualX + 2, actualY + 1,
					placeBack ? 15348 : middleObject, 0, type, height);
			if (veryMiddleObject != 0)
				p.getPacketSender().sendObject_cons(actualX + 1, actualY + 2,
						veryMiddleObject, 0, 10, height);
			p.getPacketSender().sendObject_cons(actualX + 1, actualY + 2,
					placeBack ? 15348 : middleObject, 0, type, height);
			p.getPacketSender().sendObject_cons(actualX + 2, actualY + 2,
					placeBack ? 15348 : middleObject, 0, type, height);

		} else if (s.getHotSpotId() == 86) {
			actualX = ConstructionData.BASE_X + (myTiles[0] * 8) + 2;
			actualY = ConstructionData.BASE_Y + (myTiles[1] * 8) + 2;

			p.getPacketSender().sendObject_cons(actualX + 1, actualY,
					placeBack ? 15352 : f.getFurnitureId(), 3, 0, height);
			p.getPacketSender().sendObject_cons(actualX + 2, actualY,
					placeBack ? 15352 : f.getFurnitureId(), 3, 0, height);
			p.getPacketSender().sendObject_cons(actualX + 3, actualY,
					placeBack ? 15352 : f.getFurnitureId(), 2, 2, height);
			p.getPacketSender().sendObject_cons(actualX + 3, actualY + 1,
					placeBack ? 15352 : f.getFurnitureId(), 2, 0, height);
			p.getPacketSender().sendObject_cons(actualX + 3, actualY + 2,
					placeBack ? 15352 : f.getFurnitureId() + 1, 2, 0, height);
			p.getPacketSender().sendObject_cons(actualX + 3, actualY + 3,
					placeBack ? 15352 : f.getFurnitureId(), 1, 2, height);
			p.getPacketSender().sendObject_cons(actualX + 2, actualY + 3,
					placeBack ? 15352 : f.getFurnitureId(), 1, 0, height);
			p.getPacketSender().sendObject_cons(actualX + 1, actualY + 3,
					placeBack ? 15352 : f.getFurnitureId(), 1, 0, height);
			p.getPacketSender().sendObject_cons(actualX, actualY + 3,
					placeBack ? 15352 : f.getFurnitureId(), 0, 2, height);
			p.getPacketSender().sendObject_cons(actualX, actualY + 2,
					placeBack ? 15352 : f.getFurnitureId(), 0, 0, height);
			p.getPacketSender().sendObject_cons(actualX, actualY + 1,
					placeBack ? 15352 : f.getFurnitureId(), 0, 0, height);
			p.getPacketSender().sendObject_cons(actualX, actualY,
					placeBack ? 15352 : f.getFurnitureId(), 3, 2, height);

		} else if (s.getHotSpotId() == 78) {
			actualX = ConstructionData.BASE_X + (myTiles[0] * 8);
			actualY = ConstructionData.BASE_Y + (myTiles[1] * 8);
			// south walls
			p.getPacketSender().sendObject_cons(actualX, actualY,
					placeBack ? 15369 : f.getFurnitureId(), 3, 2, height);
			p.getPacketSender().sendObject_cons(actualX + 1, actualY,
					placeBack ? 15369 : f.getFurnitureId(), 3, 0, height);
			p.getPacketSender().sendObject_cons(actualX + 2, actualY,
					placeBack ? 15369 : f.getFurnitureId(), 3, 0, height);
			p.getPacketSender().sendObject_cons(actualX + 5, actualY,
					placeBack ? 15369 : f.getFurnitureId(), 3, 0, height);
			p.getPacketSender().sendObject_cons(actualX + 6, actualY,
					placeBack ? 15369 : f.getFurnitureId(), 3, 0, height);
			p.getPacketSender().sendObject_cons(actualX + 7, actualY,
					placeBack ? 15369 : f.getFurnitureId(), 2, 2, height);
			// north walls
			p.getPacketSender().sendObject_cons(actualX, actualY + 7,
					placeBack ? 15369 : f.getFurnitureId(), 0, 2, height);
			p.getPacketSender().sendObject_cons(actualX + 1, actualY + 7,
					placeBack ? 15369 : f.getFurnitureId(), 1, 0, height);
			p.getPacketSender().sendObject_cons(actualX + 2, actualY + 7,
					placeBack ? 15369 : f.getFurnitureId(), 1, 0, height);
			p.getPacketSender().sendObject_cons(actualX + 5, actualY + 7,
					placeBack ? 15369 : f.getFurnitureId(), 1, 0, height);
			p.getPacketSender().sendObject_cons(actualX + 6, actualY + 7,
					placeBack ? 15369 : f.getFurnitureId(), 1, 0, height);
			p.getPacketSender().sendObject_cons(actualX + 7, actualY + 7,
					placeBack ? 15369 : f.getFurnitureId(), 1, 2, height);
			// left walls
			p.getPacketSender().sendObject_cons(actualX, actualY + 1,
					placeBack ? 15369 : f.getFurnitureId(), 0, 0, height);
			p.getPacketSender().sendObject_cons(actualX, actualY + 2,
					placeBack ? 15369 : f.getFurnitureId(), 0, 0, height);
			p.getPacketSender().sendObject_cons(actualX, actualY + 5,
					placeBack ? 15369 : f.getFurnitureId(), 0, 0, height);
			p.getPacketSender().sendObject_cons(actualX, actualY + 6,
					placeBack ? 15369 : f.getFurnitureId(), 0, 0, height);
			// right walls
			p.getPacketSender().sendObject_cons(actualX + 7, actualY + 1,
					placeBack ? 15369 : f.getFurnitureId(), 2, 0, height);
			p.getPacketSender().sendObject_cons(actualX + 7, actualY + 2,
					placeBack ? 15369 : f.getFurnitureId(), 2, 0, height);
			p.getPacketSender().sendObject_cons(actualX + 7, actualY + 5,
					placeBack ? 15369 : f.getFurnitureId(), 2, 0, height);
			p.getPacketSender().sendObject_cons(actualX + 7, actualY + 6,
					placeBack ? 15369 : f.getFurnitureId(), 2, 0, height);
		} else if (s.getHotSpotId() == 77) {
			actualX = ConstructionData.BASE_X + (myTiles[0] * 8);
			actualY = ConstructionData.BASE_Y + (myTiles[1] * 8);
			// left down corner
			p.getPacketSender().sendObject_cons(actualX, actualY,
					placeBack ? 15372 : f.getFurnitureId() + 1, 3, 10, height);
			p.getPacketSender().sendObject_cons(actualX + 1, actualY,
					placeBack ? 15371 : f.getFurnitureId() + 2, 0, 10, height);
			p.getPacketSender().sendObject_cons(actualX + 2, actualY,
					placeBack ? 15370 : f.getFurnitureId(), 0, 10, height);
			p.getPacketSender().sendObject_cons(actualX, actualY + 1,
					placeBack ? 15371 : f.getFurnitureId() + 2, 1, 10, height);
			p.getPacketSender().sendObject_cons(actualX, actualY + 2,
					placeBack ? 15370 : f.getFurnitureId(), 3, 10, height);
			// right down corner
			p.getPacketSender().sendObject_cons(actualX + 7, actualY,
					placeBack ? 15372 : f.getFurnitureId() + 1, 2, 10, height);
			p.getPacketSender().sendObject_cons(actualX + 6, actualY,
					placeBack ? 15371 : f.getFurnitureId() + 2, 0, 10, height);
			p.getPacketSender().sendObject_cons(actualX + 5, actualY,
					placeBack ? 15370 : f.getFurnitureId(), 2, 10, height);
			p.getPacketSender().sendObject_cons(actualX + 7, actualY + 1,
					placeBack ? 15371 : f.getFurnitureId() + 2, 3, 10, height);
			p.getPacketSender().sendObject_cons(actualX + 7, actualY + 2,
					placeBack ? 15370 : f.getFurnitureId(), 3, 10, height);
			// upper left corner
			p.getPacketSender().sendObject_cons(actualX, actualY + 7,
					placeBack ? 15372 : f.getFurnitureId() + 1, 0, 10, height);
			p.getPacketSender().sendObject_cons(actualX + 1, actualY + 7,
					placeBack ? 15371 : f.getFurnitureId() + 2, 0, 10, height);
			p.getPacketSender().sendObject_cons(actualX + 2, actualY + 7,
					placeBack ? 15370 : f.getFurnitureId(), 0, 10, height);
			p.getPacketSender().sendObject_cons(actualX, actualY + 6,
					placeBack ? 15371 : f.getFurnitureId() + 2, 1, 10, height);
			p.getPacketSender().sendObject_cons(actualX, actualY + 5,
					placeBack ? 15370 : f.getFurnitureId(), 1, 10, height);
			// upper right corner
			p.getPacketSender().sendObject_cons(actualX + 7, actualY + 7,
					placeBack ? 15372 : f.getFurnitureId() + 1, 1, 10, height);
			p.getPacketSender().sendObject_cons(actualX + 6, actualY + 7,
					placeBack ? 15371 : f.getFurnitureId() + 2, 0, 10, height);
			p.getPacketSender().sendObject_cons(actualX + 5, actualY + 7,
					placeBack ? 15370 : f.getFurnitureId(), 2, 10, height);
			p.getPacketSender().sendObject_cons(actualX + 7, actualY + 6,
					placeBack ? 15371 : f.getFurnitureId() + 2, 3, 10, height);
			p.getPacketSender().sendObject_cons(actualX + 7, actualY + 5,
					placeBack ? 15370 : f.getFurnitureId(), 1, 10, height);
		} else if (s.getHotSpotId() == 44) {
			int combatringStrings = 6951;
			int combatringFloorsCorner = 6951;
			int combatringFloorsOuter = 6951;
			int combatringFloorsInner = 6951;
			actualX = ConstructionData.BASE_X + (myTiles[0] * 8) + 1;
			actualY = ConstructionData.BASE_Y + (myTiles[1] * 8) + 1;
			if (!placeBack) {
				if (f.getFurnitureId() == 13126) {
					combatringStrings = 13132;
					combatringFloorsCorner = 13126;
					combatringFloorsOuter = 13128;
					combatringFloorsInner = 13127;
				}
				if (f.getFurnitureId() == 13133) {
					combatringStrings = 13133;
					combatringFloorsCorner = 13135;
					combatringFloorsOuter = 13134;
					combatringFloorsInner = 13136;
				}
				if (f.getFurnitureId() == 13137) {
					combatringStrings = 13137;
					combatringFloorsCorner = 13138;
					combatringFloorsOuter = 13139;
					combatringFloorsInner = 13140;
				}
			}

			p.getPacketSender().sendObject_cons(actualX + 2, actualY + 3,
					placeBack ? 15292 : combatringFloorsInner, 0, 22, height);
			p.getPacketSender().sendObject_cons(actualX + 3, actualY + 3,
					placeBack ? 15292 : combatringFloorsInner, 0, 22, height);
			p.getPacketSender().sendObject_cons(actualX + 3, actualY + 2,
					placeBack ? 15292 : combatringFloorsInner, 0, 22, height);
			p.getPacketSender().sendObject_cons(actualX + 2, actualY + 2,
					placeBack ? 15292 : combatringFloorsInner, 0, 22, height);
			p.getPacketSender().sendObject_cons(actualX + 2, actualY + 1,
					placeBack ? 15291 : combatringFloorsOuter, 3, 22, height);
			p.getPacketSender().sendObject_cons(actualX + 3, actualY + 1,
					placeBack ? 15291 : combatringFloorsOuter, 3, 22, height);
			p.getPacketSender().sendObject_cons(actualX + 2, actualY + 4,
					placeBack ? 15291 : combatringFloorsOuter, 1, 22, height);
			p.getPacketSender().sendObject_cons(actualX + 3, actualY + 4,
					placeBack ? 15291 : combatringFloorsOuter, 1, 22, height);
			p.getPacketSender().sendObject_cons(actualX + 4, actualY + 3,
					placeBack ? 15291 : combatringFloorsOuter, 2, 22, height);
			p.getPacketSender().sendObject_cons(actualX + 4, actualY + 2,
					placeBack ? 15291 : combatringFloorsOuter, 2, 22, height);
			p.getPacketSender().sendObject_cons(actualX + 1, actualY + 3,
					placeBack ? 15291 : combatringFloorsOuter, 0, 22, height);
			p.getPacketSender().sendObject_cons(actualX + 1, actualY + 2,
					placeBack ? 15291 : combatringFloorsOuter, 0, 22, height);
			p.getPacketSender().sendObject_cons(actualX + 4, actualY + 1,
					placeBack ? 15289 : combatringFloorsCorner, 3, 22, height);
			p.getPacketSender().sendObject_cons(actualX + 4, actualY + 4,
					placeBack ? 15289 : combatringFloorsCorner, 2, 22, height);
			p.getPacketSender().sendObject_cons(actualX + 1, actualY + 4,
					placeBack ? 15289 : combatringFloorsCorner, 1, 22, height);
			p.getPacketSender().sendObject_cons(actualX + 1, actualY + 1,
					placeBack ? 15289 : combatringFloorsCorner, 0, 22, height);
			p.getPacketSender().sendObject_cons(actualX, actualY + 4,
					placeBack ? 15277 : combatringStrings, 3, 0, height);
			p.getPacketSender().sendObject_cons(actualX, actualY + 1,
					placeBack ? 15277 : combatringStrings, 3, 0, height);
			p.getPacketSender().sendObject_cons(actualX + 5, actualY + 4,
					placeBack ? 15277 : combatringStrings, 3, 0, height);
			p.getPacketSender().sendObject_cons(actualX + 5, actualY + 1,
					placeBack ? 15277 : combatringStrings, 0, 3, height);
			p.getPacketSender().sendObject_cons(actualX + 1, actualY,
					placeBack ? 15277 : combatringStrings, 1, 0, height);
			p.getPacketSender().sendObject_cons(actualX + 2, actualY,
					placeBack ? 15277 : combatringStrings, 1, 0, height);
			p.getPacketSender().sendObject_cons(actualX + 3, actualY,
					placeBack ? 15277 : combatringStrings, 1, 0, height);
			p.getPacketSender().sendObject_cons(actualX + 4, actualY,
					placeBack ? 15277 : combatringStrings, 1, 0, height);
			p.getPacketSender().sendObject_cons(actualX + 5, actualY,
					placeBack ? 15277 : combatringStrings, 0, 3, height);
			p.getPacketSender().sendObject_cons(actualX + 1, actualY + 5,
					placeBack ? 15277 : combatringStrings, 3, 0, height);
			p.getPacketSender().sendObject_cons(actualX + 2, actualY + 5,
					placeBack ? 15277 : combatringStrings, 3, 0, height);
			p.getPacketSender().sendObject_cons(actualX + 3, actualY + 5,
					placeBack ? 15277 : combatringStrings, 3, 0, height);
			p.getPacketSender().sendObject_cons(actualX + 4, actualY + 5,
					placeBack ? 15277 : combatringStrings, 3, 0, height);
			p.getPacketSender().sendObject_cons(actualX + 5, actualY + 5,
					placeBack ? 15277 : combatringStrings, 3, 3, height);
			p.getPacketSender().sendObject_cons(actualX, actualY + 5,
					placeBack ? 15277 : combatringStrings, 2, 3, height);
			p.getPacketSender().sendObject_cons(actualX, actualY,
					placeBack ? 15277 : combatringStrings, 1, 3, height);
			p.getPacketSender().sendObject_cons(actualX, actualY + 4,
					placeBack ? 15277 : combatringStrings, 2, 0, height);
			p.getPacketSender().sendObject_cons(actualX, actualY + 3,
					placeBack ? 15277 : combatringStrings, 2, 0, height);
			p.getPacketSender().sendObject_cons(actualX, actualY + 2,
					placeBack ? 15277 : combatringStrings, 2, 0, height);
			p.getPacketSender().sendObject_cons(actualX, actualY + 1,
					placeBack ? 15277 : combatringStrings, 2, 0, height);
			p.getPacketSender().sendObject_cons(actualX + 5, actualY + 4,
					placeBack ? 15277 : combatringStrings, 0, 0, height);
			p.getPacketSender().sendObject_cons(actualX + 5, actualY + 3,
					placeBack ? 15277 : combatringStrings, 0, 0, height);
			p.getPacketSender().sendObject_cons(actualX + 5, actualY + 2,
					placeBack ? 15277 : combatringStrings, 0, 0, height);
			p.getPacketSender().sendObject_cons(actualX + 5, actualY + 1,
					placeBack ? 15277 : combatringStrings, 0, 0, height);

			if (f.getFurnitureId() == 13145) {
				p.getPacketSender().sendObject_cons(actualX + 1, actualY + 1,
						placeBack ? 6951 : 13145, 0, 0, height);
				p.getPacketSender().sendObject_cons(actualX + 2, actualY + 1,
						placeBack ? 6951 : 13145, 0, 0, height);
				p.getPacketSender().sendObject_cons(actualX + 1, actualY,
						placeBack ? 6951 : 13145, 1, 0, height);
				p.getPacketSender().sendObject_cons(actualX + 1, actualY + 2,
						placeBack ? 6951 : 13145, 3, 0, height);
				if (!placeBack)
					p.getPacketSender().sendObject_cons(actualX + 1, actualY + 1, 13147,
							0, 22, height);

				p.getPacketSender().sendObject_cons(actualX + 3, actualY + 3,
						placeBack ? 6951 : 13145, 0, 0, height);
				p.getPacketSender().sendObject_cons(actualX + 4, actualY + 3,
						placeBack ? 6951 : 13145, 0, 0, height);
				p.getPacketSender().sendObject_cons(actualX + 3, actualY + 2,
						placeBack ? 6951 : 13145, 1, 0, height);
				p.getPacketSender().sendObject_cons(actualX + 3, actualY + 4,
						placeBack ? 6951 : 13145, 3, 0, height);
				if (!placeBack)
					p.getPacketSender().sendObject_cons(actualX + 3, actualY + 3, 13147,
							0, 22, height);
			}
			if (f.getFurnitureId() == 13142 && !placeBack) {
				p.getPacketSender().sendObject_cons(actualX + 2, actualY + 2, 13142, 0,
						22, height);
				p.getPacketSender().sendObject_cons(actualX + 2, actualY + 1, 13143, 0,
						22, height);
				p.getPacketSender().sendObject_cons(actualX + 2, actualY + 3, 13144, 1,
						22, height);

			}
		} else if (s.getCarpetDim() != null) {
			for (int x = 0; x < s.getCarpetDim().getWidth() + 1; x++) {
				for (int y = 0; y < s.getCarpetDim().getHeight() + 1; y++) {
					boolean isEdge = (x == 0 && y == 0 || x == 0
							&& y == s.getCarpetDim().getHeight() || y == 0
							&& x == s.getCarpetDim().getWidth() || x == s
							.getCarpetDim().getWidth()
							&& y == s.getCarpetDim().getHeight());
					boolean isWall = ((x == 0 || x == s.getCarpetDim()
							.getWidth())
							&& (y != 0 && y != s.getCarpetDim().getHeight()) || (y == 0 || y == s
							.getCarpetDim().getHeight())
							&& (x != 0 && x != s.getCarpetDim().getWidth()));
					int rot = 0;
					if (x == 0 && y == s.getCarpetDim().getHeight() && isEdge)
						rot = 0;
					if (x == s.getCarpetDim().getWidth()
							&& y == s.getCarpetDim().getHeight() && isEdge)
						rot = 1;
					if (x == s.getCarpetDim().getWidth() && y == 0 && isEdge)
						rot = 2;
					if (x == 0 && y == 0 && isEdge)
						rot = 3;
					if (y == 0 && isWall)
						rot = 2;
					if (y == s.getCarpetDim().getHeight() && isWall)
						rot = 0;
					if (x == 0 && isWall)
						rot = 3;
					if (x == s.getCarpetDim().getWidth() && isWall)
						rot = 1;
					int offsetX = ConstructionData.BASE_X + (myTiles[0] * 8);
					int offsetY = ConstructionData.BASE_Y + (myTiles[1] * 8);
					offsetX += ConstructionData.getXOffsetForObjectId(
							f.getFurnitureId(), s.getXOffset() + x - 1,
							s.getYOffset() + y - 1, roomRot,
							s.getRotation(roomRot));
					offsetY += ConstructionData.getYOffsetForObjectId(
							f.getFurnitureId(), s.getXOffset() + x - 1,
							s.getYOffset() + y - 1, roomRot,
							s.getRotation(roomRot));
					if (isEdge)
						p.getPacketSender().sendObject_cons(
								offsetX,
								offsetY,
								placeBack ? s.getObjectId() + 2 : f
										.getFurnitureId(),
										HotSpots.getRotation_2(rot, roomRot), 22,
										height);
					else if (isWall)
						p.getPacketSender().sendObject_cons(
								offsetX,
								offsetY,
								placeBack ? s.getObjectId() + 1 : f
										.getFurnitureId() + 1,
										HotSpots.getRotation_2(rot, roomRot),
										s.getObjectType(), height);
					else
						p.getPacketSender().sendObject_cons(
								offsetX,
								offsetY,
								placeBack ? s.getObjectId() : f
										.getFurnitureId() + 2,
										HotSpots.getRotation_2(rot, roomRot),
										s.getObjectType(), height);
				}
			}
		} else if (s.isMutiple()) {

			Room room = p.getMapInstance().getOwner().houseRooms[p.getFields().inDungeon() ? 4 : p.getLocation().getZ()][myTiles[0] - 1][myTiles[1] - 1];
			for (HotSpots find : hsses) {
				if (find.getObjectId() != s.getObjectId())
					continue;
				if (room != null)
					if (room.getType() != find.getRoomType())
						continue;
				int actualX1 = ConstructionData.BASE_X + (myTiles[0] * 8);
				actualX1 += ConstructionData.getXOffsetForObjectId(
						find.getObjectId(), find, roomRot);
				int actualY1 = ConstructionData.BASE_Y + (myTiles[1] * 8);
				actualY1 += ConstructionData.getYOffsetForObjectId(
						find.getObjectId(), find, roomRot);

				p.getPacketSender()
				.sendObject_cons(
						actualX1,
						actualY1,
						placeBack ? s.getObjectId() : f
								.getFurnitureId(),
								find.getRotation(roomRot),
								find.getObjectType(), height);
			}
		} else {
			p.getPacketSender().sendObject_cons(actualX, actualY,
					(portalId != -1 ? portalId : placeBack ? s.getObjectId() : f.getFurnitureId()),
					s.getRotation(roomRot), s.getObjectType(), height);
		}
	}

	public static void handleThirdObjectClick(final int obX, final int obY,
			int objectId, final Player p) {
		switch(objectId)
		{
		case 13326:
		case 13323:
		case 13320:
		case 13317:
		case 13314:
			int[] myTiles = getMyChunk(p);
			if(myTiles == null)
				return;
			Room r = p.getMapInstance().getOwner().houseRooms[p.getFields().inDungeon() ? 4 : p.getLocation().getZ()][myTiles[0]-1][myTiles[1]-1];
			if(r.getType() != ConstructionData.OUBLIETTE)
				return;
			p.getPacketSender().sendMessage("You attempt to force the door...");
			final int[] myTiles_ = myTiles;
			GameServer.getTaskScheduler().schedule(new Task(3, p, false) {
				@Override
				public void execute() {
					if(Misc.random(3) == 1) {
						p.moveTo(new Position(ConstructionData.BASE_X+(myTiles_[0]*8) + 6, 
								ConstructionData.BASE_Y+(myTiles_[1]*8) + 4 , p.getLocation().getZ()));
						p.getPacketSender().sendMessage("You forced the door.");
					} else {
						p.getPacketSender().sendMessage("You failed to force the door");
					}
					this.stop();
				}

			});
			break;
		}
	}
	public static void handleSecondObjectClick(final int obX, final int obY,
			int objectId, final Player p) {
		switch (objectId) {

		case 13326:
		case 13323:
		case 13320:
		case 13317:
		case 13314:
			int[] myTiles = getMyChunk(p);
			if(myTiles == null)
				return;
			Room r = p.getMapInstance().getOwner().houseRooms[p.getFields().inDungeon() ? 4 : p.getLocation().getZ()][myTiles[0]-1][myTiles[1]-1];
			if(r.getType() != ConstructionData.OUBLIETTE)
				return;
			p.getPacketSender().sendMessage("You attempt to pick-lock the door...");
			final int[] myTiles_ = myTiles;
			
			
			
			
			TaskQueue.queue(new Task(3, p, false) {
				public void run() {
					if(Misc.randomNumber(3) == 1) {
						p.teleport(new Location(ConstructionData.BASE_X+(myTiles_[0]*8) + 6, 
								ConstructionData.BASE_Y+(myTiles_[1]*8) + 4 , p.getLocation().getZ()));
						p.getPacketSender().sendMessage("You pick-locked the door.");
					} else {
						p.getPacketSender().sendMessage("You failed to pick-lock the door");
					}
					this.stop();
				}

			});
			break;
		case 13347:
		case 13346:
		case 13344:
		case 13345:
		case 13348:
		case 13349:
			myTiles = getMyChunk(p);
			r = p.getMapInstance().getOwner().houseRooms[4][myTiles[0]-1][myTiles[1]-1];
			if(r.getType() != ConstructionData.CORRIDOR
					&& r.getType() != ConstructionData.DUNGEON_STAIR_ROOM
					&& r.getType() != ConstructionData.TREASURE_ROOM)
				break;
			p.getPacketSender().sendMessage("It's locked");
			final Room r_ = r;
			GameServer.getTaskScheduler().schedule(new Task(3, p, false) {
				@Override
				public void execute() {
					if(Misc.random(3) == 1) {
						if(p.getLocation().getX() == obX && p.getLocation().getY() == obY-1)
							p.moveTo(new Position(p.getLocation().getX(), p.getLocation().getY()+1 , p.getLocation().getZ()));
						else if(p.getLocation().getX() == obX && p.getLocation().getY() == obY && (r_.getRotation() == 0 || r_.getRotation() == 2))
							p.moveTo(new Position(p.getLocation().getX(), p.getLocation().getY()-1 , p.getLocation().getZ()));

						else if(p.getLocation().getX() == obX && p.getLocation().getY() == obY && (r_.getRotation() == 1 || r_.getRotation() == 3))
							p.moveTo(new Position(p.getLocation().getX()+1, p.getLocation().getY() , p.getLocation().getZ()));
						else if(p.getLocation().getX() == obX+1 && p.getLocation().getY() == obY)
							p.moveTo(new Position(p.getLocation().getX()-1, p.getLocation().getY() , p.getLocation().getZ()));
						p.getPacketSender().sendMessage("You pick-locked the door.");
					} else {
						p.getPacketSender().sendMessage("You failed to pick-lock the door");
					}
					this.stop();
				}

			});
			break;
		case 13405:
			if(p.getMapInstance() == null)
				return;
			if(!(p.getMapInstance() instanceof House))
				return;
			House mi = (House)p.getMapInstance();
			if(mi.getOwner() == p)
			{
				mi.setLocked(!mi.isLocked());
				p.getPacketSender().sendMessage("House "+(mi.isLocked() ? "" : "un")+"locked");
			} else {
				p.getPacketSender().sendMessage("You need to be the owner of this house to lock it");
			}
			break;
		case 13678:
		case 13679:
		case 13680:
			myTiles = getMyChunkFor(obX, obY);
			if (myTiles == null)
				break;
			int rotation = p.getMapInstance().getOwner().houseRooms[p.getFields().inDungeon() ? 4 : p.getLocation().getZ()][myTiles[0] - 1][myTiles[1] - 1]
					.getRotation();
			HotSpots hs = HotSpots.THRONE_ROOM_TRAPDOOR;
			int object = (objectId == 13677 ? 13675 : 13676);
			if (objectId == 13677)
				object = 13679;
			p.getPacketSender().sendObject_cons(obX, obY, object,
					hs.getRotation(rotation), 22, p.getLocation().getZ());
			break;
		}
	}

	public static void handleFirstItemClick(Player p, int itemId)
	{
		if(itemId == 7677)
		{
			if(p.getMapInstance() == null || !(p.getMapInstance() instanceof House))
			{
				p.getInventory().delete(7677, 1);
				return;
			}
			Mob Mob = null;
			for(Mob Mob1 : p.getMapInstance().Mobs)
			{
				if(Mob1.getId() == 3954)
				{
					Mob = Mob1;
					break;
				}
			}
			if(Mob == null)
			{
				p.getPacketSender().sendMessage("There are no fairies hiding.");
				return;
			}
			int lastDistance = p.lastFairyDistance;
			int distance = p.distanceToPoint(Mob.getLocation().getX(), Mob.getLocation().getY());
			if(distance > lastDistance)
			{
				p.getPacketSender().sendMessage("Colder");
			} else if(distance < lastDistance)
			{
				p.getPacketSender().sendMessage("Warmer");
			} else {
				p.getPacketSender().sendMessage("Same");
			}
			p.lastFairyDistance = distance;
			if(distance == 0)
			{
				Mob.getFields().mapInstance = p.getMapInstance();
				Mob.getUpdateFlag().flag(Flag.APPEARANCE);
				Mob.forceChat("Congratulations, you won!");
				p.getInventory().add(7678,  1);
				final Mob Mobc = Mob;
				GameServer.getTaskScheduler().schedule(new Task(5, p, false) {
					@Override
					public void execute()
					{
						GameServer.getWorld().deregister(Mobc);
					}
				});
			}
		}
	}
	public static boolean handleFirstMobClick(final Player p, final Mob Mob)
	{
		final int MobId = Mob.getId();
		final int MobX = Mob.getLocation().getX();
		final int MobY = Mob.getLocation().getY();
		Butlers b = Butlers.forId(MobId);
		if(b != null)
		{
			if(!(p.getMapInstance() instanceof House || p.getMapInstance() instanceof HouseDungeon)) {
				DialogueManager.start(p, ConstructionDialogues.servantDialogue(p, b.getMobId()));
			} else {
				//DialogueManager.sendDialogues(p, (p.servantCharges == 0 && p.getMapInstance().getOwner() == p)  ? 456 : 451, b.getMobId());
				if(p.getMapInstance().getOwner() == p)
					((Servant) Mob).giveItems(p);
			}
		}
		switch(MobId)
		{
		case 3944:
			if(Mob.hangManStatus == 3953)
			{
				Mob.setTransformationId(3944);
				Mob.hangManAnswer = ConstructionData.HANGMANWORDS[Misc.random(ConstructionData.HANGMANWORDS.length-1)];
				Mob.hangManStatus = 3944;
				Mob.currentHangMan = "";
				for(int i = 0; i < Mob.hangManAnswer.length(); i++)
				{
					Mob.currentHangMan += "-";
				}
			} else {
				p.getPacketSender().commandFrame(1);
			}
			return true;
		case 4097:
		case 3957:
		case 4162:
			if(Mob.lastAttackerStone != null && p == Mob.lastAttackerStone && p.getMapInstance().members.size() > 1)
			{
				p.getPacketSender().sendMessage("You can't attack the stone twice in a row");
				return true;
			}
			p.performAnimation(p.getAttackAnimation());
			GameServer.getTaskScheduler().schedule(new Task(1, p, false) {
				@Override
				protected void execute() {
					Mob.setDamage(new Damage(new Hit(1 + Misc.random(5), CombatIcon.NONE, Hitmask.NONE)));
					stop();
				}
			});
			Mob.lastAttackerStone = p;
			return true;

		case 3955:
			House house = (House)(Mob.getFields().mapInstance);
			final int myTiles[] = Construction.getMyChunkFor(MobX, MobY);
			ArrayList<PlayerFurniture> pfs = house.getActivatedObject(myTiles[0]-1, myTiles[1]-1, Mob.getFields().inDungeon() ? 4 : Mob.getLocation().getZ());
			for(PlayerFurniture pf : pfs)
			{
				if(pf.getFurnitureId() == 13390)
					house.getFurnitureActivated().remove(pf);
			}
			GameServer.getWorld().deregister(Mob);
			GameServer.getTaskScheduler().schedule(new Task(1, p, false) {
				@Override
				public void execute()
				{
					p.getPacketSender().sendGraphic(new Graphic(611), new Position(ConstructionData.BASE_X+(myTiles[0]*8)+3, ConstructionData.BASE_Y+(myTiles[1]*8)+3));
					this.stop();
				}
			});
			return true;
		default:

			return false;
		}
	}
	private static int getAttackStone(int object)
	{
		switch(object)
		{
		case 13392:
			return 4097;
		case 13393:
			return 3957;
		case 13394:
			return 4162;
		}
		return -1;
	}
	private static int getEleBalance(int object)
	{
		switch(object)
		{
		case 13395:
			return 4021;
		case 13396:
			return 4046;
		case 13397:
			return 4071;
		}
		return -1;
	}
	public static void handleSecondMobClick(final Mob Mob, final Player p)
	{
		int MobId = Mob.getId();
		int MobX = Mob.getLocation().getX();
		int MobY = Mob.getLocation().getY();
		if(MobId == 3944)
		{
			House house = (House)(Mob.getFields().mapInstance);
			int myTiles[] = Construction.getMyChunkFor(MobX, MobY);
			ArrayList<PlayerFurniture> pfs = house.getActivatedObject(myTiles[0]-1, myTiles[1]-1, Mob.getFields().inDungeon() ? 4 : Mob.getLocation().getZ());
			for(PlayerFurniture pf : pfs)
			{
				if(pf.getFurnitureId() == 13404)
					house.getFurnitureActivated().remove(pf);
			}
			GameServer.getWorld().deregister(Mob);
		}
		if(MobId >= 4021 && MobId <= 4095)
		{
			House house = (House)(Mob.getFields().mapInstance);
			int myTiles[] = Construction.getMyChunkFor(MobX, MobY);
			ArrayList<PlayerFurniture> pfs = house.getActivatedObject(myTiles[0]-1, myTiles[1]-1, Mob.getFields().inDungeon() ? 4 : Mob.getLocation().getZ());
			for(PlayerFurniture pf : pfs)
			{
				if(pf.getFurnitureId() == 13395 || pf.getFurnitureId() == 13396
						|| pf.getFurnitureId() == 13397)
					house.getFurnitureActivated().remove(pf);
			}
			
			
			
			TaskQueue.queue(new Task(1, p, false) {
				int ticks = 0;
				public void run() {
					Mob.getUpdateFlags().sendAnimation(new Animation(3585));
					if(ticks == 3)
					{
						Mob.remove();
						this.stop();
					}
					ticks++;
				}
			});
		}
	}
	private final static int[] EMOTES = new int[] 
			{
		855, 856, 857, 858, 859, 860, 861, 862, 864,
		865, 866, 2105, 2106, 2107, 2108, 2109, 2110, 2111,
		2112, 2113, 1374, 0x46b, 0x46a, 0x469, 0x468,
		0x84F, 0x850, 2836, 3544, 3543, 6111
			};
	public static void handleJester(Player p, int anim)
	{
		int[] myTiles = getMyChunk(p);
		if(myTiles == null)
			return;
		if(myTiles[0] == -1 || myTiles[1] == -1)
			return;
		Room r = p.getMapInstance().getOwner().houseRooms[p.getFields().inDungeon() ? 4 : p.getLocation().getZ()][myTiles[0]-1][myTiles[1]-1];
		if(r == null)
			return;
		if(r.getType() != ConstructionData.GAMES_ROOM)
			return;
		Mob Mob = null;
		for(Mob Mob_1 : p.getMapInstance().Mobs)
		{
			if(Mob_1 == null)
				continue;
			int[] tiles = getMyChunkFor(Mob_1.getLocation().getX(), Mob_1.getLocation().getY());
			if(tiles == null)
				return;
			if(tiles[1] == myTiles [1] && tiles[0] == myTiles[0] && p.getLocation().getZ() == Mob_1.getLocation().getZ()
					&& Mob_1.getId() == 3955)
			{
				Mob = Mob_1;
				break;
			}
		}
		if(Mob == null)
			return;
		if(anim == Mob.jesterAnim)
		{
			if(p.jesterEmotes == 9)
			{
				p.jesterEmotes = 0;
				Mob.forceChat(p.getUsername()+" won!");
			} else {
				p.jesterEmotes++;
			}
		}
	}
	public static void handleHangman(Player p, String s)
	{

		int[] myTiles = getMyChunk(p);
		Room r = p.getMapInstance().getOwner().houseRooms[p.getFields().inDungeon() ? 4 : p.getLocation().getZ()][myTiles[0]-1][myTiles[1]-1];
		if(r.getType() != ConstructionData.GAMES_ROOM)
			return;

		Mob Mob = null;
		for(Mob Mob_1 : p.getMapInstance().Mobs)
		{
			int[] tiles = getMyChunkFor(Mob_1.getLocation().getX(), Mob_1.getLocation().getY());
			if(tiles[1] == myTiles [1] && tiles[0] == myTiles[0] && p.getLocation().getZ() == Mob_1.getLocation().getZ()
					&& Mob_1.getId() == 3944)
			{
				Mob = Mob_1;
				break;
			}
		}
		if(Mob == null)
			return;
		p.getPacketSender().sendMessage(s);
		s = s.toLowerCase();
		String answer = Mob.hangManAnswer.toLowerCase();
		String ss = "";
		int correct = 0;
		for(int i = 0; i < answer.length(); i++)
		{
			if(!Mob.currentHangMan.substring(i, i+1).equals("-"))
			{
				ss += Mob.currentHangMan.charAt(i);
			} else
				if(s.equals(answer.substring(i, i+1)))
				{
					ss += s;
					correct++;
				} else {
					ss += "-";
				}
		}
		p.getPacketSender().sendMessage(Mob.currentHangMan);
		Mob.currentHangMan = ss;
		if(correct == 0)
		{
			if(Mob.hangManStatus == 3953)
			{
				Mob.forceChat("Fail you lost lol");
				return;
			}
			Mob.setTransformationId(Mob.hangManStatus+1);
			Mob.hangManStatus++;
		} else {
			if(!Mob.currentHangMan.contains("-"))
			{
				Mob.forceChat("You won!");
				Mob.setTransformationId(3944);
				Mob.hangManAnswer = ConstructionData.HANGMANWORDS[Misc.random(ConstructionData.HANGMANWORDS.length-1)];
				Mob.hangManStatus = 3944;
				Mob.currentHangMan = "";
				for(int i = 0; i < Mob.hangManAnswer.length(); i++)
				{
					Mob.currentHangMan += "-";
				}
				return;
			}
		}
		Mob.forceChat(Mob.currentHangMan);

	}
	public static Room getCurrentRoom(Player p)
	{
		Player owner = p.getMapInstance().getOwner();
		int[] myTiles = getMyChunk(p);
		return owner.houseRooms[p.getFields().inDungeon() ? 4 : p.getLocation().getZ()][myTiles[0]-1][myTiles[1]-1];
	}
	public static void handleFirstObjectClick(final int obX, final int obY,
			final int objectId, final Player p) {

		for(Portals ps : Portals.values())
		{
			if(ps == Portals.EMPTY)
				continue;
			for(int i : ps.getObjects())
			{
				if(i == objectId)
				{
					if(getCurrentRoom(p).getType() == ConstructionData.PORTAL_ROOM)
					{
						p.getMapInstance().removePlayer(p);
						p.moveTo(new Position(ps.getDestination().getX(), ps.getDestination().getY(), 0));
						return;
					}
				}
			}
		}
		switch (objectId) {
		case 15477:
		case 15482:
		case 15478:
			DialogueManager.start(p, ConstructionDialogues.mainPortalDialogue());
			p.getFields().setDialogueAction(442);
			break;
		case 13640:
		case 13641:
		case 13639:
			if(getCurrentRoom(p).getType() != ConstructionData.PORTAL_ROOM)
				break;
			p.getFields().setDialogueAction(645);
			DialogueManager.start(p, ConstructionDialogues.redirectPortalsDialogue());
			break;
		case 13523:
			if(getCurrentRoom(p).getType() != ConstructionData.QUEST_ROOM
			&& getCurrentRoom(p).getType() != ConstructionData.QUEST_HALL_DOWN)
				break;
			p.getFields().setDialogueAction(644);
			DialogueManager.start(p, ConstructionDialogues.gloryTeleportDialogue());
			break;
		case 13481:
			if(p.getMapInstance().getOwner() == p){
				int random = Misc.random(2);
				if(random == 0)
					DialogueManager.start(p, ConstructionDialogues.crawlingHandDialogue1());
				else if(random == 1)
					DialogueManager.start(p, ConstructionDialogues.crawlingHandDialogue2());
				else if(random == 2)
					DialogueManager.start(p, ConstructionDialogues.crawlingHandDialogue3());
			} else if(p.getMapInstance() != null)
				DialogueManager.start(p, ConstructionDialogues.crawlingHandDialogue4(p));
			break;
		case 13482:
			if(p.getMapInstance().getOwner() == p)
				DialogueManager.start(p, ConstructionDialogues.cockatriceDialogue1());
			else
				//DialogueManager.sendDialogues(p, 630, 4227);
				break;
			/*case 13483:
			if(p.getMapInstance().getOwner() == p)
				//DialogueManager.sendDialogues(p, 568, 4228);
				else
					//DialogueManager.sendDialogues(p, 605, 4228);
					break;
		case 13484:
			if(p.getMapInstance().getOwner() == p)
				//DialogueManager.sendDialogues(p, 512, 4229);
				else
					//DialogueManager.sendDialogues(p, 566, 4229);
					break;
		case 13485:
			if(p.getMapInstance().getOwner() == p)
				//DialogueManager.sendDialogues(p, 490, 4230);
				else
					//DialogueManager.sendDialogues(p, 510, 4230);
					break;
		case 13486:
			if(p.getMapInstance().getOwner() == p)
				//DialogueManager.sendDialogues(p, 458, 4232);
				else
					//DialogueManager.sendDialogues(p, 466, 4232);
					break;
		case 13487:
			if(p.getMapInstance().getOwner() == p)
				//DialogueManager.sendDialogues(p, 476, 4234);
				else
					//DialogueManager.sendDialogues(p, 487, 4234);
					break;*//*construcionremoved
		case 13405:
			if(p.getMapInstance() == null)
				return;
			if(!(p.getMapInstance() instanceof House))
				return;
			MapInstance mi = p.getMapInstance();
			if(mi.getOwner() == p)
			{
				mi.destroy();
			} else {
				mi.removePlayer(p);
			}
			break;
		case 13307:
		case 13308:
		case 13309:
			final Servant s = ((House)p.getMapInstance()).getButler();
			if(s == null)
			{
				p.getPacketSender().sendMessage("Ding says the bell.");
				return;
			}
			s.moveTo(p.getLocation().copy());
			final MapInstance mi_ = p.getMapInstance();
			s.getFields().mapInstance = null;
			//s.randomWalk = false;
			GameServer.getTaskScheduler().schedule(new Task(3, p, false) {
				@Override
				public void execute()
				{
					s.getFields().mapInstance = mi_;
					this.stop();
				}
			});
			break;

		case 13326:
		case 13323:
		case 13320:
		case 13317:
		case 13314:
			int[] myTiles = getMyChunk(p);
			if(myTiles == null)
				return;
			Room r = p.getMapInstance().getOwner().houseRooms[p.getFields().inDungeon() ? 4 : p.getLocation().getZ()][myTiles[0]-1][myTiles[1]-1];
			if(r.getType() != ConstructionData.OUBLIETTE)
				return;
			p.getPacketSender().sendMessage("It's locked.");
			p.moveTo(new Position(ConstructionData.BASE_X+(myTiles[0]*8) + 6, 
					ConstructionData.BASE_Y+(myTiles[1]*8) + 4 , p.getLocation().getZ()));
			break;

		case 13381:
			//DialogueManager.sendDialogues(p, 439, 1);
			break;
		case 13382:
			//DialogueManager.sendDialogues(p, 440, 1);
			break;
		case 13383:
			//DialogueManager.sendDialogues(p, 441, 1);
			break;
			/**
			 * Dungeon doors
			 *//*//constructionremoved
		case 13347:
		case 13346:
		case 13344:
		case 13345:
		case 13348:
		case 13349:
			myTiles = getMyChunk(p);
			r = p.getMapInstance().getOwner().houseRooms[4][myTiles[0]-1][myTiles[1]-1];
			if(r.getType() != ConstructionData.CORRIDOR
					&& r.getType() != ConstructionData.DUNGEON_STAIR_ROOM
					&& r.getType() != ConstructionData.TREASURE_ROOM)
				break;
			p.getPacketSender().sendMessage("It's locked");
			break;
		case 13132:
		case 13133:
		case 13137:
			myTiles = getMyChunk(p);
			if(myTiles == null)
				return;
			r = p.getMapInstance().getOwner().houseRooms[p.getFields().inDungeon() ? 4 : p.getLocation().getZ()][myTiles[0]-1][myTiles[1]-1];
			if(r.getType() != ConstructionData.COMBAT_ROOM)
				return;
			boolean canGoIn = true;
			if(objectId == 13132)
			{
				if(p.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() != -1
						|| p.getEquipment().getItems()[Equipment.CAPE_SLOT].getId() != -1
						|| p.getEquipment().getItems()[Equipment.AMULET_SLOT].getId() != -1
						|| p.getEquipment().getItems()[Equipment.AMMUNITION_SLOT].getId() != -1
						|| p.getEquipment().getItems()[Equipment.BODY_SLOT].getId() != -1
						|| p.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() != -1
						|| p.getEquipment().getItems()[Equipment.LEG_SLOT].getId() != -1
						|| p.getEquipment().getItems()[Equipment.HANDS_SLOT].getId() != -1
						|| p.getEquipment().getItems()[Equipment.FEET_SLOT].getId() != -1
						|| p.getEquipment().getItems()[Equipment.RING_SLOT].getId() != -1
						)
					canGoIn = false;
				if((p.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() != 7671
						&& p.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() != 7673)
						&& p.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() != -1)
					canGoIn = true;
				{

				}
			}
			if(objectId == 13133)
			{
				if(p.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() != -1
						|| p.getEquipment().getItems()[Equipment.CAPE_SLOT].getId() != -1
						|| p.getEquipment().getItems()[Equipment.AMULET_SLOT].getId() != -1
						|| p.getEquipment().getItems()[Equipment.AMMUNITION_SLOT].getId() != -1
						|| p.getEquipment().getItems()[Equipment.BODY_SLOT].getId() != -1
						|| p.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() != -1
						|| p.getEquipment().getItems()[Equipment.LEG_SLOT].getId() != -1
						|| p.getEquipment().getItems()[Equipment.HANDS_SLOT].getId() != -1
						|| p.getEquipment().getItems()[Equipment.FEET_SLOT].getId() != -1
						|| p.getEquipment().getItems()[Equipment.RING_SLOT].getId() != -1)
					canGoIn = false;
			}
			if(!canGoIn)
			{
				p.getPacketSender().sendMessage("You can't wear this equipment.");
				return;
			}
			int xOnTile = getXTilesOnTile(myTiles, p.getLocation().getX());
			int yOnTile = getYTilesOnTile(myTiles, p.getLocation().getY());
			if((xOnTile >= 2 && xOnTile <= 5) && yOnTile == 1)
			{
				p.moveTo(new Position(p.getLocation().getX(), p.getLocation().getY()+1, p.getLocation().getZ()));
				p.combatRingType = objectId;
			}
			if((xOnTile >= 2 && xOnTile <= 5) && yOnTile == 2)
			{
				p.moveTo(new Position(p.getLocation().getX(), p.getLocation().getY()-1, p.getLocation().getZ()));
				p.combatRingType = 0;
			}

			if((xOnTile >= 2 && xOnTile <= 5) && yOnTile == 6)
			{
				p.moveTo(new Position(p.getLocation().getX(), p.getLocation().getY()-1, p.getLocation().getZ()));
				p.combatRingType = objectId;
			}
			if((xOnTile >= 2 && xOnTile <= 5) && yOnTile == 5)
			{
				p.moveTo(new Position(p.getLocation().getX(), p.getLocation().getY()+1, p.getLocation().getZ()));
				p.combatRingType = 0;
			}


			if((yOnTile >= 2 && yOnTile <= 5) && xOnTile == 1)
			{
				p.moveTo(new Position(p.getLocation().getX()+1, p.getLocation().getY(), p.getLocation().getZ()));
				p.combatRingType = objectId;
			}
			if((yOnTile >= 2 && yOnTile <= 5) && xOnTile == 2)
			{
				p.moveTo(new Position(p.getLocation().getX()-1, p.getLocation().getY(), p.getLocation().getZ()));
				p.combatRingType = 0;
			}

			if((yOnTile >= 2 && yOnTile <= 5) && xOnTile == 6)
			{
				p.moveTo(new Position(p.getLocation().getX()-1, p.getLocation().getY(), p.getLocation().getZ()));
				p.combatRingType = objectId;
			}
			if((yOnTile >= 2 && yOnTile <= 5) && xOnTile == 5)
			{
				p.moveTo(new Position(p.getLocation().getX()+1, p.getLocation().getY(), p.getLocation().getZ()));
				p.combatRingType = 0;
			}
			break;
		case 13399:
		case 13400:
		case 13401:
		case 13402:
			myTiles = getMyChunk(p);
			r = p.getMapInstance().getOwner().houseRooms[p.getFields().inDungeon() ? 4 : p.getLocation().getZ()][myTiles[0]-1][myTiles[1]-1];
			p.resetChairAnim = false;
			p.getMovementQueue().stopMovement();
			GameServer.getTaskScheduler().schedule(new Task(1, p, false) {
				int ticks, ticks1 = 0;
				int succes = 0;
				@Override
				public void execute()
				{
					if(ticks == 0)
						p.resetChairAnim = false;
					if(p.resetChairAnim) {
						this.stop();
						return;
					}
					boolean range = RangedData.usingRanged(p);
					boolean can = RangedData.correctEquipment(p);
					if(objectId == 13402)
					{
						if(!range)
						{
							p.getPacketSender().sendMessage("You need a ranged weapon to do this");
							this.stop();
							return;
						}
						if(!can) {
							this.stop();
							return;
						}
					}
					if(objectId == 13400)
					{
						if(!RangedData.usingThrowWeapons(p.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId()))
						{
							p.getPacketSender().sendMessage("You need a throw weapon to do this.");
							this.stop();
							return;
						}
					}
					if(ticks1 == 1)
					{
						if(objectId == 13399)
							p.performAnimation(new Animation(3602));
						if(objectId == 13400)
						{
							p.performAnimation(new Animation(3605));
						}
						if(objectId == 13402)
						{
							p.performAnimation(p.weaponAnimations[0]);
						}
						if(objectId == 13400 || objectId == 13402)
							p.performGraphic(new Graphic(RangedData.getRangeProjectileGFX(p)));
					}
					if(ticks1 == 2) {
						boolean succes = (Misc.random(100) <= p.getSkillManager().getCurrentLevel(Skill.RANGED)) || Misc.random(5) == 1;
						if(succes)
							this.succes++;
						int offY = (p.getLocation().getX() - obX) * -1;
						int offX = (p.getLocation().getY() - obY) * -1;

						int gfx = (objectId == 13399 ? 612 : RangedData.getRangeProjectileGFX(p));
						//TODO p.getPacketSender().createPlayersProjectile(p.getLocation().getX(), p.getLocation().getY(), offX, offY, 50, 85,  gfx, 15, 40, -p.getId() - 1, 65);	TODO
					}
					if(ticks == 10)
					{
						p.resetChairAnim = false;
						p.getPacketSender().sendMessage("Targets hit: "+succes +"/"+ticks);
						p.performAnimation(new Animation(65535));
						this.stop();
					}
					if(ticks1 == 2)
					{
						ticks1 = 0;
						ticks++;
					} else
						ticks1++;
				}
			});
			break;

		case 13379:
			myTiles = getMyChunk(p);
			r = p.getMapInstance().getOwner().houseRooms[p.getFields().inDungeon() ? 4 : p.getLocation().getZ()][myTiles[0]-1][myTiles[1]-1];
			ArrayList<PlayerFurniture> pfs = ((House) p.getMapInstance()).getActivatedObject(myTiles[0]-1, myTiles[1]-1, p.getFields().inDungeon() ? 4 : p.getLocation().getZ());
			for(PlayerFurniture pp : pfs)
			{
				if(pp.getFurnitureId() == objectId)
				{
					p.getPacketSender().sendMessage("You cant reactivate");
					return;
				}
			}
			if(r.getType() == ConstructionData.GAMES_ROOM)
			{
				PlayerFurniture pf = new PlayerFurniture(myTiles[0]-1, myTiles[1]-1, p.getFields().inDungeon() ? 4 : p.getLocation().getZ()
						, 0, objectId, 0, 0);
				((House) p.getMapInstance()).getFurnitureActivated().add(pf);
				ArrayList<int[]> possibleRooms = new ArrayList<int[]>();
				for(int z = 0; z < 2; z++)
				{
					for(int x = 0; x < 13; x++)
					{
						for(int y = 0; y < 13; y++)
						{
							Room rr = p.getMapInstance().getOwner().houseRooms[z][x][y];
							if(rr == null) 
								continue;
							if(rr.getType() == ConstructionData.EMPTY)
								continue;
							possibleRooms.add(new int[] {ConstructionData.BASE_X+(x*8), ConstructionData.BASE_Y+(y*8), z});
						}
					}
				}
				int[] coord = possibleRooms.get(Misc.random(possibleRooms.size()));
				/*final Mob Mob = MobHandler.spawnMob(p, 3954, coord[0]+2, coord[1]+2, coord[2], 0, 100, 0, 0, 0, false, false);
				p.getMapInstance().addMob(Mob);
				Mob.getFields().mapInstance = null;
				for(Player p1 : p.getMapInstance().members)
				{
					int[] hisTiles = getMyChunk(p1);
					if(hisTiles[0] == myTiles[0] && hisTiles[1] == myTiles[1] && p.getLocation().getZ() == p1.getLocation().getZ())
					{
						p1.getInventory().add(7677, 1);
						p1.getPacketSender().sendMessage("These magic stones will guide you to me!");
					}
				}*//*//constructionremoved
			}
			break;
		case 13404:
			myTiles = getMyChunk(p);
			r = p.getMapInstance().getOwner().houseRooms[p.getFields().inDungeon() ? 4 : p.getLocation().getZ()][myTiles[0]-1][myTiles[1]-1];
			pfs = ((House) p.getMapInstance()).getActivatedObject(myTiles[0]-1, myTiles[1]-1, p.getFields().inDungeon() ? 4 : p.getLocation().getZ());
			for(PlayerFurniture pp : pfs)
			{
				if(pp.getFurnitureId() == objectId)
				{
					p.getPacketSender().sendMessage("You cant reactivate");
					return;
				}
			}
			if(r.getType() == ConstructionData.GAMES_ROOM)
			{
				PlayerFurniture pf = new PlayerFurniture(myTiles[0]-1, myTiles[1]-1, p.getFields().inDungeon() ? 4 : p.getLocation().getZ()
						, 0, objectId, 0, 0);
				((House) p.getMapInstance()).getFurnitureActivated().add(pf);
				final Mob Mob = MobManager.spawnMob(3944, new Position(obX, obY, p.getLocation().getZ()), 0, null, false, false);
				p.getMapInstance().addMob(Mob);
				String word = ConstructionData.HANGMANWORDS[Misc.random(ConstructionData.HANGMANWORDS.length-1)];
				Mob.hangManAnswer = word;
				Mob.currentHangMan = "";
				Mob.hangManStatus = 3944;
				for(int i = 0; i < word.length(); i++)
				{
					Mob.currentHangMan += "-";
				}
			}
			break;


		case 13390:
			myTiles = getMyChunk(p);
			r = p.getMapInstance().getOwner().houseRooms[p.getFields().inDungeon() ? 4 : p.getLocation().getZ()][myTiles[0]-1][myTiles[1]-1];
			pfs = ((House) p.getMapInstance()).getActivatedObject(myTiles[0]-1, myTiles[1]-1, p.getFields().inDungeon() ? 4 : p.getLocation().getZ());
			for(PlayerFurniture pp : pfs)
			{
				if(pp.getFurnitureId() == objectId)
				{
					p.getPacketSender().sendMessage("You cant reactivate");
					return;
				}
			}
			if(r.getType() == ConstructionData.GAMES_ROOM)
			{
				PlayerFurniture pf = new PlayerFurniture(myTiles[0]-1, myTiles[1]-1, p.getFields().inDungeon() ? 4 : p.getLocation().getZ()
						, 0, objectId, 0, 0);
				((House) p.getMapInstance()).getFurnitureActivated().add(pf);
				p.getPacketSender().sendGraphic(new Graphic(610), new Position(ConstructionData.BASE_X+(myTiles[0]*8)+3, ConstructionData.BASE_Y+(myTiles[1]*8)+3, 0));
				final Mob Mob = MobManager.spawnMob(3955, new Position(ConstructionData.BASE_X+(myTiles[0]*8)+3, ConstructionData.BASE_Y+(myTiles[1]*8)+3, p.getLocation().getZ()), 0, null, false, false);
				GameServer.getTaskScheduler().schedule(new Task(3, p, false) {
					@Override
					public void execute()
					{
						p.getMapInstance().addMob(Mob);
						this.stop();
					}
				});
				GameServer.getTaskScheduler().schedule(new Task(15, p, false) {
					@Override
					public void execute()
					{
						if(Mob == null)
							this.stop();
						if(GameServer.getWorld().registered(Mob))
							this.stop();
						int anim = EMOTES[Misc.random(EMOTES.length-1)];
						Mob.performAnimation(new Animation(anim));
						Mob.jesterAnim = anim;
					}
				});
			}
			break;

		case 13395:
		case 13396:
		case 13397:
			myTiles = getMyChunk(p);
			r = p.getMapInstance().getOwner().houseRooms[p.getFields().inDungeon() ? 4 : p.getLocation().getZ()][myTiles[0]-1][myTiles[1]-1];
			pfs = ((House) p.getMapInstance()).getActivatedObject(myTiles[0]-1, myTiles[1]-1, p.getFields().inDungeon() ? 4 : p.getLocation().getZ());
			for(PlayerFurniture pp : pfs)
			{
				if(pp.getFurnitureId() == objectId)
				{
					p.getPacketSender().sendMessage("You cant reactivate");
					return;
				}
			}
			if(r.getType() == ConstructionData.GAMES_ROOM)
			{
				PlayerFurniture pf = new PlayerFurniture(myTiles[0]-1, myTiles[1]-1, p.getFields().inDungeon() ? 4 : p.getLocation().getZ()
						, 0, objectId, 0, 0);
				((House) p.getMapInstance()).getFurnitureActivated().add(pf);
				final Mob Mob = MobManager.spawnMob(getEleBalance(objectId), new Position(obX, obY, p.getLocation().getZ()), 0, null, false, false);
				p.getMapInstance().addMob(Mob);
				Mob.performAnimation(new Animation(3583));
				GameServer.getTaskScheduler().schedule(new Task(3, p, false) {
					int ticks = 0;
					@Override
					public void execute()
					{
						ticks++;
						if(ticks == 1)
							Mob.performAnimation(new Animation(3584));
						else if(ticks == 3) {
							GameServer.getWorld().deregister(Mob);
							stop();
						}
					}
				});
			}
			break;

		case 13392:
		case 13393:
		case 13394:
			myTiles = getMyChunk(p);
			r = p.getMapInstance().getOwner().houseRooms[p.getFields().inDungeon() ? 4 : p.getLocation().getZ()][myTiles[0]-1][myTiles[1]-1];
			pfs = ((House) p.getMapInstance()).getActivatedObject(myTiles[0]-1, myTiles[1]-1, p.getFields().inDungeon() ? 4 : p.getLocation().getZ());
			for(PlayerFurniture pp : pfs)
			{
				if(pp.getFurnitureId() == objectId)
				{
					p.getPacketSender().sendMessage("You cant resetup");
					return;
				}
			}
			if(r.getType() == ConstructionData.GAMES_ROOM)
			{
				PlayerFurniture pf = new PlayerFurniture(myTiles[0]-1, myTiles[1]-1, p.getFields().inDungeon() ? 4 : p.getLocation().getZ()
						, 0, objectId, 0, 0);
				((House) p.getMapInstance()).getFurnitureActivated().add(pf);
				final Mob Mob = MobManager.spawnMob(getAttackStone(objectId), new Position(obX, obY, p.getLocation().getZ()), 0, null, false, false);
				p.getMapInstance().addMob(Mob);
			}
			break;
		case 13200:
		case 13202:
		case 13204:
		case 13206:
		case 13208:
		case 13210:
		case 13212: {

			myTiles = getMyChunk(p);
			if (!p.getInventory().contains(590)) {
				p.getPacketSender().sendMessage("You need a Tinderbox to light this.");
				return;
			}
			if (!p.getInventory().contains(251)) {
				p.getPacketSender().sendMessage("You need a Marrentil which you can light to do this.");
				return;
			}
			for (Player p1 : p.getMapInstance().members) {
				p1.getPacketSender().sendObject_cons(obX, obY, objectId + 1, 0, 10,
						p.getLocation().getZ());
			}
			final PlayerFurniture pf = new PlayerFurniture(myTiles[0] - 1,
					myTiles[1] - 1, (p.getFields().inDungeon() ? 4 : p.getLocation().getZ()), 1,
					objectId, 1, 1);
			((House) (p.getMapInstance())).getLitBurners().add(pf);
			final House h = (House) p.getMapInstance();
			p.getInventory().delete(251, 1);
			GameServer.getTaskScheduler().schedule(new Task(217, p, false) {
				@Override
				public void execute() {
					for (Player p1 : h.members) {
						p1.getPacketSender().sendObject_cons(obX, obY,
								pf.getFurnitureId(), 0, 10,
								pf.getRoomZ() == 4 ? 0 : pf.getRoomZ());
						h.getLitBurners().remove(pf);
						this.stop();
					}

				}
			});
		}
		break;
		case 13672:
		case 13673:
		case 13674:
			myTiles = getMyChunk(p);
			if (myTiles == null)
				return;
			Room below = p.getMapInstance().getOwner().houseRooms[4][myTiles[0] - 1][myTiles[1] - 1];
			if(below.getType() != ConstructionData.OUBLIETTE)
				return;
			for (Player p1 : p.getMapInstance().members) {
				int[] hisTiles = getMyChunk(p1);
				if (myTiles[0] == hisTiles[0] && myTiles[1] == hisTiles[1]
						&& p.getLocation().getZ() == p1.getLocation().getZ() && !p.getFields().inDungeon()) {
					xOnTile = getXTilesOnTile(myTiles, p1.getLocation().getX());
					yOnTile = getYTilesOnTile(myTiles, p1.getLocation().getY());
					if (xOnTile >= 3 && xOnTile <= 4 && yOnTile >= 3
							&& yOnTile <= 4) {
						p.toConsCoords = new int[] {3, 3};
						((House)p1.getFields().mapInstance).getDungeon().addMember(p1);

					}
				}
			}
			break;
		case 13328:
		case 13329:
		case 13330:
			myTiles = getMyChunk(p);
			if (myTiles == null)
				return;
			Room above = p.getMapInstance().getOwner().houseRooms[0][myTiles[0] - 1][myTiles[1] - 1];
			if(above.getType() != ConstructionData.OUBLIETTE)
				return;
			p.toConsCoords = new int[] {3, 3};
			((HouseDungeon) p.getMapInstance()).removePlayer(p);		

			break;
		case 13565:
			//DialogueManager.sendDialogues(p, 433, 1);
			break;
		case 13566:
			//DialogueManager.sendDialogues(p, 434, 1);
			break;
		case 13567:
			//DialogueManager.sendDialogues(p, 435, 1);
			break;
		case 13545:
			//DialogueManager.sendDialogues(p, 423, 1);
			break;
		case 13546:
			//DialogueManager.sendDialogues(p, 424, 1);
			break;
		case 13547:
			//DialogueManager.sendDialogues(p, 425, 1);
			break;
		case 13548:
			//DialogueManager.sendDialogues(p, 426, 1);
			break;
		case 13549:
			//DialogueManager.sendDialogues(p, 427, 1);
			break;
		case 13550:
			//DialogueManager.sendDialogues(p, 429, 1);
			break;
		case 13551:
			//DialogueManager.sendDialogues(p, 431, 1);
			break;

		case 13609:
		case 13611:
		case 13613:
			myTiles = getMyChunkFor(obX, obY);
			if (myTiles == null)
				break;
			int rotation = p.getMapInstance().getOwner().houseRooms[p.getFields().inDungeon() ? 4 : p.getLocation().getZ()][myTiles[0] - 1][myTiles[1] - 1]
					.getRotation();
			HotSpots fireplace = null;
			Room myRoom = p.getMapInstance().getOwner().houseRooms[p.getFields().inDungeon() ? 4 : p.getLocation().getZ()][myTiles[0] - 1][myTiles[1] - 1];
			//System.out.println(myRoom.getType());
			if (myRoom.getType() == ConstructionData.PARLOUR)
				fireplace = HotSpots.PARLOUR_FIREPLACE;
			if (myRoom.getType() == ConstructionData.DINING_ROOM)
				fireplace = HotSpots.DINING_FIREPLACE;
			if (myRoom.getType() == ConstructionData.BEDROOM)
				fireplace = HotSpots.BEDROOM_FIREPLACE;
			p.getPacketSender().sendObject_cons(obX, obY, objectId + 1,
					fireplace.getRotation(rotation), 10, p.getLocation().getZ());
			break;
		case 13678:
		case 13679:
		case 13680:
			myTiles = getMyChunk(p);
			Room below_ = p.getMapInstance().getOwner().houseRooms[4][myTiles[0] - 1][myTiles[1] - 1];
			if (below_ == null) {
				if (p.inBuildingMode)
					DialogueManager.start(p, ConstructionDialogues.buildTrapdoor(p));
				else
					p.getPacketSender().sendMessage("This room leads nowhere.");
				break;
			}
			if (below_.getType() != ConstructionData.OUBLIETTE) {
				p.getPacketSender().sendMessage("This trapdoor leads to a room without a trapdoor.");
				break;
			}
			p.toConsCoords = new int[] {1, 5};
			((House) p.getMapInstance()).getDungeon().addMember(p);
			break;
		case 13675:
		case 13676:
		case 13677:
			myTiles = getMyChunkFor(obX, obY);
			if (myTiles == null)
				break;
			rotation = p.getMapInstance().getOwner().houseRooms[p.getFields().inDungeon() ? 4 : p.getLocation().getZ()][myTiles[0] - 1][myTiles[1] - 1]
					.getRotation();
			HotSpots hs = HotSpots.THRONE_ROOM_TRAPDOOR;
			int object = (objectId == 13675 ? 13678 : 13679);
			if (objectId == 13677)
				object = 13680;
			p.getPacketSender().sendObject_cons(obX, obY, object,
					hs.getRotation(rotation), 22, p.getLocation().getZ());
			break;
			/**
			 * Throne room thrones
			 *//*/constructionremoved
		case 13665:
		case 13666:
		case 13667:
		case 13668:
		case 13669:
		case 13670:
		case 13671:
			myTiles = getMyChunkFor(obX, obY);
			if (myTiles == null)
				break;
			rotation = p.getMapInstance().getOwner().houseRooms[p.getFields().inDungeon() ? 4 : p.getLocation().getZ()][myTiles[0] - 1][myTiles[1] - 1]
					.getRotation();

			p.moveTo(new Position(obX, obY, p.getLocation().getZ()));

			Furniture f = Furniture.forFurnitureId(objectId);
			if (f == null)
				break;
			Room room = p.getMapInstance().getOwner().houseRooms[p.getFields().inDungeon() ? 4 : p.getLocation().getZ()][myTiles[0] - 1][myTiles[1] - 1];
			int anim = 0;
			if (f == Furniture.OAK_THRONE) {
				anim = 4111;
			}
			if (f == Furniture.TEAK_THRONE) {
				anim = 4112;
			}
			if (f == Furniture.MAHOGANY_THRONE) {
				anim = 4113;
			}
			if (f == Furniture.GILDED_THRONE) {
				anim = 4114;
			}
			if (f == Furniture.SKELETON_THRONE) {
				anim = 4115;
			}
			if (f == Furniture.CRYSTAL_THRONE) {
				anim = 4116;
			}
			if (f == Furniture.DEMONIC_THRONE) {
				anim = 4117;
			}
			ArrayList<HotSpots> hsses = ConstructionData.HotSpots
					.forObjectId_3(f.getHotSpotId());
			if (hsses.isEmpty())
				break;

			hs = null;
			if (hsses.size() == 1)
				hs = hsses.get(0);
			else {
				for (HotSpots find : hsses) {
					int actualX = ConstructionData.BASE_X + (myTiles[0] * 8);
					actualX += ConstructionData.getXOffsetForObjectId(
							find.getObjectId(), find, rotation);
					int actualY = ConstructionData.BASE_Y + (myTiles[1] * 8);
					actualY += ConstructionData.getYOffsetForObjectId(
							find.getObjectId(), find, rotation);
					if (obX == actualX && obY == actualY) {
						hs = find;
						break;
					}
				}
			}
			//System.out.println(hs.toString());
			if (rotation == 0) {
				p.setPositionToFace(new Position(obX, obY - 1));
			}
			if (rotation == 1) {
				p.setPositionToFace(new Position(obX - 1, obY));
			}
			if (rotation == 2) {
				p.setPositionToFace(new Position(obX, obY + 1));
			}
			if (rotation == 3) {
				p.setPositionToFace(new Position(obX + 1, obY));
			}
			final int finalAnim2 = anim;
			p.resetChairAnim = false;
			GameServer.getTaskScheduler().schedule(new Task(1, p, false) {
				@Override
				public void execute() {
					p.performAnimation(new Animation(finalAnim2));
					if (p.resetChairAnim) {
						p.resetChairAnim = false;
						p.performAnimation(new Animation(65535));
						this.stop();
					}
				}
			});
			break;

		case 13300:
		case 13301:
		case 13302:
		case 13303:
		case 13304:
		case 13305:
		case 13306:
			myTiles = getMyChunkFor(obX, obY);
			if (myTiles == null)
				break;
			rotation = p.getMapInstance().getOwner().houseRooms[p.getFields().inDungeon() ? 4 : p.getLocation().getZ()][myTiles[0] - 1][myTiles[1] - 1]
					.getRotation();

			p.moveTo(new Position(obX, obY, p.getLocation().getZ()));

			f = Furniture.forFurnitureId(objectId);
			if (f == null)
				break;
			room = p.getMapInstance().getOwner().houseRooms[p.getFields().inDungeon() ? 4 : p.getLocation().getZ()][myTiles[0] - 1][myTiles[1] - 1];
			anim = 0;
			if (f == Furniture.WOODEN_BENCH) {
				anim = 4089;
			}
			if (f == Furniture.OAK_BENCH) {
				anim = 4091;
			}
			if (f == Furniture.CARVED_OAK_BENCH) {
				anim = 4093;
			}
			if (f == Furniture.TEAK_DINING_BENCH) {
				anim = 4095;
			}
			if (f == Furniture.CARVED_TEAK_DINING_BENCH) {
				anim = 4097;
			}
			if (f == Furniture.MAHOGANY_BENCH) {
				anim = 4099;
			}
			if (f == Furniture.GILDED_BENCH) {
				anim = 4101;
			}
			hsses = ConstructionData.HotSpots.forObjectId_3(f.getHotSpotId());
			if (hsses.isEmpty())
				break;

			hs = null;
			if (hsses.size() == 1)
				hs = hsses.get(0);
			else {
				for (HotSpots find : hsses) {
					int actualX = ConstructionData.BASE_X + (myTiles[0] * 8);
					actualX += ConstructionData.getXOffsetForObjectId(
							find.getObjectId(), find, rotation);
					int actualY = ConstructionData.BASE_Y + (myTiles[1] * 8);
					actualY += ConstructionData.getYOffsetForObjectId(
							find.getObjectId(), find, rotation);
					if (obX == actualX && obY == actualY) {
						hs = find;
						break;
					}
				}
			}
			//System.out.println(hs.toString());
			boolean b = room.getType() == ConstructionData.DINING_ROOM ? (hs == HotSpots.DINING_SEATING_1
					|| hs == HotSpots.DINING_SEATING_2
					|| hs == HotSpots.DINING_SEATING_3 || hs == HotSpots.DINING_SEATING_4)
					: (hs == HotSpots.THRONE_BENCH_1
					|| hs == HotSpots.THRONE_BENCH_2
					|| hs == HotSpots.THRONE_BENCH_3
					|| hs == HotSpots.THRONE_BENCH_4 || hs == HotSpots.THRONE_BENCH_5);
			if (rotation == 0) {
				if (b)
					p.setPositionToFace(new Position(
							obX
							+ (room.getType() == ConstructionData.THRONE_ROOM ? 1
									: 0),
									obY
									+ (room.getType() == ConstructionData.PARLOUR ? 1
											: 0)));
				else
					p.setPositionToFace(new Position(
							obX
							- (room.getType() == ConstructionData.THRONE_ROOM ? 1
									: 0),
									obY
									- (room.getType() == ConstructionData.PARLOUR ? 1
											: 0)));
			}
			if (rotation == 1) {
				if (b)
					p.setPositionToFace(new Position(
							obX
							- (room.getType() == ConstructionData.PARLOUR ? 1
									: 0),
									obY
									- (room.getType() == ConstructionData.THRONE_ROOM ? 1
											: 0)));
				else
					p.setPositionToFace(new Position(
							obX
							+ (room.getType() == ConstructionData.PARLOUR ? 1
									: 0),
									obY
									+ (room.getType() == ConstructionData.THRONE_ROOM ? 1
											: 0)));
			}
			if (rotation == 2) {
				if (b)
					p.setPositionToFace(new Position(
							obX
							- (room.getType() == ConstructionData.THRONE_ROOM ? 1
									: 0),
									obY
									- (room.getType() == ConstructionData.PARLOUR ? 1
											: 0)));
				else
					p.setPositionToFace(new Position(
							obX
							+ (room.getType() == ConstructionData.THRONE_ROOM ? 1
									: 0),
									obY
									+ (room.getType() == ConstructionData.PARLOUR ? 1
											: 0)));
			}
			if (rotation == 3) {
				if (b)
					p.setPositionToFace(new Position(
							obX
							+ (room.getType() == ConstructionData.PARLOUR ? 1
									: 0),
									obY
									+ (room.getType() == ConstructionData.THRONE_ROOM ? 1
											: 0)));
				else
					p.setPositionToFace(new Position(
							obX
							- (room.getType() == ConstructionData.PARLOUR ? 1
									: 0),
									obY
									- (room.getType() == ConstructionData.THRONE_ROOM ? 1
											: 0)));
			}
			final int finalAnim1 = anim;
			p.resetChairAnim = false;
			GameServer.getTaskScheduler().schedule(new Task(1, p, false) {
				@Override
				public void execute() {
					p.performAnimation(new Animation(finalAnim1));
					if (p.resetChairAnim) {
						p.resetChairAnim = false;
						p.performAnimation(new Animation(65535));
						this.stop();
					}
				}
			});
			break;

		case 13610:
		case 13612:
		case 13614:
			myTiles = getMyChunkFor(obX, obY);
			if (myTiles == null)
				break;
			rotation = p.getMapInstance().getOwner().houseRooms[p.getFields().inDungeon() ? 4 : p.getLocation().getZ()][myTiles[0] - 1][myTiles[1] - 1]
					.getRotation();
			fireplace = null;
			myRoom = p.getMapInstance().getOwner().houseRooms[p.getFields().inDungeon() ? 4 : p.getLocation().getZ()][myTiles[0] - 1][myTiles[1] - 1];
			if (myRoom.getType() == ConstructionData.PARLOUR)
				fireplace = HotSpots.PARLOUR_FIREPLACE;
			if (myRoom.getType() == ConstructionData.DINING_ROOM)
				fireplace = HotSpots.DINING_FIREPLACE;
			if (myRoom.getType() == ConstructionData.BEDROOM)
				fireplace = HotSpots.BEDROOM_FIREPLACE;
			p.getPacketSender().sendObject_cons(obX, obY, objectId + 1,
					fireplace.getRotation(rotation), 10, p.getLocation().getZ());
			break;
		case 13581:
		case 13582:
		case 13583:
		case 13584:
		case 13585:
		case 13586:
		case 13587:
			myTiles = getMyChunkFor(obX, obY);
			if (myTiles == null)
				break;
			rotation = p.getMapInstance().getOwner().houseRooms[p.getFields().inDungeon() ? 4 : p.getLocation().getZ()][myTiles[0] - 1][myTiles[1] - 1]
					.getRotation();

			p.moveTo(new Position(obX, obY, p.getLocation().getZ()));

			f = Furniture.forFurnitureId(objectId);
			if (f == null)
				break;
			int roomRot = p.getMapInstance().getOwner().houseRooms[p.getFields().inDungeon() ? 4 : p.getLocation().getZ()][myTiles[0] - 1][myTiles[1] - 1]
					.getRotation();
			room = p.getMapInstance().getOwner().houseRooms[p.getFields().inDungeon() ? 4 : p.getLocation().getZ()][myTiles[0] - 1][myTiles[1] - 1];
			hsses = ConstructionData.HotSpots.forObjectId_3(f.getHotSpotId());
			if (hsses.isEmpty())
				break;

			hs = null;
			if (hsses.size() == 1)
				hs = hsses.get(0);
			else {
				for (HotSpots find : hsses) {
					int actualX = ConstructionData.BASE_X + (myTiles[0] * 8);
					actualX += ConstructionData.getXOffsetForObjectId(
							find.getObjectId(), find, roomRot);
					int actualY = ConstructionData.BASE_Y + (myTiles[1] * 8);
					actualY += ConstructionData.getYOffsetForObjectId(
							find.getObjectId(), find, roomRot);
					if (obX == actualX && obY == actualY) {
						hs = find;
						break;
					}
				}
			}

			anim = 0;
			if (f == Furniture.CRUDE_WOODEN_CHAIR) {
				anim = 4073;
			}
			if (f == Furniture.WOODEN_CHAIR) {
				anim = 4075;
			}
			if (f == Furniture.ROCKING_CHAIR) {
				anim = 4079;
			}
			if (f == Furniture.OAK_CHAIR) {
				anim = 4081;
			}
			if (f == Furniture.OAK_ARMCHAIR) {
				anim = 4083;
			}
			if (f == Furniture.TEAK_ARMCHAIR) {
				anim = 4085;
			}
			if (f == Furniture.MAHOGANY_ARMCHAIR) {
				anim = 4087;
			}
			if (hs == HotSpots.PARLOUR_CHAIR_2) {
			} else {
				anim++;
			}
			if (rotation == 0) {
				if (hs == HotSpots.PARLOUR_CHAIR_1)
					p.setPositionToFace(new Position(obX, obY + 1));
				if (hs == HotSpots.PARLOUR_CHAIR_2)
					p.setPositionToFace(new Position(obX, obY + 1));
				if (hs == HotSpots.PARLOUR_CHAIR_3)
					p.setPositionToFace(new Position(obX - 1, obY + 1));
			}
			if (rotation == 1) {
				if (hs == HotSpots.PARLOUR_CHAIR_1)
					p.setPositionToFace(new Position(obX + 1, obY));
				if (hs == HotSpots.PARLOUR_CHAIR_2)
					p.setPositionToFace(new Position(obX + 1, obY));
				if (hs == HotSpots.PARLOUR_CHAIR_3)
					p.setPositionToFace(new Position(obX + 1, obY));
			}
			if (rotation == 2) {
				if (hs == HotSpots.PARLOUR_CHAIR_1)
					p.setPositionToFace(new Position(obX, obY - 1));
				if (hs == HotSpots.PARLOUR_CHAIR_2)
					p.setPositionToFace(new Position(obX, obY - 1));
				if (hs == HotSpots.PARLOUR_CHAIR_3)
					p.setPositionToFace(new Position(obX, obY - 1));
			}
			if (rotation == 3) {
				if (hs == HotSpots.PARLOUR_CHAIR_1)
					p.setPositionToFace(new Position(obX + 1, obY));
				if (hs == HotSpots.PARLOUR_CHAIR_2)
					p.setPositionToFace(new Position(obX + 1, obY));
				if (hs == HotSpots.PARLOUR_CHAIR_3)
					p.setPositionToFace(new Position(obX + 1, obY));
			}
			final int finalAnim = anim;
			p.resetChairAnim = false;
			GameServer.getTaskScheduler().schedule(new Task(1, p, false) {
				@Override
				public void execute() {
					p.performAnimation(new Animation(finalAnim));
					if (p.resetChairAnim) {
						p.resetChairAnim = false;
						p.performAnimation(new Animation(65535));
						this.stop();
					}
				}
			});
			break;
		case 13597:
		case 13598:
		case 13599:
			p.getPacketSender().sendMessage("You find nothing of interest.");
			break;
		case 15306:
		case 15307:
			myTiles = getMyChunkFor(obX, obY);
			xOnTile = getXTilesOnTile(myTiles, obX);
			yOnTile = getYTilesOnTile(myTiles, obY);
			int direction = 0;
			final int LEFT = 0,
					DOWN = 1,
					RIGHT = 2,
					UP = 3;
			if (xOnTile == 0)
				direction = LEFT;
			if (yOnTile == 0)
				direction = DOWN;
			if (xOnTile == 7)
				direction = RIGHT;
			if (yOnTile == 7)
				direction = UP;
			if (direction == LEFT || direction == RIGHT) {
				p.getPacketSender().sendObject_cons(obX, obY, objectId,
						direction == LEFT ? 3 : 1, 0, p.getLocation().getZ());
				if (objectId == (direction == LEFT ? 15306 : 15305)) {
					p.getPacketSender().sendObject_cons(obX, obY - 1, objectId,
							direction == LEFT ? 3 : 1, 0, p.getLocation().getZ());
				} else {
					p.getPacketSender().sendObject_cons(obX, obY + 1,
							objectId + (objectId == 15306 ? -1 : 0),
							direction == LEFT ? 3 : 1, 0, p.getLocation().getZ());
				}
			}
			if (direction == UP || direction == DOWN) {
				p.getPacketSender().sendObject_cons(obX, obY, objectId,
						direction == UP ? 0 : 2, 0, p.getLocation().getZ());
				if (objectId == (direction == UP ? 15306 : 15305)) {
					p.getPacketSender().sendObject_cons(obX + 1, obY, objectId,
							direction == UP ? 2 : 0, 0, p.getLocation().getZ());
				} else {
					p.getPacketSender().sendObject_cons(obX - 1, obY,
							objectId + (objectId == 15306 ? -1 : 0),
							direction == UP ? 2 : 0, 0, p.getLocation().getZ());
				}
			}
			break;
		case 13409:
			myTiles = getMyChunk(p);
			room = p.getMapInstance().getOwner().houseRooms[4][myTiles[0] - 1][myTiles[1] - 1];
			if (room == null) {
				//DialogueManager.sendDialogues(p, 418, 1);
			} else if (room.getType() == ConstructionData.DUNGEON_STAIR_ROOM) {

				p.toConsCoords = new int[] {2, 3};
				(p.getMapInstance() instanceof House ? ((House)p.getMapInstance()).getDungeon() : ((HouseDungeon)p.getMapInstance())).addMember(p);
			} else {
				//DialogueManager.sendDialogues(p, 420, 1);
			}
			break;
		case 13497:
		case 13499:
		case 13501:
		case 13503:
		case 13505:
			myTiles = getMyChunk(p);
			if (!p.getFields().inDungeon()) {
				room = p.getMapInstance().getOwner().houseRooms[1][myTiles[0] - 1][myTiles[1] - 1];
				if (room == null) {
					//DialogueManager.sendDialogues(p, 413, 1);
				} else if (room.getType() == ConstructionData.SKILL_HALL_DOWN
						|| room.getType() == ConstructionData.QUEST_HALL_DOWN) {

					int[] converted = getConvertedCoords(3, 5, myTiles, room);
					p.moveTo(new Position(converted[0], converted[1], 1));
				} else {
					//DialogueManager.sendDialogues(p, 417, 1);
				}
			} else {
				room = p.getMapInstance().getOwner().houseRooms[0][myTiles[0] - 1][myTiles[1] - 1];
				if (room.getType() != ConstructionData.GARDEN
						|| room.getType() != ConstructionData.FORMAL_GARDEN) {

					((HouseDungeon) p.getMapInstance()).removePlayer(p);
				} else {
					//DialogueManager.sendDialogues(p, 417, 1);
				}
			}
			break;
		case 13498:
		case 13500:
		case 13502:
		case 13504:
		case 13506:
			myTiles = getMyChunk(p);
			room = p.getMapInstance().getOwner().houseRooms[0][myTiles[0] - 1][myTiles[1] - 1];
			if (room.getType() == ConstructionData.EMPTY) {
				//DialogueManager.sendDialogues(p, 415, 1);
			} else if (room.getType() == ConstructionData.SKILL_ROOM
					|| room.getType() == ConstructionData.QUEST_ROOM) {

				int[] converted = getConvertedCoords(3, 2, myTiles, room);
				p.moveTo(new Position(converted[0], converted[1], 0));
			} else {
				//DialogueManager.sendDialogues(p, 417, 1);
			}
			break;
		}
	}

	public static PlayerFurniture findNearestPortal(Player p)
	{
		Player owner = p.getMapInstance().getOwner();
		for(PlayerFurniture pf : owner.playerFurniture)
		{
			if(pf.getFurnitureId() != 13405)
				continue;
			if(pf.getRoomZ() != 0)
				continue;
			return pf;
		}
		return null;
	}

	public static int[] getConvertedCoords(int tileX, int tileY, int[] myTiles,
			Room room) {
		int actualX = ConstructionData.BASE_X + (myTiles[0] * 8);
		actualX += ConstructionData.getXOffsetForObjectId(1, tileX, tileY,
				room.getRotation(), 0);
		int actualY = ConstructionData.BASE_Y + (myTiles[1] * 8);
		actualY += ConstructionData.getYOffsetForObjectId(1, tileX, tileY,
				room.getRotation(), 0);
		return new int[] { actualX, actualY };
	}

	public static void handleFifthObjectClick(int obX, int obY, int objectId,
			Player p) {
		switch (objectId) {
		case 13497:
		case 13499:
		case 13501:
		case 13503:
		case 13505:
			int myTiles[] = getMyChunk(p);
			Room room = p.getMapInstance().getOwner().houseRooms[1][myTiles[0] - 1][myTiles[1] - 1];
			if (room == null) {
				p.getPacketSender().sendMessage("These stairs lead nowhere.");
			} else {
				p.getMapInstance().getOwner().houseRooms[1][myTiles[0] - 1][myTiles[1] - 1] = null;
				updatePalette(p);
				p.getPacketSender().constructMapRegionForConstruction(p.getMapInstance().getPalette());
			}
			break;
		default:
			handleFourthObjectClick(obX, obY, objectId, p);
			break;
		}
	}

	public static void handleFourthObjectClick(int obX, int obY, int objectId,
			Player p) {

		if(p.getMapInstance() == null)
			return;
		if (p.getMapInstance().getOwner() != p)
			return;
		if (!p.inBuildingMode)
			return;
		if (handleSpaceClick(obX, obY, objectId, p))
			return;
		if (handleRemoveClick(obX, obY, objectId, p))
			return;
		for (int i : ConstructionData.DOORSPACEIDS) {
			if (objectId == i) {
				if (!roomExists(p)) {
					p.getPacketSender().sendInterface(28643);
					return;
				} else {
					DialogueManager.start(p, ConstructionDialogues.rotateObjectDialogue(p));
				}
			}
		}
	}

	public static boolean handleRemoveClick(int obX, int obY, int objectId,
			Player p) {
		if (objectId == 13126 || objectId == 13127 || objectId == 13128
				|| objectId == 13132)
			objectId = 13126;
		if (objectId == 13133 || objectId == 13134 || objectId == 13135
				|| objectId == 13136)
			objectId = 13133;
		if (objectId == 13137 || objectId == 13138 || objectId == 13139
				|| objectId == 13140)
			objectId = 13137;
		if (objectId == 13145 || objectId == 13147)
			objectId = 13145;
		if (objectId == 13142 || objectId == 13143 || objectId == 13144)
			objectId = 13142;
		if (objectId == 13588 || objectId == 13589 || objectId == 13590)
			objectId = 13588;
		if (objectId == 13591 || objectId == 13592 || objectId == 13593)
			objectId = 13591;
		if (objectId == 13594 || objectId == 13595 || objectId == 13596)
			objectId = 13594;
		if (objectId > 13456 && objectId <= 13476)
			objectId = 13456;
		if (objectId > 13449 && objectId <= 13455)
			objectId = 13449;
		if (objectId > 13331 && objectId <= 13337 || objectId == 13373)
			objectId = 13331;
		if (objectId > 13313 && objectId <= 13327)
			objectId = 13313;

		Furniture f = Furniture.forFurnitureId(objectId);
		if (f == null)
			return false;
		if (f == Furniture.EXIT_PORTAL || f == Furniture.EXIT_PORTAL_) {
			int portalAmt = 0;
			for (PlayerFurniture pf : p.playerFurniture) {
				Furniture ff = Furniture.forFurnitureId(pf.getFurnitureId());
				if (ff == Furniture.EXIT_PORTAL || ff == Furniture.EXIT_PORTAL_)
					portalAmt++;
			}
			if (portalAmt < 2) {
				p.getPacketSender().sendMessage("You need atleast 1 exit portal in your house");
				return true;
			}
		}
		int[] myTiles = getMyChunk(p);
		int roomRot = p.getMapInstance().getOwner().houseRooms[p.getFields().inDungeon() ? 4 : p.getLocation().getZ()][myTiles[0] - 1][myTiles[1] - 1]
				.getRotation();
		Room room = p.getMapInstance().getOwner().houseRooms[p.getFields().inDungeon() ? 4 : p.getLocation().getZ()][myTiles[0] - 1][myTiles[1] - 1];
		ArrayList<HotSpots> hsses = ConstructionData.HotSpots.forObjectId_3(f
				.getHotSpotId());
		if (hsses.isEmpty())
			return false;

		HotSpots hs = null;
		if (hsses.size() == 1)
			hs = hsses.get(0);
		else {
			for (HotSpots find : hsses) {
				int actualX = ConstructionData.BASE_X + (myTiles[0] * 8);
				actualX += ConstructionData.getXOffsetForObjectId(
						find.getObjectId(), find, roomRot);
				int actualY = ConstructionData.BASE_Y + (myTiles[1] * 8);
				actualY += ConstructionData.getYOffsetForObjectId(
						find.getObjectId(), find, roomRot);
				if (obX == actualX && obY == actualY) {
					hs = find;
					break;
				}
			}
		}
		if (objectId == 13331) {
			hs = HotSpots.OUBLIETTE_FLOOR_1;
		}
		if (objectId == 13313) {
			hs = HotSpots.OUBLIETTE_CAGE_1;
		}
		if (objectId == 13126 || objectId == 13127 || objectId == 13128
				|| objectId == 13132 || objectId == 13133 || objectId == 13134
				|| objectId == 13135 || objectId == 13136 || objectId == 13137
				|| objectId == 13138 || objectId == 13139 || objectId == 13140
				|| objectId == 13145 || objectId == 13147 || objectId == 13142
				|| objectId == 13143 || objectId == 13144) {
			hs = HotSpots.COMBAT_RING_1;
		}
		if (objectId == 13456)
			if (room.getType() == ConstructionData.FORMAL_GARDEN)
				hs = HotSpots.FORMAL_HEDGE_1;
		if (objectId == 13449)
			if (room.getType() == ConstructionData.FORMAL_GARDEN)
				hs = HotSpots.FORMAL_FENCE;
		if (objectId == 15270 || objectId == 15273 || objectId == 15274
				|| objectId >= 13588 && objectId <= 13597) {
			if (room.getType() == ConstructionData.CHAPEL)
				hs = HotSpots.CHAPEL_RUG_1;
			if (room.getType() == ConstructionData.PARLOUR)
				hs = HotSpots.PARLOUR_RUG_3;
			if (room.getType() == ConstructionData.SKILL_ROOM
					|| room.getType() == ConstructionData.SKILL_HALL_DOWN
					|| room.getType() == ConstructionData.QUEST_ROOM
					|| room.getType() == ConstructionData.QUEST_HALL_DOWN
					|| room.getType() == ConstructionData.DUNGEON_STAIR_ROOM
					|| room.getType() == ConstructionData.SKILL_HALL_DOWN)
				hs = HotSpots.SKILL_HALL_RUG_3;
			if (room.getType() == ConstructionData.BEDROOM)
				hs = HotSpots.BEDROOM_RUG_3;
		}
		doFurniturePlace(hs, f, hsses, myTiles, obX, obY, roomRot, p, true,
				p.getLocation().getZ());
		p.performAnimation(new Animation(3685));
		Iterator<PlayerFurniture> iterator = p.playerFurniture.iterator();
		while (iterator.hasNext()) {
			PlayerFurniture pf = iterator.next();
			if (pf.getRoomX() != myTiles[0] - 1
					|| pf.getRoomY() != myTiles[1] - 1
					|| pf.getRoomZ() != (p.getFields().inDungeon() ? 4 : p.getLocation().getZ()))
				continue;
			if (pf.getStandardXOff() == hs.getXOffset()
					&& pf.getStandardYOff() == hs.getYOffset())
				iterator.remove();
		}
		return true;
	}

	public static boolean handleSpaceClick(int obX, int obY, int objectId,
			Player p) {

		int[] myTiles = getMyChunk(p);
		int roomRot = p.getMapInstance().getOwner().houseRooms[p.getFields().inDungeon() ? 4 : p.getLocation().getZ()][myTiles[0] - 1][myTiles[1] - 1]
				.getRotation();

		ArrayList<HotSpots> hsses = ConstructionData.HotSpots
				.forObjectId_2(objectId);
		if (hsses.isEmpty())
			return false;

		p.buildFurnitureX = obX;
		p.buildFurnitureY = obY;
		p.buildFurnitureId = objectId;
		HotSpots hs = null;
		int myRoom = p.houseRooms[p.getFields().inDungeon() ? 4 : p.getLocation().getZ()][myTiles[0]-1][myTiles[1]-1].getType();
		if (hsses.size() == 1)
			hs = hsses.get(0);
		else {
			for (HotSpots find : hsses) {
				int actualX = ConstructionData.BASE_X + (myTiles[0] * 8);
				actualX += ConstructionData.getXOffsetForObjectId(
						find.getObjectId(), find, roomRot);
				int actualY = ConstructionData.BASE_Y + (myTiles[1] * 8);
				actualY += ConstructionData.getYOffsetForObjectId(
						find.getObjectId(), find, roomRot);
				if (p.buildFurnitureX == actualX
						&& p.buildFurnitureY == actualY
						&& myRoom == find.getRoomType()
						|| find.getCarpetDim() != null && myRoom == find.getRoomType()) {
					hs = find;
					break;
				}
			}
		}
		if (hs == null)
			return true;
		ArrayList<Furniture> f = ConstructionData.Furniture.getForHotSpotId(hs
				.getHotSpotId());
		if (f == null)
			return false;
		handleInterfaceItems(f, p);
		handleInterfaceCrosses(f, p, hs);
		p.getPacketSender().sendInterface(38272);
		return true;
	}

	public static void handleInterfaceCrosses(ArrayList<Furniture> items,
			Player c, HotSpots hs) {
		int i = 1000;

		for (Furniture f : items) {
			c.getPacketSender().sendString(
					38275 + (i - 1000) * 6,
					Misc.formatPlayerName(f.toString().toLowerCase().replaceAll("_", " ")));
			c.getPacketSender().sendString(38280 + (i - 1000) * 6,
					"Level: " + f.getLevel());
			int i2 = 0;
			boolean canMake = true;
			for (int i1 = 0; i1 < f.getRequiredItems().length; i1++) {
				c.getPacketSender().sendString(
						(38276 + i2) + (i - 1000) * 6,
						f.getRequiredItems()[i1][1]
								+ " x " + ItemDefinition.forId(f.getRequiredItems()[i1][0]).getName());
				if (c.getInventory().getAmount(f.getRequiredItems()[i1][0]) < f.getRequiredItems()[i1][1]) {
					i2++;
					canMake = false;
					continue;
				}
				i2++;
			}
			if (f.getAdditionalSkillRequirements() != null) {
				for (int ii = 0; ii < f.getAdditionalSkillRequirements().length; ii++) {
					c.getPacketSender()
					.sendString((38276 + (i2++)) + (i - 1000) * 6,
							Skill.forId(f.getAdditionalSkillRequirements()[ii][0]).getPName()
							+ " "
							+ f.getAdditionalSkillRequirements()[ii][1]);
					if (c.getSkillManager().getCurrentLevel(Skill.forId(f.getAdditionalSkillRequirements()[ii][0])) < f
							.getAdditionalSkillRequirements()[ii][1]) {
						canMake = false;
					}
				}
			}
			if (f.getFurnitureRequired() != -1) {
				Furniture fur = Furniture.forFurnitureId(f
						.getFurnitureRequired());
				c.getPacketSender().sendString(
						(38276 + (i2++)) + (i - 1000) * 6,
						Misc.formatPlayerName(fur.toString().toLowerCase().replaceAll("_", " ")));
				if (canMake) {
					canMake = false;
					int[] myTiles = getMyChunk(c);
					for (PlayerFurniture pf : c.playerFurniture) {
						if (pf.getRoomX() == myTiles[0] - 1
								&& pf.getRoomY() == myTiles[1] - 1) {
							if (pf.getHotSpot(c.houseRooms[c.getFields().inDungeon() ? 4
									: c.getLocation().getZ()][myTiles[0] - 1][myTiles[1] - 1]
											.getRotation()) == hs) {
								if (pf.getFurnitureId() != fur.getFurnitureId()) {
									canMake = false;
								} else {
									canMake = true;
								}
							}
						}
					}
				}
			}
			if (canMake)
				c.getPacketSender().sendConfig(i, 0);
			else
				c.getPacketSender().sendConfig(i, 1);
			for (i2 = i2; i2 < 4; i2++) {
				c.getPacketSender().sendString((38276 + i2) + (i - 1000) * 6, "");

			}
			i++;
		}
		for (i = i; i < 1008; i++) {
			c.getPacketSender().sendString(38275 + (i - 1000) * 6, "");
			c.getPacketSender().sendString(38276 + (i - 1000) * 6, "");
			c.getPacketSender().sendString(38277 + (i - 1000) * 6, "");
			c.getPacketSender().sendString(38278 + (i - 1000) * 6, "");
			c.getPacketSender().sendString(38279 + (i - 1000) * 6, "");
			c.getPacketSender().sendString(38280 + (i - 1000) * 6, "");
			c.getPacketSender().sendConfig(i, 0);
		}
	}
	public static String hasReqs(Player p, Furniture f, HotSpots hs)
	{

		if(p.getRights() == PlayerRights.DEVELOPER)
			return null;
		if(p.getSkillManager().getCurrentLevel(Skill.CONSTRUCTION) < f.getLevel())
		{
			return "You need a Construction level of at least "+f.getLevel()+" to build this.";
		}
		for (int i1 = 0; i1 < f.getRequiredItems().length; i1++) {
			if (p.getInventory().getAmount(f.getRequiredItems()[i1][0]) < f.getRequiredItems()[i1][1]) {
				String s = ItemDefinition.forId(f.getRequiredItems()[i1][0]).getName();
				if(!s.endsWith("s") && f.getRequiredItems()[i1][1] > 1)
					s = s + "s";
				return "You need "+f.getRequiredItems()[i1][1]+"x "+s+" to build this.";
			}
		}
		if (f.getAdditionalSkillRequirements() != null) {
			for (int ii = 0; ii < f.getAdditionalSkillRequirements().length; ii++) {
				if (p.getSkillManager().getCurrentLevel(Skill.forId(f.getAdditionalSkillRequirements()[ii][0])) < f
						.getAdditionalSkillRequirements()[ii][1]) {
					return "You need a "+Skill.forId(f.getAdditionalSkillRequirements()[ii][0]).getPName()+" of at least "+f.getAdditionalSkillRequirements()[ii][1]+""
							+ " to build this.";
				}
			}
		}
		if (f.getFurnitureRequired() != -1) {
			Furniture fur = Furniture.forFurnitureId(f.getFurnitureRequired());
			int[] myTiles = getMyChunk(p);
			for (PlayerFurniture pf : p.playerFurniture) {
				if (pf.getRoomX() == myTiles[0] - 1 && pf.getRoomY() == myTiles[1] - 1) {
					if (pf.getHotSpot(p.houseRooms[p.getFields().inDungeon() ? 4 : p.getLocation().getZ()][myTiles[0] - 1][myTiles[1] - 1]
							.getRotation()) == hs) {
						if (pf.getFurnitureId() != fur.getFurnitureId()) {
							return "This is an upgradeable piece of furniture. (build the furniture before this first)";
						}
					}
				}
			}
		}

		return null;
	}
	public static boolean buildActions(Player p, Furniture f, HotSpots hs)
	{
		String s = hasReqs(p, f, hs);
		if(s != null) {
			p.getPacketSender().sendMessage(s);
			return false;
		}
		for (int i = 0; i < f.getRequiredItems().length; i++) {
			ItemDefinition item = ItemDefinition.forId(f.getRequiredItems()[i][0]);
			if(item.isStackable())
				p.getInventory().delete(f.getRequiredItems()[i][0], f.getRequiredItems()[i][1]);
			else {
				for(int a = 0; a < f.getRequiredItems()[i][1]; a++)
				{
					p.getInventory().delete(f.getRequiredItems()[i][0], 1);
				}
			}
		}
		p.getSkillManager().addExperience(Skill.CONSTRUCTION, f.getXP());
		return true;
	}

	public static void handleInterfaceItems(ArrayList<Furniture> items, Player c) {
		c.getPacketSender().sendConstructionInterfaceItems(items);
	}

	public static int[] getMyChunk(Player p) {
		for (int x = 0; x < 13; x++) {
			for (int y = 0; y < 13; y++) {
				int minX = ((ConstructionData.BASE_X) + (x * 8));
				int maxX = ((ConstructionData.BASE_X + 7) + (x * 8));
				int minY = ((ConstructionData.BASE_Y) + (y * 8));
				int maxY = ((ConstructionData.BASE_Y + 7) + (y * 8));
				if (p.getLocation().getX() >= minX && p.getLocation().getX() <= maxX && p.getLocation().getY() >= minY
						&& p.getLocation().getY() <= maxY) {
					return new int[] { x, y };
				}
			}
		}
		return null;
	}

	public static int[] getMyChunkFor(int xx, int yy) {
		for (int x = 0; x < 13; x++) {
			for (int y = 0; y < 13; y++) {
				int minX = ((ConstructionData.BASE_X) + (x * 8));
				int maxX = ((ConstructionData.BASE_X + 7) + (x * 8));
				int minY = ((ConstructionData.BASE_Y) + (y * 8));
				int maxY = ((ConstructionData.BASE_Y + 7) + (y * 8));
				if (xx >= minX && xx <= maxX && yy >= minY && yy <= maxY) {
					return new int[] { x, y };
				}
			}
		}
		return null;
	}

	public static int getXTilesOnTile(int[] tile, Player p) {
		int baseX = ConstructionData.BASE_X + (tile[0] * 8);
		return p.getLocation().getX() - baseX;
	}

	public static int getYTilesOnTile(int[] tile, Player p) {
		int baseY = ConstructionData.BASE_Y + (tile[1] * 8);
		return p.getLocation().getY() - baseY;
	}

	public static int getXTilesOnTile(int[] tile, int myX) {
		int baseX = ConstructionData.BASE_X + (tile[0] * 8);
		return myX - baseX;
	}

	public static int getYTilesOnTile(int[] tile, int myY) {
		int baseY = ConstructionData.BASE_Y + (tile[1] * 8);
		return myY - baseY;
	}*/
}
