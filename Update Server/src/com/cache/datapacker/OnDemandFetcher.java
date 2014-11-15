package com.cache.datapacker;

import java.io.*;

import java.net.Socket;
import java.util.zip.CRC32;
import com.cache.datapacker.sign.*;

public class OnDemandFetcher extends OnDemandFetcherParent
    implements Runnable
{
	/**
	 * Grabs the checksum of a file from the cache.
	 * @param type The type of file (0 = model, 1 = anim, 2 = midi, 3 = map, 4 = image, 5 = texture).
	 * @param id The id of the file.
	 * @return
	 */
	String[] names = {"model", "anim", "midi", "map", "image", "texture"};
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
		for(int i = 0; i < 6; i++) {
			writeChecksumList(i);
			System.out.println("Written: "+names[i]);
		}
		System.exit(1);
	}
	private int getChecksum(int type, int id) {
		int crc = -1;
		byte[] data = clientInstance.fileStores[type + 1].decompress(id);
		if (data != null) {
			int length = data.length - 2;
			crc32.reset();
			crc32.update(data, 0, length);
			crc = (int) crc32.getValue();
		}
		return crc;
	}
	private void writeChecksumList(int type) {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(signlink.findcachedir() + names[type] + "_crc.dat"));
			for (int index = 0; index < clientInstance.fileStores[type + 1].getFileCount(); index++) {
				out.writeInt(getChecksum(type, index));
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    private final void readData()
    {
        try
        {
            int readAbleBytes = inputStream.available();
            if(expectedSize == 0 && readAbleBytes >= 9)
            {
                waiting = true;
                for(int k = 0; k < 9; k += inputStream.read(ioBuffer, k, 9 - k));
                int receivedType = ioBuffer[0] & 0xff;
                int receivedID = ((ioBuffer[1] & 0xff) << 8) + (ioBuffer[2] & 0xff);
                int receivedSize = ((ioBuffer[3] & 0xff) << 32) + ((ioBuffer[4] & 0xff) << 16) + ((ioBuffer[5] & 0xff) << 8) + (ioBuffer[6] & 0xff);
                int receivedChunkId = ((ioBuffer[7] & 0xff) << 8) + (ioBuffer[8] & 0xff);
                current = null;
                for(OnDemandData ondemandData = (OnDemandData)requested.getFront(); ondemandData != null; ondemandData = (OnDemandData)requested.getNext())
                {
                    if(ondemandData.dataType == receivedType && ondemandData.ID == receivedID)
                        current = ondemandData;
                    if(current != null)
                        ondemandData.loopCycle = 0;
                }
                if(current != null)
                {
                    loopCycle = 0;
                    if(receivedSize == 0)
                    {
                        current.buffer = null;
                        if(current.incomplete)
                            synchronized(zippedNodes)
                            {
                                zippedNodes.insertBack(current);
                            }
                        else
                            current.unlink();
                        current = null;
                    } else
                    {
                        if(current.buffer == null && receivedChunkId == 0)
                            current.buffer = new byte[receivedSize];
                        if(current.buffer == null && receivedChunkId != 0)
                            throw new IOException("missing start of file");
                    }
                }
                completedSize = receivedChunkId * 500;
                expectedSize = 500;
                if(expectedSize > receivedSize - receivedChunkId * 500)
                    expectedSize = receivedSize - receivedChunkId * 500;
            }
            if(expectedSize > 0 && readAbleBytes >= expectedSize)
            {
                waiting = true;
                byte receivedData[] = ioBuffer;
                int i1 = 0;
                if(current != null)
                {
                    receivedData = current.buffer;
                    i1 = completedSize;
                }
                for(int k1 = 0; k1 < expectedSize; k1 += inputStream.read(receivedData, k1 + i1, expectedSize - k1));
                if(expectedSize + completedSize >= receivedData.length && current != null)
                {
                    if(clientInstance.fileStores[0] != null)
                        clientInstance.fileStores[current.dataType + 1].put(receivedData.length, receivedData, current.ID);
                    if(!current.incomplete && current.dataType == 3)
                    {
                        current.incomplete = true;
                        current.dataType = 93;
                    }
                    if(current.incomplete)
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
	
    final void start(Client client)
    {
            
        clientInstance = client;
        running = true;
        clientInstance.startThread(this, 2);
    }
    private final void closeRequest(OnDemandData onDemandData)
    {
        try
        {
            if(socket == null)
            {
                long l = System.currentTimeMillis();
                if(l - openSocketTime < 4000L)
                    return;
                openSocketTime = l;
                socket = clientInstance.openSocket(43596);
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                outputStream.write(15);
                for(int j = 0; j < 8; j++)
                    inputStream.read();

                loopCycle = 0;
            }
            ioBuffer[0] = (byte)onDemandData.dataType;
            ioBuffer[1] = (byte)(onDemandData.ID >> 8);
            ioBuffer[2] = (byte)onDemandData.ID;
            if(onDemandData.incomplete)
                ioBuffer[3] = 2;
            else
                ioBuffer[3] = 1;
            outputStream.write(ioBuffer, 0, 4);
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

    private final void loadToCache(int type, int id)
    {
        if(type < 0 || id < 0)
            return;
        synchronized(queue)
        {
            for(OnDemandData onDemandData_1 = (OnDemandData)queue.getFront(); onDemandData_1 != null; onDemandData_1 = (OnDemandData)queue.getNext())
                if(onDemandData_1.dataType == type && onDemandData_1.ID == id)
                    return;

            OnDemandData onDemandData_2 = new OnDemandData();
            onDemandData_2.dataType = type;
            onDemandData_2.ID = id;
            onDemandData_2.incomplete = true;
            synchronized(nodeList_3)
            {
                nodeList_3.insertBack(onDemandData_2);
            }
            queue.insertBack(onDemandData_2);
        }
    }

    public final void run()
    {
        try
        {
            while(running) 
            {
                int i = 20;
                if(filesToGet == 0 && clientInstance.fileStores[0] != null)
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
                    if(uncompletedCount == 0 && ptr >= 5)
                        break;
                    getExtraFiles();
                    if(inputStream != null)
                        readData();
                }

                boolean flag = false;
                for(OnDemandData onDemandData = (OnDemandData)requested.getFront(); onDemandData != null; onDemandData = (OnDemandData)requested.getNext())
                    if(onDemandData.incomplete)
                    {
                        flag = true;
                        onDemandData.loopCycle++;
                        if(onDemandData.loopCycle > 50)
                        {
                            onDemandData.loopCycle = 0;
                            closeRequest(onDemandData);
                        }
                    }

                if(!flag)
                {
                    for(OnDemandData onDemandData_1 = (OnDemandData)requested.getFront(); onDemandData_1 != null; onDemandData_1 = (OnDemandData)requested.getNext())
                    {
                        flag = true;
                        onDemandData_1.loopCycle++;
                        if(onDemandData_1.loopCycle > 50)
                        {
                            onDemandData_1.loopCycle = 0;
                            closeRequest(onDemandData_1);
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
                        socket = null;
                        inputStream = null;
                        outputStream = null;
                        expectedSize = 0;
                    }
                } else
                {
                    loopCycle = 0;
                    statusString = "";
                }
            }
            return;
        }
        catch(Exception exception)
        {
	    exception.printStackTrace();
        }
    }

    public final void requestArhiveFile(int idx)
    {
        loadToCache(0, idx);
    }


    private final void handleFailed()
    {
        uncompletedCount = 0;
        completedCount = 0;
        for(OnDemandData onDemandData = (OnDemandData)requested.getFront(); onDemandData != null; onDemandData = (OnDemandData)requested.getNext())
            if(onDemandData.incomplete) {
                uncompletedCount++;
           } else {
                completedCount++;
	   }

        while(uncompletedCount < 10) 
        {
        	OnDemandData onDemandData_1 = null;
            onDemandData_1 = (OnDemandData)nodeList_2.popFront();
            if(onDemandData_1 == null)
               	break;
            try {
            	if(fileStatus2[onDemandData_1.dataType][onDemandData_1.ID] != 0)
            		filesLoaded++;
            	fileStatus2[onDemandData_1.dataType][onDemandData_1.ID] = 0;
            	requested.insertBack(onDemandData_1);
            	uncompletedCount++;
            	closeRequest(onDemandData_1);
            	waiting = true;
			} catch(Exception e) { System.out.println(onDemandData_1.dataType+" "+onDemandData_1.ID);}
        }
    }

    private final void checkReceived()
    {
        OnDemandData onDemandData;
        synchronized(nodeList_3)
        {
            onDemandData = (OnDemandData)nodeList_3.popFront();
        }
        while(onDemandData != null) 
        {
            waiting = true;
            byte data[] = null;
            if(clientInstance.fileStores[0] != null)
                data = clientInstance.fileStores[onDemandData.dataType + 1].decompress(onDemandData.ID);
			if(!crcMatches(crcs[onDemandData.dataType][onDemandData.ID], data)) {
				data = null;
			}
            synchronized(nodeList_3)
            {
                if(data == null)
                {
                    nodeList_2.insertBack(onDemandData);
                } else
                {
                    onDemandData.buffer = data;
                    synchronized(zippedNodes)
                    {
                        zippedNodes.insertBack(onDemandData);
                    }
                }
                onDemandData = (OnDemandData)nodeList_3.popFront();
            }
        }
    }

    private final void getExtraFiles()
    {
        while(uncompletedCount == 0 && completedCount < 10) 
        {
            if(filesToGet == 0)
                break;
            OnDemandData onDemandData;
            synchronized(nodeList_1)
            {
                onDemandData = (OnDemandData)nodeList_1.popFront();
            }
            while(onDemandData != null) 
            {
                if(fileStatus2[onDemandData.dataType][onDemandData.ID] != 0)
                {
                    fileStatus2[onDemandData.dataType][onDemandData.ID] = 0;
                    requested.insertBack(onDemandData);
                    closeRequest(onDemandData);
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
                    onDemandData = (OnDemandData)nodeList_1.popFront();
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
                        OnDemandData onDemandData_1 = new OnDemandData();
                        onDemandData_1.dataType = j;
                        onDemandData_1.ID = l;
                        onDemandData_1.incomplete = false;
                        requested.insertBack(onDemandData_1);
                        closeRequest(onDemandData_1);
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

    OnDemandFetcher()
    {
        requested = new Deque();
        statusString = "";
        crc32 = new CRC32();
        ioBuffer = new byte[500];
        fileStatus2 = new byte[5][];
        nodeList_1 = new Deque();
        running = true;
        waiting = false;
        zippedNodes = new Deque();
        queue = new NodeSubList();
        nodeList_2 = new Deque();
        nodeList_3 = new Deque();
		crcs = new int[5][];
    }

    private int totalFiles;
    private Deque requested;
    private int filesToGet;
    private String statusString;
    private long openSocketTime;
    private int objectMapIds[];
    private byte ioBuffer[];
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
    private boolean waiting;
    private Deque zippedNodes;
    private NodeSubList queue;
    private InputStream inputStream;
    private Socket socket;
    private int uncompletedCount;
    private int completedCount;
    private Deque nodeList_2;
    private OnDemandData current;
    private Deque nodeList_3;
    private int regionIds[];
    private int loopCycle;
	private final int[][] crcs;
	private final CRC32 crc32;
}
