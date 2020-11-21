package com.example.dankeapps.content;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ModelContent {

    String id;
    String Judul;
    String Upah;
    String Deskripsi;
    String uri;

    public ModelContent() {
    }

    public ModelContent(String id, String judul, String upah, String deskripsi, String uri) {
        this.id = id;
        this.Judul = judul;
        this.Upah = upah;
        this.Deskripsi = deskripsi;
        this.uri = uri;
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

    public String getUpah() {
        return Upah;
    }

    public void setUpah(String upah) {
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
}
