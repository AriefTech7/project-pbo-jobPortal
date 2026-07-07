
package model.DAO;
import model.entity.lamaran;
import model.entity.lowongan;
import model.entity.ulasan;
import java.util.List;

public interface karyawanDAO {
    List<lowongan> getLowongan();
    List<lowongan> search(String keyword);
    List<lowongan> filter(String keyword, Integer minRating); 
    List<ulasan> getUlasan();
}
