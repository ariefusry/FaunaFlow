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
    private JFrame frame;
    private JPanel panel;

    public SwingUI(EmployeeManagementSystem ems) {
        this.ems = ems;
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

        JMenu featureMenu = new JMenu("Feature");
        JMenuItem employeeListMenuItem = new JMenuItem("Employee List");
        JMenuItem addEmployeeMenuItem = new JMenuItem("Add Employee");

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

        featureMenu.add(employeeListMenuItem);
        if (ems.isAdmin()) { // Check admin role
            featureMenu.add(addEmployeeMenuItem);
        }
        menuBar.add(featureMenu);

        frame.setJMenuBar(menuBar);

        // Display a welcome message
        JLabel welcomeLabel = new JLabel("Welcome, " + (ems.isAdmin() ? "Admin" : "User"));
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
        ArrayList<Employee> employees = ems.getAllEmployees();
        Object[][] data = new Object[employees.size()][5];

        for (int i = 0; i < employees.size(); i++) {
            Employee employee = employees.get(i);
            data[i][0] = employee.getIdEmployee();
            data[i][1] = employee.getNama();
            data[i][2] = employee.getUsia();
            data[i][3] = employee.getAlamat();
            data[i][4] = employee.getNoTelp();
        }

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
                ems.addEmployee(new Employee(0, name, age, address, phone));
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
}