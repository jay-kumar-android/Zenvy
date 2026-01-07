package com.example.zenvy.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zenvy.data.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [Purpose] - ViewModel for authentication operations
 * Architecture Layer: UI
 * 
 * WHY: Manages auth state and coordinates auth operations
 * Survives configuration changes
 * 
 * @param authRepository Repository for auth operations
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    // WHY: Private mutable state, public immutable state
    private val _authState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val authState: StateFlow<AuthUiState> = _authState.asStateFlow()
    
    init {
        // WHY: Check auth state on ViewModel creation
        checkAuthState()
    }
    
    /**
     * Check current authentication state
     * 
     * WHY: Determines if user is already logged in
     * Used on app start to navigate correctly
     */
    fun checkAuthState() {
        val currentUser = authRepository.getCurrentUser()
        _authState.value = if (currentUser != null) {
            AuthUiState.Authenticated
        } else {
            AuthUiState.Unauthenticated
        }
    }
    
    /**
     * Login user with email and password
     * 
     * WHY: Validates input, calls repository, updates state
     * 
     * @param email User email
     * @param password User password
     */
    fun login(email: String, password: String) {
        // Validate inputs
        if (email.isBlank()) {
            _authState.value = AuthUiState.Error("Email cannot be empty")
            return
        }
        
        if (password.isBlank()) {
            _authState.value = AuthUiState.Error("Password cannot be empty")
            return
        }
        
        if (password.length < 6) {
            _authState.value = AuthUiState.Error("Password must be at least 6 characters")
            return
        }
        
        // WHY: Launch coroutine in viewModelScope for automatic cancellation
        viewModelScope.launch {
            try {
                _authState.value = AuthUiState.Loading
                authRepository.login(email, password)
                _authState.value = AuthUiState.Authenticated
            } catch (e: Exception) {
                _authState.value = AuthUiState.Error(e.message ?: "Login failed")
            }
        }
    }
    
    /**
     * Register new user with email and password
     * 
     * WHY: Validates input, calls repository, updates state
     * 
     * @param fullName User's full name
     * @param email User email
     * @param password User password
     * @param confirmPassword Password confirmation
     */
    fun register(fullName: String, email: String, password: String, confirmPassword: String) {
        // Validate inputs
        if (fullName.isBlank()) {
            _authState.value = AuthUiState.Error("Name cannot be empty")
            return
        }
        
        if (email.isBlank()) {
            _authState.value = AuthUiState.Error("Email cannot be empty")
            return
        }
        
        if (password.isBlank()) {
            _authState.value = AuthUiState.Error("Password cannot be empty")
            return
        }
        
        if (password.length < 6) {
            _authState.value = AuthUiState.Error("Password must be at least 6 characters")
            return
        }
        
        if (password != confirmPassword) {
            _authState.value = AuthUiState.Error("Passwords do not match")
            return
        }
        
        // WHY: Launch coroutine in viewModelScope
        viewModelScope.launch {
            try {
                _authState.value = AuthUiState.Loading
                authRepository.register(fullName, email, password)
                _authState.value = AuthUiState.Authenticated
            } catch (e: Exception) {
                _authState.value = AuthUiState.Error(e.message ?: "Registration failed")
            }
        }
    }
    
    /**
     * Logout current user
     * 
     * WHY: Signs out user and updates state to Unauthenticated
     */
    fun logout() {
        authRepository.logout()
        _authState.value = AuthUiState.Unauthenticated
    }
    
    /**
     * Send password reset email
     * 
     * WHY: Validates email and sends password reset link via Firebase
     * 
     * @param email User email address
     */
    fun resetPassword(email: String) {
        // Validate email
        if (email.isBlank()) {
            _authState.value = AuthUiState.Error("Email cannot be empty")
            return
        }
        
        // WHY: Launch coroutine in viewModelScope
        viewModelScope.launch {
            try {
                _authState.value = AuthUiState.Loading
                authRepository.resetPassword(email)
                _authState.value = AuthUiState.PasswordResetSent
            } catch (e: Exception) {
                _authState.value = AuthUiState.Error(e.message ?: "Failed to send reset email")
            }
        }
    }
    
    /**
     * Reset auth state to Idle
     * 
     * WHY: Clears error messages after they've been shown
     */
    fun resetState() {
        _authState.value = AuthUiState.Idle
    }
}
