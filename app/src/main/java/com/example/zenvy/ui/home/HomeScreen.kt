package com.example.zenvy.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.zenvy.domain.model.Category
import com.example.zenvy.domain.model.Product
import com.example.zenvy.navigation.Screen
import com.example.zenvy.ui.theme.Dimens

/**
 * [Purpose] - Modern Home screen with search, banner, categories, and products
 * Architecture Layer: UI
 * 
 * WHY: Main discovery screen with professional UI and search functionality
 * 
 * @param navController Navigation controller for actions
 * @param onProductClick Navigate to product detail
 * @param viewModel HomeViewModel for state management
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    onProductClick: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val products by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isSearchActive by viewModel.isSearchActive.collectAsState()
    val actionMessage by viewModel.actionMessage.collectAsState()
    val wishlistProductIds by viewModel.wishlistProductIds.collectAsState()
    
    val categories = Category.mockList()
    val keyboardController = LocalSoftwareKeyboardController.current
    
    // WHY: Show action message as snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(actionMessage) {
        actionMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // WHY: Compact TopAppBar with minimal height
        TopAppBar(
            title = {
                if (!isSearchActive) {
                    Text(
                        text = "Zenvy",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    // WHY: Compact search field when search is active
                    TextField(
                        value = searchQuery,
                        onValueChange = { viewModel.onSearchQueryChange(it) },
                        placeholder = { 
                            Text(
                                "Search products",
                                fontSize = 15.sp
                            ) 
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.clearSearch() }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear"
                                    )
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = { keyboardController?.hide() }
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                            unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent
                        ),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp)
                    )
                }
            },
            navigationIcon = {
                if (isSearchActive) {
                    IconButton(onClick = { viewModel.toggleSearch() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Close Search"
                        )
                    }
                }
            },
            actions = {
                if (!isSearchActive) {
                    // Search Icon
                    IconButton(onClick = { viewModel.toggleSearch() }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                    
                    // Notification Icon with Badge
                    IconButton(onClick = {
                        navController.navigate(Screen.Notifications.route) {
                            launchSingleTop = true
                        }
                    }) {
                        // WHY: Badge shows unread notification count (currently hardcoded to 3)
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = androidx.compose.ui.graphics.Color(0xFFFF3B30), // Red color
                                    contentColor = androidx.compose.ui.graphics.Color.White
                                ) {
                                    Text(
                                        text = "3",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications"
                            )
                        }
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            windowInsets = WindowInsets(0.dp)
        )
        
        // Content with Snackbar support
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                isLoading -> {
                    // Loading State
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                error != null -> {
                    // Error State
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(Dimens.PaddingLarge),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
                        Text(
                            text = error ?: "Something went wrong",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
                        Button(onClick = { viewModel.retry() }) {
                            Text("Retry")
                        }
                    }
                }
                products.isEmpty() && isSearchActive -> {
                    // Empty Search State
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(Dimens.PaddingLarge),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = if (searchQuery.isBlank()) Icons.Default.Search else Icons.Default.Info,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
                        Text(
                            text = if (searchQuery.isBlank()) "Start searching" else "No products found",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
                        Text(
                            text = if (searchQuery.isBlank()) 
                                "Type something to search for products" 
                            else 
                                "Try searching with different keywords",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
                else -> {
                    // Content
                    val chunkedProducts = remember(products) { products.chunked(2) }
                    
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        // Banner Section (only show when not searching)
                        if (!isSearchActive) {
                            item {
                                ModernBannerCard()
                            }

                            // Category Section
                            item {
                                Spacer(modifier = Modifier.height(Dimens.PaddingLarge))
                                SectionHeader(
                                    title = "Shop by Category",
                                    onSeeAllClick = { /* Navigate to all categories */ }
                                )
                                Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
                                CategoryRow(categories = categories)
                            }

                            // Products Section Header
                            item {
                                Spacer(modifier = Modifier.height(Dimens.PaddingLarge))
                                SectionHeader(
                                    title = "Curated For You",
                                    onSeeAllClick = { /* Navigate to all products */ }
                                )
                                Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
                            }
                        } else {
                            // Search Results Header
                            item {
                                Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
                                Text(
                                    text = "Search Results (${products.size})",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = Dimens.PaddingMedium)
                                )
                                Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
                            }
                        }

                        // Product List (Grid Layout)
                        items(chunkedProducts) { productPair ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = Dimens.PaddingMedium),
                                horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium)
                            ) {
                                // First Product
                                Box(modifier = Modifier.weight(1f)) {
                                    ProductCard(
                                        product = productPair[0],
                                        isInWishlist = wishlistProductIds.contains(productPair[0].id),
                                        onClick = { onProductClick(productPair[0].id) },
                                        onToggleWishlist = { viewModel.toggleWishlist(productPair[0]) }
                                    )
                                }

                                // Second Product (or spacer if odd number)
                                Box(modifier = Modifier.weight(1f)) {
                                    if (productPair.size > 1) {
                                        ProductCard(
                                            product = productPair[1],
                                            isInWishlist = wishlistProductIds.contains(productPair[1].id),
                                            onClick = { onProductClick(productPair[1].id) },
                                            onToggleWishlist = { viewModel.toggleWishlist(productPair[1]) }
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
                        }
                    }
                }
            }
            
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

/**
 * [Purpose] - Reusable section header with "See All" link
 * Architecture Layer: UI
 * 
 * WHY: Standard e-commerce pattern for section headers with navigation
 * 
 * @param title Section title
 * @param onSeeAllClick Callback when "See All" is clicked
 */
@Composable
private fun SectionHeader(
    title: String,
    onSeeAllClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.PaddingMedium),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        TextButton(
            onClick = onSeeAllClick,
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "See All",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

/**
 * [Purpose] - Custom banner image display
 * Architecture Layer: UI
 * 
 * WHY: Displays custom promotional banner from drawable resources
 */
@Composable
private fun ModernBannerCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.PaddingMedium)
            .height(160.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.large
    ) {
        AsyncImage(
            model = coil.request.ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                .data(com.example.zenvy.R.drawable.appbanner)
                .crossfade(true)
                .error(com.example.zenvy.R.drawable.ic_launcher_foreground)
                .placeholder(com.example.zenvy.R.drawable.ic_launcher_foreground)
                .build(),
            contentDescription = "App Banner",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            onState = { state ->
                 if (state is coil.compose.AsyncImagePainter.State.Error) {
                     android.util.Log.e("CoilBannerError", "Failed to load banner: ${state.result.throwable.message}")
                 }
            }
        )
    }
}

/**
 * [Purpose] - Horizontal category row
 * Architecture Layer: UI
 * 
 * WHY: Quick category navigation with circular icons
 * 
 * @param categories List of categories to display
 */
@Composable
private fun CategoryRow(categories: List<Category>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium),
        contentPadding = PaddingValues(horizontal = Dimens.PaddingMedium)
    ) {
        items(categories) { category ->
            CategoryItem(category = category)
        }
    }
}

/**
 * [Purpose] - Individual category item
 * Architecture Layer: UI
 * 
 * WHY: Modern category card with image, gradient overlay, and elevation
 * 
 * @param category Category data
 */
@Composable
private fun CategoryItem(category: Category) {
    Card(
        modifier = Modifier
            .width(110.dp)
            .height(130.dp)
            .clickable { /* UI only */ },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Category Image
            category.imageRes?.let { imageRes ->
                AsyncImage(
                    model = coil.request.ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                        .data(imageRes)
                        .crossfade(true)
                        .error(com.example.zenvy.R.drawable.ic_launcher_foreground)
                        .placeholder(com.example.zenvy.R.drawable.ic_launcher_foreground)
                        .build(),
                    contentDescription = category.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    onState = { state ->
                         if (state is coil.compose.AsyncImagePainter.State.Error) {
                             android.util.Log.e("CoilCategoryError", "Failed to load category image: ${state.result.throwable.message}")
                         }
                    }
                )
                
                // Gradient Overlay for better text visibility
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(
                                    androidx.compose.ui.graphics.Color.Transparent,
                                    androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.7f)
                                ),
                                startY = 0f,
                                endY = Float.POSITIVE_INFINITY
                            )
                        )
                )
            }
            
            // Category Name
            Text(
                text = category.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = androidx.compose.ui.graphics.Color.White,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(Dimens.PaddingMedium)
            )
        }
    }
}

/**
 * [Purpose] - Product card component
 * Architecture Layer: UI
 * 
 * WHY: Displays product with image, name, rating, price, and wishlist icon
 * 
 * @param product Product data
 * @param isInWishlist Whether product is currently in wishlist (for heart icon color)
 * @param onClick Callback when card clicked
 * @param onToggleWishlist Callback when wishlist icon clicked
 */
@Composable
private fun ProductCard(
    product: Product,
    isInWishlist: Boolean,
    onClick: () -> Unit,
    onToggleWishlist: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.ElevationSmall)
    ) {
        Column {
            // Product Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.ProductImageHeight)
            ) {
                AsyncImage(
                    model = coil.request.ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                        .data(product.imageUrl)
                        .crossfade(true)
                        .error(com.example.zenvy.R.drawable.ic_launcher_foreground) // Fallback image
                        .placeholder(com.example.zenvy.R.drawable.ic_launcher_foreground) // Loading image
                        .build(),
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    onState = { state ->
                        if (state is coil.compose.AsyncImagePainter.State.Error) {
                            android.util.Log.e("CoilError", "Failed to load image for ${product.name}: ${state.result.throwable.message}")
                        }
                    }
                )

                // Wishlist Icon
                IconButton(
                    onClick = onToggleWishlist,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(Dimens.PaddingSmall)
                ) {
                    Icon(
                        imageVector = if (isInWishlist) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Wishlist",
                        tint = if (isInWishlist) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Product Info
            Column(
                modifier = Modifier.padding(Dimens.PaddingMedium)
            ) {
                Text(
                    text = product.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(Dimens.PaddingExtraSmall))

                // Rating
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "⭐",
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = product.rating.toString(),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(Dimens.PaddingSmall))

                // Price
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$${product.price}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    product.originalPrice?.let { originalPrice ->
                        Spacer(modifier = Modifier.width(Dimens.PaddingSmall))
                        Text(
                            text = "$${originalPrice}",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                            )
                        )
                    }
                    }
                }
            }
        }
    }

