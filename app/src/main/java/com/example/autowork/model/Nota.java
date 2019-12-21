package com.example.autowork.model;

public class Nota {
    private String nama;
    private String jumlah;
    private String total;
    private String key;


    public Nota(String nama, String jumlah, String total, String key) {
        this.nama = nama;
        this.jumlah = jumlah;
        this.total = total;
        this.key = key;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return  " "+nama+"\n" +
                " "+jumlah+"\n" +
                " "+total;
    }
}
