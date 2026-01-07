package com.example.zenvy.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zenvy.R
import com.example.zenvy.ui.theme.Dimens
import com.example.zenvy.ui.theme.ZenvyTheme

/**
 * [Purpose] - Login screen for user authentication
 * Architecture Layer: UI
 * 
 * WHY: Allows existing users to sign in with Firebase Auth integration
 * Modern card-based design with hero image and enhanced UX
 * 
 * @param viewModel AuthViewModel for auth operations
 * @param onNavigateToRegister Navigate to registration screen
 * @param onNavigateToForgotPassword Navigate to forgot password screen
 */
@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = androidx.compose.ui.platform.LocalContext.current
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }
    
    val authState by viewModel.authState.collectAsState()
    
    // WHY: Show error message when auth fails
    val errorMessage = when (authState) {
        is AuthUiState.Error -> (authState as AuthUiState.Error).message
        else -> null
    }
    
    val isLoading = authState is AuthUiState.Loading

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(Dimens.PaddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(Dimens.PaddingExtraLarge))

            // Logo/Hero Image
            Image(
                painter = painterResource(id = R.drawable.bgremovedlogo),
                contentDescription = "Zenvy Logo",
                modifier = Modifier.size(Dimens.LogoSizeAuth)
            )

            Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

            // Login Card
            androidx.compose.material3.Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.extraLarge,
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = androidx.compose.material3.CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.PaddingLarge),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Welcome Text
                    Text(
                        text = "Welcome back",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(Dimens.PaddingSmall))

                    Text(
                        text = "Sign in to enjoy the best shopping experience",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

                    // Email Input with Icon
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        leadingIcon = {
                            androidx.compose.material3.Icon(
                                painter = painterResource(id = android.R.drawable.ic_dialog_email),
                                contentDescription = "Email Icon",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !isLoading,
                        shape = MaterialTheme.shapes.medium,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

                    // Password Input with Icon and Visibility Toggle
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        leadingIcon = {
                            androidx.compose.material3.Icon(
                                painter = painterResource(id = android.R.drawable.ic_lock_idle_lock),
                                contentDescription = "Password Icon",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        trailingIcon = {
                            androidx.compose.material3.IconButton(
                                onClick = { passwordVisible = !passwordVisible }
                            ) {
                                androidx.compose.material3.Icon(
                                    painter = painterResource(
                                        id = if (passwordVisible) 
                                            android.R.drawable.ic_menu_view 
                                        else 
                                            android.R.drawable.ic_secure
                                    ),
                                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !isLoading,
                        shape = MaterialTheme.shapes.medium,
                        visualTransformation = if (passwordVisible) 
                            androidx.compose.ui.text.input.VisualTransformation.None 
                        else 
                            PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

                    // Remember Me & Forgot Password Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            androidx.compose.material3.Checkbox(
                                checked = rememberMe,
                                onCheckedChange = { rememberMe = it },
                                enabled = !isLoading,
                                colors = androidx.compose.material3.CheckboxDefaults.colors(
                                    checkedColor = MaterialTheme.colorScheme.primary
                                )
                            )
                            Text(
                                text = "Remember me",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        TextButton(
                            onClick = onNavigateToForgotPassword,
                            enabled = !isLoading
                        ) {
                            Text(
                                text = "Forgot Password?",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Error Message
                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

                    // Login Button
                    Button(
                        onClick = { viewModel.login(email, password) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Dimens.ButtonHeightLarge),
                        shape = MaterialTheme.shapes.medium,
                        enabled = !isLoading,
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text(
                                text = "Log in",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

                    // Divider with "Or login with"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        androidx.compose.material3.HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.outline
                        )
                        Text(
                            text = "  Or login with  ",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        androidx.compose.material3.HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.outline
                        )
                    }

                    Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

                    // Social Login Buttons (Placeholder)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium)
                    ) {
                        // Google Button
                        OutlinedButton(
                            onClick = { 
                                android.widget.Toast.makeText(context, "Google Sign In Coming Soon!", android.widget.Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f),
                            shape = MaterialTheme.shapes.medium,
                            enabled = !isLoading
                        ) {
                            Text(
                                text = "Google",
                                fontSize = 14.sp
                            )
                        }

                        // Apple Button
                        OutlinedButton(
                            onClick = { 
                                android.widget.Toast.makeText(context, "Apple Sign In Coming Soon!", android.widget.Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f),
                            shape = MaterialTheme.shapes.medium,
                            enabled = !isLoading
                        ) {
                            Text(
                                text = "Apple",
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

                    // Sign Up Link
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Don't have an account? ",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        TextButton(
                            onClick = onNavigateToRegister,
                            enabled = !isLoading
                        ) {
                            Text(
                                text = "Sign up",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}


/**
 * [Purpose] - Registration screen for new users
 * Architecture Layer: UI
 * 
 * WHY: Allows new users to create account with Firebase Auth integration
 * Modern card-based design with enhanced UX
 * 
 * @param viewModel AuthViewModel for auth operations
 * @param onNavigateToLogin Navigate back to login screen
 */
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    val context = androidx.compose.ui.platform.LocalContext.current
    
    val authState by viewModel.authState.collectAsState()
    
    // WHY: Show error message when auth fails
    val errorMessage = when (authState) {
        is AuthUiState.Error -> (authState as AuthUiState.Error).message
        else -> null
    }
    
    val isLoading = authState is AuthUiState.Loading

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(Dimens.PaddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

            // Logo/Hero Image
            Image(
                painter = painterResource(id = R.drawable.bgremovedlogo),
                contentDescription = "Zenvy Logo",
                modifier = Modifier.size(Dimens.LogoSizeAuth)
            )

            Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

            // Register Card
            androidx.compose.material3.Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.extraLarge,
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = androidx.compose.material3.CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.PaddingLarge),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Welcome Text
                    Text(
                        text = "Create Account",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(Dimens.PaddingSmall))

                    Text(
                        text = "Sign up to start your shopping journey",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

                    // Full Name Input with Icon
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Name") },
                        leadingIcon = {
                            androidx.compose.material3.Icon(
                                painter = painterResource(id = android.R.drawable.ic_menu_myplaces),
                                contentDescription = "Name Icon",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !isLoading,
                        shape = MaterialTheme.shapes.medium,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

                    // Email Input with Icon
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        leadingIcon = {
                            androidx.compose.material3.Icon(
                                painter = painterResource(id = android.R.drawable.ic_dialog_email),
                                contentDescription = "Email Icon",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !isLoading,
                        shape = MaterialTheme.shapes.medium,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

                    // Password Input with Icon and Visibility Toggle
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        leadingIcon = {
                            androidx.compose.material3.Icon(
                                painter = painterResource(id = android.R.drawable.ic_lock_idle_lock),
                                contentDescription = "Password Icon",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        trailingIcon = {
                            androidx.compose.material3.IconButton(
                                onClick = { passwordVisible = !passwordVisible }
                            ) {
                                androidx.compose.material3.Icon(
                                    painter = painterResource(
                                        id = if (passwordVisible) 
                                            android.R.drawable.ic_menu_view 
                                        else 
                                            android.R.drawable.ic_secure
                                    ),
                                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !isLoading,
                        shape = MaterialTheme.shapes.medium,
                        visualTransformation = if (passwordVisible) 
                            androidx.compose.ui.text.input.VisualTransformation.None 
                        else 
                            PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

                    // Confirm Password Input with Icon and Visibility Toggle
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirm Password") },
                        leadingIcon = {
                            androidx.compose.material3.Icon(
                                painter = painterResource(id = android.R.drawable.ic_lock_idle_lock),
                                contentDescription = "Password Icon",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        trailingIcon = {
                            androidx.compose.material3.IconButton(
                                onClick = { confirmPasswordVisible = !confirmPasswordVisible }
                            ) {
                                androidx.compose.material3.Icon(
                                    painter = painterResource(
                                        id = if (confirmPasswordVisible) 
                                            android.R.drawable.ic_menu_view 
                                        else 
                                            android.R.drawable.ic_secure
                                    ),
                                    contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !isLoading,
                        shape = MaterialTheme.shapes.medium,
                        visualTransformation = if (confirmPasswordVisible) 
                            androidx.compose.ui.text.input.VisualTransformation.None 
                        else 
                            PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    // Error Message
                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

                    // Register Button
                    Button(
                        onClick = { viewModel.register(fullName, email, password, confirmPassword) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Dimens.ButtonHeightLarge),
                        shape = MaterialTheme.shapes.medium,
                        enabled = !isLoading,
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text(
                                text = "Create Account",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

                    // Divider with "Or sign up with"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        androidx.compose.material3.HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.outline
                        )
                        Text(
                            text = "  Or sign up with  ",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        androidx.compose.material3.HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.outline
                        )
                    }

                    Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

                    // Social Signup Buttons (Placeholder)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium)
                    ) {
                        // Google Button
                        OutlinedButton(
                            onClick = { 
                                android.widget.Toast.makeText(context, "Google Sign Up Coming Soon!", android.widget.Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f),
                            shape = MaterialTheme.shapes.medium,
                            enabled = !isLoading
                        ) {
                            Text(
                                text = "Google",
                                fontSize = 14.sp
                            )
                        }

                        // Apple Button
                        OutlinedButton(
                            onClick = { 
                                android.widget.Toast.makeText(context, "Apple Sign Up Coming Soon!", android.widget.Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f),
                            shape = MaterialTheme.shapes.medium,
                            enabled = !isLoading
                        ) {
                            Text(
                                text = "Apple",
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

                    // Login Link
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Already have an account? ",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        TextButton(
                            onClick = onNavigateToLogin,
                            enabled = !isLoading
                        ) {
                            Text(
                                text = "Log in",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * [Purpose] - Forgot password screen for password reset
 * Architecture Layer: UI
 * 
 * WHY: Allows users to reset their password via email
 * 
 * @param viewModel AuthViewModel for auth operations
 * @param onNavigateToLogin Navigate back to login screen
 */
@Composable
fun ForgotPasswordScreen(
    viewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    
    val authState by viewModel.authState.collectAsState()
    
    // WHY: Show success message when reset email is sent
    val successMessage = when (authState) {
        is AuthUiState.PasswordResetSent -> "Password reset email sent! Check your inbox."
        else -> null
    }
    
    // WHY: Show error message when reset fails
    val errorMessage = when (authState) {
        is AuthUiState.Error -> (authState as AuthUiState.Error).message
        else -> null
    }
    
    val isLoading = authState is AuthUiState.Loading

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(Dimens.PaddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo/Brand
            Image(
                painter = painterResource(id = R.drawable.mainapplogo),
                contentDescription = "Zenvy Logo",
                modifier = Modifier.size(120.dp)
            )

        Spacer(modifier = Modifier.height(Dimens.PaddingExtraLarge))

        // Title
        Text(
            text = "Forgot Password",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

        // Description
        Text(
            text = "Enter your email address and we'll send you a link to reset your password",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

        // Email Input
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !isLoading,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(Dimens.PaddingSmall))

        // Success Message
        if (successMessage != null) {
            Text(
                text = successMessage,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
        }

        // Error Message
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
        }

        Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

        // Send Reset Email Button
        Button(
            onClick = { viewModel.resetPassword(email) },
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.ButtonHeightLarge),
            shape = MaterialTheme.shapes.medium,
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(
                    text = "Send Reset Email",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

        // Navigate to Login
        TextButton(
            onClick = {
                viewModel.resetState()
                onNavigateToLogin()
            },
            enabled = !isLoading
        ) {
            Text(
                text = "Back to Login",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
        }
    }
}

// WHY: Previews removed because they require Hilt ViewModel injection
// Test screens in running app instead
