package com.example.autowork.model;

public class Histori {

    private String namaKaryawan;
    private String namaKasir;
    private Integer totalTransaksi;
    private String key;

    public Histori(){ }


    public Histori(String namaKaryawan, String namaKasir, Integer totalTransaksi, String key) {
        this.namaKaryawan = namaKaryawan;
        this.namaKasir = namaKasir;
        this.totalTransaksi = totalTransaksi;
        this.key = key;
    }

    public String getNamaKaryawan() {
        return namaKaryawan;
    }

    public void setNamaKaryawan(String namaKaryawan) {
        this.namaKaryawan = namaKaryawan;
    }

    public String getNamaKasir() {
        return namaKasir;
    }

    public void setNamaKasir(String namaKasir) {
        this.namaKasir = namaKasir;
    }

    public Integer getTotalTransaksi() {
        return totalTransaksi;
    }

    public void setTotalTransaksi(Integer totalTransaksi) {
        this.totalTransaksi = totalTransaksi;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    @Override
    public String toString() {
        return  " "+namaKaryawan+"\n" +
                " "+namaKasir+"\n" +
                " "+totalTransaksi;

    }
}
