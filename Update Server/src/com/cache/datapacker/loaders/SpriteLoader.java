package com.cache.datapacker.loaders;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import com.cache.Settings;
import com.cache.datapacker.CacheArchive;
import com.cache.datapacker.Client;
import com.cache.datapacker.Stream;

public class SpriteLoader {

	/**
	 * Loads the sprite data and index files from the cache location. This can
	 * be edited to use an archive such as config or media to load from the
	 * cache.
	 * 
	 * @param archive
	 */
	public static void loadSprites(CacheArchive streamLoader) {
		try {
			byte[] idx = /*streamLoader.getDataForName("sprites.idx",null)*/Client.ReadFile(Settings.DUMP_PATH + "sprites.idx");
			byte[] dat = /*streamLoader.getDataForName("sprites.dat",null)*/Client.ReadFile(Settings.DUMP_PATH + "sprites.dat");
			if(idx == null || dat == null) {
				System.out.println("Index or Data was null for SpriteLoader");
				return;
			}
			Stream index = new Stream(idx);
			Stream data = new Stream(dat);
			if(index == null || data == null) {
				System.out.println("Index or Data is null for SpriteLoader");
				return;
			}
			DataInputStream indexFile = new DataInputStream(new GZIPInputStream(new ByteArrayInputStream(index.data)));
			DataInputStream dataFile = new DataInputStream(new GZIPInputStream(new ByteArrayInputStream(data.data)));
			int totalSprites = indexFile.readInt();
			if (cache == null) {
				cache = new SpriteLoader[totalSprites];
			}
			for (int i = 0; i < totalSprites; i++) {
				int id = indexFile.readInt();
				if (cache[id] == null) {
					cache[id] = new SpriteLoader();
				}
				cache[id].readValues(indexFile, dataFile);
				//createSprite(cache[id]);
			}
			indexFile.close();
			dataFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Reads the information from the index and data files.
	 * 
	 * @param index
	 *            holds the sprite indices
	 * @param data
	 *            holds the sprite data per index
	 * @throws IOException
	 */
	public void readValues(DataInputStream index, DataInputStream data) throws IOException {
		do {
			int opCode = data.readByte();
			if (opCode == 0) {
				break;
			}
			if (opCode == 1) {
				id = data.readShort();
			} else if (opCode == 2) {
				name = data.readUTF();
			} else if (opCode == 3) {
				drawOffsetX = data.readShort();
			} else if (opCode == 4) {
				drawOffsetY = data.readShort();
			} else if (opCode == 5) {
				int indexLength = index.readInt();
				byte[] dataread = new byte[indexLength];
				data.readFully(dataread);
				spriteData = dataread;
			}
		} while (true);
	}

	/**
	 * Creates a sprite out of the spriteData.
	 * 
	 * @param sprite
	 */
	/*public static void createSprite(SpriteLoader sprite) {
		/*
		 * File directory = new File(signlink.findcachedir() + "dump"); if
		 * (!directory.exists()) { directory.mkdir(); } DataUtils.writeFile(new
		 * File(directory.getAbsolutePath() +
		 * System.getProperty("file.separator") + sprite.id + ".png"),
		 * sprite.spriteData);
		 */
		/*sprites[sprite.id] = new Sprite(sprite.spriteData);
		sprites[sprite.id].drawOffsetX = sprite.drawOffsetX;
		sprites[sprite.id].drawOffsetY = sprite.drawOffsetY;
	}*/

	/**
	 * Gets the name of a specified sprite index.
	 * 
	 * @param index
	 * @return
	 */
	public static String getName(int index) {
		if (cache[index].name != null) {
			return cache[index].name;
		} else {
			return "null";
		}
	}

	/**
	 * Gets the drawOffsetX of a specified sprite index.
	 * 
	 * @param index
	 * @return
	 */
	public static int getOffsetX(int index) {
		return cache[index].drawOffsetX;
	}

	/**
	 * Gets the drawOffsetY of a specified sprite index.
	 * 
	 * @param index
	 * @return
	 */
	public static int getOffsetY(int index) {
		return cache[index].drawOffsetY;
	}

	/**
	 * Sets the default values.
	 */
	public SpriteLoader() {
		name = "name";
		id = -1;
		drawOffsetX = 0;
		drawOffsetY = 0;
		spriteData = null;
	}

	public static SpriteLoader[] cache;
	//public static Sprite[] sprites = null;
	public static int totalSprites;
	public String name;
	public int id;
	public int drawOffsetX;
	public int drawOffsetY;
	public byte[] spriteData;
	
}
