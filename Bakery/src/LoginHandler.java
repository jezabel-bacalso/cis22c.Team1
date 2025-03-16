
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class LoginHandler {
    private HashTable<User> customerTable;
    private HashTable<User> employeeTable;

    public LoginHandler() {
        customerTable = new HashTable<>(10);
        employeeTable = new HashTable<>(10);
        loadUsers();
    }

    private void loadUsers() {
        try {
            Scanner employeeScanner = new Scanner(new File("employees.txt"));
            while (employeeScanner.hasNextLine()) {
                String name = employeeScanner.nextLine();
                String email = employeeScanner.nextLine();
                String password = employeeScanner.nextLine();
                String role = employeeScanner.nextLine();
                if (employeeScanner.hasNextLine()) {
                    employeeScanner.nextLine();
                }

                employeeTable.add(new User(name, email, password, role));
            }
            employeeScanner.close();

            Scanner customerScanner = new Scanner(new File("customers.txt"));
            while (customerScanner.hasNextLine()) {
                String name = customerScanner.nextLine();
                String email = customerScanner.nextLine();
                String password = customerScanner.nextLine();
                if (customerScanner.hasNextLine()) {
                    customerScanner.nextLine();
                }

                customerTable.add(new User(name, email, password, "customer"));
            }
            customerScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    public void login(){
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Welcome to ___ Bakery!");
        System.out.println("\nPlease enter your email: ");
        String email = scanner.nextLine();
        System.out.println("Please enter your password: ");
        String password = scanner.nextLine();

        User user = customerTable.get(new User("", email, password, ""));
        if (user == null) {
            user = employeeTable.get(new User("", email, password, ""));
        }
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("Welcome " + user.getName() + "!");
            if (user.getRole().equals("customer")) {
                System.out.println("You are logged in as a customer.");
            } else {
                System.out.println("You are logged in as an employee.");
            }
        } else {
            System.out.println("No account found. Let's create one!");
            createUser();
        }
    }

    private void createUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your name: ");
        String name = scanner.nextLine();
        System.out.println("Please enter your email: ");
        String email = scanner.nextLine();
        System.out.println("Please enter your password: ");
        String password = scanner.nextLine();
        System.out.println("Are you a customer, employee, or manager?");
        String role = scanner.nextLine();

        User newUser = new User(name, email, password, role);
        if (role.equals("customer")) {
            customerTable.add(newUser);
        } else {
            employeeTable.add(newUser);
        }
        System.out.println("Account created successfully!");
    
        directUserToMenu(newUser);
    }

    private void directUserToMenu(User user) {
        switch (user.getRole().toLowerCase()){
            case "customer":
                new CustomerMenu().start();
                break;
            case "employee":
                new EmployeeMenu().start();
                break;
            case "manager":
                new ManagerMenu().start();
                break;
            default:
                System.out.println("Invalid role"); 
        }
    }


}
