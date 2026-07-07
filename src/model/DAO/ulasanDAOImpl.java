/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.DAO;

import config.connector;
import java.util.ArrayList;
import java.util.List;
import model.entity.ulasan;
import java.sql.*;
import javax.swing.JOptionPane;
import model.entity.perusahaan;

public class ulasanDAOImpl implements ulasanDAO {

    @Override
    public List<ulasan> getUlasan() {
        String sql = "SELECT * FROM ulasan order by id_ulasan";
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

    @Override
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
        ulasan ulasan = new ulasan();
        ulasan.setId_ulasan(rs.getInt("id_ulasan"));
        ulasan.setSkor_bintang(rs.getInt("skor_bintang"));
        ulasan.setIsi_ulasan(rs.getString("isi_ulasan"));
        ulasan.setTanggal_ulasan(rs.getDate("tanggal_ulasan"));
        return ulasan;
    }

    @Override
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

    @Override
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

    @Override
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

}
