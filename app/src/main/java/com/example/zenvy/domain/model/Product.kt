package com.example.zenvy.domain.model

/**
 * [Purpose] - Product domain model for eCommerce catalog
 * Architecture Layer: Domain
 * 
 * WHY: Represents a product entity with all necessary attributes for display and interaction
 * 
 * @param id Unique product identifier
 * @param name Product display name
 * @param description Product description text
 * @param price Current selling price
 * @param originalPrice Original price before discount (null if no discount)
 * @param imageUrl Product image URL
 * @param rating Average rating (0.0 to 5.0)
 * @param category Product category
 * @param colors Available color variants
 * @param sizes Available size variants
 * @param isFavorite Whether user has added to wishlist
 */
data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val originalPrice: Double? = null,
    val imageUrl: String,
    val rating: Float = 0f,
    val category: String,
    val colors: List<String> = emptyList(),
    val sizes: List<String> = emptyList(),
    val isFavorite: Boolean = false
) {
    companion object {
        /**
         * Mock product for previews and testing
         */
        fun mock() = Product(
            id = "1",
            name = "Premium Cotton T-Shirt",
            description = "Comfortable and stylish cotton t-shirt perfect for everyday wear. Made from 100% organic cotton.",
            price = 29.99,
            originalPrice = 49.99,
            imageUrl = "https://via.placeholder.com/400x400/7C3AED/FFFFFF?text=Product",
            rating = 4.5f,
            category = "Women",
            colors = listOf("#7C3AED", "#EC4899", "#10B981", "#1F2937"),
            sizes = listOf("S", "M", "L", "XL"),
            isFavorite = false
        )

        /**
         * Mock product list for previews
         */
        fun mockList() = listOf(
            mock(),
            mock().copy(
                id = "2",
                name = "Classic Denim Jeans",
                price = 59.99,
                originalPrice = 89.99,
                rating = 4.8f,
                category = "Men"
            ),
            mock().copy(
                id = "3",
                name = "Summer Floral Dress",
                price = 79.99,
                originalPrice = null,
                rating = 4.3f,
                category = "Women"
            ),
            mock().copy(
                id = "4",
                name = "Casual Sneakers",
                price = 89.99,
                originalPrice = 120.00,
                rating = 4.6f,
                category = "Teens"
            ),
            mock().copy(
                id = "5",
                name = "Kids Graphic Tee",
                price = 19.99,
                originalPrice = null,
                rating = 4.7f,
                category = "Kids"
            ),
            mock().copy(
                id = "6",
                name = "Leather Jacket",
                price = 199.99,
                originalPrice = 299.99,
                rating = 4.9f,
                category = "Men"
            )
        )
    }
}
