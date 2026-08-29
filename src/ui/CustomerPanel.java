package ui;

import model.Customer;
import service.RestaurantManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * CustomerPanel.java
 * 
 * Manages waiting customers powered by CustomQueue<Customer> (FIFO).
 * Features visual queue flow, add customer form, seat next customer action,
 * and viva explanation cards.
 */
public class CustomerPanel extends JPanel {

    private RestaurantManager manager;
    private DefaultListModel<String> queueListModel;
    private JList<String> queueList;
    private JTextArea visualQueueArea;

    private JTextField txtName;
    private JSpinner spinGuests;
    private JTextField txtPhone;
    private JCheckBox chkVIP;

    public CustomerPanel() {
        this.manager = RestaurantManager.getInstance();
        setLayout(new BorderLayout(20, 20));
        setBackground(UIUtils.COLOR_BG);
        setBorder(new EmptyBorder(20, 25, 20, 25));

        initComponents();
        refresh();
    }

    private void initComponents() {
        // Top Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel title = new JLabel(" Customer Management : Custom Queue (FIFO)");
        title.setFont(UIUtils.FONT_TITLE);
        title.setForeground(UIUtils.COLOR_TEXT_MAIN);
        headerPanel.add(title, BorderLayout.NORTH);

        JLabel subtitle = new JLabel("Customers waiting for a table enter the Queue at the Rear and are served from the Front.");
        subtitle.setFont(UIUtils.FONT_BODY);
        subtitle.setForeground(UIUtils.COLOR_TEXT_MUTED);
        headerPanel.add(subtitle, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // Center Split: Left = Add Customer Form & Controls, Right = Visual Queue & List
        JPanel mainContent = new JPanel(new GridLayout(1, 2, 20, 0));
        mainContent.setOpaque(false);

        // 1. Left Form Panel
        JPanel leftPanel = new JPanel(new BorderLayout(15, 15));
        leftPanel.setOpaque(false);

        JPanel formCard = UIUtils.createRoundedPanel(20, Color.WHITE);
        formCard.setLayout(new GridBagLayout());
        formCard.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel formHeader = new JLabel("Add New Customer");
        formHeader.setFont(UIUtils.FONT_HEADER);
        formHeader.setForeground(UIUtils.COLOR_PRIMARY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formCard.add(formHeader, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0;
        formCard.add(new JLabel("Customer Name:"), gbc);
        gbc.gridx = 1;
        txtName = new JTextField(12);
        txtName.setFont(UIUtils.FONT_BODY);
        formCard.add(txtName, gbc);

        gbc.gridy = 2; gbc.gridx = 0;
        formCard.add(new JLabel("Party Size (Guests):"), gbc);
        gbc.gridx = 1;
        spinGuests = new JSpinner(new SpinnerNumberModel(2, 1, 12, 1));
        spinGuests.setFont(UIUtils.FONT_BODY);
        formCard.add(spinGuests, gbc);

        gbc.gridy = 3; gbc.gridx = 0;
        formCard.add(new JLabel("Phone Number:"), gbc);
        gbc.gridx = 1;
        txtPhone = new JTextField(12);
        txtPhone.setFont(UIUtils.FONT_BODY);
        formCard.add(txtPhone, gbc);

        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2;
        chkVIP = new JCheckBox(" VIP Customer Priority");
        chkVIP.setFont(UIUtils.FONT_BODY_BOLD);
        chkVIP.setForeground(new Color(214, 48, 49));
        chkVIP.setOpaque(false);
        formCard.add(chkVIP, gbc);

        gbc.gridy = 5;
        JButton btnAdd = UIUtils.createRoundedButton("Add Customer to Restaurant", UIUtils.COLOR_PRIMARY, Color.WHITE);
        btnAdd.addActionListener(e -> onAddCustomer());
        formCard.add(btnAdd, gbc);

        leftPanel.add(formCard, BorderLayout.NORTH);

        // Viva "Why Queue" Card
        String whyQueue = "Restaurants must serve waiting guests fairly in the exact order they arrive (FIFO - First In, First Out).\n\n"
                        + "• enqueue(): New arrivals enter at REAR in O(1)\n"
                        + "• dequeue(): Front customer receives next free table in O(1)\n"
                        + "• peek(): Check who is next in line without removing in O(1)";
        JPanel whyCard = UIUtils.createWhyCard("Queue", whyQueue);
        leftPanel.add(whyCard, BorderLayout.CENTER);

        mainContent.add(leftPanel);

        // 2. Right Queue Visual Panel
        JPanel rightPanel = UIUtils.createRoundedPanel(20, Color.WHITE);
        rightPanel.setLayout(new BorderLayout(12, 12));
        rightPanel.setBorder(new EmptyBorder(18, 20, 18, 20));

        JLabel queueHeader = new JLabel(" Live Waiting Queue (FIFO)");
        queueHeader.setFont(UIUtils.FONT_HEADER);
        queueHeader.setForeground(UIUtils.COLOR_TEXT_MAIN);
        rightPanel.add(queueHeader, BorderLayout.NORTH);

        // Visual Flow Diagram
        visualQueueArea = new JTextArea(4, 30);
        visualQueueArea.setFont(UIUtils.FONT_MONO);
        visualQueueArea.setBackground(new Color(255, 245, 247));
        visualQueueArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        visualQueueArea.setEditable(false);
        rightPanel.add(new JScrollPane(visualQueueArea), BorderLayout.CENTER);

        // Queue List
        queueListModel = new DefaultListModel<>();
        queueList = new JList<>(queueListModel);
        queueList.setFont(UIUtils.FONT_BODY);
        queueList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScroll = new JScrollPane(queueList);
        listScroll.setPreferredSize(new Dimension(280, 140));
        rightPanel.add(listScroll, BorderLayout.SOUTH);

        mainContent.add(rightPanel);
        add(mainContent, BorderLayout.CENTER);
    }

    private void onAddCustomer() {
        String name = txtName.getText().trim();
        String phone = txtPhone.getText().trim();
        int guests = (int) spinGuests.getValue();
        boolean isVip = chkVIP.isSelected();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter customer name!", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (phone.isEmpty()) {
            phone = "984100000" + (int)(Math.random() * 90 + 10);
        }

        String result = manager.addCustomer(name, guests, phone, isVip);
        JOptionPane.showMessageDialog(this, result, "Customer Update", JOptionPane.INFORMATION_MESSAGE);

        txtName.setText("");
        txtPhone.setText("");
        chkVIP.setSelected(false);
        refresh();
    }

    public void refresh() {
        queueListModel.clear();
        List<Customer> list = manager.getWaitingQueue().toList();

        if (list.isEmpty()) {
            queueListModel.addElement(" Waiting queue is empty. All guests are seated!");
            visualQueueArea.setText("Front - [ Empty Queue ] - Rear\n\nNo customers currently waiting.");
        } else {
            StringBuilder sb = new StringBuilder("Front (Next to be seated)\n ↓\n");
            for (int i = 0; i < list.size(); i++) {
                Customer c = list.get(i);
                queueListModel.addElement((i + 1) + ". " + c.toString());
                sb.append("[").append(c.getName()).append(" (").append(c.getNumberOfPeople()).append("p)");
                if (c.isVIP()) sb.append("*");
                sb.append("]");
                if (i < list.size() - 1) sb.append(" - ");
            }
            sb.append("\n ↑\nRear (Latest arrival)");
            visualQueueArea.setText(sb.toString());
        }
    }
}
