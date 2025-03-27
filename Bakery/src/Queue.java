import java.util.NoSuchElementException;

// A generic queue implementation using a linked list.
public class Queue<T> {

    // Private static inner class representing a node in the queue.
    private static class Node<T> {
        T data;         // The data stored in the node.
        Node<T> next;   // Reference to the next node in the queue.

        // Constructs a new node with the given data.
        public Node(T data) {
            this.data = data;
        }
    }

    // Pointers to the front and rear nodes of the queue.
    private Node<T> front;
    private Node<T> rear;

    // Constructs an empty queue.
    public Queue() {
        front = null;
        rear = null;
    }

    /**
     * Checks if the queue is empty.
     *
     * @return true if there are no elements in the queue; false otherwise.
     */
    public boolean isEmpty() {
        return front == null;
    }

    /**
     * Adds a new element to the rear of the queue.
     *
     * @param data the element to be added.
     */
    public void enqueue(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            // If the queue is empty, new node becomes both front and rear.
            front = newNode;
            rear = newNode;
        } else {
            // Otherwise, link the new node at the end and update the rear pointer.
            rear.next = newNode;
            rear = newNode;
        }
    }

    /**
     * Retrieves the front element of the queue without removing it.
     *
     * @return the element at the front of the queue.
     * @throws NoSuchElementException if the queue is empty.
     */
    public T getFront() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return front.data;
    }

    /**
     * Removes and returns the front element of the queue.
     *
     * @return the element removed from the front of the queue.
     * @throws NoSuchElementException if the queue is empty.
     */
    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        T data = front.data;
        // Move the front pointer to the next node.
        front = front.next;
        // If the queue becomes empty, reset the rear pointer as well.
        if (front == null) {
            rear = null;
        }
        return data;
    }
}
