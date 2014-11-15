


public class QueueNode extends Node {

    public final void unlinkQueue()
    {
        if(previous == null)
        {
        } else
        {
            previous.next = next;
            next.previous = previous;
            next = null;
            previous = null;
        }
    }

    public QueueNode()
    {
    }

    public QueueNode next;
    public QueueNode previous;
}
