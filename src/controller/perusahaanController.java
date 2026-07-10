package controller;

import model.DAO.perusahaanDAO;
import model.DAO.ulasanDAO;
import model.entity.lowongan;
import view.pagePerusahaan;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import java.util.List;
import config.SessionManager;
import java.text.SimpleDateFormat;
import model.entity.lamaran;
import model.entity.ulasan;

public class perusahaanController {

    private final perusahaanDAO perusahaanDAO;
    private final ulasanDAO ulasanDAO;
    private final pagePerusahaan view;
    private final int idPerusahaan;

    public perusahaanController(pagePerusahaan view) {
        this.view = view;
        this.perusahaanDAO = new perusahaanDAO();
        this.ulasanDAO = new ulasanDAO();
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
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (lowongan l : list) {
            model.addRow(new Object[]{
                l.getId_lowongan(), l.getPosisi(), l.getGaji(), l.getJenis_kontrak()
            });
        }
        view.tableLowongan.setModel(model);
    }

    public void loadDataPelamar() {
        List<lamaran> list = perusahaanDAO.getPelamar();

        String[] columns = {"ID lamaran", "ID Lowongan", "ID karyawan", "Nama", "Email", "No HP", "Posisi", "Tanggal Lamaran", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (lamaran l : list) {
            model.addRow(new Object[]{
                l.getId_lamaran(),
                l.getId_lowongan(),
                l.getId_karyawan(),
                l.getNama(),
                l.getEmail(),
                l.getNo_hp(),
                l.getPosisi(),
                l.getTanggal_lamar(),
                l.getStatus()
            });
        }
        view.tablePelamar.setModel(model);
        sembunyikanKolom(view.tablePelamar, 0);
        sembunyikanKolom(view.tablePelamar, 1);
        sembunyikanKolom(view.tablePelamar, 2);

    }

    private void sembunyikanKolom(JTable namaTable, int index) {
        namaTable.getColumnModel().getColumn(index).setMinWidth(0);
        namaTable.getColumnModel().getColumn(index).setMaxWidth(0);
        namaTable.getColumnModel().getColumn(index).setWidth(0);
    }

    public void pilihBaris() {
        int row = view.tableLowongan.getSelectedRow();
        if (row == -1) {
            return;
        }

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
        if (idPerusahaan == -1) {
            return;
        }

        lowongan l = ambilInputForm();
        if (l == null) {
            return;
        }

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
        if (l == null) {
            return;
        }

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
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

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

    public void updateStatusLamaran() {
        int selectedRow = view.tablePelamar.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Pilih pelamar terlebih dahulu!");
            return;
        }

        int idLamaran = (int) view.tablePelamar.getModel().getValueAt(selectedRow, 0);

        String statusBaru = (String) view.boxStatus.getSelectedItem();

        if (statusBaru == null) {
            JOptionPane.showMessageDialog(view, "Pilih status terlebih dahulu!");
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(view,
                "Update status menjadi '" + statusBaru + "'?",
                "Konfirmasi Update",
                JOptionPane.YES_NO_OPTION);

        if (konfirmasi == JOptionPane.YES_OPTION) {
            boolean berhasil = perusahaanDAO.updateStatusLamaran(idLamaran, statusBaru);

            if (berhasil) {
                JOptionPane.showMessageDialog(view, "Status berhasil diupdate!");
                loadDataPelamar();
            } else {
                JOptionPane.showMessageDialog(view, "Gagal mengupdate status!");
            }
        }
    }

    public void onRowSelected() {
        int selectedRow = view.tablePelamar.getSelectedRow();

        if (selectedRow != -1) {
            String statusSaatIni = (String) view.tablePelamar.getValueAt(selectedRow, 5);
            view.boxStatus.setSelectedItem(statusSaatIni);
        }
    }

    public void loadUlasan() {
//        System.out.println("ID Perusahaan : " + idPerusahaan);

        List<ulasan> list = ulasanDAO.getUlasanByPerusahaanJoin(idPerusahaan);

//        System.out.println("Jumlah Data : " + list.size());

        String[] columns = {"Tanggal Ulasan", "Skor Bintang", "Isi Ulasan"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        for (ulasan u : list) {
            String tanggalFormatted = sdf.format(u.getTanggal_ulasan());
            String bintangText = getBintangText(u.getSkor_bintang());

            model.addRow(new Object[]{
                tanggalFormatted,
                bintangText,
                u.getIsi_ulasan()
            });
        }

        view.tableUlasan.setModel(model);

        double avgRating = ulasanDAO.getAverageRating(idPerusahaan);
        tampilkanRatingRataRata(avgRating);
    }

    private void tampilkanRatingRataRata(double rating) {
        // Format rating dengan 1 desimal
        String ratingText = String.format("%.1f", rating);

        // Tampilkan di label
        view.avgLabel.setText("Rating Budaya Kerja Anda: " + ratingText + "/5");

        // Opsional: Tampilkan visual bintang
//        String bintangVisual = generateBintangVisual(rating);
//        view.labelBintangVisual.setText(bintangVisual);
    }

    private String getBintangText(int skor) {
        StringBuilder bintang = new StringBuilder();
        for (int i = 0; i < skor; i++) {
            bintang.append("★");
        }
        for (int i = skor; i < 5; i++) {
            bintang.append("☆");
        }
        return bintang.toString() + " (" + skor + "/5)";
    }

}
