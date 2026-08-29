package ui;

import model.Customer;
import model.MenuItem;
import model.Order;
import model.Table;
import service.RestaurantManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * OrderPanel.java
 * 
 * Order taking interface where customers order food items.
 * Demonstrates routing between:
 * - Standard Kitchen Queue (FIFO for Normal orders)
 * - VIP Priority Queue (Priority 1=VIP, 2=Urgent)
 */
public class OrderPanel extends JPanel {

    private RestaurantManager manager;
    private JComboBox<String> comboTables;
    private JComboBox<String> comboPriority;
    private JPanel menuItemsCheckPanel;
    private List<JCheckBox> itemCheckBoxes;
    private JLabel lblCalculatedTotal;
    private DefaultListModel<String> activeOrdersModel;
    private JList<String> listActiveOrders;

    public OrderPanel() {
        this.manager = RestaurantManager.getInstance();
        setLayout(new BorderLayout(20, 20));
        setBackground(UIUtils.COLOR_BG);
        setBorder(new EmptyBorder(20, 25, 20, 25));

        initComponents();
        refresh();
    }

    private void initComponents() {
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel title = new JLabel(" Food Orders : Queue & Priority Routing");
        title.setFont(UIUtils.FONT_TITLE);
        title.setForeground(UIUtils.COLOR_TEXT_MAIN);
        headerPanel.add(title, BorderLayout.NORTH);

        JLabel subtitle = new JLabel("Normal orders go to Kitchen Queue (FIFO). VIP and Urgent orders go to Priority Queue!");
        subtitle.setFont(UIUtils.FONT_BODY);
        subtitle.setForeground(UIUtils.COLOR_TEXT_MUTED);
        headerPanel.add(subtitle, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // Center Split: Left = Create Order, Right = Active Orders List
        JPanel mainContent = new JPanel(new GridLayout(1, 2, 20, 0));
        mainContent.setOpaque(false);

        // 1. Left Create Order Form
        JPanel formCard = UIUtils.createRoundedPanel(20, Color.WHITE);
        formCard.setLayout(new BorderLayout(12, 12));
        formCard.setBorder(new EmptyBorder(18, 20, 18, 20));

        JLabel formTitle = new JLabel(" Create New Order");
        formTitle.setFont(UIUtils.FONT_HEADER);
        formTitle.setForeground(UIUtils.COLOR_PRIMARY);
        formCard.add(formTitle, BorderLayout.NORTH);

        // Form controls
        JPanel controlsPanel = new JPanel(new GridBagLayout());
        controlsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        controlsPanel.add(new JLabel("Select Seated Table:"), gbc);
        gbc.gridx = 1;
        comboTables = new JComboBox<>();
        comboTables.setFont(UIUtils.FONT_BODY);
        controlsPanel.add(comboTables, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        controlsPanel.add(new JLabel("Order Priority:"), gbc);
        gbc.gridx = 1;
        comboPriority = new JComboBox<>(new String[]{
                "Normal (Priority 3  - Kitchen Queue)",
                "Urgent (Priority 2  - Priority Queue)",
                "VIP (Priority 1 - Priority Queue)"
        });
        comboPriority.setFont(UIUtils.FONT_BODY);
        controlsPanel.add(comboPriority, gbc);

        // Menu items selection panel
        menuItemsCheckPanel = new JPanel(new GridLayout(0, 2, 8, 4));
        menuItemsCheckPanel.setOpaque(false);
        itemCheckBoxes = new ArrayList<>();

        JScrollPane menuScroll = new JScrollPane(menuItemsCheckPanel);
        menuScroll.setBorder(BorderFactory.createTitledBorder("Select Menu Items:"));
        menuScroll.setPreferredSize(new Dimension(300, 150));
        menuScroll.getViewport().setBackground(Color.WHITE);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        controlsPanel.add(menuScroll, gbc);

        // Total amount calculation
        lblCalculatedTotal = new JLabel("Total: $0.00");
        lblCalculatedTotal.setFont(UIUtils.FONT_HEADER);
        lblCalculatedTotal.setForeground(UIUtils.COLOR_PRIMARY);
        gbc.gridy = 3;
        controlsPanel.add(lblCalculatedTotal, gbc);

        // Place Order button
        JButton btnPlaceOrder = UIUtils.createRoundedButton("Send Order to Kitchen ", UIUtils.COLOR_PRIMARY, Color.WHITE);
        btnPlaceOrder.addActionListener(e -> onPlaceOrder());
        gbc.gridy = 4;
        controlsPanel.add(btnPlaceOrder, gbc);

        formCard.add(controlsPanel, BorderLayout.CENTER);
        mainContent.add(formCard);

        // 2. Right: Active Orders List & DSA Note
        JPanel rightCard = UIUtils.createRoundedPanel(20, Color.WHITE);
        rightCard.setLayout(new BorderLayout(12, 12));
        rightCard.setBorder(new EmptyBorder(18, 20, 18, 20));

        JLabel rightTitle = new JLabel(" Master Orders List");
        rightTitle.setFont(UIUtils.FONT_HEADER);
        rightTitle.setForeground(UIUtils.COLOR_TEXT_MAIN);
        rightCard.add(rightTitle, BorderLayout.NORTH);

        activeOrdersModel = new DefaultListModel<>();
        listActiveOrders = new JList<>(activeOrdersModel);
        listActiveOrders.setFont(UIUtils.FONT_BODY);
        rightCard.add(new JScrollPane(listActiveOrders), BorderLayout.CENTER);

        // Bottom note
        String whyNote = "When you place a VIP order, it skips ahead of normal orders in the Kitchen priority queue.";
        JPanel noteCard = UIUtils.createWhyCard("Order Priority", whyNote);
        rightCard.add(noteCard, BorderLayout.SOUTH);

        mainContent.add(rightCard);
        add(mainContent, BorderLayout.CENTER);
    }

    private void onPlaceOrder() {
        if (comboTables.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "No occupied tables found! Please seat a customer at a table first.", "No Seated Tables", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String tableItem = (String) comboTables.getSelectedItem();
        int tableNum = Integer.parseInt(tableItem.split(" ")[1]);
        Table table = manager.getTables()[tableNum - 1];
        String custName = (table.getCurrentCustomer() != null) ? table.getCurrentCustomer().getName() : "Guest";

        List<MenuItem> selectedItems = new ArrayList<>();
        List<MenuItem> menu = manager.getMenu();
        for (int i = 0; i < itemCheckBoxes.size(); i++) {
            if (itemCheckBoxes.get(i).isSelected()) {
                selectedItems.add(menu.get(i));
            }
        }

        if (selectedItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one menu item!", "Selection Empty", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int priorityIndex = comboPriority.getSelectedIndex();
        int priority = (priorityIndex == 0) ? Order.PRIORITY_NORMAL : (priorityIndex == 1) ? Order.PRIORITY_URGENT : Order.PRIORITY_VIP;

        Order newOrder = manager.createOrder(custName, tableNum, selectedItems, priority);
        JOptionPane.showMessageDialog(this, " Order #" + newOrder.getOrderId() + " placed! Sent to: " +
                (priority == Order.PRIORITY_NORMAL ? "Normal Kitchen Queue (FIFO)" : "VIP Priority Queue"), 
                "Order Created", JOptionPane.INFORMATION_MESSAGE);

        // Reset check boxes
        for (JCheckBox cb : itemCheckBoxes) {
            cb.setSelected(false);
        }
        lblCalculatedTotal.setText("Total: $0.00");
        refresh();
    }

    private void recalculateTotal() {
        double total = 0;
        List<MenuItem> menu = manager.getMenu();
        for (int i = 0; i < itemCheckBoxes.size() && i < menu.size(); i++) {
            if (itemCheckBoxes.get(i).isSelected()) {
                total += menu.get(i).getPrice();
            }
        }
        lblCalculatedTotal.setText(String.format("Total: $%.2f", total));
    }

    public void refresh() {
        // Refresh occupied tables combo
        comboTables.removeAllItems();
        Table[] tables = manager.getTables();
        for (Table table : tables) {
            if (table.isOccupied()) {
                String name = (table.getCurrentCustomer() != null) ? table.getCurrentCustomer().getName() : "Customer";
                comboTables.addItem("Table " + table.getTableNumber() + " (" + name + ")");
            }
        }

        // Refresh menu checkboxes
        menuItemsCheckPanel.removeAll();
        itemCheckBoxes.clear();
        List<MenuItem> menu = manager.getMenu();
        for (MenuItem item : menu) {
            JCheckBox cb = new JCheckBox(item.getName() + " ($" + item.getPrice() + ")");
            cb.setFont(UIUtils.FONT_BODY);
            cb.setOpaque(false);
            cb.addActionListener(e -> recalculateTotal());
            itemCheckBoxes.add(cb);
            menuItemsCheckPanel.add(cb);
        }
        menuItemsCheckPanel.revalidate();
        menuItemsCheckPanel.repaint();

        // Refresh master orders list
        activeOrdersModel.clear();
        List<Order> orders = manager.getAllOrders();
        for (Order o : orders) {
            activeOrdersModel.addElement(o.toString());
        }
    }
}
