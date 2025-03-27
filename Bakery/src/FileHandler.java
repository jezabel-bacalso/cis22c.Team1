import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// The FileHandler class is responsible for reading and writing data to and from files.
// It handles products, orders, and user records (customers and employees).
public class FileHandler {

    /**
     * Helper method to read the next non-empty line from the BufferedReader.
     * It trims the line and returns null if the end-of-file is reached.
     *
     * @param br the BufferedReader to read from
     * @return the next non-empty trimmed line, or null if no more lines
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
     * A default stock value is used for each product since stock is not provided in the file.
     *
     * @param filename the file to read products from
     * @return a list of Product objects read from the file
     * @throws IOException if an I/O error occurs
     */
    public List<Product> readProductsFromFile(String filename) throws IOException {
        List<Product> products = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) {
            // If the file doesn't exist, return an empty product list.
            return products;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            // Read and skip the header line.
            String header = readNonEmptyLine(br);
            if (header == null) {
                return products;
            }
            
            String name;
            // Loop until no more product records are available.
            while ((name = readNonEmptyLine(br)) != null) {
                // Read the expected six lines per product record.
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
                
                int defaultStock = 50; // Use a default stock value when not provided in the file.
                try {
                    double price = Double.parseDouble(priceStr);
                    int calories = Integer.parseInt(caloriesStr);
                    Set<String> allergens = parseAllergens(allergensStr);
                    // Create a new Product object with the parsed information.
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
     * The output format matches the expected input format:
     *   A header line followed by each product record (six lines per product).
     *
     * @param filename the file to write the products to
     * @param products the list of products to write
     * @throws IOException if an I/O error occurs
     */
    public void writeProductsToFile(String filename, List<Product> products) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            // Write the header line.
            bw.write("product/type/price/description/allergens/calories");
            bw.newLine();
            // Write each product's details over six lines.
            for (Product p : products) {
                bw.write(p.getName());
                bw.newLine();
                bw.write(p.getCategory());
                bw.newLine();
                bw.write(String.valueOf(p.getPrice()));
                bw.newLine();
                bw.write(p.getDescription());
                bw.newLine();
                // Join allergens using a comma and a space.
                bw.write(String.join(", ", p.getAllergens()));
                bw.newLine();
                bw.write(String.valueOf(p.getCalories()));
                bw.newLine();
            }
        }
    }

    /**
     * Reads a list of Orders from a CSV file.
     * Each line in the CSV is expected to be in the format:
     *   orderId,customerEmail,shippingSpeed,isShipped
     *
     * @param filename the file to read orders from
     * @return a list of Order objects read from the file
     * @throws IOException if an I/O error occurs
     */
    public List<Order> readOrdersFromFile(String filename) throws IOException {
        List<Order> orders = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) {
            return orders;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            // Process each non-empty line in the file.
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                // Split the CSV line into its components.
                String[] parts = line.split(",");
                if (parts.length < 4) {
                    System.out.println("Skipping malformed order line: " + line);
                    continue;
                }
                String orderId = parts[0].trim();
                String custEmail = parts[1].trim();
                Order.ShippingSpeed speed = Order.ShippingSpeed.valueOf(parts[2].trim());
                boolean shipped = Boolean.parseBoolean(parts[3].trim());

                // Create a new Order and mark it as shipped if indicated.
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
     * Each order is written on a separate line in the format:
     *   orderId,customerEmail,shippingSpeed,isShipped
     *
     * @param filename the file to write the orders to
     * @param orders   the list of orders to write
     * @throws IOException if an I/O error occurs
     */
    public void writeOrdersToFile(String filename, List<Order> orders) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Order o : orders) {
                // Concatenate order properties into a CSV line.
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
     * For Customers, the expected format is three lines per user:
     *   Line 1: Full name (e.g., "John Smith")
     *   Line 2: Email
     *   Line 3: Password
     * 
     * For Employees, the expected format is four lines per user:
     *   Line 1: Full name (e.g., "William Thomas")
     *   Line 2: Email
     *   Line 3: Password
     *   Line 4: Role ("Manager" or "Employee")
     *
     * @param filename the file to read users from
     * @param userType the type of users to read ("Customer" or "Employee")
     * @return a list of User objects (either Customer or Employee)
     * @throws IOException if an I/O error occurs
     */
    public List<User> readUsersFromFile(String filename, String userType) throws IOException {
        List<User> users = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) {
            return users;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            if ("Customer".equalsIgnoreCase(userType)) {
                // Process customer records: each customer record is 3 lines.
                while (true) {
                    String nameLine = readNonEmptyLine(br);
                    if (nameLine == null) break;
                    String emailLine = readNonEmptyLine(br);
                    if (emailLine == null) break;
                    String passwordLine = readNonEmptyLine(br);
                    if (passwordLine == null) break;

                    // Split the full name into first and last name.
                    String[] nameParts = nameLine.split(" ");
                    String firstName = (nameParts.length > 0) ? nameParts[0] : "";
                    String lastName  = (nameParts.length > 1) ? nameParts[1] : "";
                    
                    // Create a new Customer with minimal information.
                    Customer c = new Customer(firstName, lastName, emailLine, passwordLine);
                    users.add(c);
                }
            } else if ("Employee".equalsIgnoreCase(userType)) {
                // Process employee records: each employee record is 4 lines.
                while (true) {
                    String nameLine = readNonEmptyLine(br);
                    if (nameLine == null) break;
                    String emailLine = readNonEmptyLine(br);
                    if (emailLine == null) break;
                    String passwordLine = readNonEmptyLine(br);
                    if (passwordLine == null) break;
                    String roleLine = readNonEmptyLine(br);
                    if (roleLine == null) break;

                    // Split the full name into first and last name.
                    String[] nameParts = nameLine.split(" ");
                    String firstName = (nameParts.length > 0) ? nameParts[0] : "";
                    String lastName  = (nameParts.length > 1) ? nameParts[1] : "";
                    
                    // Determine manager status based on role.
                    boolean isManager = roleLine.equalsIgnoreCase("Manager");
                    
                    // Create a new Employee.
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
     *
     * @param filename the file to write users to
     * @param users    the list of users to write
     * @throws IOException if an I/O error occurs
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
                    // Write role based on manager status.
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
     * It splits the string on commas or semicolons, trims each token, converts it to lowercase,
     * and adds it to a Set.
     *
     * @param allergenStr the string containing allergens
     * @return a Set of allergens
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
