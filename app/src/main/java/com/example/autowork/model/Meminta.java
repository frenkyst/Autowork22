package com.example.autowork.model;

public class Meminta {

    private String barkod;
    private String nama;
    private Integer jml;

    private String key;



    private Integer hargaawal;
    private Integer hargajual;

    private Integer total;

    private Integer jmlplus;




    public Meminta() {
    }

    // MODEL INPUT DATA LOG TRANSAKSI DATA (UPDATE DATA STOK, TRANSAKSI PENJUALAN)
    public Meminta(String barkod, String nama, Integer jml) {
        this.barkod = barkod;
        this.nama = nama;
        this.jml = jml;
    }

    // MODEL INPUT DATA KALKULASI TOTAL DARI TRANSAKSI PENJUALAN (jml = Total =>  |  total = total)
    public Meminta(Integer total) {

        this.total = total;
    }


    // MODEL INPUT DATA TRANSAKSI 1
    public Meminta(String barkod, String nama, Integer jml, Integer total) {
        this.barkod = barkod;
        this.nama = nama;
        this.jml = jml;
        this.total = total;
    }

    // MODEL INPUT DATA BARANG BARU YANG BELUM PERNAH ADA SEBELUMNYA DAN MENJADI ADA
    public Meminta(String barkod, String nama, Integer jml, Integer hargaawal, Integer hargajual) {
        this.barkod = barkod;
        this.nama = nama;
        this.jml = jml;
        //this.key = key;
        this.hargaawal = hargaawal;
        this.hargajual = hargajual;
        //this.total = total;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
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

    public Integer getJml() {
        return jml;
    }

    public void setJml(Integer jml) {
        this.jml = jml;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getHargaawal() {
        return hargaawal;
    }

    public void setHargaawal(Integer hargaawal) {
        this.hargaawal = hargaawal;
    }

    public Integer getHargajual() {
        return hargajual;
    }

    public void setHargajual(Integer hargajual) {
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
