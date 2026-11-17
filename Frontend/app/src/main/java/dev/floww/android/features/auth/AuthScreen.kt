package dev.floww.android.features.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.floww.android.core.glass.GlassTextField

private val ColorSkyBlue = Color(0xFF0284C7)
private val ColorSkyBlueDark = Color(0xFF0369A1)
private val ColorForestBlack = Color(0xFF031916)
private val ColorSubtitle = Color(0xFF2E3530)
private val ColorError = Color(0xFFEF4444)

@Composable
fun AuthScreen(
    viewModel: AuthScreenViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToRooms: (userId: String, username: String) -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.actions.collect { action ->
            when (action) {
                is AuthScreenActions.NavigateBack -> onNavigateBack()
                is AuthScreenActions.NavigateToRooms -> onNavigateToRooms(action.userId, action.username)
                is AuthScreenActions.ShowToast -> { /* Can trigger snackbar */ }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF1F6FB), // Ultra soft ice tint at top
                        Color(0xFFE3EDF8), // Gentle single-tone sky tint mid
                        Color(0xFFD2E4F4)  // Soft sky blue anchor bottom
                    )
                )
            )
    ) {
        // Main Scrollable Content with IME Keyboard Padding
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // Top Bar: Back Button + Floww Brand
                AuthTopBar(
                    onBackClick = { viewModel.sendEvent(AuthScreenEvents.BackClicked) }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Bold Oblique Display Headline
                AuthHeadlineSection(isRegisterMode = state.isRegisterMode)

                Spacer(modifier = Modifier.height(24.dp))

                // Elevated Frosted Glass Card Container for Auth Form
                val formCardShape = RoundedCornerShape(26.dp)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 14.dp,
                            shape = formCardShape,
                            ambientColor = Color(0xFF031916).copy(alpha = 0.06f),
                            spotColor = ColorSkyBlue.copy(alpha = 0.14f)
                        )
                        .clip(formCardShape)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.92f),
                                    Color.White.copy(alpha = 0.82f)
                                )
                            ),
                            shape = formCardShape
                        )
                        .border(
                            width = 1.5.dp,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.White,
                                    Color.White.copy(alpha = 0.65f)
                                )
                            ),
                            shape = formCardShape
                        )
                        .padding(horizontal = 20.dp, vertical = 22.dp)
                ) {
                    Column {
                        // Floating Segmented Switcher: [ Log In | Register ]
                        AuthSegmentedSwitcher(
                            isRegisterMode = state.isRegisterMode,
                            onSwitchMode = { isReg -> viewModel.sendEvent(AuthScreenEvents.SwitchMode(isReg)) }
                        )

                        Spacer(modifier = Modifier.height(22.dp))

                        // Error Message Pill (if present)
                        AnimatedVisibility(
                            visible = state.errorMessage != null,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            state.errorMessage?.let { msg ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 16.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(ColorError.copy(alpha = 0.10f))
                                        .border(1.dp, ColorError.copy(alpha = 0.30f), RoundedCornerShape(14.dp))
                                        .padding(horizontal = 14.dp, vertical = 10.dp)
                                ) {
                                    Text(
                                        text = msg,
                                        fontSize = 12.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = ColorError,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }

                        // Dynamic Input Form
                        AnimatedContent(
                            targetState = state.isRegisterMode,
                            transitionSpec = { fadeIn(tween(220)) togetherWith fadeOut(tween(180)) },
                            label = "auth_form_transition"
                        ) { isRegister ->
                            if (isRegister) {
                                RegisterFormFields(
                                    state = state,
                                    viewModel = viewModel
                                )
                            } else {
                                LoginFormFields(
                                    state = state,
                                    viewModel = viewModel
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Primary Action Button inside the Card
                        AuthPrimaryButton(
                            text = if (state.isRegisterMode) "Create Account" else "Continue",
                            isLoading = state.isLoading,
                            onClick = { viewModel.sendEvent(AuthScreenEvents.SubmitClicked) }
                        )
                    }
                }
            }

            // Bottom Spacing
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/**
 * Top Navigation Bar with Back Button and Subtle Rotating Brand Star
 */
@Composable
private fun AuthTopBar(onBackClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "star_spin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "star_rotate"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Frosted Glass Circle Back Button
        Box(
            modifier = Modifier
                .size(40.dp)
                .shadow(2.dp, CircleShape)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.50f))
                .border(1.dp, Color.White.copy(alpha = 0.85f), CircleShape)
                .clickable(onClick = onBackClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = ColorForestBlack,
                modifier = Modifier.size(18.dp)
            )
        }

        // Clean Brand Center Icon
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "✦",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = ColorSkyBlue,
                modifier = Modifier.rotate(rotation)
            )
            Text(
                text = "Floww",
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = ColorForestBlack,
                letterSpacing = (-0.3).sp
            )
        }

        Spacer(modifier = Modifier.size(40.dp))
    }
}

/**
 * Floating Segmented Switcher Capsule: [ Log In | Register ]
 */
@Composable
private fun AuthSegmentedSwitcher(
    isRegisterMode: Boolean,
    onSwitchMode: (Boolean) -> Unit
) {
    val trackShape = RoundedCornerShape(20.dp)

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(trackShape)
                .background(Color(0xFFE2E8F0).copy(alpha = 0.55f), trackShape)
                .border(1.dp, Color(0xFFCBD5E1).copy(alpha = 0.50f), trackShape)
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Log In Tab
            val loginBgColor by animateColorAsState(
                targetValue = if (!isRegisterMode) Color.White else Color.Transparent,
                animationSpec = spring(stiffness = 400f),
                label = "login_tab_bg"
            )
            val loginTextColor by animateColorAsState(
                targetValue = if (!isRegisterMode) ColorForestBlack else Color(0xFF64748B),
                animationSpec = tween(200),
                label = "login_tab_text"
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .then(
                        if (!isRegisterMode) {
                            Modifier.shadow(2.dp, trackShape, ambientColor = Color(0x15000000), spotColor = Color(0x20000000))
                        } else Modifier
                    )
                    .clip(trackShape)
                    .background(loginBgColor, trackShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onSwitchMode(false) }
                    )
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Log In",
                    fontSize = 13.5.sp,
                    fontWeight = if (!isRegisterMode) FontWeight.ExtraBold else FontWeight.SemiBold,
                    color = loginTextColor
                )
            }

            // Register Tab
            val registerBgColor by animateColorAsState(
                targetValue = if (isRegisterMode) Color.White else Color.Transparent,
                animationSpec = spring(stiffness = 400f),
                label = "reg_tab_bg"
            )
            val registerTextColor by animateColorAsState(
                targetValue = if (isRegisterMode) ColorForestBlack else Color(0xFF64748B),
                animationSpec = tween(200),
                label = "reg_tab_text"
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .then(
                        if (isRegisterMode) {
                            Modifier.shadow(2.dp, trackShape, ambientColor = Color(0x15000000), spotColor = Color(0x20000000))
                        } else Modifier
                    )
                    .clip(trackShape)
                    .background(registerBgColor, trackShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onSwitchMode(true) }
                    )
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Register",
                    fontSize = 13.5.sp,
                    fontWeight = if (isRegisterMode) FontWeight.ExtraBold else FontWeight.SemiBold,
                    color = registerTextColor
                )
            }
        }
    }
}

/**
 * Slanted Display Headline Section
 */
@Composable
private fun AuthHeadlineSection(isRegisterMode: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { rotationZ = -2.0f }
    ) {
        Text(
            text = if (isRegisterMode) "Create your" else "Let's get you",
            fontSize = 36.sp,
            fontWeight = FontWeight.Black,
            fontStyle = FontStyle.Italic,
            color = ColorForestBlack,
            letterSpacing = (-1.0).sp,
            lineHeight = 38.sp
        )
        Text(
            text = if (isRegisterMode) "identity." else "signed in.",
            fontSize = 42.sp,
            fontWeight = FontWeight.Black,
            fontStyle = FontStyle.Italic,
            color = ColorSkyBlue,
            letterSpacing = (-1.0).sp,
            lineHeight = 44.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (isRegisterMode) {
                "Pick your unique handle and start private disappearing conversations."
            } else {
                "Access your encrypted ephemeral inbox and active secret rooms."
            },
            fontSize = 13.5.sp,
            fontWeight = FontWeight.SemiBold,
            color = ColorSubtitle,
            lineHeight = 18.sp
        )
    }
}

/**
 * Login Fields (Username/Email + Password)
 */
@Composable
private fun LoginFormFields(
    state: AuthScreenState,
    viewModel: AuthScreenViewModel
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        GlassTextField(
            value = state.username,
            onValueChange = { viewModel.sendEvent(AuthScreenEvents.UsernameChanged(it)) },
            placeholder = "Username or Email",
            leadingIcon = Icons.Default.Person,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        GlassTextField(
            value = state.password,
            onValueChange = { viewModel.sendEvent(AuthScreenEvents.PasswordChanged(it)) },
            placeholder = "Password",
            leadingIcon = Icons.Default.Lock,
            isPassword = true,
            isPasswordVisible = state.isPasswordVisible,
            onTogglePasswordVisibility = { viewModel.sendEvent(AuthScreenEvents.TogglePasswordVisibility) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Forgot password?",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = ColorSkyBlue,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .clickable { /* Future password recovery */ }
                    .padding(vertical = 4.dp, horizontal = 2.dp)
            )
        }
    }
}

/**
 * Register Fields (Full Name + Username + Email + Password)
 */
@Composable
private fun RegisterFormFields(
    state: AuthScreenState,
    viewModel: AuthScreenViewModel
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        GlassTextField(
            value = state.displayName,
            onValueChange = { viewModel.sendEvent(AuthScreenEvents.DisplayNameChanged(it)) },
            placeholder = "Full Name (e.g. Aditya)",
            leadingIcon = Icons.Default.Person,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )

        GlassTextField(
            value = state.username,
            onValueChange = { viewModel.sendEvent(AuthScreenEvents.UsernameChanged(it)) },
            placeholder = "Username (e.g. @aditya_01)",
            leadingIcon = Icons.Default.Person,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )

        GlassTextField(
            value = state.email,
            onValueChange = { viewModel.sendEvent(AuthScreenEvents.EmailChanged(it)) },
            placeholder = "Email address",
            leadingIcon = Icons.Default.Email,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        GlassTextField(
            value = state.password,
            onValueChange = { viewModel.sendEvent(AuthScreenEvents.PasswordChanged(it)) },
            placeholder = "Password (min 8 chars)",
            leadingIcon = Icons.Default.Lock,
            isPassword = true,
            isPasswordVisible = state.isPasswordVisible,
            onTogglePasswordVisibility = { viewModel.sendEvent(AuthScreenEvents.TogglePasswordVisibility) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )
    }
}

/**
 * Primary Action Button with Spring Touch & Kinetic Arrow
 */
@Composable
private fun AuthPrimaryButton(
    text: String,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = 500f),
        label = "btn_press_scale"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "arrow_bounce")
    val arrowOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "arrow_offset"
    )

    val buttonShape = RoundedCornerShape(22.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .shadow(12.dp, buttonShape, ambientColor = ColorSkyBlue.copy(alpha = 0.30f), spotColor = ColorSkyBlue.copy(alpha = 0.45f))
            .clip(buttonShape)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(ColorSkyBlue, ColorSkyBlueDark)
                ),
                shape = buttonShape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = !isLoading,
                onClick = onClick
            )
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 2.5.dp,
                modifier = Modifier.size(20.dp)
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = text,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "→",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    modifier = Modifier.offset(x = arrowOffset.dp)
                )
            }
        }
    }
}
