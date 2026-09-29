package me.fauzanium.quiz1_studentmanager.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.fauzanium.quiz1_studentmanager.data.ProgramStudiData
import me.fauzanium.quiz1_studentmanager.model.Student
import me.fauzanium.quiz1_studentmanager.ui.theme.Quiz1StudentManagerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditStudentScreen(
    modifier: Modifier = Modifier,
    student: Student? = null,
    onSave: (nim: String, name: String, programStudi: String) -> Unit,
    onBack: () -> Unit
) {
    var nim by remember { mutableStateOf(student?.nim ?: "") }
    var name by remember { mutableStateOf(student?.name ?: "") }
    var selectedProgramStudi by remember { mutableStateOf(student?.programStudi ?: "") }

    var expandedDropdown by remember { mutableStateOf(false) }

    var nimError by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf(false) }
    var programStudiError by remember { mutableStateOf(false) }

    val isEditing = student != null
    val title = if (isEditing) "Edit Mahasiswa" else "Tambah Mahasiswa"

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Field 1: NIM
            Text(
                text = "NIM",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = nim,
                onValueChange = {
                    nim = it
                    nimError = false
                },
                placeholder = { Text("Masukkan NIM") },
                isError = nimError,
                supportingText = if (nimError) {
                    { Text("NIM tidak boleh kosong") }
                } else null,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Field 2: Nama
            Text(
                text = "Nama",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    nameError = false
                },
                placeholder = { Text("Masukkan nama mahasiswa") },
                isError = nameError,
                supportingText = if (nameError) {
                    { Text("Nama tidak boleh kosong") }
                } else null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Field 3: Program Studi Dropdown
            Text(
                text = "Program Studi",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))

            ExposedDropdownMenuBox(
                expanded = expandedDropdown,
                onExpandedChange = { expandedDropdown = !expandedDropdown }
            ) {
                OutlinedTextField(
                    value = selectedProgramStudi,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Pilih program studi") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                    isError = programStudiError,
                    supportingText = if (programStudiError) {
                        { Text("Pilih program studi terlebih dahulu") }
                    } else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.LightGray
                    )
                )

                ExposedDropdownMenu(
                    expanded = expandedDropdown,
                    onDismissRequest = { expandedDropdown = false }
                ) {
                    ProgramStudiData.list.forEach { program ->
                        DropdownMenuItem(
                            text = { Text(program) },
                            onClick = {
                                selectedProgramStudi = program
                                programStudiError = false
                                expandedDropdown = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Bottom Buttons: Batal & Simpan
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Batal", fontSize = 15.sp)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = {
                        val isNimValid = nim.isNotBlank()
                        val isNameValid = name.isNotBlank()
                        val isProgramValid = selectedProgramStudi.isNotBlank()

                        nimError = !isNimValid
                        nameError = !isNameValid
                        programStudiError = !isProgramValid

                        if (isNimValid && isNameValid && isProgramValid) {
                            onSave(nim, name, selectedProgramStudi)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1976D2)
                    )
                ) {
                    Text(text = "Simpan", fontSize = 15.sp, color = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddEditStudentScreenPreview() {
    Quiz1StudentManagerTheme {
        AddEditStudentScreen(
            student = null,
            onSave = { _, _, _ -> },
            onBack = {}
        )
    }
}
