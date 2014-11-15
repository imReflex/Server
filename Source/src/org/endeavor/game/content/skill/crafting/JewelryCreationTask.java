package org.endeavor.game.content.skill.crafting;

import org.endeavor.engine.task.Task;
import org.endeavor.engine.task.TaskQueue;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.skill.smithing.Smelting;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendInterface;
import org.endeavor.game.entity.player.net.out.impl.SendInterfaceConfig;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;
import org.endeavor.game.entity.player.net.out.impl.SendSound;
import org.endeavor.game.entity.player.net.out.impl.SendString;
import org.endeavor.game.entity.player.net.out.impl.SendUpdateItemsAlt;

public class JewelryCreationTask extends Task {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5418918105861483502L;
	private final Player player;
	private final Jewelry data;
	private byte amount;
	public static final int[][] JEWELRY_INTERFACE_ITEMS = { { 2550, 1641, 2570, 1645, 11115, 6575, 11130 },
			{ 1654, 1656, 1658, 1660, 1662, 1664, 11128 }, { 1673, 1675, 1677, 1679, 1681, 1683, 6579 } };

	public static void start(Player p, int item, int amount) {
		if (Jewelry.forReward(item) != null) {
			TaskQueue.queue(new JewelryCreationTask(p, Jewelry.forReward(item), amount));
			p.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
		}
	}

	public JewelryCreationTask(Player player, Jewelry data, int amount) {
		super(player, 2, false, Task.StackType.NEVER_STACK, Task.BreakType.ON_MOVE, 0);
		this.data = data;
		this.player = player;
		this.amount = ((byte) amount);

		if (player.getMaxLevels()[12] < data.getRequiredLevel()) {
			String make = data.getReward().getDefinition().getName();
			player.getClient().queueOutgoingPacket(
					new SendMessage("You need a Crafting level of " + data.getRequiredLevel() + " to make "
							+ Misc.getAOrAn(make) + " " + make + "."));
			stop();
			return;
		}

		for (int i : data.getMaterialsRequired())
			if (!player.getInventory().hasItemId(i)) {
				String req = Item.getDefinition(i).getName();
				String make = data.getReward().getDefinition().getName();
				player.getClient().queueOutgoingPacket(
						new SendMessage("You need " + Misc.getAOrAn(req) + " " + req + " to make "
								+ Misc.getAOrAn(make) + " " + make + "."));
				stop();
				return;
			}
	}

	@Override
	public void execute() {
		for (int i : data.getMaterialsRequired()) {
			if (!player.getInventory().hasItemId(i)) {
				player.getClient().queueOutgoingPacket(
						new SendMessage("You ran out of " + Item.getDefinition(i).getName() + "s."));
				stop();
				return;
			}
		}

		for (int i : data.getMaterialsRequired()) {
			player.getInventory().remove(i, 1, false);
		}

		if ((this.amount = (byte) (amount - 1)) == 0) {
			stop();
		}

		player.getUpdateFlags().sendAnimation(Smelting.SMELTING_ANIMATION);

		player.getClient().queueOutgoingPacket(new SendSound(469, 0, 0));

		player.getInventory().add(data.getReward(), true);

		player.getSkill().addExperience(12, data.getExperience());
	}

	@Override
	public void onStop() {
	}

	public static void sendInterface(Player p) {
		for (int k = 0; k < 3; k++) {
			int interfaceId = 4233;

			if (k == 1)
				interfaceId = 4239;
			else if (k == 2) {
				interfaceId = 4245;
			}
			for (int i = 0; i < JEWELRY_INTERFACE_ITEMS[k].length; i++) {
				p.getClient().queueOutgoingPacket(
						new SendUpdateItemsAlt(interfaceId, JEWELRY_INTERFACE_ITEMS[k][i], 1, i));
			}
		}

		p.getClient().queueOutgoingPacket(new SendInterfaceConfig(4229, 0, -1));
		p.getClient().queueOutgoingPacket(new SendInterfaceConfig(4235, 0, -1));
		p.getClient().queueOutgoingPacket(new SendInterfaceConfig(4241, 0, -1));

		p.getClient().queueOutgoingPacket(new SendString("", 4230));
		p.getClient().queueOutgoingPacket(new SendString("", 4236));
		p.getClient().queueOutgoingPacket(new SendString("", 4242));

		p.getClient().queueOutgoingPacket(new SendInterface(4161));
	}

	public static boolean clickButton(Player p, int id) {
		return false;
	}
}
