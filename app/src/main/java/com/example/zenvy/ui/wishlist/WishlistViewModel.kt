package com.example.zenvy.ui.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zenvy.data.local.wishlist.WishlistEntity
import com.example.zenvy.data.repository.WishlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [Purpose] - ViewModel for Wishlist screen
 * Architecture Layer: UI
 * 
 * WHY: Manages wishlist items and operations
 * 
 * @param wishlistRepository Repository for wishlist operations
 */
@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val wishlistRepository: WishlistRepository
) : ViewModel() {
    
    // WHY: Observe wishlist items from Room database
    private val _wishlistItems = MutableStateFlow<List<WishlistEntity>>(emptyList())
    val wishlistItems: StateFlow<List<WishlistEntity>> = _wishlistItems.asStateFlow()
    
    init {
        // WHY: Observe wishlist items
        observeWishlistItems()
    }
    
    /**
     * Observe wishlist items from database
     * 
     * WHY: Reactive updates when wishlist changes
     */
    private fun observeWishlistItems() {
        viewModelScope.launch {
            wishlistRepository.getAllWishlistItems().collect { items ->
                _wishlistItems.value = items
            }
        }
    }
    
    /**
     * Remove item from wishlist
     * 
     * WHY: Delete wishlist item
     * 
     * @param productId Product ID to remove
     */
    fun removeItem(productId: String) {
        viewModelScope.launch {
            wishlistRepository.removeFromWishlist(productId)
        }
    }
    
    /**
     * Clear entire wishlist
     * 
     * WHY: Empty wishlist functionality
     */
    fun clearWishlist() {
        viewModelScope.launch {
            wishlistRepository.clearWishlist()
        }
    }
}
