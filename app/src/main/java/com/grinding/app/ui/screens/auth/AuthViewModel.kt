package com.grinding.app.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grinding.app.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val isAuthenticated: Boolean = false,
    val currentUserName: String = "Pengguna",
    val currentUserEmail: String = ""
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        observeSession()
    }

    private fun observeSession() {
        viewModelScope.launch {
            authRepository.sessionStatus.collect { status ->
                when (status) {
                    is SessionStatus.Authenticated -> {
                        _uiState.update {
                            it.copy(
                                isAuthenticated = true,
                                isLoading = false,
                                currentUserName = authRepository.getCurrentUserName() ?: "Pengguna",
                                currentUserEmail = authRepository.getCurrentUserEmail() ?: ""
                            )
                        }
                    }
                    is SessionStatus.NotAuthenticated, is SessionStatus.RefreshFailure -> {
                        _uiState.update {
                            it.copy(
                                isAuthenticated = false,
                                isLoading = false,
                                currentUserName = "Pengguna",
                                currentUserEmail = ""
                            )
                        }
                    }
                    is SessionStatus.Initializing -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }

    fun signIn(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Email dan kata sandi tidak boleh kosong.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            val result = authRepository.signIn(email.trim(), password)
            result.onSuccess {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        currentUserName = authRepository.getCurrentUserName() ?: "Pengguna",
                        currentUserEmail = authRepository.getCurrentUserEmail() ?: email.trim()
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.localizedMessage ?: "Gagal masuk. Periksa kembali email dan kata sandi."
                    )
                }
            }
        }
    }

    fun signUp(email: String, password: String, fullName: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Email dan kata sandi tidak boleh kosong.") }
            return
        }
        if (password.length < 6) {
            _uiState.update { it.copy(errorMessage = "Kata sandi minimal 6 karakter.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            val result = authRepository.signUp(email.trim(), password, fullName.trim())
            result.onSuccess {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        successMessage = "Pendaftaran berhasil! Silakan masuk dengan akun baru Anda."
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.localizedMessage ?: "Gagal mendaftar. Silakan coba kembali."
                    )
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            authRepository.signOut()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isAuthenticated = false,
                    currentUserName = "Pengguna",
                    currentUserEmail = ""
                )
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }
}
