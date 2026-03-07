# DynamisCore Dependency Review

## External Dependencies

From `mvn -DincludeScope=compile dependency:tree`:
- Compile scope: none
- Runtime scope: none
- Test scope: JUnit Jupiter only

Result:
- Transitive burden on downstream modules is currently minimal.

## Internal Coupling Graph

Observed internal imports:
- `lifecycle.InitContext` -> `config.DynamisConfig`
- `config.*Exception` -> `exception.DynamisException`
- `version.InvalidVersionException` -> `exception.DynamisException`

Interpretation:
- Most packages are cleanly independent.
- The previous resource/logging coupling has been removed by boundary hardening.

## Dependency Hygiene Findings

### Good
- Zero external compile dependencies.
- Record-heavy API reduces inheritance/dependency complexity.
- Core contracts are mostly framework-agnostic.

### Risks
- `DynamisLogger` is tied to JUL internals; this is still JDK-only but not backend-neutral.
- Resource contracts still include helper implementation types (`ResourceHandle`, `AbstractDisposable`) that may be too heavy for strict core.

## Namespace and Publishing Readiness

Current coordinates:
- `groupId`: `org.dynamis`
- `artifactId`: `dynamis-core`
- Packages: `org.dynamisengine.core.*`

Target story discussed by project direction:
- `org.dynamisengine` namespace

Gap:
- Coordinates and packages are not yet aligned with `org.dynamisengine`.

Recommended alignment plan:
1. Adopt parent/BOM with canonical group (`org.dynamisengine`) once stable.
2. Decide package migration strategy before 1.0.0 release (direct rename vs compatibility bridge module).
3. Freeze core API only after namespace choice is final to avoid cascading downstream refactors.

## Overall Verdict
- Dependency footprint: excellent.
- Dependency hygiene: good, with recent boundary leaks addressed in this slice.
- Publishing readiness: partial; namespace alignment remains a blocker.
