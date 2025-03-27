import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

// The Product class represents an item in the catalog with details such as name, category, price, stock, etc.
// It implements Comparable to allow sorting by name (primary key) and provides methods for updating product details.
public class Product implements Comparable<Product> {

    // Static counter to generate unique product IDs.
    private static int nextId = 1000;

    // Product fields.
    private final String id;
    private final String name;
    private final String category;
    private double price;
    private int stock;
    private String description;
    private final Set<String> allergens;
    private final int calories;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Full constructor to initialize a Product with all required details.
     *
     * @param name         the product name
     * @param category     the product category/type
     * @param price        the product price (must be >= 0.01)
     * @param stock        the available stock (must be >= 0)
     * @param description  a description of the product
     * @param allergens    a set of allergens (can be null)
     * @param calories     the calorie count (must be >= 1)
     */
    public Product(String name,
                   String category,
                   double price,
                   int stock,
                   String description,
                   Set<String> allergens,
                   int calories)
    {
        // Validate input parameters.
        validateInputs(name, category, price, stock, calories);
        // Synchronized block to generate a unique product ID.
        synchronized (Product.class) {
            this.id = "P" + (nextId++);
        }
        // Sanitize and assign product name and category.
        this.name = sanitize(name);
        this.category = sanitize(category);
        this.price = price;
        this.stock = stock;
        // Sanitize and assign product description.
        this.description = sanitize(description);
        // Initialize allergens set.
        this.allergens = new HashSet<>();
        if (allergens != null) {
            for (String a : allergens) {
                // Normalize allergen strings to lowercase and trim spaces.
                this.allergens.add(a.toLowerCase().trim());
            }
        }
        this.calories = calories;
        // Set creation and updated timestamps.
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    /**
     * Minimal constructor for searching by name.
     * Defaults category to "Unknown", price to 0.01, stock to 0, description to empty string,
     * allergens to null, and calories to 1.
     *
     * @param name the product name
     */
    public Product(String name) {
        this(name, "Unknown", 0.01, 0, "", null, 1);
    }

    /**
     * Validates the input parameters for a product.
     *
     * @param name     the product name
     * @param category the product category
     * @param price    the product price
     * @param stock    the available stock
     * @param calories the calorie count
     */
    private void validateInputs(String name, String category,
                                double price, int stock, int calories)
    {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty");
        }
        if (price < 0.01) {
            throw new IllegalArgumentException("Invalid price: " + price);
        }
        if (stock < 0) {
            throw new IllegalArgumentException("Invalid stock: " + stock);
        }
        if (calories < 1) {
            throw new IllegalArgumentException("Invalid calories: " + calories);
        }
    }

    /**
     * Sanitizes an input string by replacing commas and newline characters with a space,
     * then trimming the result.
     *
     * @param input the input string to sanitize
     * @return the sanitized string
     */
    private String sanitize(String input) {
        if (input == null) return "";
        return input.replaceAll("[,\n\r]", " ").trim();
    }

    // Accessor methods.
    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public String getDescription() { return description; }
    /**
     * Returns a copy of the allergens set to ensure encapsulation.
     */
    public Set<String> getAllergens() { return new HashSet<>(allergens); }
    public int getCalories() { return calories; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    /**
     * Updates the product price and refreshes the update timestamp.
     *
     * @param price the new price (must be >= 0.01)
     */
    public void setPrice(double price) {
        if (price < 0.01) {
            throw new IllegalArgumentException("Invalid price");
        }
        this.price = price;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Updates the product stock and refreshes the update timestamp.
     *
     * @param stock the new stock level (must be >= 0)
     */
    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Invalid stock");
        }
        this.stock = stock;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Updates the product description and refreshes the update timestamp.
     *
     * @param desc the new description
     */
    public void setDescription(String desc) {
        this.description = sanitize(desc);
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Decrements the product stock by a specified quantity.
     *
     * @param quantity the quantity to decrement
     * @throws IllegalArgumentException if quantity exceeds available stock.
     */
    public void decrementStock(int quantity) {
        if (quantity > this.stock) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        this.stock -= quantity;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Compares this product with another by name, ignoring case.
     * This defines the "primary key" ordering.
     *
     * @param other the other product to compare to
     * @return a negative integer, zero, or a positive integer as this product's
     *         name is less than, equal to, or greater than the other.
     */
    @Override
    public int compareTo(Product other) {
        return this.name.compareToIgnoreCase(other.name);
    }

    /**
     * Compares products by price.
     *
     * @param other the other product to compare to
     * @return a negative integer, zero, or a positive integer as this product's
     *         price is less than, equal to, or greater than the other.
     */
    public int compareByPrice(Product other) {
        return Double.compare(this.price, other.price);
    }

    /**
     * Checks equality based on the unique product ID.
     *
     * @param obj the object to compare.
     * @return true if the product IDs match, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Product)) return false;
        Product other = (Product) obj;
        return this.id.equals(other.id);
    }

    /**
     * Generates a hash code based on the product ID.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * Returns a string representation of the product.
     *
     * @return a formatted string including the name, category, price, and stock.
     */
    @Override
    public String toString() {
        return String.format("%s | %s | $%.2f | %d in stock",
                name, category, price, stock);
    }
}
