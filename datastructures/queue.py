from collections import deque

class Queue:
    def __init__(self):
        self._data = deque()

    def enqueue(self, item):    # O(1)
        self._data.append(item)

    def dequeue(self):          # O(1)
        if self.is_empty():
            raise IndexError("Queue underflow")
        return self._data.popleft()

    def front(self):            # O(1)
        if self.is_empty():
            raise IndexError("Queue is empty")
        return self._data[0]

    def is_empty(self):
        return len(self._data) == 0

    def size(self):
        return len(self._data)

    def __repr__(self):
        return f"Queue (front -> rear): {list(self._data)}"


q = Queue()
q.enqueue(10)
q.enqueue(20)
q.enqueue(30)
print(q)            # Queue (front -> rear): [10, 20, 30]
print(q.front())    # 10
print(q.dequeue())  # 10
print(q.size())     # 2
