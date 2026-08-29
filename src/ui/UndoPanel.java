package ui;

import model.Action;
import service.RestaurantManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * UndoPanel.java
 * 
 * Manages Undo operations powered by CustomStack<Action> (LIFO).
 * Each customer seated, table freed, or order placed is recorded on the Stack.
 * Clicking "Undo" pops the latest action and reverses its state.
 */
public class UndoPanel extends JPanel {

    private RestaurantManager manager;
    private DefaultListModel<String> undoListModel;
    private JTextArea visualUndoArea;
    private JLabel lblStatus;

    public UndoPanel() {
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
        JLabel title = new JLabel(" Undo System : Custom Stack (LIFO)");
        title.setFont(UIUtils.FONT_TITLE);
        title.setForeground(UIUtils.COLOR_TEXT_MAIN);
        headerPanel.add(title, BorderLayout.NORTH);

        JLabel subtitle = new JLabel("Every restaurant event is pushed onto the Undo Stack. The latest action is popped and reversed first.");
        subtitle.setFont(UIUtils.FONT_BODY);
        subtitle.setForeground(UIUtils.COLOR_TEXT_MUTED);
        headerPanel.add(subtitle, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // Center Content
        JPanel mainContent = new JPanel(new GridLayout(1, 2, 20, 0));
        mainContent.setOpaque(false);

        // 1. Left Action & Visual Panel
        JPanel leftCard = UIUtils.createRoundedPanel(20, Color.WHITE);
        leftCard.setLayout(new BorderLayout(15, 12));
        leftCard.setBorder(new EmptyBorder(18, 20, 18, 20));

        // Big Undo Button
        JButton btnUndo = UIUtils.createRoundedButton(" UNDO LAST ACTION (POP STACK)", UIUtils.COLOR_PRIMARY, Color.WHITE);
        btnUndo.setFont(UIUtils.FONT_HEADER);
        btnUndo.setPreferredSize(new Dimension(280, 48));
        btnUndo.addActionListener(e -> onUndo());
        leftCard.add(btnUndo, BorderLayout.NORTH);

        visualUndoArea = new JTextArea();
        visualUndoArea.setFont(UIUtils.FONT_MONO);
        visualUndoArea.setBackground(new Color(255, 245, 247));
        visualUndoArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        visualUndoArea.setEditable(false);
        leftCard.add(new JScrollPane(visualUndoArea), BorderLayout.CENTER);

        lblStatus = new JLabel("Ready to undo actions.");
        lblStatus.setFont(UIUtils.FONT_BODY_BOLD);
        lblStatus.setForeground(UIUtils.COLOR_PRIMARY);
        leftCard.add(lblStatus, BorderLayout.SOUTH);

        mainContent.add(leftCard);

        // 2. Right Action History List & Viva Note
        JPanel rightCard = UIUtils.createRoundedPanel(20, Color.WHITE);
        rightCard.setLayout(new BorderLayout(12, 12));
        rightCard.setBorder(new EmptyBorder(18, 20, 18, 20));

        JLabel rightTitle = new JLabel(" Action Stack Trail (Top - Bottom)");
        rightTitle.setFont(UIUtils.FONT_HEADER);
        rightTitle.setForeground(UIUtils.COLOR_TEXT_MAIN);
        rightCard.add(rightTitle, BorderLayout.NORTH);

        undoListModel = new DefaultListModel<>();
        JList<String> list = new JList<>(undoListModel);
        list.setFont(UIUtils.FONT_BODY);
        rightCard.add(new JScrollPane(list), BorderLayout.CENTER);

        String whyUndo = "When a user makes a mistake (e.g., seating the wrong customer or creating an accidental order), they expect the MOST RECENT action to be undone first.\n\n"
                       + "• push(Action): O(1) time whenever an action occurs.\n"
                       + "• pop(): O(1) time to retrieve and reverse the exact latest state.\n"
                       + "• Stack is the universal industry standard data structure for Undo / Redo!";
        JPanel whyCard = UIUtils.createWhyCard("Stack for Undo", whyUndo);
        rightCard.add(whyCard, BorderLayout.SOUTH);

        mainContent.add(rightCard);
        add(mainContent, BorderLayout.CENTER);
    }

    private void onUndo() {
        String result = manager.undoLastAction();
        lblStatus.setText(result);
        JOptionPane.showMessageDialog(this, result, "Undo State Restored", JOptionPane.INFORMATION_MESSAGE);
        refresh();
    }

    public void refresh() {
        undoListModel.clear();
        List<Action> list = manager.getUndoStack().toList();

        if (list.isEmpty()) {
            undoListModel.addElement(" Undo stack is empty. Perform some restaurant actions!");
            visualUndoArea.setText("TOP - [ Stack Empty ]\n\nNo actions recorded yet.");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("      ┌────────────────────────────────────────────────────────┐\n");
            for (int i = 0; i < list.size(); i++) {
                Action a = list.get(i);
                undoListModel.addElement((i == 0 ? "TOP ➔ " : "      ") + a.toString());

                if (i == 0) {
                    sb.append(String.format("TOP ➔ │ %-54s │ [Next to Undo]\n", truncate(a.getDescription(), 54)));
                } else {
                    sb.append(String.format("      │ %-54s │\n", truncate(a.getDescription(), 54)));
                }
                sb.append("      ├────────────────────────────────────────────────────────┤\n");
            }
            sb.append("      └────────────────────────────────────────────────────────┘ [BOTTOM]\n");
            visualUndoArea.setText(sb.toString());
        }
    }

    private String truncate(String text, int maxLen) {
        if (text.length() <= maxLen) return text;
        return text.substring(0, maxLen - 3) + "...";
    }
}
