import java.util.HashMap;

/**
 * LRU Cache Implementation in Java
 * ===================================
 * Uses a Doubly Linked List + HashMap for O(1) get and put.
 *
 * - HashMap  : key -> node  (O(1) lookup)
 * - DLL      : maintains usage order
 *              head.next = LRU (oldest)
 *              tail.prev = MRU (newest)
 *
 * On GET : move accessed node to tail (MRU end)
 * On PUT : insert new node at tail; if over capacity, evict head.next (LRU)
 */
public class LRUCache {

    // ── Inner Node class ──────────────────────────────────────────────────────

    private static class Node {
        int  key;
        int  value;
        Node prev;
        Node next;

        Node(int key, int value) {
            this.key   = key;
            this.value = value;
        }
    }

    // ── Fields ────────────────────────────────────────────────────────────────

    private final int              capacity;
    private final HashMap<Integer, Node> cache;
    private final Node             head;   // dummy head (LRU side)
    private final Node             tail;   // dummy tail (MRU side)

    // ── Constructor ───────────────────────────────────────────────────────────

    public LRUCache(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be a positive integer");
        }
        this.capacity = capacity;
        this.cache    = new HashMap<>();

        // Sentinel nodes — avoids null checks on every insert/remove
        this.head      = new Node(0, 0);
        this.tail      = new Node(0, 0);
        head.next      = tail;
        tail.prev      = head;
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /** Detach a node from the doubly linked list. */
    private void remove(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    /** Insert a node just before the dummy tail (MRU end). */
    private void insertAtTail(Node node) {
        Node prevNode  = tail.prev;
        prevNode.next  = node;
        node.prev      = prevNode;
        node.next      = tail;
        tail.prev      = node;
    }

    /** Mark a node as most-recently-used. */
    private void moveToTail(Node node) {
        remove(node);
        insertAtTail(node);
    }

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Return value for key if present, else -1.
     * Time  : O(1)
     * Space : O(1)
     */
    public int get(int key) {
        if (!cache.containsKey(key)) return -1;
        Node node = cache.get(key);
        moveToTail(node);           // mark as recently used
        return node.value;
    }

    /**
     * Insert or update key-value pair.
     * Evicts the least-recently-used entry when over capacity.
     * Time  : O(1)
     * Space : O(capacity)
     */
    public void put(int key, int value) {
        if (cache.containsKey(key)) {
            Node node  = cache.get(key);
            node.value = value;
            moveToTail(node);
        } else {
            Node newNode = new Node(key, value);
            cache.put(key, newNode);
            insertAtTail(newNode);

            if (cache.size() > capacity) {
                // Evict LRU node (first real node after dummy head)
                Node lru = head.next;
                remove(lru);
                cache.remove(lru.key);
            }
        }
    }

    /** Return current number of entries in the cache. */
    public int size() {
        return cache.size();
    }

    /** Print cache contents from LRU to MRU. */
    public void display() {
        StringBuilder sb = new StringBuilder("[LRU] ");
        Node curr = head.next;
        while (curr != tail) {
            sb.append(curr.key).append(":").append(curr.value);
            if (curr.next != tail) sb.append(" <-> ");
            curr = curr.next;
        }
        sb.append(" [MRU]");
        System.out.println(sb);
    }

    @Override
    public String toString() {
        return "LRUCache(capacity=" + capacity + ", size=" + cache.size() + ")";
    }

    // ── Demo / Tests ──────────────────────────────────────────────────────────

    public static void main(String[] args) {

        System.out.println("=".repeat(55));
        System.out.println("  LRU Cache Demo");
        System.out.println("=".repeat(55));

        // ── Basic example ─────────────────────────────────────
        System.out.println("\n── Basic Operations (capacity=3) ──");
        LRUCache cache = new LRUCache(3);

        cache.put(1, 10);
        cache.put(2, 20);
        cache.put(3, 30);
        cache.display();                    // [LRU] 1:10 <-> 2:20 <-> 3:30 [MRU]

        System.out.println(cache.get(1));   // 10  (1 moves to MRU)
        cache.display();                    // [LRU] 2:20 <-> 3:30 <-> 1:10 [MRU]

        cache.put(4, 40);                   // evicts 2 (LRU)
        cache.display();                    // [LRU] 3:30 <-> 1:10 <-> 4:40 [MRU]

        System.out.println(cache.get(2));   // -1 (evicted)
        System.out.println(cache.get(3));   // 30

        // ── Update existing key ───────────────────────────────
        System.out.println("\n── Update Existing Key ──");
        cache.put(1, 100);                  // update key 1
        cache.display();                    // 1 should be at MRU end

        // ── LeetCode-style test ───────────────────────────────
        System.out.println("\n── LeetCode Test Case ──");
        LRUCache lc = new LRUCache(2);
        lc.put(1, 1);
        lc.put(2, 2);
        System.out.println(lc.get(1));      // 1
        lc.put(3, 3);                       // evicts key 2
        System.out.println(lc.get(2));      // -1 (evicted)
        lc.put(4, 4);                       // evicts key 1
        System.out.println(lc.get(1));      // -1 (evicted)
        System.out.println(lc.get(3));      // 3
        System.out.println(lc.get(4));      // 4

        // ── Capacity 1 edge case ──────────────────────────────
        System.out.println("\n── Edge Case: Capacity 1 ──");
        LRUCache ec = new LRUCache(1);
        ec.put(1, 10);
        ec.put(2, 20);                      // evicts 1
        System.out.println(ec.get(1));      // -1
        System.out.println(ec.get(2));      // 20

        // ── Final state ───────────────────────────────────────
        System.out.println("\n── Final cache state ──");
        System.out.println(cache);          // LRUCache(capacity=3, size=3)
    }
}
