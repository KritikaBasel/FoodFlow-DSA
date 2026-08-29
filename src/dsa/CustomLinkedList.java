package dsa;

import java.util.ArrayList;
import java.util.List;

/**
 * CustomLinkedList.java
 * 
 * A custom Singly Linked List implementation where each element is stored as a Node.
 * Each node stores the data and a reference (pointer) to the next node.
 * 
 * Used in FoodFlow for:
 * - Dynamic list management (building custom Queue, Stack, and lists)
 * - Educational demonstration of node-based pointers vs contiguous arrays.
 * 
 * Time Complexities:
 * - addFirst(): O(1)
 * - addLast():  O(n) or O(1) with tail pointer
 * - get(index): O(n)
 * - remove():   O(n)
 */
public class CustomLinkedList<T> {

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

    private Node<T> head;
    private int size;

    public CustomLinkedList() {
        this.head = null;
        this.size = 0;
    }

    /**
     * Appends an element to the end of the list.
     */
    public void add(T item) {
        if (item == null) return;
        Node<T> newNode = new Node<>(item);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    /**
     * Inserts an element at the beginning (head).
     * Time Complexity: O(1)
     */
    public void addFirst(T item) {
        if (item == null) return;
        Node<T> newNode = new Node<>(item);
        newNode.next = head;
        head = newNode;
        size++;
    }

    /**
     * Retrieves an item at a specific 0-based index.
     * Time Complexity: O(n)
     */
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    /**
     * Removes an item by index.
     */
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (index == 0) {
            T data = head.data;
            head = head.next;
            size--;
            return data;
        }
        Node<T> current = head;
        for (int i = 0; i < index - 1; i++) {
            current = current.next;
        }
        T data = current.next.data;
        current.next = current.next.next;
        size--;
        return data;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public int size() {
        return size;
    }

    public List<T> toList() {
        List<T> list = new ArrayList<>();
        Node<T> current = head;
        while (current != null) {
            list.add(current.data);
            current = current.next;
        }
        return list;
    }

    public String getVisualRepresentation() {
        if (isEmpty()) return "[HEAD] - NULL";
        StringBuilder sb = new StringBuilder("[HEAD] - ");
        Node<T> current = head;
        while (current != null) {
            sb.append("[").append(current.data.toString()).append("] - ");
            current = current.next;
        }
        sb.append("NULL");
        return sb.toString();
    }
}
