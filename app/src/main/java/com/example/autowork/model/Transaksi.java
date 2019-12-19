package com.example.autowork.model;

public class Transaksi {

    private String namaKaryawan;
    private String total;
    private String uid;
    private String key;

    public Transaksi(){ }


    public Transaksi(String namaKaryawan, String total, String uid, String key) {
        this.namaKaryawan = namaKaryawan;
        this.total = total;
        this.uid = uid;
        this.key = key;
    }


    public String getNamakaryawan() {
        return namaKaryawan;
    }

    public void setNamakaryawan(String namakaryawan) {
        this.namaKaryawan = namaKaryawan;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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
                " "+total+"\n" +
                " "+uid;

    }
}
