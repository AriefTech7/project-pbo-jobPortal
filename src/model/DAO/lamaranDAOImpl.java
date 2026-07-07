/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.DAO;

import config.connector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.entity.lamaran;

public class lamaranDAOImpl implements lamaranDAO {

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

}
