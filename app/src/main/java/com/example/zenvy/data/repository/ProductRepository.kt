package com.example.zenvy.data.repository

import com.example.zenvy.data.remote.ProductRemoteDataSource
import com.example.zenvy.domain.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [Purpose] - Repository for product data
 * Architecture Layer: Data
 * 
 * WHY: Abstracts data source and provides clean API for ViewModels
 * Handles data fetching and error states
 * 
 * @param remoteDataSource Firestore data source
 */
@Singleton
class ProductRepository @Inject constructor(
    private val remoteDataSource: ProductRemoteDataSource
) {
    
    /**
     * Get all products with real-time updates from Firestore
     * 
     * WHY: Provides Flow for reactive real-time data updates
     * Wraps Firestore snapshot listener in Result wrapper for state management
     * 
     * @return Flow of Result with product list (live updates)
     */
    fun getAllProductsFlow(): Flow<Result<List<Product>>> = flow {
        try {
            // WHY: Emit loading state
            emit(Result.Loading)
            
            // WHY: Collect from real-time Firestore flow
            remoteDataSource.getAllProductsFlow().collect { products ->
                // WHY: Emit success with live data
                emit(Result.Success(products))
            }
        } catch (e: Exception) {
            // WHY: Emit error with message
            emit(Result.Error(e.message ?: "Failed to fetch products"))
        }
    }
    
    /**
     * Get all products from Firestore (one-time fetch)
     * 
     * WHY: Provides Flow for reactive data updates
     * Emits loading, success, and error states
     * 
     * @return Flow of Result with product list
     */
    fun getAllProducts(): Flow<Result<List<Product>>> = flow {
        try {
            // WHY: Emit loading state
            emit(Result.Loading)
            
            // WHY: Fetch from Firestore
            val products = remoteDataSource.getAllProducts()
            
            // WHY: Emit success with data
            emit(Result.Success(products))
        } catch (e: Exception) {
            // WHY: Emit error with message
            emit(Result.Error(e.message ?: "Failed to fetch products"))
        }
    }
    
    /**
     * Get product by ID
     * 
     * WHY: Fetch specific product for detail screen
     * 
     * @param productId Product ID
     * @return Flow of Result with product
     */
    fun getProductById(productId: String): Flow<Result<Product?>> = flow {
        try {
            // WHY: Emit loading state
            emit(Result.Loading)
            
            // WHY: Fetch from Firestore
            val product = remoteDataSource.getProductById(productId)
            
            // WHY: Emit success with data
            emit(Result.Success(product))
        } catch (e: Exception) {
            // WHY: Emit error with message
            emit(Result.Error(e.message ?: "Failed to fetch product"))
        }
    }
    
    /**
     * Search products by query
     * 
     * WHY: Enables product search with reactive Flow
     * Debouncing should be handled in ViewModel
     * 
     * @param query Search query string
     * @return Flow of Result with matching products
     */
    fun searchProducts(query: String): Flow<Result<List<Product>>> = flow {
        try {
            // WHY: Emit loading state
            emit(Result.Loading)
            
            // WHY: Search from Firestore
            val products = remoteDataSource.searchProducts(query)
            
            // WHY: Emit success with data
            emit(Result.Success(products))
        } catch (e: Exception) {
            // WHY: Emit error with message
            emit(Result.Error(e.message ?: "Search failed"))
        }
    }
    
    /**
     * Result sealed class for state management
     * 
     * WHY: Type-safe way to represent loading, success, and error states
     */
    sealed class Result<out T> {
        data object Loading : Result<Nothing>()
        data class Success<T>(val data: T) : Result<T>()
        data class Error(val message: String) : Result<Nothing>()
    }
}
