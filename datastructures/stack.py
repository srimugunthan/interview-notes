class Stack:
    def __init__(self):
        self._data = []

    def push(self, item):       # O(1)
        self._data.append(item)

    def pop(self):              # O(1)
        if self.is_empty():
            raise IndexError("Stack underflow")
        return self._data.pop()

    def peek(self):             # O(1)
        if self.is_empty():
            raise IndexError("Stack is empty")
        return self._data[-1]

    def is_empty(self):
        return len(self._data) == 0

    def size(self):
        return len(self._data)

    def __repr__(self):
        return f"Stack (top -> bottom): {self._data[::-1]}"


s = Stack()
s.push(10)
s.push(20)
s.push(30)
print(s)            # Stack (top -> bottom): [30, 20, 10]
print(s.peek())     # 30
print(s.pop())      # 30
print(s.size())     # 2
