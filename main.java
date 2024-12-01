public class main {
    public static void main(String[] args) {
        EmployeeManagementSystem system = new EmployeeManagementSystem();

        // Create some employees
        Employee admin = new Employee("E1", "Admin User", 35, "Admin Address", "1234567890", "admin", "adminpass", "admin") {
            // Implement abstract methods if any
        };
        Employee user = new Employee("E2", "Regular User", 28, "User Address", "0987654321", "user", "userpass", "user") {
            // Implement abstract methods if any
        };

        // Register employees
        system.register(admin);
        system.register(user);

        // Display GUI
        system.initializeGUI();
    }
}
