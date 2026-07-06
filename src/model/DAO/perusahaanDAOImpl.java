package model.DAO;

import model.entity.perusahaan;
import config.connector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class perusahaanDAOImpl implements perusahaanDAO {
    @Override
    public List<perusahaan> getPerusahaan() {
        String sql = "SELECT * FROM perusahaan order by id_perusahaan";
        List<perusahaan> list = new ArrayList<>();

        try (Connection conn = connector.configDB(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(extractPerusahaanFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    private perusahaan extractPerusahaanFromResultSet(ResultSet rs) throws SQLException {
        perusahaan perusahaan = new perusahaan();
        perusahaan.setNama(rs.getString("nama"));
        perusahaan.setAlamat(rs.getString("alamat"));
        perusahaan.setNomor_siup(rs.getString("nomor_siup"));
        perusahaan.setStatus(rs.getString("status"));
        return perusahaan;
    }
    

    
}
