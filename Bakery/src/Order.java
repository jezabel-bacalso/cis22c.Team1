import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order implements Comparable<Order> {

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

        public OrderItem(String productId, String productName,
                         int quantity, double unitPrice)
        {
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be positive");
            }
            if (unitPrice < 0) {
                throw new IllegalArgumentException("Unit price cannot be negative");
            }
            this.productId = productId;
            this.productName = productName;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.subtotal = quantity * unitPrice;
        }

        public String getProductId()   { return productId; }
        public String getProductName() { return productName; }
        public int getQuantity()       { return quantity; }
        public double getUnitPrice()   { return unitPrice; }
        public double getSubtotal()    { return subtotal; }
    }

    private static int nextId = 1000;

    private String id;  // not final, so we can override if needed
    private final String customerId;
    private final List<OrderItem> items;
    private final LocalDateTime orderDate;
    private final ShippingSpeed shippingSpeed;
    private final String shippingAddress;

    private boolean shipped;
    private LocalDateTime shippedDate;
    private int priority;
    private double subtotal;
    private double shippingCost;
    private double total;

    // Full constructor
    public Order(String customerId,
                 List<OrderItem> items,
                 ShippingSpeed shippingSpeed,
                 String shippingAddress)
    {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be empty");
        }
        if (items == null) {
            throw new IllegalArgumentException("Order items cannot be null");
        }
        synchronized (Order.class) {
            this.id = "O" + (nextId++);
        }
        this.customerId = customerId;
        this.items = new ArrayList<>(items);
        this.orderDate = LocalDateTime.now();
        this.shippingSpeed = (shippingSpeed == null) ? ShippingSpeed.STANDARD : shippingSpeed;
        this.shippingAddress = (shippingAddress == null) ? "" : shippingAddress;
        this.shipped = false;
        this.shippedDate = null;
        recalcTotals();
        computePriority();
    }

    // Extra: constructor with just shipping speed
    public Order(ShippingSpeed speed) {
        this("guest@noemail", new ArrayList<>(), speed, "");
    }

    // Extra: constructor with (customerId, shippingSpeed)
    public Order(String customerId, ShippingSpeed speed) {
        this(customerId, new ArrayList<>(), speed, "");
    }

    // Extra: constructor with (orderId, custEmail, shippingSpeed) if needed
    // (only if you truly want to pass a custom ID).
    public Order(String orderId, String customerId, ShippingSpeed speed) {
        this(customerId, new ArrayList<>(), speed, "");
        this.id = orderId;  // override auto-generated
    }

    private void recalcTotals() {
        this.subtotal = 0.0;
        for (OrderItem it : items) {
            this.subtotal += it.getSubtotal();
        }
        this.shippingCost = shippingSpeed.getCost();
        this.total = subtotal + shippingCost;
    }

    private void computePriority() {
        // Example approach: speed * 1,000,000 minus day index
        int speedLevel = 1;
        switch (shippingSpeed) {
            case OVERNIGHT: speedLevel = 3; break;
            case RUSH:      speedLevel = 2; break;
            case STANDARD:  speedLevel = 1; break;
        }
        int dateFactor = (int) orderDate.toLocalDate().toEpochDay();
        this.priority = (speedLevel * 1_000_000) - dateFactor;
    }

    public void addItem(OrderItem item) {
        items.add(item);
        recalcTotals();
        computePriority();
    }

    public String getId() { return id; }
    public String getCustomerId() { return customerId; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public ShippingSpeed getShippingSpeed() { return shippingSpeed; }
    public String getShippingAddress() { return shippingAddress; }

    public boolean isShipped() { return shipped; }
    public LocalDateTime getShippedDate() { return shippedDate; }

    public double getSubtotal() { return subtotal; }
    public double getShippingCost() { return shippingCost; }
    public double getTotal() { return total; }
    public int getPriority() { return priority; }
    public List<OrderItem> getItems() { return Collections.unmodifiableList(items); }

    public void ship() {
        if (!shipped) {
            shipped = true;
            shippedDate = LocalDateTime.now();
        }
    }

    public void markShipped() {
        ship();
    }

    @Override
    public int compareTo(Order other) {
        // higher priority first
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
        return String.format("Order %s (Cust=%s, $%.2f, %s)",
            id, customerId, total, shipped ? "Shipped" : "Pending");
    }
}
