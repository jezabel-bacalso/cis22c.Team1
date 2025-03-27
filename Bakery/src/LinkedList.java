import java.util.NoSuchElementException;

public class LinkedList<T> {

    private class Node {
        T data;
        Node next;
        Node prev;
        public Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    private Node first;
    private Node last;
    private Node iterator;
    private int length;

    public LinkedList() {
        first = null;
        last = null;
        iterator = null;
        length = 0;
    }

    public boolean isEmpty() {
        return (length == 0);
    }

    public int getLength() {
        return length;
    }

    public void addFirst(T data) {
        Node newNode = new Node(data);
        if (isEmpty()) {
            first = last = newNode;
        } else {
            newNode.next = first;
            first.prev = newNode;
            first = newNode;
        }
        length++;
    }

    public void addLast(T data) {
        Node newNode = new Node(data);
        if (isEmpty()) {
            first = last = newNode;
        } else {
            last.next = newNode;
            newNode.prev = last;
            last = newNode;
        }
        length++;
    }

    public T getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        return first.data;
    }

    public T getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        return last.data;
    }

    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        T temp = first.data;
        if (length == 1) {
            first = last = null;
        } else {
            first = first.next;
            first.prev = null;
        }
        if (iterator == first) {
            iterator = null;
        }
        length--;
        return temp;
    }

    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        T temp = last.data;
        if (length == 1) {
            first = last = null;
        } else {
            last = last.prev;
            last.next = null;
        }
        if (iterator == last) {
            iterator = null;
        }
        length--;
        return temp;
    }

    // Iterator methods
    public void positionIterator() {
        iterator = first;
    }

    public boolean offEnd() {
        return (iterator == null);
    }

    public void advanceIterator() {
        if (offEnd()) {
            throw new NoSuchElementException("Iterator is off end.");
        }
        iterator = iterator.next;
    }

    public void reverseIterator() {
        if (offEnd()) {
            throw new NoSuchElementException("Iterator is off end.");
        }
        iterator = iterator.prev;
    }

    public T getIterator() {
        if (offEnd()) {
            throw new NoSuchElementException("Iterator is off end.");
        }
        return iterator.data;
    }

    public void removeIterator() {
        if (offEnd()) {
            throw new NoSuchElementException("Iterator is off end.");
        }
        if (iterator == first) {
            removeFirst();
        } else if (iterator == last) {
            removeLast();
        } else {
            Node temp = iterator;
            temp.prev.next = temp.next;
            temp.next.prev = temp.prev;
            length--;
            iterator = null;
        }
    }

    public void clear() {
        first = last = iterator = null;
        length = 0;
    }

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
