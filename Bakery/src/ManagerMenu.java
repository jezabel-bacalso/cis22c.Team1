//ManagerMenu.java
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class ManagerMenu {
    private BST<Product> productTree;
    private Scanner scanner;

    public ManagerMenu() {
        productTree = new BST<>();
        scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\nManager Menu:");
            System.out.println("1. Add Product");
            System.out.println("2. Update Product");
            System.out.println("3. Remove Product");
            System.out.println("4. Search Product");
            System.out.println("5. Display Products (Sorted)");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    addProduct();
                    break;
                case 2:
                    updateProduct();
                    break;
                case 3:
                    removeProduct();
                    break;
                case 4:
                    searchProduct();
                    break;
                case 5:
                    displaySortedProducts();
                    break;
                case 6:
                    System.out.println("Exiting Manager Menu.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addProduct() {
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        System.out.print("Enter price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter stock: ");
        int stock = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        Product newProduct = new Product(name, category, price, stock, description, new HashSet<>(), 0);
        productTree.insert(newProduct);
        System.out.println("Product added successfully.");
    }

    private void updateProduct() {
        System.out.print("Enter product name to update: ");
        String name = scanner.nextLine();
        Product existingProduct = productTree.search(new Product(name, "", 0, 0, "", new HashSet<>(), 0));

        if (existingProduct != null) {
            System.out.print("Enter new price: ");
            double price = scanner.nextDouble();
            System.out.print("Enter new stock: ");
            int stock = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            existingProduct.setPrice(price);
            existingProduct.setStock(stock);
            System.out.println("Product updated successfully.");
        } else {
            System.out.println("Product not found.");
        }
    }

    private void removeProduct() {
        System.out.print("Enter product name to remove: ");
        String name = scanner.nextLine();
        boolean removed = productTree.delete(new Product(name, "", 0, 0, "", new HashSet<>(), 0));
        
        if (removed) {
            System.out.println("Product removed successfully.");
        } else {
            System.out.println("Product not found.");
        }
    }

    private void searchProduct() {
        System.out.print("Enter product name to search: ");
        String name = scanner.nextLine();
        Product product = productTree.search(new Product(name, "", 0, 0, "", new HashSet<>(), 0));
        
        if (product != null) {
            System.out.println("Product found: " + product);
        } else {
            System.out.println("Product not found.");
        }
    }

    private void displaySortedProducts() {
        List<Product> sortedProducts = productTree.inOrderTraversal();
        sortedProducts.forEach(System.out::println);
    }
}
