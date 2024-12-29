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
public class Ranger {
    private Kandang kandang;
    private Hewan hewan;

    public Ranger() {
        this.kandang = new Kandang(0, "", ""); // Initialize Kandang
        this.hewan = new Hewan("", 0, 0, 0.0); // Initialize Hewan
    }

    public Object[][] viewKandangData() {
        return kandang.getKandangData(); // Use Kandang instance to get kandang data
    }

    public Object[][] viewHewanData() {
        return hewan.getHewanData(); // Use Hewan instance to get hewan data
    }

    public void submitReport(String nama, String laporan) {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "INSERT INTO Laporan (nama, laporan) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nama);
            stmt.setString(2, laporan);
            stmt.executeUpdate();
            System.out.println("Report submitted successfully!");
        } catch (SQLException e) {
            System.out.println("Error submitting report: " + e.getMessage());
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
