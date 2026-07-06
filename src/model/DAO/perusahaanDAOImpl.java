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
        perusahaan.setId_perusahaan(rs.getInt("id_perusahaan"));
        perusahaan.setId_user(rs.getInt("id_user"));
        perusahaan.setNama(rs.getString("nama"));
        perusahaan.setAlamat(rs.getString("alamat"));
        perusahaan.setNomor_siup(rs.getString("nomor_siup"));
        perusahaan.setStatus(rs.getString("status"));
        return perusahaan;
    }

    @Override
    public boolean updateStatus(String status, int id_perusahaan) {
        String sql = "UPDATE perusahaan SET status = ? WHERE id_perusahaan = ?";
        try (Connection conn = connector.configDB(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, id_perusahaan);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<perusahaan> search(String keyword) {
        // Mencari berdasarkan nama ATAU nomor_siup
        String sql = "SELECT * FROM perusahaan WHERE nama LIKE ? OR nomor_siup LIKE ? ORDER BY id_perusahaan";
        List<perusahaan> list = new ArrayList<>();

        try (Connection conn = connector.configDB(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Tambahkan tanda % di depan dan belakang keyword untuk pencarian parsial
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(extractPerusahaanFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
