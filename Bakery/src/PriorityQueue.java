import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class PriorityQueue {

    private ArrayList<Order> heap; // 1-based indexing
    private int size;

    public PriorityQueue() {
        this.heap = new ArrayList<>();
        // dummy at index 0
        heap.add(null);
        this.size = 0;
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    public void insert(Order order) {
        size++;
        heap.add(order);
        bubbleUp(size);
    }

    public Order remove() {
        if (isEmpty()) {
            throw new NoSuchElementException("PriorityQueue is empty");
        }
        Order top = heap.get(1);
        heap.set(1, heap.get(size));
        heap.remove(size);
        size--;
        if (size > 0) {
            heapify(1);
        }
        return top;
    }

    public Order peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("PriorityQueue is empty");
        }
        return heap.get(1);
    }

    // Return a list of all orders in descending priority
    public List<Order> getAllOrdersSorted() {
        ArrayList<Order> copy = new ArrayList<>(heap.subList(1, size+1));
        // Sort by compareTo => highest first
        copy.sort((o1, o2) -> o1.compareTo(o2));
        return copy;
    }

    // Search for an unshipped order by ID
    public Order searchById(String orderId) {
        for (int i = 1; i <= size; i++) {
            if (heap.get(i).getId().equals(orderId)) {
                return heap.get(i);
            }
        }
        return null;
    }

    // Search for unshipped orders by customer email
    public List<Order> searchByCustomerEmail(String email) {
        List<Order> results = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            if (heap.get(i).getCustomerId().equalsIgnoreCase(email)) {
                results.add(heap.get(i));
            }
        }
        return results;
    }

    private void bubbleUp(int idx) {
        while (idx > 1) {
            int parent = idx / 2;
            if (heap.get(idx).compareTo(heap.get(parent)) > 0) {
                swap(idx, parent);
                idx = parent;
            } else {
                break;
            }
        }
    }

    private void heapify(int idx) {
        int left = 2*idx;
        int right = 2*idx + 1;
        int largest = idx;
        if (left <= size && heap.get(left).compareTo(heap.get(largest)) > 0) {
            largest = left;
        }
        if (right <= size && heap.get(right).compareTo(heap.get(largest)) > 0) {
            largest = right;
        }
        if (largest != idx) {
            swap(idx, largest);
            heapify(largest);
        }
    }

    private void swap(int i, int j) {
        Order temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
}
