plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.kover)
}

dependencies {
    kover(project(":app"))
    kover(project(":design-system"))
    kover(project(":core:common"))
    kover(project(":core:network"))
    kover(project(":feature:movies:data"))
    kover(project(":feature:movies:domain"))
    kover(project(":feature:movies:presentation"))
}

kover {
    reports {
        filters {
            excludes {
                classes(
                    "*.BuildConfig",
                    "*_Impl",
                    "*_Impl\$*",
                    "*ComposableSingletons*",
                    "*.di.*",
                    "com.alemicode.amromovies.MainActivity",
                    "com.alemicode.amromovies.AmroMoviesApplication",
                )
                annotatedBy(
                    "androidx.compose.ui.tooling.preview.Preview",
                    "com.alemicode.amromovies.designsystem.theme.ThemePreviews",
                )
            }
        }
    }
}