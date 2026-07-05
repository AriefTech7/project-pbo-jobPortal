package controller;

import model.entity.user;
import model.DAO.userDAO;
import model.DAO.userDAOImpl;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import view.pageLogin;
import view.pageAdmin1;
import view.pageKaryawan1;
import view.pagePerusahaan1;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class loginController implements ActionListener, MouseListener {

    private userDAO penggunaDAO;
    private pageLogin pl;

    public loginController(pageLogin pl) {
        this.penggunaDAO = new userDAOImpl();
        this.pl = pl;
        this.pl.btnLogin.addActionListener(this);
    }

    public user processLogin(String usr, String pwd) {
        if (usr.isEmpty() || pwd.isEmpty()) {
            return null;
        }
        return penggunaDAO.login(usr.trim(), pwd.trim());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == pl.btnLogin) {

            String usr = pl.textUsername.getText();
            String pwd = pl.textPassword.getText();

            user pengguna = processLogin(usr, pwd);
            if (pengguna != null) {
                JOptionPane.showMessageDialog(pl, "Selamat datang, " + pengguna.getUsername());

                if ("admin".equals(pengguna.getRole())) {
                    pageAdmin1 dashboardAdmin = new pageAdmin1();
                    dashboardAdmin.setVisible(true);
                    
                } else if("perusahaan".equals(pengguna.getRole())){
                    pagePerusahaan1 dashboardPerusahaan = new pagePerusahaan1();
                    dashboardPerusahaan.setVisible(true);
                }else if("karyawan".equals(pengguna.getRole())){
                    pageKaryawan1 dashboardKarywan = new pageKaryawan1();
                    dashboardKarywan.setVisible(true);
                }

                pl.dispose();

            } else {
                JOptionPane.showMessageDialog(pl, "Username atau Password salah/kosong!", "Login Gagal", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Implementasi MouseListener (kosongkan jika tidak dipakai)
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
