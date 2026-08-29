package ui;

import model.Order;
import service.RestaurantManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * HistoryPanel.java
 * 
 * Displays completed orders history powered by CustomStack<Order> (LIFO).
 * Shows how the most recently completed order is placed on TOP of the stack.
 */
public class HistoryPanel extends JPanel {

    private RestaurantManager manager;
    private JTextArea visualStackArea;
    private DefaultListModel<String> stackListModel;

    public HistoryPanel() {
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
        JLabel title = new JLabel(" Order History : Custom Stack (LIFO)");
        title.setFont(UIUtils.FONT_TITLE);
        title.setForeground(UIUtils.COLOR_TEXT_MAIN);
        headerPanel.add(title, BorderLayout.NORTH);

        JLabel subtitle = new JLabel("Completed orders are pushed onto a Stack. The most recently finished order is always at the TOP.");
        subtitle.setFont(UIUtils.FONT_BODY);
        subtitle.setForeground(UIUtils.COLOR_TEXT_MUTED);
        headerPanel.add(subtitle, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // Center Content Split: Left = Visual ASCII Stack Diagram, Right = Order Details & Viva Explanation
        JPanel mainContent = new JPanel(new GridLayout(1, 2, 20, 0));
        mainContent.setOpaque(false);

        // 1. Left Stack Visual
        JPanel leftCard = UIUtils.createRoundedPanel(20, Color.WHITE);
        leftCard.setLayout(new BorderLayout(12, 12));
        leftCard.setBorder(new EmptyBorder(18, 20, 18, 20));

        JLabel visualTitle = new JLabel("Visual Order History Stack");
        visualTitle.setFont(UIUtils.FONT_HEADER);
        visualTitle.setForeground(UIUtils.COLOR_PRIMARY);
        leftCard.add(visualTitle, BorderLayout.NORTH);

        visualStackArea = new JTextArea();
        visualStackArea.setFont(UIUtils.FONT_MONO);
        visualStackArea.setBackground(new Color(255, 245, 247));
        visualStackArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        visualStackArea.setEditable(false);
        leftCard.add(new JScrollPane(visualStackArea), BorderLayout.CENTER);

        mainContent.add(leftCard);

        // 2. Right List & Why Stack
        JPanel rightCard = UIUtils.createRoundedPanel(20, Color.WHITE);
        rightCard.setLayout(new BorderLayout(12, 12));
        rightCard.setBorder(new EmptyBorder(18, 20, 18, 20));

        JLabel listTitle = new JLabel("Completed Receipts (Top ➔ Bottom)");
        listTitle.setFont(UIUtils.FONT_HEADER);
        listTitle.setForeground(UIUtils.COLOR_TEXT_MAIN);
        rightCard.add(listTitle, BorderLayout.NORTH);

        stackListModel = new DefaultListModel<>();
        JList<String> list = new JList<>(stackListModel);
        list.setFont(UIUtils.FONT_BODY);
        rightCard.add(new JScrollPane(list), BorderLayout.CENTER);

        String whyHistoryStack = "Stack follows LIFO (Last In, First Out).\n\n"
                               + "• In a restaurant, the cashier or customer most frequently inquires about the MOST RECENT receipt.\n"
                               + "• Storing receipts in a Stack allows peek() to retrieve the latest order in O(1) instant time without searching the entire database!";
        JPanel whyCard = UIUtils.createWhyCard("Stack for History", whyHistoryStack);
        rightCard.add(whyCard, BorderLayout.SOUTH);

        mainContent.add(rightCard);
        add(mainContent, BorderLayout.CENTER);
    }

    public void refresh() {
        stackListModel.clear();
        List<Order> list = manager.getCompletedOrdersHistory().toList();

        if (list.isEmpty()) {
            stackListModel.addElement(" No completed orders yet. Cook orders in the Kitchen panel!");
            visualStackArea.setText("TOP ➔ [ Empty Stack ]\n\nNo orders have been completed yet.");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("      ┌───────────────────────────────────────────────┐\n");
            for (int i = 0; i < list.size(); i++) {
                Order o = list.get(i);
                stackListModel.addElement((i == 0 ? "TOP ➔ " : "      ") + o.toString());

                if (i == 0) {
                    sb.append(String.format("TOP ➔ │ Order #%d - %s ($%.2f) │ [Most Recent]\n", o.getOrderId(), o.getCustomerName(), o.getTotalAmount()));
                } else {
                    sb.append(String.format("      │ Order #%d - %s ($%.2f) │\n", o.getOrderId(), o.getCustomerName(), o.getTotalAmount()));
                }
                sb.append("      ├───────────────────────────────────────────────┤\n");
            }
            sb.append("      └───────────────────────────────────────────────┘ [BOTTOM]\n");
            visualStackArea.setText(sb.toString());
        }
    }
}
