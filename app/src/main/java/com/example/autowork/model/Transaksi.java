package com.example.autowork.model;

public class Transaksi {

    private String namakaryawan;
    private String total;
    private String uid;
    private String key;

    public Transaksi(){ }


    public Transaksi(String namakaryawan, String total, String uid, String key) {
        this.namakaryawan = namakaryawan;
        this.total = total;
        this.uid = uid;
        this.key = key;
    }


    public String getNamakaryawan() {
        return namakaryawan;
    }

    public void setNamakaryawan(String namakaryawan) {
        this.namakaryawan = namakaryawan;
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
        return  " "+namakaryawan+"\n" +
                " "+total+"\n" +
                " "+uid;

    }
}
