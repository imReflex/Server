package org.endeavor.game.content.skill.construction;

import java.io.Serializable;
import java.util.ArrayList;

import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;

/**
 * 
 * @author Owner Blade
 *
 */
/*constructionremovedpublic class MapInstance implements Serializable {

	/**
	 * 
	 *//*constructionremoved
	private static final long serialVersionUID = -2164645489857379965L;
	private Palette palette;
	private Palette secondaryPalette;
	public ArrayList<Player> members;
	public ArrayList<Mob> npcs;
	private boolean global;
	private int leaveX, leaveY;
	private Player owner;
	public String type;
	public MapInstance(int leaveX, int leaveY, boolean global)
	{
		this(leaveX, leaveY);
		this.global = global;
	}
	public MapInstance(int leaveX, int leaveY)
	{
		this.setLeaveX(leaveX);
		this.setLeaveY(leaveY);
		global = false;
		members = new ArrayList<Player>();
		npcs = new ArrayList<Mob>();
	}
	public void setPalette(Palette palette)
	{
		this.palette = palette;
	}
	public Palette getPalette()
	{
		return palette;
	}
	public void addMember(Player p)
	{
		members.add(p);
		p.setMapInstance(this);
	}
	public void addNPC(Mob npc)
	{
		npcs.add(npc);
		npc.setMapInstance(this);
	}
	public void destroy()
	{
		for(Player p : members)
		{
			if(p == null)
				continue;
			if(!(getLeaveX() == -1 && getLeaveY() == -1))
				p.teleport(new Location(getLeaveX(), getLeaveY(), 0));
			p.setMapInstance(null);
		}
		for(Mob npc : npcs)
		{
			if(npc == null)
				continue;
			if(npc.getMapInstance() == null)
				continue;
			npc.remove();
		}
		type = null;
		members = null;
		npcs = null;
	}
	public void removePlayer(Player p)
	{
		if(!(getLeaveX() == -1 && getLeaveY() == -1))
			p.teleport(new Location(getLeaveX(), getLeaveY(), 0));
		if(members == null)
			return;
		members.remove(p);
		p.setMapInstance(null);
		playerDied();
	}
	public void playerDied()
	{
		if(global)
			return;
		if(members.size() <= 1)
		{
			destroy();
		}
	}
	public Palette getSecondaryPalette() {
		return secondaryPalette;
	}
	public void setSecondaryPalette(Palette secondaryPalette) {
		this.secondaryPalette = secondaryPalette;
	}
	public Player getOwner() {
		return owner;
	}
	public void setOwner(Player owner) {
		this.owner = owner;
	}
	public int getLeaveX() {
		return leaveX;
	}
	public void setLeaveX(int leaveX) {
		this.leaveX = leaveX;
	}
	public int getLeaveY() {
		return leaveY;
	}
	public void setLeaveY(int leaveY) {
		this.leaveY = leaveY;
	}
}*/
