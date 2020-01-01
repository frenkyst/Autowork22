package com.example.autowork.model;

public class LogHistory {

    private String barkod;
    private String nama;
    private Integer jml;
    private String notaPembayaran;
    private String logapa;
    private Integer totalTransaksi;


    public LogHistory(String notaPembayaran, String nama, Integer jml, Integer totalTransaksi, String logapa) {
        this.notaPembayaran = notaPembayaran;
        this.nama = nama;
        this.jml = jml;
        this.totalTransaksi = totalTransaksi;
        this.logapa = logapa;
    }



    public LogHistory(String barkod, String nama, Integer jml, String logapa) {
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

    public Integer getJml() {
        return jml;
    }

    public void setJml(Integer jml) {
        this.jml = jml;
    }

    public Integer getTotalTransaksi() {
        return totalTransaksi;
    }

    public void setTotalTransaksi(Integer detiltransaksi) {
        this.totalTransaksi = detiltransaksi;
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
                " "+totalTransaksi+"\n" +
                " "+logapa;
    }
}
