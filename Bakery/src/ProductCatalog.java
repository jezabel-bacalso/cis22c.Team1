import java.util.ArrayList;
import java.util.List;

public class ProductCatalog {

    private BST<Product> byName;   // BST using compareTo => name
    private BST<Product> byPrice;  // BST that compares by price

    public ProductCatalog() {
        byName = new BST<>();
        byPrice = new BST<>();
    }

    public void addProduct(Product p) {
        // insert into name-based BST
        byName.insert(p);
        // insert into price-based BST, but we must override compareTo
        // easiest trick: wrap p in a "priceComparable" Product
        Product priceWrapper = makePriceComparableCopy(p);
        byPrice.insert(priceWrapper);
    }

    public Product findByName(String name) {
        Product temp = new Product(name); // minimal
        return byName.search(temp);
    }

    public Product findByExactPrice(double price) {
        // we can do an in-order traversal of byPrice
        // and compare
        List<Product> list = byPrice.inOrderTraversal();
        for (Product p : list) {
            if (Math.abs(p.getPrice() - price) < 0.000001) {
                return p;
            }
        }
        return null;
    }

    public void removeProduct(Product p) {
        byName.remove(p);
        // remove from byPrice with a matching "priceComparable"
        Product priceWrapper = makePriceComparableCopy(p);
        byPrice.remove(priceWrapper);
    }

    public void updateProduct(Product original,
                              double newPrice,
                              String newDesc,
                              int newStock)
    {
        // remove old
        removeProduct(original);
        // update fields
        original.setPrice(newPrice);
        if (newDesc != null && !newDesc.isEmpty()) {
            original.setDescription(newDesc);
        }
        original.setStock(newStock);
        // re-add
        addProduct(original);
    }

    public List<Product> getAllByName() {
        return byName.inOrderTraversal();
    }

    public List<Product> getAllByPrice() {
        return byPrice.inOrderTraversal();
    }

    // Helper that returns a "clone" of p but compareTo is by price
    private Product makePriceComparableCopy(Product p) {
        return new Product(p.getName(),
                           p.getCategory(),
                           p.getPrice(),
                           p.getStock(),
                           p.getDescription(),
                           p.getAllergens(),
                           p.getCalories())
        {
            @Override
            public int compareTo(Product other) {
                return Double.compare(this.getPrice(), other.getPrice());
            }
        };
    }
}
