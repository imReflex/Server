package com.cache.crcpacker;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.zip.CRC32;
import java.util.zip.GZIPInputStream;

@SuppressWarnings("all")
public final class OnDemandFetcher {

	/**
	 * Grabs the checksum of a file from the cache.
	 * 
	 * @param type
	 *            The type of file (0 = model, 1 = anim, 2 = midi, 3 = map, 4 = image).
	 * @param id
	 *            The id of the file.
	 * @return
	 */
	public int getChecksum(int type, int id) {
		int crc = 0;
		byte[] data = clientInstance.cacheIndices[type + 1].get(id);
		if (data != null) {
			int length = data.length - 2;
			crc32.reset();
			crc32.update(data, 0, length);
			crc = (int) crc32.getValue();
		}
		return crc;
	}

	/**
	 * Grabs the version of a file from the cache.
	 * 
	 * @param type
	 *            The type of file (0 = model, 1 = anim, 2 = midi, 3 = map, 4 = image).
	 * @param id
	 *            The id of the file.
	 * @return
	 */
	public int getVersion(int type, int id) {
		int version = 1;
		byte[] data = clientInstance.cacheIndices[type + 1].get(id);
		if (data != null) {
			int length = data.length - 2;
			version = ((data[length] & 0xff) << 8) + (data[length + 1] & 0xff);
		}
		return version;
	}

	/**
	 * Writes the checksum list for the specified archive type and length.
	 * 
	 * @param type
	 *            The type of archive (0 = model, 1 = anim, 2 = midi, 3 = map, 4 = image).
	 * @param length
	 *            The number of files in the archive.
	 */
	public void writeChecksumList(int type) {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(SignLink.findcachedir() + type + "_crc.dat"));
			for (int index = 0; index < clientInstance.cacheIndices[type + 1].getFileCount(); index++) {
				out.writeInt(getChecksum(type, index));
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes the version list for the specified archive type and length.
	 * 
	 * @param type
	 *            The type of archive (0 = model, 1 = anim, 2 = midi, 3 = map, 4 = image).
	 * @param length
	 *            The number of files in the archive.
	 */
	public void writeVersionList(int type) {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(SignLink.findcachedir() + type + "_version.dat"));
			for (int index = 0; index < clientInstance.cacheIndices[type + 1].getFileCount(); index++) {
				out.writeShort(getVersion(type, index));
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Start on demand fetcher data.
	 * 
	 * @param archive
	 *            : Cache Archive that contains update list.
	 * @param client
	 *            : Client instance.
	 */
	public void start(Client client) {
		clientInstance = client;
		for(int i = 0; i < 6; i++)
			writeChecksumList(i);
		//writeVersionList(0);
	}

	public OnDemandFetcher() {
		crc32 = new CRC32();
	}

	private final CRC32 crc32;
	private Client clientInstance;
}