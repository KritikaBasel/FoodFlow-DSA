package ui;

import model.Order;
import service.RestaurantManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * KitchenPanel.java
 * 
 * Simulates the restaurant kitchen powered by:
 * 1. CustomPriorityQueue<Order> (VIP Orders - Priority 1 & 2)
 * 2. CustomQueue<Order> (Standard Orders - FIFO)
 * 
 * Demonstrates how the chef prioritizes VIP/Urgent orders before FIFO orders!
 */
public class KitchenPanel extends JPanel {

    private RestaurantManager manager;
    private DefaultListModel<String> vipQueueModel;
    private DefaultListModel<String> normalQueueModel;
    private JTextArea currentCookingArea;

    public KitchenPanel() {
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
        JLabel title = new JLabel(" Chef's Kitchen : Priority Queue & Standard Queue");
        title.setFont(UIUtils.FONT_TITLE);
        title.setForeground(UIUtils.COLOR_TEXT_MAIN);
        headerPanel.add(title, BorderLayout.NORTH);

        JLabel subtitle = new JLabel("The chef processes all VIP Priority Orders FIRST before cooking standard FIFO orders.");
        subtitle.setFont(UIUtils.FONT_BODY);
        subtitle.setForeground(UIUtils.COLOR_TEXT_MUTED);
        headerPanel.add(subtitle, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // Center Content: Top = Currently Cooking & Action Button, Bottom = Two Queues
        JPanel centerPanel = new JPanel(new BorderLayout(20, 20));
        centerPanel.setOpaque(false);

        // Top Cooking Action Card
        JPanel actionCard = UIUtils.createRoundedPanel(20, Color.WHITE);
        actionCard.setLayout(new BorderLayout(15, 10));
        actionCard.setBorder(new EmptyBorder(16, 20, 16, 20));

        JPanel actionHeader = new JPanel(new BorderLayout());
        actionHeader.setOpaque(false);
        JLabel lblCooking = new JLabel(" Kitchen Status:");
        lblCooking.setFont(UIUtils.FONT_HEADER);
        lblCooking.setForeground(UIUtils.COLOR_TEXT_MAIN);
        actionHeader.add(lblCooking, BorderLayout.WEST);

        JButton btnCook = UIUtils.createRoundedButton(" Process & Complete Next Order", UIUtils.COLOR_PRIMARY, Color.WHITE);
        btnCook.setFont(UIUtils.FONT_BODY_BOLD);
        btnCook.addActionListener(e -> onCookNextOrder());
        actionHeader.add(btnCook, BorderLayout.EAST);

        actionCard.add(actionHeader, BorderLayout.NORTH);

        currentCookingArea = new JTextArea(2, 40);
        currentCookingArea.setFont(UIUtils.FONT_BODY_BOLD);
        currentCookingArea.setBackground(new Color(255, 245, 247));
        currentCookingArea.setBorder(new EmptyBorder(8, 10, 8, 10));
        currentCookingArea.setEditable(false);
        currentCookingArea.setText("Ready for next order...");
        actionCard.add(currentCookingArea, BorderLayout.CENTER);

        centerPanel.add(actionCard, BorderLayout.NORTH);

        // Bottom Split: VIP Priority Queue vs Normal FIFO Queue
        JPanel queuesGrid = new JPanel(new GridLayout(1, 2, 20, 0));
        queuesGrid.setOpaque(false);

        // VIP Priority Queue Card
        JPanel vipCard = UIUtils.createRoundedPanel(20, Color.WHITE);
        vipCard.setLayout(new BorderLayout(10, 10));
        vipCard.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel vipHeader = new JLabel(" VIP Priority Queue (Priority 1 & 2)");
        vipHeader.setFont(UIUtils.FONT_HEADER);
        vipHeader.setForeground(new Color(214, 48, 49));
        vipCard.add(vipHeader, BorderLayout.NORTH);

        vipQueueModel = new DefaultListModel<>();
        JList<String> vipList = new JList<>(vipQueueModel);
        vipList.setFont(UIUtils.FONT_BODY);
        vipCard.add(new JScrollPane(vipList), BorderLayout.CENTER);

        String whyPQ = "VIP and Urgent orders leapfrog standard orders so important guests are served quickly!";
        vipCard.add(UIUtils.createWhyCard("Priority Queue", whyPQ), BorderLayout.SOUTH);
        queuesGrid.add(vipCard);

        // Normal Kitchen Queue Card
        JPanel normalCard = UIUtils.createRoundedPanel(20, Color.WHITE);
        normalCard.setLayout(new BorderLayout(10, 10));
        normalCard.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel normalHeader = new JLabel(" Standard Kitchen Queue (FIFO)");
        normalHeader.setFont(UIUtils.FONT_HEADER);
        normalHeader.setForeground(UIUtils.COLOR_SECONDARY);
        normalCard.add(normalHeader, BorderLayout.NORTH);

        normalQueueModel = new DefaultListModel<>();
        JList<String> normalList = new JList<>(normalQueueModel);
        normalList.setFont(UIUtils.FONT_BODY);
        normalCard.add(new JScrollPane(normalList), BorderLayout.CENTER);

        String whyKitchenQ = "Standard orders are cooked in FIFO arrival order to ensure fairness and no food is left behind.";
        normalCard.add(UIUtils.createWhyCard("Kitchen Queue", whyKitchenQ), BorderLayout.SOUTH);
        queuesGrid.add(normalCard);

        centerPanel.add(queuesGrid, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void onCookNextOrder() {
        Order completed = manager.processNextKitchenOrder();
        if (completed == null) {
            JOptionPane.showMessageDialog(this, "No pending orders in either queue to cook!", "Kitchen Clear", JOptionPane.INFORMATION_MESSAGE);
            currentCookingArea.setText("Kitchen is currently idle (All orders completed).");
            return;
        }

        String sourceQueue = (completed.getPriority() == Order.PRIORITY_VIP || completed.getPriority() == Order.PRIORITY_URGENT)
                ? "VIP Priority Queue (Priority " + completed.getPriority() + ")"
                : "Standard Kitchen Queue (FIFO)";

        currentCookingArea.setText(" Completed Order #" + completed.getOrderId() + " for " + completed.getCustomerName() +
                " [Table #" + completed.getTableNumber() + "] from " + sourceQueue + 
                "\n Pushed onto Completed Orders Stack (LIFO)!");

        JOptionPane.showMessageDialog(this, "🍳 Order #" + completed.getOrderId() + " is ready and served!\nMoved to Order History Stack.", "Order Completed", JOptionPane.INFORMATION_MESSAGE);
        refresh();
    }

    public void refresh() {
        // Refresh VIP Priority Queue
        vipQueueModel.clear();
        List<Order> vipList = manager.getVipPriorityQueue().toList();
        if (vipList.isEmpty()) {
            vipQueueModel.addElement(" No VIP orders waiting.");
        } else {
            for (int i = 0; i < vipList.size(); i++) {
                Order o = vipList.get(i);
                vipQueueModel.addElement((i + 1) + ". Order #" + o.getOrderId() + " (" + o.getCustomerName() + ") - [" + o.getPriorityLabel() + "] Total: $" + o.getTotalAmount());
            }
        }

        // Refresh Normal Kitchen Queue
        normalQueueModel.clear();
        List<Order> normalList = manager.getKitchenQueue().toList();
        if (normalList.isEmpty()) {
            normalQueueModel.addElement(" No normal orders in queue.");
        } else {
            for (int i = 0; i < normalList.size(); i++) {
                Order o = normalList.get(i);
                normalQueueModel.addElement((i + 1) + ". Order #" + o.getOrderId() + " (" + o.getCustomerName() + ") - Table #" + o.getTableNumber() + " - Total: $" + o.getTotalAmount());
            }
        }
    }
}
