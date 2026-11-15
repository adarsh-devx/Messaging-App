# Floww — Android Implementation Plan & Development Milestones

> **Document Version:** 1.0.0  
> **Target Project Location:** `d:\Floww\Frontend`  
> **Status:** READY FOR EXECUTION  

---

## 1. Project Directory Structure Layout

The Android project in `d:\Floww\Frontend` will be organized following clean vertical slice and Signal architecture:

```text
Frontend/
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       └── kotlin/com/floww/
│           ├── FlowwApp.kt                   --> Application class (DI, Token store init)
│           ├── MainActivity.kt               --> Single Activity, Edge-to-Edge setup
│           │
│           ├── core/                         --> Reusable cross-feature engine
│           │   ├── designsystem/             --> Convx Glass & Apple Design Tokens
│           │   │   ├── AppleTokens.kt        --> Squircles, Radii, Shadows
│           │   │   ├── GlassEffect.kt        --> Modifier.liquidGlass, vibrancy boost
│           │   │   ├── GlassPool.kt          --> 120 FPS Pooled Backdrop Provider
│           │   │   ├── Theme.kt              --> FlowwGlassTheme, ColorScheme
│           │   │   └── Type.kt               --> Signal Typography Scale
│           │   │
│           │   ├── ui/                       --> Signal MVI Base Classes
│           │   │   ├── EventDrivenViewModel.kt
│           │   │   └── components/           --> GlassButton, GlassInputField, Avatar
│           │   │
│           │   ├── network/                  --> Retrofit & Socket.io Engine
│           │   │   ├── ApiClient.kt          --> Retrofit builder & OkHttp
│           │   │   ├── AuthInterceptor.kt    --> Bearer token injection
│           │   │   ├── TokenAuthenticator.kt --> 401 Auto-refresh mechanism
│           │   │   └── SocketManager.kt      --> Realtime WebSocket connection lifecycle
│           │   │
│           │   └── storage/                  --> Encrypted Security Storage
│           │       └── SecurePreferences.kt  --> EncryptedSharedPreferences wrapper
│           │
│           ├── data/                         --> DTOs, Repositories, API Interfaces
│           │   ├── api/                      --> AuthApi, UserApi, RoomApi, MessageApi
│           │   ├── model/                    --> Clean Kotlin Data Classes (DTOs)
│           │   └── repository/               --> AuthRepository, ChatRepository
│           │
│           ├── navigation/                   --> Navigation graph & routes
│           │   ├── ScreenRoute.kt            --> Sealed interface routes
│           │   └── FlowwNavHost.kt           --> Animated Compose NavHost
│           │
│           └── feature/                      --> Vertical Feature Slices (Signal 5-File Pattern)
│               ├── auth/                     --> Welcome, Login, Register screens
│               ├── rooms/                    --> RoomList (Chats tab), FloatingTabBar
│               ├── chat/                     --> Active ChatRoom, Live Countdown, Bubbles
│               ├── friends/                  --> Friends tab, AddFriend, QR Scanner
│               └── settings/                 --> Settings, Timer selection, Privacy flags
│
├── gradle/
│   └── libs.versions.toml                   --> Version Catalog
├── build.gradle.kts                         --> Root build configuration
└── settings.gradle.kts                      --> Module settings
```

---

## 2. Phased Execution Roadmap

### Phase 1: Android Studio Project Scaffolding & Dependencies
* **Deliverables:**
  * Configure `settings.gradle.kts` and `build.gradle.kts` (Kotlin 2.0 DSL).
  * Configure `libs.versions.toml` with: Compose BOM, Chris Banes Haze, Retrofit 2, OkHttp 4, Socket.io, Coil, AndroidX Crypto.
  * Setup `MainActivity.kt` with transparent status/nav bars (`enableEdgeToEdge`).
* **Verification Gate:** Gradle sync passes with 0 warnings; blank dark window launches successfully.

---

### Phase 2: Design System & Convx Glass Engine Port
* **Deliverables:**
  * Port `GlassEffect.kt`, `GlassPool.kt`, and `AppleTokens.kt` from `d:\testmusic\Convx\`.
  * Implement `FlowwGlassTheme` with OLED Obsidian `#070312`, Neon Purple, and Electric Cyan.
  * Implement Signal typography scale in `Type.kt`.
  * Build reusable components: `GlassCard`, `GlassButton`, `GlassInputField`, `FloatingTabBar`.
* **Verification Gate:** Interactive preview screen rendering 120Hz smooth blurred glass over an animated neon background.

---

### Phase 3: Core Architecture, Networking & Auth Flow
* **Deliverables:**
  * Implement `EventDrivenViewModel.kt` for MVI.
  * Setup Retrofit client with `AuthInterceptor` pointing to live backend (`http://localhost:5000` / host IP).
  * Setup `SecurePreferences.kt` for JWT storage.
  * Implement **WelcomeScreen**, **LoginScreen**, and **RegisterScreen** (5-file pattern each).
* **Verification Gate:** Successful registration and login against live Neon PostgreSQL backend; tokens persisted in EncryptedSharedPreferences.

---

### Phase 4: Room List & Social Feature Slices
* **Deliverables:**
  * Implement **RoomsScreen** (Chats tab) with unread badge counters and last message previews.
  * Implement **FriendsScreen** (Friends list, pending requests, Add Friend dialog by username).
  * Integrate `FloatingTabBar` with smooth spring transitions between tabs.
* **Verification Gate:** Displays real seeded rooms (`pkdev`, `aryan`, `maya`) fetched from `/api/rooms`.

---

### Phase 5: The Disappearing Chat Experience & Real-Time Engine
* **Deliverables:**
  * Implement `SocketManager.kt` handling Socket.io events (`ws:send_message`, `ws:new_message`, `ws:mark_read`, `ws:typing`).
  * Implement **ChatRoomScreen** with:
    * Signal typography chat bubbles (Outbox Neon Purple Glass, Inbox Frosted Glass).
    * Embedded live animated countdown timer pill.
    * Real-time typing dots banner.
    * "Vanish Now" early manual delete context action.
    * Dissolve animation on `ws:message_expired`.
* **Verification Gate:** Two concurrent clients (e.g. Android + Web/Postman) exchange messages; countdown activates upon viewing and message dissolves smoothly at 00:00.

---

### Phase 6: Privacy Hardening & Final Polish
* **Deliverables:**
  * Add `FLAG_SECURE` toggle in Settings.
  * R8 Proguard rules for zero-leak release builds.
  * Edge-to-edge keyboard IME padding polish.
* **Verification Gate:** Production APK build, verified memory leak free and 120 FPS scroll test.
