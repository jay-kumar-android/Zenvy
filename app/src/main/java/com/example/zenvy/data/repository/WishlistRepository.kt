package com.example.zenvy.data.repository

import com.example.zenvy.data.local.wishlist.WishlistDao
import com.example.zenvy.data.local.wishlist.WishlistEntity
import com.example.zenvy.domain.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [Purpose] - Repository for wishlist operations
 * Architecture Layer: Data
 * 
 * WHY: Abstracts Room database access for wishlist functionality
 * Provides clean API for ViewModels
 * 
 * @param wishlistDao Room DAO for wishlist operations
 */
@Singleton
class WishlistRepository @Inject constructor(
    private val wishlistDao: WishlistDao
) {
    
    /**
     * Get all wishlist items as Flow
     * 
     * WHY: Reactive updates when wishlist changes
     * 
     * @return Flow of wishlist items
     */
    fun getAllWishlistItems(): Flow<List<WishlistEntity>> {
        return wishlistDao.getAllWishlistItems()
    }
    
    /**
     * Add product to wishlist
     * 
     * WHY: Save product to wishlist
     * 
     * @param product Product to add
     */
    suspend fun addToWishlist(product: Product) {
        android.util.Log.d("WishlistRepository", "addToWishlist called for: ${product.name} (ID: ${product.id})")
        val wishlistItem = WishlistEntity(
            productId = product.id,
            name = product.name,
            price = product.price,
            imageUrl = product.imageUrl,
            rating = product.rating
        )
        android.util.Log.d("WishlistRepository", "Inserting wishlist item: $wishlistItem")
        wishlistDao.insertWishlistItem(wishlistItem)
        android.util.Log.d("WishlistRepository", "addToWishlist completed successfully")
    }
    
    /**
     * Remove product from wishlist
     * 
     * WHY: Delete wishlist item
     * 
     * @param productId Product ID to remove
     */
    suspend fun removeFromWishlist(productId: String) {
        wishlistDao.deleteWishlistItem(productId)
    }
    
    /**
     * Check if product is in wishlist
     * 
     * WHY: Determine if wishlist icon should be filled
     * 
     * @param productId Product ID
     * @return true if in wishlist, false otherwise
     */
    suspend fun isInWishlist(productId: String): Boolean {
        return wishlistDao.getWishlistItem(productId) != null
    }
    
    /**
     * Clear entire wishlist
     * 
     * WHY: Empty wishlist functionality
     */
    suspend fun clearWishlist() {
        wishlistDao.clearWishlist()
    }
}
