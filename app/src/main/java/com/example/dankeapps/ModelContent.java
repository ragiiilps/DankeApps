package com.example.dankeapps;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ModelContent {

    String Judul;
    String Upah;
    String Deskripsi;

    public ModelContent() {
    }

    public ModelContent(String judul, String upah, String deskripsi) {
        this.Judul = judul;
        this.Upah = upah;
        this.Deskripsi = deskripsi;
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
}
