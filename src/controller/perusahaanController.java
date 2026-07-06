package controller;

import model.DAO.perusahaanDAO;
import model.DAO.perusahaanDAOImpl;
import model.entity.perusahaan;
import view.pageAdmin;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import javax.swing.JOptionPane;

public class perusahaanController {

    private perusahaanDAO perusahaanDAO;
    private pageAdmin view;

    public perusahaanController(pageAdmin view) {
        this.view = view;
        this.perusahaanDAO = new perusahaanDAOImpl();
    }

    public void loadAllData() {
        List<perusahaan> list = perusahaanDAO.getPerusahaan();

        String[] columns = {"ID Perusahaan", "Nama", "Alamat", "Nomor SIUP", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (perusahaan p : list) {
            model.addRow(new Object[]{
                p.getId_perusahaan(),
                p.getNama(),
                p.getAlamat(),
                p.getNomor_siup(),
                p.getStatus()
            });
        }

        view.tablePerusahaan.setModel(model);
        view.tablePerusahaan.getColumnModel().getColumn(0).setMinWidth(0);
        view.tablePerusahaan.getColumnModel().getColumn(0).setMaxWidth(0);
        view.tablePerusahaan.getColumnModel().getColumn(0).setWidth(0);

    }

    public void approve() {
        int rowPerusahaan = view.tablePerusahaan.getSelectedRow();

        if (rowPerusahaan == -1) {
            JOptionPane.showMessageDialog(view, "Pilih perusahaan yang ingin di approve");
            return;
        }

        int idPerusahaan = (int) view.tablePerusahaan.getValueAt(rowPerusahaan, 0);
        boolean success = perusahaanDAO.updateStatus("approved", idPerusahaan);
        if (success) {
            JOptionPane.showMessageDialog(view, "Perusahaan disetujui.");
            loadAllData();
        }
    }

    public void reject() {
        int rowPerusahaan = view.tablePerusahaan.getSelectedRow();

        if (rowPerusahaan == -1) {
            JOptionPane.showMessageDialog(view, "Pilih perusahaan yang ingin di rejected");
            return;
        }

        int idPerusahaan = (int) view.tablePerusahaan.getValueAt(rowPerusahaan, 0);
        boolean success = perusahaanDAO.updateStatus("rejected", idPerusahaan);
        if (success) {
            JOptionPane.showMessageDialog(view, "Perusahaan ditolak.");
            loadAllData();
        }
    }

    public void searchData() {
        String keyword = view.search.getText().trim();
        List<perusahaan> list;

        if (keyword.isEmpty()) {
            list = perusahaanDAO.getPerusahaan();
        } else {
            list = perusahaanDAO.search(keyword);
        }

        String[] columns = {"ID Perusahaan", "Nama", "Alamat", "Nomor SIUP", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (perusahaan p : list) {
            model.addRow(new Object[]{
                p.getId_perusahaan(),
                p.getNama(),
                p.getAlamat(),
                p.getNomor_siup(),
                p.getStatus()
            });
        }

        view.tablePerusahaan.setModel(model);

        view.tablePerusahaan.getColumnModel().getColumn(0).setMinWidth(0);
        view.tablePerusahaan.getColumnModel().getColumn(0).setMaxWidth(0);
        view.tablePerusahaan.getColumnModel().getColumn(0).setWidth(0);
    }
}
