package model.DAO;

import model.entity.user;
import config.connector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class loginDAOImpl implements loginDAO {
//    @Override
////    public user getById(int id) {
////        String sql = "SELECT * FROM users WHERE id = ?";
////        user pengguna = null;
////        
////        try (Connection conn = connector.configDB();
////             PreparedStatement ps = conn.prepareStatement(sql)) {
////            
////            ps.setInt(1, id);
////            ResultSet rs = ps.executeQuery();
////            
////            if (rs.next()) {
////                pengguna = extractUserFromResultSet(rs);
////            }
////            
////        } catch (SQLException e) {
////            e.printStackTrace();
////        }
////        
////        return pengguna;
////    }
    @Override
    public List<user> getAll() {
        String sql = "SELECT * FROM users ORDER BY id";
        List<user> users = new ArrayList<>();
        
        try (Connection conn = connector.configDB();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                user user = extractUserFromResultSet(rs);
                users.add(user);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return users;
    }
    @Override
    public user login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        user user = null;
        
        try (Connection conn = connector.configDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
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
    private user extractUserFromResultSet(ResultSet rs) throws SQLException {
        user user = new user();
        user.setId(rs.getInt("id_user"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setRole(rs.getString("role"));
        return user;
    }
}
