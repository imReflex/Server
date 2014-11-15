public final class Deque
{

    public Deque()
    {
        head = new Node();
        head.next = head;
        head.prev = head;
    }

    public void insertBack(Node node)
    {
        if(node.prev != null)
            node.unlink();
        node.prev = head.prev;
        node.next = head;
        node.prev.next = node;
        node.next.prev = node;
    }

    public void insertFront(Node node)
    {
        if(node.prev != null)
            node.unlink();
        node.prev = head;
        node.next = head.next;
        node.prev.next = node;
        node.next.prev = node;
    }

    public Node popFront()
    {
        Node node = head.next;
        if(node == head)
        {
            return null;
        } else
        {
            node.unlink();
            return node;
        }
    }

    public Node getFront()
    {
        Node node = head.next;
        if(node == head)
        {
            current = null;
            return null;
        } else
        {
            current = node.next;
            return node;
        }
    }

    public Node getBack()
    {
        Node node = head.prev;
        if(node == head)
        {
            current = null;
            return null;
        } else
        {
            current = node.prev;
            return node;
        }
    }

    public Node getNext()
    {
        Node node = current;
        if(node == head)
        {
            current = null;
            return null;
        } else
        {
            current = node.next;
            return node;
        }
    }

    public Node getPrevious()
    {
        Node node = current;
        if(node == head)
        {
            current = null;
            return null;
        }
        current = node.prev;
        return node;
    }

    public void clear()
    {
        if(head.next == head)
            return;
        do
        {
            Node node = head.next;
            if(node == head)
                return;
            node.unlink();
        } while(true);
    }

    private final Node head;
    private Node current;
}
