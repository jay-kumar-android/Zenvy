package com.example.zenvy.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.example.zenvy.R
import com.example.zenvy.ui.theme.Dimens
import com.example.zenvy.ui.theme.ZenvyTheme

/**
 * [Purpose] - Onboarding screen 1: Discover Styles
 * Architecture Layer: UI
 * 
 * WHY: Introduces users to handpicked selection of top styles with Lottie animation
 * 
 * @param onNext Navigate to next onboarding screen
 */
@Composable
fun OnboardingScreen1(
    onNext: () -> Unit
) {
    OnboardingTemplate(
        animationRes = R.raw.onboarding1,
        title = "Discover our handpicked selection of top Styles",
        subtitle = "Choose from a wide range of products, there are multiple options available.",
        currentPage = 0,
        totalPages = 3,
        buttonText = "Next",
        onButtonClick = onNext
    )
}

/**
 * [Purpose] - Onboarding screen 2: Secure Payments
 * Architecture Layer: UI
 * 
 * WHY: Introduces secure and convenient online payment options
 * 
 * @param onNext Navigate to next onboarding screen
 */
@Composable
fun OnboardingScreen2(
    onNext: () -> Unit
) {
    OnboardingTemplate(
        animationRes = R.raw.onboarding2,
        title = "Secure and Convenient online payments",
        subtitle = "Make secure payments with all major card options available",
        currentPage = 1,
        totalPages = 3,
        buttonText = "Next",
        onButtonClick = onNext
    )
}

/**
 * [Purpose] - Onboarding screen 3: Fast Delivery
 * Architecture Layer: UI
 * 
 * WHY: Final onboarding screen highlighting fast and reliable delivery
 * 
 * @param onGetStarted Navigate to login/auth flow
 */
@Composable
fun OnboardingScreen3(
    onGetStarted: () -> Unit
) {
    OnboardingTemplate(
        animationRes = R.raw.onboarding3,
        title = "Enjoy fast, reliable delivery straight to your doorstep",
        subtitle = "Goods delivered swiftly and safely like never before right at your doorstep",
        currentPage = 2,
        totalPages = 3,
        buttonText = "Get Started",
        onButtonClick = onGetStarted
    )
}

/**
 * [Purpose] - Reusable onboarding template component
 * Architecture Layer: UI
 * 
 * WHY: DRY principle - all onboarding screens share same structure
 * 
 * @param animationRes Lottie animation resource ID
 * @param title Main heading text
 * @param subtitle Description text
 * @param currentPage Current page index (0-based)
 * @param totalPages Total number of pages
 * @param buttonText Button label
 * @param onButtonClick Button click callback
 */
@Composable
private fun OnboardingTemplate(
    animationRes: Int,
    title: String,
    subtitle: String,
    currentPage: Int,
    totalPages: Int,
    buttonText: String,
    onButtonClick: () -> Unit
) {
    // WHY: Load and configure Lottie animation
    val composition = rememberLottieComposition(LottieCompositionSpec.RawRes(animationRes))
    val progress = animateLottieCompositionAsState(
        composition = composition.value,
        iterations = LottieConstants.IterateForever
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(Dimens.PaddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(Dimens.PaddingExtraLarge))

        // Lottie Animation
        LottieAnimation(
            composition = composition.value,
            progress = { progress.value },
            modifier = Modifier
                .size(Dimens.LottieAnimationSizeLarge)
        )

        // Content Section
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = Dimens.PaddingMedium)
        ) {
            Text(
                text = title,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Dimens.PaddingSmall))

            Text(
                text = subtitle,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
        }

        // Bottom Section: Page Indicator + Button
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Page Indicator Dots
            PageIndicator(
                currentPage = currentPage,
                totalPages = totalPages
            )

            Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

            // Primary Button
            Button(
                onClick = onButtonClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.ButtonHeightLarge),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = buttonText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

/**
 * [Purpose] - Page indicator dots component
 * Architecture Layer: UI
 * 
 * WHY: Visual feedback for current onboarding page
 * 
 * @param currentPage Active page index
 * @param totalPages Total number of pages
 */
@Composable
private fun PageIndicator(
    currentPage: Int,
    totalPages: Int
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall)
    ) {
        repeat(totalPages) { index ->
            Box(
                modifier = Modifier
                    .size(if (index == currentPage) 10.dp else 8.dp)
                    .clip(CircleShape)
                    .background(
                        if (index == currentPage)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.outline
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingScreen1Preview() {
    ZenvyTheme {
        OnboardingScreen1(onNext = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingScreen2Preview() {
    ZenvyTheme {
        OnboardingScreen2(onNext = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingScreen3Preview() {
    ZenvyTheme {
        OnboardingScreen3(onGetStarted = {})
    }
}
