package com.example.zenvy.ui.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.zenvy.domain.model.CartItem
import com.example.zenvy.ui.theme.Dimens
import com.example.zenvy.ui.theme.ZenvyTheme

/**
 * [Purpose] - Shopping cart screen with item list and checkout summary
 * Architecture Layer: UI
 * 
 * WHY: Displays cart items with quantity controls and total price calculation
 */
/**
 * [Purpose] - Shopping cart screen with item list and checkout summary
 * Architecture Layer: UI
 * 
 * WHY: Displays cart items from Room database with quantity controls and total price
 * 
 * @param viewModel CartViewModel for state management
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: CartViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    // WHY: Observe cart items from ViewModel (Room database)
    val cartItems by viewModel.cartItems.collectAsState()
    val totalPrice by viewModel.totalPrice.collectAsState()
    
    // WHY: Debug logging to track cart state
    LaunchedEffect(cartItems.size) {
        android.util.Log.d("CartScreen", "Cart items count: ${cartItems.size}")
        cartItems.forEach { item ->
            android.util.Log.d("CartScreen", "Item: ${item.name}, Qty: ${item.quantity}, Price: ${item.price}")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Cart",
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
        },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                CartSummaryBar(
                    totalPrice = totalPrice,
                    onCheckout = { /* UI only */ }
                )
            }
        }
    ) { paddingValues ->
        if (cartItems.isEmpty()) {
            EmptyCartView(modifier = Modifier.padding(paddingValues))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues),
                contentPadding = PaddingValues(
                    start = Dimens.PaddingMedium,
                    end = Dimens.PaddingMedium,
                    top = Dimens.PaddingMedium,
                    bottom = 100.dp // Space for bottom bar
                ),
                verticalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium)
            ) {
                items(cartItems, key = { it.productId }) { cartEntity ->
                    CartItemCard(
                        cartEntity = cartEntity,
                        onQuantityIncrease = {
                            viewModel.increaseQuantity(cartEntity.productId)
                        },
                        onQuantityDecrease = {
                            viewModel.decreaseQuantity(cartEntity.productId)
                        },
                        onRemove = {
                            viewModel.removeItem(cartEntity.productId)
                        }
                    )
                }
            }
        }
    }
}

/**
 * [Purpose] - Individual cart item card
 * Architecture Layer: UI
 * 
 * WHY: Displays product in cart with image, details, quantity controls, and remove button
 * 
 * @param cartEntity Cart entity from Room database
 * @param onQuantityIncrease Increase quantity callback
 * @param onQuantityDecrease Decrease quantity callback
 * @param onRemove Remove item callback
 */
@Composable
private fun CartItemCard(
    cartEntity: com.example.zenvy.data.local.cart.CartEntity,
    onQuantityIncrease: () -> Unit,
    onQuantityDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.ElevationSmall)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.PaddingMedium)
        ) {
            // Product Image
            AsyncImage(
                model = cartEntity.imageUrl,
                contentDescription = cartEntity.name,
                modifier = Modifier
                    .size(80.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(Dimens.PaddingMedium))

            // Product Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = cartEntity.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(Dimens.PaddingExtraSmall))

                // Price
                Text(
                    text = "$${cartEntity.price}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(Dimens.PaddingSmall))

                // Quantity Controls
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall)
                ) {
                    // Decrease Button
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                            .clickable { onQuantityDecrease() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "−",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Text(
                        text = cartEntity.quantity.toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.widthIn(min = 24.dp)
                    )

                    // Increase Button
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                            .clickable { onQuantityIncrease() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Remove Button
                    IconButton(onClick = onRemove) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remove",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

/**
 * [Purpose] - Cart summary bar with total and checkout button
 * Architecture Layer: UI
 * 
 * WHY: Fixed bottom bar showing total price and checkout action
 * 
 * @param totalPrice Total cart price
 * @param onCheckout Checkout callback
 */
@Composable
private fun CartSummaryBar(
    totalPrice: Double,
    onCheckout: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = Dimens.ElevationMedium,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.PaddingMedium)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "$${String.format("%.2f", totalPrice)}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

            Button(
                onClick = onCheckout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.ButtonHeightLarge)
            ) {
                Text(
                    text = "Checkout",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

/**
 * [Purpose] - Empty cart view
 * Architecture Layer: UI
 * 
 * WHY: Displayed when cart has no items
 * 
 * @param modifier Modifier
 */
@Composable
private fun EmptyCartView(modifier: Modifier = Modifier) {
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
                text = "🛒",
                fontSize = 64.sp
            )
            Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
            Text(
                text = "Your cart is empty",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CartScreenPreview() {
    ZenvyTheme {
        CartScreen()
    }
}
