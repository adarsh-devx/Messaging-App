import { z } from 'zod';
import {
  MAX_BIO_LENGTH,
  MAX_DISPLAY_NAME_LENGTH,
  TIMER_OPTIONS,
} from '../../utils/constants';

export const userIdParamSchema = z.object({
  userId: z.string().uuid('A valid user id is required'),
});

export const searchUsersSchema = z
  .object({
    q: z.string().trim().min(2, 'Search for at least 2 characters').max(50),
  })
  .strict();

export const updateProfileSchema = z
  .object({
    displayName: z.string().trim().min(1).max(MAX_DISPLAY_NAME_LENGTH * 2).optional(),
    bio: z.string().max(MAX_BIO_LENGTH * 2).optional(),
    defaultTimerMinutes: z
      .number()
      .int()
      .refine((value) => (TIMER_OPTIONS as readonly number[]).includes(value), {
        message: 'Timer must be one of 0 (Off), 30s, 300s (5m), 3600s (1h), or 86400s (24h)',
      })
      .optional(),
  })
  .strict()
  .refine((value) => Object.keys(value).length > 0, {
    message: 'Provide at least one profile field',
  });

export type UpdateProfileInput = z.infer<typeof updateProfileSchema>;
export type SearchUsersQuery = z.infer<typeof searchUsersSchema>;
