/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entity;
import java.sql.Date;
public class ulasan {
    private int id_ulasan,skor_bintang;
    private String isi_ulasan;
    private Date tanggal_ulasan;

    public Date getTanggal_ulasan() {
        return tanggal_ulasan;
    }

    public void setTanggal_ulasan(Date tanggal_ulasan) {
        this.tanggal_ulasan = tanggal_ulasan;
    }

    public int getId_ulasan() {
        return id_ulasan;
    }

    public void setId_ulasan(int id_ulasan) {
        this.id_ulasan = id_ulasan;
    }

    public int getSkor_bintang() {
        return skor_bintang;
    }

    public void setSkor_bintang(int skor_bintang) {
        this.skor_bintang = skor_bintang;
    }

   

    public String getIsi_ulasan() {
        return isi_ulasan;
    }

    public void setIsi_ulasan(String isi_ulasan) {
        this.isi_ulasan = isi_ulasan;
    }
}
