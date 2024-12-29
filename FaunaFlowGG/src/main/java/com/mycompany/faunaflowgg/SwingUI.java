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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

public class SwingUI {
    private EmployeeManagementSystem ems;
    private Manager manager; // Add Manager instance
    private Kandang kandang; // Add Kandang instance
    private Hewan hewan; // Add Hewan instance
    private JFrame frame;
    private JPanel panel;

    public SwingUI(EmployeeManagementSystem ems) {
        this.ems = ems;
        this.manager = new Manager(); // Initialize Manager
        this.kandang = new Kandang(0, "", ""); // Initialize Kandang
        this.hewan = new Hewan("", 0, 0, 0.0); // Initialize Hewan with parameters
        frame = new JFrame("FaunaFlow");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
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

        // Display a welcome message
        JLabel welcomeLabel = new JLabel("Welcome to FaunaFlow");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(welcomeLabel, BorderLayout.CENTER);

        panel.revalidate();
        panel.repaint();
        frame.setVisible(true);
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
                showHomePage();
            }
        });

        accountMenu.add(logoutMenuItem);
        menuBar.add(accountMenu);

        if (ems.isAdmin()) { // Check admin role
            JMenu employeeMenu = new JMenu("Employee");
            JMenuItem employeeListMenuItem = new JMenuItem("Employee List");
            JMenuItem addEmployeeMenuItem = new JMenuItem("Add Employee");
            JMenuItem removeEmployeeMenuItem = new JMenuItem("Remove Employee");

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

            employeeMenu.add(employeeListMenuItem);
            employeeMenu.add(addEmployeeMenuItem);
            employeeMenu.add(removeEmployeeMenuItem);
            menuBar.add(employeeMenu);

            JMenu zooMenu = new JMenu("Zoo");
            JMenuItem addKandangMenuItem = new JMenuItem("Add Kandang");
            JMenuItem setHewanToKandangMenuItem = new JMenuItem("Set Hewan to Kandang");
            JMenuItem addHewanMenuItem = new JMenuItem("Add Hewan");
            JMenuItem viewKandangMenuItem = new JMenuItem("View Kandang");
            JMenuItem viewHewanMenuItem = new JMenuItem("View Hewan");

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

            addHewanMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showAddHewanPage();
                }
            });

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

            zooMenu.add(addKandangMenuItem);
            zooMenu.add(setHewanToKandangMenuItem);
            zooMenu.add(addHewanMenuItem);
            zooMenu.add(viewKandangMenuItem);
            zooMenu.add(viewHewanMenuItem);
            menuBar.add(zooMenu);
        }

        frame.setJMenuBar(menuBar);

        // Display a welcome message
        JLabel welcomeLabel = new JLabel("Welcome, " + (ems.isAdmin() ? "Manager" : "Ranger"));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(welcomeLabel, BorderLayout.CENTER);

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

        gbc.gridx = 1;
        JButton loginButton = new JButton("Login");
        panel.add(loginButton, gbc);

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

        gbc.gridx = 1;
        JButton addButton = new JButton("Add");
        panel.add(addButton, gbc);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameText.getText();
                int age = Integer.parseInt(ageText.getText());
                String address = addressText.getText();
                String phone = phoneText.getText();
                manager.addEmployee(new Employee(0, name, age, address, phone)); // Use Manager to add employee
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

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel idLabel = new JLabel("Employee ID");
        panel.add(idLabel, gbc);

        gbc.gridx = 1;
        JTextField idText = new JTextField(20);
        panel.add(idText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JButton backButton = new JButton("Back");
        panel.add(backButton, gbc);

        gbc.gridx = 1;
        JButton removeButton = new JButton("Remove");
        panel.add(removeButton, gbc);

        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(idText.getText());
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
        JLabel ukuranLabel = new JLabel("Ukuran");
        panel.add(ukuranLabel, gbc);

        gbc.gridx = 1;
        JTextField ukuranText = new JTextField(20);
        panel.add(ukuranText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel tipeLabel = new JLabel("Tipe");
        panel.add(tipeLabel, gbc);

        gbc.gridx = 1;
        JTextField tipeText = new JTextField(20);
        panel.add(tipeText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel spesialitasLabel = new JLabel("Spesialitas");
        panel.add(spesialitasLabel, gbc);

        gbc.gridx = 1;
        JTextField spesialitasText = new JTextField(20);
        panel.add(spesialitasText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JButton backButton = new JButton("Back");
        panel.add(backButton, gbc);

        gbc.gridx = 1;
        JButton addButton = new JButton("Add");
        panel.add(addButton, gbc);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String ukuran = ukuranText.getText();
                String tipe = tipeText.getText();
                String spesialitas = spesialitasText.getText();
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

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel idKandangLabel = new JLabel("ID Kandang");
        panel.add(idKandangLabel, gbc);

        gbc.gridx = 1;
        JTextField idKandangText = new JTextField(20);
        panel.add(idKandangText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel idHewanLabel = new JLabel("ID Hewan");
        panel.add(idHewanLabel, gbc);

        gbc.gridx = 1;
        JTextField idHewanText = new JTextField(20);
        panel.add(idHewanText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JButton backButton = new JButton("Back");
        panel.add(backButton, gbc);

        gbc.gridx = 1;
        JButton setButton = new JButton("Set");
        panel.add(setButton, gbc);

        setButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int idKandang = Integer.parseInt(idKandangText.getText());
                int idHewan = Integer.parseInt(idHewanText.getText());
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

        gbc.gridx = 1;
        JButton addButton = new JButton("Add");
        panel.add(addButton, gbc);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nama = namaText.getText();
                int umur = Integer.parseInt(umurText.getText());
                int jumlah = Integer.parseInt(jumlahText.getText());
                double berat = Double.parseDouble(beratText.getText());
                hewan.addHewan(nama, umur, jumlah, berat); // Use Hewan instance to add hewan
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

        System.out.println("Data length: " + data.length); // Debug statement

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
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
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
                table.getColumnModel().getColumn(i).setPreferredWidth(150); // Set preferred width for each column
            }

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            panel.add(scrollPane, BorderLayout.CENTER);
        }

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
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
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
                table.getColumnModel().getColumn(i).setPreferredWidth(150); // Set preferred width for each column
            }

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            panel.add(scrollPane, BorderLayout.CENTER);
        }

        panel.revalidate();
        panel.repaint();
    }
}