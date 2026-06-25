import java.util.Arrays;

/**
 * Heap Implementation in Java
 * =============================
 * A Heap is a complete binary tree stored as an array.
 *
 * MinHeap : root is always the smallest element
 * MaxHeap : root is always the largest element
 *
 * For a node at index i:
 *   Parent      : (i - 1) / 2
 *   Left child  : 2 * i + 1
 *   Right child : 2 * i + 2
 *
 * Supported Operations:
 *   push(val)     - Insert a value             O(log n)
 *   pop()         - Remove and return root     O(log n)
 *   peek()        - View root without removal  O(1)
 *   heapify(arr)  - Build heap from array      O(n)
 *   size()        - Number of elements         O(1)
 */
public class Heap {

    // ── Min Heap ─────────────────────────────────────────────────────────────

    static class MinHeap {
        private int[] data;
        private int   size;
        private int   capacity;

        public MinHeap(int capacity) {
            this.capacity = capacity;
            this.data     = new int[capacity];
            this.size     = 0;
        }

        public MinHeap() { this(64); }

        // ── Push ──────────────────────────────────────────────────────────────

        /** Insert val and sift up to restore heap property. Time : O(log n) */
        public void push(int val) {
            if (size == capacity) resize();
            data[size++] = val;
            siftUp(size - 1);
        }

        private void siftUp(int i) {
            while (i > 0) {
                int parent = (i - 1) / 2;
                if (data[i] < data[parent]) {
                    swap(i, parent);
                    i = parent;
                } else break;
            }
        }

        // ── Pop ───────────────────────────────────────────────────────────────

        /** Remove and return the minimum element (root). Time : O(log n) */
        public int pop() {
            if (isEmpty()) throw new RuntimeException("Heap is empty");
            int root = data[0];
            data[0]  = data[--size];
            siftDown(0);
            return root;
        }

        private void siftDown(int i) {
            while (true) {
                int smallest = i;
                int left     = 2 * i + 1;
                int right    = 2 * i + 2;
                if (left  < size && data[left]  < data[smallest]) smallest = left;
                if (right < size && data[right] < data[smallest]) smallest = right;
                if (smallest != i) { swap(i, smallest); i = smallest; }
                else break;
            }
        }

        // ── Peek ──────────────────────────────────────────────────────────────

        /** Return minimum without removing. Time : O(1) */
        public int peek() {
            if (isEmpty()) throw new RuntimeException("Heap is empty");
            return data[0];
        }

        // ── Heapify ───────────────────────────────────────────────────────────

        /** Build a min-heap from an arbitrary array in O(n). */
        public void heapify(int[] arr) {
            capacity = arr.length * 2;
            data     = Arrays.copyOf(arr, capacity);
            size     = arr.length;
            for (int i = size / 2 - 1; i >= 0; i--) {
                siftDown(i);
            }
        }

        // ── Utilities ─────────────────────────────────────────────────────────

        private void swap(int i, int j) {
            int tmp = data[i]; data[i] = data[j]; data[j] = tmp;
        }

        private void resize() {
            capacity *= 2;
            data      = Arrays.copyOf(data, capacity);
        }

        public boolean isEmpty() { return size == 0; }
        public int     size()    { return size; }

        public void display() {
            System.out.print("MinHeap array : ");
            for (int i = 0; i < size; i++) System.out.print(data[i] + " ");
            System.out.println();
        }

        @Override
        public String toString() {
            return "MinHeap" + Arrays.toString(Arrays.copyOf(data, size));
        }
    }


    // ── Max Heap ─────────────────────────────────────────────────────────────

    static class MaxHeap {
        private int[] data;
        private int   size;
        private int   capacity;

        public MaxHeap(int capacity) {
            this.capacity = capacity;
            this.data     = new int[capacity];
            this.size     = 0;
        }

        public MaxHeap() { this(64); }

        // ── Push ──────────────────────────────────────────────────────────────

        /** Time : O(log n) */
        public void push(int val) {
            if (size == capacity) resize();
            data[size++] = val;
            siftUp(size - 1);
        }

        private void siftUp(int i) {
            while (i > 0) {
                int parent = (i - 1) / 2;
                if (data[i] > data[parent]) {
                    swap(i, parent);
                    i = parent;
                } else break;
            }
        }

        // ── Pop ───────────────────────────────────────────────────────────────

        /** Remove and return the maximum element. Time : O(log n) */
        public int pop() {
            if (isEmpty()) throw new RuntimeException("Heap is empty");
            int root = data[0];
            data[0]  = data[--size];
            siftDown(0);
            return root;
        }

        private void siftDown(int i) {
            while (true) {
                int largest = i;
                int left    = 2 * i + 1;
                int right   = 2 * i + 2;
                if (left  < size && data[left]  > data[largest]) largest = left;
                if (right < size && data[right] > data[largest]) largest = right;
                if (largest != i) { swap(i, largest); i = largest; }
                else break;
            }
        }

        // ── Peek ──────────────────────────────────────────────────────────────

        /** Return maximum without removing. Time : O(1) */
        public int peek() {
            if (isEmpty()) throw new RuntimeException("Heap is empty");
            return data[0];
        }

        // ── Heapify ───────────────────────────────────────────────────────────

        /** Build a max-heap from an arbitrary array in O(n). */
        public void heapify(int[] arr) {
            capacity = arr.length * 2;
            data     = Arrays.copyOf(arr, capacity);
            size     = arr.length;
            for (int i = size / 2 - 1; i >= 0; i--) {
                siftDown(i);
            }
        }

        // ── Heap Sort (bonus) ─────────────────────────────────────────────────

        /** Sort array ascending using a MaxHeap. Time : O(n log n) */
        public static int[] heapSort(int[] arr) {
            MaxHeap h = new MaxHeap(arr.length);
            h.heapify(arr);
            int[] result = new int[arr.length];
            for (int i = arr.length - 1; i >= 0; i--) {
                result[i] = h.pop();
            }
            return result;
        }

        // ── Utilities ─────────────────────────────────────────────────────────

        private void swap(int i, int j) {
            int tmp = data[i]; data[i] = data[j]; data[j] = tmp;
        }

        private void resize() {
            capacity *= 2;
            data      = Arrays.copyOf(data, capacity);
        }

        public boolean isEmpty() { return size == 0; }
        public int     size()    { return size; }

        public void display() {
            System.out.print("MaxHeap array : ");
            for (int i = 0; i < size; i++) System.out.print(data[i] + " ");
            System.out.println();
        }

        @Override
        public String toString() {
            return "MaxHeap" + Arrays.toString(Arrays.copyOf(data, size));
        }
    }


    // ── Demo / Tests ─────────────────────────────────────────────────────────

    public static void main(String[] args) {

        System.out.println("=".repeat(55));
        System.out.println("  Heap Demo");
        System.out.println("=".repeat(55));

        // ── MinHeap ───────────────────────────────────────────
        System.out.println("\n── MinHeap ──");
        MinHeap minh = new MinHeap();
        for (int v : new int[]{10, 4, 15, 1, 7, 20, 3}) minh.push(v);

        System.out.println("Peek (min) : " + minh.peek());    // 1
        System.out.println("Pop        : " + minh.pop());     // 1
        System.out.println("Pop        : " + minh.pop());     // 3
        System.out.println("Size       : " + minh.size());    // 5
        minh.display();

        // ── Heapify ───────────────────────────────────────────
        System.out.println("\n── MinHeap.heapify (O(n) build) ──");
        MinHeap minh2 = new MinHeap();
        minh2.heapify(new int[]{9, 4, 7, 1, 8, 3, 6, 2, 5});
        System.out.print("Pop sequence : ");
        while (!minh2.isEmpty()) System.out.print(minh2.pop() + " "); // 1..9
        System.out.println();

        // ── MaxHeap ───────────────────────────────────────────
        System.out.println("\n── MaxHeap ──");
        MaxHeap maxh = new MaxHeap();
        for (int v : new int[]{10, 4, 15, 1, 7, 20, 3}) maxh.push(v);

        System.out.println("Peek (max) : " + maxh.peek());    // 20
        System.out.println("Pop        : " + maxh.pop());     // 20
        System.out.println("Pop        : " + maxh.pop());     // 15
        System.out.println("Size       : " + maxh.size());    // 5
        maxh.display();

        // ── Heap Sort ─────────────────────────────────────────
        System.out.println("\n── Heap Sort ──");
        int[] arr = {38, 12, 5, 99, 27, 3, 61};
        System.out.println("Input  : " + Arrays.toString(arr));
        System.out.println("Sorted : " + Arrays.toString(MaxHeap.heapSort(arr)));

        // ── K Largest elements ────────────────────────────────
        System.out.println("\n── K Largest Elements (k=3) ──");
        int[] data = {7, 10, 4, 3, 20, 15, 8, 2};
        int k = 3;
        MinHeap topK = new MinHeap(k + 1);
        for (int num : data) {
            topK.push(num);
            if (topK.size() > k) topK.pop();
        }
        System.out.println("Data  : " + Arrays.toString(data));
        System.out.print("Top " + k + " : ");
        int[] result = new int[k];
        for (int i = k - 1; i >= 0; i--) result[i] = topK.pop();
        System.out.println(Arrays.toString(result));           // [10, 15, 20]
    }
}
