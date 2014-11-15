package com.cache.datapacker;

import java.applet.Applet;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.zip.CRC32;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.imageio.ImageIO;

import com.cache.Settings;
import com.cache.datapacker.loaders.RealFont;
import com.cache.datapacker.loaders.SpriteLoader;
import com.cache.datapacker.sign.*;


public class Client {

	public OnDemandFetcher onDemandFetcher;
	public Decompressor fileStores[];
	public int[] expectedCRCs = new int[9];
	private CRC32 archiveCRC;
	private Socket jagGrabSocket;
	private String toPack;
	public Client(String toPack) {
		this.toPack = toPack;
		fileStores = new Decompressor[7];
		archiveCRC = new CRC32();
		startup();
	}
	public void startup() {
		signlink.storeid = 32;
		try {
			signlink.startpriv(InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		if(signlink.cache_dat != null)
		{
			for(int i = 0; i < 7; i++)
			{
				fileStores[i] = new Decompressor(signlink.cache_dat, signlink.cache_idx[i], i + 1);
			}

		}	
		//connectServer();	
		//onDemandFetcher = new OnDemandFetcher();
		//onDemandFetcher.start(this);
		try {
			packData(toPack);
			//dumpData(toPack);
		} catch (Exception e ) {
			e.printStackTrace();
		}
	}
	public static final byte[] ReadFile(String s) {
		try {
			byte abyte0[];
			File file = new File(s);
			int i = (int) file.length();
			abyte0 = new byte[i];
			DataInputStream datainputstream = new DataInputStream(
					new BufferedInputStream(new FileInputStream(s)));
			datainputstream.readFully(abyte0, 0, i);
			datainputstream.close();
			return abyte0;
		} catch (Exception e) {
			return null;
		}
	}
	
	public void dumpData(String s) {
		int idx = -1;
		if(s.equalsIgnoreCase("config"))
			idx = 0;
		if(s.equalsIgnoreCase("model"))
			idx = 1;
		if(s.equalsIgnoreCase("anim"))
			idx = 2;
		if(s.equalsIgnoreCase("music"))
			idx = 3;
		if(s.equalsIgnoreCase("map"))
			idx = 4;
		if(s.equalsIgnoreCase("image"))
			idx = 5;
		if(s.equalsIgnoreCase("texture"))
			idx = 6;
		System.out.println(fileStores[idx].getFileCount());
		if(idx != 0) {
			try {
				for(int index = 0; index < fileStores[idx].getFileCount(); index++) {
					int i = 0;
					byte[] data = fileStores[idx].decompress(index);
					if(data == null)
						continue;
					GZIPInputStream stream = new GZIPInputStream(new ByteArrayInputStream(data));
					byte[] uncompressed = new byte[999999];
					do {
						if (i == uncompressed.length)
			                throw new RuntimeException("buffer overflow!");
			            int k = stream.read(uncompressed, i, uncompressed.length - i);
			            if (k == -1)
			                break;
			            i += k;
					} while(true);
					if(idx == 5 || idx == 6) {
						InputStream in = new ByteArrayInputStream(uncompressed);
						BufferedImage bi = ImageIO.read(in);
						if(bi == null)
							continue;
						ImageIO.write(bi, "png", new File(Settings.DUMP_PATH + "" + s + "/"+index+".png"));
					} else if(idx != 3) {
						FileOutputStream out = new FileOutputStream(Settings.DUMP_PATH + "" + s + "/"+index+".dat");
						out.write(uncompressed);
						out.close();
					} else {
						FileOutputStream out = new FileOutputStream(Settings.DUMP_PATH + "" + s + "/"+index+".midi");
						out.write(uncompressed);
						out.close();
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			CacheArchive configArchive = this.getArchive(2, "config", "config", 0, (byte)1, 0);
			if(configArchive == null) {
				System.out.println("Config Archive is null!");
				return;
			}
			SpriteLoader.loadSprites(configArchive);
			System.out.println(SpriteLoader.cache.length + " Sprites Loaded from Configuration Index.");
			try {
				for(int i = 0; i < SpriteLoader.cache.length; i++) {
					byte[] data = SpriteLoader.cache[i].spriteData;
					InputStream in = new ByteArrayInputStream(data);
					BufferedImage bi = ImageIO.read(in);
					if(bi == null)
						continue;
					ImageIO.write(bi, "png", new File(Settings.DUMP_PATH + "" + s + "/config/sprites2/"+i+".png"));
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void packData(String s) {
		if(s.equalsIgnoreCase("crc"))
		{
			onDemandFetcher.writeAll();
			return;
		}
		if(s.equalsIgnoreCase("fonts")) {
			packFonts();
			return;
		}
		File file = new File(Settings.PACK_PATH + "" + s + "/");
		System.out.println(file.getPath());
		File[] fileArray = file.listFiles();
		for (int y = 0; y < fileArray.length; y++) {
			String sss = fileArray[y].getName();
			byte[] buffer = ReadFile(Settings.PACK_PATH + "" + s + "/" + sss);
			int idx = -1;
			if(s.equalsIgnoreCase("model"))
				idx = 1;
			if(s.equalsIgnoreCase("anim"))
				idx = 2;
			if(s.equalsIgnoreCase("music"))
				idx = 3;
			if(s.equalsIgnoreCase("map"))
				idx = 4;
			if(s.equalsIgnoreCase("image"))
				idx = 5;
			if(s.equalsIgnoreCase("texture"))
				idx = 6;
			if(buffer != null) {
				if(getFileNameWithoutExtension(sss).equalsIgnoreCase("thumbs"))
						continue;
				if(fileStores[idx].put(buffer.length, buffer, Integer.parseInt(getFileNameWithoutExtension(sss)))) {
					System.out.println("*new* Packed "+s+": "+ Integer.parseInt(getFileNameWithoutExtension(sss)) +" in idx "+idx+"");
				} else {
					System.out.println("*updated* Packed "+s+": "+ Integer.parseInt(getFileNameWithoutExtension(sss)) +" in idx "+idx+"");
					
				}
			}
		}
	}
	public static String getFileNameWithoutExtension(String fileName) {
		File tmpFile = new File(fileName);
		tmpFile.getName();
		int whereDot = tmpFile.getName().lastIndexOf('.');
		if (0 < whereDot && whereDot <= tmpFile.getName().length() - 2) {
			return tmpFile.getName().substring(0, whereDot);
		}
		return "";
	}
	
	public void packFonts() {   	
		File in = new File(Settings.PACK_PATH + "fonts/in/");
		ArrayList<File> fonts = new ArrayList<File>();
		for(File file : in.listFiles())
			fonts.add(file);
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(Settings.PACK_PATH + "fonts/out/" + "fonts.dat"));
			out.writeByte(fonts.size());
			for (int index = 0; index < fonts.size(); index++) {
				byte[] abyte = fileToByteArray(fonts.get(index));
				out.writeLong(abyte.length);
				out.write(abyte, 0, abyte.length);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public byte[] fileToByteArray(File file) {
		try {
			byte[] fileData = new byte[(int) file.length()];
			FileInputStream fis = new FileInputStream(file);
			fis.read(fileData);
			fis.close();
			return fileData;
		} catch (Exception e) {
			return null;
		}
	}
	
	public final DataInputStream openJagGrabInputStream(String s)
			throws IOException
			{
		if(jagGrabSocket != null)
		{
			try
			{
				jagGrabSocket.close();
			}
			catch(Exception _ex) { }
			jagGrabSocket = null;
		}
		jagGrabSocket = openSocket(43595);
		jagGrabSocket.setSoTimeout(10000);
		java.io.InputStream inputstream = jagGrabSocket.getInputStream();
		OutputStream outputstream = jagGrabSocket.getOutputStream();
		outputstream.write(("JAGGRAB /" + s + "\n\n").getBytes());
		return new DataInputStream(inputstream);
	}
	private CacheArchive getArchive(int index, String name, String achiveName, int crc, byte byte0, int k)
	{
		byte cache[] = null;
		int l = 5;
		try
		{
			if(fileStores[0] != null)
			{
				cache = fileStores[0].decompress(index);
			}
		} catch(Exception _ex) {_ex.printStackTrace(); }
		if(cache != null)
		{
			CacheArchive streamLoader = new CacheArchive(cache);
			return streamLoader;
		}
		return null;
		/*int j1 = 0;
		while(cache == null) {
			String s2 = "";
			System.out.println("Requesting " + name);
			try {
				DataInputStream datainputstream = openJagGrabInputStream(achiveName + crc);
				byte abyte1[] = new byte[6];
				datainputstream.readFully(abyte1, 0, 6);
				Stream stream = new Stream(abyte1);
				stream.currentOffset = 3;
				int i2 = stream.read3Bytes() + 6;
				int j2 = 6;
				cache = new byte[i2];
				System.arraycopy(abyte1, 0, cache, 0, 6);

				while(j2 < i2) 
				{
					int l2 = i2 - j2;
					if(l2 > 1000)
						l2 = 1000;
					int j3 = datainputstream.read(cache, j2, l2);
					if(j3 < 0)
					{
						s2 = "Length error: " + j2 + "/" + i2;
						throw new IOException("EOF");
					}
					j2 += j3;
				}
				datainputstream.close();
				try
				{
					if(fileStores[0] != null)
						fileStores[0].put(cache.length, cache, index);
				}
				catch(Exception _ex)
				{
					fileStores[0] = null;
				}
				if(cache != null) {
					archiveCRC.reset();
					archiveCRC.update(cache);
					int i3 = (int)archiveCRC.getValue();
					if(i3 != crc)
					{
						//cache = null;
						j1++;
						System.out.println("Checksum error: " + i3);
					}
				}
			}catch(IOException ioexception)
			{
				if(s2.equals("Unknown error"))
					s2 = "Connection error";
				cache = null;
			}
			catch(NullPointerException _ex)
			{
				s2 = "Null error";
				cache = null;
			}
			catch(ArrayIndexOutOfBoundsException _ex)
			{
				s2 = "Bounds error";
				cache = null;
			}
			catch(Exception _ex)
			{
				s2 = "Unexpected error";
				cache = null;
			}
			if(cache == null)
			{
				for(int l1 = l; l1 > 0; l1--)
				{
					if(j1 >= 3)
					{
						//drawLoadingText(k, "Game updated - please reload page");
						l1 = 10;
					} else
					{
						//drawLoadingText(k, s2 + " - Retrying in " + l1);
					}
					try
					{
						Thread.sleep(1000L);
					}
					catch(Exception _ex) { }
				}

				l *= 2;
				if(l > 60)
					l = 60;
			}

		}

		CacheArchive streamLoader_1 = new CacheArchive(cache);
		return streamLoader_1;*/
	}	
	public final Socket openSocket(int i) throws IOException {
		return new Socket(InetAddress.getByName("localhost"), i);
	}
	public final void startThread(Runnable runnable, int i) {
		if(i > 10)
			i = 10;
		if(signlink.mainapp != null) {
			signlink.startthread(runnable, i);
			return;
		} else {
	        Thread thread = new Thread(runnable);
	        thread.start();
	        thread.setPriority(i);
			return;
		}
	}
	
}
