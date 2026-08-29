package dsa;

import java.util.ArrayList;
import java.util.List;

/**
 * CustomPriorityQueue.java
 * 
 * A custom Priority Queue implementation using a sorted linked list.
 * Items with higher priority (lower numerical value, e.g., 1 = VIP) are 
 * placed closer to the front and served before normal items.
 * 
 * Used in FoodFlow for:
 * - VIP & Urgent Kitchen Orders:
 *     VIP (Priority 1) > Urgent (Priority 2) > Normal (Priority 3)
 * 
 * Time Complexities:
 * - enqueue(item, priority): O(n) in worst case (maintains sorted priority order)
 * - dequeue():               O(1) (removes front highest priority item)
 * - peek():                  O(1)
 * - size():                  O(1)
 * - isEmpty():               O(1)
 */
public class CustomPriorityQueue<T> {

    // Node class with priority value
    public static class PriorityNode<T> {
        private T data;
        private int priority; // Lower integer = Higher Priority (1: VIP, 2: Urgent, 3: Normal)
        private PriorityNode<T> next;

        public PriorityNode(T data, int priority) {
            this.data = data;
            this.priority = priority;
            this.next = null;
        }

        public T getData() {
            return data;
        }

        public int getPriority() {
            return priority;
        }

        public PriorityNode<T> getNext() {
            return next;
        }
    }

    private PriorityNode<T> head; // Points to highest priority element
    private int size;

    public CustomPriorityQueue() {
        this.head = null;
        this.size = 0;
    }

    /**
     * Inserts an item based on its priority (1 = VIP, 2 = Urgent, 3 = Normal).
     * Maintains FIFO for equal priorities.
     * Time Complexity: O(n)
     */
    public void enqueue(T item, int priority) {
        if (item == null) return;
        PriorityNode<T> newNode = new PriorityNode<>(item, priority);

        // If list is empty or new item has strictly higher priority than current head
        if (head == null || priority < head.priority) {
            newNode.next = head;
            head = newNode;
        } else {
            // Traverse to find the insertion point (<= allows FIFO among equal priorities)
            PriorityNode<T> current = head;
            while (current.next != null && current.next.priority <= priority) {
                current = current.next;
            }
            newNode.next = current.next;
            current.next = newNode;
        }
        size++;
    }

    /**
     * Removes and returns the highest priority item.
     * Time Complexity: O(1)
     */
    public T dequeue() {
        if (isEmpty()) {
            return null;
        }
        T data = head.data;
        head = head.next;
        size--;
        return data;
    }

    /**
     * Returns the highest priority item without removing it.
     * Time Complexity: O(1)
     */
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return head.data;
    }

    /**
     * Returns the priority value of the front item.
     */
    public int peekPriority() {
        if (isEmpty()) {
            return -1;
        }
        return head.priority;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public int size() {
        return size;
    }

    /**
     * Returns all items in priority order.
     */
    public List<T> toList() {
        List<T> list = new ArrayList<>();
        PriorityNode<T> current = head;
        while (current != null) {
            list.add(current.data);
            current = current.next;
        }
        return list;
    }

    /**
     * Removes a specific item from the priority queue.
     */
    public boolean remove(T item) {
        if (isEmpty() || item == null) return false;

        if (head.data.equals(item)) {
            dequeue();
            return true;
        }

        PriorityNode<T> current = head;
        while (current.next != null) {
            if (current.next.data.equals(item)) {
                current.next = current.next.next;
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public String getVisualRepresentation() {
        if (isEmpty()) {
            return "Priority Queue is empty";
        }
        StringBuilder sb = new StringBuilder("Priority Front (Highest) ➔\n");
        PriorityNode<T> current = head;
        while (current != null) {
            String priorityTag = (current.priority == 1) ? "[VIP *]" : (current.priority == 2) ? "[Urgent ⚡]" : "[Normal 🍔]";
            sb.append(priorityTag).append(" ").append(current.data.toString());
            if (current.next != null) {
                sb.append("\n          ↓\n");
            }
            current = current.next;
        }
        return sb.toString();
    }
}
