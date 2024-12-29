/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.faunaflowgg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author ARIEF
 */
public class Manager {
    private ArrayList<Employee> employees;

    public Manager() {
        employees = new ArrayList<>();
        loadEmployeesFromDatabase();
    }

    private void loadEmployeesFromDatabase() {
        employees.clear();
        try (Connection conn = FaunaFlowGG.getConnection()) {
            if (conn == null) {
                System.out.println("Failed to establish a database connection.");
                return;
            }
            String sql = "SELECT * FROM employee"; // Table name: employee
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Employee employee = new Employee(
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getInt("usia"),
                    rs.getString("alamat"),
                    rs.getString("notel")
                );
                employees.add(employee);
            }
        } catch (SQLException e) {
            System.out.println("Error loading employees from database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public ArrayList<Employee> getEmployees() {
        loadEmployeesFromDatabase(); // Ensure the list is up-to-date
        return employees;
    }

    public Object[][] getEmployeeData() {
        ArrayList<Employee> employees = getEmployees();
        Object[][] data = new Object[employees.size()][5];

        for (int i = 0; i < employees.size(); i++) {
            Employee employee = employees.get(i);
            data[i][0] = employee.getIdEmployee();
            data[i][1] = employee.getNama();
            data[i][2] = employee.getUsia();
            data[i][3] = employee.getAlamat();
            data[i][4] = employee.getNoTelp();
        }

        return data;
    }

    public void addEmployee(Employee employee) {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "INSERT INTO employee (nama, usia, alamat, notel) VALUES (?, ?, ?, ?)"; // Table name: employee
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, employee.getNama());
            stmt.setInt(2, employee.getUsia());
            stmt.setString(3, employee.getAlamat());
            stmt.setString(4, employee.getNoTelp());
            stmt.executeUpdate();
            loadEmployeesFromDatabase(); // Refresh the list from the database
        } catch (SQLException e) {
            System.out.println("Error adding employee to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void removeEmployee(int idEmployee) {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "DELETE FROM employee WHERE id = ?"; // Correct table name: employee
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idEmployee);
            stmt.executeUpdate();
            loadEmployeesFromDatabase(); // Refresh the list from the database
        } catch (SQLException e) {
            System.out.println("Error removing employee from database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setHewanToKandang(int idKandang, int idHewan) {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            // Update kandang table
            String sqlKandang = "UPDATE kandang SET idHewan = ? WHERE id = ?";
            PreparedStatement stmtKandang = conn.prepareStatement(sqlKandang);
            stmtKandang.setInt(1, idHewan);
            stmtKandang.setInt(2, idKandang);
            stmtKandang.executeUpdate();

            // Update hewan table
            String sqlHewan = "UPDATE hewan SET idKandang = ? WHERE idHewan = ?";
            PreparedStatement stmtHewan = conn.prepareStatement(sqlHewan);
            stmtHewan.setInt(1, idKandang);
            stmtHewan.setInt(2, idHewan);
            stmtHewan.executeUpdate();

            System.out.println("Hewan set to kandang successfully!");
        } catch (SQLException e) {
            System.out.println("Error setting hewan to kandang: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void assignRandomJobdesks() {
        List<String> jobdesks = List.of("Membersihkan kandang", "Memberi makan hewan", "Merawat hewan", "Tour Guide");
        Random random = new Random();

        try (Connection conn = FaunaFlowGG.getConnection()) {
            String insertSQL = "INSERT INTO JobdeskKaryawan (nama_karyawan, senin, selasa, rabu, kamis, jumat, sabtu) VALUES (?, ?, ?, ?, ?, ?, ?)";
            List<Employee> employees = getEmployees();

            for (Employee employee : employees) {
                List<String> shuffledJobdesks = new ArrayList<>(jobdesks);
                Collections.shuffle(shuffledJobdesks, random);

                try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                    pstmt.setString(1, employee.getNama());
                    pstmt.setString(2, shuffledJobdesks.get(0)); // Senin
                    pstmt.setString(3, shuffledJobdesks.get(1)); // Selasa
                    pstmt.setString(4, shuffledJobdesks.get(2)); // Rabu
                    pstmt.setString(5, shuffledJobdesks.get(3)); // Kamis
                    pstmt.setString(6, shuffledJobdesks.get(0)); // Jumat
                    pstmt.setString(7, shuffledJobdesks.get(1)); // Sabtu

                    pstmt.executeUpdate();
                }
            }

            System.out.println("Jobdesk karyawan berhasil di-randomize dan disimpan ke database.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Object[][] getJobdeskKaryawanData() {
        ArrayList<Object[]> jobdeskList = new ArrayList<>();
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "SELECT * FROM JobdeskKaryawan"; // Table name: JobdeskKaryawan
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] jobdesk = new Object[8];
                jobdesk[0] = rs.getInt("id");
                jobdesk[1] = rs.getString("nama_karyawan");
                jobdesk[2] = rs.getString("senin");
                jobdesk[3] = rs.getString("selasa");
                jobdesk[4] = rs.getString("rabu");
                jobdesk[5] = rs.getString("kamis");
                jobdesk[6] = rs.getString("jumat");
                jobdesk[7] = rs.getString("sabtu");
                jobdeskList.add(jobdesk);
            }
        } catch (SQLException e) {
            System.out.println("Error getting JobdeskKaryawan data from database: " + e.getMessage());
            e.printStackTrace();
        }
        return jobdeskList.toArray(new Object[0][0]);
    }

    public void deleteAllJobdeskKaryawan() {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "DELETE FROM JobdeskKaryawan";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
            System.out.println("All JobdeskKaryawan data deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting all JobdeskKaryawan data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Object[][] getAllReports() {
        ArrayList<Object[]> reportList = new ArrayList<>();
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "SELECT * FROM Laporan"; // Table name: Laporan
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] report = new Object[4];
                report[0] = rs.getInt("id");
                report[1] = rs.getString("nama");
                report[2] = rs.getString("laporan");
                report[3] = rs.getTimestamp("tanggal");
                reportList.add(report);
            }
        } catch (SQLException e) {
            System.out.println("Error getting reports from database: " + e.getMessage());
            e.printStackTrace();
        }
        return reportList.toArray(new Object[0][0]);
    }
}
