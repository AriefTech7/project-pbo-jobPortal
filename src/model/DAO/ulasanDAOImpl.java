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

}
