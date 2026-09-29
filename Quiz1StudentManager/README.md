# Student Manager

Aplikasi Android berbasis **Jetpack Compose** dan **Material 3** untuk mengelola data mahasiswa serta fitur pencarian data secara real-time.
Link Demo: https://drive.google.com/drive/u/0/folders/131TkZIJn0NbpkUhq24edsBvu5hnDS-7F

<img src="Screenshot%202026-09-29%20144111.png" alt="" width="1920" height="1038">

## 1. Fitur
- **Splash Screen**: Pembuka aplikasi dengan animasi loading.
- **Daftar Mahasiswa (Home Screen)**: Menampilkan seluruh daftar mahasiswa dalam bentuk list card.
- **Pencarian Real-Time (Search)**: Memfilter data mahasiswa secara instan berdasarkan Nama, NIM, atau Program Studi.
- **Counter Mahasiswa**: Menampilkan jumlah mahasiswa yang sesuai dengan hasil pencarian/filter secara dinamis.
- **Tambah Mahasiswa (Create)**: Form untuk menambahkan data mahasiswa baru beserta validasi input.
- **Edit Mahasiswa (Update)**: Form *pre-filled* untuk memperbarui data mahasiswa eksisting.
- **Hapus Mahasiswa (Delete)**: Dialog konfirmasi hapus data mahasiswa.
- **Empty State**: Tampilan khusus saat daftar mahasiswa kosong atau hasil pencarian tidak ditemukan.
- **Menu**:
  - **Refresh**: Memperbarui/mereset pencarian data dan memuat ulang.
  - **Tentang Aplikasi**: Dialog informasi versi aplikasi dan stack teknologi.
  - **Keluar**: Menutup aplikasi (`finish activity`).

---

## 2. Komponen UI

### 1. Splash Screen (`SplashScreen.kt`)
- Tampilan awal dengan logo, judul aplikasi, dan deskripsi singkat.
- Beralih ke Home Screen setelah beberapa detik.

### 2. Home Screen (`HomeScreen.kt`)
- **TopAppBar**: Menampilkan judul "Student Manager" dan menu titik 3.
- **Search Bar**: Input pencarian dengan ikon *search* dan ikon *clear* saat ada teks.
- **List / Empty State**: Menampilkan kumpulan `StudentCard` atau tampilan Empty State jika data tidak ditemukan.
- **Action Button**: Tombol `+` di kanan bawah untuk membuka form Tambah Mahasiswa.

### 3. Form Tambah & Edit Mahasiswa (`AddEditStudentScreen.kt`)
- Digunakan untuk penambahan data baru maupun pengubahan data yang ada.
- **Data**:
  - **NIM**: `OutlinedTextField` angka wajib diisi.
  - **Nama**: `OutlinedTextField` teks wajib diisi.
  - **Program Studi**: `Dropdown` berisi daftar pilihan program studi:
    - *Informatika*
    - *Sistem Informasi*
    - *Teknik Komputer*
    - *Desain Komunikasi Visual*
    - *Manajemen*
    - *Akuntansi*
- **Action Buttons**: Tombol **Batal** dan **Simpan**.

### 3. Dialog Konfirmasi Hapus (`DeleteConfirmationDialog.kt`)
- Meminta konfirmasi pengguna sebelum menghapus data mahasiswa secara permanen.

### 4. Dialog Tentang Aplikasi (`AboutAppDialog.kt`)
- Menampilkan informasi versi aplikasi (v1.0), deskripsi, dan daftar teknologi yang digunakan.

---

## 5. Model

### `Student.kt`
```kotlin
data class Student(
    val id: String = UUID.randomUUID().toString(),
    val nim: String,
    val name: String,
    val programStudi: String
)
```
---

## 6. Penggunaan
1. **Melihat Daftar Mahasiswa**: Saat aplikasi dibuka, daftar mahasiswa akan ditampilkan dalam daftar.
2. **Mencari Mahasiswa**: Ketik keyword pada pencarian untuk memfilter daftar berdasarkan Nama, NIM, atau Program Studi.
3. **Menambah Mahasiswa**: Klik tombol **+**, isi NIM, Nama, pilih Program Studi, lalu klik **Simpan**.
4. **Mengubah Data Mahasiswa**: Klik ikon **pensil (Edit)** pada card mahasiswa, ubah informasi yang diinginkan, lalu klik **Simpan**.
5. **Menghapus Mahasiswa**: Klik ikon **tempat sampah (Hapus)** pada card mahasiswa, lalu klik **Hapus** pada dialog konfirmasi.
6. **Menu Opsional**: Klik ikon titik 3 di kanan atas untuk menggunakan opsi **Refresh**, melihat **Tentang Aplikasi**, atau **Keluar**.
