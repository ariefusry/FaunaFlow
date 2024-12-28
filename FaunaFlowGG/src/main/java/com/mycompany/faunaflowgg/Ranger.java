/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.faunaflowgg;
import java.util.ArrayList;

/**
 *
 * @author LENOVO
 */
public class Ranger {

    private String IDEmployee;
    private String nama;
    private int usia;
    private String alamat;
    private String noTelepon;
    private ArrayList<Ranger> anakBuah = new ArrayList<>();
    private Manager picManager;
    private ArrayList<String> listStok = new ArrayList<>();
    private ArrayList<PenghuniKandang> listHewan = new ArrayList<>();

    public Ranger(String IDEmployee, String nama, int usia, String alamat, String noTelepon) {
        this.IDEmployee = IDEmployee;
        this.nama = nama;
        this.usia = usia;
        this.alamat = alamat;
        this.noTelepon = noTelepon;
    }

    // Menambahkan Hewan ke dalam listHewan
    public void addHewan(String namaHewan, int umur, String jenisKelamin, int jumlah, String tipeMakan, double berat) {
        PenghuniKandang penghuni = new PenghuniKandang(namaHewan, umur, jenisKelamin, jumlah, tipeMakan, berat);
        listHewan.add(penghuni);
    }

    // Memperbarui status kandang
    public void updateKandang(String nama, String status) {
        System.out.println("Kandang " + nama + " telah diperbarui dengan status: " + status);
    }

    // Mengontrol status kandang
    public void controlKandang(String nama, String status) {
        System.out.println("Kandang " + nama + " sedang dalam kontrol dengan status: " + status);
    }

    // Menampilkan daftar Hewan
    public void showListHewan() {
        System.out.println("Daftar Hewan di bawah pengawasan Ranger " + nama + ":");
        for (PenghuniKandang penghuni : listHewan) {
            System.out.println("Nama Hewan: " + penghuni.getNama() + ", Umur: " + penghuni.getUmur() + ", Jenis Kelamin: " + penghuni.getJenisKelamin());
        }
    }

    // Menampilkan daftar Stok Makanan
    public void showListStok() {
        System.out.println("Daftar Stok Makanan:");
        for (String stok : listStok) {
            System.out.println(stok);
        }
    }

    // Menambahkan ranger ke dalam daftar anak buah
    public void addAnakBuah(Ranger ranger) {
        anakBuah.add(ranger);
    }

    // Menampilkan PIC (Manager yang bertanggung jawab)
    public void showPIC() {
        System.out.println("PIC Manager untuk Ranger " + nama + ": " + picManager.getNama());
    }

    // Menampilkan seluruh daftar Ranger
    public void showAllRanger() {
        System.out.println("Daftar Ranger yang ada:");
        for (Ranger ranger : anakBuah) {
            System.out.println("Ranger: " + ranger.nama);
        }
    }

    // Mendapatkan daftar tugas (Job Desk)
    public List<String> getJobDesk() {
        List<String> jobDesk = new ArrayList<>();
        jobDesk.add("Mengawasi hewan");
        jobDesk.add("Memperbarui status kandang");
        jobDesk.add("Mengontrol kandang");
        jobDesk.add("Menjaga stok makanan");
        return jobDesk;
    }

    // Getter and Setter
    public String getIDEmployee() {
        return IDEmployee;
    }

    public void setIDEmployee(String IDEmployee) {
        this.IDEmployee = IDEmployee;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getUsia() {
        return usia;
    }

    public void setUsia(int usia) {
        this.usia = usia;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
    }

    public Manager getPicManager() {
        return picManager;
    }

    public void setPicManager(Manager picManager) {
        this.picManager = picManager;
    }

    public ArrayList<Ranger> getAnakBuah() {
        return anakBuah;
    }

    public void setAnakBuah(ArrayList<Ranger> anakBuah) {
        this.anakBuah = anakBuah;
    }

    public ArrayList<PenghuniKandang> getListHewan() {
        return listHewan;
    }

    public void setListHewan(ArrayList<PenghuniKandang> listHewan) {
        this.listHewan = listHewan;
    }

    public ArrayList<String> getListStok() {
        return listStok;
    }

    public void setListStok(ArrayList<String> listStok) {
        this.listStok = listStok;
    }
}
