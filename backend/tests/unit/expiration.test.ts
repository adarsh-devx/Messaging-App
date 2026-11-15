import { computeExpiresAt, isExpired, remainingMs } from '../../src/utils/expiration';

describe('computeExpiresAt (Signal-Grade)', () => {
  const sentAt = new Date('2026-01-01T00:00:00.000Z');

  it('keeps unread messages non-expiring (returns null)', () => {
    const expiresAt = computeExpiresAt(sentAt, null, 5 * 60_000);
    expect(expiresAt).toBeNull();
  });

  it('never expires messages when timer is Off (0 duration)', () => {
    const firstReadAt = new Date(sentAt.getTime() + 60_000);
    const expiresAt = computeExpiresAt(sentAt, firstReadAt, 0);
    expect(expiresAt).toBeNull();
  });

  it('starts the countdown strictly when the recipient reads', () => {
    const firstReadAt = new Date(sentAt.getTime() + 60_000);
    const expiresAt = computeExpiresAt(sentAt, firstReadAt, 5 * 60_000);
    expect(expiresAt).not.toBeNull();
    expect(expiresAt!.getTime() - firstReadAt.getTime()).toBe(5 * 60_000);
  });

  it('supports 24 hour timer', () => {
    const firstReadAt = new Date(sentAt.getTime() + 10_000);
    const duration = 24 * 60 * 60_000;
    const expiresAt = computeExpiresAt(sentAt, firstReadAt, duration);
    expect(expiresAt).not.toBeNull();
    expect(expiresAt!.getTime() - firstReadAt.getTime()).toBe(duration);
  });

  it('handles isExpired and remainingMs correctly', () => {
    const readAt = new Date('2026-01-01T00:01:00.000Z');
    const expiresAt = computeExpiresAt(sentAt, readAt, 30_000);
    expect(expiresAt).not.toBeNull();

    expect(isExpired(expiresAt, expiresAt!)).toBe(true);
    expect(remainingMs(expiresAt, new Date(expiresAt!.getTime() + 5_000))).toBe(0);
    expect(isExpired(expiresAt, new Date(expiresAt!.getTime() - 1))).toBe(false);

    // Null expiration checks
    expect(isExpired(null)).toBe(false);
    expect(remainingMs(null)).toBeNull();
  });
});
