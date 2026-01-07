package com.example.zenvy.data.auth

import com.google.firebase.auth.FirebaseUser

/**
 * [Purpose] - Repository interface for authentication operations
 * Architecture Layer: Domain
 * 
 * WHY: Abstraction layer for auth data source, enables testing and flexibility
 */
interface AuthRepository {
    /**
     * Login user with email and password
     * @param email User email
     * @param password User password
     * @return FirebaseUser on success
     * @throws Exception on failure
     */
    suspend fun login(email: String, password: String): FirebaseUser
    
    /**
     * Register new user with email and password
     * @param fullName User's full name
     * @param email User email
     * @param password User password
     * @return FirebaseUser on success
     * @throws Exception on failure
     */
    suspend fun register(fullName: String, email: String, password: String): FirebaseUser
    
    /**
     * Logout current user
     */
    fun logout()
    
    /**
     * Get currently authenticated user
     * @return FirebaseUser if authenticated, null otherwise
     */
    fun getCurrentUser(): FirebaseUser?
    
    /**
     * Send password reset email to user
     * @param email User email address
     * @throws Exception on failure
     */
    suspend fun resetPassword(email: String)
}
