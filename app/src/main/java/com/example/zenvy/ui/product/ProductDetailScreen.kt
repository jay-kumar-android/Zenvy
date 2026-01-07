package com.example.zenvy.ui.product

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.zenvy.domain.model.Product
import com.example.zenvy.ui.theme.Dimens
import com.example.zenvy.ui.theme.ZenvyTheme

/**
 * [Purpose] - Product detail screen with images, variants, and purchase options
 * Architecture Layer: UI
 * 
 * WHY: Detailed product view with real Firestore data
 * 
 * @param productId Product identifier
 * @param onBackClick Navigate back callback
 * @param viewModel ProductDetailViewModel for data and actions
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: String,
    onBackClick: () -> Unit,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    // WHY: Observe product state from ViewModel
    val product by viewModel.product.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val isInWishlist by viewModel.isInWishlist.collectAsState()
    val actionMessage by viewModel.actionMessage.collectAsState()
    
    // WHY: Local UI state for variants
    var selectedColor by remember { mutableStateOf<String?>(null) }
    var selectedSize by remember { mutableStateOf<String?>(null) }
    
    // WHY: Initialize selected variants when product loads
    val context = androidx.compose.ui.platform.LocalContext.current
    LaunchedEffect(product) {
        product?.let {
            selectedColor = it.colors.firstOrNull()
            selectedSize = it.sizes.firstOrNull()
        }
    }
    
    // WHY: Show action message as snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(actionMessage) {
        actionMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { 
                        product?.let { viewModel.toggleWishlist(it) }
                    }) {
                        Icon(
                            imageVector = if (isInWishlist) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Wishlist",
                            tint = if (isInWishlist) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            // WHY: Only show bottom bar when product is loaded
            product?.let { prod ->
                BottomCTABar(
                    onAddToCart = { viewModel.addToCart(prod) },
                    onBuyNow = { 
                        android.widget.Toast.makeText(context, "Coming Soon!", android.widget.Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    // WHY: Loading state
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                error != null -> {
                    // WHY: Error state
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(Dimens.PaddingLarge),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = error ?: "Failed to load product",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
                        Button(onClick = onBackClick) {
                            Text("Go Back")
                        }
                    }
                }
                product != null -> {
                    // WHY: Product loaded successfully
                    ProductDetailContent(
                        product = product!!,
                        selectedColor = selectedColor,
                        selectedSize = selectedSize,
                        onColorSelected = { selectedColor = it },
                        onSizeSelected = { selectedSize = it }
                    )
                }
            }
        }
    }
}

/**
 * [Purpose] - Product detail content
 * Architecture Layer: UI
 * 
 * WHY: Separated content for cleaner code
 */
@Composable
private fun ProductDetailContent(
    product: Product,
    selectedColor: String?,
    selectedSize: String?,
    onColorSelected: (String) -> Unit,
    onSizeSelected: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Product Image
        item {
            ProductImageSection(imageUrl = product.imageUrl)
        }

        // Product Info
        item {
            Column(
                modifier = Modifier.padding(Dimens.PaddingMedium)
            ) {
                // Product Name
                Text(
                    text = product.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(Dimens.PaddingSmall))

                // Rating
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "⭐",
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${product.rating} (120 reviews)",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

                // Price
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$${product.price}",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    product.originalPrice?.let { originalPrice ->
                        Spacer(modifier = Modifier.width(Dimens.PaddingMedium))
                        Text(
                            text = "$${originalPrice}",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

                // Description
                Text(
                    text = "Description",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
                Text(
                    text = product.description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

                // Color Variants
                if (product.colors.isNotEmpty()) {
                    Text(
                        text = "Colors",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
                    ColorVariantSelector(
                        colors = product.colors,
                        selectedColor = selectedColor,
                        onColorSelected = onColorSelected
                    )
                    Spacer(modifier = Modifier.height(Dimens.PaddingLarge))
                }

                // Size Variants
                if (product.sizes.isNotEmpty()) {
                    Text(
                        text = "Sizes",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
                    SizeVariantSelector(
                        sizes = product.sizes,
                        selectedSize = selectedSize,
                        onSizeSelected = onSizeSelected
                    )
                }

                Spacer(modifier = Modifier.height(80.dp)) // Space for bottom bar
            }
        }
    }
}

/**
 * [Purpose] - Product image section with indicator dots
 * Architecture Layer: UI
 * 
 * WHY: Large product image display (single image for now)
 * 
 * @param imageUrl Product image URL
 */
@Composable
private fun ProductImageSection(imageUrl: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        AsyncImage(
            model = coil.request.ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .error(com.example.zenvy.R.drawable.ic_launcher_foreground)
                .placeholder(com.example.zenvy.R.drawable.ic_launcher_foreground)
                .build(),
            contentDescription = "Product Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            onState = { state ->
                 if (state is coil.compose.AsyncImagePainter.State.Error) {
                     android.util.Log.e("CoilDetailError", "Failed to load detail image: ${state.result.throwable.message}")
                 }
            }
        )

        // Image Indicator (single dot for now)
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(Dimens.PaddingMedium),
            horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    }
}

/**
 * [Purpose] - Color variant selector
 * Architecture Layer: UI
 * 
 * WHY: Allows users to select product color variant
 * 
 * @param colors Available color hex codes
 * @param selectedColor Currently selected color
 * @param onColorSelected Callback when color selected
 */
@Composable
private fun ColorVariantSelector(
    colors: List<String>,
    selectedColor: String?,
    onColorSelected: (String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium)
    ) {
        colors.forEach { colorHex ->
            val isSelected = colorHex == selectedColor
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(android.graphics.Color.parseColor(colorHex)))
                    .border(
                        width = if (isSelected) 3.dp else 0.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
                    .clickable { onColorSelected(colorHex) }
            )
        }
    }
}

/**
 * [Purpose] - Size variant selector
 * Architecture Layer: UI
 * 
 * WHY: Allows users to select product size variant
 * 
 * @param sizes Available sizes
 * @param selectedSize Currently selected size
 * @param onSizeSelected Callback when size selected
 */
@Composable
private fun SizeVariantSelector(
    sizes: List<String>,
    selectedSize: String?,
    onSizeSelected: (String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium)
    ) {
        sizes.forEach { size ->
            val isSelected = size == selectedSize
            FilterChip(
                selected = isSelected,
                onClick = { onSizeSelected(size) },
                label = {
                    Text(
                        text = size,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
        }
    }
}

/**
 * [Purpose] - Bottom CTA bar with Add to Cart and Buy Now buttons
 * Architecture Layer: UI
 * 
 * WHY: Fixed bottom bar for primary purchase actions
 * 
 * @param onAddToCart Add to cart callback
 * @param onBuyNow Buy now callback
 */
@Composable
private fun BottomCTABar(
    onAddToCart: () -> Unit,
    onBuyNow: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = Dimens.ElevationMedium,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.PaddingMedium),
            horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium)
        ) {
            // Add to Cart Button
            OutlinedButton(
                onClick = onAddToCart,
                modifier = Modifier
                    .weight(1f)
                    .height(Dimens.ButtonHeightLarge)
            ) {
                Text(
                    text = "Add to Cart",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Buy Now Button
            Button(
                onClick = onBuyNow,
                modifier = Modifier
                    .weight(1f)
                    .height(Dimens.ButtonHeightLarge)
            ) {
                Text(
                    text = "Buy Now",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductDetailScreenPreview() {
    ZenvyTheme {
        ProductDetailScreen(
            productId = "1",
            onBackClick = {}
        )
    }
}
