package org.endeavor.game.content.skill.construction;

import java.util.ArrayList;

import org.endeavor.game.entity.mob.Mob;

/**
 * 
 * @author Owner Blade
 *
 */
public class House extends MapInstance {

	private HouseDungeon dungeon;
	private ArrayList<PlayerFurniture> litBurners;
	private ArrayList<PlayerFurniture> furnitureActivated;
	private boolean locked = false;
	public House(int leaveX, int leaveY) {
		super(leaveX, leaveY);
		setLitBurners(new ArrayList<PlayerFurniture>());
	}
	public House(int leaveX, int leaveY, boolean b)
	{
		super(leaveX, leaveY, b);
		setLitBurners(new ArrayList<PlayerFurniture>());
		setFurnitureActivated(new ArrayList<PlayerFurniture>());
		dungeon = new HouseDungeon(leaveY, leaveY, false);
		dungeon.setHouse(this);
	}
	public Servant getButler()
	{
		for(Mob npc : npcs)
		{
			if(npc.getId() == getOwner().houseServant)
				return ((Servant)npc);
		}
		return null;
	}
	
	public void processPlayer(Player p)
	{
		House house = p.getFields().mapInstance instanceof House ? (House) p.getFields().mapInstance : ((HouseDungeon)p.getFields().mapInstance).getHouse();
		int[] myTiles = Construction.getMyChunk(p);
		if(myTiles == null)
			return;
		if(myTiles[0] == -1 || myTiles[1] == -1)
			return;
		Room r = house.getOwner().houseRooms[p.getFields().inDungeon() ? 4 : p.getPosition().getZ()][myTiles[0]-1][myTiles[1]-1];
		if(r == null)
			return;
		if(r.getType() == ConstructionData.OUBLIETTE)
		{
			int xOnTile = Construction.getXTilesOnTile(myTiles, p.getPosition().getX());
			int yOnTile = Construction.getYTilesOnTile(myTiles, p.getPosition().getY());
			if(xOnTile >=  2 && xOnTile <= 5
					&& yOnTile >= 2 && yOnTile <= 5)
			{
				PlayerFurniture pf = null;
				for(PlayerFurniture pf_ : house.getOwner().playerFurniture)
				{
					if(pf_.getRoomX() == myTiles[0]-1 && pf_.getRoomY() == myTiles[1]-1
							&& pf_.getHotSpotId() == 85)
					{
						pf = pf_;
						break;
					}
				}
				if(pf != null) {
					if(pf.getFurnitureId() == 13334
							|| pf.getFurnitureId() == 13337)
					{
						if(p.getConstitution() > 0) {
							//p.getCombat().appendHit(p, 20, 0, 2, false);
							p.setDamage(new Damage(new Hit(20, CombatIcon.NONE, Hitmask.NONE)));
						}
					}
				}
			}
		}
		
		if(r.getType() == ConstructionData.CORRIDOR)
		{
			int[] converted = Construction.getConvertedCoords(3, 2, myTiles, r);
			int[] converted_1 = Construction.getConvertedCoords(4, 2, myTiles, r);
			int[] converted_2 = Construction.getConvertedCoords(3, 5, myTiles, r);
			int[] converted_3 = Construction.getConvertedCoords(4, 5, myTiles, r);
			if(p.getPosition().getX() == converted[0] && p.getPosition().getY() == converted[1]
					|| p.getPosition().getX() == converted_1[0] && p.getPosition().getY() == converted_1[1]
					|| p.getPosition().getX() == converted_2[0] && p.getPosition().getY() == converted_2[1]
					|| p.getPosition().getX() == converted_3[0] && p.getPosition().getY() == converted_3[1])
			{
				PlayerFurniture pf = null;
				for(PlayerFurniture pf_ : house.getOwner().playerFurniture)
				{
					if(pf_.getRoomX() == myTiles[0] && pf_.getRoomY() == myTiles[1]
							&& pf_.getHotSpotId() == 91)
					{
						int[] coords = Construction.getConvertedCoords(pf_.getStandardXOff(), pf_.getStandardYOff(), myTiles, r);
						if(coords[0] == myTiles[0] && coords[1] == myTiles[1]) {
							pf = pf_;
							break;
						}
					}
				}
				if(pf != null) {
					if(pf.getFurnitureId() >= 13356
							|| pf.getFurnitureId() <= 13360)
					{
						if(p.getConstitution() > 0) {
							//p.getCombat().appendHit(p, 20, 0, 2, false);
							p.setDamage(new Damage(new Hit(20, CombatIcon.NONE, Hitmask.NONE)));
						}
					}
				}
			}
		}
	}
	
	public void playerKilled(Player p)
	{
		int[] myTiles = Construction.getMyChunk(p);
		if(p.combatRingType > 0)
		{
			p.getPacketSender().sendInteractionOption("null", 3, true);
			p.combatRingType = 0;
			p.moveTo(new Position(ConstructionData.BASE_X+(myTiles[0]*8)+1, ConstructionData.BASE_Y+(myTiles[1]*8)+1, p.getPosition().getZ()));
		} else {
			PlayerFurniture portal = Construction.findNearestPortal(p);
			int toX = ConstructionData.BASE_X+((portal.getRoomX()+1)*8);
			int toY = ConstructionData.BASE_Y+((portal.getRoomY()+1)*8);
			p.moveTo(new Position(toX+2, toY+2, 0));
		}
	}
	public ArrayList<PlayerFurniture> getLitBurners() {
		return litBurners;
	}
	public void setLitBurners(ArrayList<PlayerFurniture> litBurners) {
		this.litBurners = litBurners;
	}

	public int getBurnersLit(int roomX, int roomY, int roomZ)
	{
		int i = 0;
		for(PlayerFurniture pf : litBurners)
		{
			if(roomX == pf.getRoomX() && roomY == pf.getRoomY() && roomZ == pf.getRoomZ())
				i++;
				
		}
		return i;
	}
	public ArrayList<PlayerFurniture> getFurnitureActivated() {
		return furnitureActivated;
	}
	public void setFurnitureActivated(ArrayList<PlayerFurniture> furnitureActivated) {
		this.furnitureActivated = furnitureActivated;
	}
	public ArrayList<PlayerFurniture> getActivatedObject(int roomX, int roomY, int roomZ)
	{
		ArrayList<PlayerFurniture> pfs = new ArrayList<PlayerFurniture>();
		for(PlayerFurniture pf : furnitureActivated)
		{
			if(roomX == pf.getRoomX() && roomY == pf.getRoomY() && roomZ == pf.getRoomZ())
				pfs.add(pf);
				
		}
		return pfs;
	}
	
	public void greet(Player p)
	{
		Servant s = getButler();
		if(s == null)
			return;
		if(s.isGreetVisitors())
		{
			s.forceChat("Welcome "+p.getUsername()+"!");
		}
	}
	public boolean isLocked() {
		return locked;
	}
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	public HouseDungeon getDungeon() {
		return dungeon;
	}
	public void setDungeon(HouseDungeon dungeon) {
		this.dungeon = dungeon;
	}
	@Override
	public void removePlayer(Player p)
	{
		if(dungeon != null)
			dungeon.removePlayer(p);
		super.removePlayer(p);
	}
	@Override
	public void destroy()
	{
		dungeon.destroy();
		super.destroy();
	}
	@Override
	public void setOwner(Player p)
	{
		super.setOwner(p);
		dungeon.setOwner(p);
	}
	
}
