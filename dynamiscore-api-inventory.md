# DynamisCore API Inventory

## Public Surface Summary
- `config`: 3 types
- `entity`: 3 types
- `event`: 4 types
- `exception`: 4 types
- `lifecycle`: 4 types
- `logging`: 3 types
- `resource`: 3 types
- `version`: 3 types
- root package: `package-info`

Total public types: 27 (excluding `package-info`)

## Package Inventory

### `org.dynamis.core.entity`
- `EntityId` (`record`, `Comparable`)
- `ComponentId` (`record`, `Comparable`)
- `SystemId` (`record`, `Comparable`, plus `of(String)`)

Assessment:
- Belongs in core.
- Risk: sentinel (`EntityId.NONE`) and hash-derived `SystemId.of(String)` can leak policy/collision semantics into all modules.

### `org.dynamis.core.lifecycle`
- `DynamisSubsystem` (`initialize`, `tick`, `shutdown`, `subsystemName`)
- `TickContext` (`tickNumber`, `deltaTime`, `elapsedTime`)
- `InitContext` (`DynamisConfig`)
- `SubsystemState` enum

Assessment:
- Lifecycle contracts belong in core.
- Boundary hardening applied: `InitContext` no longer carries untyped placeholders.

### `org.dynamis.core.event`
- `EngineEvent` (default `priority`, explicit `timestamp` contract)
- `EventListener<T>`
- `EventPriority`
- `EventSubscription`

Assessment:
- Base event contracts belong in core.
- Timestamp semantics are explicit: event types must provide a stable creation-time timestamp.

### `org.dynamis.core.config`
- `DynamisConfig` (+ nested `Builder`)
- `MissingConfigKeyException`
- `ConfigValueException`

Assessment:
- Config contract belongs in core.
- Risk: boolean parsing is permissive (`true` => true, everything else false), which can hide misconfiguration.

### `org.dynamis.core.logging`
- `DynamisLogger` (JUL-backed facade)
- `LogLevel`
- `LogRecord`

Assessment:
- Logging abstraction can belong in core if it remains backend-agnostic.
- Risk: hard dependency on JUL behavior in `DynamisLogger`; `LogRecord` clones `Throwable` on construction/access.

### `org.dynamis.core.resource`
- `Disposable` (includes `disposeQuietly`)
- `AbstractDisposable`
- `ResourceHandle`

Assessment:
- Cleanup contract belongs in core.
- Boundary hardening applied: `Disposable` no longer depends on `logging`.
- Rename slice complete: package naming is now `resource`.
- `ResourceHandle` and `AbstractDisposable` may be too implementation-heavy for strict core.

### `org.dynamis.core.version`
- `Version`
- `ComponentVersion`
- `InvalidVersionException`

Assessment:
- Belongs in core and is generally stable.

## API Doctrine Notes
- Strengths: mostly small records/interfaces, clear naming, no external compile dependencies.
- Weak points:
  - Runtime policy leaking into core (`DynamisInitException`, `DynamisTickException`, `DynamisShutdownException`).
  - Implementation details in core (`ResourceHandle`, JUL-backed logger).
  - Some defaults are surprising (permissive boolean parsing).
