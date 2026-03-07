# DynamisCore Contributor Notes

## Inclusion Rule
Add a type to DynamisCore only when **two or more unrelated Dynamis components** need the same contract and no single component should own it. If a type is specific to one component, it must stay in that component.

## Scope
DynamisCore is intentionally small (target: ~20-30 classes/interfaces), dependency-free at runtime, and focused on foundational contracts:
- entity and subsystem identity/value types
- subsystem lifecycle contracts and contexts
- event base abstractions
- configuration contracts
- logging abstraction
- shared exception base
- explicit resource lifecycle contracts
- version compatibility model

## Out of Scope
Do not add:
- math primitives (Vectrix)
- asset/content models (DynamisContent)
- world/session state implementations (DynamisSession)
- rendering, physics, audio, scripting business logic

## Project Shape
Single-module Maven project, package root: `org.dynamis.core`.

## Known Caveats
- `SystemId.of(String)` derives IDs from `name.hashCode()`; IDs are deterministic but not guaranteed unique because hash collisions can occur.
- Java does not allow `sealed` types with an empty `permits` list, and sealed hierarchies would also prevent event subtypes in other components/packages. `EngineEvent` is intentionally an open interface until concrete cross-component event ownership is finalized.
- SpotBugs exclusion in `spotbugs-exclude.xml` suppresses `CT_CONSTRUCTOR_THROW` for `ResourceHandle`; constructor validation for non-zero handles is required by contract.

## Class Inventory

### `org.dynamis.core.config`
- `DynamisConfig`: immutable string-backed configuration container with typed getters and builder.
- `MissingConfigKeyException`: thrown when a required configuration key is absent.
- `ConfigValueException`: thrown when a configuration value cannot be parsed to a requested type.

### `org.dynamis.core.entity`
- `EntityId`: positive long wrapper for entity identity.
- `ComponentId`: positive long wrapper for component identity.
- `SystemId`: positive long wrapper for subsystem identity, including name-hash factory.

### `org.dynamis.core.event`
- `EngineEvent`: base marker interface for engine events with default priority and explicit creation-time timestamp contract.
- `EventPriority`: dispatch priority enum with explicit numeric levels.
- `EventListener`: functional interface for typed event consumption with listener priority.
- `EventSubscription`: immutable subscription handle with cancellation callback.

### `org.dynamis.core.exception`
- `DynamisException`: root unchecked exception for all Dynamis component exception hierarchies.
- `DynamisInitException`: fatal initialization failure for a subsystem.
- `DynamisTickException`: tick-time subsystem failure, recoverable by policy.
- `DynamisShutdownException`: shutdown-time subsystem failure, recoverable by policy.

### `org.dynamis.core.lifecycle`
- `DynamisSubsystem`: core lifecycle contract (`initialize`, `tick`, `shutdown`) for subsystems.
- `TickContext`: validated per-tick timing context passed to subsystems.
- `InitContext`: validated initialization context carrying config.
- `SubsystemState`: lifecycle state enum for subsystem state tracking.

### `org.dynamis.core.logging`
- `LogLevel`: abstract logging levels with threshold comparison utility.
- `DynamisLogger`: JUL-backed logging facade with core engine-level log API.
- `LogRecord`: immutable structured log entry for capture/diagnostic workflows.

### `org.dynamis.core.resource`
- `Disposable`: idempotent cleanup contract with quiet-dispose helper.
- `AbstractDisposable`: atomic dispose-once base class for Java-managed resources.
- `ResourceHandle`: atomic dispose-once base class for resource-handle-backed resources.

### `org.dynamis.core.version`
- `Version`: semantic version value type with parsing, comparison, and compatibility checks.
- `InvalidVersionException`: thrown for malformed semantic version input.
- `ComponentVersion`: named component/version pair with compatibility checks.
