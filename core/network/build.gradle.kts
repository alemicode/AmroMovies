import java.util.Properties

plugins {
    id("amro.android.library")
    id("amro.koin")
    alias(libs.plugins.kover)
}

val tmdbReadAccessToken: String = Properties().run {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use(::load)
    }
    getProperty("TMDB_READ_ACCESS_TOKEN") ?: error(
        "Missing TMDB_READ_ACCESS_TOKEN in local.properties. Copy local.properties.sample to " +
            "local.properties and fill in a TMDB v4 Read Access Token from " +
            "https://www.themoviedb.org/settings/api"
    )
}

android {
    namespace = "com.alemicode.amromovies.core.network"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField("String", "TMDB_READ_ACCESS_TOKEN", "\"$tmdbReadAccessToken\"")
    }
}

dependencies {
    implementation(project(":core:common"))

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlinx.serialization.converter)
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.kotlinx.serialization.json)
}
