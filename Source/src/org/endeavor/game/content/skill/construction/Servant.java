package org.endeavor.game.content.skill.construction;

import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;
/**
 * 
 * @author Owner Blade
 *
 */
/*constructionremovedpublic class Servant extends Mob {

	private boolean fetching, greetVisitors;
	private int[] inventory;
	
	public Servant(int npcId, Location position, Player owner) {
		super(npcId, true, position, owner, true, false,
				null);///XXX: needs a virtual region from the house
	}
	
	public Servant(int npcId, Location position, int inventorySize, Player owner)
	{
		super(npcId, true, position, owner, true, false,
				null);///XXX: needs a virtual region from the house
		inventory = new int[inventorySize];
	}
	
	public boolean addInventoryItem(int itemId)
	{
		for(int i = 0; i < inventory.length; i++)
		{
			if(inventory[i] == 0)
			{
				inventory[i] = itemId;
				return true;
			}
		}
		return false;
	}
	
	public int freeSlots()
	{
		int value = 0;
		for(int i = 0; i < inventory.length; i++)
		{
			if(inventory[i] == 0)
			{
				value++;
			}
		}
		return value;
	}
	
	@Override
	public void onDeath()
	{
		/*if(getFields().mapInstance == null)
			return;
		GameServer.getWorld().deregister(this);*//*constructionremoved
		putBackInBank(getOwner());
	}
	
	public void putBackInBank(Player p)
	{
		/*p.getFields().setBanking(true);
		for(int i = 0; i < inventory.length; i++)
		{
			if(i <= 0)
				continue;
			int tab = Bank.getTabforItem(p, inventory[i]);
			p.getBank(tab).add(inventory[i], 1);
		}
		p.getFields().setBanking(false);*//*constructionremoved
		
		for (int i : inventory) {
			if (i != 0) {
				p.getBank().add(new Item(i, 1));
			}
		}
	}
	
	public void takeItemsFromBank(Player p, int itemId, int amount)
	{
		int removeAmount = p.getBank().remove(itemId, amount);
		
		if (removeAmount > 0) {
			for (int i = 0; i < removeAmount; i++) {
				if(!addInventoryItem(itemId)) {
					break;
				}
			}
		}
		/*for(int i = 0; i < amount; i++)
		{
			if(freeSlots() == 0)
				return;
			int tab = Bank.getTabforItem(p, inventory[i]);
			if(!p.getBank(tab).contains(itemId))
				return;
			if(addInventoryItem(itemId))
				p.getBank(tab).delete(itemId+1, 1);
		}*//*constructionremoved
	}
	
	public void giveItems(Player p)
	{
		for(int i = 0; i < inventory.length; i++)
		{
			if(p.getInventory().getFreeSlots() == 0)
				break;
			p.getInventory().addOrCreateGroundItem(inventory[i], 1, true);
			inventory[i] = 0;
		}
	}
	
	public int[] getInventory()
	{
		return inventory;
	}
	
	public boolean isFetching() {
		return fetching;
	}
	
	public void setFetching(boolean fetching) {
		this.fetching = fetching;
	}
	
	public boolean isGreetVisitors() {
		return greetVisitors;
	}
	
	public void setGreetVisitors(boolean greetVisitors) {
		this.greetVisitors = greetVisitors;
	}

	
}*/
