package com.example.autowork.model;

public class TransaksiKaryawan {
    private String barkod;
    private String nama;
    private Integer jml;
    private Integer total;
    private Integer laba;
    private String key;


    public TransaksiKaryawan(String nama, Integer jml, Integer laba) {
        this.nama = nama;
        this.jml = jml;
        this.laba = laba;
    }


    public TransaksiKaryawan(String barkod, String nama, Integer jml, Integer total, Integer laba) {
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

    public Integer getJml() {
        return jml;
    }

    public void setJml(Integer jml) {
        this.jml = jml;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getLaba() {
        return laba;
    }

    public void setLaba(Integer laba) {
        this.laba = laba;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
