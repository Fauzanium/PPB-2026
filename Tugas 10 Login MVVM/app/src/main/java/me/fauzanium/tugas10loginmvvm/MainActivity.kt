package me.fauzanium.tugas10loginmvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import me.fauzanium.tugas10loginmvvm.data.local.database.AppDatabase
import me.fauzanium.tugas10loginmvvm.data.repository.UserRepository
import me.fauzanium.tugas10loginmvvm.ui.screen.LoginScreen
import me.fauzanium.tugas10loginmvvm.ui.theme.Tugas10LoginMVVMTheme
import me.fauzanium.tugas10loginmvvm.viewmodel.LoginViewModel
import me.fauzanium.tugas10loginmvvm.viewmodel.LoginViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(applicationContext)
        val userRepository = UserRepository(database.userDao())
        val factory = LoginViewModelFactory(userRepository)

        setContent {
            Tugas10LoginMVVMTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val viewModel: LoginViewModel = viewModel(factory = factory)
                    LoginScreen(viewModel = viewModel)
                }
            }
        }
    }
}
