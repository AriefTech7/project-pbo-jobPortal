package model.DAO;

import model.entity.perusahaan;
import model.entity.lamaran;
import config.connector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.entity.lowongan;
import javax.swing.JOptionPane;

public class perusahaanDAO {

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

    public List<lowongan> getLowonganByPerusahaan(int idPerusahaan) {
        String sql = "SELECT * FROM lowongan WHERE id_perusahaan = ? ORDER BY id_lowongan";
        List<lowongan> list = new ArrayList<>();
        try (Connection conn = connector.configDB(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPerusahaan);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extractLowonganFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return list;
    }

    public List<lamaran> getPelamar() {
        String sql = "SELECT la.*,lo.posisi FROM lamaran la,lowongan lo WHERE la.id_lowongan = lo.id_lowongan";
        List<lamaran> list = new ArrayList<>();
        try (Connection conn = connector.configDB(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extractLamaranFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return list;
    }

    private lamaran extractLamaranFromResultSet(ResultSet rs) throws SQLException {
        lamaran l = new lamaran();
        l.setId_lamaran(rs.getInt("id_lamaran"));
        l.setId_lowongan(rs.getInt("id_lowongan"));
        l.setId_karyawan(rs.getInt("id_karyawan"));
        l.setNama(rs.getString("nama"));
        l.setEmail(rs.getString("email"));
        l.setNo_hp(rs.getString("no_hp"));
        l.setCv(rs.getString("cv"));
        l.setTanggal_lamar(rs.getDate("tanggal_lamar"));
        l.setPosisi(rs.getString("posisi"));
        l.setStatus(rs.getString("status"));
        return l;
    }

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

    private lowongan extractLowonganFromResultSet(ResultSet rs) throws SQLException {
        lowongan l = new lowongan();
        l.setId_lowongan(rs.getInt("id_lowongan"));
        l.setId_perusahaan(rs.getInt("id_perusahaan"));
        l.setPosisi(rs.getString("posisi"));
        l.setGaji(rs.getFloat("gaji"));
        l.setJenis_kontrak(rs.getString("jenis_kontrak"));
        l.setJobdesk(rs.getString("jobdesk"));
        return l;
    }

    public boolean updateStatusLamaran(int idLamaran, String status) {
        String sql = "UPDATE lamaran SET status = ? WHERE id_lamaran = ?";

        try (Connection conn = connector.configDB(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status.toLowerCase());
            pstmt.setInt(2, idLamaran);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            return false;
        }
    }
    public boolean regisPerusahaan(perusahaan perusahaan) {
        String sql = "INSERT INTO perusahaan (id_user, nama, alamat, nomor_siup, status) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = connector.configDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, perusahaan.getId_user());
            ps.setString(2, perusahaan.getNama());
            ps.setString(3, perusahaan.getAlamat());
            ps.setString(4, perusahaan.getNomor_siup());
            ps.setString(5, perusahaan.getStatus());
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateStatus(int idUser, String status) {
        String sql = "UPDATE perusahaan SET status = ? WHERE id_user = ?";
        
        try (Connection conn = connector.configDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            ps.setInt(2, idUser);
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
