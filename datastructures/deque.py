class Node:
    def __init__(self, data):
        self.data = data
        self.prev = None
        self.next = None

class Deque:
    def __init__(self):
        self.head = None   # front
        self.tail = None   # rear
        self._size = 0

    # Add to front - O(1)
    def add_front(self, data):
        new_node = Node(data)
        if self.head is None:
            self.head = self.tail = new_node
        else:
            new_node.next = self.head
            self.head.prev = new_node
            self.head = new_node
        self._size += 1

    # Add to rear - O(1)
    def add_rear(self, data):
        new_node = Node(data)
        if self.tail is None:
            self.head = self.tail = new_node
        else:
            new_node.prev = self.tail
            self.tail.next = new_node
            self.tail = new_node
        self._size += 1

    # Remove from front - O(1)
    def remove_front(self):
        if self.is_empty():
            raise IndexError("Deque is empty")
        data = self.head.data
        self.head = self.head.next
        if self.head:
            self.head.prev = None
        else:
            self.tail = None
        self._size -= 1
        return data

    # Remove from rear - O(1)
    def remove_rear(self):
        if self.is_empty():
            raise IndexError("Deque is empty")
        data = self.tail.data
        self.tail = self.tail.prev
        if self.tail:
            self.tail.next = None
        else:
            self.head = None
        self._size -= 1
        return data

    # Peek front - O(1)
    def peek_front(self):
        if self.is_empty():
            raise IndexError("Deque is empty")
        return self.head.data

    # Peek rear - O(1)
    def peek_rear(self):
        if self.is_empty():
            raise IndexError("Deque is empty")
        return self.tail.data

    def is_empty(self):
        return self._size == 0

    def size(self):
        return self._size

    def display(self):
        elements = []
        curr = self.head
        while curr:
            elements.append(str(curr.data))
            curr = curr.next
        print("front -> " + " <-> ".join(elements) + " <- rear")


dq = Deque()
dq.add_rear(10)
dq.add_rear(20)
dq.add_rear(30)
dq.add_front(5)
dq.display()                    # front -> 5 <-> 10 <-> 20 <-> 30 <- rear
print(dq.peek_front())          # 5
print(dq.peek_rear())           # 30
print(dq.remove_front())        # 5
print(dq.remove_rear())         # 30
dq.display()                    # front -> 10 <-> 20 <- rear
print(dq.size())                # 2
