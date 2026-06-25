"""
Graph Data Structure Implementation in Python
===============================================
A Graph is a set of vertices (nodes) connected by edges.

Types covered:
  - Undirected / Directed graphs
  - Unweighted / Weighted graphs
  - Representations: Adjacency List, Adjacency Matrix

Algorithms included:
  Traversals   : BFS, DFS (iterative + recursive)
  Shortest path: Dijkstra (weighted), BFS (unweighted)
  Spanning tree: Prim's MST
  Ordering     : Topological Sort (Kahn's BFS + DFS)
  Detection    : Cycle detection (directed + undirected)
  Analysis     : Connected components, Bipartite check
  Path         : All paths between two nodes

Graph representations:
  Adjacency List  — O(V + E) space, efficient for sparse graphs
  Adjacency Matrix — O(V²) space, O(1) edge lookup, good for dense graphs
"""

from collections import defaultdict, deque
import heapq


# ══════════════════════════════════════════════════════════════════════════════
# 1. ADJACENCY LIST GRAPH
# ══════════════════════════════════════════════════════════════════════════════

class Graph:
    """
    General-purpose graph using an adjacency list.
    Supports directed/undirected and weighted/unweighted edges.

    Space : O(V + E)
    """

    def __init__(self, directed: bool = False):
        """
        Args:
            directed : True for directed graph (digraph), False for undirected
        """
        self.directed   = directed
        self.adj        = defaultdict(list)   # vertex -> [(neighbour, weight)]
        self.vertices   = set()

    # ── Add Vertex / Edge ─────────────────────────────────────────────────────

    def add_vertex(self, v) -> None:
        """Add an isolated vertex. Time : O(1)"""
        self.vertices.add(v)
        if v not in self.adj:
            self.adj[v] = []

    def add_edge(self, u, v, weight: float = 1) -> None:
        """
        Add an edge from u to v with optional weight.
        For undirected graphs, adds edge in both directions.
        Time : O(1)
        """
        self.vertices.add(u)
        self.vertices.add(v)
        self.adj[u].append((v, weight))
        if not self.directed:
            self.adj[v].append((u, weight))

    def remove_edge(self, u, v) -> None:
        """Remove edge u->v. Time : O(degree(u))"""
        self.adj[u] = [(n, w) for n, w in self.adj[u] if n != v]
        if not self.directed:
            self.adj[v] = [(n, w) for n, w in self.adj[v] if n != u]

    def remove_vertex(self, v) -> None:
        """Remove vertex and all its edges. Time : O(V + E)"""
        self.vertices.discard(v)
        self.adj.pop(v, None)
        for u in self.adj:
            self.adj[u] = [(n, w) for n, w in self.adj[u] if n != v]

    def has_edge(self, u, v) -> bool:
        """Time : O(degree(u))"""
        return any(n == v for n, _ in self.adj[u])

    def neighbours(self, v) -> list:
        """Return list of (neighbour, weight) for vertex v."""
        return self.adj[v]

    def degree(self, v) -> int:
        """Return degree (number of edges) of vertex v."""
        return len(self.adj[v])

    # ── Traversals ────────────────────────────────────────────────────────────

    def bfs(self, start) -> list:
        """
        Breadth-First Search from start vertex.
        Explores level by level — shortest path in unweighted graphs.
        Time : O(V + E)
        """
        visited = set()
        order   = []
        queue   = deque([start])
        visited.add(start)

        while queue:
            v = queue.popleft()
            order.append(v)
            for neighbour, _ in self.adj[v]:
                if neighbour not in visited:
                    visited.add(neighbour)
                    queue.append(neighbour)
        return order

    def dfs(self, start) -> list:
        """
        Depth-First Search from start vertex (iterative).
        Time : O(V + E)
        """
        visited = set()
        order   = []
        stack   = [start]

        while stack:
            v = stack.pop()
            if v not in visited:
                visited.add(v)
                order.append(v)
                # push neighbours in reverse to maintain left-to-right order
                for neighbour, _ in reversed(self.adj[v]):
                    if neighbour not in visited:
                        stack.append(neighbour)
        return order

    def dfs_recursive(self, start) -> list:
        """Depth-First Search (recursive). Time : O(V + E)"""
        visited = set()
        order   = []

        def _dfs(v):
            visited.add(v)
            order.append(v)
            for neighbour, _ in self.adj[v]:
                if neighbour not in visited:
                    _dfs(neighbour)

        _dfs(start)
        return order

    # ── Shortest Paths ────────────────────────────────────────────────────────

    def bfs_shortest_path(self, start, end) -> list:
        """
        Shortest path in an unweighted graph using BFS.
        Returns the path as a list of vertices, or [] if no path.
        Time : O(V + E)
        """
        if start == end:
            return [start]

        visited = {start}
        parent  = {start: None}
        queue   = deque([start])

        while queue:
            v = queue.popleft()
            for neighbour, _ in self.adj[v]:
                if neighbour not in visited:
                    visited.add(neighbour)
                    parent[neighbour] = v
                    if neighbour == end:
                        return self._reconstruct_path(parent, start, end)
                    queue.append(neighbour)
        return []   # no path found

    def dijkstra(self, start) -> tuple:
        """
        Dijkstra's shortest path algorithm (weighted graph, non-negative weights).
        Returns (distances dict, previous dict) from start to all reachable vertices.
        Time : O((V + E) log V)
        """
        dist     = {v: float('inf') for v in self.vertices}
        prev     = {v: None for v in self.vertices}
        dist[start] = 0
        heap     = [(0, start)]   # (distance, vertex)

        while heap:
            d, u = heapq.heappop(heap)
            if d > dist[u]:
                continue   # stale entry
            for v, weight in self.adj[u]:
                new_dist = dist[u] + weight
                if new_dist < dist[v]:
                    dist[v] = new_dist
                    prev[v] = u
                    heapq.heappush(heap, (new_dist, v))

        return dist, prev

    def dijkstra_path(self, start, end) -> tuple:
        """
        Returns (distance, path) from start to end using Dijkstra.
        Time : O((V + E) log V)
        """
        dist, prev = self.dijkstra(start)
        if dist[end] == float('inf'):
            return float('inf'), []
        path = self._reconstruct_path(prev, start, end)
        return dist[end], path

    def _reconstruct_path(self, prev, start, end) -> list:
        path = []
        curr = end
        while curr is not None:
            path.append(curr)
            curr = prev[curr]
        path.reverse()
        return path if path[0] == start else []

    # ── Minimum Spanning Tree ─────────────────────────────────────────────────

    def prims_mst(self, start=None) -> list:
        """
        Prim's Minimum Spanning Tree algorithm.
        Only valid for undirected weighted graphs.
        Returns list of (weight, u, v) edges in the MST.
        Time : O((V + E) log V)
        """
        if start is None:
            start = next(iter(self.vertices))

        in_mst   = set()
        mst_edges = []
        total_weight = 0
        # (weight, from_vertex, to_vertex)
        heap     = [(0, start, start)]

        while heap and len(in_mst) < len(self.vertices):
            weight, u, v = heapq.heappop(heap)
            if v in in_mst:
                continue
            in_mst.add(v)
            if u != v:
                mst_edges.append((weight, u, v))
                total_weight += weight
            for neighbour, w in self.adj[v]:
                if neighbour not in in_mst:
                    heapq.heappush(heap, (w, v, neighbour))

        return mst_edges, total_weight

    # ── Topological Sort ──────────────────────────────────────────────────────

    def topological_sort_kahn(self) -> list:
        """
        Topological sort using Kahn's algorithm (BFS-based).
        Only valid for Directed Acyclic Graphs (DAGs).
        Returns sorted order, or [] if graph has a cycle.
        Time : O(V + E)
        """
        in_degree = {v: 0 for v in self.vertices}
        for u in self.vertices:
            for v, _ in self.adj[u]:
                in_degree[v] += 1

        queue  = deque(v for v in self.vertices if in_degree[v] == 0)
        result = []

        while queue:
            v = queue.popleft()
            result.append(v)
            for neighbour, _ in self.adj[v]:
                in_degree[neighbour] -= 1
                if in_degree[neighbour] == 0:
                    queue.append(neighbour)

        return result if len(result) == len(self.vertices) else []   # [] = cycle detected

    def topological_sort_dfs(self) -> list:
        """
        Topological sort using DFS-based approach.
        Adds vertex to stack AFTER all descendants are visited.
        Time : O(V + E)
        """
        visited = set()
        stack   = []

        def _dfs(v):
            visited.add(v)
            for neighbour, _ in self.adj[v]:
                if neighbour not in visited:
                    _dfs(neighbour)
            stack.append(v)

        for v in self.vertices:
            if v not in visited:
                _dfs(v)

        return stack[::-1]

    # ── Cycle Detection ───────────────────────────────────────────────────────

    def has_cycle_directed(self) -> bool:
        """
        Detect cycle in a directed graph using DFS with colouring.
        WHITE=0 (unvisited), GRAY=1 (in-stack), BLACK=2 (done).
        Time : O(V + E)
        """
        WHITE, GRAY, BLACK = 0, 1, 2
        colour = {v: WHITE for v in self.vertices}

        def _dfs(v) -> bool:
            colour[v] = GRAY
            for neighbour, _ in self.adj[v]:
                if colour[neighbour] == GRAY:
                    return True   # back edge = cycle
                if colour[neighbour] == WHITE and _dfs(neighbour):
                    return True
            colour[v] = BLACK
            return False

        return any(_dfs(v) for v in self.vertices if colour[v] == WHITE)

    def has_cycle_undirected(self) -> bool:
        """
        Detect cycle in an undirected graph using DFS with parent tracking.
        Time : O(V + E)
        """
        visited = set()

        def _dfs(v, parent) -> bool:
            visited.add(v)
            for neighbour, _ in self.adj[v]:
                if neighbour not in visited:
                    if _dfs(neighbour, v):
                        return True
                elif neighbour != parent:
                    return True   # back edge to non-parent = cycle
            return False

        return any(_dfs(v, -1) for v in self.vertices if v not in visited)

    # ── Connected Components ──────────────────────────────────────────────────

    def connected_components(self) -> list:
        """
        Find all connected components (undirected graph).
        Returns list of components, each a list of vertices.
        Time : O(V + E)
        """
        visited    = set()
        components = []

        def _dfs(v, component):
            visited.add(v)
            component.append(v)
            for neighbour, _ in self.adj[v]:
                if neighbour not in visited:
                    _dfs(neighbour, component)

        for v in self.vertices:
            if v not in visited:
                component = []
                _dfs(v, component)
                components.append(sorted(component))

        return components

    # ── Bipartite Check ───────────────────────────────────────────────────────

    def is_bipartite(self) -> tuple:
        """
        Check if the graph is bipartite (2-colourable).
        A graph is bipartite if and only if it contains no odd-length cycles.
        Returns (True, colouring_dict) or (False, {}).
        Time : O(V + E)
        """
        colour = {}

        for start in self.vertices:
            if start in colour:
                continue
            colour[start] = 0
            queue = deque([start])
            while queue:
                v = queue.popleft()
                for neighbour, _ in self.adj[v]:
                    if neighbour not in colour:
                        colour[neighbour] = 1 - colour[v]
                        queue.append(neighbour)
                    elif colour[neighbour] == colour[v]:
                        return False, {}
        return True, colour

    # ── All Paths ─────────────────────────────────────────────────────────────

    def all_paths(self, start, end) -> list:
        """
        Find all simple paths from start to end using DFS backtracking.
        Time : O(V! / (V - path_length)!) in worst case
        """
        all_found = []

        def _dfs(v, path, visited):
            if v == end:
                all_found.append(list(path))
                return
            for neighbour, _ in self.adj[v]:
                if neighbour not in visited:
                    visited.add(neighbour)
                    path.append(neighbour)
                    _dfs(neighbour, path, visited)
                    path.pop()
                    visited.discard(neighbour)

        _dfs(start, [start], {start})
        return all_found

    # ── Display ───────────────────────────────────────────────────────────────

    def display(self) -> None:
        kind = "Directed" if self.directed else "Undirected"
        print(f"\n{kind} Graph — Adjacency List:")
        for v in sorted(self.vertices, key=str):
            neighbours = ", ".join(
                f"{n}(w={w})" if w != 1 else str(n)
                for n, w in self.adj[v]
            )
            print(f"  {v} -> [{neighbours}]")
        print(f"  Vertices: {len(self.vertices)}, Edges: {sum(len(e) for e in self.adj.values())}")

    def __repr__(self):
        return (f"Graph(directed={self.directed}, "
                f"vertices={len(self.vertices)}, "
                f"edges={sum(len(e) for e in self.adj.values())})")


# ══════════════════════════════════════════════════════════════════════════════
# 2. ADJACENCY MATRIX GRAPH
# ══════════════════════════════════════════════════════════════════════════════

class MatrixGraph:
    """
    Graph represented as an adjacency matrix.
    Best for dense graphs where O(1) edge lookup matters.

    Space : O(V²)
    Edge lookup  : O(1)
    Add edge     : O(1)
    Get neighbours : O(V)
    """

    def __init__(self, num_vertices: int, directed: bool = False):
        self.n        = num_vertices
        self.directed = directed
        # matrix[i][j] = weight of edge i->j, 0 means no edge
        self.matrix   = [[0] * num_vertices for _ in range(num_vertices)]

    def add_edge(self, u: int, v: int, weight: float = 1) -> None:
        """Time : O(1)"""
        self.matrix[u][v] = weight
        if not self.directed:
            self.matrix[v][u] = weight

    def remove_edge(self, u: int, v: int) -> None:
        """Time : O(1)"""
        self.matrix[u][v] = 0
        if not self.directed:
            self.matrix[v][u] = 0

    def has_edge(self, u: int, v: int) -> bool:
        """Time : O(1)"""
        return self.matrix[u][v] != 0

    def neighbours(self, v: int) -> list:
        """Return list of (neighbour, weight). Time : O(V)"""
        return [(j, self.matrix[v][j])
                for j in range(self.n) if self.matrix[v][j] != 0]

    def bfs(self, start: int) -> list:
        """Time : O(V²)"""
        visited = [False] * self.n
        order   = []
        queue   = deque([start])
        visited[start] = True

        while queue:
            v = queue.popleft()
            order.append(v)
            for j in range(self.n):
                if self.matrix[v][j] != 0 and not visited[j]:
                    visited[j] = True
                    queue.append(j)
        return order

    def dfs(self, start: int) -> list:
        """Time : O(V²)"""
        visited = [False] * self.n
        order   = []

        def _dfs(v):
            visited[v] = True
            order.append(v)
            for j in range(self.n):
                if self.matrix[v][j] != 0 and not visited[j]:
                    _dfs(j)

        _dfs(start)
        return order

    def display(self) -> None:
        kind = "Directed" if self.directed else "Undirected"
        print(f"\n{kind} Graph — Adjacency Matrix ({self.n}×{self.n}):")
        header = "    " + "  ".join(f"{j:2}" for j in range(self.n))
        print(header)
        print("    " + "--" * self.n * 2)
        for i in range(self.n):
            row = "  ".join(f"{self.matrix[i][j]:2}" for j in range(self.n))
            print(f"  {i} | {row}")


# ══════════════════════════════════════════════════════════════════════════════
# DEMO
# ══════════════════════════════════════════════════════════════════════════════

if __name__ == "__main__":

    sep = "=" * 60

    # ── 1. Undirected Unweighted Graph ────────────────────────
    print(sep)
    print("  1. Undirected Unweighted Graph")
    print(sep)

    g = Graph(directed=False)
    for u, v in [(0,1),(0,2),(1,3),(1,4),(2,5),(3,5),(4,5)]:
        g.add_edge(u, v)
    g.display()

    print(f"\nBFS from 0  : {g.bfs(0)}")
    print(f"DFS from 0  : {g.dfs(0)}")
    print(f"DFS(recur)  : {g.dfs_recursive(0)}")

    print(f"\nShortest path 0->5 (BFS): {g.bfs_shortest_path(0, 5)}")
    print(f"All paths   0->5        : {g.all_paths(0, 5)}")

    print(f"\nConnected components    : {g.connected_components()}")
    print(f"Has cycle (undirected)  : {g.has_cycle_undirected()}")

    bipartite, colouring = g.is_bipartite()
    print(f"Is bipartite            : {bipartite}")
    if bipartite:
        group0 = [v for v, c in colouring.items() if c == 0]
        group1 = [v for v, c in colouring.items() if c == 1]
        print(f"  Group 0: {sorted(group0)}")
        print(f"  Group 1: {sorted(group1)}")

    # ── 2. Directed Weighted Graph — Dijkstra ─────────────────
    print(f"\n{sep}")
    print("  2. Directed Weighted Graph — Dijkstra")
    print(sep)

    wg = Graph(directed=True)
    edges = [
        (0, 1, 4), (0, 2, 1), (2, 1, 2),
        (1, 3, 1), (2, 3, 5), (3, 4, 3)
    ]
    for u, v, w in edges:
        wg.add_edge(u, v, w)
    wg.display()

    dist, _ = wg.dijkstra(0)
    print("\nDijkstra distances from 0:")
    for v in sorted(dist):
        print(f"  0 -> {v} : {dist[v]}")

    d, path = wg.dijkstra_path(0, 4)
    print(f"\nShortest path 0->4 : {path} (distance={d})")

    # ── 3. Directed Graph — Cycle Detection & Topo Sort ───────
    print(f"\n{sep}")
    print("  3. DAG — Topological Sort")
    print(sep)

    dag = Graph(directed=True)
    for u, v in [(5,2),(5,0),(4,0),(4,1),(2,3),(3,1)]:
        dag.add_edge(u, v)
    dag.display()

    print(f"\nHas cycle (directed)   : {dag.has_cycle_directed()}")
    print(f"Topo sort (Kahn BFS)   : {dag.topological_sort_kahn()}")
    print(f"Topo sort (DFS)        : {dag.topological_sort_dfs()}")

    # ── 4. Graph with Cycle ───────────────────────────────────
    print(f"\n{sep}")
    print("  4. Directed Graph WITH Cycle")
    print(sep)

    cg = Graph(directed=True)
    for u, v in [(0,1),(1,2),(2,0),(2,3)]:
        cg.add_edge(u, v)
    cg.display()
    print(f"\nHas cycle (directed)   : {cg.has_cycle_directed()}")
    print(f"Topo sort (Kahn)       : {cg.topological_sort_kahn()} (empty = cycle found)")

    # ── 5. Weighted Undirected — Prim's MST ───────────────────
    print(f"\n{sep}")
    print("  5. Undirected Weighted Graph — Prim's MST")
    print(sep)

    mg = Graph(directed=False)
    for u, v, w in [(0,1,2),(0,3,6),(1,2,3),(1,3,8),(1,4,5),(2,4,7),(3,4,9)]:
        mg.add_edge(u, v, w)
    mg.display()

    mst_edges, total = mg.prims_mst(start=0)
    print(f"\nPrim's MST edges: {mst_edges}")
    print(f"Total MST weight: {total}")

    # ── 6. Disconnected Graph ─────────────────────────────────
    print(f"\n{sep}")
    print("  6. Disconnected Graph — Connected Components")
    print(sep)

    dg = Graph(directed=False)
    for u, v in [(0,1),(1,2),(3,4),(5,6),(6,7)]:
        dg.add_edge(u, v)
    dg.add_vertex(8)
    dg.display()
    print(f"\nConnected components: {dg.connected_components()}")

    # ── 7. Adjacency Matrix Graph ─────────────────────────────
    print(f"\n{sep}")
    print("  7. Adjacency Matrix Representation")
    print(sep)

    mg2 = MatrixGraph(5, directed=False)
    for u, v, w in [(0,1,3),(0,2,1),(1,3,4),(2,3,2),(3,4,5)]:
        mg2.add_edge(u, v, w)
    mg2.display()

    print(f"\nBFS from 0 : {mg2.bfs(0)}")
    print(f"DFS from 0 : {mg2.dfs(0)}")
    print(f"Has edge 0->1 : {mg2.has_edge(0, 1)}")
    print(f"Has edge 0->4 : {mg2.has_edge(0, 4)}")
    print(f"Neighbours of 1 : {mg2.neighbours(1)}")

    # ── Complexity Summary ────────────────────────────────────
    print(f"\n{sep}")
    print("  Complexity Summary")
    print(sep)
    print(f"{'Operation':<30} {'Adj List':<15} {'Adj Matrix'}")
    print("-" * 60)
    rows = [
        ("Space",              "O(V + E)",   "O(V²)"),
        ("Add vertex",         "O(1)",       "O(V²) resize"),
        ("Add edge",           "O(1)",       "O(1)"),
        ("Remove edge",        "O(E)",       "O(1)"),
        ("Has edge",           "O(degree)",  "O(1)"),
        ("Get neighbours",     "O(degree)",  "O(V)"),
        ("BFS / DFS",          "O(V + E)",   "O(V²)"),
        ("Dijkstra",           "O((V+E)logV)","O(V²)"),
        ("Prim's MST",         "O((V+E)logV)","O(V²)"),
        ("Topological Sort",   "O(V + E)",   "O(V²)"),
        ("Cycle Detection",    "O(V + E)",   "O(V²)"),
    ]
    for op, al, am in rows:
        print(f"  {op:<28} {al:<15} {am}")
