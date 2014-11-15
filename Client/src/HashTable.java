
import sign.signlink;

final class HashTable {

	public HashTable(int size) {
		this.size = size;
		cache = new Node[size];
		for (int ptr = 0; ptr < size; ptr++) {
			Node node = cache[ptr] = new Node();
			node.next = node;
			node.prev = node;
		}

	}

	public Node get(long hash) {
		Node node = cache[(int) (hash & (long) (size - 1))];
		for (Node entry = node.next; entry != node; entry = entry.next)
			if (entry.hash == hash)
				return entry;

		return null;
	}

	public void put(Node entry, long hash) {
		try {
			if (entry.prev != null)
				entry.unlink();
			Node node_1 = cache[(int) (hash & (long) (size - 1))];

			entry.prev = node_1.prev;
			entry.next = node_1;
			entry.prev.next = entry;
			entry.next.prev = entry;
			entry.hash = hash;
			return;
		} catch (RuntimeException runtimeexception) {
			signlink.reporterror("91499, " + entry + ", " + hash + ", "
					+ runtimeexception.toString());
		}
		throw new RuntimeException();
	}

	private final int size;
	private final Node cache[];
}
