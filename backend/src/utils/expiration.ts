/**
 * Signal-grade authoritative expiration math.
 *
 * Rules:
 * - If timer is Off (timerDurationMs <= 0), message never expires (returns null).
 * - If unread (!firstReadAt), message never expires while unread (returns null).
 * - When first read (firstReadAt), expiresAt = firstReadAt + timerDurationMs.
 */

export function timerSecondsToMs(seconds: number): number {
  return seconds * 1000;
}

export const timerMinutesToMs = timerSecondsToMs;

export function computeExpiresAt(
  _sentAt: Date,
  firstReadAt: Date | null,
  timerDurationMs: number,
): Date | null {
  if (timerDurationMs <= 0 || !firstReadAt) {
    return null;
  }
  return new Date(firstReadAt.getTime() + timerDurationMs);
}

export function isExpired(expiresAt: Date | null, now: Date = new Date()): boolean {
  if (!expiresAt) {
    return false;
  }
  return now.getTime() >= expiresAt.getTime();
}

export function remainingMs(expiresAt: Date | null, now: Date = new Date()): number | null {
  if (!expiresAt) {
    return null;
  }
  return Math.max(0, expiresAt.getTime() - now.getTime());
}
