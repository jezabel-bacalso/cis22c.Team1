import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class PriorityQueue {
    private int heapSize;
    private ArrayList<Order> heap;
    private Comparator<Order> cmp;

/**
     * Constructor initializes the priority queue with a comparator.
     */
    public PriorityQueue(Comparator<Order> cmp) {
        this.heapSize = 0;
        this.cmp = cmp;
        this.heap = new ArrayList<>();
        heap.add(null); // Unused index 0 for simpler calculations
    }

/**
     * Custom Comparator for sorting Orders by priority.
     */
    private static class OrderComparator implements Comparator<Order> {
        @Override
        public int compare(Order o1, Order o2) {
            return Integer.compare(o1.getPriority(), o2.getPriority());
        }
    }
    /**
     * Inserts an order into the priority queue.
     */
    public void insert(Order order) {
        heapSize++;
        heap.add(order);
        heapIncreaseKey(heapSize); // Bubble up to maintain heap order
    }

    /**
     * Removes and returns the highest-priority order (Order to be shipped).
     */
    public Order remove() {
        if (heapSize == 0) {
            throw new NoSuchElementException("No orders in queue.");
        }

        Order highestPriorityOrder = heap.get(1); // Get root order
        heap.set(1, heap.get(heapSize)); // Move last order to root
        heap.remove(heapSize); // Remove last order
        heapSize--;
        heapify(1); // Restore heap order

        return highestPriorityOrder;
    }

    /**
     * Returns the highest-priority order without removing it.
     */
    public Order getHighestPriorityOrder() {
        if (heapSize == 0) {
            throw new NoSuchElementException("No orders in queue.");
        }
        return heap.get(1);
    }

    /**
     * Searches for an order by ID.
     */
    public Order searchOrderById(int orderId) {
        for (Order order : heap) {
            if (order != null && order.getOrderId() == orderId) {
                return order;
            }
        }
        return null; // Not found
    }

    /**
     * Heapify - Ensures heap property is maintained.
     */
    private void heapify(int index) {
        int left = getLeft(index);
        int right = getRight(index);
        int highestPriority = index;

        if (left <= heapSize && cmp.compare(heap.get(left), heap.get(index)) > 0) {
            highestPriority = left;
        }
        if (right <= heapSize && cmp.compare(heap.get(right), heap.get(highestPriority)) > 0) {
            highestPriority = right;
        }
        if (highestPriority != index) {
            swap(index, highestPriority);
            heapify(highestPriority);
        }
    }

    /**
     * Bubbles up an order to maintain heap priority.
     */
    private void heapIncreaseKey(int index) {
        while (index > 1 && cmp.compare(heap.get(getParent(index)), heap.get(index)) < 0) {
            swap(index, getParent(index));
            index = getParent(index);
        }
    }

    /**
     * Swaps two elements in the priority queue.
     */
    private void swap(int i, int j) {
        Order temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    /** Helper Methods **/

    public int getLeft(int index) { return 2 * index; }
    public int getRight(int index) { return 2 * index + 1; }
    public int getParent(int index) { return index / 2; }
    public boolean isEmpty() { return heapSize == 0; }

    /**
     * Returns all orders sorted by priority (Heap Sort).
     */
    public ArrayList<Order> getSortedOrders() {
        ArrayList<Order> sortedOrders = new ArrayList<>(heap);
        sortedOrders.remove(0); // Remove null placeholder
        sortedOrders.sort(cmp);
        return sortedOrders;
    }
}
