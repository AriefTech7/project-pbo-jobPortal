package controller;
import javax.swing.JOptionPane;
import model.DAO.regisDAO;
import model.DAO.regisDAOImpl;
import model.entity.user;
import view.pageLogin;
import view.pageRegister;
public class registrasiController {
    private regisDAO regisDAO;
    private pageRegister view;
    
    public registrasiController(pageRegister view){
        this.view=view;
        this.regisDAO=new regisDAOImpl();
    }
    
    public void handleRegis(String username, String password,String role,String email){
        if(username.isEmpty() || password.isEmpty() || role.isEmpty() || email.isEmpty()){
            JOptionPane.showMessageDialog(view, "Ada data yang kosong!!");
            return;
        }
        user registedUser = regisDAO.regis(username, password, role, email);
        view.dispose();
        new pageLogin().setVisible(true);
    }
}
