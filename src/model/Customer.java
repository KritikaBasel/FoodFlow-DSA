package model;

/**
 * Customer.java
 * 
 * Represents a customer in the FoodFlow restaurant system.
 * Used primarily in:
 * - CustomQueue (Waiting Queue when tables are full)
 * - Table assignment (Array of Tables)
 */
public class Customer {
    private int id;
    private String name;
    private int numberOfPeople;
    private String phoneNumber;
    private boolean isVIP;
    private int assignedTableNumber; // -1 if waiting in queue, > 0 if seated at a table

    // Constructor
    public Customer(int id, String name, int numberOfPeople, String phoneNumber, boolean isVIP) {
        this.id = id;
        this.name = name;
        this.numberOfPeople = numberOfPeople;
        this.phoneNumber = phoneNumber;
        this.isVIP = isVIP;
        this.assignedTableNumber = -1; // Default: not assigned to any table yet
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isVIP() {
        return isVIP;
    }

    public void setVIP(boolean VIP) {
        isVIP = VIP;
    }

    public int getAssignedTableNumber() {
        return assignedTableNumber;
    }

    public void setAssignedTableNumber(int assignedTableNumber) {
        this.assignedTableNumber = assignedTableNumber;
    }

    // Friendly string display for Console and UI lists
    @Override
    public String toString() {
        String vipBadge = isVIP ? " [VIP *]" : "";
        String tableStatus = (assignedTableNumber != -1) ? " (Table #" + assignedTableNumber + ")" : " (Waiting)";
        return name + vipBadge + " (" + numberOfPeople + " Guests) - " + phoneNumber + tableStatus;
    }
}
