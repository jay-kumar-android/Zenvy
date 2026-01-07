package com.example.zenvy.domain.model

import androidx.annotation.DrawableRes

/**
 * [Purpose] - Category model for product categorization
 * Architecture Layer: Domain
 * 
 * WHY: Represents product categories for navigation and filtering
 * 
 * @param id Category identifier
 * @param name Display name
 * @param icon Icon identifier or emoji (kept for backward compatibility)
 * @param imageRes Drawable resource ID for category image
 */
data class Category(
    val id: String,
    val name: String,
    val icon: String,
    @DrawableRes val imageRes: Int? = null
) {
    companion object {
        /**
         * Mock categories for UI with actual drawable images
         */
        fun mockList() = listOf(
            Category(id = "women", name = "Women", icon = "👗", imageRes = com.example.zenvy.R.drawable.women),
            Category(id = "men", name = "Men", icon = "👔", imageRes = com.example.zenvy.R.drawable.mens),
            Category(id = "teens", name = "Teens", icon = "🎒", imageRes = com.example.zenvy.R.drawable.jenz),
            Category(id = "kids", name = "Kids", icon = "🧸", imageRes = com.example.zenvy.R.drawable.kideswere)
        )
    }
}
