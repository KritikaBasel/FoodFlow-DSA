package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Order.java
 * 
 * Represents a food order placed by a customer.
 * Used in:
 * - Kitchen Orders (CustomQueue - FIFO)
 * - VIP Orders (CustomPriorityQueue - Priority 1=VIP, 2=Urgent, 3=Normal)
 * - Order History (CustomStack - LIFO)
 * - Searching and Sorting demonstrations
 */
public class Order {
    // Priority constants
    public static final int PRIORITY_VIP = 1;
    public static final int PRIORITY_URGENT = 2;
    public static final int PRIORITY_NORMAL = 3;

    private int orderId;
    private String customerName;
    private int tableNumber;
    private List<MenuItem> items;
    private int priority; // 1 = VIP, 2 = Urgent, 3 = Normal
    private String status; // "Pending", "Preparing", "Completed", "Cancelled"
    private double totalAmount;
    private String timestamp;

    // Constructor
    public Order(int orderId, String customerName, int tableNumber, int priority) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.tableNumber = tableNumber;
        this.priority = priority;
        this.items = new ArrayList<>();
        this.status = "Pending";
        this.totalAmount = 0.0;
        this.timestamp = java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    // Add item to this order
    public void addItem(MenuItem item) {
        if (item != null) {
            this.items.add(item);
            this.totalAmount += item.getPrice();
        }
    }

    // Recalculate total amount from items
    public double calculateTotal() {
        double sum = 0.0;
        for (MenuItem item : items) {
            sum += item.getPrice();
        }
        this.totalAmount = sum;
        return sum;
    }

    // Get a friendly text representation of priority
    public String getPriorityLabel() {
        switch (priority) {
            case PRIORITY_VIP:
                return "VIP (High *)";
            case PRIORITY_URGENT:
                return "Urgent (Medium -)";
            case PRIORITY_NORMAL:
            default:
                return "Normal (Standard #)";
        }
    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public void setItems(List<MenuItem> items) {
        this.items = items;
        calculateTotal();
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return String.format("Order #%d | Table %d | %s | [%s] | Total: $%.2f (%s)",
                orderId, tableNumber, customerName, getPriorityLabel(), totalAmount, status);
    }
}
