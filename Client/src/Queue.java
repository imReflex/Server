final class Queue {

	public Queue() {
		head = new QueueNode();
		head.next = head;
		head.previous = head;
	}

	public void insertBack(QueueNode node) {
		if (node.previous != null)
			node.unlinkQueue();
		node.previous = head.previous;
		node.next = head;
		node.previous.next = node;
		node.next.previous = node;
	}

	public QueueNode popFront() {
		QueueNode entry = head.next;
		if (entry == head) {
			return null;
		} else {
			entry.unlinkQueue();
			return entry;
		}
	}

	public QueueNode getFront() {
		QueueNode entry = head.next;
		if (entry == head) {
			current = null;
			return null;
		} else {
			current = entry.next;
			return entry;
		}
	}

	public QueueNode getNext() {
		QueueNode entry = current;
		if (entry == head) {
			current = null;
			return null;
		} else {
			current = entry.next;
			return entry;
		}
	}

	public int getSize() {
		int i = 0;
		for (QueueNode nodeSub = head.next; nodeSub != head; nodeSub = nodeSub.next)
			i++;

		return i;
	}

	private final QueueNode head;
	private QueueNode current;
}
