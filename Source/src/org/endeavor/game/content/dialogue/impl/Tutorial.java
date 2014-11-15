package org.endeavor.game.content.dialogue.impl;

import org.endeavor.game.content.dialogue.Dialogue;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.content.dialogue.Emotion;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.controllers.DefaultController;
import org.endeavor.game.entity.player.net.out.impl.SendFlashSidebarIcon;

public class Tutorial extends Dialogue {
	public static final TutorialController TUTORIAL_CONTROLLER = new TutorialController();
	public static final int RUNESCAPE_GUIDE = 945;

	public Tutorial(Player player) {
		this.player = player;
		player.setController(TUTORIAL_CONTROLLER);
	}

	@Override
	public void execute() {
		if ((player.isStarter()) && (next < 3)) {
			next = 3;
		}

		switch (next) {
		case 0:
			DialogueManager.sendStatement(player, new String[] { "Would you like to watch the tutorial?" });
			next = 1;
			break;
		case 1:
			DialogueManager.sendOption(player, new String[] { "Yes.", "No." });
			option = 1;
			break;
		case 2:
			DialogueManager.sendStatement(player,
					new String[] { "Talk to the RevolutionX Guide if you change your mind." });
			player.setController(ControllerManager.DEFAULT_CONTROLLER);
			player.setStarter(false);
			end();
			break;
		case 3:
			nChat(new String[] { "Welcome to RevolutionX, " + player.getUsername() + "!" });
			break;
		case 4:
			nChat(new String[] { "Before we get started we will review", "some of the basics of playing RevolutionX!" });
			break;
		case 5:
			player.getClient().queueOutgoingPacket(new SendFlashSidebarIcon(7));
			nChat(new String[] { "This is your Magic book." });
			break;
		case 6:
			nChat(new String[] { "The Magic book is your way around RevolutionX." });
			break;
		case 7:
			player.getClient().queueOutgoingPacket(new SendFlashSidebarIcon(2));
			nChat(new String[] { "This is the Quest or Information tab." });
			break;
		case 8:
			nChat(new String[] { "You will find your information on it!", "You can also scroll down to see the",
					"available quests." });
			break;
		case 9:
			tele(3093, 3494);
			nChat(new String[] { "Here at Edgeville you will find many ways", "to communicate and trade with other players." });
			break;
		case 10:
			tele(3093, 3489);
			nChat(new String[] { "This is the Exchange system.", "Here you have your own shop to sell/buy",
					"items from other players" });
			break;
		case 11:
			nChat(new String[] { "You can search for items or players.",
					"You can also view a list of players with shops!", "Remember to always check the prices of",
					"what you are buying!" });
			break;
		case 12:
			tele(3096, 3498);
			nChat(new String[] { "This is Party pete!", "He can sell you rewards for voting!", "You can vote at anything by using ::vote." });
			break;
		case 13:
			tele(3097, 3498);
			nChat(new String[] { "This is the donation shop.", "You can buy things like Chaotics, Partyhats, and more.", "You can donate via PayPal now using ::donate!" });
			break;
		case 14:
			tele(3092, 3492);
			nChat(new String[] { "You can use Crystal keys here to get Dragonstones", "and other rewards!", "Crystal keys are dropped by all monsters you fight." });
			break;
		case 15:
			tele(3091, 3494);
			nChat(new String[] { "This is Mandrith, he manages PKP and Statues.", "You get PKP and Statues from kills in the Wild.", "When you receive 100% EP you always get a statue.", "It can be worth up to 10m."});
			break;
		case 16:
			tele(3100, 3504);
			nChat(new String[] { "This is where you can change your Magic or Prayer book." });
			break;
		case 17:
			tele(3087, 3502);
			nChat(new String[] { "This is where you can change your appearance", "or get a Slayer task. You can view the Slayer shop", "by right-clicking Vanakka. Your Slayer points are ",  "in your Quest tab." });
			break;
		case 18:
			tele(3093, 3505);
			nChat(new String[] { "This is the Combat Instructor.", "He can assist you with locking experience", "or resetting stats." });
			break;
		case 19:
			tele(3085, 3493);
			nChat(new String[] { "These portals can take you to many Pking", "locations, Safe FFA, or Mini-games." });
			break;
		case 20:
			tele(3080, 3508);
			nChat(new String[] { "These are the shops.", "Everything you need is either here or at a Skilling teleport." });
			break;
		case 21:
			tele(2925, 3444);
			nChat(new String[] { "This is one of many skill locations in your",
					"Magic book. Here you can create scrolls or",
					"pouches and learn the Summoning skill.",
					"You can also purchase pets here!" });
			break;
		case 22:
			tele(1840, 5220);
			nChat(new String[] { "This is Dungeoneering, a team-based mini-game.", "You can buy Chaotics and other items with points!",
					"This area is also accessed in the Magic book." });
			break;
		case 23:
			tele(2882, 5310, 2);
			nChat(new String[] { "This is God Wars, once of many boss locations.", "You can access this location with the bosses teleport." });
			break;
		case 24:
			tele(2965, 3383);
			nChat(new String[] { "This is Falador, which can be", "accessed in your spellbook via the",
					"Town teleport." });
			break;
		case 25:
			tele(3087, 3490);
			nChat(new String[] { "There is much more to explore!",
					"Have a great time playing, " + player.getUsername() + "!" });
			player.getAchievements().incr(player, "Complete the Tutorial");
			end();
			player.setController(ControllerManager.DEFAULT_CONTROLLER);
		}
	}

	public void nChat(String[] chat) {
		DialogueManager.sendNpcChat(player, 945, Emotion.HAPPY_TALK, chat);
		next += 1;
	}

	public void pChat(String[] chat) {
		DialogueManager.sendPlayerChat(player, Emotion.HAPPY_TALK, chat);
		next += 1;
	}
	
	public void tele(int x, int y) {
		player.teleport(new Location(x, y, 0));
	}

	public void tele(int x, int y, int z) {
		player.teleport(new Location(x, y, z));
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9157:
			if (option == 1) {
				next = 3;
				execute();
			}
			return true;
		case 9158:
			if (option == 1) {
				next = 2;
				execute();
			}
			return true;
		}
		return false;
	}

	public static class TutorialController extends DefaultController {
		@Override
		public boolean canMove(Player p) {
			return false;
		}

		@Override
		public boolean canClick() {
			return false;
		}

		@Override
		public boolean canTeleport() {
			return false;
		}

		@Override
		public boolean canTrade() {
			return false;
		}

		@Override
		public boolean transitionOnWalk(Player p) {
			return false;
		}

		@Override
		public boolean canAttackNPC() {
			return false;
		}

		@Override
		public void onDisconnect(Player p) {
		}
	}
}
