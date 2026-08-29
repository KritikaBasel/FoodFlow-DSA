package ui;

import dsa.Sorting;
import model.MenuItem;
import model.Order;
import service.RestaurantManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * SortPanel.java
 * 
 * Interactive DSA Sorting Playground.
 * Demonstrates:
 * 1. Bubble Sort (Adjacent swaps)
 * 2. Selection Sort (Minimum selection)
 * 3. Insertion Sort (Prefix insertion)
 * 
 * Shows full pass-by-pass and swap-by-swap trace output.
 */
public class SortPanel extends JPanel {

    private RestaurantManager manager;
    private JComboBox<String> comboAlgorithm;
    private JComboBox<String> comboTarget;
    private JTextArea traceArea;
    private JLabel lblStats;

    public SortPanel() {
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
        JLabel title = new JLabel(" Sorting Algorithms Playground");
        title.setFont(UIUtils.FONT_TITLE);
        title.setForeground(UIUtils.COLOR_TEXT_MAIN);
        headerPanel.add(title, BorderLayout.NORTH);

        JLabel subtitle = new JLabel("Visualize Bubble Sort, Selection Sort, and Insertion Sort in action on FoodFlow menu and orders.");
        subtitle.setFont(UIUtils.FONT_BODY);
        subtitle.setForeground(UIUtils.COLOR_TEXT_MUTED);
        headerPanel.add(subtitle, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // Center Content
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setOpaque(false);

        // Controls
        JPanel controlsCard = UIUtils.createRoundedPanel(20, Color.WHITE);
        controlsCard.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 12));
        controlsCard.setBorder(new EmptyBorder(10, 15, 10, 15));

        controlsCard.add(new JLabel("Algorithm:"));
        comboAlgorithm = new JComboBox<>(new String[]{"Bubble Sort (O(n²))", "Selection Sort (O(n²))", "Insertion Sort (O(n²))"});
        comboAlgorithm.setFont(UIUtils.FONT_BODY);
        controlsCard.add(comboAlgorithm);

        controlsCard.add(new JLabel("Target:"));
        comboTarget = new JComboBox<>(new String[]{"Menu by Price (Low - High)", "Menu by Price (High - Low)", "Menu by Name (A - Z)", "Orders by Order ID"});
        comboTarget.setFont(UIUtils.FONT_BODY);
        controlsCard.add(comboTarget);

        JButton btnSort = UIUtils.createRoundedButton("Run Sorting Simulation ", UIUtils.COLOR_PRIMARY, Color.WHITE);
        btnSort.addActionListener(e -> executeSort());
        controlsCard.add(btnSort);

        centerPanel.add(controlsCard, BorderLayout.NORTH);

        // Split Panel: Trace on left, Viva notes on right
        JPanel splitPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        splitPanel.setOpaque(false);

        // Left Trace
        JPanel traceCard = UIUtils.createRoundedPanel(20, Color.WHITE);
        traceCard.setLayout(new BorderLayout(12, 12));
        traceCard.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel traceHeader = new JLabel(" Pass-by-Pass Sorting Trace");
        traceHeader.setFont(UIUtils.FONT_HEADER);
        traceHeader.setForeground(UIUtils.COLOR_PRIMARY);
        traceCard.add(traceHeader, BorderLayout.NORTH);

        traceArea = new JTextArea();
        traceArea.setFont(UIUtils.FONT_MONO);
        traceArea.setBackground(new Color(255, 245, 247));
        traceArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        traceArea.setEditable(false);
        traceArea.setText("Choose sorting criteria above and click \"Run Sorting Simulation\".");
        traceCard.add(new JScrollPane(traceArea), BorderLayout.CENTER);

        lblStats = new JLabel("Status: Idle");
        lblStats.setFont(UIUtils.FONT_BODY_BOLD);
        lblStats.setForeground(UIUtils.COLOR_TEXT_MAIN);
        traceCard.add(lblStats, BorderLayout.SOUTH);

        splitPanel.add(traceCard);

        // Right Viva Cards
        JPanel vivaCard = UIUtils.createRoundedPanel(20, Color.WHITE);
        vivaCard.setLayout(new GridLayout(3, 1, 8, 8));
        vivaCard.setBorder(new EmptyBorder(14, 16, 14, 16));

        String whyBubble = "• BUBBLE SORT:\n"
                         + "Compares neighboring elements [j] & [j+1] and swaps them if out of order.\n"
                         + "Time Complexity: Best O(n) (if already sorted), Avg/Worst O(n²).";
        vivaCard.add(UIUtils.createWhyCard("Bubble Sort", whyBubble));

        String whySelection = "• SELECTION SORT:\n"
                            + "Finds minimum element in remaining list and places it at index i.\n"
                            + "Time Complexity: O(n²) in all cases (Always makes n(n-1)/2 comparisons).";
        vivaCard.add(UIUtils.createWhyCard("Selection Sort", whySelection));

        String whyInsertion = "• INSERTION SORT:\n"
                            + "Builds sorted prefix by shifting larger elements right and inserting key.\n"
                            + "Time Complexity: Best O(n), Avg/Worst O(n²). Efficient for small/nearly sorted data.";
        vivaCard.add(UIUtils.createWhyCard("Insertion Sort", whyInsertion));

        splitPanel.add(vivaCard);
        centerPanel.add(splitPanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void executeSort() {
        int algo = comboAlgorithm.getSelectedIndex();
        int target = comboTarget.getSelectedIndex();

        StringBuilder sb = new StringBuilder();

        if (algo == 0) { // Bubble Sort
            if (target == 0) { // Price Asc
                Sorting.SortResult<MenuItem> res = Sorting.bubbleSortMenuByPrice(manager.getMenu(), true);
                renderSortResult(res);
            } else if (target == 1) { // Price Desc
                Sorting.SortResult<MenuItem> res = Sorting.bubbleSortMenuByPrice(manager.getMenu(), false);
                renderSortResult(res);
            } else if (target == 2) { // Name A-Z
                Sorting.SortResult<MenuItem> res = Sorting.bubbleSortMenuByName(manager.getMenu());
                renderSortResult(res);
            } else {
                Sorting.SortResult<Order> res = Sorting.insertionSortOrdersById(manager.getAllOrders());
                renderSortResultGeneric(res);
            }
        } else if (algo == 1) { // Selection Sort
            boolean asc = (target != 1);
            Sorting.SortResult<MenuItem> res = Sorting.selectionSortMenuByPrice(manager.getMenu(), asc);
            renderSortResult(res);
        } else { // Insertion Sort
            Sorting.SortResult<Order> res = Sorting.insertionSortOrdersById(manager.getAllOrders());
            renderSortResultGeneric(res);
        }
    }

    private void renderSortResult(Sorting.SortResult<MenuItem> res) {
        StringBuilder sb = new StringBuilder();
        for (String step : res.getLogSteps()) {
            sb.append(step).append("\n");
        }
        sb.append("\n FINAL SORTED LIST:\n");
        for (MenuItem m : res.getSortedList()) {
            sb.append(String.format("  • %-16s | %-12s | $%.2f\n", m.getName(), m.getCategory(), m.getPrice()));
        }
        traceArea.setText(sb.toString());
        lblStats.setText(String.format("%s finished with %d comparisons and %d swaps!", res.getAlgorithmName(), res.getComparisons(), res.getSwaps()));
    }

    private void renderSortResultGeneric(Sorting.SortResult<Order> res) {
        StringBuilder sb = new StringBuilder();
        for (String step : res.getLogSteps()) {
            sb.append(step).append("\n");
        }
        sb.append("\n FINAL SORTED ORDERS:\n");
        for (Order o : res.getSortedList()) {
            sb.append("  • ").append(o.toString()).append("\n");
        }
        traceArea.setText(sb.toString());
        lblStats.setText(String.format(" %s finished with %d comparisons and %d swaps/shifts!", res.getAlgorithmName(), res.getComparisons(), res.getSwaps()));
    }
}
