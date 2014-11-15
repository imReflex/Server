package org.endeavor.game.content.minigames.pestcontrol;

import static org.endeavor.game.content.minigames.pestcontrol.PestControlConstants.*;

import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.content.combat.impl.Attack;
import org.endeavor.game.content.minigames.pestcontrol.impl.Shifter;
import org.endeavor.game.content.minigames.pestcontrol.impl.Spinner;
import org.endeavor.game.entity.Animation;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;

public abstract class Pest extends Mob {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7089015335498170312L;

	private final PestControlGame game;
	
	private byte offset = 0;
	
	public Pest(PestControlGame game, int id, Location p) {
		super(game.getVirtualRegion(), id, true, false, p);
		PestControlConstants.setLevels(this);
		
		if (!(this instanceof Spinner)) {
			getCombat().setAttack(game.getVoidKnight());
		}
		
		getFollowing().setIgnoreDistance(true);
		
		getAttributes().set(PestControlGame.PEST_GAME_KEY, game);
		
		this.game = game;
		
		for (int i : BRAWLERS) {
			if (id == i) {
				getCombat().getMelee().setAttack(new Attack(1, 6), new Animation(3897));
				getCombat().setBlockAnimation(new Animation(3895, 0));
				return;
			}
		}
		
		for (int i : DEFILERS) {
			if (id == i) {
				getCombat().getRanged().setAttack(new Attack(3, 5), new Animation(3920), null, null, null);
				getCombat().setBlockAnimation(new Animation(3921, 0));
				getCombat().setCombatType(CombatTypes.RANGED);
				return;
			}
		}
		
		for (int i : RAVAGERS) {
			if (id == i) {
				getCombat().getMelee().setAttack(new Attack(1, 5), new Animation(3915));
				getCombat().setBlockAnimation(new Animation(3916, 0));
				return;
			}
		}
		
		for (int i : SHIFTERS) {
			if (id == i) {
				getCombat().setBlockAnimation(new Animation(3902, 0));
				getCombat().getMelee().setAttack(new Attack(1, 4), new Animation(3901));
				return;
			}
		}
		
		for (int i : SPINNERS) {
			if (id == i) {
				getCombat().getMelee().setAttack(new Attack(1, 4), new Animation(3908));
				getCombat().setBlockAnimation(new Animation(3909, 0));
				return;
			}
		}
		
		for (int i : SPLATTERS) {
			if (id == i) {
				getCombat().getMelee().setAttack(new Attack(1, 4), new Animation(3891));
				getCombat().setBlockAnimation(new Animation(3890, 0));
				return;
			}
		}
		
		for (int i : TORCHERS) {
			if (id == i) {
				getCombat().setBlockAnimation(new Animation(3880, 0));
				getCombat().getMagic().setAttack(new Attack(3, 5), new Animation(3882), null, null, null);
				getCombat().setCombatType(CombatTypes.MAGIC);
				return;
			}
		}
	}

	public abstract void tick();
	
	@Override
	public int getMaxHit(CombatTypes type) {
		return getDefinition().getLevel() / 10;
	}
	
	@Override
	public void updateCombatType() {}
	
	@Override
	public Animation getDeathAnimation() {
		final int id = getId();
		
		for (int i : BRAWLERS) {
			if (id == i) {
				return new Animation(3894);
			}
		}
		
		for (int i : DEFILERS) {
			if (id == i) {
				return new Animation(3922);
			}
		}
		
		for (int i : RAVAGERS) {
			if (id == i) {
				return new Animation(3917);
			}
		}
		
		for (int i : SHIFTERS) {
			if (id == i) {
				return new Animation(3903);
			}
		}
		
		for (int i : SPINNERS) {
			if (id == i) {
				return new Animation(3910);
			}
		}
		
		for (int i : SPLATTERS) {
			if (id == i) {
				return new Animation(3888);
			}
		}
		
		for (int i : TORCHERS) {
			if (id == i) {
				return new Animation(3881);
				//3880 block
				//3882 attack
			}
		}
			
		return new Animation(-1);
	}
	
	public void doAliveMobProcessing() {
		tick();
		
		if (!(this instanceof Spinner || this instanceof Shifter)) {
			if (!isMovedLastCycle() && getCombat().getAttackTimer() == 0 && ++offset >= 4) {
				
				Player p = null;
				int dist = 99999;
				
				for (Player k : game.getPlayers()) {
					int thisDist = Misc.getManhattanDistance(getLocation(), k.getLocation());
					if (thisDist <= 8) {
						if (p == null && game.getAttackers(k) < 2 || thisDist < dist && game.getAttackers(k) < 2) {
							getCombat().setAttack(k);
						}
					}
				}
				
				offset = 0;
			}
		}
	}
	
	public PestControlGame getGame() {
		return game;
	}
}
