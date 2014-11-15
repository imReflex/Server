package org.endeavor.game.content.combat;

import org.endeavor.GameSettings;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.task.impl.PlayerDeathTask;
import org.endeavor.game.content.combat.Combat.CombatTypes;
import org.endeavor.game.content.combat.Hit.HitTypes;
import org.endeavor.game.content.combat.impl.AccuracyFormulas;
import org.endeavor.game.content.combat.impl.MaxHitFormulas;
import org.endeavor.game.content.combat.impl.PoisonWeapons;
import org.endeavor.game.content.combat.impl.RingOfRecoil;
import org.endeavor.game.content.combat.special.SpecialAttackHandler;
import org.endeavor.game.content.minigames.armsrace.ARConstants;
import org.endeavor.game.content.minigames.armsrace.ArmsRaceLobby;
import org.endeavor.game.content.minigames.pestcontrol.PestControlGame;
import org.endeavor.game.content.skill.magic.MagicEffects;
import org.endeavor.game.content.skill.magic.spells.Vengeance;
import org.endeavor.game.content.skill.melee.BarrowsSpecials;
import org.endeavor.game.content.skill.ranged.BoltSpecials;
import org.endeavor.game.content.skill.slayer.SlayerMonsters;
import org.endeavor.game.content.skill.summoning.FamiliarMob;
import org.endeavor.game.content.sounds.PlayerSounds;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.mob.MobConstants;
import org.endeavor.game.entity.player.EquipmentConstants;
import org.endeavor.game.entity.player.ItemCheck;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerConstants;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;

public class PlayerCombatInterface implements CombatInterface {

	private final Player player;
	
	public PlayerCombatInterface(Player player) {
		this.player = player;
	}
	
	@Override
	public void hit(Hit hit) {
		if (!player.canTakeDamage() || player.isImmuneToHit() || player.getMagic().isTeleporting()
				|| PlayerConstants.isOwner(player) && !GameSettings.DEV_MODE && !player.getController().isSafe()) {
			return;
		}

		if (player.isDead()) {
			hit.setDamage(0);
		}

		if (hit.getAttacker() != null) {
			if (hit.getAttacker().isNpc()) {
				Mob mob = World.getNpcs()[hit.getAttacker().getIndex()];
				if (mob != null && MobConstants.isDragon(mob)) {
					if (ItemCheck.isWearingAntiDFireShield(player) && (hit.getType() == Hit.HitTypes.MAGIC)) {
						if (ItemCheck.hasDFireShield(player)) {
							player.getMagic().incrDragonFireShieldCharges();
						}
						if (player.hasFireImmunity()) {
							player.getClient().queueOutgoingPacket(new SendMessage("You reset all of the dragonfire."));
							hit.setDamage((int) 0);
						} else {
							player.getClient().queueOutgoingPacket(new SendMessage("You manage to resist some of the dragonfire."));
							hit.setDamage((int) (hit.getDamage() * 0.7));
						}
					} else if (hit.getType() == Hit.HitTypes.MAGIC && player.hasSuperFireImmunity()) {
						player.getClient().queueOutgoingPacket(new SendMessage("You reset all of the dragonfire."));
						hit.setDamage((int) 0);
					} else if (hit.getType() == Hit.HitTypes.MAGIC && player.hasFireImmunity()) {
						player.getClient().queueOutgoingPacket(new SendMessage("You manage to resist some of the dragonfire."));
						hit.setDamage((int) (hit.getDamage() * 0.5));
					} else if ((hit.getType() == Hit.HitTypes.MAGIC)) {
						player.getClient().queueOutgoingPacket(new SendMessage("You are horribly burned by the dragonfire."));
					}
				}
			} else {
				Player p = World.getPlayers()[hit.getAttacker().getIndex()];

				if (p != null && !player.getController().canAttackPlayer(p, player) || !player.getController().canAttackPlayer(player, p)) {
					return;
				}
			}

		}

		hit.setDamage(player.getPrayer().getDamage(hit));
		hit.setDamage(player.getEquipment().getEffectedDamage(hit.getDamage()));

		if (hit.getDamage() > player.getLevels()[3]) {
			hit.setDamage(player.getLevels()[3]);
		}

		if (hit.getType() == Hit.HitTypes.MELEE || hit.getType() == Hit.HitTypes.RANGED) {
			PlayerSounds.sendBlockOrHitSound(player, hit.getDamage() > 0);
		}

		if (hit.getType() != Hit.HitTypes.POISON && hit.getType() != Hit.HitTypes.NONE) {
			player.getDegrading().degradeEquipment(player);
		}
		
		
		player.getLevels()[3] = (short) (player.getLevels()[3] - hit.getDamage());

		if (!player.getUpdateFlags().isHitUpdate()) {
			player.getUpdateFlags().sendHit(hit.getDamage(), hit.getHitType(), hit.getCombatHitType());
		} else {
			player.getUpdateFlags().sendHit2(hit.getDamage(), hit.getHitType(), hit.getCombatHitType());
		}

		if (hit.getType() != Hit.HitTypes.POISON) {
			if (player.getTrade().trading()) {
				player.getTrade().end(false);
			} else {
				if (player.getInterfaceManager().hasInterfaceOpen()) {
					player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
				}
			}
		}

		checkForDeath();

		if (hit.getAttacker() != null) {
			RingOfRecoil.doRecoil(player, hit.getAttacker(), hit.getDamage());

			if (player.isRetaliate() && player.getCombat().getAttacking() == null 
					&& !player.getMovementHandler().moving()) {//still retaliate when moving?
				player.getCombat().setAttack(hit.getAttacker());
			}

			player.getCombat().getDamageTracker().addDamage(hit.getAttacker(), hit.getDamage());

			hit.getAttacker().onHit(player, hit);

			if (hit.getType() != HitTypes.NONE && hit.getType() != HitTypes.POISON) {
				if (hit.getDamage() >= 4 && player.getMagic().isVengeanceActive() && !player.isDead()) {
					Vengeance.recoil(player, hit);
				}
			}
		}

		player.getSkill().update(3);
	}

	@Override
	public void onHit(Entity entity, Hit hit) {
		if (player.getAttributes().get(PestControlGame.PEST_GAME_KEY) != null) {
			player.getAttributes().set(PestControlGame.PEST_DAMAGE_KEY, 
					player.getAttributes().get(PestControlGame.PEST_DAMAGE_KEY) != null ? player.getAttributes().getInt(PestControlGame.PEST_DAMAGE_KEY) + hit.getDamage() : hit.getDamage());
		}

		if (player.getCombat().getCombatType() == CombatTypes.RANGED) {
			player.getRanged().dropArrowAfterHit();
		}

		if (hit.getType() != Hit.HitTypes.POISON && hit.getType() != Hit.HitTypes.NONE) {
			player.getPrayer().doEffectOnHit(entity, hit);

			if (player.getController().equals(ARConstants.AR_CONTROLLER) && hit.getDamage() > 0) {
				ArmsRaceLobby.getGame().onHit(player, hit.getDamage());
			}
		}

		if (player.getMelee().isGuthanEffectActive())
			BarrowsSpecials.doGuthanEffect(player, entity, hit);
		else if (player.getMelee().isToragEffectActive())
			BarrowsSpecials.doToragEffect(player, entity);
		else if (player.getRanged().isKarilEffectActive())
			BarrowsSpecials.doKarilEffect(player, entity);
		else if (player.getMagic().isAhrimEffectActive()) {
			BarrowsSpecials.doAhrimEffect(player, entity, hit.getDamage());
		}

		if (entity.isNpc() && entity.isDead()) {
			player.getCombat().setAttackTimer(0);
			player.getCombat().resetCombatTimer();
		} else if (!entity.isNpc()) {
			player.getEarningPotential().addEarningPotentialForHit(entity, hit);
		}

		if (hit.getDamage() >= 50) {
			player.getAchievements().incr(player, "Inflict 50 damage in one hit");
			if (hit.getDamage() >= 70)
				player.getAchievements().incr(player, "Inflict 75 damage in one hit");
		}

		if (player.getMagic().isDFireShieldEffect()) {
			player.getMagic().reset();
			player.getMagic().getSpellCasting().updateMagicAttack();
			player.updateCombatType();
		}
	}

	@Override
	public boolean canAttack() {
		if (!player.getController().canUseCombatType(player, player.getCombat().getCombatType())) {
			return false;
		}

		Entity attacking = player.getCombat().getAttacking();
		CombatTypes type = player.getCombat().getCombatType();

		if (player.getSpecialAttack().isInitialized() && player.getCombat().getCombatType() != CombatTypes.MAGIC
				&& (!player.getController().canUseSpecialAttack(player) || !SpecialAttackHandler.hasSpecialAmount(player))) {
			player.getSpecialAttack().toggleSpecial();
			player.getCombat().setAttackTimer(2);
			return false;
		}

		if (type == CombatTypes.MAGIC && !player.getMagic().getSpellCasting().canCast())
			return false;
		if (type == CombatTypes.RANGED && !player.getRanged().canUseRanged()) {
			return false;
		}

		if (type == CombatTypes.MELEE && attacking.isNpc()) {
			Mob mob = World.getNpcs()[attacking.getIndex()];
			if (mob != null) {
				for (int i : MobConstants.FLYING_MOBS) {
					if (mob.getId() == i) {
						player.getClient().queueOutgoingPacket(new SendMessage("You cannot reach this npc!"));
						return false;
					}
				}
			}

		}

		if (!player.inMultiArea() || !attacking.inMultiArea()) {
			if (player.getCombat().inCombat() && player.getCombat().getLastAttackedBy() != player.getCombat().getAttacking()) {
				player.getClient().queueOutgoingPacket(new SendMessage("You are already under attack."));
				return false;
			}
			
			if (attacking.getCombat().inCombat() && attacking.getCombat().getLastAttackedBy() != player
					&& !player.getSummoning().isFamiliar(attacking.getCombat().getLastAttackedBy())) {
				player.getClient().queueOutgoingPacket(new SendMessage("This "
						+ (player.getCombat().getAttacking().isNpc() ? "monster" : "player") + " is already under attack."));
				return false;
			}

		}

		if (!attacking.isNpc()) {
			Player other = World.getPlayers()[attacking.getIndex()];

			if (other != null && !player.getController().canAttackPlayer(player, other))
				return false;
		} else {
			Mob mob = World.getNpcs()[attacking.getIndex()];

			if (mob != null) {
				if (mob instanceof FamiliarMob) {
					if (!mob.inWilderness())
						return false;
					if (mob.getOwner().equals(player)) {
						player.getClient().queueOutgoingPacket(new SendMessage("You cannot attack your own familiar!"));
						return false;
					}
				}

				if (!player.getController().canAttackNPC()) {
					player.getClient().queueOutgoingPacket(new SendMessage("You can't attack NPCs here."));
					return false;
				}
				if (!SlayerMonsters.canAttackMob(player, mob))
					return false;
				if (!mob.getDefinition().isAttackable()
						|| mob.getOwner() != null && !mob.getOwner().equals(this) && mob instanceof FamiliarMob) {
					player.getClient().queueOutgoingPacket(new SendMessage("You can't attack this NPC."));
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public void updateCombatType() {
		CombatTypes type;
		if (player.getMagic().getSpellCasting().isCastingSpell()) {
			type = CombatTypes.MAGIC;
		} else {
			type = EquipmentConstants.getCombatTypeForWeapon(player);
		}

		player.getCombat().setCombatType(type);

		switch (type) {
		case MELEE:
			player.getEquipment().updateMeleeDataForCombat();
			break;
		case RANGED:
			player.getEquipment().updateRangedDataForCombat();
			break;
		default:
			break;
		}
	}

	@Override
	public void onCombatProcess(Entity entity) {
		if (player.getSpecialAttack().isInitialized() && player.getCombat().getCombatType() != CombatTypes.MAGIC) {
			SpecialAttackHandler.handleSpecialAttack(player);
			if (player.getCombat().getCombatType() == CombatTypes.RANGED) {
				player.getRanged().doActionsForDarkBow(entity);
			}
		} else if (player.getCombat().getCombatType() == CombatTypes.RANGED) {
			BoltSpecials.checkForBoltSpecial(player, entity);
			BarrowsSpecials.checkForBarrowsSpecial(player);
			player.getRanged().doActionsForDarkBow(entity);
		} else {
			if (player.getMagic().isDFireShieldEffect()) {
				player.getMagic().decrDragonFireShieldCharges();
			}

			BarrowsSpecials.checkForBarrowsSpecial(player);
		}

		if (player.getCombat().getCombatType() != CombatTypes.MAGIC) {
			PlayerSounds.sendSoundForId(player, player.getSpecialAttack().isInitialized(),
					player.getEquipment().getItems()[3] != null ? player.getEquipment().getItems()[3].getId() : 0);
		}

		if (player.getCombat().getCombatType() == CombatTypes.MAGIC) {
			player.getMagic().getSpellCasting().appendMultiSpell(player);
			player.getAchievements().incr(player, "Cast 1,000 spells");
			player.getAchievements().incr(player, "Cast 10,000 spells");
		}

		PoisonWeapons.checkForPoison(player, entity);

		player.getDegrading().degradeWeapon(player);
	}

	@Override
	public void afterCombatProcess(Entity entity) {
		if (player.getSpecialAttack().isInitialized() && player.getCombat().getCombatType() != CombatTypes.MAGIC) {
			SpecialAttackHandler.executeSpecialEffect(player, entity);
			player.getSpecialAttack().afterSpecial();
		}
		
		if (!player.getMagic().isDFireShieldEffect()) {
			player.getMagic().getSpellCasting().resetOnAttack();
		}
		
		player.getMelee().afterCombat();
		
		//update combat type
		player.updateCombatType();
	}

	@Override
	public void onAttack(Entity attack, int hit, CombatTypes type, boolean success) {
		if (success || type == CombatTypes.MAGIC) {
			player.getSkill().addCombatExperience(type, hit);
		}

		if (!attack.isNpc()) {
			Player p = World.getPlayers()[attack.getIndex()];
			if (p != null) {
				player.getSkulling().checkForSkulling(player, p);
			}
		}

		switch (type) {
		case MAGIC:
			if (success) {
				MagicEffects.doMagicEffects(player, attack, player.getMagic().getSpellCasting().getCurrentSpellId());
			}
			player.getMagic().getSpellCasting().removeRunesForAttack();
			break;
		case MELEE:
			break;
		case RANGED:
			player.getRanged().removeArrowsOnAttack();
			
			if (player.getRanged().isOnyxEffectActive()) {
				player.getRanged().doOnyxEffect(hit);
			}
			break;
		}
	}

	@Override
	public int getAccuracy(Entity paramEntity, CombatTypes paramCombatTypes) {
		return AccuracyFormulas.getAccuracy(player, paramEntity, paramCombatTypes);
	}

	@Override
	public int getMaxHit(CombatTypes type) {
		double mod = 1.0D;

		if ((player.getController().equals(ARConstants.AR_CONTROLLER)) && (player.getAttributes().get("extradamagepowerup") != null)) {
			mod += 0.35D;
		}

		switch (type) {
		case MAGIC:
			return (int) (MaxHitFormulas.getMagicMaxHit(player) * mod);
		case MELEE:
			return (int) (MaxHitFormulas.getMeleeMaxHit(player) * mod);
		case RANGED:
			return (int) (MaxHitFormulas.getRangedMaxHit(player) * mod);
		}
		
		return (int) (MaxHitFormulas.getMeleeMaxHit(player) * mod);
	}

	@Override
	public int getCorrectedDamage(int damage) {
		Item weapon = player.getEquipment().getItems()[3];
		Item ammo = player.getEquipment().getItems()[13];
		
		if (player.getCombat().getCombatType() == CombatTypes.RANGED) {
			if (player.getSpecialAttack().isInitialized()) {
				if ((weapon != null) && (weapon.getId() == 11235)) {
					
					if (ammo != null) {
						if (ammo.getId() == 11212 || ammo.getId() == 11227 || ammo.getId() == 0
								|| ammo.getId() == 11228) {
							if (damage < 7) {
								return 7;
							}
						} else if (damage < 5) {
							return 5;
						}
					}
				}
			}
		} else if (player.getCombat().getCombatType() == CombatTypes.MAGIC && ItemCheck.hasDFireShield(player)
				&& player.getMagic().isDFireShieldEffect() && damage < 15) {
			return 15;
		}

		return damage;
	}

	@Override
	public boolean isIgnoreHitSuccess() {
		if (player.getCombat().getCombatType() == CombatTypes.RANGED && player.getSpecialAttack().isInitialized()) {
			Item weapon = player.getEquipment().getItems()[3];

			if (weapon != null && weapon.getId() == 11235) {
				return true;
			}

		}

		return false;
	}

	@Override
	public void checkForDeath() {
		if (player.getLevels()[3] <= 0 && !player.isDead()) {
			TaskQueue.queue(new PlayerDeathTask(player));
			player.getAchievements().incr(player, "Die 100 times");
			player.getAchievements().incr(player, "Die 1000 times");
		}
	}

}
