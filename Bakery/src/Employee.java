public class Employee extends User {

    private boolean isManager;

    public Employee(String firstName, String lastName,
                    String email, String password,
                    boolean isManager)
    {
        super(firstName, lastName, email, password);
        this.isManager = isManager;
    }

    public boolean isManager() { return isManager; }
    public void setManager(boolean manager) { this.isManager = manager; }

    @Override
    public String getRole() {
        return isManager ? "manager" : "employee";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Employee)) return false;
        Employee other = (Employee) obj;
        return getEmail().equalsIgnoreCase(other.getEmail());
    }

    @Override
    public int hashCode() {
        return getEmail().toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return String.format("Employee [%s %s | Email: %s | Role: %s]",
            getFirstName(), getLastName(), getEmail(),
            (isManager ? "Manager" : "Employee"));
    }
}
