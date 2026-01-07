package com.example.zenvy.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [Purpose] - Implementation of AuthRepository using Firebase Authentication
 * Architecture Layer: Data
 * 
 * WHY: Handles all Firebase Auth operations with proper error handling
 * 
 * @param firebaseAuth Firebase Authentication instance
 */
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {
    
    /**
     * Login user with email and password
     * 
     * WHY: Uses suspend function with await() for coroutine support
     * Throws exception with meaningful message on failure
     */
    override suspend fun login(email: String, password: String): FirebaseUser {
        try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            return result.user ?: throw Exception("Login failed: User is null")
        } catch (e: Exception) {
            throw mapFirebaseException(e)
        }
    }
    
    /**
     * Register new user with email and password
     * 
     * WHY: Creates new Firebase user account and sets display name
     * Automatically signs in user after registration
     * 
     * @param fullName User's full name to set as display name
     * @param email User email
     * @param password User password
     */
    override suspend fun register(fullName: String, email: String, password: String): FirebaseUser {
        try {
            // WHY: Create user account with email and password
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user ?: throw Exception("Registration failed: User is null")
            
            // WHY: Update user profile with display name
            val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder()
                .setDisplayName(fullName)
                .build()
            
            user.updateProfile(profileUpdates).await()
            
            return user
        } catch (e: Exception) {
            throw mapFirebaseException(e)
        }
    }
    
    /**
     * Logout current user
     * 
     * WHY: Signs out user from Firebase Auth
     */
    override fun logout() {
        firebaseAuth.signOut()
    }
    
    /**
     * Get currently authenticated user
     * 
     * WHY: Returns current Firebase user or null if not authenticated
     */
    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }
    
    /**
     * Send password reset email to user
     * 
     * WHY: Uses Firebase sendPasswordResetEmail to send reset link
     * User receives email with link to reset their password
     */
    override suspend fun resetPassword(email: String) {
        try {
            firebaseAuth.sendPasswordResetEmail(email).await()
        } catch (e: Exception) {
            throw mapFirebaseException(e)
        }
    }
    
    /**
     * Map Firebase exceptions to user-friendly messages
     * 
     * WHY: Provides clear error messages instead of technical Firebase errors
     * 
     * @param exception Firebase exception
     * @return Exception with user-friendly message
     */
    private fun mapFirebaseException(exception: Exception): Exception {
        val message = when {
            exception.message?.contains("There is no user record") == true ->
                "No account found with this email"
            
            exception.message?.contains("The password is invalid") == true ->
                "Incorrect password"
            
            exception.message?.contains("The email address is badly formatted") == true ->
                "Invalid email format"
            
            exception.message?.contains("The email address is already in use") == true ->
                "Email already registered"
            
            exception.message?.contains("The given password is invalid") == true ->
                "Password must be at least 6 characters"
            
            exception.message?.contains("A network error") == true ->
                "Network error. Please check your connection"
            
            exception.message?.contains("too many requests") == true ->
                "Too many attempts. Please try again later"
            
            else -> exception.message ?: "Authentication failed. Please try again"
        }
        
        return Exception(message)
    }
}
