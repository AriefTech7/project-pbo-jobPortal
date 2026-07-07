/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.DAO;

import model.entity.lowongan;
import config.connector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

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
}
