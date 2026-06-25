"""
Bloom Filter Implementation in Python
=======================================
A Bloom filter is a space-efficient probabilistic data structure
that tests whether an element is a member of a set.

Key properties:
  - False positives ARE possible  (may say "present" when it isn't)
  - False negatives are IMPOSSIBLE (never says "absent" when it is)
  - No deletion in standard Bloom filter (use Counting Bloom filter for that)
  - Space usage is O(m) bits regardless of number of elements

How it works:
  - Maintain a bit array of size m (all zeros initially)
  - Use k independent hash functions
  - INSERT : hash element k times -> set those k bit positions to 1
  - LOOKUP : hash element k times -> if ALL k positions are 1, "probably present"
                                     if ANY position is 0, "definitely absent"

Optimal parameters:
  m = -(n * ln(p)) / (ln(2)^2)     bit array size
  k = (m / n) * ln(2)              number of hash functions

  n = expected number of elements
  p = desired false positive rate

Included:
  BloomFilter               - standard Bloom filter
  CountingBloomFilter       - supports deletion (uses counters instead of bits)
"""

import math
import hashlib


# ── Bloom Filter ──────────────────────────────────────────────────────────────

class BloomFilter:
    def __init__(self, expected_elements: int, false_positive_rate: float = 0.01):
        """
        Args:
            expected_elements   : n — how many items you plan to insert
            false_positive_rate : p — acceptable false positive probability (0 < p < 1)
        """
        if not (0 < false_positive_rate < 1):
            raise ValueError("false_positive_rate must be between 0 and 1")
        if expected_elements <= 0:
            raise ValueError("expected_elements must be positive")

        self.n = expected_elements
        self.p = false_positive_rate

        # Optimal bit array size
        self.m = self._optimal_m(expected_elements, false_positive_rate)

        # Optimal number of hash functions
        self.k = self._optimal_k(self.m, expected_elements)

        # Bit array stored as a bytearray (8 bits per byte)
        self._bits    = bytearray(math.ceil(self.m / 8))
        self._count   = 0   # number of inserted elements

        print(f"BloomFilter initialised:")
        print(f"  expected elements    n = {self.n}")
        print(f"  false positive rate  p = {self.p}")
        print(f"  bit array size       m = {self.m} bits ({self.m / 8 / 1024:.2f} KB)")
        print(f"  hash functions       k = {self.k}")

    # ── Optimal parameter formulas ────────────────────────────────────────────

    @staticmethod
    def _optimal_m(n: int, p: float) -> int:
        """m = -(n * ln(p)) / (ln 2)^2"""
        return math.ceil(-(n * math.log(p)) / (math.log(2) ** 2))

    @staticmethod
    def _optimal_k(m: int, n: int) -> int:
        """k = (m / n) * ln(2)"""
        return max(1, round((m / n) * math.log(2)))

    # ── Hash functions ────────────────────────────────────────────────────────

    def _hash_positions(self, item: str) -> list:
        """
        Generate k bit positions for the given item.
        Uses double hashing: pos_i = (h1 + i * h2) % m
        This avoids needing k independent hash functions.
        """
        encoded = item.encode("utf-8")

        h1 = int(hashlib.md5(encoded).hexdigest(),    16) % self.m
        h2 = int(hashlib.sha256(encoded).hexdigest(), 16) % self.m

        return [(h1 + i * h2) % self.m for i in range(self.k)]

    # ── Bit array helpers ─────────────────────────────────────────────────────

    def _set_bit(self, pos: int) -> None:
        self._bits[pos // 8] |= (1 << (pos % 8))

    def _get_bit(self, pos: int) -> bool:
        return bool(self._bits[pos // 8] & (1 << (pos % 8)))

    # ── Core API ──────────────────────────────────────────────────────────────

    def add(self, item) -> None:
        """
        Insert item into the Bloom filter.
        Time  : O(k)
        Space : O(1)  (always sets exactly k bits)
        """
        item = str(item)
        for pos in self._hash_positions(item):
            self._set_bit(pos)
        self._count += 1

    def contains(self, item) -> bool:
        """
        Check membership.
        Returns:
          False -> item is DEFINITELY NOT in the set
          True  -> item is PROBABLY in the set (may be false positive)
        Time : O(k)
        """
        item = str(item)
        return all(self._get_bit(pos) for pos in self._hash_positions(item))

    # ── Diagnostics ───────────────────────────────────────────────────────────

    def current_false_positive_rate(self) -> float:
        """
        Actual FPR given current fill level.
        FPR = (1 - e^(-k * n / m)) ^ k
        """
        exponent = -self.k * self._count / self.m
        return (1 - math.exp(exponent)) ** self.k

    def bit_array_fill(self) -> float:
        """Fraction of bits currently set to 1."""
        set_bits = sum(bin(b).count("1") for b in self._bits)
        return set_bits / self.m

    def stats(self) -> None:
        print(f"\nBloomFilter Stats:")
        print(f"  inserted elements    : {self._count}")
        print(f"  bit array size       : {self.m} bits")
        print(f"  hash functions (k)   : {self.k}")
        print(f"  bits set (fill %)    : {self.bit_array_fill() * 100:.1f}%")
        print(f"  current FP rate      : {self.current_false_positive_rate() * 100:.4f}%")
        print(f"  theoretical FP rate  : {self.p * 100:.2f}%")

    def __repr__(self):
        return (f"BloomFilter(n={self.n}, p={self.p}, "
                f"m={self.m}, k={self.k}, inserted={self._count})")


# ── Counting Bloom Filter (supports deletion) ─────────────────────────────────

class CountingBloomFilter:
    """
    Extension of Bloom filter that uses integer counters instead of bits.
    Allows deletion at the cost of more memory.

    Each counter is incremented on add and decremented on remove.
    If any counter reaches 0 on remove, the bit is effectively cleared.

    Memory: O(m * counter_bits) instead of O(m) bits.
    """

    def __init__(self, expected_elements: int, false_positive_rate: float = 0.01,
                 max_count: int = 255):
        self.n         = expected_elements
        self.p         = false_positive_rate
        self.max_count = max_count

        self.m = BloomFilter._optimal_m(expected_elements, false_positive_rate)
        self.k = BloomFilter._optimal_k(self.m, expected_elements)

        self._counters = [0] * self.m
        self._count    = 0

        print(f"\nCountingBloomFilter initialised:")
        print(f"  bit array size  m = {self.m}")
        print(f"  hash functions  k = {self.k}")

    def _hash_positions(self, item: str) -> list:
        encoded = item.encode("utf-8")
        h1 = int(hashlib.md5(encoded).hexdigest(),    16) % self.m
        h2 = int(hashlib.sha256(encoded).hexdigest(), 16) % self.m
        return [(h1 + i * h2) % self.m for i in range(self.k)]

    def add(self, item) -> None:
        """Time : O(k)"""
        item = str(item)
        for pos in self._hash_positions(item):
            if self._counters[pos] < self.max_count:
                self._counters[pos] += 1
        self._count += 1

    def remove(self, item) -> bool:
        """
        Remove item from the filter.
        Returns False if item was definitely never added.
        Time : O(k)
        """
        item = str(item)
        positions = self._hash_positions(item)
        if not all(self._counters[pos] > 0 for pos in positions):
            return False    # item definitely not present
        for pos in positions:
            self._counters[pos] -= 1
        self._count -= 1
        return True

    def contains(self, item) -> bool:
        """Time : O(k)"""
        item = str(item)
        return all(self._counters[pos] > 0 for pos in self._hash_positions(item))

    def __repr__(self):
        return (f"CountingBloomFilter(n={self.n}, p={self.p}, "
                f"m={self.m}, k={self.k}, inserted={self._count})")


# ── Demo / Tests ──────────────────────────────────────────────────────────────

if __name__ == "__main__":

    print("=" * 60)
    print("  Bloom Filter Demo")
    print("=" * 60)

    # ── Basic Bloom Filter ────────────────────────────────────
    print("\n── Standard Bloom Filter ──")
    bf = BloomFilter(expected_elements=1000, false_positive_rate=0.01)

    # Insert words
    present_words = ["apple", "banana", "cherry", "date", "elderberry",
                     "fig", "grape", "honeydew", "kiwi", "lemon"]
    for word in present_words:
        bf.add(word)

    print(f"\nInserted {len(present_words)} words.")

    # True membership queries (should ALL be True)
    print("\n── Membership checks (inserted words) ──")
    for word in present_words:
        result = bf.contains(word)
        print(f"  contains('{word}') -> {result}")   # all True

    # Non-member queries (should mostly be False)
    print("\n── Membership checks (non-inserted words) ──")
    absent_words = ["mango", "nectarine", "orange", "papaya",
                    "quince", "raspberry", "strawberry"]
    fp_count = 0
    for word in absent_words:
        result = bf.contains(word)
        if result:
            fp_count += 1
        print(f"  contains('{word}') -> {result}{'  <- FALSE POSITIVE' if result else ''}")

    print(f"\nFalse positives: {fp_count}/{len(absent_words)}")

    # Stats
    bf.stats()

    # ── Large scale FP rate test ──────────────────────────────
    print("\n── Large Scale FP Rate Test ──")
    bf2 = BloomFilter(expected_elements=10_000, false_positive_rate=0.001)

    # Insert 10k items
    inserted = {f"user:{i}" for i in range(10_000)}
    for item in inserted:
        bf2.add(item)

    # Test 10k non-inserted items
    test_absent = [f"visitor:{i}" for i in range(10_000)]
    false_positives = sum(1 for item in test_absent if bf2.contains(item))

    print(f"Inserted       : 10,000 items")
    print(f"Tested absent  : 10,000 items")
    print(f"False positives: {false_positives}")
    print(f"Observed FPR   : {false_positives / 10_000 * 100:.3f}%")
    print(f"Expected FPR   : {0.001 * 100:.3f}%")

    # ── Counting Bloom Filter ─────────────────────────────────
    print("\n── Counting Bloom Filter (with deletion) ──")
    cbf = CountingBloomFilter(expected_elements=500, false_positive_rate=0.01)

    cbf.add("python")
    cbf.add("java")
    cbf.add("golang")

    print(f"\nAfter adding python, java, golang:")
    print(f"  contains('python') -> {cbf.contains('python')}")   # True
    print(f"  contains('java')   -> {cbf.contains('java')}")     # True
    print(f"  contains('rust')   -> {cbf.contains('rust')}")     # False

    print(f"\nAfter removing 'java':")
    cbf.remove("java")
    print(f"  contains('java')   -> {cbf.contains('java')}")     # False
    print(f"  contains('python') -> {cbf.contains('python')}")   # True (unaffected)

    print(f"\nTrying to remove non-existent 'rust':")
    print(f"  remove('rust')     -> {cbf.remove('rust')}")       # False

    # ── Use case illustration ─────────────────────────────────
    print("\n── Real-world Use Case: Username Availability Check ──")
    taken_filter = BloomFilter(expected_elements=1_000_000, false_positive_rate=0.001)

    taken_usernames = ["alice", "bob", "charlie", "david", "eve"]
    for u in taken_usernames:
        taken_filter.add(u)

    def is_available(username: str) -> str:
        if not taken_filter.contains(username):
            return "AVAILABLE (definitely)"
        else:
            return "UNAVAILABLE or possible collision — check DB"

    for name in ["alice", "frank", "bob", "grace", "heidi"]:
        print(f"  '{name}': {is_available(name)}")
