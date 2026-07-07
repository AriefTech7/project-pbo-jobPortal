/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entity;
import java.sql.Date;
public class lamaran {
    private int id_lamaran,id_lowongan,id_karyawan;
    private String nama,email,no_hp,cv,status;
    private Date tangga_lamar;

    public int getId_lamaran() {
        return id_lamaran;
    }

    public void setId_lamaran(int id_lamaran) {
        this.id_lamaran = id_lamaran;
    }

    public int getId_lowongan() {
        return id_lowongan;
    }

    public void setId_lowongan(int id_lowongan) {
        this.id_lowongan = id_lowongan;
    }

    public int getId_karyawan() {
        return id_karyawan;
    }

    public void setId_karyawan(int id_karyawan) {
        this.id_karyawan = id_karyawan;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getTangga_lamar() {
        return tangga_lamar;
    }

    public void setTangga_lamar(Date tangga_lamar) {
        this.tangga_lamar = tangga_lamar;
    }
    
}
