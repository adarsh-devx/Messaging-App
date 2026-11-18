package dev.floww.android.features.rooms

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.chrisbanes.haze.HazeDefaults
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import dev.floww.android.core.glass.glassBorder
import dev.floww.android.core.icons.FlowwIcons
import dev.floww.android.core.theme.FlowwColors
import kotlin.math.abs

// Daylight Soft Ice Sky-Blue Palette (Matches Welcome & Auth Screen)
private val ColorCanvasTop = Color(0xFFF1F6FB)
private val ColorCanvasBottom = Color(0xFFDCEBFA)
private val ColorBrandBlue = Color(0xFF0284C7)
private val ColorBrandBlueDark = Color(0xFF0369A1)
private val ColorTextPrimary = Color(0xFF0F172A)
private val ColorTextSecondary = Color(0xFF64748B)
private val ColorTextTimestamp = Color(0xFF94A3B8)
private val ColorGlassPillInactive = Color.White.copy(alpha = 0.60f)
private val ColorGlassPillBorder = Color.White.copy(alpha = 0.80f)
private val ColorSurfaceDock = Color.White.copy(alpha = 0.70f)
private val ColorDivider = Color(0xFFE2E8F0)

@Composable
fun RoomsScreen(
    viewModel: RoomsScreenViewModel,
    onNavigateToChat: (roomId: String, roomName: String) -> Unit,
    onNavigateToSettings: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val hazeState = remember { HazeState() }

    LaunchedEffect(viewModel) {
        viewModel.actions.collect { action ->
            when (action) {
                is RoomsScreenActions.OpenChatRoom -> onNavigateToChat(action.roomId, action.roomName)
                is RoomsScreenActions.OpenCreateRoomSheet -> { /* New room action */ }
                is RoomsScreenActions.OpenProfileSheet -> onNavigateToSettings()
                is RoomsScreenActions.ShowToast -> { /* Snack */ }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(ColorCanvasTop, ColorCanvasBottom)
                )
            )
    ) {
        // Main Scrollable Area with Haze Blur Source for Liquid Glass Navigation
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .haze(hazeState),
            contentPadding = PaddingValues(bottom = 104.dp)
        ) {
            // 1. WhatsApp-Style Top Bar: "Floww" on left, [ ⋮ ] on right (no rupee icon)
            item {
                FlowwBrandTopBar(
                    onMenuClick = { viewModel.sendEvent(RoomsScreenEvents.ProfileMenuClicked) }
                )
            }

            // Optional Search Bar (shown when Search is toggled from floating dock)
            item {
                AnimatedVisibility(
                    visible = state.isSearching,
                    enter = fadeIn(tween(180)),
                    exit = fadeOut(tween(180))
                ) {
                    SearchInputBar(
                        searchQuery = state.searchQuery,
                        onQueryChanged = { viewModel.sendEvent(RoomsScreenEvents.SearchQueryChanged(it)) },
                        onClose = { viewModel.sendEvent(RoomsScreenEvents.ToggleSearch) }
                    )
                }
            }

            // 2. Category Filter Capsules: [ All ] [ Chats ] [ Grups ] [ Favorite ]
            item {
                Spacer(modifier = Modifier.height(8.dp))
                CategoryPillsSection(
                    activeFilter = state.activeFilter,
                    onSelect = { viewModel.sendEvent(RoomsScreenEvents.FilterSelected(it)) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // 4. Chat List Rows
            items(
                items = state.filteredChats,
                key = { it.id }
            ) { chat ->
                ChatRowItem(
                    item = chat,
                    onClick = { viewModel.sendEvent(RoomsScreenEvents.RoomClicked(chat.id, chat.name)) }
                )
            }
        }

        // 5. Floating Capsule Dock + Add/Create Circle (Frosted Liquid Glass UI)
        FloatingDockNavigation(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(start = 18.dp, end = 18.dp, bottom = 14.dp),
            hazeState = hazeState,
            activeTab = state.activeBottomTab,
            onSelect = { viewModel.sendEvent(RoomsScreenEvents.BottomTabSelected(it)) },
            onAddClick = { viewModel.sendEvent(RoomsScreenEvents.CreateRoomClicked) }
        )
    }
}

/**
 * WhatsApp-Style Top Header Bar:
 * Left: "Floww" (Bold brand title)
 * Right: [ ⋮ ] (Three vertical dots menu button, exact position aligned to right content edge at red mark)
 */
@Composable
private fun FlowwBrandTopBar(
    onMenuClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 0.dp, top = 14.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // "Floww" in Soft Sky-Blue Brand Typography
        Text(
            text = "Floww",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = ColorBrandBlue,
            letterSpacing = (-0.3).sp
        )

        // Vertical Three Dots [ ⋮ ] - aligned to exact right margin matching red mark (~18px from canvas edge)
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .clickable(onClick = onMenuClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Menu",
                tint = ColorTextSecondary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

/**
 * 3. Category Filter Capsules:
 * Frosted Glass Pills (Daylight Soft Ice theme).
 */
@Composable
private fun CategoryPillsSection(
    activeFilter: RoomFilterTab,
    onSelect: (RoomFilterTab) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(RoomFilterTab.entries) { tab ->
            val isSelected = tab == activeFilter
            val pillShape = RoundedCornerShape(12.dp)

            Box(
                modifier = Modifier
                    .height(26.dp)
                    .clip(pillShape)
                    .background(
                        if (isSelected) ColorBrandBlue else ColorGlassPillInactive,
                        pillShape
                    )
                    .border(
                        width = 1.dp,
                        color = if (isSelected) ColorBrandBlue else ColorGlassPillBorder,
                        shape = pillShape
                    )
                    .clickable { onSelect(tab) }
                    .padding(horizontal = 10.5.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = tab.label,
                    fontSize = 10.5.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                    color = if (isSelected) Color.White else ColorTextSecondary
                )
            }
        }
    }
}

/**
 * 4. Chat List Row Item:
 * Tight padding (16dp horiz, 10dp vert), 50dp avatar, crisp typography and 18dp badges.
 */
@Composable
private fun ChatRowItem(
    item: ChatMessageItem,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (isPressed) 0.985f else 1f,
        animationSpec = spring(stiffness = 500f),
        label = "row_scale"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 9.5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar Circle (50dp)
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Color(item.avatarBgColor)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.name.take(1).uppercase(),
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = ColorTextPrimary
            )
        }

        Spacer(modifier = Modifier.width(13.dp))

        // Center Content: Name & Subtitle
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = item.name,
                fontSize = 14.5.sp,
                fontWeight = FontWeight.Bold,
                color = ColorTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(3.dp))

            if (item.isTyping) {
                Text(
                    text = "Typing...",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Normal,
                    fontStyle = FontStyle.Italic,
                    color = ColorTextSecondary
                )
            } else {
                Text(
                    text = item.lastMessage,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Normal,
                    color = ColorTextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Right Column: Time & Status
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = item.timestamp,
                fontSize = 11.sp,
                fontWeight = FontWeight.Normal,
                color = ColorTextTimestamp
            )

            if (item.unreadCount > 0) {
                // Sky Blue Badge (18dp circle)
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(ColorBrandBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.unreadCount.toString(),
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            } else if (item.isDoubleTick) {
                Icon(
                    imageVector = Icons.Default.DoneAll,
                    contentDescription = "Read",
                    tint = ColorBrandBlue,
                    modifier = Modifier.size(15.dp)
                )
            } else if (item.isSingleTick) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = "Sent",
                    tint = ColorTextTimestamp,
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }
}

/**
 * Search input bar revealed when user taps the Search button in the floating dock.
 */
@Composable
private fun SearchInputBar(
    searchQuery: String,
    onQueryChanged: (String) -> Unit,
    onClose: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .height(42.dp)
            .clip(RoundedCornerShape(21.dp))
            .background(ColorSurfaceCircle)
            .border(1.dp, Color(0x1AFFFFFF), RoundedCornerShape(21.dp))
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = ColorNeonLime,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        BasicTextField(
            value = searchQuery,
            onValueChange = onQueryChanged,
            modifier = Modifier.weight(1f),
            textStyle = TextStyle(
                color = ColorTextPrimary,
                fontSize = 14.sp
            ),
            cursorBrush = SolidColor(ColorNeonLime),
            singleLine = true,
            decorationBox = { innerTextField ->
                if (searchQuery.isEmpty()) {
                    Text(
                        text = "Search chats or people...",
                        color = ColorTextSecondary,
                        fontSize = 13.5.sp
                    )
                }
                innerTextField()
            }
        )
        if (searchQuery.isNotEmpty()) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Clear",
                tint = ColorTextSecondary,
                modifier = Modifier
                    .size(18.dp)
                    .clickable { onQueryChanged("") }
            )
        }
    }
}

/**
 * Organic Liquid Physics Spring & Stretch Constants.
 * Damping 0.76 + stiffness 280 for ultra-smooth fluid motion without jarring stops.
 * Continuous sinusoidal liquid stretch peaking at transit midpoint.
 */
private val LiquidGlassSpring = spring<Float>(
    dampingRatio = 0.76f,
    stiffness = 280f
)
private const val MAX_STRETCH = 0.20f
private const val SQUASH_RATIO = 0.35f

/**
 * 5. Floating Dock Navigation (Dial-Inspired Liquid Glass UI):
 * - Left rounded capsule container with [Chat, Calling, Profile] tabs.
 * - Real-time background blur via HazeChild with daylight glass tint and specular lit rim.
 * - Dial's animated liquid stretch selection indicator moving with smooth LiquidGlassSpring.
 * - FlowwIcons: Line (Outline) to Fill state transitions upon tab selection.
 * - Right separate floating circular glass button with + (Plus) action.
 */
@Composable
private fun FloatingDockNavigation(
    modifier: Modifier = Modifier,
    hazeState: HazeState? = null,
    activeTab: RoomsBottomTab,
    onSelect: (RoomsBottomTab) -> Unit,
    onAddClick: () -> Unit
) {
    val capsuleShape = RoundedCornerShape(29.dp)
    val circleShape = CircleShape
    val density = LocalDensity.current

    val tabs = RoomsBottomTab.entries
    val n = tabs.size
    val selectedIndex = activeTab.ordinal

    var rowSize by remember { mutableStateOf(IntSize.Zero) }
    val tabWidthPx = if (rowSize.width > 0 && n > 0) rowSize.width.toFloat() / n else 0f
    val pillTargetPx = selectedIndex * tabWidthPx

    var jumpDistancePx by remember { mutableFloatStateOf(1f) }

    val animatedPillOffset by androidx.compose.animation.core.animateFloatAsState(
        targetValue = pillTargetPx,
        animationSpec = LiquidGlassSpring,
        label = "liquidPillOffset"
    )

    LaunchedEffect(pillTargetPx) {
        val dist = abs(pillTargetPx - animatedPillOffset)
        jumpDistancePx = if (dist > 1f) dist else (if (tabWidthPx > 0f) tabWidthPx else 1f)
    }

    // Continuous sinusoidal bell curve:
    // Starts at 0, smoothly peaks at midpoint where velocity is highest, lands softly at 0
    val transitDist = abs(pillTargetPx - animatedPillOffset)
    val transitFraction = if (jumpDistancePx > 0f) {
        (transitDist / jumpDistancePx).coerceIn(0f, 1f)
    } else 0f
    val stretchTarget = kotlin.math.sin(transitFraction * Math.PI.toFloat())

    val animatedStretch by androidx.compose.animation.core.animateFloatAsState(
        targetValue = stretchTarget,
        animationSpec = spring(dampingRatio = 0.80f, stiffness = 320f),
        label = "liquidStretch"
    )

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Left Floating Frosted Glass Capsule Dock (Chat, Calling, Profile)
        val capsuleModifier = Modifier
            .weight(1f)
            .height(58.dp)
            .shadow(
                elevation = 16.dp,
                shape = capsuleShape,
                ambientColor = Color(0xFF0F172A).copy(alpha = 0.12f),
                spotColor = Color(0xFF0F172A).copy(alpha = 0.10f)
            )
            .clip(capsuleShape)

        val capsuleBlurredModifier = if (hazeState != null) {
            capsuleModifier.hazeChild(
                state = hazeState,
                shape = capsuleShape,
                style = HazeDefaults.style(
                    backgroundColor = Color.White.copy(alpha = 0.68f),
                    tint = HazeTint(Color(0xFFE0EDFA).copy(alpha = 0.35f)),
                    blurRadius = 26.dp,
                    noiseFactor = 0.01f
                )
            )
        } else {
            capsuleModifier.background(Color.White.copy(alpha = 0.85f), capsuleShape)
        }

        Box(
            modifier = capsuleBlurredModifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.70f),
                            Color.White.copy(alpha = 0.35f)
                        )
                    ),
                    shape = capsuleShape
                )
                .glassBorder(
                    width = 1.dp,
                    shape = capsuleShape,
                    startColor = Color.White,
                    endColor = Color.White.copy(alpha = 0.5f)
                )
                .padding(horizontal = 6.dp, vertical = 5.dp)
        ) {
            // Dial's Liquid Stretch Selection Pill (Slides & Deforms smoothly)
            if (tabWidthPx > 0f) {
                Box(
                    modifier = Modifier
                        .width(with(density) { tabWidthPx.toDp() })
                        .height(48.dp)
                        .padding(horizontal = 3.dp, vertical = 2.dp)
                        .graphicsLayer {
                            translationX = animatedPillOffset
                            // Liquid deformation: smoothly stretches horizontally, softly squashes vertically in transit!
                            scaleX = 1f + animatedStretch * MAX_STRETCH
                            scaleY = 1f - animatedStretch * (MAX_STRETCH * SQUASH_RATIO)
                        }
                        .clip(RoundedCornerShape(24.dp))
                        .background(ColorBrandBlue)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(24.dp),
                            ambientColor = ColorBrandBlue.copy(alpha = 0.35f),
                            spotColor = ColorBrandBlue.copy(alpha = 0.25f)
                        )
                )
            }

            // Tab Icons Row
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .onSizeChanged { rowSize = it },
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEach { tab ->
                    val isSelected = tab == activeTab
                    val interactionSource = remember { MutableInteractionSource() }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null,
                                onClick = { onSelect(tab) }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Box {
                            // Line (Outline) when unselected -> Fill (Solid) when selected
                            val icon = when (tab) {
                                RoomsBottomTab.CHAT -> if (isSelected) FlowwIcons.ChatFilled else FlowwIcons.ChatOutline
                                RoomsBottomTab.CALLING -> if (isSelected) FlowwIcons.CallingFilled else FlowwIcons.CallingOutline
                                RoomsBottomTab.PROFILE -> if (isSelected) FlowwIcons.ProfileFilled else FlowwIcons.ProfileOutline
                            }

                            val iconScale by androidx.compose.animation.core.animateFloatAsState(
                                targetValue = if (isSelected) 1.05f else 0.95f,
                                animationSpec = spring(dampingRatio = 0.70f, stiffness = 380f),
                                label = "tabIconScale_${tab.name}"
                            )

                            Icon(
                                imageVector = icon,
                                contentDescription = tab.label,
                                tint = if (isSelected) Color.White else ColorTextSecondary,
                                modifier = Modifier
                                    .scale(iconScale)
                                    .size(24.dp)
                            )

                            // Unread Badge (e.g. 9 on Chat)
                            if (tab.badgeCount > 0) {
                                Box(
                                    modifier = Modifier
                                        .offset(x = 13.dp, y = (-6).dp)
                                        .size(15.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) Color.White else ColorBrandBlue),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = tab.badgeCount.toString(),
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) ColorBrandBlue else Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Right Separate Floating Frosted Glass Circle with + (Plus) Action
        val circleModifier = Modifier
            .size(58.dp)
            .shadow(
                elevation = 16.dp,
                shape = circleShape,
                ambientColor = Color(0xFF0F172A).copy(alpha = 0.12f),
                spotColor = Color(0xFF0F172A).copy(alpha = 0.10f)
            )
            .clip(circleShape)

        val circleBlurredModifier = if (hazeState != null) {
            circleModifier.hazeChild(
                state = hazeState,
                shape = circleShape,
                style = HazeDefaults.style(
                    backgroundColor = Color.White.copy(alpha = 0.68f),
                    tint = HazeTint(Color(0xFFE0EDFA).copy(alpha = 0.35f)),
                    blurRadius = 26.dp,
                    noiseFactor = 0.01f
                )
            )
        } else {
            circleModifier.background(Color.White.copy(alpha = 0.85f), circleShape)
        }

        val addInteractionSource = remember { MutableInteractionSource() }
        val isAddPressed by addInteractionSource.collectIsPressedAsState()
        val addScale by androidx.compose.animation.core.animateFloatAsState(
            targetValue = if (isAddPressed) 0.90f else 1f,
            animationSpec = spring(dampingRatio = 0.7f, stiffness = 400f),
            label = "addScale"
        )

        Box(
            modifier = circleBlurredModifier
                .scale(addScale)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.70f),
                            Color.White.copy(alpha = 0.35f)
                        )
                    ),
                    shape = circleShape
                )
                .glassBorder(
                    width = 1.dp,
                    shape = circleShape,
                    startColor = Color.White,
                    endColor = Color.White.copy(alpha = 0.5f)
                )
                .clickable(
                    interactionSource = addInteractionSource,
                    indication = null,
                    onClick = onAddClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = FlowwIcons.Plus,
                contentDescription = "New Chat",
                tint = ColorBrandBlue,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
