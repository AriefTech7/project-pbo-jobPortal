package controller;

import model.DAO.karyawanDAO;
import model.DAO.karyawanDAOImpl;
import model.entity.lowongan;
import view.pageKaryawan;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import javax.swing.JOptionPane;

public class karyawanController {

    private karyawanDAO karyawanDAO;
    private pageKaryawan view;

    public karyawanController(pageKaryawan view) {
        this.view = view;
        this.karyawanDAO = new karyawanDAOImpl();
    }

    public void loadDataLowongan() {
        List<lowongan> list = karyawanDAO.getLowongan();

        String[] columns = {"ID lowongan","ID Perusahaan", "Nama Perusahaan", "Posisi", "Gaji", "Jenis Kontrak"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (lowongan l : list) {
            model.addRow(new Object[]{
                l.getId_lowongan(),
                l.getId_perusahaan(),
                l.getNama_perusahaan(),
                l.getPosisi(),
                l.getGaji(),
                l.getJenis_kontrak()
            });
        }
        view.tableLowongan.setModel(model);
        view.tableLowongan.getColumnModel().getColumn(0).setMinWidth(0);
        view.tableLowongan.getColumnModel().getColumn(0).setMaxWidth(0);
        view.tableLowongan.getColumnModel().getColumn(0).setWidth(0);
        view.tableLowongan.getColumnModel().getColumn(1).setMinWidth(1);
        view.tableLowongan.getColumnModel().getColumn(1).setMaxWidth(1);
        view.tableLowongan.getColumnModel().getColumn(1).setWidth(1);
    }
    public void searchData() {
        String keyword = view.search.getText().trim();
        List<lowongan> list;

        if (keyword.isEmpty()) {
            list = karyawanDAO.getLowongan();
        } else {
            list = karyawanDAO.search(keyword);
        }

        String[] columns = {"ID lowongan","ID Perusahaan", "Nama Perusahaan", "Posisi", "Gaji", "Jenis Kontrak"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (lowongan l : list) {
            model.addRow(new Object[]{
                l.getId_lowongan(),
                l.getId_perusahaan(),
                l.getNama_perusahaan(),
                l.getPosisi(),
                l.getGaji(),
                l.getJenis_kontrak()
            });
        }

        view.tableLowongan.setModel(model);

        view.tableLowongan.getColumnModel().getColumn(0).setMinWidth(0);
        view.tableLowongan.getColumnModel().getColumn(0).setMaxWidth(0);
        view.tableLowongan.getColumnModel().getColumn(0).setWidth(0);
        view.tableLowongan.getColumnModel().getColumn(1).setMinWidth(1);
        view.tableLowongan.getColumnModel().getColumn(1).setMaxWidth(1);
        view.tableLowongan.getColumnModel().getColumn(1).setWidth(1);
    }

}
