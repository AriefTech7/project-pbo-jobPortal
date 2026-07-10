package model.DAO;

import config.connector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.entity.user;
import java.sql.Statement;

public class userDAO {

    
    public user regis(String username, String password, String role, String email){
        user newUser = null;
        String sql = "insert into users (username,password,role,email) values ( ?, ?, ?, ?)";
        try (Connection conn = connector.configDB();
             PreparedStatement ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
            
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

    
    public user login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        user user = null;

        try (Connection conn = connector.configDB(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = extractUserFromResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    
    public int selectID(String username, String password) {
        String sql = "SELECT id_user FROM users WHERE username = ? AND password = ?";
        int idUser = -1;

        try (Connection conn = connector.configDB(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                idUser = rs.getInt("id_user");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idUser;
    }

    private user extractUserFromResultSet(ResultSet rs) throws SQLException {
        user user = new user();
        user.setId(rs.getInt("id_user"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setRole(rs.getString("role"));
        return user;
    }
    
    public String getEmailById(int idUser) {
        String sql = "SELECT email FROM users WHERE id_user = ?";
        try (Connection conn = connector.configDB(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUser);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("email");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
