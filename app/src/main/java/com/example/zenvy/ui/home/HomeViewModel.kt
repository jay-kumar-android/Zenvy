package com.example.zenvy.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zenvy.data.repository.ProductRepository
import com.example.zenvy.data.repository.WishlistRepository
import com.example.zenvy.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [Purpose] - ViewModel for Home screen with product listing and search
 * Architecture Layer: UI
 * 
 * WHY: Manages home screen state, product fetching, and search functionality
 * Survives configuration changes and handles business logic
 * 
 * @param productRepository Repository for product data
 */
@OptIn(FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val wishlistRepository: WishlistRepository
) : ViewModel() {
    
    // WHY: Search query state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    // WHY: Search active state
    private val _isSearchActive = MutableStateFlow(false)
    val isSearchActive: StateFlow<Boolean> = _isSearchActive.asStateFlow()
    
    // WHY: Product list state
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()
    
    // WHY: Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // WHY: Error state
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    // WHY: Track wishlist product IDs for UI state (heart icon color)
    private val _wishlistProductIds = MutableStateFlow<Set<String>>(emptySet())
    val wishlistProductIds: StateFlow<Set<String>> = _wishlistProductIds.asStateFlow()
    
    init {
        // WHY: Load products on initialization
        loadProducts()
        
        // WHY: Setup search with debouncing
        setupSearch()
        
        // WHY: Observe wishlist changes for heart icon updates
        observeWishlist()
    }
    
    /**
     * Load all products with real-time updates
     * 
     * WHY: Fetches product list from repository with live Firestore updates
     * Products will automatically update when Firestore data changes
     */
    private fun loadProducts() {
        viewModelScope.launch {
            productRepository.getAllProductsFlow().collect { result ->
                when (result) {
                    is ProductRepository.Result.Loading -> {
                        _isLoading.value = true
                        _error.value = null
                    }
                    is ProductRepository.Result.Success -> {
                        _isLoading.value = false
                        _products.value = result.data
                        _error.value = null
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
     * Observe wishlist changes
     * 
     * WHY: Track which products are in wishlist for heart icon color updates
     * Updates UI in real-time when wishlist changes
     */
    private fun observeWishlist() {
        viewModelScope.launch {
            wishlistRepository.getAllWishlistItems().collect { wishlistItems ->
                // WHY: Extract product IDs from wishlist for quick lookup
                _wishlistProductIds.value = wishlistItems.map { it.productId }.toSet()
                android.util.Log.d("HomeViewModel", "Wishlist updated: ${_wishlistProductIds.value.size} items")
            }
        }
    }
    
    /**
     * Setup search with debouncing
     * 
     * WHY: Prevents excessive Firestore queries
     * Debounces user input by 300ms
     */
    private fun setupSearch() {
        viewModelScope.launch {
            _searchQuery
                .debounce(300) // WHY: Wait 300ms after user stops typing
                .distinctUntilChanged() // WHY: Only trigger if query actually changed
                .collect { query ->
                    if (query.isBlank() && !_isSearchActive.value) {
                        // WHY: If search is not active and query is empty, show all products
                        loadProducts()
                    } else if (_isSearchActive.value && query.isNotBlank()) {
                        // WHY: Only perform search when search is active AND query is not empty
                        performSearch(query)
                    } else if (_isSearchActive.value && query.isBlank()) {
                        // WHY: Clear products when search is active but query is empty
                        _products.value = emptyList()
                        _isLoading.value = false
                        _error.value = null
                    }
                }
        }
    }
    
    /**
     * Perform product search
     * 
     * WHY: Searches products based on query
     * 
     * @param query Search query string
     */
    private fun performSearch(query: String) {
        viewModelScope.launch {
            productRepository.searchProducts(query).collect { result ->
                when (result) {
                    is ProductRepository.Result.Loading -> {
                        _isLoading.value = true
                        _error.value = null
                    }
                    is ProductRepository.Result.Success -> {
                        _isLoading.value = false
                        _products.value = result.data
                        _error.value = null
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
     * Update search query
     * 
     * WHY: Called when user types in search field
     * 
     * @param query New search query
     */
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }
    
    /**
     * Toggle search active state
     * 
     * WHY: Shows/hides search field
     */
    fun toggleSearch() {
        _isSearchActive.value = !_isSearchActive.value
        if (!_isSearchActive.value) {
            // WHY: Clear search when closing and reload products
            _searchQuery.value = ""
            loadProducts()
        } else {
            // WHY: Clear products when opening search (show empty state)
            _products.value = emptyList()
            _isLoading.value = false
            _error.value = null
        }
    }
    
    /**
     * Clear search
     * 
     * WHY: Resets search query and clears results
     */
    fun clearSearch() {
        _searchQuery.value = ""
        if (_isSearchActive.value) {
            // WHY: Clear products when clearing search query
            _products.value = emptyList()
            _isLoading.value = false
            _error.value = null
        }
    }
    
    /**
     * Retry loading products
     * 
     * WHY: Allows user to retry after error
     */
    fun retry() {
        if (_isSearchActive.value && _searchQuery.value.isNotBlank()) {
            performSearch(_searchQuery.value)
        } else {
            loadProducts()
        }
    }
    
    // WHY: Action message for user feedback (e.g., "Added to wishlist")
    private val _actionMessage = MutableStateFlow<String?>(null)
    val actionMessage: StateFlow<String?> = _actionMessage.asStateFlow()
    
    /**
     * Toggle wishlist status for a product
     * 
     * WHY: Add or remove from wishlist directly from home screen
     * 
     * @param product Product to toggle
     */
    fun toggleWishlist(product: Product) {
        viewModelScope.launch {
            try {
                // WHY: Check if product is already in wishlist
                val isInWishlist = wishlistRepository.isInWishlist(product.id)
                
                if (isInWishlist) {
                    android.util.Log.d("HomeViewModel", "Removing from wishlist: ${product.name}")
                    wishlistRepository.removeFromWishlist(product.id)
                    _actionMessage.value = "Removed from wishlist"
                } else {
                    android.util.Log.d("HomeViewModel", "Adding to wishlist: ${product.name} (ID: ${product.id})")
                    wishlistRepository.addToWishlist(product)
                    android.util.Log.d("HomeViewModel", "Successfully added to wishlist")
                    _actionMessage.value = "Added to wishlist"
                }
            } catch (e: Exception) {
                android.util.Log.e("HomeViewModel", "Failed to update wishlist", e)
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
