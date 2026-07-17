# ARCHITECTURE.md

Working notes for whoever touches this repo next. This started as a take-home interview
assignment: an MVP Android app for "AMRO" (a fictional movie-recommendation startup used as the
assignment's brief) showing this week's top 100 trending movies (TMDB-backed), with genre
filtering, sorting, and a detail screen. The gist of that brief is in "Product requirements"
below.

This file is a **living document** - keep it in sync as the implementation progresses. It is not
the handover doc (that's `README.md`, written last, for the interviewer); this one is for
whoever is *building* the thing.

## Working style for this repo

- One task at a time. Finish it, verify it builds, get explicit confirmation before starting the
  next one.
- Every task gets its own commit, message prefixed with a sequential number and a short
  imperative summary, e.g. `6 - Add MovieListViewModel`. Numbers are global across the whole
  project, not per-phase.
- Before committing anything that touches Gradle files: run `./gradlew build` (or at least
  `./gradlew :the:affected:module:build`) and confirm it's green. Don't take AGP/Gradle API
  behavior on faith - this project is on bleeding-edge versions (see below) that have already
  broken assumptions twice (AGP 9's non-generic `CommonExtension`, AGP 9's "Built-in Kotlin"
  auto-applying `org.jetbrains.kotlin.android`). When something doesn't compile the way older
  AGP/Gradle docs or muscle memory suggest, inspect the actual jars (`javap` on the class in
  question) instead of guessing.

## Product requirements (summary)

- **Trending list**: this week's top 100 trending movies (TMDB `trending/movie/week`, 5 pages of
  20 merged client-side). Show title, image, genre. Genre filter operates only on the fetched 100
  (don't try to backfill more). Sort by popularity (default) / title / release date, asc or desc.
- **Detail screen**: title, tagline, image, genre, description, vote average, vote count, budget,
  revenue, status, IMDB link, runtime, release date.
- Basic error handling (network/empty/error states).
- Architecture must support future independent feature teams (actor info, profiles, streaming,
  social), eventual offline support, and eventually multiple API sources.
- Deliberate, demonstrable test strategy - not 100% coverage for its own sake.

## Toolchain

- AGP 9.2.1, Kotlin 2.2.10, Gradle 9.4.1, Compose BOM 2026.02.01, KSP 2.2.10-2.0.2.
- compileSdk / targetSdk 37, minSdk 24.
- AGP 9's "Built-in Kotlin" compiles Kotlin itself - **do not** apply
  `org.jetbrains.kotlin.android` explicitly in any module or convention plugin; it collides with
  AGP's own `kotlin` extension. See `AndroidLibraryConventionPlugin`.
- `android.disallowKotlinSourceSets=false` is set in `gradle.properties` because KSP (Room) still
  registers generated sources via the classic `kotlin.sourceSets` DSL, which built-in Kotlin
  disallows by default. Remove this once KSP catches up.

## Architecture decisions

1. **Each feature owns its full vertical slice (data + domain + presentation).** No centralized
   `core:domain` / `core:data` *shared across features*. Modeled directly on `alemicode/Panjere`'s
   `feature/profile` module, which has its own `datasource/`, `usecase/`, `model/`, `ui/`,
   `viewmodel/` - nothing shared with other features. A centralized domain/data module would
   become a bottleneck every future feature team has to touch, which directly contradicts AMRO's
   "independent feature teams" requirement. See git history for the full reasoning (this was a
   correction after the first pass centralized things - don't repeat that mistake for new
   features).
   - Within a feature, `data`/`domain`/`presentation` are separate Gradle modules
     (`feature:movies:data`, `feature:movies:domain`, `feature:movies:presentation`), not just
     packages inside one module. This makes the dependency rule enforceable by the build graph
     instead of convention: `presentation` and `data` both depend on `domain`, `presentation`
     never depends on `data` directly (they're only wired together via DI in `:app`), and Gradle
     itself fails the build if that's violated. Still a single feature's slice - no sharing with
     other features.
   - Consequence: there's also no shared `core:database`. Room's `@Database`-annotated class must
     reference its entity classes directly, so a shared DB module would need a dependency *back*
     on the feature module that owns those entities - backwards. Each feature owns its own Room
     database instance (in its `data` module).
   - **Rule of thumb for new modules**: don't extract a shared module preemptively. If a *second*
     feature genuinely needs something a feature currently owns (e.g. the `Movie` model), that's
     the trigger to extract it - not before (YAGNI, and matches Panjere's own `common:*` tier,
     which only exists for things 2+ features actually use).
2. **DI: Koin, not Hilt.** No annotation processing/KSP overhead for DI itself, fast multi-module
   builds, plain Kotlin. Trade-off vs Hilt (less compile-time safety) is worth mentioning in the
   interview.
3. **Networking: Retrofit + OkHttp + kotlinx.serialization.** No Gson/Moshi reflection.
   `core:network` provides only generic infra (OkHttpClient, Retrofit instance, TMDB auth
   interceptor, `safeApiCall`). The TMDB API interface and DTOs are owned by
   `feature:movies:data`, not `core:network` - `core:network` doesn't know TMDB exists, so a
   second feature hitting a different API doesn't touch it.
4. **Images: Coil 3** (Compose-first, `AsyncImage`).
5. **Navigation: Compose Navigation, type-safe routes** (`@Serializable` route objects).
   `feature:movies:presentation` exposes its own graph builder; `app`'s `NavHost` includes it.
6. **Caching (offline-first, single source of truth), refresh is action-driven, not time-driven:**
   request -> write to Room -> read from Room -> display, always through that same path so the UI
   never renders anything the cache didn't see first.
   - Trending list + genres: fetch TMDB pages 1-5, replace the Room cache. Triggered once when the
     list screen/ViewModel is created (app open) and again on pull-to-refresh - no staleness
     timestamp, no time-based auto-refresh. The UI continuously observes the cache via a Room
     `Flow`, so it still renders the last successful fetch if a refresh fails or while offline.
   - Movie detail: same pattern per id - fetch, write, read, display - triggered on screen open.
   - Filtering/sorting run **in-memory** over the ~100 cached items (`derivedStateOf` / pure
     functions) - no DB queries needed. Matches the brief's "filter what's shown, don't backfill."
7. **Testing: JUnit5 + MockK + AssertK.** `core:testing` exposes these as `api` deps plus
   feature-agnostic rules (`MainDispatcherExtension`, a JUnit5 `BeforeEach`/`AfterEach` hook that
   sets `Dispatchers.Main` for ViewModel-scoped coroutines). It does **not** hold feature-specific
   fakes (e.g. a `FakeMoviesRepository`) - those live inside the feature module's own test
   sources, since `core:testing` can't depend back on a feature module. Flow-based state (e.g. a
   ViewModel's lazily-shared `stateIn(WhileSubscribed)`) is tested with a `backgroundScope`
   collector on an `UnconfinedTestDispatcher`, asserting on `.value` - see
   [Android's testing guidance](https://developer.android.com/kotlin/flow/test) - rather than a
   separate Flow-testing library.
8. **Build tooling: version catalog + `build-logic` convention plugins**, so a new feature
   module's `build.gradle.kts` is ~10 lines. See "Convention plugins" below.
9. **TMDB auth: v4 Read Access Token (Bearer) only**, not the v3 `api_key` query param. Stored in
   gitignored `local.properties`, exposed as `BuildConfig.TMDB_READ_ACCESS_TOKEN` in
   `core:network`. Copy `local.properties.sample` to get started; the build fails fast with a
   clear message if the key is missing.

## Module map

```
app                        composition root: MainActivity, NavHost, Koin startup
design-system              theme, colors, typography, spacing, shapes, reusable Compose components
core:common                Result<D,E>/DataError, DispatcherProvider - infra, no business logic
core:network               OkHttp/Retrofit/kotlinx.serialization setup, TMDB auth interceptor, safeApiCall
core:testing               MockK/AssertK/JUnit5 exposed as `api`, shared feature-agnostic test rules
feature:movies:domain      models, repository interface, use cases - no Android/Room/Retrofit deps
feature:movies:data        TMDB DTOs, Room entities/DAO, repository impl - implements domain's repository
feature:movies:presentation ViewModels, Compose screens, nav graph - depends on domain only
```

Dependency direction: `feature:*` depends on `core:*` and `design-system`, never the reverse.
`core:*` modules never depend on each other's business logic and never on `feature:*`. Two
features never depend on each other directly. Within `feature:movies`, `data` and `presentation`
both depend on `domain`; `presentation` never depends on `data` - they're wired together only via
Koin DI modules assembled in `:app`.

## Convention plugins (`build-logic/convention`)

| Plugin ID | What it does | Applies to |
|---|---|---|
| `amro.android.library` | Android Library plugin, shared compileSdk/minSdk/Java config, JUnit5 runner, common test deps | every `core:*`, transitively every `feature:*` |
| `amro.android.compose` | Compose compiler plugin, BOM, UI/tooling deps | `design-system`, transitively every `feature:*` |
| `amro.android.feature` | library + compose + koin, plus design-system/core:common/navigation/koin-compose deps | every `feature:*:presentation` |
| `amro.koin` | Koin BOM + core + test artifacts | anything declaring DI bindings |
| `amro.room` | KSP + Room runtime/ktx/compiler | anything with a local Room DB (currently `feature:movies:data`) |

`amro.android.feature` pulls in Compose/navigation/design-system, so it's meant for a feature's
`presentation` module specifically - not `data` or `domain`, which use `amro.android.library`
directly (plus `amro.room`/`amro.koin` as needed) since they have no UI concerns.

A new feature's presentation module `build.gradle.kts` should look like:
```kotlin
plugins {
    id("amro.android.feature")
    // + kotlin.serialization / whatever that specific feature needs
}

android {
    namespace = "com.alemicode.amromovies.feature.xxx.presentation"
}

dependencies {
    implementation(project(":feature:xxx:domain"))
}
```

## Status

MVP complete: trending list (genre filter, sort by popularity/title/release date, both
directions), movie detail screen with all requested fields, offline-first Room caching, error/
retry states, light/dark theme with a runtime toggle, and navigation between the two screens.
Tested with JUnit5/MockK/AssertK unit tests, Paparazzi snapshot tests, and instrumented
Compose UI tests. See `README.md` for the full rundown aimed at a reviewer; this file stays as
the build-log-style companion for anyone extending the codebase.
