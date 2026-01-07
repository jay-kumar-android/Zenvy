package com.example.zenvy.data.local.cart

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * [Purpose] - Room entity for cart items
 * Architecture Layer: Data
 * 
 * WHY: Represents cart items stored locally in Room database
 * Enables offline cart functionality
 * 
 * @param productId Unique product identifier (Primary Key)
 * @param name Product name
 * @param price Product price
 * @param imageUrl Product image URL
 * @param quantity Number of items in cart
 */
@Entity(tableName = "cart_items")
data class CartEntity(
    @PrimaryKey
    val productId: String,
    val name: String,
    val price: Double,
    val imageUrl: String,
    val quantity: Int = 1
)
