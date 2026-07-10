package controller;

import javax.swing.JOptionPane;
import model.DAO.userDAO;
import model.DAO.perusahaanDAO;
import model.entity.user;
import view.pageLogin;
import view.pageRegister;
import model.entity.perusahaan;

public class registrasiController {

    private final userDAO userDAO;
    private final perusahaanDAO perusahaanDAO;
    private final pageRegister view;

    public registrasiController(pageRegister view) {
        this.view = view;
        this.userDAO = new userDAO();
        this.perusahaanDAO = new perusahaanDAO();
    }

    public void handleRegis(String username, String password, String role, String email) {
        if (username.isEmpty() || password.isEmpty() || role.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Ada data yang kosong!!");
            return;
        }
        if (role.equals("perusahaan")) {
            if (view.textNamaPerusahaan.getText().isEmpty()
                    || view.textAlamat.getText().isEmpty()
                    || view.textSIUP.getText().isEmpty()) {
                JOptionPane.showMessageDialog(view, "Data perusahaan harus diisi lengkap!");
                return;
            }
        }
        try {
            user registedUser = userDAO.regis(username, password, role, email);

            if (registedUser == null) {
                JOptionPane.showMessageDialog(view, "Registrasi gagal! Username mungkin sudah digunakan.");
                return;
            }

            if (role.equals("perusahaan")) {
                perusahaan perusahaan = new perusahaan();
                perusahaan.setId_user(registedUser.getId()); 
                perusahaan.setNama(view.textNamaPerusahaan.getText());
                perusahaan.setAlamat(view.textAlamat.getText());
                perusahaan.setNomor_siup(view.textSIUP.getText());
                perusahaan.setStatus("pending"); 

                boolean companySaved = perusahaanDAO.regisPerusahaan(perusahaan);

                if (!companySaved) {
                    JOptionPane.showMessageDialog(view, "Gagal menyimpan data perusahaan!");
                    return;
                }
            }
            JOptionPane.showMessageDialog(view, "Registrasi berhasil!");
            view.dispose();
            new pageLogin().setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Terjadi kesalahan: " + e.getMessage());
        }
    }
    
    

    public void hidePassword() {
        if (view.checkpwd.isSelected()) {
            view.textPassword.setEchoChar((char) 0);
        } else {
            view.textPassword.setEchoChar('*');
        }
    }

    private void handleRoleSelection() {
        if (view.rbKaryawan.isSelected()) {
            view.textNamaPerusahaan.setVisible(false);
            view.labelPerusahaan.setVisible(false);
            view.labelAlamat.setVisible(false);
            view.textAlamat.setVisible(false);
            view.labelSIUP.setVisible(false);
            view.textSIUP.setVisible(false);

        } else if (view.rbPerusahaan.isSelected()) {
            view.textNamaPerusahaan.setVisible(true);
            view.labelPerusahaan.setVisible(true);
            view.labelAlamat.setVisible(true);
            view.textAlamat.setVisible(true);
            view.labelSIUP.setVisible(true);
            view.textSIUP.setVisible(true);
        }
    }

    public void RoleSelection() {

        view.rbKaryawan.addActionListener(e -> handleRoleSelection());

        view.rbPerusahaan.addActionListener(e -> handleRoleSelection());
    }
}
