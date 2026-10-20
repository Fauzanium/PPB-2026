package me.fauzanium.ets.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import me.fauzanium.ets.data.model.Member

@Composable
fun ConfirmDeleteDialog(
    member: Member,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Peringatan",
                tint = MaterialTheme.colorScheme.error
            )
        },
        title = {
            Text(
                text = "Konfirmasi Hapus Data",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "Apakah Anda yakin ingin menghapus data anggota '${member.namaLengkap}' (${member.nomorInduk})? Data yang dihapus tidak dapat dikembalikan."
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text("Hapus")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
