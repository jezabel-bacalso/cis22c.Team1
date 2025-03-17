import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Product implements Comparable<Product> {
    private final String productName;
    private final String category;
    private double price;
    private int stock;
    private final String description;
    private final Set<String> allergens;
    private final int calories;

    public Product(String productName, String category, double price, int stock,
                   String description, String allergensCSV, int calories) {
        validateInputs(productName, price, stock, calories);
        
        this.productName = sanitize(productName.trim());
        this.category = sanitize(category.trim());
        this.price = price;
        this.stock = stock;
        this.description = sanitize(description.trim());
        this.allergens = parseAllergens(allergensCSV);
        this.calories = calories;
    }

    private void validateInputs(String productName, double price, int stock, int calories) {
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (price < 0.01) throw new IllegalArgumentException("Invalid price: " + price);
        if (stock < 0) throw new IllegalArgumentException("Invalid stock: " + stock);
        if (calories < 1) throw new IllegalArgumentException("Invalid calories: " + calories);
    }

    private Set<String> parseAllergens(String allergensCSV) {
        return Arrays.stream(allergensCSV.split(","))
            .map(String::trim)
            .filter(a -> !a.isEmpty())
            .collect(Collectors.toCollection(() -> 
                new TreeSet<>(String.CASE_INSENSITIVE_ORDER)));
    }

    public static List<Product> loadFromFile(String filename) throws IOException {
        List<Product> products = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = skipEmptyLines(br)) != null) {
                String name = line.trim();
                String category = readNextLine(br);
                double price = parseDouble(readNextLine(br));
                String description = readNextLine(br);
                String allergens = readNextLine(br);
                int calories = parseInt(readNextLine(br));
                
                products.add(new Product(
                    name, category, price, 15, // Default stock to 15
                    description, allergens, calories
                ));
                
                br.readLine(); // Skip empty line
            }
        }
        return products;
    }

    private static String readNextLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        if (line == null) throw new IOException("Unexpected end of file");
        return line.trim();
    }

    private static String skipEmptyLines(BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            if (!line.trim().isEmpty()) return line;
        }
        return null;
    }

    // CSV-safe string handling
    private static String sanitize(String input) {
        return input.replace(",", "<comma>")
                    .replace("\n", "<newline>");
    }

    public String toFileString() {
        return String.join("\n",
            productName,
            category,
            String.format("%.2f", price),
            description,
            String.join(", ", allergens),
            String.valueOf(calories),
            "" // Empty line separator
        );
    }

    // Getters and setters
    public String getProductName() { return productName.replace("<comma>", ",").replace("<newline>", "\n"); }
    public String getCategory() { return category.replace("<comma>", ",").replace("<newline>", "\n"); }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public String getDescription() { return description.replace("<comma>", ",").replace("<newline>", "\n"); }
    public Set<String> getAllergens() { return new TreeSet<>(allergens); }
    public int getCalories() { return calories; }

    public void setPrice(double price) {
        if (price < 0.01) throw new IllegalArgumentException("Invalid price");
        this.price = price;
    }

    public void setStock(int stock) {
        if (stock < 0) throw new IllegalArgumentException("Invalid stock");
        this.stock = stock;
    }

    @Override
    public int compareTo(Product o) {
        return productName.compareToIgnoreCase(o.productName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        return productName.equalsIgnoreCase(((Product) o).productName);
    }

    @Override
    public int hashCode() {
        return productName.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s | %s | $%.2f | %d in stock", 
            productName, category, price, stock);
    }
}