package com.example.zenvy.data.local.wishlist

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * [Purpose] - Room entity for wishlist items
 * Architecture Layer: Data
 * 
 * WHY: Represents wishlist items stored locally in Room database
 * Enables offline wishlist functionality
 * 
 * @param productId Unique product identifier (Primary Key)
 * @param name Product name
 * @param price Product price
 * @param imageUrl Product image URL
 * @param rating Product rating
 */
@Entity(tableName = "wishlist_items")
data class WishlistEntity(
    @PrimaryKey
    val productId: String,
    val name: String,
    val price: Double,
    val imageUrl: String,
    val rating: Float
)
