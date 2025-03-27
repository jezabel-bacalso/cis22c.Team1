import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * A fully featured generic Binary Search Tree for elements
 * that implement Comparable<T>.
 *
 * @param <T> The type of data stored in the tree. Must be Comparable<T>.
 */
public class BST<T extends Comparable<T>> {
    /** A single tree node storing data, plus left/right child references. */
    private class Node {
        private T data;
        private Node left;
        private Node right;

        public Node(T data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }

    /** The root node of this BST. */
    private Node root;

    /** 
     * Constructs an empty BST.
     */
    public BST() {
        root = null;
    }

    /**
     * Copy constructor: performs a deep copy of another BST.
     * 
     * @param other the BST to copy
     */
    public BST(BST<T> other) {
        root = null;
        if (other != null && !other.isEmpty()) {
            copyHelper(other.root);
        }
    }

    /**
     * Recursive helper for the copy constructor. Inserts each node’s data
     * from the other tree into this tree.
     */
    private void copyHelper(Node node) {
        if (node == null) {
            return;
        }
        insert(node.data);
        copyHelper(node.left);
        copyHelper(node.right);
    }

    /** 
     * Checks if the tree is empty.
     *
     * @return true if there are no nodes in the tree, false otherwise
     */
    public boolean isEmpty() {
        return (root == null);
    }

    /** 
     * Returns the number of nodes in this BST.
     */
    public int getSize() {
        return getSize(root);
    }

    /** Private recursive helper for getSize. */
    private int getSize(Node node) {
        if (node == null) return 0;
        return 1 + getSize(node.left) + getSize(node.right);
    }

    /**
     * Returns the height of the BST in terms of edges, or -1 if the tree is empty.
     * Height of a single-node tree is 0 (no edges).
     */
    public int getHeight() {
        return getHeight(root);
    }

    /** Private recursive helper for getHeight. */
    private int getHeight(Node node) {
        if (node == null) {
            return -1; 
        }
        int leftH = getHeight(node.left);
        int rightH = getHeight(node.right);
        return Math.max(leftH, rightH) + 1;
    }

    /**
     * Returns the minimum value stored in the BST.
     *
     * @throws NoSuchElementException if the tree is empty
     */
    public T findMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("findMin(): Tree is empty!");
        }
        return findMin(root).data;
    }

    /** Private helper that returns the node with the minimum data, not just the value. */
    private Node findMin(Node node) {
        if (node.left == null) {
            return node;
        }
        return findMin(node.left);
    }

    /**
     * Returns the maximum value stored in the BST.
     *
     * @throws NoSuchElementException if the tree is empty
     */
    public T findMax() {
        if (isEmpty()) {
            throw new NoSuchElementException("findMax(): Tree is empty!");
        }
        return findMax(root).data;
    }

    /** Private helper that returns the node with the maximum data. */
    private Node findMax(Node node) {
        if (node.right == null) {
            return node;
        }
        return findMax(node.right);
    }

    /**
     * Searches for a given value in the BST.
     * @param data the value to locate
     * @return the matching value if found, or null if not found
     */
    public T search(T data) {
        return search(data, root);
    }

    /** Private recursive helper for search. */
    private T search(T data, Node node) {
        if (node == null) {
            return null;
        }
        int cmp = data.compareTo(node.data);
        if (cmp == 0) {
            return node.data; // found
        } else if (cmp < 0) {
            return search(data, node.left);
        } else {
            return search(data, node.right);
        }
    }

    /**
     * Inserts a new value into the BST. 
     * Duplicates go to the right subtree by convention.
     */
    public void insert(T data) {
        root = insert(data, root);
    }

    /** Private recursive helper for insert. */
    private Node insert(T data, Node node) {
        if (node == null) {
            return new Node(data);
        }
        int cmp = data.compareTo(node.data);
        if (cmp < 0) {
            node.left = insert(data, node.left);
        } else {
            // cmp >= 0 goes to the right
            node.right = insert(data, node.right);
        }
        return node;
    }

    /**
     * Removes a value from the BST, if present. 
     * Otherwise, does nothing.
     */
    public void remove(T data) {
        root = remove(data, root);
    }

    /** Private recursive helper for remove. */
    private Node remove(T data, Node node) {
        if (node == null) {
            // not found, do nothing
            return null;
        }
        int cmp = data.compareTo(node.data);
        if (cmp < 0) {
            node.left = remove(data, node.left);
        } else if (cmp > 0) {
            node.right = remove(data, node.right);
        } else {
            // found the node to remove
            if (node.left == null && node.right == null) {
                // no children
                node = null;
            } else if (node.left != null && node.right == null) {
                // only left child
                node = node.left;
            } else if (node.left == null && node.right != null) {
                // only right child
                node = node.right;
            } else {
                // two children: replace with minimum in the right subtree
                Node minRight = findMin(node.right);
                node.data = minRight.data; 
                node.right = remove(minRight.data, node.right);
            }
        }
        return node;
    }

    /* ======================================================
       TRAVERSALS
       ====================================================== */

    /**
     * Returns all elements of the tree in **in-order** (ascending) form.
     * @return a List of the data in ascending order
     */
    public List<T> inOrderTraversal() {
        List<T> list = new ArrayList<>();
        inOrderHelper(root, list);
        return list;
    }
    private void inOrderHelper(Node node, List<T> list) {
        if (node == null) return;
        inOrderHelper(node.left, list);
        list.add(node.data);
        inOrderHelper(node.right, list);
    }

    /**
     * Returns a string with the data in in-order form, separated by spaces.
     */
    public String inOrderString() {
        StringBuilder sb = new StringBuilder();
        inOrderString(root, sb);
        return sb.toString().trim();
    }
    private void inOrderString(Node node, StringBuilder sb) {
        if (node == null) return;
        inOrderString(node.left, sb);
        sb.append(node.data).append(" ");
        inOrderString(node.right, sb);
    }

    /**
     * Returns all elements in **pre-order** (root-left-right).
     */
    public List<T> preOrderTraversal() {
        List<T> list = new ArrayList<>();
        preOrderHelper(root, list);
        return list;
    }
    private void preOrderHelper(Node node, List<T> list) {
        if (node == null) return;
        list.add(node.data);
        preOrderHelper(node.left, list);
        preOrderHelper(node.right, list);
    }

    /**
     * Returns all elements in **post-order** (left-right-root).
     */
    public List<T> postOrderTraversal() {
        List<T> list = new ArrayList<>();
        postOrderHelper(root, list);
        return list;
    }
    private void postOrderHelper(Node node, List<T> list) {
        if (node == null) return;
        postOrderHelper(node.left, list);
        postOrderHelper(node.right, list);
        list.add(node.data);
    }

    /**
     * Returns all elements in **level-order** (BFS).
     */
    public List<T> levelOrderTraversal() {
        List<T> result = new ArrayList<>();
        if (root == null) return result;

        Queue<Node> q = new LinkedList<>();
        q.offer(root);

        while (!q.isEmpty()) {
            Node current = q.poll();
            result.add(current.data);

            if (current.left != null) {
                q.offer(current.left);
            }
            if (current.right != null) {
                q.offer(current.right);
            }
        }
        return result;
    }

    /* ======================================================
       OPTIONAL EXAMPLE: LOWEST COMMON ANCESTOR 
       (sometimes called sharedPrecursor in course assignments)
       ====================================================== */

    /**
     * Finds the lowest common ancestor (LCA) of two values, if both exist in the tree.
     * Returns null if either value is not found or the tree is empty.
     */
    public T sharedPrecursor(T data1, T data2) {
        // Check if both exist:
        if (search(data1) == null || search(data2) == null) {
            return null; // if either is missing, no LCA
        }
        Node ancestor = sharedPrecursor(root, data1, data2);
        return (ancestor == null) ? null : ancestor.data;
    }

    /** Private helper for LCA. */
    private Node sharedPrecursor(Node node, T data1, T data2) {
        if (node == null) {
            return null;
        }
        int cmp1 = data1.compareTo(node.data);
        int cmp2 = data2.compareTo(node.data);

        // If both smaller, go left
        if (cmp1 < 0 && cmp2 < 0) {
            return sharedPrecursor(node.left, data1, data2);
        }
        // If both larger, go right
        if (cmp1 > 0 && cmp2 > 0) {
            return sharedPrecursor(node.right, data1, data2);
        }
        // Otherwise, they diverge here, so node is LCA
        return node;
    }
    
    /* ======================================================
       toString, EQUALS, and so on 
       (optional or based on preference)
       ====================================================== */

    /**
     * Returns a string representation of the BST in level-order form.
     */
    @Override
    public String toString() {
        List<T> level = levelOrderTraversal();
        return level.toString();
    }
}
