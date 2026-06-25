"""
Tree Data Structures in Python
================================
This file covers three important tree variants beyond the basic BST:

1. General Tree       - N-ary tree (each node can have any number of children)
2. AVL Tree           - Self-balancing BST (height difference <= 1)
3. Red-Black Tree     - Self-balancing BST with colour properties

Why these matter:
  BST worst case is O(n) when inserted in sorted order (degenerates to linked list).
  AVL and Red-Black trees guarantee O(log n) for all operations by rebalancing.

  AVL Tree      : stricter balance (height diff <= 1), faster lookups
  Red-Black Tree: relaxed balance (height <= 2 * log n), faster insertions/deletions
  General Tree  : models hierarchical data (filesystems, org charts, DOM)
"""

from collections import deque


# ══════════════════════════════════════════════════════════════════════════════
# 1. GENERAL TREE  (N-ary Tree)
# ══════════════════════════════════════════════════════════════════════════════

class GNode:
    """Node for a General (N-ary) Tree."""
    def __init__(self, data):
        self.data     = data
        self.children = []   # list of GNode


class GeneralTree:
    """
    N-ary tree where each node can have any number of children.
    Models: file systems, org charts, HTML/XML DOM, comment threads.

    Operations:
      insert(data, parent_data)  - Add child to a parent node   O(n)
      search(data)               - Find a node                   O(n)
      delete(data)               - Remove node and subtree       O(n)
      depth(data)                - Depth of a node               O(n)
      height()                   - Height of the tree            O(n)
      bfs()                      - Level-order traversal         O(n)
      dfs_preorder()             - DFS preorder traversal        O(n)
      dfs_postorder()            - DFS postorder traversal       O(n)
      display()                  - Pretty-print the tree         O(n)
    """

    def __init__(self, root_data):
        self.root = GNode(root_data)

    # ── Insert ────────────────────────────────────────────────────────────────

    def insert(self, data, parent_data) -> bool:
        """Add a child node under parent_data. Returns False if parent not found."""
        parent = self.search_node(self.root, parent_data)
        if parent is None:
            return False
        parent.children.append(GNode(data))
        return True

    # ── Search ────────────────────────────────────────────────────────────────

    def search(self, data) -> bool:
        return self.search_node(self.root, data) is not None

    def search_node(self, node, data):
        if node is None:
            return None
        if node.data == data:
            return node
        for child in node.children:
            result = self.search_node(child, data)
            if result:
                return result
        return None

    # ── Delete ────────────────────────────────────────────────────────────────

    def delete(self, data) -> bool:
        """Remove node and its entire subtree."""
        if self.root.data == data:
            self.root = None
            return True
        return self._delete(self.root, data)

    def _delete(self, node, data) -> bool:
        for i, child in enumerate(node.children):
            if child.data == data:
                node.children.pop(i)
                return True
            if self._delete(child, data):
                return True
        return False

    # ── Depth ─────────────────────────────────────────────────────────────────

    def depth(self, data) -> int:
        """Return depth of node (root = 0). Returns -1 if not found."""
        return self._depth(self.root, data, 0)

    def _depth(self, node, data, current_depth) -> int:
        if node is None:
            return -1
        if node.data == data:
            return current_depth
        for child in node.children:
            result = self._depth(child, data, current_depth + 1)
            if result != -1:
                return result
        return -1

    # ── Height ────────────────────────────────────────────────────────────────

    def height(self) -> int:
        return self._height(self.root)

    def _height(self, node) -> int:
        if node is None or not node.children:
            return 0
        return 1 + max(self._height(child) for child in node.children)

    # ── Traversals ────────────────────────────────────────────────────────────

    def bfs(self) -> list:
        """Level-order traversal. Returns list of lists per level."""
        if not self.root:
            return []
        result = []
        queue  = deque([self.root])
        while queue:
            level = []
            for _ in range(len(queue)):
                node = queue.popleft()
                level.append(node.data)
                queue.extend(node.children)
            result.append(level)
        return result

    def dfs_preorder(self) -> list:
        """Root first, then children left to right."""
        result = []
        self._preorder(self.root, result)
        return result

    def _preorder(self, node, result):
        if node is None:
            return
        result.append(node.data)
        for child in node.children:
            self._preorder(child, result)

    def dfs_postorder(self) -> list:
        """Children left to right, then root."""
        result = []
        self._postorder(self.root, result)
        return result

    def _postorder(self, node, result):
        if node is None:
            return
        for child in node.children:
            self._postorder(child, result)
        result.append(node.data)

    # ── Display ───────────────────────────────────────────────────────────────

    def display(self, node=None, prefix="", is_last=True):
        if node is None:
            node = self.root
        connector = "└── " if is_last else "├── "
        print(prefix + connector + str(node.data))
        child_prefix = prefix + ("    " if is_last else "│   ")
        for i, child in enumerate(node.children):
            self.display(child, child_prefix, i == len(node.children) - 1)


# ══════════════════════════════════════════════════════════════════════════════
# 2. AVL TREE  (Self-balancing BST)
# ══════════════════════════════════════════════════════════════════════════════

class AVLNode:
    def __init__(self, key):
        self.key    = key
        self.left   = None
        self.right  = None
        self.height = 1     # height of subtree rooted here


class AVLTree:
    """
    AVL Tree — height-balanced BST.
    Invariant: |height(left) - height(right)| <= 1 at every node.

    After every insert/delete, rotations restore balance in O(log n).

    Rotations:
      LL (right rotate)         : inserted in left-left
      RR (left rotate)          : inserted in right-right
      LR (left-right rotate)    : inserted in left-right
      RL (right-left rotate)    : inserted in right-left

    Operations: insert, search, delete, inorder — all O(log n)
    """

    def __init__(self):
        self.root = None

    # ── Height and Balance Factor ─────────────────────────────────────────────

    def _height(self, node) -> int:
        return node.height if node else 0

    def _balance_factor(self, node) -> int:
        return self._height(node.left) - self._height(node.right) if node else 0

    def _update_height(self, node):
        node.height = 1 + max(self._height(node.left), self._height(node.right))

    # ── Rotations ─────────────────────────────────────────────────────────────

    def _rotate_right(self, y):
        """
        Right rotation around y:
              y                x
             / \              / \
            x   T3   -->    T1   y
           / \                  / \
          T1  T2              T2  T3
        """
        x    = y.left
        T2   = x.right
        x.right = y
        y.left  = T2
        self._update_height(y)
        self._update_height(x)
        return x     # new subtree root

    def _rotate_left(self, x):
        """
        Left rotation around x:
            x                  y
           / \                / \
          T1   y    -->      x   T3
              / \           / \
             T2  T3        T1  T2
        """
        y    = x.right
        T2   = y.left
        y.left  = x
        x.right = T2
        self._update_height(x)
        self._update_height(y)
        return y     # new subtree root

    # ── Rebalance ─────────────────────────────────────────────────────────────

    def _rebalance(self, node, key):
        self._update_height(node)
        bf = self._balance_factor(node)

        # LL case: inserted in left subtree of left child
        if bf > 1 and key < node.left.key:
            return self._rotate_right(node)

        # RR case: inserted in right subtree of right child
        if bf < -1 and key > node.right.key:
            return self._rotate_left(node)

        # LR case: inserted in right subtree of left child
        if bf > 1 and key > node.left.key:
            node.left = self._rotate_left(node.left)
            return self._rotate_right(node)

        # RL case: inserted in left subtree of right child
        if bf < -1 and key < node.right.key:
            node.right = self._rotate_right(node.right)
            return self._rotate_left(node)

        return node   # already balanced

    # ── Insert ────────────────────────────────────────────────────────────────

    def insert(self, key) -> None:
        """Time : O(log n)"""
        self.root = self._insert(self.root, key)

    def _insert(self, node, key):
        if node is None:
            return AVLNode(key)
        if key < node.key:
            node.left  = self._insert(node.left, key)
        elif key > node.key:
            node.right = self._insert(node.right, key)
        else:
            return node   # duplicate ignored

        return self._rebalance(node, key)

    # ── Delete ────────────────────────────────────────────────────────────────

    def delete(self, key) -> None:
        """Time : O(log n)"""
        self.root = self._delete(self.root, key)

    def _delete(self, node, key):
        if node is None:
            return None
        if key < node.key:
            node.left  = self._delete(node.left, key)
        elif key > node.key:
            node.right = self._delete(node.right, key)
        else:
            if node.left is None:
                return node.right
            if node.right is None:
                return node.left
            # In-order successor
            successor  = self._min_node(node.right)
            node.key   = successor.key
            node.right = self._delete(node.right, successor.key)

        self._update_height(node)
        bf = self._balance_factor(node)

        # LL
        if bf > 1 and self._balance_factor(node.left) >= 0:
            return self._rotate_right(node)
        # LR
        if bf > 1 and self._balance_factor(node.left) < 0:
            node.left = self._rotate_left(node.left)
            return self._rotate_right(node)
        # RR
        if bf < -1 and self._balance_factor(node.right) <= 0:
            return self._rotate_left(node)
        # RL
        if bf < -1 and self._balance_factor(node.right) > 0:
            node.right = self._rotate_right(node.right)
            return self._rotate_left(node)

        return node

    def _min_node(self, node):
        while node.left:
            node = node.left
        return node

    # ── Search ────────────────────────────────────────────────────────────────

    def search(self, key) -> bool:
        """Time : O(log n)"""
        node = self.root
        while node:
            if   key == node.key: return True
            elif key < node.key:  node = node.left
            else:                 node = node.right
        return False

    # ── Traversals ────────────────────────────────────────────────────────────

    def inorder(self) -> list:
        result = []
        self._inorder(self.root, result)
        return result

    def _inorder(self, node, result):
        if node:
            self._inorder(node.left, result)
            result.append(node.key)
            self._inorder(node.right, result)

    def height(self) -> int:
        return self._height(self.root)

    # ── Display ───────────────────────────────────────────────────────────────

    def display(self, node=None, prefix="", is_left=True):
        if node is None and prefix == "":
            node = self.root
        if node is None:
            return
        connector = "├── " if is_left else "└── "
        bf = self._balance_factor(node)
        print(f"{prefix}{connector}{node.key} (h={node.height}, bf={bf})")
        child_prefix = prefix + ("│   " if is_left else "    ")
        if node.left or node.right:
            self.display(node.left,  child_prefix, True)
            self.display(node.right, child_prefix, False)


# ══════════════════════════════════════════════════════════════════════════════
# 3. RED-BLACK TREE
# ══════════════════════════════════════════════════════════════════════════════

RED   = True
BLACK = False


class RBNode:
    def __init__(self, key):
        self.key    = key
        self.color  = RED
        self.left   = None
        self.right  = None
        self.parent = None


class RedBlackTree:
    """
    Red-Black Tree — relaxed self-balancing BST.

    Properties (must hold after every operation):
      1. Every node is RED or BLACK.
      2. Root is always BLACK.
      3. Every NULL leaf is BLACK.
      4. RED node's children must both be BLACK (no two consecutive reds).
      5. Every path from a node to its descendant NULLs has the same
         number of BLACK nodes (black-height is consistent).

    Guarantees height <= 2 * log2(n+1), giving O(log n) operations.

    Preferred over AVL when writes are frequent (fewer rotations on average).
    Used in: Linux CFS scheduler, Java TreeMap, C++ std::map.
    """

    def __init__(self):
        # Sentinel NIL node (BLACK) — simplifies boundary checks
        self.NIL        = RBNode(None)
        self.NIL.color  = BLACK
        self.NIL.left   = self.NIL
        self.NIL.right  = self.NIL
        self.root       = self.NIL

    # ── Rotations ─────────────────────────────────────────────────────────────

    def _left_rotate(self, x):
        y         = x.right
        x.right   = y.left
        if y.left != self.NIL:
            y.left.parent = x
        y.parent  = x.parent
        if x.parent is None:
            self.root = y
        elif x == x.parent.left:
            x.parent.left  = y
        else:
            x.parent.right = y
        y.left    = x
        x.parent  = y

    def _right_rotate(self, y):
        x         = y.left
        y.left    = x.right
        if x.right != self.NIL:
            x.right.parent = y
        x.parent  = y.parent
        if y.parent is None:
            self.root = x
        elif y == y.parent.left:
            y.parent.left  = x
        else:
            y.parent.right = x
        x.right   = y
        y.parent  = x

    # ── Insert ────────────────────────────────────────────────────────────────

    def insert(self, key) -> None:
        """Time : O(log n)"""
        node         = RBNode(key)
        node.left    = self.NIL
        node.right   = self.NIL
        node.color   = RED

        parent  = None
        current = self.root

        while current != self.NIL:
            parent = current
            if node.key < current.key:
                current = current.left
            elif node.key > current.key:
                current = current.right
            else:
                return   # duplicate ignored

        node.parent = parent
        if parent is None:
            self.root = node
        elif node.key < parent.key:
            parent.left  = node
        else:
            parent.right = node

        if node.parent is None:
            node.color = BLACK
            return
        if node.parent.parent is None:
            return

        self._fix_insert(node)

    def _fix_insert(self, z):
        """Restore Red-Black properties after insertion."""
        while z.parent and z.parent.color == RED:
            if z.parent == z.parent.parent.left:
                uncle = z.parent.parent.right
                if uncle.color == RED:
                    # Case 1: Uncle is RED — recolour
                    z.parent.color         = BLACK
                    uncle.color            = BLACK
                    z.parent.parent.color  = RED
                    z = z.parent.parent
                else:
                    if z == z.parent.right:
                        # Case 2: z is right child — left rotate parent
                        z = z.parent
                        self._left_rotate(z)
                    # Case 3: z is left child — right rotate grandparent
                    z.parent.color        = BLACK
                    z.parent.parent.color = RED
                    self._right_rotate(z.parent.parent)
            else:
                uncle = z.parent.parent.left
                if uncle.color == RED:
                    z.parent.color        = BLACK
                    uncle.color           = BLACK
                    z.parent.parent.color = RED
                    z = z.parent.parent
                else:
                    if z == z.parent.left:
                        z = z.parent
                        self._right_rotate(z)
                    z.parent.color        = BLACK
                    z.parent.parent.color = RED
                    self._left_rotate(z.parent.parent)
            if z == self.root:
                break
        self.root.color = BLACK

    # ── Delete ────────────────────────────────────────────────────────────────

    def delete(self, key) -> None:
        """Time : O(log n)"""
        node = self._find(self.root, key)
        if node == self.NIL:
            return
        self._delete_node(node)

    def _find(self, node, key):
        while node != self.NIL:
            if   key == node.key: return node
            elif key < node.key:  node = node.left
            else:                 node = node.right
        return self.NIL

    def _transplant(self, u, v):
        if u.parent is None:
            self.root = v
        elif u == u.parent.left:
            u.parent.left  = v
        else:
            u.parent.right = v
        v.parent = u.parent

    def _delete_node(self, z):
        y              = z
        y_original_color = y.color
        if z.left == self.NIL:
            x = z.right
            self._transplant(z, z.right)
        elif z.right == self.NIL:
            x = z.left
            self._transplant(z, z.left)
        else:
            y = self._min_node(z.right)
            y_original_color = y.color
            x = y.right
            if y.parent == z:
                x.parent = y
            else:
                self._transplant(y, y.right)
                y.right        = z.right
                y.right.parent = y
            self._transplant(z, y)
            y.left         = z.left
            y.left.parent  = y
            y.color        = z.color
        if y_original_color == BLACK:
            self._fix_delete(x)

    def _fix_delete(self, x):
        """Restore Red-Black properties after deletion."""
        while x != self.root and x.color == BLACK:
            if x == x.parent.left:
                w = x.parent.right
                if w.color == RED:
                    w.color        = BLACK
                    x.parent.color = RED
                    self._left_rotate(x.parent)
                    w = x.parent.right
                if w.left.color == BLACK and w.right.color == BLACK:
                    w.color = RED
                    x = x.parent
                else:
                    if w.right.color == BLACK:
                        w.left.color = BLACK
                        w.color      = RED
                        self._right_rotate(w)
                        w = x.parent.right
                    w.color        = x.parent.color
                    x.parent.color = BLACK
                    w.right.color  = BLACK
                    self._left_rotate(x.parent)
                    x = self.root
            else:
                w = x.parent.left
                if w.color == RED:
                    w.color        = BLACK
                    x.parent.color = RED
                    self._right_rotate(x.parent)
                    w = x.parent.left
                if w.right.color == BLACK and w.left.color == BLACK:
                    w.color = RED
                    x = x.parent
                else:
                    if w.left.color == BLACK:
                        w.right.color = BLACK
                        w.color       = RED
                        self._left_rotate(w)
                        w = x.parent.left
                    w.color        = x.parent.color
                    x.parent.color = BLACK
                    w.left.color   = BLACK
                    self._right_rotate(x.parent)
                    x = self.root
        x.color = BLACK

    def _min_node(self, node):
        while node.left != self.NIL:
            node = node.left
        return node

    # ── Search ────────────────────────────────────────────────────────────────

    def search(self, key) -> bool:
        """Time : O(log n)"""
        return self._find(self.root, key) != self.NIL

    # ── Traversals ────────────────────────────────────────────────────────────

    def inorder(self) -> list:
        result = []
        self._inorder(self.root, result)
        return result

    def _inorder(self, node, result):
        if node != self.NIL:
            self._inorder(node.left, result)
            result.append(node.key)
            self._inorder(node.right, result)

    def height(self) -> int:
        return self._height(self.root)

    def _height(self, node) -> int:
        if node == self.NIL:
            return 0
        return 1 + max(self._height(node.left), self._height(node.right))

    # ── Display ───────────────────────────────────────────────────────────────

    def display(self, node=None, prefix="", is_left=True):
        if node is None:
            node = self.root
        if node == self.NIL:
            return
        connector = "├── " if is_left else "└── "
        color_str = "R" if node.color == RED else "B"
        print(f"{prefix}{connector}{node.key}({color_str})")
        child_prefix = prefix + ("│   " if is_left else "    ")
        if node.left != self.NIL or node.right != self.NIL:
            self.display(node.left,  child_prefix, True)
            self.display(node.right, child_prefix, False)


# ══════════════════════════════════════════════════════════════════════════════
# DEMO
# ══════════════════════════════════════════════════════════════════════════════

if __name__ == "__main__":

    # ── 1. General Tree ───────────────────────────────────────
    print("=" * 60)
    print("  1. General Tree (N-ary)")
    print("=" * 60)

    gt = GeneralTree("CEO")
    gt.insert("CTO",  "CEO")
    gt.insert("CFO",  "CEO")
    gt.insert("COO",  "CEO")
    gt.insert("Dev1", "CTO")
    gt.insert("Dev2", "CTO")
    gt.insert("QA1",  "CTO")
    gt.insert("Fin1", "CFO")
    gt.insert("Ops1", "COO")
    gt.insert("Ops2", "COO")

    print("\nTree structure:")
    gt.display()

    print(f"\nBFS (level order) : {gt.bfs()}")
    print(f"DFS preorder      : {gt.dfs_preorder()}")
    print(f"DFS postorder     : {gt.dfs_postorder()}")
    print(f"Height            : {gt.height()}")
    print(f"Depth of 'Dev1'   : {gt.depth('Dev1')}")
    print(f"Depth of 'CFO'    : {gt.depth('CFO')}")
    print(f"Search 'QA1'      : {gt.search('QA1')}")
    print(f"Search 'VP'       : {gt.search('VP')}")

    print("\nAfter deleting 'CTO' subtree:")
    gt.delete("CTO")
    gt.display()

    # ── 2. AVL Tree ───────────────────────────────────────────
    print("\n" + "=" * 60)
    print("  2. AVL Tree (Self-Balancing BST)")
    print("=" * 60)

    avl = AVLTree()
    # Insert in sorted order — would degenerate a plain BST to O(n)
    # AVL keeps it balanced
    for k in [10, 20, 30, 40, 50, 25]:
        avl.insert(k)
        print(f"  Inserted {k:2d} | height={avl.height()} | inorder={avl.inorder()}")

    print("\nAVL Tree structure (key, height h, balance factor bf):")
    avl.display()

    print(f"\nSearch 30 : {avl.search(30)}")   # True
    print(f"Search 99 : {avl.search(99)}")     # False

    print("\nDeleting 40:")
    avl.delete(40)
    avl.display()
    print(f"Inorder after delete: {avl.inorder()}")

    # ── 3. Red-Black Tree ─────────────────────────────────────
    print("\n" + "=" * 60)
    print("  3. Red-Black Tree")
    print("=" * 60)

    rbt = RedBlackTree()
    for k in [10, 20, 30, 15, 25, 5, 1]:
        rbt.insert(k)
        print(f"  Inserted {k:2d} | height={rbt.height()} | inorder={rbt.inorder()}")

    print("\nRed-Black Tree structure (R=Red, B=Black):")
    rbt.display()

    print(f"\nSearch 15 : {rbt.search(15)}")   # True
    print(f"Search 99 : {rbt.search(99)}")     # False

    print("\nDeleting 20:")
    rbt.delete(20)
    rbt.display()
    print(f"Inorder after delete: {rbt.inorder()}")

    # ── Comparison Table ──────────────────────────────────────
    print("\n" + "=" * 60)
    print("  Tree Comparison")
    print("=" * 60)
    print(f"{'Structure':<20} {'Insert':<12} {'Search':<12} {'Delete':<12} {'Balance'}")
    print("-" * 60)
    print(f"{'General Tree':<20} {'O(n)':<12} {'O(n)':<12} {'O(n)':<12} {'None'}")
    print(f"{'BST (plain)':<20} {'O(h)':<12} {'O(h)':<12} {'O(h)':<12} {'None (O(n) worst)'}")
    print(f"{'AVL Tree':<20} {'O(log n)':<12} {'O(log n)':<12} {'O(log n)':<12} {'Strict (bf<=1)'}")
    print(f"{'Red-Black Tree':<20} {'O(log n)':<12} {'O(log n)':<12} {'O(log n)':<12} {'Relaxed (h<=2logn)'}")
