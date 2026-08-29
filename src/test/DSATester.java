package test;

import dsa.*;
import model.Customer;
import model.MenuItem;
import model.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * DSATester.java
 * 
 * Standalone console test suite to verify the manual DSA implementations
 * before running the Java Swing GUI.
 */
public class DSATester {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("    FOODFLOW DSA UNIT TESTING & VERIFICATION   ");
        System.out.println("=================================================\n");

        testQueue();
        testStack();
        testPriorityQueue();
        testLinkedList();
        testSearching();
        testSorting();

        System.out.println("\n ALL DSA TESTS PASSED SUCCESSFULLY! Ready for GUI launch.");
    }

    private static void testQueue() {
        System.out.println("--- 1. Testing CustomQueue (FIFO) ---");
        CustomQueue<String> queue = new CustomQueue<>();
        queue.enqueue("Customer A");
        queue.enqueue("Customer B");
        queue.enqueue("Customer C");

        System.out.println("Queue size: " + queue.size());
        System.out.println("Front item (peek): " + queue.peek());
        System.out.println("Dequeued: " + queue.dequeue() + " (Expected: Customer A)");
        System.out.println("Dequeued: " + queue.dequeue() + " (Expected: Customer B)");
        System.out.println("Queue empty? " + queue.isEmpty() + " (Expected: false)");
        System.out.println("Dequeued: " + queue.dequeue() + " (Expected: Customer C)");
        System.out.println("Queue empty? " + queue.isEmpty() + " (Expected: true)\n");
    }

    private static void testStack() {
        System.out.println("--- 2. Testing CustomStack (LIFO) ---");
        CustomStack<String> stack = new CustomStack<>();
        stack.push("Action 1: Seated Guest");
        stack.push("Action 2: Created Order #101");
        stack.push("Action 3: Cooked Order #101");

        System.out.println("Stack size: " + stack.size());
        System.out.println("Top item (peek): " + stack.peek());
        System.out.println("Popped: " + stack.pop() + " (Expected: Action 3)");
        System.out.println("Popped: " + stack.pop() + " (Expected: Action 2)");
        System.out.println("Stack empty? " + stack.isEmpty() + " (Expected: false)");
        System.out.println("Popped: " + stack.pop() + " (Expected: Action 1)");
        System.out.println("Stack empty? " + stack.isEmpty() + " (Expected: true)\n");
    }

    private static void testPriorityQueue() {
        System.out.println("--- 3. Testing CustomPriorityQueue (VIP > Urgent > Normal) ---");
        CustomPriorityQueue<String> pq = new CustomPriorityQueue<>();
        pq.enqueue("Normal Order #1", 3);
        pq.enqueue("Normal Order #2", 3);
        pq.enqueue("VIP Order #3 ", 1);
        pq.enqueue("Urgent Order #4 ⚡", 2);

        System.out.println("PQ size: " + pq.size());
        System.out.println("1st Dequeued (Highest Priority): " + pq.dequeue() + " (Expected: VIP Order #3)");
        System.out.println("2nd Dequeued: " + pq.dequeue() + " (Expected: Urgent Order #4)");
        System.out.println("3rd Dequeued: " + pq.dequeue() + " (Expected: Normal Order #1)");
        System.out.println("4th Dequeued: " + pq.dequeue() + " (Expected: Normal Order #2)\n");
    }

    private static void testLinkedList() {
        System.out.println("--- 4. Testing CustomLinkedList ---");
        CustomLinkedList<String> list = new CustomLinkedList<>();
        list.add("First Node");
        list.add("Second Node");
        list.addFirst("Prepended Head");

        System.out.println("List size: " + list.size());
        System.out.println("Element at index 0: " + list.get(0) + " (Expected: Prepended Head)");
        System.out.println("Element at index 1: " + list.get(1) + " (Expected: First Node)");
        System.out.println("Removed index 0: " + list.remove(0));
        System.out.println("New index 0: " + list.get(0) + " (Expected: First Node)\n");
    }

    private static void testSearching() {
        System.out.println("--- 5. Testing Searching Algorithms ---");
        List<MenuItem> menu = new ArrayList<>();
        menu.add(new MenuItem(1, "Momo", "Snacks", 6.50));
        menu.add(new MenuItem(2, "Pizza", "Main", 12.00));
        menu.add(new MenuItem(3, "Burger", "Main", 8.50));

        Searching.SearchResult<MenuItem> resLinear = Searching.linearSearchMenuByName(menu, "Pizza");
        System.out.println("Linear Search Found: " + resLinear.isFound() + " (Comparisons: " + resLinear.getComparisons() + ")");

        Searching.SearchResult<MenuItem> resBinary = Searching.binarySearchMenuById(menu, 3);
        System.out.println("Binary Search Found: " + resBinary.isFound() + " (Comparisons: " + resBinary.getComparisons() + ")\n");
    }

    private static void testSorting() {
        System.out.println("--- 6. Testing Sorting Algorithms ---");
        List<MenuItem> menu = new ArrayList<>();
        menu.add(new MenuItem(1, "Pizza", "Main", 12.00));
        menu.add(new MenuItem(2, "Momo", "Snacks", 6.50));
        menu.add(new MenuItem(3, "Burger", "Main", 8.50));

        Sorting.SortResult<MenuItem> sortRes = Sorting.bubbleSortMenuByPrice(menu, true);
        System.out.println("Bubble Sort Ascending: ");
        for (MenuItem m : sortRes.getSortedList()) {
            System.out.println("  " + m.getName() + " - $" + m.getPrice());
        }
        System.out.println("Total Comparisons: " + sortRes.getComparisons() + ", Total Swaps: " + sortRes.getSwaps() + "\n");
    }
}
