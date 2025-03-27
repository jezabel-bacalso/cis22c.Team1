import java.util.ArrayList;
import java.util.List;

// The ProductCatalog class maintains a collection of products organized in two Binary Search Trees (BSTs):
// one sorted by product name (using the default compareTo method) and another sorted by price.
// This dual-structure allows for efficient lookups by name and price.
public class ProductCatalog {

    // BST that sorts products by name (using Product.compareTo)
    private BST<Product> byName;
    // BST that sorts products by price. We override compareTo via a wrapper.
    private BST<Product> byPrice;

    /**
     * Constructs an empty ProductCatalog.
     */
    public ProductCatalog() {
        byName = new BST<>();
        byPrice = new BST<>();
    }

    /**
     * Adds a product to both BSTs.
     * The product is inserted into the name-based BST directly,
     * and into the price-based BST after wrapping it to override compareTo.
     *
     * @param p the Product to add
     */
    public void addProduct(Product p) {
        // Insert into the BST sorted by name.
        byName.insert(p);
        // Wrap the product so that its compareTo compares by price, then insert.
        Product priceWrapper = makePriceComparableCopy(p);
        byPrice.insert(priceWrapper);
    }

    /**
     * Searches for a product by its name.
     * Uses the minimal constructor of Product to create a search key.
     *
     * @param name the product name to search for
     * @return the matching Product if found, otherwise null
     */
    public Product findByName(String name) {
        // Create a minimal product (using name as the key) for search.
        Product temp = new Product(name);
        return byName.search(temp);
    }

    /**
     * Searches for a product that has an exact price match.
     * Performs an in-order traversal of the price-based BST and compares prices.
     *
     * @param price the price to search for
     * @return the matching Product if found, otherwise null
     */
    public Product findByExactPrice(double price) {
        // Retrieve products in order sorted by price.
        List<Product> list = byPrice.inOrderTraversal();
        for (Product p : list) {
            // Compare prices with a tolerance for floating point arithmetic.
            if (Math.abs(p.getPrice() - price) < 0.000001) {
                return p;
            }
        }
        return null;
    }

    /**
     * Removes a product from both BSTs.
     *
     * @param p the Product to remove
     */
    public void removeProduct(Product p) {
        // Remove from the name-based BST.
        byName.remove(p);
        // Remove from the price-based BST using the price-comparable wrapper.
        Product priceWrapper = makePriceComparableCopy(p);
        byPrice.remove(priceWrapper);
    }

    /**
     * Updates an existing product by removing it, updating its fields,
     * and re-adding it to both BSTs.
     *
     * @param original the Product to update
     * @param newPrice the new price to set
     * @param newDesc  the new description to set (if not empty)
     * @param newStock the new stock quantity to set
     */
    public void updateProduct(Product original,
                              double newPrice,
                              String newDesc,
                              int newStock)
    {
        // Remove the product from both trees.
        removeProduct(original);
        // Update the product's fields.
        original.setPrice(newPrice);
        if (newDesc != null && !newDesc.isEmpty()) {
            original.setDescription(newDesc);
        }
        original.setStock(newStock);
        // Re-add the updated product to the catalog.
        addProduct(original);
    }

    /**
     * Returns all products sorted by name (in ascending order).
     *
     * @return a list of products sorted by name.
     */
    public List<Product> getAllByName() {
        return byName.inOrderTraversal();
    }

    /**
     * Returns all products sorted by price (in ascending order).
     *
     * @return a list of products sorted by price.
     */
    public List<Product> getAllByPrice() {
        return byPrice.inOrderTraversal();
    }

    /**
     * Helper method that returns a "clone" of a product but overrides compareTo
     * to compare products by price rather than by name.
     *
     * @param p the Product to wrap
     * @return a new Product instance that compares by price
     */
    private Product makePriceComparableCopy(Product p) {
        return new Product(p.getName(),
                           p.getCategory(),
                           p.getPrice(),
                           p.getStock(),
                           p.getDescription(),
                           p.getAllergens(),
                           p.getCalories())
        {
            // Override the compareTo method so that comparisons are made based on price.
            @Override
            public int compareTo(Product other) {
                return Double.compare(this.getPrice(), other.getPrice());
            }
        };
    }
}
