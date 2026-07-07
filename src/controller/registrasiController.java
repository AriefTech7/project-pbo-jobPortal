package controller;

import javax.swing.JOptionPane;
import model.DAO.userDAO;
import model.DAO.userDAOImpl;
import model.DAO.perusahaanDAO;
import model.DAO.perusahaanDAOImpl;
import model.entity.perusahaan;
import model.entity.user;
import view.pageLogin;
import view.pageRegister;

public class registrasiController {

    private userDAO userDAO;
    private perusahaanDAO perusahaanDAO;
    private pageRegister view;

    public registrasiController(pageRegister view) {
        this.view = view;
        this.userDAO = new userDAOImpl();
        this.perusahaanDAO = new perusahaanDAOImpl();
    }

    public void handleRegis(String username, String password, String role, String email) {
        if (username.isEmpty() || password.isEmpty() || role.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Ada data yang kosong!!");
            return;
        }
        user registedUser = userDAO.regis(username, password, role, email);

        view.dispose();
        new pageLogin().setVisible(true);
    }

    public void hidePassword() {
        if (view.checkpwd.isSelected()) {
            view.textPassword.setEchoChar((char) 0);
        } else {
            view.textPassword.setEchoChar('*');
        }
    }

//    private void handleRoleSelection() {
//        if (view.rbKaryawan.isSelected()) {
//            view.textNamaPerusahaan.setVisible(false);
//            view.labelPerusahaan.setVisible(false);
//            view.labelAlamat.setVisible(false);
//            view.textAlamat.setVisible(false);
//            view.labelSIUP.setVisible(false);
//            view.textSIUP.setVisible(false);
//
//        } else if (view.rbPerusahaan.isSelected()) {
//            view.textNamaPerusahaan.setVisible(true);
//            view.labelPerusahaan.setVisible(true);
//            view.labelAlamat.setVisible(true);
//            view.textAlamat.setVisible(true);
//            view.labelSIUP.setVisible(true);
//            view.textSIUP.setVisible(true);
//        } 
//    }
//
//    public void RoleSelection() {
//
//        view.rbKaryawan.addActionListener(e -> handleRoleSelection());
//
//        view.rbPerusahaan.addActionListener(e -> handleRoleSelection());
//    }
//    public void dataPerusahaan(){
//        try {
//            int idUser = Integer.parseInt(idUserStr.trim());
//            perusahaan p = new perusahaan();
//            p.setId_user(idUser);
//            p.setNama(nama.trim());
//            p.setAlamat(alamat.trim());
//            p.setNomor_siup(nomorSiup.trim());
//            p.setStatus("pending");
//
//            if (perusahaanDAO.tambah(p)) {
//                JOptionPane.showMessageDialog(view, "Data berhasil ditambahkan.");
//                loadAllDataPerusahaan();
//            } else {
//                JOptionPane.showMessageDialog(view, "Gagal menambahkan data.");
//            }
//        } catch (NumberFormatException e) {
//            JOptionPane.showMessageDialog(view, "ID User harus berupa angka.");
//        }
//    }
}
