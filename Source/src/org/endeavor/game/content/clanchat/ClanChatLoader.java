package org.endeavor.game.content.clanchat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map.Entry;

import org.endeavor.game.content.clanchat.ClanChatChannel.JoinPermission;
import org.endeavor.game.content.io.PlayerSave;

public class ClanChatLoader {
	public static class ClanChatSave {
		private final String name;
		private final String owner;
		private final String[] rankedUsers;
		private final byte[] ranksForUsers;
		private final long[] bannedUsers;
		private final JoinPermission joinPermissions;
		
		public static ClanChatChannel load(String owner) {
			try {
				File file = new File("./data/characters/containers/" + owner + ".json");

				if (!file.exists()) {
					return null;//this clan does not exist
				}

				BufferedReader reader = new BufferedReader(new FileReader(file));
				ClanChatSave save = PlayerSave.GSON.fromJson(reader, ClanChatSave.class);
				
				if (save == null) {
					return null;
				}
				
				ClanChatChannel chan = new ClanChatChannel();
				
				chan.setOwner(save.owner);
				chan.setName(save.name);
				chan.setJoinPermissions(save.joinPermissions);
				
				if (save.rankedUsers != null) {
					for (int i = 0; i < save.rankedUsers.length; i++) {
						chan.getRights().put(save.rankedUsers[i], save.ranksForUsers[i]);
					}
				}
				
				if (save.bannedUsers != null) {
					for (long i : save.bannedUsers){
						chan.getBanned().add(i);
					}
				}
				
				return chan;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return null;//this clan did not load successfully
		}
		
		public ClanChatSave(ClanChatChannel channel) {
			this.name = channel.getName();
			this.joinPermissions = channel.getJoinPermissions();
			this.owner = channel.getOwner();
			
			int rankSize = channel.getRights().size();
			int banSize = channel.getBanned().size();
			
			if (rankSize > 0) {
				this.rankedUsers = new String[rankSize];
				this.ranksForUsers = new byte[rankSize];
				
				int index = 0;
				for (Entry<String, Byte> i : channel.getRights().entrySet()) {
					String name = i.getKey();
					byte rights = i.getValue();
					
					rankedUsers[index] = name;
					ranksForUsers[index] = rights;
					
					index++;
				}
			} else {
				this.rankedUsers = null;
				this.ranksForUsers = null;
			}
			
			if (banSize > 0) {
				this.bannedUsers = new long[banSize];
				
				int index = 0;
				for (long i : channel.getBanned()) {
					bannedUsers[index] = i;
					index++;
				}
			} else {
				this.bannedUsers = null;
			}
		}
		
		public void save() throws Exception {
			BufferedWriter writer = new BufferedWriter(new FileWriter("./data/characters/containers/"
					+ owner + ".json", false));
			try {
				writer.write(PlayerSave.GSON.toJson(this));
				writer.flush();
			} finally {
				writer.close();
			}
		}
		
		
	}

}