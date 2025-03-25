// 3/27/2025
// CustomerMenu.java
import java.util.List;
import java.util.Scanner;

public class CustomerMenu {
    private LinkedList orderList;
    private Scanner scanner;

    public CustomerMenu(LinkedList orderList) {
        this.orderList = orderList;
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n--- Customer Order Menu ---");
            System.out.println("1. View all orders");
            System.out.println("2. View shipped orders");
            System.out.println("3. View unshipped orders");
            System.out.println("4. View my order history");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // Consume invalid input
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    orderList.displayOrders();
                    break;
                case 2:
                    orderList.displayOrders(true);
                    break;
                case 3:
                    orderList.displayOrders(false);
                    break;
                case 4:
                    System.out.print("Enter your customer ID: ");
                    String customerId = scanner.nextLine();
                    List<Order> customerOrders = orderList.getCustomerOrders(customerId);
                    if (customerOrders.isEmpty()) {
                        System.out.println("No orders found.");
                    } else {
                        for (Order order : customerOrders) {
                            System.out.println(order);
                        }
                    }
                    break;
                case 5:
                    System.out.println("Exiting menu.");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}
