package com.example.zenvy.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.zenvy.navigation.Screen
import com.example.zenvy.ui.cart.CartScreen
import com.example.zenvy.ui.home.HomeScreen
import com.example.zenvy.ui.notifications.NotificationScreen
import com.example.zenvy.ui.profile.ProfileScreen
import com.example.zenvy.ui.theme.ZenvyTheme
import com.example.zenvy.ui.wishlist.WishlistScreen

/**
 * [Purpose] - Main app screen with bottom navigation
 * Architecture Layer: UI
 * 
 * WHY: Container for bottom navigation tabs (Home, Cart, Wishlist, Profile)
 * 
 * @param onProductClick Navigate to product detail
 * @param onLogout Logout callback
 */
@Composable
fun MainScreen(
    onProductClick: (String) -> Unit,
    onLogout: () -> Unit
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    navController = navController,
                    onProductClick = onProductClick
                )
            }

            composable(Screen.Cart.route) {
                CartScreen()
            }

            composable(Screen.Wishlist.route) {
                WishlistScreen(onProductClick = onProductClick)
            }

            composable(Screen.Profile.route) {
                ProfileScreen(onLogout = onLogout)
            }

            composable(Screen.Notifications.route) {
                NotificationScreen(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

/**
 * [Purpose] - Bottom navigation bar component
 * Architecture Layer: UI
 * 
 * WHY: Provides tab navigation between main app sections
 * 
 * @param navController Navigation controller
 */
@Composable
private fun BottomNavigationBar(
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Cart,
        BottomNavItem.Wishlist,
        BottomNavItem.Profile
    )

    NavigationBar {
        items.forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == item.route } == true

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
                selected = isSelected,
                onClick = {
                    // WHY: Navigate with proper back stack management
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

/**
 * [Purpose] - Bottom navigation item data class
 * Architecture Layer: UI
 * 
 * WHY: Encapsulates bottom nav item properties
 */
private sealed class BottomNavItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Home : BottomNavItem(
        route = Screen.Home.route,
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )

    object Cart : BottomNavItem(
        route = Screen.Cart.route,
        title = "Cart",
        selectedIcon = Icons.Filled.ShoppingCart,
        unselectedIcon = Icons.Outlined.ShoppingCart
    )

    object Wishlist : BottomNavItem(
        route = Screen.Wishlist.route,
        title = "Wishlist",
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Outlined.FavoriteBorder
    )

    object Profile : BottomNavItem(
        route = Screen.Profile.route,
        title = "Profile",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    ZenvyTheme {
        MainScreen(
            onProductClick = {},
            onLogout = {}
        )
    }
}
