# INITIAL DATA MODEL - 2026-09-12-23:40:30 CET

## Task
- ID -> UUID
- Title -> Non-unique
- Status -> Enum if possible
- Tags -> Universal / Vault-relative
- User it belongs to
- Collection -> General or user-made collections
- Creation / last-updated timestamps
  - Device ID / name it was created on.
  - Device ID / name it was last modified on.
- Due date
- Recurrence
  - Days of recurrence (if needed)
  - Hour / date
- Slice of time it takes place on?
- Parent task? (IDEA)

## Tag
- ID -> UUID
- Theme / title (begins with `#` on UI, no spaces, replaced with `-` by default)
- Color (for UI purposes. Defaulting to random)
- Priority (like Linux processes, allows sorting tags either by name or by
  priority)
- User it belongs to (ID)

## Status (Enum)
- TO BE DONE / TO DO
- ONGOING
- DONE
- BACKBURNER
- FUTURE
- IDEA

## Collection
- ID -> UUID
- Title (allows spaces)
- Description (what is collected here)
- User it belongs to
- Nested? (collection inside collection, collection-ception) (IDEA)
  - If so, inside which of the existing ones? (IDEA)

## Event / Change
- ID -> UUID for easy querying
- Type:
  - CREATION
  - UPDATE
  - DELETION
  - MOVE
  - SETTINGS
  - SPECIAL
  - ERROR
  - NOTIFICATION
- Timestamp
- Origin device and user IDs (normally needed unless it's server-adjacent).
- Destination device(s) and user(s) IDs (optional).
- Title + Description

## User
- ID -> UUID
- Name
- Email -> Login purposes
- Password -> Login purposes
- Devices it's logged on / remembered on
- Various UI settings (JSON storage on DB?)
