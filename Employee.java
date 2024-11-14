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
}