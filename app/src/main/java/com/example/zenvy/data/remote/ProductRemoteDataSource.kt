package com.example.zenvy.data.remote

import com.example.zenvy.domain.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [Purpose] - Remote data source for fetching products from Firestore
 * Architecture Layer: Data
 * 
 * WHY: Abstracts Firestore access and handles data mapping
 * 
 * @param firestore Firebase Firestore instance
 */
@Singleton
class ProductRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    
    companion object {
        private const val COLLECTION_PRODUCTS = "products"
    }
    
    /**
     * Get all products with real-time updates from Firestore
     * 
     * WHY: Uses snapshot listener for live data updates
     * When products are added/updated/deleted in Firestore, UI updates instantly
     * 
     * @return Flow of product list with real-time updates
     */
    fun getAllProductsFlow(): kotlinx.coroutines.flow.Flow<List<Product>> = kotlinx.coroutines.flow.callbackFlow {
        // WHY: Setup real-time listener for Firestore collection
        val listenerRegistration = firestore.collection(COLLECTION_PRODUCTS)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // WHY: Close flow on error
                    close(error)
                    return@addSnapshotListener
                }
                
                if (snapshot != null) {
                    // WHY: Map Firestore documents to Product domain models
                    val products = snapshot.documents.mapNotNull { document ->
                        try {
                            Product(
                                id = document.id,
                                name = document.getString("name") ?: "Unknown Product",
                                description = document.getString("description") ?: "",
                                price = document.getDouble("price") ?: 0.0,
                                originalPrice = document.getDouble("discountPrice"),
                                imageUrl = sanitizeImageUrl(document.getString("imageUrl") ?: ""),
                                rating = document.getDouble("rating")?.toFloat() ?: 0f,
                                category = document.getString("category") ?: "Uncategorized",
                                colors = emptyList(),
                                sizes = emptyList(),
                                isFavorite = false
                            )
                        } catch (e: Exception) {
                            // WHY: Skip malformed documents
                            null
                        }
                    }
                    
                    // WHY: Log for debugging
                    logProductImageUrls(products)
                    
                    // WHY: Emit updated product list
                    trySend(products)
                }
            }
        
        // WHY: Remove listener when flow is cancelled
        awaitClose {
            listenerRegistration.remove()
        }
    }
            
    /**
     * Helper to log image URLs for debugging
     */
    private fun logProductImageUrls(products: List<Product>) {
        products.forEach { product ->
            android.util.Log.d("ProductImageDebug", "Product: ${product.name}, ImageUrl: '${product.imageUrl}'")
        }
    }

    /**
     * Sanitize image URL
     * 
     * WHY: Fixes common data entry errors in Firestore
     * - Removes surrounding quotes
     * - Fixes GitHub blob URLs to raw content
     */
    private fun sanitizeImageUrl(url: String): String {
        var cleanUrl = url.replace("\"", "").trim()
        
        // WHY: Filter out local file paths which cannot be loaded on device
        if (cleanUrl.startsWith("file:") || cleanUrl.startsWith("/C:/")) {
            android.util.Log.e("ProductImageDebug", "Blocked invalid local path: $cleanUrl")
            return ""
        }
        
        // WHY: Filter out search result pages which are HTML, not images
        if (cleanUrl.contains("search.yahoo.com") || cleanUrl.contains("google.com/search")) {
             android.util.Log.e("ProductImageDebug", "Blocked invalid search result link: $cleanUrl")
             return ""
        }
        
        // WHY: Convert GitHub blob URLs to raw content for direct image access
        if (cleanUrl.contains("github.com") && cleanUrl.contains("/blob/")) {
            cleanUrl = cleanUrl
                .replace("github.com", "raw.githubusercontent.com")
                .replace("/blob/", "/")
        }
        
        return cleanUrl
    }
    
    /**
     * Fetch all products from Firestore (one-time fetch)
     * 
     * WHY: Kept for backward compatibility and search functionality
     * 
     * @return List of products
     * @throws Exception if fetch fails
     */
    suspend fun getAllProducts(): List<Product> {
        return try {
            val snapshot = firestore.collection(COLLECTION_PRODUCTS)
                .get()
                .await()
            
            snapshot.documents.mapNotNull { document ->
                try {
                    // WHY: Safe mapping with null checks and default values
                    Product(
                        id = document.id,
                        name = document.getString("name") ?: "Unknown Product",
                        description = document.getString("description") ?: "",
                        price = document.getDouble("price") ?: 0.0,
                        originalPrice = document.getDouble("discountPrice"),
                        imageUrl = sanitizeImageUrl(document.getString("imageUrl") ?: ""),
                        rating = document.getDouble("rating")?.toFloat() ?: 0f,
                        category = document.getString("category") ?: "Uncategorized",
                        colors = emptyList(), // WHY: Not in Firestore schema
                        sizes = emptyList(),  // WHY: Not in Firestore schema
                        isFavorite = false    // WHY: Local state, not from Firestore
                    )
                } catch (e: Exception) {
                    // WHY: Skip malformed documents instead of crashing
                    null
                }
            }
        } catch (e: Exception) {
            // WHY: Propagate exception to be handled by repository
            throw Exception("Failed to fetch products: ${e.message}")
        }
    }
    
    /**
     * Get single product by ID
     * 
     * WHY: Fetch specific product for detail screen
     * 
     * @param productId Product ID
     * @return Product or null if not found
     */
    suspend fun getProductById(productId: String): Product? {
        return try {
            val document = firestore.collection(COLLECTION_PRODUCTS)
                .document(productId)
                .get()
                .await()
            
            if (document.exists()) {
                Product(
                    id = document.id,
                    name = document.getString("name") ?: "Unknown Product",
                    description = document.getString("description") ?: "",
                    price = document.getDouble("price") ?: 0.0,
                    originalPrice = document.getDouble("discountPrice"),
                    imageUrl = sanitizeImageUrl(document.getString("imageUrl") ?: ""),
                    rating = document.getDouble("rating")?.toFloat() ?: 0f,
                    category = document.getString("category") ?: "Uncategorized",
                    colors = emptyList(),
                    sizes = emptyList(),
                    isFavorite = false
                )
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Search products by query string
     * 
     * WHY: Enables product discovery through search
     * Uses Firestore prefix-based search with orderBy + range query
     * 
     * @param query Search query string
     * @return List of matching products
     * @throws Exception if search fails
     */
    suspend fun searchProducts(query: String): List<Product> {
        if (query.isBlank()) {
            return getAllProducts()
        }
        
        return try {
            // WHY: Firestore doesn't support full-text search natively
            // Using prefix-based search with orderBy + range query
            val queryLower = query.lowercase().trim()
            val queryEnd = queryLower + '\uf8ff' // WHY: Unicode character for range end
            
            val snapshot = firestore.collection(COLLECTION_PRODUCTS)
                .orderBy("nameLower") // WHY: Requires nameLower field in Firestore
                .startAt(queryLower)
                .endAt(queryEnd)
                .get()
                .await()
            
            val results = snapshot.documents.mapNotNull { document ->
                try {
                    Product(
                        id = document.id,
                        name = document.getString("name") ?: "Unknown Product",
                        description = document.getString("description") ?: "",
                        price = document.getDouble("price") ?: 0.0,
                        originalPrice = document.getDouble("discountPrice"),
                        imageUrl = sanitizeImageUrl(document.getString("imageUrl") ?: ""),
                        rating = document.getDouble("rating")?.toFloat() ?: 0f,
                        category = document.getString("category") ?: "Uncategorized",
                        colors = emptyList(),
                        sizes = emptyList(),
                        isFavorite = false
                    )
                } catch (e: Exception) {
                    null
                }
            }
            
            // WHY: Fallback to client-side filtering if nameLower field doesn't exist
            if (results.isEmpty()) {
                getAllProducts().filter { product ->
                    product.name.contains(query, ignoreCase = true) ||
                    product.category.contains(query, ignoreCase = true) ||
                    product.description.contains(query, ignoreCase = true)
                }
            } else {
                results
            }
        } catch (e: Exception) {
            // WHY: If Firestore query fails, fallback to client-side search
            getAllProducts().filter { product ->
                product.name.contains(query, ignoreCase = true) ||
                product.category.contains(query, ignoreCase = true) ||
                product.description.contains(query, ignoreCase = true)
            }
        }
    }
}
