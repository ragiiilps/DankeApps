package com.example.dankeapps;

public class DataHelperClass {

    String name, alamat, usia, phone,pendidikan, tawar, keahlian, pengalaman,gender;



    public DataHelperClass(String name, String alamat, String usia, String phone,String pendidikan, String tawar, String keahlian, String pengalaman, String gender) {
        this.name = name;
        this.alamat = alamat;
        this.usia = usia;
        this.phone = phone;
        this.pendidikan = pendidikan;
        this.tawar = tawar;
        this.keahlian = keahlian;
        this.pengalaman = pengalaman;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getUsia() {
        return usia;
    }

    public void setUsia(String usia) {
        this.usia = usia;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTawar() {
        return tawar;
    }

    public void setTawar(String tawar) {
        this.tawar = tawar;
    }
    public String getPendidikan() {
        return pendidikan;
    }

    public void setPendidikan(String pendidikan) {
        this.pendidikan = pendidikan;
    }

    public String getKeahlian() {
        return keahlian;
    }

    public void setKeahlian(String keahlian) {
        this.keahlian = keahlian;
    }

    public String getPengalaman() {
        return pengalaman;
    }

    public void setPengalaman(String pengalaman) {
        this.pengalaman = pengalaman;
    }

    public String getGender (){
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
