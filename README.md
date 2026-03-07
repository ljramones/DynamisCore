# DynamisCore

The shared foundational layer for the Dynamis game engine ecosystem. DynamisCore owns the types, interfaces, and contracts that every other Dynamis component depends on — entity identity, subsystem lifecycle, tick context, event base types, logging abstraction, configuration, and resource management.

DynamisCore has zero dependencies. It is the one component every other Dynamis component takes.

---

## Architecture

DynamisCore is intentionally small. The inclusion rule is strict: if two or more unrelated components need the same type and neither should own it, it belongs here. If only one component needs it, it stays in that component. Target size is 20–30 classes and interfaces total.

---

## Entity Identity

**`EntityId`** — a record wrapping a `long`. The universal identifier for anything that exists in the game world. Every component that touches entities uses this type. Equality and hashing are value-based.

**`ComponentId`** — identifies a component type within an entity.

**`SystemId`** — identifies a registered subsystem within DynamisWorldEngine.

```java
EntityId player = EntityId.of(1L);
EntityId npc    = EntityId.of(2L);
```

---

## Subsystem Lifecycle

**`DynamisSubsystem`** — the interface every Dynamis subsystem implements. DynamisWorldEngine depends on this interface, not on concrete subsystem types. Subsystems register themselves and are initialized, ticked, and shut down through this contract.

```java
public interface DynamisSubsystem {
    void initialize(InitContext ctx);
    void tick(TickContext ctx);
    void shutdown();
}
```

**`TickContext`** — a record passed to every subsystem on each tick. Carries tick number, delta time in seconds, and total elapsed time. Owned here so every component uses the same type.

```java
public record TickContext(long tickNumber, double deltaTime, double elapsedTime) {}
```

**`InitContext`** — carries initialization-time shared data from core. It currently contains engine configuration and is passed once during startup.

---

## Event Base Types

**`DynamisEvent`** — sealed base interface for all engine events. Every event published through DynamisEvent (the bus component) extends this interface. Owned here so DynamisEvent (the bus) and DynamisScripting can both reference the type without depending on each other.

**`EventPriority`** — enum for event dispatch ordering: `CRITICAL`, `HIGH`, `NORMAL`, `LOW`.

**`EngineEvent#timestamp()`** — event types provide a stable timestamp captured at event creation time.

---

## Configuration

**`DynamisConfig`** — typed configuration container. Loaded at engine startup and passed through `InitContext` to every subsystem. Replaces scattered system properties and magic strings. Subsystems declare their own config sections as nested records.

---

## Logging Abstraction

**`DynamisLogger`** — thin logging facade. Every Dynamis component logs through this rather than taking a direct logging framework dependency. Allows DynamisDebug to intercept and surface log output in the diagnostic overlay without modifying component code.

```java
DynamisLogger log = DynamisLogger.get(MySubsystem.class);
log.debug("Subsystem initialized");
```

---

## Error Foundation

**`DynamisException`** — root unchecked exception for the entire ecosystem. All component-specific exception hierarchies root here rather than directly in `RuntimeException`. Provides a single catch point at the DynamisWorldEngine level for unhandled engine errors.

```java
public class DynamisException extends RuntimeException {
    public DynamisException(String message) { ... }
    public DynamisException(String message, Throwable cause) { ... }
}
```

---

## Resource Management

For components that wrap external handles — Jolt Physics rigid bodies, Vulkan handles, OpenAL buffers — DynamisCore provides explicit lifecycle contracts. Pure Java components ignore these entirely.

**`Disposable`** — interface for anything that holds external or GPU resources requiring explicit cleanup. Implemented by DynamisPhysics, DynamisLightEngine, DynamisAudio, and any other component wrapping external memory.

```java
public interface Disposable {
    void dispose();
    boolean isDisposed();
}
```

**`ResourceHandle`** — base for objects wrapping external handles. Extends `Disposable`, adds `isValid()` and `resourceHandle()`. Makes ownership explicit and auditable — you always know what holds external memory.

---

## Versioning

**`Version`** — semantic version record. Every Dynamis component declares its version. DynamisWorldEngine verifies compatibility across all registered subsystems on startup.

```java
public record Version(int major, int minor, int patch) {
    public static final Version CURRENT = new Version(1, 0, 0);
    public boolean isCompatibleWith(Version other) { ... }
}
```

---

## What DynamisCore Does NOT Own

- **Math types** — that is Vectrix
- **Asset types** — that is DynamisContent
- **World state** — that is DynamisSession
- **Rendering** — that is DynamisLightEngine
- **Business logic** — that is DynamisScripting

If a type belongs to a single component, it stays in that component.

---

## Ecosystem Position

```
DynamisCore (no dependencies)
    ↓
Every other Dynamis component
```

DynamisCore is the only Dynamis dependency that flows in one direction only. Nothing in the ecosystem depends on a component that depends on DynamisCore — the dependency graph is a strict DAG with DynamisCore at the root.

---

## Requirements

- Java 25+

---

## License

Apache 2.0 — see [LICENSE](LICENSE) for details.
