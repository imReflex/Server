package org.endeavor.game.entity.player.net.in.impl;

import org.endeavor.GameSettings;
import org.endeavor.engine.cache.inter.RSInterface;
import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.content.EasterRing;
import org.endeavor.game.content.Emotes;
import org.endeavor.game.content.ItemInteraction;
import org.endeavor.game.content.QuestTab;
import org.endeavor.game.content.clans.Clan;
import org.endeavor.game.content.clans.ClanTab;
import org.endeavor.game.content.clans.Clans;
import org.endeavor.game.content.clans.clanwars.ClanWarSetup;
import org.endeavor.game.content.dialogue.impl.SelectTitleDialogue;
import org.endeavor.game.content.dialogue.impl.StarterDialogue;
import org.endeavor.game.content.dialogue.impl.Tutorial;
import org.endeavor.game.content.minigames.duelarena.DuelingConstants;
import org.endeavor.game.content.skill.cooking.CookingTask;
import org.endeavor.game.content.skill.crafting.Crafting;
import org.endeavor.game.content.skill.crafting.HideTanning;
import org.endeavor.game.content.skill.fletching.Fletching;
import org.endeavor.game.content.skill.herblore.HerbloreFinishedPotionTask;
import org.endeavor.game.content.skill.herblore.HerbloreUnfinishedPotionTask;
import org.endeavor.game.content.skill.magic.Autocast;
import org.endeavor.game.content.skill.smithing.SmithingConstants;
import org.endeavor.game.content.skill.summoning.SummoningCreation;
import org.endeavor.game.content.sounds.MusicPlayer;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.EquipmentConstants;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerConstants;
import org.endeavor.game.entity.player.controllers.ControllerManager;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.in.IncomingPacket;
import org.endeavor.game.entity.player.net.out.impl.SendConfig;
import org.endeavor.game.entity.player.net.out.impl.SendEnterString;
import org.endeavor.game.entity.player.net.out.impl.SendInterface;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;

public class ClickButtonPacket extends IncomingPacket {
	@Override
	public int getMaxDuplicates() {
		return 5;
	}

	@Override
	public void handle(Player player, StreamBuffer.InBuffer in, int opcode, int length) {
		int buttonId = in.readShort();
		
		/*if (RSInterface.getSongId(buttonId) != -1) {
			MusicPlayer.select(player, buttonId);
			return;
		}*/
		
		in.reset();
		buttonId = Misc.hexToInt(in.readBytes(2));
		
		if (GameSettings.DEV_MODE || PlayerConstants.isOwner(player)) {
			player.getClient().queueOutgoingPacket(new SendMessage("button: " + buttonId));
			System.out.println("button: " + buttonId);
		}
		
		if ((player.getController().equals(Tutorial.TUTORIAL_CONTROLLER)) && (player.getDialogue() != null)) {
			player.getDialogue().clickButton(buttonId);
			return;
		}

		if ((player.getController().equals(EasterRing.EASTER_RING_CONTROLLER)) && (buttonId == 23132)) {
			EasterRing.cancel(player);
			return;
		}
		
		Clan myClan = Clans.getClanForOwner(player);
		if(myClan != null) {
			if(myClan.handleEditClick(player, buttonId)) {
				myClan.openClanEdit(player);
				myClan.getChannel().updateClanTabs();
				player.send(new SendMessage("Changes will take effect on your clan within the next 60 seconds..."));
				return;
			}
			if(myClan.getWarSetup() != null && myClan.getWarSetup().handleWarSetupButtons(player, buttonId)) {
				return;
			}
		}

		if (PlayerConstants.isSettingAppearance(player)) {
			if (buttonId == 14067) {
				player.getAttributes().remove("setapp");
				player.start(new StarterDialogue(player));
			}
			return;
		}

		if ((player.isDead()) || (!player.getController().canClick())) {
			return;
		}

		switch (buttonId) {
		case 155026:
			player.getClient().queueOutgoingPacket(new SendInterface(38700));
			break;
		case 59097:
			player.send(new SendInterface(15106));
			break;
		case 59100:
			player.send(new SendInterface(17100));
			break;
		case 3182:
			player.send(new SendInterface(40030));
			break;
		case 151045:
			player.getClient().queueOutgoingPacket(new SendInterface(39700));
			break;
		case 156066:
		case 156070:
			player.send(new SendRemoveInterfaces());
			break;
		case 70212:
			player.send(new SendMessage("You aren't in a clan, or do not own the clan you are in."));
			break;
		case 184163:
			player.send(new SendRemoveInterfaces());
			break;
		case 193219://leave
			if(player.getClanChannel() != null) {
				player.getClanChannel().leaveChannel(player);
			} else {
				ClanTab.clearClanTab(player);
				player.send(new SendMessage("You are not in a clan chat."));
			}
			break;
		case 79240:
			player.send(new SendMessage("You are already in a clan chat."));
			break;
		case 9118:
		case 83051:
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			break;
		//case 28166:
			//player.setEnterXInterfaceId(100);
			//player.getClient().queueOutgoingPacket(new SendEnterString());
			//break;
		case 83093:
			player.getClient().queueOutgoingPacket(new SendInterface(15106));
			break;
		case 3162:
			player.setMusicVolume((byte) 4);
			player.getClient().queueOutgoingPacket(new SendConfig(168, 4));
			break;
		case 3163:
		case 3164:
		case 3165:
		case 3166:
			player.setMusicVolume((byte) (3166 - buttonId));
			player.getClient().queueOutgoingPacket(new SendConfig(168, player.getMusicVolume()));
			break;
		case 3173:
			player.setSoundVolume((byte) 4);
			player.getClient().queueOutgoingPacket(new SendConfig(169, 4));
			break;
		case 3174:
		case 3175:
		case 3176:
		case 3177:
			player.setSoundVolume((byte) (3177 - buttonId));
			player.getClient().queueOutgoingPacket(new SendConfig(169, player.getSoundVolume()));
			break;
		case 24125:
			player.getAttributes().remove("manual");
			break;
		case 24126:
			player.getAttributes().set("manual", Byte.valueOf((byte) 1));
			break;
		case 108005:
			player.getClient().queueOutgoingPacket(new SendInterface(19148));
			break;
		case 14067:
			player.setAppearanceUpdateRequired(true);
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			break;
		case 9154:
			if (player.getCombat().inCombat()) {
				if (PlayerConstants.isOwner(player)) {
					player.logout(true);
					return;
				}
				player.getClient().queueOutgoingPacket(new SendMessage("You can't logout while in combat!"));
			} else {
				if (PlayerConstants.isOwner(player)) {
					player.logout(true);
					return;
				}

				if (player.getClient().getStage() == Client.Stages.LOGGED_IN) {
					player.logout(false);
				}
			}
			break;
		case 74:
		case 152:
		case 33230:
		case 74214:
			player.getRunEnergy().setRunning(!player.getRunEnergy().isRunning());
			player.getClient().queueOutgoingPacket(new SendConfig(429, player.getRunEnergy().isRunning() ? 1 : 0));
			break;
		case 211172:
			player.getRunEnergy().toggleResting();
			break;
		case 3138:
			player.setScreenBrightness((byte) 1);
			break;
		case 3140:
			player.setScreenBrightness((byte) 2);
			break;
		case 3142:
			player.setScreenBrightness((byte) 3);
			break;
		case 3144:
			player.setScreenBrightness((byte) 4);
			break;
		case 100228:
			player.setMultipleMouseButtons((byte) (player.getMultipleMouseButtons() == 0 ? 1 : 0));
			break;
		case 100231:
			player.setChatEffectsEnabled((byte) (player.getChatEffectsEnabled() == 0 ? 1 : 0));
			break;
		case 3189:
			player.setSplitPrivateChat((byte) (player.getSplitPrivateChat() == 0 ? 1 : 0));
			player.getClient().queueOutgoingPacket(new SendConfig(287, player.getSplitPrivateChat()));
			break;
		case 100237:
			player.setAcceptAid((byte) (player.getAcceptAid() == 0 ? 1 : 0));
			break;
		case 89061:
		case 93202:
		case 93209:
		case 93217:
		case 93225:
		case 94051:
		case 150:
			player.setRetaliate(!player.isRetaliate());
			break;
		default:
			if (QuestTab.clickButton(player, buttonId))
				break;
			if (player.getSummoning().click(buttonId))
				break;
			if (SummoningCreation.create(player, buttonId))
				break;
			if (player.getQuesting().clickQuestButton(buttonId))
				break;
			if (Fletching.clickButton(player, buttonId))
				break;			
			if (Crafting.handleCraftingByButtons(player, buttonId))
				break;				
			if (HideTanning.clickButton(player, buttonId))
				break;
			if (player.getPrayer().clickButton(buttonId))
				break;					
			if (CookingTask.handleCookingByAmount(player, buttonId))
				break;					
			if ((player.getDialogue() != null) && (player.getDialogue().clickButton(buttonId))) {
				break;
			}
			if (Autocast.clickButton(player, buttonId))
				break;			
			if (Emotes.clickButton(player, buttonId))
				break;					
			if (player.getSpecialAttack().clickSpecialButton(buttonId))
				break;					
			if (DuelingConstants.clickDuelButton(player, buttonId))
				break;								
			if (player.getTrade().clickTradeButton(buttonId))
				break;									
			if (player.getMinigames().getBetManager().clickButton(buttonId))
				break;											
			if (player.getBank().clickButton(buttonId))
				break;														
			if (player.getMagic().clickMagicButtons(buttonId))
				break;															
			if (EquipmentConstants.clickAttackStyleButtons(player, buttonId))
				break;
			if (SmithingConstants.clickSmeltSelection(player, buttonId))
				break;
			if (ItemInteraction.useExperienceLamp(player, buttonId))
				break;

			if ((player.getAttributes().get("herbloreitem1") != null) && ((((Item) player.getAttributes().get("herbloreitem1")).getId() == 227)
					|| (((Item) player.getAttributes().get("herbloreitem2")).getId() == 227) 
					? !HerbloreUnfinishedPotionTask.handleHerbloreButtons(
							player,
							buttonId): !HerbloreFinishedPotionTask.handleHerbloreButtons(player, buttonId)))
				break;
			break;
		}
	}
}
