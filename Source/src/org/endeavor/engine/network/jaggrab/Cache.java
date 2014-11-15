package org.endeavor.engine.network.jaggrab;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 * @author Joshua Barry
 */
public class Cache {

	public static final String DATA_FILE = "main_file_cache.dat";
	public static final String INDEX_FILE = "main_file_cache.idx";
	public static final int INDEX_SIZE = 6;
	public static final int HEADER_SIZE = 8;
	public static final int BLOCK_SIZE = 512;
	public static final int SECTOR_SIZE = HEADER_SIZE + BLOCK_SIZE;

	public AccessFile dataFile;
	public Map<Integer, AccessFile> indices = new HashMap<Integer, AccessFile>();
	public File path;

	public Cache(String path) throws FileNotFoundException {
		this.path = new File(path);
		this.dataFile = new AccessFile(new File(this.path, DATA_FILE));

		for (int i = 0; i < 256; i++) {
			File f = new File(this.path, INDEX_FILE + i);
			if (f.exists()) {
				this.indices.put(i, new AccessFile(f));
			}
		}
	}

	public AccessFile getIndex(int index) throws IOException {
		AccessFile f = this.indices.get(index);
		if (f != null) {
			return f;
		}
		throw new IOException("Invalid cache index: " + index);
	}

	public synchronized byte[] get(int index, int file) throws IOException {

		AccessFile f = this.getIndex(index);
		f.seek(file * INDEX_SIZE);

		int size = f.readTriByte();
		int sector = f.readTriByte();
		int sectorBounds = this.getSectorCount();

		if (size < 0) {
			throw new IOException("File non-existant.");
		}

		if (sector <= 0 || sector > sectorBounds) {
			throw new IOException("Sector out of bounds.");
		}

		int read = 0;
		int part = 0;

		byte[] data = new byte[size];

		// Increase the index since the cache internally starts at 1.
		index++;

		while (read < size) {

			if (sector == 0) {
				throw new IOException("Invalid next sector: 0");
			}

			int available = size - read;

			if (available > BLOCK_SIZE) {
				available = BLOCK_SIZE;
			}

			this.dataFile.seek(sector * SECTOR_SIZE);

			int sectorFile = this.dataFile.readShort() & 0xFFFF;
			int sectorPart = this.dataFile.readShort() & 0xFFFF;
			int nextSector = this.dataFile.readTriByte();
			int sectorCache = this.dataFile.readByte() & 0xFF;

			if (sectorFile != file) {
				throw new IOException("File mismatch: " + sectorFile + " != " + file);
			}

			if (sectorPart != part) {
				throw new IOException("Part mismatch: " + sectorPart + " != " + part);
			}

			if (nextSector < 0 || nextSector > sectorBounds) {
				throw new IOException("Next sector out of bounds.");
			}

			if (sectorCache != index) {
				throw new IOException("Cache mismatch: " + sectorCache + " != " + index);
			}

			this.dataFile.read(data, read, available);
			read += available;
			sector = nextSector;
			part++;
		}

		return data;
	}

	public synchronized int getSectorCount() throws IOException {
		return (int) (this.dataFile.length() / SECTOR_SIZE);
	}

	public synchronized void close() throws IOException {
		this.dataFile.close();
		for (AccessFile f : indices.values()) {
			f.close();
		}
	}

	public class AccessFile extends RandomAccessFile {

		public AccessFile(File f) throws FileNotFoundException {
			super(f, "r");
		}

		public final int readTriByte() throws IOException {
			int j = read();
			int k = read();
			int l = read();
			if ((j | k | l) < 0) {
				throw new EOFException();
			}
			return ((j << 16) + (k << 8) + (l << 0));
		}

	}
}
