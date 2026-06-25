public class Stack {

    private int[] data;
    private int top;
    private int capacity;

    public Stack(int capacity) {
        this.capacity = capacity;
        this.data = new int[capacity];
        this.top = -1;
    }

    // Push - O(1)
    public void push(int item) {
        if (top == capacity - 1) {
            throw new RuntimeException("Stack overflow");
        }
        data[++top] = item;
    }

    // Pop - O(1)
    public int pop() {
        if (isEmpty()) {
            throw new RuntimeException("Stack underflow");
        }
        return data[top--];
    }

    // Peek - O(1)
    public int peek() {
        if (isEmpty()) {
            throw new RuntimeException("Stack is empty");
        }
        return data[top];
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public int size() {
        return top + 1;
    }

    public void display() {
        System.out.print("Stack (top -> bottom): ");
        for (int i = top; i >= 0; i--) {
            System.out.print(data[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Stack s = new Stack(10);
        s.push(10);
        s.push(20);
        s.push(30);
        s.display();                // Stack (top -> bottom): 30 20 10
        System.out.println(s.peek());   // 30
        System.out.println(s.pop());    // 30
        System.out.println(s.size());   // 2
    }
}
