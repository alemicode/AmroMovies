plugins {
    id("amro.android.library")
    id("amro.room")
    id("amro.koin")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kover)
}

android {
    namespace = "com.alemicode.amromovies.feature.movies.data"
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:network"))
    implementation(project(":feature:movies:domain"))

    implementation(libs.retrofit.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)
}
