package org.endeavor.game.content.clans;

import org.endeavor.engine.utility.NameUtil;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.out.impl.SendInterface;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;
import org.endeavor.game.entity.player.net.out.impl.SendString;

/**
 * 
 * @author allen_000
 *
 */
public final class ClanEdit {

	private static final int EDIT_INTERFACE_ID = 40172;
	private static final int CLOSE_INTERFACE = 184163;
	public static final int CLAN_NAME = 47814;
	private static final int CLAN_ENTER = 47815;
	private static final int CLAN_TALK = 47816;
	private static final int CLAN_KICK = 47817;
	private static final int NAME_START = 44001;
	private static final int RANK_START = 44801;
	
	public static void openEditInterface(Clan clan, Player player) {
		if(clan.getOwner().equalsIgnoreCase(player.getUsername())) {
			player.send(new SendString(NameUtil.uppercaseFirstLetter(clan.getSettings().getTitle()), CLAN_NAME));
			player.send(new SendString(clan.getSettings().getJoinRank().getRankName(), CLAN_ENTER));
			player.send(new SendString(clan.getSettings().getCanTalk().getRankName(), CLAN_TALK));
			player.send(new SendString(clan.getSettings().getCanKick().getRankName(), CLAN_KICK));
			int nameIndex = 0;
			int rankIndex = 0;
			for(ClanMember member : clan.getMembers().values()) {
				if(member.getPlayerUsername().equalsIgnoreCase(player.getUsername()))
					continue;
				player.send(new SendString(member.getPlayerUsername(), NAME_START + nameIndex));
				player.send(new SendString(member.getRank().getRankName(), RANK_START + rankIndex));
				nameIndex++; 
				rankIndex++;
			}
			/*for(int nIndex = NAME_START + nameIndex, rIndex = RANK_START + rankIndex; nIndex <= (NAME_START + 198) && rIndex <= (RANK_START + 198); nIndex++, rIndex++) {
				player.send(new SendString("", nIndex));
				player.send(new SendString("", rIndex));
			}*/
			player.send(new SendInterface(EDIT_INTERFACE_ID));
		}
	}
	
	public static boolean handleEditClick(Clan clan, Player player, int buttonID) {
		if(!clan.getOwner().equalsIgnoreCase(player.getUsername())) {
			player.send(new SendMessage("You do not own this clan."));
			return false;
		}
		switch(buttonID) {
		case 70212://manage
			clan.openClanEdit(player);
			return true;
		case 184151:
			clan.getSettings().setCanJoin(ClanRanks.GUEST);
			return true;
		case 187135:
			clan.getSettings().setCanJoin(ClanRanks.MEMBER);
			return true;
		case 187134:
			clan.getSettings().setCanJoin(ClanRanks.RECRUIT);
			return true;
		case 187133:
			clan.getSettings().setCanJoin(ClanRanks.CORPORAL);
			return true;
		case 187132:
			clan.getSettings().setCanJoin(ClanRanks.SERGEANT);
			return true;
		case 187131:
			clan.getSettings().setCanJoin(ClanRanks.LIEUTENANT);
			return true;
		case 187130:
			clan.getSettings().setCanJoin(ClanRanks.CAPTAIN);
			return true;
		case 187129:
			clan.getSettings().setCanJoin(ClanRanks.GENERAL);
			return true;
		case 187128:
			clan.getSettings().setCanJoin(ClanRanks.OWNER);
			return true;
		case 184154:
			clan.getSettings().setCanTalk(ClanRanks.GUEST);
			return true;
		case 187145:
			clan.getSettings().setCanTalk(ClanRanks.MEMBER);
			return true;
		case 187144:
			clan.getSettings().setCanTalk(ClanRanks.RECRUIT);
			return true;
		case 187143:
			clan.getSettings().setCanTalk(ClanRanks.CORPORAL);
			return true;
		case 187142:
			clan.getSettings().setCanTalk(ClanRanks.SERGEANT);
			return true;
		case 187141:
			clan.getSettings().setCanTalk(ClanRanks.LIEUTENANT);
			return true;
		case 187140:
			clan.getSettings().setCanTalk(ClanRanks.CAPTAIN);
			return true;
		case 187139:
			clan.getSettings().setCanTalk(ClanRanks.GENERAL);
			return true;
		case 187138:
			clan.getSettings().setCanTalk(ClanRanks.OWNER);
			return true;
		case 184157:
			clan.getSettings().setCanKick(ClanRanks.OWNER);
			return true;
		case 187154:
			clan.getSettings().setCanKick(ClanRanks.RECRUIT);
			return true;
		case 187153:
			clan.getSettings().setCanKick(ClanRanks.CORPORAL);
			return true;
		case 187152:
			clan.getSettings().setCanKick(ClanRanks.SERGEANT);
			return true;
		case 187151:
			clan.getSettings().setCanKick(ClanRanks.LIEUTENANT);
			return true;
		case 187150:
			clan.getSettings().setCanKick(ClanRanks.CAPTAIN);
			return true;
		case 187149:
			clan.getSettings().setCanKick(ClanRanks.GENERAL);
		}
		return false;
	}
	
	public static void updateLine(Clan clan, Player player, String line, int frame) {
		
	}
	
}
