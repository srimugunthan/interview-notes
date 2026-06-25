class HashMap:
    def __init__(self, capacity=16):
        self.capacity = capacity
        self.size = 0
        self.buckets = [[] for _ in range(self.capacity)]  # each bucket is a list of (key, value) pairs

    def _hash(self, key):
        return hash(key) % self.capacity

    # Insert / Update - O(1) average
    def put(self, key, value):
        idx = self._hash(key)
        bucket = self.buckets[idx]
        for i, (k, v) in enumerate(bucket):
            if k == key:
                bucket[i] = (key, value)    # update existing key
                return
        bucket.append((key, value))         # insert new key
        self.size += 1

        # Resize if load factor exceeds 0.75
        if self.size / self.capacity > 0.75:
            self._resize()

    # Get value by key - O(1) average
    def get(self, key):
        idx = self._hash(key)
        bucket = self.buckets[idx]
        for k, v in bucket:
            if k == key:
                return v
        raise KeyError(f"Key '{key}' not found")

    # Delete by key - O(1) average
    def delete(self, key):
        idx = self._hash(key)
        bucket = self.buckets[idx]
        for i, (k, v) in enumerate(bucket):
            if k == key:
                bucket.pop(i)
                self.size -= 1
                return
        raise KeyError(f"Key '{key}' not found")

    # Check key existence - O(1) average
    def contains(self, key):
        idx = self._hash(key)
        bucket = self.buckets[idx]
        return any(k == key for k, v in bucket)

    # Resize when load factor exceeded
    def _resize(self):
        old_buckets = self.buckets
        self.capacity *= 2
        self.buckets = [[] for _ in range(self.capacity)]
        self.size = 0
        for bucket in old_buckets:
            for k, v in bucket:
                self.put(k, v)

    def display(self):
        for i, bucket in enumerate(self.buckets):
            if bucket:
                print(f"  bucket[{i}] -> {bucket}")


hm = HashMap()
hm.put("name", "Alice")
hm.put("age", 30)
hm.put("city", "Bengaluru")
hm.put("age", 31)               # update existing key

print(hm.get("name"))           # Alice
print(hm.get("age"))            # 31
print(hm.contains("city"))      # True
print(hm.contains("salary"))    # False

hm.delete("city")
print(hm.contains("city"))      # False

hm.display()
