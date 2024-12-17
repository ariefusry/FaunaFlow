/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.faunaflowgg;

/**
 *
 * @author ARIEF
 */
public class Employee {
    protected int idEmployee;
    protected String nama;
    protected int usia;
    protected String alamat;
    protected String noTelp;

    public Employee(int idEmployee, String nama, int usia, String alamat, String noTelp) {
        this.idEmployee = idEmployee;
        this.nama = nama;
        this.usia = usia;
        this.alamat = alamat;
        this.noTelp = noTelp;
    }

    // Getters and setters can be added here if needed
    public int getIdEmployee() {
        return idEmployee;
    }

    public String getNama() {
        return nama;
    }

    public int getUsia() {
        return usia;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getNoTelp() {
        return noTelp;
    }
}
