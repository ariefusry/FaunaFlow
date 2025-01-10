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
    }

    public ArrayList<Employee> getAllEmployees() {
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

    public boolean login(String username, String password) {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?"; // Table name: account
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String accountUsername = rs.getString("username");
                Integer employeeId = rs.getObject("employee_id", Integer.class);
                if (employeeId != null) {
                    currentUser = new Employee(employeeId, accountUsername); // Set currentUser with username and employeeId
                } else {
                    currentUser = new Employee(0, accountUsername); // Set currentUser with username only for admin
                }
                System.out.println("Login successful. Current user: " + currentUser.getNama());
                logDataProcessingChange(currentUser.getNama(), "User logged in");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error during login: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public void logout() {
        if (currentUser != null) {
            logDataProcessingChange(currentUser.getNama(), "User logged out");
            currentUser = null;
        }
    }

    public void removeEmployee(int idEmployee) {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            // Start a transaction
            conn.setAutoCommit(false);

            // Remove the employee
            String sqlEmployee = "DELETE FROM employee WHERE id = ?";
            PreparedStatement stmtEmployee = conn.prepareStatement(sqlEmployee);
            stmtEmployee.setInt(1, idEmployee);
            int rowsAffectedEmployee = stmtEmployee.executeUpdate();

            // Remove the associated account
            String sqlAccount = "DELETE FROM account WHERE employee_id = ?";
            PreparedStatement stmtAccount = conn.prepareStatement(sqlAccount);
            stmtAccount.setInt(1, idEmployee);
            int rowsAffectedAccount = stmtAccount.executeUpdate();

            if (rowsAffectedEmployee > 0) {
                conn.commit(); // Commit the transaction
                logDataProcessingChange(currentUser.getNama(), "Removed employee with ID: " + idEmployee + " and associated account");
                System.out.println("Employee and associated account removed successfully!");
            } else {
                conn.rollback(); // Rollback the transaction
                System.out.println("Employee with ID: " + idEmployee + " not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error removing employee and associated account from database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void logDataProcessingChange(String username, String changeDescription) {
        if (currentUser == null) {
            System.out.println("currentUser is null, cannot log data processing change.");
            return;
        }
        try (Connection conn = FaunaFlowGG.getConnection()) {
            // Check if the username exists in the account table
            String checkUserSql = "SELECT username FROM account WHERE username = ?";
            PreparedStatement checkUserStmt = conn.prepareStatement(checkUserSql);
            checkUserStmt.setString(1, currentUser.getNama());
            ResultSet rs = checkUserStmt.executeQuery();
            if (!rs.next()) {
                System.out.println("Error: Username " + currentUser.getNama() + " does not exist in the account table.");
                return;
            }

            String sql = "INSERT INTO data_processing_log (username, change_description) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, currentUser.getNama());
            stmt.setString(2, changeDescription);
            stmt.executeUpdate();
            System.out.println("Data processing change logged successfully!");
        } catch (SQLException e) {
            System.out.println("Error logging data processing change: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean isAdmin() {
        if (currentUser == null) {
            return false;
        }
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "SELECT role FROM account WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, currentUser.getNama());
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

    public Employee getCurrentUser() {
        return currentUser;
    }
}

