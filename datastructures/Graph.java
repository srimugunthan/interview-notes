import java.util.*;

/**
 * Graph Data Structure Implementation in Java
 * ==============================================
 * A Graph is a set of vertices (nodes) connected by edges.
 *
 * Types covered:
 *   - Undirected / Directed graphs
 *   - Unweighted / Weighted graphs
 *   - Representations: Adjacency List, Adjacency Matrix
 *
 * Algorithms included:
 *   Traversals   : BFS, DFS (iterative + recursive)
 *   Shortest path: Dijkstra (weighted), BFS (unweighted)
 *   Spanning tree: Prim's MST
 *   Ordering     : Topological Sort (Kahn's BFS + DFS)
 *   Detection    : Cycle detection (directed + undirected)
 *   Analysis     : Connected components, Bipartite check
 *   Path         : All paths between two nodes
 *
 * Graph representations:
 *   Adjacency List  — O(V + E) space, efficient for sparse graphs
 *   Adjacency Matrix — O(V²) space, O(1) edge lookup, dense graphs
 */
public class Graph {

    // ══════════════════════════════════════════════════════════════════════════
    // EDGE  (inner record)
    // ══════════════════════════════════════════════════════════════════════════

    static class Edge {
        int    to;
        double weight;

        Edge(int to, double weight) {
            this.to     = to;
            this.weight = weight;
        }

        @Override public String toString() {
            return weight == 1.0 ? String.valueOf(to)
                                 : to + "(w=" + weight + ")";
        }
    }


    // ══════════════════════════════════════════════════════════════════════════
    // 1. ADJACENCY LIST GRAPH
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * General-purpose graph using an adjacency list.
     * Supports directed/undirected and weighted/unweighted edges.
     * Space : O(V + E)
     */
    static class AdjGraph {
        private final Map<Integer, List<Edge>> adj;
        private final Set<Integer>             vertices;
        private final boolean                  directed;

        public AdjGraph(boolean directed) {
            this.directed = directed;
            this.adj      = new HashMap<>();
            this.vertices = new HashSet<>();
        }

        // ── Add Vertex / Edge ─────────────────────────────────────────────────

        /** Add an isolated vertex. Time : O(1) */
        public void addVertex(int v) {
            vertices.add(v);
            adj.putIfAbsent(v, new ArrayList<>());
        }

        /**
         * Add an edge from u to v with optional weight.
         * For undirected graphs, adds edge in both directions.
         * Time : O(1)
         */
        public void addEdge(int u, int v, double weight) {
            vertices.add(u);
            vertices.add(v);
            adj.computeIfAbsent(u, k -> new ArrayList<>()).add(new Edge(v, weight));
            if (!directed)
                adj.computeIfAbsent(v, k -> new ArrayList<>()).add(new Edge(u, weight));
        }

        public void addEdge(int u, int v) { addEdge(u, v, 1.0); }

        /** Remove edge u->v. Time : O(degree(u)) */
        public void removeEdge(int u, int v) {
            adj.getOrDefault(u, Collections.emptyList()).removeIf(e -> e.to == v);
            if (!directed)
                adj.getOrDefault(v, Collections.emptyList()).removeIf(e -> e.to == u);
        }

        /** Remove vertex and all its edges. Time : O(V + E) */
        public void removeVertex(int v) {
            vertices.remove(v);
            adj.remove(v);
            for (List<Edge> edges : adj.values())
                edges.removeIf(e -> e.to == v);
        }

        public boolean hasEdge(int u, int v) {
            return adj.getOrDefault(u, Collections.emptyList())
                      .stream().anyMatch(e -> e.to == v);
        }

        public List<Edge> neighbours(int v) {
            return adj.getOrDefault(v, Collections.emptyList());
        }

        public int degree(int v) {
            return adj.getOrDefault(v, Collections.emptyList()).size();
        }

        // ── Traversals ────────────────────────────────────────────────────────

        /**
         * BFS from start vertex.
         * Explores level by level — shortest path in unweighted graphs.
         * Time : O(V + E)
         */
        public List<Integer> bfs(int start) {
            Set<Integer>   visited = new HashSet<>();
            List<Integer>  order   = new ArrayList<>();
            Queue<Integer> queue   = new LinkedList<>();

            visited.add(start);
            queue.offer(start);

            while (!queue.isEmpty()) {
                int v = queue.poll();
                order.add(v);
                for (Edge e : adj.getOrDefault(v, Collections.emptyList())) {
                    if (!visited.contains(e.to)) {
                        visited.add(e.to);
                        queue.offer(e.to);
                    }
                }
            }
            return order;
        }

        /**
         * DFS from start vertex (iterative).
         * Time : O(V + E)
         */
        public List<Integer> dfs(int start) {
            Set<Integer>    visited = new HashSet<>();
            List<Integer>   order   = new ArrayList<>();
            Deque<Integer>  stack   = new ArrayDeque<>();
            stack.push(start);

            while (!stack.isEmpty()) {
                int v = stack.pop();
                if (!visited.contains(v)) {
                    visited.add(v);
                    order.add(v);
                    List<Edge> edges = adj.getOrDefault(v, Collections.emptyList());
                    // push in reverse for left-to-right traversal order
                    for (int i = edges.size() - 1; i >= 0; i--) {
                        if (!visited.contains(edges.get(i).to))
                            stack.push(edges.get(i).to);
                    }
                }
            }
            return order;
        }

        /** DFS recursive. Time : O(V + E) */
        public List<Integer> dfsRecursive(int start) {
            Set<Integer>  visited = new HashSet<>();
            List<Integer> order   = new ArrayList<>();
            dfsHelper(start, visited, order);
            return order;
        }

        private void dfsHelper(int v, Set<Integer> visited, List<Integer> order) {
            visited.add(v);
            order.add(v);
            for (Edge e : adj.getOrDefault(v, Collections.emptyList()))
                if (!visited.contains(e.to))
                    dfsHelper(e.to, visited, order);
        }

        // ── Shortest Paths ────────────────────────────────────────────────────

        /**
         * BFS shortest path (unweighted graph).
         * Returns path as list of vertices, or empty list if no path.
         * Time : O(V + E)
         */
        public List<Integer> bfsShortestPath(int start, int end) {
            if (start == end) return List.of(start);
            Map<Integer, Integer> parent  = new HashMap<>();
            Set<Integer>          visited = new HashSet<>();
            Queue<Integer>        queue   = new LinkedList<>();
            parent.put(start, null);
            visited.add(start);
            queue.offer(start);

            while (!queue.isEmpty()) {
                int v = queue.poll();
                for (Edge e : adj.getOrDefault(v, Collections.emptyList())) {
                    if (!visited.contains(e.to)) {
                        visited.add(e.to);
                        parent.put(e.to, v);
                        if (e.to == end) return reconstructPath(parent, start, end);
                        queue.offer(e.to);
                    }
                }
            }
            return Collections.emptyList();
        }

        /**
         * Dijkstra's shortest path from start to all vertices.
         * Time : O((V + E) log V)
         */
        public Map<Integer, Double> dijkstra(int start) {
            Map<Integer, Double>   dist = new HashMap<>();
            Map<Integer, Integer>  prev = new HashMap<>();
            PriorityQueue<int[]>   heap = new PriorityQueue<>(Comparator.comparingDouble(a -> a[1]));

            for (int v : vertices) { dist.put(v, Double.MAX_VALUE); prev.put(v, -1); }
            dist.put(start, 0.0);
            heap.offer(new int[]{start, 0});

            while (!heap.isEmpty()) {
                int[] top = heap.poll();
                int u = top[0]; double d = top[1];
                if (d > dist.get(u)) continue;   // stale entry
                for (Edge e : adj.getOrDefault(u, Collections.emptyList())) {
                    double nd = dist.get(u) + e.weight;
                    if (nd < dist.get(e.to)) {
                        dist.put(e.to, nd);
                        prev.put(e.to, u);
                        heap.offer(new int[]{e.to, (int) nd});
                    }
                }
            }
            return dist;
        }

        /**
         * Returns [distance, path] from start to end using Dijkstra.
         */
        public double dijkstraPath(int start, int end, List<Integer> pathOut) {
            Map<Integer, Double>  dist = new HashMap<>();
            Map<Integer, Integer> prev = new HashMap<>();
            PriorityQueue<int[]>  heap = new PriorityQueue<>(Comparator.comparingDouble(a -> a[1]));

            for (int v : vertices) { dist.put(v, Double.MAX_VALUE); prev.put(v, -1); }
            dist.put(start, 0.0);
            heap.offer(new int[]{start, 0});

            while (!heap.isEmpty()) {
                int[] top = heap.poll();
                int u = top[0]; double d = top[1];
                if (d > dist.get(u)) continue;
                for (Edge e : adj.getOrDefault(u, Collections.emptyList())) {
                    double nd = dist.get(u) + e.weight;
                    if (nd < dist.get(e.to)) {
                        dist.put(e.to, nd);
                        prev.put(e.to, u);
                        heap.offer(new int[]{e.to, (int) nd});
                    }
                }
            }
            // reconstruct
            if (dist.get(end) == Double.MAX_VALUE) return Double.MAX_VALUE;
            Deque<Integer> path = new ArrayDeque<>();
            for (int v = end; v != -1; v = prev.getOrDefault(v, -1)) path.push(v);
            pathOut.addAll(path);
            return dist.get(end);
        }

        private List<Integer> reconstructPath(Map<Integer, Integer> prev, int start, int end) {
            List<Integer> path = new ArrayList<>();
            for (Integer v = end; v != null; v = prev.get(v)) path.add(v);
            Collections.reverse(path);
            return path.isEmpty() || path.get(0) != start ? Collections.emptyList() : path;
        }

        // ── Prim's MST ────────────────────────────────────────────────────────

        /**
         * Prim's MST for undirected weighted graphs.
         * Returns list of int[]{weight, u, v} edges.
         * Time : O((V + E) log V)
         */
        public List<int[]> primsMST(int start) {
            Set<Integer>    inMST    = new HashSet<>();
            List<int[]>     mstEdges = new ArrayList<>();
            // {weight, from, to}
            PriorityQueue<int[]> heap =
                new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
            heap.offer(new int[]{0, start, start});

            while (!heap.isEmpty() && inMST.size() < vertices.size()) {
                int[] top = heap.poll();
                int w = top[0], u = top[1], v = top[2];
                if (inMST.contains(v)) continue;
                inMST.add(v);
                if (u != v) mstEdges.add(new int[]{w, u, v});
                for (Edge e : adj.getOrDefault(v, Collections.emptyList()))
                    if (!inMST.contains(e.to))
                        heap.offer(new int[]{(int) e.weight, v, e.to});
            }
            return mstEdges;
        }

        // ── Topological Sort ──────────────────────────────────────────────────

        /**
         * Kahn's Topological Sort (BFS-based).
         * Returns sorted order, or empty list if cycle exists.
         * Time : O(V + E)
         */
        public List<Integer> topologicalSortKahn() {
            Map<Integer, Integer> inDegree = new HashMap<>();
            for (int v : vertices) inDegree.put(v, 0);
            for (int u : vertices)
                for (Edge e : adj.getOrDefault(u, Collections.emptyList()))
                    inDegree.merge(e.to, 1, Integer::sum);

            Queue<Integer> queue  = new LinkedList<>();
            List<Integer>  result = new ArrayList<>();
            for (int v : vertices) if (inDegree.get(v) == 0) queue.offer(v);

            while (!queue.isEmpty()) {
                int v = queue.poll();
                result.add(v);
                for (Edge e : adj.getOrDefault(v, Collections.emptyList())) {
                    inDegree.merge(e.to, -1, Integer::sum);
                    if (inDegree.get(e.to) == 0) queue.offer(e.to);
                }
            }
            return result.size() == vertices.size() ? result : Collections.emptyList();
        }

        /**
         * DFS-based Topological Sort.
         * Time : O(V + E)
         */
        public List<Integer> topologicalSortDFS() {
            Set<Integer>   visited = new HashSet<>();
            Deque<Integer> stack   = new ArrayDeque<>();

            for (int v : vertices)
                if (!visited.contains(v))
                    topoHelper(v, visited, stack);

            List<Integer> result = new ArrayList<>(stack);
            Collections.reverse(result);
            return result;
        }

        private void topoHelper(int v, Set<Integer> visited, Deque<Integer> stack) {
            visited.add(v);
            for (Edge e : adj.getOrDefault(v, Collections.emptyList()))
                if (!visited.contains(e.to))
                    topoHelper(e.to, visited, stack);
            stack.push(v);
        }

        // ── Cycle Detection ───────────────────────────────────────────────────

        /**
         * Detect cycle in a DIRECTED graph using DFS colouring.
         * WHITE=0 (unvisited), GRAY=1 (in-stack), BLACK=2 (done).
         * Time : O(V + E)
         */
        public boolean hasCycleDirected() {
            Map<Integer, Integer> colour = new HashMap<>();
            for (int v : vertices) colour.put(v, 0);
            for (int v : vertices)
                if (colour.get(v) == 0 && dfsCycle(v, colour)) return true;
            return false;
        }

        private boolean dfsCycle(int v, Map<Integer, Integer> colour) {
            colour.put(v, 1);   // GRAY
            for (Edge e : adj.getOrDefault(v, Collections.emptyList())) {
                if (colour.get(e.to) == 1) return true;   // back edge
                if (colour.get(e.to) == 0 && dfsCycle(e.to, colour)) return true;
            }
            colour.put(v, 2);   // BLACK
            return false;
        }

        /**
         * Detect cycle in an UNDIRECTED graph using DFS + parent tracking.
         * Time : O(V + E)
         */
        public boolean hasCycleUndirected() {
            Set<Integer> visited = new HashSet<>();
            for (int v : vertices)
                if (!visited.contains(v) && dfsCycleUndirected(v, -1, visited))
                    return true;
            return false;
        }

        private boolean dfsCycleUndirected(int v, int parent, Set<Integer> visited) {
            visited.add(v);
            for (Edge e : adj.getOrDefault(v, Collections.emptyList())) {
                if (!visited.contains(e.to)) {
                    if (dfsCycleUndirected(e.to, v, visited)) return true;
                } else if (e.to != parent) return true;
            }
            return false;
        }

        // ── Connected Components ──────────────────────────────────────────────

        /**
         * Find all connected components (undirected graph).
         * Time : O(V + E)
         */
        public List<List<Integer>> connectedComponents() {
            Set<Integer>       visited    = new HashSet<>();
            List<List<Integer>> components = new ArrayList<>();

            for (int v : vertices) {
                if (!visited.contains(v)) {
                    List<Integer> component = new ArrayList<>();
                    dfsComponent(v, visited, component);
                    Collections.sort(component);
                    components.add(component);
                }
            }
            return components;
        }

        private void dfsComponent(int v, Set<Integer> visited, List<Integer> comp) {
            visited.add(v);
            comp.add(v);
            for (Edge e : adj.getOrDefault(v, Collections.emptyList()))
                if (!visited.contains(e.to))
                    dfsComponent(e.to, visited, comp);
        }

        // ── Bipartite Check ───────────────────────────────────────────────────

        /**
         * Check if the graph is bipartite (2-colourable).
         * Returns true if bipartite.
         * Time : O(V + E)
         */
        public boolean isBipartite() {
            Map<Integer, Integer> colour = new HashMap<>();
            for (int start : vertices) {
                if (colour.containsKey(start)) continue;
                colour.put(start, 0);
                Queue<Integer> queue = new LinkedList<>();
                queue.offer(start);
                while (!queue.isEmpty()) {
                    int v = queue.poll();
                    for (Edge e : adj.getOrDefault(v, Collections.emptyList())) {
                        if (!colour.containsKey(e.to)) {
                            colour.put(e.to, 1 - colour.get(v));
                            queue.offer(e.to);
                        } else if (colour.get(e.to).equals(colour.get(v))) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }

        // ── All Paths ─────────────────────────────────────────────────────────

        /** Find all simple paths from start to end. */
        public List<List<Integer>> allPaths(int start, int end) {
            List<List<Integer>> result = new ArrayList<>();
            Set<Integer>        visited = new HashSet<>();
            Deque<Integer>      path    = new ArrayDeque<>();
            visited.add(start);
            path.addLast(start);
            allPathsHelper(start, end, visited, path, result);
            return result;
        }

        private void allPathsHelper(int v, int end, Set<Integer> visited,
                                    Deque<Integer> path, List<List<Integer>> result) {
            if (v == end) {
                result.add(new ArrayList<>(path));
                return;
            }
            for (Edge e : adj.getOrDefault(v, Collections.emptyList())) {
                if (!visited.contains(e.to)) {
                    visited.add(e.to);
                    path.addLast(e.to);
                    allPathsHelper(e.to, end, visited, path, result);
                    path.removeLast();
                    visited.remove(e.to);
                }
            }
        }

        // ── Display ───────────────────────────────────────────────────────────

        public void display() {
            String kind = directed ? "Directed" : "Undirected";
            System.out.printf("%n%s Graph — Adjacency List:%n", kind);
            List<Integer> sorted = new ArrayList<>(vertices);
            Collections.sort(sorted);
            int totalEdges = 0;
            for (int v : sorted) {
                List<Edge> edges = adj.getOrDefault(v, Collections.emptyList());
                System.out.printf("  %d -> [%s]%n", v,
                        edges.stream().map(Edge::toString)
                             .reduce((a, b) -> a + ", " + b).orElse(""));
                totalEdges += edges.size();
            }
            System.out.printf("  Vertices: %d, Edges: %d%n",
                    vertices.size(), totalEdges);
        }
    }


    // ══════════════════════════════════════════════════════════════════════════
    // 2. ADJACENCY MATRIX GRAPH
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Graph represented as an adjacency matrix.
     * Best for dense graphs where O(1) edge lookup matters.
     * Space : O(V²)
     */
    static class MatrixGraph {
        private final double[][] matrix;
        private final int        n;
        private final boolean    directed;

        public MatrixGraph(int numVertices, boolean directed) {
            this.n        = numVertices;
            this.directed = directed;
            this.matrix   = new double[n][n];
        }

        public void addEdge(int u, int v, double weight) {
            matrix[u][v] = weight;
            if (!directed) matrix[v][u] = weight;
        }

        public void addEdge(int u, int v) { addEdge(u, v, 1.0); }

        public void removeEdge(int u, int v) {
            matrix[u][v] = 0;
            if (!directed) matrix[v][u] = 0;
        }

        public boolean hasEdge(int u, int v) { return matrix[u][v] != 0; }

        public List<int[]> neighbours(int v) {
            List<int[]> result = new ArrayList<>();
            for (int j = 0; j < n; j++)
                if (matrix[v][j] != 0) result.add(new int[]{j, (int) matrix[v][j]});
            return result;
        }

        public List<Integer> bfs(int start) {
            boolean[]      visited = new boolean[n];
            List<Integer>  order   = new ArrayList<>();
            Queue<Integer> queue   = new LinkedList<>();
            visited[start] = true;
            queue.offer(start);
            while (!queue.isEmpty()) {
                int v = queue.poll();
                order.add(v);
                for (int j = 0; j < n; j++)
                    if (matrix[v][j] != 0 && !visited[j]) {
                        visited[j] = true;
                        queue.offer(j);
                    }
            }
            return order;
        }

        public List<Integer> dfs(int start) {
            boolean[]     visited = new boolean[n];
            List<Integer> order   = new ArrayList<>();
            dfsHelper(start, visited, order);
            return order;
        }

        private void dfsHelper(int v, boolean[] visited, List<Integer> order) {
            visited[v] = true;
            order.add(v);
            for (int j = 0; j < n; j++)
                if (matrix[v][j] != 0 && !visited[j])
                    dfsHelper(j, visited, order);
        }

        public void display() {
            String kind = directed ? "Directed" : "Undirected";
            System.out.printf("%n%s Graph — Adjacency Matrix (%dx%d):%n", kind, n, n);
            System.out.print("     ");
            for (int j = 0; j < n; j++) System.out.printf("%4d", j);
            System.out.println();
            System.out.println("    " + "----".repeat(n));
            for (int i = 0; i < n; i++) {
                System.out.printf("  %d | ", i);
                for (int j = 0; j < n; j++)
                    System.out.printf("%4.0f", matrix[i][j]);
                System.out.println();
            }
        }
    }


    // ══════════════════════════════════════════════════════════════════════════
    // DEMO
    // ══════════════════════════════════════════════════════════════════════════

    static final String SEP = "=".repeat(60);

    public static void main(String[] args) {

        // ── 1. Undirected Unweighted ──────────────────────────
        System.out.println(SEP);
        System.out.println("  1. Undirected Unweighted Graph");
        System.out.println(SEP);

        AdjGraph g = new AdjGraph(false);
        int[][] edges1 = {{0,1},{0,2},{1,3},{1,4},{2,5},{3,5},{4,5}};
        for (int[] e : edges1) g.addEdge(e[0], e[1]);
        g.display();

        System.out.println("\nBFS from 0  : " + g.bfs(0));
        System.out.println("DFS from 0  : " + g.dfs(0));
        System.out.println("DFS(recur)  : " + g.dfsRecursive(0));
        System.out.println("BFS path 0->5: " + g.bfsShortestPath(0, 5));
        System.out.println("All paths 0->5: " + g.allPaths(0, 5));
        System.out.println("Connected components: " + g.connectedComponents());
        System.out.println("Has cycle (undirected): " + g.hasCycleUndirected());
        System.out.println("Is bipartite: " + g.isBipartite());

        // ── 2. Directed Weighted — Dijkstra ───────────────────
        System.out.println("\n" + SEP);
        System.out.println("  2. Directed Weighted Graph — Dijkstra");
        System.out.println(SEP);

        AdjGraph wg = new AdjGraph(true);
        int[][] wedges = {{0,1,4},{0,2,1},{2,1,2},{1,3,1},{2,3,5},{3,4,3}};
        for (int[] e : wedges) wg.addEdge(e[0], e[1], e[2]);
        wg.display();

        Map<Integer, Double> dist = wg.dijkstra(0);
        System.out.println("\nDijkstra distances from 0:");
        new TreeMap<>(dist).forEach((v, d) ->
                System.out.printf("  0 -> %d : %.0f%n", v, d));

        List<Integer> path = new ArrayList<>();
        double d = wg.dijkstraPath(0, 4, path);
        System.out.printf("%nShortest path 0->4 : %s (distance=%.0f)%n", path, d);

        // ── 3. DAG — Topological Sort ─────────────────────────
        System.out.println("\n" + SEP);
        System.out.println("  3. DAG — Topological Sort");
        System.out.println(SEP);

        AdjGraph dag = new AdjGraph(true);
        int[][] dagEdges = {{5,2},{5,0},{4,0},{4,1},{2,3},{3,1}};
        for (int[] e : dagEdges) dag.addEdge(e[0], e[1]);
        dag.display();

        System.out.println("\nHas cycle (directed)  : " + dag.hasCycleDirected());
        System.out.println("Topo sort (Kahn BFS)  : " + dag.topologicalSortKahn());
        System.out.println("Topo sort (DFS)       : " + dag.topologicalSortDFS());

        // ── 4. Graph with Cycle ───────────────────────────────
        System.out.println("\n" + SEP);
        System.out.println("  4. Directed Graph WITH Cycle");
        System.out.println(SEP);

        AdjGraph cg = new AdjGraph(true);
        for (int[] e : new int[][]{{0,1},{1,2},{2,0},{2,3}}) cg.addEdge(e[0], e[1]);
        cg.display();
        System.out.println("\nHas cycle (directed)  : " + cg.hasCycleDirected());
        System.out.println("Topo sort (Kahn)      : " + cg.topologicalSortKahn()
                + " (empty = cycle)");

        // ── 5. Undirected Weighted — Prim's MST ───────────────
        System.out.println("\n" + SEP);
        System.out.println("  5. Undirected Weighted Graph — Prim's MST");
        System.out.println(SEP);

        AdjGraph mg = new AdjGraph(false);
        int[][] medges = {{0,1,2},{0,3,6},{1,2,3},{1,3,8},{1,4,5},{2,4,7},{3,4,9}};
        for (int[] e : medges) mg.addEdge(e[0], e[1], e[2]);
        mg.display();

        List<int[]> mst = mg.primsMST(0);
        int total = mst.stream().mapToInt(e -> e[0]).sum();
        System.out.print("\nPrim's MST edges: ");
        for (int[] e : mst) System.out.printf("[%d-%d w=%d] ", e[1], e[2], e[0]);
        System.out.println("\nTotal MST weight: " + total);

        // ── 6. Disconnected Graph — Components ────────────────
        System.out.println("\n" + SEP);
        System.out.println("  6. Disconnected Graph — Connected Components");
        System.out.println(SEP);

        AdjGraph dg = new AdjGraph(false);
        for (int[] e : new int[][]{{0,1},{1,2},{3,4},{5,6},{6,7}}) dg.addEdge(e[0], e[1]);
        dg.addVertex(8);
        dg.display();
        System.out.println("\nConnected components: " + dg.connectedComponents());

        // ── 7. Adjacency Matrix ───────────────────────────────
        System.out.println("\n" + SEP);
        System.out.println("  7. Adjacency Matrix Representation");
        System.out.println(SEP);

        MatrixGraph mm = new MatrixGraph(5, false);
        for (int[] e : new int[][]{{0,1,3},{0,2,1},{1,3,4},{2,3,2},{3,4,5}})
            mm.addEdge(e[0], e[1], e[2]);
        mm.display();
        System.out.println("\nBFS from 0    : " + mm.bfs(0));
        System.out.println("DFS from 0    : " + mm.dfs(0));
        System.out.println("Has edge 0->1 : " + mm.hasEdge(0, 1));
        System.out.println("Has edge 0->4 : " + mm.hasEdge(0, 4));

        // ── Complexity Summary ────────────────────────────────
        System.out.println("\n" + SEP);
        System.out.println("  Complexity Summary");
        System.out.println(SEP);
        System.out.printf("%-30s %-18s %s%n", "Operation", "Adj List", "Adj Matrix");
        System.out.println("-".repeat(60));
        String[][] rows = {
            {"Space",              "O(V + E)",          "O(V²)"},
            {"Add vertex",         "O(1)",              "O(V²) resize"},
            {"Add edge",           "O(1)",              "O(1)"},
            {"Remove edge",        "O(E)",              "O(1)"},
            {"Has edge",           "O(degree)",         "O(1)"},
            {"Get neighbours",     "O(degree)",         "O(V)"},
            {"BFS / DFS",          "O(V + E)",          "O(V²)"},
            {"Dijkstra",           "O((V+E)logV)",      "O(V²)"},
            {"Prim's MST",         "O((V+E)logV)",      "O(V²)"},
            {"Topological Sort",   "O(V + E)",          "O(V²)"},
            {"Cycle Detection",    "O(V + E)",          "O(V²)"},
        };
        for (String[] r : rows)
            System.out.printf("  %-28s %-18s %s%n", r[0], r[1], r[2]);
    }
}
