import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The LoginHandler class handles user login, account creation, and user management.
 */
public class LoginHandler {
    private HashTable<Customer> customerTable; // Hash table to store customers
    private HashTable<Employee> employeeTable; // Hash table to store employees

    /**
     * Constructor for LoginHandler.
     * Initializes the hash tables and loads users from files.
     */
    public LoginHandler() {
        customerTable = new HashTable<>(10); // Initialize customer hash table
        employeeTable = new HashTable<>(10); // Initialize employee hash table
        loadUsers(); // Load users from files
    }

    /**
     * Loads users from "employees.txt" and "customers.txt" files into hash tables.
     */
    private void loadUsers() {
        try {
            // Load employees from file
            Scanner employeeScanner = new Scanner(new File("employees.txt"));
            while (employeeScanner.hasNextLine()) {
                String name = employeeScanner.nextLine();
                String email = employeeScanner.nextLine();
                String password = employeeScanner.nextLine();
                String role = employeeScanner.nextLine();
                if (employeeScanner.hasNextLine()) {
                    employeeScanner.nextLine(); // Skip blank line
                }

                employeeTable.add(new Employee(name, email, password, role)); // Add employee to hash table
            }
            employeeScanner.close();

            // Load customers from file
            Scanner customerScanner = new Scanner(new File("customers.txt"));
            while (customerScanner.hasNextLine()) {
                String name = customerScanner.nextLine();
                String email = customerScanner.nextLine();
                String password = customerScanner.nextLine();
                if (customerScanner.hasNextLine()) {
                    customerScanner.nextLine(); // Skip blank line
                }

                customerTable.add(new Customer(name, email, password)); // Add customer to hash table
            }
            customerScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found"); // Handle missing file error
        }
    }

    /**
     * Handles the login process for users.
     */
    public void login() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to ___ Bakery!");
        System.out.println("\nPlease enter your email: ");
        String email = scanner.nextLine();
        System.out.println("Please enter your password: ");
        String password = scanner.nextLine();

        // Check if the user exists in the customer or employee table
        User user = customerTable.get(new Customer("", email, password));
        if (user == null) {
            user = employeeTable.get(new Employee("", email, password, ""));
        }

        // Validate user credentials
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("Welcome " + user.getName() + "!");
            if (user instanceof Customer) {
                System.out.println("You are logged in as a customer.");
            } else if (user instanceof Employee) {
                System.out.println("You are logged in as an employee.");
            }
        } else {
            System.out.println("No account found. Let's create one!");
            createUser(); // Prompt user to create an account
        }
    }

    /**
     * Handles the account creation process for new users.
     */
    private void createUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your name: ");
        String name = scanner.nextLine();
        System.out.println("Please enter your email: ");
        String email = scanner.nextLine();
        System.out.println("Please enter your password: ");
        String password = scanner.nextLine();
        System.out.println("Are you a customer or employee?");
        String role = scanner.nextLine();

        // Create a new customer or employee based on the role
        if (role.equalsIgnoreCase("customer")) {
            Customer newCustomer = new Customer(name, email, password);
            customerTable.add(newCustomer); // Add customer to hash table
            System.out.println("Account created successfully!");
            directUserToMenu(newCustomer); // Direct user to customer menu
        } else if (role.equalsIgnoreCase("employee")) {
            Employee newEmployee = new Employee(name, email, password, "employee");
            employeeTable.add(newEmployee); // Add employee to hash table
            System.out.println("Account created successfully!");
            directUserToMenu(newEmployee); // Direct user to employee menu
        } else {
            System.out.println("Invalid role. Account creation failed.");
        }
    }

    /**
     * Directs the user to the appropriate menu based on their role.
     * @param user The user to be directed.
     */
    private void directUserToMenu(User user) {
        if (user instanceof Customer) {
            new CustomerMenu().start(); // Start customer menu
        } else if (user instanceof Employee) {
            new EmployeeMenu().start(); // Start employee menu
        } else {
            System.out.println("Invalid role");
        }
    }

    /**
     * The main method serves as the entry point for the program.
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        LoginHandler loginHandler = new LoginHandler(); // Create an instance of LoginHandler
        loginHandler.login(); // Start the login process
    }
}

// Customer class represents a customer user.
class Customer extends User {
    public Customer(String name, String email, String password) {
        super(name, email, password);
    }

    @Override
    public String getRole() {
        return "customer";
    }
}

// Employee class represents an employee user.
class Employee extends User {
    private String role;

    public Employee(String name, String email, String password, String role) {
        super(name, email, password);
        this.role = role;
    }

    @Override
    public String getRole() {
        return role;
    }
}