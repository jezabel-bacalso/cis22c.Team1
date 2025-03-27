import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileHandler {

    /**
     * Helper method to read the next non-empty line from the BufferedReader.
     * Returns null if end-of-file is reached.
     */
    private String readNonEmptyLine(BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                return line.trim();
            }
        }
        return null;
    }

    /**
     * Reads a list of Products from a multi-line text file.
     * 
     * Expected file format:
     *   Line 1: Header (e.g., "product/type/price/description/allergens/calories")
     *   Then, for each product, six lines:
     *     Line 1: Product name
     *     Line 2: Category (or type)
     *     Line 3: Price
     *     Line 4: Description
     *     Line 5: Allergens (comma- or semicolon-separated)
     *     Line 6: Calories
     * 
     * Since no stock information is provided, a default stock value is used.
     */
    public List<Product> readProductsFromFile(String filename) throws IOException {
        List<Product> products = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) {
            // If the file doesn't exist, return an empty list.
            return products;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            // Read and skip the header line
            String header = readNonEmptyLine(br);
            if (header == null) {
                return products;
            }
            
            String name;
            while ((name = readNonEmptyLine(br)) != null) {
                String type = readNonEmptyLine(br);
                if (type == null) {
                    System.out.println("Skipping malformed product record for: " + name);
                    break;
                }
                String priceStr = readNonEmptyLine(br);
                if (priceStr == null) {
                    System.out.println("Skipping malformed product record for: " + name);
                    break;
                }
                String description = readNonEmptyLine(br);
                if (description == null) {
                    System.out.println("Skipping malformed product record for: " + name);
                    break;
                }
                String allergensStr = readNonEmptyLine(br);
                if (allergensStr == null) {
                    System.out.println("Skipping malformed product record for: " + name);
                    break;
                }
                String caloriesStr = readNonEmptyLine(br);
                if (caloriesStr == null) {
                    System.out.println("Skipping malformed product record for: " + name);
                    break;
                }
                
                int defaultStock = 50; // Use default stock value
                try {
                    double price = Double.parseDouble(priceStr);
                    int calories = Integer.parseInt(caloriesStr);
                    Set<String> allergens = parseAllergens(allergensStr);
                    Product p = new Product(name, type, price, defaultStock, description, allergens, calories);
                    products.add(p);
                } catch (NumberFormatException e) {
                    System.out.println("Skipping malformed product record for: " + name);
                }
            }
        }
        return products;
    }

    /**
     * Writes a list of Products to a multi-line text file.
     * The format written is the same as the expected input:
     *   A header line followed by each product record (six lines per product).
     */
    public void writeProductsToFile(String filename, List<Product> products) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            // Write header
            bw.write("product/type/price/description/allergens/calories");
            bw.newLine();
            for (Product p : products) {
                bw.write(p.getName());
                bw.newLine();
                bw.write(p.getCategory());
                bw.newLine();
                bw.write(String.valueOf(p.getPrice()));
                bw.newLine();
                bw.write(p.getDescription());
                bw.newLine();
                // Write allergens separated by comma and space
                bw.write(String.join(", ", p.getAllergens()));
                bw.newLine();
                bw.write(String.valueOf(p.getCalories()));
                bw.newLine();
            }
        }
    }

    /**
     * Reads a list of Orders from a CSV file.
     * (This method remains unchanged.)
     */
    public List<Order> readOrdersFromFile(String filename) throws IOException {
        List<Order> orders = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) {
            return orders;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                // Example CSV line: orderId,customerEmail,shippingSpeed,isShipped
                String[] parts = line.split(",");
                if (parts.length < 4) {
                    System.out.println("Skipping malformed order line: " + line);
                    continue;
                }
                String orderId = parts[0].trim();
                String custEmail = parts[1].trim();
                Order.ShippingSpeed speed = Order.ShippingSpeed.valueOf(parts[2].trim());
                boolean shipped = Boolean.parseBoolean(parts[3].trim());

                Order o = new Order(orderId, custEmail, speed);
                if (shipped) {
                    o.markShipped();
                }
                orders.add(o);
            }
        }
        return orders;
    }

    /**
     * Writes orders to a CSV file.
     */
    public void writeOrdersToFile(String filename, List<Order> orders) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Order o : orders) {
                String line = String.join(",",
                        o.getId(),
                        o.getCustomerId(),
                        o.getShippingSpeed().name(),
                        String.valueOf(o.isShipped())
                );
                bw.write(line);
                bw.newLine();
            }
        }
    }

    /**
     * Reads a list of Users (Customers or Employees) from a text file in multi-line format.
     * 
     * For Customers, it expects 3 lines per user:
     *   Line 1: Full name (e.g., "John Smith")
     *   Line 2: Email
     *   Line 3: Password
     * 
     * For Employees, it expects 4 lines per user:
     *   Line 1: Full name (e.g., "William Thomas")
     *   Line 2: Email
     *   Line 3: Password
     *   Line 4: Role ("Manager" or "Employee")
     */
    public List<User> readUsersFromFile(String filename, String userType) throws IOException {
        List<User> users = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) {
            return users;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            if ("Customer".equalsIgnoreCase(userType)) {
                // Each customer record is 3 lines: Name, Email, Password
                while (true) {
                    String nameLine = readNonEmptyLine(br);
                    if (nameLine == null) break;
                    String emailLine = readNonEmptyLine(br);
                    if (emailLine == null) break;
                    String passwordLine = readNonEmptyLine(br);
                    if (passwordLine == null) break;

                    String[] nameParts = nameLine.split(" ");
                    String firstName = (nameParts.length > 0) ? nameParts[0] : "";
                    String lastName  = (nameParts.length > 1) ? nameParts[1] : "";
                    
                    Customer c = new Customer(firstName, lastName, emailLine, passwordLine);
                    users.add(c);
                }
            } else if ("Employee".equalsIgnoreCase(userType)) {
                // Each employee record is 4 lines: Name, Email, Password, Role
                while (true) {
                    String nameLine = readNonEmptyLine(br);
                    if (nameLine == null) break;
                    String emailLine = readNonEmptyLine(br);
                    if (emailLine == null) break;
                    String passwordLine = readNonEmptyLine(br);
                    if (passwordLine == null) break;
                    String roleLine = readNonEmptyLine(br);
                    if (roleLine == null) break;

                    String[] nameParts = nameLine.split(" ");
                    String firstName = (nameParts.length > 0) ? nameParts[0] : "";
                    String lastName  = (nameParts.length > 1) ? nameParts[1] : "";
                    
                    boolean isManager = roleLine.equalsIgnoreCase("Manager");
                    
                    Employee e = new Employee(firstName, lastName, emailLine, passwordLine, isManager);
                    users.add(e);
                }
            } else {
                System.out.println("Unknown userType: " + userType);
            }
        }
        return users;
    }

    /**
     * Writes a list of Users (Customers or Employees) to a text file.
     * For Customers, it writes 3 lines per user: Name, Email, Password.
     * For Employees, it writes 4 lines per user: Name, Email, Password, Role.
     */
    public void writeUsersToFile(String filename, List<User> users) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (User u : users) {
                if (u instanceof Customer) {
                    Customer c = (Customer) u;
                    bw.write(c.getFirstName() + " " + c.getLastName());
                    bw.newLine();
                    bw.write(c.getEmail());
                    bw.newLine();
                    bw.write(c.getPassword());
                    bw.newLine();
                } else if (u instanceof Employee) {
                    Employee e = (Employee) u;
                    bw.write(e.getFirstName() + " " + e.getLastName());
                    bw.newLine();
                    bw.write(e.getEmail());
                    bw.newLine();
                    bw.write(e.getPassword());
                    bw.newLine();
                    bw.write(e.isManager() ? "Manager" : "Employee");
                    bw.newLine();
                } else {
                    System.out.println("Skipping unknown user type: " + u.getClass());
                }
            }
        }
    }

    /**
     * Helper method to parse a string of allergens into a set.
     * Assumes allergens are separated by commas or semicolons.
     */
    private Set<String> parseAllergens(String allergenStr) {
        Set<String> allergens = new HashSet<>();
        if (allergenStr == null || allergenStr.isEmpty()) {
            return allergens;
        }
        String[] tokens = allergenStr.split("[,;]");
        for (String token : tokens) {
            allergens.add(token.trim().toLowerCase());
        }
        return allergens;
    }
}
