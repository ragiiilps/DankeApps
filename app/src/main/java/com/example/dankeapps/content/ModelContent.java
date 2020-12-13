package com.example.dankeapps.content;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ModelContent {

    String id;
    String Judul;
    int Upah;
    String Deskripsi, daerah;
    String uri;
    String name, username, email, phone, Uid;

    public ModelContent(String name, String username, String email, String phone, String Uid) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.Uid = Uid;
    }

    public ModelContent() {
    }

    public ModelContent(String id, String judul, int upah, String deskripsi, String uri, String daerah) {
        this.id = id;
        this.Judul = judul;
        this.Upah = upah;
        this.Deskripsi = deskripsi;
        this.uri = uri;
        this.daerah = daerah;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJudul() {
        return Judul;
    }

    public void setJudul(String judul) {
        this.Judul = judul;
    }

    public int getUpah() {
        return Upah;
    }

    public void setUpah(int upah) {
        this.Upah = upah;
    }

    public String getDeskripsi() {
        return Deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.Deskripsi = deskripsi;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getDaerah() {
        return daerah;
    }

    public void setDaerah(String daerah) {
        this.daerah = daerah;
    }
}
