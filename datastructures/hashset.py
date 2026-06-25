class HashSet:
    def __init__(self, capacity=16):
        self.capacity = capacity
        self.size = 0
        self.buckets = [[] for _ in range(self.capacity)]

    def _hash(self, key):
        return hash(key) % self.capacity

    # Add element - O(1) average
    def add(self, key):
        idx = self._hash(key)
        bucket = self.buckets[idx]
        if key not in bucket:
            bucket.append(key)
            self.size += 1

        # Resize if load factor exceeds 0.75
        if self.size / self.capacity > 0.75:
            self._resize()

    # Remove element - O(1) average
    def remove(self, key):
        idx = self._hash(key)
        bucket = self.buckets[idx]
        if key in bucket:
            bucket.remove(key)
            self.size -= 1
        else:
            raise KeyError(f"Key '{key}' not found")

    # Check membership - O(1) average
    def contains(self, key):
        idx = self._hash(key)
        return key in self.buckets[idx]

    # Set operations
    def union(self, other):
        result = HashSet()
        for bucket in self.buckets:
            for key in bucket:
                result.add(key)
        for bucket in other.buckets:
            for key in bucket:
                result.add(key)
        return result

    def intersection(self, other):
        result = HashSet()
        for bucket in self.buckets:
            for key in bucket:
                if other.contains(key):
                    result.add(key)
        return result

    def difference(self, other):
        result = HashSet()
        for bucket in self.buckets:
            for key in bucket:
                if not other.contains(key):
                    result.add(key)
        return result

    def _resize(self):
        old_buckets = self.buckets
        self.capacity *= 2
        self.buckets = [[] for _ in range(self.capacity)]
        self.size = 0
        for bucket in old_buckets:
            for key in bucket:
                self.add(key)

    def to_list(self):
        result = []
        for bucket in self.buckets:
            result.extend(bucket)
        return result

    def display(self):
        print("HashSet:", self.to_list())


s1 = HashSet()
s1.add(1)
s1.add(2)
s1.add(3)
s1.add(2)               # duplicate, ignored
s1.display()            # HashSet: [1, 2, 3]

s2 = HashSet()
s2.add(3)
s2.add(4)
s2.add(5)

print("Union:       ", s1.union(s2).to_list())          # [1, 2, 3, 4, 5]
print("Intersection:", s1.intersection(s2).to_list())   # [3]
print("Difference:  ", s1.difference(s2).to_list())     # [1, 2]

s1.remove(2)
print(s1.contains(2))   # False
print(s1.contains(3))   # True
