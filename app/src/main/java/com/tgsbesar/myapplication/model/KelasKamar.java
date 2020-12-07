package com.tgsbesar.myapplication.model;

import java.io.Serializable;

public class KelasKamar implements Serializable {
    public String tipe_kamar;
    public double harga_kamar;
    public String fasilitas_kamar;
    public Integer jumlah_kamar;

    public KelasKamar(String tipe_kamar, String fasilitas_kamar,double harga_kamar, int jumlah_kamar){
        this.tipe_kamar=tipe_kamar;
        this.fasilitas_kamar=fasilitas_kamar;
        this.harga_kamar=harga_kamar;
        this.jumlah_kamar=jumlah_kamar;
    }

    public String getTipe_kamar() {
        return tipe_kamar;
    }

    public void setTipe_kamar(String tipe_kamar) {
        this.tipe_kamar = tipe_kamar;
    }

    public double getHarga_kamar() {
        return harga_kamar;
    }

    public void setHarga_kamar(double harga_kamar) {
        this.harga_kamar = harga_kamar;
    }

    public String getFasilitas_kamar() {
        return fasilitas_kamar;
    }

    public void setFasilitas_kamar(String fasilitas_kamar) {
        this.fasilitas_kamar = fasilitas_kamar;
    }

    public Integer getJumlah_kamar() {
        return jumlah_kamar;
    }

    public void setJumlah_kamar(Integer jumlah_kamar) {
        this.jumlah_kamar = jumlah_kamar;
    }
}
