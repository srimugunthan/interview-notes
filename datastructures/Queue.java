public class Queue {

    private int[] data;
    private int front;
    private int rear;
    private int size;
    private int capacity;

    public Queue(int capacity) {
        this.capacity = capacity;
        this.data = new int[capacity];
        this.front = 0;
        this.rear = -1;
        this.size = 0;
    }

    // Enqueue - O(1)
    public void enqueue(int item) {
        if (size == capacity) {
            throw new RuntimeException("Queue overflow");
        }
        rear = (rear + 1) % capacity;   // circular wrap
        data[rear] = item;
        size++;
    }

    // Dequeue - O(1)
    public int dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Queue underflow");
        }
        int item = data[front];
        front = (front + 1) % capacity; // circular wrap
        size--;
        return item;
    }

    // Peek front - O(1)
    public int front() {
        if (isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }
        return data[front];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void display() {
        System.out.print("Queue (front -> rear): ");
        for (int i = 0; i < size; i++) {
            System.out.print(data[(front + i) % capacity] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Queue q = new Queue(10);
        q.enqueue(10);
        q.enqueue(20);
        q.enqueue(30);
        q.display();                    // Queue (front -> rear): 10 20 30
        System.out.println(q.front());  // 10
        System.out.println(q.dequeue()); // 10
        System.out.println(q.size());   // 2
    }
}
