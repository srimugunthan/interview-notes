import java.util.*;

/**
 * Trie (Prefix Tree) Implementation in Java
 * ===========================================
 * A Trie is a tree where each node represents one character.
 * Paths from root to a marked node spell out stored words.
 *
 * Supported Operations:
 *   insert(word)              - Insert a word                O(m)
 *   search(word)              - Exact word lookup             O(m)
 *   startsWith(prefix)        - Prefix existence check        O(m)
 *   delete(word)              - Remove a word                 O(m)
 *   autocomplete(prefix)      - All words with given prefix   O(m + k)
 *   countWordsWith(prefix)    - Count words under prefix      O(m)
 *   longestCommonPrefix()     - LCP of all stored words       O(m)
 *
 *   m = length of word/prefix
 *   k = number of matching words
 */
public class Trie {

    // ── Trie Node ─────────────────────────────────────────────────────────────

    private static class TrieNode {
        Map<Character, TrieNode> children;
        boolean isEnd;
        int     wordCount;  // number of words passing through this node

        TrieNode() {
            children  = new HashMap<>();
            isEnd     = false;
            wordCount = 0;
        }
    }

    // ── Fields ────────────────────────────────────────────────────────────────

    private final TrieNode root;
    private int numWords;

    // ── Constructor ───────────────────────────────────────────────────────────

    public Trie() {
        root     = new TrieNode();
        numWords = 0;
    }

    // ── Insert ────────────────────────────────────────────────────────────────

    /**
     * Insert a word into the Trie.
     * Time  : O(m)   m = word length
     * Space : O(m)   worst case (no shared prefix)
     */
    public void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
            node.wordCount++;
        }
        if (!node.isEnd) {
            node.isEnd = true;
            numWords++;
        }
    }

    // ── Search ────────────────────────────────────────────────────────────────

    /**
     * Return true if word exists exactly in the Trie.
     * Time  : O(m)
     */
    public boolean search(String word) {
        TrieNode node = findNode(word);
        return node != null && node.isEnd;
    }

    // ── Starts With ───────────────────────────────────────────────────────────

    /**
     * Return true if any stored word starts with prefix.
     * Time  : O(m)
     */
    public boolean startsWith(String prefix) {
        return findNode(prefix) != null;
    }

    // ── Delete ────────────────────────────────────────────────────────────────

    /**
     * Remove a word from the Trie.
     * Prunes nodes that are no longer needed.
     * Returns true if word was found and deleted, false otherwise.
     * Time  : O(m)
     */
    public boolean delete(String word) {
        if (!search(word)) return false;
        deleteHelper(root, word, 0);
        numWords--;
        return true;
    }

    /** Recursively delete and prune unused nodes. Returns true if node should be deleted. */
    private boolean deleteHelper(TrieNode node, String word, int depth) {
        if (depth == word.length()) {
            node.isEnd = false;
            return node.children.isEmpty();
        }

        char     c     = word.charAt(depth);
        TrieNode child = node.children.get(c);
        if (child == null) return false;

        child.wordCount--;
        boolean shouldDelete = deleteHelper(child, word, depth + 1);

        if (shouldDelete) {
            node.children.remove(c);
        }

        return !node.isEnd && node.children.isEmpty();
    }

    // ── Autocomplete ──────────────────────────────────────────────────────────

    /**
     * Return all words in the Trie that start with prefix.
     * Time  : O(m + k)   k = number of matching words
     */
    public List<String> autocomplete(String prefix) {
        List<String> results = new ArrayList<>();
        TrieNode     node    = findNode(prefix);
        if (node == null) return results;
        dfs(node, new StringBuilder(prefix), results);
        Collections.sort(results);
        return results;
    }

    private void dfs(TrieNode node, StringBuilder current, List<String> results) {
        if (node.isEnd) {
            results.add(current.toString());
        }
        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            current.append(entry.getKey());
            dfs(entry.getValue(), current, results);
            current.deleteCharAt(current.length() - 1);  // backtrack
        }
    }

    // ── Count Words With Prefix ───────────────────────────────────────────────

    /**
     * Count how many stored words start with prefix.
     * Time  : O(m)   uses wordCount stored at each node
     */
    public int countWordsWith(String prefix) {
        TrieNode node = findNode(prefix);
        return node == null ? 0 : node.wordCount;
    }

    // ── Longest Common Prefix ─────────────────────────────────────────────────

    /**
     * Return the longest common prefix shared by all stored words.
     * Time  : O(m)   m = length of LCP
     */
    public String longestCommonPrefix() {
        StringBuilder lcp  = new StringBuilder();
        TrieNode      node = root;
        while (true) {
            if (node.isEnd || node.children.size() != 1) break;
            Map.Entry<Character, TrieNode> entry = node.children.entrySet().iterator().next();
            lcp.append(entry.getKey());
            node = entry.getValue();
        }
        return lcp.toString();
    }

    // ── Utilities ─────────────────────────────────────────────────────────────

    /** Traverse to the node at the end of prefix. Returns null if not found. */
    private TrieNode findNode(String prefix) {
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) return null;
            node = node.children.get(c);
        }
        return node;
    }

    public int totalWords() {
        return numWords;
    }

    /** Print the Trie structure. */
    public void display() {
        System.out.println("root");
        display(root, 0);
    }

    private void display(TrieNode node, int indent) {
        List<Character> keys = new ArrayList<>(node.children.keySet());
        Collections.sort(keys);
        for (char c : keys) {
            TrieNode child  = node.children.get(c);
            String   marker = child.isEnd ? " [END]" : "";
            System.out.println("  ".repeat(indent + 1) + "|-- " + c + marker
                    + "  (count=" + child.wordCount + ")");
            display(child, indent + 1);
        }
    }

    // ── Demo / Tests ──────────────────────────────────────────────────────────

    public static void main(String[] args) {

        System.out.println("=".repeat(55));
        System.out.println("  Trie Demo");
        System.out.println("=".repeat(55));

        Trie trie = new Trie();

        // ── Insert ────────────────────────────────────────────
        System.out.println("\n── Insert words ──");
        String[] words = {"apple", "app", "application", "apply",
                          "bat", "ball", "band", "bandana", "cat"};
        for (String w : words) trie.insert(w);
        System.out.println("Inserted: " + Arrays.toString(words));
        System.out.println("Total words: " + trie.totalWords());

        // ── Search ────────────────────────────────────────────
        System.out.println("\n── Search (exact match) ──");
        System.out.println(trie.search("app"));         // true
        System.out.println(trie.search("ap"));          // false
        System.out.println(trie.search("apple"));       // true
        System.out.println(trie.search("apples"));      // false

        // ── Starts With ───────────────────────────────────────
        System.out.println("\n── Starts With (prefix check) ──");
        System.out.println(trie.startsWith("app"));     // true
        System.out.println(trie.startsWith("ban"));     // true
        System.out.println(trie.startsWith("xyz"));     // false

        // ── Autocomplete ──────────────────────────────────────
        System.out.println("\n── Autocomplete ──");
        System.out.println("Prefix 'app' : " + trie.autocomplete("app"));   // [app, apple, application, apply]
        System.out.println("Prefix 'ba'  : " + trie.autocomplete("ba"));    // [ball, band, bandana, bat]
        System.out.println("Prefix 'ca'  : " + trie.autocomplete("ca"));    // [cat]
        System.out.println("Prefix 'xyz' : " + trie.autocomplete("xyz"));   // []

        // ── Count Words With Prefix ───────────────────────────
        System.out.println("\n── Count Words With Prefix ──");
        System.out.println("'app' -> " + trie.countWordsWith("app"));       // 4
        System.out.println("'ba'  -> " + trie.countWordsWith("ba"));        // 4
        System.out.println("'xyz' -> " + trie.countWordsWith("xyz"));       // 0

        // ── Longest Common Prefix ─────────────────────────────
        System.out.println("\n── Longest Common Prefix ──");
        Trie t2 = new Trie();
        for (String w : new String[]{"flower", "flow", "flight"}) t2.insert(w);
        System.out.println("flower, flow, flight -> " + t2.longestCommonPrefix());  // fl

        Trie t3 = new Trie();
        for (String w : new String[]{"dog", "racecar", "car"}) t3.insert(w);
        System.out.println("dog, racecar, car    -> '" + t3.longestCommonPrefix() + "'"); // ''

        // ── Delete ────────────────────────────────────────────
        System.out.println("\n── Delete ──");
        System.out.println("Before delete 'app' : " + trie.autocomplete("app"));
        System.out.println("Delete 'app'        : " + trie.delete("app"));      // true
        System.out.println("After  delete 'app' : " + trie.autocomplete("app"));
        System.out.println("search 'app'        : " + trie.search("app"));      // false
        System.out.println("search 'apple'      : " + trie.search("apple"));    // true
        System.out.println("Delete 'xyz'        : " + trie.delete("xyz"));      // false

        // ── Trie Structure ────────────────────────────────────
        System.out.println("\n── Trie Structure ──");
        Trie small = new Trie();
        for (String w : new String[]{"cat", "car", "card", "care", "bat"}) small.insert(w);
        small.display();
    }
}
