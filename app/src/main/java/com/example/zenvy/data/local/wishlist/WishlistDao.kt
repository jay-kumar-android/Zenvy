package com.example.zenvy.data.local.wishlist

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * [Purpose] - Data Access Object for wishlist operations
 * Architecture Layer: Data
 * 
 * WHY: Provides database operations for wishlist items
 * Uses Flow for reactive updates
 */
@Dao
interface WishlistDao {
    
    /**
     * Get all wishlist items as Flow
     * 
     * WHY: Flow enables reactive UI updates when wishlist changes
     * 
     * @return Flow of wishlist items list
     */
    @Query("SELECT * FROM wishlist_items")
    fun getAllWishlistItems(): Flow<List<WishlistEntity>>
    
    /**
     * Insert wishlist item
     * 
     * WHY: OnConflictStrategy.REPLACE prevents duplicates
     * 
     * @param wishlistItem Wishlist item to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWishlistItem(wishlistItem: WishlistEntity)
    
    /**
     * Delete wishlist item
     * 
     * WHY: Remove product from wishlist
     * 
     * @param productId Product ID to remove
     */
    @Query("DELETE FROM wishlist_items WHERE productId = :productId")
    suspend fun deleteWishlistItem(productId: String)
    
    /**
     * Check if product is in wishlist
     * 
     * WHY: Determine wishlist icon state
     * 
     * @param productId Product ID
     * @return WishlistEntity or null
     */
    @Query("SELECT * FROM wishlist_items WHERE productId = :productId")
    suspend fun getWishlistItem(productId: String): WishlistEntity?
    
    /**
     * Clear all wishlist items
     * 
     * WHY: Clear wishlist functionality
     */
    @Query("DELETE FROM wishlist_items")
    suspend fun clearWishlist()
}
