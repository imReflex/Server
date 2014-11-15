package org.endeavor.game.content;

import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.content.dialogue.impl.ConfirmDialogue;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendSound;

public class ItemCreation {
	public static final int SHARD_1 = 11710;
	public static final int SHARD_2 = 11712;
	public static final int SHARD_3 = 11714;
	public static final int BLADE = 11690;
	public static final int ARMADYL_HILT = 11702;
	public static final int BANDOS_HILT = 11704;
	public static final int SARADOMIN_HILT = 11706;
	public static final int ZAMORAK_HILT = 11708;
	public static final int ARMADYL_GODSWORD = 11694;
	public static final int BANDOS_GODSWORD = 11696;
	public static final int SARADOMIN_GODSWORD = 11698;
	public static final int ZAMORAK_GODSWORD = 11700;
	public static final int VISAGE = 11286;
	public static final int ANTI_D_FIRE_SHIELD = 1540;
	public static final int DRAGON_FIRE_SHIELD = 11283;
	public static final int HOLY_ELIXIR = 13754;
	public static final int SPIRIT_SHIELD = 13734;
	public static final int BLESSED_SPIRIT_SHIELD = 13736;
	public static final int SPECTRAL_SIGIL = 13752;
	public static final int ARCANE_SIGIL = 13746;
	public static final int DIVINE_SIGIL = 13748;
	public static final int ELYSIAN_SIGIL = 13750;
	public static final int SPECTRAL_SH = 13744;
	public static final int ARCANE_SH = 13738;
	public static final int DIVINE_SH = 13740;
	public static final int ELYSIAN_SH = 13742;
	public static final int WOOL = 1759;
	public static final int DRAGON_PLATEBODY_ID = 14479;
	public static final int DRAGON_PLATE_PIECE_1 = 14472;
	public static final int DRAGON_PLATE_PIECE_2 = 14474;
	public static final int DRAGON_PLATE_PIECE_3 = 14476;
	public static final int[][] AMULET_STRINGING_DATA = { { 1673, 1692 }, { 1675, 1727 }, { 1677, 1729 },
			{ 1679, 1725 }, { 1681, 1731 }, { 1683, 1704 }, { 6579, 6585 } };

	public static final int[][] BOLT_CREATION_DATA = { { 9142, 9189, 9240 }, { 9142, 9190, 9241 },
			{ 9143, 9191, 9242 }, { 9143, 9192, 9243 }, { 9144, 9193, 9244 }, { 9144, 9194, 9245 } };
	public static final String CREATE_BLADE_FAILURE = "You need all 3 shards to create a Godsword blade.";
	public static final String CREATE_BLADE_SUCCESS = "You create a Godsword blade.";
	public static final String CREATE_GODSWORD_PREFIX = "You create a";
	public static final String NOT_ENOUGH_INVENTORY_SPACE = "You must clear some inventory space to disassemble your Godsword.";
	public static final String DISSASSEMBLE_MESSAGE = "You take apart your Godsword.";
	public static final String CREATE_DFIRE_SHIELD = "You bind the Draconic visage to the Anti-dragon shield.";
	public static final String CREATE_SPIRIT_SHIELD = "You pour the Holy elixir onto the Spirit shield to bless it.";
	public static final String CREATE_SPIRIT_SHIELD_A = "You combine the Blessed spirit shield and ";
	public static final String CREATE_SPIRIT_SHIELD_B = " to create the ";
	public static final int CRYSTAL_KEY_ID = 989;

	public static boolean createSomething(Player p, int item1, int item2) {
		if (item1 == 4151 && item2 == 21369 || item1 == 21369 && item2 == 4151) {
			if (p.getInventory().hasItems(new Item[] { new Item(21369), new Item(4151) }) == -1) {
				if (!p.isMember()) {
					DialogueManager.sendStatement(p, "You must be a member to do this.");
					return true;
				}
				
				p.getInventory().remove(item1, 1, false);
				p.getInventory().remove(item2, 1, false);
				p.getInventory().add(21371, 1, true);
				p.getClient().queueOutgoingPacket(new SendMessage("You create an Abyssal vine whip."));
			}
			return true;
		}
		
		if ((item1 == 15490) || (item1 == 13263) || (item1 == 15488)) {
			if (p.getInventory().hasItems(new Item[] { new Item(15490), new Item(13263), new Item(15488) }) == -1) {
				p.getInventory().remove(15490, 1, false);
				p.getInventory().remove(13263, 1, false);
				p.getInventory().remove(15488, 1, false);
				p.getInventory().add(15492, 1, true);
				p.getClient().queueOutgoingPacket(new SendMessage("You create a Full slayer helmet."));
				return true;
			}
		}
		
		if (item1 == 314) {
			if (item2 == 52) {
				if (p.getInventory().hasSpaceFor(new Item(53, 15))) {
					int amount = 15;
					final int a1 = p.getInventory().getItemAmount(item1);
					final int a2 = p.getInventory().getItemAmount(item2);
					
					if (a1 < a2) {
						amount = a1;
					} else {
						amount = a2;
					}
					
					if (amount == 0) {
						return true;
					}
					
					p.getInventory().remove(item1, 15, false);
					p.getInventory().remove(item2, 15, false);
					p.getInventory().add(53, 15, true);
				} else {
					p.getClient().queueOutgoingPacket(new SendMessage("You do not have enough inventory space to do this."));
				}
				
				return true;
			}
		} else if (item2 == 314) {
			if (item1 == 52) {
				if (p.getInventory().hasSpaceFor(new Item(53, 15))) {
					int amount = 15;
					final int a1 = p.getInventory().getItemAmount(item1);
					final int a2 = p.getInventory().getItemAmount(item2);
					
					if (a1 < a2) {
						amount = a1;
					} else {
						amount = a2;
					}
					
					if (amount == 0) {
						return true;
					}
					
					p.getInventory().remove(item1, 15, false);
					p.getInventory().remove(item2, 15, false);
					p.getInventory().add(53, 15, true);
				} else {
					p.getClient().queueOutgoingPacket(new SendMessage("You do not have enough inventory space to do this."));
				}
				
				return true;
			}
		}

		return false;
	}

	public static boolean takeApartSlayerHelm(Player p, int item1) {
		if (item1 == 15492) {
			if (p.getInventory().hasSpaceFor(new Item[] { new Item(15490), new Item(13263), new Item(15488) })) {
				p.getInventory().add(new Item[] { new Item(15490), new Item(13263), new Item(15488) }, true);
				p.getClient().queueOutgoingPacket(new SendMessage("You take apart your Full slayer helmet."));
			}
		}

		return false;
	}

	public static boolean useDieOnWhip(Player p, int item1, int item2) {
		if (item1 == 4151) {
			if (item2 == 1765) {
				p.getInventory().remove(item1, 1, false);
				p.getInventory().remove(item2, 1, false);
				p.getInventory().add(15441, 1, true);
				p.getClient().queueOutgoingPacket(new SendMessage("You die your whip."));
				return true;
			}
			if (item2 == 1767) {
				p.getInventory().remove(item1, 1, false);
				p.getInventory().remove(item2, 1, false);
				p.getInventory().add(15442, 1, true);
				p.getClient().queueOutgoingPacket(new SendMessage("You die your whip."));
				return true;
			}
			if (item2 == 1771) {
				p.getInventory().remove(item1, 1, false);
				p.getInventory().remove(item2, 1, false);
				p.getInventory().add(15444, 1, true);
				p.getClient().queueOutgoingPacket(new SendMessage("You die your whip."));
				return true;
			}
			if (item2 == 1769) {
				p.getInventory().remove(item1, 1, false);
				p.getInventory().remove(item2, 1, false);
				p.getInventory().add(15443, 1, true);
				p.getClient().queueOutgoingPacket(new SendMessage("You die your whip."));
				return true;
			}
		} else if (item2 == 4151) {
			if (item1 == 1765) {
				p.getInventory().remove(item1, 1, false);
				p.getInventory().remove(item2, 1, false);
				p.getInventory().add(15441, 1, true);
				p.getClient().queueOutgoingPacket(new SendMessage("You die your whip."));
				return true;
			}
			if (item1 == 1767) {
				p.getInventory().remove(item1, 1, false);
				p.getInventory().remove(item2, 1, false);
				p.getInventory().add(15442, 1, true);
				p.getClient().queueOutgoingPacket(new SendMessage("You die your whip."));
				return true;
			}
			if (item1 == 1771) {
				p.getInventory().remove(item1, 1, false);
				p.getInventory().remove(item2, 1, false);
				p.getInventory().add(15444, 1, true);
				p.getClient().queueOutgoingPacket(new SendMessage("You die your whip."));
				return true;
			}
			if (item2 == 1769) {
				p.getInventory().remove(item1, 1, false);
				p.getInventory().remove(item2, 1, false);
				p.getInventory().add(15443, 1, true);
				p.getClient().queueOutgoingPacket(new SendMessage("You die your whip."));
				return true;
			}
		}

		return false;
	}

	public static boolean immbueRing(Player p, int item1, int item2) {
		if (item1 == 6951) {
			if (item2 == 6731) {
				p.getInventory().remove(item1, 1, false);
				p.getInventory().remove(item2, 1, false);
				p.getInventory().add(15018, 1, true);
				p.getClient().queueOutgoingPacket(new SendMessage("You imbue your ring."));
			} else if (item2 == 6733) {
				p.getInventory().remove(item1, 1, false);
				p.getInventory().remove(item2, 1, false);
				p.getInventory().add(15019, 1, true);
				p.getClient().queueOutgoingPacket(new SendMessage("You imbue your ring."));
			} else if (item2 == 6735) {
				p.getInventory().remove(item1, 1, false);
				p.getInventory().remove(item2, 1, false);
				p.getInventory().add(15020, 1, true);
				p.getClient().queueOutgoingPacket(new SendMessage("You imbue your ring."));
			} else if (item2 == 6737) {
				p.getInventory().remove(item1, 1, false);
				p.getInventory().remove(item2, 1, false);
				p.getInventory().add(15220, 1, true);
				p.getClient().queueOutgoingPacket(new SendMessage("You imbue your ring."));
			}
		} else if (item2 == 6951) {
			if (item1 == 6731) {
				p.getInventory().remove(item1, 1, false);
				p.getInventory().remove(item2, 1, false);
				p.getInventory().add(15018, 1, true);
				p.getClient().queueOutgoingPacket(new SendMessage("You imbue your ring."));
			} else if (item1 == 6733) {
				p.getInventory().remove(item1, 1, false);
				p.getInventory().remove(item2, 1, false);
				p.getInventory().add(15019, 1, true);
				p.getClient().queueOutgoingPacket(new SendMessage("You imbue your ring."));
			} else if (item1 == 6735) {
				p.getInventory().remove(item1, 1, false);
				p.getInventory().remove(item2, 1, false);
				p.getInventory().add(15020, 1, true);
				p.getClient().queueOutgoingPacket(new SendMessage("You imbue your ring."));
			} else if (item1 == 6737) {
				p.getInventory().remove(item1, 1, false);
				p.getInventory().remove(item2, 1, false);
				p.getInventory().add(15220, 1, true);
				p.getClient().queueOutgoingPacket(new SendMessage("You imbue your ring."));
			}
		}

		return false;
	}

	public static boolean createBolts(Player p, int item1, int item2) {
		for (int[] i : BOLT_CREATION_DATA) {
			if (((item1 == i[0]) && (item2 == i[1])) || ((item1 == i[1]) && (item2 == i[0]))) {
				if (!p.getInventory().hasSpaceFor(new Item(i[2], 15))) {
					p.getClient().queueOutgoingPacket(
							new SendMessage("You do not have enough inventory space to do this."));
					return true;
				}

				int am = 0;
				int am1 = p.getInventory().getItemAmount(item1);
				int am2 = p.getInventory().getItemAmount(item2);

				if ((am1 < am2) || (am2 < am1))
					am = am1 < am2 ? am1 : am2;
				else if (am1 == am2) {
					am = am1;
				}

				if (am > 15) {
					am = 15;
				}

				p.getInventory().remove(item1, am, false);
				p.getInventory().remove(item2, am, false);
				p.getInventory().add(i[2], am, true);
				return true;
			}
		}

		return false;
	}

	public static boolean createCrystalKey(Player p, int item1, int item2) {
		if (item1 == 985) {
			if (item2 == 987) {
				p.getInventory().remove(985, 1, false);
				p.getInventory().remove(987, 1, false);
				p.getInventory().add(989, 1, true);

				return true;
			}
		} else if ((item1 == 987) && (item2 == 985)) {
			p.getInventory().remove(985, 1, false);
			p.getInventory().remove(987, 1, false);
			p.getInventory().add(989, 1, true);

			return true;
		}

		return false;
	}

	public static boolean createDragonPlatebody(Player p, int item1, int item2) {
		if ((item1 == 14472) || (item2 == 14472) || (item1 == 14474) || (item2 == 14474) || (item1 == 14476)
				|| (item2 == 14476)) {
			if ((p.getInventory().hasItemAmount(14472, 1)) && (p.getInventory().hasItemAmount(14474, 1))
					&& (p.getInventory().hasItemAmount(14476, 1))) {
				p.getInventory().remove(14472, 1, false);
				p.getInventory().remove(14474, 1, false);
				p.getInventory().remove(14476, 1, false);
				p.getInventory().add(14479, 1, true);

				p.getClient().queueOutgoingPacket(new SendMessage("You create a Dragon platebody."));
			} else {
				p.getClient().queueOutgoingPacket(
						new SendMessage("You need all the pieces to make the Dragon platebody."));
			}
			return true;
		}

		return false;
	}

	public static boolean useStringOnAmulet(Player p, int item1, int item2) {
		if ((item1 == 1759) || (item2 == 1759)) {
			for (int i = 0; i < AMULET_STRINGING_DATA.length; i++) {
				if ((item1 == AMULET_STRINGING_DATA[i][0]) || (item2 == AMULET_STRINGING_DATA[i][0])) {
					p.getInventory().remove(1759, 1, false);
					p.getInventory().remove(AMULET_STRINGING_DATA[i][0], 1, false);
					p.getInventory().add(AMULET_STRINGING_DATA[i][1], 1, true);
					p.getClient().queueOutgoingPacket(new SendMessage("You string the amulet."));
				}
			}

			return true;
		}

		return false;
	}

	public static final boolean fillVials(Player p, int item, int object) {
		if (object == 873 || object == 6097) {
			if (item == 229) {
				p.getClient().queueOutgoingPacket(new SendSound(371, 0, 0));
				int rem = p.getInventory().remove(229, 28, false);
				p.getInventory().add(227, rem, true);
				p.getClient().queueOutgoingPacket(new SendMessage("You fill your vials with water."));
				return true;
			}
		}

		return false;
	}

	public static final boolean createSpiritShield(Player p, int use1, int use2) {
		if (use2 == 13734) {
			if (use1 == 13754) {
				p.getInventory().remove(13734, 1, false);
				p.getInventory().remove(13754, 1, false);
				p.getInventory().add(13736, 1, true);
				p.getClient().queueOutgoingPacket(
						new SendMessage("You pour the Holy elixir onto the Spirit shield to bless it."));
				return true;
			}
		} else if ((use1 == 13734) && (use2 == 13754)) {
			p.getInventory().remove(13734, 1, false);
			p.getInventory().remove(13754, 1, false);
			p.getInventory().add(13736, 1, true);
			p.getClient().queueOutgoingPacket(
					new SendMessage("You pour the Holy elixir onto the Spirit shield to bless it."));
			return true;
		}

		if (use2 == 13736) {
			if (use1 == 13752) {
				p.getInventory().remove(13736, 1, false);
				p.getInventory().remove(13752, 1, false);
				p.getInventory().add(13744, 1, true);
				p.getClient()
						.queueOutgoingPacket(
								new SendMessage(
										"You combine the Blessed spirit shield and Spectral sigil to create the Spectral spirit shield."));
				return true;
			}
			if (use1 == 13746) {
				p.getInventory().remove(13736, 1, false);
				p.getInventory().remove(13746, 1, false);
				p.getInventory().add(13738, 1, true);
				p.getClient()
						.queueOutgoingPacket(
								new SendMessage(
										"You combine the Blessed spirit shield and Arcane sigil to create the Arcane spirit shield."));
				return true;
			}
			if (use1 == 13748) {
				p.getInventory().remove(13736, 1, false);
				p.getInventory().remove(13748, 1, false);
				p.getInventory().add(13740, 1, true);
				p.getClient()
						.queueOutgoingPacket(
								new SendMessage(
										"You combine the Blessed spirit shield and Divine sigil to create the Divine spirit shield."));
				return true;
			}
			if (use1 == 13750) {
				p.getInventory().remove(13736, 1, false);
				p.getInventory().remove(13750, 1, false);
				p.getInventory().add(13742, 1, true);
				p.getClient()
						.queueOutgoingPacket(
								new SendMessage(
										"You combine the Blessed spirit shield and Elysian sigil to create the Elysian spirit shield."));
				return true;
			}
		} else if (use1 == 13736) {
			if (use2 == 13752) {
				p.getInventory().remove(13736, 1, false);
				p.getInventory().remove(13752, 1, false);
				p.getInventory().add(13744, 1, true);
				p.getClient()
						.queueOutgoingPacket(
								new SendMessage(
										"You combine the Blessed spirit shield and Spectral sigil to create the Spectral spririt shield."));
				return true;
			}
			if (use2 == 13746) {
				p.getInventory().remove(13736, 1, false);
				p.getInventory().remove(13746, 1, false);
				p.getInventory().add(13738, 1, true);
				p.getClient()
						.queueOutgoingPacket(
								new SendMessage(
										"You combine the Blessed spirit shield and Arcane sigil to create the Arcane spririt shield."));
				return true;
			}
			if (use2 == 13748) {
				p.getInventory().remove(13736, 1, false);
				p.getInventory().remove(13748, 1, false);
				p.getInventory().add(13740, 1, true);
				p.getClient()
						.queueOutgoingPacket(
								new SendMessage(
										"You combine the Blessed spirit shield and Divine sigil to create the Divine spririt shield."));
				return true;
			}
			if (use2 == 13750) {
				p.getInventory().remove(13736, 1, false);
				p.getInventory().remove(13750, 1, false);
				p.getInventory().add(13742, 1, true);
				p.getClient()
						.queueOutgoingPacket(
								new SendMessage(
										"You combine the Blessed spirit shield and Elysian sigil to create the Elysian spririt shield."));
				return true;
			}
		}

		return false;
	}

	public static final boolean createDragonfireShield(Player p, int use1, int use2) {
		if (use1 == 11286) {
			if (use2 == 1540) {
				p.getInventory().remove(11286, 1, false);
				p.getInventory().remove(1540, 1, false);
				p.getInventory().add(11283, 1, true);
				p.getClient().queueOutgoingPacket(
						new SendMessage("You bind the Draconic visage to the Anti-dragon shield."));
				return true;
			}
		} else if ((use1 == 1540) && (use2 == 11286)) {
			p.getInventory().remove(11286, 1, false);
			p.getInventory().remove(1540, 1, false);
			p.getInventory().add(11283, 1, true);
			p.getClient().queueOutgoingPacket(
					new SendMessage("You bind the Draconic visage to the Anti-dragon shield."));
			return true;
		}

		return false;
	}

	public static final boolean disassemble(final Player p, final int id) {
		if (id == 21371) {
			p.start(new ConfirmDialogue(p) {
				@Override
				public void onConfirm() {
					if (!player.isMember()) {
						player.send(new SendMessage("You need to be a member to do this."));
						return;
					}
					
					if (!player.getInventory().hasItemId(21371)) {
						return;
					}
					
					if (player.getInventory().getFreeSlots() < 2) {
						player.send(new SendMessage("You do not have enough inventory space to do this."));
						return;
					}
					
					player.getInventory().remove(21371, 1, false);
					player.getInventory().add(21369, 1, false);
					player.getInventory().add(4151, 1, true);
					
					player.send(new SendMessage("You remove the whip vine from the whip."));
				}
			});
		}
		
		if ((id == 11694) || (id == 11696) || (id == 11698) || (id == 11700)) {
			p.start(new ConfirmDialogue(p) {
				@Override
				public void onConfirm() {
					if (!player.getInventory().hasItemId(id)) {
						return;
					}

					if (id == 11694) {
						if (!p.getInventory().hasSpaceFor(new Item[] { new Item(11702), new Item(11690) })) {
							p.getClient()
									.queueOutgoingPacket(
											new SendMessage(
													"You must clear some inventory space to disassemble your Godsword."));
						} else {
							p.getInventory().remove(11694, 1, false);
							p.getInventory().add(11702, 1, false);
							p.getInventory().add(11690, 1, true);
							p.getClient().queueOutgoingPacket(new SendMessage("You take apart your Godsword."));
						}

						return;
					}
					if (id == 11696) {
						if (!p.getInventory().hasSpaceFor(new Item[] { new Item(11704), new Item(11690) })) {
							p.getClient()
									.queueOutgoingPacket(
											new SendMessage(
													"You must clear some inventory space to disassemble your Godsword."));
						} else {
							p.getInventory().remove(11696, 1, false);
							p.getInventory().add(11704, 1, false);
							p.getInventory().add(11690, 1, true);
							p.getClient().queueOutgoingPacket(new SendMessage("You take apart your Godsword."));
						}

						return;
					}
					if (id == 11698) {
						if (!p.getInventory().hasSpaceFor(new Item[] { new Item(11706), new Item(11690) })) {
							p.getClient()
									.queueOutgoingPacket(
											new SendMessage(
													"You must clear some inventory space to disassemble your Godsword."));
						} else {
							p.getInventory().remove(11698, 1, false);
							p.getInventory().add(11706, 1, false);
							p.getInventory().add(11690, 1, true);
							p.getClient().queueOutgoingPacket(new SendMessage("You take apart your Godsword."));
						}

						return;
					}
					if (id == 11700) {
						if (!p.getInventory().hasSpaceFor(new Item[] { new Item(11708), new Item(11690) })) {
							p.getClient()
									.queueOutgoingPacket(
											new SendMessage(
													"You must clear some inventory space to disassemble your Godsword."));
						} else {
							p.getInventory().remove(11700, 1, false);
							p.getInventory().add(11708, 1, false);
							p.getInventory().add(11690, 1, true);
							p.getClient().queueOutgoingPacket(new SendMessage("You take apart your Godsword."));
						}

						return;
					}
				}
			});
			return true;
		}

		return false;
	}

	public static final boolean createGodsword(Player p, int use1, int use2) {
		if (use1 == 11690) {
			if (use2 == 11702) {
				p.getInventory().remove(use1, 1, false);
				p.getInventory().remove(use2, 1, false);
				p.getInventory().add(11694, 1, true);
				p.getClient().queueOutgoingPacket(new SendMessage("You create an Armadyl godsword."));
				return true;
			}
			if (use2 == 11704) {
				p.getInventory().remove(use1, 1, false);
				p.getInventory().remove(use2, 1, false);
				p.getInventory().add(11696, 1, true);
				p.getClient().queueOutgoingPacket(new SendMessage("You create a Bandos godsword."));
				return true;
			}
			if (use2 == 11706) {
				p.getInventory().remove(use1, 1, false);
				p.getInventory().remove(use2, 1, false);
				p.getInventory().add(11698, 1, true);
				p.getClient().queueOutgoingPacket(new SendMessage("You create a Saradomin godsword."));
				return true;
			}
			if (use2 == 11708) {
				p.getInventory().remove(use1, 1, false);
				p.getInventory().remove(use2, 1, false);
				p.getInventory().add(11700, 1, true);
				p.getClient().queueOutgoingPacket(new SendMessage("You create a Zamorak godsword."));
				return true;
			}
		} else if (use2 == 11690) {
			if (use1 == 11702) {
				p.getInventory().remove(use1, 1, false);
				p.getInventory().remove(use2, 1, false);
				p.getInventory().add(11694, 1, true);
				p.getClient().queueOutgoingPacket(new SendMessage("You create an Armadyl godsword."));
				return true;
			}
			if (use1 == 11704) {
				p.getInventory().remove(use1, 1, false);
				p.getInventory().remove(use2, 1, false);
				p.getInventory().add(11696, 1, true);
				p.getClient().queueOutgoingPacket(new SendMessage("You create a Bandos godsword."));
				return true;
			}
			if (use1 == 11706) {
				p.getInventory().remove(use1, 1, false);
				p.getInventory().remove(use2, 1, false);
				p.getInventory().add(11698, 1, true);
				p.getClient().queueOutgoingPacket(new SendMessage("You create a Saradomin godsword."));
				return true;
			}
			if (use1 == 11708) {
				p.getInventory().remove(use1, 1, false);
				p.getInventory().remove(use2, 1, false);
				p.getInventory().add(11700, 1, true);
				p.getClient().queueOutgoingPacket(new SendMessage("You create a Zamorak godsword."));
				return true;
			}
		}

		return false;
	}

	public static final boolean createBlade(Player p, int use1, int use2) {
		if (use1 == 11710) {
			if ((use2 == 11712) || (use2 == 11714)) {
				if (hasShards(p)) {
					p.getInventory().remove(11710, 1, false);
					p.getInventory().remove(11712, 1, false);
					p.getInventory().remove(11714, 1, false);
					p.getInventory().add(11690, 1, true);
					p.getClient().queueOutgoingPacket(new SendMessage("You create a Godsword blade."));
				} else {
					p.getClient().queueOutgoingPacket(
							new SendMessage("You need all 3 shards to create a Godsword blade."));
				}

				return true;
			}
		} else if (use1 == 11712) {
			if ((use2 == 11710) || (use2 == 11714)) {
				if (hasShards(p)) {
					p.getInventory().remove(11710, 1, false);
					p.getInventory().remove(11712, 1, false);
					p.getInventory().remove(11714, 1, false);
					p.getInventory().add(11690, 1, true);
					p.getClient().queueOutgoingPacket(new SendMessage("You create a Godsword blade."));
				} else {
					p.getClient().queueOutgoingPacket(
							new SendMessage("You need all 3 shards to create a Godsword blade."));
				}

				return true;
			}
		} else if ((use1 == 11714) && ((use2 == 11712) || (use2 == 11710))) {
			if (hasShards(p)) {
				p.getInventory().remove(11710, 1, false);
				p.getInventory().remove(11712, 1, false);
				p.getInventory().remove(11714, 1, false);
				p.getInventory().add(11690, 1, true);
				p.getClient().queueOutgoingPacket(new SendMessage("You create a Godsword blade."));
			} else {
				p.getClient().queueOutgoingPacket(new SendMessage("You need all 3 shards to create a Godsword blade."));
			}

			return true;
		}

		return false;
	}

	public static final boolean hasShards(Player p) {
		return (p.getInventory().hasItemId(11710)) && (p.getInventory().hasItemId(11712))
				&& (p.getInventory().hasItemId(11714));
	}
}
