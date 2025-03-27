import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * LoginHandler centralizes logic for loading/saving user data
 * (both Customers and Employees), and authenticating logins.
 */
public class LoginHandler {

    private HashTable<Customer> customers;  // HashTable of customers
    private HashTable<Employee> employees;  // HashTable of employees
    private FileHandler fileHandler;

    /**
     * Constructor that initializes the hash tables and the FileHandler.
     * You can pass in the desired table sizes or read from a config.
     *
     * @param fileHandler  a FileHandler to read/write user data from disk
     * @param custTableSize initial bucket size for the customer hash table
     * @param empTableSize  initial bucket size for the employee hash table
     */
    public LoginHandler(FileHandler fileHandler, int custTableSize, int empTableSize) {
        this.fileHandler = fileHandler;
        this.customers = new HashTable<>(custTableSize);
        this.employees = new HashTable<>(empTableSize);
    }

    /**
     * Loads customers and employees from files.
     * 
     * @param customerFile  e.g. "customers.txt"
     * @param employeeFile  e.g. "employees.txt"
     * @throws IOException if file read fails
     */
    public void loadUsers(String customerFile, String employeeFile) throws IOException {
        // 1) Load Customers
        List<User> custList = fileHandler.readUsersFromFile(customerFile, "Customer");
        for (User u : custList) {
            if (u instanceof Customer) {
                customers.add((Customer)u);
            }
        }
        // 2) Load Employees
        List<User> empList = fileHandler.readUsersFromFile(employeeFile, "Employee");
        for (User u : empList) {
            if (u instanceof Employee) {
                employees.add((Employee)u);
            }
        }
    }

    /**
     * Saves customers and employees to files.
     *
     * Because our HashTable may not support direct iteration,
     * we either store them in a separate collection or
     * implement an iteration approach. For now, we demonstrate a stub.
     *
     * @param customerFile path for customer data (e.g. "customers.txt")
     * @param employeeFile path for employee data (e.g. "employees.txt")
     * @throws IOException if file write fails
     */
    public void saveUsers(String customerFile, String employeeFile) throws IOException {
        // We need a list of all customers and employees to write them
        List<User> custList = gatherAllCustomers();
        fileHandler.writeUsersToFile(customerFile, custList);

        List<User> empList = gatherAllEmployees();
        fileHandler.writeUsersToFile(employeeFile, empList);
    }

    /**
     * Authenticates a user by email + password, returning either the 
     * matching Customer or Employee, or null if not found.
     *
     * @param email    The email
     * @param password The password
     * @return A Customer or Employee object if valid, otherwise null
     */
    public User authenticate(String email, String password) {
        // 1) Check Customer
        Customer cKey = new Customer("", "", email, password);
        Customer cFound = customers.get(cKey);
        if (cFound != null && cFound.getPassword().equals(password)) {
            return cFound;
        }

        // 2) Check Employee
        Employee eKey = new Employee("", "", email, password, false);
        Employee eFound = employees.get(eKey);
        if (eFound != null && eFound.getPassword().equals(password)) {
            return eFound;
        }

        // Not found or invalid
        return null;
    }

    /**
     * Creates a new customer account by prompting the user for details.
     * In a real system, you might do this from a GUI or separate menu code.
     *
     * @param sc a Scanner for console input
     */
    public void createCustomerAccount(Scanner sc) {
        System.out.print("Enter first name: ");
        String fn = sc.nextLine().trim();
        System.out.print("Enter last name: ");
        String ln = sc.nextLine().trim();
        System.out.print("Enter email: ");
        String email = sc.nextLine().trim();
        System.out.print("Enter password: ");
        String pass = sc.nextLine().trim();
        System.out.print("Enter address: ");
        String addr = sc.nextLine().trim();
        System.out.print("Enter phone: ");
        String phone = sc.nextLine().trim();

        Customer newC = new Customer(fn, ln, email, pass, addr, phone);
        customers.add(newC);
        System.out.println("New customer account created: " + newC);
    }

    /**
     * Creates a new employee account (possibly manager) by prompting user.
     */
    public void createEmployeeAccount(Scanner sc) {
        System.out.print("Enter first name: ");
        String fn = sc.nextLine().trim();
        System.out.print("Enter last name: ");
        String ln = sc.nextLine().trim();
        System.out.print("Enter email: ");
        String email = sc.nextLine().trim();
        System.out.print("Enter password: ");
        String pass = sc.nextLine().trim();
        System.out.print("Is this a manager? (y/n): ");
        String ans = sc.nextLine().trim().toLowerCase();
        boolean manager = ans.startsWith("y");

        Employee newE = new Employee(fn, ln, email, pass, manager);
        employees.add(newE);
        System.out.println("New employee account created: " + newE);
    }

    // =========== Gathering Data for Saving ===========

    /**
     * Gathers all Customer objects from the hash table into a List<User>.
     * If your HashTable lacks iteration, you might have to store them separately.
     */
    private List<User> gatherAllCustomers() {
        // If your HashTable has no iteration, this is a placeholder:
        System.out.println("** gatherAllCustomers() is a stub if there's no iteration in HashTable **");
        return new java.util.ArrayList<>();
    }

    /**
     * Gathers all Employee objects from the hash table into a List<User>.
     */
    private List<User> gatherAllEmployees() {
        System.out.println("** gatherAllEmployees() is a stub if there's no iteration in HashTable **");
        return new java.util.ArrayList<>();
    }

    // =========== Accessors (optional) ===========

    public HashTable<Customer> getCustomers() {
        return customers;
    }

    public HashTable<Employee> getEmployees() {
        return employees;
    }
}
