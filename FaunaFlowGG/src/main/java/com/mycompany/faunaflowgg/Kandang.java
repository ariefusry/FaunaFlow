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
 */
public class Kandang {
    private double ukuran;
    private String tipe;
    private String spesialitas;
    private Employee currentUser;

    public Kandang(double ukuran, String tipe, String spesialitas) {
        this.ukuran = ukuran;
        this.tipe = tipe;
        this.spesialitas = spesialitas;
    }

    public Kandang(double ukuran, String tipe, String spesialitas, Employee currentUser) {
        this.ukuran = ukuran;
        this.tipe = tipe;
        this.spesialitas = spesialitas;
        this.currentUser = currentUser;
    }

    public void addKandang(String ukuran, String tipe, String spesialitas) {
        if (currentUser == null) {
            System.out.println("Current user is not set. Cannot add kandang.");
            return;
        }
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "INSERT INTO kandang (ukuran, tipe, spesialitas) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, ukuran);
            stmt.setString(2, tipe);
            stmt.setString(3, spesialitas);
            stmt.executeUpdate();
            System.out.println("Kandang added successfully!");
            logDataProcessingChange(currentUser.getNama(), "Added kandang with ukuran: " + ukuran + ", tipe: " + tipe + ", spesialitas: " + spesialitas);
        } catch (SQLException e) {
            System.out.println("Error adding kandang to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Object[][] getKandangData() {
        ArrayList<Object[]> kandangList = new ArrayList<>();
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "SELECT * FROM kandang";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] kandang = new Object[4];
                kandang[0] = rs.getInt("id");
                kandang[1] = rs.getDouble("ukuran");
                kandang[2] = rs.getString("tipe");
                kandang[3] = rs.getString("spesialitas");
                kandangList.add(kandang);
            }
        } catch (SQLException e) {
            System.out.println("Error getting kandang data from database: " + e.getMessage());
            e.printStackTrace();
        }
        return kandangList.toArray(new Object[0][0]);
    }

    public Object[][] getKandangContents() {
        ArrayList<Object[]> kandangContentsList = new ArrayList<>();
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "SELECT k.tipe, k.ukuran, h.nama, h.jumlah " +
                         "FROM kandang k " +
                         "LEFT JOIN hewan h ON k.id = h.idKandang";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] kandangContent = new Object[4];
                kandangContent[0] = rs.getString("tipe");
                kandangContent[1] = rs.getDouble("ukuran");
                kandangContent[2] = rs.getString("nama");
                kandangContent[3] = rs.getInt("jumlah");
                kandangContentsList.add(kandangContent);
            }
        } catch (SQLException e) {
            System.out.println("Error getting kandang contents from database: " + e.getMessage());
            e.printStackTrace();
        }
        return kandangContentsList.toArray(new Object[0][0]);
    }

    public void deleteKandang(int idKandang) throws Exception {
        if (currentUser == null) {
            System.out.println("Current user is not set. Cannot delete kandang.");
            return;
        }
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String checkSql = "SELECT id FROM kandang WHERE id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, idKandang);
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next()) {
                throw new Exception("Kandang with ID: " + idKandang + " does not exist.");
            }

            String updateHewanSql = "UPDATE hewan SET idKandang = NULL WHERE idKandang = ?";
            PreparedStatement updateHewanStmt = conn.prepareStatement(updateHewanSql);
            updateHewanStmt.setInt(1, idKandang);
            updateHewanStmt.executeUpdate();

            String sql = "DELETE FROM kandang WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idKandang);
            stmt.executeUpdate();
            System.out.println("Kandang deleted successfully!");
            logDataProcessingChange(currentUser.getNama(), "Deleted kandang with ID: " + idKandang);
            System.out.println("Data processing change logged successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting kandang from database: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Error deleting kandang from database: " + e.getMessage());
        }
    }

    public void editKandang(int idKandang, String ukuran, String tipe, String spesialitas) {
        if (currentUser == null) {
            System.out.println("Current user is not set. Cannot edit kandang.");
            return;
        }
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String checkSql = "SELECT id FROM kandang WHERE id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, idKandang);
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next()) {
                System.out.println("Kandang with ID: " + idKandang + " does not exist.");
                return;
            }

            String sql = "UPDATE kandang SET ukuran = ?, tipe = ?, spesialitas = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, ukuran);
            stmt.setString(2, tipe);
            stmt.setString(3, spesialitas);
            stmt.setInt(4, idKandang);
            stmt.executeUpdate();
            System.out.println("Kandang edited successfully!");
            logDataProcessingChange(currentUser.getNama(), "Edited kandang with ID: " + idKandang + ", ukuran: " + ukuran + ", tipe: " + tipe + ", spesialitas: " + spesialitas);
            System.out.println("Data processing change logged successfully!");
        } catch (SQLException e) {
            System.out.println("Error editing kandang in database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Object[] getKandangById(int idKandang) {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "SELECT * FROM kandang WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idKandang);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Object[]{
                    rs.getInt("id"),
                    rs.getDouble("ukuran"),
                    rs.getString("tipe"),
                    rs.getString("spesialitas")
                };
            }
        } catch (SQLException e) {
            System.out.println("Error getting kandang by ID from database: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private void logDataProcessingChange(String username, String changeDescription) {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "INSERT INTO data_processing_log (username, change_description) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, changeDescription);
            stmt.executeUpdate();
            System.out.println("Data processing change logged successfully!");
        } catch (SQLException e) {
            System.out.println("Error logging data processing change: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
