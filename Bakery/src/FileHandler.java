/**
 * FileHandler.java
 * @author Team 1
 * CIS 22C, Group Project
 */


import java.io.*;
import java.util.*;

public class FileHandler {
    
    /**
     * Reads product data from a file and converts it into a list of Product objects.
     *
     * @param filename The name of the file to read product data from.
     * @return A list of Product objects created from the file data.
     * @throws IOException If an I/O error occurs while reading the file.
     */
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

    /**
     * Writes a list of Product objects to a file in CSV format.
     *
     * @param filename The name of the file to write product data to.
     * @param products The list of Product objects to write to the file.
     * @throws IOException If an I/O error occurs while writing to the file.
     */
    public void writeProductsToFile(String filename, List<Product> products) throws IOException {
    	try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Product product : products) {
                bw.write(product.toCSVString()); // Implement toCSVString() in Product.java
                bw.newLine();
            }
        }
    }

    /**
     * Reads order data from a file and converts it into a list of Order objects.
     *
     * @param filename The name of the file to read order data from.
     * @return A list of Order objects created from the file data.
     * @throws IOException If an I/O error occurs while reading the file.
     */
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

    /**
     * Writes a list of Order objects to a file in CSV format.
     *
     * @param filename The name of the file to write order data to.
     * @param orders The list of Order objects to write to the file.
     * @throws IOException If an I/O error occurs while writing to the file.
     */
    public void writeOrdersToFile(String filename, List<Order> orders) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Order order : orders) {
                bw.write(order.toCSVString()); // Implement toCSVString() in Order.java
                bw.newLine();
            }
        }
    }

    /**
     * Reads user data from a file and converts it into a list of User objects.
     * The type of user (Customer or Employee) is determined by the userType parameter.
     *
     * @param filename The name of the file to read user data from.
     * @param userType The type of user to create ("Customer" or "Employee").
     * @return A list of User objects created from the file data.
     * @throws IOException If an I/O error occurs while reading the file.
     */
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

    /**
     * Writes a list of User objects to a file in CSV format.
     *
     * @param filename The name of the file to write user data to.
     * @param users The list of User objects to write to the file.
     * @throws IOException If an I/O error occurs while writing to the file.
     */
    public void writeUsersToFile(String filename, List<User> users) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (User user : users) {
                bw.write(user.toCSVString()); // Implement toCSVString() in User.java
                bw.newLine();
            }
        }
    }
}
