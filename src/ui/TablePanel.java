package ui;

import model.Table;
import service.RestaurantManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * TablePanel.java
 * 
 * Manages dining tables using a fixed-size Java Array (Table[] tables).
 * Shows interactive cards for all tables, allows freeing/assigning tables,
 * and demonstrates O(1) direct index access.
 */
public class TablePanel extends JPanel {

    private RestaurantManager manager;
    private JPanel tablesGridPanel;

    public TablePanel() {
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
        JLabel title = new JLabel(" Restaurant Tables : Array Structure");
        title.setFont(UIUtils.FONT_TITLE);
        title.setForeground(UIUtils.COLOR_TEXT_MAIN);
        headerPanel.add(title, BorderLayout.NORTH);

        JLabel subtitle = new JLabel("Tables are represented in a fixed-size contiguous Array: Table[] tables with O(1) direct index lookup.");
        subtitle.setFont(UIUtils.FONT_BODY);
        subtitle.setForeground(UIUtils.COLOR_TEXT_MUTED);
        headerPanel.add(subtitle, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // Center: Tables Grid & Why Array Card
        JPanel centerPanel = new JPanel(new BorderLayout(20, 20));
        centerPanel.setOpaque(false);

        // Tables 2x3 Grid
        tablesGridPanel = new JPanel(new GridLayout(2, 3, 16, 16));
        tablesGridPanel.setOpaque(false);
        centerPanel.add(tablesGridPanel, BorderLayout.CENTER);

        // Bottom Why Array Card
        String whyArray = "Physical restaurants have a known, fixed number of tables.\n"
                        + "• Direct index access: tables[tableNumber - 1] is instantaneous in O(1) time.\n"
                        + "• Memory is contiguous and structured, making it easy to check availability in a simple loop.";
        JPanel whyCard = UIUtils.createWhyCard("Array", whyArray);
        centerPanel.add(whyCard, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);
    }

    public void refresh() {
        tablesGridPanel.removeAll();
        Table[] tables = manager.getTables();

        for (int i = 0; i < tables.length; i++) {
            Table table = tables[i];
            boolean occupied = table.isOccupied();

            Color cardBg = occupied ? new Color(255, 240, 243) : new Color(240, 253, 244);
            Color borderColor = occupied ? UIUtils.COLOR_PRIMARY : UIUtils.COLOR_MINT_DARK;

            JPanel card = UIUtils.createRoundedPanel(18, cardBg);
            card.setLayout(new BorderLayout(10, 8));
            card.setBorder(new EmptyBorder(14, 16, 14, 16));

            // Top row: Table number & status badge
            JPanel topRow = new JPanel(new BorderLayout());
            topRow.setOpaque(false);

            JLabel lblNum = new JLabel("Table #" + table.getTableNumber());
            lblNum.setFont(UIUtils.FONT_HEADER);
            lblNum.setForeground(UIUtils.COLOR_TEXT_MAIN);

            JLabel lblBadge = new JLabel(occupied ? "● Occupied" : "● Available");
            lblBadge.setFont(UIUtils.FONT_BODY_BOLD);
            lblBadge.setForeground(borderColor);

            topRow.add(lblNum, BorderLayout.WEST);
            topRow.add(lblBadge, BorderLayout.EAST);
            card.add(topRow, BorderLayout.NORTH);

            // Middle info: Capacity and occupant
            JPanel midRow = new JPanel(new GridLayout(2, 1, 0, 4));
            midRow.setOpaque(false);

            JLabel lblCap = new JLabel(" Capacity: " + table.getCapacity() + " Guests");
            lblCap.setFont(UIUtils.FONT_BODY);

            JLabel lblOccupant;
            if (occupied && table.getCurrentCustomer() != null) {
                lblOccupant = new JLabel(table.getCurrentCustomer().getName());
                lblOccupant.setFont(UIUtils.FONT_BODY_BOLD);
                lblOccupant.setForeground(UIUtils.COLOR_TEXT_MAIN);
            } else {
                lblOccupant = new JLabel(" Table is free and clean");
                lblOccupant.setFont(UIUtils.FONT_BODY);
                lblOccupant.setForeground(UIUtils.COLOR_TEXT_MUTED);
            }

            midRow.add(lblCap);
            midRow.add(lblOccupant);
            card.add(midRow, BorderLayout.CENTER);

            // Action button
            int tableNum = table.getTableNumber();
            JButton btnAction;
            if (occupied) {
                btnAction = UIUtils.createRoundedButton("Free Table - Seat Next in Queue", UIUtils.COLOR_PRIMARY, Color.WHITE);
                btnAction.addActionListener(e -> {
                    String msg = manager.freeTable(tableNum);
                    JOptionPane.showMessageDialog(this, msg, "Table Updated", JOptionPane.INFORMATION_MESSAGE);
                    refresh();
                });
            } else {
                btnAction = UIUtils.createRoundedButton("Seat Guest Here", UIUtils.COLOR_MINT_DARK, Color.WHITE);
                btnAction.addActionListener(e -> {
                    if (manager.getWaitingQueue().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "No customers in waiting queue to seat! Add a customer first.", "Queue Empty", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        model.Customer nextCust = manager.getWaitingQueue().peek();
                        String msg = manager.assignTableManually(tableNum, nextCust);
                        JOptionPane.showMessageDialog(this, msg, "Customer Seated", JOptionPane.INFORMATION_MESSAGE);
                        refresh();
                    }
                });
            }
            card.add(btnAction, BorderLayout.SOUTH);

            tablesGridPanel.add(card);
        }

        tablesGridPanel.revalidate();
        tablesGridPanel.repaint();
    }
}
