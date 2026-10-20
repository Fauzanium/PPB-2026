package me.fauzanium.ets.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import me.fauzanium.ets.data.model.JenisKeanggotaanOptions
import me.fauzanium.ets.data.model.Member
import me.fauzanium.ets.data.repository.MemberRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class FormUiState(
    val id: Int? = null,
    val namaLengkap: String = "",
    val nomorInduk: String = "",
    val programStudi: String = "",
    val alamat: String = "",
    val nomorTelepon: String = "",
    val email: String = "",
    val jenisKeanggotaan: String = JenisKeanggotaanOptions.MAHASISWA,
    // Errors
    val namaError: String? = null,
    val nomorIndukError: String? = null,
    val programStudiError: String? = null,
    val alamatError: String? = null,
    val nomorTeleponError: String? = null,
    val emailError: String? = null,
    val jenisKeanggotaanError: String? = null,
    val isValid: Boolean = false
)

class MemberViewModel(
    private val repository: MemberRepository = MemberRepository.getInstance()
) : ViewModel() {

    private val _members = repository.members.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    val members: StateFlow<List<Member>> = _members

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedJenisFilter = MutableStateFlow("Semua")
    val selectedJenisFilter: StateFlow<String> = _selectedJenisFilter.asStateFlow()

    val filteredMembers: StateFlow<List<Member>> = combine(
        _members,
        _searchQuery,
        _selectedJenisFilter
    ) { list, query, filter ->
        list.filter { member ->
            val matchesQuery = query.isBlank() ||
                    member.namaLengkap.contains(query, ignoreCase = true) ||
                    member.nomorInduk.contains(query, ignoreCase = true) ||
                    member.programStudi.contains(query, ignoreCase = true) ||
                    member.email.contains(query, ignoreCase = true)

            val matchesFilter = filter == "Semua" ||
                    member.jenisKeanggotaan.equals(filter, ignoreCase = true)

            matchesQuery && matchesFilter
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Form State
    private val _formState = MutableStateFlow(FormUiState())
    val formState: StateFlow<FormUiState> = _formState.asStateFlow()

    // Notification message
    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    // Member selected for Detail Dialog
    private val _selectedMemberForDetail = MutableStateFlow<Member?>(null)
    val selectedMemberForDetail: StateFlow<Member?> = _selectedMemberForDetail.asStateFlow()

    // Delete Confirmation Dialog state
    private val _memberToDelete = MutableStateFlow<Member?>(null)
    val memberToDelete: StateFlow<Member?> = _memberToDelete.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateJenisFilter(filter: String) {
        _selectedJenisFilter.value = filter
    }

    // Form Field Updaters with Real-time Validation
    fun onNamaLengkapChanged(value: String) {
        _formState.value = _formState.value.copy(
            namaLengkap = value,
            namaError = validateNamaLengkap(value)
        )
        validateEntireForm()
    }

    fun onNomorIndukChanged(value: String) {
        _formState.value = _formState.value.copy(
            nomorInduk = value,
            nomorIndukError = validateNomorInduk(value, _formState.value.id)
        )
        validateEntireForm()
    }

    fun onProgramStudiChanged(value: String) {
        _formState.value = _formState.value.copy(
            programStudi = value,
            programStudiError = validateProgramStudi(value)
        )
        validateEntireForm()
    }

    fun onAlamatChanged(value: String) {
        _formState.value = _formState.value.copy(
            alamat = value,
            alamatError = validateAlamat(value)
        )
        validateEntireForm()
    }

    fun onNomorTeleponChanged(value: String) {
        _formState.value = _formState.value.copy(
            nomorTelepon = value,
            nomorTeleponError = validateNomorTelepon(value)
        )
        validateEntireForm()
    }

    fun onEmailChanged(value: String) {
        _formState.value = _formState.value.copy(
            email = value,
            emailError = validateEmail(value)
        )
        validateEntireForm()
    }

    fun onJenisKeanggotaanChanged(value: String) {
        _formState.value = _formState.value.copy(
            jenisKeanggotaan = value,
            jenisKeanggotaanError = validateJenisKeanggotaan(value)
        )
        validateEntireForm()
    }

    // Individual Field Validators
    private fun validateNamaLengkap(nama: String): String? {
        return when {
            nama.isBlank() -> "Nama lengkap wajib diisi"
            nama.trim().length < 3 -> "Nama lengkap minimal 3 karakter"
            else -> null
        }
    }

    private fun validateNomorInduk(nomorInduk: String, currentId: Int?): String? {
        val trimmed = nomorInduk.trim()
        return when {
            trimmed.isBlank() -> "Nomor Induk (NIM/NIP) wajib diisi"
            trimmed.length < 5 -> "Nomor Induk minimal 5 karakter/digit"
            repository.isNomorIndukExists(trimmed, currentId) -> "Nomor Induk ($trimmed) sudah terdaftar!"
            else -> null
        }
    }

    private fun validateProgramStudi(prodi: String): String? {
        return if (prodi.isBlank()) "Program studi wajib dipilih/diisi" else null
    }

    private fun validateAlamat(alamat: String): String? {
        return when {
            alamat.isBlank() -> "Alamat wajib diisi"
            alamat.trim().length < 5 -> "Alamat terlalu pendek (minimal 5 karakter)"
            else -> null
        }
    }

    private fun validateNomorTelepon(telepon: String): String? {
        val trimmed = telepon.trim()
        return when {
            trimmed.isBlank() -> "Nomor telepon wajib diisi"
            !trimmed.matches(Regex("^[0-9+]{9,15}$")) -> "Nomor telepon harus angka (9-15 digit)"
            else -> null
        }
    }

    private fun validateEmail(email: String): String? {
        val trimmed = email.trim()
        return when {
            trimmed.isBlank() -> "Email wajib diisi"
            !Patterns.EMAIL_ADDRESS.matcher(trimmed).matches() -> "Format email tidak valid (contoh: nama@domain.com)"
            else -> null
        }
    }

    private fun validateJenisKeanggotaan(jenis: String): String? {
        return if (jenis.isBlank()) "Jenis keanggotaan wajib dipilih" else null
    }

    private fun validateEntireForm(): Boolean {
        val current = _formState.value
        val namaErr = validateNamaLengkap(current.namaLengkap)
        val nomorErr = validateNomorInduk(current.nomorInduk, current.id)
        val prodiErr = validateProgramStudi(current.programStudi)
        val alamatErr = validateAlamat(current.alamat)
        val telpErr = validateNomorTelepon(current.nomorTelepon)
        val emailErr = validateEmail(current.email)
        val jenisErr = validateJenisKeanggotaan(current.jenisKeanggotaan)

        val isValid = namaErr == null && nomorErr == null && prodiErr == null &&
                alamatErr == null && telpErr == null && emailErr == null && jenisErr == null

        _formState.value = current.copy(
            namaError = namaErr,
            nomorIndukError = nomorErr,
            programStudiError = prodiErr,
            alamatError = alamatErr,
            nomorTeleponError = telpErr,
            emailError = emailErr,
            jenisKeanggotaanError = jenisErr,
            isValid = isValid
        )
        return isValid
    }

    fun submitForm(): Boolean {
        if (!validateEntireForm()) {
            _userMessage.value = "Mohon lengkapi semua kolom dengan data yang valid"
            return false
        }

        val state = _formState.value
        val member = Member(
            id = state.id ?: 0,
            namaLengkap = state.namaLengkap.trim(),
            nomorInduk = state.nomorInduk.trim(),
            programStudi = state.programStudi.trim(),
            alamat = state.alamat.trim(),
            nomorTelepon = state.nomorTelepon.trim(),
            email = state.email.trim(),
            jenisKeanggotaan = state.jenisKeanggotaan.trim()
        )

        return if (state.id == null || state.id == 0) {
            val success = repository.addMember(member)
            if (success) {
                _userMessage.value = "Anggota '${member.namaLengkap}' berhasil terdaftar!"
                resetForm()
            }
            success
        } else {
            val success = repository.updateMember(member)
            if (success) {
                _userMessage.value = "Data anggota '${member.namaLengkap}' berhasil diperbarui!"
                resetForm()
            }
            success
        }
    }

    fun loadMemberForEdit(memberId: Int) {
        val member = repository.getMemberById(memberId)
        if (member != null) {
            _formState.value = FormUiState(
                id = member.id,
                namaLengkap = member.namaLengkap,
                nomorInduk = member.nomorInduk,
                programStudi = member.programStudi,
                alamat = member.alamat,
                nomorTelepon = member.nomorTelepon,
                email = member.email,
                jenisKeanggotaan = member.jenisKeanggotaan,
                isValid = true
            )
        }
    }

    fun resetForm() {
        _formState.value = FormUiState()
    }

    fun setMemberToDelete(member: Member?) {
        _memberToDelete.value = member
    }

    fun confirmDeleteMember() {
        val member = _memberToDelete.value
        if (member != null) {
            val success = repository.deleteMember(member.id)
            if (success) {
                _userMessage.value = "Anggota '${member.namaLengkap}' berhasil dihapus."
            } else {
                _userMessage.value = "Gagal menghapus anggota."
            }
            _memberToDelete.value = null
        }
    }

    fun setSelectedMemberForDetail(member: Member?) {
        _selectedMemberForDetail.value = member
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }
}
