# Floww — Product Requirements Document (PRD)

> **Document Version:** 1.0.0  
> **Target Release:** MVP Phase 1 (Native Android)  
> **Status:** APPROVED & LOCKED  
> **Author:** Floww Product & Engineering Team  

---

## 1. Executive Summary & Vision

**Floww** is an ultra-fast, zero-footprint personal messaging application. It is engineered with a strict **"Privacy by Architecture, Delight by Design"** philosophy. 

Unlike traditional messengers that store permanent ledgers of your conversations, Floww treats digital communication like real-world spoken conversations: once heard and understood, words dissolve.

### Core Value Pillars
1. **True Disappearing Messaging & Flexible Lifecycles:**
   * Messages self-destruct upon reading after a customizable countdown timer: **30 seconds, 5 minutes, 1 hour, 24 hours, or Off (Permanent Chat)**.
   * Hard database deletion — no soft deletes, no audit logs of chat text, no lingering files on disk.
2. **Apple Liquid Glass (Frosted VisionOS Aesthetic):**
   * Next-generation translucent frosted glass design, optical refraction, specular highlights, and 120 FPS fluid motion.
3. **Signal-Grade Architectural Discipline:**
   * Pure Kotlin, Jetpack Compose, Elm/MVI event-driven architecture, resilient offline-first state handling.

---

## 2. User Personas

* **Persona A: The Privacy-Conscious Communicator (Primary)**
  * Needs a hassle-free, secure channel to share sensitive ideas, photos, or candid thoughts without worrying about screenshots, message history leaks, or cloud backups.
* **Persona B: The Aesthetics Enthusiast (Secondary)**
  * Tired of dated, flat Material Design or messy clutter. Craves ultra-smooth 120Hz animations, translucent depth, and tactile haptic micro-interactions.

---

## 3. Disappearing Messaging Lifecycle & Business Rules

### 3.1 The Signal-Grade "Read-Triggered" Timer Rule
A message's timer **does NOT start when sent**; it starts strictly when the recipient **first reads the message**:
1. **Unread Persistence (Signal Invariant):**
   * If a message is sent with an active timer (e.g. 5m, 1h, 24h), it remains safely in the recipient's inbox until the recipient opens the chat. Unread messages are not arbitrarily deleted while unread.
2. **First-Read Activation:**
   * As soon as the recipient opens the room and the message enters the visible viewport:
     * Recipient client fires `mark_read`.
     * Backend sets `firstReadAt = now()`.
     * Backend calculates `expiresAt = firstReadAt + timerDuration`.
     * WebSocket pushes `ws:message_read` to all participants with the expiration timestamp.
3. **Off (Permanent Chat Mode):**
   * When room timer is set to `Off`, messages have `expiresAt = null`. They behave like permanent chat messages and never auto-expire unless manually deleted by the user.
4. **Auto-Eviction on Expiration:**
   * When `expiresAt <= now()`, backend cron engine hard-deletes the row and unlinks any attached photo. WebSocket emits `ws:message_expired` to smoothly dissolve the bubble from the screen.

### 3.2 Discreet & Compact Indicator Rule
* **No Loud Stopwatches:** Unlike gimmicky messengers with ticking digital countdowns (`04:59, 04:58...`), messages display an **ultra-compact 10dp hairline circular draining ring** beside the timestamp (`20:45 ◷ ✓✓`).
* The ring subtly drains clockwise without causing visual noise or battery drain. Tapping the bubble reveals a frosted tooltip with the exact remaining time.

### 3.2 Media & Attachment Policy
* Photos uploaded are strictly deleted when the message expires.
* Files are processed via Sharp image pipeline on backend (optimized WebP/JPEG, EXIF/metadata completely stripped).
* Stored with randomly generated nonces; when message expires, disk storage is instantly unlinked.

---

## 4. Functional Requirements

### 4.1 Authentication & Identity
* **FR-AUTH-1:** User registration with unique `username`, `displayName`, and secure `password` (Bcrypt 12 rounds).
* **FR-AUTH-2:** Login returns short-lived JWT Access Token ($15\text{ mins}$) and long-lived Refresh Token ($30\text{ days}$).
* **FR-AUTH-3:** Secure token storage on Android via AndroidX `EncryptedSharedPreferences` backed by Android KeyStore.
* **FR-AUTH-4:** Seamless token refresh interceptor via OkHttp Authenticator.
* **FR-AUTH-5:** Complete logout revoking tokens on both client and server database.

### 4.2 User Profile & Discovery
* **FR-USER-1:** Users can update `displayName`, `bio`, `defaultTimerMinutes`, and avatar photo.
* **FR-USER-2:** User search by `@username` prefix matching.
* **FR-USER-3:** Personal Invite Code / QR Code generation for one-tap friend requests.

### 4.3 Social & Connections (Friends & Invites)
* **FR-SOC-1:** Send friend request via username or invite code.
* **FR-SOC-2:** Accept, reject, or cancel pending friend requests.
* **FR-SOC-3:** Remove friend (immediately breaks direct room link).
* **FR-SOC-4:** Real-time push / socket notification on friend request events.

### 4.4 Rooms & Conversations
* **FR-ROOM-1: 1-on-1 Direct Messaging (DM):**
  * Auto-created or resumed via deterministic pair key `dmKey = min(userA, userB) + ":" + max(userA, userB)`.
  * Displays friend's avatar, online/last active indicator, and current room timer.
* **FR-ROOM-2: Group Chats:**
  * Multi-member chat with group title and optional avatar.
  * Role-based permissions: Creator/Admin can rename, add members, kick members, or change self-destruct timer.
* **FR-ROOM-3: Room Timer Configuration:**
  * Room timer options: **30 seconds, 5 minutes, 1 hour, 24 hours, or Off (Permanent Chat)**.
  * Changing timer updates future messages in room and notifies participants ("Timer set to 1 hour").

### 4.5 Chat & Real-Time Messaging
* **FR-CHAT-1:** Send instant text messages over WebSocket with REST HTTP fallback.
* **FR-CHAT-2:** Send camera/gallery photo attachments.
* **FR-CHAT-3:** Real-time typing indicators (`"Alice is typing..."`) with 3-second debounce timeout.
* **FR-CHAT-4:** Real-time read receipts with millisecond timestamp sync.
* **FR-CHAT-5:** Ultra-compact 10dp hairline circular draining ring beside timestamp (no loud digital numbers).
* **FR-CHAT-6:** Early manual delete ("Vanish Now") allowing sender to delete a message before timer expires.

---

## 5. Non-Functional Requirements

### 5.1 Performance & Frame Rate
* **NFR-PERF-1:** UI must render at **locked 60Hz / 120Hz** on ProMotion/High-Refresh displays without frame drops during scrolling.
* **NFR-PERF-2:** Liquid glass blurs must utilize pooled backdrop scaling (35% scale) to prevent GPU thermal throttling.
* **NFR-PERF-3:** End-to-end message latency $< 150\text{ms}$ on standard 4G/5G/Wi-Fi connections.

### 5.2 Security & Privacy Invariants
* **NFR-SEC-1:** `FLAG_SECURE` window policy option to prevent screenshot captures and Android Recents previews in sensitive rooms.
* **NFR-SEC-2:** Zero plaintext sensitive data in Android `Logcat` production builds (`R8 / ProGuard` stripping).
* **NFR-SEC-3:** Database level cascading deletes: deleting a room or user vaporizes all associated messages, reads, and media uploads immediately.

---

## 6. Success Metrics & Criteria
* **Time to First Message:** $< 1.5\text{ seconds}$ from app launch to sending a message in existing room.
* **Disappearance Precision:** Message rows removed from database and client view within $\pm 2\text{ seconds}$ of expiration timestamp.
* **App Cold Startup Time:** $< 800\text{ms}$ on Android 12+ devices.
