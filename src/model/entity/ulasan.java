/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entity;
import java.sql.Date;
public class ulasan {
    private int id_ulasan,skor_bintang,id_perusahaan,id_karyawan;
    private String isi_ulasan,nama_perusahaan;
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

    public int getId_perusahaan() {
        return id_perusahaan;
    }

    public void setId_perusahaan(int id_perusahaan) {
        this.id_perusahaan = id_perusahaan;
    }

    public int getId_karyawan() {
        return id_karyawan;
    }

    public void setId_karyawan(int id_karyawan) {
        this.id_karyawan = id_karyawan;
    }

    public String getNama_perusahaan() {
        return nama_perusahaan;
    }

    public void setNama_perusahaan(String nama_perusahaan) {
        this.nama_perusahaan = nama_perusahaan;
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
