/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.DAO;

import java.util.List;
import model.entity.ulasan;

public interface ulasanDAO {
    List<ulasan> getUlasan();
    boolean delUlasan(int idUlasan);
}
