package com.tgsbesar.myapplication.model;

import java.io.Serializable;

public class transaksiLaboratorium implements Serializable {


    public String paket_checkUp;
    public String jam_checkUp;
    public String tgl_checkUp;
    public double harga_checkUp;
    public String email;


    public transaksiLaboratorium(String paket_checkUp, String tgl_checkUp, double harga_checkUp, String jam_checkUp, String email) {
        this.paket_checkUp = paket_checkUp;
        this.tgl_checkUp = tgl_checkUp;
        this.harga_checkUp = harga_checkUp;
        this.jam_checkUp = jam_checkUp;
        this.email = email;
    }

    public String getPaket_checkUp() {
        return paket_checkUp;
    }

    public void setPaket_checkUp(String paket_checkUp) {
        this.paket_checkUp = paket_checkUp;
    }

    public String getJam_checkUp() {
        return jam_checkUp;
    }

    public void setJam_checkUp(String jam_checkUp) {
        this.jam_checkUp = jam_checkUp;
    }

    public String getTgl_checkUp() {
        return tgl_checkUp;
    }

    public void setTgl_checkUp(String tgl_checkUp) {
        this.tgl_checkUp = tgl_checkUp;
    }

    public double getHarga_checkUp() {
        return harga_checkUp;
    }

    public void setHarga_checkUp(double harga_checkUp) {
        this.harga_checkUp = harga_checkUp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
