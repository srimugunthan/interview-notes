class Node:
    def __init__(self, data):
        self.data = data
        self.next = None

class LinkedList:
    def __init__(self):
        self.head = None

    def append(self, data):          # O(n)
        new_node = Node(data)
        if not self.head:
            self.head = new_node
            return
        curr = self.head
        while curr.next:
            curr = curr.next
        curr.next = new_node

    def prepend(self, data):         # O(1)
        new_node = Node(data)
        new_node.next = self.head
        self.head = new_node

    def delete(self, data):          # O(n)
        if not self.head:
            return
        if self.head.data == data:
            self.head = self.head.next
            return
        curr = self.head
        while curr.next:
            if curr.next.data == data:
                curr.next = curr.next.next
                return
            curr = curr.next

    def search(self, data):          # O(n)
        curr = self.head
        while curr:
            if curr.data == data:
                return True
            curr = curr.next
        return False

    def display(self):
        elements = []
        curr = self.head
        while curr:
            elements.append(curr.data)
            curr = curr.next
        print(" -> ".join(map(str, elements)))


ll = LinkedList()
ll.append(10)
ll.append(20)
ll.append(30)
ll.prepend(5)
ll.display()        # 5 -> 10 -> 20 -> 30
ll.delete(20)
ll.display()        # 5 -> 10 -> 30
print(ll.search(10))  # True
