package ui;

import dsa.Searching;
import model.Customer;
import model.MenuItem;
import model.Order;
import service.RestaurantManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * SearchPanel.java
 * 
 * Interactive DSA Searching Playground.
 * Demonstrates:
 * 1. Linear Search - Checks elements sequentially O(n).
 * 2. Binary Search - Divide & Conquer on sorted elements O(log n).
 * 
 * Displays live step-by-step traces of every comparison made!
 */
public class SearchPanel extends JPanel {

    private RestaurantManager manager;
    private JComboBox<String> comboAlgorithm;
    private JComboBox<String> comboSearchType;
    private JTextField txtQuery;
    private JTextArea traceArea;
    private JLabel lblStats;

    public SearchPanel() {
        this.manager = RestaurantManager.getInstance();
        setLayout(new BorderLayout(20, 20));
        setBackground(UIUtils.COLOR_BG);
        setBorder(new EmptyBorder(20, 25, 20, 25));

        initComponents();
    }

    private void initComponents() {
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel title = new JLabel(" Searching Algorithms Playground");
        title.setFont(UIUtils.FONT_TITLE);
        title.setForeground(UIUtils.COLOR_TEXT_MAIN);
        headerPanel.add(title, BorderLayout.NORTH);

        JLabel subtitle = new JLabel("Compare Linear Search O(n) and Binary Search O(log n) with live step-by-step comparison traces.");
        subtitle.setFont(UIUtils.FONT_BODY);
        subtitle.setForeground(UIUtils.COLOR_TEXT_MUTED);
        headerPanel.add(subtitle, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // Center Content Split: Top Controls & Stats, Bottom Trace & Viva Notes
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setOpaque(false);

        // Controls Card
        JPanel controlsCard = UIUtils.createRoundedPanel(20, Color.WHITE);
        controlsCard.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 12));
        controlsCard.setBorder(new EmptyBorder(10, 15, 10, 15));

        controlsCard.add(new JLabel("Algorithm:"));
        comboAlgorithm = new JComboBox<>(new String[]{"Linear Search (O(n))", "Binary Search (O(log n) - Sorted)"});
        comboAlgorithm.setFont(UIUtils.FONT_BODY);
        controlsCard.add(comboAlgorithm);

        controlsCard.add(new JLabel("Target:"));
        comboSearchType = new JComboBox<>(new String[]{"Menu Item by Name", "Menu Item by ID", "Order by Order ID", "Customer by Name"});
        comboSearchType.setFont(UIUtils.FONT_BODY);
        controlsCard.add(comboSearchType);

        controlsCard.add(new JLabel("Query:"));
        txtQuery = new JTextField(10);
        txtQuery.setFont(UIUtils.FONT_BODY);
        controlsCard.add(txtQuery);

        JButton btnSearch = UIUtils.createRoundedButton("Execute Search ", UIUtils.COLOR_PRIMARY, Color.WHITE);
        btnSearch.addActionListener(e -> executeSearch());
        controlsCard.add(btnSearch);

        centerPanel.add(controlsCard, BorderLayout.NORTH);

        // Split Area: Left = Trace Logs, Right = Viva Complexity Notes
        JPanel splitPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        splitPanel.setOpaque(false);

        // Trace Logs Card
        JPanel traceCard = UIUtils.createRoundedPanel(20, Color.WHITE);
        traceCard.setLayout(new BorderLayout(12, 12));
        traceCard.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel traceHeader = new JLabel("Execution Step-by-Step Trace");
        traceHeader.setFont(UIUtils.FONT_HEADER);
        traceHeader.setForeground(UIUtils.COLOR_PRIMARY);
        traceCard.add(traceHeader, BorderLayout.NORTH);

        traceArea = new JTextArea();
        traceArea.setFont(UIUtils.FONT_MONO);
        traceArea.setBackground(new Color(255, 245, 247));
        traceArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        traceArea.setEditable(false);
        traceArea.setText("Select search criteria and click \"Execute Search\" to see step-by-step comparison trace.");
        traceCard.add(new JScrollPane(traceArea), BorderLayout.CENTER);

        lblStats = new JLabel("Status: Idle");
        lblStats.setFont(UIUtils.FONT_BODY_BOLD);
        lblStats.setForeground(UIUtils.COLOR_TEXT_MAIN);
        traceCard.add(lblStats, BorderLayout.SOUTH);

        splitPanel.add(traceCard);

        // Right Viva Cards
        JPanel vivaCard = UIUtils.createRoundedPanel(20, Color.WHITE);
        vivaCard.setLayout(new GridLayout(2, 1, 10, 10));
        vivaCard.setBorder(new EmptyBorder(16, 18, 16, 18));

        String whyLinear = "• LINEAR SEARCH:\n"
                         + "Checks elements one by one sequentially.\n"
                         + "Works on any unsorted data.\n"
                         + "Best Case: O(1) [First item matches]\n"
                         + "Average / Worst Case: O(n) [Traverses entire list]";
        vivaCard.add(UIUtils.createWhyCard("Linear Search", whyLinear));

        String whyBinary = "• BINARY SEARCH:\n"
                         + "Requires data to be SORTED in advance.\n"
                         + "Repeatedly divides the search space in half.\n"
                         + "Best Case: O(1) [Middle item matches]\n"
                         + "Average / Worst Case: O(log n) [Extremely fast for large datasets!]";
        vivaCard.add(UIUtils.createWhyCard("Binary Search", whyBinary));

        splitPanel.add(vivaCard);
        centerPanel.add(splitPanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void executeSearch() {
        String query = txtQuery.getText().trim();
        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search query!", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int algoIdx = comboAlgorithm.getSelectedIndex();
        int targetIdx = comboSearchType.getSelectedIndex();

        StringBuilder sb = new StringBuilder();

        if (algoIdx == 0) {
            // Linear Search
            if (targetIdx == 0) { // Menu by name
                Searching.SearchResult<MenuItem> res = Searching.linearSearchMenuByName(manager.getMenu(), query);
                displaySteps(res.getSteps(), res.getComparisons(), res.isFound() ? "Found: " + res.getFoundItem().getName() : "Not Found");
            } else if (targetIdx == 1) { // Menu by ID
                try {
                    int id = Integer.parseInt(query);
                    Searching.SearchResult<MenuItem> res = Searching.linearSearchMenuByName(manager.getMenu(), String.valueOf(id));
                    displaySteps(res.getSteps(), res.getComparisons(), res.isFound() ? "Found: " + res.getFoundItem().getName() : "Not Found");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid numeric ID!");
                }
            } else if (targetIdx == 2) { // Order by ID
                try {
                    int orderId = Integer.parseInt(query);
                    Searching.SearchResult<Order> res = Searching.linearSearchOrderById(manager.getAllOrders(), orderId);
                    displaySteps(res.getSteps(), res.getComparisons(), res.isFound() ? "Found Order #" + res.getFoundItem().getOrderId() : "Not Found");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid numeric Order ID (e.g. 101)!");
                }
            } else { // Customer by name
                Searching.SearchResult<Customer> res = Searching.linearSearchCustomerByName(manager.getAllCustomers(), query);
                displaySteps(res.getSteps(), res.getComparisons(), res.isFound() ? "Found Customer: " + res.getFoundItem().getName() : "Not Found");
            }
        } else {
            // Binary Search
            if (targetIdx == 1 || targetIdx == 0) {
                try {
                    int id = Integer.parseInt(query);
                    // Ensure sorted by ID
                    List<MenuItem> menu = manager.getMenu();
                    Searching.SearchResult<MenuItem> res = Searching.binarySearchMenuById(menu, id);
                    displaySteps(res.getSteps(), res.getComparisons(), res.isFound() ? "Found: " + res.getFoundItem().getName() : "Not Found");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Binary search demo on Menu requires a numeric Item ID (e.g. 1, 2, 3)!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Binary Search requires sorted keys (Try Menu Item by ID)!");
            }
        }
    }

    private void displaySteps(List<String> steps, int comparisons, String resultSummary) {
        StringBuilder sb = new StringBuilder();
        for (String step : steps) {
            sb.append(step).append("\n");
        }
        traceArea.setText(sb.toString());
        lblStats.setText("⚡ Comparisons: " + comparisons + " | " + resultSummary);
    }
}
