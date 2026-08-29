package dsa;

import model.MenuItem;
import model.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Sorting.java
 * 
 * Contains custom implementations of classic Sorting Algorithms:
 * 1. Bubble Sort - Compares adjacent elements and swaps them if in wrong order.
 * 2. Selection Sort - Repeatedly finds the minimum element and places it at the sorted front.
 * 3. Insertion Sort - Builds sorted array one item at a time.
 * 
 * Includes step logging for interactive UI demonstrations and viva explanations!
 */
public class Sorting {

    public static class SortResult<T> {
        private List<T> sortedList;
        private int comparisons;
        private int swaps;
        private List<String> logSteps;
        private String algorithmName;

        public SortResult(List<T> sortedList, int comparisons, int swaps, List<String> logSteps, String algorithmName) {
            this.sortedList = sortedList;
            this.comparisons = comparisons;
            this.swaps = swaps;
            this.logSteps = logSteps;
            this.algorithmName = algorithmName;
        }

        public List<T> getSortedList() { return sortedList; }
        public int getComparisons() { return comparisons; }
        public int getSwaps() { return swaps; }
        public List<String> getLogSteps() { return logSteps; }
        public String getAlgorithmName() { return algorithmName; }
    }

    /**
     * Bubble Sort on Menu Items by Price (Ascending: Low to High)
     * Time Complexity: Best O(n), Average O(n²), Worst O(n²)
     */
    public static SortResult<MenuItem> bubbleSortMenuByPrice(List<MenuItem> originalList, boolean ascending) {
        List<MenuItem> list = new ArrayList<>(originalList);
        List<String> steps = new ArrayList<>();
        int n = list.size();
        int comparisons = 0;
        int swaps = 0;

        steps.add(" Starting Bubble Sort by Price (" + (ascending ? "Low ➔ High" : "High ➔ Low") + ")");

        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            steps.add(String.format("--- Pass %d ---", (i + 1)));

            for (int j = 0; j < n - i - 1; j++) {
                comparisons++;
                MenuItem a = list.get(j);
                MenuItem b = list.get(j + 1);

                boolean shouldSwap = ascending ? (a.getPrice() > b.getPrice()) : (a.getPrice() < b.getPrice());

                if (shouldSwap) {
                    // Swap adjacent elements
                    list.set(j, b);
                    list.set(j + 1, a);
                    swaps++;
                    swapped = true;
                    steps.add(String.format("  Swap: %s ($%.2f) ⇄ %s ($%.2f)", a.getName(), a.getPrice(), b.getName(), b.getPrice()));
                }
            }

            // Optimized bubble sort early exit
            if (!swapped) {
                steps.add("⚡ No swaps occurred in pass " + (i + 1) + ". List is already sorted!");
                break;
            }
        }

        steps.add(String.format("Sorted successfully in %d comparisons and %d swaps.", comparisons, swaps));
        return new SortResult<>(list, comparisons, swaps, steps, "Bubble Sort");
    }

    /**
     * Bubble Sort on Menu Items by Name (Alphabetical)
     */
    public static SortResult<MenuItem> bubbleSortMenuByName(List<MenuItem> originalList) {
        List<MenuItem> list = new ArrayList<>(originalList);
        List<String> steps = new ArrayList<>();
        int n = list.size();
        int comparisons = 0;
        int swaps = 0;

        steps.add(" Starting Bubble Sort Alphabetically (A ➔ Z)");

        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                comparisons++;
                MenuItem a = list.get(j);
                MenuItem b = list.get(j + 1);

                if (a.getName().compareToIgnoreCase(b.getName()) > 0) {
                    list.set(j, b);
                    list.set(j + 1, a);
                    swaps++;
                    swapped = true;
                    steps.add(String.format("  Swap: \"%s\" ⇄ \"%s\"", a.getName(), b.getName()));
                }
            }
            if (!swapped) break;
        }

        steps.add(String.format(" Alphabetical sorting finished with %d comparisons and %d swaps.", comparisons, swaps));
        return new SortResult<>(list, comparisons, swaps, steps, "Bubble Sort (Name)");
    }

    /**
     * Selection Sort on Menu Items by Price
     * Time Complexity: O(n²) in all cases
     */
    public static SortResult<MenuItem> selectionSortMenuByPrice(List<MenuItem> originalList, boolean ascending) {
        List<MenuItem> list = new ArrayList<>(originalList);
        List<String> steps = new ArrayList<>();
        int n = list.size();
        int comparisons = 0;
        int swaps = 0;

        steps.add(" Starting Selection Sort by Price");

        for (int i = 0; i < n - 1; i++) {
            int targetIdx = i;
            for (int j = i + 1; j < n; j++) {
                comparisons++;
                boolean isBetter = ascending ? 
                        (list.get(j).getPrice() < list.get(targetIdx).getPrice()) : 
                        (list.get(j).getPrice() > list.get(targetIdx).getPrice());
                if (isBetter) {
                    targetIdx = j;
                }
            }

            if (targetIdx != i) {
                MenuItem temp = list.get(i);
                list.set(i, list.get(targetIdx));
                list.set(targetIdx, temp);
                swaps++;
                steps.add(String.format("Step %d: Selected min item \"%s\" ($%.2f) and placed at index %d",
                        i + 1, list.get(i).getName(), list.get(i).getPrice(), i));
            }
        }

        steps.add(String.format(" Selection Sort complete: %d comparisons, %d swaps.", comparisons, swaps));
        return new SortResult<>(list, comparisons, swaps, steps, "Selection Sort");
    }

    /**
     * Insertion Sort on Orders by Order ID (Ascending)
     */
    public static SortResult<Order> insertionSortOrdersById(List<Order> originalList) {
        List<Order> list = new ArrayList<>(originalList);
        List<String> steps = new ArrayList<>();
        int n = list.size();
        int comparisons = 0;
        int shifts = 0;

        steps.add(" Starting Insertion Sort on Orders by ID");

        for (int i = 1; i < n; i++) {
            Order key = list.get(i);
            int j = i - 1;

            steps.add("Inserting Order #" + key.getOrderId() + " into sorted prefix [0.." + j + "]");

            while (j >= 0) {
                comparisons++;
                if (list.get(j).getOrderId() > key.getOrderId()) {
                    list.set(j + 1, list.get(j));
                    shifts++;
                    j = j - 1;
                } else {
                    break;
                }
            }
            list.set(j + 1, key);
        }

        steps.add(" Insertion Sort finished with " + comparisons + " comparisons and " + shifts + " shifts.");
        return new SortResult<>(list, comparisons, shifts, steps, "Insertion Sort");
    }
}
