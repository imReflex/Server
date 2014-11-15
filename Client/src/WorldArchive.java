

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;

import sign.signlink;

public final class WorldArchive 
{

	public static RSApplet shell;
		
	public static CacheArchive loadWorldMap()
	{
		byte worldMapData[] = null;
		String cacheFolder = null;
		try
		{
			cacheFolder = signlink.findcachedir();
			worldMapData = FileOperations.ReadFile(cacheFolder + "worldmap.dat");
			if(!checkSha(worldMapData))
				worldMapData = null;
			if(worldMapData != null)
				return new CacheArchive(worldMapData);
		} catch(Throwable _ex)
		{ 
		}
		worldMapData = downloadMap();
		if(cacheFolder != null && worldMapData != null)
			try 
			{
				writeFile(cacheFolder + "worldmap.dat", worldMapData);
			} catch(Throwable _ex) 
			{ 
			}
		return new CacheArchive(worldMapData);
	}

	public static byte[] downloadMap() {
		shell.drawLoadingText(0, "Requesting map");
		try {
			String worldmapSuffix = "";
			for(int i = 0; i < 10; i++)
				worldmapSuffix = worldmapSuffix + WorldMapSHA.shaHashs[i];

			DataInputStream datainputstream;
			datainputstream = new DataInputStream(new FileInputStream(signlink.findcachedir() + "worldmap.dat"));
			int lastPercentage = 0;
			int offset = 0;
			int fileSize = 0x53901;
			byte abyte0[] = new byte[fileSize];
			while(offset < fileSize) {
				int length = fileSize - offset;
				if(length > 1000)
					length = 1000;
				int j1 = datainputstream.read(abyte0, offset, length);
				if(j1 < 0) {
					datainputstream.close();
					throw new IOException("End of file");
				}
				offset += j1;
				int percentage = (offset * 100) / fileSize;
				if(percentage != lastPercentage)
					shell.drawLoadingText(percentage, "Loading map - " + percentage + "%");
				lastPercentage = percentage;
			}
			datainputstream.close();
			return abyte0;
		} catch(IOException ioexception) {
			System.out.println("[ARCHIVE]: Error loading");
			ioexception.printStackTrace();
			return null;
		}
	}

	public byte[] getFileData(String fileName) throws IOException {
		File file = new File(fileName);
		if(!file.exists()) {
			return null;
		} else {
			int fileLength = (int)file.length();
			byte buffer[] = new byte[fileLength];
			DataInputStream datainputstream = new DataInputStream(new BufferedInputStream(new FileInputStream(fileName)));
			datainputstream.readFully(buffer, 0, fileLength);
			datainputstream.close();
			return buffer;
		}
	}

	public static void writeFile(String fileName, byte data[]) throws IOException {
		FileOutputStream fileoutputstream = new FileOutputStream(fileName);
		fileoutputstream.write(data, 0, data.length);
		fileoutputstream.close();
	}

	public static boolean checkSha(byte data[]) throws Exception {
		if(data == null)
			return false;
		MessageDigest messagedigest = MessageDigest.getInstance("SHA");
		messagedigest.reset();
		messagedigest.update(data);
		byte dataDigestHash[] = messagedigest.digest();
		for(int i = 0; i < 20; i++)
			if(dataDigestHash[i] != WorldMapSHA.shaHashs[i]) {
				System.out.println("[SHA]: SHA VERIFICATION FAIL, PLEASE UPDATE SHA HASHS");
				return false;
			}

		return true;
	}
	
    public static void reporterror(String s) {
        System.out.println("[DEBUG]: Error: " + s);
    }

    private WorldArchive() {
	
    }

}