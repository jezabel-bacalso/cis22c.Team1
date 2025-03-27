import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

// A priority queue implementation using a binary heap stored in an ArrayList.
// The heap uses 1-based indexing for easier parent/child calculations.
public class PriorityQueue {

    // The heap array where index 0 is unused (dummy), so elements start at index 1.
    private ArrayList<Order> heap;
    // The current number of orders in the heap.
    private int size;

    /**
     * Constructs an empty PriorityQueue.
     */
    public PriorityQueue() {
        this.heap = new ArrayList<>();
        // Add a dummy element at index 0 to simplify index calculations.
        heap.add(null);
        this.size = 0;
    }

    /**
     * Checks whether the priority queue is empty.
     *
     * @return true if the queue is empty, false otherwise.
     */
    public boolean isEmpty() {
        return (size == 0);
    }

    /**
     * Inserts an order into the priority queue.
     * After adding, the heap property is restored by bubbling up.
     *
     * @param order the Order to insert.
     */
    public void insert(Order order) {
        size++;
        // Add the new order at the end of the heap.
        heap.add(order);
        // Restore heap property by bubbling up the new order.
        bubbleUp(size);
    }

    /**
     * Removes and returns the order with the highest priority (at the top of the heap).
     *
     * @return the Order with the highest priority.
     * @throws NoSuchElementException if the queue is empty.
     */
    public Order remove() {
        if (isEmpty()) {
            throw new NoSuchElementException("PriorityQueue is empty");
        }
        // The highest priority order is at index 1.
        Order top = heap.get(1);
        // Move the last element to the top.
        heap.set(1, heap.get(size));
        // Remove the last element since it has been moved.
        heap.remove(size);
        size--;
        // Restore the heap property if the queue is not empty.
        if (size > 0) {
            heapify(1);
        }
        return top;
    }

    /**
     * Returns the order with the highest priority without removing it.
     *
     * @return the Order at the top of the heap.
     * @throws NoSuchElementException if the queue is empty.
     */
    public Order peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("PriorityQueue is empty");
        }
        return heap.get(1);
    }

    /**
     * Returns a list of all orders in descending priority order.
     *
     * @return a sorted List of Orders with highest priority first.
     */
    public List<Order> getAllOrdersSorted() {
        // Create a copy of the heap (excluding the dummy element).
        ArrayList<Order> copy = new ArrayList<>(heap.subList(1, size + 1));
        // Sort the list using the Order.compareTo method so that highest priority comes first.
        copy.sort((o1, o2) -> o1.compareTo(o2));
        return copy;
    }

    /**
     * Searches for an unshipped order by its ID.
     *
     * @param orderId the ID of the order to search for.
     * @return the Order if found, or null if not found.
     */
    public Order searchById(String orderId) {
        for (int i = 1; i <= size; i++) {
            if (heap.get(i).getId().equals(orderId)) {
                return heap.get(i);
            }
        }
        return null;
    }

    /**
     * Searches for unshipped orders by customer email.
     *
     * @param email the customer's email.
     * @return a list of Orders that match the customer's email.
     */
    public List<Order> searchByCustomerEmail(String email) {
        List<Order> results = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            if (heap.get(i).getCustomerId().equalsIgnoreCase(email)) {
                results.add(heap.get(i));
            }
        }
        return results;
    }

    /**
     * Restores the heap property by bubbling up the element at the given index.
     *
     * @param idx the index of the element to bubble up.
     */
    private void bubbleUp(int idx) {
        while (idx > 1) {
            int parent = idx / 2;
            // If current order has higher priority than its parent, swap them.
            if (heap.get(idx).compareTo(heap.get(parent)) > 0) {
                swap(idx, parent);
                idx = parent;
            } else {
                break;
            }
        }
    }

    /**
     * Restores the heap property by moving the element at the given index down.
     *
     * @param idx the index of the element to heapify.
     */
    private void heapify(int idx) {
        int left = 2 * idx;
        int right = 2 * idx + 1;
        int largest = idx;
        // Check if the left child has higher priority.
        if (left <= size && heap.get(left).compareTo(heap.get(largest)) > 0) {
            largest = left;
        }
        // Check if the right child has higher priority.
        if (right <= size && heap.get(right).compareTo(heap.get(largest)) > 0) {
            largest = right;
        }
        // If a child has higher priority, swap and continue heapifying.
        if (largest != idx) {
            swap(idx, largest);
            heapify(largest);
        }
    }

    /**
     * Swaps two elements in the heap given their indices.
     *
     * @param i index of the first element.
     * @param j index of the second element.
     */
    private void swap(int i, int j) {
        Order temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
}
