package org.endeavor.game.content.minigames.armsrace;

import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.skill.magic.Autocast;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.Controller;

public class ARConstants {
	public static final Controller AR_WAIT_CONTROLLER = new ARWaitingController();
	public static final Controller AR_CONTROLLER = new ARController();
	//public static final int GAME_TIMER = 300;
	//public static final int SWITCH_TIMER = 70;
	public static final String DAMAGE_KEY = "argamedamage";
	public static final String EXTRA_DMG_POWERUP = "extradamagepowerup";
	public static final String ATTACK_TIMER_POWERUP = "attacktimerpowerup";
	public static final String KILL_STREAK = "killstreakar";
	public static final Location START = new Location(3272, 5065);
	private static final Location START_MAX = new Location(3292, 5086);

	public static final Location TO_WAITING = new Location(1653, 5600);
	public static final Location FROM_WAITING = new Location(1718, 5598);

	public static ARSwitch[] AR_SWITCHES = { new ARSwitch() {
		@Override
		public void execute(Player p) {
			ARConstants.setWeapon(p, 11694);
		}
	}, new ARSwitch() {
		@Override
		public void execute(Player p) {
			ARConstants.setWeapon(p, 14484);
		}
	}, new ARSwitch() {
		@Override
		public void execute(Player p) {
			ARConstants.setWeapon(p, 18351);
		}
	}, new ARSwitch() {
		@Override
		public void execute(Player p) {
			ARConstants.setWeapon(p, 4151);
		}
	}, new ARSwitch() {
		@Override
		public void execute(Player p) {
			ARConstants.setWeapon(p, 4214);
		}
	}, new ARSwitch() {
		@Override
		public void execute(Player p) {
			ARConstants.setWeapon(p, 5698);
		}
	}, new ARSwitch() {
		@Override
		public void execute(Player p) {
			ARConstants.setWeapon(p, 15403);
		}
	}, new ARSwitch() {
		@Override
		public void execute(Player p) {
			p.getEquipment().getItems()[0] = new Item(4716);
			p.getEquipment().getItems()[3] = new Item(4718);
			p.getEquipment().getItems()[4] = new Item(4720);
			p.getEquipment().getItems()[7] = new Item(4722);
		}
	}, new ARSwitch() {
		@Override
		public void execute(Player p) {
			p.getEquipment().getItems()[0] = new Item(4753);
			p.getEquipment().getItems()[3] = new Item(4755);
			p.getEquipment().getItems()[4] = new Item(4757);
			p.getEquipment().getItems()[7] = new Item(4759);
		}
	}, new ARSwitch() {
		@Override
		public void execute(Player p) {
			p.getEquipment().getItems()[3] = new Item(811, 350);
		}
	}, new ARSwitch() {
		@Override
		public void execute(Player p) {
			p.getEquipment().getItems()[3] = new Item(9185);
			p.getEquipment().getItems()[13] = new Item(9245, 250);
		}
	}, new ARSwitch() {
		@Override
		public void execute(Player p) {
			ARConstants.setWeapon(p, 1434);
		}
	}, new ARSwitch() {
		@Override
		public void execute(Player p) {
			ARConstants.setWeapon(p, 4566);
		}
	}, new ARSwitch() {
		@Override
		public void execute(Player p) {
			ARConstants.setWeapon(p, 15241);
			p.getEquipment().getItems()[13] = new Item(15243, 150);
		}
	}, new ARSwitch() {
		@Override
		public void execute(Player p) {
			p.getEquipment().getItems()[0] = new Item(13896);
			p.getEquipment().getItems()[3] = new Item(13899);
			p.getEquipment().getItems()[4] = new Item(13887);
			p.getEquipment().getItems()[7] = new Item(13893);
		}
	}, new ARSwitch() {
		@Override
		public void execute(Player p) {
			ARConstants.setWeapon(p, 15486);
			Autocast.setAutocast(p, 12891);
		}
	}, new ARSwitch() {
		@Override
		public void execute(Player p) {
			ARConstants.setWeapon(p, 861);
			p.getEquipment().getItems()[13] = new Item(892, 350);
		}
	}, new ARSwitch() {
		@Override
		public void execute(Player p) {
			ARConstants.setWeapon(p, 11235);
			p.getEquipment().getItems()[13] = new Item(11212, 350);
		}
	}, new ARSwitch() {
		@Override
		public void execute(Player p) {
			ARConstants.setWeapon(p, 4747);
		}
	}, new ARSwitch() {
		@Override
		public void execute(Player p) {
			ARConstants.setWeapon(p, 11730);
		}
	}, new ARSwitch() {
		@Override
		public void execute(Player p) {
			p.getEquipment().getItems()[5] = new Item(11283);
		}
	}, new ARSwitch() {
		@Override
		public void execute(Player p) {
			p.getEquipment().getItems()[3] = new Item(13879, 250);
		}
	}, new ARSwitch() {
		@Override
		public void execute(Player p) {
			ARConstants.setWeapon(p, 15486);
			Autocast.setAutocast(p, 12871);
		}
	}, new ARSwitch() {
		@Override
		public void execute(Player p) {
			ARConstants.setWeapon(p, 15486);
			Autocast.setAutocast(p, 12929);
		}
	}, new ARSwitch() {
		@Override
		public void execute(Player p) {
			ARConstants.setWeapon(p, 15486);
			Autocast.setAutocast(p, 13023);
		}
	}, new ARSwitch() {
		@Override
		public void execute(Player p) {
			ARConstants.setWeapon(p, 15486);
			Autocast.setAutocast(p, 12975);
		}
	} };

	public static int getXMod() {
		return Misc.randomNumber(START_MAX.getX() - START.getX());
	}

	public static int getYMod() {
		return Misc.randomNumber(START_MAX.getY() - START.getY());
	}

	public static void reset(Player p) {
	}

	public static void setWeapon(Player p, int id) {
		p.getEquipment().getItems()[3] = new Item(id);
	}
}
