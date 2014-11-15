

import java.io.IOException;
import java.io.RandomAccessFile;

final class Decompressor {

	public Decompressor(RandomAccessFile randomaccessfile, RandomAccessFile randomaccessfile1, int j) {
		storeId = j;
		dataFile = randomaccessfile;
		indexFile = randomaccessfile1;
	}
	
	/**
	 * Returns the number of files in the cache index.
	 * @return
	 */
	public long getFileCount() {
		try {
			if (indexFile != null) {
				return (indexFile.length() / 6);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public synchronized byte[] get(int index) {
		try {
			seekTo(indexFile, index * 6);
			int _fileSize;
			for(int indexPart = 0; indexPart < 6; indexPart += _fileSize)
			{
				_fileSize = indexFile.read(buffer, indexPart, 6 - indexPart);
				if(_fileSize == -1)
					return null;
			}
			int fileSize = ((buffer[0] & 0xff) << 16) + ((buffer[1] & 0xff) << 8) + (buffer[2] & 0xff);
			int fileBlock = ((buffer[3] & 0xff) << 16) + ((buffer[4] & 0xff) << 8) + (buffer[5] & 0xff);
			if(fileBlock <= 0 || (long)fileBlock > dataFile.length() / 520L)
				return null;
			byte fileBuffer[] = new byte[fileSize];
			int read = 0;
			for(int cycle = 0; read < fileSize; cycle++) {
				if(fileBlock == 0)
					return null;
				seekTo(dataFile, fileBlock * 520);
				int size = 0;
				int remaining = fileSize - read;
				int nextFileId2 = -1;
				int currentPartId = -1;
				int nextBlockId = -1;
				int nextStoreId = -1;
				if(index > 65535) {
					if(remaining > 510)
						remaining = 510;
					int nextFileId;
					for(; size < remaining + 10; size += nextFileId) {
						nextFileId = dataFile.read(buffer, size, (remaining + 10) - size);
						if(nextFileId == -1)
							return null;
					}
					nextFileId2 = ((buffer[0] & 0xff) << 24) + ((buffer[1] & 0xff) << 16) + ((buffer[2] & 0xff) << 8) + ((buffer[3] & 0xff));
					currentPartId = ((buffer[4] & 0xff) << 8) + (buffer[5] & 0xff);
					nextBlockId = ((buffer[6] & 0xff) << 16) + ((buffer[7] & 0xff) << 8) + (buffer[8] & 0xff);
					nextStoreId = buffer[9] & 0xff;
				} else {
					if(remaining > 512)
						remaining = 512;
					int nextFileId;
					for(; size < remaining + 8; size += nextFileId) {
						nextFileId = dataFile.read(buffer, size, (remaining + 8) - size);
						if(nextFileId == -1)
							return null;
					}
					nextFileId2 = ((buffer[0] & 0xff) << 8) + (buffer[1] & 0xff);
					currentPartId = ((buffer[2] & 0xff) << 8) + (buffer[3] & 0xff);
					nextBlockId = ((buffer[4] & 0xff) << 16) + ((buffer[5] & 0xff) << 8) + (buffer[6] & 0xff);
					nextStoreId = buffer[7] & 0xff;
				}
				if(nextFileId2 != index || currentPartId != cycle || nextStoreId != storeId)
					return null;
				if(nextBlockId < 0 || (long)nextBlockId > dataFile.length() / 520L)
					return null;
				for(int offset = 0; offset < remaining; offset++)
					fileBuffer[read++] = buffer[offset + 8];

				fileBlock = nextBlockId;
			}

			return fileBuffer;
		} catch(IOException _ex) {
			return null;
		}
	}

	public synchronized boolean put(int length, byte data[], int index) {
		boolean entered = put(true, index, length, data);
		if(!entered)
			entered = put(false, index, length, data);
		return entered;
	}

	private synchronized boolean put(boolean exists, int index, int length, byte data[]) {
		try {
			int sector;
			if(exists) {
				seekTo(indexFile, index * 6);
				int len;
				for(int offset = 0; offset < 6; offset += len) {
					len = indexFile.read(buffer, offset, 6 - offset);
					if(len == -1)
						return false;
				}
				sector = ((buffer[3] & 0xff) << 16) + ((buffer[4] & 0xff) << 8) + (buffer[5] & 0xff);
				if(sector <= 0 || (long)sector > dataFile.length() / 520L)
					return false;
			} else {
				sector = (int)((dataFile.length() + 519L) / 520L);
				if(sector == 0)
					sector = 1;
			}
			buffer[0] = (byte)(length >> 16);
			buffer[1] = (byte)(length >> 8);
			buffer[2] = (byte)length;
			buffer[3] = (byte)(sector >> 16);
			buffer[4] = (byte)(sector >> 8);
			buffer[5] = (byte)sector;
			seekTo(indexFile, index * 6);
			indexFile.write(buffer, 0, 6);
			int written = 0;
			for(int zero = 0; written < length; zero++) {
				int nextSector = 0;
				if(exists) 	{
					seekTo(dataFile, sector * 520);
					int currentFile;
					int idx;
					int currentFile2 = -1;
					int currentPart = -1;
					int currentCache = -1;
					if(index > 65535) {
						for(currentFile = 0; currentFile < 10; currentFile += idx) {
							idx = dataFile.read(buffer, currentFile, 10 - currentFile);
							if(idx == -1)
								break;
						}
						if(currentFile == 10) {
							currentFile2 = ((buffer[0] & 0xff) << 24) + ((buffer[1] & 0xff) << 16) + ((buffer[2] & 0xff) << 8) + (buffer[3] & 0xff);
							currentPart = ((buffer[4] & 0xff) << 8) + (buffer[5] & 0xff);
							nextSector = ((buffer[6] & 0xff) << 16) + ((buffer[7] & 0xff) << 8) + (buffer[8] & 0xff);
							currentCache = buffer[9] & 0xff;
						}
					} else {
						for(currentFile = 0; currentFile < 8; currentFile += idx) {
							idx = dataFile.read(buffer, currentFile, 8 - currentFile);
							if(idx == -1)
								break;
						}
						if(currentFile == 8) {
							currentFile2 = ((buffer[0] & 0xff) << 8) + (buffer[1] & 0xff);
							currentPart = ((buffer[2] & 0xff) << 8) + (buffer[3] & 0xff);
							nextSector = ((buffer[4] & 0xff) << 16) + ((buffer[5] & 0xff) << 8) + (buffer[6] & 0xff);
							currentCache = buffer[7] & 0xff;
						}
					}
					if(currentFile2 == -1 || currentPart == -1 || currentCache == -1)
						return false;
					if(currentFile2 != index || currentPart != zero || currentCache != storeId)
						return false;
					if(nextSector < 0 || (long)nextSector > dataFile.length() / 520L)
						return false;
				}
				if(nextSector == 0) {
					exists = false;
					nextSector = (int)((dataFile.length() + 519L) / 520L);
					if(nextSector == 0)
						nextSector++;
					if(nextSector == sector)
						nextSector++;
				}
				if(length - written <= 512)
					nextSector = 0;
				if(index > 65535) {
					buffer[0] = (byte)(index >> 24);
					buffer[1] = (byte)(index >> 16);
					buffer[2] = (byte)(index >> 8);
					buffer[3] = (byte)index;
					buffer[4] = (byte)(zero >> 8);
					buffer[5] = (byte)zero;
					buffer[6] = (byte)(nextSector >> 16);
					buffer[7] = (byte)(nextSector >> 8);
					buffer[8] = (byte)nextSector;
					buffer[9] = (byte)storeId;
					seekTo(dataFile, sector * 520);
					dataFile.write(buffer, 0, 10);
					int remaining = length - written;
					if(remaining > 510)
						remaining = 510;
					dataFile.write(data, written, remaining);
					written += remaining;
				} else {
					buffer[0] = (byte)(index >> 8);
					buffer[1] = (byte)index;
					buffer[2] = (byte)(zero >> 8);
					buffer[3] = (byte)zero;
					buffer[4] = (byte)(nextSector >> 16);
					buffer[5] = (byte)(nextSector >> 8);
					buffer[6] = (byte)nextSector;
					buffer[7] = (byte)storeId;
					seekTo(dataFile, sector * 520);
					dataFile.write(buffer, 0, 8);
					int remaining = length - written;
					if(remaining > 512)
						remaining = 512;
					dataFile.write(data, written, remaining);
					written += remaining;
				}
				sector = nextSector;
			}

			return true;
		} catch(IOException _ex) {
			return false;
		}
	}

	private synchronized void seekTo(RandomAccessFile randomaccessfile, int j) throws IOException {
		try {
			/*if (j < 0 || j > 0x3c00000) {
				System.out.println("Badseek - pos:" + j + " len:" + randomaccessfile.length());
				j = 0x3c00000;
				try {
					Thread.sleep(1000L);
				} catch (Exception _ex) {
				}
			}*/
			randomaccessfile.seek(j);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static final byte[] buffer = new byte[520];
	private final RandomAccessFile dataFile;
	private final RandomAccessFile indexFile;
	private final int storeId;

}
