public class Node
{

    public void unlink()
    {
        if(prev == null)
        {
            return;
        } else
        {
            prev.next = next;
            next.prev = prev;
            next = null;
            prev = null;
            return;
        }
    }

    public Node()
    {
    }

    public long hash;
    public Node next;
    public Node prev;
}
