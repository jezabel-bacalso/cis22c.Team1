import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents an Order with details about the customer, items, shipping, and totals.
 * Implements Comparable to support priority ordering.
 */
public class Order implements Comparable<Order> {

    // Enum representing different shipping speeds along with cost and estimated delivery days.
    public enum ShippingSpeed {
        STANDARD(5.99, 5),
        RUSH(15.99, 2),
        OVERNIGHT(29.99, 1);

        private final double cost;
        private final int estimatedDays;
        
        // Constructor for each shipping speed.
        ShippingSpeed(double cost, int estimatedDays) {
            this.cost = cost;
            this.estimatedDays = estimatedDays;
        }
        
        // Returns the shipping cost.
        public double getCost() { return cost; }
        // Returns the estimated delivery days.
        public int getEstimatedDays() { return estimatedDays; }
    }

    // Inner static class representing an item in the order.
    public static class OrderItem {
        private final String productId;
        private final String productName;
        private final int quantity;
        private final double unitPrice;
        private final double subtotal;

        /**
         * Constructs an OrderItem and calculates its subtotal.
         * Throws an exception if quantity is not positive or unit price is negative.
         *
         * @param productId   the product's identifier
         * @param productName the product's name
         * @param quantity    the quantity ordered (must be > 0)
         * @param unitPrice   the price per unit (cannot be negative)
         */
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
            // Calculate subtotal for the order item.
            this.subtotal = quantity * unitPrice;
        }

        public String getProductId()   { return productId; }
        public String getProductName() { return productName; }
        public int getQuantity()       { return quantity; }
        public double getUnitPrice()   { return unitPrice; }
        public double getSubtotal()    { return subtotal; }
    }

    // Static counter for generating unique order IDs.
    private static int nextId = 1000;

    // Order fields
    private String id;  // Unique order identifier (modifiable to allow custom IDs)
    private final String customerId;
    private final List<OrderItem> items;
    private final LocalDateTime orderDate;
    private final ShippingSpeed shippingSpeed;
    private final String shippingAddress;

    private boolean shipped;
    private LocalDateTime shippedDate;
    private int priority;       // Calculated priority for order processing
    private double subtotal;    // Sum of item subtotals
    private double shippingCost;
    private double total;       // subtotal + shippingCost

    /**
     * Full constructor for creating an Order with all details.
     * Calculates totals and sets order priority.
     *
     * @param customerId      the customer identifier (must be non-empty)
     * @param items           the list of order items (must not be null)
     * @param shippingSpeed   the shipping speed (null defaults to STANDARD)
     * @param shippingAddress the shipping address (null defaults to empty string)
     */
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
        // Synchronized block to safely generate unique order ID.
        synchronized (Order.class) {
            this.id = "O" + (nextId++);
        }
        this.customerId = customerId;
        // Create a defensive copy of the order items.
        this.items = new ArrayList<>(items);
        // Capture the order creation date/time.
        this.orderDate = LocalDateTime.now();
        // Use provided shipping speed or default to STANDARD.
        this.shippingSpeed = (shippingSpeed == null) ? ShippingSpeed.STANDARD : shippingSpeed;
        // Use provided shipping address or default to empty string.
        this.shippingAddress = (shippingAddress == null) ? "" : shippingAddress;
        this.shipped = false;
        this.shippedDate = null;
        // Calculate order totals and priority.
        recalcTotals();
        computePriority();
    }

    // Extra constructors for convenience

    /**
     * Constructor with just shipping speed.
     * Defaults customer ID to "guest@noemail" and no order items.
     *
     * @param speed the shipping speed
     */
    public Order(ShippingSpeed speed) {
        this("guest@noemail", new ArrayList<>(), speed, "");
    }

    /**
     * Constructor with customerId and shipping speed.
     * Defaults with no order items.
     *
     * @param customerId the customer identifier
     * @param speed      the shipping speed
     */
    public Order(String customerId, ShippingSpeed speed) {
        this(customerId, new ArrayList<>(), speed, "");
    }

    /**
     * Constructor that allows setting a custom order ID.
     * Useful if you need to override the auto-generated ID.
     *
     * @param orderId    the custom order ID
     * @param customerId the customer identifier
     * @param speed      the shipping speed
     */
    public Order(String orderId, String customerId, ShippingSpeed speed) {
        this(customerId, new ArrayList<>(), speed, "");
        this.id = orderId;  // Override auto-generated ID.
    }

    /**
     * Recalculates the order totals (subtotal, shipping cost, and overall total)
     * based on the current list of order items.
     */
    private void recalcTotals() {
        this.subtotal = 0.0;
        for (OrderItem it : items) {
            this.subtotal += it.getSubtotal();
        }
        this.shippingCost = shippingSpeed.getCost();
        this.total = subtotal + shippingCost;
    }

    /**
     * Computes the priority of the order based on shipping speed and order date.
     * Higher shipping speed yields higher priority.
     */
    private void computePriority() {
        // Assign a speed level where higher value means higher priority.
        int speedLevel = 1;
        switch (shippingSpeed) {
            case OVERNIGHT: speedLevel = 3; break;
            case RUSH:      speedLevel = 2; break;
            case STANDARD:  speedLevel = 1; break;
        }
        // Use order date (converted to epoch day) as a factor.
        int dateFactor = (int) orderDate.toLocalDate().toEpochDay();
        // Calculate priority so that higher speed and earlier dates result in higher priority.
        this.priority = (speedLevel * 1_000_000) - dateFactor;
    }

    /**
     * Adds an item to the order, then recalculates totals and priority.
     *
     * @param item the OrderItem to add.
     */
    public void addItem(OrderItem item) {
        items.add(item);
        recalcTotals();
        computePriority();
    }

    // Accessor methods

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
    // Returns an unmodifiable view of the order items.
    public List<OrderItem> getItems() { return Collections.unmodifiableList(items); }

    /**
     * Marks the order as shipped if it is not already shipped.
     * Captures the shipment date/time.
     */
    public void ship() {
        if (!shipped) {
            shipped = true;
            shippedDate = LocalDateTime.now();
        }
    }

    /**
     * Marks the order as shipped.
     * This method is provided as an alias for ship().
     */
    public void markShipped() {
        ship();
    }

    /**
     * Compares this order with another based on priority.
     * Orders with higher priority come first.
     *
     * @param other the other order to compare to.
     * @return negative if this order has higher priority, positive if lower.
     */
    @Override
    public int compareTo(Order other) {
        // Compare so that orders with higher priority (larger number) are "less than"
        // in ordering to appear first in sorted collections.
        return Integer.compare(other.priority, this.priority);
    }

    /**
     * Checks equality based on the unique order ID.
     *
     * @param obj the object to compare.
     * @return true if the order IDs are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Order)) return false;
        Order other = (Order) obj;
        return this.id.equals(other.id);
    }

    /**
     * Generates a hash code based on the order ID.
     *
     * @return the hash code for the order.
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * Returns a formatted string representation of the order,
     * including the order ID, customer, total cost, and shipment status.
     *
     * @return a string summarizing the order.
     */
    @Override
    public String toString() {
        return String.format("Order %s (Cust=%s, $%.2f, %s)",
            id, customerId, total, shipped ? "Shipped" : "Pending");
    }
}
