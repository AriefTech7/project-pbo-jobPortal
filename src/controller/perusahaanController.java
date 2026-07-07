package controller;

import model.DAO.perusahaanDAO;
import model.DAO.perusahaanDAOImpl;
import model.entity.lowongan;
import view.pagePerusahaan;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import java.util.List;
import config.SessionManager;

public class perusahaanController {

    private perusahaanDAO perusahaanDAO;
    private pagePerusahaan view;
    private int idPerusahaan;

    public perusahaanController(pagePerusahaan view) {
        this.view = view;
        this.perusahaanDAO = new perusahaanDAOImpl();
        this.idPerusahaan = perusahaanDAO.getIdPerusahaanByUser(SessionManager.getCurrentUser());

        if (idPerusahaan == -1) {
            JOptionPane.showMessageDialog(view,
                "Akun ini belum memiliki data perusahaan terdaftar.",
                "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void loadDataLowongan() {
        List<lowongan> list = perusahaanDAO.getLowonganByPerusahaan(idPerusahaan);

        String[] columns = {"ID Lowongan", "Posisi", "Gaji", "Jenis Kontrak"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        for (lowongan l : list) {
            model.addRow(new Object[]{
                l.getId_lowongan(), l.getPosisi(), l.getGaji(), l.getJenis_kontrak()
            });
        }
        view.tableLowongan.setModel(model);
    }

    public void pilihBaris() {
        int row = view.tableLowongan.getSelectedRow();
        if (row == -1) return;

        view.textPosisi.setText(view.tableLowongan.getModel().getValueAt(row, 1).toString());
        view.textGaji.setText(view.tableLowongan.getModel().getValueAt(row, 2).toString());
        view.boxKontrak.setSelectedItem(view.tableLowongan.getModel().getValueAt(row, 3).toString());

        List<lowongan> list = perusahaanDAO.getLowonganByPerusahaan(idPerusahaan);
        int idLowongan = (int) view.tableLowongan.getModel().getValueAt(row, 0);
        for (lowongan l : list) {
            if (l.getId_lowongan() == idLowongan) {
                view.textJobdesk.setText(l.getJobdesk());
                break;
            }
        }
    }

    private lowongan ambilInputForm() {
        String posisi = view.textPosisi.getText().trim();
        String gajiStr = view.textGaji.getText().trim();
        String jenisKontrak = (String) view.boxKontrak.getSelectedItem();
        String jobdesk = view.textJobdesk.getText().trim();

        if (posisi.isEmpty() || gajiStr.isEmpty() || jobdesk.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Semua field wajib diisi.");
            return null;
        }

        float gaji;
        try {
            gaji = Float.parseFloat(gajiStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Gaji harus berupa angka.");
            return null;
        }

        lowongan l = new lowongan();
        l.setId_perusahaan(idPerusahaan);
        l.setPosisi(posisi);
        l.setGaji(gaji);
        l.setJenis_kontrak(jenisKontrak.toLowerCase());
        l.setJobdesk(jobdesk);
        return l;
    }

    public void tambahLowongan() {
        if (idPerusahaan == -1) return;

        lowongan l = ambilInputForm();
        if (l == null) return;

        boolean berhasil = perusahaanDAO.tambahLowongan(l);
        if (berhasil) {
            JOptionPane.showMessageDialog(view, "Lowongan berhasil ditambahkan.");
            clearForm();
            loadDataLowongan();
        } else {
            JOptionPane.showMessageDialog(view, "Gagal menambahkan lowongan.");
        }
    }

    public void ubahLowongan() {
        int row = view.tableLowongan.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Pilih data yang ingin diubah terlebih dahulu.");
            return;
        }

        lowongan l = ambilInputForm();
        if (l == null) return;

        int idLowongan = (int) view.tableLowongan.getModel().getValueAt(row, 0);
        l.setId_lowongan(idLowongan);

        boolean berhasil = perusahaanDAO.ubahLowongan(l);
        if (berhasil) {
            JOptionPane.showMessageDialog(view, "Lowongan berhasil diperbarui.");
            clearForm();
            loadDataLowongan();
        } else {
            JOptionPane.showMessageDialog(view, "Gagal memperbarui lowongan.");
        }
    }

    public void hapusLowongan() {
        int row = view.tableLowongan.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Pilih data yang ingin dihapus terlebih dahulu.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(view,
                "Yakin ingin menghapus lowongan ini?", "Konfirmasi",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        int idLowongan = (int) view.tableLowongan.getModel().getValueAt(row, 0);
        boolean berhasil = perusahaanDAO.hapusLowongan(idLowongan);

        if (berhasil) {
            JOptionPane.showMessageDialog(view, "Lowongan berhasil dihapus.");
            clearForm();
            loadDataLowongan();
        } else {
            JOptionPane.showMessageDialog(view, "Gagal menghapus lowongan.");
        }
    }

    public void clearForm() {
        view.textPosisi.setText("");
        view.textGaji.setText("");
        view.boxKontrak.setSelectedIndex(0);
        view.textJobdesk.setText("");
        view.tableLowongan.clearSelection();
    }
}