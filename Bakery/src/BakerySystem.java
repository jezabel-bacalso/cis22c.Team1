import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BakerySystem {
    private final FileHandler fileHandler;
    private final ProductCatalog catalog;
    private final PriorityQueue orderQueue;
    private final List<Order> allOrders;
    private final HashTable<Customer> customers;
    private final HashTable<Employee> employees;

    public BakerySystem() {
        fileHandler = new FileHandler();
        catalog = new ProductCatalog();
        orderQueue = new PriorityQueue();
        allOrders = new ArrayList<>();
        customers = new HashTable<>(20);
        employees = new HashTable<>(20);
        initializeData();
    }

    private void initializeData() {
        try {
            // Load products from products.txt using a file handler method.
            List<Product> productList = fileHandler.readProductsFromFile("products.txt");
            
            // Check if products were loaded, otherwise you can optionally add sample products
            if (productList != null && !productList.isEmpty()) {
                for (Product p : productList) {
                    catalog.addProduct(p);
                }
            } else {
                System.out.println("No products found in products.txt. Loading default sample products.");
                // Optionally, load default sample products if the file is empty or not found.
                productList = new ArrayList<>();
                productList.add(new Product("Chocolate Croissant", "Pastry", 2.20, 50, 
                    "A flaky, buttery croissant filled with chocolate", null, 330));
                productList.add(new Product("Custard Bun", "Pastry", 2.50, 30,
                    "Soft brioche filled with sweet custard", null, 280));
                productList.add(new Product("Brioche", "Pastry", 3.20, 20,
                    "Soft, buttery bread", null, 810));
                for (Product p : productList) {
                    catalog.addProduct(p);
                }
            }
    
            // Load customers from customers.txt
            List<User> custs = fileHandler.readUsersFromFile("customers.txt", "Customer");
            for (User u : custs) {
                if (u instanceof Customer) {
                    customers.add((Customer) u);
                }
            }
    
            // Load employees from employees.txt
            List<User> emps = fileHandler.readUsersFromFile("employees.txt", "Employee");
            for (User u : emps) {
                if (u instanceof Employee) {
                    employees.add((Employee) u);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    public void start() {
        Scanner sc = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("\n=== Bakery System Main Menu ===");
            System.out.println("1) Login as Customer");
            System.out.println("2) Create new Customer account");
            System.out.println("3) Login as Guest");
            System.out.println("4) Login as Employee");
            System.out.println("5) Login as Manager");
            System.out.println("6) Exit");
            System.out.print("Choice: ");
            
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1":
                    doCustomerLogin(sc);
                    break;
                case "2":
                    doCreateCustomer(sc);
                    break;
                case "3":
                    doGuestLogin();
                    break;
                case "4":
                    doEmployeeLogin(sc, false);
                    break;
                case "5":
                    doEmployeeLogin(sc, true);
                    break;
                case "6":
                    running = false;
                    saveData();
                    System.out.println("Exiting system...");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
        sc.close();
    }

    private void doCustomerLogin(Scanner sc) {
        System.out.print("Enter email: ");
        String email = sc.nextLine().trim();
        System.out.print("Enter password: ");
        String pw = sc.nextLine().trim();

        Customer key = new Customer("", "", email, pw);
        Customer found = customers.get(key);
        if (found == null || !found.getPassword().equals(pw)) {
            System.out.println("Invalid credentials.");
            return;
        }
        System.out.println("Welcome, " + found.getFirstName() + "!");
        CustomerMenu cm = new CustomerMenu(found, catalog, orderQueue, allOrders);
        cm.showMenu();
    }

    private void doCreateCustomer(Scanner sc) {
        System.out.print("Enter first name: ");
        String fn = sc.nextLine().trim();
        System.out.print("Enter last name: ");
        String ln = sc.nextLine().trim();
        System.out.print("Enter email: ");
        String email = sc.nextLine().trim();
        System.out.print("Enter password: ");
        String pw = sc.nextLine().trim();
        System.out.print("Enter address: ");
        String addr = sc.nextLine().trim();
        System.out.print("Enter phone: ");
        String phone = sc.nextLine().trim();

        Customer c = new Customer(fn, ln, email, pw, addr, phone);
        customers.add(c);
        System.out.println("Account created: " + c);
    }

    private void doGuestLogin() {
        Customer guest = new Customer("Guest", "User", "guest@noemail", "guest");
        System.out.println("Logged in as guest.");
        CustomerMenu cm = new CustomerMenu(guest, catalog, orderQueue, allOrders);
        cm.showMenu();
    }

    private void doEmployeeLogin(Scanner sc, boolean managerMode) {
        System.out.print("Enter email: ");
        String email = sc.nextLine().trim();
        System.out.print("Enter password: ");
        String pw = sc.nextLine().trim();

        Employee key = new Employee("", "", email, pw, managerMode);
        Employee found = employees.get(key);
        if (found == null || !found.getPassword().equals(pw)) {
            System.out.println("Invalid credentials.");
            return;
        }
        if (managerMode && !found.isManager()) {
            System.out.println("You do not have manager privileges.");
            return;
        }

        if (managerMode) {
            ManagerMenu mm = new ManagerMenu(orderQueue, allOrders, customers, employees, catalog);
            mm.showMenu();
        } else {
            EmployeeMenu em = new EmployeeMenu(orderQueue, allOrders, customers, employees);
            em.showMenu();
        }
    }

    private void saveData() {
        try {
            List<Product> products = catalog.getAllByName();
            fileHandler.writeProductsToFile("products.csv", products);
            fileHandler.writeOrdersToFile("orders.csv", allOrders);
            
            List<User> custList = new ArrayList<>();
            List<User> empList = new ArrayList<>();
            
            fileHandler.writeUsersToFile("customers.txt", custList);
            fileHandler.writeUsersToFile("employees.txt", empList);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        BakerySystem bs = new BakerySystem();
        bs.start();
    }
}