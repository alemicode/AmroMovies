# AmroMovies

A native Android app for discovering this week's trending movies, built as a Clean
Architecture / multi-module MVP: a trending list with genre filtering and sorting, a full movie
detail screen, offline-first caching, and light/dark theming — backed by
[TMDB](https://www.themoviedb.org/).

This README is written as a handover: what the app does, why it's built the way it is, and how
to run it. For a more detailed, build-log-style account of the architecture decisions and
trade-offs, see [`ARCHITECTURE.md`](./ARCHITECTURE.md).

## Features

- **Trending list** — this week's top 100 trending movies (5 paged TMDB requests merged
  client-side), each showing title, poster, genres, and release year.
- **Genre filter** — filters within the fetched 100, doesn't try to backfill more from the API.
- **Sorting** — popularity (default), title, or release date, ascending or descending, with an
  animated sort-direction indicator.
- **Movie detail screen** — title, tagline, poster, genres, overview, vote average/count, budget,
  revenue, status, runtime, release date, and a link to IMDB.
- **Offline-first** — everything renders from a local Room cache; a failed refresh falls back to
  whatever was last cached instead of an empty/broken screen.
- **Error handling** — explicit loading/error/content states with a retry action, at both the list
  and detail screen.
- **Light/dark theme** — follows the system setting by default, with a manual toggle.

## Tech stack

| Concern | Choice | Why |
|---|---|---|
| UI | Jetpack Compose + Material 3 | Required by the assignment; single source of truth for UI state |
| Architecture | Clean Architecture, MVI (State/Action + single `StateFlow`) | Testable, unidirectional data flow, no scattered mutable state |
| DI | Koin | Plain Kotlin, no annotation-processing overhead for DI itself, fast multi-module builds |
| Networking | Retrofit + OkHttp + kotlinx.serialization | No reflection-based JSON (Gson/Moshi); serialization is compile-time checked |
| Persistence | Room | Single source of truth for the offline-first read path; a typed `Flow` from the DB is what the UI actually observes |
| Images | Coil 3 | Compose-first async image loading |
| Navigation | Navigation Compose, type-safe `@Serializable` routes | No string-based route args; each feature owns its own nav graph |
| Async | Kotlin Coroutines + Flow | `stateIn(WhileSubscribed)` for lazily-shared, lifecycle-aware ViewModel state |
| Unit testing | JUnit5 + MockK + Turbine + AssertK | Modern JVM test stack; Turbine for Flow assertions |
| Screenshot testing | Paparazzi | JVM-only Compose screenshot tests, no emulator needed |
| UI testing | Compose UI Test (instrumented) | Verifies real interaction (clicks, navigation callbacks) on-device |
| Build | Gradle version catalog + `build-logic` convention plugins | A new feature module's `build.gradle.kts` is ~10 lines |

Full version numbers are in [`gradle/libs.versions.toml`](./gradle/libs.versions.toml).

## Architecture

Each feature module owns its **entire vertical slice** — data, domain, and presentation — rather
than reaching into a shared `core:domain`/`core:data`. This is deliberate: the assignment brief
calls for the codebase to support independent feature teams shipping features (actor info, user
profiles, streaming, social) on top of this MVP without stepping on each other, and a centralized
domain/data module becomes exactly the kind of shared bottleneck that contradicts that. See
`ARCHITECTURE.md` for the full reasoning and the rest of the decision log (offline-first caching
strategy, why Koin over Hilt, why Retrofit + kotlinx.serialization, etc.).

```
app                composition root: MainActivity, NavHost, Koin startup
design-system       theme, colors, typography, spacing, shapes, reusable Compose components
core:common         Result<D,E>/DataError, DispatcherProvider - infra, no business logic
core:network        OkHttp/Retrofit/kotlinx.serialization setup, TMDB auth interceptor, safeApiCall
core:testing        MockK/Turbine/AssertK/JUnit5 exposed as `api`, shared feature-agnostic test rules
feature:movies      self-contained: TMDB DTOs, Room entities/DAO, repository, use cases,
                    ViewModels, Compose screens, nav graph - everything movies-specific
```

`feature:*` depends on `core:*` and `design-system`, never the reverse. `core:*` modules never
depend on each other's business logic or on `feature:*`. Two features never depend on each other
directly — cross-feature navigation is always a lambda callback assembled in `:app`.

Within `feature:movies`, presentation follows MVI: a single `StateFlow<State>` per screen, a
sealed `Action` interface for user intent, and a `ViewModel` that's the only thing allowed to
mutate state (`.update { it.copy(...) }`). Navigation-triggering actions are intercepted directly
in the screen's Root composable and forwarded to a plain callback — there's no `Channel`/event
side-channel for that.

## Testing

53 tests across unit, screenshot, and instrumented layers:

- **Unit tests** (JUnit5 + MockK + Turbine + AssertK) — mappers, use cases (`SortMovies`,
  `FilterMoviesByGenre`, `RefreshTrendingMoviesUseCase`), the repository's offline-first fallback
  behavior, `safeApiCall`'s exception-to-`DataError` mapping, and both ViewModels — including
  exercising the lazily-shared `stateIn(WhileSubscribed)` state correctly (a background collector
  activates it, matching [Android's documented testing guidance](https://developer.android.com/kotlin/flow/test)).
- **Screenshot tests** (Paparazzi) — the movie detail screen's content (light + dark), loading,
  and error states, run on the JVM with no emulator.
- **Instrumented UI tests** (Compose UI Test) — real click/navigation-callback behavior on a
  device/emulator.

```
./gradlew test                                    # unit tests
./gradlew :feature:movies:verifyPaparazziDebug     # screenshot tests
./gradlew connectedDebugAndroidTest                # instrumented UI tests (needs a device/emulator)
```

## Getting started

1. Get a free TMDB account and a **v4 Read Access Token** at
   [themoviedb.org/settings/api](https://www.themoviedb.org/settings/api) (not the v3 API key).
2. Copy `local.properties.sample` to `local.properties` and fill in `TMDB_READ_ACCESS_TOKEN`.
   (`local.properties` is gitignored — the build fails fast with a clear message if the key is
   missing.)
3. Open the project in Android Studio (or `./gradlew :app:assembleDebug` from the CLI) and run
   the `app` configuration.

Minimum SDK 24, compile/target SDK 37.

## Known limitations / next steps

- Genre/sort selection isn't restored across process death (the movie list itself is, via the
  Room cache) — a `SavedStateHandle` would close that gap.
- Error messages are a plain boolean flag rather than a localized, resource-backed message type —
  fine for an MVP, but a real `UiText`-style abstraction would be the next step for i18n.
- No pagination beyond the fixed 100 trending movies, per the assignment brief.
- Single data source (TMDB) — the architecture (repository interface, per-feature data
  ownership) is intentionally shaped to make adding a second source additive rather than a
  rewrite, but no second source is wired up.
