package dev.floww.android.features.welcome

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.floww.android.core.glass.FloatingGlassCapsule
import dev.floww.android.core.glass.GlassBadge
import dev.floww.android.core.glass.GlassButton
import dev.floww.android.core.glass.GlassButtonVariant
import dev.floww.android.core.glass.GlassCard
import dev.floww.android.core.glass.GlassIconButton
import dev.floww.android.core.glass.GlassSegmentedPill
import dev.floww.android.core.glass.LivingAuraBackground
import dev.floww.android.core.glass.glassBorder
import dev.floww.android.core.theme.FlowwColors
import dev.floww.android.core.theme.FlowwShapes
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun WelcomeScreen(
    viewModel: WelcomeScreenViewModel,
    onNavigateToAuth: (isRegister: Boolean) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val hazeState = remember { HazeState() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .haze(state = hazeState)
    ) {
        // 1. Ambient Breathing Living Aura Background
        LivingAuraBackground(modifier = Modifier.fillMaxSize())

        // 2. Foreground Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Top Header: Minimal Starburst Brand Mark + Menu Icon
            TopHeaderRow(
                onMenuClick = { viewModel.onEvent(WelcomeScreenEvents.InfoClicked) }
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Dynamic Editorial Headline with smooth animated transitions
            AnimatedContent(
                targetState = state.activeFeatureIndex,
                transitionSpec = {
                    fadeIn(animationSpec = tween(400)) togetherWith
                            fadeOut(animationSpec = tween(300))
                },
                label = "editorial_headline"
            ) { index ->
                val current = state.features[index]
                Column {
                    Text(
                        text = current.title,
                        style = MaterialTheme.typography.displayLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 44.sp,
                            letterSpacing = (-1.0).sp
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = current.subtitle,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = FlowwColors.TextSecondary,
                            lineHeight = 22.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Segmented Glass Filter Pills
            GlassSegmentedPill(
                items = listOf("Disappearing", "Zero Trace", "Liquid Glass"),
                selectedIndex = state.activeFeatureIndex,
                onSelectIndex = { viewModel.onEvent(WelcomeScreenEvents.FeatureSelected(it)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Bento-Style Glass Preview Card (Interactive Optical Glass Container)
            val activeFeature = state.features[state.activeFeatureIndex]
            GlassBentoPreviewCard(
                feature = activeFeature,
                hazeState = hazeState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Floating Bottom Action Capsule
            BottomFloatingActionCapsule(
                hazeState = hazeState,
                onGetStarted = { onNavigateToAuth(true) },
                onSignIn = { onNavigateToAuth(false) },
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(bottom = 12.dp)
            )
        }
    }
}

/**
 * Top Navigation Row: Starburst Geometric Brand Mark + Glass Circle Action
 */
@Composable
private fun TopHeaderRow(
    onMenuClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Ethereal 12-Ray Starburst Brand Icon
            StarburstBrandMark(modifier = Modifier.size(36.dp))

            Text(
                text = "Floww",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = (-0.5).sp
                )
            )
        }

        // 4-Dot Frosted Circle Button
        GlassIconButton(
            onClick = onMenuClick,
            size = 44.dp
        ) {
            Icon(
                imageVector = Icons.Default.MoreHoriz,
                contentDescription = "Options",
                tint = Color.White.copy(alpha = 0.85f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

/**
 * Procedural Starburst Brand Mark (Seen in user reference)
 */
@Composable
private fun StarburstBrandMark(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "starburst_subtle_glow")
    val alphaGlow by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "burst_alpha"
    )

    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val outerRadius = size.width / 2f * 0.95f
        val innerRadius = outerRadius * 0.45f
        val rayCount = 12
        val strokeWidth = 3.dp.toPx()

        for (i in 0 until rayCount) {
            val angleRad = (i * (360f / rayCount) * (Math.PI / 180f)).toFloat()
            val start = Offset(
                x = center.x + innerRadius * cos(angleRad),
                y = center.y + innerRadius * sin(angleRad)
            )
            val end = Offset(
                x = center.x + outerRadius * cos(angleRad),
                y = center.y + outerRadius * sin(angleRad)
            )
            drawLine(
                color = Color.White.copy(alpha = alphaGlow),
                start = start,
                end = end,
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
        }
        drawCircle(
            color = Color.White.copy(alpha = alphaGlow),
            radius = innerRadius * 0.5f,
            center = center
        )
    }
}

/**
 * Bento Glass Preview Card
 */
@Composable
private fun GlassBentoPreviewCard(
    feature: WelcomeFeature,
    hazeState: HazeState,
    modifier: Modifier = Modifier
) {
    GlassCard(
        hazeState = hazeState,
        modifier = modifier,
        shape = FlowwShapes.SquircleCard,
        backgroundColor = Color.White.copy(alpha = 0.07f),
        blurRadius = 26.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Card Top Row with Badge & Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlassBadge(
                    text = feature.badge,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = FlowwColors.FlowwCyan,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                )

                // Encrypted Lock Status
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Encrypted",
                        tint = FlowwColors.TextMuted,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "Encrypted",
                        fontSize = 11.sp,
                        color = FlowwColors.TextMuted,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Realistic Simulated Disappearing Bubble with Hairline Draining Ring
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(FlowwShapes.SquircleBento)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                FlowwColors.FlowwIris.copy(alpha = 0.22f),
                                Color(0xFF1D1733).copy(alpha = 0.40f)
                            )
                        ),
                        shape = FlowwShapes.SquircleBento
                    )
                    .glassBorder(
                        width = 1.dp,
                        shape = FlowwShapes.SquircleBento,
                        startColor = Color.White.copy(alpha = 0.28f),
                        endColor = Color.White.copy(alpha = 0.04f)
                    )
                    .padding(18.dp)
            ) {
                Column {
                    Text(
                        text = "Priyanshu",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = FlowwColors.FlowwIrisBright
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = feature.demoMessage,
                        fontSize = 14.sp,
                        color = Color.White,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Timestamp + 10dp Hairline Circular Draining Ring
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "16:42",
                            fontSize = 11.sp,
                            color = FlowwColors.TextMuted
                        )
                        Spacer(modifier = Modifier.width(6.dp))

                        // Hairline Circular Draining Ring
                        HairlineDrainingRing(
                            progress = 0.65f,
                            modifier = Modifier.size(10.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "✓✓",
                            fontSize = 10.sp,
                            color = FlowwColors.FlowwCyan,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Feature Highlights Micro Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GlassBadge(
                    text = "No Cloud Storage",
                    backgroundColor = Color.White.copy(alpha = 0.05f)
                )
                GlassBadge(
                    text = "RAM-Only Buffer",
                    backgroundColor = Color.White.copy(alpha = 0.05f)
                )
            }
        }
    }
}

/**
 * 10dp Hairline Circular Draining Ring (1.2dp stroke)
 */
@Composable
private fun HairlineDrainingRing(
    progress: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val strokeWidth = 1.2.dp.toPx()
        // Background track ring
        drawCircle(
            color = Color.White.copy(alpha = 0.20f),
            style = Stroke(width = strokeWidth)
        )
        // Foreground draining arc
        drawArc(
            color = FlowwColors.FlowwCyan,
            startAngle = -90f,
            sweepAngle = 360f * progress,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
    }
}

/**
 * Floating Bottom Action Capsule
 */
@Composable
private fun BottomFloatingActionCapsule(
    hazeState: HazeState,
    onGetStarted: () -> Unit,
    onSignIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Main Prominent Button
        GlassButton(
            onClick = onGetStarted,
            variant = GlassButtonVariant.Prominent,
            shape = FlowwShapes.SquirclePill,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Get Started",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Secondary Text Button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onSignIn)
                .padding(vertical = 4.dp, horizontal = 12.dp)
        ) {
            Text(
                text = "Already have an account? ",
                fontSize = 13.sp,
                color = FlowwColors.TextSecondary
            )
            Text(
                text = "Log in",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = FlowwColors.FlowwIrisBright
            )
        }
    }
}
