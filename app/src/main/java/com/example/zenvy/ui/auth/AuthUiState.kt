package com.example.zenvy.ui.auth

/**
 * [Purpose] - Sealed class representing authentication UI states
 * Architecture Layer: UI
 * 
 * WHY: Type-safe state management for authentication flow
 */
sealed class AuthUiState {
    /**
     * Initial state before any auth action
     */
    data object Idle : AuthUiState()
    
    /**
     * Loading state during auth operations
     */
    data object Loading : AuthUiState()
    
    /**
     * User is authenticated
     */
    data object Authenticated : AuthUiState()
    
    /**
     * User is not authenticated
     */
    data object Unauthenticated : AuthUiState()
    
    /**
     * Password reset email sent successfully
     */
    data object PasswordResetSent : AuthUiState()
    
    /**
     * Error occurred during auth operation
     * @param message User-friendly error message
     */
    data class Error(val message: String) : AuthUiState()
}
