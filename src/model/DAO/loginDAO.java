package model.DAO;
import model.entity.user;
import java.util.List;
public interface loginDAO {
//    user getById(int id);
    List<user>getAll();
    user login(String username, String password);
}
