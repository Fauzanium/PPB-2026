package me.fauzanium.kuis1_studentmanager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Model data Mahasiswa
data class Student(
    val id: String,
    val name: String,
    val nim: String,
    val major: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentManagerHomeScreen(
    onAddClick: () -> Unit = {},
    onEditClick: (Student) -> Unit = {},
    onDeleteClick: (Student) -> Unit = {}
) {
    // State pencarian
    var searchQuery by remember { mutableStateOf("") }

    // Contoh data dummy
    var studentList by remember {
        mutableStateOf(
            listOf(
                Student("1", "Budi Santoso", "2301001", "Informatika"),
                Student("2", "Siti Aminah", "2301002", "Sistem Informasi"),
                Student("3", "Andi Wijaya", "2301003", "Teknik Komputer")
            )
        )
    }

    // Filter daftar berdasarkan pencarian
    val filteredList = studentList.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
                it.nim.contains(searchQuery, ignoreCase = true) ||
                it.major.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Student Manager",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                actions = {
                    IconButton(onClick = { /* Menu aksi tambahan */ }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Menu Aksi"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF3F4F8)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = Color(0xFF1976D2), // Warna biru tombo
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambah Mahasiswa",
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        containerColor = Color(0xFFF8F9FE)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Field Pencarian
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(text = "Cari mahasiswa...", color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Ikon Cari",
                        tint = Color.Gray
                    )
                },
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = Color(0xFF1976D2)
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Teks Informasi Jumlah Mahasiswa
            Row {
                Text(
                    text = "Jumlah mahasiswa: ",
                    fontSize = 15.sp,
                    color = Color.Black
                )
                Text(
                    text = "${filteredList.size}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Daftar Mahasiswa
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(filteredList, key = { it.id }) { student ->
                    StudentItemCard(
                        student = student,
                        onEdit = { onEditClick(student) },
                        onDelete = {
                            studentList = studentList.filter { it.id != student.id }
                            onDeleteClick(student)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun StudentItemCard(
    student: Student,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Placeholder Foto/Avatar Profil
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Avatar Mahasiswa",
                    tint = Color.Gray,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Informasi Detail Mahasiswa
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = student.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF1E293B)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "NIM: ${student.nim}",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
                Text(
                    text = student.major,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            // Tombol Aksi Edit
            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Data",
                    tint = Color(0xFF424242)
                )
            }

            // Tombol Aksi Hapus
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Hapus Data",
                    tint = Color(0xFFE53935)
                )
            }
        }
    }
}