package com.ecoquest.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ecoquest.app.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AuthState(
    val user: Any? = null,
    val isTestUser: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepository = AuthRepository(application.applicationContext)
    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun adminLogin(email: String, pass: String) {
        if (email == "admin" && pass == "admin") {
            _authState.value = _authState.value.copy(isTestUser = true, error = null)
        } else {
            _authState.value = _authState.value.copy(error = "Use admin / admin to login")
        }
    }

    fun signOut() {
        authRepository.signOut()
        _authState.value = AuthState()
    }
}
