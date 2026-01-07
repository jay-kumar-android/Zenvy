package com.example.zenvy.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * [Purpose] - Hilt module for Firebase Firestore dependencies
 * Architecture Layer: DI
 * 
 * WHY: Provides Firestore instance for dependency injection
 */
@Module
@InstallIn(SingletonComponent::class)
object FirestoreModule {
    
    /**
     * Provides Firebase Firestore instance
     * 
     * WHY: Singleton Firestore instance for the entire app
     */
    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }
}
