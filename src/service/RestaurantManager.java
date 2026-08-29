package service;

import dsa.CustomPriorityQueue;
import dsa.CustomQueue;
import dsa.CustomStack;
import model.Action;
import model.Customer;
import model.MenuItem;
import model.Order;
import model.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * RestaurantManager.java
 * 
 * Central business logic and state manager for the FoodFlow restaurant.
 * Directly integrates all DSA structures:
 * - Table[] tables                   ➔ Array (Fixed-size tables with O(1) index access)
 * - CustomQueue<Customer>            ➔ Queue (FIFO Waiting List)
 * - CustomQueue<Order>               ➔ Queue (FIFO Normal Kitchen Orders)
 * - CustomPriorityQueue<Order>       ➔ Priority Queue (VIP=1, Urgent=2, Normal=3)
 * - CustomStack<Action>              ➔ Stack (LIFO Undo History)
 * - CustomStack<Order>               ➔ Stack (LIFO Completed Orders History)
 */
public class RestaurantManager {

    // Singleton instance for easy shared access across UI panels
    private static RestaurantManager instance;

    // DSA Data Structures
    private Table[] tables;                                // Array
    private CustomQueue<Customer> waitingQueue;            // Queue (FIFO)
    private CustomQueue<Order> kitchenQueue;              // Queue (FIFO)
    private CustomPriorityQueue<Order> vipPriorityQueue;  // Priority Queue
    private CustomStack<Action> undoStack;                // Stack (LIFO Undo)
    private CustomStack<Order> completedOrdersHistory;    // Stack (LIFO History)

    // Master lists
    private List<MenuItem> menu;
    private List<Customer> allCustomers;
    private List<Order> allOrders;

    private int nextCustomerId = 1;
    private int nextOrderId = 101;
    private int nextMenuId = 1;

    public static synchronized RestaurantManager getInstance() {
        if (instance == null) {
            instance = new RestaurantManager();
        }
        return instance;
    }

    public RestaurantManager() {
        // Initialize DSA structures
        this.tables = new Table[6]; // 6 fixed restaurant tables
        this.waitingQueue = new CustomQueue<>();
        this.kitchenQueue = new CustomQueue<>();
        this.vipPriorityQueue = new CustomPriorityQueue<>();
        this.undoStack = new CustomStack<>();
        this.completedOrdersHistory = new CustomStack<>();

        this.menu = new ArrayList<>();
        this.allCustomers = new ArrayList<>();
        this.allOrders = new ArrayList<>();

        initializeDefaultData();
    }

    /**
     * Seeds initial restaurant state with cute menu items and initial tables.
     */
    private void initializeDefaultData() {
        // 1. Initialize Tables (Array)
        tables[0] = new Table(1, 2);
        tables[1] = new Table(2, 2);
        tables[2] = new Table(3, 4);
        tables[3] = new Table(4, 4);
        tables[4] = new Table(5, 6);
        tables[5] = new Table(6, 8);

        // 2. Initialize Menu Items
        addMenuItem("Momo", "Snacks", 6.50);
        addMenuItem("Margherita Pizza", "Main Course", 12.00);
        addMenuItem("Cheesy Burger", "Main Course", 8.50);
        addMenuItem("Creamy Pasta", "Main Course", 10.00);
        addMenuItem("Veg Chowmein", "Snacks", 7.00);
        addMenuItem("Fried Rice", "Main Course", 7.50);
        addMenuItem("Iced Latte", "Beverages", 4.50);
        addMenuItem("Strawberry Cake", "Dessert", 5.50);
        addMenuItem("Boba Milk Tea", "Beverages", 5.00);
        addMenuItem("French Fries", "Snacks", 4.00);

        // 3. Seed some initial seated customers
        Customer c1 = new Customer(nextCustomerId++, "Aarav Sharma", 2, "9841000001", false);
        allCustomers.add(c1);
        tables[0].occupy(c1);

        Customer c2 = new Customer(nextCustomerId++, "Priya Adhikari", 4, "9841000002", true);
        allCustomers.add(c2);
        tables[2].occupy(c2);

        // 4. Seed initial waiting queue customers
        Customer c3 = new Customer(nextCustomerId++, "Kritika Basel", 2, "9841000003", false);
        Customer c4 = new Customer(nextCustomerId++, "Barsha Thapa", 3, "9841000004", true);
        Customer c5 = new Customer(nextCustomerId++, "Sita Shrestha", 2, "9841000005", false);
        allCustomers.add(c3);
        allCustomers.add(c4);
        allCustomers.add(c5);
        waitingQueue.enqueue(c3);
        waitingQueue.enqueue(c4);
        waitingQueue.enqueue(c5);

        // 5. Seed initial kitchen orders
        Order o1 = new Order(nextOrderId++, "Aarav Sharma", 1, Order.PRIORITY_NORMAL);
        o1.addItem(menu.get(0)); // Momo
        o1.addItem(menu.get(6)); // Iced Latte
        kitchenQueue.enqueue(o1);
        allOrders.add(o1);

        Order o2 = new Order(nextOrderId++, "Priya Adhikari", 3, Order.PRIORITY_VIP);
        o2.addItem(menu.get(1)); // Pizza
        o2.addItem(menu.get(7)); // Strawberry Cake
        vipPriorityQueue.enqueue(o2, Order.PRIORITY_VIP);
        allOrders.add(o2);
    }

    // ==========================================
    // CUSTOMER & TABLE LOGIC (Queue & Array)
    // ==========================================

    /**
     * Adds a customer. If a suitable table is free, seats them.
     * Otherwise, enqueues them to the waiting queue (FIFO).
     */
    public String addCustomer(String name, int numberOfPeople, String phoneNumber, boolean isVIP) {
        Customer customer = new Customer(nextCustomerId++, name, numberOfPeople, phoneNumber, isVIP);
        allCustomers.add(customer);

        // Check if a suitable table is available in the Array
        Table availableTable = findAvailableTable(numberOfPeople);

        if (availableTable != null) {
            availableTable.occupy(customer);
            undoStack.push(new Action(Action.ActionType.ASSIGN_TABLE, 
                    "Seated " + customer.getName() + " at Table #" + availableTable.getTableNumber(), 
                    new Object[]{availableTable.getTableNumber(), customer}));
            return  customer.getName() + " was seated at Table #" + availableTable.getTableNumber() + "!";
        } else {
            // Enqueue into Waiting Queue (FIFO)
            waitingQueue.enqueue(customer);
            undoStack.push(new Action(Action.ActionType.ADD_CUSTOMER, 
                    "Added " + customer.getName() + " to Waiting Queue", 
                    customer));
            return " Tables full! " + customer.getName() + " entered the Waiting Queue at position #" + waitingQueue.size() + ".";
        }
    }

    /**
     * Finds the best fitting available table in the Array.
     * Time Complexity: O(n) where n = number of tables.
     */
    public Table findAvailableTable(int peopleCount) {
        Table bestTable = null;
        for (Table table : tables) {
            if (!table.isOccupied() && table.getCapacity() >= peopleCount) {
                if (bestTable == null || table.getCapacity() < bestTable.getCapacity()) {
                    bestTable = table;
                }
            }
        }
        return bestTable;
    }

    /**
     * Frees a table and automatically serves the next waiting customer from the Queue if one is waiting!
     */
    public String freeTable(int tableNumber) {
        if (tableNumber < 1 || tableNumber > tables.length) {
            return "Invalid table number!";
        }
        Table table = tables[tableNumber - 1];
        if (!table.isOccupied()) {
            return "Table #" + tableNumber + " is already free!";
        }

        Customer departingCustomer = table.freeTable();
        undoStack.push(new Action(Action.ActionType.FREE_TABLE, 
                "Freed Table #" + tableNumber + " (was occupied by " + (departingCustomer != null ? departingCustomer.getName() : "Unknown") + ")", 
                new Object[]{tableNumber, departingCustomer}));

        // Check Waiting Queue (FIFO)
        if (!waitingQueue.isEmpty()) {
            Customer nextCustomer = waitingQueue.dequeue();
            table.occupy(nextCustomer);
            return "Table #" + tableNumber + " freed! Next customer from Queue (" + nextCustomer.getName() + ") has been seated at Table #" + tableNumber + " (FIFO).";
        }

        return " Table #" + tableNumber + " is now clean and available.";
    }

    /**
     * Manually assigns a customer from the queue to a specific table.
     */
    public String assignTableManually(int tableNumber, Customer customer) {
        if (tableNumber < 1 || tableNumber > tables.length) return "Invalid table number.";
        Table table = tables[tableNumber - 1];
        if (table.isOccupied()) return "Table #" + tableNumber + " is already occupied!";

        waitingQueue.remove(customer);
        table.occupy(customer);

        undoStack.push(new Action(Action.ActionType.ASSIGN_TABLE, 
                "Manually seated " + customer.getName() + " at Table #" + tableNumber, 
                new Object[]{tableNumber, customer}));
        return  customer.getName() + " is now seated at Table #" + tableNumber + ".";
    }

    // ==========================================
    // ORDER LOGIC (Queue, Priority Queue, Stack)
    // ==========================================

    /**
     * Creates a new order and enqueues into Kitchen Queue or VIP Priority Queue.
     */
    public Order createOrder(String customerName, int tableNumber, List<MenuItem> items, int priority) {
        Order order = new Order(nextOrderId++, customerName, tableNumber, priority);
        for (MenuItem item : items) {
            order.addItem(item);
        }
        allOrders.add(order);

        if (priority == Order.PRIORITY_VIP || priority == Order.PRIORITY_URGENT) {
            vipPriorityQueue.enqueue(order, priority);
        } else {
            kitchenQueue.enqueue(order);
        }

        undoStack.push(new Action(Action.ActionType.ADD_ORDER, 
                "Created Order #" + order.getOrderId() + " (" + order.getPriorityLabel() + ")", 
                order));
        return order;
    }

    /**
     * Processes the next order in the kitchen.
     * VIP Orders (Priority Queue) are ALWAYS cooked before normal FIFO kitchen orders!
     */
    public Order processNextKitchenOrder() {
        Order cookedOrder = null;

        // 1. Check VIP Priority Queue first
        if (!vipPriorityQueue.isEmpty()) {
            cookedOrder = vipPriorityQueue.dequeue();
        } 
        // 2. Otherwise process standard Kitchen Queue (FIFO)
        else if (!kitchenQueue.isEmpty()) {
            cookedOrder = kitchenQueue.dequeue();
        }

        if (cookedOrder != null) {
            cookedOrder.setStatus("Completed");
            // Push onto Completed Orders Stack (LIFO)
            completedOrdersHistory.push(cookedOrder);

            undoStack.push(new Action(Action.ActionType.PROCESS_ORDER, 
                    "Completed Order #" + cookedOrder.getOrderId() + " for " + cookedOrder.getCustomerName(), 
                    cookedOrder));
        }

        return cookedOrder;
    }

    // ==========================================
    // UNDO FUNCTIONALITY (Stack - LIFO)
    // ==========================================

    /**
     * Undoes the most recent user action by popping from the Undo Stack.
     */
    public String undoLastAction() {
        if (undoStack.isEmpty()) {
            return "No recent actions to undo!";
        }

        Action action = undoStack.pop();
        switch (action.getType()) {
            case ADD_CUSTOMER:
                Customer addedCustomer = (Customer) action.getData();
                waitingQueue.remove(addedCustomer);
                allCustomers.remove(addedCustomer);
                return "↩ Undone: Removed " + addedCustomer.getName() + " from Waiting Queue.";

            case ASSIGN_TABLE:
                Object[] tableData = (Object[]) action.getData();
                int tNum = (int) tableData[0];
                Customer seatedCustomer = (Customer) tableData[1];
                if (tNum >= 1 && tNum <= tables.length) {
                    tables[tNum - 1].freeTable();
                }
                // Return customer to waiting queue if appropriate
                waitingQueue.enqueue(seatedCustomer);
                return " Undone: Seated action for " + seatedCustomer.getName() + " cancelled; returned to Queue.";

            case FREE_TABLE:
                Object[] freeData = (Object[]) action.getData();
                int freedTNum = (int) freeData[0];
                Customer prevCust = (Customer) freeData[1];
                if (freedTNum >= 1 && freedTNum <= tables.length && prevCust != null) {
                    tables[freedTNum - 1].occupy(prevCust);
                }
                return "↩ Undone: Restored " + (prevCust != null ? prevCust.getName() : "Customer") + " to Table #" + freedTNum + ".";

            case ADD_ORDER:
                Order addedOrder = (Order) action.getData();
                if (addedOrder.getPriority() == Order.PRIORITY_VIP || addedOrder.getPriority() == Order.PRIORITY_URGENT) {
                    vipPriorityQueue.remove(addedOrder);
                } else {
                    kitchenQueue.remove(addedOrder);
                }
                allOrders.remove(addedOrder);
                return " Undone: Cancelled Order #" + addedOrder.getOrderId() + ".";

            case PROCESS_ORDER:
                Order processedOrder = (Order) action.getData();
                completedOrdersHistory.pop(); // Remove from history stack
                processedOrder.setStatus("Pending");
                if (processedOrder.getPriority() == Order.PRIORITY_VIP || processedOrder.getPriority() == Order.PRIORITY_URGENT) {
                    vipPriorityQueue.enqueue(processedOrder, processedOrder.getPriority());
                } else {
                    kitchenQueue.enqueue(processedOrder);
                }
                return "↩ Undone: Returned Order #" + processedOrder.getOrderId() + " back to Kitchen Queue.";

            default:
                return "↩ Undone action: " + action.getDescription();
        }
    }

    // ==========================================
    // MENU OPERATIONS
    // ==========================================

    public MenuItem addMenuItem(String name, String category, double price) {
        MenuItem item = new MenuItem(nextMenuId++, name, category, price);
        menu.add(item);
        return item;
    }

    public boolean removeMenuItem(MenuItem item) {
        return menu.remove(item);
    }

    // ==========================================
    // STATS & GETTERS
    // ==========================================

    public int getAvailableTablesCount() {
        int count = 0;
        for (Table table : tables) {
            if (!table.isOccupied()) count++;
        }
        return count;
    }

    public int getOccupiedTablesCount() {
        return tables.length - getAvailableTablesCount();
    }

    public int getWaitingQueueSize() {
        return waitingQueue.size();
    }

    public int getPendingOrdersCount() {
        return kitchenQueue.size() + vipPriorityQueue.size();
    }

    public int getVipOrdersCount() {
        return vipPriorityQueue.size();
    }

    public Table[] getTables() { return tables; }
    public CustomQueue<Customer> getWaitingQueue() { return waitingQueue; }
    public CustomQueue<Order> getKitchenQueue() { return kitchenQueue; }
    public CustomPriorityQueue<Order> getVipPriorityQueue() { return vipPriorityQueue; }
    public CustomStack<Action> getUndoStack() { return undoStack; }
    public CustomStack<Order> getCompletedOrdersHistory() { return completedOrdersHistory; }
    public List<MenuItem> getMenu() { return menu; }
    public List<Customer> getAllCustomers() { return allCustomers; }
    public List<Order> getAllOrders() { return allOrders; }
}
