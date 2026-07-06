package config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class connector {
    private static Connection MySQLConfig;
    
    public static Connection configDB() throws SQLException {
        try {
            String url = "jdbc:mysql://localhost:3306/pa_pbo"; 
            String user = "root"; 
            String pass = ""; 

            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            MySQLConfig = DriverManager.getConnection(url, user, pass);

        } catch (SQLException e) {
            System.err.println("koneksi gagal " + e.getMessage()); 
        }
        return MySQLConfig;
    }
}