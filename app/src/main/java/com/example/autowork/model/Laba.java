package com.example.autowork.model;

public class Laba {

    private String namaKaryawan;
    private String namaKasir;
    private Integer totalTransaksi;
    private Integer totalLaba;
    private String tanggalTransaksi;
    private String key;

    public Laba() {
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

    public Integer getTotalLaba() {
        return totalLaba;
    }

    public void setTotalLaba(Integer totalLaba) {
        this.totalLaba = totalLaba;
    }

    public String getTanggalTransaksi() {
        return tanggalTransaksi;
    }

    public void setTanggalTransaksi(String tanggalTransaksi) {
        this.tanggalTransaksi = tanggalTransaksi;
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
                " "+totalTransaksi+"\n" +
                " "+totalLaba+"\n" +
                " "+tanggalTransaksi;
    }
}