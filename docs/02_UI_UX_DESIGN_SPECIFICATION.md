# Floww — UI/UX Design Specification (Apple Liquid Glass System)

> **Document Version:** 1.0.0  
> **Aesthetic Philosophy:** Frosted VisionOS / Apple Liquid Glass meets Deep Cyber Obsidian  
> **Source Engine:** Battle-tested Compose Glass Engine from `d:\testmusic\Convx\`  
> **Target Framework:** Jetpack Compose + Material 3 + Chris Banes Haze  

---

## 1. Visual Language & Creative Thesis

Floww breaks free from standard flat Android Material Design. The interface treats light and depth as primary design materials:
* **Optical Depth:** Translucent surfaces that blur and refract the elements moving beneath them.
* **Specular Rim Lighting:** A crisp, razor-sharp 1px edge highlight catching the light on every card, bubble, and sheet.
* **OLED Obsidian Ground:** Deep, inky blacks (`#070312`) that make frosted glass and neon glows pop with intense vibrancy.

---

## 2. Color System & Palettes

```text
┌────────────────────────────────────────────────────────────────────────┐
│                              COLOR SYSTEM                              │
├─────────────────────┬───────────────────┬──────────────────────────────┤
│ Name                │ Hex Code / Alpha  │ Usage                        │
├─────────────────────┼───────────────────┼──────────────────────────────┤
│ Obsidian OLED       │ #070312           │ Main app scaffold background │
│ Midnight Surface    │ #0B061A           │ Secondary card backing       │
│ Liquid Glass Base   │ #FFFFFF @ 0.06f   │ Translucent glass surface    │
│ Specular Edge Rim   │ #FFFFFF @ 0.28f   │ 1px top/left light highlight │
│ Neon Purple Prime   │ #A855F7           │ Primary brand, my bubble     │
│ Electric Cyan Glow  │ #06B6D4           │ Timer countdown, active dot  │
│ Danger Crimson      │ #EF4444           │ Destructing bubble (<10s)    │
│ Text Primary        │ #FFFFFF @ 1.00f   │ Headlines, chat bubble text  │
│ Text Secondary      │ #C084FC @ 0.70f   │ Subtitles, timestamps        │
│ Text Muted          │ #94A3B8 @ 0.50f   │ Inactive tabs, placeholder   │
└─────────────────────┴───────────────────┴──────────────────────────────┘
```

---

## 3. Apple Liquid Glass Engine & Compose Tokens

Based on `GlassEffect.kt` and `GlassPool.kt` from `d:\testmusic\Convx\`:

### 3.1 Core Glass Formula
```kotlin
fun Modifier.liquidGlass(
    shape: Shape = AppleTokens.ContinuousRoundedRectangle(cornerRadius = 24.dp),
    blurRadius: Dp = 25.dp,
    noiseAlpha: Float = 0.03f,
    rimAlpha: Float = 0.28f,
    vibrancy: Float = 2.0f
): Modifier
```

* **Vibrancy Boost ($2.0\times$):** Prevents the washed-out gray look of naive blur shaders, keeping saturated neon colors punchy beneath the glass.
* **3D Lens Refraction:** Optical curvature on border contours (`lensHeight = 0.60f`, `lensAmount = 0.69f`).
* **Continuous Squircles:** Native Apple G2 curvature via `ContinuousRoundedRectangle` rather than standard circular fillets.

### 3.2 120 FPS Performance Architecture (`GlassPool`)
Independent blur filters on 20+ chat bubbles during fast scrolling would overwhelm the GPU and drop frames below 60 FPS.
* **The Solution:** A centralized `GlassPool` captures a single blurred backdrop buffer rendered at **35% resolution (`GlassPoolScale = 0.35f`)**.
* Every bubble and floating bar samples from this single hardware-accelerated shared buffer, delivering locked **120Hz scrolling**.

---

## 4. Typography Scale (Signal Exact SP & Weights)

Based on Signal-Android reverse engineering (`FRONTEND_REVERSE_ENGINEERING_NOTES.md`):

| Token | Size | Line Height | Weight | Letter Spacing | Purpose |
|---|---|---|---|---|---|
| `displayLarge` | 36.sp | 42.sp | Bold | -0.5.sp | Welcome splash branding |
| `headlineLarge` | 32.sp | 38.sp | Bold | -0.5.sp | Top headers, Room titles |
| `titleLarge` | 22.sp | 28.sp | SemiBold | -0.2.sp | Section titles, Dialog headers |
| `titleMedium` | 18.sp | 24.sp | Medium | 0.sp | Contact names in list |
| `bodyLarge` | 16.sp | 22.sp | Normal | 0.sp | **Chat message text** |
| `bodyMedium` | 14.sp | 18.sp | Normal | 0.sp | Previews, subtitles, bio |
| `labelSmall` | 11.sp | 14.sp | Medium | 0.5.sp | **Countdown timer, timestamps** |

---

## 5. Screen Components Specification

### 5.1 The Chat Bubble (Minimalist, Apple-Discreet)
```text
  [Outgoing Message - Me]                       [Incoming Message - Friend]
  ┌───────────────────────────────────────┐   ┌───────────────────────────────────────┐
  │ Hey, take a look at this document.    │   │ I saw it! Looks great.                │
  │                                       │   │                                       │
  │                             20:25 ◷ ✓✓│   │                             20:26 ◷   │
  └───────────────────────────────────────┘   └───────────────────────────────────────┘
  (Neon Purple gradient glass + rim glow)     (Dark frosted glass + specular edge rim)
```

1. **Ultra-Compact Hairline Draining Ring (Discreet Indicator):**
   * **Size:** Micro `10dp × 10dp` — occupies no more horizontal space than a single text character.
   * **Stroke:** Ultra-fine `1.2dp` hairline stroke.
   * **Placement:** Sits inline right beside the timestamp (`20:25 ◷ ✓✓`).
   * **Clockwise Drain:** Ring is 100% full when message is read and drains smoothly clockwise towards 0%.
   * **Zero Clutter:** No loud digital ticking numbers (`04:59, 04:58...`). 
   * **Modes:**
     * `Off (Permanent):` Ring is completely hidden. Renders as a clean normal bubble (`20:25 ✓✓`).
     * `24 Hours / 1 Hour / 5 Minutes:` Subtle static/slow drain without visual distraction or battery churn.
     * `30 Seconds:` Smooth fluid drain over 30s.
   * **Tap for Details:** Tapping the bubble reveals a lightweight frosted tooltip: *"Disappears in 3m 42s"*.
2. **Dissolve Animation (On Expiry):**
   * Message bubble disintegrates using a scale-down ($1.0 \to 0.9$), soft gaussian blur expansion, and alpha fade over $320\text{ms}$.

### 5.2 Floating Glass Bottom Bar (`FloatingTabBar`)
* Positioned 16.dp above screen bottom, floating over the room list.
* 44.dp height, full frosted glass backdrop with subtle drop-shadow.
* Items: **Chats** (Message icon), **Friends** (Users icon), **Settings** (Gear icon).
* Active item highlighted with a glowing pill indicator that glides horizontally with an Apple spring curve.

### 5.3 Glass Chat Input Bar
* Anchored to keyboard with smooth IME padding animation.
* Translucent glass capsule with text field, attachment button (paperclip/camera), and glowing Send circle.
* When typing text, Send button transitions from grayed state to glowing Neon Purple with spring scale pop ($0.8 \to 1.0$).

---

## 6. Micro-Interactions & Haptic Feedback

1. **Message Sent:** Light crisp tap (`HapticFeedbackType.TextHandleMove`).
2. **Message Arrived (Foreground):** Subtle dual-pulse haptic.
3. **Timer Critical (<10s):** Periodic micro-tick haptic if chat is currently open.
4. **Vanish Now (Early Delete):** Long press bubble $\to$ Glass Context Menu $\to$ "Vanish Now" with gentle crumble vibration.

---

## 7. Curated References from LiquidGlassDesign.com & App Integration

We have analyzed and mapped 4 leading showcases from `liquidglassdesign.com` directly into Floww's Jetpack Compose component suite:

### 7.1 Apple Liquid Glass Navigation Bar (`/apple-liquid-glass-design-navigation-bar`)
* **What it is:** A floating pill navigation bar featuring real-time light refraction, dynamic optical distortion beneath the bar, and a glowing pill active indicator that glides between items.
* **How we use it in Floww:** This defines our **`FloatingTabBar`** on the main dashboard (`Chats`, `Friends`, `Settings`).
* **Compose Implementation:**
  * Uses `Modifier.liquidGlass()` with continuous squircle corners (`AppleTokens.ContinuousRoundedRectangle(cornerRadius = 28.dp)`).
  * Gliding tab indicator uses Compose `spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMediumLow)` so the glow indicator physically springs across the bar when switching tabs.

### 7.2 Liquid Glass UI — Soft Motion Experiment (`/liquid-glass-ui-a-soft-motion-experiment`)
* **What it is:** Liquid physics applied to micro-interactions — cards and buttons squish slightly on press, text bubbles pop with fluid damping, and elements dissolve into soft light.
* **How we use it in Floww:**
  * **Bubble Entry:** New incoming/sent messages pop with a soft scale overshoot ($0.88 \to 1.04 \to 1.0$) with slight alpha blur fade.
  * **Vanish Dissolve:** When the timer hits `00:00`, the message bubble disintegrates using a soft gaussian blur expansion + scale shrink ($1.0 \to 0.9$) + particle dissolve over $320\text{ms}$.
  * **Input Send Action:** The Send circle button expands on text input with a soft liquid spring.

### 7.3 Liquid Glass Frosted Edition Notification (`/liquid-glass-frosted-edition-notification`)
* **What it is:** A VisionOS-style floating frosted glass notification card with bright specular edge highlights, high-contrast typography, and an embedded circular timer/progress indicator.
* **How we use it in Floww:**
  * **In-App Disappearing Notification Pill:** When a message arrives while browsing Friends or Settings, a floating frosted glass banner drops down from the top edge.
  * **Disappearance Warning Alert:** When an open chat's active message is $< 10\text{s}$ from vanishing, a subtle frosted warning pill glides into the top bar with a pulsing crimson rim.

### 7.4 Minimal Navigation, Maximum Clarity (`/minimal-navigation-maximum-clarity`)
* **What it is:** A minimalist top bar navigation with zero visual clutter, razor-sharp edge border, and dynamic blur that lets background content scroll smoothly underneath without reducing readability.
* **How we use it in Floww:**
  * **`ChatRoomTopBar` & `RoomListHeader`:** Minimalist header holding the friend's avatar, status dot (Electric Cyan for online), room name, and the self-destruct timer selector pill (`⏱ 15m`).
  * As the message list scrolls under the top bar, it blurs seamlessly via `GlassPool` without muddying the white title text.

---

## 8. Premium Iconography System (Phosphor / SF-Style Optical Precision)

To prevent the generic, blocky look of legacy Android Material icons, Floww adopts an **Apple SF Symbols / Phosphor precision icon system**:

### 8.1 Optical Rules & Calibration
* **Corner Terminals:** Round cap and round join (`StrokeCap.Round`, `StrokeJoin.Round`) to harmonize with our G2 squircle glass containers.
* **Stroke Calibration:**
  * Regular weight: `1.5dp` stroke (matches our `1.2dp` hairline glass specular rim).
  * Bold / Active weight: `2.0dp` stroke with subtle neon glow on active state.
* **Dual-State Mechanics (Bottom Tab Bar):**
  * Unselected: Crisp 1.5dp outline (`Color.White.copy(alpha = 0.50f)`).
  * Selected: Solid frosted fill + Neon Purple/Cyan accent glow with spring scale pop ($0.85 \to 1.05 \to 1.0$).

### 8.2 Floww Core Icon Matrix

| Context | Icon Concept | Normal State | Active / Filled State |
|---|---|---|---|
| **Chats Tab** | `ChatCircleText` | 1.5dp Outline | Frosted Glow Fill |
| **Friends Tab** | `UsersThree` | 1.5dp Outline | Frosted Glow Fill |
| **Settings Tab** | `GearSix` | 1.5dp Outline | Frosted Glow Fill |
| **Search** | `MagnifyingGlass` | 1.5dp Outline | - |
| **New Chat (FAB)** | `Plus` | 2.0dp Bold | Spring Rotate 45° on open |
| **Send Action** | `ArrowUp` / `PaperPlaneTilt` | Recessed Gray | Glowing Purple Circle |
| **Attachment** | `Paperclip` / `Image` | 1.5dp Outline | - |
| **Timer / Expiry** | `Timer` / `HourglassSimple` | Electric Cyan Accent | Pulsing Crimson (<10s) |
| **Read Receipts** | `Checks` (Double Tick) | Gray Single Tick (Sent) | Electric Cyan Double Tick |
| **Vanish Now** | `Sparkle` / `TrashSimple` | Soft Crimson Warning | - |
| **Security Shield** | `ShieldCheck` | 1.5dp Outline | Electric Cyan Fill |
| **QR Code** | `QrCode` | 1.5dp Outline | - |


