//Products.java
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Product implements Comparable<Product> {
    private static int nextId = 1000;
    
    private final String id;
    private final String name;
    private final String category;
    private double price;
    private int stock;
    private final String description;
    private final Set<String> allergens;
    private final int calories;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Product(
        String name,
        String category,
        double price,
        int stock,
        String description,
        Set<String> allergens,
        int calories
    ) {
        validateInputs(name, category, price, stock, calories);
        
        synchronized(Product.class) {
            this.id = String.format("P%d", nextId++);
        }
        this.name = sanitize(name.trim());
        this.category = sanitize(category.trim());
        this.price = price;
        this.stock = stock;
        this.description = sanitize(description.trim());
        this.allergens = new HashSet<>();
        for (String allergen : allergens) {
            this.allergens.add(allergen.toLowerCase().trim());
        }
        this.calories = calories;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    private void validateInputs(
        String name,
        String category,
        double price,
        int stock,
        int calories
    ) {
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

    private String sanitize(String input) {
        return input.replaceAll("[,\\n\\r]", " ").trim();
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public String getDescription() { return description; }
    public Set<String> getAllergens() { return new HashSet<>(allergens); }
    public int getCalories() { return calories; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters with validation
    public void setPrice(double price) {
        if (price < 0.01) {
            throw new IllegalArgumentException("Invalid price");
        }
        this.price = price;
        this.updatedAt = LocalDateTime.now();
    }

    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Invalid stock");
        }
        this.stock = stock;
        this.updatedAt = LocalDateTime.now();
    }

    public void decrementStock(int quantity) {
        if (quantity > this.stock) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        this.stock -= quantity;
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public int compareTo(Product other) {
        return this.name.compareToIgnoreCase(other.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Product)) return false;
        Product other = (Product) obj;
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s | %s | $%.2f | %d in stock",
            name, category, price, stock);
    }
}
