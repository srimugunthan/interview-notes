public class LinkedList {

    // Node definition
    static class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;

    // Append to end - O(n)
    public void append(int data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = newNode;
            return;
        }
        Node curr = head;
        while (curr.next != null) {
            curr = curr.next;
        }
        curr.next = newNode;
    }

    // Prepend to front - O(1)
    public void prepend(int data) {
        Node newNode = new Node(data);
        newNode.next = head;
        head = newNode;
    }

    // Delete by value - O(n)
    public void delete(int data) {
        if (head == null) return;

        if (head.data == data) {
            head = head.next;
            return;
        }

        Node curr = head;
        while (curr.next != null) {
            if (curr.next.data == data) {
                curr.next = curr.next.next;
                return;
            }
            curr = curr.next;
        }
    }

    // Search - O(n)
    public boolean search(int data) {
        Node curr = head;
        while (curr != null) {
            if (curr.data == data) return true;
            curr = curr.next;
        }
        return false;
    }

    // Display
    public void display() {
        Node curr = head;
        StringBuilder sb = new StringBuilder();
        while (curr != null) {
            sb.append(curr.data);
            if (curr.next != null) sb.append(" -> ");
            curr = curr.next;
        }
        System.out.println(sb);
    }

    public static void main(String[] args) {
        LinkedList ll = new LinkedList();
        ll.append(10);
        ll.append(20);
        ll.append(30);
        ll.prepend(5);
        ll.display();               // 5 -> 10 -> 20 -> 30
        ll.delete(20);
        ll.display();               // 5 -> 10 -> 30
        System.out.println(ll.search(10));  // true
        System.out.println(ll.search(20));  // false
    }
}
