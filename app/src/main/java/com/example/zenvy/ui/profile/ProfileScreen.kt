package com.example.zenvy.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.zenvy.R
import com.example.zenvy.ui.theme.Dimens
import com.example.zenvy.ui.theme.ZenvyTheme

/**
 * [Purpose] - User profile screen with account info and menu options
 * Architecture Layer: UI
 * 
 * WHY: Displays user profile and app settings/actions
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onLogout: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {
    // WHY: Collect user data from ViewModel
    val userName by viewModel.userName.collectAsState()
    val userEmail by viewModel.userEmail.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // TopAppBar without Scaffold
        TopAppBar(
            title = {
                Text(
                    text = "Profile",
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
            // Profile Header
            item {
                ProfileHeader(
                    userName = userName,
                    userEmail = userEmail
                )
            }

            item {
                Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
            }

            // Menu Items
            item {
                ProfileMenuItem(
                    icon = Icons.Default.ShoppingCart,
                    title = "My Orders",
                    onClick = { /* UI only */ }
                )
            }

            item {
                ProfileMenuItem(
                    icon = Icons.Default.LocationOn,
                    title = "Addresses",
                    onClick = { /* UI only */ }
                )
            }

            item {
                ProfileMenuItem(
                    icon = Icons.Default.AccountCircle,
                    title = "Payment Methods",
                    onClick = { /* UI only */ }
                )
            }

            item {
                ProfileMenuItem(
                    icon = Icons.Default.Notifications,
                    title = "Notifications",
                    onClick = { /* UI only */ }
                )
            }

            item {
                ProfileMenuItem(
                    icon = Icons.Default.Settings,
                    title = "Settings",
                    onClick = { /* UI only */ }
                )
            }

            item {
                ProfileMenuItem(
                    icon = Icons.Default.Info,
                    title = "Help & Support",
                    onClick = { /* UI only */ }
                )
            }

            item {
                Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
            }

            // Logout Button
            item {
                OutlinedButton(
                    onClick = onLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.ButtonHeightLarge),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Logout",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(Dimens.PaddingSmall))
                    Text(
                        text = "Logout",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

/**
 * [Purpose] - Profile header with avatar and user info
 * Architecture Layer: UI
 * 
 * WHY: Displays user's profile picture and email from Firebase Auth
 * 
 * @param userName User's display name
 * @param userEmail User's email address
 */
@Composable
private fun ProfileHeader(
    userName: String? = null,
    userEmail: String? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.ElevationSmall)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.PaddingLarge),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Avatar
            Image(
                painter = painterResource(id = R.drawable.manavtar),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(Dimens.PaddingMedium))

            // User Info
            Column {
                Text(
                    text = userName ?: "User",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = userEmail ?: "user@example.com",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * [Purpose] - Profile menu item component
 * Architecture Layer: UI
 * 
 * WHY: Reusable menu item with icon, title, and navigation arrow
 * 
 * @param icon Menu item icon
 * @param title Menu item title
 * @param onClick Click callback
 */
@Composable
private fun ProfileMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit
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
                .padding(Dimens.PaddingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(Dimens.PaddingMedium))

            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Navigate",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    ZenvyTheme {
        ProfileScreen()
    }
}
