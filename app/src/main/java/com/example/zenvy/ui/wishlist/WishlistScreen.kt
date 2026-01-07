package com.example.zenvy.ui.wishlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.zenvy.domain.model.Product
import com.example.zenvy.ui.theme.Dimens
import com.example.zenvy.ui.theme.ZenvyTheme

/**
 * [Purpose] - Wishlist screen displaying saved products
 * Architecture Layer: UI
 * 
 * WHY: Shows user's favorite/saved products from Room database
 * 
 * @param onProductClick Product click callback
 * @param viewModel WishlistViewModel for state management
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(
    onProductClick: (String) -> Unit = {},
    viewModel: WishlistViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    // WHY: Observe wishlist items from ViewModel (Room database)
    val wishlistItems by viewModel.wishlistItems.collectAsState()
    
    // WHY: Debug logging to track wishlist state
    LaunchedEffect(wishlistItems.size) {
        android.util.Log.d("WishlistScreen", "Wishlist items count: ${wishlistItems.size}")
        wishlistItems.forEach { item ->
            android.util.Log.d("WishlistScreen", "Item: ${item.name}, Price: ${item.price}")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // TopAppBar without Scaffold
        TopAppBar(
            title = {
                Text(
                    text = "Wishlist",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            windowInsets = WindowInsets(0.dp)
        )
        
        // Content
        if (wishlistItems.isEmpty()) {
            EmptyWishlistView(modifier = Modifier.fillMaxSize())
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(
                    start = Dimens.PaddingMedium,
                    end = Dimens.PaddingMedium,
                    top = Dimens.PaddingMedium,
                    bottom = Dimens.PaddingMedium
                ),
                verticalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium)
            ) {
                items(wishlistItems, key = { it.productId }) { wishlistEntity ->
                    WishlistItemCard(
                        wishlistEntity = wishlistEntity,
                        onClick = { onProductClick(wishlistEntity.productId) },
                        onRemove = {
                            viewModel.removeItem(wishlistEntity.productId)
                        }
                    )
                }
            }
        }
    }
}

/**
 * [Purpose] - Individual wishlist item card
 * Architecture Layer: UI
 * 
 * WHY: Displays saved product with image, details, price, and remove button
 * 
 * @param wishlistEntity Wishlist entity from Room database
 * @param onClick Product click callback
 * @param onRemove Remove from wishlist callback
 */
@Composable
private fun WishlistItemCard(
    wishlistEntity: com.example.zenvy.data.local.wishlist.WishlistEntity,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.ElevationSmall)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.PaddingMedium)
        ) {
            // Product Image
            AsyncImage(
                model = wishlistEntity.imageUrl,
                contentDescription = wishlistEntity.name,
                modifier = Modifier
                    .size(100.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(Dimens.PaddingMedium))

            // Product Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = wishlistEntity.name,
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
                        text = wishlistEntity.rating.toString(),
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
                        text = "$${wishlistEntity.price}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(Dimens.PaddingSmall))

                // Remove Button
                TextButton(
                    onClick = onRemove,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Remove",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

/**
 * [Purpose] - Empty wishlist view
 * Architecture Layer: UI
 * 
 * WHY: Displayed when wishlist has no items
 * 
 * @param modifier Modifier
 */
@Composable
private fun EmptyWishlistView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "❤️",
                fontSize = 64.sp
            )
            Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
            Text(
                text = "Your wishlist is empty",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WishlistScreenPreview() {
    ZenvyTheme {
        WishlistScreen()
    }
}
