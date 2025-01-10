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
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author ARIEF
 */
public class Manager {
    private ArrayList<Employee> employees;
    private Gudang gudang;
    private Employee currentUser;

    public Manager(Employee currentUser) {
        this.currentUser = currentUser;
        employees = new ArrayList<>();
        loadEmployeesFromDatabase();
        this.gudang = new Gudang(currentUser); // Initialize Gudang with currentUser
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

    public ArrayList<Employee> getEmployees() {
        loadEmployeesFromDatabase(); // Ensure the list is up-to-date
        return employees;
    }

    public Object[][] getEmployeeData() {
        ArrayList<Employee> employees = getEmployees();
        Object[][] data = new Object[employees.size()][5];

        for (int i = 0; i < employees.size(); i++) {
            Employee employee = employees.get(i);
            data[i][0] = employee.getIdEmployee();
            data[i][1] = employee.getNama();
            data[i][2] = employee.getUsia();
            data[i][3] = employee.getAlamat();
            data[i][4] = employee.getNoTelp();
        }

        return data;
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
            logDataProcessingChange(currentUser.getNama(), "Added employee: " + employee.getNama());
        } catch (SQLException e) {
            System.out.println("Error adding employee to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean removeEmployee(int idEmployee) {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            // Check if the employee exists
            String checkSql = "SELECT id FROM employee WHERE id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, idEmployee);
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next()) {
                return false; // Employee not found
            }

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
                return true; // Success
            } else {
                conn.rollback(); // Rollback the transaction
                return false; // Failed to remove employee
            }
        } catch (SQLException e) {
            System.out.println("Error removing employee and associated account from database: " + e.getMessage());
            e.printStackTrace();
            return false; // Error occurred
        }
    }

    public void setHewanToKandang(int idKandang, int idHewan) throws Exception {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            // Check if the kandang exists
            String checkKandangSql = "SELECT id FROM kandang WHERE id = ?";
            PreparedStatement checkKandangStmt = conn.prepareStatement(checkKandangSql);
            checkKandangStmt.setInt(1, idKandang);
            ResultSet rsKandang = checkKandangStmt.executeQuery();
            if (!rsKandang.next()) {
                throw new Exception("Kandang with ID: " + idKandang + " does not exist.");
            }

            // Check if the hewan exists
            String checkHewanSql = "SELECT idHewan, idKandang FROM hewan WHERE idHewan = ?";
            PreparedStatement checkHewanStmt = conn.prepareStatement(checkHewanSql);
            checkHewanStmt.setInt(1, idHewan);
            ResultSet rsHewan = checkHewanStmt.executeQuery();
            if (!rsHewan.next()) {
                throw new Exception("Hewan with ID: " + idHewan + " does not exist.");
            }

            // Check if the hewan is already set to a kandang
            if (rsHewan.getInt("idKandang") != 0) {
                throw new Exception("Hewan with ID: " + idHewan + " is already set to a kandang.");
            }

            // Check if the kandang already has a hewan
            String checkKandangHewanSql = "SELECT idHewan FROM hewan WHERE idKandang = ?";
            PreparedStatement checkKandangHewanStmt = conn.prepareStatement(checkKandangHewanSql);
            checkKandangHewanStmt.setInt(1, idKandang);
            ResultSet rsKandangHewan = checkKandangHewanStmt.executeQuery();
            if (rsKandangHewan.next()) {
                throw new Exception("Kandang with ID: " + idKandang + " already has a hewan.");
            }

            // Set the hewan to the kandang
            String sqlHewan = "UPDATE hewan SET idKandang = ? WHERE idHewan = ?";
            PreparedStatement stmtHewan = conn.prepareStatement(sqlHewan);
            stmtHewan.setInt(1, idKandang);
            stmtHewan.setInt(2, idHewan);
            stmtHewan.executeUpdate();

            System.out.println("Hewan set to kandang successfully!");
            if (currentUser != null) {
                logDataProcessingChange(currentUser.getNama(), "Set hewan with ID: " + idHewan + " to kandang with ID: " + idKandang);
            }
        } catch (SQLException e) {
            System.out.println("Error setting hewan to kandang: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Error setting hewan to kandang: " + e.getMessage());
        }
    }

    public void assignRandomJobdesks() {
        List<String> jobdesks = List.of("Membersihkan kandang", "Memberi makan hewan", "Merawat hewan", "Tour Guide", "Merapihkan Gudang", "Mengurus Stok Gudang");
        Random random = new Random();

        try (Connection conn = FaunaFlowGG.getConnection()) {
            // Delete existing jobdesk data
            String deleteSQL = "DELETE FROM JobdeskKaryawan";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSQL)) {
                deleteStmt.executeUpdate();
            }

            String insertSQL = "INSERT INTO JobdeskKaryawan (nama_karyawan, senin, selasa, rabu, kamis, jumat, sabtu, minggu) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            List<Employee> employees = getEmployees();

            for (Employee employee : employees) {
                List<String> shuffledJobdesks = new ArrayList<>(jobdesks);
                Collections.shuffle(shuffledJobdesks, random);

                try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                    pstmt.setString(1, employee.getNama());
                    pstmt.setString(2, shuffledJobdesks.get(0)); // Senikon
                    pstmt.setString(3, shuffledJobdesks.get(1)); // Selasa
                    pstmt.setString(4, shuffledJobdesks.get(2)); // Rabu
                    pstmt.setString(5, shuffledJobdesks.get(3)); // Kamis
                    pstmt.setString(6, shuffledJobdesks.get(0)); // Jumat
                    pstmt.setString(7, shuffledJobdesks.get(1)); // Sabtu
                    pstmt.setString(8, shuffledJobdesks.get(2)); // Minggu

                    pstmt.executeUpdate();
                }
            }

            logDataProcessingChange(currentUser.getNama(), "Assigned random jobdesks");
            System.out.println("Jobdesk karyawan berhasil di-randomize dan disimpan ke database.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Object[][] getJobdeskKaryawanData() {
        ArrayList<Object[]> jobdeskList = new ArrayList<>();
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "SELECT * FROM JobdeskKaryawan"; // Table name: JobdeskKaryawan
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] jobdesk = new Object[9]; // Adjust the array size to 9
                jobdesk[0] = rs.getInt("id");
                jobdesk[1] = rs.getString("nama_karyawan");
                jobdesk[2] = rs.getString("senin");
                jobdesk[3] = rs.getString("selasa");
                jobdesk[4] = rs.getString("rabu");
                jobdesk[5] = rs.getString("kamis");
                jobdesk[6] = rs.getString("jumat");
                jobdesk[7] = rs.getString("sabtu");
                jobdesk[8] = rs.getString("minggu");
                jobdeskList.add(jobdesk);
            }
        } catch (SQLException e) {
            System.out.println("Error getting JobdeskKaryawan data from database: " + e.getMessage());
            e.printStackTrace();
        }
        return jobdeskList.toArray(new Object[0][0]);
    }

    public void deleteAllJobdeskKaryawan() {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "DELETE FROM JobdeskKaryawan";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
            logDataProcessingChange(currentUser.getNama(), "Deleted all JobdeskKaryawan data");
            System.out.println("All JobdeskKaryawan data deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting all JobdeskKaryawan data: " + e.getMessage());
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

    public boolean deleteReport(int idReport) {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            System.out.println("Attempting to delete report with ID: " + idReport); // Debugging statement
            String sql = "DELETE FROM Laporan WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idReport);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                logDataProcessingChange(currentUser.getNama(), "Deleted report with ID: " + idReport);
                System.out.println("Report deleted successfully!");
                return true;
            } else {
                System.out.println("Failed to delete report. Report ID not found.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error deleting report from database: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void deleteAllReports() {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "DELETE FROM Laporan";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
            logDataProcessingChange(currentUser.getNama(), "Deleted all reports");
            System.out.println("All reports deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting all reports from database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void tambahStok(String kategoriStok, String namaStok, int jumlah, String satuan, int idGudang) {
        gudang.loadStokFromDatabase(); // Refresh the list from the database
        gudang.tambahStok(kategoriStok, namaStok, jumlah, satuan, idGudang);
        logDataProcessingChange(currentUser.getNama(), "Added stock: " + namaStok + " in category: " + kategoriStok);
    }

    public boolean updateStok(int idStok, int jumlah) {
        gudang.loadStokFromDatabase(); // Refresh the list from the database
        boolean updated = gudang.updateStok(idStok, jumlah);
        if (updated) {
            gudang.loadStokFromDatabase(); // Refresh the list from the database
            logDataProcessingChange(currentUser.getNama(), "Updated stock with ID: " + idStok);
        }
        return updated;
    }

    public boolean updateStok(int idStok, String kategori, String nama, int jumlah, String satuan, String gudang) {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "UPDATE Stok SET kategoriStok = ?, namaStok = ?, jumlahStok = ?, Satuan = ?, idGudang = " +
                         "(SELECT idGudang FROM Gudang WHERE namaGudang = ?) WHERE idStok = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, kategori);
            stmt.setString(2, nama);
            stmt.setInt(3, jumlah);
            stmt.setString(4, satuan);
            stmt.setString(5, gudang);
            stmt.setInt(6, idStok);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                this.gudang.loadStokFromDatabase(); // Refresh the list from the database
                logDataProcessingChange(currentUser.getNama(), "Updated stock with ID: " + idStok);
                System.out.println("Stok updated successfully!");
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

    public boolean deleteStok(int idStok) {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "DELETE FROM Stok WHERE idStok = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idStok);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                gudang.loadStokFromDatabase();
                logDataProcessingChange(currentUser.getNama(), "Deleted stock with ID: " + idStok);
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error deleting stok from database: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void loadStokFromDatabase() {
        gudang.loadStokFromDatabase();
    }

    public void editEmployee(int idEmployee, String name, int age, String address, String phone) {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "UPDATE employee SET nama = ?, usia = ?, alamat = ?, notel = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, address);
            stmt.setString(4, phone);
            stmt.setInt(5, idEmployee);
            stmt.executeUpdate();
            loadEmployeesFromDatabase(); // Refresh the list from the database
            logDataProcessingChange(currentUser.getNama(), "Edited employee with ID: " + idEmployee);
            System.out.println("Employee edited successfully!");
        } catch (SQLException e) {
            System.out.println("Error editing employee in database: " + e.getMessage());
            e.printStackTrace();
        }
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

    public Stok getStokById(int idStok) {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "SELECT s.idStok, s.kategoriStok, s.namaStok, s.jumlahStok, s.Satuan, g.namaGudang " +
                         "FROM Stok s JOIN Gudang g ON s.idGudang = g.idGudang WHERE s.idStok = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idStok);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Stok(
                    rs.getInt("idStok"),
                    rs.getString("kategoriStok"),
                    rs.getString("namaStok"),
                    rs.getInt("jumlahStok"),
                    rs.getString("Satuan"),
                    rs.getString("namaGudang")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error getting stok by ID from database: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void createAccount(String username, String password, String role, Employee employee) {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            if (role.equals("user") && employee != null) {
                // Insert employee first
                String employeeSql = "INSERT INTO employee (nama, usia, alamat, notel) VALUES (?, ?, ?, ?)";
                PreparedStatement employeeStmt = conn.prepareStatement(employeeSql, PreparedStatement.RETURN_GENERATED_KEYS);
                employeeStmt.setString(1, employee.getNama());
                employeeStmt.setInt(2, employee.getUsia());
                employeeStmt.setString(3, employee.getAlamat());
                employeeStmt.setString(4, employee.getNoTelp());
                employeeStmt.executeUpdate();

                // Get the generated employee ID
                ResultSet generatedKeys = employeeStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int employeeId = generatedKeys.getInt(1);

                    // Insert account with the employee ID
                    String accountSql = "INSERT INTO account (username, password, role, employee_id) VALUES (?, ?, ?, ?)";
                    PreparedStatement accountStmt = conn.prepareStatement(accountSql);
                    accountStmt.setString(1, username);
                    accountStmt.setString(2, password);
                    accountStmt.setString(3, role);
                    accountStmt.setInt(4, employeeId);
                    accountStmt.executeUpdate();

                    logDataProcessingChange(currentUser.getNama(), "Created account for username: " + username);
                    System.out.println("Account and employee created successfully!");
                }
            } else {
                // Insert account without employee ID for admin
                String accountSql = "INSERT INTO account (username, password, role, employee_id) VALUES (?, ?, ?, NULL)";
                PreparedStatement accountStmt = conn.prepareStatement(accountSql);
                accountStmt.setString(1, username);
                accountStmt.setString(2, password);
                accountStmt.setString(3, role);
                accountStmt.executeUpdate();

                logDataProcessingChange(currentUser.getNama(), "Created admin account for username: " + username);
                System.out.println("Admin account created successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error creating account and employee: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void logDataProcessingChange(String username, String changeDescription) {
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

    public Object[][] getLogsData() {
        ArrayList<Object[]> logsList = new ArrayList<>();
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "SELECT * FROM data_processing_log"; // Table name: data_processing_log
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] log = new Object[4];
                log[0] = rs.getInt("id");
                log[1] = rs.getString("username");
                log[2] = rs.getString("change_description");
                log[3] = rs.getTimestamp("change_time");
                logsList.add(log);
            }
        } catch (SQLException e) {
            System.out.println("Error getting logs data from database: " + e.getMessage());
            e.printStackTrace();
        }
        return logsList.toArray(new Object[0][0]);
    }

    public void addKandang(String ukuran, String tipe, String spesialitas) {
        if (tipe.length() > 150 || spesialitas.length() > 150) {
            System.out.println("Tipe and Spesialitas must be less than 150 characters.");
            return;
        }
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "INSERT INTO kandang (ukuran, tipe, spesialitas) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, ukuran);
            stmt.setString(2, tipe);
            stmt.setString(3, spesialitas);
            stmt.executeUpdate();
            logDataProcessingChange(currentUser.getNama(), "Added kandang with ukuran: " + ukuran + ", tipe: " + tipe + ", spesialitas: " + spesialitas);
        } catch (SQLException e) {
            System.out.println("Error adding kandang to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteKandang(int idKandang) {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String updateHewanSql = "UPDATE hewan SET idKandang = NULL WHERE idKandang = ?";
            PreparedStatement updateHewanStmt = conn.prepareStatement(updateHewanSql);
            updateHewanStmt.setInt(1, idKandang);
            updateHewanStmt.executeUpdate();

            String sql = "DELETE FROM kandang WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idKandang);
            stmt.executeUpdate();
            logDataProcessingChange(currentUser.getNama(), "Deleted kandang with ID: " + idKandang);
        } catch (SQLException e) {
            System.out.println("Error deleting kandang from database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void editKandang(int idKandang, String ukuran, String tipe, String spesialitas) {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "UPDATE kandang SET ukuran = ?, tipe = ?, spesialitas = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, ukuran);
            stmt.setString(2, tipe);
            stmt.setString(3, spesialitas);
            stmt.setInt(4, idKandang);
            stmt.executeUpdate();
            logDataProcessingChange(currentUser.getNama(), "Edited kandang with ID: " + idKandang);
        } catch (SQLException e) {
            System.out.println("Error editing kandang in database: " + e.getMessage());
            e.printStackTrace();
        }
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
            stmt.executeUpdate();
            logDataProcessingChange(currentUser.getNama(), "Deleted hewan with ID: " + idHewan);
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

    public void deleteAllLogs() {
        try (Connection conn = FaunaFlowGG.getConnection()) {
            String sql = "DELETE FROM data_processing_log";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
            logDataProcessingChange(currentUser.getNama(), "Deleted all logs");
            System.out.println("All logs deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting all logs: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void cekStok(ArrayList<Object[]> stokDataList) {
        gudang.loadStokFromDatabase();
        gudang.cekStok(stokDataList);
    }
}
