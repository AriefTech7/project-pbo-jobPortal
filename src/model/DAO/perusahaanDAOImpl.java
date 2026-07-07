package model.DAO;

import model.entity.perusahaan;
import config.connector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.entity.lowongan;
import javax.swing.JOptionPane;

public class perusahaanDAOImpl implements perusahaanDAO {
        
    //page Admin

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
        String sql = "SELECT * FROM perusahaan WHERE nama LIKE ? OR nomor_siup LIKE ? ORDER BY id_perusahaan";
        List<perusahaan> list = new ArrayList<>();

        try (Connection conn = connector.configDB(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

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

    @Override
    public boolean tambah(perusahaan p) {
        String sql = "INSERT INTO perusahaan (id_user, nama, alamat, nomor_siup, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connector.configDB(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, p.getId_user());
            pstmt.setString(2, p.getNama());
            pstmt.setString(3, p.getAlamat());
            pstmt.setString(4, p.getNomor_siup());
            pstmt.setString(5, p.getStatus());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean edit(perusahaan p) {
        String sql = "UPDATE perusahaan SET nama=?, alamat=?, nomor_siup=? WHERE id_perusahaan=?";
        try (Connection conn = connector.configDB(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, p.getNama());
            pstmt.setString(2, p.getAlamat());
            pstmt.setString(3, p.getNomor_siup());
            pstmt.setInt(4, p.getId_perusahaan());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean hapus(int id_perusahaan) {
        String sql = "DELETE FROM perusahaan WHERE id_perusahaan=?";
        try (Connection conn = connector.configDB(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id_perusahaan);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<perusahaan> getApproved() {
        String sql = "SELECT * FROM perusahaan WHERE status='approved' ORDER BY nama";
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
    
    //page Perusahaan
    @Override
    public int getIdPerusahaanByUser(int idUser) {
        String sql = "SELECT id_perusahaan FROM perusahaan WHERE id_user = ?";
        try (Connection conn = connector.configDB(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUser);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_perusahaan");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return -1; 
    }

    @Override
    public List<lowongan> getLowonganByPerusahaan(int idPerusahaan) {
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
    public boolean tambahLowongan(lowongan l) {
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
    public boolean ubahLowongan(lowongan l) {
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
    public boolean hapusLowongan(int idLowongan) {
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
        lowongan l = new lowongan();
        l.setId_lowongan(rs.getInt("id_lowongan"));
        l.setId_perusahaan(rs.getInt("id_perusahaan"));
        l.setPosisi(rs.getString("posisi"));
        l.setGaji(rs.getFloat("gaji"));
        l.setJenis_kontrak(rs.getString("jenis_kontrak"));
        l.setJobdesk(rs.getString("jobdesk"));
        return l;
    }

}
