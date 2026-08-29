package model;

/**
 * Table.java
 * 
 * Represents a dining table inside the restaurant.
 * Used in:
 * - Table Management using a fixed-size Java Array (Table[] tables)
 * - Assigning tables to customers and freeing them up
 */
public class Table {
    private int tableNumber;
    private int capacity;
    private boolean isOccupied;
    private Customer currentCustomer;

    // Constructor
    public Table(int tableNumber, int capacity) {
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.isOccupied = false;
        this.currentCustomer = null;
    }

    // Assign customer to this table
    public void occupy(Customer customer) {
        this.currentCustomer = customer;
        this.isOccupied = true;
        if (customer != null) {
            customer.setAssignedTableNumber(this.tableNumber);
        }
    }

    // Free the table for the next waiting customer
    public Customer freeTable() {
        Customer leavingCustomer = this.currentCustomer;
        if (leavingCustomer != null) {
            leavingCustomer.setAssignedTableNumber(-1);
        }
        this.currentCustomer = null;
        this.isOccupied = false;
        return leavingCustomer;
    }

    // Getters and Setters
    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    @Override
    public String toString() {
        if (isOccupied && currentCustomer != null) {
            return "Table #" + tableNumber + " (Seats " + capacity + ") - Occupied by " + currentCustomer.getName();
        } else {
            return "Table #" + tableNumber + " (Seats " + capacity + ") - Available [Free]";
        }
    }
}
