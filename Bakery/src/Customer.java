import java.util.Objects;

public class Customer extends User {

    private String address;
    private String phone;
    private String city;
    private String state;
    private String zip;

    private LinkedList<Order> unshippedOrders;
    private LinkedList<Order> shippedOrders;

    /** 
     * The "largest" constructor: 9 args 
     * (firstName, lastName, email, password, address, phone, city, state, zip).
     */
    public Customer(String firstName, String lastName,
                    String email, String password,
                    String address, String phone,
                    String city, String state, String zip)
    {
        super(firstName, lastName, email, password);
        this.address = address;
        this.phone = phone;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.unshippedOrders = new LinkedList<>();
        this.shippedOrders = new LinkedList<>();
    }

    /** 
     * 6-arg constructor (no city/state/zip).
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
     */
    public Customer(String firstName, String lastName,
                    String email, String password)
    {
        this(firstName, lastName, email, password,
             "", "", "", "", "");
    }

    @Override
    public String getRole() {
        return "customer";
    }

    // getters/setters
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

    public LinkedList<Order> getUnshippedOrders() { return unshippedOrders; }
    public LinkedList<Order> getShippedOrders() { return shippedOrders; }

    public void addUnshippedOrder(Order order) {
        if (order != null) {
            unshippedOrders.addLast(order);
        }
    }

    /** 
     * Move an order from unshipped to shipped (if found).
     */
    public void moveOrderToShipped(Order order) {
        if (order == null) return;
        unshippedOrders.positionIterator();
        while (!unshippedOrders.offEnd()) {
            Order current = unshippedOrders.getIterator();
            if (Objects.equals(current, order)) {
                unshippedOrders.removeIterator();
                if (!order.isShipped()) {
                    order.ship();
                }
                shippedOrders.addLast(order);
                return;
            }
            unshippedOrders.advanceIterator();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Customer)) return false;
        Customer other = (Customer) obj;
        // Typically, we treat email as the unique key
        return getEmail().equalsIgnoreCase(other.getEmail());
    }

    @Override
    public int hashCode() {
        return getEmail().toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return String.format("Customer [%s %s | Email: %s | Addr: %s, %s, %s %s | Phone: %s]",
            getFirstName(), getLastName(), getEmail(),
            address, city, state, zip, phone);
    }
}

