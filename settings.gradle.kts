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

// core: shared, feature-agnostic building blocks.
include(":core:model")
include(":core:common")
include(":core:network")
include(":core:database")
include(":core:domain")
include(":core:data")
include(":core:testing")

// feature: presentation-only modules, one per product feature.
include(":feature:movies")
