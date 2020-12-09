package com.tgsbesar.myapplication.model;

public class transaksiRawatJalan {

    private String nama_dokter, spesialis, tgl_rjalan, jam_rjalan;

    public transaksiRawatJalan(String nama_dokter, String spesialis, String tgl_rjalan, String jam_rjalan) {
        this.nama_dokter = nama_dokter;
        this.spesialis=spesialis;
        this.tgl_rjalan=tgl_rjalan;
        this.jam_rjalan=jam_rjalan;


    }

    public String getNama_dokter() {
        return nama_dokter;
    }

    public void setNama_dokter(String nama_dokter) {
        this.nama_dokter = nama_dokter;
    }

    public String getSpesialis() {
        return spesialis;
    }

    public void setSpesialis(String spesialis) {
        this.spesialis = spesialis;
    }

    public String getTgl_rjalan() {
        return tgl_rjalan;
    }

    public void setTgl_rjalan(String tgl_rjalan) {
        this.tgl_rjalan = tgl_rjalan;
    }

    public String getJam_rjalan() {
        return jam_rjalan;
    }

    public void setJam_rjalan(String jam_rjalan) {
        this.jam_rjalan = jam_rjalan;
    }
}
