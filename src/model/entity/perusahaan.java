package model.entity;

public class perusahaan {

    private int id_perusahaan, id_user;
    private String nama, alamat, nomor_siup, status;
    
    public perusahaan() {}

    public perusahaan(int idPerusahaan, int idUser, String nama, String alamat, String nomorSiup, String status) {
        this.id_perusahaan = idPerusahaan;
        this.id_user = idUser;
        this.nama = nama;
        this.alamat = alamat;
        this.nomor_siup = nomorSiup;
        this.status = status;
    }
    
    public int getId_perusahaan() {
        return id_perusahaan;
    }

    public void setId_perusahaan(int id_perusahaan) {
        this.id_perusahaan = id_perusahaan;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNomor_siup() {
        return nomor_siup;
    }

    public void setNomor_siup(String nomor_siup) {
        this.nomor_siup = nomor_siup;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
