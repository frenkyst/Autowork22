package com.example.autowork.model;

public class LogHistory {

    private String barkod;
    private String nama;
    private String jml;
    private String detiltransaksi;
    private String logapa;


    public LogHistory(String barkod, String nama, String jml, String detiltransaksi, String logapa) {
        this.barkod = barkod;
        this.nama = nama;
        this.jml = jml;
        this.detiltransaksi = detiltransaksi;
        this.logapa = logapa;
    }



    public LogHistory(String barkod, String nama, String jml, String logapa) {
        this.barkod = barkod;
        this.nama = nama;
        this.jml = jml;
        this.logapa = logapa;

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

    public String getDetiltransaksi() {
        return detiltransaksi;
    }

    public void setDetiltransaksi(String detiltransaksi) {
        this.detiltransaksi = detiltransaksi;
    }

    public String getLogapa() {
        return logapa;
    }

    public void setLogapa(String logapa) {
        this.logapa = logapa;
    }

    @Override
    public String toString() {
        return " "+barkod+"\n" +
                " "+nama+"\n" +
                " "+jml+"\n" +
                " "+detiltransaksi+"\n" +
                " "+logapa;
    }
}
