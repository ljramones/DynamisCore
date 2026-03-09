# DynamisCore Architecture Boundary Ratification Review

Date: 2026-03-09

## Intent and Scope

This is a **boundary-ratification review** for DynamisCore based on current repository code and docs.

This pass does not refactor code or rename APIs. It establishes a strict ownership and dependency boundary that can be used as a reference for follow-on reviews (`DynamisEvent`, `DynamisECS`, `DynamisSceneGraph`, `DynamisLightEngine`).

## 1) Repo Overview (Grounded)

Repository shape:

- Single-module Java 25 JAR (`pom.xml`, packaging `jar`)
- Root package exports via JPMS (`src/main/java/module-info.java`):
  - `org.dynamisengine.core.config`
  - `org.dynamisengine.core.entity`
  - `org.dynamisengine.core.event`
  - `org.dynamisengine.core.exception`
  - `org.dynamisengine.core.lifecycle`
  - `org.dynamisengine.core.logging`
  - `org.dynamisengine.core.resource`
  - `org.dynamisengine.core.version`
- Compile/runtime external dependencies: none (test-only JUnit)

Current public type inventory is small and contract-oriented (27 types; consistent with `dynamiscore-api-inventory.md`).

## 2) Strict Ownership Statement

### 2.1 What DynamisCore should own

DynamisCore should own **minimal, cross-component contracts** that are:

1. required by multiple unrelated repos
2. policy-neutral
3. implementation-light
4. dependency-light

Based on code, this includes:

- Identity/value contracts:
  - `EntityId`, `ComponentId`, `SystemId`
- Lifecycle contracts and timing context:
  - `DynamisSubsystem`, `InitContext`, `TickContext`, `SubsystemState`
- Event base contracts (not event bus implementation):
  - `EngineEvent`, `EventListener`, `EventPriority`, `EventSubscription`
- Core exception root and minimal shared exception types:
  - `DynamisException`, config/version parsing exceptions
- Shared resource lifecycle interfaces:
  - `Disposable` (+ possibly `ResourceHandle` contract semantics)
- Version compatibility contracts:
  - `Version`, `ComponentVersion`

### 2.2 What DynamisCore must never own

DynamisCore must not own:

- feature logic (AI, UI, rendering, physics, audio, terrain, etc.)
- runtime orchestration/policy
- subsystem-specific execution behavior
- renderer/world/session-specific policy
- heavy general-purpose utility expansion unrelated to strict cross-component contracts

This aligns with the repository’s own doctrine in `README.md` and `docs/dynamiscore-final-contract.md`.

## 3) Dependency Rules

### 3.1 Allowed dependencies for DynamisCore

- JDK only (current state: `requires java.logging` in JPMS)
- test-only dependencies for verification

### 3.2 Forbidden dependencies for DynamisCore

- Any Dynamis feature/runtime modules (`DynamisGPU`, `DynamisLightEngine`, `DynamisWorldEngine`, etc.)
- third-party runtime libraries that would impose transitive policy/runtime coupling
- backend-specific APIs (graphics/window/audio/native integrations)

### 3.3 Who may depend on DynamisCore

All higher-layer Dynamis repos may depend on DynamisCore contracts.

Required direction:

- `DynamisCore` is dependency-root substrate
- no cyclic or upward dependencies into policy/feature/runtime repos

## 4) Public vs Internal Boundary

### 4.1 Canonical public boundary

Current exported packages in `module-info.java` define public boundary and are appropriate as core contract surface:

- `config`, `entity`, `event`, `exception`, `lifecycle`, `logging`, `resource`, `version`

### 4.2 Internal/private areas

There is little explicit internal package segregation in this repo (single module, mostly exported contracts).

Practical interpretation:

- types in exported packages are effectively public API and should be treated as stable contracts
- helper implementation classes inside exported packages are public by module design and require stronger discipline to avoid policy drift

## 5) Drift / Overreach Findings

### 5.1 Clean boundaries confirmed

- No external runtime dependencies.
- Public surface is mostly small records/interfaces and enums.
- Core remains feature-agnostic and avoids direct coupling to subsystem implementations.
- Identity and lifecycle contracts are straightforward and stable.

### 5.2 Areas to watch (constraints, not immediate refactor)

1. `org.dynamisengine.core.logging`
- `DynamisLogger` is explicitly JUL-backed (`java.util.logging`).
- This is still JDK-only and acceptable now, but it is not a fully backend-neutral facade.
- Boundary risk: logging backend policy can leak into core if expanded.

2. `org.dynamisengine.core.resource`
- `ResourceHandle` and `AbstractDisposable` include concrete concurrency/disposal behavior.
- They are useful, but more implementation-heavy than pure contracts.
- Boundary risk: core becoming a lifecycle utility home instead of a contract layer.

3. `org.dynamisengine.core.exception`
- `DynamisInitException`, `DynamisTickException`, `DynamisShutdownException` encode engine lifecycle semantics in core.
- These are still generic enough, but they are closer to orchestration policy than pure substrate types.
- Boundary risk: additional orchestration-specific exception hierarchies drifting into core.

4. `org.dynamisengine.core.config.DynamisConfig`
- Strongly useful as shared config contract.
- Boolean parsing (`true` => true, everything else false) is permissive and can hide configuration mistakes.
- This is a behavior-quality concern, not a boundary violation.

## 6) Ratification Result

**Ratified with constraints**.

Why:

- The current repository is fundamentally aligned with strict core-substrate responsibilities.
- No major feature/runtime policy contamination is present.
- A few packages (`logging`, parts of `resource`, lifecycle-specific exceptions) require ongoing boundary discipline to prevent gradual overreach.

## 7) Strict Boundary Rules to Carry Forward

1. Add to core only if at least two unrelated components require it and neither should own it.
2. Keep core policy-free and backend-free.
3. Prevent utility sprawl: no broad helper libraries in core.
4. Treat all exported package types as contract-grade APIs.
5. Keep orchestration and feature policy above core (WorldEngine/LightEngine/Scripting layers).

## 8) Recommended Next Step

Next deep review should be **DynamisEvent**.

Reason:

- It is directly downstream from Core contracts (`EngineEvent`, listener/subscription semantics).
- It is the first place where policy leakage risk appears (dispatch behavior, ordering semantics, runtime wiring expectations).
- Ratifying Event next creates a stable boundary before reviewing `DynamisECS`, `DynamisSceneGraph`, and `DynamisLightEngine`.
