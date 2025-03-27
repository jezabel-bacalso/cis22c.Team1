import java.util.List;
import java.util.Scanner;

public class ManagerMenu extends EmployeeMenu {

    private ProductCatalog catalog;
    private Scanner managerScanner;

    public ManagerMenu(PriorityQueue orderQueue,
                       List<Order> allOrders,
                       HashTable<Customer> customers,
                       HashTable<Employee> employees,
                       ProductCatalog catalog)
    {
        super(orderQueue, allOrders, customers, employees);
        this.catalog = catalog;
        this.managerScanner = new Scanner(System.in);
    }

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

            String choice = managerScanner.nextLine().trim();
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
                    doAddNewProduct();
                    break;
                case "7":
                    doUpdateProduct();
                    break;
                case "8":
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
        java.util.Set<String> allergSet = new java.util.HashSet<>();
        if (!allergLine.trim().isEmpty()) {
            String[] tokens = allergLine.split(";");
            for (String t : tokens) {
                allergSet.add(t.trim().toLowerCase());
            }
        }

        Product p = new Product(name, cat, price, stock, desc, allergSet, cals);
        catalog.addProduct(p);
        System.out.println("Product added: " + p);
    }

    private void doUpdateProduct() {
        System.out.print("Enter product name to update: ");
        String name = managerScanner.nextLine().trim();

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

        catalog.updateProduct(existing, newPrice, newDesc, newStock);
        System.out.println("Updated product: " + existing);
    }

    private void doRemoveProduct() {
        System.out.print("Enter product name to remove: ");
        String name = managerScanner.nextLine().trim();

        Product existing = catalog.findByName(name);
        if (existing == null) {
            System.out.println("No product found with that name.");
            return;
        }
        catalog.removeProduct(existing);
        System.out.println("Removed product: " + existing);
    }
}
