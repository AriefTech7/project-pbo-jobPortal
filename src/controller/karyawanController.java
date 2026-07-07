package controller;

import model.DAO.karyawanDAO;
import model.DAO.karyawanDAOImpl;
import model.entity.lowongan;
import model.entity.ulasan;
import view.pageKaryawan;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import java.io.File;

public class karyawanController {

    private karyawanDAO karyawanDAO;
    private pageKaryawan view;
    private File cvTerpilih;

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

//    public void lamarSekarang() {
//        int row = view.tableLowongan.getSelectedRow();
//        if (row == -1) {
//            JOptionPane.showMessageDialog(view, "Pilih lowongan terlebih dahulu.");
//            return;
//        }
//        if (cvTerpilih == null) {
//            JOptionPane.showMessageDialog(view, "Upload CV terlebih dahulu sebelum melamar.");
//            return;
//        }
//
//        int idLowongan = (int) view.tableLowongan.getModel().getValueAt(row, 0);
//        int idKaryawan = Session.getIdUser();
//
//        boolean berhasil = karyawanDAO.ajukanLamaran(idLowongan, idKaryawan, cvTerpilih.getAbsolutePath());
//
//        if (berhasil) {
//            JOptionPane.showMessageDialog(view, "Lamaran berhasil dikirim.");
//        } else {
//            JOptionPane.showMessageDialog(view, "Gagal mengirim lamaran.");
//        }
//    }
//    public void loadDataUlasan(){
//        tampilkanDataUlasan(karyawanDAO.getUlasan());
//    }
//    
//    private void tampilkanDataUlasan(List<ulasan> list) {
//        String[] columns = {"ID ulasan", "ID Perusahaan", "ID karyawan", "tanggal_ulasan", "skor_bintang", "isi_ulasan"};
//        DefaultTableModel model = new DefaultTableModel(columns, 0) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return false;
//            }
//        };
//        for (ulasan u : list) {
//            model.addRow(new Object[]{
//               u.getId_ulasan(),
//               u.getId_perusahaan(),
//               u.getId_karyawan(),
//               u.getTanggal_ulasan(),
//               u.getSkor_bintang(),
//               u.getIsi_ulasan()
//            });
//        }
//        view.tableLowongan.setModel(model);
//
//        sembunyikanKolom(0);
//        sembunyikanKolom(1);
//        sembunyikanKolom(2);
//
//    }
}
