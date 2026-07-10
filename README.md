# 🏢 Job Portal Application

## 📋 Deskripsi Proyek

**Job Portal Application** adalah sebuah sistem manajemen lowongan pekerjaan berbasis desktop yang dibangun menggunakan **Java Swing** dengan arsitektur **Model-View-Controller (MVC)**. Aplikasi ini menghubungkan tiga aktor utama dalam ekosistem perekrutan: **Admin**, **Perusahaan**, dan **Karyawan**, melalui satu platform terintegrasi.

Proyek ini dikembangkan sebagai tugas akhir mata kuliah **Pemrograman Berorientasi Objek (PBO)** dengan menerapkan konsep OOP seperti inheritance, encapsulation, polymorphism, dan design pattern DAO (Data Access Object). Aplikasi ini memungkinkan perusahaan untuk mempublikasikan lowongan, karyawan untuk mencari dan melamar pekerjaan, serta admin untuk memverifikasi dan mengelola seluruh sistem.

Aplikasi ini dilengkapi dengan fitur rating dan ulasan budaya kerja perusahaan, yang memungkinkan transparansi dan membantu calon pelamar dalam mengambil keputusan. Sistem ini juga mendukung upload file CV dalam format PDF untuk proses lamaran pekerjaan.

---

## 🎯 Fitur Utama

### 👑 **Admin**
- **Verifikasi SIUP Perusahaan**: Menyetujui atau menolak registrasi perusahaan berdasarkan dokumen SIUP
- **Kelola Data Perusahaan**: Melihat seluruh daftar perusahaan yang terdaftar
- **Filter & Pencarian**: Mencari perusahaan berdasarkan nama atau nomor SIUP
- **Kelola Ulasan**: Menghapus ulasan yang tidak sesuai atau melanggar kebijakan
- **Dashboard Monitoring**: Melihat keseluruhan aktivitas sistem

### 🏢 **Perusahaan**
- **Manajemen Lowongan Pekerjaan**:
  - Tambah lowongan baru (posisi, gaji, jenis kontrak, jobdesk)
  - Edit dan hapus lowongan yang sudah dipublikasikan
- **Kelola Pelamar**:
  - Melihat daftar pelamar yang melamar ke perusahaan
  - Update status lamaran (Diproses/Diterima/Ditolak)
- **Monitoring Rating & Ulasan**:
  - Melihat ulasan dan rating dari karyawan
  - Menampilkan rata-rata rating budaya kerja perusahaan

### 👨‍💻 **Karyawan**
- **Cari Lowongan Pekerjaan**:
  - Pencarian berdasarkan kata kunci (nama perusahaan, posisi)
  - Filter berdasarkan rating perusahaan
  - Lihat detail jobdesk dan informasi lowongan
- **Lamaran Pekerjaan**:
  - Upload CV dalam format PDF
  - Kirim lamaran ke lowongan yang diminati
- **Riwayat Lamaran**:
  - Melihat status lamaran yang telah diajukan
  - Monitoring proses rekrutmen (Diproses/Diterima/Ditolak)
- **Rating & Ulasan Budaya Kerja**:
  - Memberi rating bintang (1-5) untuk perusahaan
  - Menulis ulasan pengalaman bekerja
  - Melihat ulasan dari karyawan lain

---

## 🛠️ Teknologi yang Digunakan

| **Komponen** | **Teknologi** |
|--------------|---------------|
| **Bahasa Pemrograman** | Java 26 |
| **Framework GUI** | Swing |
| **Database** | MySQL |
| **JDBC Driver** | MySQL Connector/J 9.7.0 |
| **Arsitektur** | MVC (Model-View-Controller) |
| **Design Pattern** | DAO (Data Access Object) |
| **Build Tool** | Apache NetBeans |

---

## 📊 Struktur Database

### Tabel-tabel Utama:

#### 1. **users**
| Field | Type | Description |
|-------|------|-------------|
| id_user | INT(255) | Primary Key, Auto Increment |
| username | VARCHAR(255) | Nama pengguna untuk login |
| password | VARCHAR(255) | Password pengguna |
| role | ENUM | Role pengguna (admin/perusahaan/karyawan) |
| email | VARCHAR(255) | Alamat email pengguna |

#### 2. **perusahaan**
| Field | Type | Description |
|-------|------|-------------|
| id_perusahaan | INT(255) | Primary Key, Auto Increment |
| id_user | INT(255) | Foreign Key ke users |
| nama | VARCHAR(255) | Nama perusahaan |
| alamat | TEXT | Alamat perusahaan |
| nomor_siup | VARCHAR(255) | Nomor SIUP perusahaan |
| status | ENUM | Status verifikasi (pending/approved/rejected) |

#### 3. **lowongan**
| Field | Type | Description |
|-------|------|-------------|
| id_lowongan | INT | Primary Key, Auto Increment |
| id_perusahaan | INT | Foreign Key ke perusahaan |
| posisi | VARCHAR(255) | Posisi pekerjaan |
| gaji | FLOAT | Besaran gaji |
| jenis_kontrak | VARCHAR(255) | Jenis kontrak (tetap/kontrak/magang) |
| jobdesk | TEXT | Deskripsi pekerjaan |

#### 4. **lamaran**
| Field | Type | Description |
|-------|------|-------------|
| id_lamaran | INT | Primary Key, Auto Increment |
| id_lowongan | INT | Foreign Key ke lowongan |
| id_karyawan | INT | Foreign Key ke users |
| nama | VARCHAR(255) | Nama lengkap pelamar |
| email | VARCHAR(255) | Email pelamar |
| no_hp | VARCHAR(255) | Nomor HP pelamar |
| cv | VARCHAR(255) | Path file CV |
| status | VARCHAR(255) | Status lamaran (diproses/diterima/ditolak) |
| tanggal_lamar | DATE | Tanggal melamar |

#### 5. **ulasan**
| Field | Type | Description |
|-------|------|-------------|
| id_ulasan | INT | Primary Key, Auto Increment |
| id_perusahaan | INT | Foreign Key ke perusahaan |
| id_karyawan | INT | Foreign Key ke users |
| skor_bintang | INT | Rating bintang (1-5) |
| isi_ulasan | TEXT | Isi ulasan pengalaman bekerja |
| tanggal_ulasan | DATE | Tanggal memberi ulasan |

### Relasi Antar Tabel

```
users (1) ---< (M) perusahaan  (1 user memiliki 1 perusahaan)
users (1) ---< (M) lamaran     (1 karyawan memiliki banyak lamaran)
users (1) ---< (M) ulasan      (1 karyawan memiliki banyak ulasan)

perusahaan (1) ---< (M) lowongan  (1 perusahaan memiliki banyak lowongan)
perusahaan (1) ---< (M) ulasan    (1 perusahaan memiliki banyak ulasan)

lowongan (1) ---< (M) lamaran     (1 lowongan memiliki banyak lamaran)
```

---

## 🏗️ Arsitektur Aplikasi

```
projectAkhir/
├── config/                 # Konfigurasi aplikasi
│   ├── connector.java      # Koneksi database
│   └── SessionManager.java # Manajemen sesi pengguna
├── controller/             # Controller (Logika Bisnis)
│   ├── adminController.java
│   ├── karyawanController.java
│   ├── loginController.java
│   ├── perusahaanController.java
│   └── registrasiController.java
├── model.DAO/              # Data Access Object
│   ├── lamaranDAO.java
│   ├── lowonganDAO.java
│   ├── perusahaanDAO.java
│   ├── ulasanDAO.java
│   └── userDAO.java
├── model.entity/           # Entity Class
│   ├── lamaran.java
│   ├── lowongan.java
│   ├── perusahaan.java
│   ├── ulasan.java
│   └── user.java
└── view/                   # GUI (Swing)
    ├── pageAdmin.java
    ├── pageKaryawan.java
    ├── pageLogin.java
    ├── pagePerusahaan.java
    └── pageRegister.java
```

---

## 🚀 Cara Menjalankan Aplikasi

### Prasyarat:
- **JDK 26** atau lebih tinggi
- **MySQL Server** (XAMPP, WAMP, atau standalone)
- **NetBeans IDE** (opsional, untuk pengembangan)

### Langkah-langkah:

#### 1. **Clone Repository**
```bash
git clone https://github.com/AriefTech7/project-pbo-jobPortal.git
```

#### 2. **Setup Database**
   - Import file `database.sql` (jika tersedia) ke MySQL
   - Atau buat database sesuai struktur tabel di atas

#### 3. **Konfigurasi Koneksi Database**
   - Buka file `config/connector.java`
   - Sesuaikan URL, username, dan password MySQL
```java
private static final String URL = "jdbc:mysql://localhost:3306/jobportal";
private static final String USER = "root";
private static final String PASSWORD = "";
```

#### 4. **Tambahkan Library MySQL Connector**
   - Download `mysql-connector-j-9.7.0.jar`
   - Tambahkan ke classpath project

#### 5. **Jalankan Aplikasi**
   - Buka project di NetBeans atau IDE favorit
   - Run file `view/pageLogin.java`

#### 6. **Login Awal**
   - **Admin**: Gunakan akun admin yang sudah terdaftar di database
   - **Registrasi**: Buat akun baru sebagai Perusahaan atau Karyawan

---


## 📌 Fitur yang Akan Dikembangkan (Roadmap)

- [ ] **Notifikasi real-time** untuk status lamaran
- [ ] **Export laporan** dalam format PDF/Excel
- [ ] **Fitur chat** antara perusahaan dan pelamar
- [ ] **Dashboard statistik** untuk admin
- [ ] **Recaptcha** pada halaman registrasi
- [ ] **Reset password** via email
- [ ] **Multi-language support** (Indonesia/English)
- [ ] **Enkripsi password** menggunakan BCrypt
- [ ] **Fitur bookmark** lowongan pekerjaan

---

## 🧪 Testing

| **Role** | **Fitur yang Diuji** | **Status** |
|----------|----------------------|------------|
| Admin | Verifikasi SIUP | ✅ |
| Admin | Hapus Ulasan | ✅ |
| Perusahaan | CRUD Lowongan | ✅ |
| Perusahaan | Update Status Lamaran | ✅ |
| Karyawan | Pencarian & Filter | ✅ |
| Karyawan | Upload CV & Lamaran | ✅ |
| Karyawan | Rating & Ulasan | ✅ |
| All | Login & Registrasi | ✅ |

---

## 🐛 Known Issues

| **Issue** | **Status** | **Solusi** |
|-----------|------------|------------|
| Password disimpan dalam plain text | 🔴 Kritis | Implementasi BCrypt hashing |
| Error handling kurang optimal | 🟡 Sedang | Implementasi Logger dan exception handling yang lebih baik |
| Query berulang di beberapa DAO | 🟢 Rendah | Konsolidasi query ke satu DAO |

---

## 👥 Tim Pengembang

| **Nama** | **Role** | **GitHub** |
|----------|----------|------------|
| [Arief Fajar Nugraha](https://github.com/AriefTech7) | Lead Developer / Project Manager | [@AriefTech7](https://github.com/AriefTech7) |

---

## 🤝 Kontribusi

Kami sangat terbuka untuk kontribusi! Jika Anda ingin berkontribusi pada project ini:

1. **Fork** repository ini
2. **Clone** hasil fork Anda
3. Buat **branch** baru untuk fitur Anda (`git checkout -b fitur-anda`)
4. **Commit** perubahan Anda (`git commit -m 'Menambahkan fitur baru'`)
5. **Push** ke branch Anda (`git push origin fitur-anda`)
6. Buat **Pull Request**

---

## 📝 Lisensi

Proyek ini dikembangkan untuk tujuan **pendidikan** (tugas akhir mata kuliah PBO). Tidak diperkenankan untuk digunakan secara komersial tanpa izin dari tim pengembang.

---

## 🙏 Ucapan Terima Kasih

- **Dosen Pengampu Mata Kuliah PBO** atas bimbingan dan arahan
- **Teman-teman kelas** atas dukungan dan kolaborasi
- **Forum dan komunitas Java** atas sumber belajar yang bermanfaat

---

## 📞 Kontak

Untuk pertanyaan, saran, atau kolaborasi, silakan hubungi:
- **Email**: [arief.fajar@example.com](mailto:arief.fajar@example.com)
- **GitHub**: [AriefTech7](https://github.com/AriefTech7)

---


**✨ Happy Coding! Semoga project ini bermanfaat untuk pembelajaran Anda! ✨**

⭐ Jika Anda menyukai project ini, berikan bintang di GitHub! ⭐


---

## 📚 Dokumentasi Tambahan

### Class Diagram

```
+-------------------+     +-------------------+     +-------------------+
|      User         |     |   Perusahaan      |     |    Lowongan       |
+-------------------+     +-------------------+     +-------------------+
| - id_user: int    |     | - id_perusahaan   |     | - id_lowongan     |
| - username: str   |<----| - id_user: int    |<----| - id_perusahaan   |
| - password: str   |     | - nama: str       |     | - posisi: str     |
| - role: str       |     | - alamat: str     |     | - gaji: float     |
| - email: str      |     | - nomor_siup: str |     | - jenis_kontrak   |
+-------------------+     | - status: str     |     | - jobdesk: str    |
                          +-------------------+     +-------------------+
                                    |                         |
                                    |                         |
                          +-------------------+     +-------------------+
                          |     Ulasan        |     |     Lamaran       |
                          +-------------------+     +-------------------+
                          | - id_ulasan: int  |     | - id_lamaran: int |
                          | - id_perusahaan   |     | - id_lowongan     |
                          | - id_karyawan     |     | - id_karyawan     |
                          | - skor_bintang    |     | - nama: str       |
                          | - isi_ulasan: str |     | - email: str      |
                          | - tanggal_ulasan  |     | - no_hp: str      |
                          +-------------------+     | - cv: str         |
                                                    | - status: str     |
                                                    | - tanggal_lamar   |
                                                    +-------------------+
```



---

## 📖 Panduan Pengguna

### Untuk Karyawan:
1. **Registrasi**: Pilih role "karyawan", isi username, password, dan email
2. **Login**: Masuk dengan username dan password
3. **Cari Lowongan**: Gunakan fitur pencarian dan filter rating
4. **Lihat Jobdesk**: Klik lowongan untuk melihat deskripsi pekerjaan
5. **Upload CV**: Klik "Upload CV" untuk memilih file PDF
6. **Lamar**: Klik "Lamar Sekarang" untuk mengirim lamaran
7. **Cek Status**: Lihat riwayat lamaran di menu "Status Lamaran"
8. **Beri Ulasan**: Isi rating dan ulasan budaya kerja perusahaan

### Untuk Perusahaan:
1. **Registrasi**: Pilih role "perusahaan", isi data perusahaan lengkap
2. **Verifikasi**: Tunggu admin menyetujui SIUP perusahaan
3. **Login**: Masuk setelah akun diverifikasi
4. **Tambah Lowongan**: Isi form posisi, gaji, kontrak, dan jobdesk
5. **Kelola Lowongan**: Edit atau hapus lowongan yang sudah dibuat
6. **Lihat Pelamar**: Cek daftar pelamar yang melamar
7. **Update Status**: Ubah status lamaran (diproses/diterima/ditolak)
8. **Lihat Rating**: Cek rating budaya kerja perusahaan

### Untuk Admin:
1. **Login**: Masuk dengan akun admin
2. **Verifikasi SIUP**: Setujui atau tolak registrasi perusahaan
3. **Cari Perusahaan**: Gunakan fitur pencarian
4. **Kelola Ulasan**: Hapus ulasan yang tidak sesuai


---


**Terima kasih telah menggunakan Job Portal Application!** 🎉