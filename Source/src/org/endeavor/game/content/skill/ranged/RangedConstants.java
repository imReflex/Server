package org.endeavor.game.content.skill.ranged;

import org.endeavor.game.entity.Graphic;
import org.endeavor.game.entity.Projectile;
import org.endeavor.game.entity.item.Item;
import org.endeavor.game.entity.player.Player;

public class RangedConstants {
	public static final int AVAS_ACCUMULATOR_ID = 10499;
	public static final int DARK_BOW_ID = 11235;
	public static final int BOLT_SPECIAL_CHANCE = 20;
	public static final int DRAGON_BOLT_SPECIAL_GFX = 756;
	public static final int DIAMOND_BOLT_SPECIAL_GFX = 758;
	public static final int EMERALD_BOLT_SPECIAL_GFX = 752;
	public static final int RUBY_BOLT_SPECIAL_GFX = 754;
	public static final int ONYX_BOLT_SPECIAL_GFX = 753;
	public static final int RUNE_BOLT_ID = 9144;
	public static final int ONYX_BOLT_ID = 9245;
	public static final int RUBY_BOLT_ID = 9242;
	public static final int EMERALD_BOLT_ID = 9241;
	public static final int DIAMOND_BOLT_ID = 9243;
	public static final int DRAGON_BOLT_ID = 9244;
	public static final int BRONZE_ARROW_ID = 882;
	public static final int IRON_ARROW_ID = 884;
	public static final int STEEL_ARROW_ID = 886;
	public static final int MITHRIL_ARROW_ID = 888;
	public static final int ADAMANT_ARROW_ID = 890;
	public static final int RUNE_ARROW_ID = 892;
	public static final int DRAGON_ARROW_ID = 11212;
	public static final int BRONZE_ARROW_P_ID = 883;
	public static final int IRON_ARROW_P_ID = 885;
	public static final int STEEL_ARROW_P_ID = 887;
	public static final int MITHRIL_ARROW_P_ID = 889;
	public static final int ADAMANT_ARROW_P_ID = 891;
	public static final int RUNE_ARROW_P_ID = 893;
	public static final int DRAGON_ARROW_P_ID = 11227;
	public static final int BRONZE_ARROW_PP_ID = 5616;
	public static final int IRON_ARROW_PP_ID = 5617;
	public static final int STEEL_ARROW_PP_ID = 5618;
	public static final int MITHRIL_ARROW_PP_ID = 5619;
	public static final int ADAMANT_ARROW_PP_ID = 5620;
	public static final int RUNE_ARROW_PP_ID = 5621;
	public static final int DRAGON_ARROW_PP_ID = 0;
	public static final int BRONZE_ARROW_PPP_ID = 5622;
	public static final int IRON_ARROW_PPP_ID = 5623;
	public static final int STEEL_ARROW_PPP_ID = 5624;
	public static final int MITHRIL_ARROW_PPP_ID = 5625;
	public static final int ADAMANT_ARROW_PPP_ID = 5626;
	public static final int RUNE_ARROW_PPP_ID = 5627;
	public static final int DRAGON_ARROW_PPP_ID = 11228;
	public static final int BRONZE_ARROW_GRAPHIC = 19;
	public static final int IRON_ARROW_GRAPHIC = 18;
	public static final int STEEL_ARROW_GRAPHIC = 20;
	public static final int MITHRIL_ARROW_GRAPHIC = 21;
	public static final int ADAMANT_ARROW_GRAPHIC = 22;
	public static final int RUNE_ARROW_GRAPHIC = 24;
	public static final int DRAGON_ARROW_GRAPHIC = 11229;
	public static final int BOLT_GRAPHIC = 28;
	public static final int DRAGON_ARROW_PROJECTILE = 17;
	public static final int BRONZE_ARROW_PROJECTILE = 10;
	public static final int IRON_ARROW_PROJECTILE = 9;
	public static final int RUNE_ARROW_PROJECTILE = 15;
	public static final int ADAMANT_ARROW_PROJECTILE = 13;
	public static final int MITHRIL_ARROW_PROJECTILE = 12;
	public static final int STEEL_ARROW_PROJECTILE = 11;
	public static final int BOLT_PROJECTILE = 27;

	public static Graphic getDrawbackGraphic(Player player) {
		Item weapon = player.getEquipment().getItems()[3];
		Item ammo = player.getEquipment().getItems()[13];

		if ((weapon == null) || (weapon.getRangedDefinition() == null)) {
			return null;
		}

		switch (weapon.getRangedDefinition().getType()) {
		case SHOT:
			switch (weapon.getId()) {
			case 4214:
				return new Graphic(250, 0, true);
			}

			if (ammo == null) {
				return null;
			}

			AmmoData.Ammo data = AmmoData.Ammo.forId(ammo.getId());

			if (data == null) {
				return null;
			}
			if (weapon.getId() == 11235) {
				return data.getDrawback2();
			}
			return data.getDrawback1();
		case THROWN:
			switch (weapon.getId()) {
			case 811:
				return new Graphic(1240, 12, true);
			case 810:
				return new Graphic(1239, 12, true);
			case 809:
				return new Graphic(1238, 12, true);
			case 808:
				return new Graphic(1236, 12, true);
			case 807:
				return new Graphic(1235, 12, true);
			case 806:
				return new Graphic(1234, 12, true);
			case 805:
				return new Graphic(48, 0, true);
			case 804:
				return new Graphic(46, 0, true);
			case 803:
				return new Graphic(45, 0, true);
			case 802:
				return new Graphic(44, 0, true);
			case 801:
				return new Graphic(42, 0, true);
			case 800:
				return new Graphic(43, 0, true);
			case 830:
				return new Graphic(211, 1, true);
			case 829:
				return new Graphic(210, 1, true);
			case 828:
				return new Graphic(209, 1, true);
			case 827:
				return new Graphic(208, 1, true);
			case 826:
				return new Graphic(207, 1, true);
			case 825:
				return new Graphic(206, 1, true);
			case 11230:
			case 11231:
				return new Graphic(1123);
			}

			return null;
		}
		return null;
	}

	public static final Graphic getEndGraphic() {
		return null;
	}

	public static Projectile getProjectile(Player player) {
		Item weapon = player.getEquipment().getItems()[3];
		Item ammo = player.getEquipment().getItems()[13];
		if ((weapon == null) || (weapon.getRangedDefinition() == null)) {
			return null;
		}

		switch (weapon.getRangedDefinition().getType()) {
		case SHOT:
			switch (weapon.getId()) {
			case 4214:
				return new Projectile(249);
			case 15241:
				Projectile p = new Projectile(55);
				p.setStartHeight(23);
				p.setEndHeight(23);
				p.setCurve(0);
				return p;
			}

			if (ammo == null) {
				return null;
			}

			if (AmmoData.Ammo.forId(ammo.getId()) == null) {
				return null;
			}

			return AmmoData.Ammo.forId(ammo.getId()).getProjectile();
		case THROWN:
			switch (weapon.getId()) {
			case 13879:
			case 13882:
				return new Projectile(1837);
			case 6522:
				return new Projectile(442);
			case 811:
				return new Projectile(231);
			case 810:
				return new Projectile(230);
			case 809:
				return new Projectile(229);
			case 808:
				return new Projectile(228);
			case 807:
				return new Projectile(227);
			case 806:
				return new Projectile(226);
			case 805:
				return new Projectile(41);
			case 804:
				return new Projectile(39);
			case 803:
				return new Projectile(38);
			case 802:
				return new Projectile(37);
			case 801:
				return new Projectile(35);
			case 800:
				return new Projectile(36);
			case 830:
				return new Projectile(205);
			case 829:
				return new Projectile(204);
			case 828:
				return new Projectile(203);
			case 827:
				return new Projectile(202);
			case 826:
				return new Projectile(201);
			case 825:
				return new Projectile(200);
			case 11230:
			case 11231:
				return new Projectile(1122);
			}
			return null;
		}
		return null;
	}
}
