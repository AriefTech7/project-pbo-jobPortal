package model.DAO;

import config.connector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.DAO.lowonganDAO;
import model.entity.lowongan;

public class lowonganDAOImpl implements lowonganDAO {

    @Override
    public List<lowongan> getAll() {
        String sql = "SELECT l.*, p.nama "
                + "FROM lowongan l "
                + "JOIN perusahaan p ON l.id_perusahaan = p.id_perusahaan "
                + "ORDER BY l.id_lowongan";
        List<lowongan> list = new ArrayList<>();

        try (Connection conn = connector.configDB(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(extractFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<lowongan> search(String keyword) {
        String sql = "SELECT l.*, p.nama  "
                + "FROM lowongan l "
                + "JOIN perusahaan p ON l.id_perusahaan = p.id_perusahaan "
                + "WHERE p.nama LIKE ? OR l.posisi LIKE ? "
                + "ORDER BY l.id_lowongan";

        List<lowongan> list = new ArrayList<>();

        try (Connection conn = connector.configDB(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<lowongan> filter(String keyword, Integer minRating) {
        StringBuilder sql = new StringBuilder(
                "SELECT l.*, p.nama, AVG(u.skor_bintang) as avg_rating "
                + "FROM lowongan l "
                + "JOIN perusahaan p ON l.id_perusahaan = p.id_perusahaan "
                + "LEFT JOIN ulasan u ON u.id_perusahaan = p.id_perusahaan "
                + "WHERE 1=1 "
        );

        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        if (hasKeyword) {
            sql.append("AND (p.nama LIKE ? OR l.posisi LIKE ?) ");
        }
        sql.append("GROUP BY l.id_lowongan ");
        if (minRating != null) {
            sql.append("HAVING AVG(u.skor_bintang) >= ? ");
        }
        sql.append("ORDER BY l.id_lowongan");
        List<lowongan> list = new ArrayList<>();
        try (Connection conn = connector.configDB(); PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            if (hasKeyword) {
                String pattern = "%" + keyword.trim() + "%";
                pstmt.setString(idx++, pattern);
                pstmt.setString(idx++, pattern);
            }
            if (minRating != null) {
                pstmt.setInt(idx, minRating);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extractFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<lowongan> getByPerusahaan(int idPerusahaan) {
        String sql = "SELECT * FROM lowongan WHERE id_perusahaan = ? ORDER BY id_lowongan";
        List<lowongan> list = new ArrayList<>();
        try (Connection conn = connector.configDB(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPerusahaan);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extractFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public boolean tambah(lowongan l) {
        String sql = "INSERT INTO lowongan (id_perusahaan, posisi, gaji, jenis_kontrak, jobdesk) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connector.configDB(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, l.getId_perusahaan());
            pstmt.setString(2, l.getPosisi());
            pstmt.setFloat(3, l.getGaji());
            pstmt.setString(4, l.getJenis_kontrak());
            pstmt.setString(5, l.getJobdesk());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean ubah(lowongan l) {
        String sql = "UPDATE lowongan SET posisi=?, gaji=?, jenis_kontrak=?, jobdesk=? "
                + "WHERE id_lowongan=? AND id_perusahaan=?";
        try (Connection conn = connector.configDB(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, l.getPosisi());
            pstmt.setFloat(2, l.getGaji());
            pstmt.setString(3, l.getJenis_kontrak());
            pstmt.setString(4, l.getJobdesk());
            pstmt.setInt(5, l.getId_lowongan());
            pstmt.setInt(6, l.getId_perusahaan()); // pengaman: pastikan hanya bisa ubah milik sendiri
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean hapus(int idLowongan) {
        String sql = "DELETE FROM lowongan WHERE id_lowongan=?";
        try (Connection conn = connector.configDB(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idLowongan);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            return false;
        }
    }

    private lowongan extractFromResultSet(ResultSet rs) throws SQLException {
        lowongan lowongan = new lowongan();
        lowongan.setId_lowongan(rs.getInt("id_lowongan"));
        lowongan.setId_perusahaan(rs.getInt("id_perusahaan"));
        lowongan.setNama_perusahaan(rs.getString("nama"));
        lowongan.setGaji(rs.getFloat("gaji"));
        lowongan.setJenis_kontrak(rs.getString("jenis_kontrak"));
        lowongan.setPosisi(rs.getString("posisi"));
        lowongan.setJobdesk(rs.getString("jobdesk"));
        return lowongan;
    }
}
