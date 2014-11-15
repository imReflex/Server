package org.endeavor.game.content.skill.magic;

import java.util.HashMap;
import java.util.Map;

import org.endeavor.game.content.skill.magic.effects.BindEffect;
import org.endeavor.game.content.skill.magic.effects.BloodBarrageEffect;
import org.endeavor.game.content.skill.magic.effects.BloodBlitzEffect;
import org.endeavor.game.content.skill.magic.effects.BloodBurstEffect;
import org.endeavor.game.content.skill.magic.effects.BloodRushEffect;
import org.endeavor.game.content.skill.magic.effects.ClawsOfGuthixEffect;
import org.endeavor.game.content.skill.magic.effects.EntangleEffect;
import org.endeavor.game.content.skill.magic.effects.FlamesOfZamorakEffect;
import org.endeavor.game.content.skill.magic.effects.IceBarrageEffect;
import org.endeavor.game.content.skill.magic.effects.IceBlitzEffect;
import org.endeavor.game.content.skill.magic.effects.IceBurstEffect;
import org.endeavor.game.content.skill.magic.effects.IceRushEffect;
import org.endeavor.game.content.skill.magic.effects.SaradominStrikeEffect;
import org.endeavor.game.content.skill.magic.effects.ShadowBarrageEffect;
import org.endeavor.game.content.skill.magic.effects.ShadowBlitzEffect;
import org.endeavor.game.content.skill.magic.effects.ShadowBurstEffect;
import org.endeavor.game.content.skill.magic.effects.ShadowRushEffect;
import org.endeavor.game.content.skill.magic.effects.SmokeBarrageEffect;
import org.endeavor.game.content.skill.magic.effects.SmokeBlitzEffect;
import org.endeavor.game.content.skill.magic.effects.SmokeBurstEffect;
import org.endeavor.game.content.skill.magic.effects.SmokeRushEffect;
import org.endeavor.game.content.skill.magic.effects.SnareEffect;
import org.endeavor.game.content.skill.magic.effects.TeleBlockEffect;
import org.endeavor.game.entity.Entity;
import org.endeavor.game.entity.player.CombatEffect;
import org.endeavor.game.entity.player.Player;

public class MagicEffects {
	private static final Map<Integer, CombatEffect> effects = new HashMap<Integer, CombatEffect>();

	public static final void declare() {
		effects.put(Integer.valueOf(12939), new SmokeRushEffect());
		effects.put(Integer.valueOf(12987), new ShadowRushEffect());
		effects.put(Integer.valueOf(12901), new BloodRushEffect());
		effects.put(Integer.valueOf(12861), new IceRushEffect());
		effects.put(Integer.valueOf(12963), new SmokeBurstEffect());
		effects.put(Integer.valueOf(13011), new ShadowBurstEffect());
		effects.put(Integer.valueOf(12919), new BloodBurstEffect());
		effects.put(Integer.valueOf(12881), new IceBurstEffect());
		effects.put(Integer.valueOf(12951), new SmokeBlitzEffect());
		effects.put(Integer.valueOf(12999), new ShadowBlitzEffect());
		effects.put(Integer.valueOf(12911), new BloodBlitzEffect());
		effects.put(Integer.valueOf(12871), new IceBlitzEffect());
		effects.put(Integer.valueOf(12975), new SmokeBarrageEffect());
		effects.put(Integer.valueOf(13023), new ShadowBarrageEffect());
		effects.put(Integer.valueOf(12929), new BloodBarrageEffect());
		effects.put(Integer.valueOf(12891), new IceBarrageEffect());

		effects.put(Integer.valueOf(1190), new SaradominStrikeEffect());
		effects.put(Integer.valueOf(1191), new ClawsOfGuthixEffect());
		effects.put(Integer.valueOf(1192), new FlamesOfZamorakEffect());

		effects.put(Integer.valueOf(1572), new BindEffect());
		effects.put(Integer.valueOf(1582), new SnareEffect());
		effects.put(Integer.valueOf(1592), new EntangleEffect());

		effects.put(Integer.valueOf(12445), new TeleBlockEffect());
	}

	public static void doMagicEffects(Player attacker, Entity attacked, int spellId) {
		CombatEffect effect = effects.get(Integer.valueOf(spellId));

		if (effect == null) {
			return;
		}

		effect.execute(attacker, attacked);
	}
}
