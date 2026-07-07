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

    private Integer getSelectedId() {
        int row = view.tablePerusahaan.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Pilih data terlebih dahulu.");
            return null;
        }
        return (Integer) view.tablePerusahaan.getModel().getValueAt(row, 0);
    }

    public void addData() {
        String nama = JOptionPane.showInputDialog(view, "Nama Perusahaan:");
        if (nama == null || nama.trim().isEmpty()) {
            return;
        }

        String alamat = JOptionPane.showInputDialog(view, "Alamat:");
        if (alamat == null) {
            return;
        }

        String nomorSiup = JOptionPane.showInputDialog(view, "Nomor SIUP:");
        if (nomorSiup == null) {
            return;
        }

        String idUserStr = JOptionPane.showInputDialog(view, "ID User pemilik:");
        if (idUserStr == null || idUserStr.trim().isEmpty()) {
            return;
        }
        try {
            int idUser = Integer.parseInt(idUserStr.trim());
            perusahaan p = new perusahaan();
            p.setId_user(idUser);
            p.setNama(nama.trim());
            p.setAlamat(alamat.trim());
            p.setNomor_siup(nomorSiup.trim());
            p.setStatus("pending");

            if (perusahaanDAO.tambah(p)) {
                JOptionPane.showMessageDialog(view, "Data berhasil ditambahkan.");
                loadAllData();
            } else {
                JOptionPane.showMessageDialog(view, "Gagal menambahkan data.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "ID User harus berupa angka.");
        }
    }

    public void editData() {
        Integer id = getSelectedId();
        if (id == null) {
            return;
        }

        int row = view.tablePerusahaan.getSelectedRow();
        String namaLama = (String) view.tablePerusahaan.getModel().getValueAt(row, 1);
        String alamatLama = (String) view.tablePerusahaan.getModel().getValueAt(row, 2);
        String siupLama = (String) view.tablePerusahaan.getModel().getValueAt(row, 3);

        String nama = JOptionPane.showInputDialog(view, "Nama Perusahaan:", namaLama);
        if (nama == null || nama.trim().isEmpty()) {
            return;
        }

        String alamat = JOptionPane.showInputDialog(view, "Alamat:", alamatLama);
        if (alamat == null) {
            return;
        }

        String nomorSiup = JOptionPane.showInputDialog(view, "Nomor SIUP:", siupLama);
        if (nomorSiup == null) {
            return;
        }

        perusahaan p = new perusahaan();
        p.setId_perusahaan(id);
        p.setNama(nama.trim());
        p.setAlamat(alamat.trim());
        p.setNomor_siup(nomorSiup.trim());

        if (perusahaanDAO.edit(p)) {
            JOptionPane.showMessageDialog(view, "Data berhasil diperbarui.");
            loadAllData();
        } else {
            JOptionPane.showMessageDialog(view, "Gagal memperbarui data.");
        }
    }

    public void delData() {
        Integer id = getSelectedId();
        if (id == null) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(view,
                "Yakin ingin menghapus data ini?", "Konfirmasi",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        if (perusahaanDAO.hapus(id)) {
            JOptionPane.showMessageDialog(view, "Data berhasil dihapus.");
            loadAllData();
        } else {
            JOptionPane.showMessageDialog(view, "Gagal menghapus data.");
        }
    }

}
