import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.io.*;

public class Order implements Comparable<Order> {
    public enum ShippingSpeed {
        STANDARD(1, 5.99), RUSH(2, 15.99), OVERNIGHT(3, 29.99);

        final int priority;
        final double cost;

        ShippingSpeed(int priority, double cost) {
            this.priority = priority;
            this.cost = cost;
        }
    }

    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1000);
    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final int orderId;
    private final String customerId;
    private final LocalDateTime orderTime;
    private final List<OrderItem> items;
    private final ShippingSpeed speed;
    private final String shippingAddress;
    private final double total;
    private final int priority;
    private boolean shipped;

    public Order(String customerId, ShippingSpeed speed, String address) {
        this.orderId = ID_GENERATOR.getAndIncrement();
        this.customerId = validateCustomerId(customerId);
        this.orderTime = LocalDateTime.now();
        this.speed = speed;
        this.shippingAddress = validateAddress(address);
        this.items = new ArrayList<>();
        this.priority = calculatePriority();
        this.total = 0.0;
    }

    private String validateCustomerId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid customer ID");
        }
        return id.trim();
    }

    private String validateAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid shipping address");
        }
        return address.replace("\n", " ").trim();
    }

    private int calculatePriority() {
        return (speed.priority * 1_000_000_000) - 
               (int) orderTime.toEpochSecond(ZoneOffset.UTC);
    }

    public boolean addItem(Product product, int quantity) {
        if (product == null || quantity < 1) return false;
        if (product.getStock() < quantity) return false;

        synchronized (this) {
            product.setStock(product.getStock() - quantity);
            items.add(new OrderItem(product, quantity));
            total = items.stream()
                .mapToDouble(OrderItem::getPrice)
                .sum() + speed.cost;
            return true;
        }
    }

    public String toCSV() {
        return String.format("%d,%s,%s,%s,%b,%s,%.2f%s",
            orderId,
            escape(customerId),
            orderTime.format(DATE_FORMATTER),
            speed.name(),
            shipped,
            escape(shippingAddress),
            total,
            items.stream()
                .map(i -> "," + escape(i.productName) + "," + i.quantity)
                .collect(Collectors.joining())
        );
    }

    public static Order fromCSV(String csv, Map<String, Product> products) {
        String[] parts = unescape(csv).split(",", -1);
        if (parts.length < 7) throw new IllegalArgumentException("Invalid CSV format");

        Order order = new Order(
            parts[1],
            ShippingSpeed.valueOf(parts[3]),
            parts[5]
        );

        order.orderId = Integer.parseInt(parts[0]);
        order.orderTime = LocalDateTime.parse(parts[2], DATE_FORMATTER);
        order.shipped = Boolean.parseBoolean(parts[4]);
        order.total = Double.parseDouble(parts[6]);

        for (int i = 7; i < parts.length; i += 2) {
            if (i+1 >= parts.length) break;
            
            Product p = products.get(parts[i]);
            if (p != null) {
                order.items.add(new OrderItem(p, Integer.parseInt(parts[i+1])));
            }
        }

        updateIdGenerator(order.orderId);
        return order;
    }

    private static void updateIdGenerator(int loadedId) {
        ID_GENERATOR.updateAndGet(current -> 
            Math.max(current, loadedId + 1));
    }

    private static String escape(String input) {
        return input.replace(",", "<comma>")
                    .replace("\n", "<newline>")
                    .replace("\\", "<backslash>");
    }

    private static String unescape(String input) {
        return input.replace("<comma>", ",")
                    .replace("<newline>", "\n")
                    .replace("<backslash>", "\\");
    }

    // OrderItem inner class
    private static class OrderItem {
        final String productName;
        final int quantity;
        final double unitPrice;

        OrderItem(Product product, int quantity) {
            this.productName = product.getProductName();
            this.quantity = quantity;
            this.unitPrice = product.getPrice();
        }

        double getPrice() {
            return unitPrice * quantity;
        }
    }

    // Getters and comparison methods
    public int getOrderId() { return orderId; }
    public String getCustomerId() { return customerId; }
    public LocalDateTime getOrderTime() { return orderTime; }
    public List<OrderItem> getItems() { return Collections.unmodifiableList(items); }
    public ShippingSpeed getSpeed() { return speed; }
    public boolean isShipped() { return shipped; }
    public String getAddress() { return shippingAddress; }
    public double getTotal() { return total; }

    @Override
    public int compareTo(Order o) {
        return Integer.compare(o.priority, priority);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Order && this.orderId == ((Order) o).orderId;
    }

    @Override
    public int hashCode() {
        return orderId;
    }

    @Override
    public String toString() {
        return String.format("Order #%d - %s - %s - Total: $%.2f",
            orderId, customerId, orderTime.format(DateTimeFormatter.ISO_LOCAL_DATE), total);
    }
}