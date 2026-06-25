import java.util.*;

/**
 * Tree Data Structures in Java
 * ================================
 * This file covers three important tree variants beyond the basic BST:
 *
 * 1. General Tree       - N-ary tree (each node can have any number of children)
 * 2. AVL Tree           - Self-balancing BST (height difference <= 1)
 * 3. Red-Black Tree     - Self-balancing BST with colour properties
 *
 * Why these matter:
 *   BST worst case is O(n) when inserted in sorted order (degenerates to linked list).
 *   AVL and Red-Black trees guarantee O(log n) for all operations by rebalancing.
 *
 *   AVL Tree      : stricter balance (height diff <= 1), faster lookups
 *   Red-Black Tree: relaxed balance (height <= 2 * log n), faster insertions/deletions
 *   General Tree  : models hierarchical data (filesystems, org charts, DOM)
 */
public class Trees {

    // ══════════════════════════════════════════════════════════════════════════
    // 1. GENERAL TREE  (N-ary Tree)
    // ══════════════════════════════════════════════════════════════════════════

    static class GNode {
        String          data;
        List<GNode>     children;

        GNode(String data) {
            this.data     = data;
            this.children = new ArrayList<>();
        }
    }

    static class GeneralTree {
        /**
         * N-ary tree where each node can have any number of children.
         * Models: file systems, org charts, HTML/XML DOM, comment threads.
         *
         * Operations:
         *   insert(data, parentData)  - Add child to a parent node   O(n)
         *   search(data)              - Find a node                   O(n)
         *   delete(data)              - Remove node and subtree       O(n)
         *   depth(data)               - Depth of a node               O(n)
         *   height()                  - Height of the tree            O(n)
         *   bfs()                     - Level-order traversal         O(n)
         *   dfsPreorder()             - DFS preorder traversal        O(n)
         *   dfsPostorder()            - DFS postorder traversal       O(n)
         *   display()                 - Pretty-print the tree         O(n)
         */

        GNode root;

        GeneralTree(String rootData) {
            this.root = new GNode(rootData);
        }

        // ── Insert ────────────────────────────────────────────────────────────

        /** Add a child node under parentData. Returns false if parent not found. */
        public boolean insert(String data, String parentData) {
            GNode parent = searchNode(root, parentData);
            if (parent == null) return false;
            parent.children.add(new GNode(data));
            return true;
        }

        // ── Search ────────────────────────────────────────────────────────────

        public boolean search(String data) {
            return searchNode(root, data) != null;
        }

        private GNode searchNode(GNode node, String data) {
            if (node == null)         return null;
            if (node.data.equals(data)) return node;
            for (GNode child : node.children) {
                GNode result = searchNode(child, data);
                if (result != null) return result;
            }
            return null;
        }

        // ── Delete ────────────────────────────────────────────────────────────

        /** Remove node and its entire subtree. */
        public boolean delete(String data) {
            if (root != null && root.data.equals(data)) {
                root = null;
                return true;
            }
            return deleteHelper(root, data);
        }

        private boolean deleteHelper(GNode node, String data) {
            if (node == null) return false;
            for (int i = 0; i < node.children.size(); i++) {
                if (node.children.get(i).data.equals(data)) {
                    node.children.remove(i);
                    return true;
                }
                if (deleteHelper(node.children.get(i), data)) return true;
            }
            return false;
        }

        // ── Depth ─────────────────────────────────────────────────────────────

        /** Return depth of node (root = 0). Returns -1 if not found. */
        public int depth(String data) {
            return depthHelper(root, data, 0);
        }

        private int depthHelper(GNode node, String data, int currentDepth) {
            if (node == null)            return -1;
            if (node.data.equals(data))  return currentDepth;
            for (GNode child : node.children) {
                int result = depthHelper(child, data, currentDepth + 1);
                if (result != -1) return result;
            }
            return -1;
        }

        // ── Height ────────────────────────────────────────────────────────────

        public int height() { return heightHelper(root); }

        private int heightHelper(GNode node) {
            if (node == null || node.children.isEmpty()) return 0;
            int max = 0;
            for (GNode child : node.children) max = Math.max(max, heightHelper(child));
            return 1 + max;
        }

        // ── Traversals ────────────────────────────────────────────────────────

        /** Level-order traversal. Returns list of lists per level. */
        public List<List<String>> bfs() {
            List<List<String>> result = new ArrayList<>();
            if (root == null) return result;
            Queue<GNode> queue = new LinkedList<>();
            queue.offer(root);
            while (!queue.isEmpty()) {
                List<String> level = new ArrayList<>();
                int sz = queue.size();
                for (int i = 0; i < sz; i++) {
                    GNode node = queue.poll();
                    level.add(node.data);
                    queue.addAll(node.children);
                }
                result.add(level);
            }
            return result;
        }

        public List<String> dfsPreorder() {
            List<String> result = new ArrayList<>();
            preorder(root, result);
            return result;
        }

        private void preorder(GNode node, List<String> result) {
            if (node == null) return;
            result.add(node.data);
            for (GNode child : node.children) preorder(child, result);
        }

        public List<String> dfsPostorder() {
            List<String> result = new ArrayList<>();
            postorder(root, result);
            return result;
        }

        private void postorder(GNode node, List<String> result) {
            if (node == null) return;
            for (GNode child : node.children) postorder(child, result);
            result.add(node.data);
        }

        // ── Display ───────────────────────────────────────────────────────────

        public void display() { display(root, "", true); }

        private void display(GNode node, String prefix, boolean isLast) {
            if (node == null) return;
            String connector = isLast ? "└── " : "├── ";
            System.out.println(prefix + connector + node.data);
            String childPrefix = prefix + (isLast ? "    " : "│   ");
            for (int i = 0; i < node.children.size(); i++) {
                display(node.children.get(i), childPrefix,
                        i == node.children.size() - 1);
            }
        }
    }


    // ══════════════════════════════════════════════════════════════════════════
    // 2. AVL TREE  (Self-balancing BST)
    // ══════════════════════════════════════════════════════════════════════════

    static class AVLNode {
        int     key, height;
        AVLNode left, right;

        AVLNode(int key) {
            this.key    = key;
            this.height = 1;
        }
    }

    static class AVLTree {
        /**
         * AVL Tree — height-balanced BST.
         * Invariant: |height(left) - height(right)| <= 1 at every node.
         *
         * Rotations:
         *   LL (right rotate)      : inserted in left-left
         *   RR (left rotate)       : inserted in right-right
         *   LR (left-right rotate) : inserted in left-right
         *   RL (right-left rotate) : inserted in right-left
         *
         * All operations: O(log n)
         */

        AVLNode root;

        // ── Height & Balance ──────────────────────────────────────────────────

        private int height(AVLNode n)        { return n == null ? 0 : n.height; }
        private int balanceFactor(AVLNode n) { return n == null ? 0 : height(n.left) - height(n.right); }

        private void updateHeight(AVLNode n) {
            n.height = 1 + Math.max(height(n.left), height(n.right));
        }

        // ── Rotations ─────────────────────────────────────────────────────────

        /**
         * Right rotation:
         *       y              x
         *      / \            / \
         *     x   T3   ->   T1   y
         *    / \                / \
         *   T1  T2            T2  T3
         */
        private AVLNode rotateRight(AVLNode y) {
            AVLNode x  = y.left;
            AVLNode T2 = x.right;
            x.right = y;
            y.left  = T2;
            updateHeight(y);
            updateHeight(x);
            return x;
        }

        /**
         * Left rotation:
         *     x                y
         *    / \              / \
         *   T1   y    ->     x   T3
         *       / \         / \
         *      T2  T3      T1  T2
         */
        private AVLNode rotateLeft(AVLNode x) {
            AVLNode y  = x.right;
            AVLNode T2 = y.left;
            y.left  = x;
            x.right = T2;
            updateHeight(x);
            updateHeight(y);
            return y;
        }

        // ── Insert ────────────────────────────────────────────────────────────

        /** Time : O(log n) */
        public void insert(int key) { root = insert(root, key); }

        private AVLNode insert(AVLNode node, int key) {
            if (node == null) return new AVLNode(key);
            if      (key < node.key) node.left  = insert(node.left,  key);
            else if (key > node.key) node.right = insert(node.right, key);
            else                     return node;   // duplicate ignored

            updateHeight(node);
            int bf = balanceFactor(node);

            // LL
            if (bf > 1  && key < node.left.key)  return rotateRight(node);
            // RR
            if (bf < -1 && key > node.right.key) return rotateLeft(node);
            // LR
            if (bf > 1  && key > node.left.key) {
                node.left = rotateLeft(node.left);
                return rotateRight(node);
            }
            // RL
            if (bf < -1 && key < node.right.key) {
                node.right = rotateRight(node.right);
                return rotateLeft(node);
            }
            return node;
        }

        // ── Delete ────────────────────────────────────────────────────────────

        /** Time : O(log n) */
        public void delete(int key) { root = delete(root, key); }

        private AVLNode delete(AVLNode node, int key) {
            if (node == null) return null;
            if      (key < node.key) node.left  = delete(node.left,  key);
            else if (key > node.key) node.right = delete(node.right, key);
            else {
                if (node.left  == null) return node.right;
                if (node.right == null) return node.left;
                AVLNode successor = minNode(node.right);
                node.key   = successor.key;
                node.right = delete(node.right, successor.key);
            }

            updateHeight(node);
            int bf = balanceFactor(node);

            if (bf > 1  && balanceFactor(node.left)  >= 0) return rotateRight(node);
            if (bf > 1  && balanceFactor(node.left)  <  0) {
                node.left = rotateLeft(node.left);
                return rotateRight(node);
            }
            if (bf < -1 && balanceFactor(node.right) <= 0) return rotateLeft(node);
            if (bf < -1 && balanceFactor(node.right) >  0) {
                node.right = rotateRight(node.right);
                return rotateLeft(node);
            }
            return node;
        }

        private AVLNode minNode(AVLNode node) {
            while (node.left != null) node = node.left;
            return node;
        }

        // ── Search ────────────────────────────────────────────────────────────

        public boolean search(int key) {
            AVLNode node = root;
            while (node != null) {
                if      (key == node.key) return true;
                else if (key < node.key)  node = node.left;
                else                      node = node.right;
            }
            return false;
        }

        // ── Traversal & Utilities ─────────────────────────────────────────────

        public List<Integer> inorder() {
            List<Integer> result = new ArrayList<>();
            inorder(root, result);
            return result;
        }

        private void inorder(AVLNode node, List<Integer> result) {
            if (node == null) return;
            inorder(node.left,  result);
            result.add(node.key);
            inorder(node.right, result);
        }

        public int height() { return height(root); }

        public void display() { display(root, "", true); }

        private void display(AVLNode node, String prefix, boolean isLeft) {
            if (node == null) return;
            String connector = isLeft ? "├── " : "└── ";
            System.out.printf("%s%s%d (h=%d, bf=%d)%n",
                    prefix, connector, node.key, node.height, balanceFactor(node));
            String childPrefix = prefix + (isLeft ? "│   " : "    ");
            if (node.left != null || node.right != null) {
                display(node.left,  childPrefix, true);
                display(node.right, childPrefix, false);
            }
        }
    }


    // ══════════════════════════════════════════════════════════════════════════
    // 3. RED-BLACK TREE
    // ══════════════════════════════════════════════════════════════════════════

    static final boolean RED   = true;
    static final boolean BLACK = false;

    static class RBNode {
        int     key;
        boolean color;
        RBNode  left, right, parent;

        RBNode(int key) {
            this.key   = key;
            this.color = RED;
        }
    }

    static class RedBlackTree {
        /**
         * Red-Black Tree — relaxed self-balancing BST.
         *
         * Properties:
         *   1. Every node is RED or BLACK.
         *   2. Root is always BLACK.
         *   3. Every NULL leaf is BLACK.
         *   4. RED node's children must both be BLACK.
         *   5. Every root-to-null path has the same number of BLACK nodes.
         *
         * Guarantees height <= 2 * log2(n+1), giving O(log n) operations.
         * Used in: Linux CFS scheduler, Java TreeMap, C++ std::map.
         */

        private final RBNode NIL;
        private       RBNode root;

        RedBlackTree() {
            NIL        = new RBNode(0);
            NIL.color  = BLACK;
            NIL.left   = NIL;
            NIL.right  = NIL;
            root       = NIL;
        }

        // ── Rotations ─────────────────────────────────────────────────────────

        private void leftRotate(RBNode x) {
            RBNode y  = x.right;
            x.right   = y.left;
            if (y.left != NIL) y.left.parent = x;
            y.parent  = x.parent;
            if      (x.parent == null)         root           = y;
            else if (x == x.parent.left)       x.parent.left  = y;
            else                               x.parent.right = y;
            y.left    = x;
            x.parent  = y;
        }

        private void rightRotate(RBNode y) {
            RBNode x  = y.left;
            y.left    = x.right;
            if (x.right != NIL) x.right.parent = y;
            x.parent  = y.parent;
            if      (y.parent == null)         root           = x;
            else if (y == y.parent.left)       y.parent.left  = x;
            else                               y.parent.right = x;
            x.right   = y;
            y.parent  = x;
        }

        // ── Insert ────────────────────────────────────────────────────────────

        /** Time : O(log n) */
        public void insert(int key) {
            RBNode node   = new RBNode(key);
            node.left     = NIL;
            node.right    = NIL;
            node.color    = RED;

            RBNode parent  = null;
            RBNode current = root;

            while (current != NIL) {
                parent = current;
                if      (node.key < current.key) current = current.left;
                else if (node.key > current.key) current = current.right;
                else    return;   // duplicate ignored
            }

            node.parent = parent;
            if      (parent == null)          root          = node;
            else if (node.key < parent.key)   parent.left   = node;
            else                              parent.right  = node;

            if (node.parent == null)          { node.color = BLACK; return; }
            if (node.parent.parent == null)   return;

            fixInsert(node);
        }

        private void fixInsert(RBNode z) {
            while (z.parent != null && z.parent.color == RED) {
                if (z.parent == z.parent.parent.left) {
                    RBNode uncle = z.parent.parent.right;
                    if (uncle.color == RED) {
                        // Case 1: recolour
                        z.parent.color         = BLACK;
                        uncle.color            = BLACK;
                        z.parent.parent.color  = RED;
                        z = z.parent.parent;
                    } else {
                        if (z == z.parent.right) {
                            // Case 2: left rotate
                            z = z.parent;
                            leftRotate(z);
                        }
                        // Case 3: right rotate
                        z.parent.color        = BLACK;
                        z.parent.parent.color = RED;
                        rightRotate(z.parent.parent);
                    }
                } else {
                    RBNode uncle = z.parent.parent.left;
                    if (uncle.color == RED) {
                        z.parent.color        = BLACK;
                        uncle.color           = BLACK;
                        z.parent.parent.color = RED;
                        z = z.parent.parent;
                    } else {
                        if (z == z.parent.left) {
                            z = z.parent;
                            rightRotate(z);
                        }
                        z.parent.color        = BLACK;
                        z.parent.parent.color = RED;
                        leftRotate(z.parent.parent);
                    }
                }
                if (z == root) break;
            }
            root.color = BLACK;
        }

        // ── Delete ────────────────────────────────────────────────────────────

        /** Time : O(log n) */
        public void delete(int key) {
            RBNode node = find(root, key);
            if (node == NIL) return;
            deleteNode(node);
        }

        private RBNode find(RBNode node, int key) {
            while (node != NIL) {
                if      (key == node.key) return node;
                else if (key < node.key)  node = node.left;
                else                      node = node.right;
            }
            return NIL;
        }

        private void transplant(RBNode u, RBNode v) {
            if      (u.parent == null)        root          = v;
            else if (u == u.parent.left)      u.parent.left = v;
            else                              u.parent.right= v;
            v.parent = u.parent;
        }

        private void deleteNode(RBNode z) {
            RBNode y = z;
            boolean yOriginalColor = y.color;
            RBNode x;
            if (z.left == NIL) {
                x = z.right;
                transplant(z, z.right);
            } else if (z.right == NIL) {
                x = z.left;
                transplant(z, z.left);
            } else {
                y = minNode(z.right);
                yOriginalColor = y.color;
                x = y.right;
                if (y.parent == z) {
                    x.parent = y;
                } else {
                    transplant(y, y.right);
                    y.right         = z.right;
                    y.right.parent  = y;
                }
                transplant(z, y);
                y.left         = z.left;
                y.left.parent  = y;
                y.color        = z.color;
            }
            if (yOriginalColor == BLACK) fixDelete(x);
        }

        private void fixDelete(RBNode x) {
            while (x != root && x.color == BLACK) {
                if (x == x.parent.left) {
                    RBNode w = x.parent.right;
                    if (w.color == RED) {
                        w.color        = BLACK;
                        x.parent.color = RED;
                        leftRotate(x.parent);
                        w = x.parent.right;
                    }
                    if (w.left.color == BLACK && w.right.color == BLACK) {
                        w.color = RED;
                        x = x.parent;
                    } else {
                        if (w.right.color == BLACK) {
                            w.left.color = BLACK;
                            w.color      = RED;
                            rightRotate(w);
                            w = x.parent.right;
                        }
                        w.color        = x.parent.color;
                        x.parent.color = BLACK;
                        w.right.color  = BLACK;
                        leftRotate(x.parent);
                        x = root;
                    }
                } else {
                    RBNode w = x.parent.left;
                    if (w.color == RED) {
                        w.color        = BLACK;
                        x.parent.color = RED;
                        rightRotate(x.parent);
                        w = x.parent.left;
                    }
                    if (w.right.color == BLACK && w.left.color == BLACK) {
                        w.color = RED;
                        x = x.parent;
                    } else {
                        if (w.left.color == BLACK) {
                            w.right.color = BLACK;
                            w.color       = RED;
                            leftRotate(w);
                            w = x.parent.left;
                        }
                        w.color        = x.parent.color;
                        x.parent.color = BLACK;
                        w.left.color   = BLACK;
                        rightRotate(x.parent);
                        x = root;
                    }
                }
            }
            x.color = BLACK;
        }

        private RBNode minNode(RBNode node) {
            while (node.left != NIL) node = node.left;
            return node;
        }

        // ── Search & Traversal ────────────────────────────────────────────────

        public boolean search(int key) { return find(root, key) != NIL; }

        public List<Integer> inorder() {
            List<Integer> result = new ArrayList<>();
            inorder(root, result);
            return result;
        }

        private void inorder(RBNode node, List<Integer> result) {
            if (node == NIL) return;
            inorder(node.left,  result);
            result.add(node.key);
            inorder(node.right, result);
        }

        public int height() { return height(root); }

        private int height(RBNode node) {
            if (node == NIL) return 0;
            return 1 + Math.max(height(node.left), height(node.right));
        }

        public void display() { display(root, "", true); }

        private void display(RBNode node, String prefix, boolean isLeft) {
            if (node == NIL) return;
            String connector = isLeft ? "├── " : "└── ";
            String color     = node.color == RED ? "R" : "B";
            System.out.printf("%s%s%d(%s)%n", prefix, connector, node.key, color);
            String childPrefix = prefix + (isLeft ? "│   " : "    ");
            if (node.left != NIL || node.right != NIL) {
                display(node.left,  childPrefix, true);
                display(node.right, childPrefix, false);
            }
        }
    }


    // ══════════════════════════════════════════════════════════════════════════
    // DEMO
    // ══════════════════════════════════════════════════════════════════════════

    public static void main(String[] args) {

        // ── 1. General Tree ───────────────────────────────────
        System.out.println("=".repeat(60));
        System.out.println("  1. General Tree (N-ary)");
        System.out.println("=".repeat(60));

        GeneralTree gt = new GeneralTree("CEO");
        gt.insert("CTO",  "CEO");
        gt.insert("CFO",  "CEO");
        gt.insert("COO",  "CEO");
        gt.insert("Dev1", "CTO");
        gt.insert("Dev2", "CTO");
        gt.insert("QA1",  "CTO");
        gt.insert("Fin1", "CFO");
        gt.insert("Ops1", "COO");
        gt.insert("Ops2", "COO");

        System.out.println("\nTree structure:");
        gt.display();

        System.out.println("\nBFS (level order) : " + gt.bfs());
        System.out.println("DFS preorder      : " + gt.dfsPreorder());
        System.out.println("DFS postorder     : " + gt.dfsPostorder());
        System.out.println("Height            : " + gt.height());
        System.out.println("Depth of 'Dev1'   : " + gt.depth("Dev1"));
        System.out.println("Depth of 'CFO'    : " + gt.depth("CFO"));
        System.out.println("Search 'QA1'      : " + gt.search("QA1"));
        System.out.println("Search 'VP'       : " + gt.search("VP"));

        System.out.println("\nAfter deleting 'CTO' subtree:");
        gt.delete("CTO");
        gt.display();

        // ── 2. AVL Tree ───────────────────────────────────────
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  2. AVL Tree (Self-Balancing BST)");
        System.out.println("=".repeat(60));

        AVLTree avl = new AVLTree();
        for (int k : new int[]{10, 20, 30, 40, 50, 25}) {
            avl.insert(k);
            System.out.printf("  Inserted %2d | height=%d | inorder=%s%n",
                    k, avl.height(), avl.inorder());
        }

        System.out.println("\nAVL Tree structure (key, height h, balance factor bf):");
        avl.display();

        System.out.println("\nSearch 30 : " + avl.search(30));
        System.out.println("Search 99 : " + avl.search(99));

        System.out.println("\nDeleting 40:");
        avl.delete(40);
        avl.display();
        System.out.println("Inorder after delete: " + avl.inorder());

        // ── 3. Red-Black Tree ─────────────────────────────────
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  3. Red-Black Tree");
        System.out.println("=".repeat(60));

        RedBlackTree rbt = new RedBlackTree();
        for (int k : new int[]{10, 20, 30, 15, 25, 5, 1}) {
            rbt.insert(k);
            System.out.printf("  Inserted %2d | height=%d | inorder=%s%n",
                    k, rbt.height(), rbt.inorder());
        }

        System.out.println("\nRed-Black Tree structure (R=Red, B=Black):");
        rbt.display();

        System.out.println("\nSearch 15 : " + rbt.search(15));
        System.out.println("Search 99 : " + rbt.search(99));

        System.out.println("\nDeleting 20:");
        rbt.delete(20);
        rbt.display();
        System.out.println("Inorder after delete: " + rbt.inorder());

        // ── Comparison Table ──────────────────────────────────
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  Tree Comparison");
        System.out.println("=".repeat(60));
        System.out.printf("%-20s %-12s %-12s %-12s %s%n",
                "Structure", "Insert", "Search", "Delete", "Balance");
        System.out.println("-".repeat(60));
        System.out.printf("%-20s %-12s %-12s %-12s %s%n",
                "General Tree", "O(n)", "O(n)", "O(n)", "None");
        System.out.printf("%-20s %-12s %-12s %-12s %s%n",
                "BST (plain)", "O(h)", "O(h)", "O(h)", "None (O(n) worst)");
        System.out.printf("%-20s %-12s %-12s %-12s %s%n",
                "AVL Tree", "O(log n)", "O(log n)", "O(log n)", "Strict (bf<=1)");
        System.out.printf("%-20s %-12s %-12s %-12s %s%n",
                "Red-Black Tree", "O(log n)", "O(log n)", "O(log n)", "Relaxed (h<=2logn)");
    }
}
