package org.endeavor.game.content.skill.ranged;

import java.io.Serializable;

import org.endeavor.engine.definitions.RangedWeaponDefinition;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.combat.impl.Ranged;
import org.endeavor.game.content.minigames.armsrace.ARConstants;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.Projectile;
import org.endeavor.game.entity.item.BasicItemContainer;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.item.ItemContainer;
import org.endeavor.game.entity.mob.impl.SeaTrollQueen;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class RangedSkill implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7240489214594553811L;
	private final Player player;
	private Item arrow = null;

	private Location aLocation = null;

	private final ItemContainer savedArrows = new BasicItemContainer(40);

	private boolean onyxEffectActive = false;
	private boolean karilEffectActive = false;

	public RangedSkill(Player player) {
		this.player = player;
	}

	public void getFromAvasAccumulator() {
		for (Item i : savedArrows.getItems())
			if (i != null) {
				int r = player.getInventory().add(new Item(i));
				savedArrows.remove(new Item(i.getId(), r));
			}
	}

	public boolean canUseRanged() {
		Item weapon = player.getEquipment().getItems()[3];
		Item ammo = player.getEquipment().getItems()[13];

		if ((weapon == null) || (weapon.getRangedDefinition() == null)) {
			return false;
		}

		RangedWeaponDefinition def = weapon.getRangedDefinition();
		Item[] arrows = def.getArrows();

		if ((def.getType() == RangedWeaponDefinition.RangedTypes.SHOT) && (requiresArrow(player, weapon.getId()))
				&& (arrows != null) && (arrows.length != 0)) {
			if (ammo == null) {
				player.getClient().queueOutgoingPacket(
						new SendMessage("You do not have the correct ammo to use this weapon."));
				return false;
			}

			boolean has = false;

			for (Item i : arrows) {
				if (i != null) {
					if ((ammo.equals(i)) && (ammo.getAmount() >= i.getAmount())) {
						has = true;
					}
				}
			}
			if (!has) {
				player.getClient().queueOutgoingPacket(
						new SendMessage("You do not have the correct ammo to use this weapon."));
				return false;
			}

		}

		return true;
	}

	public void doActionsForDarkBow(Entity attacking) {
		Item weapon = player.getEquipment().getItems()[3];

		if ((weapon == null) || (weapon.getId() != 11235)) {
			return;
		}

		Ranged r = player.getCombat().getRanged();

		player.getSpecialAttack().isInitialized();

		r.setProjectile(new Projectile(player.getCombat().getRanged().getProjectile().getId()));
		r.getProjectile().setDelay(35);

		r.execute(attacking);
		r.setProjectile(new Projectile(player.getCombat().getRanged().getProjectile().getId()));

		player.getSpecialAttack().isInitialized();
	}

	public static final boolean requiresArrow(Player player, int id) {
		if (id == 4214) {
			return false;
		}

		Item weapon = player.getEquipment().getItems()[3];

		if ((weapon == null) || (weapon.getRangedDefinition() == null)
				|| (weapon.getRangedDefinition().getType() == RangedWeaponDefinition.RangedTypes.THROWN)) {
			return false;
		}

		return true;
	}

	public void removeArrowsOnAttack() {
		Item weapon = player.getEquipment().getItems()[3];
		Item ammo = player.getEquipment().getItems()[13];
		Item cape = player.getEquipment().getItems()[1];

		if ((weapon == null) || (weapon.getRangedDefinition() == null)) {
			return;
		}

		RangedWeaponDefinition def = weapon.getRangedDefinition();

		if ((requiresArrow(player, weapon.getId())) && (Misc.randomNumber(100) <= 70) && (cape != null)
				&& (cape.getId() == 10499)) {
			if ((def != null) && (Misc.randomNumber(100) <= 40)) {
				if ((def.getType() == RangedWeaponDefinition.RangedTypes.SHOT) && (ammo != null))
					savedArrows.add(ammo.getId(), 1, false);
				else if ((def.getType() == RangedWeaponDefinition.RangedTypes.THROWN) && (weapon != null)) {
					savedArrows.add(weapon.getId(), 1, false);
				}

			}

			arrow = null;
			return;
		}

		switch (def.getType()) {
		case SHOT:
			Item[] arrows = weapon.getRangedDefinition().getArrows();

			for (Item i : arrows) {
				if (i != null) {
					if ((ammo.equals(i)) && (ammo.getAmount() >= i.getAmount())) {
						arrow = new Item(i.getId(), i.getAmount());
						break;
					}
				}
			}
			if (ammo != null && arrow != null) {
				ammo.remove(arrow.getAmount());

				if (ammo.getAmount() == 0) {
					player.getEquipment().unequip(13);
					player.getClient().queueOutgoingPacket(new SendMessage("You have run out of ammo."));
				} else {
					player.getEquipment().update(13);
				}

				Entity attack = player.getCombat().getAttacking();
				aLocation = (attack == null ? player.getLocation() : attack.getLocation());
				
				if (attack != null && attack instanceof SeaTrollQueen) {
					aLocation = new Location(2344, 3699);
				}
			}
			break;
		case THROWN:
			weapon.remove(1);
			if (weapon.getAmount() == 0)
				player.getEquipment().unequip(3);
			else {
				player.getEquipment().update(3);
			}

			arrow = new Item(weapon.getId(), 1);
			Entity attack = player.getCombat().getAttacking();
			aLocation = (attack == null ? player.getLocation() : attack.getLocation());
			break;
		}
	}

	public void doOnyxEffect(int damage) {
		if (damage > 0) {
			int max = player.getMaxLevels()[3];
			int newLvl = player.getSkill().getLevels()[3] + (int) (damage * 0.25D);
			int set = newLvl > max ? max : newLvl;
			player.getSkill().getLevels()[3] = ((byte) set);
			player.getSkill().update(3);
			player.getClient().queueOutgoingPacket(new SendMessage("You absorb some of your opponent's hitpoints."));
		}
		onyxEffectActive = false;
	}

	public void dropArrowAfterHit() {
		if (player.getController().equals(ARConstants.AR_CONTROLLER)) {
			return;
		}

		if ((arrow == null) || (aLocation == null)) {
			return;
		}

		if ((arrow.getId() == 15243) || (arrow.getId() == 4740)) {
			return;
		}

		if (Misc.randomNumber(2) == 0) {
			player.getGroundItems().drop(arrow, aLocation);
		} else {
			
			Item cape = player.getEquipment().getItems()[1];

			if (cape != null && cape.getId() == 10499) {
				if (Misc.randomNumber(100) <= 40) {
					savedArrows.add(arrow.getId(), 1, false);
				}
			}
		}

		arrow = null;
		aLocation = null;
	}

	public boolean isOnyxEffectActive() {
		return onyxEffectActive;
	}

	public void setOnyxEffectActive(boolean onyxEffectActive) {
		this.onyxEffectActive = onyxEffectActive;
	}

	public boolean isKarilEffectActive() {
		return karilEffectActive;
	}

	public void setKarilEffectActive(boolean karilEffectActive) {
		this.karilEffectActive = karilEffectActive;
	}

	public ItemContainer getSavedArrows() {
		return savedArrows;
	}
}
