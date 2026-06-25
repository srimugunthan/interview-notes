import java.util.ArrayList;
import java.util.LinkedList;

public class HashMap {

    // Each entry holds a key-value pair
    static class Entry {
        String key;
        int value;

        Entry(String key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    private int capacity;
    private int size;
    private LinkedList<Entry>[] buckets;
    private static final double LOAD_FACTOR = 0.75;

    @SuppressWarnings("unchecked")
    public HashMap(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.buckets = new LinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new LinkedList<>();
        }
    }

    public HashMap() {
        this(16);
    }

    private int hash(String key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    // Insert / Update - O(1) average
    public void put(String key, int value) {
        int idx = hash(key);
        LinkedList<Entry> bucket = buckets[idx];

        for (Entry entry : bucket) {
            if (entry.key.equals(key)) {
                entry.value = value;    // update existing
                return;
            }
        }

        bucket.add(new Entry(key, value));  // insert new
        size++;

        if ((double) size / capacity > LOAD_FACTOR) {
            resize();
        }
    }

    // Get value by key - O(1) average
    public int get(String key) {
        int idx = hash(key);
        for (Entry entry : buckets[idx]) {
            if (entry.key.equals(key)) return entry.value;
        }
        throw new RuntimeException("Key '" + key + "' not found");
    }

    // Delete by key - O(1) average
    public void delete(String key) {
        int idx = hash(key);
        LinkedList<Entry> bucket = buckets[idx];
        for (Entry entry : bucket) {
            if (entry.key.equals(key)) {
                bucket.remove(entry);
                size--;
                return;
            }
        }
        throw new RuntimeException("Key '" + key + "' not found");
    }

    // Check key existence - O(1) average
    public boolean contains(String key) {
        int idx = hash(key);
        for (Entry entry : buckets[idx]) {
            if (entry.key.equals(key)) return true;
        }
        return false;
    }

    // Resize when load factor exceeded
    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = capacity * 2;
        LinkedList<Entry>[] newBuckets = new LinkedList[newCapacity];
        for (int i = 0; i < newCapacity; i++) {
            newBuckets[i] = new LinkedList<>();
        }

        for (LinkedList<Entry> bucket : buckets) {
            for (Entry entry : bucket) {
                int newIdx = Math.abs(entry.key.hashCode()) % newCapacity;
                newBuckets[newIdx].add(entry);
            }
        }

        buckets = newBuckets;
        capacity = newCapacity;
    }

    public int size() { return size; }

    public void display() {
        for (int i = 0; i < capacity; i++) {
            if (!buckets[i].isEmpty()) {
                System.out.print("  bucket[" + i + "] -> ");
                for (Entry entry : buckets[i]) {
                    System.out.print("{" + entry.key + ": " + entry.value + "} ");
                }
                System.out.println();
            }
        }
    }

    public static void main(String[] args) {
        HashMap hm = new HashMap();
        hm.put("name", 1);
        hm.put("age", 30);
        hm.put("city", 99);
        hm.put("age", 31);              // update existing key

        System.out.println(hm.get("age"));      // 31
        System.out.println(hm.contains("city")); // true

        hm.delete("city");
        System.out.println(hm.contains("city")); // false

        hm.display();
    }
}
