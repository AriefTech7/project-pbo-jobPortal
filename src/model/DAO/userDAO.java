package model.DAO;
import model.entity.user;
import java.util.List;
public interface userDAO {
    boolean insert(user users);
    boolean update(user users);
    boolean delete(int d);
    user getById(int id);
    List<user>getAll();
    user login(String username, String password);
}
