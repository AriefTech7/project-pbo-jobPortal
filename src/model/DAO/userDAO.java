/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.DAO;

import model.entity.user;

public interface userDAO {

    user regis(String username, String password, String role, String email);

    user login(String username, String password);

    int selectID(String username, String password);
    String getEmailById(int idUser);

}
