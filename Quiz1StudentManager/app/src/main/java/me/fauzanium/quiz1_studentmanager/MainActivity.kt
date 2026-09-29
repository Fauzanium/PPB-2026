package me.fauzanium.quiz1_studentmanager

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import me.fauzanium.quiz1_studentmanager.ui.components.AboutAppDialog
import me.fauzanium.quiz1_studentmanager.ui.components.DeleteConfirmationDialog
import me.fauzanium.quiz1_studentmanager.ui.screens.AddEditStudentScreen
import me.fauzanium.quiz1_studentmanager.ui.screens.HomeScreen
import me.fauzanium.quiz1_studentmanager.ui.screens.SplashScreen
import me.fauzanium.quiz1_studentmanager.ui.theme.Quiz1StudentManagerTheme
import me.fauzanium.quiz1_studentmanager.viewmodel.Screen
import me.fauzanium.quiz1_studentmanager.viewmodel.StudentViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Quiz1StudentManagerTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding()
                ) {
                    StudentManagerApp()
                }
            }
        }
    }
}

@Composable
fun StudentManagerApp(
    viewModel: StudentViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearToastMessage()
        }
    }

    Crossfade(targetState = uiState.currentScreen, label = "ScreenTransition") { screen ->
        when (screen) {
            is Screen.Splash -> {
                SplashScreen(
                    onTimeout = { viewModel.onSplashFinished() }
                )
            }
            is Screen.Home -> {
                HomeScreen(
                    students = uiState.filteredStudents,
                    searchQuery = uiState.searchQuery,
                    onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                    onAddStudentClick = { viewModel.navigateTo(Screen.AddStudent) },
                    onEditStudentClick = { student -> viewModel.navigateTo(Screen.EditStudent(student)) },
                    onDeleteStudentClick = { student -> viewModel.requestDeleteStudent(student) },
                    onRefreshClick = { viewModel.refreshData() },
                    onAboutClick = { viewModel.showAboutDialog(true) },
                    onExitClick = { (context as? Activity)?.finish() }
                )
            }
            is Screen.AddStudent -> {
                AddEditStudentScreen(
                    student = null,
                    onSave = { nim, name, programStudi ->
                        viewModel.addStudent(nim, name, programStudi)
                    },
                    onBack = { viewModel.navigateTo(Screen.Home) }
                )
            }
            is Screen.EditStudent -> {
                AddEditStudentScreen(
                    student = screen.student,
                    onSave = { nim, name, programStudi ->
                        viewModel.updateStudent(screen.student.id, nim, name, programStudi)
                    },
                    onBack = { viewModel.navigateTo(Screen.Home) }
                )
            }
        }
    }

    uiState.studentToDelete?.let { student ->
        DeleteConfirmationDialog(
            student = student,
            onConfirm = { viewModel.confirmDeleteStudent() },
            onDismiss = { viewModel.cancelDeleteStudent() }
        )
    }

    if (uiState.showAboutDialog) {
        AboutAppDialog(
            onDismiss = { viewModel.showAboutDialog(false) }
        )
    }
}
