import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.zip.CRC32;
import java.util.zip.GZIPInputStream;

import javax.swing.JOptionPane;

import sign.signlink;

public class OnDemandFetcher extends OnDemandFetcherParent
    implements Runnable
{
	/**
	 * Grabs the checksum of a file from the cache.
	 * @param type The type of file (0 = model, 1 = anim, 2 = midi, 3 = map).
	 * @param id The id of the file.
	 * @return
	 */
	private boolean crcMatches(int expectedValue, byte crcData[])
	{
		if(crcData == null || crcData.length < 2)
			return false;
		int length = crcData.length - 2;
		crc32.reset();
		crc32.update(crcData, 0, length);
		int crcValue = (int) crc32.getValue();
		return crcValue == expectedValue;
	}
	public void writeAll() {
		for(int i = 0; i < 5; i++) {
			writeChecksumList(i);
			writeVersionList(i);
		}
	}
	public int getChecksum(int type, int id) {
		int crc = -1;
		byte[] data = clientInstance.cacheIndices[type + 1].get(id);
		if (data != null) {
			int length = data.length - 2;
			crc32.reset();
			crc32.update(data, 0, length);
			crc = (int) crc32.getValue();
		}
		return crc;
	}
	public int getVersion(int type, int id) {
		int version = -1;
		byte[] data = clientInstance.cacheIndices[type + 1].get(id);
		if (data != null) {
			int length = data.length - 2;
			version = ((data[length] & 0xff) << 8) + (data[length + 1] & 0xff);
		}
		return version;
	}
	public void writeChecksumList(int type) {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(signlink.findcachedir() + type + "_crc.dat"));
			for (int index = 0; index < clientInstance.cacheIndices[type + 1].getFileCount(); index++) {
				out.writeInt(getChecksum(type, index));
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void writeVersionList(int type) {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(signlink.findcachedir() + type + "_version.dat"));
			for (int index = 0; index < clientInstance.cacheIndices[type + 1].getFileCount(); index++) {
				out.writeShort(getVersion(type, index));
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void writeDWord(DataOutputStream dos, int i) {
		try {
			dos.writeByte((byte) (i >> 24));
			dos.writeByte((byte) (i >> 16));
			dos.writeByte((byte) (i >> 8));	
			dos.writeByte((byte) i);				
		} catch(IOException ioe) {
		}
	}
	
    private final void readData()
    {
        try
        {
            int readAbleBytes = inputStream.available();
            if(expectedSize == 0 && readAbleBytes >= 11)
            {
                waiting = true;
                for(int k = 0; k < 11; k += inputStream.read(ioBuffer, k, 11 - k));
                int receivedType = ioBuffer[0] & 0xff;
                int receivedID = ((ioBuffer[1] & 0xff) << 24) + ((ioBuffer[2] & 0xff) << 16) + ((ioBuffer[3] & 0xff) << 8) + (ioBuffer[4] & 0xff);
                int receivedSize = ((ioBuffer[5] & 0xff) << 32) + ((ioBuffer[6] & 0xff) << 16) + ((ioBuffer[7] & 0xff) << 8) + (ioBuffer[8] & 0xff);
                int chunkId = ((ioBuffer[9] & 0xff) << 8) + (ioBuffer[10] & 0xff);
                current = null;
                for(OnDemandRequest OnDemandRequest = (OnDemandRequest)requested.getFront(); OnDemandRequest != null; OnDemandRequest = (OnDemandRequest)requested.getNext())
                {
                    if(OnDemandRequest.dataType == receivedType && OnDemandRequest.id == receivedID)
                        current = OnDemandRequest;
                    if(current != null)
                        OnDemandRequest.loopCycle = 0;
                }
                if(current != null)
                {
                    loopCycle = 0;
                    if(receivedSize == 0)
                    {
                        signlink.reporterror("Rej: " + receivedType + "," + receivedID);
                        current.buffer = null;
                        if(current.isNotExtraFile)
                            synchronized(zippedNodes)
                            {
                                zippedNodes.insertBack(current);
                            }
                        else
                            current.unlink();
                        current = null;
                    } else
                    {
                        if(current.buffer == null && chunkId == 0)
                            current.buffer = new byte[receivedSize];
                        if(current.buffer == null && chunkId != 0)
                            throw new IOException("missing start of file");
                    }
                }
                completedSize = chunkId * 500;
                expectedSize = 500;
                if(expectedSize > receivedSize - chunkId * 500)
                    expectedSize = receivedSize - chunkId * 500;
            }
            if(expectedSize > 0 && readAbleBytes >= expectedSize)
            {
                waiting = true;
                byte receivedData[] = ioBuffer;
                int tempCompSize = 0;
                if(current != null)
                {
                    receivedData = current.buffer;
                    tempCompSize = completedSize;
                }
                for(int dataReadIdx = 0; dataReadIdx < expectedSize; dataReadIdx += inputStream.read(receivedData, dataReadIdx + tempCompSize, expectedSize - dataReadIdx));
                if(expectedSize + completedSize >= receivedData.length && current != null)
                {
                    //if(clientInstance.cacheIndices[0] != null)
                        clientInstance.cacheIndices[current.dataType + 1].put(receivedData.length, receivedData, current.id);
                    if(!current.isNotExtraFile && current.dataType == 3)
                    {
                        current.isNotExtraFile = true;
                        current.dataType = 93;
                    }
                    if(current.isNotExtraFile)
                        synchronized(zippedNodes)
                        {
                            zippedNodes.insertBack(current);
                        }
                    else
                        current.unlink();
                }
                expectedSize = 0;
                return;
            }
        }
        catch(IOException ioexception)
        {
            try
            {
                socket.close();
            }
            catch(Exception _ex) { }
            socket = null;
            inputStream = null;
            outputStream = null;
            expectedSize = 0;
        }
    }
	
    public final void start(CacheArchive streamLoader, Client client)
    {
		String crcNames[] = {
				"model_crc", "anim_crc", "midi_crc", "map_crc", "image_crc", "texture_crc"
		};
		for(int cachePtr = 0; cachePtr < 6; cachePtr++)
		{
			byte crcFile[] = streamLoader.getDataForName(crcNames[cachePtr]);
			int length = 0;
			//if(crcFile != null) {
				length = crcFile.length / 4;
				if(cachePtr == 0)
					System.out.println("Model CRC length: " + length);
				Stream crcStream = new Stream(crcFile);
				crcs[cachePtr] = new int[length];
				fileStatus2[cachePtr] = new byte[length];
				for(int ptr = 0; ptr < length; ptr++)
					crcs[cachePtr][ptr] = crcStream.readDWord();
			//}
		}
        byte modelIndex[] = streamLoader.getDataForName("map_index");
        Stream data = new Stream(modelIndex);
        int mapCount = data.readUnsignedWord();
        regionIds = new int[mapCount];
        landscapeIds = new int[mapCount];
        objectMapIds = new int[mapCount];
        regionIsMembers = new int[mapCount];
		int[] dntUse = new int[] {5181, 5182, 5183, 5184, 5180, 5179, 5175, 5176, 4014, 3997, 5314, 5315, 5172};
        for(int i2 = 0; i2 < mapCount; i2++)
        {
            regionIds[i2] = data.readUnsignedWord();
            landscapeIds[i2] = data.readUnsignedWord();
            objectMapIds[i2] = data.readUnsignedWord();
			for(int i : dntUse)
			{
				if(landscapeIds[i2] == i)
					landscapeIds[i2] = -1;
				if(objectMapIds[i2] == i)
					objectMapIds[i2] = -1;
			}
        }
        modelIndex = streamLoader.getDataForName("model_index");
		int j1 = crcs[0].length;
		modelIndices = new byte[j1];
		for (int k1 = 0; k1 < j1; k1++)
			if (k1 < modelIndex.length)
				modelIndices[k1] = modelIndex[k1];
			else
				modelIndices[k1] = 0;

        modelIndex = streamLoader.getDataForName("midi_index");
        data = new Stream(modelIndex);
        mapCount = modelIndex.length;
        midiIndices = new int[mapCount];
        for(int k2 = 0; k2 < mapCount; k2++)
            midiIndices[k2] = data.readUnsignedByte();

        clientInstance = client;
        running = true;
        clientInstance.startRunnable(this, 2);
       // writeAll();
    }

    public final int getNodeCount()
    {
        synchronized(queue)
        {
            return queue.getSize();
        }
    }
	public int getImageCount()
	{
		return crcs[4].length;
	}
	public int getModelIndex(int id) {
		return modelIndices[id] & 0xff;
	}
    public final void dispose()
    {
        running = false;
    }

    public final void fetchMaps(boolean isMember)
    {
        int length = regionIds.length;
        for(int mapPtr = 0; mapPtr < length; mapPtr++)
            if(isMember || regionIsMembers[mapPtr] != 0)
            {
            	if(objectMapIds[mapPtr] == -1 || landscapeIds[mapPtr] == -1)
            		continue;
            	if(!clientInstance.loggedIn) {
            		
            		fetchPriorityFile((byte)2, 3, objectMapIds[mapPtr]);
                	fetchPriorityFile((byte)2, 3, landscapeIds[mapPtr]);
            	}
            }

    }

    public final int getVersionCount(int idx)
    {
    	return versions[idx].length;
    }


    public final int getModelCount()
    {
    	return 65534;
    }
    private final void closeRequest(OnDemandRequest OnDemandRequest)
    {
        try
        {
            if(socket == null)
            {
                long l = System.currentTimeMillis();
                if(l - openSocketTime < 4000L)
                    return;
                openSocketTime = l;
                socket = clientInstance.openSocket(43596, Configuration.jaggrab);
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                outputStream.write(15);
                for(int j = 0; j < 8; j++)
                    inputStream.read();

                loopCycle = 0;
            }
            ioBuffer[0] = (byte)OnDemandRequest.dataType;
            ioBuffer[1] = (byte)(OnDemandRequest.id >> 24);
            ioBuffer[2] = (byte)(OnDemandRequest.id >> 16);
            ioBuffer[3] = (byte)(OnDemandRequest.id >> 8);
            ioBuffer[4] = (byte)OnDemandRequest.id;
            if(OnDemandRequest.isNotExtraFile)
                ioBuffer[5] = 1;
            else
            if(!clientInstance.loggedIn)
                ioBuffer[5] = 1;
            else
                ioBuffer[5] = 0;
            outputStream.write(ioBuffer, 0, 6);
            writeLoopCycle = 0;
            return;
        }
        catch(IOException ioexception) { }
        try
        {
            socket.close();
        }
        catch(Exception _ex) { }
        socket = null;
        inputStream = null;
        outputStream = null;
        expectedSize = 0;
    }

    public final int getAnimCount()
    {
        return 33568;
    }

    public final void requestFileData(int type, int id)
    {
        if(type < 0 || id < 0)
            return;
        synchronized(queue)
        {
            for(OnDemandRequest OnDemandRequest_1 = (OnDemandRequest)queue.getFront(); OnDemandRequest_1 != null; OnDemandRequest_1 = (OnDemandRequest)queue.getNext())
                if(OnDemandRequest_1.dataType == type && OnDemandRequest_1.id == id)
                    return;

            OnDemandRequest OnDemandRequest_2 = new OnDemandRequest();
            OnDemandRequest_2.dataType = type;
            OnDemandRequest_2.id = id;
            OnDemandRequest_2.isNotExtraFile = true;
            synchronized(fetchFromCacheList)
            {
                fetchFromCacheList.insertBack(OnDemandRequest_2);
            }
            queue.insertBack(OnDemandRequest_2);
        }
    }

    public final void run()
    {
        try
        {
            while(running) 
            {
                onDemandCycle++;
                int i = 20;
                if(filesToGet == 0 && clientInstance.cacheIndices[0] != null)
                    i = 50;
                try
                {
                    Thread.sleep(i);
                }
                catch(Exception _ex) { }
                waiting = true;
                for(int ptr = 0; ptr < 100; ptr++)
                {
                    if(!waiting)
                        break;
                    waiting = false;
                    checkReceived();
                    handleFailed();
                    if(extraFileCount == 0 && ptr >= 5)
                        break;
                    getExtraFiles();
                    if(inputStream != null)
                        readData();
                }

                boolean flag = false;
                for(OnDemandRequest OnDemandRequest = (OnDemandRequest)requested.getFront(); OnDemandRequest != null; OnDemandRequest = (OnDemandRequest)requested.getNext())
                    if(OnDemandRequest.isNotExtraFile)
                    {
                        flag = true;
                        OnDemandRequest.loopCycle++;
                        if(OnDemandRequest.loopCycle > 50)
                        {
                            OnDemandRequest.loopCycle = 0;
                            closeRequest(OnDemandRequest);
                        }
                    }

                if(!flag)
                {
                    for(OnDemandRequest OnDemandRequest_1 = (OnDemandRequest)requested.getFront(); OnDemandRequest_1 != null; OnDemandRequest_1 = (OnDemandRequest)requested.getNext())
                    {
                        flag = true;
                        OnDemandRequest_1.loopCycle++;
                        if(OnDemandRequest_1.loopCycle > 50)
                        {
                            OnDemandRequest_1.loopCycle = 0;
                            closeRequest(OnDemandRequest_1);
                        }
                    }

                }
                if(flag)
                {
                    loopCycle++;
                    if(loopCycle > 750)
                    {
                        try
                        {
                            socket.close();
                        }
                        catch(Exception _ex) { }
                        OnDemandRequest OnDemandRequest_1 = (OnDemandRequest)requested.getFront();
                        JOptionPane.showMessageDialog(null, "Please report this to the RevolutionX report a bug section \n "+OnDemandRequest_1+"");
                        socket = null;
                        inputStream = null;
                        outputStream = null;
                        expectedSize = 0;
                        requested.popFront();
                        
                    }
                } else
                {
                    loopCycle = 0;
                    statusString = "";
                }
                if(clientInstance.loggedIn && socket != null && outputStream != null && (filesToGet > 0 || clientInstance.cacheIndices[0] == null))
                {
                    writeLoopCycle++;
                    if(writeLoopCycle > 500)
                    {
                        writeLoopCycle = 0;
                        ioBuffer[0] = 0;
                        ioBuffer[1] = 0;
                        ioBuffer[2] = 0;
                        ioBuffer[3] = 10;
                        try
                        {
                            outputStream.write(ioBuffer, 0, 4);
                        }
                        catch(IOException _ex)
                        {
                            loopCycle = 5000;
                        }
                    }
                }
            }
            return;
        }
        catch(Exception exception)
        {
	    exception.printStackTrace();
            signlink.reporterror("od_ex " + exception.getMessage());
        }
    }

    public final void insertExtraFilesRequest(int fileId, int idx)
    {
        if(clientInstance.cacheIndices[0] == null)
            return;
		if(fileStatus2[idx][fileId] == 0)
			return;
        if(filesToGet == 0)
            return;
        OnDemandRequest OnDemandRequest = new OnDemandRequest();
        OnDemandRequest.dataType = idx;
        OnDemandRequest.id = fileId;
        OnDemandRequest.isNotExtraFile = false;
        synchronized(nodeList_1)
        {
            nodeList_1.insertBack(OnDemandRequest);
        }
    }

    public final OnDemandRequest getNextNode()
    {
        OnDemandRequest OnDemandRequest;
        synchronized(zippedNodes)
        {
            OnDemandRequest = (OnDemandRequest)zippedNodes.popFront();
        }
        if(OnDemandRequest == null)
            return null;
        synchronized(queue)
        {
            OnDemandRequest.unlinkQueue();
        }
        if(OnDemandRequest.buffer == null)
            return OnDemandRequest;
        int readData = 0;
        try
        {
            GZIPInputStream gzipinputstream = new GZIPInputStream(new ByteArrayInputStream(OnDemandRequest.buffer));
            do
            {
                if(readData >= gzipInputBuffer.length) {
                	System.out.println("File: "+OnDemandRequest.id+" too large");
                    break;
            	}
                int tempReadData = -1;
                try {
                	tempReadData = gzipinputstream.read(gzipInputBuffer, readData, gzipInputBuffer.length - readData);
                } catch(Exception e) { e.printStackTrace();}
                if(tempReadData == -1)
                    break;
                readData += tempReadData;
            } while(true);
        }
        catch(IOException _ex)
        {
        	_ex.printStackTrace();
            throw new RuntimeException("error unzipping");
        }
        OnDemandRequest.buffer = new byte[readData];
        for(int bufferPtr = 0; bufferPtr < readData; bufferPtr++)
            OnDemandRequest.buffer[bufferPtr] = gzipInputBuffer[bufferPtr];

        return OnDemandRequest;
    }

    public final int getMapIdForRegions(int landscapeOrObject, int regionY, int regionX)
    {
        int regionId = (regionX << 8) + regionY;
        for(int j1 = 0; j1 < regionIds.length; j1++)
            if(regionIds[j1] == regionId) {
                if(landscapeOrObject == 0) {
                	return landscapeIds[j1];
                } else {
                    return objectMapIds[j1];
                }
            }
        return -1;
    }
    public final void get(int idx)
    {
    	requestFileData(0, idx);
    }

    public final void fetchPriorityFile(byte fileData, int idx, int fileId)
    {
        if(clientInstance.cacheIndices[0] == null)
            return;
        byte data[] = clientInstance.cacheIndices[idx + 1].get(fileId);
		if(crcMatches(crcs[idx][fileId], data))
			return;
        fileStatus2[idx][fileId] = fileData;
        if(fileData > filesToGet)
            filesToGet = fileData;
        totalFiles++;
    }

    public final boolean mapIsObjectMap(int obectMap)
    {
        for(int ptr = 0; ptr < regionIds.length; ptr++)
            if(objectMapIds[ptr] == obectMap)
                return true;

        return false;
    }

    private final void handleFailed()
    {
        extraFileCount = 0;
        completedCount = 0;
        for(OnDemandRequest OnDemandRequest = (OnDemandRequest)requested.getFront(); OnDemandRequest != null; OnDemandRequest = (OnDemandRequest)requested.getNext())
            if(OnDemandRequest.isNotExtraFile) {
                extraFileCount++;
           } else {
                completedCount++;
	   }

        while(extraFileCount < 10) 
        {
        	OnDemandRequest OnDemandRequest_1 = null;
            OnDemandRequest_1 = (OnDemandRequest)receivedFilesList.popFront();
            if(OnDemandRequest_1 == null)
               	break;
            try {
            	if(fileStatus2[OnDemandRequest_1.dataType][OnDemandRequest_1.id] != 0)
            		filesLoaded++;
            	fileStatus2[OnDemandRequest_1.dataType][OnDemandRequest_1.id] = 0;
            	requested.insertBack(OnDemandRequest_1);
            	extraFileCount++;
            	closeRequest(OnDemandRequest_1);
            	waiting = true;
			} catch(Exception e) { System.out.println(OnDemandRequest_1.dataType+" "+OnDemandRequest_1.id);}
        }
    }

    public final void clearExtraFilesList()
    {
        synchronized(nodeList_1)
        {
            nodeList_1.clear();
        }
    }

    private final void checkReceived()
    {
        OnDemandRequest OnDemandRequest;
        synchronized(fetchFromCacheList)
        {
            OnDemandRequest = (OnDemandRequest)fetchFromCacheList.popFront();
        }
        while(OnDemandRequest != null) 
        {
            waiting = true;
            byte data[] = null;
            if(clientInstance.cacheIndices[0] != null)
                data = clientInstance.cacheIndices[OnDemandRequest.dataType + 1].get(OnDemandRequest.id);
			if(!crcMatches(crcs[OnDemandRequest.dataType][OnDemandRequest.id], data)) {
				data = null;
			}
            synchronized(fetchFromCacheList)
            {
                if(data == null)
                {
                    receivedFilesList.insertBack(OnDemandRequest);
                } else
                {
                    OnDemandRequest.buffer = data;
                    synchronized(zippedNodes)
                    {
                        zippedNodes.insertBack(OnDemandRequest);
                    }
                }
                OnDemandRequest = (OnDemandRequest)fetchFromCacheList.popFront();
            }
        }
    }

    private final void getExtraFiles()
    {
        while(extraFileCount == 0 && completedCount < 10) 
        {
            if(filesToGet == 0)
                break;
            OnDemandRequest OnDemandRequest;
            synchronized(nodeList_1)
            {
                OnDemandRequest = (OnDemandRequest)nodeList_1.popFront();
            }
            while(OnDemandRequest != null) 
            {
                if(fileStatus2[OnDemandRequest.dataType][OnDemandRequest.id] != 0)
                {
                    fileStatus2[OnDemandRequest.dataType][OnDemandRequest.id] = 0;
                    requested.insertBack(OnDemandRequest);
                    closeRequest(OnDemandRequest);
                    waiting = true;
                    if(filesLoaded < totalFiles)
                        filesLoaded++;
                    statusString = "Loading extra files - " + (filesLoaded * 100) / totalFiles + "%";
                    System.out.println(statusString);
                    completedCount++;
                    if(completedCount == 10)
                        return;
                }
                synchronized(nodeList_1)
                {
                    OnDemandRequest = (OnDemandRequest)nodeList_1.popFront();
                }
            }
            for(int j = 0; j < 5; j++)
            {
                byte fileData[] = fileStatus2[j];
                int k = fileData.length;
                for(int l = 0; l < k; l++)
                    if(fileData[l] == filesToGet)
                    {
                        fileData[l] = 0;
                        OnDemandRequest OnDemandRequest_1 = new OnDemandRequest();
                        OnDemandRequest_1.dataType = j;
                        OnDemandRequest_1.id = l;
                        OnDemandRequest_1.isNotExtraFile = false;
                        requested.insertBack(OnDemandRequest_1);
                        closeRequest(OnDemandRequest_1);
                        waiting = true;
                        if(filesLoaded < totalFiles)
                            filesLoaded++;
                        statusString = "Loading extra files - " + (filesLoaded * 100) / totalFiles + "%";
                        completedCount++;
                        if(completedCount == 10)
                            return;
                    }

            }

            filesToGet--;
        }
    }

    public final boolean midiExists(int i)
    {
        return midiIndices[i] == 1;
    }

    public OnDemandFetcher()
    {
        requested = new Deque();
        statusString = "";
        crc32 = new CRC32();
        ioBuffer = new byte[500];
        fileStatus2 = new byte[7][];
        nodeList_1 = new Deque();
        running = true;
        waiting = false;
        zippedNodes = new Deque();
        gzipInputBuffer = new byte[1000000];
        queue = new Queue();
        versions = new int[7][];
        receivedFilesList = new Deque();
        fetchFromCacheList = new Deque();
		crcs = new int[6][];
    }
	public int getRemaining() {
		synchronized (queue) {
			return queue.getSize();
		}
	}
    private int totalFiles;
    private Deque requested;
    private int filesToGet;
    public String statusString;
    private int writeLoopCycle;
    private long openSocketTime;
    private int objectMapIds[];
    private byte ioBuffer[];
    public int onDemandCycle;
    private byte fileStatus2[][];
    private Client clientInstance;
    private Deque nodeList_1;
    private int completedSize;
    private int expectedSize;
    private int midiIndices[];
    private int landscapeIds[];
    private int filesLoaded;
    private boolean running;
    private OutputStream outputStream;
    private int regionIsMembers[];
    private boolean waiting;
    private Deque zippedNodes;
    private byte gzipInputBuffer[];
    private Queue queue;
    private InputStream inputStream;
    private Socket socket;
    private int versions[][];
    private int extraFileCount;
    private int completedCount;
    private Deque receivedFilesList;
    private OnDemandRequest current;
    private Deque fetchFromCacheList;
    private int regionIds[];
    private int loopCycle;
	private final int[][] crcs;
	private byte[] modelIndices;
	private final CRC32 crc32;
}
