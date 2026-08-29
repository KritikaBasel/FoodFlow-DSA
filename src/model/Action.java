package model;

/**
 * Action.java
 * 
 * Represents a user operation in the restaurant management system.
 * Stored in CustomStack<Action> to implement the UNDO functionality (LIFO - Last In, First Out).
 * When the user clicks "Undo Last Action", the top action is popped and reversed.
 */
public class Action {
    public enum ActionType {
        ADD_CUSTOMER,
        REMOVE_CUSTOMER,
        ASSIGN_TABLE,
        FREE_TABLE,
        ADD_ORDER,
        PROCESS_ORDER,
        CANCEL_ORDER
    }

    private ActionType type;
    private String description;
    private Object data; // Reference to the object involved (Customer, Order, Table number, etc.)
    private String timestamp;

    // Constructor
    public Action(ActionType type, String description, Object data) {
        this.type = type;
        this.description = description;
        this.data = data;
        this.timestamp = java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    // Getters and Setters
    public ActionType getType() {
        return type;
    }

    public void setType(ActionType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + description;
    }
}
