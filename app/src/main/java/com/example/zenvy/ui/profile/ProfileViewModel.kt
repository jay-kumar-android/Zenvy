package com.example.zenvy.ui.profile

import androidx.lifecycle.ViewModel
import com.example.zenvy.data.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * [Purpose] - ViewModel for Profile screen
 * Architecture Layer: UI
 * 
 * WHY: Manages user profile state and provides user data from Firebase Auth
 * 
 * @param authRepository Repository for auth operations
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    // WHY: Private mutable state, public immutable state
    private val _userEmail = MutableStateFlow<String?>(null)
    val userEmail: StateFlow<String?> = _userEmail.asStateFlow()
    
    private val _userName = MutableStateFlow<String?>(null)
    val userName: StateFlow<String?> = _userName.asStateFlow()
    
    init {
        // WHY: Load user data when ViewModel is created
        loadUserData()
    }
    
    /**
     * Load current user data from Firebase Auth
     * 
     * WHY: Fetches email and display name from Firebase Auth
     * Display name is set during registration
     */
    private fun loadUserData() {
        val currentUser = authRepository.getCurrentUser()
        currentUser?.let { user ->
            _userEmail.value = user.email
            
            // WHY: Use displayName from Firebase Auth (set during registration)
            _userName.value = user.displayName ?: "User"
        }
    }
    
    /**
     * Refresh user data
     * 
     * WHY: Allows manual refresh of user data if needed
     */
    fun refreshUserData() {
        loadUserData()
    }
}
