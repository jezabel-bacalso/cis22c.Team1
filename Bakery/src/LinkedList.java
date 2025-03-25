// 3/27/2025
// LinkedList.Java

import java.util.*;import java.util.NoSuchElementException;

public class LinkedList<T> {
    private class Node {
        private T data;
        private Node next;
        private Node prev;

        public Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    private int length;
    private Node first;
    private Node last;
    private Node iterator;
    private int indexIterator;

    /**** CONSTRUCTORS ****/

    /**
     * Instantiates a new LinkedList with default values
     * @postcondition The LinkedList is initialized with length 0, and first, last, and iterator set to null.
     */
    public LinkedList() {
        length = 0;
        first = null;
        last = null;
        iterator = null;
        indexIterator = -1;
    }

    /**
     * Converts the given array into a LinkedList
     * @param array the array of values to insert into this LinkedList
     * @postcondition The LinkedList contains all elements from the array in the same order.
     */
    public LinkedList(T[] array) {
        for (T element : array) {
            addLast(element);
        }
    }

    /**
     * Instantiates a new LinkedList by copying another List
     * @param original the LinkedList to copy
     * @postcondition A new List object, which is an identical, but separate, copy of the LinkedList original.
     */
    public LinkedList(LinkedList<T> original) {
        if (original == null) {
            throw new NullPointerException("The original LinkedList is null.");
        }
        if (original.length == 0) {
            length = 0;
            first = null;
            last = null;
            iterator = null;
        } else {
            Node temp = original.first;
            while (temp != null) {
                addLast(temp.data);
                temp = temp.next;
            }
            iterator = null;
        }
    }

    /**** ACCESSORS ****/

    /**
     * Returns the value stored in the first node
     * @precondition The LinkedList is not empty.
     * @return the value stored at node first
     * @throws NoSuchElementException if the LinkedList is empty.
     */
    public T getFirst() throws NoSuchElementException {
        if (first == null) {
            throw new NoSuchElementException("getFirst: List is empty, no data to access.");
        }
        return first.data;
    }

    /**
     * Returns the value stored in the last node
     * @precondition The LinkedList is not empty.
     * @return the value stored in the node last
     * @throws NoSuchElementException if the LinkedList is empty.
     */
    public T getLast() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("getLast(): List is empty.");
        }
        return last.data;
    }

    /**
     * Returns the data stored in the iterator node
     * @precondition The iterator is not off end.
     * @return the data stored in the iterator node
     * @throws NullPointerException if the iterator is off end.
     */
    public T getIterator() throws NullPointerException {
        if (offEnd()) {
            throw new NullPointerException("getIterator(): Iterator is off end.");
        }
        return iterator.data;
    }

    /**
     * Returns the current length of the LinkedList
     * @return the length of the LinkedList from 0 to n
     */
    public int getLength() {
        return length;
    }

    /**
     * Returns whether the LinkedList is currently empty
     * @return whether the LinkedList is empty
     */
    public boolean isEmpty() {
        return length == 0;
    }

    /**
     * Returns whether the iterator is offEnd, i.e. null
     * @return whether the iterator is null
     */
    public boolean offEnd() {
        return iterator == null;
    }

    /**** MUTATORS ****/

    /**
     * Creates a new first element
     * @param data the data to insert at the front of the LinkedList
     * @postcondition The new node is added to the front of the LinkedList.
     */
    public void addFirst(T data) {
        if (first == null) {
            first = last = new Node(data);
        } else {
            Node N = new Node(data);
            N.next = first;
            first.prev = N;
            first = N;
        }
        length++;
    }

    /**
     * Creates a new last element
     * @param data the data to insert at the end of the LinkedList
     * @postcondition The new node is added to the end of the LinkedList.
     */
    public void addLast(T data) {
        if (last == null) {
            first = last = new Node(data);
        } else {
            Node N = new Node(data);
            last.next = N;
            N.prev = last;
            last = N;
        }
        length++;
    }

    /**
     * Inserts a new element after the iterator
     * @param data the data to insert
     * @precondition The iterator is not off end.
     * @throws NullPointerException if the iterator is off end.
     */
    public void addIterator(T data) throws NullPointerException {
        if (offEnd()) {
            throw new NullPointerException("addIterator(): Iterator is off end.");
        }
        else if (iterator == last) {
            addLast(data);
        } else {
            Node N = new Node(data);
            N.next = iterator.next;
            N.prev = iterator;
            iterator.next.prev = N;
            iterator.next = N;
            length++;
    }
    }

    /**
     * removes the element at the front of the LinkedList
     * @precondition The LinkedList is not empty.
     * @postcondition The first element is removed from the LinkedList.
     * @throws NoSuchElementException if the LinkedList is empty.
     */
    public void removeFirst() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("removeFirst(): List is empty.");
        }
        if (length == 1) {
            first = last = null;
        } else {
            first = first.next;
            first.prev = null;
        }
        length--;
    }

    /**
     * removes the element at the end of the LinkedList
     * @precondition The LinkedList is not empty.
     * @postcondition The last element is removed from the LinkedList.
     * @throws NoSuchElementException if the LinkedList is empty.
     */
    public void removeLast() throws NoSuchElementException {
        if (length == 0) {
            throw new NoSuchElementException("removeLast(): List is empty.");
        }
        else if (length == 1) {
            first = last = null;
        } else {
            last = last.prev;
            last.next = null;
        }
        length--;
    }

    /**
     * removes the element referenced by the iterator
     * @precondition The iterator is not off end.
     * @postcondition The element referenced by the iterator is removed from the LinkedList.
     * @throws NullPointerException.
     */
    public void removeIterator() throws NullPointerException {
        if (offEnd()) {
            throw new NullPointerException("removeIterator(): Iterator is off end.");
        }
          else if (iterator == first) {
            removeFirst();
        } else if (iterator == last) {
            removeLast();
        } else {
            iterator.prev.next = iterator.next;
            iterator.next.prev = iterator.prev;
            length--;
        }
        iterator = null;
        indexIterator = -1;
    }

    /**
     * places the iterator at the first node
     * @postcondition The iterator is positioned at the first node.
     */
    public void positionIterator() {
        iterator = first;
        indexIterator = 0;
    }

    /**
     * Moves the iterator one node towards the last
     * @precondition The iterator is not off end.
     * @postcondition The iterator is advanced to the next node.
     * @throws NullPointerException if the iterator is off end.
     */
    public void advanceIterator() throws NullPointerException {
        if (iterator == null) {
            throw new NullPointerException("advanceIterator(): Iterator is off end.");
        }
        iterator = iterator.next;
        indexIterator++;
    }

    /**
     * Moves the iterator one node towards the first
     * @precondition The iterator is not off end.
     * @postcondition The iterator is moved to the previous node.
     * @throws NullPointerException if the iterator is off end.
     */
    public void reverseIterator() throws NullPointerException {
        if (iterator == null) {
            throw new NullPointerException("reverseIterator(): Iterator is off end.");
        }
        iterator = iterator.prev;
        indexIterator--;
    }

    /**** ADDITIONAL OPERATIONS ****/

    /**
     * Re-sets LinkedList to empty as if the
     * default constructor had just been called
     */
    public void clear() {
        first = null;
        last = null;
        iterator = null;
        length = 0;
    }

    /**
     * Converts the LinkedList to a String, with each value separated by a
     * blank space. At the end of the String, place a new line character.
     * @return the LinkedList as a String
     */
    @Override
    public String toString() {
        String result = "";
        Node temp = first;
        while (temp != null) {
            result += temp.data + " ";
            temp = temp.next;
        }
        return result + "\n";
    }

    /**
     * Returns each element in the LinkedList along with its
     * numerical position from 1 to n, followed by a newline.
     * @return the numbered LinkedList elements as a String.
     */
    public String numberedListString() {
        if (isEmpty()) {
            return " (empty String)\\n\n";
        }

        StringBuilder result = new StringBuilder();
        Node current = first;
        int position = 1;

        while (current != null) {
            result.append(position).append(". ").append(current.data).append("\n");
            current = current.next;
            position++;
        }

        result.append("\n"); // Add an extra newline at the end
        return result.toString();
    }

    /**
     * Determines whether the given Object is
     * another LinkedList, containing
     * the same data in the same order
     * @param obj another Object
     * @return whether there is equality
     */
    @SuppressWarnings("unchecked") //good practice to remove warning here
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof LinkedList)) {
            return false;
        } else {
            LinkedList<T> L = (LinkedList<T>) obj;
            if (this.length != L.length) {
                return false;
            } else {
                Node temp1 = this.first;
                Node temp2 = L.first;
                while (temp1 != null) {
                    if (!(temp1.data.equals(temp2.data))) {
                        return false;
                    }
                    temp1 = temp1.next;
                    temp2 = temp2.next;
                }
                return true;
    }
        }
    }

    /**CHALLENGE METHODS*/

    /**
     * Moves all nodes in the list towards the end
     * of the list the number of times specified
     * Any node that falls off the end of the list as it
     * moves forward will be placed the front of the list
     * For example: [1, 2, 3, 4, 5], numMoves = 2 -> [4, 5, 1, 2 ,3]
     * For example: [1, 2, 3, 4, 5], numMoves = 4 -> [2, 3, 4, 5, 1]
     * For example: [1, 2, 3, 4, 5], numMoves = 7 -> [4, 5, 1, 2 ,3]
     * @param numMoves the number of times to move each node.
     * @precondition numMoves >= 0
     * @postcondition iterator position unchanged (i.e. still referencing
     * the same node in the list, regardless of new location of Node)
     * @throws IllegalArgumentException when numMoves < 0
     */
    public void spinList(int numMoves) throws IllegalArgumentException {
        if (numMoves < 0) {
            throw new IllegalArgumentException("numMoves must be >= 0");
        }
        if (isEmpty() || numMoves == 0) {
            return;
        }

        numMoves = numMoves % length; // Optimize the number of moves
        for (int i = 0; i < numMoves; i++) {
            Node temp = last;
            last = last.prev;
            last.next = null;
            temp.prev = null;
            temp.next = first;
            first.prev = temp;
            first = temp;
        }
    }

    /**
     * Splices together two LinkedLists to create a third List
     * which contains alternating values from this list
     * and the given parameter
     * For example: [1,2,3] and [4,5,6] -> [1,4,2,5,3,6]
     * For example: [1, 2, 3, 4] and [5, 6] -> [1, 5, 2, 6, 3, 4]
     * For example: [1, 2] and [3, 4, 5, 6] -> [1, 3, 2, 4, 5, 6]
     * @param list the second LinkedList
     * @return a new LinkedList, which is the result of
     * interlocking this and list
     * @postcondition this and list are unchanged
     */
    public LinkedList<T> altLists(LinkedList<T> list) {
        LinkedList<T> result = new LinkedList<>();
        Node currentThis = this.first;
        Node currentOther = list.first;

        while (currentThis != null || currentOther != null) {
            if (currentThis != null) {
                result.addLast(currentThis.data);
                currentThis = currentThis.next;
            }
            if (currentOther != null) {
                result.addLast(currentOther.data);
                currentOther = currentOther.next;
            }
        }

        return result;
    }

    /** MORE METHODS */

    /**
     * Searches the LinkedList for a given element's index.
     * @param data the data whose index to locate.
     * @return the index of the data or -1 if the data is not contained
     * in the LinkedList.
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
     * Advances the iterator to location within the LinkedList
     * specified by the given index.
     * @param index the index at which to place the iterator.
     * @precondition index >= 0, index < length
     * @throws IndexOutOfBoundsException when the index is out of bounds
     */
    public void advanceIteratorToIndex(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("Index is out of bounds.");
        }
        iterator = first;
        for (int i = 0; i < index; i++) {
            iterator = iterator.next;
        }
    }

    // Add the following methods for handling orders:

    /**
     * Adds an order to the linked list in sorted order.
     * @param order the order to add
     */
    public void addOrder(Order order) {
        Node newNode = new Node(order);
        if (first == null || ((Order) first.data).compareTo(order) > 0) {
            newNode.next = first;
            first = newNode;
        } else {
            Node current = first;
            while (current.next != null && ((Order) current.next.data).compareTo(order) < 0) {
                current = current.next;
            }
            newNode.next = current.next;
            current.next = newNode;
        }
    }

    /**
     * Displays all orders in the linked list.
     */
    public void displayOrders() {
        if (first == null) {
            System.out.println("No orders available.");
            return;
        }
        Node current = first;
        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }
    }

    /**
     * Displays orders by shipping status.
     * @param shipped true for shipped orders, false for unshipped orders
     */
    public void displayOrders(boolean shipped) {
        boolean found = false;
        Node current = first;
        while (current != null) {
            Order order = (Order) current.data;
            if (order.isShipped() == shipped) {
                System.out.println(order);
                found = true;
            }
            current = current.next;
        }
        if (!found) {
            System.out.println(shipped ? "No shipped orders found." : "No unshipped orders found.");
        }
    }

    /**
     * Gets all orders for a specific customer.
     * @param customerId the ID of the customer
     * @return a list of orders for the customer
     */
    public List<Order> getCustomerOrders(String customerId) {
        List<Order> orders = new ArrayList<>();
        Node current = first;
        while (current != null) {
            Order order = (Order) current.data;
            if (order.getCustomerId().equals(customerId)) {
                orders.add(order);
            }
            current = current.next;
        }
        return orders;
    }
}
