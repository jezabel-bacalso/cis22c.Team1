import java.util.List;
import java.util.Scanner;

// The CustomerMenu class provides a console-based interface for customers
// to interact with the system (e.g., search products, place orders, view orders, etc.).
public class CustomerMenu {

    // Current customer logged in.
    private Customer currentCustomer;
    // Catalog containing available products.
    private ProductCatalog catalog;
    // Priority queue for orders (for processing orders in some priority order).
    private PriorityQueue orderQueue;
    // List to hold all orders.
    private List<Order> allOrders;
    // Scanner for reading user input from the console.
    private Scanner sc;

    /**
     * Constructor for the CustomerMenu.
     *
     * @param currentCustomer the customer using the menu
     * @param catalog the product catalog
     * @param orderQueue the queue of orders to be processed
     * @param allOrders the list of all orders
     */
    public CustomerMenu(Customer currentCustomer,
                        ProductCatalog catalog,
                        PriorityQueue orderQueue,
                        List<Order> allOrders)
    {
        this.currentCustomer = currentCustomer;
        this.catalog = catalog;
        this.orderQueue = orderQueue;
        this.allOrders = allOrders;
        // Initialize the scanner to read from standard input.
        this.sc = new Scanner(System.in);
    }

    /**
     * Displays the main customer menu and processes user selections.
     */
    public void showMenu() {
        boolean keepGoing = true;
        // Continue to display the menu until the user chooses to quit.
        while (keepGoing) {
            System.out.println("\n=== Customer Menu ===");
            System.out.println("1) Search for a product (by partial name/category)");
            System.out.println("2) Find & display one record by primary key (name)");
            System.out.println("3) Find & display one record by secondary key (price)");
            System.out.println("4) List all products by name");
            System.out.println("5) List all products by price");
            System.out.println("6) Place an Order");
            System.out.println("7) View Purchases (shipped + unshipped)");
            System.out.println("8) View Shipped Orders");
            System.out.println("9) View Unshipped Orders");
            System.out.println("10) Quit (return to main menu)");
            // Prompt for the user's choice.
            System.out.print("Choice: ");

            String choice = sc.nextLine().trim();
            // Process the menu selection using a switch-case.
            switch (choice) {
                case "1":
                    searchProduct();
                    break;
                case "2":
                    findByPrimaryKey();
                    break;
                case "3":
                    findBySecondaryKey();
                    break;
                case "4":
                    listByName();
                    break;
                case "5":
                    listByPrice();
                    break;
                case "6":
                    placeOrder();
                    break;
                case "7":
                    viewPurchases();
                    break;
                case "8":
                    viewShipped();
                    break;
                case "9":
                    viewUnshipped();
                    break;
                case "10":
                    keepGoing = false;
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    /**
     * Searches the product catalog for products matching a partial name or category.
     * It displays all matching products.
     */
    private void searchProduct() {
        System.out.print("Enter partial product name or category: ");
        String kw = sc.nextLine().toLowerCase();
        // Retrieve all products sorted by name.
        List<Product> allByName = catalog.getAllByName();
        boolean foundAny = false;
        // Check each product for a match with the provided keyword.
        for (Product p : allByName) {
            if (p.getName().toLowerCase().contains(kw)
                || p.getCategory().toLowerCase().contains(kw))
            {
                System.out.println("  " + p);
                foundAny = true;
            }
        }
        if (!foundAny) {
            System.out.println("No matching products found.");
        }
    }

    /**
     * Finds and displays a product by its primary key (exact name match).
     */
    private void findByPrimaryKey() {
        System.out.print("Enter product name (exact): ");
        String name = sc.nextLine().trim();
        Product found = catalog.findByName(name);
        if (found == null) {
            System.out.println("No product with that name.");
        } else {
            System.out.println("Found: " + found);
        }
    }

    /**
     * Finds and displays a product by its secondary key (exact price match).
     */
    private void findBySecondaryKey() {
        System.out.print("Enter product price: ");
        double val;
        try {
            // Parse the input into a double.
            val = Double.parseDouble(sc.nextLine());
        } catch (NumberFormatException ex) {
            System.out.println("Invalid numeric input.");
            return;
        }
        Product found = catalog.findByExactPrice(val);
        if (found == null) {
            System.out.println("No product with that price.");
        } else {
            System.out.println("Found: " + found);
        }
    }

    /**
     * Lists all products in the catalog sorted by name.
     */
    private void listByName() {
        List<Product> list = catalog.getAllByName();
        if (list.isEmpty()) {
            System.out.println("No products in catalog.");
            return;
        }
        System.out.println("== Products (by name) ==");
        for (Product p : list) {
            System.out.println("  " + p);
        }
    }

    /**
     * Lists all products in the catalog sorted by price.
     */
    private void listByPrice() {
        List<Product> list = catalog.getAllByPrice();
        if (list.isEmpty()) {
            System.out.println("No products in catalog.");
            return;
        }
        System.out.println("== Products (by price) ==");
        for (Product p : list) {
            System.out.println("  " + p);
        }
    }

    /**
     * Allows the customer to place an order for a product.
     * It checks product availability, prompts for quantity and shipping speed,
     * creates a new order, updates product stock, and adds the order to various records.
     */
    private void placeOrder() {
        System.out.print("Enter product name to order: ");
        String name = sc.nextLine().trim();
        Product found = catalog.findByName(name);
        if (found == null) {
            System.out.println("No product by that name.");
            return;
        }
        System.out.print("Enter quantity: ");
        int qty;
        try {
            qty = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException ex) {
            System.out.println("Invalid quantity.");
            return;
        }
        if (qty <= 0) {
            System.out.println("Quantity must be > 0.");
            return;
        }
        if (qty > found.getStock()) {
            System.out.println("Not enough stock.");
            return;
        }

        // Prompt the customer to select a shipping speed.
        System.out.println("Select shipping speed:");
        System.out.println("  1) OVERNIGHT");
        System.out.println("  2) RUSH");
        System.out.println("  3) STANDARD");
        String sChoice = sc.nextLine().trim();
        Order.ShippingSpeed speed;
        switch (sChoice) {
            case "1": 
                speed = Order.ShippingSpeed.OVERNIGHT; 
                break;
            case "2": 
                speed = Order.ShippingSpeed.RUSH; 
                break;
            default:  
                speed = Order.ShippingSpeed.STANDARD; 
                break;
        }

        // Construct a new order with the customer's email and chosen shipping speed.
        Order newOrder = new Order(currentCustomer.getEmail(), speed);
        // Create a new order item with product details and quantity.
        Order.OrderItem item = new Order.OrderItem(
            found.getId(), found.getName(), qty, found.getPrice()
        );
        // Add the item to the new order.
        newOrder.addItem(item);

        // Decrement the product stock by the ordered quantity.
        found.setStock(found.getStock() - qty);

        // Add the order to the order queue and the global orders list.
        orderQueue.insert(newOrder);
        allOrders.add(newOrder);
        // Record the order as an unshipped order for the customer.
        currentCustomer.addUnshippedOrder(newOrder);

        System.out.println("Order placed: " + newOrder);
    }

    /**
     * Displays all purchases for the current customer, separating shipped and unshipped orders.
     */
    private void viewPurchases() {
        System.out.println("== All Purchases for " + currentCustomer.getEmail() + " ==");
        LinkedList<Order> unshipped = currentCustomer.getUnshippedOrders();
        LinkedList<Order> shipped = currentCustomer.getShippedOrders();

        System.out.println("Unshipped Orders:");
        // If there are no unshipped orders, display an appropriate message.
        if (unshipped.isEmpty()) {
            System.out.println("  none");
        } else {
            // Iterate through the unshipped orders using the list's iterator.
            unshipped.positionIterator();
            while (!unshipped.offEnd()) {
                System.out.println("  " + unshipped.getIterator());
                unshipped.advanceIterator();
            }
        }

        System.out.println("Shipped Orders:");
        // If there are no shipped orders, display an appropriate message.
        if (shipped.isEmpty()) {
            System.out.println("  none");
        } else {
            // Iterate through the shipped orders using the list's iterator.
            shipped.positionIterator();
            while (!shipped.offEnd()) {
                System.out.println("  " + shipped.getIterator());
                shipped.advanceIterator();
            }
        }
    }

    /**
     * Displays only the shipped orders for the current customer.
     */
    private void viewShipped() {
        LinkedList<Order> shipped = currentCustomer.getShippedOrders();
        if (shipped.isEmpty()) {
            System.out.println("No shipped orders.");
            return;
        }
        System.out.println("== Shipped Orders ==");
        shipped.positionIterator();
        while (!shipped.offEnd()) {
            System.out.println("  " + shipped.getIterator());
            shipped.advanceIterator();
        }
    }

    /**
     * Displays only the unshipped orders for the current customer.
     */
    private void viewUnshipped() {
        LinkedList<Order> unshipped = currentCustomer.getUnshippedOrders();
        if (unshipped.isEmpty()) {
            System.out.println("No unshipped orders.");
            return;
        }
        System.out.println("== Unshipped Orders ==");
        unshipped.positionIterator();
        while (!unshipped.offEnd()) {
            System.out.println("  " + unshipped.getIterator());
            unshipped.advanceIterator();
        }
    }
}
