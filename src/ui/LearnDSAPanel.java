package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * LearnDSAPanel.java
 * 
 * Comprehensive University DSA Viva & Learning Section.
 * Contains full educational explanations, ASCII diagrams, operation lists,
 * time complexities, and "Why Did We Use This?" cards for all DSA concepts:
 * 1. Array
 * 2. Linked List
 * 3. Stack
 * 4. Queue
 * 5. Priority Queue
 * 6. Searching (Linear & Binary)
 * 7. Sorting (Bubble, Selection, Insertion)
 * 8. Binary Search Tree (BST)
 * 9. Graph
 * 10. Master Summary Table
 */
public class LearnDSAPanel extends JPanel {

    private CardLayout cardLayout;
    private JPanel contentContainer;
    private Map<String, JButton> navButtons;

    public LearnDSAPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(UIUtils.COLOR_BG);
        setBorder(new EmptyBorder(20, 25, 20, 25));

        initComponents();
    }

    private void initComponents() {
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel title = new JLabel(" Learn DSA : University Viva Knowledge Base");
        title.setFont(UIUtils.FONT_TITLE);
        title.setForeground(UIUtils.COLOR_TEXT_MAIN);
        headerPanel.add(title, BorderLayout.NORTH);

        JLabel subtitle = new JLabel("Explore deep explanations, ASCII diagrams, time complexities, and why each structure powers FoodFlow.");
        subtitle.setFont(UIUtils.FONT_BODY);
        subtitle.setForeground(UIUtils.COLOR_TEXT_MUTED);
        headerPanel.add(subtitle, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // Center Split: Left Topic Selector List, Right Detail Card View
        JPanel centerSplit = new JPanel(new BorderLayout(15, 0));
        centerSplit.setOpaque(false);

        // Left Navigation Pills
        JPanel leftNav = UIUtils.createRoundedPanel(20, Color.WHITE);
        leftNav.setLayout(new BoxLayout(leftNav, BoxLayout.Y_AXIS));
        leftNav.setBorder(new EmptyBorder(15, 12, 15, 12));
        leftNav.setPreferredSize(new Dimension(200, 480));

        JLabel navTitle = new JLabel(" DSA Topics");
        navTitle.setFont(UIUtils.FONT_HEADER);
        navTitle.setForeground(UIUtils.COLOR_PRIMARY);
        navTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftNav.add(navTitle);
        leftNav.add(Box.createVerticalStrut(10));

        String[] topics = {
                " Queue",
                " Stack",
                " Priority Queue",
                " Array",
                " Linked List",
                " Searching",
                " Sorting",
                " Binary Search Tree",
                " Graph",
                " Summary Cheat Sheet"
        };

        cardLayout = new CardLayout();
        contentContainer = new JPanel(cardLayout);
        contentContainer.setOpaque(false);
        navButtons = new HashMap<>();

        for (String topic : topics) {
            String key = topic.split(" ")[1];
            JButton btn = new JButton(topic);
            btn.setFont(UIUtils.FONT_BODY_BOLD);
            btn.setForeground(UIUtils.COLOR_TEXT_MAIN);
            btn.setBackground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            btn.setMaximumSize(new Dimension(180, 34));
            btn.setHorizontalAlignment(SwingConstants.LEFT);

            btn.addActionListener(e -> selectTopic(key));
            navButtons.put(key, btn);
            leftNav.add(btn);
            leftNav.add(Box.createVerticalStrut(4));
        }

        centerSplit.add(leftNav, BorderLayout.WEST);

        // Populate Cards
        contentContainer.add(createQueueCard(), "Queue");
        contentContainer.add(createStackCard(), "Stack");
        contentContainer.add(createPriorityQueueCard(), "Priority");
        contentContainer.add(createArrayCard(), "Array");
        contentContainer.add(createLinkedListCard(), "Linked");
        contentContainer.add(createSearchingCard(), "Searching");
        contentContainer.add(createSortingCard(), "Sorting");
        contentContainer.add(createBSTCard(), "Binary");
        contentContainer.add(createGraphCard(), "Graph");
        contentContainer.add(createSummaryCard(), "Summary");

        centerSplit.add(contentContainer, BorderLayout.CENTER);
        add(centerSplit, BorderLayout.CENTER);

        selectTopic("Queue");
    }

    private void selectTopic(String key) {
        cardLayout.show(contentContainer, key);
        for (Map.Entry<String, JButton> entry : navButtons.entrySet()) {
            if (entry.getKey().equals(key)) {
                entry.getValue().setForeground(UIUtils.COLOR_PRIMARY);
                entry.getValue().setFont(new Font("Segoe UI", Font.BOLD, 13));
            } else {
                entry.getValue().setForeground(UIUtils.COLOR_TEXT_MAIN);
                entry.getValue().setFont(UIUtils.FONT_BODY);
            }
        }
    }

    private JPanel createDetailCard(String title, String whatIsIt, String asciiDiagram, String operations, String foodflowUsage, String whyText, String complexities) {
        JPanel card = UIUtils.createRoundedPanel(20, Color.WHITE);
        card.setLayout(new BorderLayout(12, 12));
        card.setBorder(new EmptyBorder(20, 22, 20, 22));

        // Title
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(UIUtils.FONT_TITLE);
        lblTitle.setForeground(UIUtils.COLOR_PRIMARY);
        card.add(lblTitle, BorderLayout.NORTH);

        // Content Area inside scroll
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        // 1. What is it?
        content.add(createSectionTitle(" What is it?"));
        content.add(createWrappedText(whatIsIt));
        content.add(Box.createVerticalStrut(10));

        // 2. ASCII Visual
        content.add(createSectionTitle(" Visual Representation:"));
        JTextArea ascii = new JTextArea(asciiDiagram);
        ascii.setFont(UIUtils.FONT_MONO);
        ascii.setBackground(new Color(255, 245, 247));
        ascii.setBorder(new EmptyBorder(8, 10, 8, 10));
        ascii.setEditable(false);
        content.add(ascii);
        content.add(Box.createVerticalStrut(10));

        // 3. Operations
        content.add(createSectionTitle(" Main Operations:"));
        content.add(createWrappedText(operations));
        content.add(Box.createVerticalStrut(10));

        // 4. FoodFlow Usage & Why Card
        content.add(createSectionTitle("FoodFlow Restaurant Usage:"));
        content.add(createWrappedText(foodflowUsage));
        content.add(Box.createVerticalStrut(8));
        content.add(UIUtils.createWhyCard(title.replace("Topic: ", ""), whyText));
        content.add(Box.createVerticalStrut(10));

        // 5. Complexity Table
        content.add(createSectionTitle("Time Complexity:"));
        content.add(createWrappedText(complexities));

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        card.add(scroll, BorderLayout.CENTER);

        return card;
    }

    private JLabel createSectionTitle(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UIUtils.FONT_HEADER);
        lbl.setForeground(UIUtils.COLOR_TEXT_MAIN);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JTextArea createWrappedText(String text) {
        JTextArea area = new JTextArea(text);
        area.setFont(UIUtils.FONT_BODY);
        area.setForeground(UIUtils.COLOR_TEXT_MAIN);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        area.setOpaque(false);
        area.setAlignmentX(Component.LEFT_ALIGNMENT);
        return area;
    }

    // --- TOPIC BUILDERS ---

    private JPanel createQueueCard() {
        String what = "A Queue is a linear data structure that operates strictly on the FIFO (First In, First Out) principle. The element added first is always removed first.";
        String ascii = "FRONT (Head)                               REAR (Tail)\n"
                     + "  ↓                                          ↓\n"
                     + "[Customer A] ➔ [Customer B] ➔ [Customer C] ➔ [Customer D]\n\n"
                     + "dequeue() removes from FRONT               enqueue() adds at REAR";
        String ops = "• enqueue(item): Adds item to the rear of the queue.\n"
                   + "• dequeue(): Removes and returns the front item.\n"
                   + "• peek(): Looks at the front item without removing.\n"
                   + "• isEmpty(): Checks if queue has no elements.\n"
                   + "• size(): Returns the current element count.";
        String usage = "1. Customer Waiting List: When dining tables are full, new arrivals wait in Queue.\n"
                     + "2. Standard Kitchen Orders: Chef cooks normal food orders in order of arrival.";
        String why = "Fairness in a restaurant requires serving guests in arrival order. A Queue mathematically guarantees FIFO order with O(1) instantaneous addition and removal.";
        String comp = "• enqueue() ➔ O(1)\n• dequeue() ➔ O(1)\n• peek()    ➔ O(1)\n• search()  ➔ O(n)\n• Space     ➔ O(n)";
        return createDetailCard(" Topic: QUEUE (FIFO)", what, ascii, ops, usage, why, comp);
    }

    private JPanel createStackCard() {
        String what = "A Stack is a linear data structure that operates on the LIFO (Last In, First Out) principle. The most recently added element is always the first one to be removed.";
        String ascii = "TOP ➔ ┌───────────────────────────┐\n"
                     + "      │ Action #3 (Latest Action) │\n"
                     + "      ├───────────────────────────┤\n"
                     + "      │ Action #2                 │\n"
                     + "      ├───────────────────────────┤\n"
                     + "      │ Action #1 (Oldest Action) │\n"
                     + "      └───────────────────────────┘ [BOTTOM]";
        String ops = "• push(item): Pushes a new item onto the top of the stack.\n"
                   + "• pop(): Removes and returns the top element.\n"
                   + "• peek(): Views the top element without removing it.\n"
                   + "• isEmpty(): Checks if stack is empty.";
        String usage = "1. ↩ Undo System: Storing user actions so the latest mistake can be reversed first.\n"
                     + "2.  Completed Order History: Cashier can immediately access the most recently printed receipt at the top.";
        String why = "When undoing an action or looking up the last completed receipt, the user only cares about the most recent state. Stack provides O(1) top-of-stack access.";
        String comp = "• push()  ➔ O(1)\n• pop()   ➔ O(1)\n• peek()  ➔ O(1)\n• search()➔ O(n)\n• Space   ➔ O(n)";
        return createDetailCard(" Topic: STACK (LIFO)", what, ascii, ops, usage, why, comp);
    }

    private JPanel createPriorityQueueCard() {
        String what = "A Priority Queue is an extension of a Queue where each element has an assigned priority. Elements with higher priority are dequeued before normal elements, regardless of arrival time.";
        String ascii = "HEAD (Highest Priority)\n"
                     + "  ↓\n"
                     + "[Priority 1: VIP Order #103] \n"
                     + "  ↓\n"
                     + "[Priority 2: Urgent Order #102] ⚡\n"
                     + "  ↓\n"
                     + "[Priority 3: Normal Order #101] \n"
                     + "  ↓\n"
                     + "NULL";
        String ops = "• enqueue(item, priority): Inserts item in sorted priority order.\n"
                   + "• dequeue(): Removes the highest priority item.\n"
                   + "• peek(): Inspects the highest priority item.";
        String usage = "VIP & Urgent Kitchen Orders: High-paying or VIP guests receive immediate culinary attention before standard orders.";
        String why = "A normal FIFO queue would force VIP guests to wait behind everyone. Priority Queue gives special priority without losing FIFO order among equal-priority items.";
        String comp = "• enqueue() ➔ O(n) (sorted insertion) or O(log n) (heap)\n• dequeue() ➔ O(1)\n• peek()    ➔ O(1)\n• Space     ➔ O(n)";
        return createDetailCard(" Topic: PRIORITY QUEUE", what, ascii, ops, usage, why, comp);
    }

    private JPanel createArrayCard() {
        String what = "An Array is a fixed-size, contiguous memory data structure storing elements of the same type indexed from 0 to n-1.";
        String ascii = "Index:      0          1          2          3          4          5\n"
                     + "         ┌──────────┬──────────┬──────────┬──────────┬──────────┬──────────┐\n"
                     + "Table[]: │ Table #1 │ Table #2 │ Table #3 │ Table #4 │ Table #5 │ Table #6 │\n"
                     + "         └──────────┴──────────┴──────────┴──────────┴──────────┴──────────┘\n"
                     + "Status:    Occupied     Free     Occupied     Free       Free     Occupied";
        String ops = "• tables[i]: Direct index access in O(1) time.\n"
                   + "• tables[i] = new Table(): Update element at index.";
        String usage = "Restaurant Dining Tables: Restaurant has exactly 6 physical tables. Each table has a fixed table number corresponding directly to array indices.";
        String why = "Physical table count is predetermined and rarely changes dynamically. Arrays provide maximum memory efficiency and O(1) instant lookup by table number.";
        String comp = "• Access by Index ➔ O(1)\n• Update by Index ➔ O(1)\n• Linear Search   ➔ O(n)\n• Space           ➔ O(n)";
        return createDetailCard(" Topic: ARRAY", what, ascii, ops, usage, why, comp);
    }

    private JPanel createLinkedListCard() {
        String what = "A Linked List is a dynamic linear data structure composed of Nodes. Each node contains data and a pointer reference to the next node in memory.";
        String ascii = "[HEAD] ➔ [Data: A | Next] ➔ [Data: B | Next] ➔ [Data: C | Next] ➔ NULL";
        String ops = "• addFirst(item): O(1) insertion at head.\n• addLast(item): Insertion at tail.\n• remove(index): Deletion by unlinking pointers.";
        String usage = "Underlying mechanism for our CustomQueue and CustomStack implementations!";
        String why = "Unlike arrays, Linked Lists do not require a predetermined size and can grow or shrink dynamically in memory without expensive array resizing.";
        String comp = "• Insert at Head ➔ O(1)\n• Delete at Head ➔ O(1)\n• Access index i ➔ O(n)\n• Search         ➔ O(n)";
        return createDetailCard(" Topic: LINKED LIST", what, ascii, ops, usage, why, comp);
    }

    private JPanel createSearchingCard() {
        String what = "Searching algorithms locate target data in a collection. We implement Linear Search and Binary Search.";
        String ascii = "LINEAR SEARCH:  [10] ➔ [20] ➔ [30 (MATCH!)] ➔ [40]\n\n"
                     + "BINARY SEARCH:  [10, 20, 30, 40, 50, 60, 70]\n"
                     + "                             ↑\n"
                     + "                        mid = 40 (Target 60 > 40, search right half!)";
        String ops = "• linearSearch(): Iterates sequentially through list.\n• binarySearch(): Divides sorted search space in half repeatedly.";
        String usage = "Finding specific customer records, orders by ID, and dishes from the menu.";
        String why = "Linear search works on unsorted lists; Binary search provides logarithmic O(log n) ultra-fast search when menu/order data is pre-sorted.";
        String comp = "• Linear Search ➔ Best: O(1), Avg/Worst: O(n)\n• Binary Search ➔ Best: O(1), Avg/Worst: O(log n)";
        return createDetailCard(" Topic: SEARCHING ALGORITHMS", what, ascii, ops, usage, why, comp);
    }

    private JPanel createSortingCard() {
        String what = "Sorting arranges elements in a specified order (e.g., ascending price or alphabetical name). We implement Bubble Sort, Selection Sort, and Insertion Sort.";
        String ascii = "BUBBLE SORT PASS 1:\n"
                     + "[12.0, 6.5, 8.0] ➔ Compare (12.0 > 6.5) ➔ Swap ➔ [6.5, 12.0, 8.0]\n"
                     + "                 ➔ Compare (12.0 > 8.0) ➔ Swap ➔ [6.5, 8.0, 12.0] (Sorted!)";
        String ops = "• bubbleSort(): Compares adjacent pairs and bubbles largest to the end.\n"
                   + "• selectionSort(): Picks minimum and places at start.\n"
                   + "• insertionSort(): Inserts into sorted subarray.";
        String usage = "Sorting menu items by price (Low to High / High to Low), organizing dishes alphabetically, and sorting orders by ID.";
        String why = "Allows restaurant customers to browse cheapest dishes first, and allows managers to view structured chronological reports.";
        String comp = "• Bubble Sort    ➔ Best: O(n)*, Avg: O(n²), Worst: O(n²)\n• Selection Sort ➔ Best/Avg/Worst: O(n²)\n• Insertion Sort ➔ Best: O(n), Avg/Worst: O(n²)";
        return createDetailCard(" Topic: SORTING ALGORITHMS", what, ascii, ops, usage, why, comp);
    }

    private JPanel createBSTCard() {
        String what = "A Binary Search Tree (BST) is a hierarchical tree data structure where each node has at most two children. The left subtree contains values smaller than the root, and the right subtree contains values larger.";
        String ascii = "               [50 $ Pizza]\n"
                     + "              /            \\\n"
                     + "       [30 $ Burger]     [70 $ Feast]\n"
                     + "       /          \\             \\\n"
                     + " [20 $ Momo]  [40 $ Pasta]    [80 $ Platter]\n\n"
                     + "Search 40: 50 ➔ Left (30) ➔ Right (40) ➔ FOUND in 2 steps!";
        String ops = "• insert(key)\n• search(key)\n• inorderTraversal() (Produces sorted sequence!)";
        String usage = "Educational Concept: Can store restaurant menu prices for lightning fast O(log n) price range queries and sorted in-order traversal.";
        String why = "BST combines the fast O(log n) search of a sorted array with the dynamic node insertion efficiency of a linked list.";
        String comp = "• Search ➔ Average: O(log n), Worst: O(n) (unbalanced)\n• Insert ➔ Average: O(log n), Worst: O(n)\n• Space  ➔ O(n)";
        return createDetailCard(" Topic: BINARY SEARCH TREE (BST)", what, ascii, ops, usage, why, comp);
    }

    private JPanel createGraphCard() {
        String what = "A Graph is a non-linear data structure consisting of Vertices (Nodes) and Edges that connect pairs of vertices. It can be directed or undirected, weighted or unweighted.";
        String ascii = "          [ KITCHEN ] ─────────────── [ COUNTER ]\n"
                     + "               │                           │\n"
                     + "               │                           │\n"
                     + "               │                           │\n"
                     + "          [ STORAGE ] ────────────── [ DINING HALL ]\n\n"
                     + "Vertices = Locations inside restaurant\n"
                     + "Edges    = Walkways / Delivery routes between locations";
        String ops = "• addVertex(V)\n• addEdge(U, V, weight)\n• BFS (Breadth-First Search - Shortest Path)\n• DFS (Depth-First Search - Exploration)";
        String usage = "Educational Concept: Represents the physical floor plan of the restaurant, waiter delivery paths, or food delivery routes to customer homes.";
        String why = "Graphs naturally model interconnected physical spaces, networks, and routing optimizations (Dijkstra's shortest path from kitchen to table).";
        String comp = "• BFS / DFS Traversal ➔ O(V + E)\n• Adjacency List Space ➔ O(V + E)\n• Adjacency Matrix Space ➔ O(V²)";
        return createDetailCard("🕸 Topic: GRAPH DATA STRUCTURE", what, ascii, ops, usage, why, comp);
    }

    private JPanel createSummaryCard() {
        JPanel card = UIUtils.createRoundedPanel(20, Color.WHITE);
        card.setLayout(new BorderLayout(12, 12));
        card.setBorder(new EmptyBorder(20, 22, 20, 22));

        JLabel title = new JLabel(" Master DSA Viva Summary Cheat Sheet");
        title.setFont(UIUtils.FONT_TITLE);
        title.setForeground(UIUtils.COLOR_PRIMARY);
        card.add(title, BorderLayout.NORTH);

        String text = "┌──────────────────┬─────────────────────────────┬─────────────────────┬───────────────────────────┐\n"
                    + "│ Data Structure   │ FoodFlow Restaurant Role    │ Principle / Nature  │ Key Time Complexity       │\n"
                    + "├──────────────────┼─────────────────────────────┼─────────────────────┼───────────────────────────┤\n"
                    + "│ Array            │ Restaurant Tables           │ Contiguous O(1) idx │ Access: O(1), Search: O(n)│\n"
                    + "│ Custom Queue     │ Customer Waiting List       │ FIFO (First In)     │ enqueue: O(1), dequeue:O1 │\n"
                    + "│ Custom Queue     │ Standard Kitchen Orders     │ FIFO (First In)     │ enqueue: O(1), dequeue:O1 │\n"
                    + "│ Custom Stack     │ Undo Last Action            │ LIFO (Last In)      │ push: O(1), pop: O(1)     │\n"
                    + "│ Custom Stack     │ Completed Order Receipts    │ LIFO (Last In)      │ push: O(1), peek: O(1)    │\n"
                    + "│ Priority Queue   │ VIP & Urgent Kitchen Orders │ Min/Max Priority    │ dequeue: O(1), enq: O(n)  │\n"
                    + "│ Linked List      │ Node structure for Q & S    │ Dynamic Pointers    │ Insert head: O(1)         │\n"
                    + "│ Linear Search    │ Finding Orders & Customers  │ Sequential Scan     │ Best: O(1), Avg/Worst:O(n)│\n"
                    + "│ Binary Search    │ Searching Sorted Menu Items │ Divide & Conquer    │ Best: O(1), Avg: O(log n) │\n"
                    + "│ Bubble Sort      │ Sorting Menu by Price/Name  │ Neighbor Swapping   │ Best: O(n), Avg: O(n²)    │\n"
                    + "│ Selection Sort   │ Sorting Menu by Price       │ Min Element Pick    │ All Cases: O(n²)          │\n"
                    + "│ BST              │ Fast Price Indexing         │ Left < Root < Right │ Search Avg: O(log n)      │\n"
                    + "│ Graph            │ Floor Layout & Routes       │ Vertices & Edges    │ Traversal: O(V + E)       │\n"
                    + "└──────────────────┴─────────────────────────────┴─────────────────────┴───────────────────────────┘\n\n"
                    + "🎓 KEY VIVA QUESTIONS & ANSWERS:\n\n"
                    + "Q1: Why did you not use Java's built-in Queue/Stack?\n"
                    + "A1: To demonstrate practical node-based memory management, pointer manipulation, and pure DSA concepts from scratch!\n\n"
                    + "Q2: What is the difference between Queue and Priority Queue in your project?\n"
                    + "A2: Normal Queue cooks orders strictly by arrival time (FIFO). Priority Queue allows VIP orders (Priority 1) to jump to the front!\n\n"
                    + "Q3: Why is Stack ideal for Undo?\n"
                    + "A3: Undo must reverse the most recently performed operation. Stack's LIFO property provides instant O(1) access to that exact recent state.";

        JTextArea area = new JTextArea(text);
        area.setFont(UIUtils.FONT_MONO);
        area.setBackground(new Color(255, 248, 240));
        area.setBorder(new EmptyBorder(10, 10, 10, 10));
        area.setEditable(false);

        card.add(new JScrollPane(area), BorderLayout.CENTER);
        return card;
    }
}
