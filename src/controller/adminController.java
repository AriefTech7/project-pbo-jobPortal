package controller;

import model.DAO.perusahaanDAO;
import model.DAO.ulasanDAO;
import model.entity.perusahaan;
import view.pageAdmin;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import java.util.List;
import model.entity.ulasan;

public class adminController {

    private final perusahaanDAO perusahaanDAO;
    private final ulasanDAO ulasanDAO;
    private final pageAdmin view;

    public adminController(pageAdmin view) {
        this.view = view;
        this.perusahaanDAO = new perusahaanDAO();
        this.ulasanDAO = new ulasanDAO();
    }

    public void loadAllDataPerusahaan() {
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
            loadAllDataPerusahaan();
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
            loadAllDataPerusahaan();
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

    private Integer getSelectedId() {
        int row = view.tablePerusahaan.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Pilih data terlebih dahulu.");
            return null;
        }
        return (Integer) view.tablePerusahaan.getModel().getValueAt(row, 0);
    }
    public void loadAllDataUlasan() {
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
        int rowUlasan = view.tableUlasan.getSelectedRow();

        if (rowUlasan == -1) {
            JOptionPane.showMessageDialog(view, "Pilih ulasan yang ingin dihapus terlebih dahulu!");
            return;
        }

        int idUlasan = (int) view.tableUlasan.getValueAt(rowUlasan, 0);
        boolean hasil = ulasanDAO.delUlasan(idUlasan);
        if (hasil) {
            JOptionPane.showMessageDialog(view, "Ulasan berhasil dihapus");
            loadAllDataUlasan();
        } else {
            JOptionPane.showMessageDialog(view, "Ulasan gagal dihapus");
        }
    }
}
