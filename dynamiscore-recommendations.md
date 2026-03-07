# DynamisCore Recommendations

## Executive Answer
DynamisCore is close to a minimal foundation, but it is showing early junk-drawer signals in a few boundary types. Fixing those now is low cost and high leverage.

## Status After Boundary-Hardening Slice
- Resolved: `InitContext` no longer exposes untyped `Object` placeholders.
- Resolved: `Disposable` no longer depends on logging (`DynamisLogger` import removed).

## Keep / Move / Rename / Remove

## Keep
- `entity` IDs (`EntityId`, `ComponentId`, `SystemId`) with validation
- `Version` and `ComponentVersion`
- `DynamisConfig` shape (builder + typed accessors)
- `EventPriority`, `EventListener`, `EventSubscription`
- `TickContext`, `SubsystemState`, `DynamisSubsystem` lifecycle contract
- Base `DynamisException`

## Move (or narrow)
- `DynamisInitException`, `DynamisTickException`, `DynamisShutdownException`
  - Reason: these encode engine runtime policy; better in runtime/orchestrator module.
- `NativeResource`, `AbstractDisposable`
  - Reason: these are implementation helpers, not minimal shared contracts.

## Rename / Refactor
- `native_` package -> `resource` (or `lifecycle.resource`)
- `InitContext`:
  - Completed in this slice by removing untyped placeholder fields.
- `Disposable.disposeQuietly()`:
  - Completed in this slice by removing direct logging side effects/dependency.

## Remove / Change Behavior
- `EngineEvent.timestamp()` default method:
  - Remove default or document as non-stable call-time value and discourage repeated calls.
- `EntityId.NONE` sentinel:
  - Prefer `Optional<EntityId>` or explicit nullable contract at call sites.
- `SystemId.of(String)` hash-based ID:
  - Keep only if collision semantics are explicitly acceptable; otherwise remove from core.

## Hot-Path and Allocation Review

High-risk pervasive patterns:
- `LogRecord` copies `Throwable` on construction and on accessor (`cause()`): hidden allocation and loss of throwable type/cause chain fidelity.
- `EngineEvent.timestamp()` calls `System.nanoTime()` each invocation: cheap but semantically unstable and encourages repeated time reads.

Moderate risks:
- `TickContext` with `double` time fields can accumulate precision drift in long sessions.
- Spin-wait loops in disposable base classes are acceptable for short critical sections but can burn CPU under prolonged disposal contention.

## API Doctrine Improvements
- Keep core contracts boring and policy-free:
  - data carriers + tiny interfaces
  - no backend assumptions
  - no hidden side effects in default methods
- Prefer explicit semantics:
  - creation timestamp vs observation timestamp
  - strict config parsing vs permissive fallback

## Namespace and Publishing Actions

Before broader publication:
1. Decide canonical namespace (`org.dynamisengine`) and update Maven coordinates.
2. Lock package naming policy and migration strategy.
3. Separate contract API from helper implementations (especially resource and logging internals).
4. Run `mvn clean verify` after refactor to baseline style + bug checks.

## Suggested Implementation Order
1. Refactor boundary leaks (`InitContext`, `Disposable`, `native_` package).
2. Remove/adjust surprising defaults (`EngineEvent.timestamp`, `EntityId.NONE`, `SystemId.of(String)`).
3. Namespace/publishing alignment (`groupId`, package strategy, parent adoption).
4. Only then cut stable public API milestone.
