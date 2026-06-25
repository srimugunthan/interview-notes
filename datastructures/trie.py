"""
Trie (Prefix Tree) Implementation in Python
=============================================
A Trie is a tree where each node represents one character.
Paths from root to a marked node spell out stored words.

Supported Operations:
  insert(word)            - Insert a word              O(m)
  search(word)            - Exact word lookup           O(m)
  starts_with(prefix)     - Prefix existence check      O(m)
  delete(word)            - Remove a word               O(m)
  autocomplete(prefix)    - All words with given prefix  O(m + k)
  count_words_with(prefix)- Count words under prefix     O(m + k)
  longest_common_prefix() - LCP of all stored words      O(m * n)

  m = length of word/prefix
  k = number of matching words
"""


# ── Trie Node ─────────────────────────────────────────────────────────────────

class TrieNode:
    def __init__(self):
        self.children   = {}    # char -> TrieNode
        self.is_end     = False  # True if a word ends at this node
        self.word_count = 0      # number of words passing through this node


# ── Trie ──────────────────────────────────────────────────────────────────────

class Trie:
    def __init__(self):
        self.root       = TrieNode()
        self._num_words = 0

    # ── Insert ────────────────────────────────────────────────────────────────

    def insert(self, word: str) -> None:
        """
        Insert a word into the Trie.
        Time  : O(m)   m = len(word)
        Space : O(m)   worst case (no shared prefix)
        """
        node = self.root
        for char in word:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
            node.word_count += 1

        if not node.is_end:
            node.is_end = True
            self._num_words += 1

    # ── Search ────────────────────────────────────────────────────────────────

    def search(self, word: str) -> bool:
        """
        Return True if word exists exactly in the Trie.
        Time  : O(m)
        """
        node = self._find_node(word)
        return node is not None and node.is_end

    # ── Starts With ───────────────────────────────────────────────────────────

    def starts_with(self, prefix: str) -> bool:
        """
        Return True if any stored word starts with prefix.
        Time  : O(m)
        """
        return self._find_node(prefix) is not None

    # ── Delete ────────────────────────────────────────────────────────────────

    def delete(self, word: str) -> bool:
        """
        Remove a word from the Trie.
        Prunes nodes that are no longer needed.
        Returns True if word was found and deleted, False otherwise.
        Time  : O(m)
        """
        if not self.search(word):
            return False
        self._delete_helper(self.root, word, 0)
        self._num_words -= 1
        return True

    def _delete_helper(self, node: TrieNode, word: str, depth: int) -> bool:
        """Recursively delete and prune unused nodes. Returns True if node should be deleted."""
        if depth == len(word):
            node.is_end = False
            return len(node.children) == 0  # prune if leaf

        char = word[depth]
        child = node.children.get(char)
        if child is None:
            return False

        child.word_count -= 1
        should_delete = self._delete_helper(child, word, depth + 1)

        if should_delete:
            del node.children[char]

        return not node.is_end and len(node.children) == 0

    # ── Autocomplete ──────────────────────────────────────────────────────────

    def autocomplete(self, prefix: str) -> list:
        """
        Return all words in the Trie that start with prefix.
        Time  : O(m + k)   k = number of matching words
        """
        results = []
        node = self._find_node(prefix)
        if node is None:
            return results
        self._dfs(node, prefix, results)
        return sorted(results)

    def _dfs(self, node: TrieNode, current: str, results: list) -> None:
        if node.is_end:
            results.append(current)
        for char, child in node.children.items():
            self._dfs(child, current + char, results)

    # ── Count Words With Prefix ───────────────────────────────────────────────

    def count_words_with(self, prefix: str) -> int:
        """
        Count how many stored words start with prefix.
        Time  : O(m)   uses word_count stored at each node
        """
        node = self._find_node(prefix)
        if node is None:
            return 0
        # word_count on the last prefix node = number of words through it
        return node.word_count

    # ── Longest Common Prefix ─────────────────────────────────────────────────

    def longest_common_prefix(self) -> str:
        """
        Return the longest common prefix shared by all stored words.
        Time  : O(m)   m = length of LCP
        """
        prefix = []
        node   = self.root
        while True:
            # Stop if this node is the end of a word or branches into multiple children
            if node.is_end or len(node.children) != 1:
                break
            char        = next(iter(node.children))
            prefix.append(char)
            node        = node.children[char]
        return "".join(prefix)

    # ── Utilities ─────────────────────────────────────────────────────────────

    def _find_node(self, prefix: str):
        """Traverse to the node at the end of prefix. Returns None if not found."""
        node = self.root
        for char in prefix:
            if char not in node.children:
                return None
            node = node.children[char]
        return node

    def total_words(self) -> int:
        return self._num_words

    def display(self, node=None, prefix="", indent=0) -> None:
        """Print the Trie structure."""
        if node is None:
            node = self.root
            print("root")
        for char, child in sorted(node.children.items()):
            marker = " [END]" if child.is_end else ""
            print("  " * (indent + 1) + f"|-- {char}{marker}  (count={child.word_count})")
            self.display(child, prefix + char, indent + 1)


# ── Demo / Tests ──────────────────────────────────────────────────────────────

if __name__ == "__main__":

    print("=" * 55)
    print("  Trie Demo")
    print("=" * 55)

    trie = Trie()

    # ── Insert ────────────────────────────────────────────────
    print("\n── Insert words ──")
    words = ["apple", "app", "application", "apply",
             "bat", "ball", "band", "bandana", "cat"]
    for w in words:
        trie.insert(w)
    print(f"Inserted: {words}")
    print(f"Total words: {trie.total_words()}")

    # ── Search ────────────────────────────────────────────────
    print("\n── Search (exact match) ──")
    print(trie.search("app"))           # True
    print(trie.search("ap"))            # False (not inserted as word)
    print(trie.search("apple"))         # True
    print(trie.search("apples"))        # False

    # ── Starts With ───────────────────────────────────────────
    print("\n── Starts With (prefix check) ──")
    print(trie.starts_with("app"))      # True
    print(trie.starts_with("ban"))      # True
    print(trie.starts_with("xyz"))      # False

    # ── Autocomplete ──────────────────────────────────────────
    print("\n── Autocomplete ──")
    print("Prefix 'app' :", trie.autocomplete("app"))    # app, apple, application, apply
    print("Prefix 'ba'  :", trie.autocomplete("ba"))     # ball, band, bandana, bat
    print("Prefix 'ca'  :", trie.autocomplete("ca"))     # cat
    print("Prefix 'xyz' :", trie.autocomplete("xyz"))    # []

    # ── Count Words With Prefix ───────────────────────────────
    print("\n── Count Words With Prefix ──")
    print("'app' ->", trie.count_words_with("app"))      # 4
    print("'ba'  ->", trie.count_words_with("ba"))       # 4
    print("'xyz' ->", trie.count_words_with("xyz"))      # 0

    # ── Longest Common Prefix ─────────────────────────────────
    print("\n── Longest Common Prefix ──")
    t2 = Trie()
    for w in ["flower", "flow", "flight"]:
        t2.insert(w)
    print("flower, flow, flight ->", t2.longest_common_prefix())   # fl

    t3 = Trie()
    for w in ["dog", "racecar", "car"]:
        t3.insert(w)
    print("dog, racecar, car    ->", repr(t3.longest_common_prefix()))  # ''

    # ── Delete ────────────────────────────────────────────────
    print("\n── Delete ──")
    print("Before delete 'app' :", trie.autocomplete("app"))
    print("Delete 'app'        :", trie.delete("app"))       # True
    print("After  delete 'app' :", trie.autocomplete("app"))
    print("search 'app'        :", trie.search("app"))       # False
    print("search 'apple'      :", trie.search("apple"))     # True (still intact)
    print("Delete 'xyz'        :", trie.delete("xyz"))       # False

    # ── Trie Structure ────────────────────────────────────────
    print("\n── Trie Structure ──")
    small = Trie()
    for w in ["cat", "car", "card", "care", "bat"]:
        small.insert(w)
    small.display()
