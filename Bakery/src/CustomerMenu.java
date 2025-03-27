import java.util.List;
import java.util.Scanner;

public class CustomerMenu {

    private Customer currentCustomer;
    private ProductCatalog catalog;
    private PriorityQueue orderQueue;
    private List<Order> allOrders;
    private Scanner sc;

    public CustomerMenu(Customer currentCustomer,
                        ProductCatalog catalog,
                        PriorityQueue orderQueue,
                        List<Order> allOrders)
    {
        this.currentCustomer = currentCustomer;
        this.catalog = catalog;
        this.orderQueue = orderQueue;
        this.allOrders = allOrders;
        this.sc = new Scanner(System.in);
    }

    public void showMenu() {
        boolean keepGoing = true;
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
            // Added prompt at the end
            System.out.print("Choice: ");

            String choice = sc.nextLine().trim();
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

    private void searchProduct() {
        System.out.print("Enter partial product name or category: ");
        String kw = sc.nextLine().toLowerCase();
        List<Product> allByName = catalog.getAllByName();
        boolean foundAny = false;
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

    private void findBySecondaryKey() {
        System.out.print("Enter product price: ");
        double val;
        try {
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

        System.out.println("Select shipping speed:");
        System.out.println("  1) OVERNIGHT");
        System.out.println("  2) RUSH");
        System.out.println("  3) STANDARD");
        String sChoice = sc.nextLine().trim();
        Order.ShippingSpeed speed;
        switch (sChoice) {
            case "1": speed = Order.ShippingSpeed.OVERNIGHT; break;
            case "2": speed = Order.ShippingSpeed.RUSH; break;
            default:  speed = Order.ShippingSpeed.STANDARD; break;
        }

        // Construct new order
        Order newOrder = new Order(currentCustomer.getEmail(), speed);
        // Add item
        Order.OrderItem item = new Order.OrderItem(
            found.getId(), found.getName(), qty, found.getPrice()
        );
        newOrder.addItem(item);

        // decrement product stock
        found.setStock(found.getStock() - qty);

        // add to queue + allOrders
        orderQueue.insert(newOrder);
        allOrders.add(newOrder);
        currentCustomer.addUnshippedOrder(newOrder);

        System.out.println("Order placed: " + newOrder);
    }

    private void viewPurchases() {
        System.out.println("== All Purchases for " + currentCustomer.getEmail() + " ==");
        LinkedList<Order> unshipped = currentCustomer.getUnshippedOrders();
        LinkedList<Order> shipped = currentCustomer.getShippedOrders();

        System.out.println("Unshipped Orders:");
        if (unshipped.isEmpty()) {
            System.out.println("  none");
        } else {
            unshipped.positionIterator();
            while (!unshipped.offEnd()) {
                System.out.println("  " + unshipped.getIterator());
                unshipped.advanceIterator();
            }
        }

        System.out.println("Shipped Orders:");
        if (shipped.isEmpty()) {
            System.out.println("  none");
        } else {
            shipped.positionIterator();
            while (!shipped.offEnd()) {
                System.out.println("  " + shipped.getIterator());
                shipped.advanceIterator();
            }
        }
    }

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
