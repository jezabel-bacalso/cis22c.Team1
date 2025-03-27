// The Employee class extends User and represents an employee in the system.
// It includes an additional property to indicate if the employee has manager privileges.
public class Employee extends User {

    // Flag to determine if the employee is a manager.
    private boolean isManager;

    /**
     * Constructor for creating an Employee.
     *
     * @param firstName the employee's first name
     * @param lastName  the employee's last name
     * @param email     the employee's email (unique identifier)
     * @param password  the employee's password
     * @param isManager true if the employee has manager privileges; false otherwise
     */
    public Employee(String firstName, String lastName,
                    String email, String password,
                    boolean isManager)
    {
        // Initialize the User superclass with basic details.
        super(firstName, lastName, email, password);
        this.isManager = isManager;
    }

    /**
     * Returns whether this employee is a manager.
     *
     * @return true if the employee is a manager; false otherwise
     */
    public boolean isManager() { return isManager; }

    /**
     * Sets the manager status for this employee.
     *
     * @param manager true to set as manager, false otherwise
     */
    public void setManager(boolean manager) { this.isManager = manager; }

    /**
     * Overrides the getRole() method from User.
     * Returns "manager" if the employee is a manager; otherwise, "employee".
     *
     * @return a string representing the employee's role
     */
    @Override
    public String getRole() {
        return isManager ? "manager" : "employee";
    }

    /**
     * Checks equality between this employee and another object.
     * Two employees are considered equal if their emails match (case-insensitive).
     *
     * @param obj the object to compare with
     * @return true if the emails are equal; false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Employee)) return false;
        Employee other = (Employee) obj;
        return getEmail().equalsIgnoreCase(other.getEmail());
    }

    /**
     * Generates a hash code based on the employee's email (converted to lowercase).
     *
     * @return the hash code of the email
     */
    @Override
    public int hashCode() {
        return getEmail().toLowerCase().hashCode();
    }

    /**
     * Returns a string representation of the employee, including their name, email, and role.
     *
     * @return a formatted string with the employee's details
     */
    @Override
    public String toString() {
        return String.format("Employee [%s %s | Email: %s | Role: %s]",
            getFirstName(), getLastName(), getEmail(),
            (isManager ? "Manager" : "Employee"));
    }
}
