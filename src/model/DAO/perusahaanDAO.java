package model.DAO;

import model.entity.perusahaan;
import java.util.List;

public interface perusahaanDAO {

    List<perusahaan> getPerusahaan();

    boolean updateStatus(String status, int id_perusahaan);

    List<perusahaan> search(String keyword);

    boolean tambah(perusahaan p);

    boolean edit(perusahaan p);

    boolean hapus(int id_perusahaan);
    List<perusahaan> getApproved();
}
