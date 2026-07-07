
package model.entity;
import java.sql.Date;
public class lamaran {
    private int id_lamaran,id_lowongan,id_karyawan;
    private String nama,email,no_hp,cv,status;
    private Date tanggal_lamar;
    private String nama_perusahaan,posisi;

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

    public Date getTanggal_lamar() {
        return tanggal_lamar;
    }

    public void setTanggal_lamar(Date tanggal_lamar) {
        this.tanggal_lamar = tanggal_lamar;
    }

    public String getNama_perusahaan() {
        return nama_perusahaan;
    }

    public void setNama_perusahaan(String nama_perusahaan) {
        this.nama_perusahaan = nama_perusahaan;
    }

    public String getPosisi() {
        return posisi;
    }

    public void setPosisi(String posisi) {
        this.posisi = posisi;
    }
    
    
}
