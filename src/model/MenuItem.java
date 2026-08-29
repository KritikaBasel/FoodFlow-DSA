package model;

/**
 * MenuItem.java
 * 
 * Represents a food or beverage item on the FoodFlow restaurant menu.
 * Used in:
 * - Menu display
 * - Order creation
 * - Searching (Linear / Binary Search)
 * - Sorting (Bubble Sort by price or name)
 */
public class MenuItem {
    private int id;
    private String name;
    private String category;
    private double price;

    // Constructor
    public MenuItem(int id, String name, String category, double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Friendly string representation for Menu and Order lists
    @Override
    public String toString() {
        return String.format("#%02d %-12s [%-10s] - $%.2f", id, name, category, price);
    }
}
