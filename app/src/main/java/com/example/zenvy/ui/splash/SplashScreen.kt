package com.example.zenvy.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.zenvy.R
import com.example.zenvy.ui.theme.ZenvyTheme
import kotlinx.coroutines.delay

/**
 * [Purpose] - Splash screen with fade-in animation
 * Architecture Layer: UI
 * 
 * WHY: First screen users see, establishes brand identity with logo animation
 * 
 * @param onNavigateToOnboarding Callback when splash completes
 */
@Composable
fun SplashScreen(
    onNavigateToOnboarding: () -> Unit
) {
    // WHY: Animatable for smooth fade-in effect
    val alpha = remember { Animatable(0f) }

    // WHY: Auto-navigate after animation completes
    LaunchedEffect(Unit) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
        delay(1500) // Hold for 1.5 seconds
        onNavigateToOnboarding()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.mainapplogo),
            contentDescription = "Zenvy Logo",
            modifier = Modifier
                .size(200.dp)
                .alpha(alpha.value)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    ZenvyTheme {
        SplashScreen(onNavigateToOnboarding = {})
    }
}
