import java.util.ArrayList;

public class ArrayDemo {
    public static void main(String[] args) {

        // Static array
        int[] arr = {10, 20, 30, 40, 50};

        // Access - O(1)
        System.out.println(arr[2]);         // 30

        // Update - O(1)
        arr[2] = 35;

        // Traverse - O(n)
        for (int x : arr) {
            System.out.print(x + " ");      // 10 20 35 40 50
        }

        // Dynamic array using ArrayList
        ArrayList<Integer> list = new ArrayList<>();

        list.add(10);           // append - O(1) amortized
        list.add(20);
        list.add(30);
        list.add(1, 15);        // insert at index - O(n)
        list.remove(Integer.valueOf(20));    // remove by value - O(n)
        list.remove(0);                     // remove by index - O(n)

        System.out.println(list.contains(30));  // true
        System.out.println(list);               // [15, 30]
    }
}
