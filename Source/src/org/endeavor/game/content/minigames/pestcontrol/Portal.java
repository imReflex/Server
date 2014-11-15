package org.endeavor.game.content.minigames.pestcontrol;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.endeavor.engine.cache.map.Region;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.GameConstants;
import org.endeavor.game.content.minigames.pestcontrol.impl.Shifter;
import org.endeavor.game.content.skill.SkillConstants;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.mob.Mob;

public class Portal extends Mob {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6658001932891184379L;

	private final List<Mob> pests = new ArrayList<Mob>();
	
	private final List<Mob> shifters = new ArrayList<Mob>();
	
	private final PestControlGame game;
	
	public Portal(PestControlGame game, int id, Location p, int z) {
		super(game.getVirtualRegion(), id, false, false, new Location(p, z));
		setRetaliate(false);
		init();
		
		getLevels()[SkillConstants.HITPOINTS] = 250;
		getLevels()[SkillConstants.DEFENCE] = 250;
		getLevels()[SkillConstants.MAGIC] = 250;
		
		getMaxLevels()[SkillConstants.HITPOINTS] = 250;
		getMaxLevels()[SkillConstants.DEFENCE] = 250;
		getMaxLevels()[SkillConstants.MAGIC] = 250;
		
		setRetaliate(false);
		setRespawnable(false);
		
		this.game = game;
		
		getAttributes().set(PestControlGame.PEST_GAME_KEY, game);
	}
	
	public void cleanup() {
		if (pests.size() > 0) {
			for (Mob i : pests) {
				i.remove();
			}
		}
		
		if (shifters.size() > 0) {
			for (Mob i : shifters) {
				i.remove();
			}
		}
	}
	
	public Animation getDeathAnimation() {
		return new Animation(65535, 0);
	}
	
	public List<Mob> getPests() {
		return pests;
	}
	
	@Override
	public void onDeath() {
		remove();
	}

	public boolean isDamaged() {
		return getLevels()[3] < getMaxLevels()[3];
	}

	public void heal(int amount) {
		getLevels()[3] += amount;
		
		if (getLevels()[3] > getMaxLevels()[3])
			getLevels()[3] = getMaxLevels()[3];
	}

	public void init() {
	}

	@Override
	public void process() {
		if (isDead()) {
			return;
		}
		
		for (Iterator<Mob> i = pests.iterator(); i.hasNext();) {
			if (i.next().isDead()) {
				i.remove();
			}
		}
		
		for (Iterator<Mob> i = shifters.iterator(); i.hasNext();) {
			if (i.next().isDead()) {
				i.remove();
			}
		}
		
		if (Misc.randomNumber(35) == 0 && pests.size() < 3) {
			Location l = GameConstants.getClearAdjacentLocation(getLocation(), getSize(), game.getVirtualRegion());
			
			if (l != null) {
				pests.add(PestControlConstants.getRandomPest(l, game, this));
			}
		}
		
		if (game.getPlayers().size() > 6) {
			if (Misc.randomNumber(35) == 0 && shifters.size() < 1) {
				int baseX = 2656;
				int baseY = 2592;
				
				int x = baseX + (Misc.randomNumber(2) == 0 ? Misc.randomNumber(7) : -Misc.randomNumber(7));
				int y = baseY + (Misc.randomNumber(2) == 0 ? Misc.randomNumber(7) : -Misc.randomNumber(7));
				
				while (Region.getRegion(x, y).getClip(x, y, 0) == 256) {
					x = baseX + (Misc.randomNumber(2) == 0 ? Misc.randomNumber(7) : -Misc.randomNumber(7));
					y = baseY + (Misc.randomNumber(2) == 0 ? Misc.randomNumber(7) : -Misc.randomNumber(7));
				}
				
				shifters.add(new Shifter(new Location(x, y, game.getZ()), game));
			}
		}
	}
}















