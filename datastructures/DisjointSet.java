import java.util.*;

/**
 * Disjoint Set (Union-Find) Implementation in Java
 * ==================================================
 * A Disjoint Set tracks a collection of elements partitioned into
 * non-overlapping (disjoint) sets.
 *
 * Core operations:
 *   find(x)         - Find the representative (root) of x's set
 *   union(x, y)     - Merge the sets containing x and y
 *   connected(x, y) - Check if x and y belong to the same set
 *
 * Two key optimizations that make operations nearly O(1):
 *   1. Union by Rank    : attach smaller tree under larger tree
 *   2. Path Compression : flatten the tree during find()
 *
 * With both optimizations, amortized time per operation is O(α(n)),
 * where α is the inverse Ackermann function — effectively O(1)
 * for all practical values of n (α(n) ≤ 4 for n < 10^600).
 *
 * Real-world applications:
 *   - Kruskal's MST algorithm
 *   - Network connectivity checking
 *   - Image segmentation (connected components)
 *   - Cycle detection in undirected graphs
 *   - Social network friend groups
 *   - Percolation theory
 *
 * File contains:
 *   DisjointSet             - Core Union-Find with rank + path compression
 *   DisjointSetWeighted     - Variant with union by size
 *   DisjointSetWithHistory  - Supports rollback (undo last union)
 *   Applications            - Kruskal's MST, connected components, cycle detection
 */
public class DisjointSet {

    // ══════════════════════════════════════════════════════════════════════════
    // 1. CORE DISJOINT SET  (Union by Rank + Path Compression)
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Standard Union-Find with:
     *   - Union by Rank    : keeps tree height minimal
     *   - Path Compression : flattens tree during find()
     *
     * Time complexity (amortized): O(α(n)) per operation ≈ O(1)
     * Space complexity            : O(n)
     */
    static class UnionFind {
        private final int[] parent;
        private final int[] rank;
        private int numSets;
        private final int n;

        /**
         * Initialise n elements, each in its own set: {0}, {1}, ..., {n-1}.
         */
        public UnionFind(int n) {
            if (n <= 0) throw new IllegalArgumentException("n must be positive");
            this.n       = n;
            this.numSets = n;
            this.parent  = new int[n];
            this.rank    = new int[n];
            for (int i = 0; i < n; i++) parent[i] = i;   // each element is its own root
        }

        // ── Find (with path compression) ──────────────────────────────────────

        /**
         * Return the representative (root) of x's set.
         * Applies path compression: all nodes on path point directly to root.
         * Time : O(α(n)) amortized
         */
        public int find(int x) {
            validate(x);
            if (parent[x] != x) {
                parent[x] = find(parent[x]);   // path compression
            }
            return parent[x];
        }

        // ── Union (by rank) ───────────────────────────────────────────────────

        /**
         * Merge the sets containing x and y.
         * Returns true  if they were in different sets (merge happened).
         * Returns false if they were already in the same set.
         * Time : O(α(n)) amortized
         */
        public boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) return false;   // already in same set

            // Union by rank: attach smaller-rank tree under larger-rank tree
            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                // Equal rank: attach rootY under rootX, increment rootX's rank
                parent[rootY] = rootX;
                rank[rootX]++;
            }
            numSets--;
            return true;
        }

        // ── Connected ─────────────────────────────────────────────────────────

        /** Return true if x and y are in the same set. Time : O(α(n)) */
        public boolean connected(int x, int y) {
            return find(x) == find(y);
        }

        // ── Set Info ──────────────────────────────────────────────────────────

        /** Return a map from each root to the list of members in that set. */
        public Map<Integer, List<Integer>> getSets() {
            Map<Integer, List<Integer>> sets = new TreeMap<>();
            for (int i = 0; i < n; i++) {
                int root = find(i);
                sets.computeIfAbsent(root, k -> new ArrayList<>()).add(i);
            }
            return sets;
        }

        /** Return all members of x's set. */
        public List<Integer> setOf(int x) {
            int root = find(x);
            List<Integer> members = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if (find(i) == root) members.add(i);
            }
            return members;
        }

        public int setCount() { return numSets; }

        // ── Utilities ─────────────────────────────────────────────────────────

        private void validate(int x) {
            if (x < 0 || x >= n)
                throw new IndexOutOfBoundsException("Element " + x + " out of range [0," + (n-1) + "]");
        }

        public void display() {
            System.out.printf("%nDisjoint Sets (%d sets):%n", numSets);
            for (Map.Entry<Integer, List<Integer>> e : getSets().entrySet()) {
                System.out.printf("  Set[root=%d] -> %s%n", e.getKey(), e.getValue());
            }
        }

        public void displayInternals() {
            System.out.println("\nInternal State:");
            System.out.println("  index  : " + Arrays.toString(Arrays.copyOf(parent, n)).replace(parent.toString(), ""));
            System.out.print("  parent : [");
            for (int i = 0; i < n; i++) System.out.print((i > 0 ? ", " : "") + parent[i]);
            System.out.println("]");
            System.out.print("  rank   : [");
            for (int i = 0; i < n; i++) System.out.print((i > 0 ? ", " : "") + rank[i]);
            System.out.println("]");
        }

        @Override
        public String toString() {
            return String.format("DisjointSet(n=%d, sets=%d)", n, numSets);
        }
    }


    // ══════════════════════════════════════════════════════════════════════════
    // 2. WEIGHTED DISJOINT SET  (Union by Size)
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Variant that uses union by SIZE instead of rank.
     * Tracks exact set sizes — useful for size queries.
     * Time: O(α(n)) amortized — same as rank variant.
     */
    static class WeightedUnionFind {
        private final int[] parent;
        private final int[] size;
        private int numSets;
        private final int n;

        public WeightedUnionFind(int n) {
            this.n       = n;
            this.numSets = n;
            this.parent  = new int[n];
            this.size    = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i]   = 1;
            }
        }

        public int find(int x) {
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }

        public boolean union(int x, int y) {
            int rootX = find(x), rootY = find(y);
            if (rootX == rootY) return false;
            // Attach smaller set under larger set
            if (size[rootX] < size[rootY]) { int tmp = rootX; rootX = rootY; rootY = tmp; }
            parent[rootY]  = rootX;
            size[rootX]   += size[rootY];
            numSets--;
            return true;
        }

        public boolean connected(int x, int y) { return find(x) == find(y); }

        /** Return the size of x's set. Time : O(α(n)) */
        public int getSize(int x) { return size[find(x)]; }

        public Map<Integer, List<Integer>> getSets() {
            Map<Integer, List<Integer>> sets = new TreeMap<>();
            for (int i = 0; i < n; i++)
                sets.computeIfAbsent(find(i), k -> new ArrayList<>()).add(i);
            return sets;
        }

        public void display() {
            System.out.printf("%nWeighted Disjoint Sets (%d sets):%n", numSets);
            for (Map.Entry<Integer, List<Integer>> e : getSets().entrySet()) {
                System.out.printf("  Set[root=%d, size=%d] -> %s%n",
                        e.getKey(), size[e.getKey()], e.getValue());
            }
        }

        @Override
        public String toString() {
            return String.format("WeightedUnionFind(n=%d, sets=%d)", n, numSets);
        }
    }


    // ══════════════════════════════════════════════════════════════════════════
    // 3. DISJOINT SET WITH ROLLBACK  (Supports undo)
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Variant that supports rollback (undo the last union).
     * Uses union by rank WITHOUT path compression
     * (path compression mutates parent pointers, making rollback impossible).
     *
     * Use case: offline dynamic connectivity, game state backtracking.
     * Time per operation: O(log n) — no path compression
     */
    static class RollbackUnionFind {
        private final int[] parent;
        private final int[] rank;
        // Each history entry stores: [rootY, oldParentY, rootX, oldRankX]
        // null entry = no-op union
        private final Deque<int[]> history;
        private int numSets;
        private final int n;

        public RollbackUnionFind(int n) {
            this.n       = n;
            this.numSets = n;
            this.parent  = new int[n];
            this.rank    = new int[n];
            this.history = new ArrayDeque<>();
            for (int i = 0; i < n; i++) parent[i] = i;
        }

        /** Find without path compression (needed for rollback). Time : O(log n) */
        public int find(int x) {
            while (parent[x] != x) x = parent[x];
            return x;
        }

        public boolean union(int x, int y) {
            int rootX = find(x), rootY = find(y);
            if (rootX == rootY) {
                history.push(null);   // no-op, push null so rollback count is consistent
                return false;
            }
            if (rank[rootX] < rank[rootY]) { int tmp = rootX; rootX = rootY; rootY = tmp; }
            // Save state before mutating
            history.push(new int[]{ rootY, parent[rootY], rootX, rank[rootX] });
            parent[rootY] = rootX;
            if (rank[rootX] == rank[rootY]) rank[rootX]++;
            numSets--;
            return true;
        }

        /** Undo the last union. Returns false if history is empty. */
        public boolean rollback() {
            if (history.isEmpty()) return false;
            int[] entry = history.pop();
            if (entry == null) return true;   // was a no-op union
            int rootY = entry[0], oldParentY = entry[1],
                rootX = entry[2], oldRankX   = entry[3];
            parent[rootY] = oldParentY;
            rank[rootX]   = oldRankX;
            numSets++;
            return true;
        }

        public boolean connected(int x, int y) { return find(x) == find(y); }

        public Map<Integer, List<Integer>> getSets() {
            Map<Integer, List<Integer>> sets = new TreeMap<>();
            for (int i = 0; i < n; i++)
                sets.computeIfAbsent(find(i), k -> new ArrayList<>()).add(i);
            return sets;
        }

        public void display() {
            System.out.printf("%nDisjointSet with History (%d sets):%n", numSets);
            for (Map.Entry<Integer, List<Integer>> e : getSets().entrySet())
                System.out.printf("  Set[root=%d] -> %s%n", e.getKey(), e.getValue());
        }

        @Override
        public String toString() {
            return String.format("RollbackUnionFind(n=%d, sets=%d, historyDepth=%d)",
                    n, numSets, history.size());
        }
    }


    // ══════════════════════════════════════════════════════════════════════════
    // APPLICATIONS
    // ══════════════════════════════════════════════════════════════════════════

    /** Kruskal's MST: sort edges by weight, add if it doesn't form a cycle. */
    static int kruskalMST(int n, int[][] edges) {
        // edges[i] = {weight, u, v}
        Arrays.sort(edges, (a, b) -> a[0] - b[0]);
        UnionFind ds = new UnionFind(n);
        List<int[]> mst = new ArrayList<>();
        int totalWeight = 0;

        for (int[] edge : edges) {
            int w = edge[0], u = edge[1], v = edge[2];
            if (ds.union(u, v)) {
                mst.add(edge);
                totalWeight += w;
                if (mst.size() == n - 1) break;
            }
        }

        System.out.print("Kruskal's MST edges: ");
        for (int[] e : mst) System.out.printf("[%d-%d w=%d] ", e[1], e[2], e[0]);
        System.out.println("\nTotal weight: " + totalWeight);
        return totalWeight;
    }

    /** Count connected components in an undirected graph. */
    static int countComponents(int n, int[][] edges) {
        UnionFind ds = new UnionFind(n);
        for (int[] e : edges) ds.union(e[0], e[1]);
        return ds.setCount();
    }

    /** Detect cycle in an undirected graph. */
    static boolean detectCycle(int n, int[][] edges) {
        UnionFind ds = new UnionFind(n);
        for (int[] e : edges) {
            if (!ds.union(e[0], e[1])) return true;   // already connected → cycle
        }
        return false;
    }


    // ══════════════════════════════════════════════════════════════════════════
    // DEMO
    // ══════════════════════════════════════════════════════════════════════════

    public static void main(String[] args) {

        System.out.println("=".repeat(60));
        System.out.println("  Disjoint Set (Union-Find) Demo");
        System.out.println("=".repeat(60));

        // ── 1. Basic Operations ───────────────────────────────
        System.out.println("\n── 1. Basic DisjointSet (Rank + Path Compression) ──");
        UnionFind ds = new UnionFind(8);
        ds.display();

        System.out.println("\nUnion operations:");
        System.out.println("  union(0,1) -> " + ds.union(0, 1));
        System.out.println("  union(2,3) -> " + ds.union(2, 3));
        System.out.println("  union(4,5) -> " + ds.union(4, 5));
        System.out.println("  union(6,7) -> " + ds.union(6, 7));
        System.out.println("  union(0,2) -> " + ds.union(0, 2));  // merges {0,1} and {2,3}
        System.out.println("  union(4,6) -> " + ds.union(4, 6));  // merges {4,5} and {6,7}
        ds.display();

        System.out.println("\nConnectivity checks:");
        System.out.println("  connected(0,3) -> " + ds.connected(0, 3));  // true
        System.out.println("  connected(0,5) -> " + ds.connected(0, 5));  // false
        System.out.println("  connected(4,7) -> " + ds.connected(4, 7));  // true

        System.out.println("\nSet of element 2 : " + ds.setOf(2));         // [0,1,2,3]
        System.out.println("Number of sets   : " + ds.setCount());         // 2

        ds.displayInternals();

        // ── 2. Weighted Variant ───────────────────────────────
        System.out.println("\n── 2. WeightedUnionFind (Union by Size) ──");
        WeightedUnionFind dsw = new WeightedUnionFind(6);
        dsw.union(0, 1);
        dsw.union(1, 2);
        dsw.union(3, 4);
        dsw.display();
        System.out.println("  Size of set containing 0 : " + dsw.getSize(0));  // 3
        System.out.println("  Size of set containing 3 : " + dsw.getSize(3));  // 2
        System.out.println("  Size of set containing 5 : " + dsw.getSize(5));  // 1

        // ── 3. Rollback Variant ───────────────────────────────
        System.out.println("\n── 3. RollbackUnionFind ──");
        RollbackUnionFind dsh = new RollbackUnionFind(5);
        System.out.println("Initial state:");
        dsh.display();

        dsh.union(0, 1);
        dsh.union(2, 3);
        System.out.println("\nAfter union(0,1) and union(2,3):");
        dsh.display();

        dsh.union(0, 2);
        System.out.println("\nAfter union(0,2) — merges both groups:");
        dsh.display();
        System.out.println("  connected(1,3) -> " + dsh.connected(1, 3));  // true

        dsh.rollback();
        System.out.println("\nAfter rollback (undo union(0,2)):");
        dsh.display();
        System.out.println("  connected(1,3) -> " + dsh.connected(1, 3));  // false

        dsh.rollback();
        System.out.println("\nAfter second rollback (undo union(2,3)):");
        dsh.display();

        // ── 4. Kruskal's MST ──────────────────────────────────
        System.out.println("\n── 4. Application: Kruskal's MST ──");
        int[][] edges = {
            {4, 0, 1}, {8, 0, 2}, {7, 2, 3},
            {2, 1, 3}, {9, 0, 3}, {6, 1, 2}
        };
        kruskalMST(4, edges);

        // ── 5. Connected Components ────────────────────────────
        System.out.println("\n── 5. Application: Connected Components ──");
        int[][] graphEdges = {{0,1},{1,2},{3,4},{5,6},{6,7}};
        int components = countComponents(8, graphEdges);
        System.out.println("Graph edges : " + Arrays.deepToString(graphEdges));
        System.out.println("Components  : " + components);   // 3

        // ── 6. Cycle Detection ────────────────────────────────
        System.out.println("\n── 6. Application: Cycle Detection ──");
        int[][] noCycle  = {{0,1},{1,2},{2,3}};
        int[][] hasCycle = {{0,1},{1,2},{2,0}};
        System.out.println("Edges " + Arrays.deepToString(noCycle)
                + "  -> cycle: " + detectCycle(4, noCycle));    // false
        System.out.println("Edges " + Arrays.deepToString(hasCycle)
                + " -> cycle: " + detectCycle(3, hasCycle));    // true

        // ── 7. Social Network ─────────────────────────────────
        System.out.println("\n── 7. Application: Social Network Friend Groups ──");
        // 0=Alice 1=Bob 2=Charlie 3=David 4=Eve 5=Frank
        String[] names = {"Alice", "Bob", "Charlie", "David", "Eve", "Frank"};
        UnionFind friends = new UnionFind(6);
        friends.union(0, 1);   // Alice-Bob
        friends.union(1, 2);   // Bob-Charlie
        friends.union(3, 4);   // David-Eve

        System.out.println("Friend groups:");
        for (Map.Entry<Integer, List<Integer>> e : friends.getSets().entrySet()) {
            List<String> group = new ArrayList<>();
            for (int m : e.getValue()) group.add(names[m]);
            System.out.println("  Group: " + group);
        }
        System.out.println("\nAre Alice and Charlie friends? " + friends.connected(0, 2));  // true
        System.out.println("Are Alice and David friends?   " + friends.connected(0, 3));  // false
    }
}
