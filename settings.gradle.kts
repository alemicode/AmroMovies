pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "AmroMovies"

include(":app")

// design-system: theme, colors, typography, spacing, shapes, reusable components.
include(":design-system")

// core: shared, feature-agnostic infrastructure only - no business logic. Domain and data live
// inside each feature module (see feature:movies), not centralized here, so feature teams stay
// independent of one another.
include(":core:common")
include(":core:network")
include(":core:testing")

// feature: each module owns its full vertical slice (data, domain, presentation).
include(":feature:movies")
