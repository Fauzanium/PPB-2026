package me.fauzanium.ets.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.fauzanium.ets.data.model.JenisKeanggotaanOptions
import me.fauzanium.ets.navigation.Screen
import me.fauzanium.ets.ui.components.AppTopBar
import me.fauzanium.ets.ui.components.MemberCard
import me.fauzanium.ets.ui.components.StatCard
import me.fauzanium.ets.viewmodel.MemberViewModel

@Composable
fun HomeScreen(
    viewModel: MemberViewModel,
    onNavigateTo: (String) -> Unit
) {
    val members by viewModel.members.collectAsState()

    val totalCount = members.size
    val mahasiswaCount = members.count { it.jenisKeanggotaan == JenisKeanggotaanOptions.MAHASISWA }
    val dosenCount = members.count { it.jenisKeanggotaan == JenisKeanggotaanOptions.DOSEN }
    val tendikUmumCount = members.count {
        it.jenisKeanggotaan == JenisKeanggotaanOptions.TENDIK ||
                it.jenisKeanggotaan == JenisKeanggotaanOptions.UMUM
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "LibraryReg",
                canNavigateBack = false
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            // Hero Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.tertiary
                                    )
                                ),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "LibraryReg",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 24.sp,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Aplikasi Pendaftaran & Pengelolaan Anggota Perpustakaan",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalLibrary,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Quick Stats Grid Header
            item {
                Text(
                    text = "Ringkasan Data Anggota",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCard(
                            title = "Total Anggota",
                            count = totalCount,
                            icon = Icons.Default.Group,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Mahasiswa",
                            count = mahasiswaCount,
                            icon = Icons.Default.People,
                            color = Color(0xFF1E88E5),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCard(
                            title = "Dosen",
                            count = dosenCount,
                            icon = Icons.Default.Psychology,
                            color = Color(0xFF00897B),
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Tendik & Umum",
                            count = tendikUmumCount,
                            icon = Icons.Default.People,
                            color = Color(0xFFFB8C00),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Navigation Menu Grid Section
            item {
                Text(
                    text = "Menu Utamaperpustakaan",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    MenuNavCard(
                        title = "Pendaftaran Anggota",
                        subtitle = "Isi formulir pendaftaran anggota baru",
                        icon = Icons.Default.PersonAdd,
                        backgroundColor = Color(0xFFE3F2FD),
                        iconColor = Color(0xFF1976D2),
                        onClick = { onNavigateTo(Screen.Register.createRoute()) }
                    )

                    MenuNavCard(
                        title = "Daftar Anggota",
                        subtitle = "Lihat seluruh daftar anggota terpustakaan",
                        icon = Icons.Default.People,
                        backgroundColor = Color(0xFFE8F5E9),
                        iconColor = Color(0xFF388E3C),
                        onClick = { onNavigateTo(Screen.MemberList.route) }
                    )

                    MenuNavCard(
                        title = "Pencarian Data",
                        subtitle = "Cari anggota berdasarkan Nama / NIM / NIP",
                        icon = Icons.Default.Search,
                        backgroundColor = Color(0xFFFFF3E0),
                        iconColor = Color(0xFFF57C00),
                        onClick = { onNavigateTo(Screen.Search.route) }
                    )

                    MenuNavCard(
                        title = "Kelola Data",
                        subtitle = "Ubah data dan hapus anggota (CRUD)",
                        icon = Icons.Default.Settings,
                        backgroundColor = Color(0xFFF3E5F5),
                        iconColor = Color(0xFF7B1FA2),
                        onClick = { onNavigateTo(Screen.ManageData.route) }
                    )
                }
            }

            // Recent Members Section Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Anggota Terbaru",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    TextButton(onClick = { onNavigateTo(Screen.MemberList.route) }) {
                        Text("Lihat Semua")
                    }
                }
            }

            // Recent Members Preview List
            val recentList = members.take(3)
            if (recentList.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Belum ada data anggota",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(recentList, key = { it.id }) { member ->
                    MemberCard(
                        member = member,
                        onCardClick = { onNavigateTo(Screen.MemberDetail.createRoute(member.id)) },
                        onEditClick = { onNavigateTo(Screen.Register.createRoute(member.id)) },
                        onDeleteClick = { viewModel.setMemberToDelete(member) }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun MenuNavCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    backgroundColor: Color,
    iconColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
