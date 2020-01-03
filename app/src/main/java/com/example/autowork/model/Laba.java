package com.example.autowork.model;

public class Laba {

    private String nama;
    private Integer jml;
    private Integer laba;
    private String key;

    public Laba() {
    }

    public Laba(String nama, Integer jml, Integer laba) {
        this.nama = nama;
        this.jml = jml;
        this.laba = laba;
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
        return  " "+nama+"\n" +
                " "+jml+"\n" +
                " "+laba;
    }
}