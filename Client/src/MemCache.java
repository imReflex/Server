

import sign.signlink;

public final class MemCache {

    public MemCache(int size)
    {
        queue_head = new QueueNode();
        usageList = new Queue();
        max_size = size;
        free_slots = size;
        hashTable = new HashTable(1024);
    }

    public QueueNode get(long hash)
    {
        QueueNode node = (QueueNode) hashTable.get(hash);
        if(node != null)
        {
            usageList.insertBack(node);
        }
        return node;
    }

    public void put(QueueNode entry, long hash)
    {
        try
        {
            if(free_slots == 0)
            {
                QueueNode dropEntry = usageList.popFront();
                dropEntry.unlink();
                dropEntry.unlinkQueue();
                if(dropEntry == queue_head)
                {
                    dropEntry = usageList.popFront();
                    dropEntry.unlink();
                    dropEntry.unlinkQueue();
                }
            } else
            {
                free_slots--;
            }
            hashTable.put(entry, hash);
            usageList.insertBack(entry);
            return;
        }
        catch(RuntimeException runtimeexception)
        {
            signlink.reporterror("47547, " + entry + ", " + hash + ", " + (byte)2 + ", " + runtimeexception.toString());
        }
        throw new RuntimeException();
    }

    public void clear()
    {
        do
        {
            QueueNode node = usageList.popFront();
            if(node != null)
            {
                node.unlink();
                node.unlinkQueue();
            } else
            {
                free_slots = max_size;
                return;
            }
        } while(true);
    }
    public Queue getUsageList()
    {
    	return usageList;
    }
    public HashTable getHashTable()
    {
    	return hashTable;
    }

    
    private final QueueNode queue_head;
    private final int max_size;
    private int free_slots;
    private final HashTable hashTable;
    private final Queue usageList;
}
