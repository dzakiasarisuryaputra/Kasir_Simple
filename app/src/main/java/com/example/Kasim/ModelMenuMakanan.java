package com.example.Kasim;

import java.io.Serializable;

public class ModelMenuMakanan  implements Serializable {
    private String nama, deskripsi;
    private int idItem, id_gambar;
    private int iAmount, iPrice;

    public ModelMenuMakanan(String nama, String deskripsi, int id_gambar, int idItem, int iAmount, int iPrice) {
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.id_gambar = id_gambar;
        this.idItem = idItem;
        this.iAmount = iAmount;
        this.iPrice = iPrice;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public int getId_gambar() {
        return id_gambar;
    }

    public void setId_gambar(int id_gambar) {
        this.id_gambar = id_gambar;
    }

    public int getiAmount() {
        return iAmount;
    }

    public void setiAmount(int iAmount) {
        this.iAmount = iAmount;
    }

    public int getiPrice() {
        return iPrice;
    }

    public void setiPrice(int iPrice) {
        this.iPrice = iPrice;
    }
}
