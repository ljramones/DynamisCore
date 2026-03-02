# Repository Guidelines

## Project Structure & Module Organization
This repository is currently bootstrap-level and contains architecture documentation in `README.md`.
As code is added, keep DynamisCore focused on shared contracts only (IDs, lifecycle, events, config, logging, resource lifecycle, versioning), as defined in the README.
Use a standard Java layout:
- `src/main/java/...` for production code
- `src/test/java/...` for tests
- `docs/` for additional design notes (if needed)

Do not place component-specific logic here; that belongs in downstream Dynamis modules.

## Build, Test, and Development Commands
No build tool files (`pom.xml`, `build.gradle`, wrapper scripts) are committed yet.
When scaffolding is introduced, prefer project-local wrappers and document commands in `README.md`.
Typical commands to standardize on:
- `./mvnw clean verify` or `./gradlew build` for full validation
- `./mvnw test` or `./gradlew test` for test-only runs

Target runtime/toolchain: Java 25+ (see `.java-version`).

## Coding Style & Naming Conventions
- Language: Java
- Indentation: 4 spaces, no tabs
- Use clear, domain-first names: `EntityId`, `TickContext`, `DynamisSubsystem`
- Prefer immutable value types (`record`) for shared data carriers
- Keep interfaces small and dependency-free
- Package names should stay stable and reflect core domains, not implementation details

If formatter/linter config is added (for example Spotless or Checkstyle), run it before opening a PR.

## Testing Guidelines
A test framework is not configured yet. When adding tests:
- Mirror source packages under `src/test/java`
- Name test classes `*Test` (unit) and `*IntegrationTest` (integration)
- Cover contract behavior, equality/hash semantics, and lifecycle edge cases

## Commit & Pull Request Guidelines
Git history is currently minimal (`initial`), so use conventional, imperative commit subjects going forward:
- `core: add EntityId record`
- `test: cover TickContext validation`

For pull requests:
- Explain what changed and why
- Link related issues/tasks
- List verification performed (build/test commands)
- Keep changes scoped; avoid mixing refactors with new behavior

## Architecture Guardrails
DynamisCore must remain dependency-light and reusable across all Dynamis components.
Before adding a type, confirm it is shared by at least two unrelated modules.
