# ADR-004: Rust desktop client with Iced

## Status

Accepted for the desktop client direction.

## Context

TDNR is intended to support desktop users on macOS, Linux, and Windows. Linux compatibility is especially important, including x86_64 PCs and ARM64 systems related to the Raspberry Pi deployment environment.

The desktop client must work as an independent client of the backend, support local persistence and queued synchronization, and avoid coupling its UI to the Java server implementation.

The main UI candidates are a pure-Rust toolkit such as Iced or egui, or a Tauri application using a web frontend. Tauri would maximize reuse with the web client but introduces system WebView and packaging dependencies. A pure-Rust toolkit better matches the goal of a native Rust desktop application.

## Decision

Build the desktop client in Rust using Iced as the first UI toolkit candidate.

The desktop client will connect to the backend only through the versioned API. Its initial internal boundary will be:

```text
Iced UI
  |
Application/use-case layer
  |
Local SQLite store + outbound sync queue
  |
REST/JSON API client
```

The first required Linux targets are x86_64 and ARM64. macOS Intel, macOS Apple Silicon, and Windows x86_64 are supported targets for the cross-platform client, with packaging and release automation added after the core workflow is proven.

The Raspberry Pi is a server target, not a required desktop UI target. The server exposes the API and hosts the authoritative database; clients do not access its database directly.

## Consequences

- The desktop client can share synchronization semantics with the future Flutter client without sharing UI code.
- Iced becomes a technology risk to validate early through a small window and task-list prototype.
- The application must isolate platform-specific concerns such as paths, notifications, packaging, and secure storage.
- Linux ARM64 builds should be tested on real or emulated target environments; successful x86_64 compilation is not sufficient.
- A Tauri migration remains possible if web UI reuse becomes more important than a pure-Rust native client.
- The backend API and sync contract remain the primary cross-client compatibility boundary.

## Alternatives considered

### egui

Still viable for rapid utility-style prototyping, but Iced is selected as the first candidate because its application structure better fits a multi-screen client with explicit state and commands.

### Tauri

Not selected initially because the requested desktop direction is native Rust and system WebView availability complicates consistent Linux packaging.

### Desktop UI on the Raspberry Pi

Rejected as a deployment requirement. The Pi may run the server, while desktop clients run on user workstations.

## Review trigger

Reconsider the toolkit if the first prototype exposes unacceptable accessibility, text input, rendering, platform integration, or packaging limitations.
