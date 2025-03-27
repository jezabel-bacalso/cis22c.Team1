import java.util.Objects;

// Customer class extending User and representing a customer in the system.
public class Customer extends User {

    // Additional fields specific to a customer.
    private String address;
    private String phone;
    private String city;
    private String state;
    private String zip;

    // Linked lists to store orders that are unshipped and shipped.
    private LinkedList<Order> unshippedOrders;
    private LinkedList<Order> shippedOrders;

    /**
     * The "largest" constructor: 9 args 
     * (firstName, lastName, email, password, address, phone, city, state, zip).
     *
     * @param firstName the customer's first name
     * @param lastName  the customer's last name
     * @param email     the customer's email (unique identifier)
     * @param password  the customer's password
     * @param address   the customer's address
     * @param phone     the customer's phone number
     * @param city      the customer's city
     * @param state     the customer's state
     * @param zip       the customer's zip code
     */
    public Customer(String firstName, String lastName,
                    String email, String password,
                    String address, String phone,
                    String city, String state, String zip)
    {
        // Call to the superclass (User) constructor.
        super(firstName, lastName, email, password);
        this.address = address;
        this.phone = phone;
        this.city = city;
        this.state = state;
        this.zip = zip;
        // Initialize the order lists.
        this.unshippedOrders = new LinkedList<>();
        this.shippedOrders = new LinkedList<>();
    }

    /**
     * 6-arg constructor (no city/state/zip).
     * Calls the 9-arg constructor with empty strings for missing fields.
     */
    public Customer(String firstName, String lastName,
                    String email, String password,
                    String address, String phone)
    {
        this(firstName, lastName, email, password,
             address, phone, "", "", "");
    }

    /**
     * 8-arg constructor (no phone).
     * Calls the 9-arg constructor with an empty string for phone.
     */
    public Customer(String firstName, String lastName,
                    String email, String password,
                    String address, String city,
                    String state, String zip)
    {
        this(firstName, lastName, email, password,
             address, "", city, state, zip);
    }

    /**
     * 4-arg constructor (just name/email/pw).
     * Calls the 9-arg constructor with empty strings for address and contact info.
     */
    public Customer(String firstName, String lastName,
                    String email, String password)
    {
        this(firstName, lastName, email, password,
             "", "", "", "", "");
    }

    /**
     * Returns the role of this user as a customer.
     *
     * @return a string "customer"
     */
    @Override
    public String getRole() {
        return "customer";
    }

    // Getters and setters for address, phone, city, state, and zip.
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getZip() { return zip; }
    public void setZip(String zip) { this.zip = zip; }

    // Returns the list of orders that have not yet been shipped.
    public LinkedList<Order> getUnshippedOrders() { return unshippedOrders; }
    
    // Returns the list of orders that have been shipped.
    public LinkedList<Order> getShippedOrders() { return shippedOrders; }

    /**
     * Adds an order to the list of unshipped orders.
     *
     * @param order the order to be added
     */
    public void addUnshippedOrder(Order order) {
        if (order != null) {
            unshippedOrders.addLast(order);
        }
    }

    /**
     * Move an order from the unshipped orders list to the shipped orders list.
     * This method searches for the order, removes it from unshipped if found,
     * ships the order (if not already shipped), and adds it to shipped orders.
     *
     * @param order the order to be moved
     */
    public void moveOrderToShipped(Order order) {
        if (order == null) return;
        // Initialize the iterator for the unshipped orders list.
        unshippedOrders.positionIterator();
        // Iterate over the unshipped orders to find the specified order.
        while (!unshippedOrders.offEnd()) {
            Order current = unshippedOrders.getIterator();
            // Use Objects.equals for safe comparison (handles nulls)
            if (Objects.equals(current, order)) {
                // Remove the found order from the unshipped list.
                unshippedOrders.removeIterator();
                // If the order hasn't been marked as shipped, mark it as shipped.
                if (!order.isShipped()) {
                    order.ship();
                }
                // Add the order to the shipped orders list.
                shippedOrders.addLast(order);
                return;
            }
            unshippedOrders.advanceIterator();
        }
    }

    /**
     * Checks equality based on the unique email (case-insensitive).
     *
     * @param obj the object to compare with
     * @return true if the emails match (ignoring case), false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Customer)) return false;
        Customer other = (Customer) obj;
        // Typically, email is treated as the unique key.
        return getEmail().equalsIgnoreCase(other.getEmail());
    }

    /**
     * Generates a hash code based on the lowercase email.
     *
     * @return the hash code of the email in lowercase
     */
    @Override
    public int hashCode() {
        return getEmail().toLowerCase().hashCode();
    }

    /**
     * Returns a string representation of the Customer, including personal and contact details.
     *
     * @return a formatted string with customer information
     */
    @Override
    public String toString() {
        return String.format("Customer [%s %s | Email: %s | Addr: %s, %s, %s %s | Phone: %s]",
            getFirstName(), getLastName(), getEmail(),
            address, city, state, zip, phone);
    }
}
