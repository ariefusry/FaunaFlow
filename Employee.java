import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

public abstract class Employee {
    protected String idEmployee;
    protected String nama;
    protected int usia;
    protected String alamat;
    protected String noTelp;
    protected String username;
    protected String password;
    protected String role;

    public Employee(String idEmployee, String nama, int usia, String alamat, String noTelp, String username, String password, String role) {
        this.idEmployee = idEmployee;
        this.nama = nama;
        this.usia = usia;
        this.alamat = alamat;
        this.noTelp = noTelp;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getIdEmployee() {
        return idEmployee;
    }

    public String getNama() {
        return nama;
    }

    public int getUsia() {
        return usia;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setUsia(int usia) {
        this.usia = usia;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(role);
    }

    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public void displayEmployeeDetails() {
        JFrame frame = new JFrame("Employee Details");
        frame.setSize(300, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(0, 2));

        frame.add(new JLabel("ID:"));
        frame.add(new JLabel(idEmployee));

        frame.add(new JLabel("Name:"));
        frame.add(new JLabel(nama));

        frame.add(new JLabel("Age:"));
        frame.add(new JLabel(String.valueOf(usia)));

        frame.add(new JLabel("Address:"));
        frame.add(new JLabel(alamat));

        frame.add(new JLabel("Phone:"));
        frame.add(new JLabel(noTelp));

        frame.add(new JLabel("Username:"));
        frame.add(new JLabel(username));

        frame.add(new JLabel("Role:"));
        frame.add(new JLabel(role));

        frame.setVisible(true);
    }
}