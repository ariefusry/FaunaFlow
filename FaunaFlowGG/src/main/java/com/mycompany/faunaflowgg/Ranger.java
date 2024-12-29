/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.faunaflowgg;

/**
 *
 * @author ARIEF
 */
public class Ranger {
    private Kandang kandang;
    private Hewan hewan;

    public Ranger() {
        this.kandang = new Kandang(0, "", ""); // Initialize Kandang
        this.hewan = new Hewan("", 0, 0, 0.0); // Initialize Hewan
    }

    public Object[][] viewKandangData() {
        return kandang.getKandangData(); // Use Kandang instance to get kandang data
    }

    public Object[][] viewHewanData() {
        return hewan.getHewanData(); // Use Hewan instance to get hewan data
    }
}
