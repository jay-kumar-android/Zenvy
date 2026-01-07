package com.example.zenvy.di

import android.content.Context
import androidx.room.Room
import com.example.zenvy.data.local.AppDatabase
import com.example.zenvy.data.local.cart.CartDao
import com.example.zenvy.data.local.wishlist.WishlistDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * [Purpose] - Hilt module for Room database dependencies
 * Architecture Layer: DI
 * 
 * WHY: Provides Room database and DAOs for dependency injection
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    /**
     * Provides Room database instance
     * 
     * WHY: Singleton database for the entire app
     * Uses fallbackToDestructiveMigration for portfolio project
     */
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "zenvy_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    
    /**
     * Provides CartDao instance
     * 
     * WHY: Access cart database operations
     */
    @Provides
    @Singleton
    fun provideCartDao(database: AppDatabase): CartDao {
        return database.cartDao()
    }
    
    /**
     * Provides WishlistDao instance
     * 
     * WHY: Access wishlist database operations
     */
    @Provides
    @Singleton
    fun provideWishlistDao(database: AppDatabase): WishlistDao {
        return database.wishlistDao()
    }
}
