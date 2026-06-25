"""
LRU Cache Implementation in Python
====================================
Uses a Doubly Linked List + HashMap for O(1) get and put.

- HashMap  : key -> node (for O(1) lookup)
- DLL      : maintains usage order (most recent at tail, LRU at head)

On GET  : move accessed node to tail
On PUT  : insert new node at tail; if over capacity, evict head (LRU)
"""


# ── Node for Doubly Linked List ───────────────────────────────────────────────

class Node:
    def __init__(self, key=0, value=0):
        self.key   = key
        self.value = value
        self.prev  = None
        self.next  = None


# ── LRU Cache ─────────────────────────────────────────────────────────────────

class LRUCache:
    def __init__(self, capacity: int):
        if capacity <= 0:
            raise ValueError("Capacity must be a positive integer")

        self.capacity = capacity
        self.cache    = {}          # key -> Node

        # Sentinel head (LRU end) and tail (MRU end)
        # Avoids edge-case null checks on every insert/delete
        self.head = Node()          # dummy head  (oldest / LRU side)
        self.tail = Node()          # dummy tail  (newest / MRU side)
        self.head.next = self.tail
        self.tail.prev = self.head

    # ── Private helpers ───────────────────────────────────────────────────────

    def _remove(self, node: Node):
        """Detach a node from the doubly linked list."""
        prev_node = node.prev
        next_node = node.next
        prev_node.next = next_node
        next_node.prev = prev_node

    def _insert_at_tail(self, node: Node):
        """Insert a node just before the dummy tail (most-recently-used end)."""
        prev_node      = self.tail.prev
        prev_node.next = node
        node.prev      = prev_node
        node.next      = self.tail
        self.tail.prev = node

    def _move_to_tail(self, node: Node):
        """Mark a node as most-recently-used."""
        self._remove(node)
        self._insert_at_tail(node)

    # ── Public API ────────────────────────────────────────────────────────────

    def get(self, key: int) -> int:
        """
        Return value for key if present, else -1.
        Time  : O(1)
        Space : O(1)
        """
        if key not in self.cache:
            return -1
        node = self.cache[key]
        self._move_to_tail(node)    # mark as recently used
        return node.value

    def put(self, key: int, value: int) -> None:
        """
        Insert or update key-value pair.
        Evicts the least-recently-used entry when over capacity.
        Time  : O(1)
        Space : O(capacity)
        """
        if key in self.cache:
            node = self.cache[key]
            node.value = value
            self._move_to_tail(node)
        else:
            new_node = Node(key, value)
            self.cache[key] = new_node
            self._insert_at_tail(new_node)

            if len(self.cache) > self.capacity:
                # Evict LRU node (first real node after dummy head)
                lru = self.head.next
                self._remove(lru)
                del self.cache[lru.key]

    def display(self) -> None:
        """Print cache contents from LRU to MRU."""
        entries = []
        curr = self.head.next
        while curr != self.tail:
            entries.append(f"{curr.key}:{curr.value}")
            curr = curr.next
        print(f"[LRU] {' <-> '.join(entries)} [MRU]")

    def __len__(self) -> int:
        return len(self.cache)

    def __repr__(self) -> str:
        return f"LRUCache(capacity={self.capacity}, size={len(self.cache)})"


# ── Demo / Tests ──────────────────────────────────────────────────────────────

if __name__ == "__main__":

    print("=" * 55)
    print("  LRU Cache Demo")
    print("=" * 55)

    # ── Basic example ─────────────────────────────────────────
    print("\n── Basic Operations (capacity=3) ──")
    cache = LRUCache(3)

    cache.put(1, 10)
    cache.put(2, 20)
    cache.put(3, 30)
    cache.display()                     # [LRU] 1:10 <-> 2:20 <-> 3:30 [MRU]

    print(cache.get(1))                 # 10  (1 moves to MRU)
    cache.display()                     # [LRU] 2:20 <-> 3:30 <-> 1:10 [MRU]

    cache.put(4, 40)                    # evicts 2 (LRU)
    cache.display()                     # [LRU] 3:30 <-> 1:10 <-> 4:40 [MRU]

    print(cache.get(2))                 # -1  (evicted)
    print(cache.get(3))                 # 30

    # ── Update existing key ───────────────────────────────────
    print("\n── Update Existing Key ──")
    cache.put(1, 100)                   # update key 1
    cache.display()                     # 1 should be at MRU end

    # ── LeetCode-style test ───────────────────────────────────
    print("\n── LeetCode Test Case ──")
    lc = LRUCache(2)
    lc.put(1, 1)
    lc.put(2, 2)
    print(lc.get(1))                    # 1
    lc.put(3, 3)                        # evicts key 2
    print(lc.get(2))                    # -1 (evicted)
    lc.put(4, 4)                        # evicts key 1
    print(lc.get(1))                    # -1 (evicted)
    print(lc.get(3))                    # 3
    print(lc.get(4))                    # 4

    # ── Capacity 1 edge case ──────────────────────────────────
    print("\n── Edge Case: Capacity 1 ──")
    ec = LRUCache(1)
    ec.put(1, 10)
    ec.put(2, 20)                       # evicts 1
    print(ec.get(1))                    # -1
    print(ec.get(2))                    # 20

    print("\n── Final cache state ──")
    print(cache)                        # LRUCache(capacity=3, size=3)
