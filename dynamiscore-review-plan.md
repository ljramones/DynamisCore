# DynamisCore Review Plan

## Objective
Determine whether DynamisCore is a minimal, stable foundation or trending into a junk drawer.

## Current Snapshot (2026-03-06)
- Production types: 28 (`src/main/java/org/dynamis/core/**`)
- Test classes: 8 (`src/test/java/org/dynamis/core/**`)
- External compile dependencies: none (`mvn dependency:tree`)
- Java target: 25 (`pom.xml`)

## Review Passes

### Pass 1: Structural Review
- Map module purpose to actual contents.
- Inventory public API by package and type.
- Identify belongs/does-not-belong mismatches against core guardrails.
- Produce keep/move/rename/remove candidates.

Deliverables:
- `dynamiscore-api-inventory.md`
- first section of `dynamiscore-recommendations.md`

### Pass 2: Hot-Path and Allocation Review
- Inspect default methods and common flows for hidden allocations.
- Flag pervasive patterns likely to appear in tick/event/logging paths.
- Classify each risk as semantic, allocation, or contention risk.

Deliverables:
- performance section in `dynamiscore-recommendations.md`

### Pass 3: Namespace and Publishing Readiness
- Check package namespace consistency.
- Check Maven coordinates and parent alignment readiness.
- Validate publication baseline (dependency hygiene, API stability, docs alignment).

Deliverables:
- `dynamiscore-dependency-review.md`
- publishing section in `dynamiscore-recommendations.md`

## Decision Rubric
DynamisCore is healthy if all are true:
- Compile surface remains dependency-light and framework-agnostic.
- API contracts are stable and hard to misuse.
- No component-specific runtime policy leaks into core types.
- Default behaviors avoid hidden allocation or surprising semantics.
- Coordinates/packages align with `org.dynamisengine` publishing direction.

## Working Evidence
- Source: `src/main/java/org/dynamis/core/**`
- Tests: `src/test/java/org/dynamis/core/**`
- Build metadata: `pom.xml`, `README.md`
- Validation commands:
  - `mvn test`
  - `mvn -DincludeScope=compile dependency:tree`

## Slice Status
- Boundary-hardening slice complete:
  - `InitContext` is now config-only.
  - Untyped provider placeholders were removed.
  - `Disposable` no longer depends on logging and remains policy-free.
  - Legacy resource package naming was normalized to `resource`, and `ResourceHandle` is now the core handle wrapper type.
  - Compile dependency footprint remains effectively zero external dependencies.
- Next cleanup candidates:
  - permissive config boolean parsing behavior.
  - `SystemId.of(String)` collision/policy semantics.
  - runtime-policy exceptions currently living in core.
