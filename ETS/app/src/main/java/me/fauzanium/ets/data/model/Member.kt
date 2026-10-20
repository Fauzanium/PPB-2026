package me.fauzanium.ets.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "members")
data class Member(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val namaLengkap: String,
    val nomorInduk: String, // NIM / NIP
    val programStudi: String,
    val alamat: String,
    val nomorTelepon: String,
    val email: String,
    val jenisKeanggotaan: String // Mahasiswa, Dosen, Tendik, Umum
)

object JenisKeanggotaanOptions {
    val ALL = listOf("Mahasiswa", "Dosen", "Tendik", "Umum")
    const val MAHASISWA = "Mahasiswa"
    const val DOSEN = "Dosen"
    const val TENDIK = "Tendik"
    const val UMUM = "Umum"
}

object ProgramStudiOptions {
    val LIST = listOf(
        "Teknik Informatika",
        "Sistem Informasi",
        "Teknik Elektro",
        "Teknik Industri",
        "Ilmu Perpustakaan",
        "Manajemen",
        "Akuntansi",
        "Hukum",
        "Lainnya"
    )
}
