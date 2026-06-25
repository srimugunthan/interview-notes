import java.util.*;

/**
 * Binary Search Tree (BST) Implementation in Java
 * =================================================
 * BST property : for every node N,
 *   - all keys in N's left subtree  < N.key
 *   - all keys in N's right subtree > N.key
 *
 * Supported Operations:
 *   insert(key)           - Insert a key              O(h)
 *   search(key)           - Find a key                O(h)
 *   delete(key)           - Remove a key              O(h)
 *   inorder()             - Sorted traversal          O(n)
 *   preorder()            - Root → Left → Right       O(n)
 *   postorder()           - Left → Right → Root       O(n)
 *   levelOrder()          - BFS level by level        O(n)
 *   minVal()              - Minimum key               O(h)
 *   maxVal()              - Maximum key               O(h)
 *   height()              - Tree height               O(n)
 *   isValidBST()          - Validate BST property     O(n)
 *   kthSmallest(k)        - Kth smallest element      O(h + k)
 *   floor(key)            - Largest key <= given key  O(h)
 *   ceil(key)             - Smallest key >= given key O(h)
 *
 *   h = height (O(log n) balanced, O(n) worst case)
 */
public class BST {

    // ── BST Node ──────────────────────────────────────────────────────────────

    private static class Node {
        int  key;
        Node left, right;

        Node(int key) {
            this.key = key;
        }
    }

    // ── Fields ────────────────────────────────────────────────────────────────

    private Node root;
    private int  kthCount;   // helper counter for kthSmallest

    // ── Insert ────────────────────────────────────────────────────────────────

    /** Time : O(h) */
    public void insert(int key) {
        root = insert(root, key);
    }

    private Node insert(Node node, int key) {
        if (node == null) return new Node(key);
        if      (key < node.key) node.left  = insert(node.left,  key);
        else if (key > node.key) node.right = insert(node.right, key);
        // duplicate keys are ignored
        return node;
    }

    // ── Search ────────────────────────────────────────────────────────────────

    /** Time : O(h) */
    public boolean search(int key) {
        return search(root, key);
    }

    private boolean search(Node node, int key) {
        if (node == null)      return false;
        if (key == node.key)   return true;
        if (key < node.key)    return search(node.left,  key);
        return                        search(node.right, key);
    }

    // ── Delete ────────────────────────────────────────────────────────────────

    /**
     * Three cases:
     *   1. Node is a leaf        -> remove directly
     *   2. One child             -> replace with child
     *   3. Two children          -> replace with in-order successor
     * Time : O(h)
     */
    public void delete(int key) {
        root = delete(root, key);
    }

    private Node delete(Node node, int key) {
        if (node == null) return null;
        if      (key < node.key) node.left  = delete(node.left,  key);
        else if (key > node.key) node.right = delete(node.right, key);
        else {
            // Case 1 & 2
            if (node.left  == null) return node.right;
            if (node.right == null) return node.left;
            // Case 3 : in-order successor
            Node successor = minNode(node.right);
            node.key       = successor.key;
            node.right     = delete(node.right, successor.key);
        }
        return node;
    }

    // ── Traversals ────────────────────────────────────────────────────────────

    /** Left -> Root -> Right (returns sorted order). Time : O(n) */
    public List<Integer> inorder() {
        List<Integer> result = new ArrayList<>();
        inorder(root, result);
        return result;
    }

    private void inorder(Node node, List<Integer> result) {
        if (node == null) return;
        inorder(node.left,  result);
        result.add(node.key);
        inorder(node.right, result);
    }

    /** Root -> Left -> Right. Time : O(n) */
    public List<Integer> preorder() {
        List<Integer> result = new ArrayList<>();
        preorder(root, result);
        return result;
    }

    private void preorder(Node node, List<Integer> result) {
        if (node == null) return;
        result.add(node.key);
        preorder(node.left,  result);
        preorder(node.right, result);
    }

    /** Left -> Right -> Root. Time : O(n) */
    public List<Integer> postorder() {
        List<Integer> result = new ArrayList<>();
        postorder(root, result);
        return result;
    }

    private void postorder(Node node, List<Integer> result) {
        if (node == null) return;
        postorder(node.left,  result);
        postorder(node.right, result);
        result.add(node.key);
    }

    /** BFS level by level. Time : O(n) */
    public List<List<Integer>> levelOrder() {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;
        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            List<Integer> level = new ArrayList<>();
            int sz = queue.size();
            for (int i = 0; i < sz; i++) {
                Node node = queue.poll();
                level.add(node.key);
                if (node.left  != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
            result.add(level);
        }
        return result;
    }

    // ── Min / Max ─────────────────────────────────────────────────────────────

    /** Return minimum key. Time : O(h) */
    public int minVal() {
        if (root == null) throw new RuntimeException("Tree is empty");
        return minNode(root).key;
    }

    private Node minNode(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    /** Return maximum key. Time : O(h) */
    public int maxVal() {
        if (root == null) throw new RuntimeException("Tree is empty");
        Node node = root;
        while (node.right != null) node = node.right;
        return node.key;
    }

    // ── Height ────────────────────────────────────────────────────────────────

    /** Return height of the tree (-1 for empty, 0 for single node). Time : O(n) */
    public int height() {
        return height(root);
    }

    private int height(Node node) {
        if (node == null) return -1;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    // ── BST Validation ────────────────────────────────────────────────────────

    /** Validate BST property using min/max bounds. Time : O(n) */
    public boolean isValidBST() {
        return validate(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private boolean validate(Node node, int minVal, int maxVal) {
        if (node == null) return true;
        if (node.key <= minVal || node.key >= maxVal) return false;
        return validate(node.left,  minVal,   node.key) &&
               validate(node.right, node.key, maxVal);
    }

    // ── Kth Smallest ──────────────────────────────────────────────────────────

    /** Return the kth smallest element. Time : O(h + k) */
    public int kthSmallest(int k) {
        kthCount = 0;
        Integer result = kthSmallest(root, k);
        if (result == null) throw new RuntimeException("k out of range");
        return result;
    }

    private Integer kthSmallest(Node node, int k) {
        if (node == null) return null;
        Integer left = kthSmallest(node.left, k);
        if (left != null) return left;
        if (++kthCount == k) return node.key;
        return kthSmallest(node.right, k);
    }

    // ── Floor and Ceil ────────────────────────────────────────────────────────

    /** Largest key <= given key. Returns Integer.MIN_VALUE if none exists. */
    public int floor(int key) {
        Node result = floor(root, key);
        if (result == null) throw new RuntimeException("No floor found");
        return result.key;
    }

    private Node floor(Node node, int key) {
        if (node == null) return null;
        if (key == node.key)  return node;
        if (key < node.key)   return floor(node.left, key);
        Node right = floor(node.right, key);
        return right != null ? right : node;
    }

    /** Smallest key >= given key. */
    public int ceil(int key) {
        Node result = ceil(root, key);
        if (result == null) throw new RuntimeException("No ceil found");
        return result.key;
    }

    private Node ceil(Node node, int key) {
        if (node == null) return null;
        if (key == node.key)  return node;
        if (key > node.key)   return ceil(node.right, key);
        Node left = ceil(node.left, key);
        return left != null ? left : node;
    }

    // ── Display ───────────────────────────────────────────────────────────────

    public void display() {
        display(root, "", true);
    }

    private void display(Node node, String prefix, boolean isLeft) {
        if (node == null) return;
        String connector = isLeft ? "|-- " : "\\-- ";
        System.out.println(prefix + connector + node.key);
        String newPrefix = prefix + (isLeft ? "|   " : "    ");
        display(node.left,  newPrefix, true);
        display(node.right, newPrefix, false);
    }

    // ── Demo / Tests ──────────────────────────────────────────────────────────

    public static void main(String[] args) {

        System.out.println("=".repeat(55));
        System.out.println("  Binary Search Tree Demo");
        System.out.println("=".repeat(55));

        BST bst = new BST();
        for (int k : new int[]{50, 30, 70, 20, 40, 60, 80, 10, 25, 35, 45}) {
            bst.insert(k);
        }

        // ── Traversals ────────────────────────────────────────
        System.out.println("\n── Traversals ──");
        System.out.println("Inorder   (sorted) : " + bst.inorder());
        System.out.println("Preorder           : " + bst.preorder());
        System.out.println("Postorder          : " + bst.postorder());
        System.out.println("Level order        : " + bst.levelOrder());

        // ── Search ────────────────────────────────────────────
        System.out.println("\n── Search ──");
        System.out.println("search(40) : " + bst.search(40));  // true
        System.out.println("search(99) : " + bst.search(99));  // false

        // ── Min / Max / Height ────────────────────────────────
        System.out.println("\n── Min / Max / Height ──");
        System.out.println("Min    : " + bst.minVal());         // 10
        System.out.println("Max    : " + bst.maxVal());         // 80
        System.out.println("Height : " + bst.height());         // 3

        // ── Validation ────────────────────────────────────────
        System.out.println("\n── BST Validation ──");
        System.out.println("Is valid BST : " + bst.isValidBST()); // true

        // ── Kth Smallest ──────────────────────────────────────
        System.out.println("\n── Kth Smallest ──");
        System.out.println("1st smallest : " + bst.kthSmallest(1));  // 10
        System.out.println("3rd smallest : " + bst.kthSmallest(3));  // 25
        System.out.println("5th smallest : " + bst.kthSmallest(5));  // 35

        // ── Floor and Ceil ────────────────────────────────────
        System.out.println("\n── Floor / Ceil ──");
        System.out.println("floor(55) : " + bst.floor(55));    // 50
        System.out.println("ceil(55)  : " + bst.ceil(55));     // 60
        System.out.println("floor(20) : " + bst.floor(20));    // 20
        System.out.println("ceil(20)  : " + bst.ceil(20));     // 20

        // ── Delete ────────────────────────────────────────────
        System.out.println("\n── Delete ──");
        System.out.println("Before               : " + bst.inorder());
        bst.delete(20);
        System.out.println("After del 20 (leaf)          : " + bst.inorder());
        bst.delete(30);
        System.out.println("After del 30 (two children)  : " + bst.inorder());
        bst.delete(70);
        System.out.println("After del 70 (two children)  : " + bst.inorder());

        // ── Tree Structure ────────────────────────────────────
        System.out.println("\n── Tree Structure ──");
        BST bst2 = new BST();
        for (int k : new int[]{50, 30, 70, 20, 40, 60, 80}) bst2.insert(k);
        bst2.display();
    }
}
