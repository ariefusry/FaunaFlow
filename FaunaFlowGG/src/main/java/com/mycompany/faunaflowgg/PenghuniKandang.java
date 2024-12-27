/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.faunaflowgg;

/**
 *
 * @author ASUS
 */
public class PenghuniKandang {
    private String nama;
    private int umur;
    private String jKelamin;
    private int jumlah;
    private String tipeMakan;
    private double berat;
    
    public PenghuniKandang(String nama, int umur, String jKelamin, int jumlah, String tipeMakan, double berat) {
        this.nama = nama;
        this.umur = umur;
        this.jKelamin = jKelamin;
        this.jumlah = jumlah;
        this.tipeMakan = tipeMakan;
        this.berat = berat;
    }
    
    public String getUkuranKandang() {
        return "Ukuran kandang ditentukan";
    }

    public String getTipeKandang() {
        return "Tipe kandang ditentukan";
    }

    public String getSpesialKandang() {
        return "Kebutuhan spesial kandang ditentukan";
    }

    public String statusHewan() {
        return "Status hewan ditentukan";
    }

    public String statusKandang() {
        return "Status kandang ditentukan";
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getUmur() {
        return umur;
    }

    public void setUmur(int umur) {
        this.umur = umur;
    }

    public String getJKelamin() {
        return jKelamin;
    }

    public void setJKelamin(String jKelamin) {
        this.jKelamin = jKelamin;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public String getTipeMakan() {
        return tipeMakan;
    }

    public void setTipeMakan(String tipeMakan) {
        this.tipeMakan = tipeMakan;
    }

    public double getBerat() {
        return berat;
    }

    public void setBerat(double berat) {
        this.berat = berat;
    }
}
