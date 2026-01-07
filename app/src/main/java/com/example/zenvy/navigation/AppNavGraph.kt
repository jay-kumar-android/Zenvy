package com.example.zenvy.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.zenvy.ui.auth.AuthUiState
import com.example.zenvy.ui.auth.AuthViewModel
import com.example.zenvy.ui.auth.ForgotPasswordScreen
import com.example.zenvy.ui.auth.LoginScreen
import com.example.zenvy.ui.auth.RegisterScreen
import com.example.zenvy.ui.main.MainScreen
import com.example.zenvy.ui.onboarding.OnboardingScreen1
import com.example.zenvy.ui.onboarding.OnboardingScreen2
import com.example.zenvy.ui.onboarding.OnboardingScreen3
import com.example.zenvy.ui.product.ProductDetailScreen

/**
 * [Purpose] - Main navigation graph for the entire app
 * Architecture Layer: Navigation
 * 
 * WHY: Centralized navigation setup with auth-driven navigation
 * 
 * @param navController Main navigation controller
 * @param authViewModel Shared AuthViewModel for auth state
 */
@Composable
fun AppNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsState()
    
    // WHY: Determine start destination based on auth state
    val startDestination = when (authState) {
        is AuthUiState.Authenticated -> Screen.Main.route
        is AuthUiState.Unauthenticated -> Screen.Onboarding1.route
        else -> Screen.Onboarding1.route
    }
    
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Onboarding Flow
        composable(Screen.Onboarding1.route) {
            OnboardingScreen1(
                onNext = {
                    navController.navigate(Screen.Onboarding2.route)
                }
            )
        }

        composable(Screen.Onboarding2.route) {
            OnboardingScreen2(
                onNext = {
                    navController.navigate(Screen.Onboarding3.route)
                }
            )
        }

        composable(Screen.Onboarding3.route) {
            OnboardingScreen3(
                onGetStarted = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Onboarding1.route) { inclusive = true }
                    }
                }
            )
        }

        // Auth Flow
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(Screen.ForgotPassword.route)
                }
            )
            
            // WHY: Observe auth state and navigate when authenticated
            LaunchedEffect(authState) {
                if (authState is AuthUiState.Authenticated) {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
            
            // WHY: Observe auth state and navigate when authenticated
            LaunchedEffect(authState) {
                if (authState is AuthUiState.Authenticated) {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }
        }

        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                viewModel = authViewModel,
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // Main App
        composable(Screen.Main.route) {
            MainScreen(
                onProductClick = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                },
                onLogout = {
                    authViewModel.logout()
                }
            )
            
            // WHY: Observe auth state and navigate when unauthenticated
            LaunchedEffect(authState) {
                if (authState is AuthUiState.Unauthenticated) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                }
            }
        }

        // Product Detail
        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(
                navArgument("productId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ProductDetailScreen(
                productId = productId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
