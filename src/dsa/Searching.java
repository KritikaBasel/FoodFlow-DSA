package dsa;

import model.Customer;
import model.MenuItem;
import model.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Searching.java
 * 
 * Contains custom implementations of Searching Algorithms:
 * 1. Linear Search - Checks elements sequentially one by one.
 * 2. Binary Search - Divide and conquer on a sorted dataset.
 * 
 * Also includes step tracking for UI live visualization and viva explanation!
 */
public class Searching {

    public static class SearchResult<T> {
        private T foundItem;
        private int index;
        private int comparisons;
        private List<String> steps;

        public SearchResult(T foundItem, int index, int comparisons, List<String> steps) {
            this.foundItem = foundItem;
            this.index = index;
            this.comparisons = comparisons;
            this.steps = steps;
        }

        public T getFoundItem() { return foundItem; }
        public int getIndex() { return index; }
        public int getComparisons() { return comparisons; }
        public List<String> getSteps() { return steps; }
        public boolean isFound() { return foundItem != null; }
    }

    /**
     * Linear Search on Menu Items by Name (case-insensitive)
     * Time Complexity: Best O(1), Average O(n), Worst O(n)
     */
    public static SearchResult<MenuItem> linearSearchMenuByName(List<MenuItem> list, String targetName) {
        List<String> steps = new ArrayList<>();
        int comparisons = 0;

        if (list == null || targetName == null || targetName.trim().isEmpty()) {
            steps.add("Search query is empty.");
            return new SearchResult<>(null, -1, 0, steps);
        }

        String search = targetName.trim().toLowerCase();
        steps.add(" Starting Linear Search for menu item: \"" + targetName + "\" across " + list.size() + " items.");

        for (int i = 0; i < list.size(); i++) {
            comparisons++;
            MenuItem item = list.get(i);
            steps.add("Step " + comparisons + ": Checking index [" + i + "] ➔ \"" + item.getName() + "\"");

            if (item.getName().toLowerCase().contains(search)) {
                steps.add(" MATCH FOUND! \"" + item.getName() + "\" found at index " + i + " after " + comparisons + " comparisons.");
                return new SearchResult<>(item, i, comparisons, steps);
            }
        }

        steps.add(" Item \"" + targetName + "\" not found after " + comparisons + " comparisons.");
        return new SearchResult<>(null, -1, comparisons, steps);
    }

    /**
     * Linear Search on Orders by Order ID
     * Time Complexity: Best O(1), Average O(n), Worst O(n)
     */
    public static SearchResult<Order> linearSearchOrderById(List<Order> list, int targetOrderId) {
        List<String> steps = new ArrayList<>();
        int comparisons = 0;

        steps.add(" Starting Linear Search for Order #" + targetOrderId + " across " + list.size() + " orders.");

        for (int i = 0; i < list.size(); i++) {
            comparisons++;
            Order order = list.get(i);
            steps.add("Step " + comparisons + ": Checking Order #" + order.getOrderId() + " (" + order.getCustomerName() + ")");

            if (order.getOrderId() == targetOrderId) {
                steps.add(" FOUND Order #" + targetOrderId + " at index " + i + " in " + comparisons + " comparisons!");
                return new SearchResult<>(order, i, comparisons, steps);
            }
        }

        steps.add(" Order #" + targetOrderId + " not found after " + comparisons + " comparisons.");
        return new SearchResult<>(null, -1, comparisons, steps);
    }

    /**
     * Linear Search for Customers by Name
     */
    public static SearchResult<Customer> linearSearchCustomerByName(List<Customer> list, String name) {
        List<String> steps = new ArrayList<>();
        int comparisons = 0;
        String query = name.trim().toLowerCase();

        steps.add(" Starting Linear Search for customer: \"" + name + "\"");
        for (int i = 0; i < list.size(); i++) {
            comparisons++;
            Customer customer = list.get(i);
            steps.add("Step " + comparisons + ": Checking Customer \"" + customer.getName() + "\"");

            if (customer.getName().toLowerCase().contains(query)) {
                steps.add(" FOUND Customer \"" + customer.getName() + "\" at position " + i + "!");
                return new SearchResult<>(customer, i, comparisons, steps);
            }
        }

        steps.add(" Customer \"" + name + "\" not found.");
        return new SearchResult<>(null, -1, comparisons, steps);
    }

    /**
     * Binary Search on Menu Items sorted by ID
     * Note: Data MUST be sorted by ID prior to searching!
     * Time Complexity: Best O(1), Average O(log n), Worst O(log n)
     */
    public static SearchResult<MenuItem> binarySearchMenuById(List<MenuItem> sortedList, int targetId) {
        List<String> steps = new ArrayList<>();
        int comparisons = 0;

        steps.add(" Starting Binary Search for Menu Item ID #" + targetId + " in sorted list of " + sortedList.size() + " items.");

        int low = 0;
        int high = sortedList.size() - 1;

        while (low <= high) {
            comparisons++;
            int mid = low + (high - low) / 2;
            MenuItem midItem = sortedList.get(mid);

            steps.add(String.format("Step %d: Range [%d..%d], Middle index [%d] has ID #%d (%s)",
                    comparisons, low, high, mid, midItem.getId(), midItem.getName()));

            if (midItem.getId() == targetId) {
                steps.add(" FOUND! Item #" + targetId + " located at index " + mid + " in " + comparisons + " steps!");
                return new SearchResult<>(midItem, mid, comparisons, steps);
            } else if (midItem.getId() < targetId) {
                steps.add("  ➜ Target " + targetId + " is GREATER than " + midItem.getId() + ". Searching RIGHT half [" + (mid + 1) + ".." + high + "].");
                low = mid + 1;
            } else {
                steps.add("  ➜ Target " + targetId + " is LESS than " + midItem.getId() + ". Searching LEFT half [" + low + ".." + (mid - 1) + "].");
                high = mid - 1;
            }
        }

        steps.add(" Item ID #" + targetId + " not found after " + comparisons + " comparisons.");
        return new SearchResult<>(null, -1, comparisons, steps);
    }
}
