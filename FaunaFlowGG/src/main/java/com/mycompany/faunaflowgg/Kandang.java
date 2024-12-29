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
public class Kandang {
    private double ukuran;
    private String tipe;
    private String spesialitas;

    public Kandang(double ukuran, String tipe, String spesialitas) {
        this.ukuran = ukuran;
        this.tipe = tipe;
        this.spesialitas = spesialitas;
    }

    public void addKandang(String ukuran, String tipe, String spesialitas) {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "INSERT INTO kandang (ukuran, tipe, spesialitas) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, ukuran);
            stmt.setString(2, tipe);
            stmt.setString(3, spesialitas);
            stmt.executeUpdate();
            System.out.println("Kandang added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding kandang to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Object[][] getKandangData() {
        ArrayList<Object[]> kandangList = new ArrayList<>();
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "SELECT * FROM kandang"; // Table name: kandang
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
                         "LEFT JOIN hewan h ON k.id = h.idKandang"; // Join kandang and hewan tables
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

    public void deleteKandang(int idKandang) {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "DELETE FROM kandang WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idKandang);
            stmt.executeUpdate();
            System.out.println("Kandang deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting kandang from database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
