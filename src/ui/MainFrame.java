package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * MainFrame.java
 * 
 * The main top-level window for the FoodFlow application.
 * Features a cute, pastel pink navigation sidebar on the left
 * and a dynamic card-layout content view on the right.
 */
public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentContainer;
    private Map<String, JButton> navButtons;

    // Panels
    private DashboardPanel dashboardPanel;
    private CustomerPanel customerPanel;
    private TablePanel tablePanel;
    private OrderPanel orderPanel;
    private KitchenPanel kitchenPanel;
    private HistoryPanel historyPanel;
    private UndoPanel undoPanel;
    private MenuPanel menuPanel;
    private SearchPanel searchPanel;
    private SortPanel sortPanel;
    private LearnDSAPanel learnDSAPanel;

    public MainFrame() {
        super(" FoodFlow — Restaurant Management System (DSA)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 760);
        setMinimumSize(new Dimension(1050, 680));
        setLocationRelativeTo(null); // Center on screen

        initUI();
    }

    private void initUI() {
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(UIUtils.COLOR_BG);

        // 1. Left Sidebar
        JPanel sidebar = createSidebar();
        rootPanel.add(sidebar, BorderLayout.WEST);

        // 2. Right Dynamic Content Container
        cardLayout = new CardLayout();
        contentContainer = new JPanel(cardLayout);
        contentContainer.setOpaque(false);

        // Instantiate panels
        dashboardPanel = new DashboardPanel();
        customerPanel = new CustomerPanel();
        tablePanel = new TablePanel();
        orderPanel = new OrderPanel();
        kitchenPanel = new KitchenPanel();
        historyPanel = new HistoryPanel();
        undoPanel = new UndoPanel();
        menuPanel = new MenuPanel();
        searchPanel = new SearchPanel();
        sortPanel = new SortPanel();
        learnDSAPanel = new LearnDSAPanel();

        // Add to card container
        contentContainer.add(dashboardPanel, "Dashboard");
        contentContainer.add(customerPanel, "Customers");
        contentContainer.add(tablePanel, "Tables");
        contentContainer.add(orderPanel, "Orders");
        contentContainer.add(kitchenPanel, "Kitchen");
        contentContainer.add(historyPanel, "History");
        contentContainer.add(undoPanel, "Undo");
        contentContainer.add(menuPanel, "Menu");
        contentContainer.add(searchPanel, "Search");
        contentContainer.add(sortPanel, "Sort");
        contentContainer.add(learnDSAPanel, "Learn DSA");

        rootPanel.add(contentContainer, BorderLayout.CENTER);
        setContentPane(rootPanel);

        // Start on Dashboard
        switchView("Dashboard");
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UIUtils.COLOR_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(230, 760));
        sidebar.setBorder(new EmptyBorder(22, 16, 22, 16));

        // Brand Logo & Title
        JLabel brandLogo = new JLabel(" FoodFlow");
        brandLogo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        brandLogo.setForeground(UIUtils.COLOR_PRIMARY);
        brandLogo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(brandLogo);

        JLabel brandSub = new JLabel("Restaurant DSA System");
        brandSub.setFont(UIUtils.FONT_SMALL);
        brandSub.setForeground(UIUtils.COLOR_TEXT_MUTED);
        brandSub.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(brandSub);

        sidebar.add(Box.createVerticalStrut(20));

        // Nav items definition
        String[][] navItems = {
                {"Dashboard", "Dashboard"},
                {" Customers", "Customers"},
                {" Tables", "Tables"},
                {" Orders", "Orders"},
                {" Kitchen", "Kitchen"},
                {" History", "History"},
                {"Undo", "Undo"},
                {" Menu", "Menu"},
                {" Search", "Search"},
                {" Sort", "Sort"},
                {" Learn DSA", "Learn DSA"}
        };

        navButtons = new HashMap<>();

        for (String[] item : navItems) {
            String label = item[0];
            String key = item[1];

            JButton btn = new JButton(label);
            btn.setFont(UIUtils.FONT_BODY_BOLD);
            btn.setForeground(UIUtils.COLOR_TEXT_MAIN);
            btn.setBackground(UIUtils.COLOR_SIDEBAR);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setMaximumSize(new Dimension(200, 38));
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);

            btn.addActionListener(e -> switchView(key));

            navButtons.put(key, btn);
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(3));
        }

        sidebar.add(Box.createVerticalGlue());

        // Footer Tag
        JLabel lblFooter = new JLabel(" University DSA Project");
        lblFooter.setFont(UIUtils.FONT_SMALL);
        lblFooter.setForeground(UIUtils.COLOR_TEXT_MUTED);
        lblFooter.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(lblFooter);

        return sidebar;
    }

    public void switchView(String key) {
        cardLayout.show(contentContainer, key);

        // Highlight active navigation button
        for (Map.Entry<String, JButton> entry : navButtons.entrySet()) {
            if (entry.getKey().equals(key)) {
                entry.getValue().setForeground(UIUtils.COLOR_PRIMARY);
                entry.getValue().setFont(new Font("Segoe UI", Font.BOLD, 14));
            } else {
                entry.getValue().setForeground(UIUtils.COLOR_TEXT_MAIN);
                entry.getValue().setFont(UIUtils.FONT_BODY);
            }
        }

        // Refresh dynamic data
        if ("Dashboard".equals(key)) dashboardPanel.refresh();
        else if ("Customers".equals(key)) customerPanel.refresh();
        else if ("Tables".equals(key)) tablePanel.refresh();
        else if ("Orders".equals(key)) orderPanel.refresh();
        else if ("Kitchen".equals(key)) kitchenPanel.refresh();
        else if ("History".equals(key)) historyPanel.refresh();
        else if ("Undo".equals(key)) undoPanel.refresh();
        else if ("Menu".equals(key)) menuPanel.refresh();
    }
}
