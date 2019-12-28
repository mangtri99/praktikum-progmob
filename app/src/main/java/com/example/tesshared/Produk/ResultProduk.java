package com.example.tesshared.Produk;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultProduk {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("kode")
    @Expose
    private String kode;
    @SerializedName("ukuran")
    @Expose
    private String ukuran;
    @SerializedName("harga")
    @Expose
    private int harga;
    private String kategori;
    private String image;


    public  String getKode(){
        return kode;
    }

    public void SetKode(String kode){ this.kode = kode; }

    public String getUkuran() {
        return ukuran;
    }

    public void setUkuran(String ukuran) {
        this.ukuran = ukuran;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
