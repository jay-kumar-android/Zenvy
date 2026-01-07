package com.example.zenvy.domain.model

/**
 * [Purpose] - Cart item model representing a product in user's shopping cart
 * Architecture Layer: Domain
 * 
 * WHY: Encapsulates cart-specific data like quantity and selected variants
 * 
 * @param product The product being added to cart
 * @param quantity Number of items
 * @param selectedColor Selected color variant
 * @param selectedSize Selected size variant
 */
data class CartItem(
    val product: Product,
    val quantity: Int = 1,
    val selectedColor: String? = null,
    val selectedSize: String? = null
) {
    /**
     * Calculate total price for this cart item
     */
    val totalPrice: Double
        get() = product.price * quantity

    companion object {
        /**
         * Mock cart item for previews
         */
        fun mock() = CartItem(
            product = Product.mock(),
            quantity = 2,
            selectedColor = "#7C3AED",
            selectedSize = "M"
        )

        /**
         * Mock cart item list for previews
         */
        fun mockList() = listOf(
            mock(),
            mock().copy(
                product = Product.mock().copy(
                    id = "2",
                    name = "Classic Denim Jeans",
                    price = 59.99
                ),
                quantity = 1,
                selectedSize = "L"
            ),
            mock().copy(
                product = Product.mock().copy(
                    id = "3",
                    name = "Summer Floral Dress",
                    price = 79.99
                ),
                quantity = 3,
                selectedSize = "S"
            )
        )
    }
}
