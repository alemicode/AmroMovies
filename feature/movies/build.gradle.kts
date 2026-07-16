plugins {
    id("amro.android.feature")
    id("amro.room")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.alemicode.amromovies.feature.movies"
}

dependencies {
    // Generic networking infra only (OkHttpClient, Retrofit instance, auth interceptor,
    // safeApiCall). The TMDB-specific API interface and DTOs are owned by this module.
    implementation(project(":core:network"))

    implementation(libs.retrofit.core)
    implementation(libs.kotlinx.serialization.json)
}
