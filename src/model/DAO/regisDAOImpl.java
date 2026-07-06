
package model.DAO;
import config.connector;
import model.entity.user;
import java.sql.*;

public class regisDAOImpl implements regisDAO {
    @Override
    public user regis(String username, String password, String role, String email){
        user newUser = null;
        String sql = "insert into users (username,password,role,email) values ( ?, ?, ?, ?)";
        try (Connection conn = connector.configDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);
            ps.setString(4, email);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int newId = generatedKeys.getInt(1);

                    newUser = new user();
                    newUser.setId(newId);
                    newUser.setUsername(username);
                    newUser.setPassword(password);
                    newUser.setRole(role);
                    newUser.setEmail(email);
                }
            }
        }
            
            
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return newUser;
    }
}
