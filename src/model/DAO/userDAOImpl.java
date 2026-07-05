package model.DAO;

import model.entity.user;
import config.connector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class userDAOImpl implements userDAO {
    @Override
    public boolean insert(user pengguna){
         String sql = "INSERT INTO users (username,password,email,role) values (?,?,?,?)";
         try (Connection conn=connector.configDB();
                 PreparedStatement ps = conn.prepareStatement(sql)){
             ps.setString(1, pengguna.getUsername());
             ps.setString(2, pengguna.getPassword());
             ps.setString(3, pengguna.getEmail());
             ps.setString(4, pengguna.getRole());
             
             int rowsAffected = ps.executeUpdate();
             return rowsAffected>0;
             
         } catch (SQLException ex) {
             ex.printStackTrace();
             return false;
         }
    }
    @Override
    public user getById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        user pengguna = null;
        
        try (Connection conn = connector.configDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                pengguna = extractUserFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return pengguna;
    }
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
    public boolean update(user user) {
        String sql = "UPDATE users SET username=?, password=?, email=?, role=? WHERE id=?";
        
        try (Connection conn = connector.configDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getRole());
            ps.setInt(5, user.getId());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
     
     @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        
        try (Connection conn = connector.configDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setRole(rs.getString("role"));
        return user;
    }
}
