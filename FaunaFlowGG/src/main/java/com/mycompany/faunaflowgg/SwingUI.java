/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.faunaflowgg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class SwingUI {
    private EmployeeManagementSystem ems;
    private Manager manager; // Add Manager instance
    private Kandang kandang; // Add Kandang instance
    private Hewan hewan; // Add Hewan instance
    private Ranger ranger; // Add Ranger instance
    private JFrame frame;
    private JPanel panel;

    public SwingUI(EmployeeManagementSystem ems) {
        this.ems = ems;
        this.manager = new Manager(); // Initialize Manager
        this.kandang = new Kandang(0, "", ""); // Initialize Kandang
        this.hewan = new Hewan("", 0, 0, 0.0); // Initialize Hewan with parameters
        this.ranger = new Ranger(); // Initialize Ranger

        // Membuat JFrame
        frame = new JFrame("FaunaFlow");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Atur mode fullscreen
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Memaksimalkan jendela
        frame.setSize(800, 600);

        // Atur panel
        panel = new JPanel(new GridBagLayout());
        frame.add(panel);
    }

    public void showHomePage() {
        panel.removeAll();
        panel.setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        JMenu accountMenu = new JMenu("Account");
        JMenuItem loginMenuItem = new JMenuItem("Login");

        loginMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoginPage();
            }
        });

        accountMenu.add(loginMenuItem);
        menuBar.add(accountMenu);
        frame.setJMenuBar(menuBar);

        // Display JobdeskKaryawan table
        showJobdeskKaryawanTable();

        panel.revalidate();
        panel.repaint();
        frame.setVisible(true);
    }

    private void showJobdeskKaryawanTable() {
        panel.removeAll();
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Weekly Activity Ranger");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"Nama Karyawan", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu"};
        Object[][] data = manager.getJobdeskKaryawanData(); // Use Manager to get JobdeskKaryawan data

        // Remove the ID column from the data
        Object[][] tableData = new Object[data.length][7];
        for (int i = 0; i < data.length; i++) {
            System.arraycopy(data[i], 1, tableData[i], 0, 7);
        }

        if (tableData.length == 0) {
            JLabel noDataLabel = new JLabel("No JobdeskKaryawan data available.");
            noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(noDataLabel, BorderLayout.CENTER);
        } else {
            DefaultTableModel model = new DefaultTableModel(tableData, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make table cells non-editable
                }
            };

            JTable table = new JTable(model);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            table.setFillsViewportHeight(true);
            table.setRowHeight(30);
            table.setFont(new Font("Arial", Font.PLAIN, 14));
            table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
            table.getTableHeader().setBackground(Color.LIGHT_GRAY);
            table.setGridColor(Color.GRAY);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            panel.add(scrollPane, BorderLayout.CENTER);
        }

        panel.revalidate();
        panel.repaint();
    }

    public void showLoggedInHomePage() {
        panel.removeAll();
        panel.setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        JMenu accountMenu = new JMenu("Account");
        JMenuItem logoutMenuItem = new JMenuItem("Logout");

        logoutMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ems.logout();
                manager.loadStokFromDatabase(); // Reload stock data
                showHomePage();
            }
        });

        accountMenu.add(logoutMenuItem);
        menuBar.add(accountMenu);

        JMenu zooMenu = new JMenu("Zoo");
        JMenu kandangMenu = new JMenu("Kandang");
        JMenu hewanMenu = new JMenu("Hewan");

        JMenuItem viewKandangMenuItem = new JMenuItem("View Kandang");
        JMenuItem viewHewanMenuItem = new JMenuItem("View Hewan");
        JMenuItem viewKandangContentsMenuItem = new JMenuItem("View Penghuni Kandang");

        viewKandangMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showKandangList();
            }
        });

        viewHewanMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showHewanList();
            }
        });

        viewKandangContentsMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showKandangContents();
            }
        });

        kandangMenu.add(viewKandangMenuItem);
        kandangMenu.add(viewKandangContentsMenuItem);
        hewanMenu.add(viewHewanMenuItem);

        zooMenu.add(kandangMenu);
        zooMenu.add(hewanMenu);
        menuBar.add(zooMenu);

        if (ems.isAdmin()) { // Check admin role
            JMenu employeeMenu = new JMenu("Employee");
            JMenuItem employeeListMenuItem = new JMenuItem("Employee List");
            JMenuItem addEmployeeMenuItem = new JMenuItem("Add Employee");
            JMenuItem removeEmployeeMenuItem = new JMenuItem("Remove Employee");
            JMenuItem assignJobdeskMenuItem = new JMenuItem("Assign Random Jobdesks");
            JMenuItem deleteAllJobdeskMenuItem = new JMenuItem("Delete All Jobdesks");
            JMenuItem viewReportsMenuItem = new JMenuItem("View Reports");
            JMenuItem editEmployeeMenuItem = new JMenuItem("Edit Employee");

            employeeListMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showEmployeeList();
                }
            });

            addEmployeeMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showAddEmployeePage();
                }
            });

            removeEmployeeMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showRemoveEmployeePage();
                }
            });

            assignJobdeskMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    manager.assignRandomJobdesks(); // Use Manager to assign random jobdesks
                    JOptionPane.showMessageDialog(panel, "Jobdesks assigned successfully!");
                    showJobdeskKaryawanTable(); // Refresh the jobdesk table
                }
            });

            deleteAllJobdeskMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    manager.deleteAllJobdeskKaryawan(); // Use Manager to delete all jobdesks
                    JOptionPane.showMessageDialog(panel, "All Jobdesks deleted successfully!");
                    showJobdeskKaryawanTable();
                }
            });

            viewReportsMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showReportsPage(); // Show reports page
                }
            });

            editEmployeeMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showEditEmployeePage();
                }
            });

            employeeMenu.add(employeeListMenuItem);
            employeeMenu.add(addEmployeeMenuItem);
            employeeMenu.add(removeEmployeeMenuItem);
            employeeMenu.add(editEmployeeMenuItem);
            employeeMenu.addSeparator();
            employeeMenu.add(assignJobdeskMenuItem);
            employeeMenu.add(deleteAllJobdeskMenuItem);
            employeeMenu.addSeparator();
            employeeMenu.add(viewReportsMenuItem);
            menuBar.add(employeeMenu);

            JMenu kandangManagementMenu = new JMenu("Kandang Management");
            JMenuItem addKandangMenuItem = new JMenuItem("Add Kandang");
            JMenuItem setHewanToKandangMenuItem = new JMenuItem("Set Hewan to Kandang");
            JMenuItem deleteKandangMenuItem = new JMenuItem("Delete Kandang");
            JMenuItem editKandangMenuItem = new JMenuItem("Edit Kandang");

            addKandangMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showAddKandangPage();
                }
            });

            setHewanToKandangMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showSetHewanToKandangPage();
                }
            });

            deleteKandangMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showDeleteKandangPage();
                }
            });

            editKandangMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showEditKandangPage();
                }
            });

            kandangManagementMenu.add(addKandangMenuItem);
            kandangManagementMenu.add(setHewanToKandangMenuItem);
            kandangManagementMenu.add(deleteKandangMenuItem);
            kandangManagementMenu.add(editKandangMenuItem);
            kandangMenu.add(kandangManagementMenu);

            JMenu hewanManagementMenu = new JMenu("Hewan Management");
            JMenuItem addHewanMenuItem = new JMenuItem("Add Hewan");
            JMenuItem deleteHewanMenuItem = new JMenuItem("Delete Hewan");
            JMenuItem editHewanMenuItem = new JMenuItem("Edit Hewan");

            addHewanMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showAddHewanPage();
                }
            });

            deleteHewanMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showDeleteHewanPage();
                }
            });

            editHewanMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showEditHewanPage();
                }
            });

            hewanManagementMenu.add(addHewanMenuItem);
            hewanManagementMenu.add(deleteHewanMenuItem);
            hewanManagementMenu.add(editHewanMenuItem);
            hewanMenu.add(hewanManagementMenu);
        } else if (ems.isUser()) { // Check user role
            JMenu reportMenu = new JMenu("Report");
            JMenuItem submitReportMenuItem = new JMenuItem("Submit Report");
            JMenuItem viewReportsMenuItem = new JMenuItem("View Reports");

            submitReportMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showSubmitReportPage();
                }
            });

            viewReportsMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showReportsPage(); // Show reports page
                }
            });

            reportMenu.add(submitReportMenuItem);
            reportMenu.add(viewReportsMenuItem);
            menuBar.add(reportMenu);
        }

        if (ems.isAdmin() || ems.isUser()) { // Check admin or user role
            JMenu gudangMenu = new JMenu("Gudang");
            JMenuItem tambahStokMenuItem = new JMenuItem("Tambah Stok");
            JMenuItem updateStokMenuItem = new JMenuItem("Update Stok");
            JMenuItem deleteStokMenuItem = new JMenuItem("Delete Stok");
            JMenuItem cekStokMenuItem = new JMenuItem("Cek Stok");

            tambahStokMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showTambahStokPage();
                }
            });

            updateStokMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showUpdateStokPage();
                }
            });

            deleteStokMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showDeleteStokPage();
                }
            });

            cekStokMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showCekStokPage();
                }
            });

            if (ems.isAdmin()) {
                gudangMenu.add(tambahStokMenuItem);
                gudangMenu.add(deleteStokMenuItem);
            }
            gudangMenu.add(updateStokMenuItem);
            gudangMenu.add(cekStokMenuItem);
            menuBar.add(gudangMenu);
        }

        frame.setJMenuBar(menuBar);

        // Display JobdeskKaryawan table
        showJobdeskKaryawanTable();

        panel.revalidate();
        panel.repaint();
        frame.setVisible(true);
    }

    private void showEmployeeList() {
        panel.removeAll();
        panel.setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Name", "Age", "Address", "Phone"};
        Object[][] data = manager.getEmployeeData(); // Use Manager to get employee data

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        table.getTableHeader().setBackground(Color.LIGHT_GRAY);
        table.setGridColor(Color.GRAY);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        //Styling
        backButton.setBackground(Color.decode("#2c52b3")); // Warna biru
        backButton.setForeground(Color.white);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoggedInHomePage();
            }
        });
        panel.add(backButton, BorderLayout.SOUTH);

        panel.revalidate();
        panel.repaint();
    }

    private void showLoginPage() {
        panel.removeAll();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel userLabel = new JLabel("User");
        panel.add(userLabel, gbc);

        gbc.gridx = 1;
        JTextField userText = new JTextField(20);
        panel.add(userText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel passwordLabel = new JLabel("Password");
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        JPasswordField passwordText = new JPasswordField(20);
        panel.add(passwordText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JButton backButton = new JButton("Back");
        panel.add(backButton, gbc);
        //Styling
        backButton.setBackground(Color.decode("#2c52b3")); // Warna biru
        backButton.setForeground(Color.white);

        gbc.gridx = 1;
        JButton loginButton = new JButton("Login");
        panel.add(loginButton, gbc);
        //Styling
        loginButton.setBackground(Color.decode("#79AC78")); // Warna hijau
        loginButton.setForeground(Color.white);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passwordText.getPassword());
                if (ems.login(username, password)) {
                    JOptionPane.showMessageDialog(panel, "Login successful!");
                    showLoggedInHomePage();
                } else {
                    JOptionPane.showMessageDialog(panel, "Invalid credentials.");
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showHomePage();
            }
        });

        panel.revalidate();
        panel.repaint();
    }

    private void showAddEmployeePage() {
        panel.removeAll();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Name");
        panel.add(nameLabel, gbc);

        gbc.gridx = 1;
        JTextField nameText = new JTextField(20);
        panel.add(nameText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel ageLabel = new JLabel("Age");
        panel.add(ageLabel, gbc);

        gbc.gridx = 1;
        JTextField ageText = new JTextField(20);
        panel.add(ageText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel addressLabel = new JLabel("Address");
        panel.add(addressLabel, gbc);

        gbc.gridx = 1;
        JTextField addressText = new JTextField(20);
        panel.add(addressText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel phoneLabel = new JLabel("Phone");
        panel.add(phoneLabel, gbc);

        gbc.gridx = 1;
        JTextField phoneText = new JTextField(20);
        panel.add(phoneText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        JButton backButton = new JButton("Back");
        panel.add(backButton, gbc);
        //Styling
        backButton.setBackground(Color.decode("#2c52b3")); // Warna biru
        backButton.setForeground(Color.white);

        gbc.gridx = 1;
        JButton addButton = new JButton("Add");
        panel.add(addButton, gbc);
        //Styling
        addButton.setBackground(Color.decode("#79AC78")); // Warna hijau
        addButton.setForeground(Color.white);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameText.getText().trim();
                String age = ageText.getText().trim();
                String address = addressText.getText().trim();
                String phone = phoneText.getText().trim();

                if (name.isEmpty() || age.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Belum Terisi", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                manager.addEmployee(new Employee(0, name, Integer.parseInt(age), address, phone)); // Use Manager to add employee
                JOptionPane.showMessageDialog(panel, "Employee added successfully!");
                showLoggedInHomePage();
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoggedInHomePage();
            }
        });

        panel.revalidate();
        panel.repaint();
    }

    private void showRemoveEmployeePage() {
        panel.removeAll();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Display all employee data
        String[] columnNames = {"ID", "Name", "Age", "Address", "Phone"};
        Object[][] data = manager.getEmployeeData(); // Use Manager instance to get employee data

        if (data.length == 0) {
            JLabel noDataLabel = new JLabel("No Employee data available.");
            noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(noDataLabel, gbc);
        } else {
            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make table cells non-editable
                }
            };

            JTable table = new JTable(model);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            table.setFillsViewportHeight(true);
            table.setRowHeight(30);
            table.setFont(new Font("Arial", Font.PLAIN, 14));
            table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
            table.getTableHeader().setBackground(Color.LIGHT_GRAY);
            table.setGridColor(Color.GRAY);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            JScrollPane scrollPane = new JScrollPane(table);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            panel.add(scrollPane, gbc);
            gbc.gridwidth = 1;
        }

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel idLabel = new JLabel("Employee ID");
        panel.add(idLabel, gbc);

        gbc.gridx = 1;
        JTextField idText = new JTextField(20);
        panel.add(idText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JButton backButton = new JButton("Back");
        panel.add(backButton, gbc);
        //Styling
        backButton.setBackground(Color.decode("#2c52b3")); // Warna biru
        backButton.setForeground(Color.white);

        gbc.gridx = 1;
        JButton removeButton = new JButton("Remove");
        panel.add(removeButton, gbc);
        //Styling
        removeButton.setBackground(Color.decode("#FF0000")); // Warna merah
        removeButton.setForeground(Color.white);

        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String idStr = idText.getText().trim();
                if (idStr.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Employee ID tidak boleh kosong.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int id = Integer.parseInt(idStr);
                manager.removeEmployee(id); // Use Manager to remove employee
                JOptionPane.showMessageDialog(panel, "Employee removed successfully!");
                showLoggedInHomePage();
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoggedInHomePage();
            }
        });

        panel.revalidate();
        panel.repaint();
    }

    private void showAddKandangPage() {
        panel.removeAll();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel ukuranLabel = new JLabel("Ukuran (m2)");
        panel.add(ukuranLabel, gbc);

        gbc.gridx = 1;
        JTextField ukuranText = new JTextField(20);
        panel.add(ukuranText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel tipeLabel = new JLabel("Tipe");
        panel.add(tipeLabel, gbc);

        gbc.gridx = 1;
        JTextArea tipeText = new JTextArea(5, 20); // Change to JTextArea
        tipeText.setLineWrap(true); // Enable line wrap
        tipeText.setWrapStyleWord(true); // Wrap at word boundaries
        JScrollPane tipeScrollPane = new JScrollPane(tipeText);
        panel.add(tipeScrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel spesialitasLabel = new JLabel("Spesialitas");
        panel.add(spesialitasLabel, gbc);

        gbc.gridx = 1;
        JTextArea spesialitasText = new JTextArea(5, 20); // Change to JTextArea
        spesialitasText.setLineWrap(true); // Enable line wrap
        spesialitasText.setWrapStyleWord(true); // Wrap at word boundaries
        JScrollPane spesialitasScrollPane = new JScrollPane(spesialitasText);
        panel.add(spesialitasScrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JButton backButton = new JButton("Back");
        panel.add(backButton, gbc);
        //Styling
        backButton.setBackground(Color.decode("#2c52b3")); // Warna biru
        backButton.setForeground(Color.white);

        gbc.gridx = 1;
        JButton addButton = new JButton("Add");
        panel.add(addButton, gbc);
        //Styling
        addButton.setBackground(Color.decode("#79AC78")); // Warna hijau
        addButton.setForeground(Color.white);
        
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String ukuran = ukuranText.getText().trim();
                String tipe = tipeText.getText().trim();
                String spesialitas = spesialitasText.getText().trim();

                if (ukuran.isEmpty() || tipe.isEmpty() || spesialitas.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Ukuran, Tipe, dan Spesialitas tidak boleh kosong.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                kandang.addKandang(ukuran, tipe, spesialitas); // Use Kandang instance to add kandang
                JOptionPane.showMessageDialog(panel, "Kandang added successfully!");
                showLoggedInHomePage();
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoggedInHomePage();
            }
        });

        panel.revalidate();
        panel.repaint();
    }

    private void showSetHewanToKandangPage() {
        panel.removeAll();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Display all kandang data
        String[] kandangColumnNames = {"ID", "Ukuran", "Tipe", "Spesialitas"};
        Object[][] kandangData = kandang.getKandangData(); // Use Kandang instance to get kandang data

        if (kandangData.length == 0) {
            JLabel noKandangDataLabel = new JLabel("No Kandang data available.");
            noKandangDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            panel.add(noKandangDataLabel, gbc);
            gbc.gridwidth = 1;
        } else {
            DefaultTableModel kandangModel = new DefaultTableModel(kandangData, kandangColumnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make table cells non-editable
                }
            };

            JTable kandangTable = new JTable(kandangModel);
            kandangTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            kandangTable.setFillsViewportHeight(true);
            kandangTable.setRowHeight(30);
            kandangTable.setFont(new Font("Arial", Font.PLAIN, 14));
            kandangTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
            kandangTable.getTableHeader().setBackground(Color.LIGHT_GRAY);
            kandangTable.setGridColor(Color.GRAY);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < kandangTable.getColumnCount(); i++) {
                kandangTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            JScrollPane kandangScrollPane = new JScrollPane(kandangTable);
            kandangScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            kandangScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.gridheight = 4;
            panel.add(kandangScrollPane, gbc);
        }

        // Display all hewan data
        String[] hewanColumnNames = {"ID", "Nama", "Umur", "Jumlah", "Berat"};
        Object[][] hewanData = hewan.getHewanData(); // Use Hewan instance to get hewan data

        if (hewanData.length == 0) {
            JLabel noHewanDataLabel = new JLabel("No Hewan data available.");
            noHewanDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            panel.add(noHewanDataLabel, gbc);
            gbc.gridwidth = 1;
        } else {
            DefaultTableModel hewanModel = new DefaultTableModel(hewanData, hewanColumnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make table cells non-editable
                }
            };

            JTable hewanTable = new JTable(hewanModel);
            hewanTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            hewanTable.setFillsViewportHeight(true);
            hewanTable.setRowHeight(30);
            hewanTable.setFont(new Font("Arial", Font.PLAIN, 14));
            hewanTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
            hewanTable.getTableHeader().setBackground(Color.LIGHT_GRAY);
            hewanTable.setGridColor(Color.GRAY);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < hewanTable.getColumnCount(); i++) {
                hewanTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            JScrollPane hewanScrollPane = new JScrollPane(hewanTable);
            hewanScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            hewanScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.gridheight = 4;
            panel.add(hewanScrollPane, gbc);
        }

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        JLabel idKandangLabel = new JLabel("ID Kandang");
        panel.add(idKandangLabel, gbc);

        gbc.gridx = 1;
        JTextField idKandangText = new JTextField(20);
        panel.add(idKandangText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel idHewanLabel = new JLabel("ID Hewan");
        panel.add(idHewanLabel, gbc);

        gbc.gridx = 1;
        JTextField idHewanText = new JTextField(20);
        panel.add(idHewanText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        JButton backButton = new JButton("Back");
        panel.add(backButton, gbc);
        //Styling
        backButton.setBackground(Color.decode("#2c52b3")); // Warna biru
        backButton.setForeground(Color.white);

        gbc.gridx = 1;
        JButton setButton = new JButton("Set");
        panel.add(setButton, gbc);
        //Styling
        setButton.setBackground(Color.decode("#79AC78")); // Warna hijau
        setButton.setForeground(Color.white);

        setButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String idKandangStr = idKandangText.getText().trim();
                String idHewanStr = idHewanText.getText().trim();
                if (idKandangStr.isEmpty() || idHewanStr.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "ID Kandang dan ID Hewan tidak boleh kosong.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int idKandang = Integer.parseInt(idKandangStr);
                int idHewan = Integer.parseInt(idHewanStr);
                manager.setHewanToKandang(idKandang, idHewan); // Use Manager to set hewan to kandang
                JOptionPane.showMessageDialog(panel, "Hewan set to kandang successfully!");
                showLoggedInHomePage();
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoggedInHomePage();
            }
        });

        panel.revalidate();
        panel.repaint();
    }

    private void showAddHewanPage() {
        panel.removeAll();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel namaLabel = new JLabel("Nama");
        panel.add(namaLabel, gbc);

        gbc.gridx = 1;
        JTextField namaText = new JTextField(20);
        panel.add(namaText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel umurLabel = new JLabel("Umur");
        panel.add(umurLabel, gbc);

        gbc.gridx = 1;
        JTextField umurText = new JTextField(20);
        panel.add(umurText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel jumlahLabel = new JLabel("Jumlah");
        panel.add(jumlahLabel, gbc);

        gbc.gridx = 1;
        JTextField jumlahText = new JTextField(20);
        panel.add(jumlahText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel beratLabel = new JLabel("Berat");
        panel.add(beratLabel, gbc);

        gbc.gridx = 1;
        JTextField beratText = new JTextField(20);
        panel.add(beratText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        JButton backButton = new JButton("Back");
        panel.add(backButton, gbc);
        //Styling
        backButton.setBackground(Color.decode("#2c52b3")); // Warna biru
        backButton.setForeground(Color.white);

        gbc.gridx = 1;
        JButton addButton = new JButton("Add");
        panel.add(addButton, gbc);
        //Styling
        addButton.setBackground(Color.decode("#79AC78")); // Warna hijau
        addButton.setForeground(Color.white);
        
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nama = namaText.getText().trim();
                String umur = umurText.getText().trim();
                String jumlah = jumlahText.getText().trim();
                String berat = beratText.getText().trim();

                if (nama.isEmpty() || umur.isEmpty() || jumlah.isEmpty() || berat.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Belum Terisi", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                hewan.addHewan(nama, Integer.parseInt(umur), Integer.parseInt(jumlah), Double.parseDouble(berat)); // Use Hewan instance to add hewan
                JOptionPane.showMessageDialog(panel, "Hewan added successfully!");
                showLoggedInHomePage();
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoggedInHomePage();
            }
        });

        panel.revalidate();
        panel.repaint();
    }

    private void showKandangList() {
        panel.removeAll();
        panel.setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Ukuran", "Tipe", "Spesialitas"};
        Object[][] data = kandang.getKandangData(); // Use Kandang instance to get kandang data

        if (data.length == 0) {
            JLabel noDataLabel = new JLabel("No Kandang data available.");
            noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(noDataLabel, BorderLayout.CENTER);
        } else {
            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make table cells non-editable
                }
            };

            JTable table = new JTable(model);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            table.setFillsViewportHeight(true);
            table.setRowHeight(30);
            table.setFont(new Font("Arial", Font.PLAIN, 14));
            table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
            table.getTableHeader().setBackground(Color.LIGHT_GRAY);
            table.setGridColor(Color.GRAY);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            panel.add(scrollPane, BorderLayout.CENTER);
        }

        JButton backButton = new JButton("Back");
        //Styling
        backButton.setBackground(Color.decode("#2c52b3")); // Warna biru
        backButton.setForeground(Color.white);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoggedInHomePage();
            }
        });
        panel.add(backButton, BorderLayout.SOUTH);

        panel.revalidate();
        panel.repaint();
    }

    private void showHewanList() {
        panel.removeAll();
        panel.setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Nama", "Umur", "Jumlah", "Berat"};
        Object[][] data = hewan.getHewanData(); // Use Hewan instance to get hewan data

        if (data.length == 0) {
            JLabel noDataLabel = new JLabel("No Hewan data available.");
            noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(noDataLabel, BorderLayout.CENTER);
        } else {
            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make table cells non-editable
                }
            };

            JTable table = new JTable(model);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            table.setFillsViewportHeight(true);
            table.setRowHeight(30);
            table.setFont(new Font("Arial", Font.PLAIN, 14));
            table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
            table.getTableHeader().setBackground(Color.LIGHT_GRAY);
            table.setGridColor(Color.GRAY);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            panel.add(scrollPane, BorderLayout.CENTER);
        }

        JButton backButton = new JButton("Back");
        //Styling
        backButton.setBackground(Color.decode("#2c52b3")); // Warna biru
        backButton.setForeground(Color.white);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoggedInHomePage();
            }
        });
        panel.add(backButton, BorderLayout.SOUTH);

        panel.revalidate();
        panel.repaint();
    }

    private void showKandangContents() {
        panel.removeAll();
        panel.setLayout(new BorderLayout());

        String[] columnNames = {"Tipe", "Ukuran", "Nama Hewan", "Jumlah"};
        Object[][] data = kandang.getKandangContents(); // Use Kandang instance to get kandang contents data

        if (data.length == 0) {
            JLabel noDataLabel = new JLabel("No Kandang contents available.");
            noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(noDataLabel, BorderLayout.CENTER);
        } else {
            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make table cells non-editable
                }
            };

            JTable table = new JTable(model);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            table.setFillsViewportHeight(true);
            table.setRowHeight(30);
            table.setFont(new Font("Arial", Font.PLAIN, 14));
            table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
            table.getTableHeader().setBackground(Color.LIGHT_GRAY);
            table.setGridColor(Color.GRAY);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            panel.add(scrollPane, BorderLayout.CENTER);
        }

        JButton backButton = new JButton("Back");
        //Styling
        backButton.setBackground(Color.decode("#2c52b3")); // Warna biru
        backButton.setForeground(Color.white);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoggedInHomePage();
            }
        });
        panel.add(backButton, BorderLayout.SOUTH);

        panel.revalidate();
        panel.repaint();
    }

    private void showDeleteKandangPage() {
        panel.removeAll();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Display all kandang data
        String[] columnNames = {"ID", "Ukuran", "Tipe", "Spesialitas"};
        Object[][] data = kandang.getKandangData(); // Use Kandang instance to get kandang data

        if (data.length == 0) {
            JLabel noDataLabel = new JLabel("No Kandang data available.");
            noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(noDataLabel, gbc);
        } else {
            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make table cells non-editable
                }
            };

            JTable table = new JTable(model);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            table.setFillsViewportHeight(true);
            table.setRowHeight(30);
            table.setFont(new Font("Arial", Font.PLAIN, 14));
            table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
            table.getTableHeader().setBackground(Color.LIGHT_GRAY);
            table.setGridColor(Color.GRAY);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            JScrollPane scrollPane = new JScrollPane(table);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            panel.add(scrollPane, gbc);
            gbc.gridwidth = 1;
        }

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel idLabel = new JLabel("Kandang ID");
        panel.add(idLabel, gbc);

        gbc.gridx = 1;
        JTextField idText = new JTextField(20);
        panel.add(idText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JButton backButton = new JButton("Back");
        panel.add(backButton, gbc);
        //Styling
        backButton.setBackground(Color.decode("#2c52b3")); // Warna biru
        backButton.setForeground(Color.white);

        gbc.gridx = 1;
        JButton deleteButton = new JButton("Delete");
        panel.add(deleteButton, gbc);
        //Styling
        deleteButton.setBackground(Color.decode("#FF0000")); // Warna merah
        deleteButton.setForeground(Color.white);

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String idStr = idText.getText().trim();
                if (idStr.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Kandang ID tidak boleh kosong.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int id = Integer.parseInt(idStr);
                kandang.deleteKandang(id); // Use Kandang instance to delete kandang
                JOptionPane.showMessageDialog(panel, "Kandang deleted successfully!");
                showLoggedInHomePage();
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoggedInHomePage();
            }
        });

        panel.revalidate();
        panel.repaint();
    }

    private void showDeleteHewanPage() {
        panel.removeAll();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Display all hewan data
        String[] columnNames = {"ID", "Nama", "Umur", "Jumlah", "Berat"};
        Object[][] data = hewan.getHewanData(); // Use Hewan instance to get hewan data

        if (data.length == 0) {
            JLabel noDataLabel = new JLabel("No Hewan data available.");
            noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(noDataLabel, gbc);
        } else {
            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make table cells non-editable
                }
            };

            JTable table = new JTable(model);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            table.setFillsViewportHeight(true);
            table.setRowHeight(30);
            table.setFont(new Font("Arial", Font.PLAIN, 14));
            table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
            table.getTableHeader().setBackground(Color.LIGHT_GRAY);
            table.setGridColor(Color.GRAY);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            JScrollPane scrollPane = new JScrollPane(table);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            panel.add(scrollPane, gbc);
            gbc.gridwidth = 1;
        }

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel idLabel = new JLabel("Hewan ID");
        panel.add(idLabel, gbc);

        gbc.gridx = 1;
        JTextField idText = new JTextField(20);
        panel.add(idText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JButton backButton = new JButton("Back");
        panel.add(backButton, gbc);
        //Styling
        backButton.setBackground(Color.decode("#2c52b3")); // Warna biru
        backButton.setForeground(Color.white);

        gbc.gridx = 1;
        JButton deleteButton = new JButton("Delete");
        panel.add(deleteButton, gbc);
        //Styling
        deleteButton.setBackground(Color.decode("#FF0000")); // Warna merah
        deleteButton.setForeground(Color.white);

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String idStr = idText.getText().trim();
                if (idStr.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Hewan ID tidak boleh kosong.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int id = Integer.parseInt(idStr);
                hewan.deleteHewan(id); // Use Hewan instance to delete hewan
                JOptionPane.showMessageDialog(panel, "Hewan deleted successfully!");
                showLoggedInHomePage();
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoggedInHomePage();
            }
        });

        panel.revalidate();
        panel.repaint();
    }

    private void showSubmitReportPage() {
        panel.removeAll();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Name");
        panel.add(nameLabel, gbc);

        gbc.gridx = 1;
        JTextField nameText = new JTextField(20);
        panel.add(nameText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel reportLabel = new JLabel("Report");
        panel.add(reportLabel, gbc);

        gbc.gridx = 1;
        JTextArea reportText = new JTextArea(5, 20);
        reportText.setLineWrap(true); // Enable line wrap
        reportText.setWrapStyleWord(true); // Wrap at word boundaries
        JScrollPane scrollPane = new JScrollPane(reportText);
        panel.add(scrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JButton backButton = new JButton("Back");
        panel.add(backButton, gbc);
        //Styling
        backButton.setBackground(Color.decode("#2c52b3")); // Warna biru
        backButton.setForeground(Color.white);

        gbc.gridx = 1;
        JButton submitButton = new JButton("Submit");
        panel.add(submitButton, gbc);
        //Styling
        submitButton.setBackground(Color.decode("#79AC78")); // Warna hijau
        submitButton.setForeground(Color.white);

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nama = nameText.getText().trim();
                String laporan = reportText.getText().trim();

                if (nama.isEmpty() || laporan.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Belum Terisi", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                ranger.submitReport(nama, laporan); // Use Ranger to submit report
                JOptionPane.showMessageDialog(panel, "Report submitted successfully!");
                showLoggedInHomePage();
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoggedInHomePage();
            }
        });

        panel.revalidate();
        panel.repaint();
    }

    private void showReportsPage() {
        panel.removeAll();
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Reports");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"Nama", "Laporan", "Tanggal"};
        Object[][] data = ems.isAdmin() ? manager.getAllReports() : ranger.getAllReports(); // Use Manager or Ranger to get reports

        // Remove the ID column from the data
        Object[][] tableData = new Object[data.length][3];
        for (int i = 0; i < data.length; i++) {
            System.arraycopy(data[i], 1, tableData[i], 0, 3);
        }

        if (tableData.length == 0) {
            JLabel noDataLabel = new JLabel("No reports available.");
            noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(noDataLabel, BorderLayout.CENTER);
        } else {
            DefaultTableModel model = new DefaultTableModel(tableData, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make table cells non-editable
                }
            };

            JTable table = new JTable(model);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            table.setFillsViewportHeight(true);
            table.setRowHeight(30);
            table.setFont(new Font("Arial", Font.PLAIN, 14));
            table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
            table.getTableHeader().setBackground(Color.LIGHT_GRAY);
            table.setGridColor(Color.GRAY);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            panel.add(scrollPane, BorderLayout.CENTER);
        }

        JButton backButton = new JButton("Back");
        //Styling
        backButton.setBackground(Color.decode("#2c52b3")); // Warna biru
        backButton.setForeground(Color.white);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoggedInHomePage();
            }
        });
        panel.add(backButton, BorderLayout.SOUTH);

        panel.revalidate();
        panel.repaint();
    }

    private void showTambahStokPage() {
        panel.removeAll();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel kategoriLabel = new JLabel("Kategori Stok");
        panel.add(kategoriLabel, gbc);

        gbc.gridx = 1;
        JTextField kategoriText = new JTextField(20);
        panel.add(kategoriText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel namaLabel = new JLabel("Nama Stok");
        panel.add(namaLabel, gbc);

        gbc.gridx = 1;
        JTextField namaText = new JTextField(20);
        panel.add(namaText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel jumlahLabel = new JLabel("Jumlah");
        panel.add(jumlahLabel, gbc);

        gbc.gridx = 1;
        JTextField jumlahText = new JTextField(20);
        panel.add(jumlahText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel satuanLabel = new JLabel("Satuan");
        panel.add(satuanLabel, gbc);

        gbc.gridx = 1;
        JTextField satuanText = new JTextField(20);
        panel.add(satuanText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel gudangLabel = new JLabel("ID Gudang");
        panel.add(gudangLabel, gbc);

        gbc.gridx = 1;
        JTextField gudangText = new JTextField(20);
        panel.add(gudangText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        JButton backButton = new JButton("Back");
        panel.add(backButton, gbc);
        //Styling
        backButton.setBackground(Color.decode("#2c52b3")); // Warna biru
        backButton.setForeground(Color.white);

        gbc.gridx = 1;
        JButton addButton = new JButton("Add");
        panel.add(addButton, gbc);
        //Styling
        addButton.setBackground(Color.decode("#79AC78")); // Warna hijau
        addButton.setForeground(Color.white);
        
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String kategori = kategoriText.getText().trim();
                String nama = namaText.getText().trim();
                String jumlah = jumlahText.getText().trim();
                String satuan = satuanText.getText().trim();
                String gudang = gudangText.getText().trim();

                if (kategori.isEmpty() || nama.isEmpty() || jumlah.isEmpty() || satuan.isEmpty() || gudang.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Belum Terisi", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (ems.isAdmin()) {
                    manager.tambahStok(kategori, nama, Integer.parseInt(jumlah), satuan, Integer.parseInt(gudang));
                } else {
                    ranger.tambahStok(kategori, nama, Integer.parseInt(jumlah), satuan, Integer.parseInt(gudang));
                }
                JOptionPane.showMessageDialog(panel, "Stok berhasil ditambahkan!");
                showCekStokPage(); // Refresh the stock page
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoggedInHomePage();
            }
        });

        panel.revalidate();
        panel.repaint();
    }

    private void showUpdateStokPage() {
        panel.removeAll();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Display all stock data
        String[] columnNames = {"ID", "Kategori", "Nama Stok", "Jumlah", "Satuan", "Gudang"};
        ArrayList<Object[]> stokDataList = new ArrayList<>();
        if (ems.isAdmin()) {
            manager.cekStok(stokDataList);
        } else {
            ranger.cekStok(stokDataList);
        }
        Object[][] data = stokDataList.toArray(new Object[0][]);

        if (data.length == 0) {
            JLabel noDataLabel = new JLabel("No Stok data available.");
            noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(noDataLabel, gbc);
        } else {
            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make table cells non-editable
                }
            };

            JTable table = new JTable(model);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            table.setFillsViewportHeight(true);
            table.setRowHeight(30);
            table.setFont(new Font("Arial", Font.PLAIN, 14));
            table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
            table.getTableHeader().setBackground(Color.LIGHT_GRAY);
            table.setGridColor(Color.GRAY);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            panel.add(scrollPane, gbc);
            gbc.gridwidth = 1;
        }

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel idLabel = new JLabel("ID Stok");
        panel.add(idLabel, gbc);

        gbc.gridx = 1;
        JTextField idText = new JTextField(20);
        panel.add(idText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JButton backButton = new JButton("Back");
        panel.add(backButton, gbc);
        //Styling
        backButton.setBackground(Color.decode("#2c52b3")); // Warna biru
        backButton.setForeground(Color.white);

        gbc.gridx = 1;
        JButton loadButton = new JButton("Load Data");
        panel.add(loadButton, gbc);
        //Styling
        loadButton.setBackground(Color.decode("#79AC78")); // Warna hijau
        loadButton.setForeground(Color.white);

        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String idStr = idText.getText().trim();
                if (idStr.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "ID Stok tidak boleh kosong.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int id = Integer.parseInt(idStr);
                Stok stokData = manager.getStokById(id); // Use Manager instance to get stock data by ID

                if (stokData == null) {
                    JOptionPane.showMessageDialog(panel, "ID Stok tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Display the data for editing
                gbc.gridx = 0;
                gbc.gridy = 3;
                JLabel kategoriLabel = new JLabel("Kategori");
                panel.add(kategoriLabel, gbc);

                gbc.gridx = 1;
                JTextField kategoriText = new JTextField(20);
                kategoriText.setText(stokData.getKategoriStok());
                panel.add(kategoriText, gbc);

                gbc.gridx = 0;
                gbc.gridy = 4;
                JLabel namaLabel = new JLabel("Nama Stok");
                panel.add(namaLabel, gbc);

                gbc.gridx = 1;
                JTextField namaText = new JTextField(20);
                namaText.setText(stokData.getNamaStok());
                panel.add(namaText, gbc);

                gbc.gridx = 0;
                gbc.gridy = 5;
                JLabel jumlahLabel = new JLabel("Jumlah");
                panel.add(jumlahLabel, gbc);

                gbc.gridx = 1;
                JTextField jumlahText = new JTextField(20);
                jumlahText.setText(String.valueOf(stokData.getJumlah()));
                panel.add(jumlahText, gbc);

                gbc.gridx = 0;
                gbc.gridy = 6;
                JLabel satuanLabel = new JLabel("Satuan");
                panel.add(satuanLabel, gbc);

                gbc.gridx = 1;
                JTextField satuanText = new JTextField(20);
                satuanText.setText(stokData.getSatuan());
                panel.add(satuanText, gbc);

                gbc.gridx = 0;
                gbc.gridy = 7;
                JLabel gudangLabel = new JLabel("Gudang");
                panel.add(gudangLabel, gbc);

                gbc.gridx = 1;
                JTextField gudangText = new JTextField(20);
                gudangText.setText(stokData.getNamaGudang());
                panel.add(gudangText, gbc);

                gbc.gridx = 0;
                gbc.gridy = 8;
                JButton editButton = new JButton("Edit");
                panel.add(editButton, gbc);
                //Styling
                editButton.setBackground(Color.decode("#79AC78")); // Warna hijau
                editButton.setForeground(Color.white);

                editButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String kategori = kategoriText.getText().trim();
                        String nama = namaText.getText().trim();
                        String jumlah = jumlahText.getText().trim();
                        String satuan = satuanText.getText().trim();
                        String gudang = gudangText.getText().trim();

                        if (kategori.isEmpty() || nama.isEmpty() || jumlah.isEmpty() || satuan.isEmpty() || gudang.isEmpty()) {
                            JOptionPane.showMessageDialog(panel, "Belum Terisi", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        manager.updateStok(id, kategori, nama, Integer.parseInt(jumlah), satuan, gudang); // Use Manager instance to update stock
                        JOptionPane.showMessageDialog(panel, "Stok updated successfully!");
                        showCekStokPage();
                    }
                });

                panel.revalidate();
                panel.repaint();
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoggedInHomePage();
            }
        });

        panel.revalidate();
        panel.repaint();
    }

    private void showDeleteStokPage() {
        panel.removeAll();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Display all stock data
        String[] columnNames = {"ID", "Kategori", "Nama Stok", "Jumlah", "Satuan", "Gudang"};
        ArrayList<Object[]> stokDataList = new ArrayList<>();
        if (ems.isAdmin()) {
            manager.cekStok(stokDataList);
        } else {
            ranger.cekStok(stokDataList);
        }
        Object[][] data = stokDataList.toArray(new Object[0][]);

        if (data.length == 0) {
            JLabel noDataLabel = new JLabel("No Stok data available.");
            noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(noDataLabel, gbc);
        } else {
            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make table cells non-editable
                }
            };

            JTable table = new JTable(model);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            table.setFillsViewportHeight(true);
            table.setRowHeight(30);
            table.setFont(new Font("Arial", Font.PLAIN, 14));
            table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
            table.getTableHeader().setBackground(Color.LIGHT_GRAY);
            table.setGridColor(Color.GRAY);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            JScrollPane scrollPane = new JScrollPane(table);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            panel.add(scrollPane, gbc);
            gbc.gridwidth = 1;
        }

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel idLabel = new JLabel("ID Stok");
        panel.add(idLabel, gbc);

        gbc.gridx = 1;
        JTextField idText = new JTextField(20);
        panel.add(idText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JButton backButton = new JButton("Back");
        panel.add(backButton, gbc);
        //Styling
        backButton.setBackground(Color.decode("#2c52b3")); // Warna biru
        backButton.setForeground(Color.white);

        gbc.gridx = 1;
        JButton deleteButton = new JButton("Delete");
        panel.add(deleteButton, gbc);
        //Styling
        deleteButton.setBackground(Color.decode("#FF0000")); // Warna merah
        deleteButton.setForeground(Color.white);

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String id = idText.getText().trim();

                if (id.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Belum Terisi", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (ems.isAdmin()) {
                    manager.deleteStok(Integer.parseInt(id));
                } else {
                    ranger.deleteStok(Integer.parseInt(id));
                }
                JOptionPane.showMessageDialog(panel, "Stok berhasil dihapus!");
                showCekStokPage(); // Refresh the stock page
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoggedInHomePage();
            }
        });

        panel.revalidate();
        panel.repaint();
    }

    private void showCekStokPage() {
        panel.removeAll();
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Stok");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Kategori", "Nama Stok", "Jumlah", "Satuan", "Gudang"};
        ArrayList<Object[]> stokDataList = new ArrayList<>();
        if (ems.isAdmin()) {
            manager.cekStok(stokDataList);
        } else {
            ranger.cekStok(stokDataList);
        }
        Object[][] stokData = stokDataList.toArray(new Object[0][]);

        DefaultTableModel model = new DefaultTableModel(stokData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };

        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        table.getTableHeader().setBackground(Color.LIGHT_GRAY);
        table.setGridColor(Color.GRAY);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        //Styling
        backButton.setBackground(Color.decode("#2c52b3")); // Warna biru
        backButton.setForeground(Color.white);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoggedInHomePage();
            }
        });
        panel.add(backButton, BorderLayout.SOUTH);

        panel.revalidate();
        panel.repaint();
    }

    private void showEditKandangPage() {
        panel.removeAll();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Display all kandang data
        String[] columnNames = {"ID", "Ukuran", "Tipe", "Spesialitas"};
        Object[][] data = kandang.getKandangData(); // Use Kandang instance to get kandang data

        if (data.length == 0) {
            JLabel noDataLabel = new JLabel("No Kandang data available.");
            noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(noDataLabel, gbc);
        } else {
            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make table cells non-editable
                }
            };

            JTable table = new JTable(model);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            table.setFillsViewportHeight(true);
            table.setRowHeight(30);
            table.setFont(new Font("Arial", Font.PLAIN, 14));
            table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
            table.getTableHeader().setBackground(Color.LIGHT_GRAY);
            table.setGridColor(Color.GRAY);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            panel.add(scrollPane, gbc);
            gbc.gridwidth = 1;
        }

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel idLabel = new JLabel("Kandang ID");
        panel.add(idLabel, gbc);

        gbc.gridx = 1;
        JTextField idText = new JTextField(20);
        panel.add(idText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JButton backButton = new JButton("Back");
        panel.add(backButton, gbc);
        //Styling
        backButton.setBackground(Color.decode("#2c52b3")); // Warna biru
        backButton.setForeground(Color.white);

        gbc.gridx = 1;
        JButton loadButton = new JButton("Load Data");
        panel.add(loadButton, gbc);
        //Styling
        loadButton.setBackground(Color.decode("#79AC78")); // Warna hijau
        loadButton.setForeground(Color.white);

        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String idStr = idText.getText().trim();
                if (idStr.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Kandang ID tidak boleh kosong.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int id = Integer.parseInt(idStr);
                Object[] kandangData = kandang.getKandangById(id); // Use Kandang instance to get kandang data by ID

                if (kandangData == null) {
                    JOptionPane.showMessageDialog(panel, "Kandang ID tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Display the data for editing
                gbc.gridx = 0;
                gbc.gridy = 3;
                JLabel ukuranLabel = new JLabel("Ukuran");
                panel.add(ukuranLabel, gbc);

                gbc.gridx = 1;
                JTextField ukuranText = new JTextField(20);
                ukuranText.setText(String.valueOf(kandangData[1]));
                panel.add(ukuranText, gbc);

                gbc.gridx = 0;
                gbc.gridy = 4;
                JLabel tipeLabel = new JLabel("Tipe");
                panel.add(tipeLabel, gbc);

                gbc.gridx = 1;
                JTextField tipeText = new JTextField(20);
                tipeText.setText(String.valueOf(kandangData[2]));
                panel.add(tipeText, gbc);

                gbc.gridx = 0;
                gbc.gridy = 5;
                JLabel spesialitasLabel = new JLabel("Spesialitas");
                panel.add(spesialitasLabel, gbc);

                gbc.gridx = 1;
                JTextField spesialitasText = new JTextField(20);
                spesialitasText.setText(String.valueOf(kandangData[3]));
                panel.add(spesialitasText, gbc);

                gbc.gridx = 0;
                gbc.gridy = 6;
                JButton editButton = new JButton("Edit");
                panel.add(editButton, gbc);
                //Styling
                editButton.setBackground(Color.decode("#79AC78")); // Warna hijau
                editButton.setForeground(Color.white);

                editButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String ukuran = ukuranText.getText().trim();
                        String tipe = tipeText.getText().trim();
                        String spesialitas = spesialitasText.getText().trim();

                        if (ukuran.isEmpty() || tipe.isEmpty() || spesialitas.isEmpty()) {
                            JOptionPane.showMessageDialog(panel, "Belum Terisi", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        kandang.editKandang(id, ukuran, tipe, spesialitas); // Use Kandang instance to edit kandang
                        JOptionPane.showMessageDialog(panel, "Kandang edited successfully!");
                        showLoggedInHomePage();
                    }
                });

                panel.revalidate();
                panel.repaint();
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoggedInHomePage();
            }
        });

        panel.revalidate();
        panel.repaint();
    }

    private void showEditHewanPage() {
        panel.removeAll();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Display all hewan data
        String[] columnNames = {"ID", "Nama", "Umur", "Jumlah", "Berat"};
        Object[][] data = hewan.getHewanData(); // Use Hewan instance to get hewan data

        if (data.length == 0) {
            JLabel noDataLabel = new JLabel("No Hewan data available.");
            noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(noDataLabel, gbc);
        } else {
            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make table cells non-editable
                }
            };

            JTable table = new JTable(model);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            table.setFillsViewportHeight(true);
            table.setRowHeight(30);
            table.setFont(new Font("Arial", Font.PLAIN, 14));
            table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
            table.getTableHeader().setBackground(Color.LIGHT_GRAY);
            table.setGridColor(Color.GRAY);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            panel.add(scrollPane, gbc);
            gbc.gridwidth = 1;
        }

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel idLabel = new JLabel("Hewan ID");
        panel.add(idLabel, gbc);

        gbc.gridx = 1;
        JTextField idText = new JTextField(20);
        panel.add(idText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JButton backButton = new JButton("Back");
        panel.add(backButton, gbc);
        //Styling
        backButton.setBackground(Color.decode("#2c52b3")); // Warna biru
        backButton.setForeground(Color.white);

        gbc.gridx = 1;
        JButton loadButton = new JButton("Load Data");
        panel.add(loadButton, gbc);
        //Styling
        loadButton.setBackground(Color.decode("#79AC78")); // Warna hijau
        loadButton.setForeground(Color.white);

        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String idStr = idText.getText().trim();
                if (idStr.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Hewan ID tidak boleh kosong.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int id = Integer.parseInt(idStr);
                Object[] hewanData = hewan.getHewanById(id); // Use Hewan instance to get hewan data by ID

                if (hewanData == null) {
                    JOptionPane.showMessageDialog(panel, "Hewan ID tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Display the data for editing
                gbc.gridx = 0;
                gbc.gridy = 3;
                JLabel umurLabel = new JLabel("Umur");
                panel.add(umurLabel, gbc);

                gbc.gridx = 1;
                JTextField umurText = new JTextField(20);
                umurText.setText(String.valueOf(hewanData[2]));
                panel.add(umurText, gbc);

                gbc.gridx = 0;
                gbc.gridy = 4;
                JLabel jumlahLabel = new JLabel("Jumlah");
                panel.add(jumlahLabel, gbc);

                gbc.gridx = 1;
                JTextField jumlahText = new JTextField(20);
                jumlahText.setText(String.valueOf(hewanData[3]));
                panel.add(jumlahText, gbc);

                gbc.gridx = 0;
                gbc.gridy = 5;
                JLabel beratLabel = new JLabel("Berat");
                panel.add(beratLabel, gbc);

                gbc.gridx = 1;
                JTextField beratText = new JTextField(20);
                beratText.setText(String.valueOf(hewanData[4]));
                panel.add(beratText, gbc);

                gbc.gridx = 0;
                gbc.gridy = 6;
                JButton editButton = new JButton("Edit");
                panel.add(editButton, gbc);
                //Styling
                editButton.setBackground(Color.decode("#79AC78")); // Warna hijau
                editButton.setForeground(Color.white);

                editButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String umur = umurText.getText().trim();
                        String jumlah = jumlahText.getText().trim();
                        String berat = beratText.getText().trim();

                        if (umur.isEmpty() || jumlah.isEmpty() || berat.isEmpty()) {
                            JOptionPane.showMessageDialog(panel, "Belum Terisi", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        hewan.editHewan(id, Integer.parseInt(umur), Integer.parseInt(jumlah), Double.parseDouble(berat)); // Use Hewan instance to edit hewan
                        JOptionPane.showMessageDialog(panel, "Hewan edited successfully!");
                        showLoggedInHomePage();
                    }
                });

                panel.revalidate();
                panel.repaint();
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoggedInHomePage();
            }
        });

        panel.revalidate();
        panel.repaint();
    }

    private void showEditEmployeePage() {
        panel.removeAll();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Display all employee data
        String[] columnNames = {"ID", "Name", "Age", "Address", "Phone"};
        Object[][] data = manager.getEmployeeData(); // Use Manager instance to get employee data

        if (data.length == 0) {
            JLabel noDataLabel = new JLabel("No Employee data available.");
            noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(noDataLabel, gbc);
        } else {
            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make table cells non-editable
                }
            };

            JTable table = new JTable(model);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            table.setFillsViewportHeight(true);
            table.setRowHeight(30);
            table.setFont(new Font("Arial", Font.PLAIN, 14));
            table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
            table.getTableHeader().setBackground(Color.LIGHT_GRAY);
            table.setGridColor(Color.GRAY);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            panel.add(scrollPane, gbc);
            gbc.gridwidth = 1;
        }

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel idLabel = new JLabel("Employee ID");
        panel.add(idLabel, gbc);

        gbc.gridx = 1;
        JTextField idText = new JTextField(20);
        panel.add(idText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JButton backButton = new JButton("Back");
        panel.add(backButton, gbc);
        //Styling
        backButton.setBackground(Color.decode("#2c52b3")); // Warna biru
        backButton.setForeground(Color.white);

        gbc.gridx = 1;
        JButton loadButton = new JButton("Load Data");
        panel.add(loadButton, gbc);
        //Styling
        loadButton.setBackground(Color.decode("#79AC78")); // Warna hijau
        loadButton.setForeground(Color.white);

        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String idStr = idText.getText().trim();
                if (idStr.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Employee ID tidak boleh kosong.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int id = Integer.parseInt(idStr);
                Employee employeeData = manager.getEmployeeById(id); // Use Manager instance to get employee data by ID

                if (employeeData == null) {
                    JOptionPane.showMessageDialog(panel, "Employee ID tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Display the data for editing
                gbc.gridx = 0;
                gbc.gridy = 3;
                JLabel nameLabel = new JLabel("Name");
                panel.add(nameLabel, gbc);

                gbc.gridx = 1;
                JTextField nameText = new JTextField(20);
                nameText.setText(employeeData.getNama());
                panel.add(nameText, gbc);

                gbc.gridx = 0;
                gbc.gridy = 4;
                JLabel ageLabel = new JLabel("Age");
                panel.add(ageLabel, gbc);

                gbc.gridx = 1;
                JTextField ageText = new JTextField(20);
                ageText.setText(String.valueOf(employeeData.getUsia()));
                panel.add(ageText, gbc);

                gbc.gridx = 0;
                gbc.gridy = 5;
                JLabel addressLabel = new JLabel("Address");
                panel.add(addressLabel, gbc);

                gbc.gridx = 1;
                JTextField addressText = new JTextField(20);
                addressText.setText(employeeData.getAlamat());
                panel.add(addressText, gbc);

                gbc.gridx = 0;
                gbc.gridy = 6;
                JLabel phoneLabel = new JLabel("Phone");
                panel.add(phoneLabel, gbc);

                gbc.gridx = 1;
                JTextField phoneText = new JTextField(20);
                phoneText.setText(employeeData.getNoTelp());
                panel.add(phoneText, gbc);

                gbc.gridx = 0;
                gbc.gridy = 7;
                JButton editButton = new JButton("Edit");
                panel.add(editButton, gbc);
                //Styling
                editButton.setBackground(Color.decode("#79AC78")); // Warna hijau
                editButton.setForeground(Color.white);

                editButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String name = nameText.getText().trim();
                        String age = ageText.getText().trim();
                        String address = addressText.getText().trim();
                        String phone = phoneText.getText().trim();

                        if (name.isEmpty() || age.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                            JOptionPane.showMessageDialog(panel, "Belum Terisi", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        manager.editEmployee(id, name, Integer.parseInt(age), address, phone); // Use Manager instance to edit employee
                        JOptionPane.showMessageDialog(panel, "Employee edited successfully!");
                        showLoggedInHomePage();
                    }
                });

                panel.revalidate();
                panel.repaint();
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoggedInHomePage();
            }
        });

        panel.revalidate();
        panel.repaint();
    }
}