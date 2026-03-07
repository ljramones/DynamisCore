# DynamisCore Final Contract

## 1. Purpose Of DynamisCore
- DynamisCore must remain a minimal foundation shared across unrelated Dynamis modules.
- DynamisCore must remain policy-free and dependency-light.
- DynamisCore must define stable contracts, not module-specific behavior.

## 2. EntityId
- `EntityId` must always represent a real, valid entity identity.
- `EntityId` must not provide a sentinel value such as `NONE`.
- Entity absence must be handled at usage boundaries (for example, `Optional<EntityId>`), not inside the identity type.

## 3. SystemId
- `SystemId` must represent explicit numeric identity.
- DynamisCore must not provide string-derived `SystemId` construction.
- Name-to-ID mapping does not belong in DynamisCore and must be owned by higher-level modules.

## 4. EngineEvent.timestamp()
- `EngineEvent.timestamp()` must be explicit and implemented by each event type.
- The timestamp must represent creation-time semantics.
- Timestamp values must be stable for a given event instance.
- DynamisCore must not provide accessor-time/default timestamp behavior.

## 5. Resource Lifecycle
- `ResourceHandle` must represent externally-backed resource ownership in core contracts.
- `Disposable` must remain policy-free.
- Core cleanup contracts must not couple to logging behavior.

## 6. InitContext
- `InitContext` must remain config-only in DynamisCore.
- DynamisCore must not use untyped provider placeholders in `InitContext`.
- Extension and module policy does not belong in `InitContext` and must be defined outside core.

## 7. What Core Must Not Do
- DynamisCore must not leak runtime policy into foundational contracts.
- DynamisCore must not couple low-level cleanup contracts to logging behavior.
- DynamisCore must not introduce sentinel identity shortcuts.
- DynamisCore must not introduce string-derived identity shortcuts.
- DynamisCore must not introduce untyped boundary placeholders.

## 8. Future Change Rule
- Changes to these contracts must be rare, deliberate, and reviewed as boundary changes.
- Any proposed contract change must explicitly document ecosystem impact before acceptance.
