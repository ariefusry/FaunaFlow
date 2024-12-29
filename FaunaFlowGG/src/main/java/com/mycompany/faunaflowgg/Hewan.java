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

    public Hewan(String nama, int umur, int jumlah, double berat) {
        this.nama = nama;
        this.umur = umur;
        this.jumlah = jumlah;
        this.berat = berat;
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
            System.out.println("Hewan added successfully!");
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

    public void deleteHewan(int idHewan) {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "DELETE FROM hewan WHERE idHewan = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idHewan);
            stmt.executeUpdate();
            System.out.println("Hewan deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting hewan from database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
