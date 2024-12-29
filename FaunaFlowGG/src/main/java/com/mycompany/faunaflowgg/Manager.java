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
            String sql = "UPDATE kandang SET idHewan = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idHewan);
            stmt.setInt(2, idKandang);
            stmt.executeUpdate();
            System.out.println("Hewan set to kandang successfully!");
        } catch (SQLException e) {
            System.out.println("Error setting hewan to kandang: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
