package controller;
import javax.swing.JOptionPane;
//import model.DAO.userDAO;
//import model.DAO.userDAOImpl;
import model.DAO.userDAO;
import model.DAO.userDAOImpl;
import model.entity.user;
import view.pageLogin;
import view.pageRegister;
public class registrasiController {
    private userDAO userDAO;
    private pageRegister view;
    
    public registrasiController(pageRegister view){
        this.view=view;
        this.userDAO=new userDAOImpl();
    }
    
    public void handleRegis(String username, String password,String role,String email){
        if(username.isEmpty() || password.isEmpty() || role.isEmpty() || email.isEmpty()){
            JOptionPane.showMessageDialog(view, "Ada data yang kosong!!");
            return;
        }
        user registedUser = userDAO.regis(username, password, role, email);
        view.dispose();
        new pageLogin().setVisible(true);
    }
    public void hidePassword(){
        if (view.checkpwd.isSelected()){
           view.textPassword.setEchoChar((char)0);
        }else {
            view.textPassword.setEchoChar('*');
        }
    }
}
