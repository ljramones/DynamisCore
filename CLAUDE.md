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
- explicit native resource lifecycle contracts
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
- Checkstyle suppression in `checkstyle-suppressions.xml` allows `org.dynamis.core.native_` package naming; trailing underscore is intentional because `native` is a Java keyword.
- SpotBugs exclusion in `spotbugs-exclude.xml` suppresses `CT_CONSTRUCTOR_THROW` for `NativeResource`; constructor validation for non-zero handles is required by contract.
