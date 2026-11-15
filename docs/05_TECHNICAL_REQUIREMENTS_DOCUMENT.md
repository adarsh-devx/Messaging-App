# Floww — Technical Requirements Document (TRD)

> **Document Version:** 1.0.0  
> **Target OS:** Android 8.0+ (API Level 26 to API Level 35)  
> **Primary Language:** 100% Kotlin (Zero Legacy Java, Zero XML layouts)  
> **UI Toolkit:** Jetpack Compose + Material 3  

---

## 1. System Architecture Overview

Floww Android follows an **Offline-First, Elm-like MVI (Model-View-Intent)** architecture inspired by **Signal-Android**:

```text
┌────────────────────────────────────────────────────────────────────────┐
│                        PRESENTATION LAYER (COMPOSE)                    │
│   Screen.kt (Stateless) ◄── StateFlow ── ScreenViewModel.kt (MVI)      │
│            │                                  ▲                        │
│            └────────── User Events (Channel) ─┘                        │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │ Calls
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│                          DOMAIN / DATA LAYER                           │
│   ChatRepository  ◄──►  Local Database (Room)  ◄──►  Remote Data Source│
│                                                       (Retrofit/Socket)│
└───────────────────────────────────┬────────────────────────────────────┘
                                    │
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│                         INFRASTRUCTURE LAYER                           │
│   OkHttp Authenticator │ Socket.io Client │ EncryptedSharedPreferences │
└────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Technology Stack & Key Dependencies

| Component | Library / Framework | Version / Note |
|---|---|---|
| **Language** | Kotlin | `2.0.x` |
| **UI Toolkit** | AndroidX Jetpack Compose | BOM `2024.10+` |
| **Design Engine** | Material 3 + Chris Banes Haze | `dev.chrisbanes.haze:haze:1.0.0+` |
| **Glass Acceleration** | Convx `GlassPool` + Hardware Blur | API 31+ `RenderEffect`, fallback below |
| **Asynchronous Engine** | Kotlin Coroutines & StateFlow | `1.8.x` |
| **REST Networking** | Retrofit 2 + OkHttp 4 | JSON Moshi / Kotlinx Serialization |
| **Realtime Engine** | Socket.io Client Java | `io.socket:socket.io-client:2.1.0` |
| **Local Storage** | Room Database | SQLite with automated eviction hooks |
| **Secure Secrets** | AndroidX Security Crypto | `EncryptedSharedPreferences` |
| **Image Loading** | Coil Compose | `io.coil-kt:coil-compose:2.7.0` |

---

## 3. Signal's 5-File MVI Architectural Blueprint

Every feature screen in the app strictly conforms to Signal's 5-file module isolation:

```text
feature/<name>/
├── <Name>Screen.kt          --> Pure Compose UI (Receives State, emits Events)
├── <Name>ScreenState.kt     --> Immutable UI State Data Class
├── <Name>ScreenEvents.kt    --> Sealed class of user actions
├── <Name>ScreenActions.kt   --> Sealed class for single-shot side effects (Navigate, Toast)
└── <Name>ScreenViewModel.kt --> Extends EventDrivenViewModel<E>
```

### 3.1 The `EventDrivenViewModel` Engine
```kotlin
package com.floww.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class EventDrivenViewModel<E : Any>(
    private val tag: String
) : ViewModel() {
    private val eventChannel = Channel<E>(Channel.UNLIMITED)

    init {
        viewModelScope.launch {
            for (event in eventChannel) {
                processEvent(event)
            }
        }
    }

    fun onEvent(event: E) {
        eventChannel.trySend(event)
    }

    protected abstract suspend fun processEvent(event: E)
}
```

* **Why this beats standard MVVM:**
  * Eliminates race conditions in rapid user inputs (e.g. typing, quick reactions).
  * Backpressure safe with guaranteed event ordering.
  * Zero mutable state exposed outside the ViewModel.

---

## 4. Hardware-Accelerated Glass Pipeline (`GlassPool`)

Ported from Convx (`d:\testmusic\Convx\app\src\main\kotlin\com\convx\music\ui\component\backdrop\GlassPool.kt`):

```text
                     Full-Screen Visual Scene
                                │
                  [GlassPool Backdrop Provider]
                                │
     Renders at 35% resolution (0.35f scale) to offscreen buffer
                                │
               Applies Chris Banes Haze Blur Filter
                                │
       ┌────────────────────────┴────────────────────────┐
       ▼                                                 ▼
[Floating Tab Bar]                             [Active Chat Bubble]
Samples shared blurred surface                 Samples shared blurred surface
Locked at 120 FPS                              Locked at 120 FPS
```

* **Compatibility Fallback:**
  * On Android 12+ (API 31+): Direct hardware GPU `RenderEffect`.
  * On Android 8–11: Fast downsampled dual-pass blur shader with frosted fallback tint (`Color.White.copy(alpha = 0.12f)`).

---

## 5. Network & Authentication Architecture

### 5.1 Token Management & Automatic Re-auth
```text
HTTP Request ──► [AuthInterceptor: Attach Bearer Token] ──► Server
                                                               │
                                                       Returns 401 Unauthorized
                                                               │
Server ◄── [TokenAuthenticator: POST /api/auth/refresh] ◄──────┘
   │
Acquires new Access Token & retries original request transparently
```

* **Mutex Lock:** Token refresh requests are synchronized via Kotlin Coroutine `Mutex` so 5 simultaneous failing requests trigger only **one single** refresh call to the server.

### 5.2 Realtime WebSocket Sync Engine
* Single persistent connection managed by an Android `Service` / Application Singleton.
* Automatically registers room rooms when entering chat screen (`ws:join_room`) and unbinds on exit (`ws:leave_room`).
* Reconnection with exponential jittered backoff ($1\text{s}, 2\text{s}, 4\text{s}, \dots, 30\text{s}$).

---

## 6. Security & Memory Hardening

1. **`FLAG_SECURE` Integration:**
   * Window flag toggleable in Settings to block screenshots, screen recording, and system app switcher previews.
2. **KeyStore Token Storage:**
   * Master key generated via `MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)` inside Android hardware Trusted Execution Environment (TEE).
3. **RAM Sanitation:**
   * Sensitive decrypted messages are held in in-memory models; when Room clears or app is killed, garbage collector reclaims buffers immediately.
