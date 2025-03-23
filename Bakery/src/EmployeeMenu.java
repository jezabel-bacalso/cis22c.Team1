//EmployeeMenu.java
import java.util.LinkedList;
import java.util.Scanner;

public class EmployeeMenu {
    private PriorityQueue orderQueue;
    private LinkedList<Order> shippedOrders;

    public EmployeeMenu() {
        orderQueue = new PriorityQueue(new OrderComparator());
        shippedOrders = new LinkedList<>();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Employee Menu ---");
            System.out.println("1. Search for an Order (by ID)");
            System.out.println("2. View Highest Priority Order");
            System.out.println("3. View All Orders (Sorted by Priority)");
            System.out.println("4. Ship an Order");
            System.out.println("5. Exit");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("Enter Order ID: ");
                    int orderId = scanner.nextInt();
                    Order order = orderQueue.searchOrderById(orderId);
                    System.out.println(order != null ? order : "Order not found.");
                    break;
                case 2:
                    System.out.println("Highest Priority Order: " + orderQueue.getHighestPriorityOrder());
                    break;
                case 3:
                    System.out.println("Orders Sorted by Priority:");
                    for (Order o : orderQueue.getSortedOrders()) {
                        System.out.println(o);
                    }
                    break;
                case 4:
                    shipOrder();
                    break;
                case 5:
                    return;
            }
        }
    }

    /**
     * Ships an order by removing it from the Priority Queue and adding it to the Linked List.
     */
    private void shipOrder() {
        if (orderQueue.isEmpty()) {
            System.out.println("No orders to ship.");
            return;
        }

        Order shippedOrder = orderQueue.remove(); // Remove highest-priority order
        shippedOrders.addLast(shippedOrder); // Move to shipped orders linked list

        System.out.println("Order shipped: " + shippedOrder);
    }
}
    
