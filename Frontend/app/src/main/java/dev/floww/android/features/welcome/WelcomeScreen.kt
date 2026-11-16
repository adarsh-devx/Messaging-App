package dev.floww.android.features.welcome

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.floww.android.core.glass.LivingAuraBackground

// Clean Luxury Sky Blue Palette
private val ColorSkyBlue = Color(0xFF0284C7)
private val ColorForestBlack = Color(0xFF031916)
private val ColorSubtitle = Color(0xFF2E3530)
private val ColorDisclaimer = Color(0xFF475569)

@Composable
fun WelcomeScreen(
    viewModel: WelcomeScreenViewModel? = null,
    onNavigateToAuth: (isRegister: Boolean) -> Unit
) {
    LaunchedEffect(viewModel) {
        viewModel?.actions?.collect { action ->
            when (action) {
                is WelcomeScreenActions.NavigateToAuth -> onNavigateToAuth(action.isRegister)
                is WelcomeScreenActions.ShowInfoDialog -> { /* Dialog preview */ }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Edge-to-Edge Living Daylight Aura Background
        LivingAuraBackground(
            isDark = false,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 2. Clean Unboxed Brand Header (No pill capsule, pure floating typography)
            TopBrandSection()

            // 3. Main Center Content with Liquid Floating Avatar Wave & Spring Physics
            MainContentSection(
                onGetStartedClick = {
                    if (viewModel != null) {
                        viewModel.sendEvent(WelcomeScreenEvents.GetStartedClicked)
                    } else {
                        onNavigateToAuth(false)
                    }
                }
            )

            // 4. Minimalist Legal Disclaimer
            MinimalistLegalDisclaimer()
        }
    }
}

/**
 * Clean Unboxed Brand Header (No Pill Container)
 */
@Composable
private fun TopBrandSection() {
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
            .padding(start = 24.dp, top = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
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
        Text(
            text = "•",
            fontSize = 10.sp,
            color = Color.Gray
        )
        Text(
            text = "Disappearing Moments",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = ColorSubtitle
        )
    }
}

@Composable
private fun MainContentSection(onGetStartedClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Ultra-Crisp Balanced Vector Avatar Badges Arc with Liquid Floating Wave Motion
        AvatarBadgesRow()

        Spacer(modifier = Modifier.height(26.dp))

        // Forward-Bent Italic Display Headline
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.graphicsLayer { rotationZ = -2.0f }
        ) {
            Text(
                text = "Let's get you",
                fontSize = 38.sp,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                color = ColorForestBlack,
                letterSpacing = (-1.0).sp,
                textAlign = TextAlign.Center,
                lineHeight = 40.sp
            )
            Text(
                text = "logged in.",
                fontSize = 46.sp,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                color = ColorSkyBlue,
                letterSpacing = (-1.0).sp,
                textAlign = TextAlign.Center,
                lineHeight = 48.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Semi-Bold Subtitle
        Text(
            text = "Start finding your favourites and earning exclusive discounts. It only takes a minute.",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = ColorSubtitle,
            lineHeight = 19.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(44.dp))

        // Primary Action Button with Spring Touch & Kinetic Arrow Nudge
        AnimatedGetStartedButton(onClick = onGetStartedClick)
    }
}

@Composable
private fun AnimatedGetStartedButton(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = 500f),
        label = "btn_spring_press"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "arrow_nudge")
    val arrowOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "arrow_offset"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .scale(scale)
            .clip(CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 10.dp, horizontal = 20.dp)
    ) {
        Text(
            text = "Get Started",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = ColorSkyBlue
        )
        Spacer(modifier = Modifier.size(6.dp))
        Text(
            text = "→",
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            color = ColorSkyBlue,
            modifier = Modifier.offset(x = arrowOffset.dp)
        )
    }
}

@Composable
private fun AvatarBadgesRow() {
    val infiniteTransition = rememberInfiniteTransition(label = "avatar_liquid_wave")

    val wavePhase1 by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wave_phase_1"
    )

    val wavePhase2 by infiniteTransition.animateFloat(
        initialValue = 3f,
        targetValue = -3f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wave_phase_2"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        BadgeCircle(size = 48, icon = "🎁", color = Color(0xFF831843), dynamicOffset = wavePhase1, baseOffset = (-3))
        BadgeCircle(size = 50, icon = "🛍️", color = Color(0xFFF43F5E), dynamicOffset = wavePhase2, baseOffset = 3, negativeMargin = (-12))
        BadgeCircle(size = 52, icon = "💬", color = Color(0xFF38BDF8), dynamicOffset = wavePhase1, baseOffset = (-1), negativeMargin = (-12))
        
        BadgeCircle(
            size = 60,
            icon = "👨‍🍳",
            color = Color(0xFFF43F5E),
            dynamicOffset = wavePhase2 * 0.7f,
            baseOffset = (-6),
            negativeMargin = (-12),
            isCenter = true
        )

        BadgeCircle(size = 52, icon = "🍹", color = Color(0xFFEAB308), dynamicOffset = wavePhase1, baseOffset = (-1), negativeMargin = (-12))
        BadgeCircle(size = 50, icon = "🍔", color = Color(0xFF6366F1), dynamicOffset = wavePhase2, baseOffset = 3, negativeMargin = (-12))
        BadgeCircle(size = 48, icon = "🍲", color = Color(0xFFDC2626), dynamicOffset = wavePhase1, baseOffset = (-3), negativeMargin = (-12))
    }
}

@Composable
private fun BadgeCircle(
    size: Int,
    icon: String,
    color: Color,
    dynamicOffset: Float = 0f,
    baseOffset: Int = 0,
    negativeMargin: Int = 0,
    isCenter: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val tapScale by animateFloatAsState(
        targetValue = if (isPressed) 1.25f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 400f),
        label = "bubble_tap"
    )

    Box(
        modifier = Modifier
            .offset(x = negativeMargin.dp, y = (baseOffset + dynamicOffset).dp)
            .size(size.dp)
            .scale(tapScale)
            .shadow(if (isCenter) 10.dp else 4.dp, CircleShape)
            .clip(CircleShape)
            .background(color)
            .border(if (isCenter) 3.5.dp else 3.dp, Color.White, CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {}
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = icon,
            fontSize = if (isCenter) 24.sp else 18.sp
        )
    }
}

@Composable
private fun MinimalistLegalDisclaimer() {
    val disclaimerText = buildAnnotatedString {
        append("By continuing you agree to our ")
        withStyle(SpanStyle(fontWeight = FontWeight.Bold, textDecoration = TextDecoration.Underline, color = ColorSkyBlue)) {
            append("T&Cs")
        }
        append(". Please also check out our ")
        withStyle(SpanStyle(fontWeight = FontWeight.Bold, textDecoration = TextDecoration.Underline, color = ColorSkyBlue)) {
            append("Privacy Policy")
        }
        append(". We use your data to offer you a personalised experience. For more information ")
        withStyle(SpanStyle(fontWeight = FontWeight.Bold, textDecoration = TextDecoration.Underline, color = ColorSkyBlue)) {
            append("see here")
        }
        append(".")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 28.dp, vertical = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = disclaimerText,
            fontSize = 11.5.sp,
            lineHeight = 16.sp,
            color = ColorDisclaimer,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(14.dp))
        Box(
            modifier = Modifier
                .size(width = 120.dp, height = 4.dp)
                .clip(CircleShape)
                .background(Color(0xFF1E293B).copy(alpha = 0.35f))
        )
    }
}
