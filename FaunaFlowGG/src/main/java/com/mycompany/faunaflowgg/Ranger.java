package com.mycompany.faunaflowgg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 */
public class Ranger {
    private Kandang kandang;
    private Hewan hewan;
    private Gudang gudang;
    private Employee currentUser;

    public Ranger(Employee currentUser) {
        this.currentUser = currentUser;
        this.kandang = new Kandang(0.0, "", "", currentUser);
        this.hewan = new Hewan("", 0, 0, 0.0, currentUser); // Pass currentUser to Hewan
        this.gudang = new Gudang(currentUser);
    }

    public Object[][] viewKandangData() {
        return kandang.getKandangData();
    }

    public Object[][] viewHewanData() {
        return hewan.getHewanData();
    }

    public void submitReport(String nama, String laporan) {
        if (nama == null || nama.trim().isEmpty() || laporan == null || laporan.trim().isEmpty()) {
            System.out.println("Nama atau laporan tidak boleh kosong.");
            return;
        }

        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "INSERT INTO Laporan (nama, laporan) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nama);
            stmt.setString(2, laporan);
            stmt.executeUpdate();
            logDataProcessingChange(currentUser.getNama(), "Submitted report by: " + nama);
            System.out.println("Report submitted successfully!");
        } catch (SQLException e) {
            System.out.println("Error submitting report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Object[][] getAllReports() {
        ArrayList<Object[]> reportList = new ArrayList<>();
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "SELECT * FROM Laporan";
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

    public void tambahStokMakanan(String namaStok, int jumlah) {
        gudang.loadStokFromDatabase();
        gudang.tambahStok("Makanan", namaStok, jumlah, "kg", 1);
    }

    public boolean updateStokMakanan(String namaStok, int jumlah) {
        gudang.loadStokFromDatabase();
        return gudang.updateStokByName("Makanan", namaStok, jumlah);
    }

    public void deleteStokMakanan(String namaStok) {
        gudang.loadStokFromDatabase();
        gudang.deleteStokByName("Makanan", namaStok);
    }

    public void cekStok(ArrayList<Object[]> stokDataList) {
        gudang.loadStokFromDatabase();
        gudang.cekStok(stokDataList);
    }

    public void tambahStok(String kategoriStok, String namaStok, int jumlah, String satuan, int idGudang) {
        gudang.loadStokFromDatabase();
        gudang.tambahStok(kategoriStok, namaStok, jumlah, satuan, idGudang);
        logDataProcessingChange(currentUser.getNama(), "Added stock: " + namaStok + " in category: " + kategoriStok);
    }

    public boolean updateStok(int idStok, int jumlah) {
        gudang.loadStokFromDatabase();
        boolean updated = gudang.updateStok(idStok, jumlah);
        if (updated) {
            gudang.loadStokFromDatabase();
            logDataProcessingChange(currentUser.getNama(), "Updated stock with ID: " + idStok);
        }
        return updated;
    }

    public void deleteStok(int idStok) {
        gudang.loadStokFromDatabase();
        gudang.deleteStok(idStok);
        gudang.loadStokFromDatabase();
        logDataProcessingChange(currentUser.getNama(), "Deleted stock with ID: " + idStok);
    }

    public void loadStokFromDatabase() {
        gudang.loadStokFromDatabase();
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
            logDataProcessingChange(currentUser.getNama(), "Added hewan: " + nama);
        } catch (SQLException e) {
            System.out.println("Error adding hewan to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteHewan(int idHewan) {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "DELETE FROM hewan WHERE idHewan = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idHewan);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                logDataProcessingChange(currentUser.getNama(), "Deleted hewan with ID: " + idHewan);
                System.out.println("Hewan deleted successfully!");
            } else {
                System.out.println("Hewan with ID: " + idHewan + " not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting hewan from database: " + e.getMessage());
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
            logDataProcessingChange(currentUser.getNama(), "Edited hewan with ID: " + idHewan);
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
            // Check if the username exists in the account table
            String checkUserSql = "SELECT username FROM account WHERE username = ?";
            PreparedStatement checkUserStmt = conn.prepareStatement(checkUserSql);
            checkUserStmt.setString(1, username);
            ResultSet rs = checkUserStmt.executeQuery();
            if (!rs.next()) {
                System.out.println("Error: Username " + username + " does not exist in the account table.");
                return;
            }

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
