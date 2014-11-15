package org.endeavor.game.entity.player;

import java.io.Serializable;

import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class ItemDegrading implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4303854667538840090L;

	private DegradedItem[] degrading = new DegradedItem[50];

	public static final int[][] WEAPON_FINAL_STAGE = { { 18349, 18350 }, { 18351, 18352 }, { 18353, 18354 } };

	public static final int[][] WEAPON_DEGRADING = { { 13899, 13901 }, { 13902, 13904 }, { 13901, 13901 },
			{ 13904, 13904 }, { 18349, 18349 }, { 18351, 18351 }, { 18353, 18353 } };

	public static final int[][] EQUIPMENT_DEGRADING = { { 13858, 13860 }, { 13861, 13863 }, { 13864, 13866 },
			{ 13870, 13872 }, { 13873, 13875 }, { 13876, 13878 }, { 13884, 13886 }, { 13887, 13889 }, { 13890, 13892 },
			{ 13893, 13895 }, { 13896, 13898 }, { 13860, 13860 }, { 13863, 13863 }, { 13866, 13866 }, { 13872, 13872 },
			{ 13875, 13875 }, { 13878, 13878 }, { 13886, 13886 }, { 13889, 13889 }, { 13892, 13892 }, { 13895, 13895 },
			{ 13898, 13898 } };

	public static void declare() {
		for (int[] i : EQUIPMENT_DEGRADING) {
			GameDefinitionLoader.getItemDef(i[1]).setUntradable();
		}

		for (int[] i : WEAPON_DEGRADING)
			GameDefinitionLoader.getItemDef(i[1]).setUntradable();
	}

	public void degradeWeapon(Player p) {
		Item weapon = p.getEquipment().getItems()[3];

		if (weapon != null) {
			DegradedItem ref = getDegradingReference(weapon.getId());

			if (ref == null) {
				int index = getUnDegradedIndex(weapon.getId(), WEAPON_DEGRADING);

				if (index != -1) {
					if (weapon.getId() != WEAPON_DEGRADING[index][1]) {
						p.getClient().queueOutgoingPacket(
								new SendMessage("Your " + weapon.getDefinition().getName() + " has degraded."));
					}

					p.getEquipment().getItems()[3].setId(WEAPON_DEGRADING[index][1]);
					p.getEquipment().onLogin();

					DegradedItem add = new DegradedItem(WEAPON_DEGRADING[index][1], 0, 0);

					insert(add);

					ref = add;
				}
			}

			if (ref != null) {
				ref.incr();

				if (isFullDegraded(ref)) {
					int lastStage = getFinalWeaponStage(ref.id);

					if (lastStage == -1) {
						p.getClient().queueOutgoingPacket(
								new SendMessage("Your " + weapon.getDefinition().getName() + " has degraded to dust."));
						p.getEquipment().getItems()[3] = null;
					} else {
						p.getClient().queueOutgoingPacket(
								new SendMessage("Your " + weapon.getDefinition().getName()
										+ " is now fully degraded and needs repaired."));
						p.getEquipment().getItems()[3].setId(lastStage);
					}

					p.setAppearanceUpdateRequired(true);
					p.getEquipment().onLogin();

					removeDegradingReference(ref);
				}
			}
		}
	}

	public void degradeEquipment(Player p) {
		for (int i = 0; i < 14; i++)
			if (i != 3) {
				Item item = p.getEquipment().getItems()[i];

				if (item != null) {
					DegradedItem ref = getDegradingReference(item.getId());

					if (ref == null) {
						int index = getUnDegradedIndex(item.getId(), EQUIPMENT_DEGRADING);

						if (index != -1) {
							if (item.getId() != EQUIPMENT_DEGRADING[index][1]) {
								p.getClient().queueOutgoingPacket(
										new SendMessage("Your " + item.getDefinition().getName() + " has degraded."));
							}

							p.getEquipment().getItems()[i].setId(EQUIPMENT_DEGRADING[index][1]);
							p.getEquipment().onLogin();

							DegradedItem add = new DegradedItem(EQUIPMENT_DEGRADING[index][1], 0, 0);

							insert(add);

							ref = add;
						}
					}

					if (ref != null) {
						ref.incr();

						if (isFullDegraded(ref)) {
							p.getClient()
									.queueOutgoingPacket(
											new SendMessage("Your " + item.getDefinition().getName()
													+ " has degraded to dust."));

							p.setAppearanceUpdateRequired(true);
							p.getEquipment().getItems()[i] = null;
							p.getEquipment().onLogin();

							removeDegradingReference(ref);
						}
					}
				}
			}
	}

	public boolean isFullDegraded(DegradedItem i) {
		return i.hits >= 25000;
	}

	public static int getFinalWeaponStage(int id) {
		for (int[] i : WEAPON_FINAL_STAGE) {
			if (i[0] == id) {
				return i[1];
			}
		}

		return -1;
	}

	public void insert(DegradedItem add) {
		for (int i = 0; i < degrading.length; i++) {
			if (degrading[i] == null) {
				degrading[i] = add;
				return;
			}
			if (degrading[i].getId() == add.getId()) {
				return;
			}

		}

		throw new IllegalArgumentException("Could not add new degrading item..");
	}

	public DegradedItem getDegradingReference(int id) {
		for (DegradedItem i : degrading) {
			if ((i != null) && (i.getId() == id)) {
				return i;
			}
		}

		return null;
	}

	public void removeDegradingReference(DegradedItem item) {
		for (int i = 0; i < degrading.length; i++)
			if (degrading[i].getId() == item.getId()) {
				degrading[i] = null;
				return;
			}
	}

	public static int getUnDegradedIndex(int id, int[][] array) {
		for (int k = 0; k < array.length; k++) {
			int[] i = array[k];

			if (i[0] == id) {
				return k;
			}
		}

		return -1;
	}

	public void setDegrading(DegradedItem[] degrading) {
		if (degrading.length < 50) {
			for (int i = 0; i < degrading.length; i++) {
				if (degrading[i] != null)
					this.degrading[i] = degrading[i];
			}
		} else
			this.degrading = degrading;
	}

	public DegradedItem[] getDegrading() {
		return degrading;
	}

	public static class DegradedItem {
		private int id;
		private int hits;
		private final int maxHits;

		public DegradedItem(int id, int hits, int maxHits) {
			this.id = id;
			this.hits = hits;
			this.maxHits = maxHits;
		}

		public int getHits() {
			return hits;
		}

		public int getId() {
			return id;
		}

		public void incr() {
			hits += 1;
		}

		public boolean degrade() {
			return hits >= maxHits;
		}
	}
}
