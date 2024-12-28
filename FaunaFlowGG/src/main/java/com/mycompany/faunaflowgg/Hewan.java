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
public class Hewan {
    private ArrayList<penghunikandang> listHewan = new ArrayList<>();

    public void addHewan(penghunikandang penghuni) {
        listHewan.add(penghuni);
    }

    public void showAllHewan() {
        for (penghunikandang penghuni : listHewan) {
            System.out.println("Nama: " + penghuni.getNama() + ", Umur: " + penghuni.getUmur());
        }
    }
}
