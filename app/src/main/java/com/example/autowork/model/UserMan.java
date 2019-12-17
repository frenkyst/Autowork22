package com.example.autowork.model;

public class UserMan {

    private String Nama;
    private String email;
    private String key;
    private String uid;


    public UserMan(String nama, String email, String key, String uid) {
        this.Nama = nama;
        this.email = email;
        this.key = key;
        this.uid = uid;
    }

    public UserMan(){ }

    public UserMan(String nama, String email, String uid) {
        this.Nama = nama;
        this.email = email;
        this.uid = uid;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        return  " "+Nama+"\n" +
                " "+email+"\n" +
                " "+uid;

    }


}
