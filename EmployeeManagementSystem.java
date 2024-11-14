import java.util.ArrayList;

public class EmployeeManagementSystem extends Employee{
    private ArrayList<Employee> employees;
    private Employee currentUser;

    public EmployeeManagementSystem() {
        employees = new ArrayList<Employee>();
        this.currentUser = null;
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }
}
