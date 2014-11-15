package org.endeavor.game.content;

import org.endeavor.game.entity.player.Player;

public class BarrowsSet {

	public static boolean openBarrowsSet(Player player, int itemId) {
		switch(itemId) {
		case 11846: // ahrim
			player.getInventory().add(4708, 1);
			player.getInventory().add(4710, 1);
			player.getInventory().add(4712, 1);
			player.getInventory().add(4714, 1);
			player.getInventory().remove(11846);
			return true;
		case 11850: // guthans
			player.getInventory().add(4724, 1);
			player.getInventory().add(4726, 1);
			player.getInventory().add(4728, 1);
			player.getInventory().add(4730, 1);
			player.getInventory().remove(11850);
			return true;
		case 11852: // karils
			player.getInventory().add(4732, 1);
			player.getInventory().add(4734, 1);
			player.getInventory().add(4736, 1);
			player.getInventory().add(4738, 1);
			player.getInventory().remove(11852);
			return true;
		case 11854: // torags
			player.getInventory().add(4745, 1);
			player.getInventory().add(4747, 1);
			player.getInventory().add(4749, 1);
			player.getInventory().add(4751, 1);
			player.getInventory().remove(11854);
			return true;
		case 11856: // veracs
			player.getInventory().add(4753, 1);
			player.getInventory().add(4755, 1);
			player.getInventory().add(4757, 1);
			player.getInventory().add(4759, 1);
			player.getInventory().remove(11856);
			return true;
		}
		return false;
		
	}
	
}
