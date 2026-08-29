package dsa;

import java.util.ArrayList;
import java.util.List;

/**
 * CustomQueue.java
 * 
 * A custom generic Queue implementation using a singly linked Node structure.
 * Follows FIFO (First In, First Out) principle.
 * 
 * Used in FoodFlow for:
 * 1. Customer Waiting List (Customers are served in arrival order)
 * 2. Kitchen Orders (Orders are cooked in the order placed)
 * 
 * Time Complexities:
 * - enqueue(): O(1)
 * - dequeue(): O(1)
 * - peek():    O(1)
 * - size():    O(1)
 * - isEmpty(): O(1)
 */
public class CustomQueue<T> {

    // Node class representing each element in the queue
    public static class Node<T> {
        private T data;
        private Node<T> next;

        public Node(T data) {
            this.data = data;
            this.next = null;
        }

        public T getData() {
            return data;
        }

        public Node<T> getNext() {
            return next;
        }
    }

    private Node<T> front; // Points to the first element (to be removed first)
    private Node<T> rear;  // Points to the last element (where new elements enter)
    private int size;      // Tracks number of elements in O(1)

    // Constructor: initialize empty queue
    public CustomQueue() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    /**
     * Adds an item to the end (rear) of the queue.
     * Time Complexity: O(1)
     */
    public void enqueue(T item) {
        if (item == null) return;
        
        Node<T> newNode = new Node<>(item);
        if (isEmpty()) {
            front = newNode;
            rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    /**
     * Removes and returns the item at the front of the queue.
     * Time Complexity: O(1)
     */
    public T dequeue() {
        if (isEmpty()) {
            return null;
        }
        T data = front.data;
        front = front.next;
        if (front == null) {
            rear = null;
        }
        size--;
        return data;
    }

    /**
     * Returns the item at the front without removing it.
     * Time Complexity: O(1)
     */
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return front.data;
    }

    /**
     * Checks if the queue has no elements.
     * Time Complexity: O(1)
     */
    public boolean isEmpty() {
        return front == null;
    }

    /**
     * Returns total number of items in queue.
     * Time Complexity: O(1)
     */
    public int size() {
        return size;
    }

    /**
     * Converts queue elements into a standard List for easy UI rendering.
     */
    public List<T> toList() {
        List<T> list = new ArrayList<>();
        Node<T> current = front;
        while (current != null) {
            list.add(current.data);
            current = current.next;
        }
        return list;
    }

    /**
     * Removes a specific item from the queue (used if a waiting customer leaves).
     * Time Complexity: O(n)
     */
    public boolean remove(T item) {
        if (isEmpty() || item == null) return false;

        if (front.data.equals(item)) {
            dequeue();
            return true;
        }

        Node<T> current = front;
        while (current.next != null) {
            if (current.next.data.equals(item)) {
                if (current.next == rear) {
                    rear = current;
                }
                current.next = current.next.next;
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**
     * Returns an ASCII diagram of the queue state.
     * Example: Front -> [A] -> [B] -> [C] -> Rear
     */
    public String getVisualRepresentation() {
        if (isEmpty()) {
            return "Queue is empty (Front = null, Rear = null)";
        }
        StringBuilder sb = new StringBuilder("Front ↓\n");
        Node<T> current = front;
        while (current != null) {
            sb.append("[").append(current.data.toString()).append("]");
            if (current.next != null) {
                sb.append(" ➔ ");
            }
            current = current.next;
        }
        sb.append("\n  ↑ Rear");
        return sb.toString();
    }
}
