plugins {
    id("amro.android.library")
    id("amro.koin")
    alias(libs.plugins.kover)
}

android {
    namespace = "com.alemicode.amromovies.feature.movies.domain"
}

dependencies {
    implementation(project(":core:common"))

    implementation(libs.kotlinx.datetime)
}
