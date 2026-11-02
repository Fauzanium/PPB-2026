package me.fauzanium.tugas10loginmvvm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.fauzanium.tugas10loginmvvm.data.local.entity.User
import me.fauzanium.tugas10loginmvvm.data.repository.UserRepository

sealed interface LoginState {
    object Idle : LoginState
    data class Success(val message: String, val username: String) : LoginState
    data class Error(val message: String) : LoginState
}

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            if (repository.login("admin", "12345") == null) {
                repository.insertUser(User(username = "admin", password = "12345"))
            }
            if (repository.login("Fauzan", "12345") == null) {
                repository.insertUser(User(username = "Fauzan", password = "12345"))
            }
        }
    }

    fun onUsernameChange(newUsername: String) {
        _username.value = newUsername
        if (_loginState.value !is LoginState.Idle) {
            _loginState.value = LoginState.Idle
        }
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
        if (_loginState.value !is LoginState.Idle) {
            _loginState.value = LoginState.Idle
        }
    }

    fun login() {
        val currentUsername = _username.value.trim()
        val currentPassword = _password.value.trim()

        viewModelScope.launch {
            val user = repository.login(currentUsername, currentPassword)
            if (user != null) {
                _loginState.value = LoginState.Success(
                    message = "Login Berhasil! Selamat datang, ${user.username}",
                    username = user.username
                )
            } else {
                _loginState.value = LoginState.Error("Username atau Password Salah! Coba lagi")
            }
        }
    }
}
