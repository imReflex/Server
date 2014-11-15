package org.endeavor.game.content.skill.summoning;

import org.endeavor.GameSettings;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class SummoningCreation {
	public static boolean create(Player p, int button) {
		if (((button >= 155031) && (button <= 156006)) || ((button >= 156000) && (button <= 156006))) {
			int index = (button - 155031) / 3;

			if ((button >= 156000) && (button <= 156006)) {
				index = 75 + (button - 156000) / 3;
			}

			if ((index >= 0) && (index < Pouch.values().length)) {
				Pouch pouch = Pouch.values()[index];

				if (pouch != null) {
					if (!p.getInventory().hasSpaceFor(new Item(pouch.pouchId, 1))) {
						p.getClient().queueOutgoingPacket(
								new SendMessage("You must free some inventory space to do this."));
						return true;
					}

					int shards = pouch.shardsRequired;
					int ingredient = pouch.secondIngredientId;
					int charm = pouch.charmId;

					if (!GameSettings.DEV_MODE) {
						if (p.getMaxLevels()[21] < pouch.levelRequired) {
							p.getClient().queueOutgoingPacket(
									new SendMessage("You need a Summoning level of " + pouch.levelRequired
											+ " to make this."));
							return true;
						}
	
						if (!p.getInventory().hasItemAmount(new Item(12525, 1))) {
							p.getClient().queueOutgoingPacket(new SendMessage("You do not have an empty pouch."));
							return true;
						}
	
						if (!p.getInventory().hasItemAmount(new Item(18016, shards))) {
							p.getClient().queueOutgoingPacket(
									new SendMessage("You do not have enough shards to create this."));
							return true;
						}
	
						if (!p.getInventory().hasItemAmount(new Item(ingredient, 1))) {
							String name = Item.getDefinition(ingredient).getName();
							p.getClient().queueOutgoingPacket(
									new SendMessage("You need " + Misc.getAOrAn(name) + " " + name + " to create this."));
							return true;
						}
	
						if (!p.getInventory().hasItemAmount(new Item(charm, 1))) {
							String name = Item.getDefinition(charm).getName();
							p.getClient().queueOutgoingPacket(
									new SendMessage("You need " + Misc.getAOrAn(name) + " " + name + " to create this."));
							return true;
						}
					}

					p.getInventory().remove(new Item(12525, 1), false);
					p.getInventory().remove(new Item(18016, shards), false);
					p.getInventory().remove(new Item(ingredient, 1), false);
					p.getInventory().remove(new Item(charm, 1), false);
					p.getInventory().add(new Item(pouch.pouchId), true);
					p.getSkill().addExperience(21, pouch.experience);
				}
			}
		} else if (((button >= 151055) && (button <= 151253)) || ((button >= 152000) && (button <= 152030))) {
			int index = (button - 151055) / 3;

			if ((button >= 152000) && (button <= 152030)) {
				index = 67 + (button - 152000) / 3;
			}

			if ((index >= 0) && (index < Pouch.values().length)) {
				Scroll scroll = Scroll.values()[index];

				if (scroll != null) {
					if (!p.getInventory().hasSpaceFor(new Item(scroll.itemId, 1))) {
						p.getClient().queueOutgoingPacket(
								new SendMessage("You must free some inventory space to do this."));
						return true;
					}

					Item pouch = scroll.pouch;

					if (p.getMaxLevels()[21] < scroll.levelRequired) {
						p.getClient().queueOutgoingPacket(
								new SendMessage("You need a Summoning level of " + scroll.levelRequired
										+ " to make this."));
						return true;
					}

					if (!p.getInventory().hasItemAmount(new Item(pouch))) {
						String name = pouch.getDefinition().getName();
						p.getClient().queueOutgoingPacket(
								new SendMessage("You need " + Misc.getAOrAn(name) + " " + name + " to make this."));
						return true;
					}

					p.getInventory().remove(new Item(pouch), false);
					p.getInventory().add(new Item(scroll.itemId, 10), true);
					p.getSkill().addExperience(21, scroll.experience);
				}
			}
		}

		return false;
	}
}
