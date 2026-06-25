public class Deque {

    static class Node {
        int data;
        Node prev;
        Node next;

        Node(int data) {
            this.data = data;
            this.prev = null;
            this.next = null;
        }
    }

    private Node head;   // front
    private Node tail;   // rear
    private int size;

    public Deque() {
        head = null;
        tail = null;
        size = 0;
    }

    // Add to front - O(1)
    public void addFront(int data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    // Add to rear - O(1)
    public void addRear(int data) {
        Node newNode = new Node(data);
        if (tail == null) {
            head = tail = newNode;
        } else {
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    // Remove from front - O(1)
    public int removeFront() {
        if (isEmpty()) throw new RuntimeException("Deque is empty");
        int data = head.data;
        head = head.next;
        if (head != null) {
            head.prev = null;
        } else {
            tail = null;
        }
        size--;
        return data;
    }

    // Remove from rear - O(1)
    public int removeRear() {
        if (isEmpty()) throw new RuntimeException("Deque is empty");
        int data = tail.data;
        tail = tail.prev;
        if (tail != null) {
            tail.next = null;
        } else {
            head = null;
        }
        size--;
        return data;
    }

    // Peek front - O(1)
    public int peekFront() {
        if (isEmpty()) throw new RuntimeException("Deque is empty");
        return head.data;
    }

    // Peek rear - O(1)
    public int peekRear() {
        if (isEmpty()) throw new RuntimeException("Deque is empty");
        return tail.data;
    }

    public boolean isEmpty() { return size == 0; }
    public int size()        { return size; }

    public void display() {
        StringBuilder sb = new StringBuilder("front -> ");
        Node curr = head;
        while (curr != null) {
            sb.append(curr.data);
            if (curr.next != null) sb.append(" <-> ");
            curr = curr.next;
        }
        sb.append(" <- rear");
        System.out.println(sb);
    }

    public static void main(String[] args) {
        Deque dq = new Deque();
        dq.addRear(10);
        dq.addRear(20);
        dq.addRear(30);
        dq.addFront(5);
        dq.display();                       // front -> 5 <-> 10 <-> 20 <-> 30 <- rear
        System.out.println(dq.peekFront()); // 5
        System.out.println(dq.peekRear());  // 30
        System.out.println(dq.removeFront()); // 5
        System.out.println(dq.removeRear());  // 30
        dq.display();                       // front -> 10 <-> 20 <- rear
        System.out.println(dq.size());      // 2
    }
}
