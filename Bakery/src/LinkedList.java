import java.util.NoSuchElementException;

// A generic doubly linked list implementation with an internal iterator.
// The list supports basic operations such as adding, removing, and searching.
public class LinkedList<T> {

    // Private inner class representing a node in the list.
    private class Node {
        T data;
        Node next;
        Node prev;
        
        // Constructs a node with the specified data.
        public Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    // References to the first and last nodes of the list.
    private Node first;
    private Node last;
    // The iterator reference used for traversals.
    private Node iterator;
    // Number of elements in the list.
    private int length;

    // Constructs an empty linked list.
    public LinkedList() {
        first = null;
        last = null;
        iterator = null;
        length = 0;
    }

    /**
     * Checks whether the list is empty.
     * @return true if the list has no elements, false otherwise.
     */
    public boolean isEmpty() {
        return (length == 0);
    }

    /**
     * Returns the number of elements in the list.
     * @return the length of the list.
     */
    public int getLength() {
        return length;
    }

    /**
     * Adds an element to the beginning of the list.
     * @param data the data to add.
     */
    public void addFirst(T data) {
        Node newNode = new Node(data);
        if (isEmpty()) {
            // When list is empty, newNode becomes both first and last.
            first = last = newNode;
        } else {
            // Link new node in front of current first node.
            newNode.next = first;
            first.prev = newNode;
            first = newNode;
        }
        length++;
    }

    /**
     * Adds an element to the end of the list.
     * @param data the data to add.
     */
    public void addLast(T data) {
        Node newNode = new Node(data);
        if (isEmpty()) {
            // When list is empty, newNode becomes both first and last.
            first = last = newNode;
        } else {
            // Link new node after current last node.
            last.next = newNode;
            newNode.prev = last;
            last = newNode;
        }
        length++;
    }

    /**
     * Retrieves the first element of the list.
     * @return the data of the first node.
     * @throws NoSuchElementException if the list is empty.
     */
    public T getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        return first.data;
    }

    /**
     * Retrieves the last element of the list.
     * @return the data of the last node.
     * @throws NoSuchElementException if the list is empty.
     */
    public T getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        return last.data;
    }

    /**
     * Removes and returns the first element of the list.
     * @return the data removed.
     * @throws NoSuchElementException if the list is empty.
     */
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        T temp = first.data;
        if (length == 1) {
            // List becomes empty after removal.
            first = last = null;
        } else {
            // Move first pointer to next node.
            first = first.next;
            first.prev = null;
        }
        // Reset iterator if it was pointing to the removed node.
        if (iterator == first) {
            iterator = null;
        }
        length--;
        return temp;
    }

    /**
     * Removes and returns the last element of the list.
     * @return the data removed.
     * @throws NoSuchElementException if the list is empty.
     */
    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        T temp = last.data;
        if (length == 1) {
            // List becomes empty after removal.
            first = last = null;
        } else {
            // Move last pointer to previous node.
            last = last.prev;
            last.next = null;
        }
        // Reset iterator if it was pointing to the removed node.
        if (iterator == last) {
            iterator = null;
        }
        length--;
        return temp;
    }

    // Iterator methods

    /**
     * Positions the iterator at the beginning of the list.
     */
    public void positionIterator() {
        iterator = first;
    }

    /**
     * Checks if the iterator is off the end of the list.
     * @return true if the iterator is null, false otherwise.
     */
    public boolean offEnd() {
        return (iterator == null);
    }

    /**
     * Advances the iterator to the next node.
     * @throws NoSuchElementException if the iterator is off the end.
     */
    public void advanceIterator() {
        if (offEnd()) {
            throw new NoSuchElementException("Iterator is off end.");
        }
        iterator = iterator.next;
    }

    /**
     * Moves the iterator to the previous node.
     * @throws NoSuchElementException if the iterator is off the end.
     */
    public void reverseIterator() {
        if (offEnd()) {
            throw new NoSuchElementException("Iterator is off end.");
        }
        iterator = iterator.prev;
    }

    /**
     * Retrieves the data at the current iterator position.
     * @return the data pointed to by the iterator.
     * @throws NoSuchElementException if the iterator is off the end.
     */
    public T getIterator() {
        if (offEnd()) {
            throw new NoSuchElementException("Iterator is off end.");
        }
        return iterator.data;
    }

    /**
     * Removes the node at the current iterator position.
     * After removal, the iterator is set to null.
     * @throws NoSuchElementException if the iterator is off the end.
     */
    public void removeIterator() {
        if (offEnd()) {
            throw new NoSuchElementException("Iterator is off end.");
        }
        if (iterator == first) {
            removeFirst();
        } else if (iterator == last) {
            removeLast();
        } else {
            // Unlink the iterator node from the list.
            Node temp = iterator;
            temp.prev.next = temp.next;
            temp.next.prev = temp.prev;
            length--;
            // Reset iterator after removal.
            iterator = null;
        }
    }

    /**
     * Clears the entire list.
     */
    public void clear() {
        first = last = iterator = null;
        length = 0;
    }

    /**
     * Finds the index of the first occurrence of the specified data.
     * @param data the data to find.
     * @return the index of the data, or -1 if not found.
     */
    public int findIndex(T data) {
        Node current = first;
        int index = 0;
        while (current != null) {
            if (current.data.equals(data)) {
                return index;
            }
            current = current.next;
            index++;
        }
        return -1;
    }

    /**
     * Returns a string representation of the list.
     * @return a space-separated list of element data.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node current = first;
        while (current != null) {
            sb.append(current.data).append(" ");
            current = current.next;
        }
        return sb.toString().trim();
    }

    /**
     * Checks if two LinkedList objects are equal by comparing their sizes
     * and the data in each corresponding node.
     * @param obj the object to compare.
     * @return true if the lists are equal, false otherwise.
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof LinkedList)) return false;
        LinkedList<T> other = (LinkedList<T>) obj;
        if (this.length != other.length) return false;
        Node c1 = this.first;
        Node c2 = other.first;
        while (c1 != null) {
            if (!c1.data.equals(c2.data)) {
                return false;
            }
            c1 = c1.next;
            c2 = c2.next;
        }
        return true;
    }
}
