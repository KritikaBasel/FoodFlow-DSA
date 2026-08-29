package ui;

import service.RestaurantManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * DashboardPanel.java
 * 
 * Home dashboard displaying welcome greeting, restaurant real-time stats cards,
 * quick action buttons, and a "Today's DSA" mapping summary.
 */
public class DashboardPanel extends JPanel {

    private RestaurantManager manager;
    private JLabel lblAvailTables;
    private JLabel lblWaitCust;
    private JLabel lblPendingOrders;
    private JLabel lblVipOrders;

    public DashboardPanel() {
        this.manager = RestaurantManager.getInstance();
        setLayout(new BorderLayout(20, 20));
        setBackground(UIUtils.COLOR_BG);
        setBorder(new EmptyBorder(25, 30, 25, 30));

        initComponents();
        refresh();
    }

    private void initComponents() {
        // Top Welcome Banner
        JPanel welcomePanel = UIUtils.createRoundedPanel(20, Color.WHITE);
        welcomePanel.setLayout(new BorderLayout(15, 10));
        welcomePanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel lblTitle = new JLabel("Welcome to FoodFlow! ");
        lblTitle.setFont(UIUtils.FONT_TITLE);
        lblTitle.setForeground(UIUtils.COLOR_PRIMARY);

        JLabel lblSubtitle = new JLabel("Your cute university restaurant management system powered by Data Structures & Algorithms.");
        lblSubtitle.setFont(UIUtils.FONT_BODY);
        lblSubtitle.setForeground(UIUtils.COLOR_TEXT_MUTED);

        welcomePanel.add(lblTitle, BorderLayout.NORTH);
        welcomePanel.add(lblSubtitle, BorderLayout.CENTER);

        add(welcomePanel, BorderLayout.NORTH);

        // Center Content: Stats Cards + DSA Overview
        JPanel centerPanel = new JPanel(new BorderLayout(20, 20));
        centerPanel.setOpaque(false);

        // 4 Stats Cards Grid
        JPanel statsGrid = new JPanel(new GridLayout(1, 4, 15, 0));
        statsGrid.setOpaque(false);

        lblAvailTables = new JLabel("0");
        lblWaitCust = new JLabel("0");
        lblPendingOrders = new JLabel("0");
        lblVipOrders = new JLabel("0");

        statsGrid.add(createCard("🪑", "Available Tables", lblAvailTables, UIUtils.COLOR_MINT_DARK));
        statsGrid.add(createCard("👥", "Waiting Customers", lblWaitCust, UIUtils.COLOR_PRIMARY));
        statsGrid.add(createCard("📋", "Pending Orders", lblPendingOrders, UIUtils.COLOR_SECONDARY));
        statsGrid.add(createCard("⭐", "VIP Orders", lblVipOrders, new Color(225, 112, 85)));

        centerPanel.add(statsGrid, BorderLayout.NORTH);

        // Lower Grid: "Today's DSA in Action" & "Quick Restaurant Summary"
        JPanel lowerGrid = new JPanel(new GridLayout(1, 2, 20, 0));
        lowerGrid.setOpaque(false);

        // DSA Card
        JPanel dsaCard = UIUtils.createRoundedPanel(20, Color.WHITE);
        dsaCard.setLayout(new BorderLayout(12, 12));
        dsaCard.setBorder(new EmptyBorder(18, 20, 18, 20));

        JLabel dsaHeader = new JLabel(" Today's DSA in Action");
        dsaHeader.setFont(UIUtils.FONT_HEADER);
        dsaHeader.setForeground(UIUtils.COLOR_TEXT_MAIN);
        dsaCard.add(dsaHeader, BorderLayout.NORTH);

        String dsaText = "• Queue (FIFO) - Waiting Customers & Kitchen Orders\n"
                       + "• Stack (LIFO) - Undo Actions & Order History\n"
                       + "• Priority Queue - VIP & Urgent Orders (1 > 2 > 3)\n"
                       + "• Array (O(1)) - Restaurant Tables (Fixed capacity)\n"
                       + "• Searching - Linear & Binary Search for Orders/Menu\n"
                       + "• Sorting - Bubble & Selection Sort for Menu Prices\n"
                       + "• Linked List - Dynamic Node Chaining for Queues/Stacks";

        JTextArea dsaArea = new JTextArea(dsaText);
        dsaArea.setFont(UIUtils.FONT_BODY);
        dsaArea.setForeground(UIUtils.COLOR_TEXT_MAIN);
        dsaArea.setLineWrap(true);
        dsaArea.setWrapStyleWord(true);
        dsaArea.setEditable(false);
        dsaArea.setOpaque(false);
        dsaCard.add(dsaArea, BorderLayout.CENTER);

        // Why DSA Card
        JPanel vivaCard = UIUtils.createRoundedPanel(20, new Color(255, 240, 245));
        vivaCard.setLayout(new BorderLayout(12, 12));
        vivaCard.setBorder(new EmptyBorder(18, 20, 18, 20));

        JLabel vivaHeader = new JLabel(" Academic Viva Highlights");
        vivaHeader.setFont(UIUtils.FONT_HEADER);
        vivaHeader.setForeground(UIUtils.COLOR_PRIMARY);
        vivaCard.add(vivaHeader, BorderLayout.NORTH);

        String vivaText = "This system runs on custom-coded DSA implementations (No built-in Java Collections for queues/stacks)!\n\n"
                        + " Custom Node-based Queue for First-Come First-Served tables.\n"
                        + " Custom Priority Queue to guarantee VIP customers are cooked first.\n"
                        + " Custom Stack allowing single-click rollback of the latest action.";

        JTextArea vivaArea = new JTextArea(vivaText);
        vivaArea.setFont(UIUtils.FONT_BODY);
        vivaArea.setForeground(UIUtils.COLOR_TEXT_MAIN);
        vivaArea.setLineWrap(true);
        vivaArea.setWrapStyleWord(true);
        vivaArea.setEditable(false);
        vivaArea.setOpaque(false);
        vivaCard.add(vivaArea, BorderLayout.CENTER);

        lowerGrid.add(dsaCard);
        lowerGrid.add(vivaCard);

        centerPanel.add(lowerGrid, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createCard(String emoji, String title, JLabel valueLabel, Color accentColor) {
        JPanel card = UIUtils.createRoundedPanel(18, Color.WHITE);
        card.setLayout(new BorderLayout(10, 5));
        card.setBorder(new EmptyBorder(15, 18, 15, 18));

        JLabel lblEmoji = new JLabel(emoji);
        lblEmoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        textPanel.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(UIUtils.FONT_SMALL);
        lblTitle.setForeground(UIUtils.COLOR_TEXT_MUTED);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(accentColor);

        textPanel.add(lblTitle);
        textPanel.add(valueLabel);

        card.add(lblEmoji, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        return card;
    }

    public void refresh() {
        lblAvailTables.setText(String.valueOf(manager.getAvailableTablesCount()));
        lblWaitCust.setText(String.valueOf(manager.getWaitingQueueSize()));
        lblPendingOrders.setText(String.valueOf(manager.getPendingOrdersCount()));
        lblVipOrders.setText(String.valueOf(manager.getVipOrdersCount()));
    }
}
