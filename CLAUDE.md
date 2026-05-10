# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

```bat
# Build debug APK
build.bat

# Install on connected device/emulator
./gradlew installDebug

# Run Android Lint
./gradlew lint

# Clean build outputs
./gradlew clean
```

No test suite is configured. Lint is the primary static analysis tool.

## Architecture

Single-activity Android app (Kotlin + Jetpack Compose) for organizing outfit photos with tag-based filtering.

**Stack:** Kotlin 2.0.21, Compose BOM 2024.09.03, Room 2.6.1, Coil 2.7.0, KSP, AGP 8.13.2, Gradle 8.7  
**Namespace:** `de.schmelzle.outfitorganizer` | Min SDK 26, Target/Compile SDK 35

### Layer overview

```
MainActivity
  └── OutfitScreen          ← single screen composable; owns no state
        └── OutfitViewModel ← AndroidViewModel; exposes StateFlow / LiveData
              └── OutfitRepository
                    ├── OutfitDao (Room)
                    └── ImageStorage (internal files dir)
```

**Data flow:**
1. `OutfitViewModel` collects `Repository.allOutfits: Flow<List<Outfit>>` and applies tag filtering in-memory (AND logic — all active tags must match).
2. `OutfitScreen` renders filtered outfits through `OutfitGallery` (2-column grid of `OutfitCard`s). `showImportDialog` is local `remember` state in `OutfitScreen`, not in the ViewModel.
3. Tapping a card opens `ImageDetailOverlay` (full-screen viewer with swipe navigation across the filtered list). `showNext()`/`showPrevious()` close the overlay when navigating past the first/last item.
4. The FAB / top-bar add button opens `ImportDialog`, which immediately auto-launches the system photo picker; cancelling the picker dismisses the dialog without showing tag UI.
5. Delete removes both the DB row and the copied image file via `ImageStorage.delete()`.

### Key files

| File | Role |
|------|------|
| `app/src/main/java/.../OutfitViewModel.kt` | All business logic: filter, select, import, delete |
| `app/src/main/java/.../OutfitRepository.kt` | Maps `OutfitEntity` ↔ `Outfit`; insert/delete |
| `app/src/main/java/.../OutfitDatabase.kt` | Room singleton (`outfits.db`, version 1) |
| `app/src/main/java/.../data/OutfitEntity.kt` | Tags stored as comma-separated string |
| `app/src/main/java/.../model/Tag.kt` | Enum: `FRUEHLING`…`FEEL_GOOD`; each has a `.label` with German display name |
| `app/src/main/java/.../util/ImageStorage.kt` | Copies URI → app internal storage; deletes file |
| `app/src/main/java/.../ui/components/` | All Compose UI components |
| `gradle/libs.versions.toml` | Central version catalog for all dependencies |

### Permissions

`READ_MEDIA_IMAGES` (Android 13+) with `READ_EXTERNAL_STORAGE` fallback declared in the manifest. Runtime permission handling is in `OutfitViewModel`.
