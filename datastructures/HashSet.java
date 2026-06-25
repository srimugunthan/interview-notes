import java.util.LinkedList;
import java.util.ArrayList;

public class HashSet {

    private int capacity;
    private int size;
    private LinkedList<Integer>[] buckets;
    private static final double LOAD_FACTOR = 0.75;

    @SuppressWarnings("unchecked")
    public HashSet(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.buckets = new LinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new LinkedList<>();
        }
    }

    public HashSet() {
        this(16);
    }

    private int hash(int key) {
        return Math.abs(Integer.hashCode(key)) % capacity;
    }

    // Add element - O(1) average
    public void add(int key) {
        int idx = hash(key);
        if (!buckets[idx].contains(key)) {
            buckets[idx].add(key);
            size++;
        }

        if ((double) size / capacity > LOAD_FACTOR) {
            resize();
        }
    }

    // Remove element - O(1) average
    public void remove(int key) {
        int idx = hash(key);
        if (buckets[idx].contains(key)) {
            buckets[idx].remove(Integer.valueOf(key));
            size--;
        } else {
            throw new RuntimeException("Key " + key + " not found");
        }
    }

    // Check membership - O(1) average
    public boolean contains(int key) {
        int idx = hash(key);
        return buckets[idx].contains(key);
    }

    // Union
    public HashSet union(HashSet other) {
        HashSet result = new HashSet();
        for (LinkedList<Integer> bucket : this.buckets)
            for (int key : bucket) result.add(key);
        for (LinkedList<Integer> bucket : other.buckets)
            for (int key : bucket) result.add(key);
        return result;
    }

    // Intersection
    public HashSet intersection(HashSet other) {
        HashSet result = new HashSet();
        for (LinkedList<Integer> bucket : this.buckets)
            for (int key : bucket)
                if (other.contains(key)) result.add(key);
        return result;
    }

    // Difference (this - other)
    public HashSet difference(HashSet other) {
        HashSet result = new HashSet();
        for (LinkedList<Integer> bucket : this.buckets)
            for (int key : bucket)
                if (!other.contains(key)) result.add(key);
        return result;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = capacity * 2;
        LinkedList<Integer>[] newBuckets = new LinkedList[newCapacity];
        for (int i = 0; i < newCapacity; i++) {
            newBuckets[i] = new LinkedList<>();
        }
        for (LinkedList<Integer> bucket : buckets) {
            for (int key : bucket) {
                int newIdx = Math.abs(Integer.hashCode(key)) % newCapacity;
                newBuckets[newIdx].add(key);
            }
        }
        buckets = newBuckets;
        capacity = newCapacity;
    }

    public ArrayList<Integer> toList() {
        ArrayList<Integer> result = new ArrayList<>();
        for (LinkedList<Integer> bucket : buckets)
            result.addAll(bucket);
        return result;
    }

    public int size() { return size; }

    public void display() {
        System.out.println("HashSet: " + toList());
    }

    public static void main(String[] args) {
        HashSet s1 = new HashSet();
        s1.add(1);
        s1.add(2);
        s1.add(3);
        s1.add(2);              // duplicate, ignored
        s1.display();           // HashSet: [1, 2, 3]

        HashSet s2 = new HashSet();
        s2.add(3);
        s2.add(4);
        s2.add(5);

        System.out.println("Union:        " + s1.union(s2).toList());
        System.out.println("Intersection: " + s1.intersection(s2).toList());
        System.out.println("Difference:   " + s1.difference(s2).toList());

        s1.remove(2);
        System.out.println(s1.contains(2));  // false
        System.out.println(s1.contains(3));  // true
    }
}
