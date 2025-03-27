
public abstract class User {
    // Private fields for storing user details.
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    /**
     * Constructs a User with the specified details.
     * If any parameter is null, it defaults to an empty string.
     * Email is converted to lowercase after trimming.
     *
     * @param firstName the user's first name
     * @param lastName  the user's last name
     * @param email     the user's email (used as a unique identifier)
     * @param password  the user's password
     */
    public User(String firstName, String lastName, String email, String password) {
        // Trim input strings and handle null values.
        this.firstName = (firstName == null) ? "" : firstName.trim();
        this.lastName  = (lastName == null)  ? "" : lastName.trim();
        // Convert email to lowercase for consistent comparisons.
        this.email     = (email == null)     ? "" : email.trim().toLowerCase();
        this.password  = (password == null)  ? "" : password.trim();
    }

    // Getter methods for user details.
    public String getFirstName() { return firstName; }
    public String getLastName()  { return lastName; }
    public String getEmail()     { return email; }
    public String getPassword()  { return password; }

    // Setter methods for updating user details.
    public void setFirstName(String firstName) {
        this.firstName = firstName.trim();
    }

    public void setLastName(String lastName) {
        this.lastName = lastName.trim();
    }

    public void setEmail(String email) {
        // Trim and convert email to lowercase.
        this.email = email.trim().toLowerCase();
    }

    public void setPassword(String password) {
        this.password = password.trim();
    }

    /**
     * Abstract method to get the role of the user.
     * Subclasses (e.g., Customer, Employee) must implement this method
     * to return a string indicating the user role (such as "customer", "employee", or "manager").
     *
     * @return the role of the user.
     */
    public abstract String getRole();

    /**
     * Returns a string representation of the user, including name, email, and password.
     *
     * @return a formatted string summarizing the user's information.
     */
    @Override
    public String toString() {
        return String.format("Name: %s %s | Email: %s | Password: %s",
                firstName, lastName, email, password);
    }
}
