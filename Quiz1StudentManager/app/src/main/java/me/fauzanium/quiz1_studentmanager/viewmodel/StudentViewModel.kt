package me.fauzanium.quiz1_studentmanager.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.fauzanium.quiz1_studentmanager.model.Student

sealed interface Screen {
    data object Splash : Screen
    data object Home : Screen
    data object AddStudent : Screen
    data class EditStudent(val student: Student) : Screen
}

data class StudentUiState(
    val students: List<Student> = initialStudents,
    val searchQuery: String = "",
    val currentScreen: Screen = Screen.Splash,
    val studentToDelete: Student? = null,
    val showAboutDialog: Boolean = false,
    val toastMessage: String? = null
) {
    val filteredStudents: List<Student>
        get() {
            if (searchQuery.isBlank()) return students
            val query = searchQuery.trim().lowercase()
            return students.filter { student ->
                student.name.lowercase().contains(query) ||
                        student.nim.lowercase().contains(query) ||
                        student.programStudi.lowercase().contains(query)
            }
        }
}

private val initialStudents = listOf<Student>()

class StudentViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(StudentUiState())
    val uiState: StateFlow<StudentUiState> = _uiState.asStateFlow()

    fun onSplashFinished() {
        _uiState.update { it.copy(currentScreen = Screen.Home) }
    }

    fun navigateTo(screen: Screen) {
        _uiState.update { it.copy(currentScreen = screen) }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun addStudent(nim: String, name: String, programStudi: String) {
        val newStudent = Student(nim = nim.trim(), name = name.trim(), programStudi = programStudi.trim())
        _uiState.update { state ->
            state.copy(
                students = state.students + newStudent,
                currentScreen = Screen.Home,
                toastMessage = "Mahasiswa berhasil ditambahkan"
            )
        }
    }

    fun updateStudent(id: String, nim: String, name: String, programStudi: String) {
        _uiState.update { state ->
            val updatedList = state.students.map { student ->
                if (student.id == id) {
                    student.copy(nim = nim.trim(), name = name.trim(), programStudi = programStudi.trim())
                } else {
                    student
                }
            }
            state.copy(
                students = updatedList,
                currentScreen = Screen.Home,
                toastMessage = "Data mahasiswa berhasil diperbarui"
            )
        }
    }

    fun requestDeleteStudent(student: Student) {
        _uiState.update { it.copy(studentToDelete = student) }
    }

    fun confirmDeleteStudent() {
        _uiState.update { state ->
            val student = state.studentToDelete
            val updatedList = if (student != null) state.students.filter { it.id != student.id } else state.students
            state.copy(
                students = updatedList,
                studentToDelete = null,
                toastMessage = "Data mahasiswa berhasil dihapus"
            )
        }
    }

    fun cancelDeleteStudent() {
        _uiState.update { it.copy(studentToDelete = null) }
    }

    fun showAboutDialog(show: Boolean) {
        _uiState.update { it.copy(showAboutDialog = show) }
    }

    fun refreshData() {
        _uiState.update { state ->
            state.copy(
                searchQuery = "",
                toastMessage = "Data mahasiswa telah diperbarui"
            )
        }
    }

    fun clearToastMessage() {
        _uiState.update { it.copy(toastMessage = null) }
    }
}
