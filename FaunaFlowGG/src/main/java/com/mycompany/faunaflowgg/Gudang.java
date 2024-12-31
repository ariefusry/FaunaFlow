package com.mycompany.faunaflowgg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Gudang {
    private ArrayList<Stok> stokList;

    public Gudang() {
        stokList = new ArrayList<>();
        loadStokFromDatabase();
    }

    public void loadStokFromDatabase() {
        stokList.clear();
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "SELECT s.idStok, s.kategoriStok, s.namaStok, s.jumlahStok, s.Satuan, g.namaGudang " +
                         "FROM Stok s JOIN Gudang g ON s.idGudang = g.idGudang";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Stok stok = new Stok(
                    rs.getInt("idStok"),
                    rs.getString("kategoriStok"),
                    rs.getString("namaStok"),
                    rs.getInt("jumlahStok"),
                    rs.getString("Satuan"),
                    rs.getString("namaGudang")
                );
                stokList.add(stok);
            }
        } catch (SQLException e) {
            System.out.println("Error loading stok from database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void tambahStok(String kategoriStok, String namaStok, int jumlah, String satuan, int idGudang) {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "INSERT INTO Stok (kategoriStok, namaStok, jumlahStok, Satuan, idGudang) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, kategoriStok);
            stmt.setString(2, namaStok);
            stmt.setInt(3, jumlah);
            stmt.setString(4, satuan);
            stmt.setInt(5, idGudang);
            stmt.executeUpdate();
            loadStokFromDatabase(); // Refresh the list from the database
            System.out.println("Stok berhasil ditambahkan!");
        } catch (SQLException e) {
            System.out.println("Error adding stok to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean updateStok(int idStok, int jumlah) {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "UPDATE Stok SET jumlahStok = ? WHERE idStok = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, jumlah);
            stmt.setInt(2, idStok);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                loadStokFromDatabase(); // Refresh the list from the database
                System.out.println("Stok berhasil diperbarui!");
                return true;
            } else {
                System.out.println("Stok tidak ditemukan!");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error updating stok in database: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStokByName(String kategoriStok, String namaStok, int jumlah) {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "UPDATE Stok SET jumlahStok = ? WHERE kategoriStok = ? AND namaStok = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, jumlah);
            stmt.setString(2, kategoriStok);
            stmt.setString(3, namaStok);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                loadStokFromDatabase(); // Refresh the list from the database
                System.out.println("Stok berhasil diperbarui!");
                return true;
            } else {
                System.out.println("Stok tidak ditemukan!");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error updating stok in database: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void deleteStok(int idStok) {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "DELETE FROM Stok WHERE idStok = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idStok);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                loadStokFromDatabase(); // Refresh the list from the database
                System.out.println("Stok berhasil dihapus!");
            } else {
                System.out.println("Stok tidak ditemukan!");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting stok from database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteStokByName(String kategoriStok, String namaStok) {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "DELETE FROM Stok WHERE kategoriStok = ? AND namaStok = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, kategoriStok);
            stmt.setString(2, namaStok);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                loadStokFromDatabase(); // Refresh the list from the database
                System.out.println("Stok berhasil dihapus!");
            } else {
                System.out.println("Stok tidak ditemukan!");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting stok from database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void cekStok(ArrayList<Object[]> stokDataList) {
        if (stokList.isEmpty()) {
            stokDataList.add(new Object[]{"Tidak ada stok tersedia.", "", "", "", ""});
        } else {
            for (Stok stok : stokList) {
                stokDataList.add(new Object[]{stok.idStok, stok.kategoriStok, stok.namaStok, stok.jumlah, stok.satuan, stok.namaGudang});
            }
        }
    }

    private class Stok {
        private int idStok;
        private String kategoriStok;
        private String namaStok;
        private int jumlah;
        private String satuan;
        private String namaGudang;

        public Stok(int idStok, String kategoriStok, String namaStok, int jumlah, String satuan, String namaGudang) {
            this.idStok = idStok;
            this.kategoriStok = kategoriStok;
            this.namaStok = namaStok;
            this.jumlah = jumlah;
            this.satuan = satuan;
            this.namaGudang = namaGudang;
        }

        @Override
        public String toString() {
            return "ID: " + idStok + ", Kategori: " + kategoriStok + ", Nama: " + namaStok + ", Jumlah: " + jumlah + " " + satuan + ", Gudang: " + namaGudang;
        }
    }
}
