package com.example.zenvy.ui.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zenvy.ui.theme.Dimens

/**
 * [Purpose] - Notifications screen displaying user notifications
 * Architecture Layer: UI
 * 
 * WHY: Shows order updates, promotions, and app notifications
 * 
 * @param onBackClick Navigate back to previous screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    onBackClick: () -> Unit
) {
    // WHY: Mock notification data for UI demonstration (limited to 3 for clean UI)
    val notifications = listOf(
        NotificationItem(
            id = "1",
            title = "Order Shipped",
            message = "Your order #12345 has been shipped and will arrive soon!",
            time = "2 hours ago",
            isRead = false
        ),
        NotificationItem(
            id = "2",
            title = "New Collection Available",
            message = "Check out our latest summer collection with amazing discounts!",
            time = "5 hours ago",
            isRead = false
        ),
        NotificationItem(
            id = "3",
            title = "Order Delivered",
            message = "Your order #12344 has been delivered successfully.",
            time = "1 day ago",
            isRead = true
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Notifications",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                windowInsets = WindowInsets(0.dp)
            )
        }
    ) { paddingValues ->
        if (notifications.isEmpty()) {
            // Empty State
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
                    Text(
                        text = "No notifications yet",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
                    Text(
                        text = "We'll notify you when something arrives",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
        } else {
            // Notifications List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues),
                contentPadding = PaddingValues(vertical = Dimens.PaddingSmall)
            ) {
                items(notifications) { notification ->
                    NotificationCard(notification = notification)
                }
            }
        }
    }
}

/**
 * [Purpose] - Individual notification card
 * Architecture Layer: UI
 * 
 * WHY: Displays notification details with read/unread state
 * 
 * @param notification Notification data
 */
@Composable
private fun NotificationCard(notification: NotificationItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = Dimens.PaddingMedium,
                vertical = Dimens.PaddingSmall
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (notification.isRead) 1.dp else 3.dp
        ),
        border = if (!notification.isRead) {
            androidx.compose.foundation.BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            )
        } else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.PaddingMedium),
            verticalAlignment = Alignment.Top
        ) {
            // Notification Icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = MaterialTheme.shapes.small
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(Dimens.PaddingMedium))

            // Notification Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.title,
                        fontSize = 16.sp,
                        fontWeight = if (notification.isRead) FontWeight.Medium else FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )

                    // Unread indicator
                    if (!notification.isRead) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = MaterialTheme.shapes.extraLarge
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(Dimens.PaddingExtraSmall))

                Text(
                    text = notification.message,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(Dimens.PaddingSmall))

                Text(
                    text = notification.time,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        }
    }
}

/**
 * [Purpose] - Notification data model
 * Architecture Layer: UI
 * 
 * WHY: Represents a single notification item
 */
data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val time: String,
    val isRead: Boolean
)
