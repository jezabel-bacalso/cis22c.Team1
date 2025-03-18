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

    public LinkedList() {
        this.head = null;
    }

    // Add order to the linked list
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
    }

    // Display all orders
    public void displayOrders() {
        Node current = head;
        while (current != null) {
            System.out.println(current.order);
            current = current.next;
        }
    }

    // Display orders filtered by shipping status
    public void displayOrders(boolean shipped) {
        Node current = head;
        while (current != null) {
            if (current.order.isShipped() == shipped) {
                System.out.println(current.order);
            }
            current = current.next;
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

