/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

 package com.mycompany.faunaflowgg;

 import java.sql.Connection;
 import java.sql.DriverManager;
 import java.sql.SQLException;
 
 /**
  *
  * @author ARIEF
  */
 public class FaunaFlowGG {
     private static final String URL = "jdbc:mysql://localhost:3306/faunaflow";
     private static final String USER = "root";
     private static final String PASSWORD = "";
 
     public static Connection getConnection() {
         Connection connection = null;
         try {
             connection = DriverManager.getConnection(URL, USER, PASSWORD);
             System.out.println("Koneksi Berhasil!");
         } catch (SQLException e) {
             System.out.println("Koneksi Gagal! Error: " + e.getMessage());
             e.printStackTrace();
         }
         return connection;
     }
 
     public static void main(String[] args) {
         EmployeeManagementSystem ems = new EmployeeManagementSystem();
 
         SwingUI ui = new SwingUI(ems);
         ui.showHomePage();
     }
 }
 