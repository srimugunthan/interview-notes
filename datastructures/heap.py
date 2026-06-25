"""
Heap Implementation in Python
==============================
A Heap is a complete binary tree stored as an array.

MinHeap : root is always the smallest element
MaxHeap : root is always the largest element

For a node at index i:
  Parent      : (i - 1) // 2
  Left child  : 2 * i + 1
  Right child : 2 * i + 2

Supported Operations:
  push(val)     - Insert a value             O(log n)
  pop()         - Remove and return root     O(log n)
  peek()        - View root without removal  O(1)
  heapify(list) - Build heap from a list     O(n)
  size()        - Number of elements         O(1)
  display()     - Print heap array           O(1)
"""


# ── Min Heap ──────────────────────────────────────────────────────────────────

class MinHeap:
    def __init__(self):
        self._data = []

    # ── Push ──────────────────────────────────────────────────────────────────

    def push(self, val) -> None:
        """
        Insert val and sift up to restore heap property.
        Time : O(log n)
        """
        self._data.append(val)
        self._sift_up(len(self._data) - 1)

    def _sift_up(self, i: int) -> None:
        while i > 0:
            parent = (i - 1) // 2
            if self._data[i] < self._data[parent]:
                self._data[i], self._data[parent] = self._data[parent], self._data[i]
                i = parent
            else:
                break

    # ── Pop ───────────────────────────────────────────────────────────────────

    def pop(self):
        """
        Remove and return the minimum element (root).
        Swap root with last element, remove last, sift down.
        Time : O(log n)
        """
        if self.is_empty():
            raise IndexError("Heap is empty")
        root = self._data[0]
        last = self._data.pop()
        if self._data:
            self._data[0] = last
            self._sift_down(0)
        return root

    def _sift_down(self, i: int) -> None:
        n = len(self._data)
        while True:
            smallest = i
            left     = 2 * i + 1
            right    = 2 * i + 2

            if left < n and self._data[left] < self._data[smallest]:
                smallest = left
            if right < n and self._data[right] < self._data[smallest]:
                smallest = right

            if smallest != i:
                self._data[i], self._data[smallest] = self._data[smallest], self._data[i]
                i = smallest
            else:
                break

    # ── Peek ──────────────────────────────────────────────────────────────────

    def peek(self):
        """Return minimum without removing. Time : O(1)"""
        if self.is_empty():
            raise IndexError("Heap is empty")
        return self._data[0]

    # ── Heapify ───────────────────────────────────────────────────────────────

    def heapify(self, arr: list) -> None:
        """
        Build a heap from an arbitrary list in O(n).
        Sift down from the last internal node up to root.
        Time  : O(n)   (better than n * O(log n) repeated inserts)
        """
        self._data = list(arr)
        # Last internal node is at index (n // 2 - 1)
        for i in range(len(self._data) // 2 - 1, -1, -1):
            self._sift_down(i)

    # ── Utilities ─────────────────────────────────────────────────────────────

    def is_empty(self) -> bool:
        return len(self._data) == 0

    def size(self) -> int:
        return len(self._data)

    def display(self) -> None:
        print(f"MinHeap array : {self._data}")
        self._print_tree(0, "", True)

    def _print_tree(self, i, prefix, is_left):
        if i >= len(self._data):
            return
        connector = "|-- " if is_left else "\\-- "
        print(prefix + connector + str(self._data[i]))
        new_prefix = prefix + ("|   " if is_left else "    ")
        self._print_tree(2 * i + 1, new_prefix, True)
        self._print_tree(2 * i + 2, new_prefix, False)

    def __repr__(self):
        return f"MinHeap({self._data})"


# ── Max Heap ──────────────────────────────────────────────────────────────────

class MaxHeap:
    def __init__(self):
        self._data = []

    # ── Push ──────────────────────────────────────────────────────────────────

    def push(self, val) -> None:
        """Time : O(log n)"""
        self._data.append(val)
        self._sift_up(len(self._data) - 1)

    def _sift_up(self, i: int) -> None:
        while i > 0:
            parent = (i - 1) // 2
            if self._data[i] > self._data[parent]:
                self._data[i], self._data[parent] = self._data[parent], self._data[i]
                i = parent
            else:
                break

    # ── Pop ───────────────────────────────────────────────────────────────────

    def pop(self):
        """Remove and return the maximum element. Time : O(log n)"""
        if self.is_empty():
            raise IndexError("Heap is empty")
        root = self._data[0]
        last = self._data.pop()
        if self._data:
            self._data[0] = last
            self._sift_down(0)
        return root

    def _sift_down(self, i: int) -> None:
        n = len(self._data)
        while True:
            largest = i
            left    = 2 * i + 1
            right   = 2 * i + 2

            if left < n and self._data[left] > self._data[largest]:
                largest = left
            if right < n and self._data[right] > self._data[largest]:
                largest = right

            if largest != i:
                self._data[i], self._data[largest] = self._data[largest], self._data[i]
                i = largest
            else:
                break

    # ── Peek ──────────────────────────────────────────────────────────────────

    def peek(self):
        """Return maximum without removing. Time : O(1)"""
        if self.is_empty():
            raise IndexError("Heap is empty")
        return self._data[0]

    # ── Heapify ───────────────────────────────────────────────────────────────

    def heapify(self, arr: list) -> None:
        """Build a max-heap from an arbitrary list in O(n)."""
        self._data = list(arr)
        for i in range(len(self._data) // 2 - 1, -1, -1):
            self._sift_down(i)

    # ── Heap Sort (bonus) ─────────────────────────────────────────────────────

    @staticmethod
    def heap_sort(arr: list) -> list:
        """
        Sort array in ascending order using a MaxHeap.
        Time  : O(n log n)
        Space : O(n)
        """
        h = MaxHeap()
        h.heapify(arr)
        result = []
        while not h.is_empty():
            result.append(h.pop())
        return result[::-1]     # pop gives descending; reverse for ascending

    # ── Utilities ─────────────────────────────────────────────────────────────

    def is_empty(self) -> bool:
        return len(self._data) == 0

    def size(self) -> int:
        return len(self._data)

    def display(self) -> None:
        print(f"MaxHeap array : {self._data}")
        self._print_tree(0, "", True)

    def _print_tree(self, i, prefix, is_left):
        if i >= len(self._data):
            return
        connector = "|-- " if is_left else "\\-- "
        print(prefix + connector + str(self._data[i]))
        new_prefix = prefix + ("|   " if is_left else "    ")
        self._print_tree(2 * i + 1, new_prefix, True)
        self._print_tree(2 * i + 2, new_prefix, False)

    def __repr__(self):
        return f"MaxHeap({self._data})"


# ── Demo / Tests ──────────────────────────────────────────────────────────────

if __name__ == "__main__":

    print("=" * 55)
    print("  Heap Demo")
    print("=" * 55)

    # ── MinHeap ───────────────────────────────────────────────
    print("\n── MinHeap ──")
    minh = MinHeap()
    for v in [10, 4, 15, 1, 7, 20, 3]:
        minh.push(v)

    print(f"Peek (min)  : {minh.peek()}")        # 1
    print(f"Pop         : {minh.pop()}")          # 1
    print(f"Pop         : {minh.pop()}")          # 3
    print(f"Size        : {minh.size()}")         # 5
    minh.display()

    # ── Heapify ───────────────────────────────────────────────
    print("\n── MinHeap.heapify (O(n) build) ──")
    minh2 = MinHeap()
    minh2.heapify([9, 4, 7, 1, 8, 3, 6, 2, 5])
    print(minh2)
    print(f"Pop sequence : ", end="")
    while not minh2.is_empty():
        print(minh2.pop(), end=" ")              # 1 2 3 4 5 6 7 8 9
    print()

    # ── MaxHeap ───────────────────────────────────────────────
    print("\n── MaxHeap ──")
    maxh = MaxHeap()
    for v in [10, 4, 15, 1, 7, 20, 3]:
        maxh.push(v)

    print(f"Peek (max)  : {maxh.peek()}")        # 20
    print(f"Pop         : {maxh.pop()}")          # 20
    print(f"Pop         : {maxh.pop()}")          # 15
    print(f"Size        : {maxh.size()}")         # 5
    maxh.display()

    # ── Heap Sort ─────────────────────────────────────────────
    print("\n── Heap Sort ──")
    arr = [38, 12, 5, 99, 27, 3, 61]
    print(f"Input  : {arr}")
    print(f"Sorted : {MaxHeap.heap_sort(arr)}")   # [3, 5, 12, 27, 38, 61, 99]

    # ── K Largest elements (classic heap use-case) ────────────
    print("\n── K Largest Elements (k=3) ──")
    data = [7, 10, 4, 3, 20, 15, 8, 2]
    minh3 = MinHeap()
    k = 3
    for num in data:
        minh3.push(num)
        if minh3.size() > k:
            minh3.pop()                 # evict smallest; keep top-k
    result = []
    while not minh3.is_empty():
        result.append(minh3.pop())
    print(f"Data    : {data}")
    print(f"Top {k}   : {sorted(result, reverse=True)}")   # [20, 15, 10]
