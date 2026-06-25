"""
Disjoint Set (Union-Find) Implementation in Python
====================================================
A Disjoint Set (also called Union-Find) is a data structure that tracks
a collection of elements partitioned into non-overlapping (disjoint) sets.

Core operations:
  find(x)        - Find the representative (root) of x's set
  union(x, y)    - Merge the sets containing x and y
  connected(x,y) - Check if x and y belong to the same set

Two key optimizations that make operations nearly O(1):
  1. Union by Rank    : always attach smaller tree under larger tree
  2. Path Compression : flatten the tree during find() so future finds are faster

With both optimizations, amortized time per operation is O(α(n)),
where α is the inverse Ackermann function — effectively O(1) for all
practical values of n (α(n) ≤ 4 for n < 10^600).

Real-world applications:
  - Kruskal's MST algorithm
  - Network connectivity checking
  - Image segmentation (connected components)
  - Cycle detection in undirected graphs
  - Social network friend groups
  - Percolation theory (physics simulations)

File contains:
  DisjointSet              - Core Union-Find with rank + path compression
  DisjointSetWeighted      - Variant with weighted union (by size, not rank)
  DisjointSetWithHistory   - Supports rollback (undo last union)
"""


# ══════════════════════════════════════════════════════════════════════════════
# 1. CORE DISJOINT SET  (Union by Rank + Path Compression)
# ══════════════════════════════════════════════════════════════════════════════

class DisjointSet:
    """
    Standard Union-Find with:
      - Union by Rank    : keeps tree height minimal
      - Path Compression : flattens tree during find()

    Time complexity (amortized): O(α(n)) per operation ≈ O(1)
    Space complexity            : O(n)
    """

    def __init__(self, n: int):
        """
        Initialise n elements, each in its own set: {0}, {1}, ..., {n-1}.

        Args:
            n : number of elements (0-indexed from 0 to n-1)
        """
        if n <= 0:
            raise ValueError("n must be positive")

        self.n          = n
        self.parent     = list(range(n))   # parent[i] = i initially (self-loop)
        self.rank       = [0] * n          # rank[i] = upper bound on tree height
        self.num_sets   = n                # number of disjoint sets

    # ── Find (with path compression) ──────────────────────────────────────────

    def find(self, x: int) -> int:
        """
        Return the representative (root) of x's set.
        Applies path compression: all nodes on the path point directly to root.
        Time : O(α(n)) amortized
        """
        self._validate(x)
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])   # path compression
        return self.parent[x]

    # ── Union (by rank) ───────────────────────────────────────────────────────

    def union(self, x: int, y: int) -> bool:
        """
        Merge the sets containing x and y.
        Returns True if they were in different sets (merge happened),
                False if they were already in the same set.
        Time : O(α(n)) amortized
        """
        root_x = self.find(x)
        root_y = self.find(y)

        if root_x == root_y:
            return False   # already in the same set

        # Union by rank: attach smaller-rank tree under larger-rank tree
        if self.rank[root_x] < self.rank[root_y]:
            self.parent[root_x] = root_y
        elif self.rank[root_x] > self.rank[root_y]:
            self.parent[root_y] = root_x
        else:
            # Equal rank: arbitrarily attach root_y under root_x, increment rank
            self.parent[root_y] = root_x
            self.rank[root_x]  += 1

        self.num_sets -= 1
        return True

    # ── Connected ─────────────────────────────────────────────────────────────

    def connected(self, x: int, y: int) -> bool:
        """Return True if x and y are in the same set. Time : O(α(n))"""
        return self.find(x) == self.find(y)

    # ── Set Info ──────────────────────────────────────────────────────────────

    def get_sets(self) -> dict:
        """Return a dict mapping each root -> list of members in that set."""
        sets = {}
        for i in range(self.n):
            root = self.find(i)
            sets.setdefault(root, []).append(i)
        return sets

    def set_count(self) -> int:
        """Return the number of disjoint sets."""
        return self.num_sets

    def set_of(self, x: int) -> list:
        """Return all members of x's set."""
        root = self.find(x)
        return [i for i in range(self.n) if self.find(i) == root]

    # ── Utilities ─────────────────────────────────────────────────────────────

    def _validate(self, x: int):
        if not (0 <= x < self.n):
            raise IndexError(f"Element {x} out of range [0, {self.n - 1}]")

    def display(self) -> None:
        print(f"\nDisjoint Sets ({self.num_sets} sets):")
        for root, members in sorted(self.get_sets().items()):
            print(f"  Set[root={root}] -> {members}")

    def display_internals(self) -> None:
        print(f"\nInternal State:")
        print(f"  index  : {list(range(self.n))}")
        print(f"  parent : {self.parent}")
        print(f"  rank   : {self.rank}")

    def __repr__(self):
        return f"DisjointSet(n={self.n}, sets={self.num_sets})"


# ══════════════════════════════════════════════════════════════════════════════
# 2. WEIGHTED DISJOINT SET  (Union by Size instead of Rank)
# ══════════════════════════════════════════════════════════════════════════════

class DisjointSetWeighted:
    """
    Variant that uses union by SIZE instead of rank.
    Size tracks the exact number of elements in each set,
    which is useful when you need to query set sizes efficiently.

    Time: O(α(n)) amortized — same as rank variant.
    """

    def __init__(self, n: int):
        self.n       = n
        self.parent  = list(range(n))
        self.size    = [1] * n          # size[i] = size of set rooted at i
        self.num_sets = n

    def find(self, x: int) -> int:
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]

    def union(self, x: int, y: int) -> bool:
        root_x = self.find(x)
        root_y = self.find(y)
        if root_x == root_y:
            return False
        # Attach smaller set under larger set
        if self.size[root_x] < self.size[root_y]:
            root_x, root_y = root_y, root_x
        self.parent[root_y]  = root_x
        self.size[root_x]   += self.size[root_y]
        self.num_sets        -= 1
        return True

    def connected(self, x: int, y: int) -> bool:
        return self.find(x) == self.find(y)

    def get_size(self, x: int) -> int:
        """Return the size of x's set. Time : O(α(n))"""
        return self.size[self.find(x)]

    def get_sets(self) -> dict:
        sets = {}
        for i in range(self.n):
            root = self.find(i)
            sets.setdefault(root, []).append(i)
        return sets

    def display(self) -> None:
        print(f"\nWeighted Disjoint Sets ({self.num_sets} sets):")
        for root, members in sorted(self.get_sets().items()):
            print(f"  Set[root={root}, size={self.size[root]}] -> {members}")

    def __repr__(self):
        return f"DisjointSetWeighted(n={self.n}, sets={self.num_sets})"


# ══════════════════════════════════════════════════════════════════════════════
# 3. DISJOINT SET WITH ROLLBACK  (Supports undo / history)
# ══════════════════════════════════════════════════════════════════════════════

class DisjointSetWithHistory:
    """
    Variant that supports rollback (undo the last union).
    Uses union by rank WITHOUT path compression
    (path compression makes rollback impossible since it mutates parent pointers).

    Use case: offline dynamic connectivity, game state backtracking.

    Time per operation: O(log n)  — no path compression
    """

    def __init__(self, n: int):
        self.n       = n
        self.parent  = list(range(n))
        self.rank    = [0] * n
        self.history = []   # stack of (node, old_parent, node2, old_rank)
        self.num_sets = n

    def find(self, x: int) -> int:
        """Find without path compression (needed for rollback). Time : O(log n)"""
        while self.parent[x] != x:
            x = self.parent[x]
        return x

    def union(self, x: int, y: int) -> bool:
        root_x = self.find(x)
        root_y = self.find(y)
        if root_x == root_y:
            self.history.append(None)   # record no-op so rollback count stays consistent
            return False
        if self.rank[root_x] < self.rank[root_y]:
            root_x, root_y = root_y, root_x
        # Save state before mutating (for rollback)
        self.history.append((root_y, self.parent[root_y],
                              root_x, self.rank[root_x]))
        self.parent[root_y]  = root_x
        if self.rank[root_x] == self.rank[root_y]:
            self.rank[root_x] += 1
        self.num_sets -= 1
        return True

    def rollback(self) -> bool:
        """Undo the last union operation. Returns False if nothing to undo."""
        if not self.history:
            return False
        entry = self.history.pop()
        if entry is None:
            return True   # was a no-op union
        root_y, old_parent_y, root_x, old_rank_x = entry
        self.parent[root_y] = old_parent_y
        self.rank[root_x]   = old_rank_x
        self.num_sets       += 1
        return True

    def connected(self, x: int, y: int) -> bool:
        return self.find(x) == self.find(y)

    def get_sets(self) -> dict:
        sets = {}
        for i in range(self.n):
            root = self.find(i)
            sets.setdefault(root, []).append(i)
        return sets

    def display(self) -> None:
        print(f"\nDisjointSet with History ({self.num_sets} sets):")
        for root, members in sorted(self.get_sets().items()):
            print(f"  Set[root={root}] -> {members}")

    def __repr__(self):
        return f"DisjointSetWithHistory(n={self.n}, sets={self.num_sets}, history_depth={len(self.history)})"


# ══════════════════════════════════════════════════════════════════════════════
# APPLICATIONS
# ══════════════════════════════════════════════════════════════════════════════

def kruskal_mst(n: int, edges: list) -> list:
    """
    Kruskal's Minimum Spanning Tree using DisjointSet.

    Args:
        n     : number of vertices (0 to n-1)
        edges : list of (weight, u, v) tuples

    Returns:
        list of (weight, u, v) edges in the MST
    """
    edges.sort()    # sort by weight ascending
    ds  = DisjointSet(n)
    mst = []
    total_weight = 0

    for weight, u, v in edges:
        if ds.union(u, v):          # only add edge if it doesn't form a cycle
            mst.append((weight, u, v))
            total_weight += weight
            if len(mst) == n - 1:   # MST complete when n-1 edges added
                break

    print(f"\nKruskal's MST: {mst}")
    print(f"Total weight : {total_weight}")
    return mst


def count_connected_components(n: int, edges: list) -> int:
    """Count connected components in an undirected graph."""
    ds = DisjointSet(n)
    for u, v in edges:
        ds.union(u, v)
    return ds.set_count()


def detect_cycle(n: int, edges: list) -> bool:
    """Detect cycle in an undirected graph using Union-Find."""
    ds = DisjointSet(n)
    for u, v in edges:
        if not ds.union(u, v):  # union returns False if already connected → cycle
            return True
    return False


# ══════════════════════════════════════════════════════════════════════════════
# DEMO
# ══════════════════════════════════════════════════════════════════════════════

if __name__ == "__main__":

    print("=" * 60)
    print("  Disjoint Set (Union-Find) Demo")
    print("=" * 60)

    # ── Basic Operations ──────────────────────────────────────
    print("\n── 1. Basic DisjointSet (Rank + Path Compression) ──")
    ds = DisjointSet(8)
    ds.display()

    print("\nUnion operations:")
    print(f"  union(0,1) -> {ds.union(0, 1)}")
    print(f"  union(2,3) -> {ds.union(2, 3)}")
    print(f"  union(4,5) -> {ds.union(4, 5)}")
    print(f"  union(6,7) -> {ds.union(6, 7)}")
    print(f"  union(0,2) -> {ds.union(0, 2)}")   # merges {0,1} and {2,3}
    print(f"  union(4,6) -> {ds.union(4, 6)}")   # merges {4,5} and {6,7}
    ds.display()

    print("\nConnectivity checks:")
    print(f"  connected(0,3) -> {ds.connected(0, 3)}")   # True
    print(f"  connected(0,5) -> {ds.connected(0, 5)}")   # False
    print(f"  connected(4,7) -> {ds.connected(4, 7)}")   # True

    print(f"\nSet of element 2 : {ds.set_of(2)}")         # [0, 1, 2, 3]
    print(f"Number of sets   : {ds.set_count()}")         # 2

    ds.display_internals()

    # ── Weighted Variant ──────────────────────────────────────
    print("\n── 2. DisjointSet Weighted (Union by Size) ──")
    dsw = DisjointSetWeighted(6)
    dsw.union(0, 1)
    dsw.union(1, 2)
    dsw.union(3, 4)
    dsw.display()
    print(f"  Size of set containing 0 : {dsw.get_size(0)}")  # 3
    print(f"  Size of set containing 3 : {dsw.get_size(3)}")  # 2
    print(f"  Size of set containing 5 : {dsw.get_size(5)}")  # 1

    # ── Rollback Variant ──────────────────────────────────────
    print("\n── 3. DisjointSet with Rollback ──")
    dsh = DisjointSetWithHistory(5)
    print("Initial state:")
    dsh.display()

    dsh.union(0, 1)
    dsh.union(2, 3)
    print("\nAfter union(0,1) and union(2,3):")
    dsh.display()

    dsh.union(0, 2)
    print("\nAfter union(0,2) — merges both groups:")
    dsh.display()
    print(f"  connected(1, 3) -> {dsh.connected(1, 3)}")   # True

    dsh.rollback()
    print("\nAfter rollback (undo union(0,2)):")
    dsh.display()
    print(f"  connected(1, 3) -> {dsh.connected(1, 3)}")   # False

    dsh.rollback()
    print("\nAfter second rollback (undo union(2,3)):")
    dsh.display()

    # ── Kruskal's MST ─────────────────────────────────────────
    print("\n── 4. Application: Kruskal's MST ──")
    #  Graph:
    #    0 --4-- 1
    #    |       |
    #    8       2
    #    |       |
    #    2 --7-- 3
    edges = [
        (4, 0, 1),
        (8, 0, 2),
        (7, 2, 3),
        (2, 1, 3),
        (9, 0, 3),
        (6, 1, 2),
    ]
    kruskal_mst(4, edges)

    # ── Connected Components ──────────────────────────────────
    print("\n── 5. Application: Connected Components ──")
    graph_edges = [(0,1), (1,2), (3,4), (5,6), (6,7)]
    components  = count_connected_components(8, graph_edges)
    print(f"Graph edges     : {graph_edges}")
    print(f"Components      : {components}")   # 3: {0,1,2}, {3,4}, {5,6,7}

    # ── Cycle Detection ───────────────────────────────────────
    print("\n── 6. Application: Cycle Detection ──")
    no_cycle = [(0,1), (1,2), (2,3)]
    has_cycle = [(0,1), (1,2), (2,0)]
    print(f"Edges {no_cycle}  -> cycle: {detect_cycle(4, no_cycle)}")    # False
    print(f"Edges {has_cycle} -> cycle: {detect_cycle(3, has_cycle)}")   # True

    # ── Social Network Groups ─────────────────────────────────
    print("\n── 7. Application: Social Network Friend Groups ──")
    # 0=Alice 1=Bob 2=Charlie 3=David 4=Eve 5=Frank
    friends = DisjointSet(6)
    names   = ["Alice", "Bob", "Charlie", "David", "Eve", "Frank"]

    friends.union(0, 1)  # Alice-Bob
    friends.union(1, 2)  # Bob-Charlie   (Alice, Bob, Charlie now one group)
    friends.union(3, 4)  # David-Eve

    print("Friend groups:")
    for root, members in sorted(friends.get_sets().items()):
        group = [names[m] for m in members]
        print(f"  Group: {group}")

    print(f"\nAre Alice and Charlie friends? {friends.connected(0, 2)}")   # True
    print(f"Are Alice and David friends?   {friends.connected(0, 3)}")   # False
