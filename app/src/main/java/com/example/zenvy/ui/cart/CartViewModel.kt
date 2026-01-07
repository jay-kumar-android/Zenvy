package com.example.zenvy.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zenvy.data.local.cart.CartEntity
import com.example.zenvy.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [Purpose] - ViewModel for Cart screen
 * Architecture Layer: UI
 * 
 * WHY: Manages cart items and operations
 * 
 * @param cartRepository Repository for cart operations
 */
@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {
    
    // WHY: Observe cart items from Room database
    private val _cartItems = MutableStateFlow<List<CartEntity>>(emptyList())
    val cartItems: StateFlow<List<CartEntity>> = _cartItems.asStateFlow()
    
    // WHY: Calculate total price
    private val _totalPrice = MutableStateFlow(0.0)
    val totalPrice: StateFlow<Double> = _totalPrice.asStateFlow()
    
    init {
        // WHY: Observe cart items and calculate total
        observeCartItems()
    }
    
    /**
     * Observe cart items from database
     * 
     * WHY: Reactive updates when cart changes
     */
    private fun observeCartItems() {
        viewModelScope.launch {
            cartRepository.getAllCartItems().collect { items ->
                _cartItems.value = items
                // WHY: Calculate total price
                _totalPrice.value = items.sumOf { it.price * it.quantity }
            }
        }
    }
    
    /**
     * Increase item quantity
     * 
     * WHY: Increment quantity by 1
     * 
     * @param productId Product ID
     */
    fun increaseQuantity(productId: String) {
        viewModelScope.launch {
            val item = _cartItems.value.find { it.productId == productId }
            item?.let {
                cartRepository.updateQuantity(productId, it.quantity + 1)
            }
        }
    }
    
    /**
     * Decrease item quantity
     * 
     * WHY: Decrement quantity by 1
     * Removes item if quantity becomes 0
     * 
     * @param productId Product ID
     */
    fun decreaseQuantity(productId: String) {
        viewModelScope.launch {
            val item = _cartItems.value.find { it.productId == productId }
            item?.let {
                cartRepository.updateQuantity(productId, it.quantity - 1)
            }
        }
    }
    
    /**
     * Remove item from cart
     * 
     * WHY: Delete cart item
     * 
     * @param productId Product ID to remove
     */
    fun removeItem(productId: String) {
        viewModelScope.launch {
            cartRepository.removeFromCart(productId)
        }
    }
    
    /**
     * Clear entire cart
     * 
     * WHY: Empty cart functionality
     */
    fun clearCart() {
        viewModelScope.launch {
            cartRepository.clearCart()
        }
    }
}
