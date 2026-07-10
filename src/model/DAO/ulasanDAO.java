
package model.DAO;

import config.connector;
import java.util.ArrayList;
import java.util.List;
import model.entity.ulasan;
import java.sql.*;
import javax.swing.JOptionPane;
import model.entity.perusahaan;

public class ulasanDAO {

    public List<ulasan> getUlasan() {
        String sql = "SELECT u.*, p.nama as nama_perusahaan, la.nama as nama_karyawan FROM ulasan u JOIN  perusahaan p "
                + "ON u.id_perusahaan=p.id_perusahaan JOIN lowongan lo ON "
                + "lo.id_perusahaan=p.id_perusahaan JOIN lamaran la ON la.id_lowongan = lo.id_lowongan "
                + "order by u.id_ulasan";
        List<ulasan> list = new ArrayList<>();

        try (Connection conn = connector.configDB(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(extractUlasanFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean delUlasan(int idUlasan) {
        String sql = "DELETE FROM ulasan WHERE id_ulasan = ?";

        try (Connection conn = connector.configDB(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUlasan);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private ulasan extractUlasanFromResultSet(ResultSet rs) throws SQLException {
        ulasan u = new ulasan();
        u.setId_ulasan(rs.getInt("id_ulasan"));
        u.setId_perusahaan(rs.getInt("id_perusahaan"));
        u.setId_karyawan(rs.getInt("id_karyawan"));
        u.setTanggal_ulasan(rs.getDate("tanggal_ulasan"));
        u.setSkor_bintang(rs.getInt("skor_bintang"));
        u.setIsi_ulasan(rs.getString("isi_ulasan"));
        u.setNama_perusahaan(rs.getString("nama_perusahaan"));
        u.setNama_karyawan(rs.getString("nama_karyawan"));
        return u;
    }

    
    public boolean tambahUlasan(int idPerusahaan, int idKaryawan, int skorBintang, String isiUlasan) {
        String sql = "INSERT INTO ulasan (id_perusahaan, id_karyawan, tanggal_ulasan, skor_bintang, isi_ulasan) "
                + "VALUES (?, ?, CURDATE(), ?, ?)";
        try (Connection conn = connector.configDB(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPerusahaan);
            pstmt.setInt(2, idKaryawan);
            pstmt.setInt(3, skorBintang);
            pstmt.setString(4, isiUlasan);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    public List<ulasan> getUlasanByPerusahaan(Integer idPerusahaan) {
        String sql = "SELECT * FROM ulasan WHERE (? IS NULL OR id_perusahaan = ?) ORDER BY tanggal_ulasan DESC";
        List<ulasan> list = new ArrayList<>();
        try (Connection conn = connector.configDB(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (idPerusahaan == null) {
                pstmt.setNull(1, Types.INTEGER);
                pstmt.setNull(2, Types.INTEGER);
            } else {
                pstmt.setInt(1, idPerusahaan);
                pstmt.setInt(2, idPerusahaan);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ulasan u = new ulasan();
                    u.setId_ulasan(rs.getInt("id_ulasan"));
                    u.setId_perusahaan(rs.getInt("id_perusahaan"));
                    u.setId_karyawan(rs.getInt("id_karyawan"));
                    u.setTanggal_ulasan(rs.getDate("tanggal_ulasan"));
                    u.setSkor_bintang(rs.getInt("skor_bintang"));
                    u.setIsi_ulasan(rs.getString("isi_ulasan"));
                    list.add(u);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    
    public List<perusahaan> getPerusahaanApproved() {
        String sql = "SELECT * FROM perusahaan WHERE status='approved' ORDER BY nama";
        List<perusahaan> list = new ArrayList<>();
        try (Connection conn = connector.configDB(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                perusahaan p = new perusahaan();
                p.setId_perusahaan(rs.getInt("id_perusahaan"));
                p.setId_user(rs.getInt("id_user"));
                p.setNama(rs.getString("nama"));
                p.setAlamat(rs.getString("alamat"));
                p.setNomor_siup(rs.getString("nomor_siup"));
                p.setStatus(rs.getString("status"));
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return list;
    }

    
    public List<ulasan> getUlasanByPerusahaanJoin(int idPerusahaan) {
        String sql = "SELECT u.*, p.nama as nama_perusahaan, k.username as nama_karyawan "
                + "FROM ulasan u "
                + "JOIN perusahaan p ON u.id_perusahaan = p.id_perusahaan "
                + "JOIN users k ON u.id_karyawan = k.id_user "
                + "WHERE u.id_perusahaan = ? "
                + "ORDER BY u.tanggal_ulasan DESC";

        List<ulasan> list = new ArrayList<>();

        try (Connection conn = connector.configDB(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPerusahaan);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ulasan u = extractUlasanFromResultSet(rs);
                    list.add(u);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return list;
    }

    
    public double getAverageRating(int idPerusahaan) {
        
        String sql = "SELECT AVG(skor_bintang) as avg_rating FROM ulasan WHERE id_perusahaan = ?";

        try (Connection conn = connector.configDB(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPerusahaan);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("avg_rating");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    
    public boolean addUlasan(ulasan u) {
        String sql = "INSERT INTO ulasan (id_perusahaan, id_karyawan, tanggal_ulasan, skor_bintang, isi_ulasan) "
                + "VALUES (?, ?, CURDATE(), ?, ?)";

        try (Connection conn = connector.configDB(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, u.getId_perusahaan());
            pstmt.setInt(2, u.getId_karyawan());
            pstmt.setInt(3, u.getSkor_bintang());
            pstmt.setString(4, u.getIsi_ulasan());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            return false;
        }
    }

}
