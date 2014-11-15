package org.endeavor.game.content.skill.hunter;

import java.util.concurrent.TimeUnit;

import org.endeavor.engine.TasksExecutor;
import org.endeavor.engine.cache.map.Region;
import org.endeavor.game.content.skill.SkillConstants;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.item.impl.GroundItemHandler;
import org.endeavor.game.entity.object.GameObject;
import org.endeavor.game.entity.object.ObjectManager;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public abstract class Trap {
	
	protected Player owner;
	protected Location location;
	private boolean broke = false;
	private boolean caught = false;
	private GameObject trapObject;
	
	public Trap(Player owner, Location location) {
		this.owner = owner;
		this.location = location;
		this.trapObject = null;
	}

	public boolean canLayTrap() {
		if(owner.getSkill().getLevels()[SkillConstants.HUNTER] < this.getLevelRequired()) {
			owner.send(new SendMessage("You need a hunter level of " + this.getLevelRequired() + " to lay this trap."));
			return false;
		}
		if(Hunter.containsTrap(location)) {
			owner.send(new SendMessage("There is already a trap here."));
			return false;
		}
		if(ObjectManager.objectExists(owner.getLocation())) {
			owner.send(new SendMessage("You cannot lay a trap here."));
			return false;
		}
		if(owner.getHunter().getTrapCount() == Hunter.getTrapsForLevel(owner.getSkill().getLevels()[SkillConstants.HUNTER])) {
			owner.send(new SendMessage("You have layed the maximum amount of traps for your hunter level."));
			return false;
		}
		return true;
	}
	
	public void layTrap() {
		if(this.canLayTrap()) {
			owner.getUpdateFlags().sendAnimation(new Animation(this.getAnimationID()));
			TasksExecutor.slowExecutor.schedule(new Runnable() {
				@Override
				public void run() {
					setGameObject(new GameObject(getObjectID(), new Location(owner.getX(), owner.getY(), 0), 10 ,0));
					ObjectManager.register(getGameObject());
				}
			}, 2500, TimeUnit.MILLISECONDS);
		}
	}
	
	public abstract void pickupTrap();

	public abstract boolean baitTrap(Item useOn);
	
	public abstract int getId();
	
	public abstract int getObjectID();
	
	public abstract int getBrokenObjectID();
	
	public abstract int getLevelRequired();
	
	public abstract int getAnimationID();
	
	public void sendBreakTimer() {
		if(this.getGameObject() != null) {
			TasksExecutor.slowExecutor.schedule(new Runnable() {
				@Override
				public void run() {
					if(getGameObject() == null)
						return;
					owner.send(new SendMessage("The wind has knocked down your trap."));
					breakTrap();
				}
			}, 30, TimeUnit.SECONDS);
		}
	}
	
	public void breakTrap() {
		if(this.getGameObject() != null) {
			ObjectManager.remove(this.getGameObject());
		} else {
			return;
		}
		this.getGameObject().setId(getBrokenObjectID());
		this.setGameObject(this.getGameObject());
		ObjectManager.register(this.getGameObject());
		this.setBroke(true);
		//Region.getRegion(location).addObject(new RSObject());
		final Trap trap = this;
		TasksExecutor.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				if(getGameObject() == null)
					return;
				ObjectManager.remove(getGameObject());
				GroundItemHandler.add(new Item(10006, 1), location, owner);
				if(owner.getHunter().isTrapRemoved(trap)) {
					owner.send(new SendMessage("Your trap has fell to the ground."));
				}
			}
		}, 80, TimeUnit.SECONDS);
	}
	
	public void setTrapCaught(int objectId) {
		if(this.getGameObject() != null && Hunter.containsTrap(this.location)) {
			ObjectManager.register(this.getGameObject());
			this.getGameObject().setId(objectId);
			this.setGameObject(this.getGameObject());
			ObjectManager.register(this.getGameObject());
			this.setCaught(true);
		}
	}
	
	public boolean isBroke() {
		return broke;
	}
	
	public void setBroke(boolean broke) {
		this.broke = broke;
	}
	
	public boolean isCaught() {
		return caught;
	}
	
	public void setCaught(boolean caught) {
		this.caught = caught;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public Player getOwner() {
		return owner;
	}
	
	public GameObject getGameObject() {
		return trapObject;
	}
	
	public void setGameObject(GameObject trapObject) {
		this.trapObject = trapObject;
	}
	
	public int getCurrentObjectID() {
		return this.getGameObject() == null ? this.getObjectID() : this.getGameObject().getId();
	}
	
}
