package dsa;

import java.util.ArrayList;
import java.util.List;

/**
 * CustomStack.java
 * 
 * A custom generic Stack implementation using a singly linked Node structure.
 * Follows LIFO (Last In, First Out) principle.
 * 
 * Used in FoodFlow for:
 * 1. Undo System (The most recent action is reversed first)
 * 2. Completed Order History (The most recently finished order is at the top)
 * 
 * Time Complexities:
 * - push(): O(1)
 * - pop():  O(1)
 * - peek(): O(1)
 * - size(): O(1)
 * - isEmpty(): O(1)
 */
public class CustomStack<T> {

    // Node class representing each element in the stack
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

    private Node<T> top; // Points to the top element of the stack
    private int size;    // Tracks number of elements in O(1)

    // Constructor: initialize empty stack
    public CustomStack() {
        this.top = null;
        this.size = 0;
    }

    /**
     * Pushes a new item onto the top of the stack.
     * Time Complexity: O(1)
     */
    public void push(T item) {
        if (item == null) return;
        Node<T> newNode = new Node<>(item);
        newNode.next = top;
        top = newNode;
        size++;
    }

    /**
     * Removes and returns the item from the top of the stack.
     * Time Complexity: O(1)
     */
    public T pop() {
        if (isEmpty()) {
            return null;
        }
        T data = top.data;
        top = top.next;
        size--;
        return data;
    }

    /**
     * Returns the item at the top of the stack without removing it.
     * Time Complexity: O(1)
     */
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return top.data;
    }

    /**
     * Checks if the stack has no elements.
     * Time Complexity: O(1)
     */
    public boolean isEmpty() {
        return top == null;
    }

    /**
     * Returns the total number of elements in the stack.
     * Time Complexity: O(1)
     */
    public int size() {
        return size;
    }

    /**
     * Converts stack elements from top to bottom into a standard List for UI rendering.
     */
    public List<T> toList() {
        List<T> list = new ArrayList<>();
        Node<T> current = top;
        while (current != null) {
            list.add(current.data);
            current = current.next;
        }
        return list;
    }

    /**
     * Returns an ASCII diagram of the stack state.
     * Example:
     * TOP -> [Order #103]
     *        [Order #102]
     *        [Order #101]
     */
    public String getVisualRepresentation() {
        if (isEmpty()) {
            return "Stack is empty (Top = null)";
        }
        StringBuilder sb = new StringBuilder();
        Node<T> current = top;
        boolean isFirst = true;
        while (current != null) {
            if (isFirst) {
                sb.append("TOP ➔ | ").append(current.data.toString()).append(" |\n");
                isFirst = false;
            } else {
                sb.append("      | ").append(current.data.toString()).append(" |\n");
            }
            sb.append("      +--------------------------------------+\n");
            current = current.next;
        }
        return sb.toString();
    }
}
