package controller;


import model.DAO.userDAO;
import model.entity.user;
import config.SessionManager;
import view.pageLogin;
import view.pageAdmin;
import view.pageKaryawan;
import view.pagePerusahaan;
import javax.swing.JOptionPane;

public class loginController {

    private final userDAO userDAO;
    private final pageLogin view;

    public loginController(pageLogin view) {
        this.view = view;
        this.userDAO = new userDAO();
    }

    public void handleLogin(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Username dan password wajib diisi!");
            return;
        }
        
        user loggedInUser = userDAO.login(username, password);
        int id_User =userDAO.selectID(username, password);
        SessionManager.login(id_User); 
        if (loggedInUser == null) {
            JOptionPane.showMessageDialog(view, "Username atau password salah!");
            return;
        }

        navigateByRole(loggedInUser);
    }

    private void navigateByRole(user u) {
        switch (u.getRole().toLowerCase()) {
            case "admin":
                view.dispose(); 
                new pageAdmin().setVisible(true);
                break;
            case "karyawan":
                view.dispose();
                new pageKaryawan().setVisible(true);
                break;
            case "perusahaan":
                view.dispose();
                new pagePerusahaan().setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(view, "Role tidak dikenali!");
        }
    }
    
    public void hidePassword(){
        if (view.checkpwd.isSelected()){
           view.textPassword.setEchoChar((char)0);
        }else {
            view.textPassword.setEchoChar('*');
        }
    }
}