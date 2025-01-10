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
public class Hewan {
    private String nama;
    private int umur;
    private int jumlah;
    private double berat;
    private Employee currentUser; // Add this field

    public Hewan(String nama, int umur, int jumlah, double berat, Employee currentUser) { // Update constructor
        this.nama = nama;
        this.umur = umur;
        this.jumlah = jumlah;
        this.berat = berat;
        this.currentUser = currentUser;
    }

    public void addHewan(String nama, int umur, int jumlah, double berat) {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "INSERT INTO hewan (nama, umur, jumlah, berat) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nama);
            stmt.setInt(2, umur);
            stmt.setInt(3, jumlah);
            stmt.setDouble(4, berat);
            stmt.executeUpdate();
            logDataProcessingChange("ranger", "Added hewan: " + nama);
        } catch (SQLException e) {
            System.out.println("Error adding hewan to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Object[][] getHewanData() {
        ArrayList<Object[]> hewanList = new ArrayList<>();
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "SELECT * FROM hewan"; // Table name: hewan
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] hewan = new Object[5];
                hewan[0] = rs.getInt("idHewan");
                hewan[1] = rs.getString("nama");
                hewan[2] = rs.getInt("umur");
                hewan[3] = rs.getInt("jumlah");
                hewan[4] = rs.getDouble("berat");
                hewanList.add(hewan);
            }
        } catch (SQLException e) {
            System.out.println("Error getting hewan data from database: " + e.getMessage());
            e.printStackTrace();
        }
        return hewanList.toArray(new Object[0][0]);
    }

    public Object[] getHewanById(int idHewan) {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "SELECT * FROM hewan WHERE idHewan = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idHewan);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Object[]{
                    rs.getInt("idHewan"),
                    rs.getString("nama"),
                    rs.getInt("umur"),
                    rs.getInt("jumlah"),
                    rs.getDouble("berat")
                };
            }
        } catch (SQLException e) {
            System.out.println("Error getting hewan by ID from database: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void deleteHewan(int idHewan) {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            // First check if the hewan exists
            String checkSql = "SELECT idHewan FROM hewan WHERE idHewan = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, idHewan);
            ResultSet rs = checkStmt.executeQuery();
            
            if (!rs.next()) {
                throw new SQLException("Hewan with ID: " + idHewan + " does not exist.");
            }

            // Set idHewan to NULL in kandang table to handle foreign key constraint
            String updateKandangSql = "UPDATE kandang SET idHewan = NULL WHERE idHewan = ?";
            PreparedStatement updateKandangStmt = conn.prepareStatement(updateKandangSql);
            updateKandangStmt.setInt(1, idHewan);
            updateKandangStmt.executeUpdate();

            // If hewan exists, proceed with deletion
            String sql = "DELETE FROM hewan WHERE idHewan = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idHewan);
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                logDataProcessingChange(currentUser.getNama(), "Deleted hewan with ID: " + idHewan);
                System.out.println("Hewan deleted successfully!");
            } else {
                throw new SQLException("Failed to delete hewan with ID: " + idHewan);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void editHewan(int idHewan, int umur, int jumlah, double berat) {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "UPDATE hewan SET umur = ?, jumlah = ?, berat = ? WHERE idHewan = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, umur);
            stmt.setInt(2, jumlah);
            stmt.setDouble(3, berat);
            stmt.setInt(4, idHewan);
            stmt.executeUpdate();
            logDataProcessingChange("ranger", "Edited hewan with ID: " + idHewan);
        } catch (SQLException e) {
            System.out.println("Error editing hewan in database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void logDataProcessingChange(String username, String changeDescription) {
        if (currentUser == null) {
            System.out.println("currentUser is null, cannot log data processing change.");
            return;
        }
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "INSERT INTO data_processing_log (username, change_description) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, currentUser.getNama()); // Use currentUser.getNama() instead of username
            stmt.setString(2, changeDescription);
            stmt.executeUpdate();
            System.out.println("Data processing change logged successfully!");
        } catch (SQLException e) {
            System.out.println("Error logging data processing change: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
