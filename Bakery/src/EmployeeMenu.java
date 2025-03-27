import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// The EmployeeMenu class provides a console interface for employees to manage orders.
// It allows employees to search for orders, view and ship orders, and perform various related tasks.
public class EmployeeMenu {
    // Queue for managing orders based on their priority.
    protected PriorityQueue orderQueue;
    // List containing all orders in the system.
    protected List<Order> allOrders;
    // Hash table storing customer information.
    protected HashTable<Customer> customers;
    // Hash table storing employee information.
    protected HashTable<Employee> employees;
    // Scanner for reading user input.
    protected Scanner sc;

    /**
     * Constructor to initialize the EmployeeMenu.
     *
     * @param orderQueue the priority queue of orders
     * @param allOrders  the list of all orders
     * @param customers  the hash table of customers
     * @param employees  the hash table of employees
     */
    public EmployeeMenu(PriorityQueue orderQueue,
                        List<Order> allOrders,
                        HashTable<Customer> customers,
                        HashTable<Employee> employees)
    {
        this.orderQueue = orderQueue;
        this.allOrders = allOrders;
        this.customers = customers;
        this.employees = employees;
        // Initialize the scanner for input reading.
        this.sc = new Scanner(System.in);
    }

    /**
     * Displays the main menu for employees and processes user selections.
     */
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

    /**
     * Prompts the user to enter an Order ID and then searches for it.
     * Displays the order details if found, or informs the user if not found.
     */
    protected void doSearchById() {
        System.out.print("Enter Order ID: ");
        String orderId = sc.nextLine().trim();
        // Search in the order queue for an unshipped order with the given ID.
        Order found = searchById(orderId);
        if (found != null) {
            System.out.println("Found unshipped: " + found);
        } else {
            // If not found in the queue, check the allOrders list (possibly shipped orders).
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
     * Searches for an order in the order queue by its ID.
     * This method is declared as protected so that ManagerMenu can also use it.
     *
     * @param orderId the ID of the order to search for
     * @return the Order if found, otherwise null
     */
    protected Order searchById(String orderId) {
        return orderQueue.searchById(orderId);
    }

    /**
     * Prompts the user for a customer's first and last name,
     * and then searches for orders (both unshipped and shipped) associated with that customer.
     */
    protected void doSearchByCustomerName() {
        System.out.print("Enter first name: ");
        String fn = sc.nextLine().trim().toLowerCase();
        System.out.print("Enter last name: ");
        String ln = sc.nextLine().trim().toLowerCase();

        // Retrieve a list of customers that match the given first and last names.
        List<Customer> matches = getCustomersByName(fn, ln);
        if (matches.isEmpty()) {
            System.out.println("No matching customers found.");
            return;
        }
        // For each matching customer, search for both unshipped and shipped orders.
        for (Customer c : matches) {
            String email = c.getEmail();
            // Search for unshipped orders in the queue using customer's email.
            List<Order> unshipped = searchByCustomerEmail(email);
            if (unshipped.isEmpty()) {
                System.out.println("No unshipped orders for " + email);
            } else {
                System.out.println("Unshipped orders for " + email + ":");
                for (Order o : unshipped) {
                    System.out.println("  " + o);
                }
            }
            // Check for shipped orders in the global allOrders list.
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

    /**
     * Searches the order queue for orders associated with a specific customer's email.
     *
     * @param email the customer's email address
     * @return a list of orders associated with that email
     */
    protected List<Order> searchByCustomerEmail(String email) {
        return orderQueue.searchByCustomerEmail(email);
    }

    /**
     * This method is a stub. If the HashTable implementation does not support iteration,
     * it's not possible to retrieve customers by name unless they are stored separately in a List.
     *
     * @param first the customer's first name (in lowercase)
     * @param last  the customer's last name (in lowercase)
     * @return an empty list, as this is a stub implementation
     */
    protected List<Customer> getCustomersByName(String first, String last) {
        System.out.println("** getCustomersByName() STUB: no iteration in HashTable **");
        return new ArrayList<>();
    }

    /**
     * Displays the order with the highest priority (i.e., the first order in the queue).
     */
    protected void doViewHighestPriority() {
        if (orderQueue.isEmpty()) {
            System.out.println("No unshipped orders in queue.");
            return;
        }
        Order top = orderQueue.peek();
        System.out.println("Highest priority: " + top);
    }

    /**
     * Displays all unshipped orders sorted by priority (from high to low).
     */
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

    /**
     * Ships the highest priority order from the order queue.
     * After shipping, the order is removed from the customer's unshipped orders list.
     */
    protected void doShipOrder() {
        if (orderQueue.isEmpty()) {
            System.out.println("No unshipped orders to ship.");
            return;
        }
        // Remove the order with the highest priority from the queue.
        Order shippingNow = orderQueue.remove();
        // Mark the order as shipped.
        shippingNow.ship();
        // Create a temporary customer key using the order's customer ID.
        Customer key = new Customer("", "", shippingNow.getCustomerId(), "");
        // Retrieve the actual customer from the hash table.
        Customer actual = customers.get(key);
        if (actual != null) {
            // Update the customer's records by moving the order from unshipped to shipped.
            actual.moveOrderToShipped(shippingNow);
            System.out.println("Shipped: " + shippingNow);
        } else {
            System.out.println("No matching customer for that order. Shipped anyway.");
        }
    }
}
