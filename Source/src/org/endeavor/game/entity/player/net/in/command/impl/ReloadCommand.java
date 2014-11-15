package org.endeavor.game.entity.player.net.in.command.impl;

import org.endeavor.engine.utility.GameDefinitionLoader;
import org.endeavor.game.content.Emotes;
import org.endeavor.game.content.combat.special.SpecialAttackHandler;
import org.endeavor.game.content.dialogue.OneLineDialogue;
import org.endeavor.game.content.shopping.Shop;
import org.endeavor.game.content.skill.magic.MagicEffects;
import org.endeavor.game.content.skill.summoning.SummoningConstants;
import org.endeavor.game.content.sounds.MobSounds;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.mob.MobConstants;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerConstants;
import org.endeavor.game.entity.player.net.in.PacketHandler;
import org.endeavor.game.entity.player.net.in.command.Command;
import org.endeavor.game.entity.player.net.in.command.CommandManager;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class ReloadCommand implements Command {
	@Override
	public void handleCommand(Player player, String command) {
		String[] args = command.split(" ");
		String toReload = args[1];
		try {
			World.setIgnoreTick(true);

			if (toReload.equalsIgnoreCase("combatdef")) {
				GameDefinitionLoader.loadNpcCombatDefinitions();
				
				for (Mob i : World.getNpcs()) {
					if (i != null) {
						i.resetLevels();
					}
				}
				
				player.getClient().queueOutgoingPacket(new SendMessage("Reloaded."));
			} else if (toReload.equalsIgnoreCase("npcdef")) {
				GameDefinitionLoader.loadNpcDefinitions();
				player.getClient().queueOutgoingPacket(new SendMessage("Reloaded."));
			} else if (toReload.equalsIgnoreCase("packets")) {
				PacketHandler.declare();
				player.getClient().queueOutgoingPacket(new SendMessage("Reloaded."));
			} else if (toReload.equalsIgnoreCase("dialogue")) {
				OneLineDialogue.declare();
				player.getClient().queueOutgoingPacket(new SendMessage("Reloaded."));
			} else if (toReload.equalsIgnoreCase("npcspawns")) {
				for (Mob i : World.getNpcs()) {
					if (i != null) {
						i.remove();
						World.getNpcs()[i.getIndex()] = null;
						
						for (Player k : World.getPlayers()) {
							if (k != null) {
								k.getClient().getNpcs().remove(i);
							}
						}
					}
				}
				
				Mob.spawnBosses();
				GameDefinitionLoader.loadNpcSpawns();
			} else if (toReload.equalsIgnoreCase("shops")) {
				GameDefinitionLoader.loadShopDefinitions();
				Shop.declare();
				player.getClient().queueOutgoingPacket(new SendMessage("Reloaded."));
			} else if (toReload.equalsIgnoreCase("commands")) {
				CommandManager.reloadCommands();
				player.getClient().queueOutgoingPacket(new SendMessage("Reloaded."));
			} else if (toReload.equalsIgnoreCase("sounds")) {
				MobSounds.declare();
				player.getClient().queueOutgoingPacket(new SendMessage("Reloaded."));
			} else if (toReload.equalsIgnoreCase("aggress")) {
				MobConstants.declare();
				player.getClient().queueOutgoingPacket(new SendMessage("Successfully reloaded aggressive npcs."));
			} else if (toReload.equalsIgnoreCase("equipmentdef")) {
				GameDefinitionLoader.loadEquipmentDefinitions();
				GameDefinitionLoader.setRequirements();
				player.getClient().queueOutgoingPacket(new SendMessage("Reloaded."));
			} else if (toReload.equalsIgnoreCase("weapondef")) {
				GameDefinitionLoader.loadWeaponDefinitions();
				player.getClient().queueOutgoingPacket(new SendMessage("Reloaded."));
			} else if (toReload.equalsIgnoreCase("rangedstr")) {
				GameDefinitionLoader.loadRangedStrengthDefinitions();
				player.getClient().queueOutgoingPacket(new SendMessage("Reloaded."));
			} else if (toReload.equalsIgnoreCase("rangeddef")) {
				GameDefinitionLoader.loadRangedWeaponDefinitions();
				player.getClient().queueOutgoingPacket(new SendMessage("Reloaded."));
			} else if (toReload.equalsIgnoreCase("itemdef")) {
				GameDefinitionLoader.loadItemDefinitions();
				player.getClient().queueOutgoingPacket(new SendMessage("Reloaded."));
			} else if (toReload.equalsIgnoreCase("itemvalues")) {
				GameDefinitionLoader.loadItemValues();
				player.getClient().queueOutgoingPacket(new SendMessage("Item values have been reloaded."));
			} else if (toReload.equalsIgnoreCase("npcdrops")) {
				GameDefinitionLoader.loadNpcDropDefinitions();
				player.getClient().queueOutgoingPacket(new SendMessage("Reloaded."));
			} else if (toReload.equalsIgnoreCase("bonuses")) {
				GameDefinitionLoader.loadItemBonusDefinitions();
				player.getClient().queueOutgoingPacket(new SendMessage("Reloaded."));
			} else if (toReload.equalsIgnoreCase("specs")) {
				SpecialAttackHandler.declare();
				GameDefinitionLoader.loadSpecialAttackDefinitions();
				player.getClient().queueOutgoingPacket(new SendMessage("Specials have been reloaded."));
			} else if (toReload.equalsIgnoreCase("dropchances")) {
				GameDefinitionLoader.loadRareDropChances();
				player.getClient().queueOutgoingPacket(new SendMessage("Rare drop chances have been reloaded."));
			} else if (toReload.equalsIgnoreCase("spells")) {
				GameDefinitionLoader.loadCombatSpellDefinitions();
				MagicEffects.declare();
				player.getClient().queueOutgoingPacket(new SendMessage("Spells have been reloaded."));
			} else if (toReload.equalsIgnoreCase("sc")) {
				Emotes.declare();
				player.getClient().queueOutgoingPacket(new SendMessage("SC have been reloaded."));
			} else if (toReload.equalsIgnoreCase("potions")) {
				GameDefinitionLoader.loadPotionDefinitions();
				player.getClient().queueOutgoingPacket(new SendMessage("Reloaded."));
			} else if (toReload.equalsIgnoreCase("summoning")) {
				SummoningConstants.declare();
				player.getClient().queueOutgoingPacket(new SendMessage("Reloaded."));
			} else {
				player.getClient().queueOutgoingPacket(new SendMessage("Reload command not recognized."));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int rightsRequired() {
		return 0;
	}

	@Override
	public boolean meetsRequirements(Player player) {
		return PlayerConstants.isOwner(player);
	}
}
