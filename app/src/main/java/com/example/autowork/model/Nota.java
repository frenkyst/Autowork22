package com.example.autowork.model;

public class Nota {
    private String nama;
    private Integer jumlah;
    private Integer total;
    private String key;


    public Nota(String nama, Integer jumlah, Integer total, String key) {
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

    public Integer getJumlah() {
        return jumlah;
    }

    public void setJumlah(Integer jumlah) {
        this.jumlah = jumlah;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
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
