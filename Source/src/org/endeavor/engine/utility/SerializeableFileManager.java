package org.endeavor.engine.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ConcurrentModificationException;

import org.endeavor.GameSettings;
import org.endeavor.game.content.clans.Clan;
import org.endeavor.game.content.clans.Clans;
import org.endeavor.game.entity.player.Player;

public class SerializeableFileManager {

	private static final String PATH = !GameSettings.DEV_MODE ? "C:/Users/Administrator/Desktop/Server Data/characters/" : "data/characters/";
	private static final String BACKUP_PATH = !GameSettings.DEV_MODE ? "C:/Users/Administrator/Desktop/Server Data/charactersBackup/" : "data/charactersBackup/";

	public synchronized static final boolean containsPlayer(String username) {
		return new File(PATH + username + ".p").exists()|| new File(BACKUP_PATH + username + ".p").exists();
	}

	public synchronized static Player loadPlayer(String username) {
		try {
			return (Player) loadSerializedFile(new File(PATH + username + ".p"));
		} catch (Throwable e) {
			//Logger.handle(e);
		}
		try {
			//Logger.log("SerializableFilesManager", "Recovering account: " + username);
			return (Player) loadSerializedFile(new File(BACKUP_PATH + username + ".p"));
		} catch (Throwable e) {
			//Logger.handle(e);
		}
		return null;
	}

	public static boolean createBackup(String username) {
		if(!GameSettings.DEV_MODE) {
			try {
				FileUtils.copy(new File(PATH + username + ".p"), new File( BACKUP_PATH + username + ".p"));
				return true;
			} catch (Throwable e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	public synchronized static void savePlayer(Player player) {
		try {
			if(player == null)
				return;
			storeSerializableClass(player, new File(PATH + player.getUsername() + ".p"));
		} catch (ConcurrentModificationException e) {
			System.out.println("CONCURRENT SAVE MODIFICATION ERROR.");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public synchronized static Clan loadClan(File file) {
		try {
			return (Clan) loadSerializedFile(file);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public synchronized static void saveClan(Clan clan) {
		try {
			if(clan == null)
				return;
			storeSerializableClass(clan, new File(Clans.CLAN_DIRECTORY + clan.getOwner() + ".c"));
		} catch (ConcurrentModificationException e) {
			System.out.println("CONCURRENT SAVE MODIFICATION ERROR.");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public static final Object loadSerializedFile(File f) {
		if(f == null)
			return null;
		try {
			if (!f.exists())
				return null;
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
			Object object = in.readObject();
			in.close();
			return object;
		} catch (Exception e) {
			System.out.println("ERROR LOADING PLAYER/CLAN FILE: " + f.getName());
			e.printStackTrace();
			return null;
		}
	}

	public static final void storeSerializableClass(Serializable o, File f) throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
		out.writeObject(o);
		out.close();
	}
	
}
