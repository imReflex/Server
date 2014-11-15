package org.endeavor.game.content.skill.hunter.snare;

import java.util.concurrent.TimeUnit;

import org.endeavor.engine.TasksExecutor;
import org.endeavor.engine.cache.map.RSObject;
import org.endeavor.engine.cache.map.Region;
import org.endeavor.game.content.skill.SkillConstants;
import org.endeavor.game.content.skill.hunter.Hunter;
import org.endeavor.game.content.skill.hunter.Trap;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.object.GameObject;
import org.endeavor.game.entity.object.ObjectManager;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

/**
 * 
 * @author Allen K.
 *
 */
public class BirdSnare extends Trap {
	
	public BirdSnare(Player owner, Location location) {
		super(owner,location);
	}

	@Override
	public boolean canLayTrap() {
		if(!owner.getInventory().hasItemId(10006) && owner.getAttributes().get("relay_trap") != null) {
			owner.send(new SendMessage("You need a bird snare in your inventory to lay."));
			return false;
		}
		return super.canLayTrap();
	}
	
	@Override
	public void layTrap() {
		if(this.canLayTrap()) {
			if(owner.getAttributes().get("relay_trap") != null)
				owner.getAttributes().remove("relay_trap");
			owner.getUpdateFlags().sendAnimation(new Animation(this.getAnimationID()));
			TasksExecutor.slowExecutor.schedule(new Runnable() {
				@Override
				public void run() {
					GameObject trapO = new GameObject(getObjectID(), new Location(owner.getLocation()), 10 ,0);
					ObjectManager.register(trapO);
					setGameObject(trapO);
					//Region.getRegion(owner.getLocation()).addObject(new RSObject(owner.getX(), owner.getY(), 0, getObjectID(), 10 ,0));
					owner.getHunter().walk();
					owner.getInventory().remove(10006);
					sendBreakTimer();
				}
			}, 2500, TimeUnit.MILLISECONDS);
		}
	}

	@Override
	public void pickupTrap() {
		if(owner.getHunter().isTrapRemoved(this)) {
			GameObject trapO = this.getGameObject();
			ObjectManager.remove(trapO);
			Region.getRegion(trapO.getLocation()).removeObject(new RSObject(trapO.getLocation().getX(), trapO.getLocation().getY(), 0, getObjectID(), 10 ,0));
			owner.send(new SendMessage("You pick up the bird snare."));
			owner.getInventory().add(10006, 1);
			this.setGameObject(null);
		}
	}
	
	@Override
	public boolean baitTrap(Item useOn) {
		return false;
	}

	@Override
	public int getId() {
		return 10006;
	}

	@Override
	public int getObjectID() {
		return 19175;
	}
	
	@Override
	public int getBrokenObjectID() {
		return 19174;
	}

	@Override
	public int getLevelRequired() {
		return 1;
	}

	@Override
	public int getAnimationID() {
		// TODO Auto-generated method stub
		return 5208;
	}

}
