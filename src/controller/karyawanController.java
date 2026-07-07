package controller;

import model.DAO.karyawanDAO;
import model.DAO.karyawanDAOImpl;
import model.entity.lowongan;
import model.entity.lamaran;
import model.entity.ulasan;
import model.entity.perusahaan;
import config.SessionManager;
import view.pageKaryawan;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;

public class karyawanController {

    private karyawanDAO karyawanDAO;
    private pageKaryawan view;
    private File cvTerpilih;
    private Map<String, Integer> petaPerusahaan = new HashMap<>();

    public karyawanController(pageKaryawan view) {
        this.view = view;
        this.karyawanDAO = new karyawanDAOImpl();
    }

    public void loadDataLowongan() {
        tampilkanDataLowongan(karyawanDAO.getLowongan());
    }

    public void searchData() {
        String keyword = view.search.getText().trim();
        List<lowongan> list;

        if (keyword.isEmpty()) {
            list = karyawanDAO.getLowongan();
        } else {
            list = karyawanDAO.search(keyword);
        }

        String[] columns = {"ID lowongan", "ID Perusahaan", "Nama Perusahaan", "Posisi", "Gaji", "Jenis Kontrak"};
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

    public void filterData() {
        String keyword = view.search.getText().trim();
        String ratingText = (String) view.ratingBox.getSelectedItem();
        Integer minRating = null;
        if (ratingText != null) {
            String angka = ratingText.replaceAll("[^0-9]", "");
            if (!angka.isEmpty()) {
                minRating = Integer.parseInt(angka);
            }
        }
        tampilkanDataLowongan(karyawanDAO.filter(keyword, minRating));
    }

    private void sembunyikanKolom(int index) {
        view.tableLowongan.getColumnModel().getColumn(index).setMinWidth(0);
        view.tableLowongan.getColumnModel().getColumn(index).setMaxWidth(0);
        view.tableLowongan.getColumnModel().getColumn(index).setWidth(0);
    }

    private void tampilkanDataLowongan(List<lowongan> list) {
        String[] columns = {"ID lowongan", "ID Perusahaan", "Nama Perusahaan", "Posisi", "Gaji", "Jenis Kontrak", "Jobdesk"};
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
                l.getJenis_kontrak(),
                l.getJobdesk()
            });
        }
        view.tableLowongan.setModel(model);

        sembunyikanKolom(0);
        sembunyikanKolom(1);
        sembunyikanKolom(6);

        view.textAreaDP.setText("");
    }

    public void tampilkanJobdesk() {
        int row = view.tableLowongan.getSelectedRow();
        if (row == -1) {
            return;
        }
        String jobdesk = (String) view.tableLowongan.getModel().getValueAt(row, 6);

        if (jobdesk != null) {
            jobdesk = jobdesk.replaceAll("\\.\\s+(?=[A-Z])", ".\n");
            ;
        }

        view.textAreaDP.setText(jobdesk != null ? jobdesk : "-");
    }

    public void uploadCV() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Pilih File CV (PDF)");
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF Files", "pdf"));
        int result = chooser.showOpenDialog(view);
        if (result == JFileChooser.APPROVE_OPTION) {
            cvTerpilih = chooser.getSelectedFile();
            JOptionPane.showMessageDialog(view, "CV terpilih: " + cvTerpilih.getName());
        }
    }

    public void lamarSekarang() {
        int row = view.tableLowongan.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Pilih lowongan terlebih dahulu.");
            return;
        }
        if (cvTerpilih == null) {
            JOptionPane.showMessageDialog(view, "Upload CV terlebih dahulu sebelum melamar.");
            return;
        }

        int idKaryawan = SessionManager.getCurrentUser();
        if (!SessionManager.isLoggedIn()) {
            JOptionPane.showMessageDialog(view, "Sesi login tidak ditemukan. Silakan login ulang.");
            return;
        }

        String nama = JOptionPane.showInputDialog(view, "Nama Lengkap:");
        if (nama == null || nama.trim().isEmpty()) {
            return;
        }

        String noHp = JOptionPane.showInputDialog(view, "Nomor HP:");
        if (noHp == null || noHp.trim().isEmpty()) {
            return;
        }

        String email = karyawanDAO.getEmailById(idKaryawan); // ambil otomatis dari tabel users
        if (email == null) {
            JOptionPane.showMessageDialog(view, "Gagal mengambil data email user.");
            return;
        }
        int idLowongan = (int) view.tableLowongan.getModel().getValueAt(row, 0);

        boolean berhasil = karyawanDAO.ajukanLamaran(
                idLowongan, idKaryawan, nama.trim(), email, noHp.trim(), cvTerpilih.getAbsolutePath()
        );

        if (berhasil) {
            JOptionPane.showMessageDialog(view, "Lamaran berhasil dikirim.");
            cvTerpilih = null;
        } else {
            JOptionPane.showMessageDialog(view, "Gagal mengirim lamaran.");
        }
    }

    public void loadRiwayat() {
        int currentID = SessionManager.getCurrentUser();
        List<lamaran> list = karyawanDAO.getRiwayatByKaryawan(currentID);

        String[] columns = {"Nama Perusahaan", "Posisi Pekerjaan", "Tanggal Lamaran", "Status Lamaran"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (lamaran lm : list) {
            model.addRow(new Object[]{
                lm.getNama_perusahaan(),
                lm.getPosisi(),
                lm.getTangga_lamar(),
                formatStatus(lm.getStatus())
            });
        }

        view.tableLamaran.setModel(model);
    }

    private String formatStatus(String status) {
        if (status == null) {
            return "Diproses";
        }
        switch (status.toLowerCase()) {
            case "diterima":
                return "Diterima";
            case "ditolak":
                return "Ditolak";
            case "diproses":
                return "Diproses";
            default:
                return status;
        }
    }

    public void loadComboPerusahaan() {
        List<perusahaan> list = karyawanDAO.getPerusahaanApproved();
        petaPerusahaan.clear();

        String[] namaSaja = new String[list.size()];
        String[] namaDenganSemua = new String[list.size() + 1];
        namaDenganSemua[0] = "Semua Perusahaan";

        for (int i = 0; i < list.size(); i++) {
            namaSaja[i] = list.get(i).getNama();
            namaDenganSemua[i + 1] = list.get(i).getNama();
            petaPerusahaan.put(list.get(i).getNama(), list.get(i).getId_perusahaan());
        }

        view.boxPerusahaanTulis.setModel(new DefaultComboBoxModel<>(namaSaja));
        view.boxPerusahaanFilter.setModel(new DefaultComboBoxModel<>(namaDenganSemua));
        // boxRating TIDAK disentuh di sini — biarkan statis "1".."5"
    }

    public void submitUlasan() {
        String namaPerusahaan = (String) view.boxPerusahaanTulis.getSelectedItem();
        String ratingStr = (String) view.boxRating.getSelectedItem();
        String isiUlasan = view.areaUlasan.getText().trim();

        if (namaPerusahaan == null || !petaPerusahaan.containsKey(namaPerusahaan)) {
            JOptionPane.showMessageDialog(view, "Pilih perusahaan terlebih dahulu.");
            return;
        }
        if (ratingStr == null || ratingStr.trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Pilih rating terlebih dahulu.");
            return;
        }
        if (isiUlasan.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Ulasan tidak boleh kosong.");
            return;
        }

        int idPerusahaan = petaPerusahaan.get(namaPerusahaan);
        int idKaryawan = SessionManager.getCurrentUser();
        int skorBintang = Integer.parseInt(ratingStr.trim());

        boolean berhasil = karyawanDAO.tambahUlasan(idPerusahaan, idKaryawan, skorBintang, isiUlasan);

        if (berhasil) {
            JOptionPane.showMessageDialog(view, "Ulasan berhasil dikirim. Terima kasih!");
            view.areaUlasan.setText("");
            view.boxRating.setSelectedIndex(0);
        } else {
            JOptionPane.showMessageDialog(view, "Gagal mengirim ulasan.");
        }
    }

    public void loadUlasan() {
        String namaPerusahaan = (String) view.boxPerusahaanFilter.getSelectedItem();
        Integer idPerusahaan = null;

        if (namaPerusahaan != null && !namaPerusahaan.equals("Semua Perusahaan")) {
            idPerusahaan = petaPerusahaan.get(namaPerusahaan);
        }

        List<ulasan> list = karyawanDAO.getUlasanByPerusahaan(idPerusahaan);

        String[] columns = {"Tanggal Ulasan", "Skor Bintang", "Ulasan Pengalaman"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (ulasan u : list) {
            model.addRow(new Object[]{
                u.getTanggal_ulasan(),
                u.getSkor_bintang() + " Bintang",
                u.getIsi_ulasan()
            });
        }
        view.tableUlasan.setModel(model);
        view.totalUlasan.setText("Total Ulasan: " + list.size() + " Karyawan");
    }
}
