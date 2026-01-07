package com.example.zenvy.data.repository

import com.example.zenvy.data.local.cart.CartDao
import com.example.zenvy.data.local.cart.CartEntity
import com.example.zenvy.domain.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [Purpose] - Repository for cart operations
 * Architecture Layer: Data
 * 
 * WHY: Abstracts Room database access for cart functionality
 * Provides clean API for ViewModels
 * 
 * @param cartDao Room DAO for cart operations
 */
@Singleton
class CartRepository @Inject constructor(
    private val cartDao: CartDao
) {
    
    /**
     * Get all cart items as Flow
     * 
     * WHY: Reactive updates when cart changes
     * 
     * @return Flow of cart items
     */
    fun getAllCartItems(): Flow<List<CartEntity>> {
        return cartDao.getAllCartItems()
    }
    
    /**
     * Add product to cart
     * 
     * WHY: If product exists, increase quantity
     * If new, add with quantity 1
     * 
     * @param product Product to add
     */
    suspend fun addToCart(product: Product) {
        android.util.Log.d("CartRepository", "addToCart called for: ${product.name} (ID: ${product.id})")
        val existingItem = cartDao.getCartItem(product.id)
        
        if (existingItem != null) {
            // WHY: Increase quantity if already in cart
            val newQuantity = existingItem.quantity + 1
            android.util.Log.d("CartRepository", "Item exists, updating quantity from ${existingItem.quantity} to $newQuantity")
            cartDao.updateQuantity(product.id, newQuantity)
        } else {
            // WHY: Add new item to cart
            val cartItem = CartEntity(
                productId = product.id,
                name = product.name,
                price = product.price,
                imageUrl = product.imageUrl,
                quantity = 1
            )
            android.util.Log.d("CartRepository", "Adding new item to cart: ${cartItem}")
            cartDao.insertCartItem(cartItem)
        }
        android.util.Log.d("CartRepository", "addToCart completed successfully")
    }
    
    /**
     * Update cart item quantity
     * 
     * WHY: Allows user to change quantity
     * Removes item if quantity is 0
     * 
     * @param productId Product ID
     * @param quantity New quantity
     */
    suspend fun updateQuantity(productId: String, quantity: Int) {
        if (quantity <= 0) {
            // WHY: Remove item if quantity is 0
            cartDao.deleteCartItem(productId)
        } else {
            cartDao.updateQuantity(productId, quantity)
        }
    }
    
    /**
     * Remove item from cart
     * 
     * WHY: Delete cart item
     * 
     * @param productId Product ID to remove
     */
    suspend fun removeFromCart(productId: String) {
        cartDao.deleteCartItem(productId)
    }
    
    /**
     * Clear entire cart
     * 
     * WHY: Empty cart functionality
     */
    suspend fun clearCart() {
        cartDao.clearCart()
    }
}
