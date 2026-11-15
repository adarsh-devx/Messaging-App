# Floww — Backend Schema & API Contracts Specification

> **Document Version:** 1.0.0  
> **Source Codebase:** `d:\Floww\backend`  
> **Status:** 100% COMPLETE, VERIFIED & HOSTED ON NEON POSTGRESQL  

---

## 1. Database Schema (Prisma PostgreSQL)

### 1.1 Enumerations
```prisma
enum RoomType {
  PRIVATE
  GROUP
}

enum MemberRole {
  ADMIN
  MEMBER
}

enum FriendRequestStatus {
  PENDING
  ACCEPTED
  REJECTED
}

enum NotificationType {
  FRIEND_REQUEST
  FRIEND_ACCEPTED
  ROOM_INVITE
  SYSTEM
}
```

### 1.2 Core Data Models
* **`User`:**
  * `id`: String (UUID PK)
  * `username`: String (Unique, e.g. "pkdev")
  * `displayName`: String
  * `bio`: String (Default "")
  * `avatarPath`: String? (Internal filename served via authenticated route)
  * `passwordHash`: String (Bcrypt 12 rounds)
  * `defaultTimerMinutes`: Int (Default 15)
  * `inviteCode`: String (Unique UUID)
  * `createdAt`, `updatedAt`: DateTime
* **`RefreshToken`:**
  * `id`: String (UUID PK)
  * `token`: String (SHA-256 hash of refresh JWT)
  * `userId`: String (FK -> User.id Cascade)
  * `expiresAt`: DateTime
* **`Room`:**
  * `id`: String (UUID PK)
  * `name`: String
  * `type`: RoomType (`PRIVATE` or `GROUP`)
  * `avatarUrl`: String?
  * `timerSeconds`: Int (0 = Off/Permanent, 30 = 30s, 300 = 5m, 3600 = 1h, 86400 = 24h)
  * `adminId`: String? (Null for private DMs)
  * `dmKey`: String? (Unique, sorted `"userA:userB"`)
* **`RoomMember`:**
  * `id`: String (UUID PK)
  * `roomId`: String (FK -> Room.id Cascade)
  * `userId`: String (FK -> User.id Cascade)
  * `role`: MemberRole (`ADMIN`, `MEMBER`)
  * `joinedAt`: DateTime
  * `@@unique([roomId, userId])`
* **`Message`:**
  * `id`: String (UUID PK)
  * `roomId`: String (FK -> Room.id Cascade)
  * `senderId`: String (FK -> User.id Cascade)
  * `text`: String
  * `photoPath`: String?
  * `photoMimeType`: String?
  * `sentAt`: DateTime
  * `firstReadAt`: DateTime? (Locked on first recipient view)
  * `firstReadBy`: String?
  * `timerDurationMs`: Int (0 for Off/Permanent)
  * `expiresAt`: DateTime? (null if unread or Off; calculated as `firstReadAt + timerDurationMs` upon first read)
* **`MessageRead`:**
  * `id`: String (UUID PK)
  * `messageId`: String (FK -> Message.id Cascade)
  * `userId`: String (FK -> User.id Cascade)
  * `readAt`: DateTime
* **`Friendship` & `FriendRequest`:**
  * Two-way symmetrical friendship records and pending requests.

---

## 2. REST API Endpoints Specification

Base URL: `http://<HOST>:5000/api`  
Authentication: `Authorization: Bearer <ACCESS_TOKEN>` header required for all routes except `/auth/login` and `/auth/register`.

### 2.1 Authentication Module (`/api/auth`)

#### `POST /api/auth/register`
* **Body:**
  ```json
  {
    "username": "pkdev",
    "displayName": "Priyanshu",
    "password": "Password123!"
  }
  ```
* **Response (201 Created):**
  ```json
  {
    "success": true,
    "data": {
      "user": {
        "id": "uuid",
        "username": "pkdev",
        "displayName": "Priyanshu",
        "avatarUrl": null,
        "defaultTimerMinutes": 15
      },
      "accessToken": "jwt_string",
      "refreshToken": "jwt_string"
    }
  }
  ```

#### `POST /api/auth/login`
* **Body:**
  ```json
  {
    "username": "pkdev",
    "password": "Password123!"
  }
  ```
* **Response (200 OK):** Identical to register response.

#### `POST /api/auth/refresh`
* **Body:** `{ "refreshToken": "jwt_string" }`
* **Response (200 OK):** `{ "accessToken": "new_jwt_string", "refreshToken": "new_jwt_string" }`

#### `POST /api/auth/logout`
* **Headers:** `Authorization: Bearer <ACCESS_TOKEN>`
* **Response (200 OK):** `{ "success": true, "message": "Logged out successfully" }`

---

### 2.2 User Profile & Discovery (`/api/users`)

#### `GET /api/users/me`
* Returns authenticated user profile, stats, and default timer.

#### `PATCH /api/users/me`
* **Body:** `{ "displayName": "New Name", "bio": "Privacy matters", "defaultTimerMinutes": 5 }`

#### `GET /api/users/search?q=aryan`
* **Response (200 OK):** Array of matching public user profiles `{ id, username, displayName, avatarUrl }`.

---

### 2.3 Rooms & Conversations (`/api/rooms`)

#### `GET /api/rooms`
* **Response (200 OK):**
  ```json
  {
    "success": true,
    "data": [
      {
        "id": "room_uuid",
        "name": "Aryan",
        "type": "PRIVATE",
        "avatarUrl": "https://...",
        "timerMinutes": 15,
        "unreadCount": 2,
        "lastMessage": {
          "text": "See you soon!",
          "sentAt": "2026-11-15T20:00:00Z",
          "expiresAt": "2026-11-15T20:15:00Z"
        }
      }
    ]
  }
  ```

#### `POST /api/rooms/dm`
* **Body:** `{ "targetUserId": "user_uuid" }`
* **Response (200/201):** Returns existing or newly created 1-on-1 private room.

#### `POST /api/rooms/group`
* **Body:** `{ "name": "Floww Devs", "memberIds": ["uuid1", "uuid2"], "timerMinutes": 15 }`

#### `PATCH /api/rooms/:roomId/timer`
* **Body:** `{ "timerMinutes": 5 }` (Admin or member in DM)

---

### 2.4 Messages & Media (`/api/rooms/:roomId/messages`)

#### `GET /api/rooms/:roomId/messages?cursor=uuid&limit=30`
* Returns paginated list of non-expired messages.

#### `POST /api/rooms/:roomId/messages`
* **Body:** `{ "text": "Self-destruct in 5 mins", "photoUploadId": null }`
* **Response (201 Created):**
  ```json
  {
    "success": true,
    "data": {
      "id": "msg_uuid",
      "roomId": "room_uuid",
      "senderId": "my_uuid",
      "text": "Self-destruct in 5 mins",
      "photoUrl": null,
      "sentAt": "2026-11-15T20:30:00.000Z",
      "firstReadAt": null,
      "timerDurationMs": 300000,
      "expiresAt": "2026-11-15T21:30:00.000Z"
    }
  }
  ```

#### `POST /api/rooms/:roomId/messages/:messageId/read`
* Starts the recipient-first countdown timer.
* Emits `ws:message_read` over WebSocket.

#### `DELETE /api/rooms/:roomId/messages/:messageId`
* "Vanish Now" early manual delete by sender.

---

## 3. Real-Time WebSockets (Socket.io)

### Connection Handshake
* **Endpoint:** `ws://<HOST>:5000`
* **Auth Query / Header:** `{ token: "<ACCESS_TOKEN>" }`

### Client-to-Server Events
1. `ws:join_room` $\to$ `{ roomId: string }`
2. `ws:leave_room` $\to$ `{ roomId: string }`
3. `ws:send_message` $\to$ `{ roomId: string, text: string, photoUploadId?: string }`
4. `ws:mark_read` $\to$ `{ roomId: string, messageId: string }`
5. `ws:typing` $\to$ `{ roomId: string, isTyping: boolean }`

### Server-to-Client Broadcasts
1. `ws:new_message` $\to$ `{ message: MessagePayload }`
2. `ws:message_read` $\to$ `{ messageId: string, firstReadAt: string, expiresAt: string }`
3. `ws:message_expired` $\to$ `{ messageId: string, roomId: string }`
4. `ws:user_typing` $\to$ `{ roomId: string, userId: string, isTyping: boolean }`
5. `ws:room_updated` $\to$ `{ roomId: string, timerMinutes: number }`

---

## 4. Automated Cleanup Cron Engine (`expiration.job.ts`)
* Runs every **30 seconds** on Node.js backend.
* **Database Query:**
  ```sql
  DELETE FROM "Message" WHERE "expiresAt" <= NOW();
  ```
* **Disk Purge:** Automatically triggers `fs.unlinkSync()` for any unreferenced photo attachments.
* **Broadcast:** Emits `ws:message_expired` to active sockets so clients can animate vapor dissolve.
