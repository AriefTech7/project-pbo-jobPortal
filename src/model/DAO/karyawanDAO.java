package model.DAO;

import model.entity.lamaran;
import model.entity.lowongan;
import model.entity.ulasan;
import model.entity.perusahaan;
import java.util.List;

public interface karyawanDAO {

    List<lowongan> getLowongan();

    List<lowongan> search(String keyword);

    List<lowongan> filter(String keyword, Integer minRating);

    List<ulasan> getUlasan();

    List<lamaran> getRiwayatByKaryawan(int idKaryawan);

    boolean ajukanLamaran(int idLowongan, int idKaryawan, String nama, String email, String noHp, String cvPath);

    String getEmailById(int idUser);

    List<perusahaan> getPerusahaanApproved();

    boolean tambahUlasan(int idPerusahaan, int idKaryawan, int skorBintang, String isiUlasan);

    List<ulasan> getUlasanByPerusahaan(Integer idPerusahaan);
}
