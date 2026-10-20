package me.fauzanium.ets.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import me.fauzanium.ets.data.model.JenisKeanggotaanOptions
import me.fauzanium.ets.data.model.ProgramStudiOptions
import me.fauzanium.ets.ui.components.AppTopBar
import me.fauzanium.ets.viewmodel.MemberViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RegisterScreen(
    viewModel: MemberViewModel,
    editingMemberId: Int? = null,
    onNavigateBack: () -> Unit
) {
    val formState by viewModel.formState.collectAsState()
    var expandedProdiDropdown by remember { mutableStateOf(false) }

    val isEditing = editingMemberId != null && editingMemberId != 0

    LaunchedEffect(editingMemberId) {
        if (isEditing) {
            viewModel.loadMemberForEdit(editingMemberId)
        } else {
            viewModel.resetForm()
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = if (isEditing) "Ubah Data Anggota" else "Form Pendaftaran Anggota",
                canNavigateBack = true,
                onNavigateBack = {
                    viewModel.resetForm()
                    onNavigateBack()
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = if (isEditing) "Perbarui informasi data diri anggota di bawah ini."
                else "Lengkapi formulir pendaftaran anggota perpustakaan di bawah ini.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Input Nama Lengkap
            FormTextField(
                value = formState.namaLengkap,
                onValueChange = { viewModel.onNamaLengkapChanged(it) },
                label = "Nama Lengkap *",
                icon = Icons.Default.Person,
                errorText = formState.namaError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            // Input NIM / NIP
            FormTextField(
                value = formState.nomorInduk,
                onValueChange = { viewModel.onNomorIndukChanged(it) },
                label = "NIM / NIP (Nomor Induk) *",
                icon = Icons.Default.Badge,
                errorText = formState.nomorIndukError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )

            // Input Program Studi - Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedProdiDropdown,
                onExpandedChange = { expandedProdiDropdown = !expandedProdiDropdown },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = formState.programStudi,
                    onValueChange = { viewModel.onProgramStudiChanged(it) },
                    readOnly = false,
                    label = { Text("Program Studi / Unit Kerja *") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedProdiDropdown) },
                    isError = formState.programStudiError != null,
                    supportingText = {
                        formState.programStudiError?.let {
                            Text(text = it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                )

                ExposedDropdownMenu(
                    expanded = expandedProdiDropdown,
                    onDismissRequest = { expandedProdiDropdown = false }
                ) {
                    ProgramStudiOptions.LIST.forEach { prodi ->
                        DropdownMenuItem(
                            text = { Text(prodi) },
                            onClick = {
                                viewModel.onProgramStudiChanged(prodi)
                                expandedProdiDropdown = false
                            }
                        )
                    }
                }
            }

            // Input Email
            FormTextField(
                value = formState.email,
                onValueChange = { viewModel.onEmailChanged(it) },
                label = "Alamat Email *",
                icon = Icons.Default.Email,
                errorText = formState.emailError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            // Input Nomor Telepon
            FormTextField(
                value = formState.nomorTelepon,
                onValueChange = { viewModel.onNomorTeleponChanged(it) },
                label = "Nomor Telepon / WhatsApp *",
                icon = Icons.Default.Phone,
                errorText = formState.nomorTeleponError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                )
            )

            // Input Alamat
            FormTextField(
                value = formState.alamat,
                onValueChange = { viewModel.onAlamatChanged(it) },
                label = "Alamat Lengkap *",
                icon = Icons.Default.Home,
                errorText = formState.alamatError,
                singleLine = false,
                maxLines = 3,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                )
            )

            // Selector Jenis Keanggotaan
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Jenis Keanggotaan *",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    JenisKeanggotaanOptions.ALL.forEach { jenis ->
                        val selected = formState.jenisKeanggotaan == jenis
                        FilterChip(
                            selected = selected,
                            onClick = { viewModel.onJenisKeanggotaanChanged(jenis) },
                            label = { Text(jenis) },
                            leadingIcon = if (selected) {
                                {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            } else null,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }

                formState.jenisKeanggotaanError?.let { err ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = err,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Global Validation Indicator / Notice
            AnimatedVisibility(visible = !formState.isValid && formState.namaLengkap.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Pastikan seluruh kolom wajib bertanda (*) terisi dengan benar.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { viewModel.resetForm() },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Reset")
                }

                Button(
                    onClick = {
                        val success = viewModel.submitForm()
                        if (success) {
                            onNavigateBack()
                        }
                    },
                    modifier = Modifier.weight(2f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = if (isEditing) Icons.Default.Save else Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isEditing) "Simpan Perubahan" else "Daftar Anggota")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    errorText: String? = null,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (errorText != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        },
        isError = errorText != null,
        supportingText = {
            errorText?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
        },
        singleLine = singleLine,
        maxLines = maxLines,
        keyboardOptions = keyboardOptions,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    )
}
