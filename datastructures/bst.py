"""
Binary Search Tree (BST) Implementation in Python
===================================================
BST property : for every node N,
  - all keys in N's left subtree  < N.key
  - all keys in N's right subtree > N.key

Supported Operations:
  insert(key)          - Insert a key              O(h)
  search(key)          - Find a key                O(h)
  delete(key)          - Remove a key              O(h)
  inorder()            - Sorted traversal          O(n)
  preorder()           - Root → Left → Right       O(n)
  postorder()          - Left → Right → Root       O(n)
  level_order()        - BFS level by level        O(n)
  min_val()            - Minimum key               O(h)
  max_val()            - Maximum key               O(h)
  height()             - Tree height               O(n)
  is_valid_bst()       - Validate BST property     O(n)
  kth_smallest(k)      - Kth smallest element      O(h + k)
  floor(key)           - Largest key <= given key  O(h)
  ceil(key)            - Smallest key >= given key O(h)

  h = height (O(log n) balanced, O(n) worst case)
"""

from collections import deque


# ── BST Node ──────────────────────────────────────────────────────────────────

class Node:
    def __init__(self, key):
        self.key   = key
        self.left  = None
        self.right = None


# ── Binary Search Tree ────────────────────────────────────────────────────────

class BST:
    def __init__(self):
        self.root = None

    # ── Insert ────────────────────────────────────────────────────────────────

    def insert(self, key) -> None:
        """Time : O(h)"""
        self.root = self._insert(self.root, key)

    def _insert(self, node, key):
        if node is None:
            return Node(key)
        if key < node.key:
            node.left  = self._insert(node.left, key)
        elif key > node.key:
            node.right = self._insert(node.right, key)
        # duplicate keys are ignored
        return node

    # ── Search ────────────────────────────────────────────────────────────────

    def search(self, key) -> bool:
        """Time : O(h)"""
        return self._search(self.root, key)

    def _search(self, node, key) -> bool:
        if node is None:
            return False
        if key == node.key:
            return True
        if key < node.key:
            return self._search(node.left, key)
        return self._search(node.right, key)

    # ── Delete ────────────────────────────────────────────────────────────────

    def delete(self, key) -> None:
        """
        Three cases:
          1. Node is a leaf          -> remove directly
          2. Node has one child      -> replace with child
          3. Node has two children   -> replace with in-order successor
                                        (smallest in right subtree)
        Time : O(h)
        """
        self.root = self._delete(self.root, key)

    def _delete(self, node, key):
        if node is None:
            return None
        if key < node.key:
            node.left  = self._delete(node.left, key)
        elif key > node.key:
            node.right = self._delete(node.right, key)
        else:
            # Case 1 & 2: zero or one child
            if node.left is None:
                return node.right
            if node.right is None:
                return node.left
            # Case 3: two children — find in-order successor
            successor  = self._min_node(node.right)
            node.key   = successor.key
            node.right = self._delete(node.right, successor.key)
        return node

    # ── Traversals ────────────────────────────────────────────────────────────

    def inorder(self) -> list:
        """Left -> Root -> Right  (returns sorted order). Time : O(n)"""
        result = []
        self._inorder(self.root, result)
        return result

    def _inorder(self, node, result):
        if node:
            self._inorder(node.left, result)
            result.append(node.key)
            self._inorder(node.right, result)

    def preorder(self) -> list:
        """Root -> Left -> Right. Time : O(n)"""
        result = []
        self._preorder(self.root, result)
        return result

    def _preorder(self, node, result):
        if node:
            result.append(node.key)
            self._preorder(node.left, result)
            self._preorder(node.right, result)

    def postorder(self) -> list:
        """Left -> Right -> Root. Time : O(n)"""
        result = []
        self._postorder(self.root, result)
        return result

    def _postorder(self, node, result):
        if node:
            self._postorder(node.left, result)
            self._postorder(node.right, result)
            result.append(node.key)

    def level_order(self) -> list:
        """BFS level by level. Time : O(n)"""
        if not self.root:
            return []
        result = []
        queue  = deque([self.root])
        while queue:
            level = []
            for _ in range(len(queue)):
                node = queue.popleft()
                level.append(node.key)
                if node.left:  queue.append(node.left)
                if node.right: queue.append(node.right)
            result.append(level)
        return result

    # ── Min / Max ─────────────────────────────────────────────────────────────

    def min_val(self):
        """Return minimum key. Time : O(h)"""
        if not self.root:
            raise ValueError("Tree is empty")
        return self._min_node(self.root).key

    def _min_node(self, node):
        while node.left:
            node = node.left
        return node

    def max_val(self):
        """Return maximum key. Time : O(h)"""
        if not self.root:
            raise ValueError("Tree is empty")
        node = self.root
        while node.right:
            node = node.right
        return node.key

    # ── Height ────────────────────────────────────────────────────────────────

    def height(self) -> int:
        """Return height of the tree (0 for single node). Time : O(n)"""
        return self._height(self.root)

    def _height(self, node) -> int:
        if node is None:
            return -1
        return 1 + max(self._height(node.left), self._height(node.right))

    # ── BST Validation ────────────────────────────────────────────────────────

    def is_valid_bst(self) -> bool:
        """Validate BST property using min/max bounds. Time : O(n)"""
        return self._validate(self.root, float('-inf'), float('inf'))

    def _validate(self, node, min_val, max_val) -> bool:
        if node is None:
            return True
        if not (min_val < node.key < max_val):
            return False
        return (self._validate(node.left,  min_val,   node.key) and
                self._validate(node.right, node.key,  max_val))

    # ── Kth Smallest ──────────────────────────────────────────────────────────

    def kth_smallest(self, k: int):
        """
        Return the kth smallest element using inorder traversal.
        Time : O(h + k)
        """
        self._kth_count = 0
        return self._kth_smallest(self.root, k)

    def _kth_smallest(self, node, k):
        if node is None:
            return None
        left = self._kth_smallest(node.left, k)
        if left is not None:
            return left
        self._kth_count += 1
        if self._kth_count == k:
            return node.key
        return self._kth_smallest(node.right, k)

    # ── Floor and Ceil ────────────────────────────────────────────────────────

    def floor(self, key):
        """Largest key <= given key. Time : O(h)"""
        result = self._floor(self.root, key)
        return result.key if result else None

    def _floor(self, node, key):
        if node is None:
            return None
        if key == node.key:
            return node
        if key < node.key:
            return self._floor(node.left, key)
        right = self._floor(node.right, key)
        return right if right else node

    def ceil(self, key):
        """Smallest key >= given key. Time : O(h)"""
        result = self._ceil(self.root, key)
        return result.key if result else None

    def _ceil(self, node, key):
        if node is None:
            return None
        if key == node.key:
            return node
        if key > node.key:
            return self._ceil(node.right, key)
        left = self._ceil(node.left, key)
        return left if left else node

    # ── Display ───────────────────────────────────────────────────────────────

    def display(self) -> None:
        """Print tree sideways (right subtree on top)."""
        self._display(self.root, "", True)

    def _display(self, node, prefix, is_left):
        if node is None:
            return
        connector = "|-- " if is_left else "\\-- "
        print(prefix + connector + str(node.key))
        new_prefix = prefix + ("|   " if is_left else "    ")
        self._display(node.left,  new_prefix, True)
        self._display(node.right, new_prefix, False)


# ── Demo / Tests ──────────────────────────────────────────────────────────────

if __name__ == "__main__":

    print("=" * 55)
    print("  Binary Search Tree Demo")
    print("=" * 55)

    bst = BST()
    keys = [50, 30, 70, 20, 40, 60, 80, 10, 25, 35, 45]
    for k in keys:
        bst.insert(k)

    # ── Traversals ────────────────────────────────────────────
    print("\n── Traversals ──")
    print(f"Inorder   (sorted) : {bst.inorder()}")
    print(f"Preorder           : {bst.preorder()}")
    print(f"Postorder          : {bst.postorder()}")
    print(f"Level order        : {bst.level_order()}")

    # ── Search ────────────────────────────────────────────────
    print("\n── Search ──")
    print(f"search(40) : {bst.search(40)}")       # True
    print(f"search(99) : {bst.search(99)}")       # False

    # ── Min / Max / Height ────────────────────────────────────
    print("\n── Min / Max / Height ──")
    print(f"Min    : {bst.min_val()}")             # 10
    print(f"Max    : {bst.max_val()}")             # 80
    print(f"Height : {bst.height()}")              # 3

    # ── BST Validation ────────────────────────────────────────
    print("\n── BST Validation ──")
    print(f"Is valid BST : {bst.is_valid_bst()}")  # True

    # ── Kth Smallest ──────────────────────────────────────────
    print("\n── Kth Smallest ──")
    print(f"1st smallest : {bst.kth_smallest(1)}")  # 10
    print(f"3rd smallest : {bst.kth_smallest(3)}")  # 25
    print(f"5th smallest : {bst.kth_smallest(5)}")  # 35

    # ── Floor and Ceil ────────────────────────────────────────
    print("\n── Floor / Ceil ──")
    print(f"floor(55) : {bst.floor(55)}")           # 50
    print(f"ceil(55)  : {bst.ceil(55)}")            # 60
    print(f"floor(20) : {bst.floor(20)}")           # 20 (exact match)
    print(f"ceil(20)  : {bst.ceil(20)}")            # 20 (exact match)

    # ── Delete ────────────────────────────────────────────────
    print("\n── Delete ──")
    print(f"Before : {bst.inorder()}")
    bst.delete(20)    # leaf node
    print(f"After del 20 (leaf)          : {bst.inorder()}")
    bst.delete(30)    # node with two children
    print(f"After del 30 (two children)  : {bst.inorder()}")
    bst.delete(70)    # node with two children
    print(f"After del 70 (two children)  : {bst.inorder()}")

    # ── Tree Structure ────────────────────────────────────────
    print("\n── Tree Structure ──")
    bst2 = BST()
    for k in [50, 30, 70, 20, 40, 60, 80]:
        bst2.insert(k)
    bst2.display()
