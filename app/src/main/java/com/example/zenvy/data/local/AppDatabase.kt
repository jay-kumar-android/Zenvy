package com.example.zenvy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.zenvy.data.local.cart.CartDao
import com.example.zenvy.data.local.cart.CartEntity
import com.example.zenvy.data.local.wishlist.WishlistDao
import com.example.zenvy.data.local.wishlist.WishlistEntity

/**
 * [Purpose] - Room database for Zenvy app
 * Architecture Layer: Data
 * 
 * WHY: Provides local storage for cart and wishlist
 * Enables offline functionality
 * 
 * Entities: CartEntity, WishlistEntity
 * DAOs: CartDao, WishlistDao
 */
@Database(
    entities = [CartEntity::class, WishlistEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    /**
     * Get CartDao instance
     * 
     * WHY: Access cart database operations
     */
    abstract fun cartDao(): CartDao
    
    /**
     * Get WishlistDao instance
     * 
     * WHY: Access wishlist database operations
     */
    abstract fun wishlistDao(): WishlistDao
}
