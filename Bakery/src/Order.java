import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Order implements Comparable<Order> {
    private static int nextId = 1000;

    public enum ShippingSpeed {
        STANDARD(5.99, 5),
        RUSH(15.99, 2),
        OVERNIGHT(29.99, 1);

        private final double cost;
        private final int estimatedDays;

        ShippingSpeed(double cost, int estimatedDays) {
            this.cost = cost;
            this.estimatedDays = estimatedDays;
        }

        public double getCost() { return cost; }
        public int getEstimatedDays() { return estimatedDays; }
    }

    public static class OrderItem {
        private final String productId;
        private final String productName;
        private final int quantity;
        private final double unitPrice;
        private final double subtotal;

        public OrderItem(String productId, String productName, int quantity, double unitPrice) {
            this.productId = productId;
            this.productName = productName;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.subtotal = quantity * unitPrice;
        }

        // Getters
        public String getProductId() { return productId; }
        public String getProductName() { return productName; }
        public int getQuantity() { return quantity; }
        public double getUnitPrice() { return unitPrice; }
        public double getSubtotal() { return subtotal; }
    }

    private final String id;
    private final String customerId;
    private final List<OrderItem> items;
    private final LocalDateTime orderDate;
    private final ShippingSpeed shippingSpeed;
    private final String shippingAddress;
    private final double subtotal;
    private final double shippingCost;
    private final double total;
    private boolean shipped;
    private LocalDateTime shippedDate;
    private final int priority;

    public Order(
        String customerId,
        List<OrderItem> items,
        ShippingSpeed shippingSpeed,
        String shippingAddress
    ) {
        validateInputs(customerId, items, shippingAddress);

        synchronized(Order.class) {
            this.id = String.format("O%d", nextId++);
        }
        this.customerId = customerId;
        this.items = new ArrayList<>(items);
        this.orderDate = LocalDateTime.now();
        this.shippingSpeed = shippingSpeed;
        this.shippingAddress = sanitizeAddress(shippingAddress);
        this.subtotal = calculateSubtotal();
        this.shippingCost = shippingSpeed.getCost();
        this.total = subtotal + shippingCost;
        this.shipped = false;
        this.shippedDate = null;
        this.priority = calculatePriority();
    }
    
    //PULL REQUEST FOR FILEHANDLER - ITSNOTVII
    public String toCSVString() {
        String itemsStr = items.stream()
            .map(item -> String.join(":",
                item.getProductId(),
                item.getProductName(),
                String.valueOf(item.getQuantity()),
                String.valueOf(item.getUnitPrice())
            ))
            .collect(Collectors.joining(";"));

        return String.join(",",
            id,
            customerId,
            itemsStr,
            orderDate.toString(),
            shippingSpeed.name(),
            shippingAddress,
            String.valueOf(total),
            String.valueOf(shipped),
            shippedDate != null ? shippedDate.toString() : "null"
        );
    }
    
    // PULL REQUEST FOR FILEHANDLER - ITSNOTVII
    public Order(String[] csvData) {
        this.id = csvData[0];
        this.customerId = csvData[1];
        this.items = parseItemsFromCSV(csvData[2]);
        this.orderDate = LocalDateTime.parse(csvData[3]);
        this.shippingSpeed = ShippingSpeed.valueOf(csvData[4]);
        this.shippingAddress = csvData[5];
        this.total = Double.parseDouble(csvData[6]);
        this.shipped = Boolean.parseBoolean(csvData[7]);
        this.shippedDate = "null".equals(csvData[8]) ? null : LocalDateTime.parse(csvData[8]);
        this.priority = calculatePriority();
    }

    private List<OrderItem> parseItemsFromCSV(String itemsCSV) {
        List<OrderItem> parsedItems = new ArrayList<>();
        String[] itemEntries = itemsCSV.split(";");
        
        for (String entry : itemEntries) {
            String[] parts = entry.split(":");
            parsedItems.add(new OrderItem(
                parts[0], // productId
                parts[1], // productName
                Integer.parseInt(parts[2]), // quantity
                Double.parseDouble(parts[3]) // unitPrice
            ));
        }
        return parsedItems;
    }

    private void validateInputs(
        String customerId,
        List<OrderItem> items,
        String address
    ) {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be empty");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Shipping address cannot be empty");
        }
    }

    private String sanitizeAddress(String address) {
        return address.replaceAll("[\\n\\r]", " ").trim();
    }

    private double calculateSubtotal() {
        return items.stream()
            .mapToDouble(OrderItem::getSubtotal)
            .sum();
    }

    private int calculatePriority() {
        int speedPriority;
        switch (shippingSpeed) {
            case OVERNIGHT: speedPriority = 3; break;
            case RUSH: speedPriority = 2; break;
            default: speedPriority = 1;
        }
        
        // Higher priority = earlier shipping needed
        return (speedPriority * 1_000_000) - 
               (int) (orderDate.toLocalDate().toEpochDay());
    }

    // Getters
    public String getId() { return id; }
    public String getCustomerId() { return customerId; }
    public List<OrderItem> getItems() { return Collections.unmodifiableList(items); }
    public LocalDateTime getOrderDate() { return orderDate; }
    public ShippingSpeed getShippingSpeed() { return shippingSpeed; }
    public String getShippingAddress() { return shippingAddress; }
    public double getSubtotal() { return subtotal; }
    public double getShippingCost() { return shippingCost; }
    public double getTotal() { return total; }
    public boolean isShipped() { return shipped; }
    public LocalDateTime getShippedDate() { return shippedDate; }
    public int getPriority() { return priority; }

    public void ship() {
        if (shipped) {
            throw new IllegalStateException("Order has already been shipped");
        }
        shipped = true;
        shippedDate = LocalDateTime.now();
    }

    public LocalDateTime getEstimatedDeliveryDate() {
        LocalDateTime baseDate = shippedDate != null ? shippedDate : LocalDateTime.now();
        return baseDate.plusDays(shippingSpeed.getEstimatedDays());
    }

    @Override
    public int compareTo(Order other) {
        // Higher priority orders should come first
        return Integer.compare(other.priority, this.priority);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Order)) return false;
        Order other = (Order) obj;
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Order #%s | Customer: %s | Total: $%.2f | Status: %s",
            id, customerId, total, shipped ? "Shipped" : "Pending");
    }
    
    
    
}
