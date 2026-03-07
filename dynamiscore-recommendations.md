# DynamisCore Recommendations

## Executive Answer
DynamisCore is close to a minimal foundation, but it is showing early junk-drawer signals in a few boundary types. Fixing those now is low cost and high leverage.

Canonical contract reference: `docs/dynamiscore-final-contract.md`.

## Status After Boundary-Hardening Slice
- Resolved: `InitContext` no longer exposes untyped `Object` placeholders.
- Resolved: `Disposable` no longer depends on logging (`DynamisLogger` import removed).
- Resolved: resource package naming was normalized and `ResourceHandle` is the canonical core handle wrapper.
- Resolved: `EngineEvent.timestamp()` is now an explicit stable creation-time contract (no call-time default).
- Resolved: `EntityId.NONE` sentinel was removed from the core identity contract.
- Resolved: `SystemId.of(String)` was removed; core system IDs are explicit numeric identities.

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
- `ResourceHandle`, `AbstractDisposable`
  - Reason: these are implementation helpers, not minimal shared contracts.

## Rename / Refactor
- `resource` package rename:
  - Completed in this slice.
- `InitContext`:
  - Completed in this slice by removing untyped placeholder fields.
- `Disposable.disposeQuietly()`:
  - Completed in this slice by removing direct logging side effects/dependency.

## Hot-Path and Allocation Review

High-risk pervasive patterns:
- `LogRecord` copies `Throwable` on construction and on accessor (`cause()`): hidden allocation and loss of throwable type/cause chain fidelity.

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
1. Refactor boundary leaks (`InitContext`, `Disposable`, `resource` package naming cleanup) [completed].
2. Remove/adjust surprising defaults [completed].
3. Namespace/publishing alignment (`groupId`, package strategy, parent adoption).
4. Only then cut stable public API milestone.
