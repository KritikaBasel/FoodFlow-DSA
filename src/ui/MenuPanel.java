package ui;

import dsa.Sorting;
import model.MenuItem;
import service.RestaurantManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * MenuPanel.java
 * 
 * Displays and manages the restaurant menu catalog.
 * Allows adding items, removing items, and sorting dishes using Bubble & Selection Sort.
 */
public class MenuPanel extends JPanel {

    private RestaurantManager manager;
    private JTable menuTable;
    private DefaultTableModel tableModel;
    private JTextField txtName;
    private JComboBox<String> comboCategory;
    private JTextField txtPrice;

    public MenuPanel() {
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
        JLabel title = new JLabel(" FoodFlow Menu Catalog");
        title.setFont(UIUtils.FONT_TITLE);
        title.setForeground(UIUtils.COLOR_TEXT_MAIN);
        headerPanel.add(title, BorderLayout.NORTH);

        JLabel subtitle = new JLabel("Browse, add, or sort restaurant menu dishes by price and name.");
        subtitle.setFont(UIUtils.FONT_BODY);
        subtitle.setForeground(UIUtils.COLOR_TEXT_MUTED);
        headerPanel.add(subtitle, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // Center Content Split: Left = Menu Table & Sorting Toolbar, Right = Add Item Card
        JPanel mainContent = new JPanel(new BorderLayout(20, 20));
        mainContent.setOpaque(false);

        // Left Menu Table Panel
        JPanel leftCard = UIUtils.createRoundedPanel(20, Color.WHITE);
        leftCard.setLayout(new BorderLayout(12, 12));
        leftCard.setBorder(new EmptyBorder(18, 20, 18, 20));

        // Quick Sorting Toolbar
        JPanel sortToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        sortToolbar.setOpaque(false);
        sortToolbar.add(new JLabel(" Quick Sort:"));

        JButton btnSortPriceAsc = UIUtils.createRoundedButton("Price (Low - High)", UIUtils.COLOR_TAG_BG, UIUtils.COLOR_PRIMARY);
        btnSortPriceAsc.addActionListener(e -> onSort(true));
        sortToolbar.add(btnSortPriceAsc);

        JButton btnSortPriceDesc = UIUtils.createRoundedButton("Price (High - Low)", UIUtils.COLOR_TAG_BG, UIUtils.COLOR_PRIMARY);
        btnSortPriceDesc.addActionListener(e -> onSort(false));
        sortToolbar.add(btnSortPriceDesc);

        JButton btnSortName = UIUtils.createRoundedButton("Name (A - Z)", UIUtils.COLOR_TAG_BG, UIUtils.COLOR_PRIMARY);
        btnSortName.addActionListener(e -> onSortName());
        sortToolbar.add(btnSortName);

        leftCard.add(sortToolbar, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Item Name", "Category", "Price ($)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        menuTable = new JTable(tableModel);
        menuTable.setFont(UIUtils.FONT_BODY);
        menuTable.setRowHeight(28);
        menuTable.getTableHeader().setFont(UIUtils.FONT_BODY_BOLD);
        leftCard.add(new JScrollPane(menuTable), BorderLayout.CENTER);

        mainContent.add(leftCard, BorderLayout.CENTER);

        // Right Add Item Form
        JPanel rightCard = UIUtils.createRoundedPanel(20, Color.WHITE);
        rightCard.setLayout(new GridBagLayout());
        rightCard.setBorder(new EmptyBorder(20, 20, 20, 20));
        rightCard.setPreferredSize(new Dimension(280, 400));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel formHeader = new JLabel(" Add Menu Item");
        formHeader.setFont(UIUtils.FONT_HEADER);
        formHeader.setForeground(UIUtils.COLOR_PRIMARY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        rightCard.add(formHeader, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0;
        rightCard.add(new JLabel("Item Name:"), gbc);
        gbc.gridx = 1;
        txtName = new JTextField(10);
        rightCard.add(txtName, gbc);

        gbc.gridy = 2; gbc.gridx = 0;
        rightCard.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        comboCategory = new JComboBox<>(new String[]{"Snacks", "Main Course", "Beverages", "Dessert"});
        rightCard.add(comboCategory, gbc);

        gbc.gridy = 3; gbc.gridx = 0;
        rightCard.add(new JLabel("Price ($):"), gbc);
        gbc.gridx = 1;
        txtPrice = new JTextField(10);
        rightCard.add(txtPrice, gbc);

        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2;
        JButton btnAdd = UIUtils.createRoundedButton("Add to Menu", UIUtils.COLOR_PRIMARY, Color.WHITE);
        btnAdd.addActionListener(e -> onAddItem());
        rightCard.add(btnAdd, gbc);

        JButton btnDelete = UIUtils.createRoundedButton("Delete Selected Item", new Color(255, 235, 238), UIUtils.COLOR_PRIMARY);
        btnDelete.addActionListener(e -> onDeleteItem());
        gbc.gridy = 5;
        rightCard.add(btnDelete, gbc);

        mainContent.add(rightCard, BorderLayout.EAST);
        add(mainContent, BorderLayout.CENTER);
    }

    private void onAddItem() {
        String name = txtName.getText().trim();
        String cat = (String) comboCategory.getSelectedItem();
        String priceStr = txtPrice.getText().trim();

        if (name.isEmpty() || priceStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter item name and price!", "Input Missing", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            manager.addMenuItem(name, cat, price);
            txtName.setText("");
            txtPrice.setText("");
            JOptionPane.showMessageDialog(this, "Item \"" + name + "\" added to menu!", "Item Added", JOptionPane.INFORMATION_MESSAGE);
            refresh();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric price (e.g., 7.50)", "Invalid Price", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onDeleteItem() {
        int selectedRow = menuTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item in the table to delete.", "Select Item", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        MenuItem target = null;
        for (MenuItem item : manager.getMenu()) {
            if (item.getId() == id) {
                target = item;
                break;
            }
        }
        if (target != null) {
            manager.removeMenuItem(target);
            JOptionPane.showMessageDialog(this, "Deleted \"" + target.getName() + "\" from menu.", "Item Deleted", JOptionPane.INFORMATION_MESSAGE);
            refresh();
        }
    }

    private void onSort(boolean ascending) {
        Sorting.SortResult<MenuItem> res = Sorting.bubbleSortMenuByPrice(manager.getMenu(), ascending);
        populateTable(res.getSortedList());
        JOptionPane.showMessageDialog(this, String.format("Bubble Sort Complete!\nComparisons: %d | Swaps: %d", res.getComparisons(), res.getSwaps()), "Sorted by Price", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onSortName() {
        Sorting.SortResult<MenuItem> res = Sorting.bubbleSortMenuByName(manager.getMenu());
        populateTable(res.getSortedList());
        JOptionPane.showMessageDialog(this, String.format("Bubble Sort Complete!\nComparisons: %d | Swaps: %d", res.getComparisons(), res.getSwaps()), "Sorted by Name", JOptionPane.INFORMATION_MESSAGE);
    }

    private void populateTable(List<MenuItem> items) {
        tableModel.setRowCount(0);
        for (MenuItem m : items) {
            tableModel.addRow(new Object[]{m.getId(), m.getName(), m.getCategory(), String.format("%.2f", m.getPrice())});
        }
    }

    public void refresh() {
        populateTable(manager.getMenu());
    }
}
