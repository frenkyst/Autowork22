package com.example.autowork.model;

public class TransaksiKaryawan {
    private String barkod;
    private String nama;
    private String jml;
    private String total;

    public TransaksiKaryawan(String nama, String jml, String laba) {
        this.nama = nama;
        this.jml = jml;
        this.laba = laba;
    }

    private String laba;

    public TransaksiKaryawan(String barkod, String nama, String jml, String total, String laba) {
        this.barkod = barkod;
        this.nama = nama;
        this.jml = jml;
        this.total = total;
        this.laba = laba;
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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getLaba() {
        return laba;
    }

    public void setLaba(String laba) {
        this.laba = laba;
    }

    @Override
    public String toString() {
        return " "+barkod+"\n" +
                " "+nama+"\n" +
                " "+jml+"\n" +
                " "+total+"\n"+
                " "+laba;
    }
}
