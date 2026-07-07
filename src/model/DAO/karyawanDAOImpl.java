package model.DAO;

import model.entity.lowongan;
import config.connector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.entity.lamaran;
import model.entity.ulasan;

public class karyawanDAOImpl implements karyawanDAO {

    @Override
    public List<lowongan> getLowongan() {
        String sql = "SELECT l.*, p.nama "
                + "FROM lowongan l "
                + "JOIN perusahaan p ON l.id_perusahaan = p.id_perusahaan "
                + "ORDER BY l.id_lowongan";
        List<lowongan> list = new ArrayList<>();

        try (Connection conn = connector.configDB(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(extractlowonganFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return list;
    }

    private lowongan extractlowonganFromResultSet(ResultSet rs) throws SQLException {
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

    private ulasan extractulasanFromResultSet(ResultSet rs) throws SQLException {
        ulasan ulasan = new ulasan();
        ulasan.setId_ulasan(rs.getInt("id_ulasan"));
        ulasan.setId_perusahaan(rs.getInt("id_perusahaan"));
        ulasan.setId_karyawan(rs.getInt("id_karyawan"));
        ulasan.setTanggal_ulasan(rs.getDate("tanggal_ulasan"));
        ulasan.setSkor_bintang(rs.getInt("skor_bintang"));
        ulasan.setIsi_ulasan(rs.getString("isi_ulasan"));
        return ulasan;
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
                list.add(extractlowonganFromResultSet(rs));
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
                    list.add(extractlowonganFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<ulasan> getUlasan() {
        String sql = "SELECT * FROM ulasan order by id_ulasan";
        List<ulasan> list = new ArrayList<>();

        try (Connection conn = connector.configDB(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(extractulasanFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<lamaran> getRiwayatByKaryawan(int idKaryawan) {
        String sql = "SELECT lm.*, l.posisi, p.nama AS nama_perusahaan "
                + "FROM lamaran lm "
                + "JOIN lowongan l ON lm.id_lowongan = l.id_lowongan "
                + "JOIN perusahaan p ON l.id_perusahaan = p.id_perusahaan "
                + "WHERE lm.id_karyawan = ? "
                + "ORDER BY lm.tanggal_lamar DESC";
        List<lamaran> list = new ArrayList<>();
        try (Connection conn = connector.configDB(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idKaryawan);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lamaran lm = new lamaran();
                    lm.setId_lamaran(rs.getInt("id_lamaran"));
                    lm.setId_lowongan(rs.getInt("id_lowongan"));
                    lm.setId_karyawan(rs.getInt("id_karyawan"));
                    lm.setNama(rs.getString("nama"));
                    lm.setEmail(rs.getString("email"));
                    lm.setNo_hp(rs.getString("no_hp"));
                    lm.setCv(rs.getString("cv"));
                    lm.setTangga_lamar(rs.getDate("tanggal_lamar"));
                    lm.setStatus(rs.getString("status"));
                    lm.setPosisi(rs.getString("posisi"));
                    lm.setNama_perusahaan(rs.getString("nama_perusahaan"));
                    list.add(lm);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean ajukanLamaran(int idLowongan, int idKaryawan, String nama, String email, String noHp, String cvPath) {
        String sql = "INSERT INTO lamaran (id_lowongan, id_karyawan, nama, email, no_hp, cv, tanggal_lamar, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, CURDATE(), 'diproses')";

        try (Connection conn = connector.configDB(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idLowongan);
            pstmt.setInt(2, idKaryawan);
            pstmt.setString(3, nama);
            pstmt.setString(4, email);
            pstmt.setString(5, noHp);
            pstmt.setString(6, cvPath);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            return false;
        }
    }
    @Override
    public String getEmailById(int idUser) {
        String sql = "SELECT email FROM users WHERE id_user = ?";
        try (Connection conn = connector.configDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
