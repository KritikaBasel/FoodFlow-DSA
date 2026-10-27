<div align="center">

# 🍽️ FoodFlow

### A Restaurant Management System Built on Core Data Structures & Algorithms

FoodFlow is a Java Swing desktop application that simulates the day-to-day operations of a restaurant from seating customers, taking orders, running the kitchen queue, to tracking history while using **hand-built implementations** of classic DSA structures (Stack, Queue, Priority Queue, Linked List) instead of Java's built-in collections. Every screen doubles as a live demo of the structure powering it.

<br>

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/GUI-Java%20Swing-blue?style=for-the-badge)
![DSA](https://img.shields.io/badge/Focus-Data%20Structures%20%26%20Algorithms-purple?style=for-the-badge)
![Desktop App](https://img.shields.io/badge/Platform-Desktop-success?style=for-the-badge)

</div>

---

## 📖 About

FoodFlow was built as a Data Structures & Algorithms project, the goal wasn't just to make a working restaurant app, but to make every core DSA concept *visible and tangible* through a real interface instead of a console print statement.

Rather than importing `java.util.Stack` or `LinkedList`, the project implements its own `CustomStack`, `CustomQueue`, `CustomPriorityQueue`, and `CustomLinkedList` from scratch, then wires them directly into restaurant operations: the waiting list is a real FIFO queue, the kitchen prioritizes VIP orders with a real priority queue, and "Undo" pops a real stack of past actions. A dedicated **Learn DSA** section then explains each structure with diagrams, time complexities, and "why we used this here" callouts.

---

## ✨ Features

### 🏪 Restaurant Operations
- 🏠 Live dashboard with real-time stats (tables, waiting customers, pending orders)
- 🍽️ Table management with fixed-size array allocation and instant occupancy status
- 🙋 Customer waiting list served strictly in arrival order
- 🧾 Order-taking flow with menu browsing and cart building
- 👨‍🍳 Kitchen view that prioritizes VIP/urgent orders ahead of standard FIFO orders
- 📜 Completed order history, most recent first
- ↩️ Undo system that reverses the last action taken (seat, free table, place order, etc.)

### 🧠 DSA Playground
- 🔍 Interactive **Search** panel: Linear vs. Binary Search, step-by-step
- 🔃 Interactive **Sort** panel: Bubble, Selection, and Insertion Sort, step-by-step
- 📚 **Learn DSA** section covering Arrays, Linked Lists, Stacks, Queues, Priority Queues, Searching, Sorting, BSTs, and Graphs: each with diagrams, complexities, and real-world mapping back to FoodFlow's own code

### 🎨 UI
- Soft pastel restaurant-themed interface built entirely with Java Swing
- Sidebar navigation with card-layout content switching
- Custom rounded panels, buttons, and badges (no external UI framework)

---

## 🧩 Data Structures → Feature Map

| Data Structure | Custom Class | Used For |
|---|---|---|
| **Array** | `Table[]` | Fixed dining tables, O(1) index access |
| **Queue (FIFO)** | `CustomQueue<Customer>` | Waiting list — first come, first seated |
| **Queue (FIFO)** | `CustomQueue<Order>` | Standard kitchen orders, cooked in order placed |
| **Priority Queue** | `CustomPriorityQueue<Order>` | VIP (1) / Urgent (2) / Normal (3) kitchen prioritization |
| **Stack (LIFO)** | `CustomStack<Action>` | Undo system — reverses the most recent action first |
| **Stack (LIFO)** | `CustomStack<Order>` | Completed order history, latest on top |
| **Linked List** | `CustomLinkedList` | Underlying node-based structure behind the custom Queue/Stack |
| **Sorting** | `Sorting.java` | Bubble, Selection, and Insertion Sort on menu items/orders |
| **Searching** | `Searching.java` | Linear and Binary Search across menu/customers/orders |

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| **Language** | Java |
| **GUI** | Java Swing (Nimbus Look & Feel) |
| **Architecture** | MVC-style — `model/`, `ui/`, `service/`, `dsa/` |
| **IDE** | IntelliJ IDEA |

---

## 📂 Project Structure

```text
FoodFlow/
│
├── src/
│   ├── model/              # Data models
│   │   ├── Customer.java
│   │   ├── MenuItem.java
│   │   ├── Order.java
│   │   ├── Table.java
│   │   └── Action.java
│   │
│   ├── dsa/                 # Hand-built data structures & algorithms
│   │   ├── CustomLinkedList.java
│   │   ├── CustomStack.java
│   │   ├── CustomQueue.java
│   │   ├── CustomPriorityQueue.java
│   │   ├── Sorting.java
│   │   └── Searching.java
│   │
│   ├── service/
│   │   └── RestaurantManager.java   # Central state manager wiring DSA structures to app logic
│   │
│   ├── ui/                  # Swing panels (one per feature/screen)
│   │   ├── MainFrame.java
│   │   ├── DashboardPanel.java
│   │   ├── TablePanel.java
│   │   ├── CustomerPanel.java
│   │   ├── MenuPanel.java
│   │   ├── OrderPanel.java
│   │   ├── KitchenPanel.java
│   │   ├── HistoryPanel.java
│   │   ├── UndoPanel.java
│   │   ├── SearchPanel.java
│   │   ├── SortPanel.java
│   │   ├── LearnDSAPanel.java
│   │   └── UIUtils.java
│   │
│   ├── test/
│   │   └── DSATester.java
│   │
│   └── Main.java             # Application entry point
│
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites
- [JDK 8+](https://www.oracle.com/java/technologies/downloads/) installed and on your `PATH`
- IntelliJ IDEA (recommended) or any Java-capable IDE, or just the command line

### Run from an IDE
1. Open the `FoodFlow/` folder as a project in IntelliJ IDEA.
2. Let it index the `src/` folder as a source root.
3. Run `Main.java`.

### Run from the command line
```bash
cd FoodFlow/src
javac -d ../bin $(find . -name "*.java")
java -cp ../bin Main
```

The app launches as a desktop window with a sidebar for navigating between the Dashboard, Tables, Customers, Menu, Orders, Kitchen, History, Undo, Search, Sort, and Learn DSA sections.

---

## 🖼️ Screenshots 

| Dashboard | Kitchen View |
|---|---|
| <img src = ss1.png>  | <img src = ss2.png> |

| Learn DSA Panel | Sorting Playground |
|---|---|
| <img src = ss4.png> | <img src = ss3.png> |

**A lot more can be found in our project**

---

## 🎯 Key Highlights

- Zero reliance on `java.util.Stack`/`Queue`/`LinkedList` — every structure built from nodes up
- Real operations (seating, ordering, cooking) mapped 1:1 onto textbook DSA structures
- Step-by-step visualizers for sorting and searching, not just final results
- Built-in "Learn DSA" reference section usable for study or live demonstration
- Custom Swing UI kit (rounded panels/buttons/badges) with no external design library

---

## 📚 What We Learned

- Implementing Stack, Queue, Priority Queue, and Linked List from first principles
- Applying FIFO/LIFO/priority ordering to a real, non-trivial domain problem
- Trade-offs between array-based and node-based structures (fixed tables vs. dynamic queues)
- Writing sorting/searching algorithms with step-tracking for visualization
- Structuring a desktop app with a clear model/service/UI separation
- Building a custom look and feel in Java Swing without external UI libraries

---

## 🗺️ Future Improvements

- [ ] Persist data (menu, orders, history) to a file or database instead of in-memory state
- [ ] Add a Binary Search Tree-backed menu catalog for category browsing
- [ ] Add a Graph-based table/seating layout visualizer

---

## 👥 Team

FoodFlow was developed collaboratively as an academic Data Structures & Algorithms project.

| Member | Role / Contribution |
|---|---|
| Kritika Basel | Custom DSA implementations (Stack, Queue, Priority Queue, Sorting & Searching) |
| Sabin Paudel | UI: Dashboard, Table & Customer panels|
| Smarika Mainali| UI: Kitchen, Order & History panels |
| Stuti Adhikari | Learn DSA content |

---

<div align="center">

### ⭐ If you found this project interesting, consider giving it a star!

</div>
