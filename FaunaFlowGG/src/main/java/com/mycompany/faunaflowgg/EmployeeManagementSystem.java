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

public class EmployeeManagementSystem {
    private ArrayList<Employee> employees;
    private Employee currentUser;

    public EmployeeManagementSystem() {
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
            String sql = "DELETE FROM account WHERE id = ?"; // Table name: account
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idEmployee);
            stmt.executeUpdate();
            loadEmployeesFromDatabase(); // Refresh the list from the database
        } catch (SQLException e) {
            System.out.println("Error removing employee from database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public ArrayList<Employee> getAllEmployees() {
        loadEmployeesFromDatabase(); // Ensure the list is up-to-date
        return employees;
    }

    public Employee getEmployeeById(int idEmployee) {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "SELECT * FROM employee WHERE id = ?"; // Table name: employee
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idEmployee);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Employee(
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getInt("usia"),
                    rs.getString("alamat"),
                    rs.getString("notel")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error getting employee by ID from database: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Employee getEmployeeByUsername(String username) {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "SELECT e.id, e.nama, e.usia, e.alamat, e.notel FROM employee e JOIN account a ON e.id = a.id WHERE a.username = ?"; // Join employee and account tables
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Employee(
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getInt("usia"),
                    rs.getString("alamat"),
                    rs.getString("notel")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error getting employee by username from database: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean login(String username, String password) {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?"; // Table name: account
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                currentUser = new Employee(
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getInt("usia"),
                    rs.getString("alamat"),
                    rs.getString("notel")
                );
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error during login: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public void logout() {
        currentUser = null;
    }

    public boolean isAdmin() {
        if (currentUser == null) {
            return false;
        }
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "SELECT role FROM account WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, currentUser.getIdEmployee());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return "admin".equalsIgnoreCase(rs.getString("role"));
            }
        } catch (SQLException e) {
            System.out.println("Error checking admin role: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean isUser() {
        return currentUser != null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}

