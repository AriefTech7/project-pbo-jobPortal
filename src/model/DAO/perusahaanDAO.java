package model.DAO;

import model.entity.perusahaan;
import model.entity.lowongan;
import java.util.List;
import model.entity.lamaran;

public interface perusahaanDAO {
    //page Admin

    List<perusahaan> getPerusahaan();

    boolean updateStatus(String status, int id_perusahaan);

    List<perusahaan> search(String keyword);

    boolean tambah(perusahaan p);

    boolean edit(perusahaan p);

    boolean hapus(int id_perusahaan);

    List<perusahaan> getApproved();

    //page Perusahaan
    
    int getIdPerusahaanByUser(int idUser);
    List<lowongan> getLowonganByPerusahaan(int idPerusahaan);
    boolean tambahLowongan(lowongan l);
    boolean ubahLowongan(lowongan l);
    boolean hapusLowongan(int idLowongan);
    List<lamaran> getPelamar();
    boolean updateStatusLamaran(int idLamaran, String status);
}
