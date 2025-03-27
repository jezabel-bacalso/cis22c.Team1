import java.util.List;
import java.util.Scanner;

// ManagerMenu extends EmployeeMenu and adds additional administrative options
// such as adding, updating, and removing products.
public class ManagerMenu extends EmployeeMenu {

    // Reference to the ProductCatalog for product management.
    private ProductCatalog catalog;
    // A separate scanner instance for manager-specific inputs.
    private Scanner managerScanner;

    /**
     * Constructor to initialize the ManagerMenu.
     *
     * @param orderQueue the priority queue for orders.
     * @param allOrders  the list of all orders.
     * @param customers  the hash table of customers.
     * @param employees  the hash table of employees.
     * @param catalog    the product catalog to manage products.
     */
    public ManagerMenu(PriorityQueue orderQueue,
                       List<Order> allOrders,
                       HashTable<Customer> customers,
                       HashTable<Employee> employees,
                       ProductCatalog catalog)
    {
        // Call the parent constructor to initialize order and user management.
        super(orderQueue, allOrders, customers, employees);
        this.catalog = catalog;
        // Initialize a separate Scanner for manager inputs.
        this.managerScanner = new Scanner(System.in);
    }

    /**
     * Displays the manager menu and processes manager selections.
     */
    @Override
    public void showMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Manager Menu ===");
            System.out.println("1) Search for an Order by ID");
            System.out.println("2) Search for an Order by Customer Name");
            System.out.println("3) View Highest Priority Order");
            System.out.println("4) View All Orders Sorted by Priority");
            System.out.println("5) Ship an Order");
            System.out.println("6) Add New Product");
            System.out.println("7) Update an Existing Product");
            System.out.println("8) Remove a Product");
            System.out.println("9) Quit (back to main)");

            // Read the manager's choice from input.
            String choice = managerScanner.nextLine().trim();
            switch (choice) {
                case "1":
                    // Inherited functionality from EmployeeMenu.
                    doSearchById();
                    break;
                case "2":
                    // Inherited functionality from EmployeeMenu.
                    doSearchByCustomerName();
                    break;
                case "3":
                    // Inherited functionality from EmployeeMenu.
                    doViewHighestPriority();
                    break;
                case "4":
                    // Inherited functionality from EmployeeMenu.
                    doViewAllOrdersSorted();
                    break;
                case "5":
                    // Inherited functionality from EmployeeMenu.
                    doShipOrder();
                    break;
                case "6":
                    // Manager-specific option to add a new product.
                    doAddNewProduct();
                    break;
                case "7":
                    // Manager-specific option to update an existing product.
                    doUpdateProduct();
                    break;
                case "8":
                    // Manager-specific option to remove a product.
                    doRemoveProduct();
                    break;
                case "9":
                    running = false;
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    /**
     * Prompts the manager for details and adds a new product to the catalog.
     */
    private void doAddNewProduct() {
        System.out.print("Enter product name: ");
        String name = managerScanner.nextLine().trim();

        System.out.print("Enter category: ");
        String cat = managerScanner.nextLine().trim();

        System.out.print("Enter price: ");
        double price = Double.parseDouble(managerScanner.nextLine().trim());

        System.out.print("Enter stock quantity: ");
        int stock = Integer.parseInt(managerScanner.nextLine().trim());

        System.out.print("Enter description: ");
        String desc = managerScanner.nextLine().trim();

        System.out.print("Enter calories: ");
        int cals = Integer.parseInt(managerScanner.nextLine().trim());

        System.out.print("Enter allergens (semicolon-separated): ");
        String allergLine = managerScanner.nextLine();
        // Parse the allergen input into a set.
        java.util.Set<String> allergSet = new java.util.HashSet<>();
        if (!allergLine.trim().isEmpty()) {
            String[] tokens = allergLine.split(";");
            for (String t : tokens) {
                allergSet.add(t.trim().toLowerCase());
            }
        }

        // Create and add the new product.
        Product p = new Product(name, cat, price, stock, desc, allergSet, cals);
        catalog.addProduct(p);
        System.out.println("Product added: " + p);
    }

    /**
     * Prompts the manager for product details and updates an existing product.
     */
    private void doUpdateProduct() {
        System.out.print("Enter product name to update: ");
        String name = managerScanner.nextLine().trim();

        // Find the product by name.
        Product existing = catalog.findByName(name);
        if (existing == null) {
            System.out.println("No product found with that name.");
            return;
        }
        System.out.println("Current product: " + existing);

        System.out.print("New price (blank = no change): ");
        String pStr = managerScanner.nextLine().trim();
        double newPrice = existing.getPrice();
        if (!pStr.isEmpty()) {
            newPrice = Double.parseDouble(pStr);
        }

        System.out.print("New description (blank = no change): ");
        String dStr = managerScanner.nextLine().trim();
        String newDesc = dStr.isEmpty() ? existing.getDescription() : dStr;

        System.out.print("Add to stock (blank = 0): ");
        String sStr = managerScanner.nextLine().trim();
        int newStock = existing.getStock();
        if (!sStr.isEmpty()) {
            int addQty = Integer.parseInt(sStr);
            newStock += addQty;
        }

        // Update the product in the catalog.
        catalog.updateProduct(existing, newPrice, newDesc, newStock);
        System.out.println("Updated product: " + existing);
    }

    /**
     * Prompts the manager for a product name and removes that product from the catalog.
     */
    private void doRemoveProduct() {
        System.out.print("Enter product name to remove: ");
        String name = managerScanner.nextLine().trim();

        // Find the product by name.
        Product existing = catalog.findByName(name);
        if (existing == null) {
            System.out.println("No product found with that name.");
            return;
        }
        // Remove the product from the catalog.
        catalog.removeProduct(existing);
        System.out.println("Removed product: " + existing);
    }
}
