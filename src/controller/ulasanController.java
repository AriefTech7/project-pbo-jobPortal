package controller;

import model.DAO.ulasanDAO;
import model.DAO.ulasanDAOImpl;
import model.entity.ulasan;
import view.pageAdmin;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import javax.swing.JOptionPane;

public class ulasanController {

    private ulasanDAO ulasanDAO;
    private pageAdmin view;

    public ulasanController(pageAdmin view) {
        this.view = view;
        this.ulasanDAO = new ulasanDAOImpl();
    }

    public void loadAllData() {
        List<ulasan> list = ulasanDAO.getUlasan();

        String[] columns = {"ID", "Ulasan", "Rating", "Tanggal Ulasan"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (ulasan p : list) {
            model.addRow(new Object[]{
                p.getId_ulasan(),
                p.getIsi_ulasan(),
                p.getSkor_bintang(),
                p.getTanggal_ulasan()
            });

        }
        view.tableUlasan.setModel(model);
    }

    public void deleteUlasan() {
//        int selectedRow = view.tableUlasan.getSelectedRow(); 
//
//        if (selectedRow == -1) {
//            JOptionPane.showMessageDialog(view, "Pilih ulasan yang ingin dihapus terlebih dahulu!");
//            return;
//        }
        int rowUlasan = view.tablePerusahaan.getSelectedRow();
        int idUlasan = (int) view.tableUlasan.getValueAt(rowUlasan, 0);
//        boolean hasil = ulasanDAO.delUlasan(idUlasan);

//        int idUlasan = (int) view.tableUlasan.getValueAt(selectedRow, 0); 
        boolean hasil = ulasanDAO.delUlasan(idUlasan);
        if (hasil) {
            JOptionPane.showMessageDialog(view, "Ulasan berhasil dihapus");
            loadAllData();
        } else {
            JOptionPane.showMessageDialog(view, "Ulasan gagal dihapus");
        }
    }
}
