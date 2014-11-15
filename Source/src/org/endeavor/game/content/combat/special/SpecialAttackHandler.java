package org.endeavor.game.content.combat.special;

import java.util.HashMap;
import java.util.Map;

import org.endeavor.game.content.combat.special.effects.AbyssalWhipEffect;
import org.endeavor.game.content.combat.special.effects.BandosGodswordEffect;
import org.endeavor.game.content.combat.special.effects.BarrelchestAnchorEffect;
import org.endeavor.game.content.combat.special.effects.DragonScimitarEffect;
import org.endeavor.game.content.combat.special.effects.SaradominGodswordEffect;
import org.endeavor.game.content.combat.special.effects.SeercullEffect;
import org.endeavor.game.content.combat.special.effects.StaffOfLightEffect;
import org.endeavor.game.content.combat.special.effects.StatiusWarhammerEffect;
import org.endeavor.game.content.combat.special.specials.AbyssalWhipSpecialAttack;
import org.endeavor.game.content.combat.special.specials.AnchorSpecialAttack;
import org.endeavor.game.content.combat.special.specials.ArmadylGodswordSpecialAttack;
import org.endeavor.game.content.combat.special.specials.BandosGodswordSpecialAttack;
import org.endeavor.game.content.combat.special.specials.DarkBowSpecialAttack;
import org.endeavor.game.content.combat.special.specials.DragonClawsSpecialAttack;
import org.endeavor.game.content.combat.special.specials.DragonDaggerSpecialAttack;
import org.endeavor.game.content.combat.special.specials.DragonHalberdSpecialAttack;
import org.endeavor.game.content.combat.special.specials.DragonLongswordSpecialAttack;
import org.endeavor.game.content.combat.special.specials.DragonMaceSpecialAttack;
import org.endeavor.game.content.combat.special.specials.DragonScimitarSpecialAttack;
import org.endeavor.game.content.combat.special.specials.GraniteMaulSpecialAttack;
import org.endeavor.game.content.combat.special.specials.HandCannonSpecialAttack;
import org.endeavor.game.content.combat.special.specials.MagicShortbowSpecialAttack;
import org.endeavor.game.content.combat.special.specials.MorrigansJavelinSpecialAttack;
import org.endeavor.game.content.combat.special.specials.SaradominGodswordSpecialAttack;
import org.endeavor.game.content.combat.special.specials.SaradominSwordSpecialAttack;
import org.endeavor.game.content.combat.special.specials.StaffOfLightSpecialAttack;
import org.endeavor.game.content.combat.special.specials.StatiusWarhammerSpecialAttack;
import org.endeavor.game.content.combat.special.specials.VestaLongswordSpecialAttack;
import org.endeavor.game.content.combat.special.specials.ZamorakGodswordSpecialAttack;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.CombatEffect;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerConstants;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendString;
import org.endeavor.game.entity.player.net.out.impl.SendUpdateSpecialBar;

public class SpecialAttackHandler {
	private static Map<Integer, Special> specials = new HashMap<Integer, Special>();

	private static Map<Integer, CombatEffect> effects = new HashMap<Integer, CombatEffect>();

	private static void add(int weaponId, Special special) {
		specials.put(Integer.valueOf(weaponId), special);
	}

	private static void add(int weaponId, CombatEffect effect) {
		effects.put(Integer.valueOf(weaponId), effect);
	}

	public static void declare() {
		add(15486, new StaffOfLightSpecialAttack());
		add(1215, new DragonDaggerSpecialAttack());
		add(1231, new DragonDaggerSpecialAttack());
		add(5680, new DragonDaggerSpecialAttack());
		add(5698, new DragonDaggerSpecialAttack());
		add(4587, new DragonScimitarSpecialAttack());
		add(1305, new DragonLongswordSpecialAttack());
		add(1434, new DragonMaceSpecialAttack());
		add(10887, new AnchorSpecialAttack());
		add(4153, new GraniteMaulSpecialAttack());
		add(13902, new StatiusWarhammerSpecialAttack());
		add(13904, new StatiusWarhammerSpecialAttack());
		add(13926, new StatiusWarhammerSpecialAttack());
		add(13928, new StatiusWarhammerSpecialAttack());
		add(13899, new VestaLongswordSpecialAttack());
		add(13901, new VestaLongswordSpecialAttack());
		add(11694, new ArmadylGodswordSpecialAttack());
		add(11700, new ZamorakGodswordSpecialAttack());
		add(11696, new BandosGodswordSpecialAttack());
		add(11698, new SaradominGodswordSpecialAttack());
		add(3204, new DragonHalberdSpecialAttack());
		add(861, new MagicShortbowSpecialAttack());
		add(859, new MagicShortbowSpecialAttack());
		add(11235, new DarkBowSpecialAttack());
		add(14484, new DragonClawsSpecialAttack());

		add(13879, new MorrigansJavelinSpecialAttack());
		add(13882, new MorrigansJavelinSpecialAttack());

		add(11696, new BandosGodswordEffect());
		add(4587, new DragonScimitarEffect());
		add(11698, new SaradominGodswordEffect());
		add(10887, new BarrelchestAnchorEffect());
		add(6724, new SeercullEffect());
		add(15486, new StaffOfLightEffect());

		add(13902, new StatiusWarhammerEffect());
		add(13926, new StatiusWarhammerEffect());
		add(13928, new StatiusWarhammerEffect());

		add(4151, new AbyssalWhipSpecialAttack());
		add(4151, new AbyssalWhipEffect());
		
		add(21371, new AbyssalWhipSpecialAttack());
		add(21371, new AbyssalWhipEffect());

		add(15442, new AbyssalWhipSpecialAttack());
		add(15442, new AbyssalWhipEffect());

		add(15441, new AbyssalWhipSpecialAttack());
		add(15441, new AbyssalWhipEffect());

		add(15444, new AbyssalWhipSpecialAttack());
		add(15444, new AbyssalWhipEffect());

		add(15241, new HandCannonSpecialAttack());
		
		add(11730, new SaradominSwordSpecialAttack());
	}

	public static void executeSpecialEffect(Player player, Entity attacked) {
		Item weapon = player.getEquipment().getItems()[3];

		if (weapon == null) {
			return;
		}

		CombatEffect effect = effects.get(Integer.valueOf(weapon.getId()));

		if (effect == null) {
			return;
		}
		effect.execute(player, attacked);
	}

	public static void handleSpecialAttack(Player player) {
		Item weapon = player.getEquipment().getItems()[3];

		if (weapon == null) {
			return;
		}

		Special special = specials.get(Integer.valueOf(weapon.getId()));

		if (special == null) {
			return;
		}

		if (special.checkRequirements(player)) {
			player.getAchievements().incr(player, "Use a Special attack");
			player.getAchievements().incr(player, "Use 1,000 Special attacks");
			special.handleAttack(player);
			if (!PlayerConstants.isOwner(player))
				player.getSpecialAttack().deduct(special.getSpecialAmountRequired());
		}
	}

	public static boolean hasSpecialAmount(Player player) {
		Item weapon = player.getEquipment().getItems()[3];

		if (weapon == null) {
			return true;
		}

		Special special = specials.get(Integer.valueOf(weapon.getId()));

		if (special == null) {
			return true;
		}

		if (player.getSpecialAttack().getAmount() < special.getSpecialAmountRequired()) {
			player.getClient()
					.queueOutgoingPacket(new SendMessage("You do not have enough special attack to do that."));
			return false;
		}
		return true;
	}

	public static void updateSpecialBarText(Player p, int id, int amount, boolean init) {
		if (init)
			p.getClient().queueOutgoingPacket(new SendString("@yel@Special Attack (" + amount + "%)", id));
		else
			p.getClient().queueOutgoingPacket(new SendString("@bla@Special Attack (" + amount + "%)", id));
	}

	public static void updateSpecialAmount(Player p, int id, int amount) {
		int specialCheck = 100;
		for (int i = 0; i < 10; i++) {
			id--;
			p.getClient().queueOutgoingPacket(new SendUpdateSpecialBar(amount >= specialCheck ? 500 : 0, id));
			specialCheck -= 10;
		}
	}
}
