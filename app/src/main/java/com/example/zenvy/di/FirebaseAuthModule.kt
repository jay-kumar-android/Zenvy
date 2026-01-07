package com.example.zenvy.di

import com.example.zenvy.data.auth.AuthRepository
import com.example.zenvy.data.auth.AuthRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * [Purpose] - Hilt module for Firebase Authentication dependencies
 * Architecture Layer: DI
 * 
 * WHY: Provides Firebase Auth instance and AuthRepository for dependency injection
 */
@Module
@InstallIn(SingletonComponent::class)
object FirebaseAuthModule {
    
    /**
     * Provides Firebase Authentication instance
     * 
     * WHY: Singleton instance of FirebaseAuth for the entire app
     */
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
    
    /**
     * Provides AuthRepository implementation
     * 
     * WHY: Binds AuthRepositoryImpl to AuthRepository interface
     * Enables easy testing and swapping implementations
     */
    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth
    ): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth)
    }
}
