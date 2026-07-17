plugins {
    id("amro.android.feature")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.paparazzi)
    alias(libs.plugins.kover)
}

android {
    namespace = "com.alemicode.amromovies.feature.movies.presentation"
}

dependencies {
    implementation(project(":feature:movies:domain"))

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)
}
