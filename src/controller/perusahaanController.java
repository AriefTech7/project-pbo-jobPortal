
package controller;
import model.DAO.perusahaanDAO;
import model.DAO.perusahaanDAOImpl;
import model.entity.perusahaan;
import view.pageAdmin;
import javax.swing.table.DefaultTableModel;
import java.util.List;
public class perusahaanController {
    private perusahaanDAO perusahaanDAO;
    private pageAdmin view;
    
    public perusahaanController(pageAdmin view){
        this.view=view;
        this.perusahaanDAO= new perusahaanDAOImpl();
    }
    public void loadAllData() {
        List<perusahaan> list = perusahaanDAO.getPerusahaan();

        String[] columns = {"Nama", "Alamat", "Nomor SIUP", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (perusahaan p : list) {
            model.addRow(new Object[]{
                    p.getNama(),
                    p.getAlamat(),
                    p.getNomor_siup(),
                    p.getStatus()
            });
        }

        view.tablePerusahaan.setModel(model);
    }
}
