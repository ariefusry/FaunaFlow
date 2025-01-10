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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class SwingUI {
    private EmployeeManagementSystem ems;
    private Manager manager;
    private Kandang kandang;
    private Hewan hewan;
    private Ranger ranger;
    private JFrame frame;
    private JPanel panel;

    public SwingUI(EmployeeManagementSystem ems) {
        this.ems = ems;
        this.manager = new Manager(ems.getCurrentUser()); // Initialize Manager with current user
        this.kandang = new Kandang(0.0, "", "", ems.getCurrentUser()); // Initialize Kandang with current user
        this.hewan = new Hewan("", 0, 0, 0.0, ems.getCurrentUser()); // Initialize Hewan with parameters
        this.ranger = new Ranger(ems.getCurrentUser()); // Initialize Ranger with current user

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

        // Add About menu
        JMenu aboutMenu = new JMenu("About");
        JMenuItem aboutMenuItem = new JMenuItem("About");

        aboutMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAboutPage();
            }
        });

        aboutMenu.add(aboutMenuItem);
        menuBar.add(aboutMenu);

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

        String[] columnNames = {"No", "Nama Karyawan", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"};
        Object[][] data = manager.getJobdeskKaryawanData(); // Use Manager to get JobdeskKaryawan data

        // Add row numbers and remove the ID column from the data
        Object[][] tableData = new Object[data.length][9];
        for (int i = 0; i < data.length; i++) {
            tableData[i][0] = i + 1; // Row number
            System.arraycopy(data[i], 1, tableData[i], 1, 8); // Adjust the length to 8
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

        // Re-initialize all instances with the current user after login
        this.manager = new Manager(ems.getCurrentUser());
        this.kandang = new Kandang(0.0, "", "", ems.getCurrentUser()); // Reinitialize with current user
        this.hewan = new Hewan("", 0, 0, 0.0, ems.getCurrentUser()); 
        this.ranger = new Ranger(ems.getCurrentUser());

        JMenuBar menuBar = new JMenuBar();
        JMenu accountMenu = new JMenu("Account"); // Add this line to declare accountMenu
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

        // Add About menu next to Account menu
        JMenu aboutMenu = new JMenu("About");
        JMenuItem aboutMenuItem = new JMenuItem("About");

        aboutMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAboutPage();
            }
        });

        aboutMenu.add(aboutMenuItem);
        menuBar.add(aboutMenu);

        if (ems.isAdmin()) { // Check admin role
            JMenuItem createAccountMenuItem = new JMenuItem("Create Account");
            createAccountMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showCreateAccountPage();
                }
            });
            accountMenu.add(createAccountMenuItem);
        }

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
            JMenuItem removeEmployeeMenuItem = new JMenuItem("Remove Employee");
            JMenuItem assignJobdeskMenuItem = new JMenuItem("Assign Random Jobdesks");
            JMenuItem deleteAllJobdeskMenuItem = new JMenuItem("Delete All Jobdesks");
            JMenuItem viewReportsMenuItem = new JMenuItem("View Reports");
            JMenuItem editEmployeeMenuItem = new JMenuItem("Edit Employee");
            JMenuItem viewLogsMenuItem = new JMenuItem("View Logs"); // New menu item for viewing logs
            JMenuItem deleteAllLogsMenuItem = new JMenuItem("Delete All Logs"); // New menu item for deleting all logs
            JMenuItem deleteReportMenuItem = new JMenuItem("Delete Report"); // New menu item for deleting reports

            employeeListMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showEmployeeList();
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

            viewLogsMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showLogsPage(); // Show logs page
                }
            });

            deleteAllLogsMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int confirm = JOptionPane.showConfirmDialog(panel, "Are you sure you want to delete all logs?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        manager.deleteAllLogs();
                        JOptionPane.showMessageDialog(panel, "All logs deleted successfully!");
                    }
                }
            });

            deleteReportMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showDeleteReportPage(); // Show delete report page
                }
            });

            employeeMenu.add(employeeListMenuItem);
            employeeMenu.add(removeEmployeeMenuItem);
            employeeMenu.add(editEmployeeMenuItem);
            employeeMenu.addSeparator();
            employeeMenu.add(assignJobdeskMenuItem);
            employeeMenu.add(deleteAllJobdeskMenuItem);
            employeeMenu.addSeparator();
            employeeMenu.add(viewReportsMenuItem);
            employeeMenu.add(viewLogsMenuItem); // Add the new menu item to the employee menu
            employeeMenu.add(deleteAllLogsMenuItem); // Add the new menu item to the employee menu
            employeeMenu.add(deleteReportMenuItem); // Add the new menu item to the employee menu
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
                    // Reinitialize kandang with current user after successful login
                    kandang = new Kandang(0.0, "", "", ems.getCurrentUser());
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
                try {
                    int id = Integer.parseInt(idStr);
                    boolean success = manager.removeEmployee(id); // Use Manager to remove employee
                    if (success) {
                        JOptionPane.showMessageDialog(panel, "Employee and associated account removed successfully!");
                        showLoggedInHomePage();
                    } else {
                        JOptionPane.showMessageDialog(panel, "Employee with ID: " + id + " not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Invalid input for Employee ID. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
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
                String ukuranStr = ukuranText.getText().trim();
                String tipe = tipeText.getText().trim();
                String spesialitas = spesialitasText.getText().trim();

                if (ukuranStr.isEmpty() || tipe.isEmpty() || spesialitas.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "All fields must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (tipe.length() > 150 || spesialitas.length() > 150) {
                    JOptionPane.showMessageDialog(frame, "Tipe and Spesialitas must be less than 150 characters.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    double ukuran = Double.parseDouble(ukuranStr);
                    kandang.addKandang(String.valueOf(ukuran), tipe, spesialitas);
                    JOptionPane.showMessageDialog(frame, "Kandang added successfully!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid input for Ukuran. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
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
                    JOptionPane.showMessageDialog(panel, "ID Kandang and ID Hewan cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    int idKandang = Integer.parseInt(idKandangStr);
                    int idHewan = Integer.parseInt(idHewanStr);
                    manager.setHewanToKandang(idKandang, idHewan);
                    JOptionPane.showMessageDialog(panel, "Hewan set to kandang successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    showLoggedInHomePage();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Invalid number format for ID Kandang or ID Hewan.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
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
                String umurStr = umurText.getText().trim();
                String jumlahStr = jumlahText.getText().trim();
                String beratStr = beratText.getText().trim();

                if (nama.isEmpty() || umurStr.isEmpty() || jumlahStr.isEmpty() || beratStr.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Belum Terisi", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    int umur = Integer.parseInt(umurStr);
                    int jumlah = Integer.parseInt(jumlahStr);
                    double berat = Double.parseDouble(beratStr);

                    hewan.addHewan(nama, umur, jumlah, berat); // Use Hewan instance to add hewan
                    JOptionPane.showMessageDialog(panel, "Hewan added successfully!");
                    showLoggedInHomePage();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Umur harus berupa angka, Jumlah harus berupa angka, dan Berat harus berupa angka desimal.", "Error", JOptionPane.ERROR_MESSAGE);
                }
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

        JLabel titleLabel = new JLabel("Penghuni Kandang");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"Tipe", "Ukuran", "Nama Hewan", "Jumlah"};
        Object[][] data = manager.getKandangContents(); // Use Manager instance to get kandang contents data

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
                try {
                    int id = Integer.parseInt(idStr);
                    kandang.deleteKandang(id); // Use Kandang instance to delete kandang
                    JOptionPane.showMessageDialog(panel, "Kandang deleted successfully!");
                    showLoggedInHomePage();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Invalid input for Kandang ID. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
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
                try {
                    String idStr = idText.getText().trim();
                    if (idStr.isEmpty()) {
                        JOptionPane.showMessageDialog(panel, "Hewan ID tidak boleh kosong.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int id = Integer.parseInt(idStr);
                    
                    // Check if hewan exists before attempting to delete
                    Object[] hewanData = hewan.getHewanById(id);
                    if (hewanData == null) {
                        JOptionPane.showMessageDialog(panel, "Hewan with ID: " + id + " does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    hewan.deleteHewan(id);
                    JOptionPane.showMessageDialog(panel, "Hewan deleted successfully!");
                    showLoggedInHomePage();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Please enter a valid ID number", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
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
        JLabel reportLabel = new JLabel("Report");
        panel.add(reportLabel, gbc);

        gbc.gridx = 1;
        JTextArea reportText = new JTextArea(5, 20);
        reportText.setLineWrap(true); // Enable line wrap
        reportText.setWrapStyleWord(true); // Wrap at word boundaries
        JScrollPane scrollPane = new JScrollPane(reportText);
        panel.add(scrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
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
                String laporan = reportText.getText().trim();

                if (laporan.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Report cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Employee currentUser = ems.getCurrentUser();
                if (currentUser == null) {
                    JOptionPane.showMessageDialog(panel, "No user is currently logged in.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String nama = currentUser.getNama(); // Get the name of the logged-in user from the employee table
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
                String jumlahStr = jumlahText.getText().trim();
                String satuan = satuanText.getText().trim();
                String gudangStr = gudangText.getText().trim();

                if (kategori.isEmpty() || nama.isEmpty() || jumlahStr.isEmpty() || satuan.isEmpty() || gudangStr.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "All fields must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    int jumlah = Integer.parseInt(jumlahStr);
                    int idGudang = Integer.parseInt(gudangStr);

                    if (ems.getCurrentUser() == null) {
                        JOptionPane.showMessageDialog(panel, "Current user is not set.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    manager.tambahStok(kategori, nama, jumlah, satuan, idGudang);
                    JOptionPane.showMessageDialog(panel, "Stock added successfully!");
                    showLoggedInHomePage();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Jumlah harus berupa angka dan ID Gudang harus berupa angka.", "Error", JOptionPane.ERROR_MESSAGE);
                }
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

                try {
                    int id = Integer.parseInt(idStr);
                    Stok stokData = manager.getStokById(id); // Use Manager instance to get stock data by ID

                    if (stokData == null) {
                        JOptionPane.showMessageDialog(panel, "ID Stok tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Navigate to new page for editing
                    showEditStokDetailsPage(id, stokData);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Invalid input for ID Stok. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
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

    private void showEditStokDetailsPage(int id, Stok stokData) {
        panel.removeAll();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel kategoriLabel = new JLabel("Kategori");
        panel.add(kategoriLabel, gbc);

        gbc.gridx = 1;
        JTextField kategoriText = new JTextField(20);
        kategoriText.setText(stokData.getKategoriStok());
        panel.add(kategoriText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel namaLabel = new JLabel("Nama Stok");
        panel.add(namaLabel, gbc);

        gbc.gridx = 1;
        JTextField namaText = new JTextField(20);
        namaText.setText(stokData.getNamaStok());
        panel.add(namaText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel jumlahLabel = new JLabel("Jumlah");
        panel.add(jumlahLabel, gbc);

        gbc.gridx = 1;
        JTextField jumlahText = new JTextField(20);
        jumlahText.setText(String.valueOf(stokData.getJumlah()));
        panel.add(jumlahText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel satuanLabel = new JLabel("Satuan");
        panel.add(satuanLabel, gbc);

        gbc.gridx = 1;
        JTextField satuanText = new JTextField(20);
        satuanText.setText(stokData.getSatuan());
        panel.add(satuanText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel gudangLabel = new JLabel("Gudang");
        panel.add(gudangLabel, gbc);

        gbc.gridx = 1;
        JTextField gudangText = new JTextField(20);
        gudangText.setText(stokData.getNamaGudang());
        panel.add(gudangText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        JButton backButton = new JButton("Back");
        panel.add(backButton, gbc);
        //Styling
        backButton.setBackground(Color.decode("#2c52b3")); // Warna biru
        backButton.setForeground(Color.white);

        gbc.gridx = 1;
        JButton editButton = new JButton("Edit");
        panel.add(editButton, gbc);
        //Styling
        editButton.setBackground(Color.decode("#79AC78")); // Warna hijau
        editButton.setForeground(Color.white);

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String kategori = kategoriText.getText().trim();
                String nama = namaText.getText().trim();
                String jumlahStr = jumlahText.getText().trim();
                String satuan = satuanText.getText().trim();
                String gudang = gudangText.getText().trim();

                if (kategori.isEmpty() || nama.isEmpty() || jumlahStr.isEmpty() || satuan.isEmpty() || gudang.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Belum Terisi", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    int jumlah = Integer.parseInt(jumlahStr);
                    manager.updateStok(id, kategori, nama, jumlah, satuan, gudang); // Use Manager instance to update stock
                    JOptionPane.showMessageDialog(panel, "Stok updated successfully!");
                    showCekStokPage();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Jumlah harus berupa angka.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showUpdateStokPage();
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
        JButton deleteButton = new JButton("Delete");
        panel.add(deleteButton, gbc);
        //Styling
        deleteButton.setBackground(Color.decode("#FF0000")); // Warna merah
        deleteButton.setForeground(Color.white);

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String idStr = idText.getText().trim();
                if (idStr.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "ID Stok tidak boleh kosong.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    int id = Integer.parseInt(idStr);
                    boolean success = manager.deleteStok(id); // Use Manager to delete stock
                    if (success) {
                        JOptionPane.showMessageDialog(panel, "Stok berhasil dihapus!");
                        showCekStokPage(); // Refresh the stock page
                    } else {
                        JOptionPane.showMessageDialog(panel, "Stok dengan ID: " + id + " tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Invalid input for ID Stok. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
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

                try {
                    int id = Integer.parseInt(idStr);
                    Object[] kandangData = kandang.getKandangById(id); // Use Kandang instance to get kandang data by ID

                    if (kandangData == null) {
                        JOptionPane.showMessageDialog(panel, "Kandang ID tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Navigate to new page for editing
                    showEditKandangDetailsPage(id, kandangData);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Invalid input for Kandang ID. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
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

    private void showEditKandangDetailsPage(int id, Object[] kandangData) {
        panel.removeAll();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel ukuranLabel = new JLabel("Ukuran");
        panel.add(ukuranLabel, gbc);

        gbc.gridx = 1;
        JTextField ukuranText = new JTextField(20);
        ukuranText.setText(String.valueOf(kandangData[1]));
        panel.add(ukuranText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel tipeLabel = new JLabel("Tipe");
        panel.add(tipeLabel, gbc);

        gbc.gridx = 1;
        JTextField tipeText = new JTextField(20);
        tipeText.setText(String.valueOf(kandangData[2]));
        panel.add(tipeText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel spesialitasLabel = new JLabel("Spesialitas");
        panel.add(spesialitasLabel, gbc);

        gbc.gridx = 1;
        JTextField spesialitasText = new JTextField(20);
        spesialitasText.setText(String.valueOf(kandangData[3]));
        panel.add(spesialitasText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JButton backButton = new JButton("Back");
        panel.add(backButton, gbc);
        //Styling
        backButton.setBackground(Color.decode("#2c52b3")); // Warna biru
        backButton.setForeground(Color.white);

        gbc.gridx = 1;
        JButton editButton = new JButton("Edit");
        panel.add(editButton, gbc);
        //Styling
        editButton.setBackground(Color.decode("#79AC78")); // Warna hijau
        editButton.setForeground(Color.white);

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String ukuranStr = ukuranText.getText().trim();
                String tipe = tipeText.getText().trim();
                String spesialitas = spesialitasText.getText().trim();

                if (ukuranStr.isEmpty() || tipe.isEmpty() || spesialitas.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Belum Terisi", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    double ukuran = Double.parseDouble(ukuranStr);
                    kandang.editKandang(id, ukuranStr, tipe, spesialitas); // Use Kandang instance to edit kandang
                    JOptionPane.showMessageDialog(panel, "Kandang edited successfully!");
                    showLoggedInHomePage();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Ukuran harus berupa angka desimal.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showEditKandangPage();
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

                try {
                    int id = Integer.parseInt(idStr);
                    Object[] hewanData = hewan.getHewanById(id); // Use Hewan instance to get hewan data by ID

                    if (hewanData == null) {
                        JOptionPane.showMessageDialog(panel, "Hewan ID tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Navigate to new page for editing
                    showEditHewanDetailsPage(id, hewanData);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Invalid input for Hewan ID. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
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

    private void showEditHewanDetailsPage(int id, Object[] hewanData) {
        panel.removeAll();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel umurLabel = new JLabel("Umur");
        panel.add(umurLabel, gbc);

        gbc.gridx = 1;
        JTextField umurText = new JTextField(20);
        umurText.setText(String.valueOf(hewanData[2]));
        panel.add(umurText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel jumlahLabel = new JLabel("Jumlah");
        panel.add(jumlahLabel, gbc);

        gbc.gridx = 1;
        JTextField jumlahText = new JTextField(20);
        jumlahText.setText(String.valueOf(hewanData[3]));
        panel.add(jumlahText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel beratLabel = new JLabel("Berat");
        panel.add(beratLabel, gbc);

        gbc.gridx = 1;
        JTextField beratText = new JTextField(20);
        beratText.setText(String.valueOf(hewanData[4]));
        panel.add(beratText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JButton backButton = new JButton("Back");
        panel.add(backButton, gbc);
        //Styling
        backButton.setBackground(Color.decode("#2c52b3")); // Warna biru
        backButton.setForeground(Color.white);

        gbc.gridx = 1;
        JButton editButton = new JButton("Edit");
        panel.add(editButton, gbc);
        //Styling
        editButton.setBackground(Color.decode("#79AC78")); // Warna hijau
        editButton.setForeground(Color.white);

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String umurStr = umurText.getText().trim();
                String jumlahStr = jumlahText.getText().trim();
                String beratStr = beratText.getText().trim();

                if (umurStr.isEmpty() || jumlahStr.isEmpty() || beratStr.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Belum Terisi", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    int umur = Integer.parseInt(umurStr);
                    int jumlah = Integer.parseInt(jumlahStr);
                    double berat = Double.parseDouble(beratStr);
                    hewan.editHewan(id, umur, jumlah, berat); // Use Hewan instance to edit hewan
                    JOptionPane.showMessageDialog(panel, "Hewan edited successfully!");
                    showLoggedInHomePage();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Umur harus berupa angka, Jumlah harus berupa angka, dan Berat harus berupa angka desimal.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showEditHewanPage();
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

                try {
                    int id = Integer.parseInt(idStr);
                    Employee employeeData = manager.getEmployeeById(id); // Use Manager instance to get employee data by ID

                    if (employeeData == null) {
                        JOptionPane.showMessageDialog(panel, "Employee ID tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    showEditEmployeeDetailsPage(id, employeeData); // Navigate to the edit details page
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Invalid input for Employee ID. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
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

    private void showEditEmployeeDetailsPage(int id, Employee employeeData) {
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
        nameText.setText(employeeData.getNama());
        panel.add(nameText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel ageLabel = new JLabel("Age");
        panel.add(ageLabel, gbc);

        gbc.gridx = 1;
        JTextField ageText = new JTextField(20);
        ageText.setText(String.valueOf(employeeData.getUsia()));
        panel.add(ageText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel addressLabel = new JLabel("Address");
        panel.add(addressLabel, gbc);

        gbc.gridx = 1;
        JTextField addressText = new JTextField(20);
        addressText.setText(employeeData.getAlamat());
        panel.add(addressText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel phoneLabel = new JLabel("Phone");
        panel.add(phoneLabel, gbc);

        gbc.gridx = 1;
        JTextField phoneText = new JTextField(20);
        phoneText.setText(employeeData.getNoTelp());
        panel.add(phoneText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        JButton backButton = new JButton("Back");
        panel.add(backButton, gbc);
        //Styling
        backButton.setBackground(Color.decode("#2c52b3")); // Warna biru
        backButton.setForeground(Color.white);

        gbc.gridx = 1;
        JButton editButton = new JButton("Edit");
        panel.add(editButton, gbc);
        //Styling
        editButton.setBackground(Color.decode("#79AC78")); // Warna hijau
        editButton.setForeground(Color.white);

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameText.getText().trim();
                String ageStr = ageText.getText().trim();
                String address = addressText.getText().trim();
                String phoneStr = phoneText.getText().trim();

                if (name.isEmpty() || ageStr.isEmpty() || address.isEmpty() || phoneStr.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "All fields must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    int age = Integer.parseInt(ageStr);
                    int phone = Integer.parseInt(phoneStr); // Validate phone as integer
                    manager.editEmployee(id, name, age, address, phoneStr);
                    JOptionPane.showMessageDialog(panel, "Employee edited successfully!");
                    showLoggedInHomePage();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Age and Phone must be valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showEditEmployeePage();
            }
        });

        panel.revalidate();
        panel.repaint();
    }

    private void showAboutPage() {
        panel.removeAll();
        panel.setLayout(new BorderLayout());

        // Create a title label
        JLabel titleLabel = new JLabel("About FaunaFlow");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Create a styled text area for the about content
        JTextPane aboutText = new JTextPane();
        aboutText.setContentType("text/html");
        aboutText.setText("<html><body style='font-family: Arial; font-size: 14px; text-align: center;'>" +
            "<p>Pengelolaan kebun binatang memerlukan sistem terintegrasi untuk memudahkan pencatatan data hewan dan staf. " +
            "Manajemen yang kurang efektif dapat mengakibatkan ketidaksesuaian pengaturan pada jadwal perawatan, kesulitan pencatatan kesehatan hewan, " +
            "juga ketidakjelasan tanggung jawab para keeper. Aplikasi GUI ini dirancang untuk memudahkan manajemen kebun binatang dengan mengumpulkan berbagai fungsi " +
            "dalam satu platform yang bertujuan mendukung kelancaran operasional dan meningkatkan kualitas hidup hewan.</p>" +
            "<h2>Dibuat oleh:</h2>" +
            "<ul style='list-style-type: none;'>" +
            "<li>Arief Muhammad Usry - 1301223069</li>" +
            "<li>Ghazy Fadhal Ramadhan - 1301223359</li>" +
            "<li>Ratu Bunga Sabil Janah - 1301220061</li>" +
            "<li>Nazwa Betha Kirana - 1301223235</li>" +
            "</ul>" +
            "<p>Telkom University - S1 Informatika - Pemodelan Berbasis Objek</p>" +
            "</body></html>");
        aboutText.setEditable(false);
        aboutText.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(aboutText);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Create a back button
        JButton backButton = new JButton("Back");
        backButton.setBackground(Color.decode("#2c52b3")); // Warna biru
        backButton.setForeground(Color.white);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ems.isLoggedIn()) {
                    showLoggedInHomePage();
                } else {
                    showHomePage();
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        buttonPanel.add(backButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        panel.revalidate();
        panel.repaint();
    }

    private void showCreateAccountPage() {
        if (!ems.isAdmin()) {
            JOptionPane.showMessageDialog(panel, "Only admins can create accounts.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        panel.removeAll();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel usernameLabel = new JLabel("Username");
        panel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        JTextField usernameText = new JTextField(20);
        panel.add(usernameText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel passwordLabel = new JLabel("Password");
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        JPasswordField passwordText = new JPasswordField(20);
        panel.add(passwordText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel roleLabel = new JLabel("Role");
        panel.add(roleLabel, gbc);

        gbc.gridx = 1;
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"admin", "user"});
        panel.add(roleComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JButton backButton = new JButton("Back");
        panel.add(backButton, gbc);
        //Styling
        backButton.setBackground(Color.decode("#2c52b3")); // Warna biru
        backButton.setForeground(Color.white);

        gbc.gridx = 1;
        JButton nextButton = new JButton("Next");
        panel.add(nextButton, gbc);
        //Styling
        nextButton.setBackground(Color.decode("#79AC78")); // Warna hijau
        nextButton.setForeground(Color.white);

        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameText.getText().trim();
                String password = new String(passwordText.getPassword()).trim();
                String role = (String) roleComboBox.getSelectedItem();

                if (username.isEmpty() || password.isEmpty() || role.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "All fields must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (role.equals("user")) {
                    showCreateEmployeePage(username, password, role);
                } else {
                    manager.createAccount(username, password, role, null);
                    JOptionPane.showMessageDialog(panel, "Admin account created successfully!");
                    showLoggedInHomePage();
                }
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

    private void showCreateEmployeePage(String username, String password, String role) {
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
        JButton createButton = new JButton("Create");
        panel.add(createButton, gbc);
        //Styling
        createButton.setBackground(Color.decode("#79AC78")); // Warna hijau
        createButton.setForeground(Color.white);

        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameText.getText().trim();
                String age = ageText.getText().trim();
                String address = addressText.getText().trim();
                String phone = phoneText.getText().trim();

                if (name.isEmpty() || age.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "All fields must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    int ageInt = Integer.parseInt(age);
                    int phoneInt = Integer.parseInt(phone); // Validate phone as integer
                    Employee employee = new Employee(0, name, ageInt, address, phone);
                    manager.createAccount(username, password, role, employee);
                    JOptionPane.showMessageDialog(panel, "User account and employee created successfully!");
                    showLoggedInHomePage();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Age and Phone must be valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showCreateAccountPage();
            }
        });

        panel.revalidate();
        panel.repaint();
    }

    private void showLogsPage() {
        panel.removeAll();
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Data Processing Logs");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Username", "Change Description", "Change Time"};
        Object[][] data = manager.getLogsData(); // Use Manager to get logs data

        if (data.length == 0) {
            JLabel noDataLabel = new JLabel("No logs available.");
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

    private void showDeleteReportPage() {
        if (!ems.isAdmin()) {
            JOptionPane.showMessageDialog(panel, "Only admins can delete reports.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        panel.removeAll();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Display all reports data
        String[] columnNames = {"ID", "Nama", "Laporan", "Tanggal"};
        Object[][] data = manager.getAllReports(); // Use Manager to get reports

        if (data.length == 0) {
            JLabel noDataLabel = new JLabel("No reports available.");
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
        JLabel idLabel = new JLabel("Report ID");
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

        gbc.gridx = 0;
        gbc.gridy = 3;
        JButton deleteAllButton = new JButton("Delete All");
        panel.add(deleteAllButton, gbc);
        //Styling
        deleteAllButton.setBackground(Color.decode("#FF0000")); // Warna merah
        deleteAllButton.setForeground(Color.white);

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int idReport = Integer.parseInt(idText.getText().trim());
                    System.out.println("Parsed Report ID: " + idReport); // Debugging statement
                    boolean success = manager.deleteReport(idReport);
                    if (success) {
                        JOptionPane.showMessageDialog(panel, "Report deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        showLoggedInHomePage(); // Navigate away from the delete report page
                    } else {
                        JOptionPane.showMessageDialog(panel, "Failed to delete report. Report ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Invalid Report ID. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(panel, "Are you sure you want to delete all reports?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    manager.deleteAllReports(); // Use Manager to delete all reports
                    JOptionPane.showMessageDialog(panel, "All reports deleted successfully!");
                    showDeleteReportPage(); // Refresh the page
                }
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