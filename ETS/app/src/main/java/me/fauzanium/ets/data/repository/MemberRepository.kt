package me.fauzanium.ets.data.repository

import me.fauzanium.ets.data.model.JenisKeanggotaanOptions
import me.fauzanium.ets.data.model.Member
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class MemberRepository {

    private val initialMembers = listOf(
        Member(
            id = 1,
            namaLengkap = "Andi Pratama",
            nomorInduk = "231001001",
            programStudi = "Teknik Informatika",
            alamat = "Jl. Sukolilo No. 12, Surabaya",
            nomorTelepon = "081234567890",
            email = "andi.pratama@student.ac.id",
            jenisKeanggotaan = JenisKeanggotaanOptions.MAHASISWA
        ),
        Member(
            id = 2,
            namaLengkap = "Siti Aisyah",
            nomorInduk = "231001002",
            programStudi = "Sistem Informasi",
            alamat = "Jl. Gubeng Kertajaya No. 45, Surabaya",
            nomorTelepon = "082345678901",
            email = "siti.aisyah@student.ac.id",
            jenisKeanggotaan = JenisKeanggotaanOptions.MAHASISWA
        ),
        Member(
            id = 3,
            namaLengkap = "Dr. Budi Santoso, M.T.",
            nomorInduk = "198501001",
            programStudi = "Teknik Informatika",
            alamat = "Jl. Mulyosari No. 88, Surabaya",
            nomorTelepon = "081398765432",
            email = "budi.santoso@dosen.ac.id",
            jenisKeanggotaan = JenisKeanggotaanOptions.DOSEN
        ),
        Member(
            id = 4,
            namaLengkap = "Rina Lestari",
            nomorInduk = "231001003",
            programStudi = "Ilmu Perpustakaan",
            alamat = "Jl. Raya Darmo No. 102, Surabaya",
            nomorTelepon = "085678901234",
            email = "rina.lestari@student.ac.id",
            jenisKeanggotaan = JenisKeanggotaanOptions.MAHASISWA
        ),
        Member(
            id = 5,
            namaLengkap = "Eko Prasetyo, S.Kom.",
            nomorInduk = "199204005",
            programStudi = "Lainnya",
            alamat = "Jl. Pemuda No. 15, Surabaya",
            nomorTelepon = "087812345678",
            email = "eko.prasetyo@tendik.ac.id",
            jenisKeanggotaan = JenisKeanggotaanOptions.TENDIK
        ),
        Member(
            id = 6,
            namaLengkap = "Maya Indah, S.P.",
            nomorInduk = "3578019002",
            programStudi = "Lainnya",
            alamat = "Jl. Diponegoro No. 77, Surabaya",
            nomorTelepon = "089611223344",
            email = "maya.indah@gmail.com",
            jenisKeanggotaan = JenisKeanggotaanOptions.UMUM
        )
    )

    private val _members = MutableStateFlow(initialMembers)
    val members: Flow<List<Member>> = _members.asStateFlow()

    private var nextId = initialMembers.maxOf { it.id } + 1

    fun getAllMembers(): Flow<List<Member>> {
        return members
    }

    fun getMemberById(id: Int): Member? {
        return _members.value.find { it.id == id }
    }

    fun isNomorIndukExists(nomorInduk: String, currentMemberId: Int? = null): Boolean {
        return _members.value.any {
            it.nomorInduk.equals(nomorInduk.trim(), ignoreCase = true) && it.id != currentMemberId
        }
    }

    fun addMember(member: Member): Boolean {
        val newMember = member.copy(id = nextId++)
        _members.value = _members.value + newMember
        return true
    }

    fun updateMember(member: Member): Boolean {
        val currentList = _members.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == member.id }
        if (index != -1) {
            currentList[index] = member
            _members.value = currentList
            return true
        }
        return false
    }

    fun deleteMember(id: Int): Boolean {
        val currentList = _members.value.toMutableList()
        val removed = currentList.removeIf { it.id == id }
        if (removed) {
            _members.value = currentList
        }
        return removed
    }

    fun searchMembers(query: String, filterJenis: String = "Semua"): Flow<List<Member>> {
        return members.map { list ->
            list.filter { member ->
                val matchesQuery = query.isBlank() ||
                        member.namaLengkap.contains(query, ignoreCase = true) ||
                        member.nomorInduk.contains(query, ignoreCase = true) ||
                        member.programStudi.contains(query, ignoreCase = true) ||
                        member.email.contains(query, ignoreCase = true)

                val matchesFilter = filterJenis == "Semua" ||
                        member.jenisKeanggotaan.equals(filterJenis, ignoreCase = true)

                matchesQuery && matchesFilter
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: MemberRepository? = null

        fun getInstance(): MemberRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = MemberRepository()
                INSTANCE = instance
                instance
            }
        }
    }
}
