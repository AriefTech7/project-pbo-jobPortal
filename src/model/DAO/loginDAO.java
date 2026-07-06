package model.DAO;
import model.entity.user;
public interface loginDAO {

    user login(String username, String password);
}
