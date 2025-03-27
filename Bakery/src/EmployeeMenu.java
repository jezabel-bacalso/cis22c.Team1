import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeeMenu {
    protected PriorityQueue orderQueue;
    protected List<Order> allOrders;
    protected HashTable<Customer> customers;
    protected HashTable<Employee> employees;
    protected Scanner sc;

    public EmployeeMenu(PriorityQueue orderQueue,
                        List<Order> allOrders,
                        HashTable<Customer> customers,
                        HashTable<Employee> employees)
    {
        this.orderQueue = orderQueue;
        this.allOrders = allOrders;
        this.customers = customers;
        this.employees = employees;
        this.sc = new Scanner(System.in);
    }

    public void showMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Employee Menu ===");
            System.out.println("1) Search for an Order by ID");
            System.out.println("2) Search for an Order by Customer first+last name");
            System.out.println("3) View Order with Highest Priority");
            System.out.println("4) View All Orders Sorted by Priority");
            System.out.println("5) Ship an Order");
            System.out.println("6) Quit");

            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1":
                    doSearchById();
                    break;
                case "2":
                    doSearchByCustomerName();
                    break;
                case "3":
                    doViewHighestPriority();
                    break;
                case "4":
                    doViewAllOrdersSorted();
                    break;
                case "5":
                    doShipOrder();
                    break;
                case "6":
                    running = false;
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    protected void doSearchById() {
        System.out.print("Enter Order ID: ");
        String orderId = sc.nextLine().trim();
        Order found = searchById(orderId);
        if (found != null) {
            System.out.println("Found unshipped: " + found);
        } else {
            // maybe it's shipped?
            Order shipped = null;
            for (Order o : allOrders) {
                if (o.getId().equals(orderId)) {
                    shipped = o;
                    break;
                }
            }
            if (shipped != null) {
                System.out.println("Order is shipped or not in queue: " + shipped);
            } else {
                System.out.println("Order not found at all.");
            }
        }
    }

    /** 
     * So ManagerMenu can call it too.
     */
    protected Order searchById(String orderId) {
        return orderQueue.searchById(orderId);
    }

    protected void doSearchByCustomerName() {
        System.out.print("Enter first name: ");
        String fn = sc.nextLine().trim().toLowerCase();
        System.out.print("Enter last name: ");
        String ln = sc.nextLine().trim().toLowerCase();

        List<Customer> matches = getCustomersByName(fn, ln);
        if (matches.isEmpty()) {
            System.out.println("No matching customers found.");
            return;
        }
        for (Customer c : matches) {
            String email = c.getEmail();
            List<Order> unshipped = searchByCustomerEmail(email);
            if (unshipped.isEmpty()) {
                System.out.println("No unshipped orders for " + email);
            } else {
                System.out.println("Unshipped orders for " + email + ":");
                for (Order o : unshipped) {
                    System.out.println("  " + o);
                }
            }
            // check shipped in allOrders
            boolean anyShipped = false;
            for (Order o : allOrders) {
                if (o.getCustomerId().equalsIgnoreCase(email) && o.isShipped()) {
                    if (!anyShipped) {
                        System.out.println("Shipped orders for " + email + ":");
                        anyShipped = true;
                    }
                    System.out.println("  " + o);
                }
            }
            if (!anyShipped) {
                System.out.println("No shipped orders for " + email);
            }
        }
    }

    protected List<Order> searchByCustomerEmail(String email) {
        return orderQueue.searchByCustomerEmail(email);
    }

    /** 
     * STUB. If HashTable lacks iteration, you can’t truly do it 
     * unless you store customers in a separate List. 
     */
    protected List<Customer> getCustomersByName(String first, String last) {
        System.out.println("** getCustomersByName() STUB: no iteration in HashTable **");
        return new ArrayList<>();
    }

    protected void doViewHighestPriority() {
        if (orderQueue.isEmpty()) {
            System.out.println("No unshipped orders in queue.");
            return;
        }
        Order top = orderQueue.peek();
        System.out.println("Highest priority: " + top);
    }

    protected void doViewAllOrdersSorted() {
        if (orderQueue.isEmpty()) {
            System.out.println("No unshipped orders.");
            return;
        }
        List<Order> sorted = orderQueue.getAllOrdersSorted();
        System.out.println("Unshipped Orders (high->low priority):");
        for (Order o : sorted) {
            System.out.println("  " + o);
        }
    }

    protected void doShipOrder() {
        if (orderQueue.isEmpty()) {
            System.out.println("No unshipped orders to ship.");
            return;
        }
        Order shippingNow = orderQueue.remove();
        shippingNow.ship();
        // move from unshipped to shipped in the customer
        Customer key = new Customer("", "", shippingNow.getCustomerId(), "");
        Customer actual = customers.get(key);
        if (actual != null) {
            actual.moveOrderToShipped(shippingNow);
            System.out.println("Shipped: " + shippingNow);
        } else {
            System.out.println("No matching customer for that order. Shipped anyway.");
        }
    }
}
