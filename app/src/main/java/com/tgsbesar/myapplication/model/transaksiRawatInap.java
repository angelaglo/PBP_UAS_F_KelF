package com.tgsbesar.myapplication.model;

import java.io.Serializable;
import java.util.Calendar;

public class transaksiRawatInap implements Serializable {
    private String kelas;
    private String tanggal_masuk;
    private int no_id;
    private Double harga_kamar;

    public transaksiRawatInap( String kelas_kamar, Double harga_kamar, String tanggal, int no_id){
        this.kelas = kelas_kamar;
        this.tanggal_masuk = tanggal;
        this.harga_kamar=harga_kamar;
        this.no_id=no_id;

    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public String getTanggal_masuk() {
        return tanggal_masuk;
    }

    public void setTanggal_masuk(String tanggal_masuk) {
        this.tanggal_masuk = tanggal_masuk;
    }

    public int getNo_id() {
        return no_id;
    }

    public void setNo_id(int no_id) {
        this.no_id = no_id;
    }

    public Double getHarga_kamar() {
        return harga_kamar;
    }

    public void setHarga_kamar(Double harga_kamar) {
        this.harga_kamar = harga_kamar;
    }
}
