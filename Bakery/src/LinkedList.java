import java.util.*;

public class LinkedList {
    private static class Node {
        Order order;
        Node next;

        Node(Order order) {
            this.order = order;
            this.next = null;
        }
    }

    private Node head;
    private PriorityQueue<Order> priorityQueue; // Integration with PriorityQueue

    public LinkedList(PriorityQueue<Order> priorityQueue) {
        this.head = null;
        this.priorityQueue = priorityQueue;
    }

    // Add order to the linked list in sorted order
    public void addOrder(Order order) {
        Node newNode = new Node(order);
        if (head == null || head.order.compareTo(order) > 0) {
            newNode.next = head;
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null && current.next.order.compareTo(order) < 0) {
                current = current.next;
            }
            newNode.next = current.next;
            current.next = newNode;
        }

        // If the order is not shipped, add it to the priority queue
        if (!order.isShipped()) {
            priorityQueue.add(order);
        }
    }

    // Display all orders
    public void displayOrders() {
        if (head == null) {
            System.out.println("No orders available.");
            return;
        }

        Node current = head;
        while (current != null) {
            System.out.println(current.order);
            current = current.next;
        }
    }

    // Display orders by shipping status
    public void displayOrders(boolean shipped) {
        boolean found = false;
        Node current = head;
        while (current != null) {
            if (current.order.isShipped() == shipped) {
                System.out.println(current.order);
                found = true;
            }
            current = current.next;
        }

        if (!found) {
            System.out.println(shipped ? "No shipped orders found." : "No unshipped orders found.");
        }
    }

    // Get all orders for a specific customer
    public List<Order> getCustomerOrders(String customerId) {
        List<Order> orders = new ArrayList<>();
        Node current = head;
        while (current != null) {
            if (current.order.getCustomerId().equals(customerId)) {
                orders.add(current.order);
            }
            current = current.next;
        }
        return orders;
    }
}
