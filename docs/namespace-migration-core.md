# DynamisCore Namespace Migration Plan

Old root: `org.dynamis.core`  
New root: `org.dynamisengine.core`

## Scope
- Java packages
- imports
- module descriptor
- tests
- docs

## Out Of Scope
- behavioral changes
- API redesign

## Validation
- `mvn -q test` passed.
- `mvn -q -DskipTests package` passed.
- `rg "org\.dynamis\.core"` now matches only this plan document's historical "Old root" line.
