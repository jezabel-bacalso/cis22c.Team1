import java.util.ArrayList;

/**
 * A generic hash table implementation that uses an ArrayList of custom LinkedLists.
 * The type T must implement proper hashCode() and equals() methods.
 */
public class HashTable<T> {

    private int numElements;                      
    private ArrayList<LinkedList<T>> table;       

    /**
     * Constructs a HashTable with a specified number of buckets.
     *
     * @param size number of buckets
     * @throws IllegalArgumentException if size <= 0
     */
    public HashTable(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("HashTable size must be > 0");
        }
        table = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            table.add(new LinkedList<>());
        }
        numElements = 0;
    }

    /**
     * Constructs a HashTable and inserts all items from an array.
     *
     * @param array array of elements to insert (can be null)
     * @param size  number of buckets
     * @throws IllegalArgumentException if size <= 0
     */
    public HashTable(T[] array, int size) {
        this(size);
        if (array != null) {
            for (T element : array) {
                add(element);
            }
        }
    }

    /**
     * Computes the bucket index for a given object.
     *
     * @param obj the object to hash
     * @return bucket index
     */
    private int hash(T obj) {
        int code = obj.hashCode() & 0x7fffffff;   // force non-negative
        return code % table.size();
    }

    /**
     * Returns the total number of elements in the hash table.
     */
    public int getNumElements() {
        return numElements;
    }

    /**
     * Returns the number of elements in a specific bucket.
     *
     * @param index bucket index
     * @return count of elements in the bucket
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public int countBucket(int index) {
        if (index < 0 || index >= table.size()) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return table.get(index).getLength(); // Assumes your LinkedList has getLength()
    }

    /**
     * Searches for an element and returns it if found.
     *
     * @param elmt the element to search for
     * @return the matching element or null if not found
     * @throws NullPointerException if elmt is null
     */
    public T get(T elmt) {
        if (elmt == null) {
            throw new NullPointerException("get(): Key cannot be null");
        }
        int bucket = hash(elmt);
        LinkedList<T> list = table.get(bucket);
        int indexInList = list.findIndex(elmt);
        if (indexInList != -1) {
            list.positionIterator();
            while (!list.offEnd()) {
                T current = list.getIterator();
                if (current.equals(elmt)) {
                    return current;
                }
                list.advanceIterator();
            }
        }
        return null;
    }

    /**
     * Finds the bucket index where an element is stored.
     *
     * @param elmt the element to locate
     * @return bucket index if found, or -1 otherwise
     * @throws NullPointerException if elmt is null
     */
    public int find(T elmt) {
        if (elmt == null) {
            throw new NullPointerException("find(): Element cannot be null");
        }
        int bucket = hash(elmt);
        if (table.get(bucket).findIndex(elmt) != -1) {
            return bucket;
        }
        return -1;
    }

    /**
     * Checks whether the hash table contains the specified element.
     *
     * @param elmt the element to check
     * @return true if found, false otherwise
     * @throws NullPointerException if elmt is null
     */
    public boolean contains(T elmt) {
        return find(elmt) != -1;
    }

    /**
     * Adds an element to the hash table.
     *
     * @param elmt the element to add
     * @throws NullPointerException if elmt is null
     */
    public void add(T elmt) {
        if (elmt == null) {
            throw new NullPointerException("add(): Element cannot be null");
        }
        int bucket = hash(elmt);
        table.get(bucket).addLast(elmt);
        numElements++;
    }

    /**
     * Removes an element from the hash table.
     *
     * @param elmt the element to remove
     * @return true if removed, false if not found
     * @throws NullPointerException if elmt is null
     */
    public boolean delete(T elmt) {
        if (elmt == null) {
            throw new NullPointerException("delete(): Element cannot be null");
        }
        int bucket = hash(elmt);
        LinkedList<T> list = table.get(bucket);
        list.positionIterator();
        while (!list.offEnd()) {
            if (list.getIterator().equals(elmt)) {
                list.removeIterator();
                numElements--;
                return true;
            }
            list.advanceIterator();
        }
        return false;
    }

    /**
     * Clears the hash table.
     */
    public void clear() {
        for (int i = 0; i < table.size(); i++) {
            table.get(i).clear();
        }
        numElements = 0;
    }

    /**
     * Computes the load factor of the hash table.
     *
     * @return load factor (elements divided by number of buckets)
     */
    public double getLoadFactor() {
        return (double) numElements / table.size();
    }

    /**
     * Returns a string representation of the contents of a specific bucket.
     *
     * @param bucket the bucket index
     * @return string listing the elements in the bucket
     * @throws IndexOutOfBoundsException if bucket index is invalid
     */
    public String bucketToString(int bucket) {
        if (bucket < 0 || bucket >= table.size()) {
            throw new IndexOutOfBoundsException("bucketToString(): Invalid bucket index " + bucket);
        }
        StringBuilder sb = new StringBuilder();
        LinkedList<T> list = table.get(bucket);
        list.positionIterator();
        while (!list.offEnd()) {
            sb.append(list.getIterator().toString()).append(" ");
            list.advanceIterator();
        }
        sb.append("\n");
        return sb.toString();
    }

    /**
     * Returns a string summarizing each bucket with its first element or "empty".
     */
    public String rowToString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < table.size(); i++) {
            sb.append("Bucket ").append(i).append(": ");
            if (table.get(i).isEmpty()) {
                sb.append("empty\n");
            } else {
                sb.append(table.get(i).getFirst()).append("\n");
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < table.size(); i++) {
            LinkedList<T> list = table.get(i);
            list.positionIterator();
            while (!list.offEnd()) {
                result.append(list.getIterator().toString()).append(" ");
                list.advanceIterator();
            }
            if (!list.isEmpty() && i < table.size() - 1) {
                result.append("\n");
            }
        }
        return result.toString();
    }
}
