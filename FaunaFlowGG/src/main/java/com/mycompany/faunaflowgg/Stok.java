package com.mycompany.faunaflowgg;

public class Stok {
    private int idStok;
    private String kategoriStok;
    private String namaStok;
    private int jumlah;
    private String satuan;
    private String namaGudang;

    public Stok(int idStok, String kategoriStok, String namaStok, int jumlah, String satuan, String namaGudang) {
        this.idStok = idStok;
        this.kategoriStok = kategoriStok;
        this.namaStok = namaStok;
        this.jumlah = jumlah;
        this.satuan = satuan;
        this.namaGudang = namaGudang;
    }

    // Getters
    public int getIdStok() {
        return idStok;
    }

    public String getKategoriStok() {
        return kategoriStok;
    }

    public String getNamaStok() {
        return namaStok;
    }

    public int getJumlah() {
        return jumlah;
    }

    public String getSatuan() {
        return satuan;
    }

    public String getNamaGudang() {
        return namaGudang;
    }

    @Override
    public String toString() {
        return "ID: " + idStok + ", Kategori: " + kategoriStok + ", Nama: " + namaStok + ", Jumlah: " + jumlah + " " + satuan + ", Gudang: " + namaGudang;
    }
}
