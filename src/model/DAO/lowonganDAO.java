package model.DAO;

import model.entity.lowongan;
import java.util.List;

public interface lowonganDAO {

    List<lowongan> getAll();                          // dulu: karyawanDAO.getLowongan()

    List<lowongan> search(String keyword);             // dulu: karyawanDAO.search()

    List<lowongan> filter(String keyword, Integer minRating); // dulu: karyawanDAO.filter()

    List<lowongan> getByPerusahaan(int idPerusahaan);   // dulu: perusahaanDAO.getLowonganByPerusahaan()

    boolean tambah(lowongan l);                         // dulu: perusahaanDAO.tambahLowongan()

    boolean ubah(lowongan l);                           // dulu: perusahaanDAO.ubahLowongan()

    boolean hapus(int idLowongan);
}
