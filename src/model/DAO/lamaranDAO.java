/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.DAO;

import java.util.List;
import model.entity.lamaran;

/**
 *
 * @author guebanget
 */
public interface lamaranDAO {
    List<lamaran> getRiwayatByKaryawan(int idKaryawan);
    boolean ajukanLamaran(int idLowongan, int idKaryawan, String nama, String email, String noHp, String cvPath);
}
