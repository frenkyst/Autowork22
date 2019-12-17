package com.example.autowork.model;

public class Meminta {

    private String barkod;
    private String nama;
    private String jml;

    private String key;



    private String hargaawal;
    private String hargajual;

    private String total;

    private String jmlplus;




    public Meminta() {
    }

    // MODEL INPUT DATA LOG TRANSAKSI DATA (UPDATE DATA STOK, TRANSAKSI PENJUALAN)
    public Meminta(String barkod, String nama, String jml) {
        this.barkod = barkod;
        this.nama = nama;
        this.jml = jml;
    }

    // MODEL INPUT DATA KALKULASI TOTAL DARI TRANSAKSI PENJUALAN (jml = Total =>  |  total = total)
    public Meminta(String total) {

        this.total = total;
    }


    // MODEL INPUT DATA TRANSAKSI 1
    public Meminta(String barkod, String nama, String jml, String total) {
        this.barkod = barkod;
        this.nama = nama;
        this.jml = jml;
        this.total = total;
    }

    // MODEL INPUT DATA BARANG BARU YANG BELUM PERNAH ADA SEBELUMNYA DAN MENJADI ADA
    public Meminta(String barkod, String nama, String jml, String hargaawal, String hargajual) {
        this.barkod = barkod;
        this.nama = nama;
        this.jml = jml;
        //this.key = key;
        this.hargaawal = hargaawal;
        this.hargajual = hargajual;
        //this.total = total;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getBarkod() {
        return barkod;
    }

    public void setBarkod(String barkod) {
        this.barkod = barkod;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJml() {
        return jml;
    }

    public void setJml(String jml) {
        this.jml = jml;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getHargaawal() {
        return hargaawal;
    }

    public void setHargaawal(String hargaawal) {
        this.hargaawal = hargaawal;
    }

    public String getHargajual() {
        return hargajual;
    }

    public void setHargajual(String hargajual) {
        this.hargajual = hargajual;
    }

    @Override
    public String toString() {
        return " "+barkod+"\n" +
                " "+nama+"\n" +
                " "+jml+"\n" +
                " "+hargaawal+"\n" +
                " "+hargajual+"\n" +
                " "+total;
    }

    //*/

}
