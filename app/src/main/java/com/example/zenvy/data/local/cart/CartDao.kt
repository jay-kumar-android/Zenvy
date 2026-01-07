package com.example.zenvy.data.local.cart

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * [Purpose] - Data Access Object for cart operations
 * Architecture Layer: Data
 * 
 * WHY: Provides database operations for cart items
 * Uses Flow for reactive updates
 */
@Dao
interface CartDao {
    
    /**
     * Get all cart items as Flow
     * 
     * WHY: Flow enables reactive UI updates when cart changes
     * 
     * @return Flow of cart items list
     */
    @Query("SELECT * FROM cart_items")
    fun getAllCartItems(): Flow<List<CartEntity>>
    
    /**
     * Insert or update cart item
     * 
     * WHY: OnConflictStrategy.REPLACE updates existing items
     * Used when adding product to cart
     * 
     * @param cartItem Cart item to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartEntity)
    
    /**
     * Update cart item quantity
     * 
     * WHY: Allows incrementing/decrementing quantity
     * 
     * @param productId Product ID
     * @param quantity New quantity
     */
    @Query("UPDATE cart_items SET quantity = :quantity WHERE productId = :productId")
    suspend fun updateQuantity(productId: String, quantity: Int)
    
    /**
     * Delete cart item
     * 
     * WHY: Remove product from cart
     * 
     * @param productId Product ID to remove
     */
    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun deleteCartItem(productId: String)
    
    /**
     * Clear all cart items
     * 
     * WHY: Empty cart functionality
     */
    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
    
    /**
     * Get cart item by product ID
     * 
     * WHY: Check if product is already in cart
     * 
     * @param productId Product ID
     * @return CartEntity or null
     */
    @Query("SELECT * FROM cart_items WHERE productId = :productId")
    suspend fun getCartItem(productId: String): CartEntity?
}
