/**
 * FileHandler.java
 * @author Team 1
 * CIS 22C, Group Project
 */


import java.io.*;
import java.util.*;

public class FileHandler {

    public List<Product> readProductsFromFile(String filename) throws IOException {
        List<Product> products = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Product product = new Product(data[0], data[1], Integer.parseInt(data[2]), data[3], Double.parseDouble(data[4]), data[5], Integer.parseInt(data[6]));
                products.add(product);
            }
        }
        return products;
    }

    // Write product data to file
    public void writeProductsToFile(String filename, List<Product> products) throws IOException {
    	try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Product product : products) {
                bw.write(product.toCSVString()); // Implement toCSVString() in Product.java
                bw.newLine();
            }
        }
    }

    // Read order data from file
    public List<Order> readOrdersFromFile(String filename) throws IOException {
        List<Order> orders = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                // Parse data and create Order object
                // Add to orders list
            }
        }
        return orders;
    }

    // Write order data to file
    public void writeOrdersToFile(String filename, List<Order> orders) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Order order : orders) {
                bw.write(order.toCSVString()); // Implement toCSVString() in Order.java
                bw.newLine();
            }
        }
    }

    // Read user data from file
    public List<User> readUsersFromFile(String filename, String userType) throws IOException {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (userType.equals("Customer")) {
                    Customer customer = new Customer(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7]);
                    users.add(customer);
                } else if (userType.equals("Employee")) {
                    Employee employee = new Employee(data[0], data[1], data[2], data[3], Boolean.parseBoolean(data[4]));
                    users.add(employee);
                }
            }
        }
        return users;
    }

    // Write user data to file
    public void writeUsersToFile(String filename, List<User> users) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (User user : users) {
                bw.write(user.toCSVString()); // Implement toCSVString() in User.java
                bw.newLine();
            }
        }
    }
}