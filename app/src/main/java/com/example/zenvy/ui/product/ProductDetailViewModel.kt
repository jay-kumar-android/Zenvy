package com.example.zenvy.ui.product

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zenvy.data.repository.CartRepository
import com.example.zenvy.data.repository.ProductRepository
import com.example.zenvy.data.repository.WishlistRepository
import com.example.zenvy.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [Purpose] - ViewModel for Product Detail screen
 * Architecture Layer: UI
 * 
 * WHY: Manages product data fetching, cart, and wishlist operations
 * 
 * @param savedStateHandle For retrieving navigation arguments
 * @param productRepository Repository for product data
 * @param cartRepository Repository for cart operations
 * @param wishlistRepository Repository for wishlist operations
 */
@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    private val wishlistRepository: WishlistRepository
) : ViewModel() {
    
    // WHY: Get productId from navigation arguments
    private val productId: String = checkNotNull(savedStateHandle["productId"])
    
    // WHY: Product state
    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product.asStateFlow()
    
    // WHY: Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // WHY: Error state
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    // WHY: Track if product is in wishlist
    private val _isInWishlist = MutableStateFlow(false)
    val isInWishlist: StateFlow<Boolean> = _isInWishlist.asStateFlow()
    
    // WHY: Track action feedback messages
    private val _actionMessage = MutableStateFlow<String?>(null)
    val actionMessage: StateFlow<String?> = _actionMessage.asStateFlow()
    
    init {
        // WHY: Load product on initialization
        loadProduct()
    }
    
    /**
     * Load product by ID from Firestore
     * 
     * WHY: Fetch real product data instead of using dummy data
     */
    private fun loadProduct() {
        viewModelScope.launch {
            productRepository.getProductById(productId).collect { result ->
                when (result) {
                    is ProductRepository.Result.Loading -> {
                        _isLoading.value = true
                        _error.value = null
                    }
                    is ProductRepository.Result.Success -> {
                        _isLoading.value = false
                        _product.value = result.data
                        _error.value = null
                        
                        // WHY: Check wishlist status after product loads
                        result.data?.let { checkWishlistStatus(it.id) }
                    }
                    is ProductRepository.Result.Error -> {
                        _isLoading.value = false
                        _error.value = result.message
                    }
                }
            }
        }
    }
    
    /**
     * Check if product is in wishlist
     * 
     * WHY: Update wishlist icon state
     * 
     * @param productId Product ID to check
     */
    fun checkWishlistStatus(productId: String) {
        viewModelScope.launch {
            _isInWishlist.value = wishlistRepository.isInWishlist(productId)
        }
    }
    
    /**
     * Add product to cart
     * 
     * WHY: Adds product to local Room database
     * Shows success message
     * 
     * @param product Product to add
     */
    fun addToCart(product: Product) {
        viewModelScope.launch {
            try {
                Log.d("ProductDetailVM", "Adding to cart: ${product.name} (ID: ${product.id})")
                cartRepository.addToCart(product)
                Log.d("ProductDetailVM", "Successfully added to cart")
                _actionMessage.value = "Added to cart"
            } catch (e: Exception) {
                Log.e("ProductDetailVM", "Failed to add to cart", e)
                _actionMessage.value = "Failed to add to cart"
            }
        }
    }
    
    /**
     * Toggle wishlist status
     * 
     * WHY: Add or remove from wishlist based on current state
     * 
     * @param product Product to toggle
     */
    fun toggleWishlist(product: Product) {
        viewModelScope.launch {
            try {
                if (_isInWishlist.value) {
                    Log.d("ProductDetailVM", "Removing from wishlist: ${product.name}")
                    wishlistRepository.removeFromWishlist(product.id)
                    _isInWishlist.value = false
                    _actionMessage.value = "Removed from wishlist"
                } else {
                    Log.d("ProductDetailVM", "Adding to wishlist: ${product.name} (ID: ${product.id})")
                    wishlistRepository.addToWishlist(product)
                    _isInWishlist.value = true
                    Log.d("ProductDetailVM", "Successfully added to wishlist")
                    _actionMessage.value = "Added to wishlist"
                }
            } catch (e: Exception) {
                Log.e("ProductDetailVM", "Failed to update wishlist", e)
                _actionMessage.value = "Failed to update wishlist"
            }
        }
    }
    
    /**
     * Clear action message
     * 
     * WHY: Reset message after showing to user
     */
    fun clearMessage() {
        _actionMessage.value = null
    }
}
