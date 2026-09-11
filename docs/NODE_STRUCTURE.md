# NODES
## SERVER
- Ideally no logins / changes from here. Only serving / receiving content and
  logging.
- Main DB. Ideally every client sends updates immediately here when they happen.

## ANDROID CLIENT
- Local DB for changes, but with conditions (no reorganising of old files
  offline). If not, merging differing histories can be a nightmare?
- Sync engine with a queue that activates when online / connected to the server.

## DESKTOP APP
- Rust client using Iced for the cross-platform UI.
- Local DB for offline work and an outbound synchronization queue.
- Connects to the server through the versioned REST/JSON API only.
- Required Linux targets: x86_64 and ARM64.
- Planned additional targets: macOS Intel, macOS Apple Silicon, and Windows x86_64.

## WEB UI
- Uses the same versioned REST/JSON API as the desktop and Android clients.
