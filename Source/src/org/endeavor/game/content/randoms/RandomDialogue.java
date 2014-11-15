package org.endeavor.game.content.randoms;

import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.content.dialogue.Emotion;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;

public class RandomDialogue extends Dialogue {
	private final Mob mob;

	public RandomDialogue(Player player, Mob mob) {
		this.player = player;
		this.mob = mob;
	}

	@Override
	public void execute() {
		switch (next) {
		case 0:
			if (mob.getOwner().equals(player)) {
				player.getAttributes().set("pauserandom", Byte.valueOf((byte) 0));

				if (player.getAttributes().get("randomevent") == null) {
					System.out.println("Null random attribute for player: " + player.toString());
					player.start(null);
					return;
				}
				DialogueManager.sendNpcChat(player, mob.getId(), Emotion.HAPPY_TALK,
						new String[] { "Hello, " + player.getUsername() + ".", "Enjoy this free gift." });
				next = 1;
			} else if (mob.getOwner() != null) {
				DialogueManager.sendNpcChat(player, mob.getId(), Emotion.HAPPY_TALK,
						new String[] { "Hello, " + player.getUsername() + ".",
								"I'm trying to talk to " + mob.getOwner().getUsername() + "." });
				end();
			}

			break;
		case 1:
			player.getAttributes().remove("pauserandom");
			RandomEvent c = (RandomEvent) player.getAttributes().get("randomevent");
			c.doSuccess(player, mob);
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			end();
		}
	}

	@Override
	public boolean clickButton(int id) {
		return false;
	}
}
