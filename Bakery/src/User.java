public abstract class User {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = (firstName == null) ? "" : firstName.trim();
        this.lastName  = (lastName == null)  ? "" : lastName.trim();
        this.email     = (email == null)     ? "" : email.trim().toLowerCase();
        this.password  = (password == null)  ? "" : password.trim();
    }

    public String getFirstName() { return firstName; }
    public String getLastName()  { return lastName; }
    public String getEmail()     { return email; }
    public String getPassword()  { return password; }

    public void setFirstName(String firstName) {
        this.firstName = firstName.trim();
    }

    public void setLastName(String lastName) {
        this.lastName = lastName.trim();
    }

    public void setEmail(String email) {
        this.email = email.trim().toLowerCase();
    }

    public void setPassword(String password) {
        this.password = password.trim();
    }

    // Each subclass (Customer/Employee) will define getRole() as "customer" or "employee"/"manager".
    public abstract String getRole();

    @Override
    public String toString() {
        return String.format("Name: %s %s | Email: %s | Password: %s",
                firstName, lastName, email, password);
    }
}
