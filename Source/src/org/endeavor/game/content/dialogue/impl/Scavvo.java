package org.endeavor.game.content.dialogue.impl;

import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.content.dialogue.Emotion;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class Scavvo extends Dialogue {
	private final int repair;
	public static final int[][] REPAIR = { { 18349, 7000000, 18350 }, { 18351, 7000000, 18352 },
			{ 18353, 7000000, 18354 }, { 4708, 250000, 4860 }, { 4710, 250000, 4866 }, { 4712, 250000, 4872 },
			{ 4714, 250000, 4878 }, { 4716, 250000, 4884 }, { 4718, 250000, 4890 }, { 4720, 250000, 4896 },
			{ 4722, 250000, 4902 }, { 4724, 250000, 4908 }, { 4726, 250000, 4914 }, { 4728, 250000, 4920 },
			{ 4730, 250000, 4926 }, { 4732, 250000, 4932 }, { 4734, 250000, 4938 }, { 4736, 250000, 4944 },
			{ 4738, 250000, 4950 }, { 4745, 250000, 4956 }, { 4747, 250000, 4962 }, { 4749, 250000, 4968 },
			{ 4751, 250000, 4974 }, { 4753, 250000, 4980 }, { 4755, 250000, 4986 }, { 4757, 250000, 4992 },
			{ 4759, 250000, 4998 } };

	public Scavvo(Player player) {
		repair = 0;
		this.player = player;
	}

	public Scavvo(Player player, int repair) {
		this.repair = repair;
		this.player = player;
	}

	@Override
	public void execute() {
		if (repair == 0) {
			DialogueManager.sendNpcChat(player, 519, Emotion.CALM, new String[] {
					"I can repair your broken equipment.", "Just use the item on me and I can repair it." });
			end();
		} else {
			int[] data = getData(repair);

			if (data == null) {
				DialogueManager.sendNpcChat(player, 519, Emotion.CALM,
						new String[] { "I'm sorry, I can't repair that." });
				end();
				return;
			}
			final int price = (int) (data[0] * (player.isMember() ? 0.75D : 1.0D));
			final int repaired = data[1];

			player.start(new ConfirmDialogue(player, new String[] {
					"This will cost " + Misc.formatCoins(price) + " to repair.", "Do you accept this price?" }) {
				@Override
				public void onConfirm() {
					if (!player.getInventory().hasItemId(repair)) {
						return;
					}

					if (!player.getInventory().hasItemAmount(new Item(995, price))) {
						player.getClient().queueOutgoingPacket(
								new SendMessage("You do not have " + Misc.formatCoins(price) + "."));
						return;
					}

					player.getAchievements().incr(player, "Repair 25 equipment");
					player.getAchievements().incr(player, "Repair 75 equipment");

					player.getInventory().remove(repair, 1, false);
					player.getInventory().remove(new Item(995, price), false);
					player.getInventory().add(repaired, 1, true);
					player.getClient().queueOutgoingPacket(
							new SendMessage("You receive a repaired " + Item.getDefinition(repaired).getName() + "."));
				}
			});
		}
	}

	@Override
	public boolean clickButton(int id) {
		return false;
	}

	public int[] getData(int id) {
		for (int[] i : REPAIR) {
			if (i[2] == id) {
				return new int[] { i[1], i[0] };
			}
		}

		return null;
	}

	public static void doItemBreaking(Item item) {
		for (int i = 3; i < REPAIR.length; i++)
			if (item.getId() == REPAIR[i][0]) {
				item.setId(REPAIR[i][2]);
				return;
			}
	}
}
