package com.tgsbesar.myapplication.menu_rawatJalan;

import java.io.Serializable;

public class Dokter implements Serializable {
    public String nama;
    public String spesialis;

    public Dokter(String nama, String spesialis) {
        this.nama = nama;
        this.spesialis = spesialis;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) { this.nama = nama; }

    public String getSpesialis() {
        return spesialis;
    }

    public void setSpesialis(String namaspes) { this.spesialis = spesialis; }
}
