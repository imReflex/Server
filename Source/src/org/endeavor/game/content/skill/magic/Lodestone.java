package org.endeavor.game.content.skill.magic;

import org.endeavor.game.content.skill.magic.MagicSkill.TeleportTypes;
import org.endeavor.game.entity.Location;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendRemoveInterfaces;

public class Lodestone {
	
	public static int[][] MAP_ICON_TELEPORTS = {
		//{button, x, y},
		//{button, x, y},
		{185187, 2644, 3676},//rellekka theiving
		{185193, 3206, 9379},//rellekka slayer dungeon
		{185238, 2474, 3438},//agility
		{185220, 2431, 3424},//gnome stronghold wc
		//{186003, , },//ardougne theiving
		{186000, 3161, 4237},//basic training
		{185253, 2590, 3420},//fishing guild
		{185148, 2715, 3463},//seers village wc
		{185145, 2815, 3461},//catherby farming
		{185142, 2839, 3434},//catherby fishing
		{185196, 2884, 9798},//taverly dungeon
		{185154, 2897, 3428},//herb shop
		{185178, 2926, 3444},//summoning
		{185181, 3047, 4971},//rouges den
		{185205, 2919, 3750},//god wars
		{185211, 3008, 3850},//KBD
		{185160, 2981, 3595},//lvl 13 green drags
		{185157, 3090, 3532},//edge wild
		{185139, 3106, 3554},//abyss entrance
		{185172, 2946, 3380},//make over mage fally
		{185130, 2974, 3369},//smithing area fally
		{185151, 3023, 3339},//mining area fally
		{185199, 3009, 3376},//farming area fally
		{185124, 3086, 3231},//fishing - town by lumby
		{185133, 3079, 3250},//thieving - town by lmby
		{185133, 3087, 3233},//woodcutting - town by lumby
		{185217, 3086, 3232},//fishing in lumby
		{185223, 3230, 3204},//bob shop
		{185232, 3277, 3186},//jewelry craft in al kharid
		{185226, 3287, 3209},//gem shop in al kharid
		{185163, 3320, 3193},//crafting area in al kharid
		{185250, 3304, 3120},//shantay pass
		{185247, 3404, 3090},//corp
		{185190, 3228, 3110},//kalphite cave
		{185202, 3428, 3538},//slayer tower
		{185229, 3577, 9927},//experiments
		{185241, 3511, 3507},//slayer master
		{185166, 3368, 3266},//dueling
		{185127, 3295, 3272},//al kharid mining
	};
	
	public static boolean clickButton(Player player, int button) {
		for (int i = 0; i < MAP_ICON_TELEPORTS.length; i++) {
			if (button == MAP_ICON_TELEPORTS[i][0]) {
				tollTeleport(player, MAP_ICON_TELEPORTS[i][1], MAP_ICON_TELEPORTS[i][2]);
				return true;
			}
		}
		
		switch (button) {
		case 134254:
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			return true;
		
		case 135012://edge
			player.getMagic().teleport(3087, 3491, 0, TeleportTypes.SPELL_BOOK);
			return true;
		case 135009://fally
			player.getMagic().teleport(2965, 3385, 0, TeleportTypes.SPELL_BOOK);
			return true;
		case 135006://port sarim
			player.getMagic().teleport(3053, 3248, 0, TeleportTypes.SPELL_BOOK);
			return true;
		case 135003://lumby
			player.getMagic().teleport(3219, 3247, 0, TeleportTypes.SPELL_BOOK);
			return true;
		case 135030://canifis
			player.getMagic().teleport(3491, 3484, 0, TeleportTypes.SPELL_BOOK);
			return true;
		case 135015://burthrope
			player.getMagic().teleport(2895, 3535, 0, TeleportTypes.SPELL_BOOK);
			return true;
		case 135018://camelot
			player.getMagic().teleport(2757, 3478, 0, TeleportTypes.SPELL_BOOK);
			return true;
		case 135027://yanile
			player.getMagic().teleport(2606, 3093, 0, TeleportTypes.SPELL_BOOK);
			return true;
		case 135021://rellekka
			player.getMagic().teleport(2659, 3661, 0, TeleportTypes.SPELL_BOOK);
			return true;
		case 135033://mort'ton
			player.getMagic().teleport(3487, 3287, 0, TeleportTypes.SPELL_BOOK);
			return true;
		case 135036://pest control
			player.getMagic().teleport(2662, 2651, 0, TeleportTypes.SPELL_BOOK);
			return true;
		case 135039://duel arena
			player.getMagic().teleport(3364, 3266, 0, TeleportTypes.SPELL_BOOK);
			return true;
		case 135042://gnome stronghold
			player.getMagic().teleport(2446, 3432, 1, TeleportTypes.SPELL_BOOK);
			return true;
			
		case 135024://tzhaar
			player.getMagic().teleport(2450, 5166, 0, TeleportTypes.SPELL_BOOK);
			return true;
		
		case 135045://brimhaven 
			player.getMagic().teleport(2712, 9564, 0, TeleportTypes.SPELL_BOOK);
			return true;
			
		case 135048://waterbirth 
			player.getMagic().teleport(2547, 3758, 0, TeleportTypes.SPELL_BOOK);
			return true;
			
		case 135051://fishing guild
			player.getMagic().teleport(2590, 3420, 0, TeleportTypes.SPELL_BOOK);
			return true;
		}
		
		return false;
	}
	
	public static void tollTeleport(Player player, int x, int y) {
		//if (player.getInventory().hasItemId(563)) {
			//player.getInventory().remove(563, 1, true);
			player.teleport(new Location(x, y, 0));
		//} else {
			//player.send(new SendMessage("You need a law rune to fast-travel directly to this area."));
			//player.send(new SendMessage("Otherwise, you can fast-travel using the <img=19> icon closest."));
		//}
	}

}
