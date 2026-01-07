package com.example.zenvy.navigation

/**
 * [Purpose] - Navigation routes for the entire app
 * Architecture Layer: UI/Navigation
 * 
 * WHY: Centralized route definitions prevent typos and ensure type-safe navigation
 */
sealed class Screen(val route: String) {
    // Onboarding Flow
    object Splash : Screen("splash")
    object Onboarding1 : Screen("onboarding1")
    object Onboarding2 : Screen("onboarding2")
    object Onboarding3 : Screen("onboarding3")
    
    // Auth Flow
    object Login : Screen("login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")
    
    // Main App
    object Main : Screen("main")
    
    // Bottom Nav Destinations
    object Home : Screen("home")
    object Cart : Screen("cart")
    object Wishlist : Screen("wishlist")
    object Profile : Screen("profile")
    object Notifications : Screen("notifications")
    
    // Detail Screens
    object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: String) = "product_detail/$productId"
    }
}
