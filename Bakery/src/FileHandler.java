import java.io.*;
import java.util.*;

public class FileHandler {

    // Helper method to convert a string of allergens (delimited by semicolons)
    // into a Set<String>
    private Set<String> parseAllergens(String allergenStr) {
        Set<String> allergens = new HashSet<>();
        if (allergenStr != null && !allergenStr.trim().isEmpty()) {
            String[] tokens = allergenStr.split(";");
            for (String token : tokens) {
                allergens.add(token.trim());
            }
        }
        return allergens;
    }

    /* ==================== PRODUCTS ==================== */
    public List<Product> readProductsFromFile(String filename) throws IOException {
        List<Product> products = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // skip blank lines
                String[] data = line.split(",");
                if (data.length != 7) {
                    throw new IOException("Invalid product CSV format. Expected 7 fields, got " 
                                          + data.length + "\nLine: " + line);
                }
                String name = data[0].trim();
                String category = data[1].trim();
                // Notice: The actual Product constructor expects price (double) then stock (int)
                double price = Double.parseDouble(data[4].trim());
                int stock = Integer.parseInt(data[2].trim());
                String description = data[3].trim();
                Set<String> allergens = parseAllergens(data[5].trim());
                int calories = Integer.parseInt(data[6].trim());
                
                // Call the existing constructor:
                Product product = new Product(name, category, price, stock, description, allergens, calories);
                products.add(product);
            }
        }
        return products;
    }

    /**
     * Writes products to a CSV file.
     * Instead of relying on a toCSVString() method, we build the CSV line here.
     */
    public void writeProductsToFile(String filename, List<Product> products) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Product product : products) {
                // Build a CSV line with the following order:
                // name, category, stock, description, price, allergens, calories
                String allergensJoined = String.join(";", product.getAllergens());
                String line = product.getName() + "," +
                              product.getCategory() + "," +
                              product.getStock() + "," +
                              product.getDescription().replace(",", " ") + "," +
                              product.getPrice() + "," +
                              allergensJoined + "," +
                              product.getCalories();
                bw.write(line);
                bw.newLine();
            }
        }
    }

    /* ==================== ORDERS ==================== */

    /**
     * Reads orders from a CSV file.
     * Expected CSV format (9 fields). We assume Order(String[] csvData) exists.
     */
    public List<Order> readOrdersFromFile(String filename) throws IOException {
        List<Order> orders = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                if (data.length != 9) {
                    throw new IOException("Invalid order CSV format. Expected 9 fields, got " 
                                          + data.length + "\nLine: " + line);
                }
                orders.add(new Order(data));  // Order has a CSV constructor
            }
        }
        return orders;
    }

    /**
     * Writes orders to a CSV file.
     * We assume Order has a working toCSVString() method.
     */
    public void writeOrdersToFile(String filename, List<Order> orders) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Order order : orders) {
                bw.write(order.toCSVString());
                bw.newLine();
            }
        }
    }

    /* ==================== USERS (CUSTOMERS & EMPLOYEES) ==================== */

    /**
     * Reads users from a CSV file.
     * For Customers, we expect at least 3 fields: name, email, password.
     * For Employees, we expect at least 4 fields: name, email, password, role.
     */
    public List<User> readUsersFromFile(String filename, String userType) throws IOException {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                if (userType.equalsIgnoreCase("Customer")) {
                    if (data.length < 3) {
                        throw new IOException("Invalid Customer CSV: Expected at least 3 fields, got " 
                                              + data.length + "\nLine: " + line);
                    }
                    // Use the existing three-argument constructor
                    Customer customer = new Customer(data[0].trim(), data[1].trim(), data[2].trim());
                    users.add(customer);
                } else if (userType.equalsIgnoreCase("Employee")) {
                    if (data.length < 4) {
                        throw new IOException("Invalid Employee CSV: Expected at least 4 fields, got " 
                                              + data.length + "\nLine: " + line);
                    }
                    // Use the existing four-argument constructor
                    Employee employee = new Employee(data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim());
                    users.add(employee);
                }
            }
        }
        return users;
    }

    /**
     * Writes users to a CSV file.
     * Since toCSVString() isn’t defined in User, we build the CSV line here.
     * For Customers: name, email, password.
     * For Employees: name, email, password, role.
     */
    public void writeUsersToFile(String filename, List<User> users) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (User user : users) {
                if (user instanceof Customer) {
                    Customer c = (Customer) user;
                    String line = c.getName() + "," + c.getEmail() + "," + c.getPassword();
                    bw.write(line);
                } else if (user instanceof Employee) {
                    Employee e = (Employee) user;
                    String line = e.getName() + "," + e.getEmail() + "," + e.getPassword() + "," + e.getRole();
                    bw.write(line);
                }
                bw.newLine();
            }
        }
    }

    /**
     * Reads customers from a CSV file.
     * Expected CSV format: name,email,password (3 fields)
     */
    public List<Customer> readCustomersFromFile(String filename) throws IOException {
        List<Customer> customers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                if (data.length < 3) {
                    throw new IOException("Invalid Customer record: Expected 3 fields, got " 
                                          + data.length + "\nLine: " + line);
                }
                Customer customer = new Customer(data[0].trim(), data[1].trim(), data[2].trim());
                customers.add(customer);
            }
        }
        return customers;
    }

    /**
     * Reads employees from a CSV file.
     * Expected CSV format: name,email,password,role (4 fields)
     */
    public List<Employee> readEmployeesFromFile(String filename) throws IOException {
        List<Employee> employees = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                try {
                    String[] data = line.split(",");
                    if (data.length < 4) {
                        throw new IOException("Invalid Employee record: Expected 4 fields, got " 
                                              + data.length + "\nLine: " + line);
                    }
                    Employee employee = new Employee(data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim());
                    employees.add(employee);
                } catch (Exception e) {
                    System.err.println("Skipping invalid employee record: " + line);
                    e.printStackTrace();
                }
            }
        }
        return employees;
    }
}