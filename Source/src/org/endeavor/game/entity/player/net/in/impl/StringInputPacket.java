package org.endeavor.game.entity.player.net.in.impl;

import org.endeavor.GameSettings;
import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.engine.utility.Misc;
import org.endeavor.engine.utility.NameUtil;
import org.endeavor.game.content.QuestTab;
import org.endeavor.game.content.clans.Clans;
import org.endeavor.game.content.combat.spawning.Loadout;
import org.endeavor.game.content.combat.spawning.UseLoadoutDialogue;
import org.endeavor.game.content.dialogue.DialogueManager;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.in.IncomingPacket;
import org.endeavor.game.entity.player.net.out.impl.SendString;

import com.golden.tools.Settings;


public class StringInputPacket extends IncomingPacket {
	@Override
	public void handle(Player player, StreamBuffer.InBuffer in, int opcode, int length) {
		String input = Misc.longToPlayerName2(in.readLong());
		input = input.replaceAll("_", " ");

		if(GameSettings.DEV_MODE)
			System.out.println("InputString Packet: " + input);
		
		if (player.getEnterXInterfaceId() == 100)
			player.getSlayer().setSocialSlayerPartner(input);
		/*else if (player.getEnterXInterfaceId() == 55777)
			player.getShopping().open(World.getPlayerByName(input));
		else if (player.getEnterXInterfaceId() == 55778)
			player.getPlayerShop().setSearch(input);*/
		else if (player.getEnterXInterfaceId() == 36471) {
			
			int index = player.getAttributes().getInt(UseLoadoutDialogue.NEW_LOADOUT_INDEX);
			Item[] items = (Item[]) player.getAttributes().get(UseLoadoutDialogue.NEW_LOADOUT_ITEMS_KEY);
			
			player.getCustomSpawnLoadouts()[index] = new Loadout(items, NameUtil.uppercaseFirstLetter(input));
			
			DialogueManager.sendStatement(player, "Your Loadout has been added successfully!");
			
			player.send(new SendString(QuestTab.getLoadoutName(player, 0), QuestTab.QUEST_TAB_LINE_IDS[2]));
			player.send(new SendString(QuestTab.getLoadoutName(player, 1), QuestTab.QUEST_TAB_LINE_IDS[3]));
			player.send(new SendString(QuestTab.getLoadoutName(player, 2), QuestTab.QUEST_TAB_LINE_IDS[4]));
			player.send(new SendString(QuestTab.getLoadoutName(player, 3), QuestTab.QUEST_TAB_LINE_IDS[5]));
			player.send(new SendString(QuestTab.getLoadoutName(player, 4), QuestTab.QUEST_TAB_LINE_IDS[6]));
			player.send(new SendString(QuestTab.getLoadoutName(player, 5), QuestTab.QUEST_TAB_LINE_IDS[7]));
			
		} else {
			Clans.joinChannelForName(player, input);
		}
	}

	@Override
	public int getMaxDuplicates() {
		return 1;
	}
}
